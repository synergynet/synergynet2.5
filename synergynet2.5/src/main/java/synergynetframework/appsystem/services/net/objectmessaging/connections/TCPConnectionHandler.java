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
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

import synergynetframework.appsystem.services.net.objectmessaging.Network;
import synergynetframework.appsystem.services.net.objectmessaging.utility.serializers.IntSerializer;
import synergynetframework.appsystem.services.net.objectmessaging.utility.serializers.SerializationException;



/**
 * The Class TCPConnectionHandler.
 */
public class TCPConnectionHandler {
	
	/** The Constant log. */
	private static final Logger log = Logger.getLogger(TCPConnectionHandler.class.getName());

	/** The alive time. */
	protected int aliveTime = 60000;
	
	/** The socket channel. */
	protected SocketChannel socketChannel;
	
	/** The write buffer. */
	protected final ByteBuffer readBuffer, writeBuffer;
	
	/** The write length buffer. */
	protected final ByteBuffer writeLengthBuffer = ByteBuffer.allocateDirect(32 * 1000);
	
	/** The connection handler. */
	protected final ConnectionHandler connectionHandler;
	
	/** The selection key. */
	protected SelectionKey selectionKey;
	
	/** The write lock. */
	protected final Object writeLock = new Object();
	
	/** The current object length. */
	protected int currentObjectLength;
	
	/** The last communication time. */
	protected long lastCommunicationTime;

	/**
	 * Instantiates a new TCP connection handler.
	 *
	 * @param connectionHandler the connection handler
	 * @param bufferSize the buffer size
	 */
	public TCPConnectionHandler (ConnectionHandler connectionHandler, int bufferSize) {
		this.connectionHandler = connectionHandler;
		readBuffer = ByteBuffer.allocateDirect(bufferSize);
		writeBuffer = ByteBuffer.allocateDirect(bufferSize);
	}

	/**
	 * Accept.
	 *
	 * @param selector the selector
	 * @param socketChannel the socket channel
	 * @return the selection key
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public SelectionKey accept (Selector selector, SocketChannel socketChannel) throws IOException {
		try {
			close();
			this.socketChannel = socketChannel;
			socketChannel.configureBlocking(false);
			socketChannel.socket().setTcpNoDelay(true);

			selectionKey = socketChannel.register(selector, SelectionKey.OP_READ);

			log.info("Port " + socketChannel.socket().getLocalPort() + "/TCP connected to: " + socketChannel.socket().getRemoteSocketAddress());

			lastCommunicationTime = System.currentTimeMillis();

			return selectionKey;
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
	 * @param timeout the timeout
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void connect (Selector selector, SocketAddress remoteAddress, int timeout) throws IOException {
		close();
		try {
			SocketChannel socketChannel = selector.provider().openSocketChannel();
			socketChannel.socket().setTcpNoDelay(true);
			socketChannel.socket().bind(null);
			socketChannel.socket().connect(remoteAddress, timeout);
			socketChannel.configureBlocking(false);
			this.socketChannel = socketChannel;

			selectionKey = socketChannel.register(selector, SelectionKey.OP_READ);
			selectionKey.attach(this);

			log.info("Port " + socketChannel.socket().getLocalPort() + "/TCP connected to: " + socketChannel.socket().getRemoteSocketAddress());

			lastCommunicationTime = System.currentTimeMillis();
		} catch (IOException ex) {
			close();
			IOException ioEx = new IOException("Unable to connect to: " + remoteAddress);
			ioEx.initCause(ex);
			throw ioEx;
		}
	}

	/**
	 * Read object.
	 *
	 * @return the object
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws SerializationException the serialization exception
	 */
	public Object readObject () throws IOException, SerializationException {
		SocketChannel socketChannel = this.socketChannel;
		if (socketChannel == null) throw new SocketException("Connection is closed.");
		int bytesRead = socketChannel.read(readBuffer);
		if (bytesRead == -1) throw new SocketException("Connection is closed.");

		lastCommunicationTime = System.currentTimeMillis();

		readBuffer.flip();
		try {
			if (currentObjectLength == 0) {
				if (!readBuffer.hasRemaining()) return null;
				if (!IntSerializer.canRead(readBuffer, true)) return null;
				currentObjectLength = IntSerializer.get(readBuffer, true);
				if (currentObjectLength < 0) currentObjectLength += 65536;
				if (currentObjectLength > readBuffer.capacity())
					throw new SerializationException("Unable to read object larger than read buffer: " + currentObjectLength);
			}

			int length = currentObjectLength;
			if (readBuffer.remaining() < length) return null;
			currentObjectLength = 0;

			int startPosition = readBuffer.position();
			int limit = readBuffer.limit();
			readBuffer.limit(startPosition + length);
			Object object = Network.readClassAndObject(connectionHandler, readBuffer, true);
			readBuffer.limit(limit);
			if (readBuffer.position() - startPosition != length)
				throw new SerializationException("Incorrect number of bytes (" + (startPosition + length - readBuffer.position())
					+ " remaining) used to deserialized object: " + object);

			return object;
		} finally {
			readBuffer.compact();
		}
	}

	/**
	 * Write operation.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeOperation () throws IOException {
		synchronized (writeLock) {
			if (writeToSocket()) selectionKey.interestOps(SelectionKey.OP_READ);
		}
	}

	/**
	 * Write to socket.
	 *
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private boolean writeToSocket () throws IOException {
		SocketChannel socketChannel = this.socketChannel;
		if (socketChannel == null) throw new SocketException("Connection is closed.");
		writeBuffer.flip();
		while (writeBuffer.hasRemaining())
			if (socketChannel.write(writeBuffer) == 0) break;
		boolean wasFullWrite = !writeBuffer.hasRemaining();
		writeBuffer.compact();

		lastCommunicationTime = System.currentTimeMillis();

		return wasFullWrite;
	}

	/**
	 * Send.
	 *
	 * @param object the object
	 * @return the int
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws SerializationException the serialization exception
	 */
	public int send (Object object) throws IOException, SerializationException {
		SocketChannel socketChannel = this.socketChannel;
		if (socketChannel == null) throw new SocketException("Connection is closed.");
		synchronized (writeLock) {
			int start = writeBuffer.position();
			try {
				Network.writeClassAndObject(connectionHandler, object, writeBuffer, true);
			} catch (SerializationException ex) {
				writeBuffer.position(start);
				throw new SerializationException("Unable to serialize object of type: " + object.getClass().getName(), ex);
			}

			// Write data length to socket.
			int dataLength = writeBuffer.position() - start;
			writeLengthBuffer.clear();
			int lengthLength = IntSerializer.put(writeLengthBuffer, dataLength, true);
			writeLengthBuffer.flip();
			while (writeLengthBuffer.hasRemaining())
				if (socketChannel.write(writeLengthBuffer) == 0) break;
			if (writeLengthBuffer.hasRemaining()) {
				// If writing the length failed, shift the object data over.
				int shift = writeLengthBuffer.remaining();
				for (int i = dataLength - 1; i >= 0; i--)
					writeBuffer.put(i + shift, writeBuffer.get(i));
				// Insert the part of the length that failed.
				writeBuffer.position(start);
				while (writeLengthBuffer.hasRemaining())
					writeBuffer.put(writeLengthBuffer.get());
				writeBuffer.position(start + dataLength + shift);
			}

			// If it was a partial write, set the OP_WRITE flag to be notified when more writing can occur.
			if (!writeToSocket()) selectionKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);

			return lengthLength + dataLength;
		}
	}

	/**
	 * Close.
	 */
	public void close () {
		try {
			if (socketChannel != null) {
				socketChannel.close();
				socketChannel = null;
				if (selectionKey != null) selectionKey.selector().wakeup();
			}
		} catch (IOException ex) {
			log.severe("Unable to close TCP connection : " + ex.getMessage());
		}
	}

	/**
	 * Needs keep alive.
	 *
	 * @return true, if successful
	 */
	public boolean needsKeepAlive () {
		return socketChannel != null && aliveTime > 0 && System.currentTimeMillis() - lastCommunicationTime > aliveTime;
	}
	
	/**
	 * Gets the socket channel.
	 *
	 * @return the socket channel
	 */
	public SocketChannel getSocketChannel(){
		return socketChannel;
	}
	
	/**
	 * Gets the read buffer.
	 *
	 * @return the read buffer
	 */
	public ByteBuffer getReadBuffer(){
		return readBuffer;
	}
	
	/**
	 * Gets the write buffer.
	 *
	 * @return the write buffer
	 */
	public ByteBuffer getWriteBuffer(){
		return writeBuffer;
	}
	
	/**
	 * Sets the alive time.
	 *
	 * @param aliveTime the new alive time
	 */
	public void setAliveTime(int aliveTime){
		this.aliveTime = aliveTime;
	}
	
	/**
	 * Gets the alive time.
	 *
	 * @return the alive time
	 */
	public int getAliveTime(){
		return aliveTime;
	}
}
