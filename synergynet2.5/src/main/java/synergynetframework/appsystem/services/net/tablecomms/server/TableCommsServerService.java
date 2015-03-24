/*
 * Copyright (c) 2009 University of Durham, England All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of 'SynergyNet' nor the names of
 * its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission. THIS SOFTWARE IS PROVIDED
 * BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
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

/**
 * The Class TableCommsServerService.
 */
public class TableCommsServerService extends SynergyNetService implements
		MessageHandler {

	/** The Constant log. */
	private static final Logger log = Logger
			.getLogger(TableCommsServerService.class.getName());

	/** The Constant SERVICE_NAME. */
	public static final String SERVICE_NAME = "tablecomms";

	/** The Constant SERVICE_TYPE. */
	public static final String SERVICE_TYPE = "SynergyNet";

	/** The Constant TCP_PORT. */
	public static final int TCP_PORT = 1874;
	
	/** The Constant UDP_PORT. */
	public static final int UDP_PORT = 1876;

	/**
	 * Gets the class name.
	 *
	 * @param c
	 *            the c
	 * @return the class name
	 */
	public static String getClassName(Class<?> c) {
		String fqClassName = c.getName();
		int indxDot;
		indxDot = fqClassName.lastIndexOf('.') + 1;
		if (indxDot > 0) {
			fqClassName = fqClassName.substring(indxDot);
		}
		return fqClassName;
	}

	// which TCPConnectionHandlers are registered for a particular application?
	/** The app receivers. */
	protected Map<String, List<ConnectionHandler>> appReceivers = new HashMap<String, List<ConnectionHandler>>();
	
	/** The has started. */
	protected boolean hasStarted = false;

	/** The message processors. */
	protected Map<String, ServerMessageProcessor> messageProcessors = new HashMap<String, ServerMessageProcessor>();
	
	/** The monitors. */
	protected List<ServerMonitor> monitors = new ArrayList<ServerMonitor>();

	/** The object queue. */
	protected List<ObjectQueueEntry> objectQueue = new ArrayList<ObjectQueueEntry>();
	
	/** The receivers. */
	protected Map<TableIdentity, ConnectionHandler> receivers = new HashMap<TableIdentity, ConnectionHandler>();
	
	/** The server. */
	private Server server;
	
	/**
	 * Instantiates a new table comms server service.
	 */
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
		for (Field f : fields) {
			try {
				Network.register((Class<?>) f.get(null));
			} catch (IllegalArgumentException e) {
				log.warning(e.toString());
			} catch (IllegalAccessException e) {
				log.warning(e.toString());
			}
		}
	}
	
	/**
	 * Advertise service.
	 *
	 * @throws CouldNotStartServiceException
	 *             the could not start service exception
	 */
	private void advertiseService() throws CouldNotStartServiceException {
		NetworkServiceDiscoveryService nsds = (NetworkServiceDiscoveryService) ServiceManager
				.getInstance().get(NetworkServiceDiscoveryService.class);
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
	
	/**
	 * Broadcast.
	 *
	 * @param obj
	 *            the obj
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void broadcast(TableMessage obj) throws IOException {
		log.info("Broadcasting " + obj);
		server.sendMessageToAll(obj);
	}
	
	/**
	 * Gets the applications registered for handler.
	 *
	 * @param handler
	 *            the handler
	 * @return the applications registered for handler
	 */
	public List<String> getApplicationsRegisteredForHandler(
			ConnectionHandler handler) {
		List<String> apps = new ArrayList<String>();
		for (String s : appReceivers.keySet()) {
			List<ConnectionHandler> list = appReceivers.get(s);
			if (list.contains(handler)) {
				apps.add(s);
			}
		}
		return apps;
	}
	
	/**
	 * Gets the applications registered for table.
	 *
	 * @param id
	 *            the id
	 * @return the applications registered for table
	 */
	public List<String> getApplicationsRegisteredForTable(TableIdentity id) {
		ConnectionHandler handler = receivers.get(id);
		return getApplicationsRegisteredForHandler(handler);
	}
	
	/**
	 * Gets the handlers for application.
	 *
	 * @param name
	 *            the name
	 * @return the handlers for application
	 */
	public List<ConnectionHandler> getHandlersForApplication(String name) {
		List<ConnectionHandler> list = appReceivers.get(name);
		if (list == null) {
			list = new ArrayList<ConnectionHandler>();
			appReceivers.put(name, list);
		}
		return list;
	}
	
	/**
	 * Gets the monitors.
	 *
	 * @return the monitors
	 */
	public List<ServerMonitor> getMonitors() {
		return monitors;
	}
	
	/**
	 * Gets the processor.
	 *
	 * @param classname
	 *            the classname
	 * @return the processor
	 */
	public ServerMessageProcessor getProcessor(String classname) {
		ServerMessageProcessor p = messageProcessors.get(classname);
		if (p == null) {
			try {
				p = (ServerMessageProcessor) Class.forName(classname)
						.newInstance();
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
	
	/**
	 * Gets the processor.
	 *
	 * @param msg
	 *            the msg
	 * @return the processor
	 */
	public ServerMessageProcessor getProcessor(TableMessage msg) {
		String classname = "synergynetframework.appsystem.services.net.tablecomms.server.processors."
				+ getClassName(msg.getClass()) + "Processor";
		return getProcessor(classname);
	}
	
	/**
	 * Gets the receivers.
	 *
	 * @return the receivers
	 */
	public Map<TableIdentity, ConnectionHandler> getReceivers() {
		return receivers;
	}
	
	/**
	 * Gets the table identity for handler.
	 *
	 * @param handler
	 *            the handler
	 * @return the table identity for handler
	 */
	public TableIdentity getTableIdentityForHandler(ConnectionHandler handler) {
		for (TableIdentity id : receivers.keySet()) {
			ConnectionHandler h = receivers.get(id);
			if (h == handler) {
				return id;
			}
		}
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.services.net.objectmessaging.connections
	 * .MessageHandler
	 * #handlerConnected(synergynetframework.appsystem.services.net
	 * .objectmessaging.connections.ConnectionHandler)
	 */
	@Override
	public void handlerConnected(ConnectionHandler connectionHandler) {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.services.net.objectmessaging.connections
	 * .MessageHandler
	 * #handlerDisconnected(synergynetframework.appsystem.services
	 * .net.objectmessaging.connections.ConnectionHandler)
	 */
	public void handlerDisconnected(ConnectionHandler connectionHandler) {
		log.info("Handler disconnected.");
		TableIdentity id = getTableIdentityForHandler(connectionHandler);
		removeApplicationAssociationForHandler(connectionHandler);
		receivers.remove(id);
		TableStatusRequestProcessor sp = (TableStatusRequestProcessor) getProcessor(TableStatusRequestProcessor.class
				.getName());
		try {
			sp.sendTableStatusResponse(this, getReceivers().values());
		} catch (IOException e) {
			log.warning(e.toString());
		}
		
		log.info("Informing ServerMonitors");
		for (ServerMonitor m : monitors) {
			m.tableDisconnected(id);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.services.SynergyNetService#hasStarted()
	 */
	@Override
	public boolean hasStarted() {
		return hasStarted;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.services.net.objectmessaging.connections
	 * .MessageHandler#messageReceived(java.lang.Object,
	 * synergynetframework.appsystem
	 * .services.net.objectmessaging.connections.ConnectionHandler)
	 */
	public void messageReceived(Object obj, ConnectionHandler handler) {
		log.info("Received object " + obj);
		// ObjectQueueEntry entry = new ObjectQueueEntry(obj, handler);
		// objectQueue.add(entry);
		// }
		//
		// public void update() {
		// ObjectQueueEntry objQE = null;
		// if(objectQueue.size() > 0) {
		// objQE = objectQueue.remove(0);
		// }
		//
		//
		// if(objQE == null) return;
		// Object obj = objQE.getObj();
		// TCPConnectionHandler handler = objQE.getHandler();
		
		if (!(obj instanceof TableMessage)) {
			log.warning("CLIENT SENDING A NON-TABLEMESSAGE OBJECT!!!");
			return;
		}
		
		if (obj instanceof FromClientTableControlMessage) {
			log.info("Processing FromClientTableControlMessage: " + obj);
			try {
				getProcessor((TableMessage) obj).handle(this, handler,
						(TableMessage) obj);
			} catch (IOException e) {
				log.warning("Error processing message " + obj);
				log.warning(e.toString());
			}
		} else if (obj instanceof UnicastApplicationMessage) {
			log.info("Processing UnicastApplicationMessage " + obj);
			UnicastApplicationMessage msg = (UnicastApplicationMessage) obj;
			try {
				if (receivers.containsKey(msg.getRecipient())) {
					receivers.get(msg.getRecipient()).sendMessage(msg);
				}
			} catch (IOException e) {
				log.warning(e.toString());
			}
		} else if (obj instanceof BroadcastApplicationMessage) {
			log.info("Processing BroadcastApplicationMessage " + obj);
			BroadcastApplicationMessage msg = (BroadcastApplicationMessage) obj;
			System.out.println("msg class name: " + msg.getTargetClassName());
			List<ConnectionHandler> recips = getHandlersForApplication(msg
					.getTargetClassName());
			log.info("Broadcasting to " + recips);
			for (ConnectionHandler h : recips) {
				try {
					h.sendMessage(msg);
				} catch (IOException e) {
					log.warning(e.toString());
				}
			}
		}
		
		for (ServerMonitor mon : monitors) {
			mon.serverReceivedMessage(obj);
		}
	}
	
	/**
	 * Register handler for application.
	 *
	 * @param name
	 *            the name
	 * @param handler
	 *            the handler
	 */
	public void registerHandlerForApplication(String name,
			ConnectionHandler handler) {
		List<ConnectionHandler> list = getHandlersForApplication(name);
		if (!list.contains(handler)) {
			list.add(handler);
		}
	}
	
	/**
	 * Register server monitor.
	 *
	 * @param m
	 *            the m
	 */
	public void registerServerMonitor(ServerMonitor m) {
		if (!monitors.contains(m)) {
			monitors.add(m);
		}
	}
	
	/**
	 * Removes the application association for handler.
	 *
	 * @param handler
	 *            the handler
	 */
	public void removeApplicationAssociationForHandler(ConnectionHandler handler) {
		for (List<ConnectionHandler> list : appReceivers.values()) {
			list.remove(handler);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.services.SynergyNetService#shutdown()
	 */
	@Override
	public void shutdown() {
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.services.SynergyNetService#start()
	 */
	@Override
	public void start() throws CouldNotStartServiceException {
		synchronized (this) {
			hasStarted = true;
		}
		
		log.info("Starting Server on ports: TCP (" + TCP_PORT + ") , UDP ("
				+ UDP_PORT + ")");
		
		server = new Server();
		new Thread(server).start();
		try {
			server.bind(TCP_PORT, UDP_PORT);
			server.addMessageHandler(this);
			advertiseService();
		} catch (IOException e) {
			log.warning("Errors starting "
					+ TableCommsServerService.class.getName());
			log.warning(e.toString());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.services.SynergyNetService#stop()
	 */
	@Override
	public void stop() {
		server.stop();
	}
	
	/**
	 * Unregister handler for application.
	 *
	 * @param name
	 *            the name
	 * @param handler
	 *            the handler
	 */
	public void unregisterHandlerForApplication(String name,
			ConnectionHandler handler) {
		List<ConnectionHandler> list = getHandlersForApplication(name);
		list.remove(handler);
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.services.SynergyNetService#update()
	 */
	@Override
	public void update() {
	}
}
