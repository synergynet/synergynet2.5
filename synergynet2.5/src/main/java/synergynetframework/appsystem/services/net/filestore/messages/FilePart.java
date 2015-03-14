package synergynetframework.appsystem.services.net.filestore.messages;

import java.io.Serializable;

public class FilePart implements Serializable {
	private static final long serialVersionUID = 850319486798684664L;
	
	private byte[] byteBuffer;
	private int len;

	public void setBytes(byte[] bytes, int len) {
		byteBuffer = new byte[len];
		for(int i = 0 ; i < len; i++) {
			byteBuffer[i] = bytes[i];
		}
		this.setLen(len);
	}

	public byte[] getByteBuffer() {
		return byteBuffer;
	}

	public void setLen(int len) {
		this.len = len;
	}

	public int getLen() {
		return len;
	}


}
