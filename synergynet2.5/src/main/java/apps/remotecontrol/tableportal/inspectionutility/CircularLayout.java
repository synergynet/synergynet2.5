package apps.remotecontrol.tableportal.inspectionutility;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.RoundFrame;
import synergynetframework.appsystem.contentsystem.items.listener.ItemListener;
import synergynetframework.appsystem.contentsystem.items.utils.Location;

import apps.remotecontrol.tableportal.TablePortal;
import apps.remotecontrol.tableportal.WorkspaceManager;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Line;
import com.jme.scene.Spatial;
import com.jme.scene.Spatial.LightCombineMode;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;

public class CircularLayout {
	public TablePortal sourcePortal;
	public Map<ContentItem,Location> originalLocsMap;
	public List<Line> lines;
	public RoundFrame centrePoint;
	public List<CircularLayoutListener> listeners = new ArrayList<CircularLayoutListener>();
	
	public CircularLayout(ContentSystem contentSystem, List<ContentItem> items, Vector3f centre, float distance){
		lines = new ArrayList<Line>();
		originalLocsMap = new HashMap<ContentItem, Location>();
		centrePoint = (RoundFrame) contentSystem.createContentItem(RoundFrame.class);
		centrePoint.setRadius(20);
		centrePoint.setBackgroundColour(Color.white);
		sourcePortal = WorkspaceManager.getInstance().getSourceTablePortal(items.get(0));
		sourcePortal.getDisplayPanel().getWindow().addSubItem(centrePoint);
		Spatial bf = ((Spatial)WorkspaceManager.getInstance().getSourceTablePortal(items.get(0)).getDisplayPanel().getWindow().getBackgroundFrame().getImplementationObject());
		Vector3f store = new Vector3f();
		bf.worldToLocal(centre, store);
		centrePoint.setLocalLocation(store.x, store.y);
		centrePoint.setOrder(99999);
		
		
		float shiftAngle = FastMath.DEG_TO_RAD * (360/items.size());
		float currentAngle = shiftAngle;
		for(ContentItem item:items){
			originalLocsMap.put(item, new Location(item.getLocalLocation().getX(), item.getLocalLocation().getY(),0));
			positionItem(item, currentAngle);
			
			Line line = new Line(UUID.randomUUID().toString(), new Vector3f[]{new Vector3f(store.x, store.y,0), new Vector3f(item.getLocalLocation().x, item.getLocalLocation().y,0)}, null, new ColorRGBA[]{ColorRGBA.white,  ColorRGBA.white}, null);
			line.setLineWidth(1f);
			line.setSolidColor(new ColorRGBA(sourcePortal.getWindow().getBackgroundColour().getRed(), sourcePortal.getWindow().getBackgroundColour().getGreen(), sourcePortal.getWindow().getBackgroundColour().getBlue(), sourcePortal.getWindow().getBackgroundColour().getAlpha()));
	        line.setRenderQueueMode(Renderer.QUEUE_ORTHO);
            line.setLightCombineMode(LightCombineMode.Off);
	        ZBufferState zbs = DisplaySystem.getDisplaySystem().getRenderer().createZBufferState();
	        zbs.setEnabled(false);
	        line.setRenderState(zbs);
	        sourcePortal.getDisplayPanel().getWindow().getNode().attachChild(line);
			line.updateGeometricState(0, true);
			sourcePortal.getDisplayPanel().getWindow().getNode().updateGeometricState(0, true);
			lines.add(line);
			sourcePortal.getDisplayPanel().getWindow().getNode().updateGeometricState(0, true);
			line.updateRenderState();
			
			currentAngle+= shiftAngle;
		}


		
		
		centrePoint.addItemListener(new ItemListener(){
			public void cursorChanged(ContentItem item, long id, float x, float y, float pressure) {}
			public void cursorClicked(ContentItem item, long id, float x, float y, float pressure) {	}
			public void cursorDoubleClicked(ContentItem item, long id, float x, float y, float pressure) {}
			public void cursorLongHeld(ContentItem item, long id, float x, float y, float pressure) {	}
			public void cursorRightClicked(ContentItem item, long id, float x, float y, float pressure) {}
			public void cursorPressed(ContentItem item, long id, float x, float y, float pressure) { }

			@Override
			public void cursorReleased(ContentItem item, long id, float x, float y, float pressure) {
				collapse();
			}
		});
	}
	
	private void positionItem(ContentItem item, float angle){
		float distance = 1;
		do{
			float oldX = item.getLocalLocation().getX();
			float oldY = item.getLocalLocation().getY();
			item.setLocalLocation(oldX + (distance * FastMath.cos(angle)), oldY+ (distance * FastMath.sin(angle)));
			distance+=1;
		}while(itemCollide(item));
	}
	
	private boolean itemCollide(ContentItem item){
		if(item.hasCollision(this.centrePoint))
			return true;
		for(ContentItem otherItem: this.originalLocsMap.keySet()){
			if(!otherItem.getName().equals(item.getName()) && item.hasCollision(otherItem))
				return true;
		}
		return false;
	}
	
	public void addLayoutListener(CircularLayoutListener l){
		if(!listeners.contains(l)) 
			listeners.add(l);
	}
	public void collapse(){
	
		for(ContentItem item: originalLocsMap.keySet())
			item.setLocalLocation(originalLocsMap.get(item));
		for(Line line: lines)
			line.removeFromParent();
		centrePoint.setVisible(false);
		sourcePortal.getDisplayPanel().getWindow().getNode().updateGeometricState(0, true);
		for(CircularLayoutListener l: listeners)
			l.layoutCollapsed(this);
	}
	
	public interface CircularLayoutListener{
		public void layoutCollapsed(CircularLayout cl);
	}

}
