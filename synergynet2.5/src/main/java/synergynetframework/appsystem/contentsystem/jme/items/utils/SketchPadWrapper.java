package synergynetframework.appsystem.contentsystem.jme.items.utils;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Set;

import synergynetframework.appsystem.contentsystem.jme.items.JMESketchPad;
import synergynetframework.jme.cursorsystem.cursordata.ScreenCursor;
import synergynetframework.jme.cursorsystem.elements.twod.OrthoControlPointRotateTranslateScaleWithListener;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;

import com.jme.math.Vector3f;
import com.jme.scene.Spatial;

public class SketchPadWrapper extends OrthoControlPointRotateTranslateScaleWithListener {
	protected JMESketchPad jmeSketchPad;
	protected Rectangle interactArea = new Rectangle();
	protected Set<Long> interactAreaCursors = new HashSet<Long>();

	public SketchPadWrapper(JMESketchPad jmeSketchPad, Spatial targetSpatial, Rectangle interactArea) {
		super(targetSpatial);
		this.interactArea.x = interactArea.x;
		this.interactArea.y = interactArea.y;
		this.interactArea.width = interactArea.width;
		this.interactArea.height = interactArea.height;
		this.jmeSketchPad = jmeSketchPad;
	}

	@Override
	public void cursorPressed(ScreenCursor c, MultiTouchCursorEvent event) {
		super.cursorPressed(c, event);
		Point p = getCurrentElement2DCoordsForCursor(getScreenCursorByID(event.getCursorID()));
		if(p == null) return;
		if(interactArea.contains(p)) {
			jmeSketchPad.cursorPressed(event.getCursorID(), p.x, p.y);		
			interactAreaCursors.add(event.getCursorID());
		}
	}

	@Override
	public void cursorReleased(ScreenCursor c, MultiTouchCursorEvent event) {	
		super.cursorReleased(c, event);
		Point p = getCurrentElement2DCoordsForCursor(this.getScreenCursorByID(event.getCursorID()));
		if(p == null) return;
		if(interactArea.contains(p)) {
			jmeSketchPad.cursorReleased(event.getCursorID(), p.x, p.y);
			interactAreaCursors.remove(event.getCursorID());
		}		
	}
	
	@Override
	public void cursorClicked(ScreenCursor c, MultiTouchCursorEvent event) {
		super.cursorClicked(c, event);
		Point p = getCurrentElement2DCoordsForCursor(this.getScreenCursorByID(event.getCursorID()));
		if(p == null) return;
		if(interactArea.contains(p)) {
			jmeSketchPad.cursorClicked(event.getCursorID(), p.x, p.y);
		}
	}

	public Point getCurrentElement2DCoordsForCursor(ScreenCursor cursor) {
		if(cursor == null) return null;
		Vector3f cursorPosition = new Vector3f(cursor.getCurrentCursorScreenPosition().x, cursor.getCurrentCursorScreenPosition().y, 0f);
		Vector3f selectionLocal = new Vector3f();
		pickingSpatial.worldToLocal(cursorPosition, selectionLocal);
		selectionLocal.addLocal(new Vector3f(jmeSketchPad.getWidth()/2f, jmeSketchPad.getHeight()/2f, 0f));		
		Point p = new Point();
		p.x = (int)(selectionLocal.x / jmeSketchPad.getWidth() * jmeSketchPad.getImageWidth());
		p.y = jmeSketchPad.getImageHeight() - ((int)(selectionLocal.y / jmeSketchPad.getHeight() * jmeSketchPad.getImageHeight()));
		return p;		
	}

	@Override
	public void cursorChanged(ScreenCursor c, MultiTouchCursorEvent event) {
		super.cursorChanged(c, event);

	}

	@Override
	protected void applyMultiCursorTransform() {		
		boolean allowTransform = false;
		for(ScreenCursor sc : screenCursors) {
			if(!interactAreaCursors.contains(sc.getID())) {
				allowTransform = true;
			}else{
				Point p = getCurrentElement2DCoordsForCursor(sc);
				if(p != null && interactArea.contains(p)) {
					jmeSketchPad.cursorDragged(sc.getID(), p.x, p.y);				
				}
			}
		}

		if(allowTransform) {
			super.applyMultiCursorTransform();
		}		
	}

	@Override
	protected void applySingleCursorTransform() {
		ScreenCursor c = getScreenCursorByIndex(0);
		Point p1 = getCurrentElement2DCoordsForCursor(c);
		if(p1 != null && interactArea.contains(p1)) {
			jmeSketchPad.cursorDragged(c.getID(), p1.x, p1.y);				
		}else if(!interactAreaCursors.contains(c.getID())){
			super.applySingleCursorTransform();
		}

	}
}
