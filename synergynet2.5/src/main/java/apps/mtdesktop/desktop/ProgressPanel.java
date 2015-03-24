package apps.mtdesktop.desktop;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;


/**
 * The Class ProgressPanel.
 */
public class ProgressPanel extends JPanel{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1827004695294779403L;
	
	/** The status label. */
	private JLabel statusLabel;
	
	/** The progress bar. */
	private JProgressBar progressBar;
	
	/**
	 * Instantiates a new progress panel.
	 */
	public ProgressPanel(){
		setLayout(new FlowLayout(FlowLayout.RIGHT));
		statusLabel = new JLabel("", JLabel.LEFT);
		this.add(statusLabel);

		progressBar = new JProgressBar();
		progressBar.setIndeterminate(false);
		this.add(progressBar);
	}
	
	/**
	 * Sets the status.
	 *
	 * @param status the status
	 * @param isBusy the is busy
	 */
	public void setStatus(String status, boolean isBusy){
		statusLabel.setText(status);
		progressBar.setIndeterminate(isBusy);
	}
}
