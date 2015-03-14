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

public class IntSerializer extends Serializer {
	private boolean optimizePositive = true;

	public IntSerializer () {
	}

	public IntSerializer (Boolean optimizePositive) {
		this.optimizePositive = optimizePositive;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Integer readObjectData (ConnectionHandler connectionHandler, ByteBuffer buffer, Class type, boolean lengthKnown)
		throws SerializationException {
		int i = get(buffer, optimizePositive);
		return i;
	}

	public void writeObjectData (ConnectionHandler connectionHandler, ByteBuffer buffer, Object object, boolean lengthKnown)
		throws SerializationException {
		put(buffer, (Integer)object, optimizePositive);
	}

	static private final byte SHORT = -128;
	static private final byte INT = -127;
	static private final byte SHORT_POSITIVE = -1;
	static private final byte INT_POSITIVE = -2;

	static public int put (ByteBuffer buffer, int value, boolean optimizePositive) {
		if (optimizePositive) {
			if (value >= 0 && value <= 253) {
				buffer.put((byte)value);
				return 1;
			} else if (value >= 0 && value < 65536) {
				buffer.put(SHORT_POSITIVE);
				buffer.putShort((short)value);
				return 3;
			}
			buffer.put(INT_POSITIVE);
			buffer.putInt(value);
		} else {
			if (value >= -126 && value <= 127) {
				buffer.put((byte)value);
				return 1;
			} else if (value >= Short.MIN_VALUE && value <= Short.MAX_VALUE) {
				buffer.put(SHORT);
				buffer.putShort((short)value);
				return 3;
			}
			buffer.put(INT);
			buffer.putInt(value);
		}
		return 5;
	}

	static public int get (ByteBuffer buffer, boolean optimizePositive) {
		byte value = buffer.get();
		if (optimizePositive) {
			switch (value) {
			case SHORT_POSITIVE:
				int shortValue = buffer.getShort();
				if (shortValue < 0) return shortValue + 65536;
				return shortValue;
			case INT_POSITIVE:
				return buffer.getInt();
			}
			if (value < 0) return value + 256;
		} else {
			switch (value) {
			case SHORT:
				return buffer.getShort();
			case INT:
				return buffer.getInt();
			}
		}
		return value;
	}

	static public boolean canRead (ByteBuffer buffer, boolean optimizePositive) {
		int position = buffer.position();
		byte value = buffer.get();
		buffer.position(position);
		if (optimizePositive) {
			switch (value) {
			case SHORT_POSITIVE:
				return buffer.remaining() >= 2;
			case INT_POSITIVE:
				return buffer.remaining() >= 4;
			}
		} else {
			switch (value) {
			case SHORT:
				return buffer.remaining() >= 2;
			case INT:
				return buffer.remaining() >= 4;
			}
		}
		return true;
	}
}
