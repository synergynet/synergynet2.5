package apps.mtdesktop.tabletop.basket;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import apps.mtdesktop.MTDesktopConfigurations;
import apps.mtdesktop.desktop.DesktopClient;
import apps.mtdesktop.tabletop.MTTableClient;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.system.DisplaySystem;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.LightImageLabel;
import synergynetframework.appsystem.contentsystem.items.LineItem;
import synergynetframework.appsystem.contentsystem.items.OrthoContentItem;
import synergynetframework.appsystem.contentsystem.items.Window;
import synergynetframework.appsystem.contentsystem.items.listener.OrthoControlPointRotateTranslateScaleListener;
import synergynetframework.appsystem.contentsystem.items.listener.ScreenCursorListener;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;

public class JmeNetworkedBasket{

	
	protected TableIdentity tableId;
	protected ContentSystem contentSystem;
	protected Window window;
	//protected TextLabel label;
	protected int width = (int)(0.6 * DisplaySystem.getDisplaySystem().getWidth()), hight = (int)(0.6 *DisplaySystem.getDisplaySystem().getHeight());
	protected float defaultScale = 0.35f;
	protected int borderWidth = 20;
	protected DesktopClient.Position currentPosition = DesktopClient.Position.NORTH;
	protected LightImageLabel bin, copy, note;
	protected List<Class<? extends ContentItem>> excludedItems = new ArrayList<Class<? extends ContentItem>>();
	protected Map<ContentItem, LineItem> linkMap = new HashMap<ContentItem, LineItem>();
	
	public JmeNetworkedBasket(ContentSystem contentSystem){
		this.contentSystem = contentSystem;
		window = (Window) contentSystem.createContentItem(Window.class);
		window.setWidth(width);
		window.setHeight(hight);
		window.setScale(defaultScale);
		window.setAsTopObject();
		window.setBackgroundColour(Color.getHSBColor( (new Random()).nextFloat(), 1.0F, 1.0F ));
        
		window.setBorderSize(borderWidth);
		window.setBorderColour(Color.white);
		//label = (TextLabel) contentSystem.createContentItem(TextLabel.class);
		//label.setBorderSize(0);
		//label.setBackgroundColour(Color.white);
		//label.setScale(1/defaultScale);
		//label.setLocalLocation((label.getWidth() - window.getWidth())/2, (window.getHeight() - label.getHeight())/2) ;
		//window.addSubItem(label);
		
		bin = (LightImageLabel) contentSystem.createContentItem(LightImageLabel.class);
		bin.setAutoFitSize(false);
		bin.drawImage(MTDesktopConfigurations.class.getResource("tabletop/bin.png"));
		bin.setWidth(180);
		bin.setHeight(180);
		bin.setLocalLocation(-window.getWidth()+bin.getWidth(), 150);
		
		copy = (LightImageLabel) contentSystem.createContentItem(LightImageLabel.class);
		copy.setAutoFitSize(false);
		copy.drawImage(MTDesktopConfigurations.class.getResource("tabletop/copy.png"));
		copy.setWidth(180);
		copy.setHeight(180);
		copy.setLocalLocation(-window.getWidth()+copy.getWidth(), -150);
		window.addSubItem(bin);
		window.addSubItem(copy);
		
		window.setRotateTranslateScalable(false);
		window.setBringToTopable(false);
		
		((Spatial)window.getBackgroundFrame().getImplementationObject()).setZOrder(MTTableClient.background.getOrder()+1);

	}
	
	public TableIdentity getTableId() {
		return tableId;
	}

	public void setTableId(TableIdentity tableId) {
		this.tableId = tableId;
		String idLabel = tableId.toString();
		if(idLabel.length() >10) idLabel = idLabel.substring(0, 10)+"..";
		//label.setText(idLabel);
		//label.setLocalLocation(0 , window.getHeight()/2- label.getHeight()) ;
	}
	
	public void addItem(final ContentItem basketItem){
		window.addSubItem(basketItem);
		((OrthoContentItem)basketItem).setOrder(999999);
		((OrthoContentItem)basketItem).addScreenCursorListener(new ScreenCursorListener(){
			@Override
			public void screenCursorPressed(ContentItem item, long id, float x,	float y, float pressure) {}

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

	}
	
	public void detachItem(ContentItem item){
		window.detachSubItem(item);
		((OrthoContentItem)item).removeScreenCursorListeners();
		((OrthoContentItem)item).setScale(1);
		((OrthoContentItem)item).setRotateTranslateScalable(true);
		for(ContentItem i: window.getAllItemsIncludeSystemItems()) ((OrthoContentItem)i).setRotateTranslateScalable(false);
	}
	
	public void removeItem(ContentItem item){
		((OrthoContentItem)item).removeScreenCursorListeners();
		window.removeSubItem(item);
	}
	
	public void setPosition(DesktopClient.Position position){
		this.currentPosition = position;
		if(position.equals(DesktopClient.Position.SOUTH)){
			window.setAngle(0);
			window.setLocalLocation(352, 105);
		}else if(position.equals(DesktopClient.Position.NORTH)){
			window.setAngle(180 * FastMath.DEG_TO_RAD);
			window.setLocalLocation(740, 674);			
		}else if(position.equals(DesktopClient.Position.EAST)){
			window.setAngle(90 * FastMath.DEG_TO_RAD);
			window.setLocalLocation(909, 249);
		}else if(position.equals(DesktopClient.Position.WEST)){
			window.setAngle(270 * FastMath.DEG_TO_RAD);
			window.setLocalLocation(124, 517);
		}
	}
	
	public Window getWindow(){
		return window;
	}
	/*
	public TextLabel getTextLabel(){
		return label;
	}
	*/
	public DesktopClient.Position getPosition(){
		return this.currentPosition;
	}
	
	public String getComponentAt(float x, float y){
		Point.Float p = new Point.Float(x,y);
		if(window.getBackgroundFrame().contains(p)){
			return "basket";
		}else if(bin.contains(p)){
			return "bin";
		}else if(copy.contains(p)){
			return "copy";
		}else{
			return null;
		}
	}
	
	public boolean isBasketComponent(String spatialName){
		if(this.getWindow().getName().equals(spatialName) || this.getWindow().getBackgroundFrame().getName().equals(spatialName) || /*this.getTextLabel().getName().equals(spatialName) || */this.bin.getName().equals(spatialName) || this.copy.getName().equals(spatialName))
			return true;
		else
			return false;
	}
	
	public void excludeItem(Class<? extends ContentItem> itemClass){
		this.excludedItems.add(itemClass);
	}
	
	public List<Class<? extends ContentItem>> getExcludedItems(){
		return excludedItems;
	}

	public boolean isExcluded(ContentItem contentItem) {
		for(Class<? extends ContentItem> excludedClass: this.excludedItems){
			if(contentItem.getClass().equals(excludedClass))
				return true;
		}
		return false;
	}


	public void linkItem(ContentItem item) {
		if(item == null) return;
		item.setAngle(this.getWindow().getAngle());
		final LineItem line = (LineItem) item.getContentSystem().createContentItem(LineItem.class);
		line.setArrowMode(LineItem.NO_ARROWS);
		line.setLineMode(LineItem.CONNECTED_LINE);
		line.setLineColour(Color.black);
		line.setAsTopObject();
		line.removeItemListerners();
		line.setSourceLocation(this.getWindow().getLocalLocation());
		line.setTargetLocation(item.getLocalLocation());
		((OrthoContentItem)item).addOrthoControlPointRotateTranslateScaleListener(new OrthoControlPointRotateTranslateScaleListener(){

			@Override
			public void itemScaled(ContentItem item, float newScaleFactor,
					float oldScaleFactor) {
				line.setTargetLocation(item.getLocalLocation());
			}

			@Override
			public void itemTranslated(ContentItem item, float newLocationX,
					float newLocationY, float oldLocationX, float oldLocationY) {
				line.setTargetLocation(item.getLocalLocation());
			}

			@Override
			public void itemRotated(ContentItem item, float newAngle,
					float oldAngle) {
				line.setTargetLocation(item.getLocalLocation());
			}
			
		});
		linkMap.put(item, line);
	}
	
	public void unlinkItem(ContentItem item){
		if(linkMap.containsKey(item)){
			LineItem line = linkMap.get(item);
			item.getContentSystem().removeContentItem(line);
			linkMap.remove(item);
		}
	}
	
}
