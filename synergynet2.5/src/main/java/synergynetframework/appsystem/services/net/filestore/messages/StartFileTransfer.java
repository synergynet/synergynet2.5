package synergynetframework.appsystem.services.net.filestore.messages;

import java.io.Serializable;

/**
 * The Class StartFileTransfer.
 */
public class StartFileTransfer implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2461427440498162930L;

	/** The file name. */
	private String fileName;

	/** The M d5. */
	private String MD5;

	/**
	 * Gets the file name.
	 *
	 * @return the file name
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Gets the m d5.
	 *
	 * @return the m d5
	 */
	public String getMD5() {
		return MD5;
	}

	/**
	 * Sets the file name.
	 *
	 * @param fileName
	 *            the new file name
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Sets the m d5.
	 *
	 * @param mD5
	 *            the new m d5
	 */
	public void setMD5(String mD5) {
		MD5 = mD5;
	}
	
}
