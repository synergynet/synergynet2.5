package apps.remotecontrol.tableportal.inspectionutility;

import java.util.ArrayList;
import java.util.List;

import apps.remotecontrol.tableportal.TablePortal;
import apps.remotecontrol.tableportal.WorkspaceManager;
import apps.remotecontrol.tableportal.inspectionutility.CircularLayout.CircularLayoutListener;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;

import core.SynergyNetDesktop;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.jme.mtinputbridge.MultiTouchInputFilterManager;
import synergynetframework.jme.pickingsystem.IJMEMultiTouchPicker;
import synergynetframework.jme.pickingsystem.PickSystemException;
import synergynetframework.jme.pickingsystem.data.PickRequest;
import synergynetframework.jme.pickingsystem.data.PickResultData;
import synergynetframework.mtinput.IMultiTouchEventListener;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;
import synergynetframework.mtinput.events.MultiTouchObjectEvent;


/**
 * The Class InspectionUtility.
 */
public class InspectionUtility implements IMultiTouchEventListener, CircularLayoutListener{
	
	/** The pick system. */
	private IJMEMultiTouchPicker pickSystem;
	
	/** The inspection utility. */
	private static InspectionUtility inspectionUtility;
	
	/** The content system. */
	private ContentSystem contentSystem;
	
	/** The layouts. */
	private List<CircularLayout> layouts = new ArrayList<CircularLayout>();
	
	/**
	 * Instantiates a new inspection utility.
	 */
	private InspectionUtility(){
		this.pickSystem = MultiTouchInputFilterManager.getInstance().getPickingSystem();
		SynergyNetDesktop.getInstance().getMultiTouchInputComponent().registerMultiTouchEventListener(this);
		
	}
	
	/**
	 * Gets the single instance of InspectionUtility.
	 *
	 * @return single instance of InspectionUtility
	 */
	public static InspectionUtility getInstance(){
		inspectionUtility = new InspectionUtility();
		return inspectionUtility;
	}
	
	/**
	 * Sets the content system.
	 *
	 * @param contentSystem the new content system
	 */
	public void setContentSystem(ContentSystem contentSystem){
		this.contentSystem = contentSystem;
	}
	
	/**
	 * Gets the picked items.
	 *
	 * @param id the id
	 * @param position the position
	 * @return the picked items
	 */
	public List<ContentItem> getPickedItems(long id, Vector2f position)
	{
		PickRequest req = new PickRequest(id, position);
		List<PickResultData> pickResults;
		List<ContentItem> pickedItems = new ArrayList<ContentItem>();
		try {
				pickResults = pickSystem.doPick(req);
				for(PickResultData pr : pickResults) {
					for(TablePortal portal: WorkspaceManager.getInstance().inspectedPortals){
						for(ContentItem item: portal.getDisplayPanel().getOnlineItems()){
							if(item.getName().equals(pr.getPickedSpatial().getName()))
								pickedItems.add(item);
						}
					}
				}
			}
		catch (PickSystemException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return pickedItems;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#cursorChanged(synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorChanged(MultiTouchCursorEvent event) {
		 
		
	}

	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#cursorClicked(synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorClicked(MultiTouchCursorEvent event) {
		 
		
	}

	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#cursorPressed(synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorPressed(MultiTouchCursorEvent event) {
		int x = SynergyNetDesktop.getInstance().tableToScreenX(event.getPosition().x);
		int y = SynergyNetDesktop.getInstance().tableToScreenY(event.getPosition().y);
		if(isCircularCentrePicked(event.getCursorID(), new Vector2f(x,y))) return;
		List<ContentItem> items = getPickedItems(event.getCursorID(), new Vector2f(x,y));
		if(items.size() >1){
			CircularLayout cl = new CircularLayout(contentSystem, items, new Vector3f(x,y,0), 300);
			cl.addLayoutListener(this);
			layouts.add(cl);
		}
	}


	/**
	 * Checks if is circular centre picked.
	 *
	 * @param id the id
	 * @param position the position
	 * @return true, if is circular centre picked
	 */
	private boolean isCircularCentrePicked(long id, Vector2f position) {
		PickRequest req = new PickRequest(id, position);
		List<PickResultData> pickResults;
		try {
				pickResults = pickSystem.doPick(req);
				for(PickResultData pr : pickResults) {
					for(CircularLayout layout: layouts){
						if(layout.centrePoint.isVisible() && layout.centrePoint.getName().equals(pr.getPickedSpatial().getName()))
							return true;
					}
				}
			}
		catch (PickSystemException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#cursorReleased(synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorReleased(MultiTouchCursorEvent event) {
		 

	}

	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#objectAdded(synergynetframework.mtinput.events.MultiTouchObjectEvent)
	 */
	@Override
	public void objectAdded(MultiTouchObjectEvent event) {
		 
		
	}

	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#objectChanged(synergynetframework.mtinput.events.MultiTouchObjectEvent)
	 */
	@Override
	public void objectChanged(MultiTouchObjectEvent event) {
		 
		
	}

	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#objectRemoved(synergynetframework.mtinput.events.MultiTouchObjectEvent)
	 */
	@Override
	public void objectRemoved(MultiTouchObjectEvent event) {
		 
		
	}

	/* (non-Javadoc)
	 * @see apps.remotecontrol.tableportal.inspectionutility.CircularLayout.CircularLayoutListener#layoutCollapsed(apps.remotecontrol.tableportal.inspectionutility.CircularLayout)
	 */
	@Override
	public void layoutCollapsed(CircularLayout cl) {
		layouts.remove(cl);
	}

}
