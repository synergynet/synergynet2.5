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


package synergynetframework.appsystem.services.net.objectmessaging;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import synergynetframework.appsystem.services.net.objectmessaging.connections.ConnectionHandler;
import synergynetframework.appsystem.services.net.objectmessaging.messages.frameworkmessages.KeepAlive;
import synergynetframework.appsystem.services.net.objectmessaging.messages.frameworkmessages.RegisterTCP;
import synergynetframework.appsystem.services.net.objectmessaging.messages.frameworkmessages.RegisterUDP;
import synergynetframework.appsystem.services.net.objectmessaging.utility.serializers.ArraySerializer;
import synergynetframework.appsystem.services.net.objectmessaging.utility.serializers.BooleanSerializer;
import synergynetframework.appsystem.services.net.objectmessaging.utility.serializers.ByteSerializer;
import synergynetframework.appsystem.services.net.objectmessaging.utility.serializers.CharSerializer;
import synergynetframework.appsystem.services.net.objectmessaging.utility.serializers.CollectionSerializer;
import synergynetframework.appsystem.services.net.objectmessaging.utility.serializers.CustomSerialization;
import synergynetframework.appsystem.services.net.objectmessaging.utility.serializers.CustomSerializer;
import synergynetframework.appsystem.services.net.objectmessaging.utility.serializers.DoubleSerializer;
import synergynetframework.appsystem.services.net.objectmessaging.utility.serializers.EnumSerializer;
import synergynetframework.appsystem.services.net.objectmessaging.utility.serializers.FieldSerializer;
import synergynetframework.appsystem.services.net.objectmessaging.utility.serializers.FloatSerializer;
import synergynetframework.appsystem.services.net.objectmessaging.utility.serializers.IntSerializer;
import synergynetframework.appsystem.services.net.objectmessaging.utility.serializers.LongSerializer;
import synergynetframework.appsystem.services.net.objectmessaging.utility.serializers.MapSerializer;
import synergynetframework.appsystem.services.net.objectmessaging.utility.serializers.SerializableSerializer;
import synergynetframework.appsystem.services.net.objectmessaging.utility.serializers.SerializationException;
import synergynetframework.appsystem.services.net.objectmessaging.utility.serializers.Serializer;
import synergynetframework.appsystem.services.net.objectmessaging.utility.serializers.ShortSerializer;
import synergynetframework.appsystem.services.net.objectmessaging.utility.serializers.StringSerializer;

public class Network {
	static private final HashMap<Short,RegisteredClass> idToRegisteredClass = new HashMap<Short, RegisteredClass>();
	static private final HashMap<Class<?>, RegisteredClass> classToRegisteredClass = new HashMap<Class<?>,RegisteredClass>();
	static private final short CLASS_NAME = -1;
	static private final byte ID_NULL_OBJECT = 0;
	static private short nextClassID = 1;

	static private final FieldSerializer fieldSerializer = new FieldSerializer();
	static private final CustomSerializer customSerializer = new CustomSerializer();
	static private final ArraySerializer arraySerializer = new ArraySerializer();
	static private final EnumSerializer enumSerializer = new EnumSerializer();
	static private final CollectionSerializer collectionSerializer = new CollectionSerializer();
	static private final MapSerializer mapSerializer = new MapSerializer();
	static private final SerializableSerializer serializableSerializer = new SerializableSerializer();

	static {

		register(boolean.class, nextClassID++, new BooleanSerializer()).setCanBeNull(false);
		register(byte.class, nextClassID++, new ByteSerializer()).setCanBeNull(false);
		register(char.class, nextClassID++, new CharSerializer()).setCanBeNull(false);
		register(short.class, nextClassID++, new ShortSerializer()).setCanBeNull(false);
		register(int.class, nextClassID++, new IntSerializer()).setCanBeNull(false);
		register(long.class, nextClassID++, new LongSerializer()).setCanBeNull(false);
		register(float.class, nextClassID++, new FloatSerializer()).setCanBeNull(false);
		register(double.class, nextClassID++, new DoubleSerializer()).setCanBeNull(false);

		register(Boolean.class, nextClassID++, new BooleanSerializer());
		register(Byte.class, nextClassID++, new ByteSerializer());
		register(Character.class, nextClassID++, new CharSerializer());
		register(Short.class, nextClassID++, new ShortSerializer());
		register(Integer.class, nextClassID++, new IntSerializer());
		register(Long.class, nextClassID++, new LongSerializer());
		register(Float.class, nextClassID++, new FloatSerializer());
		register(Double.class, nextClassID++, new DoubleSerializer());

		register(String.class, nextClassID++, new StringSerializer());

		register(RegisterTCP.class, nextClassID++, fieldSerializer);
		register(RegisterUDP.class, nextClassID++, fieldSerializer);
		register(KeepAlive.class, nextClassID++, fieldSerializer);
	}

	public static HashMap<Short, RegisteredClass> getRegisteredClasses(){
		return idToRegisteredClass;
	}
	
	private static Serializer register (Class<?> type, short id, Serializer serializer) {
		RegisteredClass registeredClass = new RegisteredClass();
		registeredClass.type = type;
		registeredClass.id = id;
		registeredClass.serializer = serializer;
		idToRegisteredClass.put(id, registeredClass);
		classToRegisteredClass.put(type, registeredClass);
		return serializer;
	}

	public static void register (Class<?> type, Serializer serializer) {
		if (type == null) throw new IllegalArgumentException("type cannot be null.");
		if (serializer == null) throw new IllegalArgumentException("serializer cannot be null.");
		short id;
		RegisteredClass existingRegisteredClass = classToRegisteredClass.get(type);
		if (existingRegisteredClass != null)
			id = existingRegisteredClass.id;
		else
			id = nextClassID++;
		register(type, id, serializer);
	}


	public static void register (Class<?> type) {
		if (type == null) throw new IllegalArgumentException("type cannot be null.");
		Serializer serializer;
		if (type.isArray())
			serializer = arraySerializer;
		else if (CustomSerialization.class.isAssignableFrom(type))
			serializer = customSerializer;
		else if (Collection.class.isAssignableFrom(type))
			serializer = collectionSerializer;
		else if (Map.class.isAssignableFrom(type))
			serializer = mapSerializer;
		else if (Enum.class.isAssignableFrom(type))
			serializer = enumSerializer;
		else {
			serializer = fieldSerializer;
		}
		register(type, serializer);
	}

	public static  RegisteredClass getRegisteredClass (Class<?> type) {
		if (type == null) throw new IllegalArgumentException("type cannot be null.");
		RegisteredClass registeredClass = classToRegisteredClass.get(type);
		if (registeredClass == null) {
			if (Proxy.isProxyClass(type)) return getRegisteredClass(InvocationHandler.class);
			if (type.isArray()) {
				ArraySerializer.getElementClass(type);
				StringBuilder buffer = new StringBuilder(16);
				for (int i = 0, n = ArraySerializer.getDimensionCount(type); i < n; i++)
					buffer.append("[]");
			}
		}
		return registeredClass;
	}

	public static RegisteredClass getRegisteredClass (short classID) {
		RegisteredClass registeredClass = idToRegisteredClass.get(classID);
		if (registeredClass == null) throw new IllegalArgumentException("Class ID is not registered: " + classID);
		return registeredClass;
	}

	public static  class RegisteredClass {
		public Class<?> type;
		public short id;
		public Serializer serializer;
	}

	public static  RegisteredClass writeClass (Class<?> type, ByteBuffer buffer) throws SerializationException {
		RegisteredClass registeredClass = getRegisteredClass(type);
		if(registeredClass == null){ 
			ShortSerializer.put(buffer, Network.CLASS_NAME, true);			
			StringSerializer.put(buffer, type.getName());
			registeredClass = new RegisteredClass();
			registeredClass.id = Network.CLASS_NAME;
			registeredClass.serializer = serializableSerializer;
			registeredClass.type = type;
		}
		else
			ShortSerializer.put(buffer, registeredClass.id, true);
		return registeredClass;
	}

	public static  RegisteredClass readClass (ByteBuffer buffer) throws SerializationException {
		short classID = ShortSerializer.get(buffer, true);
		RegisteredClass registeredClass = null;
		if (classID == ID_NULL_OBJECT) {
			return null;
		}else if(classID == Network.CLASS_NAME){
			registeredClass = new RegisteredClass();
			try {
				registeredClass.type = Class.forName(StringSerializer.get(buffer));
				registeredClass.id = CLASS_NAME;
				registeredClass.serializer = serializableSerializer;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		else	
			registeredClass = idToRegisteredClass.get(classID);
		if (registeredClass == null) throw new SerializationException("Encountered unregistered class ID: " + classID);
		return registeredClass;
	}

	public static  void writeClassAndObject (ConnectionHandler connectionHandler, Object object, ByteBuffer writeBuffer)
		throws SerializationException {
		writeClassAndObject(connectionHandler, object, writeBuffer, false);
	}

	public static  void writeClassAndObject (ConnectionHandler connectionHandler, Object object, ByteBuffer writeBuffer, boolean lengthKnown)
		throws SerializationException {
		if (object == null) {
			writeBuffer.put(ID_NULL_OBJECT);
			return;
		}
		try {
			RegisteredClass registeredClass = writeClass(object.getClass(), writeBuffer);
			registeredClass.serializer.writeObjectData(connectionHandler, writeBuffer, object, lengthKnown);
		} catch (SerializationException ex) {
			throw new SerializationException("Unable to serialize object of type: " + object.getClass().getName(), ex);
		}
	}

	public static  Object readClassAndObject (ConnectionHandler connectionHandler, ByteBuffer readBuffer) throws SerializationException {
		return readClassAndObject(connectionHandler, readBuffer, false);
	}

	static public Object readClassAndObject (ConnectionHandler connectionHandler, ByteBuffer readBuffer, boolean lengthKnown)
		throws SerializationException {
		RegisteredClass registeredClass = null;
		try {
			registeredClass = readClass(readBuffer);
			if (registeredClass == null) return null;
			return registeredClass.serializer.readObjectData(connectionHandler, readBuffer, registeredClass.type, lengthKnown);
		} catch (SerializationException ex) {
			if (registeredClass != null)
				throw new SerializationException("Unable to deserialize object of type: " + registeredClass.type.getName(), ex);
			throw new SerializationException("Unable to deserialize an object.", ex);
		}
	}
}
