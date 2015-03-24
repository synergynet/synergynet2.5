package apps.mtdesktop.desktop.tree;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTree;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import apps.mtdesktop.desktop.FileDragAndDropHandler;
import apps.mtdesktop.desktop.popup.InboxNodePopupMenu;
import apps.mtdesktop.desktop.popup.OutboxNodePopUpMenu;
import apps.mtdesktop.desktop.tree.nodes.AssetNode;
import apps.mtdesktop.desktop.tree.nodes.InboxNode;
import apps.mtdesktop.desktop.tree.nodes.OutboxNode;
import apps.mtdesktop.desktop.tree.nodes.PeerData;
import apps.mtdesktop.desktop.tree.nodes.TopNode;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;



/**
 * The Class TabletopTree.
 */
public class TabletopTree extends JTree implements KeyListener, MouseListener{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2736822653140337099L;

	/** The tree model. */
	protected DefaultTreeModel treeModel;
	
	/** The renderer. */
	protected CustomTreeCellRenderer renderer;
	
	/** The top. */
	protected TopNode top;
	
	/** The outbox popup. */
	protected OutboxNodePopUpMenu outboxPopup;
	
	/** The inbox popup. */
	protected InboxNodePopupMenu inboxPopup;
	
	/** The listeners. */
	protected List<DataTransferListener> listeners;
	
	/** The controller. */
	protected TabletopTreeController controller;
	
	/**
	 * Instantiates a new tabletop tree.
	 */
	public TabletopTree() { 
		super(); 
		BasicTreeUI basicTreeUI = (BasicTreeUI) this.getUI();
		basicTreeUI.setRightChildIndent(30);
		
		controller = new TabletopTreeController(this);
		top = new TopNode();
		treeModel = new DefaultTreeModel(top);
		this.setModel(treeModel);
		this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.setShowsRootHandles(true);
		this.setEditable(false);

		renderer = new CustomTreeCellRenderer();
		this.setCellRenderer(renderer);
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.setTransferHandler(new FileDragAndDropHandler());
		this.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		outboxPopup = new OutboxNodePopUpMenu(controller);
		this.add(outboxPopup);
		inboxPopup = new InboxNodePopupMenu(controller);
		this.add(inboxPopup);
		listeners = new ArrayList<DataTransferListener>();
		
	}
	
	/**
	 * Instantiates a new tabletop tree.
	 *
	 * @param newModel the new model
	 */
	public TabletopTree(TreeModel newModel) { super(newModel); }
	
	/**
	 * Instantiates a new tabletop tree.
	 *
	 * @param root the root
	 */
	public TabletopTree(TreeNode root) { super(root);  }
	
	/**
	 * Instantiates a new tabletop tree.
	 *
	 * @param root the root
	 * @param asks the asks
	 */
	public TabletopTree(TreeNode root, boolean asks) { super(root, asks); }

	/**
	 * Register table.
	 *
	 * @param peerData the peer data
	 * @return true, if successful
	 */
	public boolean registerTable(PeerData peerData) {
		if(this.getOutboxNode(peerData.getTableId()) != null && this.getInboxNode(peerData.getTableId()) != null){
			return false;
		}
		top.add(new OutboxNode(peerData));
		InboxNode inbox = new InboxNode(peerData);
		inbox.setUserObject("Inbox (0)");
		top.add(inbox);
		controller.updateInboxNode();
        this.expandRow(top.getLevel());
		return true;
	}
	
	/**
	 * Unregister table.
	 *
	 * @param tableId the table id
	 */
	public void unregisterTable(TableIdentity tableId) {
		OutboxNode outNode = this.getOutboxNode(tableId);
		if(outNode != null)
			((DefaultTreeModel) this.getModel()).removeNodeFromParent(outNode);
		InboxNode inNode = this.getInboxNode(tableId);
		if(inNode != null)
			((DefaultTreeModel) this.getModel()).removeNodeFromParent(inNode);
        this.expandRow(top.getLevel());
	}

	/**
	 * Gets the outbox node.
	 *
	 * @param tableId the table id
	 * @return the outbox node
	 */
	public OutboxNode getOutboxNode(TableIdentity tableId){
		for(int i=0; i< top.getChildCount(); i++){
			if(top.getChildAt(i) != null && top.getChildAt(i) instanceof OutboxNode && ((OutboxNode)top.getChildAt(i)).getPeerData().getTableId().equals(tableId))
				return ((OutboxNode)top.getChildAt(i));
		}
		return null;
	}
	
	/**
	 * Gets the inbox node.
	 *
	 * @param tableId the table id
	 * @return the inbox node
	 */
	public InboxNode getInboxNode(TableIdentity tableId){
		for(int i=0; i< top.getChildCount(); i++){
			if(top.getChildAt(i) != null && top.getChildAt(i) instanceof InboxNode && ((InboxNode)top.getChildAt(i)).getPeerData().getTableId().equals(tableId))
				return ((InboxNode)top.getChildAt(i));
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	public void keyReleased(KeyEvent e) {}
	
	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	public void keyTyped(KeyEvent e) {}
	
	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_DELETE){
			TreePath[] paths = this.getSelectionPaths();
			if(paths == null || paths.length == 0) return;
			for(TreePath path: paths){
				MutableTreeNode mNode = (MutableTreeNode)path.getLastPathComponent();
				if(!(mNode instanceof AssetNode)) return;
			}
			controller.deleteSelected(paths);
		}else if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_V){
	    	DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) this.getSelectionPath().getLastPathComponent();
	    	if(selectedNode instanceof OutboxNode){
	    		controller.setSelectedNode(selectedNode);
	    		outboxPopup.update();
	    		controller.processPaste();
	    	}
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e) {
	    if (e.isPopupTrigger()) {
	    	if(this.getSelectionPaths() == null || this.getSelectionPaths().length == 0) 
	    		return;
	    	DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) this.getSelectionPath().getLastPathComponent();
	    	if(selectedNode instanceof OutboxNode){
	    		controller.setSelectedNode(selectedNode);
	    		outboxPopup.update();
	    		outboxPopup.show((Component)e.getSource(), e.getX(), e.getY());
	    	}else if(selectedNode instanceof InboxNode){
	    		inboxPopup.setSelectedNode(selectedNode);
	    		inboxPopup.show((Component)e.getSource(), e.getX(), e.getY());	    		
	    	}
	    }
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
	    if (e.isPopupTrigger()) {
	    	if(this.getSelectionPaths() == null || this.getSelectionPaths().length == 0) 
	    		return;
	    	DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) this.getSelectionPath().getLastPathComponent();
	    	if(selectedNode instanceof OutboxNode){
	    		controller.setSelectedNode(selectedNode);
	    		outboxPopup.update();
	    		outboxPopup.show((Component)e.getSource(), e.getX(), e.getY());
	    	}else if(selectedNode instanceof InboxNode){
	    		inboxPopup.setSelectedNode(selectedNode);
	    		inboxPopup.show((Component)e.getSource(), e.getX(), e.getY());	    		
	    	}
	    }
	}
	
	/**
	 * The listener interface for receiving dataTransfer events.
	 * The class that is interested in processing a dataTransfer
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addDataTransferListener<code> method. When
	 * the dataTransfer event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see DataTransferEvent
	 */
	public interface DataTransferListener{
		
		/**
		 * Object transfered.
		 *
		 * @param object the object
		 * @param tableId the table id
		 */
		public void objectTransfered(Object object, TableIdentity tableId);
	}

	/**
	 * Gets the controller.
	 *
	 * @return the controller
	 */
	public TabletopTreeController getController(){
		return controller;
	}
}
