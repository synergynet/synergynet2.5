package apps.mysteriestableportal;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import apps.mysteriestableportal.messages.AnnounceProjectorMessage;
import apps.mysteriestableportal.messages.ProjectorPostItemsMessage;
import apps.mysteriestableportal.messages.SearchProjectorsMessage;

import com.jme.system.DisplaySystem;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.Frame;
import synergynetframework.appsystem.contentsystem.items.OrthoContentItem;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.RapidNetworkManager;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.handlers.MessageProcessor;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.handlers.NetworkedContentMessageProcessor.NetworkedContentListener;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.appsystem.table.appregistry.menucontrol.HoldTopRightExit;

public class ProjectorApp extends DefaultSynergyNetApp{

	protected ContentSystem contentSystem;
	
	public ProjectorApp(ApplicationInfo info) {
		super(info);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addContent() {
		setMenuController(new HoldTopRightExit());
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(this);
		contentSystem.removeAllContentItems();
	}
	
	@Override
	public void onActivate() {
		RapidNetworkManager.addNetworkedContentListener(new NetworkedContentListener(){

			@Override
			public void itemsReceived(List<ContentItem> item,
					TableIdentity tableId) {
				 
				
			}

			@Override
			public void tableDisconnected() {

			}

			@Override
			public void tableConnected() {
				announce();
			}
			
		});
		RapidNetworkManager.registerMessageProcessor(new MessageProcessor(){

			@Override
			public void process(Object obj) {
				if(obj instanceof SearchProjectorsMessage){
					announce();
				}else if(obj instanceof ProjectorPostItemsMessage){
					contentSystem.removeAllContentItems();
					Frame frame = (Frame) contentSystem.createContentItem(Frame.class);
					frame.setWidth(DisplaySystem.getDisplaySystem().getWidth());
					frame.setHeight(DisplaySystem.getDisplaySystem().getHeight());
					frame.setBackgroundColour(Color.black);
					frame.setBorderSize(20);
					frame.setOrder(-99999);
					frame.setRotateTranslateScalable(false);
					frame.setBringToTopable(false);

					TableIdentity sourceTableId = ((ProjectorPostItemsMessage)obj).getSourceTableId();
					if(sourceTableId.toString().equals("red")){
						frame.setBorderColour(Color.red);
					}else if(sourceTableId.toString().equals("green")){
						frame.setBorderColour(Color.green);
					}else if(sourceTableId.toString().equals("yellow")){
						frame.setBorderColour(Color.yellow);
					}else if(sourceTableId.toString().equals("blue")){
						frame.setBorderColour(Color.blue);
					}

					
					List<ContentItem> items = ((ProjectorPostItemsMessage)obj).getItems();
					for(ContentItem item: items){
						if(contentSystem.getAllContentItems().containsKey(item.getName())) return;
						
						contentSystem.addContentItem(item);

						((OrthoContentItem)item).setBringToTopable(true);
						((OrthoContentItem)item).setRotateTranslateScalable(true);
						float oldX = ((OrthoContentItem)item).getLocation().x;
						float oldY = ((OrthoContentItem)item).getLocation().y;
						((OrthoContentItem)item).setLocation(oldX+ DisplaySystem.getDisplaySystem().getWidth()/2, oldY+ DisplaySystem.getDisplaySystem().getHeight()/2);
					}
					
					frame.setLocation(DisplaySystem.getDisplaySystem().getWidth()/2, DisplaySystem.getDisplaySystem().getHeight()/2);
				}
			}
			
		});
		RapidNetworkManager.setAutoReconnect(true);
		RapidNetworkManager.connect(this);
	}
	
	public void announce(){
		try {
			RapidNetworkManager.getTableCommsClientService().sendMessage(new AnnounceProjectorMessage(ControllerApp.class));
		} catch (IOException e) {
			 
			e.printStackTrace();
		}
	}

}
