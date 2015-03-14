/*
 * Copyright (c) 2009 University of Durham, England
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'SynergyNet' nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package synergynetframework.appsystem.services.net.tablecomms.server;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import synergynetframework.appsystem.server.ServerMonitor;
import synergynetframework.appsystem.services.ServiceManager;
import synergynetframework.appsystem.services.SynergyNetService;
import synergynetframework.appsystem.services.exceptions.CouldNotStartServiceException;
import synergynetframework.appsystem.services.net.landiscovery.ServiceAnnounceSystem;
import synergynetframework.appsystem.services.net.landiscovery.ServiceDescriptor;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.netservicediscovery.NetworkServiceDiscoveryService;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.SynchroniseData;
import synergynetframework.appsystem.services.net.objectmessaging.Network;
import synergynetframework.appsystem.services.net.objectmessaging.Server;
import synergynetframework.appsystem.services.net.objectmessaging.connections.ConnectionHandler;
import synergynetframework.appsystem.services.net.objectmessaging.connections.MessageHandler;
import synergynetframework.appsystem.services.net.tablecomms.common.ObjectQueueEntry;
import synergynetframework.appsystem.services.net.tablecomms.messages.TableMessage;
import synergynetframework.appsystem.services.net.tablecomms.messages.application.ApplicationMessage;
import synergynetframework.appsystem.services.net.tablecomms.messages.application.BroadcastApplicationMessage;
import synergynetframework.appsystem.services.net.tablecomms.messages.application.UnicastApplicationMessage;
import synergynetframework.appsystem.services.net.tablecomms.messages.control.FromClientTableControlMessage;
import synergynetframework.appsystem.services.net.tablecomms.messages.control.FromServerTableControlMessage;
import synergynetframework.appsystem.services.net.tablecomms.messages.control.TableControlMessage;
import synergynetframework.appsystem.services.net.tablecomms.messages.control.fromclient.ApplicationCommsRequest;
import synergynetframework.appsystem.services.net.tablecomms.messages.control.fromclient.TableJoinRequest;
import synergynetframework.appsystem.services.net.tablecomms.messages.control.fromclient.TableStatusRequest;
import synergynetframework.appsystem.services.net.tablecomms.messages.control.fromserver.TableStatusResponse;
import synergynetframework.appsystem.services.net.tablecomms.server.processors.TableStatusRequestProcessor;
import synergynetframework.appsystem.table.appregistry.NetworkRegistry;

public class TableCommsServerService extends SynergyNetService implements MessageHandler {
	public static final int TCP_PORT = 1874;
	public static final int UDP_PORT = 1876;
	public static final String SERVICE_TYPE = "SynergyNet";
	public static final String SERVICE_NAME = "tablecomms";

	private static final Logger log = Logger.getLogger(TableCommsServerService.class.getName());
	private Server server;
	protected boolean hasStarted = false;

	protected Map<String,ServerMessageProcessor> messageProcessors = new HashMap<String,ServerMessageProcessor>();
	protected Map<TableIdentity,ConnectionHandler> receivers = new HashMap<TableIdentity,ConnectionHandler>();

	// which TCPConnectionHandlers are registered for a particular application?
	protected Map<String,List<ConnectionHandler>> appReceivers = new HashMap<String,List<ConnectionHandler>>();
	protected List<ObjectQueueEntry> objectQueue = new ArrayList<ObjectQueueEntry>();	
	protected List<ServerMonitor> monitors = new ArrayList<ServerMonitor>();

	public TableCommsServerService() {
		
		Network.register(TableMessage.class);
		// Register application messages
		Network.register(ApplicationMessage.class);
		Network.register(BroadcastApplicationMessage.class);
		Network.register(UnicastApplicationMessage.class);
		
		// Register control messages
		Network.register(TableControlMessage.class);
		Network.register(FromServerTableControlMessage.class);
		Network.register(FromClientTableControlMessage.class);
		
		// Register "control->fromclient" messages
		Network.register(ApplicationCommsRequest.class);
		Network.register(TableJoinRequest.class);
		Network.register(TableStatusRequest.class);
		
		// Register "control-> fromserver" messages
		Network.register(TableStatusResponse.class);
		
		Network.register(SynchroniseData.class);
		// Register application messages
		Field[] fields = NetworkRegistry.class.getFields();
		for(Field f : fields) {
			try {
				Network.register((Class<?>)f.get(null));
			} catch (IllegalArgumentException e) {
				log.warning(e.toString());
			} catch (IllegalAccessException e) {
				log.warning(e.toString());
			}
		}
	}

	public void registerServerMonitor(ServerMonitor m) {
		if(!monitors.contains(m)) monitors.add(m);
	}

	public Map<TableIdentity, ConnectionHandler> getReceivers() {
		return receivers;
	}

	public List<ConnectionHandler> getHandlersForApplication(String name) {
		List<ConnectionHandler> list = appReceivers.get(name);
		if(list == null) { 
			list = new ArrayList<ConnectionHandler>();
			appReceivers.put(name, list);
		}
		return list;
	}

	public TableIdentity getTableIdentityForHandler(ConnectionHandler handler) {
		for(TableIdentity id : receivers.keySet()) {
			ConnectionHandler h = receivers.get(id);
			if(h == handler) return id;
		}
		return null;
	}

	public void registerHandlerForApplication(String name, ConnectionHandler handler) {
		List<ConnectionHandler> list = getHandlersForApplication(name);
		if(!list.contains(handler)) list.add(handler);
	}

	public void unregisterHandlerForApplication(String name, ConnectionHandler handler) {
		List<ConnectionHandler> list = getHandlersForApplication(name);
		list.remove(handler);
	}

	public void removeApplicationAssociationForHandler(ConnectionHandler handler) {
		for(List<ConnectionHandler> list : appReceivers.values()) {
			list.remove(handler);
		}
	}

	public List<String> getApplicationsRegisteredForHandler(ConnectionHandler handler) {
		List<String> apps = new ArrayList<String>();
		for(String s : appReceivers.keySet()) {
			List<ConnectionHandler> list = appReceivers.get(s);
			if(list.contains(handler)) apps.add(s);
		}
		return apps;
	}

	public List<String> getApplicationsRegisteredForTable(TableIdentity id) {
		ConnectionHandler handler = receivers.get(id);
		return getApplicationsRegisteredForHandler(handler);
	}


	@Override
	public boolean hasStarted() {
		return hasStarted;
	}

	@Override
	public void shutdown() {		
	}

	@Override
	public void start() throws CouldNotStartServiceException {
		synchronized(this) {
			hasStarted = true;
		}

		log.info("Starting Server on ports: TCP (" + TCP_PORT + ") , UDP ("+ UDP_PORT + ")");

		server = new Server();
		new Thread(server).start();
		try{
			server.bind(TCP_PORT, UDP_PORT);
			server.addMessageHandler(this);
			advertiseService();
		} catch (IOException e) {
			log.warning("Errors starting " + TableCommsServerService.class.getName());
			log.warning(e.toString());
		}
	}

	private void advertiseService() throws CouldNotStartServiceException {
		NetworkServiceDiscoveryService nsds = (NetworkServiceDiscoveryService) ServiceManager.getInstance().get(NetworkServiceDiscoveryService.class);				
		ServiceAnnounceSystem sa = nsds.getServiceAnnouncer();
		ServiceDescriptor s = new ServiceDescriptor();
		s.setServiceType(SERVICE_TYPE);
		s.setServiceName(SERVICE_NAME);
		try {
			s.setServiceAddress(InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			log.warning(e.toString());
		}
		s.setServicePort(TCP_PORT);
		s.setUserData(TableIdentity.getTableIdentity().toString());
		sa.registerService(s);
	}


	@Override
	public void stop() {
		server.stop();
	}

	public void messageReceived(Object obj, ConnectionHandler handler) {
		log.info("Received object " + obj);
		//		ObjectQueueEntry entry = new ObjectQueueEntry(obj, handler);
		//			objectQueue.add(entry);
		//	}
		//
		//	public void update() {
		//		ObjectQueueEntry objQE = null;
		//			if(objectQueue.size() > 0) {
		//				objQE = objectQueue.remove(0);
		//			}
		//
		//
		//		if(objQE == null) return;
		//		Object obj = objQE.getObj();
		//		TCPConnectionHandler handler = objQE.getHandler();

		if(!(obj instanceof TableMessage)) {
			log.warning("CLIENT SENDING A NON-TABLEMESSAGE OBJECT!!!");
			return;
		}

		if(obj instanceof FromClientTableControlMessage) {
			log.info("Processing FromClientTableControlMessage: " + obj);
			try {
				getProcessor((TableMessage) obj).handle(this, handler, (TableMessage)obj);
			} catch (IOException e) {
				log.warning("Error processing message " + obj);
				log.warning(e.toString());
			}
		}else if(obj instanceof UnicastApplicationMessage) {
			log.info("Processing UnicastApplicationMessage " + obj);
			UnicastApplicationMessage msg = (UnicastApplicationMessage) obj;
			try {
				if(receivers.containsKey(msg.getRecipient())) receivers.get(msg.getRecipient()).sendMessage(msg);
			} catch (IOException e) {
				log.warning(e.toString());
			}
		}else if(obj instanceof BroadcastApplicationMessage) {
			log.info("Processing BroadcastApplicationMessage " + obj);
			BroadcastApplicationMessage msg = (BroadcastApplicationMessage) obj;
			System.out.println("msg class name: "+msg.getTargetClassName());
			List<ConnectionHandler> recips = getHandlersForApplication(msg.getTargetClassName());
			log.info("Broadcasting to " + recips);
			for(ConnectionHandler h : recips) {
				try {
					h.sendMessage(msg);
				} catch (IOException e) {
					log.warning(e.toString());
				}
			}
		}

		for(ServerMonitor mon : monitors) {
			mon.serverReceivedMessage(obj);
		}
	}


	public ServerMessageProcessor getProcessor(TableMessage msg) {
		String classname = "synergynetframework.appsystem.services.net.tablecomms.server.processors." + getClassName(msg.getClass()) + "Processor";
		return getProcessor(classname);
	}

	public ServerMessageProcessor getProcessor(String classname) {		
		ServerMessageProcessor p = messageProcessors.get(classname);
		if(p == null) {
			try {
				p = (ServerMessageProcessor) Class.forName(classname).newInstance();
				messageProcessors.put(classname, p);
			} catch (InstantiationException e) {
				log.warning(e.toString());
			} catch (IllegalAccessException e) {
				log.warning(e.toString());
			} catch (ClassNotFoundException e) {
				log.warning(e.toString());
			}	
		}
		return p;
	}

	public void broadcast(TableMessage obj) throws IOException {
		log.info("Broadcasting " + obj);
		server.sendMessageToAll(obj);
	}

	public void handlerDisconnected(ConnectionHandler connectionHandler) {
		log.info("Handler disconnected.");
		TableIdentity id = getTableIdentityForHandler(connectionHandler);
		removeApplicationAssociationForHandler(connectionHandler);
		receivers.remove(id);
		TableStatusRequestProcessor sp = (TableStatusRequestProcessor) getProcessor(TableStatusRequestProcessor.class.getName());		
		try {
			sp.sendTableStatusResponse(this, getReceivers().values());
		} catch (IOException e) {
			log.warning(e.toString());
		}

		log.info("Informing ServerMonitors");
		for(ServerMonitor m : monitors) {
			m.tableDisconnected(id);
		}
	}

	public static String getClassName(Class<?> c) {
		String fqClassName = c.getName();
		int indxDot;
		indxDot = fqClassName.lastIndexOf ('.') + 1;
		if(indxDot > 0) {
			fqClassName = fqClassName.substring (indxDot);
		}
		return fqClassName;
	}

	public List<ServerMonitor> getMonitors() {
		return monitors;
	}

	@Override
	public void update() {}

	@Override
	public void handlerConnected(ConnectionHandler connectionHandler) {
		 

	}
}
