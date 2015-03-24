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

package synergynetframework.appsystem.services.net.objectmessaging.utility.serializers;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import synergynetframework.appsystem.services.net.objectmessaging.connections.ConnectionHandler;

/**
 * The Class StringSerializer.
 */
public class StringSerializer extends Serializer {

	/**
	 * Gets the.
	 *
	 * @param buffer
	 *            the buffer
	 * @return the string
	 */
	static public String get(ByteBuffer buffer) {
		byte[] b = new byte[IntSerializer.get(buffer, true)];
		buffer.get(b);
		try {
			return new String(b, "UTF-8");
		} catch (UnsupportedEncodingException ignored) {
			return "";
		}
	}
	
	/**
	 * Put.
	 *
	 * @param buffer
	 *            the buffer
	 * @param value
	 *            the value
	 */
	static public void put(ByteBuffer buffer, String value) {
		try {
			byte[] b = value.getBytes("UTF-8");
			IntSerializer.put(buffer, b.length, true);
			buffer.put(b);
		} catch (UnsupportedEncodingException ignored) {
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.services.net.objectmessaging.utility.
	 * serializers
	 * .Serializer#readObjectData(synergynetframework.appsystem.services
	 * .net.objectmessaging.connections.ConnectionHandler, java.nio.ByteBuffer,
	 * java.lang.Class, boolean)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String readObjectData(ConnectionHandler connectionHandler,
			ByteBuffer buffer, Class type, boolean lengthKnown)
			throws SerializationException {
		String s = get(buffer);
		return s;
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.services.net.objectmessaging.utility.
	 * serializers
	 * .Serializer#writeObjectData(synergynetframework.appsystem.services
	 * .net.objectmessaging.connections.ConnectionHandler, java.nio.ByteBuffer,
	 * java.lang.Object, boolean)
	 */
	public void writeObjectData(ConnectionHandler connectionHandler,
			ByteBuffer buffer, Object object, boolean lengthKnown)
			throws SerializationException {
		String s = (String) object;
		put(buffer, s);
	}
}
