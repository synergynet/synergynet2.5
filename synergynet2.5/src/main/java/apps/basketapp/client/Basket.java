package apps.basketapp.client;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import apps.basketapp.controller.ContentSubMenu;
import apps.basketapp.controller.mysteries.TableMystery;

import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.system.DisplaySystem;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.OrthoContentItem;
import synergynetframework.appsystem.contentsystem.items.TextLabel;
import synergynetframework.appsystem.contentsystem.items.Window;
import synergynetframework.appsystem.contentsystem.items.listener.ScreenCursorListener;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;

public class Basket {
	
	protected TableIdentity tableId;
	protected ContentSystem contentSystem;
	protected Window window;
	protected TextLabel label;
	protected int width = (int)(DisplaySystem.getDisplaySystem().getWidth() * 0.5f), hight = (int)(DisplaySystem.getDisplaySystem().getHeight() * 0.5f);
	protected float defaultScale = 0.42f;
	protected int borderWidth = 20;
	private List<ContentItem> basketItems = new ArrayList<ContentItem>();
	
	private List<BasketListener> listeners = new ArrayList<BasketListener>();
	
	public enum STATUS{CONNECTED, DISCONNECTED};
	
	public Basket(ContentSystem contentSystem, TableIdentity tableId){
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
	
	public void addBasketListener(BasketListener listener){
		if(!listeners.contains(listener))
			listeners.add(listener);
	}
	
	public void removeBasketListener(BasketListener listener){
		if(listeners.contains(listener))
			listeners.remove(listener);
	}
	
	public void setStatus(STATUS status){
		if(status.equals(STATUS.CONNECTED)){
			
		}else{
			
		}
	
	}
	
	private void setColor(TableIdentity tableId) {
		Color color = null;
		try {
			    Field field = Class.forName("java.awt.Color").getField(tableId.toString());
			    color = (Color)field.get(null);
			} catch (Exception e) {
				color = Color.getHSBColor( (new Random()).nextFloat(), 1.0F, 1.0F );
			}		
		window.setBackgroundColour(color);	
	}

	public boolean addItem(final ContentItem basketItem){
		if(basketItem == null || !contentSystem.getAllContentItems().containsKey(basketItem.getName()))
			return false;
		if(basketItems.contains(basketItem))
			return false;
		basketItems.add(basketItem);
		
		window.addSubItem(basketItem);
		((OrthoContentItem)basketItem).setAsTopObject();
		((OrthoContentItem)basketItem).setRotateTranslateScalable(true);
		window.setRotateTranslateScalable(false);
		((OrthoContentItem)basketItem).addScreenCursorListener(new ScreenCursorListener(){
			@Override
			public void screenCursorPressed(ContentItem item, long id, float x,	float y, float pressure) {
				((OrthoContentItem)basketItem).setOrder(999999);
			}

			@Override
			public void screenCursorChanged(ContentItem item, long id, float x,
					float y, float pressure) {
				Spatial ws = (Spatial) window.getImplementationObject();
				Vector3f localv = new Vector3f();
				ws.worldToLocal(new Vector3f(x,y,0), localv);
				basketItem.setLocalLocation(localv.x, localv.y);
			}

			@Override
			public void screenCursorReleased(ContentItem item, long id,
					float x, float y, float pressure) {
				 
				
			}

			@Override
			public void screenCursorClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				 
				
			}
		});
		
		for(BasketListener listener: listeners)
			listener.itemAdded(basketItem);
		return true;
	}
	
	public void addItems(List<ContentItem> items){
		for(ContentItem item: items)
			addItem(item);
	}
	
	public boolean detachItem(ContentItem item){
		if(!basketItems.contains(item)) 
			return false;
		basketItems.remove(item);
		window.detachSubItem(item);
		((OrthoContentItem)item).removeScreenCursorListeners();
		((OrthoContentItem)item).setScale(1);
		((OrthoContentItem)item).setRotateTranslateScalable(true);
		((OrthoContentItem)item).setBringToTopable(true);		
		if(item.getNote().equalsIgnoreCase(ContentSubMenu.TABLE)){
			((OrthoContentItem)item).setScaleLimit(TableMystery.MIN_SCALE, TableMystery.MAX_SCALE);
			((OrthoContentItem)item).setScale(TableMystery.INITIAL_SCALE);
		}		
		for(BasketListener listener: listeners)
			listener.itemDetached(item);

		return true;
	}
	
	public void detachItems(List<ContentItem> items){
		Iterator<ContentItem> iter = items.iterator();
		while(iter.hasNext()){
			ContentItem item = iter.next();
			window.detachSubItem(item);
			((OrthoContentItem)item).removeScreenCursorListeners();
			((OrthoContentItem)item).setScale(1);
			((OrthoContentItem)item).setRotateTranslateScalable(true);
			iter.remove();
		}
	}
	
	public void clear(){
		Iterator<ContentItem> iter = basketItems.iterator();
		while(iter.hasNext()){
			ContentItem item = iter.next();
			window.removeSubItem(item);
			((OrthoContentItem)item).removeScreenCursorListeners();
			((OrthoContentItem)item).setScale(1);
			((OrthoContentItem)item).setRotateTranslateScalable(true);
			if(item.getNote().equalsIgnoreCase(ContentSubMenu.TABLE)){
				((OrthoContentItem)item).setScaleLimit(TableMystery.MIN_SCALE, TableMystery.MAX_SCALE);
				((OrthoContentItem)item).setScale(TableMystery.INITIAL_SCALE);
			}	
			iter.remove();
		}
	}
	
	public List<ContentItem> getItems(){
		return basketItems;
	}
	
	public Window getWindow(){
		return window;
	}
	
	public TableIdentity getTableId(){
		return tableId;
	}

	public boolean containsItem(ContentItem item) {
		return basketItems.contains(item);
	}
	
	public interface BasketListener{
		public void itemAdded(ContentItem item);
		public void itemDetached(ContentItem item);
	}
}
