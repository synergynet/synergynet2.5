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

package synergynetframework.appsystem.contentsystem.jme.items.utils;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Set;

import synergynetframework.appsystem.contentsystem.jme.items.JMEKeyboard;
import synergynetframework.jme.cursorsystem.cursordata.ScreenCursor;
import synergynetframework.jme.cursorsystem.elements.twod.OrthoControlPointRotateTranslateScale;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;

import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Quad;


/**
 * The Class KeyboardWrapper.
 */
public class KeyboardWrapper extends OrthoControlPointRotateTranslateScale {
	
	/** The keyboard geom. */
	protected JMEKeyboard keyboardGeom;
	
	/** The interact area. */
	protected Rectangle interactArea;
	
	/** The interact area cursors. */
	protected Set<Long> interactAreaCursors = new HashSet<Long>();
	
	/** The keyboard quad. */
	private Quad keyboardQuad;

	/**
	 * Instantiates a new keyboard wrapper.
	 *
	 * @param keyboardGeom the keyboard geom
	 * @param targetSpatial the target spatial
	 * @param interactArea the interact area
	 */
	public KeyboardWrapper(JMEKeyboard keyboardGeom, Spatial targetSpatial, Rectangle interactArea) {
		super((Spatial)keyboardGeom.getImplementationObject(), targetSpatial);
		this.interactArea = interactArea;
		this.keyboardGeom = keyboardGeom;
		this.keyboardQuad = (Quad)keyboardGeom.getImplementationObject();
	}
	
	/**
	 * Instantiates a new keyboard wrapper.
	 *
	 * @param keyboardGeom the keyboard geom
	 * @param interactArea the interact area
	 */
	public KeyboardWrapper(JMEKeyboard keyboardGeom, Rectangle interactArea) {
		this(keyboardGeom, (Spatial)keyboardGeom.getImplementationObject(), interactArea);
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.elements.twod.OrthoControlPointRotateTranslateScale#cursorPressed(synergynetframework.jme.cursorsystem.cursordata.ScreenCursor, synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorPressed(ScreenCursor c, MultiTouchCursorEvent event) {
		super.cursorPressed(c, event);
		Point p = getCurrentElement2DCoordsForCursor(getScreenCursorByID(event.getCursorID()));
		if(p == null) return;
		if(interactArea.contains(p)) {
			//keyboardGeom.cursorPressed(event.getCursorID(), p.x, p.y);		
			interactAreaCursors.add(event.getCursorID());
		}
	}

	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.elements.twod.OrthoControlPointRotateTranslateScale#cursorReleased(synergynetframework.jme.cursorsystem.cursordata.ScreenCursor, synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorReleased(ScreenCursor c, MultiTouchCursorEvent event) {	
		super.cursorReleased(c, event);
		Point p = getCurrentElement2DCoordsForCursor(getScreenCursorByID(event.getCursorID()));
		if(p == null) return;
		if(interactArea.contains(p)) { 
			//keyboardGeom.cursorReleased(event.getCursorID(), p.x, p.y);		
			interactAreaCursors.add(event.getCursorID());
		}		
	}

	/**
	 * Gets the current element2 d coords for cursor.
	 *
	 * @param cursor the cursor
	 * @return the current element2 d coords for cursor
	 */
	public Point getCurrentElement2DCoordsForCursor(ScreenCursor cursor) {
		if(cursor == null) return null;
		Vector3f cursorPosition = new Vector3f(cursor.getCurrentCursorScreenPosition().x, cursor.getCurrentCursorScreenPosition().y, 0f);
		Vector3f selectionLocal = new Vector3f();
		pickingSpatial.worldToLocal(cursorPosition, selectionLocal);
		selectionLocal.addLocal(new Vector3f(keyboardQuad.getWidth()/2f, keyboardQuad.getHeight()/2f, 0f));		
		Point p = new Point();
		p.x = (int)(selectionLocal.x / keyboardQuad.getWidth() * keyboardGeom.getImageWidth());
		p.y = keyboardGeom.getImageHeight() - ((int)(selectionLocal.y / keyboardQuad.getHeight() * keyboardGeom.getImageHeight()));
		return p;		
	}

	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.elements.twod.OrthoControlPointRotateTranslateScale#cursorChanged(synergynetframework.jme.cursorsystem.cursordata.ScreenCursor, synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorChanged(ScreenCursor c, MultiTouchCursorEvent event) {
		super.cursorChanged(c, event);
	}

	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.elements.twod.OrthoControlPointRotateTranslateScale#applyMultiCursorTransform()
	 */
	@Override
	protected void applyMultiCursorTransform() {
		//long id0 = getScreenCursorByIndex(0).getID();
		//long id1 = getScreenCursorByIndex(1).getID();

		//if(!interactAreaCursors.contains(id0) && !interactAreaCursors.contains(id1)) {
			super.applyMultiCursorTransform();
		//}
	}

	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.elements.twod.OrthoControlPointRotateTranslateScale#applySingleCursorTransform()
	 */
	@Override
	protected void applySingleCursorTransform() {
		ScreenCursor c = getScreenCursorByIndex(0);
		Point p1 = getCurrentElement2DCoordsForCursor(c);
		if(p1 != null && interactArea.contains(p1)) {
			keyboardGeom.cursorDragged(c.getID(), p1.x, p1.y + interactArea.height);
		}//else if(!interactAreaCursors.contains(c.getID())){
			super.applySingleCursorTransform();
		//}

	}

	/**
	 * The listener interface for receiving keyboardEven events.
	 * The class that is interested in processing a keyboardEven
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addKeyboardEvenListener<code> method. When
	 * the keyboardEven event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see KeyboardEvenEvent
	 */
	public interface KeyboardEvenListener {
		
		/**
		 * Item rotated.
		 *
		 * @param multiTouchElement the multi touch element
		 * @param targetSpatial the target spatial
		 * @param newAngle the new angle
		 * @param oldAngle the old angle
		 */
		public void itemRotated(OrthoControlPointRotateTranslateScale multiTouchElement, Spatial targetSpatial, float newAngle, float oldAngle);
		
		/**
		 * Item moved.
		 *
		 * @param multiTouchElement the multi touch element
		 * @param targetSpatial the target spatial
		 * @param newLocationX the new location x
		 * @param newLocationY the new location y
		 * @param oldLocationX the old location x
		 * @param oldLocationY the old location y
		 */
		public void itemMoved(OrthoControlPointRotateTranslateScale multiTouchElement, Spatial targetSpatial,  float newLocationX, float newLocationY, float oldLocationX, float oldLocationY);
		
		/**
		 * Item scaled.
		 *
		 * @param multiTouchElement the multi touch element
		 * @param targetSpatial the target spatial
		 * @param scaleChange the scale change
		 */
		public void itemScaled(OrthoControlPointRotateTranslateScale multiTouchElement, Spatial targetSpatial,  float scaleChange);		
	}
}
