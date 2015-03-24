package apps.basketapp.client;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

import apps.basketapp.client.Basket.BasketListener;
import apps.basketapp.controller.ContentSubMenu;
import apps.basketapp.controller.mysteries.TableMystery;
import apps.basketapp.controller.mysteries.TextMystery;
import apps.basketapp.messages.ClearMessage;
import apps.basketapp.messages.LockAllMessage;
import apps.basketapp.messages.SwapBasketsMessage;
import apps.basketapp.messages.TableAnounceMessage;
import apps.basketapp.messages.UnicastCaptureTableMessage;
import apps.basketapp.messages.UnicastClearMessage;
import apps.mysteriestableportal.MysteriesPortalClientApp;

import com.jme.util.GameTaskQueueManager;

import core.SynergyNetDesktop;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.Frame;
import synergynetframework.appsystem.contentsystem.items.LightImageLabel;
import synergynetframework.appsystem.contentsystem.items.MediaPlayer;
import synergynetframework.appsystem.contentsystem.items.OrthoContentItem;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.RapidNetworkManager;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.constructionmanagers.ConstructionManager;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.handlers.MessageProcessor;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.handlers.NetworkedContentMessageProcessor.NetworkedContentListener;
import synergynetframework.appsystem.services.net.tablecomms.messages.TableMessage;
import synergynetframework.appsystem.table.SynergyNetAppUtils;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.appsystem.table.appregistry.menucontrol.HoldTopRightConfirmVisualExit;


/**
 * The Class BasketApp.
 */
public class BasketApp extends DefaultSynergyNetApp implements BasketListener{

	/** The content system. */
	private ContentSystem contentSystem;
	
	/** The manager. */
	private BasketManager manager;
	
	/** The online items. */
	private List<ContentItem> onlineItems = new ArrayList<ContentItem>();
	
	/** The lock image. */
	private Frame lockImage;
	
	/** The dispatcher. */
	private SnapshotDispatcher dispatcher = new SnapshotDispatcher();
	//private StateRecorder stateRecorder;
	
	//private boolean loadState = true;
	
	/**
	 * Instantiates a new basket app.
	 *
	 * @param info the info
	 */
	public BasketApp(ApplicationInfo info) {
		super(info);
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#addContent()
	 */
	@Override
	public void addContent() {
		SynergyNetAppUtils.addTableOverlay(this);
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(this);
		contentSystem.removeAllContentItems();
		setMenuController(new HoldTopRightConfirmVisualExit(this));
		
		manager = new BasketManager(this);
		
		lockImage = (Frame) contentSystem.createContentItem(Frame.class);
		lockImage.setWidth(80);
		lockImage.setHeight(100);
		lockImage.drawImage(MysteriesPortalClientApp.class.getResource("lock.png"));
		lockImage.setOrder(99999);
		lockImage.setRotateTranslateScalable(false);
		lockImage.setLocalLocation(100, 600);
		lockImage.setVisible(false);
		/*
		if(loadState){
			StateLoader loader = new StateLoader(contentSystem);
			List<ContentItem> items = loader.loadItems();
			if(items != null) onlineItems.addAll(items);
		}
		*/
		//stateRecorder = new StateRecorder(this);
		//stateRecorder.startStateRecording();
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#onActivate()
	 */
	@Override
	public void onActivate() {
		RapidNetworkManager.addNetworkedContentListener(new NetworkedContentListener(){

			@Override
			public void itemsReceived(List<ContentItem> items, TableIdentity tableId) {
				for(ContentItem item: items){
					item.setName(UUID.randomUUID().toString());
					if(item.getNote().equalsIgnoreCase(ContentSubMenu.CONTENT_MAP)){
						MapInitializer mapInit = new MapInitializer((LightImageLabel)item);
						onlineItems.addAll(mapInit.getMystery(contentSystem));
					}else if(item.getNote().equalsIgnoreCase(ContentSubMenu.CONTENT_TEXT)){
						((OrthoContentItem)item).setScaleLimit(TextMystery.MIN_SCALE, TextMystery.MAX_SCALE);
					}else if(item.getNote().equalsIgnoreCase(ContentSubMenu.TABLE)){
						((OrthoContentItem)item).setScaleLimit(TableMystery.MIN_SCALE, TableMystery.MAX_SCALE);
					}
				}
				onlineItems.addAll(items);
				if(!tableId.toString().equalsIgnoreCase("teacher")){
					Basket basket = manager.getBasket(TableIdentity.getTableIdentity());
					if(basket != null){
						basket.addItems(items);
					}
				}
			}

			@Override
			public void tableDisconnected() {
				 
				
			}

			@Override
			public void tableConnected() {
				try {
					Thread.sleep(1000);
					initBaskets();
					RapidNetworkManager.getTableCommsClientService().sendMessage(new TableAnounceMessage(BasketApp.class));
				} catch (IOException e) {
					 
					e.printStackTrace();
				} catch (InterruptedException e) {
					 
					e.printStackTrace();
				}				
			}
			
		});
		RapidNetworkManager.setAutoReconnect(true);
		RapidNetworkManager.connect(this);
		RapidNetworkManager.getReceiverClasses().clear();
		RapidNetworkManager.getReceiverClasses().add(BasketApp.class);
		
		RapidNetworkManager.registerMessageProcessor(new MessageProcessor(){

			@Override
			public void process(Object obj) {
				if(obj instanceof TableAnounceMessage){
					initBaskets();
				}else if(obj instanceof ClearMessage || obj instanceof UnicastClearMessage){
					for(ContentItem item: onlineItems){
						for(Basket basket: manager.getBaskets()){
							if(basket.containsItem(item)) basket.detachItem(item);
						}
						contentSystem.removeContentItem(item);
					}
					onlineItems.clear();
				}else if(obj instanceof SwapBasketsMessage){
					manager.swapBaskets();
				}else if(obj instanceof LockAllMessage){
					LockAllMessage msg = (LockAllMessage) obj;
					SynergyNetDesktop.getInstance().getMultiTouchInputComponent().setMultiTouchInputEnabled(!msg.isLocked());
					BasketApp.this.lockImage.setVisible(msg.isLocked());
				}else if(obj instanceof UnicastCaptureTableMessage){
					dispatcher.dispatchSnapshot(((TableMessage)obj).getSender());					
				}
			}
			
		});
		
		RapidNetworkManager.registerConstructionManager(MediaPlayer.class, new ConstructionManager(){

			@Override
			public HashMap<String, Object> buildConstructionInfo(
					ContentItem item) {
				HashMap<String, Object> info = new HashMap<String, Object>();
				info.put("media_url", ((MediaPlayer)item).getMediaURL());
				info.put("video_time", ((MediaPlayer)item).getVideoPlayer().getVideoTime());
				return info;
			}

			@Override
			public void processConstructionInfo(ContentItem item,
					HashMap<String, Object> info) {
				((MediaPlayer)item).setMediaURL((URL)info.get("media_url"));
				((MediaPlayer)item).getVideoPlayer().setVideoTime(Double.valueOf(info.get("video_time").toString()));
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					 
					e.printStackTrace();
				}
			}
			
		});

	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp#stateUpdate(float)
	 */
	@Override
	protected void stateUpdate(float tpf) {
		super.stateUpdate(tpf);
		if(contentSystem != null) contentSystem.update(tpf);
		if(manager != null) manager.update(tpf);
	}
	
	/**
	 * Inits the baskets.
	 */
	public void initBaskets(){
		GameTaskQueueManager.getManager().update(new Callable<Object>() {
			public Object call() throws Exception {
				int x = 180;
				int y = 650;
				for(TableIdentity tableId: RapidNetworkManager.getTableCommsClientService().getCurrentlyOnline()){
					if(tableId.toString().equalsIgnoreCase("teacher")) continue;
					Basket basket = null;
					if(!manager.isRegistered(tableId)){
						basket = new Basket(contentSystem, tableId);
						manager.registerBasket(basket);
					}else{
						basket = manager.getBasket(tableId);
					}
					if(basket != null){
						basket.getWindow().setLocalLocation(x, y);
						basket.getWindow().setOrder(-999999);
						basket.addBasketListener(BasketApp.this);
					}
					 x+=230;
				}
				return null;
			}
		});
	}
	
	/**
	 * Creates the lock image.
	 */
	public void createLockImage(){
		if(lockImage == null){
			lockImage = (Frame) contentSystem.createContentItem(Frame.class);
			lockImage.setWidth(80);
			lockImage.setHeight(100);
			lockImage.drawImage(MysteriesPortalClientApp.class.getResource("lock.png"));
			lockImage.setOrder(99999);
			lockImage.setRotateTranslateScalable(false);
			lockImage.setLocalLocation(100, 600);
		}
		lockImage.setVisible(!SynergyNetDesktop.getInstance().getMultiTouchInputComponent().isMultiTouchInputEnabled());
	}

	/**
	 * Gets the online items.
	 *
	 * @return the online items
	 */
	public List<ContentItem> getOnlineItems() {
		return onlineItems;
	}

	/* (non-Javadoc)
	 * @see apps.basketapp.client.Basket.BasketListener#itemAdded(synergynetframework.appsystem.contentsystem.items.ContentItem)
	 */
	@Override
	public void itemAdded(ContentItem item) {
		
	}

	/* (non-Javadoc)
	 * @see apps.basketapp.client.Basket.BasketListener#itemDetached(synergynetframework.appsystem.contentsystem.items.ContentItem)
	 */
	@Override
	public void itemDetached(ContentItem item) {
		if(item.getNote().equalsIgnoreCase("text")){
			((OrthoContentItem)item).setScale(0.5f);
			((OrthoContentItem)item).setScaleLimit(TextMystery.MIN_SCALE, TextMystery.MAX_SCALE);
		}
	}
}
