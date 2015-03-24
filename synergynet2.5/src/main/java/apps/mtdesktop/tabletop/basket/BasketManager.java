package apps.mtdesktop.tabletop.basket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
import apps.mtdesktop.desktop.DesktopClient;
import apps.mtdesktop.messages.BasketItemMessage;
import apps.mtdesktop.tabletop.TabletopContentManager.TabletopContentManagerListener;
import core.SynergyNetDesktop;

/**
 * The Class BasketManager.
 */
public class BasketManager implements TabletopContentManagerListener {
	
	/** The image item. */
	public static String IMAGE_ITEM = "image";

	/** The pdf item. */
	public static String PDF_ITEM = "pdf";

	/** The text item. */
	public static String TEXT_ITEM = "text";

	/** The video item. */
	public static String VIDEO_ITEM = "video";

	/** The baskets. */
	protected Map<TableIdentity, JmeNetworkedBasket> baskets = new HashMap<TableIdentity, JmeNetworkedBasket>();

	/** The content system. */
	protected ContentSystem contentSystem;

	/** The io controller. */
	protected InputOutputBasketController ioController;

	/** The is transfer enabled. */
	protected boolean isTransferEnabled = true;

	/**
	 * Instantiates a new basket manager.
	 *
	 * @param app
	 *            the app
	 */
	public BasketManager(final DefaultSynergyNetApp app) {
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(app);
		ioController = new InputOutputBasketController(this, app);
		SynergyNetDesktop.getInstance().getMultiTouchInputComponent()
				.registerMultiTouchEventListener(ioController);
	}

	/**
	 * Gets the baskets.
	 *
	 * @return the baskets
	 */
	public Map<TableIdentity, JmeNetworkedBasket> getBaskets() {
		return baskets;
	}

	/**
	 * Gets the content system.
	 *
	 * @return the content system
	 */
	public ContentSystem getContentSystem() {
		return contentSystem;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * apps.mtdesktop.tabletop.TabletopContentManager.TabletopContentManagerListener
	 * #itemReceived(synergynetframework.appsystem.services.net.localpresence.
	 * TableIdentity,
	 * synergynetframework.appsystem.contentsystem.items.ContentItem)
	 */
	@Override
	public void itemReceived(TableIdentity tableId, ContentItem item) {
		JmeNetworkedBasket localBasket = baskets.get(tableId);
		if (localBasket != null) {
			localBasket.addItem(item);
			ioController.remoteItemReceived(item);
		}
	}
	
	/**
	 * Register basket for peer.
	 *
	 * @param tableId
	 *            the table id
	 * @param position
	 *            the position
	 * @return the jme networked basket
	 */
	public JmeNetworkedBasket registerBasketForPeer(TableIdentity tableId,
			DesktopClient.Position position) {
		if (!baskets.containsKey(tableId)) {
			JmeNetworkedBasket basket = new JmeNetworkedBasket(contentSystem);
			basket.setPosition(position);
			basket.setTableId(tableId);
			baskets.put(tableId, basket);
		} else {
			baskets.get(tableId).setTableId(tableId);
		}
		return baskets.get(tableId);
	}

	/**
	 * Send item to remote basket.
	 *
	 * @param item
	 *            the item
	 * @param tableId
	 *            the table id
	 */
	public void sendItemToRemoteBasket(ContentItem item, TableIdentity tableId) {
		try {
			Map<String, Object> itemInfo = new HashMap<String, Object>();
			if (item instanceof HQPDFViewer) {
				itemInfo.put(PDF_ITEM, ((HQPDFViewer) item).getPdfURL());
			} else if (item instanceof LightImageLabel) {
				itemInfo.put(IMAGE_ITEM,
						((LightImageLabel) item).getImageResource());
			} else if (item instanceof MediaPlayer) {
				itemInfo.put(VIDEO_ITEM, ((MediaPlayer) item).getMediaURL());
			} else if (item instanceof HtmlFrame) {
				String html = ((HtmlFrame) item).getHtmlContent();
				TextExtractor exct = new TextExtractor(new Source(html));
				itemInfo.put(TEXT_ITEM, exct.toString());
			}

			BasketItemMessage msg = new BasketItemMessage(DesktopClient.class,
					tableId);
			msg.setItemInfo(itemInfo);
			System.out.println("item sent to desktop app");
			try {
				RapidNetworkManager.getTableCommsClientService().sendMessage(
						msg);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
	
	/**
	 * Unregister basket.
	 *
	 * @param tableId
	 *            the table id
	 */
	public void unregisterBasket(TableIdentity tableId) {
		if (baskets.containsKey(tableId)) {
			JmeNetworkedBasket basket = baskets.get(tableId);
			contentSystem.removeContentItem(basket.getWindow());
			baskets.remove(tableId);
		}
	}

	/**
	 * Update.
	 *
	 * @param tpf
	 *            the tpf
	 */
	public void update(float tpf) {
		if (ioController != null) {
			ioController.update(tpf);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * apps.mtdesktop.tabletop.TabletopContentManager.TabletopContentManagerListener
	 * #
	 * vncClientClosed(synergynetframework.appsystem.services.net.localpresence.
	 * TableIdentity,
	 * synergynetframework.appsystem.contentsystem.items.VncFrame)
	 */
	@Override
	public void vncClientClosed(TableIdentity tableId, VncFrame vncFrame) {
		JmeNetworkedBasket localBasket = baskets.get(tableId);
		if (localBasket != null) {
			localBasket.unlinkItem(vncFrame);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * apps.mtdesktop.tabletop.TabletopContentManager.TabletopContentManagerListener
	 * #
	 * vncClientLaunched(synergynetframework.appsystem.services.net.localpresence
	 * .TableIdentity,
	 * synergynetframework.appsystem.contentsystem.items.VncFrame)
	 */
	@Override
	public void vncClientLaunched(TableIdentity tableId, VncFrame vncFrame) {
		JmeNetworkedBasket localBasket = baskets.get(tableId);
		if (localBasket != null) {
			vncFrame.setAngle(localBasket.getWindow().getAngle());
			localBasket.linkItem(vncFrame);
		}
	}
	
}
