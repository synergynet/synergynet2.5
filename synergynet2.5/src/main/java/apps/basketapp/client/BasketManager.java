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


/**
 * The Class BasketManager.
 */
public class BasketManager implements IMultiTouchEventListener{
	
	/** The baskets. */
	private List<Basket> baskets = new ArrayList<Basket>();
	
	/** The pick system. */
	private IJMEMultiTouchPicker pickSystem;
	
	/** The ortho node. */
	protected Node orthoNode;
	
	/** The content system. */
	protected ContentSystem contentSystem;

	/** The check delay. */
	protected float checkDelay = 2f;
	
	/** The frame rate. */
	protected float frameRate = checkDelay;
	
	/**
	 * Instantiates a new basket manager.
	 *
	 * @param app the app
	 */
	public BasketManager(DefaultSynergyNetApp app){
		pickSystem = MultiTouchInputFilterManager.getInstance().getPickingSystem();
		orthoNode = app.getOrthoNode();
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(app);
		SynergyNetDesktop.getInstance().getMultiTouchInputComponent().registerMultiTouchEventListener(this);
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#cursorPressed(synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorPressed(MultiTouchCursorEvent event) {

	}

	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#cursorReleased(synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
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

	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#cursorClicked(synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorClicked(MultiTouchCursorEvent event) {
		 
		
	}

	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#cursorChanged(synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorChanged(MultiTouchCursorEvent event) {
		 
		
	}

	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#objectAdded(synergynetframework.mtinput.events.MultiTouchObjectEvent)
	 */
	@Override
	public void objectAdded(MultiTouchObjectEvent event) {
		 
		
	}

	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#objectRemoved(synergynetframework.mtinput.events.MultiTouchObjectEvent)
	 */
	@Override
	public void objectRemoved(MultiTouchObjectEvent event) {
		 
		
	}

	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#objectChanged(synergynetframework.mtinput.events.MultiTouchObjectEvent)
	 */
	@Override
	public void objectChanged(MultiTouchObjectEvent event) {
		 
		
	}
	
	/**
	 * Gets the picked items.
	 *
	 * @param spatials the spatials
	 * @return the picked items
	 */
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
	
	/**
	 * Gets the picked spatials.
	 *
	 * @param id the id
	 * @param position the position
	 * @return the picked spatials
	 */
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
	
	/**
	 * Checks if is basket.
	 *
	 * @param objectName the object name
	 * @return true, if is basket
	 */
	private boolean isBasket(String objectName) {
		for(Basket basket: baskets){
			if(basket.getWindow().getName().equalsIgnoreCase(objectName) || basket.getWindow().getBackgroundFrame().getName().equalsIgnoreCase(objectName))
				return true;
		}
		return false;
	}
	
	/**
	 * Gets the picked basket.
	 *
	 * @param spatials the spatials
	 * @return the picked basket
	 */
	private Basket getPickedBasket(List<Spatial> spatials){
		for(Basket basket: baskets){
			for(Spatial spatial: spatials){
				if(basket.getWindow().getName().equals(spatial.getName()) || basket.getWindow().getBackgroundFrame().getName().equals(spatial.getName()))
					return basket;
			}
		}
		return null;
	}
	
	/**
	 * Gets the container basket for item.
	 *
	 * @param item the item
	 * @return the container basket for item
	 */
	private Basket getContainerBasketForItem(ContentItem item){
		for(Basket basket: baskets)
			if(basket.getWindow().getAllItemsIncludeSystemItems().contains(item)) return basket;
		return null;
	}
	
	/**
	 * Register basket.
	 *
	 * @param basket the basket
	 */
	public void registerBasket(Basket basket){
		if(!baskets.contains(basket))
			baskets.add(basket);
	}
	
	/**
	 * Unregister basket.
	 *
	 * @param basket the basket
	 */
	public void unregisterBasket(Basket basket){
		if(baskets.contains(basket)){
			contentSystem.removeContentItem(basket.getWindow());
			baskets.remove(basket);
		}
	}

	/**
	 * Checks if is registered.
	 *
	 * @param tableId the table id
	 * @return true, if is registered
	 */
	public boolean isRegistered(TableIdentity tableId){
		for(Basket basket: baskets){
			if(basket.getTableId().equals(tableId))
					return true;
		}
		return false;
	}
	
	/**
	 * Gets the basket.
	 *
	 * @param tableId the table id
	 * @return the basket
	 */
	public Basket getBasket(TableIdentity tableId){
		for(Basket basket: baskets){
			if(basket.getTableId().equals(tableId))
					return basket;
		}
		return null;
	}

	/**
	 * Gets the baskets.
	 *
	 * @return the baskets
	 */
	public List<Basket> getBaskets(){
		return baskets;
	}
	
	/**
	 * Update.
	 *
	 * @param tpf the tpf
	 */
	public void update(float tpf) {
		
		if((frameRate - tpf) > 0){
			frameRate-= tpf;
			return;
		}
		checkConnectivity();
		frameRate = checkDelay;
	}
	
	/**
	 * Check connectivity.
	 */
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

	/**
	 * Swap baskets.
	 */
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
