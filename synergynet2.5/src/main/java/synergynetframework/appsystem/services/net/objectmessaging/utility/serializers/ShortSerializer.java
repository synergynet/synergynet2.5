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

public class ShortSerializer extends Serializer {
	private boolean optimizePositive = true;

	public ShortSerializer () {
	}

	public ShortSerializer (Boolean optimizePositive) {
		this.optimizePositive = optimizePositive;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Short readObjectData (ConnectionHandler connectionHandler, ByteBuffer buffer, Class type, boolean lengthKnown)
		throws SerializationException {
		short s = get(buffer, optimizePositive);
		return s;
	}

	public void writeObjectData (ConnectionHandler connectionHandler, ByteBuffer buffer, Object object, boolean lengthKnown)
		throws SerializationException {
		put(buffer, (Short)object, optimizePositive);
	}

	static private final byte SHORT = -128;
	static private final byte SHORT_POSITIVE = -1;

	static public short put (ByteBuffer buffer, short value, boolean optimizePositive) {
		if (optimizePositive) {
			if (value >= 0 && value <= 254) {
				buffer.put((byte)value);
				return 1;
			}
			buffer.put(SHORT_POSITIVE);
			buffer.putShort(value);
		} else {
			if (value >= -127 && value <= 127) {
				buffer.put((byte)value);
				return 1;
			}
			buffer.put(SHORT);
			buffer.putShort(value);
		}
		return 3;
	}

	static public short get (ByteBuffer buffer, boolean optimizePositive) {
		byte value = buffer.get();
		if (optimizePositive) {
			if (value == SHORT_POSITIVE) return buffer.getShort();
			if (value < 0) return (short)(value + 256);
		} else {
			if (value == SHORT) return buffer.getShort();
		}
		return value;
	}
}
