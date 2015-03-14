package apps.mtdesktop.desktop.tree.nodes;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;

import apps.mtdesktop.MTDesktopConfigurations;

public class InboxNode extends DefaultMutableTreeNode{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1560704399177419844L;
	protected PeerData peerData;

	public InboxNode (){
		  super("Inbox Node");
	}

	public InboxNode(PeerData peerData){
		this.peerData = peerData;
		this.setUserObject(peerData.getPeerName());
	}
	
	public PeerData getPeerData(){
		return peerData;
	}
	
	 public Icon getIcon(){
		 return new ImageIcon(MTDesktopConfigurations.class.getResource("desktop/treeicons/inbox_icon.jpg"));
	 }
}
