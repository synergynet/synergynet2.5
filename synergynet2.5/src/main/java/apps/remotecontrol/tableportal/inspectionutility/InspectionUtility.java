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

public class InspectionUtility implements IMultiTouchEventListener, CircularLayoutListener{
	private IJMEMultiTouchPicker pickSystem;
	private static InspectionUtility inspectionUtility;
	private ContentSystem contentSystem;
	private List<CircularLayout> layouts = new ArrayList<CircularLayout>();
	
	private InspectionUtility(){
		this.pickSystem = MultiTouchInputFilterManager.getInstance().getPickingSystem();
		SynergyNetDesktop.getInstance().getMultiTouchInputComponent().registerMultiTouchEventListener(this);
		
	}
	
	public static InspectionUtility getInstance(){
		inspectionUtility = new InspectionUtility();
		return inspectionUtility;
	}
	
	public void setContentSystem(ContentSystem contentSystem){
		this.contentSystem = contentSystem;
	}
	
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

	@Override
	public void cursorChanged(MultiTouchCursorEvent event) {
		 
		
	}

	@Override
	public void cursorClicked(MultiTouchCursorEvent event) {
		 
		
	}

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

	@Override
	public void cursorReleased(MultiTouchCursorEvent event) {
		 

	}

	@Override
	public void objectAdded(MultiTouchObjectEvent event) {
		 
		
	}

	@Override
	public void objectChanged(MultiTouchObjectEvent event) {
		 
		
	}

	@Override
	public void objectRemoved(MultiTouchObjectEvent event) {
		 
		
	}

	@Override
	public void layoutCollapsed(CircularLayout cl) {
		layouts.remove(cl);
	}

}
