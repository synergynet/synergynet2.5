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

package synergynetframework.appsystem.services.net.objectmessaging.connections;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import core.ConfigurationSystem;


import synergynetframework.appsystem.services.net.objectmessaging.EndPoint;
import synergynetframework.appsystem.services.net.objectmessaging.connections.TCPConnectionHandler;
import synergynetframework.appsystem.services.net.objectmessaging.connections.UDPConnectionHandler;
import synergynetframework.appsystem.services.net.objectmessaging.messages.Message;
import synergynetframework.appsystem.services.net.objectmessaging.messages.UDPMessage;
import synergynetframework.appsystem.services.net.objectmessaging.utility.serializers.SerializationException;
import synergynetframework.config.position.PositionConfigPrefsItem;


public class ConnectionHandler {
	private static final Logger log = Logger.getLogger(ConnectionHandler.class.getName());

	protected String name;
	protected short id = -1;
	protected EndPoint endPoint;
	protected TCPConnectionHandler tcp;
	protected UDPConnectionHandler udp;
	public InetSocketAddress udpRemoteAddress;
	protected ArrayList<MessageHandler> handlers = new ArrayList<MessageHandler>();


	public ConnectionHandler (int bufferSize) {
		tcp = new TCPConnectionHandler(this, bufferSize);
	}

	public short getID () {
		return id;
	}

	public void setID (short id) {
		this.id = id;
	}

	public void sendMessage(Message message) throws IOException{
		if(message == null|| id == -1) return;
		if(message instanceof UDPMessage) sendUDP(message);
		else sendTCP(message);
	}

	public void sendTCP (Object object) throws IOException {
		if (object == null || id == -1) return;
		try {
			tcp.send(object);
		} catch (SerializationException ex) {
			ex.printStackTrace();
			close();
		}
	}

	public void sendUDP (Object object) throws IOException {
		if (object == null || id == -1) return;
		try {
			SocketAddress address = udpRemoteAddress;
			if (address == null && udp != null)	address = udp.connectedAddress;
			if (address == null) return;
			udp.send(this, object, address);
		} catch (SerializationException ex) {
			ex.printStackTrace();
			close();
		}
	}

	public void close () {
		tcp.close();
		if (udp != null && udp.connectedAddress != null) udp.close();
		if (id != -1) notifyDisconnected();
		id = -1;
	}

	public void setTCPAliveTime (int keepAliveMillis) {
		tcp.aliveTime = keepAliveMillis;
	}

	public void addMessageHandler (MessageHandler handler) {
		if (handler == null) return;
		for(MessageHandler h: handlers)	if (h == handler) return;
		handlers.add(handler);
	}

	public void removeMessageHandler (MessageHandler handler) {
		if (handler == null) return;
		handlers.remove(handler);
	}

	public void notifyConnected () {
			SocketChannel socketChannel = tcp.socketChannel;
			if (socketChannel != null) {
				Socket socket = tcp.socketChannel.socket();
				if (socket != null) {
					InetSocketAddress remoteSocketAddress = (InetSocketAddress)socket.getRemoteSocketAddress();
					if (remoteSocketAddress != null){
						Preferences prefs = ConfigurationSystem.getPreferences(PositionConfigPrefsItem.class);
						prefs.putInt(PositionConfigPrefsItem.CURRENT_CONNECTIONS, id);
						log.info(this + " connected: " + remoteSocketAddress.getAddress());
					}
				}
			}
		for (MessageHandler h: handlers)
			h.handlerConnected(this);
	}

	private void notifyDisconnected () {
		for (MessageHandler h: handlers)
			h.handlerDisconnected(this);
	}

	public void notifyReceived (Object object) {
		for (MessageHandler h: handlers)
			h.messageReceived(object, this);
	}

	public EndPoint getEndPoint () {
		return endPoint;
	}

	public void setEndPoint (EndPoint endPoint) {
		this.endPoint = endPoint;
	}

	public TCPConnectionHandler getTCPConnection(){
		return tcp;
	}

	public void setTCPConnection(TCPConnectionHandler tcp){
		this.tcp = tcp;
	}

	public UDPConnectionHandler getUDPConnection(){
		return udp;
	}

	public void setUDPConnection(UDPConnectionHandler udp){
		this.udp = udp;
	}

	public InetSocketAddress getRemoteAddressTCP () {
		SocketChannel socketChannel = tcp.socketChannel;
		if (socketChannel != null) {
			Socket socket = tcp.socketChannel.socket();
			if (socket != null) {
				return (InetSocketAddress)socket.getRemoteSocketAddress();
			}
		}
		return null;
	}

	public InetSocketAddress getRemoteAddressUDP () {
		InetSocketAddress connectedAddress = udp.connectedAddress;
		if (connectedAddress != null) return connectedAddress;
		return udpRemoteAddress;
	}

	public void setName (String name) {
		this.name = name;
	}

	public String toString () {
		if (name != null) return name;
		return "Connection " + id;
	}
}
