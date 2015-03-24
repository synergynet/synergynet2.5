package apps.mtdesktop.tabletop;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;


import apps.mtdesktop.MTDesktopConfigurations;
import apps.mtdesktop.desktop.DesktopClient;
import apps.mtdesktop.messages.AnnounceTableMessage;
import apps.mtdesktop.messages.UnicastSearchTableMessage;
import apps.mtdesktop.tabletop.basket.JmeNetworkedBasket;
import apps.mtdesktop.tabletop.fileserver.WebServer;

import com.jme.system.DisplaySystem;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.LightImageLabel;
import synergynetframework.appsystem.contentsystem.items.LineItem;
import synergynetframework.appsystem.contentsystem.items.MultiLineTextLabel;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.VncFrame;
import synergynetframework.appsystem.contentsystem.items.Window;
import synergynetframework.appsystem.contentsystem.items.listener.ItemListener;
import synergynetframework.appsystem.contentsystem.items.listener.OrthoControlPointRotateTranslateScaleListener;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.RapidNetworkManager;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.handlers.MessageProcessor;
import synergynetframework.appsystem.table.SynergyNetAppUtils;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.appsystem.table.appregistry.menucontrol.HoldTopRightConfirmVisualExit;


/**
 * The Class MTTableClient.
 */
public class MTTableClient extends DefaultSynergyNetApp{

	/** The content system. */
	private ContentSystem contentSystem;
	
	/** The manager. */
	private TabletopContentManager manager;
	
	/** The background. */
	public static LightImageLabel background;
	
	/**
	 * Instantiates a new MT table client.
	 *
	 * @param info the info
	 */
	public MTTableClient(ApplicationInfo info) {
		super(info);
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#addContent()
	 */
	@Override
	public void addContent() {
		
  		File outboxFolder = new File(MTDesktopConfigurations.OutboxFolder);
  		if(outboxFolder.exists()){
  			File[] files = outboxFolder.listFiles();
  			for (File file : files) 
  				file.delete();
  		}
  		
  		File tempFolder = new File(MTDesktopConfigurations.tabletopTempFolder);
  		if(tempFolder.exists()){
  			File[] files = tempFolder.listFiles();
  			for (File file : files) 
  				file.delete();
  		}
  		
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(this);
		SynergyNetAppUtils.addTableOverlay(this);
		setMenuController(new HoldTopRightConfirmVisualExit(this));


		manager = new TabletopContentManager(contentSystem, this);
		
		background = (LightImageLabel) contentSystem.createContentItem(LightImageLabel.class);
		background.drawImage(MTDesktopConfigurations.class.getResource("tabletop/wood-texture.jpg"));
		background.setAutoFitSize(false);
		background.setWidth(DisplaySystem.getDisplaySystem().getWidth());
		background.setHeight(DisplaySystem.getDisplaySystem().getHeight());
		background.centerItem();
		background.setRotateTranslateScalable(false);
		background.setBringToTopable(false);
		background.setOrder(-99999999);

		RapidNetworkManager.registerMessageProcessor(manager);
		  
		try {
			WebServer server = new WebServer();
			new Thread(server).start();
			server.getFtpServerServlet().addFtpServletListener(manager);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		/*
		MTKeyboard keyboard = (MTKeyboard) contentSystem.createContentItem(MTKeyboard.class);
		keyboard.addKeyListener(new MTKeyListener(){

			@Override
			public void keyPressedEvent(KeyEvent evt) {
				System.out.println(evt.getKeyChar());
			}

			@Override
			public void keyReleasedEvent(KeyEvent evt) {
				 
				
			}
			
		});
		keyboard.setScale(0.4f);
		
		Window w = (Window) contentSystem.createContentItem(Window.class);
		w.addSubItem(keyboard);
		w.centerItem();
		*/
		
		final Window w = (Window) contentSystem.createContentItem(Window.class);
		w.getBackgroundFrame().setVisible(false, false);
		
		SimpleButton b1 = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		b1.setAutoFitSize(false);
		b1.setWidth(30);
		b1.setHeight(30);
		b1.drawImage(MTDesktopConfigurations.class.getResource("tabletop/text.png"));
		
		SimpleButton b2 = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		b2.setAutoFitSize(false);
		b2.setWidth(30);
		b2.setHeight(30);
		b2.drawImage(MTDesktopConfigurations.class.getResource("tabletop/link.gif"));
		
		w.addSubItem(b1);
		w.addSubItem(b2);
		b1.setLocalLocation(0, -17);
		b2.setLocalLocation(0, 17);
		
		w.setLocation(500, 100);
		
		final MultiLineTextLabel mttl = (MultiLineTextLabel) contentSystem.createContentItem(MultiLineTextLabel.class);
		mttl.setBackgroundColour(Color.white);
		mttl.setTextColour(Color.black);
		mttl.setFont(new Font("Helvetica", Font.BOLD,  14));
		mttl.setCRLFSeparatedString("HP CN216B Photosmart Plus e-All-in-One Web Enabled Printer\n Technical Details: \n\n Functions: Print, Copy, Scan, Web \n Print Technology HP Thermal Inkjet \n No. Of cartridges: 4 (1 each black, cyan, magenta, yellow) \n Standard Connectivity: 1 USB 2.0, 1 Wireless 802.11b \n Network Ready: Standard (built-in WiFi 802.11b/g/n) "); 
		mttl.setAutoFitSize(false);
		mttl.setHeight(150);
		mttl.setWidth(450);
		mttl.setLocation(500, 200);
		
		final LineItem line = (LineItem) contentSystem.createContentItem(LineItem.class);
		line.setVisible(true);
		line.setAsTopObject();
		//line.setBackgroundColour(Color.white);
		line.setWidth(2);
		line.setArrowMode(LineItem.ARROW_TO_SOURCE);
		line.setSourceItem(mttl);
		line.setTargetItem(b2);
		
		line.setSourceLocation(mttl.getLocation());
		line.setTargetLocation(w.getLocation());

		w.addItemListener(new ItemListener(){

			@Override
			public void cursorPressed(ContentItem item, long id, float x,
					float y, float pressure) {
				line.setTargetLocation(w.getLocation());
				
			}

			@Override
			public void cursorChanged(ContentItem item, long id, float x,
					float y, float pressure) {
				line.setTargetLocation(w.getLocation());
				
			}

			@Override
			public void cursorReleased(ContentItem item, long id, float x,
					float y, float pressure) {
				line.setTargetLocation(w.getLocation());
				
			}

			@Override
			public void cursorClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				line.setTargetLocation(w.getLocation());
				
			}

			@Override
			public void cursorRightClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				 
				
			}

			@Override
			public void cursorLongHeld(ContentItem item, long id, float x,
					float y, float pressure) {
				 
				
			}

			@Override
			public void cursorDoubleClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				 
				
			}
			
		});
		
		
		mttl.addOrthoControlPointRotateTranslateScaleListener(new OrthoControlPointRotateTranslateScaleListener(){

			@Override
			public void itemScaled(ContentItem item, float newScaleFactor,
					float oldScaleFactor) {
				line.setSourceLocation(mttl.getLocation());
			}

			@Override
			public void itemTranslated(ContentItem item, float newLocationX,
					float newLocationY, float oldLocationX, float oldLocationY) {
				line.setSourceLocation(mttl.getLocation());
			}

			@Override
			public void itemRotated(ContentItem item, float newAngle,
					float oldAngle) {
				line.setSourceLocation(mttl.getLocation());
			}
			
		});
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#onActivate()
	 */
	@Override
	public void onActivate() {
		
		RapidNetworkManager.registerMessageProcessor(new MessageProcessor(){

			@Override
			public void process(Object obj) {
				if(obj instanceof UnicastSearchTableMessage){
					UnicastSearchTableMessage msg = (UnicastSearchTableMessage) obj;
					if(manager.getBasketManager().getBaskets().containsKey(msg.getSender())){
						manager.getBasketManager().unregisterBasket(msg.getSender());
						manager.unregisterMouseCursor(msg.getSender());
						manager.getPad().unregisterUser(msg.getSender());
					}
					JmeNetworkedBasket basket = manager.getBasketManager().registerBasketForPeer(msg.getSender(), msg.getClientPosition());
					if(basket != null){
						manager.getPad().registerUser(basket.getTableId(), basket.getWindow().getBackgroundColour());
						basket.excludeItem(VncFrame.class);
					}
					announce();
				}
			}
			
		});
	
		
		if(!RapidNetworkManager.getReceiverClasses().contains(DesktopClient.class)) RapidNetworkManager.getReceiverClasses().add(DesktopClient.class);
		RapidNetworkManager.setAutoReconnect(true);
		RapidNetworkManager.connect(this);
	}
	
	/**
	 * Announce.
	 */
	public void announce(){
		try {
			String fileServerUrl = "http://"+InetAddress.getLocalHost().getHostAddress()+":"+ MTDesktopConfigurations.SERVER_PORT;
			for(Class<?> targetClass: RapidNetworkManager.getReceiverClasses())	
				RapidNetworkManager.getTableCommsClientService().sendMessage(new AnnounceTableMessage(targetClass, fileServerUrl));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#cleanup()
	 */
	@Override
	public void cleanup(){
  		File outboxFolder = new File(MTDesktopConfigurations.OutboxFolder);
  		if(outboxFolder.exists()){
  			File[] files = outboxFolder.listFiles();
  			for (File file : files) 
  				file.delete();
  		}
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
}
