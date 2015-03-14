package apps.mtdesktop.tabletop.basket;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import core.SynergyNetDesktop;

import apps.mtdesktop.desktop.DesktopClient;
import apps.mtdesktop.messages.BasketItemMessage;
import apps.mtdesktop.tabletop.TabletopContentManager.TabletopContentManagerListener;

import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.TextExtractor;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.HQPDFViewer;
import synergynetframework.appsystem.contentsystem.items.HtmlFrame;
import synergynetframework.appsystem.contentsystem.items.LightImageLabel;
import synergynetframework.appsystem.contentsystem.items.MediaPlayer;
import synergynetframework.appsystem.contentsystem.items.VncFrame;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.RapidNetworkManager;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;


public class BasketManager implements TabletopContentManagerListener{

	public static String PDF_ITEM = "pdf";
	public static String IMAGE_ITEM = "image";
	public static String TEXT_ITEM = "text";
	public static String VIDEO_ITEM = "video";
	
	protected Map<TableIdentity, JmeNetworkedBasket> baskets = new HashMap<TableIdentity, JmeNetworkedBasket>();
	protected ContentSystem contentSystem;
	protected InputOutputBasketController ioController;
	protected boolean isTransferEnabled = true;
	
	public BasketManager(final DefaultSynergyNetApp app) {
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(app);
		ioController = new InputOutputBasketController(this, app);
		SynergyNetDesktop.getInstance().getMultiTouchInputComponent().registerMultiTouchEventListener(ioController);
	}
	
	public JmeNetworkedBasket registerBasketForPeer(TableIdentity tableId, DesktopClient.Position position){
		if(!baskets.containsKey(tableId)){
			JmeNetworkedBasket basket = new JmeNetworkedBasket(contentSystem);
			basket.setPosition(position);
			basket.setTableId(tableId);
			baskets.put(tableId, basket);
		}else{
			baskets.get(tableId).setTableId(tableId);
		}
		return baskets.get(tableId);
	}
	
	public void unregisterBasket(TableIdentity tableId){
		if(baskets.containsKey(tableId)){
			JmeNetworkedBasket basket = baskets.get(tableId);
			contentSystem.removeContentItem(basket.getWindow());
			baskets.remove(tableId);
		}
	}
	
	public Map<TableIdentity, JmeNetworkedBasket> getBaskets(){
		return baskets;
	}

	public void sendItemToRemoteBasket(ContentItem item, TableIdentity tableId) {
		try{
			Map<String, Object> itemInfo = new HashMap<String, Object>();
			if(item instanceof HQPDFViewer){
				itemInfo.put(PDF_ITEM, ((HQPDFViewer)item).getPdfURL());
			}else if(item instanceof LightImageLabel){
				itemInfo.put(IMAGE_ITEM, ((LightImageLabel)item).getImageResource());
			}else if(item instanceof MediaPlayer){
				itemInfo.put(VIDEO_ITEM, ((MediaPlayer)item).getMediaURL());
			}else if(item instanceof HtmlFrame){
				String html = ((HtmlFrame)item).getHtmlContent();
				TextExtractor exct = new TextExtractor(new Source(html));
				itemInfo.put(TEXT_ITEM, exct.toString());
			}
			
			BasketItemMessage msg = new BasketItemMessage(DesktopClient.class, tableId);
			msg.setItemInfo(itemInfo);
			System.out.println("item sent to desktop app");
			try {
				RapidNetworkManager.getTableCommsClientService().sendMessage(msg);
			} catch (IOException e) {
				 
				e.printStackTrace();
			}
		}catch(Exception exp){
			exp.printStackTrace();
		}
	}
	
	public void update(float tpf){
		if(ioController != null) ioController.update(tpf);
	}

	public ContentSystem getContentSystem() {
		return contentSystem;
	}
	
	@Override
	public void itemReceived(TableIdentity tableId, ContentItem item) {
		JmeNetworkedBasket localBasket = baskets.get(tableId);
		if(localBasket != null){
			localBasket.addItem(item);
			ioController.remoteItemReceived(item);
		}
	}

	@Override
	public void vncClientLaunched(TableIdentity tableId, VncFrame vncFrame) {
		JmeNetworkedBasket localBasket = baskets.get(tableId);
		if(localBasket != null){
			vncFrame.setAngle(localBasket.getWindow().getAngle());
			localBasket.linkItem(vncFrame);
		}
	}

	@Override
	public void vncClientClosed(TableIdentity tableId, VncFrame vncFrame) {
		JmeNetworkedBasket localBasket = baskets.get(tableId);
		if(localBasket != null){
			localBasket.unlinkItem(vncFrame);
		}		
	}

}

