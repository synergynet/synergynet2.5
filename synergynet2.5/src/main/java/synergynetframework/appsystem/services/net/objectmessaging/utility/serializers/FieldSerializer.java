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

public class FieldSerializer extends Serializer {
	static private final FieldSerializer instance = new FieldSerializer();

	private boolean fieldsAreNotNull, setFieldsAsAccessible = true;
	private final HashMap<Class<?>, CachedField[]> fieldCache = new HashMap<Class<?>, CachedField[]>();

	public FieldSerializer () {
	}

	public FieldSerializer (boolean fieldsAreNotNull, boolean setFieldsAsAccessible) {
		this.fieldsAreNotNull = fieldsAreNotNull;
		this.setFieldsAsAccessible = setFieldsAsAccessible;
	}

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

	static class CachedField {
		public Field field;
		public Serializer serializer;
		public boolean canBeNull;

		public String toString () {
			return field.getName();
		}
	}

	static public void put (ConnectionHandler connectionHandler, ByteBuffer buffer, Object object, boolean fieldsAreNotNull,
		boolean setFieldsAsAccessible) throws SerializationException {
		instance.fieldsAreNotNull = fieldsAreNotNull;
		instance.setFieldsAsAccessible = setFieldsAsAccessible;
		instance.writeObjectData(connectionHandler, buffer, object, false);
	}

	static public <T> T get (ConnectionHandler connectionHandler, ByteBuffer buffer, Class<T> type, boolean fieldsAreNotNull,
		boolean setFieldsAsAccessible) throws SerializationException {
		instance.fieldsAreNotNull = fieldsAreNotNull;
		instance.setFieldsAsAccessible = setFieldsAsAccessible;
		return instance.readObjectData(connectionHandler, buffer, type, false);
	}
}
