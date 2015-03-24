package synergynetframework.appsystem.services.net.objectmessaging.utility.serializers;

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

import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;

import synergynetframework.appsystem.services.net.objectmessaging.connections.ConnectionHandler;

/**
 * The Class Serializer.
 */
abstract public class Serializer {

	/**
	 * Read null.
	 *
	 * @param buffer
	 *            the buffer
	 * @return true, if successful
	 */
	static public boolean readNull(ByteBuffer buffer) {
		if (buffer.get() == 0) {
			return false;
		}
		return true;
	}
	
	/**
	 * Write null.
	 *
	 * @param buffer
	 *            the buffer
	 * @param object
	 *            the object
	 * @return true, if successful
	 */
	static public boolean writeNull(ByteBuffer buffer, Object object) {
		if (object == null) {
			buffer.put((byte) 0);
			return false;
		}
		buffer.put((byte) 1);
		return true;
	}
	
	/** The can be null. */
	private boolean canBeNull = true;
	
	/**
	 * New instance.
	 *
	 * @param <T>
	 *            the generic type
	 * @param type
	 *            the type
	 * @return the t
	 * @throws SerializationException
	 *             the serialization exception
	 */
	public <T> T newInstance(Class<T> type) throws SerializationException {
		try {
			return type.newInstance();
		} catch (Exception ex) {
			if (ex instanceof InstantiationException) {
				Constructor<?>[] constructors = type.getConstructors();
				boolean hasZeroArgConstructor = false;
				for (int i = 0, n = constructors.length; i < n; i++) {
					Constructor<?> constructor = constructors[i];
					if (constructor.getParameterTypes().length == 0) {
						hasZeroArgConstructor = true;
						break;
					}
				}
				if (!hasZeroArgConstructor) {
					throw new SerializationException(
							"Class cannot be created (missing no-arg constructor): "
									+ type.getName(), ex);
				}
			}
			throw new SerializationException(
					"Error constructing instance of class: " + type.getName(),
					ex);
		}
	}
	
	/**
	 * Read object.
	 *
	 * @param <T>
	 *            the generic type
	 * @param connectionHandler
	 *            the connection handler
	 * @param buffer
	 *            the buffer
	 * @param type
	 *            the type
	 * @return the t
	 * @throws SerializationException
	 *             the serialization exception
	 */
	public final <T> T readObject(ConnectionHandler connectionHandler,
			ByteBuffer buffer, Class<T> type) throws SerializationException {
		if (canBeNull && (buffer.get() == 0)) {
			return null;
		}
		return readObjectData(connectionHandler, buffer, type, false);
	}
	
	/**
	 * Read object data.
	 *
	 * @param <T>
	 *            the generic type
	 * @param connectionHandler
	 *            the connection handler
	 * @param buffer
	 *            the buffer
	 * @param type
	 *            the type
	 * @return the t
	 * @throws SerializationException
	 *             the serialization exception
	 */
	public <T> T readObjectData(ConnectionHandler connectionHandler,
			ByteBuffer buffer, Class<T> type) throws SerializationException {
		return readObjectData(connectionHandler, buffer, type, false);
	}
	
	/**
	 * Read object data.
	 *
	 * @param <T>
	 *            the generic type
	 * @param connectionHandler
	 *            the connection handler
	 * @param buffer
	 *            the buffer
	 * @param type
	 *            the type
	 * @param lengthKnown
	 *            the length known
	 * @return the t
	 * @throws SerializationException
	 *             the serialization exception
	 */
	abstract public <T> T readObjectData(ConnectionHandler connectionHandler,
			ByteBuffer buffer, Class<T> type, boolean lengthKnown)
			throws SerializationException;
	
	/**
	 * Sets the can be null.
	 *
	 * @param canBeNull
	 *            the new can be null
	 */
	public void setCanBeNull(boolean canBeNull) {
		this.canBeNull = canBeNull;
	}
	
	/**
	 * Write object.
	 *
	 * @param connectionHandler
	 *            the connection handler
	 * @param object
	 *            the object
	 * @param buffer
	 *            the buffer
	 * @throws SerializationException
	 *             the serialization exception
	 */
	public final void writeObject(ConnectionHandler connectionHandler,
			Object object, ByteBuffer buffer) throws SerializationException {
		if (canBeNull) {
			if (object == null) {
				buffer.put((byte) 0);
				return;
			}
			buffer.put((byte) 1);
		}
		writeObjectData(connectionHandler, buffer, object, false);
	}
	
	/**
	 * Write object data.
	 *
	 * @param connectionHandler
	 *            the connection handler
	 * @param buffer
	 *            the buffer
	 * @param object
	 *            the object
	 * @param lengthKnown
	 *            the length known
	 * @throws SerializationException
	 *             the serialization exception
	 */
	public abstract void writeObjectData(ConnectionHandler connectionHandler,
			ByteBuffer buffer, Object object, boolean lengthKnown)
			throws SerializationException;
	
	/**
	 * Write object data.
	 *
	 * @param connectionHandler
	 *            the connection handler
	 * @param object
	 *            the object
	 * @param buffer
	 *            the buffer
	 * @throws SerializationException
	 *             the serialization exception
	 */
	public void writeObjectData(ConnectionHandler connectionHandler,
			Object object, ByteBuffer buffer) throws SerializationException {
		writeObjectData(connectionHandler, buffer, object, false);
	}
}
