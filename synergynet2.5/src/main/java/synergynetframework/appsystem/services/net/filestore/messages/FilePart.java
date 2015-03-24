package synergynetframework.appsystem.services.net.filestore.messages;

import java.io.Serializable;

/**
 * The Class FilePart.
 */
public class FilePart implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 850319486798684664L;

	/** The byte buffer. */
	private byte[] byteBuffer;

	/** The len. */
	private int len;
	
	/**
	 * Gets the byte buffer.
	 *
	 * @return the byte buffer
	 */
	public byte[] getByteBuffer() {
		return byteBuffer;
	}
	
	/**
	 * Gets the len.
	 *
	 * @return the len
	 */
	public int getLen() {
		return len;
	}
	
	/**
	 * Sets the bytes.
	 *
	 * @param bytes
	 *            the bytes
	 * @param len
	 *            the len
	 */
	public void setBytes(byte[] bytes, int len) {
		byteBuffer = new byte[len];
		for (int i = 0; i < len; i++) {
			byteBuffer[i] = bytes[i];
		}
		this.setLen(len);
	}
	
	/**
	 * Sets the len.
	 *
	 * @param len
	 *            the new len
	 */
	public void setLen(int len) {
		this.len = len;
	}
	
}
