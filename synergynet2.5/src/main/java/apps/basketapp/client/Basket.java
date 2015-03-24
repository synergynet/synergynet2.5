package apps.basketapp.client;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.OrthoContentItem;
import synergynetframework.appsystem.contentsystem.items.TextLabel;
import synergynetframework.appsystem.contentsystem.items.Window;
import synergynetframework.appsystem.contentsystem.items.listener.ScreenCursorListener;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import apps.basketapp.controller.ContentSubMenu;
import apps.basketapp.controller.mysteries.TableMystery;

import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.system.DisplaySystem;

/**
 * The Class Basket.
 */
public class Basket {

	/**
	 * The listener interface for receiving basket events. The class that is
	 * interested in processing a basket event implements this interface, and
	 * the object created with that class is registered with a component using
	 * the component's <code>addBasketListener<code> method. When
	 * the basket event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see BasketEvent
	 */
	public interface BasketListener {

		/**
		 * Item added.
		 *
		 * @param item
		 *            the item
		 */
		public void itemAdded(ContentItem item);

		/**
		 * Item detached.
		 *
		 * @param item
		 *            the item
		 */
		public void itemDetached(ContentItem item);
	}

	/**
	 * The Enum STATUS.
	 */
	public enum STATUS {
		/** The connected. */
		CONNECTED, /** The disconnected. */
		DISCONNECTED
	}

	/** The basket items. */
	private List<ContentItem> basketItems = new ArrayList<ContentItem>();

	/** The border width. */
	protected int borderWidth = 20;

	/** The content system. */
	protected ContentSystem contentSystem;

	/** The default scale. */
	protected float defaultScale = 0.42f;

	/** The label. */
	protected TextLabel label;

	/** The listeners. */
	private List<BasketListener> listeners = new ArrayList<BasketListener>();

	/** The table id. */
	protected TableIdentity tableId;

	/** The hight. */
	protected int width = (int) (DisplaySystem.getDisplaySystem().getWidth() * 0.5f),
			hight = (int) (DisplaySystem.getDisplaySystem().getHeight() * 0.5f);;

	/** The window. */
	protected Window window;

	/**
	 * Instantiates a new basket.
	 *
	 * @param contentSystem
	 *            the content system
	 * @param tableId
	 *            the table id
	 */
	public Basket(ContentSystem contentSystem, TableIdentity tableId) {
		this.contentSystem = contentSystem;
		this.tableId = tableId;
		this.contentSystem = contentSystem;
		window = (Window) contentSystem.createContentItem(Window.class);
		window.setWidth(width);
		window.setHeight(hight);
		window.setScale(defaultScale);
		window.setAsBottomObject();
		window.setRotateTranslateScalable(true);
		window.setBorderSize(borderWidth);
		window.setBorderColour(Color.white);
		window.setRotateTranslateScalable(false);
		window.setBringToTopable(false);
		window.centerItem();
		this.setColor(tableId);
	}

	/**
	 * Adds the basket listener.
	 *
	 * @param listener
	 *            the listener
	 */
	public void addBasketListener(BasketListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	/**
	 * Adds the item.
	 *
	 * @param basketItem
	 *            the basket item
	 * @return true, if successful
	 */
	public boolean addItem(final ContentItem basketItem) {
		if ((basketItem == null)
				|| !contentSystem.getAllContentItems().containsKey(
						basketItem.getName())) {
			return false;
		}
		if (basketItems.contains(basketItem)) {
			return false;
		}
		basketItems.add(basketItem);

		window.addSubItem(basketItem);
		((OrthoContentItem) basketItem).setAsTopObject();
		((OrthoContentItem) basketItem).setRotateTranslateScalable(true);
		window.setRotateTranslateScalable(false);
		((OrthoContentItem) basketItem)
				.addScreenCursorListener(new ScreenCursorListener() {
					@Override
					public void screenCursorChanged(ContentItem item, long id,
							float x, float y, float pressure) {
						Spatial ws = (Spatial) window.getImplementationObject();
						Vector3f localv = new Vector3f();
						ws.worldToLocal(new Vector3f(x, y, 0), localv);
						basketItem.setLocalLocation(localv.x, localv.y);
					}
					
					@Override
					public void screenCursorClicked(ContentItem item, long id,
							float x, float y, float pressure) {
						
					}
					
					@Override
					public void screenCursorPressed(ContentItem item, long id,
							float x, float y, float pressure) {
						((OrthoContentItem) basketItem).setOrder(999999);
					}
					
					@Override
					public void screenCursorReleased(ContentItem item, long id,
							float x, float y, float pressure) {
						
					}
				});

		for (BasketListener listener : listeners) {
			listener.itemAdded(basketItem);
		}
		return true;
	}

	/**
	 * Adds the items.
	 *
	 * @param items
	 *            the items
	 */
	public void addItems(List<ContentItem> items) {
		for (ContentItem item : items) {
			addItem(item);
		}
	}
	
	/**
	 * Clear.
	 */
	public void clear() {
		Iterator<ContentItem> iter = basketItems.iterator();
		while (iter.hasNext()) {
			ContentItem item = iter.next();
			window.removeSubItem(item);
			((OrthoContentItem) item).removeScreenCursorListeners();
			((OrthoContentItem) item).setScale(1);
			((OrthoContentItem) item).setRotateTranslateScalable(true);
			if (item.getNote().equalsIgnoreCase(ContentSubMenu.TABLE)) {
				((OrthoContentItem) item).setScaleLimit(TableMystery.MIN_SCALE,
						TableMystery.MAX_SCALE);
				((OrthoContentItem) item).setScale(TableMystery.INITIAL_SCALE);
			}
			iter.remove();
		}
	}

	/**
	 * Contains item.
	 *
	 * @param item
	 *            the item
	 * @return true, if successful
	 */
	public boolean containsItem(ContentItem item) {
		return basketItems.contains(item);
	}

	/**
	 * Detach item.
	 *
	 * @param item
	 *            the item
	 * @return true, if successful
	 */
	public boolean detachItem(ContentItem item) {
		if (!basketItems.contains(item)) {
			return false;
		}
		basketItems.remove(item);
		window.detachSubItem(item);
		((OrthoContentItem) item).removeScreenCursorListeners();
		((OrthoContentItem) item).setScale(1);
		((OrthoContentItem) item).setRotateTranslateScalable(true);
		((OrthoContentItem) item).setBringToTopable(true);
		if (item.getNote().equalsIgnoreCase(ContentSubMenu.TABLE)) {
			((OrthoContentItem) item).setScaleLimit(TableMystery.MIN_SCALE,
					TableMystery.MAX_SCALE);
			((OrthoContentItem) item).setScale(TableMystery.INITIAL_SCALE);
		}
		for (BasketListener listener : listeners) {
			listener.itemDetached(item);
		}
		
		return true;
	}

	/**
	 * Detach items.
	 *
	 * @param items
	 *            the items
	 */
	public void detachItems(List<ContentItem> items) {
		Iterator<ContentItem> iter = items.iterator();
		while (iter.hasNext()) {
			ContentItem item = iter.next();
			window.detachSubItem(item);
			((OrthoContentItem) item).removeScreenCursorListeners();
			((OrthoContentItem) item).setScale(1);
			((OrthoContentItem) item).setRotateTranslateScalable(true);
			iter.remove();
		}
	}

	/**
	 * Gets the items.
	 *
	 * @return the items
	 */
	public List<ContentItem> getItems() {
		return basketItems;
	}

	/**
	 * Gets the table id.
	 *
	 * @return the table id
	 */
	public TableIdentity getTableId() {
		return tableId;
	}

	/**
	 * Gets the window.
	 *
	 * @return the window
	 */
	public Window getWindow() {
		return window;
	}

	/**
	 * Removes the basket listener.
	 *
	 * @param listener
	 *            the listener
	 */
	public void removeBasketListener(BasketListener listener) {
		if (listeners.contains(listener)) {
			listeners.remove(listener);
		}
	}
	
	/**
	 * Sets the color.
	 *
	 * @param tableId
	 *            the new color
	 */
	private void setColor(TableIdentity tableId) {
		Color color = null;
		try {
			Field field = Class.forName("java.awt.Color").getField(
					tableId.toString());
			color = (Color) field.get(null);
		} catch (Exception e) {
			color = Color.getHSBColor((new Random()).nextFloat(), 1.0F, 1.0F);
		}
		window.setBackgroundColour(color);
	}

	/**
	 * Sets the status.
	 *
	 * @param status
	 *            the new status
	 */
	public void setStatus(STATUS status) {
		if (status.equals(STATUS.CONNECTED)) {

		} else {

		}
		
	}
}
