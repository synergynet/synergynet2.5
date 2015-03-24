
package synergynetframework.appsystem.services.net.objectmessaging.utility.serializers;

import java.nio.ByteBuffer;

import synergynetframework.appsystem.services.net.objectmessaging.connections.ConnectionHandler;


/**
 * The Class EnumSerializer.
 */
public class EnumSerializer extends Serializer {
	
	/** The Constant instance. */
	static private final EnumSerializer instance = new EnumSerializer();

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.services.net.objectmessaging.utility.serializers.Serializer#readObjectData(synergynetframework.appsystem.services.net.objectmessaging.connections.ConnectionHandler, java.nio.ByteBuffer, java.lang.Class, boolean)
	 */
	public <T> T readObjectData (ConnectionHandler connectionHandler, ByteBuffer buffer, Class<T> type, boolean lengthKnown)
		throws SerializationException {
		T[] enumConstants = type.getEnumConstants();
		if (enumConstants == null) throw new SerializationException("Class is not an enum: " + type.getName());
		int ordinal = IntSerializer.get(buffer, true);
		if (ordinal < 0 || ordinal > enumConstants.length - 1)
			throw new SerializationException("Invalid ordinal for enum \"" + type.getName() + "\": " + ordinal);
		return enumConstants[ordinal];
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.services.net.objectmessaging.utility.serializers.Serializer#writeObjectData(synergynetframework.appsystem.services.net.objectmessaging.connections.ConnectionHandler, java.nio.ByteBuffer, java.lang.Object, boolean)
	 */
	@SuppressWarnings({"rawtypes" })
	public void writeObjectData (ConnectionHandler connectionHandler, ByteBuffer buffer, Object object, boolean lengthKnown)
		throws SerializationException {
		IntSerializer.put(buffer, ((Enum)object).ordinal(), true);
	}

	/**
	 * Put.
	 *
	 * @param buffer the buffer
	 * @param value the value
	 * @throws SerializationException the serialization exception
	 */
	static public void put (ByteBuffer buffer, Enum<?> value) throws SerializationException {
		instance.writeObjectData(null, value, buffer);
	}

	/**
	 * Gets the.
	 *
	 * @param buffer the buffer
	 * @return the enum
	 * @throws SerializationException the serialization exception
	 */
	static public Enum<?> get (ByteBuffer buffer) throws SerializationException {
		return instance.readObjectData(null, buffer, null);
	}
}
