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




public class ControlPanel extends JPanel implements TreeSelectionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = -686894565857115520L;
	
	private List<ControlPanelListener> listeners;
	private JButton deleteBtn;
	private JButton shareDesktopBtn;
	boolean isShared = false;
	
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
	
	public void addControlPanelListener(ControlPanelListener listener){
		if(!listeners.contains(listener)) 
			listeners.add(listener);
	}
	
	public interface ControlPanelListener{
		public void deletePressed();
		public void shareDesktopPressed(boolean share);
	}
}
