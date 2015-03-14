package apps.mtdesktop.desktop.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import apps.mtdesktop.desktop.tree.TabletopTreeController;


public class OutboxNodePopUpMenu extends JPopupMenu{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5809360199329351914L;
	protected JMenuItem paste, delete;
	protected TabletopTreeController treeController;
	
	public OutboxNodePopUpMenu(final TabletopTreeController treeController){
		this.treeController = treeController;
		paste = new JMenuItem( "Paste Clipboard" );
		delete = new JMenuItem( "Delete Assets from table" );
		
		paste.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				treeController.processPaste();
			}
		});
		
		delete.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				treeController.processDelete();
			}
		});
		this.add( paste );
		this.add(delete);
	}
	
	public void update() {
		if(treeController.getClipboard().getContents(treeController.getClipboard()) == null) 
			paste.setEnabled(false);
		else
			paste.setEnabled(true);
	}
}
