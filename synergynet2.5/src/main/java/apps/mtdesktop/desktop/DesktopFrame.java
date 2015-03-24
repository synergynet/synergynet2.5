package apps.mtdesktop.desktop;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import apps.mtdesktop.MTDesktopConfigurations;
import apps.mtdesktop.desktop.ControlPanel.ControlPanelListener;
import apps.mtdesktop.desktop.DesktopClient.ConnectionStatus;
import apps.mtdesktop.desktop.DesktopClient.mtdesktopNetworkListener;
import apps.mtdesktop.desktop.tablemonitor.TableRadar;
import apps.mtdesktop.desktop.tree.TabletopTree;
import apps.mtdesktop.desktop.tree.nodes.PeerData;
import apps.mtdesktop.fileutility.AssetRegistry;
import apps.mtdesktop.fileutility.FileTransferListener;
import apps.mtdesktop.messages.LaunchVncMessage;
import apps.mtdesktop.messages.ShowMultiPadMessage;
import apps.mtdesktop.tabletop.MTTableClient;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.RapidNetworkManager;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;



/**
 * The Class DesktopFrame.
 */
public class DesktopFrame extends JFrame implements mtdesktopNetworkListener, FileTransferListener, ControlPanelListener{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The tree. */
	protected TabletopTree tree;
	
	/** The status panel. */
	protected ConnectionStatusPanel statusPanel;
	
	/** The control panel. */
	protected ControlPanel controlPanel;
	
	/** The progress panel. */
	protected ProgressPanel progressPanel;
	
	/** The menu bar. */
	protected JMenuBar menuBar;
	
	/** The menu. */
	protected JMenu menu;
	
	/** The menu2. */
	protected JMenu menu2;
	//protected JMenuItem menuItem1;
	/** The menu item2. */
	protected JMenuItem menuItem2;
	
	/** The menu item3. */
	protected JMenuItem menuItem3;
	
	/** The menu item4. */
	protected JMenuItem menuItem4;
	
	/** The menu item5. */
	protected JMenuItem menuItem5;
	
	/** The table radar. */
	private TableRadar tableRadar = null;

	/**
	 * Instantiates a new desktop frame.
	 *
	 * @param app the app
	 */
	public DesktopFrame(final DefaultSynergyNetApp app){
		super("Users");
		
		menuBar = new JMenuBar();
		menu = new JMenu("File");
		//menuItem1 = new JMenuItem("Test MTMouse");
		//menu.add(menuItem1);
		menuItem2 = new JMenuItem("Watch table");
		menu.add(menuItem2);
		menuItem3 =	new JCheckBoxMenuItem("Shared Notepad");
		menu.add(menuItem3);
		menuItem5 = new JMenuItem("Connect");
		menu.add(menuItem5);
		menuBar.add(menu);
		menu2 = new JMenu("Settings");
		
		this.setJMenuBar(menuBar);
		menuItem4 = new JMenuItem("Set Position");
		menu2.add(menuItem4);
		menuBar.add(menu2);
		
		tree = new TabletopTree();
		statusPanel = new ConnectionStatusPanel();
		controlPanel = new ControlPanel();
		progressPanel = new ProgressPanel();
		tree.addTreeSelectionListener(controlPanel);
		controlPanel.addControlPanelListener(this);
	    setSize(500, 700);
	    JScrollPane s = new JScrollPane();
	    s.getViewport().add(tree);
	    getContentPane().add(s, BorderLayout.CENTER);
	    getContentPane().add(statusPanel, BorderLayout.NORTH);
	    JPanel southPanel = new JPanel();
	    southPanel.setLayout(new BorderLayout());
	    southPanel.add(controlPanel, BorderLayout.CENTER);
	    southPanel.add(progressPanel, BorderLayout.SOUTH);
	    getContentPane().add(southPanel, BorderLayout.SOUTH);
	    
	    WindowListener wndCloser = new WindowAdapter(){
	      public void windowClosing(WindowEvent e){
	  		File inboxFolder = new File(MTDesktopConfigurations.inboxFolder);
	  		if(inboxFolder.exists()){
	  			File[] files = inboxFolder.listFiles();
	  			for (File file : files) 
	  				file.delete();
	  			System.exit(0);
	      }
	    }};
	    addWindowListener(wndCloser);
	    
	    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	    int w = this.getSize().width;
	    int h = this.getSize().height;
	    int x = (dim.width-w)/2;
	    int y = (dim.height-h)/2;
	    
	    this.setLocation(x, y);
	    
	    setVisible(true);
	    AssetRegistry.getInstance().addFileTransferListener(this);
	    /*
	    menuItem1.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				new MTMouseTest();				
			}
	    	
	    });
	    */
	    menuItem2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(tableRadar == null || !tableRadar.isVisible())	
					tableRadar = new TableRadar();				
			}
	    	
	    });
	    
		menuItem3.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent e) {
				ShowMultiPadMessage msg = new ShowMultiPadMessage(MTTableClient.class, DesktopClient.tableId, false);
				if(e.getStateChange() == ItemEvent.SELECTED){
					msg.setShow(true);
				}else{
					msg.setShow(false);
				}
				try {
					RapidNetworkManager.getTableCommsClientService().sendMessage(msg);
				} catch (IOException e1) {
					 
					e1.printStackTrace();
				}
			}
			
		});
		
		menuItem4.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				new PositionDialog();
			}
			
		});
		
		menuItem5.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				RapidNetworkManager.connect(app, false);
			}
			
		});
	}

	/* (non-Javadoc)
	 * @see apps.mtdesktop.desktop.DesktopClient.mtdesktopNetworkListener#connectionUpdate(apps.mtdesktop.desktop.DesktopClient.ConnectionStatus)
	 */
	@Override
	public void connectionUpdate(ConnectionStatus status) {
		statusPanel.setConnectionStatus(status);
		if(status.equals(ConnectionStatus.SEARCHING_FOR_TABLE)){
			progressPanel.setStatus("Searching...", true);
		}else{
			progressPanel.setStatus("", false);
		}
	}

	/* (non-Javadoc)
	 * @see apps.mtdesktop.desktop.DesktopClient.mtdesktopNetworkListener#tableConnected(synergynetframework.appsystem.services.net.localpresence.TableIdentity)
	 */
	@Override
	public void tableConnected(TableIdentity tableId) {
		String shortTableName = tableId.toString();
		if(shortTableName.length() > 7) shortTableName = shortTableName.substring(0,7) + "..";
	    tree.registerTable(new PeerData("Table : "+shortTableName, tableId));
		statusPanel.setConnectedTable(tableId);
	}

	/* (non-Javadoc)
	 * @see apps.mtdesktop.fileutility.FileTransferListener#fileUploadStarted(java.io.File)
	 */
	@Override
	public void fileUploadStarted(File file) {
		progressPanel.setStatus("Uploading "+file.getName(), true);
	}

	/* (non-Javadoc)
	 * @see apps.mtdesktop.fileutility.FileTransferListener#fileUploadCompleted(java.io.File)
	 */
	@Override
	public void fileUploadCompleted(File file) {
		progressPanel.setStatus("Upload complete ", false);
	}

	/* (non-Javadoc)
	 * @see apps.mtdesktop.fileutility.FileTransferListener#fileDownloadStarted(java.io.File)
	 */
	@Override
	public void fileDownloadStarted(File file) {
		 
		
	}

	/* (non-Javadoc)
	 * @see apps.mtdesktop.fileutility.FileTransferListener#fileDownloadCompleted(java.io.File)
	 */
	@Override
	public void fileDownloadCompleted(File file) {
		 
		
	}

	/* (non-Javadoc)
	 * @see apps.mtdesktop.fileutility.FileTransferListener#fileUploadFailed(java.io.File, java.lang.String)
	 */
	@Override
	public void fileUploadFailed(File file, String responseMessage) {
		progressPanel.setStatus("Upload failed ", false);
	}

	/* (non-Javadoc)
	 * @see apps.mtdesktop.fileutility.FileTransferListener#fileDownloadFailed(java.io.File, java.lang.String)
	 */
	@Override
	public void fileDownloadFailed(File file, String responseMessage) {
		 
		
	}

	
	/* (non-Javadoc)
	 * @see apps.mtdesktop.desktop.ControlPanel.ControlPanelListener#deletePressed()
	 */
	@Override
	public void deletePressed() {
		tree.getController().deleteSelected(tree.getSelectionPaths());
	}

	/* (non-Javadoc)
	 * @see apps.mtdesktop.desktop.ControlPanel.ControlPanelListener#shareDesktopPressed(boolean)
	 */
	@Override
	public void shareDesktopPressed(boolean isEnabled) {
		try{
			if(DesktopClient.tableId != null){
				String ipAddress = InetAddress.getLocalHost().getHostAddress().toString();
				RapidNetworkManager.getTableCommsClientService().sendMessage(new LaunchVncMessage(MTTableClient.class, DesktopClient.tableId, UUID.randomUUID().toString(), ipAddress, MTDesktopConfigurations.vncPort, MTDesktopConfigurations.vncPassword, isEnabled));
			}
		}catch(Exception exp){
			JOptionPane.showMessageDialog(this, "Error", exp.getMessage(), JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Gets the tree.
	 *
	 * @return the tree
	 */
	public TabletopTree getTree(){
		return tree;
	}
}
