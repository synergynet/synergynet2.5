package apps.mtdesktop.desktop.tree.nodes;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;

import apps.mtdesktop.MTDesktopConfigurations;

public class OutboxNode extends DefaultMutableTreeNode{
	 
	/**
	 * 
	 */
	
	
	private static final long serialVersionUID = -6987549845971451228L;

	protected PeerData peerData;
	
	public OutboxNode (){
		  super("Outbox Node");
	}
	
	public OutboxNode(PeerData peerData){
		this.peerData = peerData;
		this.setUserObject(peerData.getPeerName());
	}
	
	public PeerData getPeerData(){
		return peerData;
	}

	 public Icon getIcon(){
		 return new ImageIcon(MTDesktopConfigurations.class.getResource("desktop/treeicons/tabletop.png"));
	 }
}
