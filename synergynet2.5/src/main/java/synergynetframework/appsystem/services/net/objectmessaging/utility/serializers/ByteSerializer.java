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


import java.nio.ByteBuffer;

import synergynetframework.appsystem.services.net.objectmessaging.connections.ConnectionHandler;


/**
 * The Class ByteSerializer.
 */
public class ByteSerializer extends Serializer {
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.services.net.objectmessaging.utility.serializers.Serializer#readObjectData(synergynetframework.appsystem.services.net.objectmessaging.connections.ConnectionHandler, java.nio.ByteBuffer, java.lang.Class, boolean)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Byte readObjectData (ConnectionHandler connectionHandler, ByteBuffer buffer, Class type, boolean lengthKnown)
		throws SerializationException {
		byte b = buffer.get();
		return b;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.services.net.objectmessaging.utility.serializers.Serializer#writeObjectData(synergynetframework.appsystem.services.net.objectmessaging.connections.ConnectionHandler, java.nio.ByteBuffer, java.lang.Object, boolean)
	 */
	public void writeObjectData (ConnectionHandler connectionHandler, ByteBuffer buffer, Object object, boolean lengthKnown)
		throws SerializationException {
		buffer.put((Byte)object);
	}

	/**
	 * Writes the specified non-negative int to the buffer, cast as a byte.
	 *
	 * @param buffer the buffer
	 * @param value the value
	 */
	static public void putUnsigned (ByteBuffer buffer, int value) {
		if (value < 0) throw new IllegalArgumentException("value cannot be less than zero: " + value);
		buffer.put((byte)value);
	}

	/**
	 * Reads a non-negative byte from the buffer that was written with {@link #putUnsigned(ByteBuffer, int)}.
	 *
	 * @param buffer the buffer
	 * @return the unsigned
	 */
	static public int getUnsigned (ByteBuffer buffer) {
		byte value = buffer.get();
		if (value < 0) return value + 256;
		return value;
	}
}
