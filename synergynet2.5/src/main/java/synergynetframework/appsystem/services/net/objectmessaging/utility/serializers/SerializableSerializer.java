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


package synergynetframework.appsystem.services.net.objectmessaging.utility.serializers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;

import synergynetframework.appsystem.services.net.objectmessaging.connections.ConnectionHandler;

public class SerializableSerializer extends Serializer {
	static private final SerializableSerializer instance = new SerializableSerializer();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object readObjectData (ConnectionHandler connectionHandler, ByteBuffer buffer, Class type, boolean lengthKnown)
		throws SerializationException {
		int length = IntSerializer.get(buffer, true);
		byte[] array = new byte[length];
		buffer.get(array);
		try {
			Object object = new ObjectInputStream(new ByteArrayInputStream(array)).readObject();
			return object;
		} catch (Exception ex) {
			throw new SerializationException("Error during Java deserialization.", ex);
		}
	}

	public void writeObjectData (ConnectionHandler connectionHandler, ByteBuffer buffer, Object object, boolean lengthKnown)
		throws SerializationException {
		try {
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
			objectStream.writeObject(object);
			objectStream.close();
			byte[] array = byteStream.toByteArray();
			IntSerializer.put(buffer, array.length, true);
			buffer.put(array);
		} catch (BufferOverflowException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new SerializationException("Error during Java serialization.", ex);
		}
	}

	static public void put (ByteBuffer buffer, Object object) throws SerializationException {
		instance.writeObjectData(null, object, buffer);
	}

	static public <T> T get (ByteBuffer buffer, Class<T> type) throws SerializationException {
		return instance.readObjectData(null, buffer, type);
	}
}
