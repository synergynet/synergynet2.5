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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import synergynetframework.appsystem.services.net.objectmessaging.Network;
import synergynetframework.appsystem.services.net.objectmessaging.connections.ConnectionHandler;

public class BeanSerializer extends Serializer {
	static private final BeanSerializer instance = new BeanSerializer();

	private final HashMap<Class<?>, CachedMethod[]> setterMethodCache = new HashMap<Class<?>, CachedMethod[]>();
	private final HashMap<Class<?>, CachedMethod[]> getterMethodCache = new HashMap<Class<?>, CachedMethod[]>();

	private void cache (Class<?> type) throws SerializationException {
		BeanInfo info;
		try {
			info = Introspector.getBeanInfo(type);
		} catch (IntrospectionException ex) {
			throw new SerializationException("Error getting bean info.", ex);
		}
		PropertyDescriptor[] descriptors = info.getPropertyDescriptors();
		Arrays.sort(descriptors, new Comparator<PropertyDescriptor>() {
			public int compare (PropertyDescriptor o1, PropertyDescriptor o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		ArrayList<CachedMethod> getterMethods = new ArrayList<CachedMethod>(descriptors.length);
		ArrayList<CachedMethod> setterMethods = new ArrayList<CachedMethod>(descriptors.length);
		for (int i = 0, n = descriptors.length; i < n; i++) {
			PropertyDescriptor property = descriptors[i];
			if (property.getName().equals("class")) continue;
			Method getMethod = property.getReadMethod();
			Method setMethod = property.getWriteMethod();
			if (getMethod == null || setMethod == null) continue;

			Serializer serializer = null;
			Class<?> returnType = getMethod.getReturnType();
			if (Modifier.isFinal(returnType.getModifiers())) serializer = Network.getRegisteredClass(returnType).serializer;

			CachedMethod cachedGetMethod = new CachedMethod();
			cachedGetMethod.method = getMethod;
			cachedGetMethod.serializer = serializer;
			getterMethods.add(cachedGetMethod);

			CachedMethod cachedSetMethod = new CachedMethod();
			cachedSetMethod.method = setMethod;
			cachedSetMethod.serializer = serializer;
			cachedSetMethod.type = setMethod.getParameterTypes()[0];
			setterMethods.add(cachedSetMethod);
		}
		getterMethodCache.put(type, getterMethods.toArray(new CachedMethod[getterMethods.size()]));
		setterMethodCache.put(type, setterMethods.toArray(new CachedMethod[setterMethods.size()]));
	}

	private CachedMethod[] getGetterMethods (Class<?> type) throws SerializationException {
		CachedMethod[] getterMethods = getterMethodCache.get(type);
		if (getterMethods != null) return getterMethods;
		cache(type);
		return getterMethodCache.get(type);
	}

	private CachedMethod[] getSetterMethods (Class<?> type) throws SerializationException {
		CachedMethod[] setterMethods = setterMethodCache.get(type);
		if (setterMethods != null) return setterMethods;
		cache(type);
		return setterMethodCache.get(type);
	}

	public void writeObjectData (ConnectionHandler connectionHandler, ByteBuffer buffer, Object object, boolean lengthKnown)
		throws SerializationException {
		Class<?> type = object.getClass();
		Object[] noArgs = new Object[0];
		try {
			CachedMethod[] getterMethods = getGetterMethods(type);
			for (int i = 0, n = getterMethods.length; i < n; i++) {
				CachedMethod cachedMethod = getterMethods[i];
				Object value = cachedMethod.method.invoke(object, noArgs);
				Serializer serializer = cachedMethod.serializer;
				if (serializer != null)
					serializer.writeObject(connectionHandler, value, buffer);
				else
					Network.writeClassAndObject(connectionHandler, value, buffer);
			}
		} catch (IllegalAccessException ex) {
			throw new SerializationException("Error accessing getter method in class: " + type.getName(), ex);
		} catch (InvocationTargetException ex) {
			throw new SerializationException("Error invoking getter method in class: " + type.getName(), ex);
		}
	}

	public <T> T readObjectData (ConnectionHandler connectionHandler, ByteBuffer buffer, Class<T> type, boolean lengthKnown)
		throws SerializationException {
		T object = newInstance(type);
		try {
			CachedMethod[] setterMethods = getSetterMethods(object.getClass());
			for (int i = 0, n = setterMethods.length; i < n; i++) {
				CachedMethod cachedMethod = setterMethods[i];
				Object value;
				Serializer serializer = cachedMethod.serializer;
				if (serializer != null)
					value = serializer.readObject(connectionHandler, buffer, cachedMethod.type);
				else
					value = Network.readClassAndObject(connectionHandler, buffer);
				cachedMethod.method.invoke(object, new Object[] {value});
			}
		} catch (IllegalAccessException ex) {
			throw new SerializationException("Error accessing setter method in class: " + type.getName(), ex);
		} catch (InvocationTargetException ex) {
			throw new SerializationException("Error invoking setter method in class: " + type.getName(), ex);
		}
		return object;
	}

	static class CachedMethod {
		public Method method;
		public Serializer serializer;
		public Class<?> type; // Only populated for setter methods.

		public String toString () {
			return method.getName();
		}
	}

	static public void put (ConnectionHandler connectionHandler, ByteBuffer buffer, Object object) throws SerializationException {
		instance.writeObjectData(connectionHandler, object, buffer);
	}

	static public <T> T get (ConnectionHandler connectionHandler, ByteBuffer buffer, Class<T> type) throws SerializationException {
		return instance.readObjectData(connectionHandler, buffer, type);
	}
}
