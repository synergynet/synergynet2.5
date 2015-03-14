package synergynetframework.appsystem.services.net.filestore.messages;

import java.io.Serializable;

public class FileTransferComplete implements Serializable {
	private static final long serialVersionUID = 5854560171625486843L;
	private String name;

	
	public FileTransferComplete(String name) {
		this.setName(name);
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getName() {
		return name;
	}


}
