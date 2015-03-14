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


public class TabletopTree extends JTree implements KeyListener, MouseListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2736822653140337099L;

	protected DefaultTreeModel treeModel;
	protected CustomTreeCellRenderer renderer;
	protected TopNode top;
	protected OutboxNodePopUpMenu outboxPopup;
	protected InboxNodePopupMenu inboxPopup;
	protected List<DataTransferListener> listeners;
	protected TabletopTreeController controller;
	
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
	public TabletopTree(TreeModel newModel) { super(newModel); }
	public TabletopTree(TreeNode root) { super(root);  }
	public TabletopTree(TreeNode root, boolean asks) { super(root, asks); }

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
	
	public void unregisterTable(TableIdentity tableId) {
		OutboxNode outNode = this.getOutboxNode(tableId);
		if(outNode != null)
			((DefaultTreeModel) this.getModel()).removeNodeFromParent(outNode);
		InboxNode inNode = this.getInboxNode(tableId);
		if(inNode != null)
			((DefaultTreeModel) this.getModel()).removeNodeFromParent(inNode);
        this.expandRow(top.getLevel());
	}

	public OutboxNode getOutboxNode(TableIdentity tableId){
		for(int i=0; i< top.getChildCount(); i++){
			if(top.getChildAt(i) != null && top.getChildAt(i) instanceof OutboxNode && ((OutboxNode)top.getChildAt(i)).getPeerData().getTableId().equals(tableId))
				return ((OutboxNode)top.getChildAt(i));
		}
		return null;
	}
	
	public InboxNode getInboxNode(TableIdentity tableId){
		for(int i=0; i< top.getChildCount(); i++){
			if(top.getChildAt(i) != null && top.getChildAt(i) instanceof InboxNode && ((InboxNode)top.getChildAt(i)).getPeerData().getTableId().equals(tableId))
				return ((InboxNode)top.getChildAt(i));
		}
		return null;
	}
	
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
	
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

	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
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
	
	public interface DataTransferListener{
		public void objectTransfered(Object object, TableIdentity tableId);
	}

	public TabletopTreeController getController(){
		return controller;
	}
}
