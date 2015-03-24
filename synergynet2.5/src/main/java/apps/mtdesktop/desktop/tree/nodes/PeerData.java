package apps.mtdesktop.desktop.tree.nodes;

import java.io.Serializable;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;


/**
 * The Class PeerData.
 */
public class PeerData implements Serializable{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5373639108455745863L;

	/** The peer name. */
	protected String peerName;
	
	/** The table id. */
	protected TableIdentity tableId;
	
	/**
	 * Instantiates a new peer data.
	 */
	public PeerData(){}
	
	/**
	 * Instantiates a new peer data.
	 *
	 * @param peerName the peer name
	 * @param tableId the table id
	 */
	public PeerData(String peerName, TableIdentity tableId){
		this.peerName = peerName;
		this.tableId = tableId;
	}
	
	/**
	 * Sets the table id.
	 *
	 * @param tableId the new table id
	 */
	public void setTableId(TableIdentity tableId){
		this.tableId = tableId;
	}
	
	/**
	 * Sets the peer name.
	 *
	 * @param peerName the new peer name
	 */
	public void setPeerName(String peerName){
		this.peerName = peerName;
	}
	
	/**
	 * Gets the table id.
	 *
	 * @return the table id
	 */
	public TableIdentity getTableId(){
		return tableId;
	}
	
	/**
	 * Gets the peer name.
	 *
	 * @return the peer name
	 */
	public String getPeerName(){
		return peerName;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return this.getPeerName();
	}
}
