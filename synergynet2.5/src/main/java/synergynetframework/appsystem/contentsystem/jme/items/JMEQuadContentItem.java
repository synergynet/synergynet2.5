/* Copyright (c) 2008 University of Durham, England
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'SynergyNet' nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package synergynetframework.appsystem.contentsystem.jme.items;

import java.awt.Point;
import java.awt.geom.Point2D;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.OrthoContentItem;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IQuadContentItemImplementation;
import synergynetframework.appsystem.contentsystem.items.listener.ItemListener;
import synergynetframework.appsystem.contentsystem.items.listener.ScreenCursorListener;
import synergynetframework.jme.cursorsystem.MultiTouchCursorSystem;
import synergynetframework.jme.cursorsystem.cursordata.ScreenCursor;
import synergynetframework.jme.cursorsystem.elements.twod.OrthoCursorEventDispatcher;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Quad;

public class JMEQuadContentItem extends JMEOrthoContentItem implements IQuadContentItemImplementation {

	public JMEQuadContentItem(ContentItem contentItem, Spatial spatial) {
		super(contentItem, spatial);
	}

	@Override
	public void setHeight(int height) {
		 		
	}

	@Override
	public void setWidth(int width) {
		 		
	}
	
	@Override
	public void setAutoFitSize(boolean isEnabled) {
		 
		
	}

	//item cursor events
	@Override
	public void cursorChanged(
			OrthoCursorEventDispatcher commonCursorEventDispatcher,
			ScreenCursor c, MultiTouchCursorEvent event) {
		Point p = getCurrentElement2DCoordsForCursor(c);
		for (ItemListener l: this.itemListeners)
			l.cursorChanged(this.contentItem, event.getCursorID(), p.x, p.y, event.getPressure());

		this.longHoldDetector.updateCursorPostion(c.getID(), new Point2D.Float(p.x, p.y));
		
		Vector2f screenPos = MultiTouchCursorSystem.tableToScreen(event.getPosition());
		for (ScreenCursorListener l: this.screenCursorListeners)
			l.screenCursorChanged(this.contentItem, event.getCursorID(), screenPos.x, screenPos.y, event.getPressure());
		
		OrthoContentItem parentItem = ((OrthoContentItem)contentItem).getParent();
		while(parentItem != null){
			parentItem.fireCursorChanged(event.getCursorID(), p.x, p.y, event.getPressure());
			parentItem.fireScreenCursorChanged(event.getCursorID(), screenPos.x, screenPos.y, event.getPressure());
			parentItem = parentItem.getParent();
		}
		
	}

	@Override
	public void cursorClicked(
			OrthoCursorEventDispatcher commonCursorEventDispatcher,
			ScreenCursor c, MultiTouchCursorEvent event) {
		Point p = getCurrentElement2DCoordsForCursor(c);
		for (ItemListener l: this.itemListeners){
			if (event.getClickCount()==2){
				l.cursorDoubleClicked(this.contentItem, event.getCursorID(), p.x, p.y, event.getPressure());
			}
			else{			
				l.cursorClicked(this.contentItem, event.getCursorID(), p.x, p.y, event.getPressure());	
			}
		}	
		
		Vector2f screenPos = MultiTouchCursorSystem.tableToScreen(event.getPosition());
		for (ScreenCursorListener l: this.screenCursorListeners)
			l.screenCursorClicked(this.contentItem, event.getCursorID(), screenPos.x, screenPos.y, event.getPressure());
		
		OrthoContentItem parentItem = ((OrthoContentItem)contentItem).getParent();
		while(parentItem != null){
			if (event.getClickCount()==2)
				parentItem.fireCursorDoubleClicked(event.getCursorID(), p.x, p.y, event.getPressure());
			else
				parentItem.fireCursorClicked(event.getCursorID(), p.x, p.y, event.getPressure());
			parentItem.fireScreenCursorClicked(event.getCursorID(), screenPos.x, screenPos.y, event.getPressure());
			parentItem = parentItem.getParent();
		}
	}

	@Override
	public void cursorPressed(
			OrthoCursorEventDispatcher commonCursorEventDispatcher,
			ScreenCursor c, MultiTouchCursorEvent event) {
		Point p = getCurrentElement2DCoordsForCursor(c);
		for (ItemListener l: this.itemListeners)
			l.cursorPressed(this.contentItem, event.getCursorID(), p.x, p.y, event.getPressure());
		
		this.longHoldDetector.registerCursor(c.getID(),  new Point2D.Float(p.x, p.y));
		
		Vector2f screenPos = MultiTouchCursorSystem.tableToScreen(event.getPosition());
		for (ScreenCursorListener l: this.screenCursorListeners)
			l.screenCursorPressed(this.contentItem, event.getCursorID(), screenPos.x, screenPos.y, event.getPressure());

		OrthoContentItem parentItem = ((OrthoContentItem)contentItem).getParent();
		while(parentItem != null){
			parentItem.fireCursorPressed(event.getCursorID(), p.x, p.y, event.getPressure());
			parentItem.fireScreenCursorPressed(event.getCursorID(), screenPos.x, screenPos.y, event.getPressure());
			parentItem = parentItem.getParent();
		}
	}

	@Override
	public void cursorReleased(
			OrthoCursorEventDispatcher commonCursorEventDispatcher,
			ScreenCursor c, MultiTouchCursorEvent event) {
		Point p = getCurrentElement2DCoordsForCursor(c);
		for (ItemListener l: this.itemListeners)
			l.cursorReleased(this.contentItem, event.getCursorID(), p.x, p.y, event.getPressure());
		
		this.longHoldDetector.unregisterCursor(c.getID());
		
		Vector2f screenPos = MultiTouchCursorSystem.tableToScreen(event.getPosition());
		for (ScreenCursorListener l: this.screenCursorListeners)
			l.screenCursorReleased(this.contentItem, event.getCursorID(), screenPos.x, screenPos.y, event.getPressure());
		
		OrthoContentItem parentItem = ((OrthoContentItem)contentItem).getParent();
		while(parentItem != null){
			parentItem.fireCursorReleased(event.getCursorID(), p.x, p.y, event.getPressure());
			parentItem.fireScreenCursorReleased(event.getCursorID(), screenPos.x, screenPos.y, event.getPressure());
			parentItem = parentItem.getParent();
		}
	}
		
	protected Point getCurrentElement2DCoordsForCursor(ScreenCursor cursor) {
		if(cursor == null) return null;
		Vector3f cursorPosition = new Vector3f(cursor.getCurrentCursorScreenPosition().x, cursor.getCurrentCursorScreenPosition().y, 0f);
		Vector3f selectionLocal = new Vector3f();
		this.spatial.worldToLocal(cursorPosition, selectionLocal);
		selectionLocal.addLocal(new Vector3f(((Quad)this.spatial).getWidth()/2f, ((Quad)this.spatial).getHeight()/2f, 0f));		
		Point p = new Point();
		p.x = (int)(selectionLocal.x);
		p.y = (int) (((Quad)this.spatial).getHeight()- (int)(selectionLocal.y)) ;
		return p;		
	}
		
}
