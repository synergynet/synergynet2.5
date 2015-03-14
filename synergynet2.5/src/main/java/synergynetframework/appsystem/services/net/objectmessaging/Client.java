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
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import synergynetframework.appsystem.services.net.objectmessaging.connections.ConnectionHandler;
import synergynetframework.appsystem.services.net.objectmessaging.connections.MessageHandler;
import synergynetframework.appsystem.services.net.objectmessaging.connections.UDPConnectionHandler;
import synergynetframework.appsystem.services.net.objectmessaging.messages.frameworkmessages.KeepAlive;
import synergynetframework.appsystem.services.net.objectmessaging.messages.frameworkmessages.RegisterTCP;
import synergynetframework.appsystem.services.net.objectmessaging.messages.frameworkmessages.RegisterUDP;
import synergynetframework.appsystem.services.net.objectmessaging.utility.serializers.SerializationException;


public class Client extends ConnectionHandler implements EndPoint {
	
	public static final Logger log = Logger.getLogger(Client.class.getName());
	
	private Selector selector;
	private boolean udpRegistered;
	private Object udpRegistrationLock = new Object();
	private volatile boolean shutdown;
	private Object updateLock = new Object();
	private Thread updateThread;

	public Client () {
		this(1024 * 1024);
	}

	public Client (int bufferSize) {
		super(bufferSize);
		endPoint = this;
		try {
			selector = Selector.open();
		} catch (IOException ex) {
			throw new RuntimeException("Error opening selector.", ex);
		}
	}

	public void connect (int timeout, String host, int tcpPort) throws IOException {
		connect(timeout, InetAddress.getByName(host), tcpPort, -1);
	}

	public void connect (int timeout, String host, int tcpPort, int udpPort) throws IOException {
		connect(timeout, InetAddress.getByName(host), tcpPort, udpPort);
	}

	public void connect (int timeout, InetAddress host, int tcpPort) throws IOException {
		connect(timeout, host, tcpPort, -1);
	}

	public void connect (int timeout, InetAddress host, int tcpPort, int udpPort) throws IOException {
		if (host == null) throw new IllegalArgumentException("host cannot be null.");
		close();
		try {
			if (udpPort != -1) udp = new UDPConnectionHandler(tcp.getReadBuffer().capacity());

			long endTime;
			synchronized (updateLock) {
				selector.wakeup();
				endTime = System.currentTimeMillis() + timeout;
				tcp.connect(selector, new InetSocketAddress(host, tcpPort), 5000);
			}

			while (System.currentTimeMillis() < endTime && id == -1) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException ignored) {
				}
			}
			if (id == -1) throw new SocketTimeoutException("Connected, but timed out during TCP registration.");

			if (udpPort != -1) {
				InetSocketAddress udpAddress = new InetSocketAddress(host, udpPort);
				synchronized (updateLock) {
					selector.wakeup();
					udp.connect(selector, udpAddress);
				}

				while (System.currentTimeMillis() < endTime && !udpRegistered) {
					synchronized (udpRegistrationLock) {  
						while (System.currentTimeMillis() < endTime && !udpRegistered) { 
							RegisterUDP registerUDP = new RegisterUDP();
							registerUDP.connectionID = id;
						try {
							udp.send(this, registerUDP, udpAddress);
						} catch (SerializationException ex) {
							IOException ioEx = new IOException();
							ioEx.initCause(ex);
							throw ioEx; 
						}
						try {  
							udpRegistrationLock.wait(200);
						}catch (InterruptedException ignored) {	}  
							}  
						if (!udpRegistered) throw new SocketTimeoutException("Connected, but timed out during UDP registration.");  
						} 
						}
						}
			
		} catch (IOException ex) {
			close();
			throw ex;
		}
	}

	public void update (int timeout) throws IOException, SerializationException {
		updateThread = Thread.currentThread();
		synchronized (updateLock) { 
		}
		if (timeout > 0) {
			selector.select(timeout);
		} else {
			selector.selectNow();
		}
		if(shutdown) return;
		Set<SelectionKey> keys = selector.selectedKeys();
		for (Iterator<SelectionKey> iter = keys.iterator(); iter.hasNext();) {
			SelectionKey selectionKey = iter.next();
			iter.remove();
			int ops = selectionKey.readyOps();
			if ((ops & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
				if (selectionKey.attachment() == tcp) {
					while (true) {
						Object object = tcp.readObject();
						if (object == null) break;
						if (id == -1 || (udp != null && !udpRegistered)) {
							if (object instanceof RegisterTCP) id = ((RegisterTCP)object).connectionID;
							if (object instanceof RegisterUDP) {
								synchronized (udpRegistrationLock) {
								udpRegistered = true;	
								udpRegistrationLock.notifyAll();
								}
								log.info("Port " + udp.datagramChannel.socket().getLocalPort() + "/UDP connected to: "	+ udp.connectedAddress);
							}
							if (id != -1 && (udp == null || udpRegistered)) notifyConnected();
							continue;
						}
						log.info(this + " received TCP: " + object);
						notifyReceived(object);
					}
				} else {
					if (udp.readFromAddress() == null) continue;
					Object object = udp.readObject(this);
					if (object == null) continue;
					log.info(this + " received UDP: " + object);
					notifyReceived(object);
				}
			}
			if ((ops & SelectionKey.OP_WRITE) == SelectionKey.OP_WRITE) tcp.writeOperation();
		}
		if (id != -1 && tcp.needsKeepAlive()) sendTCP(new KeepAlive());
		if (udp != null && udpRegistered && udp.needsKeepAlive()) sendUDP(new KeepAlive());
	}

	public void run () {
		log.info("Client thread started.");
		shutdown = false;
		while (!shutdown) {
			try {
				update(500);
			} catch (IOException ex) {
				log.severe("Unable to undate connection : "+ ex.getMessage());
				close();
			} catch (SerializationException ex) {
				log.severe("Unable to undate connection : "+ ex.getMessage());
				close();
			}
		}
		log.info("Client thread stopped");
		try {  
			selector.selectNow();  
			} catch (IOException ignored) {
				
			} 
	}

	public void stop () {
		close();
		log.info("Client thread stopping.");
		shutdown = true;
		selector.wakeup();
	}

	public void close () {
		super.close();
		udpRegistered = false;
	}

	public void addMessageHandler (MessageHandler handler) {
		super.addMessageHandler(handler);
	}

	public void removeMessageHandler (MessageHandler listener) {
		super.removeMessageHandler(listener);
	}

	public boolean isConnected () {
		return id != -1;
	}

	public Thread getUpdateThread () {
		return updateThread;
	}
}
