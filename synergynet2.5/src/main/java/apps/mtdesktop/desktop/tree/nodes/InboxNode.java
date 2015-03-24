package apps.mtdesktop.desktop.tree.nodes;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;

import apps.mtdesktop.MTDesktopConfigurations;


/**
 * The Class InboxNode.
 */
public class InboxNode extends DefaultMutableTreeNode{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1560704399177419844L;
	
	/** The peer data. */
	protected PeerData peerData;

	/**
	 * Instantiates a new inbox node.
	 */
	public InboxNode (){
		  super("Inbox Node");
	}

	/**
	 * Instantiates a new inbox node.
	 *
	 * @param peerData the peer data
	 */
	public InboxNode(PeerData peerData){
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
		 return new ImageIcon(MTDesktopConfigurations.class.getResource("desktop/treeicons/inbox_icon.jpg"));
	 }
}
