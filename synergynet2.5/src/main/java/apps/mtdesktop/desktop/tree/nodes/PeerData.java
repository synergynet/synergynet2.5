package apps.mtdesktop.desktop.tree.nodes;

import java.io.Serializable;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;

public class PeerData implements Serializable{
	private static final long serialVersionUID = -5373639108455745863L;

	protected String peerName;
	protected TableIdentity tableId;
	
	public PeerData(){}
	
	public PeerData(String peerName, TableIdentity tableId){
		this.peerName = peerName;
		this.tableId = tableId;
	}
	
	public void setTableId(TableIdentity tableId){
		this.tableId = tableId;
	}
	
	public void setPeerName(String peerName){
		this.peerName = peerName;
	}
	
	public TableIdentity getTableId(){
		return tableId;
	}
	
	public String getPeerName(){
		return peerName;
	}
	
	public String toString(){
		return this.getPeerName();
	}
}
