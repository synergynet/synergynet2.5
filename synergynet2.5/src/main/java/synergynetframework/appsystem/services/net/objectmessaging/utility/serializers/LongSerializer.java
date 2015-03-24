package synergynetframework.appsystem.services.net.objectmessaging.utility.serializers;

import java.nio.ByteBuffer;

import synergynetframework.appsystem.services.net.objectmessaging.connections.ConnectionHandler;

/**
 * The Class LongSerializer.
 */
public class LongSerializer extends Serializer {

	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.services.net.objectmessaging.utility.
	 * serializers
	 * .Serializer#readObjectData(synergynetframework.appsystem.services
	 * .net.objectmessaging.connections.ConnectionHandler, java.nio.ByteBuffer,
	 * java.lang.Class, boolean)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Long readObjectData(ConnectionHandler connectionHandler,
			ByteBuffer buffer, Class type, boolean lengthKnown)
			throws SerializationException {
		long l = buffer.getLong();
		return l;
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
		buffer.putLong((Long) object);
	}
}
