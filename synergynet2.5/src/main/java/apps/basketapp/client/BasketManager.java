package apps.basketapp.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;

import core.SynergyNetDesktop;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.OrthoContentItem;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.RapidNetworkManager;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.jme.mtinputbridge.MultiTouchInputFilterManager;
import synergynetframework.jme.pickingsystem.IJMEMultiTouchPicker;
import synergynetframework.jme.pickingsystem.PickSystemException;
import synergynetframework.jme.pickingsystem.data.PickRequest;
import synergynetframework.jme.pickingsystem.data.PickResultData;
import synergynetframework.mtinput.IMultiTouchEventListener;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;
import synergynetframework.mtinput.events.MultiTouchObjectEvent;

public class BasketManager implements IMultiTouchEventListener{
	
	private List<Basket> baskets = new ArrayList<Basket>();
	private IJMEMultiTouchPicker pickSystem;
	protected Node orthoNode;
	protected ContentSystem contentSystem;

	protected float checkDelay = 2f;
	protected float frameRate = checkDelay;
	
	public BasketManager(DefaultSynergyNetApp app){
		pickSystem = MultiTouchInputFilterManager.getInstance().getPickingSystem();
		orthoNode = app.getOrthoNode();
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(app);
		SynergyNetDesktop.getInstance().getMultiTouchInputComponent().registerMultiTouchEventListener(this);
	}
	
	@Override
	public void cursorPressed(MultiTouchCursorEvent event) {

	}

	@Override
	public void cursorReleased(MultiTouchCursorEvent event) {
		int x = SynergyNetDesktop.getInstance().tableToScreenX(event.getPosition().x);
		int y = SynergyNetDesktop.getInstance().tableToScreenY(event.getPosition().y);
		List<Spatial> spatials = getPickedSpatials(event.getCursorID(), new Vector2f(x, y));
		Basket targetBasket = getPickedBasket(spatials);
		List<ContentItem> pickedItems = getPickedItems(spatials);
		for(ContentItem item: pickedItems){
			if(targetBasket != null){
				if(!targetBasket.getWindow().getAllItemsIncludeSystemItems().contains(item)){
					//put item in basket
					targetBasket.addItem(item);
					Spatial windowSpatial = (Spatial) targetBasket.getWindow().getBackgroundFrame().getImplementationObject();
					Vector3f newLoc = new Vector3f();
					windowSpatial.worldToLocal(new Vector3f(x,y,0), newLoc);
					item.setLocalLocation(newLoc.x, newLoc.y);
				}
			}else if(targetBasket == null){
				Basket containerBasket = getContainerBasketForItem(item);
				if(containerBasket != null){
					// take item out of basket
					containerBasket.detachItem(item);
					item.setLocalLocation(x, y);
					((OrthoContentItem)item).setOrder(999999);
					((Spatial)item.getImplementationObject()).updateRenderState();
				}
			}
		}
	}

	@Override
	public void cursorClicked(MultiTouchCursorEvent event) {
		 
		
	}

	@Override
	public void cursorChanged(MultiTouchCursorEvent event) {
		 
		
	}

	@Override
	public void objectAdded(MultiTouchObjectEvent event) {
		 
		
	}

	@Override
	public void objectRemoved(MultiTouchObjectEvent event) {
		 
		
	}

	@Override
	public void objectChanged(MultiTouchObjectEvent event) {
		 
		
	}
	
	private List<ContentItem> getPickedItems(List<Spatial> spatials){
		List<ContentItem> items = new ArrayList<ContentItem>();
		for(Spatial spatial: spatials){
			if(!isBasket(spatial.getName())){
				ContentItem item = contentSystem.getContentItem(spatial.getName());
				items.add(item);
			}
		}
		return items;
	}
	
	private List<Spatial> getPickedSpatials(long id, Vector2f position)
	{
		PickRequest req = new PickRequest(id, position);
		List<PickResultData> pickResults;
		List<Spatial> pickedSpatials = new ArrayList<Spatial>();
		try {
				pickResults = pickSystem.doPick(req);
				for(PickResultData pr : pickResults) {
					Spatial targetSpatial = pr.getPickedSpatial();
					while(targetSpatial.getParent() != null && targetSpatial.getParent()!= orthoNode && !isBasket(targetSpatial.getParent().getName())){
						targetSpatial = targetSpatial.getParent();
					}
					pickedSpatials.add(targetSpatial);
				}
			}
		catch (PickSystemException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return pickedSpatials;
	}
	
	private boolean isBasket(String objectName) {
		for(Basket basket: baskets){
			if(basket.getWindow().getName().equalsIgnoreCase(objectName) || basket.getWindow().getBackgroundFrame().getName().equalsIgnoreCase(objectName))
				return true;
		}
		return false;
	}
	
	private Basket getPickedBasket(List<Spatial> spatials){
		for(Basket basket: baskets){
			for(Spatial spatial: spatials){
				if(basket.getWindow().getName().equals(spatial.getName()) || basket.getWindow().getBackgroundFrame().getName().equals(spatial.getName()))
					return basket;
			}
		}
		return null;
	}
	
	private Basket getContainerBasketForItem(ContentItem item){
		for(Basket basket: baskets)
			if(basket.getWindow().getAllItemsIncludeSystemItems().contains(item)) return basket;
		return null;
	}
	
	public void registerBasket(Basket basket){
		if(!baskets.contains(basket))
			baskets.add(basket);
	}
	
	public void unregisterBasket(Basket basket){
		if(baskets.contains(basket)){
			contentSystem.removeContentItem(basket.getWindow());
			baskets.remove(basket);
		}
	}

	public boolean isRegistered(TableIdentity tableId){
		for(Basket basket: baskets){
			if(basket.getTableId().equals(tableId))
					return true;
		}
		return false;
	}
	
	public Basket getBasket(TableIdentity tableId){
		for(Basket basket: baskets){
			if(basket.getTableId().equals(tableId))
					return basket;
		}
		return null;
	}

	public List<Basket> getBaskets(){
		return baskets;
	}
	
	public void update(float tpf) {
		
		if((frameRate - tpf) > 0){
			frameRate-= tpf;
			return;
		}
		checkConnectivity();
		frameRate = checkDelay;
	}
	
	private void checkConnectivity() {
		
		List<Basket> temp = new ArrayList<Basket>();
		if(RapidNetworkManager.getTableCommsClientService() == null) temp.addAll(baskets);
		if(RapidNetworkManager.getTableCommsClientService() != null){
			List<TableIdentity> onlineTables = RapidNetworkManager.getTableCommsClientService().getCurrentlyOnline();
			for(Basket basket: baskets){
				if(!onlineTables.contains(basket.getTableId())){
					temp.add(basket);
				}
			}
		}

		for(Basket basket: temp){
			this.unregisterBasket(basket);
			basket.setStatus(Basket.STATUS.CONNECTED);
		}
		temp.clear();
	}

	public void swapBaskets() {
		if(RapidNetworkManager.getTableCommsClientService() != null){
			for(Basket basket: baskets){
				if(!basket.getTableId().equals(TableIdentity.getTableIdentity())){
					try {
						RapidNetworkManager.postItems(basket.getItems(), basket.getTableId());
						Thread.sleep(1000);
						basket.clear();
					} catch (IOException e) {
						 
						e.printStackTrace();
					} catch (InterruptedException e) {
						 
						e.printStackTrace();
					}
				}
			}
		}
	}
}
