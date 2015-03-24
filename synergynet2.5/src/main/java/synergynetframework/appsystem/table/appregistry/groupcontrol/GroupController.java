package synergynetframework.appsystem.table.appregistry.groupcontrol;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apps.mathpadapp.MathPadResources;

import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.system.DisplaySystem;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.BackgroundController;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.ObjShape;
import synergynetframework.appsystem.contentsystem.items.OrthoContentItem;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.SketchPad;
import synergynetframework.appsystem.contentsystem.items.Window;
import synergynetframework.appsystem.contentsystem.items.listener.ItemListener;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;
import synergynetframework.appsystem.contentsystem.jme.items.utils.DrawData;
import synergynetframework.appsystem.contentsystem.jme.items.utils.DrawLine;
import synergynetframework.appsystem.table.appdefinitions.SynergyNetApp;


/**
 * The Class GroupController.
 */
public class GroupController {
	
	/** The content system. */
	protected ContentSystem contentSystem;
	
	/** The group bounds. */
	protected Map<Long, GroupBoundaries> groupBounds = new HashMap<Long, GroupBoundaries>();
	
	/**
	 * Instantiates a new group controller.
	 *
	 * @param app the app
	 */
	public GroupController(SynergyNetApp app){
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(app);
		final SketchPad backgroundController;
		backgroundController = (SketchPad)contentSystem.createContentItem(SketchPad.class);
		backgroundController.setWidth(DisplaySystem.getDisplaySystem().getWidth());
		backgroundController.setHeight(DisplaySystem.getDisplaySystem().getHeight());
		backgroundController.setSketchArea(new Rectangle(0,0,backgroundController.getWidth(), backgroundController.getHeight()));
		backgroundController.setBringToTopable(false);
		backgroundController.centerItem();
		backgroundController.setOrder(Integer.MIN_VALUE);
		backgroundController.setBackgroundColour(Color.black);
		backgroundController.setBorderSize(0);
		backgroundController.setTextColor(Color.red);
		backgroundController.setLineWidth(3);
		backgroundController.addItemListener(new ItemListener(){

			@Override
			public void cursorChanged(ContentItem item, long id, float x,
					float y, float pressure) {

			}

			@Override
			public void cursorClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				 
				
			}

			@Override
			public void cursorDoubleClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				 
				
			}

			@Override
			public void cursorLongHeld(ContentItem item, long id, float x,
					float y, float pressure) {
				 
				
			}

			@Override
			public void cursorPressed(ContentItem item, long id, float x,
					float y, float pressure) {
				groupBounds.put(id, new GroupBoundaries());				
			}

			@Override
			public void cursorReleased(ContentItem item, long id, float x,
					float y, float pressure) {
				GroupBoundaries gb = null;
				for(DrawData data: backgroundController.getDrawData()){
					if(data instanceof DrawLine){
						DrawLine line = (DrawLine) data;
						if(groupBounds.containsKey(id) && line.getCursorId() == id){
							gb = groupBounds.get(id);
							if(line.getStartPoint().getX()< gb.minX) gb.minX = line.getStartPoint().x;
							if(line.getStartPoint().getX()> gb.maxX) gb.maxX = line.getStartPoint().x;
							if(line.getStartPoint().getY()< gb.minY) gb.minY = line.getStartPoint().y;
							if(line.getStartPoint().getY()> gb.maxY) gb.maxY = line.getStartPoint().y;
							
							if(line.getEndPoint().getX()< gb.minX) gb.minX = line.getEndPoint().x;
							if(line.getEndPoint().getX()> gb.maxX) gb.maxX = line.getEndPoint().x;
							if(line.getEndPoint().getY()< gb.minY) gb.minY = line.getEndPoint().y;
							if(line.getEndPoint().getY()> gb.maxY) gb.maxY = line.getEndPoint().y;
						}
					}
				}
				if(gb == null) return;
				int temp1 = gb.minY,temp2 = gb.maxY;
				gb.maxY = DisplaySystem.getDisplaySystem().getHeight() - temp1;
				gb.minY = DisplaySystem.getDisplaySystem().getHeight() - temp2;

				final List<ContentItem> toGroup = new ArrayList<ContentItem>();
				for(ContentItem contentItem:contentSystem.getAllContentItems().values()){
					if(contentItem instanceof OrthoContentItem){
						OrthoContentItem orthoItem = (OrthoContentItem) contentItem;
						if(!(orthoItem instanceof  ObjShape) && !orthoItem.getName().equals(backgroundController.getName()) && orthoItem.getParent() == null){
							if(orthoItem.getLocation().x>= (float)gb.minX && orthoItem.getLocation().x<= (float)gb.maxX && orthoItem.getLocation().getY()>= (float)gb.minY && orthoItem.getLocation().getY()<= (float)gb.maxY){
								toGroup.add(orthoItem);
							}
						}
					}
				}
				
				if(!toGroup.isEmpty()){
					System.out.println("size = "+toGroup.size());
					final Window group = (Window)contentSystem.createContentItem(Window.class);
					group.setLocation(gb.minX + (gb.maxX-gb.minX)/2, gb.minY + (gb.maxY-gb.minY)/2);
					group.setWidth(gb.maxX-gb.minX);
					group.setHeight(gb.maxY-gb.minY);
					for(ContentItem subItem: toGroup){
						Vector3f wl = ((Spatial)subItem.getImplementationObject()).getWorldTranslation();
						Vector3f ll = new Vector3f();
						((Spatial)group.getImplementationObject()).worldToLocal(wl, ll);
						group.addSubItem(subItem);
						((Spatial)subItem.getImplementationObject()).setLocalTranslation(ll);
						((Spatial)group.getImplementationObject()).updateGeometricState(0, false);
						((Spatial)group.getImplementationObject()).updateRenderState();
					}
					
					final SimpleButton closeButton = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
					closeButton.setAutoFitSize(false);
					closeButton.setWidth(18);
					closeButton.setHeight(18);
					closeButton.setBackgroundColour(Color.white);
					closeButton.drawImage(MathPadResources.class.getResource("buttons/close.png"),0,0,18,18);
					closeButton.setLocation(group.getBackgroundFrame().getWidth()/2, group.getBackgroundFrame().getHeight()/2);
					closeButton.addButtonListener(new SimpleButtonAdapter(){
						
						@Override
						public void buttonReleased(SimpleButton b, long id, float x,
								float y, float pressure) {
							for(ContentItem subItem: toGroup){
								if(!subItem.getName().equals(closeButton.getName()) && !(subItem instanceof BackgroundController))	
									group.detachSubItem(subItem);
							}
							if(group.getParent()!= null) group.getParent().removeSubItem(group);
							else contentSystem.removeContentItem(group);
						}
						
					});
					group.addSubItem(closeButton);
					group.setOrder(1000);
				}
				backgroundController.clearAll();
				groupBounds.remove(id);
			}

			@Override
			public void cursorRightClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				 
			}
			
		});

	}
}
