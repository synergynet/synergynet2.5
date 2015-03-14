
package synergynetframework.appsystem.services.net.objectmessaging.utility.serializers;

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


import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;

import synergynetframework.appsystem.services.net.objectmessaging.connections.ConnectionHandler;

abstract public class Serializer {
	private boolean canBeNull = true;

	public void setCanBeNull (boolean canBeNull) {
		this.canBeNull = canBeNull;
	}

	public final void writeObject (ConnectionHandler connectionHandler, Object object, ByteBuffer buffer) throws SerializationException {
		if (canBeNull) {
			if (object == null) {
				buffer.put((byte)0);
				return;
			}
			buffer.put((byte)1);
		}
		writeObjectData(connectionHandler, buffer, object, false);
	}

	public abstract void writeObjectData (ConnectionHandler connectionHandler, ByteBuffer buffer, Object object, boolean lengthKnown)
		throws SerializationException;

	public void writeObjectData (ConnectionHandler connectionHandler, Object object, ByteBuffer buffer) throws SerializationException {
		writeObjectData(connectionHandler, buffer, object, false);
	}

	public final <T> T readObject (ConnectionHandler connectionHandler, ByteBuffer buffer, Class<T> type) throws SerializationException {
		if (canBeNull && buffer.get() == 0) {
			return null;
		}
		return readObjectData(connectionHandler, buffer, type, false);
	}

	abstract public <T> T readObjectData (ConnectionHandler connectionHandler, ByteBuffer buffer, Class<T> type, boolean lengthKnown)
		throws SerializationException;

	public <T> T readObjectData (ConnectionHandler connectionHandler, ByteBuffer buffer, Class<T> type) throws SerializationException {
		return readObjectData(connectionHandler, buffer, type, false);
	}

	public <T> T newInstance (Class<T> type) throws SerializationException {
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
				if (!hasZeroArgConstructor)
					throw new SerializationException("Class cannot be created (missing no-arg constructor): " + type.getName(), ex);
			}
			throw new SerializationException("Error constructing instance of class: " + type.getName(), ex);
		}
	}

	static public boolean writeNull (ByteBuffer buffer, Object object) {
		if (object == null) {
			buffer.put((byte)0);
			return false;
		}
		buffer.put((byte)1);
		return true;
	}

	static public boolean readNull (ByteBuffer buffer) {
		if (buffer.get() == 0) {
			return false;
		}
		return true;
	}
}
