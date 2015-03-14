package apps.mtdesktop.messages;

import java.awt.event.KeyEvent;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.tablecomms.messages.application.UnicastApplicationMessage;

public class KeyEventMessage extends UnicastApplicationMessage{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 832254747031925652L;
	private KeyEvent keyEvent;
	
	public KeyEventMessage(){
		super();
	}
	
	public KeyEventMessage(Class<?> targetClass, TableIdentity targetTableId, KeyEvent evt){
		super(targetClass);
		this.setRecipient(targetTableId);
		keyEvent = evt;
	}
	
	public void setKeyEvent(KeyEvent keyEvent){
		this.keyEvent = keyEvent;
	}

	public KeyEvent getKeyEvent(){
		return keyEvent;
	}

}
