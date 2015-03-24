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
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import synergynetframework.appsystem.services.net.objectmessaging.Network;
import synergynetframework.appsystem.services.net.objectmessaging.connections.ConnectionHandler;


/**
 * The Class MapSerializer.
 */
public class MapSerializer extends Serializer {
	
	/** The Constant instance. */
	static private final MapSerializer instance = new MapSerializer();

	/** The key class. */
	private Class<?> keyClass;
	
	/** The value class. */
	private Class<?> valueClass;
	
	/** The key serializer. */
	private Serializer keySerializer;
	
	/** The value serializer. */
	private Serializer valueSerializer;
	
	/** The keys are not null. */
	private boolean keysAreNotNull;
	
	/** The values are not null. */
	private boolean valuesAreNotNull;

	/**
	 * Instantiates a new map serializer.
	 */
	public MapSerializer () {
	}

	/**
	 * Instantiates a new map serializer.
	 *
	 * @param keyClass the key class
	 * @param keysAreNotNull the keys are not null
	 * @param valueClass the value class
	 * @param valuesAreNotNull the values are not null
	 */
	public MapSerializer (Class<?> keyClass, boolean keysAreNotNull, Class<?> valueClass, boolean valuesAreNotNull) {
		this.keyClass = keyClass;
		this.keysAreNotNull = keysAreNotNull;
		keySerializer = keyClass == null ? null : Network.getRegisteredClass(keyClass).serializer;
		this.valueClass = valueClass;
		this.valuesAreNotNull = valuesAreNotNull;
		valueSerializer = valueClass == null ? null : Network.getRegisteredClass(valueClass).serializer;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.services.net.objectmessaging.utility.serializers.Serializer#writeObjectData(synergynetframework.appsystem.services.net.objectmessaging.connections.ConnectionHandler, java.nio.ByteBuffer, java.lang.Object, boolean)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void writeObjectData (ConnectionHandler connectionHandler, ByteBuffer buffer, Object object, boolean lengthKnown)
		throws SerializationException {
		Map<Object, Object> map = (Map)object;
		int length = map.size();
		IntSerializer.put(buffer, length, true);
		if (length == 0) return;
		for (Iterator iter = map.entrySet().iterator(); iter.hasNext();) {
			Entry entry = (Entry)iter.next();
			if (keySerializer != null) {
				if (keysAreNotNull)
					keySerializer.writeObjectData(connectionHandler, buffer, entry.getKey(), false);
				else
					keySerializer.writeObject(connectionHandler, entry.getKey(), buffer);
			} else
				Network.writeClassAndObject(connectionHandler, entry.getKey(), buffer);
			if (valueSerializer != null) {
				if (valuesAreNotNull)
					valueSerializer.writeObjectData(connectionHandler, buffer, entry.getValue(), false);
				else
					valueSerializer.writeObject(connectionHandler, entry.getValue(), buffer);
			} else
				Network.writeClassAndObject(connectionHandler, entry.getValue(), buffer);
		}
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.services.net.objectmessaging.utility.serializers.Serializer#readObjectData(synergynetframework.appsystem.services.net.objectmessaging.connections.ConnectionHandler, java.nio.ByteBuffer, java.lang.Class, boolean)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> T readObjectData (ConnectionHandler connectionHandler, ByteBuffer buffer, Class<T> type, boolean lengthKnown)
		throws SerializationException {
		Map map = (Map)newInstance(type);
		int length = IntSerializer.get(buffer, true);
		if (length == 0) return (T)map;
		for (int i = 0; i < length; i++) {
			Object key;
			if (keySerializer != null) {
				if (keysAreNotNull)
					key = keySerializer.readObjectData(connectionHandler, buffer, keyClass, false);
				else
					key = keySerializer.readObject(connectionHandler, buffer, keyClass);
			} else
				key = Network.readClassAndObject(connectionHandler, buffer);
			Object value;
			if (valueSerializer != null) {
				if (valuesAreNotNull)
					value = valueSerializer.readObjectData(connectionHandler, buffer, valueClass, false);
				else
					value = valueSerializer.readObject(connectionHandler, buffer, valueClass);
			} else
				value = Network.readClassAndObject(connectionHandler, buffer);
			map.put(key, value);
		}
		return (T)map;
	}

	/**
	 * Put.
	 *
	 * @param connectionHandler the connection handler
	 * @param buffer the buffer
	 * @param object the object
	 * @param keyClass the key class
	 * @param keysAreNotNull the keys are not null
	 * @param valueClass the value class
	 * @param valuesAreNotNull the values are not null
	 * @throws SerializationException the serialization exception
	 */
	static public void put (ConnectionHandler connectionHandler, ByteBuffer buffer, Object object, Class<?> keyClass, boolean keysAreNotNull,
		Class<?> valueClass, boolean valuesAreNotNull) throws SerializationException {
		instance.keyClass = keyClass;
		instance.keysAreNotNull = keysAreNotNull;
		instance.valueClass = valueClass;
		instance.valuesAreNotNull = valuesAreNotNull;
		instance.writeObjectData(connectionHandler, buffer, object, false);
	}

	/**
	 * Gets the.
	 *
	 * @param <T> the generic type
	 * @param connectionHandler the connection handler
	 * @param buffer the buffer
	 * @param type the type
	 * @param keyClass the key class
	 * @param keysAreNotNull the keys are not null
	 * @param valueClass the value class
	 * @param valuesAreNotNull the values are not null
	 * @return the t
	 * @throws SerializationException the serialization exception
	 */
	static public <T> T get (ConnectionHandler connectionHandler, ByteBuffer buffer, Class<T> type, Class<?> keyClass, boolean keysAreNotNull,
		Class<?> valueClass, boolean valuesAreNotNull) throws SerializationException {
		instance.keyClass = keyClass;
		instance.keysAreNotNull = keysAreNotNull;
		instance.valueClass = valueClass;
		instance.valuesAreNotNull = valuesAreNotNull;
		return instance.readObjectData(connectionHandler, buffer, type, false);
	}
}
