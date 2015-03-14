package apps.mtdesktop.desktop;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class ProgressPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1827004695294779403L;
	private JLabel statusLabel;
	private JProgressBar progressBar;
	
	public ProgressPanel(){
		setLayout(new FlowLayout(FlowLayout.RIGHT));
		statusLabel = new JLabel("", JLabel.LEFT);
		this.add(statusLabel);

		progressBar = new JProgressBar();
		progressBar.setIndeterminate(false);
		this.add(progressBar);
	}
	
	public void setStatus(String status, boolean isBusy){
		statusLabel.setText(status);
		progressBar.setIndeterminate(isBusy);
	}
}
