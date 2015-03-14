package apps.mtdesktop.messages;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;

public class LaunchVncMessage extends ContentMessage{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3396456151986951311L;
	protected String ipAddress;
	protected int port;
	protected String password;
	protected boolean isEnabled;
	
	public LaunchVncMessage(){
		super();
	}
	
	public LaunchVncMessage(Class<?> targetClass, TableIdentity targetTableId, String contentId, String ipAddress, int port, String password, boolean isEnabled){
		super(targetClass, targetTableId, contentId);
		this.ipAddress = ipAddress;
		this.port = port;
		this.password = password;
		this.isEnabled = isEnabled;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setIsEnabled(boolean isEnabled){
		this.isEnabled = isEnabled;
	}
	
	public boolean isEnabled(){
		return isEnabled;
	}
}
