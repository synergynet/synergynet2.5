package apps.mtdesktop.desktop.popup;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import apps.mtdesktop.MTDesktopConfigurations;
import apps.mtdesktop.desktop.tree.TabletopTreeController;



public class InboxNodePopupMenu extends JPopupMenu{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5809360199329351914L;
	protected DefaultMutableTreeNode selectedNode;
	protected JMenuItem open, refresh, delete;
	protected TabletopTreeController treeController;
	
	public InboxNodePopupMenu(final TabletopTreeController treeController){
		this.treeController = treeController;
		open = new JMenuItem( "Open Inbox Folder" );
		refresh = new JMenuItem( "Refresh" );
		delete = new JMenuItem( "Delete inbox files" );
		
		open.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					Desktop.getDesktop().open(new File(MTDesktopConfigurations.inboxFolder));
				} catch (IOException e) {
					JOptionPane.showMessageDialog(InboxNodePopupMenu.this, e.getMessage().toString(), "Unable to open folder", JOptionPane.ERROR_MESSAGE);
				}
			}

		});
		
		delete.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(selectedNode != null){
					TreePath[] paths = new TreePath[selectedNode.getChildCount()];
					for(int i=0;i< selectedNode.getChildCount(); i++)
						paths[i] = new TreePath(selectedNode.getChildAt(i));
					treeController.deleteSelected(paths);
				}
			}
			
		});
		
		refresh.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				treeController.updateInboxNode();
			}
			
		});
		
		delete.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				File dir = new File(MTDesktopConfigurations.inboxFolder); 
				File[] files = dir.listFiles();

		        for (int i = 0; i < files.length; i++){
		                files[i].delete();
		        }
		        treeController.updateInboxNode();
			}
			
		});
		
		this.add( open );
		this.add( refresh );
		this.add(delete);
	}
	
	public void setSelectedNode(DefaultMutableTreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}
}

