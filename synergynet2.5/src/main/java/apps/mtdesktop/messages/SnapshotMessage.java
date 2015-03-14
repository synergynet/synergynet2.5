package apps.mtdesktop.messages;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.tablecomms.messages.application.UnicastApplicationMessage;

public class SnapshotMessage extends UnicastApplicationMessage{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2918061014330431267L;

	private byte[] bytes;
	
	public SnapshotMessage(){
		super();
	}
	
	public SnapshotMessage(Class<?> targetClass, TableIdentity targetTableId, byte[] bytes){
		super(targetClass);
		this.setRecipient(targetTableId);
		this.bytes = bytes;
	}
	
	public void setImageBytes(byte[] bytes){
		this.bytes = bytes;
	}
	
	public byte[] getImageBytes(){
		return bytes;
	}
}

