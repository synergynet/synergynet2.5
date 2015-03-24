package apps.mtdesktop.desktop;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;

/**
 * The Class PositionDialog.
 */
public class PositionDialog extends JDialog implements ActionListener {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7322407154079850855L;

	/** The pet strings. */
	private String[] petStrings = { "North", "South", "East", "West" };

	/**
	 * Instantiates a new position dialog.
	 */
	@SuppressWarnings({})
	public PositionDialog() {
		this.setModal(true);
		this.setLayout(new FlowLayout());
		JLabel label = new JLabel("Position : ");

		JComboBox combo = new JComboBox(petStrings);

		if (DesktopClient.currentPosition.equals(DesktopClient.Position.NORTH)) {
			combo.setSelectedIndex(0);
		} else if (DesktopClient.currentPosition
				.equals(DesktopClient.Position.SOUTH)) {
			combo.setSelectedIndex(1);
		} else if (DesktopClient.currentPosition
				.equals(DesktopClient.Position.EAST)) {
			combo.setSelectedIndex(2);
		} else if (DesktopClient.currentPosition
				.equals(DesktopClient.Position.WEST)) {
			combo.setSelectedIndex(3);
		}
		combo.addActionListener(this);

		this.getContentPane().add(label);
		this.getContentPane().add(combo);
		this.setSize(300, 100);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int w = this.getSize().width;
		int h = this.getSize().height;
		int x = (dim.width - w) / 2;
		int y = (dim.height - h) / 2;
		
		this.setLocation(x, y);
		this.setVisible(true);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		JComboBox cb = (JComboBox) e.getSource();
		String position = (String) cb.getSelectedItem();
		if (position.equalsIgnoreCase("North")) {
			DesktopClient.currentPosition = DesktopClient.Position.NORTH;
		} else if (position.equalsIgnoreCase("South")) {
			DesktopClient.currentPosition = DesktopClient.Position.SOUTH;
		} else if (position.equalsIgnoreCase("East")) {
			DesktopClient.currentPosition = DesktopClient.Position.EAST;
		} else if (position.equalsIgnoreCase("West")) {
			DesktopClient.currentPosition = DesktopClient.Position.WEST;
		}
		Preferences.userRoot().put("client_position",
				DesktopClient.currentPosition.toString());
	}
}
