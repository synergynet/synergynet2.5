package apps.mtdesktop.desktop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import apps.mtdesktop.MTDesktopConfigurations;
import apps.mtdesktop.desktop.inputchannels.KeyboardRedirector;
import apps.mtdesktop.messages.AnnounceTableMessage;
import apps.mtdesktop.messages.UnicastSearchTableMessage;
import apps.mtdesktop.tabletop.MTTableClient;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.RapidNetworkManager;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.handlers.MessageProcessor;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.handlers.NetworkedContentMessageProcessor.NetworkedContentListener;
import synergynetframework.appsystem.services.net.tablecomms.messages.TableMessage;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.config.logging.LoggingConfigPrefsItem;
import synergynetframework.config.logging.LoggingConfigPrefsItem.LoggingLevel;

public class DesktopClient extends DefaultSynergyNetApp{

	public enum Position{NORTH, SOUTH, EAST, WEST};
	public static Position currentPosition = Position.WEST;
	
	public static String File_SERVER_URL;
	public static enum ConnectionStatus{CONNECTED_TO_SERVER, SEARCHING_FOR_TABLE, CONNECTED_TO_TABLE, DISCONNECTED};
	
	public static TableIdentity tableId;
	
	protected DesktopFrame desktopFrame;
	protected ConnectionStatus status = DesktopClient.ConnectionStatus.DISCONNECTED;
	protected List<mtdesktopNetworkListener> listeners = new ArrayList<mtdesktopNetworkListener>();
	
	public DesktopClient(ApplicationInfo info) {
		super(info);
		Preferences root = Preferences.userRoot();
		String position = root.get("client_position", Position.EAST.toString());
		if(position.equalsIgnoreCase("north"))
			currentPosition = Position.NORTH;
		else if(position.equalsIgnoreCase("south"))
			currentPosition = Position.SOUTH;
		else if(position.equalsIgnoreCase("east"))
			currentPosition = Position.EAST;
		else if(position.equalsIgnoreCase("west"))
			currentPosition = Position.WEST;
		
		//set logging level
		LoggingConfigPrefsItem logPrefs = new LoggingConfigPrefsItem();
		LoggingLevel loggingLevel = logPrefs.getLoggingLevel();		
		Logger.getLogger("").setLevel(Level.parse(loggingLevel.name()));
		
		File inboxFolder = new File(MTDesktopConfigurations.inboxFolder);
		if(!inboxFolder.exists()) inboxFolder.mkdir();
		
  		File tempFolder = new File(MTDesktopConfigurations.desktopTempFolder);
  		if(tempFolder.exists()){
  			File[] files = tempFolder.listFiles();
  			for (File file : files) 
  				file.delete();
  		}
  		
		desktopFrame = new DesktopFrame(this);
		
		this.addNetworkListener(desktopFrame);
		
		RapidNetworkManager.addNetworkedContentListener(new NetworkedContentListener(){

			@Override
			public void itemsReceived(List<ContentItem> item,
					TableIdentity tableId) {
				 
				
			}

			@Override
			public void tableDisconnected() {
				status = DesktopClient.ConnectionStatus.DISCONNECTED;
				for(mtdesktopNetworkListener listener: listeners)
					listener.connectionUpdate(status);
			}

			@Override
			public void tableConnected() {
				status = DesktopClient.ConnectionStatus.SEARCHING_FOR_TABLE;
				// search for table
				new Thread(new SearchForTableThread()).start();
				for(mtdesktopNetworkListener listener: listeners)
					listener.connectionUpdate(status);
			}
			
		});
		
		RapidNetworkManager.registerMessageProcessor(new MessageProcessor(){

			@Override
			public void process(Object obj) {
				if(obj instanceof AnnounceTableMessage){
					if(!(((TableMessage)obj).getSender().equals(MTDesktopConfigurations.tableId))) return;
					tableId = ((TableMessage)obj).getSender();
					File_SERVER_URL = ((AnnounceTableMessage)obj).getFileServerUrl();
					synchronized(this){
						status = DesktopClient.ConnectionStatus.CONNECTED_TO_TABLE;
					}
					for(mtdesktopNetworkListener listener: listeners){
						listener.connectionUpdate(status);
						listener.tableConnected(tableId);
					}
				}
			}
			
		});
		RapidNetworkManager.registerMessageProcessor(new DesktopInboxContentManager(desktopFrame.getTree()));
		RapidNetworkManager.registerMessageProcessor(new KeyboardRedirector(desktopFrame));
		if(!RapidNetworkManager.getReceiverClasses().contains(MTTableClient.class)) RapidNetworkManager.getReceiverClasses().add(MTTableClient.class);
		RapidNetworkManager.setAutoReconnect(true);
		
		new Thread(new Runnable(){

			@Override
			public void run() {
				while(true)
					if(RapidNetworkManager.getTableCommsClientService() != null){
						try {
							Thread.sleep(MTDesktopConfigurations.CLINET_CONNECTION_UPDATE);
						} catch (InterruptedException e) {
							 
							e.printStackTrace();
						}
						if(RapidNetworkManager.getTableCommsClientService() != null)	
							RapidNetworkManager.getTableCommsClientService().update();
					}
			}
			
		}).start();
	}

	public void addNetworkListener(mtdesktopNetworkListener listener){
		if(!listeners.contains(listener))
			listeners.add(listener);
	}
	
	public static void main(String args[]){
		ApplicationInfo info = null;
		try {
			info = new ApplicationInfo(DesktopClient.class.getName(), "Desktop Client", "version1.0", ApplicationInfo.REACTIVATEPOLICY_RESTART);
		} catch (ClassNotFoundException e) {
			 
			e.printStackTrace();
		}
		new DesktopClient(info);
	}

	@Override
	public void addContent() {

	}
	
	@Override
	public void onActivate() {

	}
	
	public interface mtdesktopNetworkListener{
		public void connectionUpdate(ConnectionStatus status);
		public void tableConnected(TableIdentity TableId);
	}
	
	class SearchForTableThread implements Runnable{
		
		@Override
		public void run() {
			synchronized(DesktopClient.this){
				try {
					while(!status.equals(ConnectionStatus.CONNECTED_TO_TABLE)){
						RapidNetworkManager.getTableCommsClientService().sendMessage(new UnicastSearchTableMessage(MTTableClient.class, MTDesktopConfigurations.tableId, currentPosition));
						Thread.sleep(MTDesktopConfigurations.TABLE_SEARCH_DELAY);
					}
				} catch (InterruptedException e) {
					 
					e.printStackTrace();
				} catch (IOException e) {
					 
					e.printStackTrace();
				}
			}
		}
	}

}
