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


package synergynetframework.appsystem.services.net.objectmessaging;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

import synergynetframework.appsystem.services.net.objectmessaging.connections.ConnectionHandler;
import synergynetframework.appsystem.services.net.objectmessaging.connections.MessageHandler;
import synergynetframework.appsystem.services.net.objectmessaging.connections.UDPConnectionHandler;
import synergynetframework.appsystem.services.net.objectmessaging.messages.Message;
import synergynetframework.appsystem.services.net.objectmessaging.messages.frameworkmessages.FrameworkMessage;
import synergynetframework.appsystem.services.net.objectmessaging.messages.frameworkmessages.RegisterTCP;
import synergynetframework.appsystem.services.net.objectmessaging.messages.frameworkmessages.RegisterUDP;
import synergynetframework.appsystem.services.net.objectmessaging.utility.serializers.SerializationException;


public class Server implements EndPoint {
	
	private static final Logger log = Logger.getLogger(Server.class.getName());

	protected int bufferSize;
	protected Selector selector;
	protected ServerSocketChannel serverChannel;
	protected UDPConnectionHandler udp;
	protected Queue<ConnectionHandler> connectionHandlers = new ConcurrentLinkedQueue<ConnectionHandler>();
	protected ArrayList <MessageHandler> handlers = new ArrayList<MessageHandler>();
	protected short nextConnectionID = 1;
	private volatile boolean shutdown = false;
	protected Object updateLock = new Object();
	protected Thread updateThread;

	private MessageHandler dispatchConnectionHandler = new MessageHandler() {
		public void handlerConnected (ConnectionHandler connectionHandler) {
			for(MessageHandler h: handlers)
				h.handlerConnected(connectionHandler);
		}

		public void handlerDisconnected (ConnectionHandler connectionHandler) {
			connectionHandlers.remove(connectionHandler);
			for(MessageHandler h: handlers)
				h.handlerDisconnected(connectionHandler);
		}

		public void messageReceived (Object object, ConnectionHandler connectionHandler) {
			for(MessageHandler h: handlers)
				h.messageReceived(object, connectionHandler);
		}
	};

	public Server () {
		this(1024 * 1024 * 10);
	}

	public Server (int bufferSize) {
		this.bufferSize = bufferSize;
		try {
			selector = Selector.open();
		} catch (IOException ex) {
			throw new RuntimeException("Error opening selector.", ex);
		}
	}

	public void bind (int tcpPort) throws IOException {
		bind(tcpPort, -1);
	}

	public void bind (int tcpPort, int udpPort) throws IOException {
		close();
		synchronized (updateLock) {
			try {
				selector.wakeup();
				serverChannel = selector.provider().openServerSocketChannel();
				serverChannel.socket().bind(new InetSocketAddress(tcpPort));
				serverChannel.configureBlocking(false);
				serverChannel.register(selector, SelectionKey.OP_ACCEPT);
				log.info("Accepting connections on port: " + tcpPort + "/TCP");

				if (udpPort != -1) {
					udp = new UDPConnectionHandler(bufferSize);
					udp.bind(selector, udpPort);
					log.info("Accepting connections on port: " + udpPort + "/UDP");
				}
			} catch (IOException ex) {
				close();
				throw ex;
			}
		}
		log.info("Server started.");
	}

	public void update (int timeout) throws IOException {
		updateThread = Thread.currentThread();
		synchronized (updateLock) { 
		}
		if (timeout > 0) {
			selector.select(timeout);
		} else {
			selector.selectNow();
		}
		Set<SelectionKey> keys = selector.selectedKeys();
		outer: 
		for (Iterator<SelectionKey> iter = keys.iterator(); iter.hasNext();) {
			SelectionKey selectionKey = iter.next();
			iter.remove();
			int ops = selectionKey.readyOps();
			ConnectionHandler keyConnection = (ConnectionHandler)selectionKey.attachment();

			if (keyConnection != null) {
				if ((ops & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
					try {
						while (true) {
							Object object = keyConnection.getTCPConnection().readObject();
							if (object == null) break;
							log.info(keyConnection + " received TCP: " + object);
							keyConnection.notifyReceived(object);
						}
					} catch (IOException ex) {
						log.info(keyConnection + " update: " + ex.getMessage());
						keyConnection.close();
					} catch (SerializationException ex) {
						log.severe("Error reading TCP from connection: " + keyConnection +" : " + ex.getMessage());
						keyConnection.close();
					}
				}
				if ((ops & SelectionKey.OP_WRITE) == SelectionKey.OP_WRITE) {
					try {
						keyConnection.getTCPConnection().writeOperation();
					} catch (IOException ex) {
						log.info("Unable to write TCP to connection: " + keyConnection +  ex.getMessage());
						keyConnection.close();
					}
				}
				continue;
			}

			if ((ops & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
				try {
					SocketChannel socketChannel = serverChannel.accept();
					if (socketChannel != null) acceptOperation(socketChannel);
				} catch (IOException ex) {
					log.warning("Unable to accept new connection." + ex.getMessage());
				}
				continue;
			}

			if (udp == null) continue;
			InetSocketAddress fromAddress;
			try {
				fromAddress = udp.readFromAddress();
			} catch (IOException ex) {
				IOException ioEx = new IOException("Error reading UDP data.");
				ioEx.initCause(ex);
				throw ioEx;
			}
			if (fromAddress == null) continue;

			ConnectionHandler fromConnection = null;
			for (ConnectionHandler connectionHandler : connectionHandlers) {
				if (fromAddress.equals(connectionHandler.udpRemoteAddress)) {
					fromConnection = connectionHandler;
					break;
				}
			}

			Object object;
			try {
				object = udp.readObject(fromConnection);
			} catch (SerializationException ex) {
					if(fromConnection != null) log.warning("Error reading UDP from unregistered address: " + fromConnection + " : " + ex.getMessage());
					continue;
			}

			if (object instanceof FrameworkMessage) {
				if (object instanceof RegisterUDP) {
					short fromConnectionID = ((RegisterUDP)object).connectionID;
					for (ConnectionHandler connectionHandler : connectionHandlers) {
						if (connectionHandler.getID() == fromConnectionID) {
							if (connectionHandler.udpRemoteAddress != null) continue outer;
							connectionHandler.udpRemoteAddress = fromAddress;
							connectionHandler.sendTCP(new RegisterUDP());
							log.info("Port " + udp.datagramChannel.socket().getLocalPort() + "/UDP connected to: " + fromAddress);
							connectionHandler.notifyConnected();
							continue outer;
						}
					}
					continue;
				}
			}

			if (fromConnection != null) {
				fromConnection.notifyReceived(object);
				continue;
			}
		}
	}

	public void run () {
		log.info("Server thread started.");
		shutdown = false;
		while (!shutdown) {
			try {
				update(500);
			} catch (IOException ex) {
				log.severe("Error updating server connections." + ex.getMessage());
				close();
			}
		}
	}

	public void stop () {
		close();
		log.info("Server thread stopping.");
		shutdown = true;
		selector.wakeup();
	}

	private void acceptOperation (SocketChannel socketChannel) {
		ConnectionHandler connectionHandler = newConnection(bufferSize);
		connectionHandler.setEndPoint(this);
		if (udp != null) connectionHandler.setUDPConnection(udp);
		try {
			SelectionKey selectionKey = connectionHandler.getTCPConnection().accept(selector, socketChannel);
			selectionKey.attach(connectionHandler);

			connectionHandlers.add(connectionHandler);
			connectionHandler.addMessageHandler(dispatchConnectionHandler);
			short id = nextConnectionID++;
			connectionHandler.setID(id);

			RegisterTCP registerConnection = new RegisterTCP();
			registerConnection.connectionID = id;
			connectionHandler.sendTCP(registerConnection);

			if (udp == null) connectionHandler.notifyConnected();
		} catch (IOException ex) {
			connectionHandler.close();
			log.info("Unable to accept TCP connection." + ex.getMessage());
		}
	}


	protected ConnectionHandler newConnection (int bufferSize) {
		return new ConnectionHandler(bufferSize);
	}

	public void sendMessage (short connectionID, Message message) throws IOException {
		for(ConnectionHandler connectionHandler: connectionHandlers) {
			if (connectionHandler.getID() == connectionID) {
				connectionHandler.sendMessage(message);
				break;
			}
		}
	}
	
	public void sendMessageToAll (Message message) throws IOException {
		for(ConnectionHandler connectionHandler: connectionHandlers){
			connectionHandler.sendMessage(message);
		}
	}

	public void sendMessageToAllExcept (short connectionID, Message message) throws IOException {
		for(ConnectionHandler connectionHandler: connectionHandlers) {
			if (connectionHandler.getID() != connectionID) 
				connectionHandler.sendMessage(message);
		}
	}
	
	public void sendToAllTCP (Object object) throws IOException {
		for(ConnectionHandler connectionHandler: connectionHandlers){
			connectionHandler.sendTCP(object);
		}
	}

	public void sendToAllExceptTCP (short connectionID, Object object) throws IOException {
		for(ConnectionHandler connectionHandler: connectionHandlers) {
			if (connectionHandler.getID() != connectionID) 
				connectionHandler.sendTCP(object);
		}
	}

	public void sendToTCP (short connectionID, Object object) throws IOException {
		for(ConnectionHandler connectionHandler: connectionHandlers) {
			if (connectionHandler.getID() == connectionID) {
				connectionHandler.sendTCP(object);
				break;
			}
		}
	}

	public void sendToAllUDP (Object object) throws IOException {
		for(ConnectionHandler connectionHandler: connectionHandlers)
			connectionHandler.sendUDP(object);
	}

	public void sendToAllExceptUDP (short connectionID, Object object) throws IOException {
		for(ConnectionHandler connectionHandler: connectionHandlers) {
			if (connectionHandler.getID() != connectionID) connectionHandler.sendUDP(object);
		}
	}

	public void sendToUDP (short connectionID, Object object) throws IOException {
		for(ConnectionHandler connectionHandler: connectionHandlers) {
			if (connectionHandler.getID() == connectionID) {
				connectionHandler.sendUDP(object);
				break;
			}
		}
	}

	public void addMessageHandler (MessageHandler handler) {
		if (handler == null) throw new IllegalArgumentException("listener cannot be null.");
		for(MessageHandler h: handlers)
			if (h == handler) return;
		handlers.add(handler);
	}

	public void removeMessageHandler (MessageHandler handler) {
		if (handler == null) throw new IllegalArgumentException("handler cannot be null.");
		handlers.remove(handler);
	}


	public void close () {
		for (ConnectionHandler connectionHandler: connectionHandlers)
			connectionHandler.close();
		connectionHandlers.clear();
		if (serverChannel != null) {
			try {
				serverChannel.close();
				selector.selectNow();
				selector.wakeup();
				log.info("Server closed.");
			} catch (IOException ex) {
				log.severe("Unable to close server : "+ ex.getMessage());
			}
			serverChannel = null;
		}
		if (udp != null) {
			udp.close();
			udp = null;
		}
	}

	public Thread getUpdateThread () {
		return updateThread;
	}
	
	public boolean isRunning(){
		return !shutdown;
	}
	
	public Queue<ConnectionHandler> getHandlers(){
		return connectionHandlers;
	}
}
