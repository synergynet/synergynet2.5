package apps.mtdesktop.desktop.tree.nodes;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;

import apps.mtdesktop.MTDesktopConfigurations;


/**
 * The Class OutboxNode.
 */
public class OutboxNode extends DefaultMutableTreeNode{
	 
	/** The Constant serialVersionUID. */
	
	
	private static final long serialVersionUID = -6987549845971451228L;

	/** The peer data. */
	protected PeerData peerData;
	
	/**
	 * Instantiates a new outbox node.
	 */
	public OutboxNode (){
		  super("Outbox Node");
	}
	
	/**
	 * Instantiates a new outbox node.
	 *
	 * @param peerData the peer data
	 */
	public OutboxNode(PeerData peerData){
		this.peerData = peerData;
		this.setUserObject(peerData.getPeerName());
	}
	
	/**
	 * Gets the peer data.
	 *
	 * @return the peer data
	 */
	public PeerData getPeerData(){
		return peerData;
	}

	 /**
 	 * Gets the icon.
 	 *
 	 * @return the icon
 	 */
 	public Icon getIcon(){
		 return new ImageIcon(MTDesktopConfigurations.class.getResource("desktop/treeicons/tabletop.png"));
	 }
}
