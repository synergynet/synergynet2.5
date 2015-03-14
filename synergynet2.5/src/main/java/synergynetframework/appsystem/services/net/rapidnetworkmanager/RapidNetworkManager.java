package synergynetframework.appsystem.services.net.rapidnetworkmanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;


import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.services.ServiceManager;
import synergynetframework.appsystem.services.exceptions.CouldNotStartServiceException;
import synergynetframework.appsystem.services.exceptions.ServiceNotRunningException;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.netservicediscovery.NetworkServiceDiscoveryService;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.constructionmanagers.ConstructionManager;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.handlers.DefaultMessageHandler;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.handlers.MessageProcessor;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.handlers.NetworkFlickMessageProcessor;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.handlers.NetworkedContentMessageProcessor;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.handlers.NetworkedContentMessageProcessor.NetworkedContentListener;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.messages.BroadcastItemConstructionMessage;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.messages.BroadcastItemsMessage;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.messages.PostItemConstructionMessage;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.messages.PostItemsMessage;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.messages.networkflick.AnnounceTableMessage;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.messages.networkflick.EnableFlickMessage;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.messages.networkflick.UnregisterTableMessage;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.utils.networkflick.TableInfo;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.utils.networkflick.TransferController;
import synergynetframework.appsystem.services.net.tablecomms.client.TableCommsApplicationListener;
import synergynetframework.appsystem.services.net.tablecomms.client.TableCommsClientService;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;

public class RapidNetworkManager{

	private static final Logger log = Logger.getLogger(RapidNetworkManager.class.getName());
	
	protected static TableCommsClientService comms;
	protected static HashMap<Class<? extends ContentItem>, ConstructionManager> constructionManagers = new HashMap<Class<? extends ContentItem>, ConstructionManager>();
	protected static List<NetworkedContentListener> contentListeners = new ArrayList<NetworkedContentListener>();
	protected static List<Class<?>> receiverClasses = new ArrayList<Class<?>>();
	protected static boolean autoReconnect = false;
	public static boolean isConnected = false;
	protected static boolean isFlickEnabled = false;
	protected static int reconnectTime = 1000;
	protected static DefaultMessageHandler messageHandler =  new DefaultMessageHandler();
	protected static DefaultSynergyNetApp app;
	protected static TransferController transferController;
	protected static TableInfo tableInfo = new TableInfo(TableIdentity.getTableIdentity(),0, 0, 0);
	protected static NetworkFlickMessageProcessor nfmp;
	protected static NetworkedContentMessageProcessor processor;
	protected static NetworkServiceDiscoveryService nsds = null;
	
	public enum SyncType{UNIDIRECTIONAL_SYNC, BIDIRECTIONAL_SYNC};

	public static void broadcastItem(ContentItem item) throws IOException {
		List<ContentItem> items = new ArrayList<ContentItem>();
		items.add(item);
		broadcastItems(items);
	}

	public static void broadcastItems(List<ContentItem> items) throws IOException {
		if(comms == null) throw new IOException("Table is not connected");
		BroadcastItemsMessage msgBindableItems = null;
		BroadcastItemConstructionMessage msgNonBindableItems = null;
		List<ContentItem> bindableItems = new ArrayList<ContentItem>();
		List<ContentItem> nonBindableItems = new ArrayList<ContentItem>();
		for(ContentItem item: items){
			if(constructionManagers.containsKey(item.getClass())) nonBindableItems.add(item);
			else bindableItems.add(item);
		}
		if(!bindableItems.isEmpty()){
			msgBindableItems = new BroadcastItemsMessage();
			msgBindableItems.setItems(bindableItems);
		}
		if(!nonBindableItems.isEmpty()){
			HashMap<ContentItem, HashMap<String, Object>> constructInfo = new HashMap<ContentItem, HashMap<String, Object>>();
			for(ContentItem item: nonBindableItems){
				ConstructionManager contruct = constructionManagers.get(item.getClass());
				constructInfo.put(item, contruct.buildConstructionInfo(item));
			}
			msgNonBindableItems = new BroadcastItemConstructionMessage();
			msgNonBindableItems.setConstructionInfo(constructInfo);
		}
		
		for(Class<?> targetClass: receiverClasses){
			if(msgBindableItems != null){
				msgBindableItems.setTargetClassName(targetClass.getName());
				comms.sendMessage(msgBindableItems);
				log.info("Broadcast bindable items");
			}else if(msgNonBindableItems != null){
				msgNonBindableItems.setTargetClassName(targetClass.getName());
				comms.sendMessage(msgNonBindableItems);
				log.info("Broadcast non-bindable items");
			}
		}
	}
	
	
	public static void postItem(ContentItem item, TableIdentity tableIdentity) throws IOException {
		List<ContentItem> items = new ArrayList<ContentItem>();
		items.add(item);
		postItems(items, tableIdentity);
		
	}
	
	public static void postItems(List<ContentItem> items, TableIdentity tableIdentity) throws IOException {
		if(comms == null) throw new IOException("Table is not connected");
		PostItemsMessage msgBindableItems = null;
		PostItemConstructionMessage msgNonBindableItems = null;
		List<ContentItem> bindableItems = new ArrayList<ContentItem>();
		List<ContentItem> nonBindableItems = new ArrayList<ContentItem>();
		for(ContentItem item: items){
			if(constructionManagers.containsKey(item.getClass())) nonBindableItems.add(item);
			else bindableItems.add(item);
		}
		if(!bindableItems.isEmpty()){
			msgBindableItems = new PostItemsMessage();
			msgBindableItems.setRecipient(tableIdentity);
			msgBindableItems.setItems(bindableItems);
		}
		if(!nonBindableItems.isEmpty()){
			HashMap<ContentItem, HashMap<String, Object>> constructInfo = new HashMap<ContentItem, HashMap<String, Object>>();
			for(ContentItem item: nonBindableItems){
				ConstructionManager contruct = constructionManagers.get(item.getClass());
				constructInfo.put(item, contruct.buildConstructionInfo(item));
			}
			msgNonBindableItems = new PostItemConstructionMessage();
			msgNonBindableItems.setRecipient(tableIdentity);
			msgNonBindableItems.setConstructionInfo(constructInfo);
		}
		
		for(Class<?> targetClass: receiverClasses){
			if(msgBindableItems != null){
				msgBindableItems.setTargetClassName(targetClass.getName());
				comms.sendMessage(msgBindableItems);
				log.info("Post bindable items to table-"+tableIdentity.toString());
			}else if(msgNonBindableItems != null){
				msgNonBindableItems.setTargetClassName(targetClass.getName());
				comms.sendMessage(msgNonBindableItems);
				log.info("Post non-bindable items to table-"+tableIdentity.toString());
			}
		}
	}

	public static void registerConstructionManager(Class<? extends ContentItem> itemClass, ConstructionManager constructManager) {
		constructionManagers.put(itemClass, constructManager);
		log.info("Register construction manager");
	}


	public static void shareItem(ContentItem item, TableIdentity tableIdentity, SyncType syncType) throws IOException {
		if(comms == null) throw new IOException("Table is not connected");
		List<ContentItem> items = new ArrayList<ContentItem>();
		items.add(item);
	}


	public static void unshareItem(ContentItem item, TableIdentity tableIdentity, SyncType syncType) throws IOException {
		List<ContentItem> items = new ArrayList<ContentItem>();
		items.add(item);
	}


	public static void connect(final DefaultSynergyNetApp app){
		RapidNetworkManager.connect(app, true);
	}
	
	public static void connect(final DefaultSynergyNetApp app, final boolean contentSystemExists){
		RapidNetworkManager.app = app;
		//if(!receiverClasses.contains(app.getClass())) receiverClasses.add(app.getClass());
		new Thread(new Runnable(){
			@Override
			public void run(){
				try {
					if(comms == null) comms = (TableCommsClientService) ServiceManager.getInstance().get(TableCommsClientService.class);
					if(comms != null && RapidNetworkManager.autoReconnect){
							while(!comms.isClientConnected()){
							Thread.sleep(reconnectTime);
							if(nsds == null){
								nsds = (NetworkServiceDiscoveryService) ServiceManager.getInstance().get(NetworkServiceDiscoveryService.class);
							}else{
								nsds.shutdown();
							}
							nsds.start();
							comms.start();
						}
						if(nsds != null) nsds.shutdown();
						if(processor != null) processor.removeNetworkedContentListeners();
						if(contentSystemExists)
							processor = new NetworkedContentMessageProcessor(ContentSystem.getContentSystemForSynergyNetApp(app));
						else
							processor = new NetworkedContentMessageProcessor(null);
						messageHandler.registerMessageProcessor(processor);
						processor.addNetworkedContentListener(new NetworkedContentListener(){

							@Override
							public void itemsReceived(List<ContentItem> item,	TableIdentity tableId) {
								for(NetworkedContentListener listener: contentListeners){
									listener.itemsReceived(item, tableId);
								}
							}

							@Override
							public void tableDisconnected() {
								 
								
							}

							@Override
							public void tableConnected() {
								 
								
							}
							
						});
						comms.register(app, messageHandler);
						comms.register("connection_listener", new TableCommsApplicationListener(){
							@Override
							public void messageReceived(Object obj) {
								 
								
							}

							@Override
							public void tableDisconnected() {
								if(comms != null)
									try {
										comms.stop();
									} catch (ServiceNotRunningException e) {
										 
										e.printStackTrace();
									}
								comms = null;
								isConnected = false;
								for(NetworkedContentListener listener: contentListeners){
									listener.tableDisconnected();
								}
								if(RapidNetworkManager.autoReconnect) connect(app, contentSystemExists);
							}
							
						});
						isConnected = true;
						for(NetworkedContentListener listener: contentListeners){
							listener.tableConnected();
						}
					}
				} catch (CouldNotStartServiceException e1) {
					log.warning(e1.toString());
				} catch (IOException e) {
					log.warning(e.toString());
				} catch (InterruptedException e) {
					log.warning(e.toString());
				}
			}
		}).start();
		
		log.info("Connected to server");
	}

	public static TableCommsClientService getTableCommsClientService(){
		return comms;
	}
	
	public static void update(){
		if(transferController != null) transferController.update();
	}
	
	public static HashMap<Class<? extends ContentItem>, ConstructionManager> getConstructionManagers(){
		return constructionManagers;
	}
	
	public static void setAutoReconnect(boolean autoReconnect){
		RapidNetworkManager.autoReconnect = autoReconnect;
	}
	
	public static void setAutoConnectTime(int reconnectTime){
		RapidNetworkManager.reconnectTime = reconnectTime;
	}
	
	public static void registerMessageProcessor(MessageProcessor processor){
		if(!messageHandler.getMessageProcessors().contains(processor)) messageHandler.registerMessageProcessor(processor);
	}

	public static void removeMessageProcessor(MessageProcessor processor){
		messageHandler.removeMessageProcessor(processor);
	}
	
	public static List<Class<?>> getReceiverClasses() {
		return receiverClasses;
	}
	
	
	public static void setTablePosition(int x, int y){
		tableInfo.setTablePosition(x, y);
		try {
			RapidNetworkManager.enableNetworkFlick(RapidNetworkManager.isFlickEnabled);
		} catch (IOException e) {
			log.info(e.toString());
		}
	}
	
	public static void setTableOrientation(float angle){
		tableInfo.setAngle(angle);
		try {
			RapidNetworkManager.enableNetworkFlick(RapidNetworkManager.isFlickEnabled);
		} catch (IOException e) {
			log.info(e.toString());
		}
	}
	
	public static void enableNetworkFlick(boolean isFlickEnabled) throws IOException{
		if(comms == null) throw new IOException("Table is not connected");
		if(isFlickEnabled){
			RapidNetworkManager.isFlickEnabled = isFlickEnabled;
			if(transferController == null) transferController = new TransferController(app);
			transferController.enableNetworkFlick(isFlickEnabled);
			if(nfmp == null) nfmp = new NetworkFlickMessageProcessor(transferController);
			RapidNetworkManager.registerMessageProcessor(nfmp);
			transferController.setLocalTableInfo(tableInfo);
			for(Class<?> receiverClass: RapidNetworkManager.getReceiverClasses()){
				comms.sendMessage( new AnnounceTableMessage(receiverClass, tableInfo));
				comms.sendMessage(new EnableFlickMessage(receiverClass,true));
			}
		}else{
			for(Class<?> receiverClass: RapidNetworkManager.getReceiverClasses()){
				if(transferController != null) transferController.enableNetworkFlick(isFlickEnabled);
				comms.sendMessage( new UnregisterTableMessage(receiverClass));
				comms.sendMessage(new EnableFlickMessage(receiverClass,false));
			}
		}
		
		log.info("Enable network flick");
	}
	
	public static void addNetworkedContentListener(NetworkedContentListener listener){
		contentListeners.add(listener);
	}
	
	public static void removeNetworkedContentListeners(){
		contentListeners.clear();
	}

	public static void removeMessageProcessors() {
		
		messageHandler.removeMessageProcessors();
	}
}
