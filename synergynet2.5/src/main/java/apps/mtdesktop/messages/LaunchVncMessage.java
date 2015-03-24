package apps.mtdesktop.messages;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;

/**
 * The Class LaunchVncMessage.
 */
public class LaunchVncMessage extends ContentMessage {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3396456151986951311L;

	/** The ip address. */
	protected String ipAddress;

	/** The is enabled. */
	protected boolean isEnabled;

	/** The password. */
	protected String password;

	/** The port. */
	protected int port;

	/**
	 * Instantiates a new launch vnc message.
	 */
	public LaunchVncMessage() {
		super();
	}

	/**
	 * Instantiates a new launch vnc message.
	 *
	 * @param targetClass
	 *            the target class
	 * @param targetTableId
	 *            the target table id
	 * @param contentId
	 *            the content id
	 * @param ipAddress
	 *            the ip address
	 * @param port
	 *            the port
	 * @param password
	 *            the password
	 * @param isEnabled
	 *            the is enabled
	 */
	public LaunchVncMessage(Class<?> targetClass, TableIdentity targetTableId,
			String contentId, String ipAddress, int port, String password,
			boolean isEnabled) {
		super(targetClass, targetTableId, contentId);
		this.ipAddress = ipAddress;
		this.port = port;
		this.password = password;
		this.isEnabled = isEnabled;
	}
	
	/**
	 * Gets the ip address.
	 *
	 * @return the ip address
	 */
	public String getIpAddress() {
		return ipAddress;
	}
	
	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Gets the port.
	 *
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * Checks if is enabled.
	 *
	 * @return true, if is enabled
	 */
	public boolean isEnabled() {
		return isEnabled;
	}
	
	/**
	 * Sets the ip address.
	 *
	 * @param ipAddress
	 *            the new ip address
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	/**
	 * Sets the checks if is enabled.
	 *
	 * @param isEnabled
	 *            the new checks if is enabled
	 */
	public void setIsEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	/**
	 * Sets the password.
	 *
	 * @param password
	 *            the new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Sets the port.
	 *
	 * @param port
	 *            the new port
	 */
	public void setPort(int port) {
		this.port = port;
	}
}
