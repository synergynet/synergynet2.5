/*
 * Copyright (c) 2009 University of Durham, England
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

package synergynetframework.jme.cursorsystem.elements.twod;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Set;

import synergynetframework.jme.cursorsystem.cursordata.ScreenCursor;
import synergynetframework.jme.gfx.twod.DrawableSpatialImage;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;

import com.jme.math.Vector3f;

public class TwoDQuadWithMultiTouchInteraction extends OrthoControlPointRotateTranslateScaleWithListener {
	protected DrawableSpatialImage quad;
	protected Rectangle interactArea;
	protected Set<Long> interactAreaCursors = new HashSet<Long>();

	public TwoDQuadWithMultiTouchInteraction(DrawableSpatialImage pickingAndTargetSpatial, Rectangle interactArea) {
		super(pickingAndTargetSpatial.getSpatial());
		this.interactArea = interactArea;
		if(this.interactArea == null) this.interactArea = new Rectangle(0, 0, 0, 0);
		this.quad = pickingAndTargetSpatial;
	}

	@Override
	public void cursorPressed(ScreenCursor c, MultiTouchCursorEvent event) {
		super.cursorPressed(c, event);
		Point p = getCurrentElement2DCoordsForCursor(getScreenCursorByID(event.getCursorID()));
		if(p == null) return;
		if(interactArea.contains(p)) {
			quad.cursorPressed(event.getCursorID(), p.x, p.y);		
			quad.draw();
			interactAreaCursors.add(event.getCursorID());
		}
	}

	@Override
	public void cursorReleased(ScreenCursor c, MultiTouchCursorEvent event) {	
		super.cursorReleased(c, event);
		Point p = getCurrentElement2DCoordsForCursor(this.getScreenCursorByID(event.getCursorID()));
		if(p == null) return;
		if(interactArea.contains(p)) {
			quad.cursorReleased(event.getCursorID(), p.x, p.y);
			quad.draw();
			interactAreaCursors.remove(event.getCursorID());
		}		
	}
	
	@Override
	public void cursorClicked(ScreenCursor c, MultiTouchCursorEvent event) {
		super.cursorClicked(c, event);
		Point p = getCurrentElement2DCoordsForCursor(this.getScreenCursorByID(event.getCursorID()));
		if(p == null) return;
		if(interactArea.contains(p)) {
			quad.cursorClicked(event.getCursorID(), p.x, p.y);
			quad.draw();
		}
	}

	public Point getCurrentElement2DCoordsForCursor(ScreenCursor cursor) {
		if(cursor == null) return null;
		Vector3f cursorPosition = new Vector3f(cursor.getCurrentCursorScreenPosition().x, cursor.getCurrentCursorScreenPosition().y, 0f);
		Vector3f selectionLocal = new Vector3f();
		pickingSpatial.worldToLocal(cursorPosition, selectionLocal);
		selectionLocal.addLocal(new Vector3f(quad.getWidth()/2f, quad.getHeight()/2f, 0f));		
		Point p = new Point();
		p.x = (int)(selectionLocal.x / quad.getWidth() * quad.getImageWidth());
		p.y = quad.getImageHeight() - ((int)(selectionLocal.y / quad.getHeight() * quad.getImageHeight()));
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
					quad.cursorDragged(sc.getID(), p.x, p.y);				
					quad.draw();
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
			quad.cursorDragged(c.getID(), p1.x, p1.y);				
			quad.draw();
		}else if(!interactAreaCursors.contains(c.getID())){
			super.applySingleCursorTransform();
		}

	}
}
