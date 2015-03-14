
package synergynetframework.appsystem.services.net.objectmessaging.utility.serializers;

import java.nio.ByteBuffer;

import synergynetframework.appsystem.services.net.objectmessaging.connections.ConnectionHandler;

public class LongSerializer extends Serializer {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Long readObjectData (ConnectionHandler connectionHandler, ByteBuffer buffer, Class type, boolean lengthKnown)
		throws SerializationException {
		long l = buffer.getLong();
		return l;
	}

	public void writeObjectData (ConnectionHandler connectionHandler, ByteBuffer buffer, Object object, boolean lengthKnown)
		throws SerializationException {
		buffer.putLong((Long)object);
	}
}
