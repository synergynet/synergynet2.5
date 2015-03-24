package apps.geognetworkflick;


import java.awt.Color;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.prefs.Preferences;

import javax.swing.ImageIcon;

import com.jme.math.FastMath;
import com.jme.util.GameTaskQueueManager;

import core.ConfigurationSystem;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ImageTextLabel;
import synergynetframework.appsystem.services.ServiceManager;
import synergynetframework.appsystem.services.exceptions.CouldNotStartServiceException;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.networkedcontentmanager.NetworkedContentManager;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messagehandler.DefaultMessageHandler;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.networkedflick.AnnounceTableMessage;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.networkedflick.EnableFlickMessage;
import synergynetframework.appsystem.services.net.networkedcontentmanager.utils.networkedflick.TableInfo;
import synergynetframework.appsystem.services.net.networkedcontentmanager.utils.networkedflick.TransferController;
import synergynetframework.appsystem.services.net.tablecomms.client.TableCommsClientService;
import synergynetframework.appsystem.services.net.tablecomms.messages.control.fromclient.ApplicationCommsRequest;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.appsystem.table.appregistry.menucontrol.HoldTopRightConfirmVisualExit;
import synergynetframework.config.position.PositionConfigPrefsItem;


/**
 * The Class GeogNetworkFlickApp.
 */
public class GeogNetworkFlickApp extends DefaultSynergyNetApp{

	/** The comms. */
	private TableCommsClientService comms;
	
	/** The content system. */
	protected ContentSystem contentSystem;
	
	/** The message handler. */
	protected DefaultMessageHandler messageHandler;
	
	/** The networked content manager. */
	protected NetworkedContentManager networkedContentManager;
	
	/** The transfer controller. */
	protected TransferController transferController;

	/**
	 * Instantiates a new geog network flick app.
	 *
	 * @param info the info
	 */
	public GeogNetworkFlickApp(ApplicationInfo info) {
		super(info);
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#addContent()
	 */
	@Override
	public void addContent() {
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(this);		
		setMenuController(new HoldTopRightConfirmVisualExit(this));
	
		TableIdentity yellowTableID = new TableIdentity("yellow");
		TableIdentity greenTableID = new TableIdentity("green");
		TableIdentity blueTableID = new TableIdentity("blue");
		TableIdentity redTableID = new TableIdentity("red");
		
		if(TableIdentity.getTableIdentity().equals(yellowTableID)) {
			backgroundLoad(yellowTable);
		}else if(TableIdentity.getTableIdentity().equals(greenTableID)) {
			backgroundLoad(greenTable);
		}else if(TableIdentity.getTableIdentity().equals(blueTableID)) {
			backgroundLoad(blueTable);
		}else if(TableIdentity.getTableIdentity().equals(redTableID)) {
			backgroundLoad(redTable);
		}else{
			backgroundLoad(textImages);
		}

	}
	
	/**
	 * Background load.
	 *
	 * @param imageSet the image set
	 */
	protected void backgroundLoad(final String[] imageSet) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				for(final String img : imageSet) {


					GameTaskQueueManager.getManager().update(new Callable<Void>() {

						@Override
						public Void call() throws Exception {
							addImage(GeogNetworkFlickApp.class.getResource(img));						
							return null;
						}					
					});
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		}).start();

	}

	/**
	 * Adds the image.
	 *
	 * @param resource the resource
	 */
	private void addImage(URL resource) {
		
		ImageTextLabel mlt3 = (ImageTextLabel) contentSystem.createContentItem(ImageTextLabel.class);
		mlt3.setImageInfo(resource);
        ImageIcon image = new ImageIcon(resource);
        mlt3.setAutoFit(false);
        mlt3.setWidth(image.getIconWidth());
        mlt3.setHeight(image.getIconHeight());
        mlt3.setScale(0.3f);
        mlt3.setScaleLimit(0.2f, 2f);
		mlt3.setBorderSize(3);
		mlt3.setBorderColour(Color.white);
		mlt3.setBackgroundColour(Color.black);
		mlt3.setTextColour(Color.lightGray);
		mlt3.makeFlickable(0.5f);
		mlt3.setBoundaryEnabled(false);
		mlt3.centerItem();
	}


	/** The text images. */
	String[] textImages = {
			"text_beijing.jpg",
			"text_brasilia.jpg",
			"text_canberra.jpg",
			"text_newdelhi.jpg"
	};
	
	/** The red table. */
	String[] redTable = {
			"australia_flag.jpg",
			"australia_kangaroo.jpg",
			"australia_map.jpg",
			"australia_Sydney.jpg",
	};
			
	/** The blue table. */
	String[] blueTable = {
			"brasil_flag.jpg",
			"brasil_llama.jpg",
			"brasil_map.jpg",
			"brasil_riodejaneirocorcovado.jpeg",
			
	};

	/** The green table. */
	String[] greenTable = {
			"china_ panda.jpg",
			"china_flag.jpg",
			"china_great_wall.jpg",
			"china_map.gif",
	};
	
	/** The yellow table. */
	String[] yellowTable = {
			"india_map.jpg",
			"india_Taj-Mahal.jpg",
			"india_tiger.jpg",
			"india-flag.jpg",
			
	};
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#onActivate()
	 */
	@Override
	public void onActivate(){
		if (networkedContentManager!=null) return;
		try{
			if(comms == null) {
				comms = (TableCommsClientService) ServiceManager.getInstance().get(TableCommsClientService.class);
				List<Class<?>> receiverClasses = new ArrayList<Class<?>>();
				receiverClasses.add(GeogNetworkFlickApp.class);
				networkedContentManager = new NetworkedContentManager(contentSystem, comms, receiverClasses);
				transferController = new TransferController(this, comms, networkedContentManager);
				networkedContentManager.getNetworkedFlickController().setNetworkFlickEnabled(true);
				messageHandler = new DefaultMessageHandler(contentSystem, this.networkedContentManager);
				((DefaultMessageHandler)messageHandler).setTransferController(transferController);
			}
			if(comms != null) comms.register(this, messageHandler);
			if(comms != null) comms.sendMessage(new ApplicationCommsRequest(GeogNetworkFlickApp.class.getName()));

			Preferences prefs = ConfigurationSystem.getPreferences(PositionConfigPrefsItem.class);
			int location_x = prefs.getInt(PositionConfigPrefsItem.PREFS_LOCATION_X, 0);
			int location_y = prefs.getInt(PositionConfigPrefsItem.PREFS_LOCATION_Y, 0);
			float angle = prefs.getFloat(PositionConfigPrefsItem.PREFS_ANGLE, 0);


			if (new PositionConfigPrefsItem().getDeveloperMode()){
				if (prefs.get(PositionConfigPrefsItem.HORIZONTAL_PLACEMENT, "false").equals("true")){
					if(prefs.getInt(PositionConfigPrefsItem.GRID_LIMIT_X, 0) != 0){
						int xPos = (prefs.getInt(PositionConfigPrefsItem.CURRENT_CONNECTIONS, 0)-1) % prefs.getInt(PositionConfigPrefsItem.GRID_LIMIT_X, 0);
						location_x = xPos * (prefs.getInt(PositionConfigPrefsItem.GRID_DISTANCE_X, 0));
						int yPos = (prefs.getInt(PositionConfigPrefsItem.CURRENT_CONNECTIONS, 0)-1) / prefs.getInt(PositionConfigPrefsItem.GRID_LIMIT_X, 0);
						location_y = yPos * (prefs.getInt(PositionConfigPrefsItem.GRID_DISTANCE_Y, 0));
					}else{
						location_x = (prefs.getInt(PositionConfigPrefsItem.CURRENT_CONNECTIONS, 0)-1) * (prefs.getInt(PositionConfigPrefsItem.GRID_DISTANCE_X, 0));
						location_y = 0;
					}
				}else{
					if(prefs.getInt(PositionConfigPrefsItem.GRID_LIMIT_Y, 0) != 0){
						int yPos = (prefs.getInt(PositionConfigPrefsItem.CURRENT_CONNECTIONS, 0)-1) % prefs.getInt(PositionConfigPrefsItem.GRID_LIMIT_Y, 0);
						location_y = yPos * (prefs.getInt(PositionConfigPrefsItem.GRID_DISTANCE_Y, 0));
						int xPos = (prefs.getInt(PositionConfigPrefsItem.CURRENT_CONNECTIONS, 0)-1) / prefs.getInt(PositionConfigPrefsItem.GRID_LIMIT_Y, 0);
						location_x = xPos * (prefs.getInt(PositionConfigPrefsItem.GRID_DISTANCE_X, 0));
					}else{
						location_y = (prefs.getInt(PositionConfigPrefsItem.CURRENT_CONNECTIONS, 0)-1) * (prefs.getInt(PositionConfigPrefsItem.GRID_DISTANCE_Y, 0));
						location_x = 0;
					}
				}
				angle = 0;
			}

			TableInfo tableInfo = new TableInfo(TableIdentity.getTableIdentity(),location_x, location_y, angle * FastMath.DEG_TO_RAD);
			transferController.setLocalTableInfo(tableInfo);
			for(Class<?> receiverClass: networkedContentManager.getReceiverClasses()){
				if(comms == null) return;
				comms.sendMessage( new AnnounceTableMessage(receiverClass, tableInfo));
				comms.sendMessage(new EnableFlickMessage(receiverClass,true));
			}
		}catch(IOException e){
			e.printStackTrace();

		} catch (CouldNotStartServiceException e) {
			e.printStackTrace();
		}

	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp#stateUpdate(float)
	 */
	@Override
	public void stateUpdate(float tpf) {
		super.stateUpdate(tpf);
		if(networkedContentManager!= null) networkedContentManager.stateUpdate(tpf);
		if(transferController != null) transferController.update();
		if(contentSystem != null) contentSystem.update(tpf);
	}
}
