
package synergynetframework.appsystem.services.net.objectmessaging.utility.serializers;

import java.nio.ByteBuffer;

import synergynetframework.appsystem.services.net.objectmessaging.connections.ConnectionHandler;

public class EnumSerializer extends Serializer {
	static private final EnumSerializer instance = new EnumSerializer();

	public <T> T readObjectData (ConnectionHandler connectionHandler, ByteBuffer buffer, Class<T> type, boolean lengthKnown)
		throws SerializationException {
		T[] enumConstants = type.getEnumConstants();
		if (enumConstants == null) throw new SerializationException("Class is not an enum: " + type.getName());
		int ordinal = IntSerializer.get(buffer, true);
		if (ordinal < 0 || ordinal > enumConstants.length - 1)
			throw new SerializationException("Invalid ordinal for enum \"" + type.getName() + "\": " + ordinal);
		return enumConstants[ordinal];
	}

	@SuppressWarnings({"rawtypes" })
	public void writeObjectData (ConnectionHandler connectionHandler, ByteBuffer buffer, Object object, boolean lengthKnown)
		throws SerializationException {
		IntSerializer.put(buffer, ((Enum)object).ordinal(), true);
	}

	static public void put (ByteBuffer buffer, Enum<?> value) throws SerializationException {
		instance.writeObjectData(null, value, buffer);
	}

	static public Enum<?> get (ByteBuffer buffer) throws SerializationException {
		return instance.readObjectData(null, buffer, null);
	}
}
