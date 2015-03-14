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

package synergynetframework.appsystem.services.net.tablecomms.client;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import synergynetframework.appsystem.services.ServiceManager;
import synergynetframework.appsystem.services.SynergyNetService;
import synergynetframework.appsystem.services.exceptions.CouldNotStartServiceException;
import synergynetframework.appsystem.services.exceptions.ServiceNotRunningException;
import synergynetframework.appsystem.services.net.landiscovery.ServiceDescriptor;
import synergynetframework.appsystem.services.net.landiscovery.ServiceDiscoverySystem;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.netservicediscovery.NetworkServiceDiscoveryService;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.SynchroniseData;
import synergynetframework.appsystem.services.net.objectmessaging.Client;
import synergynetframework.appsystem.services.net.objectmessaging.Network;
import synergynetframework.appsystem.services.net.objectmessaging.connections.ConnectionHandler;
import synergynetframework.appsystem.services.net.objectmessaging.connections.MessageHandler;
import synergynetframework.appsystem.services.net.objectmessaging.messages.Message;
import synergynetframework.appsystem.services.net.peer.ServerStatusMonitor;
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
import synergynetframework.appsystem.services.net.tablecomms.server.TableCommsServerService;
import synergynetframework.appsystem.table.appregistry.NetworkRegistry;

public class TableCommsClientService extends SynergyNetService implements MessageHandler {
	private static final Logger log = Logger.getLogger(TableCommsClientService.class.getName());
	protected Map<String,ClientMessageProcessor> messageProcessors = new HashMap<String,ClientMessageProcessor>();
	protected Client client;
	protected List<ObjectQueueEntry> objectQueue = new ArrayList<ObjectQueueEntry>();
	protected boolean joinedMessageSent = false;

	public TableCommsClientService() {
		
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
		client = new Client();
	}

	@Override
	public boolean hasStarted() {
		return false;
	}

	@Override
	public void shutdown() {
	}

	@Override
	public void start() throws CouldNotStartServiceException {
		NetworkServiceDiscoveryService nsds = (NetworkServiceDiscoveryService) ServiceManager.getInstance().get(NetworkServiceDiscoveryService.class);
		ServiceDiscoverySystem serviceDiscovery = nsds.getServiceDiscovery();
		ServerStatusMonitor smon = new ServerStatusMonitor(TableCommsServerService.SERVICE_TYPE, TableCommsServerService.SERVICE_NAME, 3000);
		serviceDiscovery.registerListener(smon);
		serviceDiscovery.registerServiceForListening(TableCommsServerService.SERVICE_TYPE, TableCommsServerService.SERVICE_NAME);

		try {
			smon.connect();
			boolean serverFound = smon.serverFound();
			if(!serverFound) {
				log.severe("Could not find server!");
			}else{
				ServiceDescriptor found = smon.getServerServiceDescriptor();
				new Thread(client).start();
				client.addMessageHandler(this);
				client.connect(5000, found.getServiceAddress(), TableCommsServerService.TCP_PORT, TableCommsServerService.UDP_PORT);
				log.info("Connected to server");
			}
		} catch (InterruptedException e) {
			log.warning(e.toString());
		} catch (UnknownHostException e) {
			log.warning(e.toString());
		} catch (IOException e) {
			log.warning(e.toString());
		}

	}



	protected Map<String,TableCommsApplicationListener> appListeners = new HashMap<String,TableCommsApplicationListener>();
	private List<TableIdentity> currentlyOnline = new ArrayList<TableIdentity>();

	public List<TableIdentity> getCurrentlyOnline() {
		return currentlyOnline;
	}

	/**
	 * Register a TableCommsApplicationListener object for listening. Convenience method that calls
	 * register() with the class name of the caller object. This allows an associated class to
	 * register a different TableCommsApplicationListener.
	 * @param caller
	 * @param applistener
	 * @throws IOException
	 */
	public void register(Object caller, TableCommsApplicationListener applistener) throws IOException {
		register(caller.getClass().getName(), applistener);
	}

	/**
	 * Register a TableCommsApplicationListener object for listening.
	 * @param name
	 * @param applistener
	 * @throws IOException
	 */
	public void register(String name, TableCommsApplicationListener applistener) throws IOException {
		appListeners.put(name, applistener);
		sendMessage(new ApplicationCommsRequest(name));
	}

	@Override
	public void stop() throws ServiceNotRunningException {
		if(client != null) client.stop();
		ServiceManager.getInstance().unregister(this.getClass().getName());
	}

	public void sendMessage(Object obj) throws IOException {
		if(client != null) {			
			if(!joinedMessageSent) {
				client.sendMessage(new TableJoinRequest());
				joinedMessageSent = true;			
			}			
			log.info("Sending " + obj);
			client.sendMessage((Message)obj);
		}else{ 
			log.warning("Cannot send a message when client is not connected!");
		}
	}

	public void messageReceived(Object obj, ConnectionHandler handler) {
		log.info("Received " + obj);
		ObjectQueueEntry entry = new ObjectQueueEntry(obj, handler);
		//		synchronized(objectQueue) {
		objectQueue.add(entry);
		//		}
	}

	public void update() {
		ObjectQueueEntry objQE = null;
		//		synchronized(objectQueue) {
		if(objectQueue.size() > 0) {
			objQE = objectQueue.remove(0);
		}
		//		}

		if(objQE == null) return;
		Object obj = objQE.getObj();
		ConnectionHandler handler = objQE.getHandler();

		log.info("CLIENT RECEIVED: " + obj);
		if(obj instanceof ApplicationMessage) {
			TableCommsApplicationListener l = appListeners.get(((ApplicationMessage) obj).getTargetClassName());
			if (l!=null)
				l.messageReceived(obj);
		}else if(obj instanceof FromServerTableControlMessage) {			
			getProcessor((TableMessage) obj).handle(this, handler, (TableMessage)obj);
		}
	}

	public boolean isConnected() {
		return client != null && client.isConnected();
	}

	public void handlerDisconnected(ConnectionHandler connectionHandler) {
		client = null;
		for(TableCommsApplicationListener l: appListeners.values()) l.tableDisconnected();
	}

	public ClientMessageProcessor getProcessor(TableMessage msg) {
		String classname = "synergynetframework.appsystem.services.net.tablecomms.client.processors." + getClassName(msg.getClass()) + "Processor";
		return getProcessor(classname);
	}


	public ClientMessageProcessor getProcessor(String classname) {		
		ClientMessageProcessor p = messageProcessors.get(classname);
		if(p == null) {
			try {
				p = (ClientMessageProcessor) Class.forName(classname).newInstance();
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

	public static String getClassName(Class<?> c) {
		String fqClassName = c.getName();
		int indxDot;
		indxDot = fqClassName.lastIndexOf ('.') + 1;
		if(indxDot > 0) {
			fqClassName = fqClassName.substring (indxDot);
		}
		return fqClassName;
	}


	public static void main(String[] args) throws  CouldNotStartServiceException {
		ServiceManager.getInstance().get(NetworkServiceDiscoveryService.class);
		TableCommsClientService client = new TableCommsClientService();
		client.start();
	}

	@Override
	public void handlerConnected(ConnectionHandler connectionHandler) {
		 

	}

	public boolean isClientConnected() {
		return (client != null && client.isConnected());
	}

	public Client getClient(){
		return client;
	}

}
