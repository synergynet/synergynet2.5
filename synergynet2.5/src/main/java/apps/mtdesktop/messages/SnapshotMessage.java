package apps.mtdesktop.messages;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.tablecomms.messages.application.UnicastApplicationMessage;


/**
 * The Class SnapshotMessage.
 */
public class SnapshotMessage extends UnicastApplicationMessage{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2918061014330431267L;

	/** The bytes. */
	private byte[] bytes;
	
	/**
	 * Instantiates a new snapshot message.
	 */
	public SnapshotMessage(){
		super();
	}
	
	/**
	 * Instantiates a new snapshot message.
	 *
	 * @param targetClass the target class
	 * @param targetTableId the target table id
	 * @param bytes the bytes
	 */
	public SnapshotMessage(Class<?> targetClass, TableIdentity targetTableId, byte[] bytes){
		super(targetClass);
		this.setRecipient(targetTableId);
		this.bytes = bytes;
	}
	
	/**
	 * Sets the image bytes.
	 *
	 * @param bytes the new image bytes
	 */
	public void setImageBytes(byte[] bytes){
		this.bytes = bytes;
	}
	
	/**
	 * Gets the image bytes.
	 *
	 * @return the image bytes
	 */
	public byte[] getImageBytes(){
		return bytes;
	}
}

