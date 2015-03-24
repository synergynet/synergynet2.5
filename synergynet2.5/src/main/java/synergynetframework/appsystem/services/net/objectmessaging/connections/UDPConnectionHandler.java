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
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.logging.Logger;

import synergynetframework.appsystem.services.net.objectmessaging.Network;
import synergynetframework.appsystem.services.net.objectmessaging.utility.serializers.SerializationException;


/**
 * The Class UDPConnectionHandler.
 */
public class UDPConnectionHandler {
	
	/** The Constant log. */
	private static final Logger log = Logger.getLogger(UDPConnectionHandler.class.getName());

	/** The connected address. */
	public InetSocketAddress connectedAddress;
	
	/** The datagram channel. */
	public DatagramChannel datagramChannel;
	
	/** The alive time. */
	public int aliveTime = 20000;
	
	/** The write buffer. */
	public final ByteBuffer readBuffer, writeBuffer;
	
	/** The selection key. */
	private SelectionKey selectionKey;
	
	/** The write lock. */
	private final Object writeLock = new Object();
	
	/** The last communication time. */
	private long lastCommunicationTime;

	/**
	 * Instantiates a new UDP connection handler.
	 *
	 * @param bufferSize the buffer size
	 */
	public UDPConnectionHandler (int bufferSize) {
		readBuffer = ByteBuffer.allocateDirect(bufferSize);
		writeBuffer = ByteBuffer.allocateDirect(bufferSize);
	}

	/**
	 * Bind.
	 *
	 * @param selector the selector
	 * @param localPort the local port
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void bind (Selector selector, int localPort) throws IOException {
		close();
		try {
			datagramChannel = selector.provider().openDatagramChannel();
			datagramChannel.socket().bind(new InetSocketAddress(localPort));
			datagramChannel.configureBlocking(false);
			selectionKey = datagramChannel.register(selector, SelectionKey.OP_READ);

			lastCommunicationTime = System.currentTimeMillis();
		} catch (IOException ex) {
			close();
			throw ex;
		}
	}

	/**
	 * Connect.
	 *
	 * @param selector the selector
	 * @param remoteAddress the remote address
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void connect (Selector selector, InetSocketAddress remoteAddress) throws IOException {
		close();
		try {
			datagramChannel = selector.provider().openDatagramChannel();
			datagramChannel.socket().bind(null);
			datagramChannel.socket().connect(remoteAddress);
			datagramChannel.configureBlocking(false);

			selectionKey = datagramChannel.register(selector, SelectionKey.OP_READ);

			lastCommunicationTime = System.currentTimeMillis();

			connectedAddress = remoteAddress;
		} catch (IOException ex) {
			close();
			IOException ioEx = new IOException("Unable to connect to: " + remoteAddress);
			ioEx.initCause(ex);
			throw ioEx;
		}
	}

	/**
	 * Read from address.
	 *
	 * @return the inet socket address
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public InetSocketAddress readFromAddress () throws IOException {
		DatagramChannel datagramChannel = this.datagramChannel;
		if (datagramChannel == null) throw new SocketException("Connection is closed.");
		lastCommunicationTime = System.currentTimeMillis();
		return (InetSocketAddress)datagramChannel.receive(readBuffer);
	}

	/**
	 * Read object.
	 *
	 * @param connectionHandler the connection handler
	 * @return the object
	 * @throws SerializationException the serialization exception
	 */
	public Object readObject (ConnectionHandler connectionHandler) throws SerializationException {
		readBuffer.flip();
		try {
			Object object = Network.readClassAndObject(connectionHandler, readBuffer, true);
			if (readBuffer.hasRemaining())
				throw new SerializationException("Incorrect number of bytes (" + readBuffer.remaining()
					+ " remaining) used to deserialized object: " + object);
			return object;
		} finally {
			readBuffer.clear();
		}
	}

	/**
	 * Send.
	 *
	 * @param connectionHandler the connection handler
	 * @param object the object
	 * @param address the address
	 * @return the int
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws SerializationException the serialization exception
	 */
	public int send (ConnectionHandler connectionHandler, Object object, SocketAddress address) throws IOException, SerializationException {
		DatagramChannel datagramChannel = this.datagramChannel;
		if (datagramChannel == null) throw new SocketException("Connection is closed.");
		synchronized (writeLock) {
			try {
				Network.writeClassAndObject(connectionHandler, object, writeBuffer, true);
				writeBuffer.flip();
				int length = writeBuffer.limit();
				datagramChannel.send(writeBuffer, address);

				lastCommunicationTime = System.currentTimeMillis();

				boolean wasFullWrite = !writeBuffer.hasRemaining();
				return wasFullWrite ? length : -1;
			} finally {
				writeBuffer.clear();
			}
		}
	}

	/**
	 * Close.
	 */
	public void close () {
		connectedAddress = null;
		try {
			if (datagramChannel != null) {
				datagramChannel.close();
				datagramChannel = null;
				if (selectionKey != null) selectionKey.selector().wakeup();
			}
		} catch (IOException ex) {
			log.severe("Unable to close UDP connection : " + ex.getMessage());
		}
	}

	/**
	 * Needs keep alive.
	 *
	 * @return true, if successful
	 */
	public boolean needsKeepAlive () {
		return connectedAddress != null && aliveTime > 0 && System.currentTimeMillis() - lastCommunicationTime > aliveTime;
	}
}
