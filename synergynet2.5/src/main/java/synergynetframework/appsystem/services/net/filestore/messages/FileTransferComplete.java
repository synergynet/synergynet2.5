package synergynetframework.appsystem.services.net.filestore.messages;

import java.io.Serializable;

/**
 * The Class FileTransferComplete.
 */
public class FileTransferComplete implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5854560171625486843L;

	/** The name. */
	private String name;
	
	/**
	 * Instantiates a new file transfer complete.
	 *
	 * @param name
	 *            the name
	 */
	public FileTransferComplete(String name) {
		this.setName(name);
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name.
	 *
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
}
