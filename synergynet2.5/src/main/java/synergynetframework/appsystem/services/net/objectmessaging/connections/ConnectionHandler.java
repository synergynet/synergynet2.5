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

package synergynetframework.appsystem.services.net.objectmessaging.connections;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import synergynetframework.appsystem.services.net.objectmessaging.EndPoint;
import synergynetframework.appsystem.services.net.objectmessaging.messages.Message;
import synergynetframework.appsystem.services.net.objectmessaging.messages.UDPMessage;
import synergynetframework.appsystem.services.net.objectmessaging.utility.serializers.SerializationException;
import synergynetframework.config.position.PositionConfigPrefsItem;
import core.ConfigurationSystem;

/**
 * The Class ConnectionHandler.
 */
public class ConnectionHandler {

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(ConnectionHandler.class
			.getName());
	
	/** The end point. */
	protected EndPoint endPoint;

	/** The handlers. */
	protected ArrayList<MessageHandler> handlers = new ArrayList<MessageHandler>();

	/** The id. */
	protected short id = -1;

	/** The name. */
	protected String name;

	/** The tcp. */
	protected TCPConnectionHandler tcp;

	/** The udp. */
	protected UDPConnectionHandler udp;

	/** The udp remote address. */
	public InetSocketAddress udpRemoteAddress;
	
	/**
	 * Instantiates a new connection handler.
	 *
	 * @param bufferSize
	 *            the buffer size
	 */
	public ConnectionHandler(int bufferSize) {
		tcp = new TCPConnectionHandler(this, bufferSize);
	}
	
	/**
	 * Adds the message handler.
	 *
	 * @param handler
	 *            the handler
	 */
	public void addMessageHandler(MessageHandler handler) {
		if (handler == null) {
			return;
		}
		for (MessageHandler h : handlers) {
			if (h == handler) {
				return;
			}
		}
		handlers.add(handler);
	}
	
	/**
	 * Close.
	 */
	public void close() {
		tcp.close();
		if ((udp != null) && (udp.connectedAddress != null)) {
			udp.close();
		}
		if (id != -1) {
			notifyDisconnected();
		}
		id = -1;
	}
	
	/**
	 * Gets the end point.
	 *
	 * @return the end point
	 */
	public EndPoint getEndPoint() {
		return endPoint;
	}
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public short getID() {
		return id;
	}
	
	/**
	 * Gets the remote address tcp.
	 *
	 * @return the remote address tcp
	 */
	public InetSocketAddress getRemoteAddressTCP() {
		SocketChannel socketChannel = tcp.socketChannel;
		if (socketChannel != null) {
			Socket socket = tcp.socketChannel.socket();
			if (socket != null) {
				return (InetSocketAddress) socket.getRemoteSocketAddress();
			}
		}
		return null;
	}
	
	/**
	 * Gets the remote address udp.
	 *
	 * @return the remote address udp
	 */
	public InetSocketAddress getRemoteAddressUDP() {
		InetSocketAddress connectedAddress = udp.connectedAddress;
		if (connectedAddress != null) {
			return connectedAddress;
		}
		return udpRemoteAddress;
	}
	
	/**
	 * Gets the TCP connection.
	 *
	 * @return the TCP connection
	 */
	public TCPConnectionHandler getTCPConnection() {
		return tcp;
	}
	
	/**
	 * Gets the UDP connection.
	 *
	 * @return the UDP connection
	 */
	public UDPConnectionHandler getUDPConnection() {
		return udp;
	}
	
	/**
	 * Notify connected.
	 */
	public void notifyConnected() {
		SocketChannel socketChannel = tcp.socketChannel;
		if (socketChannel != null) {
			Socket socket = tcp.socketChannel.socket();
			if (socket != null) {
				InetSocketAddress remoteSocketAddress = (InetSocketAddress) socket
						.getRemoteSocketAddress();
				if (remoteSocketAddress != null) {
					Preferences prefs = ConfigurationSystem
							.getPreferences(PositionConfigPrefsItem.class);
					prefs.putInt(PositionConfigPrefsItem.CURRENT_CONNECTIONS,
							id);
					log.info(this + " connected: "
							+ remoteSocketAddress.getAddress());
				}
			}
		}
		for (MessageHandler h : handlers) {
			h.handlerConnected(this);
		}
	}
	
	/**
	 * Notify disconnected.
	 */
	private void notifyDisconnected() {
		for (MessageHandler h : handlers) {
			h.handlerDisconnected(this);
		}
	}
	
	/**
	 * Notify received.
	 *
	 * @param object
	 *            the object
	 */
	public void notifyReceived(Object object) {
		for (MessageHandler h : handlers) {
			h.messageReceived(object, this);
		}
	}
	
	/**
	 * Removes the message handler.
	 *
	 * @param handler
	 *            the handler
	 */
	public void removeMessageHandler(MessageHandler handler) {
		if (handler == null) {
			return;
		}
		handlers.remove(handler);
	}
	
	/**
	 * Send message.
	 *
	 * @param message
	 *            the message
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void sendMessage(Message message) throws IOException {
		if ((message == null) || (id == -1)) {
			return;
		}
		if (message instanceof UDPMessage) {
			sendUDP(message);
		} else {
			sendTCP(message);
		}
	}
	
	/**
	 * Send tcp.
	 *
	 * @param object
	 *            the object
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void sendTCP(Object object) throws IOException {
		if ((object == null) || (id == -1)) {
			return;
		}
		try {
			tcp.send(object);
		} catch (SerializationException ex) {
			ex.printStackTrace();
			close();
		}
	}
	
	/**
	 * Send udp.
	 *
	 * @param object
	 *            the object
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void sendUDP(Object object) throws IOException {
		if ((object == null) || (id == -1)) {
			return;
		}
		try {
			SocketAddress address = udpRemoteAddress;
			if ((address == null) && (udp != null)) {
				address = udp.connectedAddress;
			}
			if (address == null) {
				return;
			}
			udp.send(this, object, address);
		} catch (SerializationException ex) {
			ex.printStackTrace();
			close();
		}
	}
	
	/**
	 * Sets the end point.
	 *
	 * @param endPoint
	 *            the new end point
	 */
	public void setEndPoint(EndPoint endPoint) {
		this.endPoint = endPoint;
	}
	
	/**
	 * Sets the id.
	 *
	 * @param id
	 *            the new id
	 */
	public void setID(short id) {
		this.id = id;
	}
	
	/**
	 * Sets the name.
	 *
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Sets the TCP alive time.
	 *
	 * @param keepAliveMillis
	 *            the new TCP alive time
	 */
	public void setTCPAliveTime(int keepAliveMillis) {
		tcp.aliveTime = keepAliveMillis;
	}
	
	/**
	 * Sets the TCP connection.
	 *
	 * @param tcp
	 *            the new TCP connection
	 */
	public void setTCPConnection(TCPConnectionHandler tcp) {
		this.tcp = tcp;
	}
	
	/**
	 * Sets the UDP connection.
	 *
	 * @param udp
	 *            the new UDP connection
	 */
	public void setUDPConnection(UDPConnectionHandler udp) {
		this.udp = udp;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (name != null) {
			return name;
		}
		return "Connection " + id;
	}
}
