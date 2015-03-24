package apps.mtdesktop.desktop;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import apps.mtdesktop.desktop.tree.nodes.AssetNode;





/**
 * The Class ControlPanel.
 */
public class ControlPanel extends JPanel implements TreeSelectionListener{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -686894565857115520L;
	
	/** The listeners. */
	private List<ControlPanelListener> listeners;
	
	/** The delete btn. */
	private JButton deleteBtn;
	
	/** The share desktop btn. */
	private JButton shareDesktopBtn;
	
	/** The is shared. */
	boolean isShared = false;
	
	/**
	 * Instantiates a new control panel.
	 */
	public ControlPanel(){
		listeners = new ArrayList<ControlPanelListener>();
		deleteBtn = new JButton("Delete");
		deleteBtn.setEnabled(false);
		deleteBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				for(ControlPanelListener listener: listeners)
					listener.deletePressed();
			}

		});
		this.add(deleteBtn);
		
		shareDesktopBtn = new JButton("Share Desktop");
		shareDesktopBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				isShared = !isShared;
				if(isShared)
					shareDesktopBtn.setText("Unshare Desktop");
				else
					shareDesktopBtn.setText("Share Desktop");
				for(ControlPanelListener listener: listeners)
					listener.shareDesktopPressed(isShared);
			}

		});
		this.add(shareDesktopBtn);
		this.setBorder(BorderFactory.createEtchedBorder());
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		TreePath[] paths = e.getPaths();
		for(TreePath path: paths){
			DefaultMutableTreeNode mNode = (DefaultMutableTreeNode)path.getLastPathComponent();
			if(!(mNode instanceof AssetNode) && e.isAddedPath(path)){
				deleteBtn.setEnabled(false);
				return;
			}
		}
		deleteBtn.setEnabled(true);
	}
	
	/**
	 * Adds the control panel listener.
	 *
	 * @param listener the listener
	 */
	public void addControlPanelListener(ControlPanelListener listener){
		if(!listeners.contains(listener)) 
			listeners.add(listener);
	}
	
	/**
	 * The listener interface for receiving controlPanel events.
	 * The class that is interested in processing a controlPanel
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addControlPanelListener<code> method. When
	 * the controlPanel event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see ControlPanelEvent
	 */
	public interface ControlPanelListener{
		
		/**
		 * Delete pressed.
		 */
		public void deletePressed();
		
		/**
		 * Share desktop pressed.
		 *
		 * @param share the share
		 */
		public void shareDesktopPressed(boolean share);
	}
}
