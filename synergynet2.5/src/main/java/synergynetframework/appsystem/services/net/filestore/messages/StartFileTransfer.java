package synergynetframework.appsystem.services.net.filestore.messages;

import java.io.Serializable;

public class StartFileTransfer implements Serializable {
	private static final long serialVersionUID = 2461427440498162930L;
	private String fileName;
	private String MD5;
	
	public void setMD5(String mD5) {
		MD5 = mD5;
	}
	public String getMD5() {
		return MD5;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileName() {
		return fileName;
	}


}
