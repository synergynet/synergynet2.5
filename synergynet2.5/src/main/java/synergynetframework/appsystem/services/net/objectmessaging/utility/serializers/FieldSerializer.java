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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

import synergynetframework.appsystem.services.net.objectmessaging.Network;
import synergynetframework.appsystem.services.net.objectmessaging.connections.ConnectionHandler;


/**
 * The Class FieldSerializer.
 */
public class FieldSerializer extends Serializer {
	
	/** The Constant instance. */
	static private final FieldSerializer instance = new FieldSerializer();

	/** The set fields as accessible. */
	private boolean fieldsAreNotNull, setFieldsAsAccessible = true;
	
	/** The field cache. */
	private final HashMap<Class<?>, CachedField[]> fieldCache = new HashMap<Class<?>, CachedField[]>();

	/**
	 * Instantiates a new field serializer.
	 */
	public FieldSerializer () {
	}

	/**
	 * Instantiates a new field serializer.
	 *
	 * @param fieldsAreNotNull the fields are not null
	 * @param setFieldsAsAccessible the set fields as accessible
	 */
	public FieldSerializer (boolean fieldsAreNotNull, boolean setFieldsAsAccessible) {
		this.fieldsAreNotNull = fieldsAreNotNull;
		this.setFieldsAsAccessible = setFieldsAsAccessible;
	}

	/**
	 * Cache.
	 *
	 * @param type the type
	 * @return the cached field[]
	 */
	private CachedField[] cache (Class<?> type) {
		if (type.isInterface()) return new CachedField[0];
		ArrayList<Field> allFields = new ArrayList<Field>();
		Class<?> nextClass = type;
		while (nextClass != Object.class) {
			Collections.addAll(allFields, nextClass.getDeclaredFields());
			nextClass = nextClass.getSuperclass();
		}
		PriorityQueue<CachedField> cachedFields = new PriorityQueue<CachedField>(Math.max(1, allFields.size()), new Comparator<CachedField>() {
			public int compare (CachedField o1, CachedField o2) {
				return o1.field.getName().compareTo(o2.field.getName());
			}
		});
		for (int i = 0, n = allFields.size(); i < n; i++) {
			Field field = allFields.get(i);
			int modifiers = field.getModifiers();
			if (Modifier.isTransient(modifiers)) continue;
			if (Modifier.isFinal(modifiers)) continue;
			if (Modifier.isStatic(modifiers)) continue;
			if (field.isSynthetic()) continue;
			if (setFieldsAsAccessible)
				field.setAccessible(true);
			else if (Modifier.isPrivate(modifiers)) {
				continue;
			}

			CachedField cachedField = new CachedField();
			cachedField.field = field;
			cachedField.canBeNull = !field.isAnnotationPresent(NonNull.class);

			Class<?> fieldClass = field.getType();
			if (Modifier.isFinal(fieldClass.getModifiers()))
				cachedField.serializer = Network.getRegisteredClass(fieldClass).serializer;

			cachedFields.add(cachedField);
		}
		CachedField[] cachedFieldArray = cachedFields.toArray(new CachedField[cachedFields.size()]);
		fieldCache.put(type, cachedFieldArray);
		return cachedFieldArray;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.services.net.objectmessaging.utility.serializers.Serializer#writeObjectData(synergynetframework.appsystem.services.net.objectmessaging.connections.ConnectionHandler, java.nio.ByteBuffer, java.lang.Object, boolean)
	 */
	public void writeObjectData (ConnectionHandler connectionHandler, ByteBuffer buffer, Object object, boolean lengthKnown)
		throws SerializationException {
		try {
			CachedField[] fields = fieldCache.get(object.getClass());
			if (fields == null) fields = cache(object.getClass());
			for (int i = 0, n = fields.length; i < n; i++) {
				CachedField cachedField = fields[i];
				Object value = cachedField.field.get(object);
				Serializer serializer = cachedField.serializer;
				if (serializer != null) {
					if (fieldsAreNotNull || !cachedField.canBeNull)
						serializer.writeObjectData(connectionHandler, buffer, value, false);
					else
						serializer.writeObject(connectionHandler, value, buffer);
				} else
					Network.writeClassAndObject(connectionHandler, value, buffer);
			}
		} catch (IllegalAccessException ex) {
			throw new SerializationException("Error accessing field in class: " + object.getClass().getName(), ex);
		}
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.services.net.objectmessaging.utility.serializers.Serializer#readObjectData(synergynetframework.appsystem.services.net.objectmessaging.connections.ConnectionHandler, java.nio.ByteBuffer, java.lang.Class, boolean)
	 */
	public <T> T readObjectData (ConnectionHandler connectionHandler, ByteBuffer buffer, Class<T> type, boolean lengthKnown)
		throws SerializationException {
		T object = newInstance(type);
		try {
			CachedField[] fields = fieldCache.get(object.getClass());
			if (fields == null) fields = cache(object.getClass());
			for (int i = 0, n = fields.length; i < n; i++) {
				CachedField cachedField = fields[i];
				Object value;
				Field field = cachedField.field;
				Serializer serializer = cachedField.serializer;
				if (serializer != null) {
					if (fieldsAreNotNull || !cachedField.canBeNull)
						value = serializer.readObjectData(connectionHandler, buffer, field.getType(), false);
					else
						value = serializer.readObject(connectionHandler, buffer, field.getType());
				} else
					value = Network.readClassAndObject(connectionHandler, buffer);
				field.set(object, value);
			}
		} catch (IllegalAccessException ex) {
			throw new SerializationException("Error accessing field in class: " + type.getName(), ex);
		}
		return object;
	}

	/**
	 * Sets the field.
	 *
	 * @param type the type
	 * @param fieldName the field name
	 * @param serializer the serializer
	 * @param canBeNull the can be null
	 */
	public void setField (Class<?> type, String fieldName, Serializer serializer, boolean canBeNull) {
		CachedField[] fields = fieldCache.get(type);
		if (fields == null) fields = cache(type);
		for (CachedField cachedField : fields) {
			if (cachedField.field.getName().equals(fieldName)) {
				cachedField.serializer = serializer;
				cachedField.canBeNull = canBeNull;
				break;
			}
		}
	}

	/**
	 * The Class CachedField.
	 */
	static class CachedField {
		
		/** The field. */
		public Field field;
		
		/** The serializer. */
		public Serializer serializer;
		
		/** The can be null. */
		public boolean canBeNull;

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString () {
			return field.getName();
		}
	}

	/**
	 * Put.
	 *
	 * @param connectionHandler the connection handler
	 * @param buffer the buffer
	 * @param object the object
	 * @param fieldsAreNotNull the fields are not null
	 * @param setFieldsAsAccessible the set fields as accessible
	 * @throws SerializationException the serialization exception
	 */
	static public void put (ConnectionHandler connectionHandler, ByteBuffer buffer, Object object, boolean fieldsAreNotNull,
		boolean setFieldsAsAccessible) throws SerializationException {
		instance.fieldsAreNotNull = fieldsAreNotNull;
		instance.setFieldsAsAccessible = setFieldsAsAccessible;
		instance.writeObjectData(connectionHandler, buffer, object, false);
	}

	/**
	 * Gets the.
	 *
	 * @param <T> the generic type
	 * @param connectionHandler the connection handler
	 * @param buffer the buffer
	 * @param type the type
	 * @param fieldsAreNotNull the fields are not null
	 * @param setFieldsAsAccessible the set fields as accessible
	 * @return the t
	 * @throws SerializationException the serialization exception
	 */
	static public <T> T get (ConnectionHandler connectionHandler, ByteBuffer buffer, Class<T> type, boolean fieldsAreNotNull,
		boolean setFieldsAsAccessible) throws SerializationException {
		instance.fieldsAreNotNull = fieldsAreNotNull;
		instance.setFieldsAsAccessible = setFieldsAsAccessible;
		return instance.readObjectData(connectionHandler, buffer, type, false);
	}
}
