package apps.mtdesktop.desktop.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import apps.mtdesktop.desktop.tree.TabletopTreeController;

/**
 * The Class OutboxNodePopUpMenu.
 */
public class OutboxNodePopUpMenu extends JPopupMenu {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5809360199329351914L;

	/** The delete. */
	protected JMenuItem paste, delete;

	/** The tree controller. */
	protected TabletopTreeController treeController;

	/**
	 * Instantiates a new outbox node pop up menu.
	 *
	 * @param treeController
	 *            the tree controller
	 */
	public OutboxNodePopUpMenu(final TabletopTreeController treeController) {
		this.treeController = treeController;
		paste = new JMenuItem("Paste Clipboard");
		delete = new JMenuItem("Delete Assets from table");

		paste.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				treeController.processPaste();
			}
		});

		delete.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				treeController.processDelete();
			}
		});
		this.add(paste);
		this.add(delete);
	}

	/**
	 * Update.
	 */
	public void update() {
		if (treeController.getClipboard().getContents(
				treeController.getClipboard()) == null) {
			paste.setEnabled(false);
		} else {
			paste.setEnabled(true);
		}
	}
}
