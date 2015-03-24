/*
 * Copyright (c) 2008 University of Durham, England All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of 'SynergyNet' nor the names of
 * its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission. THIS SOFTWARE IS PROVIDED
 * BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package synergynetframework.jme.cursorsystem.elements.threed;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Set;

import synergynetframework.jme.cursorsystem.ThreeDMultiTouchElement;
import synergynetframework.jme.cursorsystem.cursordata.ScreenCursor;
import synergynetframework.jme.gfx.JMEGfxUtils;
import synergynetframework.jme.gfx.twod.DrawableSpatialImage;
import synergynetframework.jme.pickingsystem.AccuratePickingUtility;
import synergynetframework.jme.pickingsystem.data.ThreeDPickResultData;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;

import com.jme.math.FastMath;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;

/**
 * The Class MultiTouchTransformableDrawableElement.
 */
public class MultiTouchTransformableDrawableElement extends
		ThreeDMultiTouchElement {

	/** The interact area. */
	protected Rectangle interactArea;

	/** The interact area cursors. */
	protected Set<Long> interactAreaCursors = new HashSet<Long>();

	/** The pixels per unit. */
	protected float pixelsPerUnit;
	
	/** The quad. */
	protected DrawableSpatialImage quad;
	
	/**
	 * Instantiates a new multi touch transformable drawable element.
	 *
	 * @param drawableSpatialImage
	 *            the drawable spatial image
	 * @param pixelsPerUnit
	 *            the pixels per unit
	 * @param interactArea
	 *            the interact area
	 */
	public MultiTouchTransformableDrawableElement(
			DrawableSpatialImage drawableSpatialImage, float pixelsPerUnit,
			Rectangle interactArea) {
		super(drawableSpatialImage.getSpatial());

		this.interactArea = interactArea;
		this.quad = drawableSpatialImage;
		this.pixelsPerUnit = pixelsPerUnit;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.jme.cursorsystem.MultiTouchElement#cursorChanged(
	 * synergynetframework.jme.cursorsystem.cursordata.ScreenCursor,
	 * synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorChanged(ScreenCursor c, MultiTouchCursorEvent event) {
		if (screenCursors.size() < 2) {
			singleCursorChanged(c, event);
		} else {
			multipleCursorsChanged();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.jme.cursorsystem.MultiTouchElement#cursorClicked(
	 * synergynetframework.jme.cursorsystem.cursordata.ScreenCursor,
	 * synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorClicked(ScreenCursor c, MultiTouchCursorEvent event) {
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.jme.cursorsystem.MultiTouchElement#cursorPressed(
	 * synergynetframework.jme.cursorsystem.cursordata.ScreenCursor,
	 * synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorPressed(ScreenCursor c, MultiTouchCursorEvent event) {
		Point p = getOriginElement2DCoordsForEvent(event);
		if (interactArea.contains(p)) {
			quad.cursorPressed(event.getCursorID(), p.x, p.y);
			quad.draw();
			interactAreaCursors.add(event.getCursorID());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.jme.cursorsystem.MultiTouchElement#cursorReleased
	 * (synergynetframework.jme.cursorsystem.cursordata.ScreenCursor,
	 * synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorReleased(ScreenCursor c, MultiTouchCursorEvent event) {
		Point p = getOriginElement2DCoordsForEvent(event);
		if (interactArea.contains(p)) {
			quad.cursorReleased(event.getCursorID(), p.x, p.y);
			quad.draw();
			interactAreaCursors.remove(event.getCursorID());
		}
	}
	
	/**
	 * Gets the current element2 d coords for cursor index.
	 *
	 * @param index
	 *            the index
	 * @return the current element2 d coords for cursor index
	 */
	protected Point getCurrentElement2DCoordsForCursorIndex(int index) {
		Vector3f cursorPosition = AccuratePickingUtility
				.getPointOfIntersection(
						pickingSpatial.getParent(),
						this.screenCursors.get(index)
								.getCurrentCursorScreenPosition().getPosition(),
						pickingSpatial);
		if (cursorPosition == null) {
			return null;
		}
		Vector3f selectionLocal = new Vector3f();
		pickingSpatial.worldToLocal(cursorPosition, selectionLocal);
		selectionLocal.addLocal(new Vector3f(quad.getWidth() / 2f, quad
				.getHeight() / 2f, 0f));
		Point p = new Point();
		p.x = (int) ((selectionLocal.x / quad.getWidth()) * quad
				.getImageWidth());
		p.y = quad.getImageHeight()
				- ((int) ((selectionLocal.y / quad.getHeight()) * quad
						.getImageHeight()));
		return p;
		
	}
	
	/**
	 * Gets the origin element2 d coords for cursor.
	 *
	 * @param id
	 *            the id
	 * @return the origin element2 d coords for cursor
	 */
	protected Point getOriginElement2DCoordsForCursor(long id) {
		ThreeDPickResultData pickResultData = getPickDataForCursorID(id);
		Vector3f pointOfSelection = pickResultData.getPointOfSelection();
		Vector3f selectionLocal = new Vector3f();
		getPickingSpatial().worldToLocal(pointOfSelection, selectionLocal);
		selectionLocal.addLocal(new Vector3f(quad.getWidth() / 2f, quad
				.getHeight() / 2f, 0f));
		Point p = new Point();
		p.x = (int) ((selectionLocal.x / quad.getWidth()) * quad
				.getImageWidth());
		p.y = quad.getImageHeight()
				- ((int) ((selectionLocal.y / quad.getHeight()) * quad
						.getImageHeight()));
		return p;
	}
	
	/**
	 * Gets the origin element2 d coords for event.
	 *
	 * @param event
	 *            the event
	 * @return the origin element2 d coords for event
	 */
	protected Point getOriginElement2DCoordsForEvent(MultiTouchCursorEvent event) {
		return getOriginElement2DCoordsForCursor(event.getCursorID());
	}
	
	/**
	 * Multiple cursors changed.
	 */
	protected void multipleCursorsChanged() {
		long id0 = getScreenCursorByIndex(0).getID();
		long id1 = getScreenCursorByIndex(1).getID();
		
		if (!interactAreaCursors.contains(id0)
				&& !interactAreaCursors.contains(id1)) {
			
			Vector2f originalScreenPosCursor1 = getScreenCursorByIndex(0)
					.getCursorOrigin().getPosition();
			Vector2f originalScreenPosCursor2 = getScreenCursorByIndex(1)
					.getCursorOrigin().getPosition();
			Vector2f screenPosCursor1 = getScreenCursorByIndex(0)
					.getCurrentCursorScreenPosition().getPosition();
			Vector2f screenPosCursor2 = getScreenCursorByIndex(1)
					.getCurrentCursorScreenPosition().getPosition();
			ThreeDPickResultData pr = getPickResultFromCursorIndex(0);
			Vector2f spatialScreenAtPick = pr.getSpatialScreenLocationAtPick();
			Vector2f cursor1ToSpatialAtPick = spatialScreenAtPick
					.subtract(originalScreenPosCursor1);
			
			float originalCursorAngle = originalScreenPosCursor2.subtract(
					originalScreenPosCursor1).getAngle();
			float newCursorAngle = screenPosCursor2.subtract(screenPosCursor1)
					.getAngle();
			float theta = newCursorAngle - originalCursorAngle;
			
			float newCursor1ToSpatialAngle = cursor1ToSpatialAtPick.getAngle()
					+ theta;
			
			float scale = screenPosCursor2.subtract(screenPosCursor1).length()
					/ originalScreenPosCursor2.subtract(
							originalScreenPosCursor1).length();
			
			float newDistFromCursor1ToSpatial = scale
					* cursor1ToSpatialAtPick.length();
			float dx = newDistFromCursor1ToSpatial
					* FastMath.cos(newCursor1ToSpatialAngle);
			float dy = newDistFromCursor1ToSpatial
					* FastMath.sin(newCursor1ToSpatialAngle);
			
			Vector2f newScreenPosition = screenPosCursor1.add(new Vector2f(dx,
					-dy));
			
			Vector3f newPosition = JMEGfxUtils
					.getCursorWorldCoordinatesOnSpatialPlane(newScreenPosition,
							targetSpatial);
			if (newPosition != null) {
				targetSpatial.getParent().worldToLocal(newPosition,
						targetSpatial.getLocalTranslation());
				targetSpatial
						.setLocalRotation(getCurrentTargetSpatialRotationFromCursorChange());
				targetSpatial.setLocalScale(getScaleAtOrigin().mult(scale));
				targetSpatial.updateGeometricState(0f, true);
			}
		} else {
			for (int i = 0; i < screenCursors.size(); i++) {
				Point p = getCurrentElement2DCoordsForCursorIndex(i);
				if ((p != null) && interactArea.contains(p)) {
					quad.cursorDragged(getScreenCursorByIndex(i).getID(), p.x,
							p.y);
				}
			}
			quad.draw();
		}
	}
	
	/**
	 * Single cursor changed.
	 *
	 * @param c
	 *            the c
	 * @param event
	 *            the event
	 */
	protected void singleCursorChanged(ScreenCursor c,
			MultiTouchCursorEvent event) {
		long id = getScreenCursorByIndex(0).getID();
		Vector2f screenPos = c.getCurrentCursorScreenPosition().getPosition();
		Point p1 = getCurrentElement2DCoordsForCursorIndex(0);
		
		if ((p1 != null) && interactArea.contains(p1)) {
			quad.cursorDragged(id, p1.x, p1.y);
			quad.draw();
		} else if (!interactAreaCursors.contains(id)) {
			long eventCursorID = event.getCursorID();
			ThreeDPickResultData pickResultData = getPickDataForCursorID(eventCursorID);
			
			Vector2f cursorPosition = screenPos.subtract(pickResultData
					.getCursorToSpatialScreenOffset());
			Vector3f newPosition = JMEGfxUtils
					.getCursorWorldCoordinatesOnSpatialPlane(cursorPosition,
							targetSpatial);
			if (newPosition != null) {
				targetSpatial.getParent().worldToLocal(newPosition,
						targetSpatial.getLocalTranslation());
			}
		}
	}
	
}
