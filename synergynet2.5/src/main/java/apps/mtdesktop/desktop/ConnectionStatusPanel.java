package apps.mtdesktop.desktop;

import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import apps.mtdesktop.MTDesktopConfigurations;
import apps.mtdesktop.desktop.DesktopClient.ConnectionStatus;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
	

/**
 * The Class ConnectionStatusPanel.
 */
public class ConnectionStatusPanel extends JPanel{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4267211627104351237L;
	
	/** The status label. */
	private JLabel statusLabel;
	
	/** The disconnected icon. */
	private Icon connectedIcon, disconnectedIcon;
	
	/**
	 * Instantiates a new connection status panel.
	 */
	public ConnectionStatusPanel(){
		setLayout(new FlowLayout(FlowLayout.LEFT));
		connectedIcon = new ImageIcon(MTDesktopConfigurations.class.getResource("desktop/icons/Connect.png"));
		disconnectedIcon = new ImageIcon(MTDesktopConfigurations.class.getResource("desktop/icons/Disconnect.png"));

		statusLabel = new JLabel("Disconnected", disconnectedIcon, JLabel.LEFT);
		this.add(statusLabel);
		this.setBorder(BorderFactory.createEtchedBorder());
	}
	
	/**
	 * Sets the connection status.
	 *
	 * @param status the new connection status
	 */
	public void setConnectionStatus(ConnectionStatus status){
		if(status.equals(ConnectionStatus.CONNECTED_TO_SERVER)){
			statusLabel.setText("Connected to the server ");
			statusLabel.setIcon(connectedIcon);
		}else	if(status.equals(ConnectionStatus.SEARCHING_FOR_TABLE)){
			statusLabel.setText("Searching for the tabletop ... ");
			statusLabel.setIcon(connectedIcon);
		}else if(status.equals(ConnectionStatus.CONNECTED_TO_TABLE)){
			statusLabel.setText("Connected");
			statusLabel.setIcon(connectedIcon);
		}else{
			statusLabel.setText("Disconnected");
			statusLabel.setIcon(disconnectedIcon);			
		}
	}
	
	/**
	 * Sets the connected table.
	 *
	 * @param tableId the new connected table
	 */
	public void setConnectedTable(TableIdentity tableId){
		statusLabel.setText("Connected to table : "+ tableId.toString());
	}
}
