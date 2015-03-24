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

package apps.projectmanagement.gesture;

import java.util.ArrayList;
import java.util.List;

import apps.projectmanagement.component.ganttchart.ScrollBar.Direction;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;

import synergynetframework.jme.cursorsystem.cursordata.WorldCursorRecord;
import synergynetframework.jme.cursorsystem.elements.twod.OrthoControlPointRotateTranslateScale;


/**
 * The Class ScrollBarControl.
 */
public class ScrollBarControl extends OrthoControlPointRotateTranslateScale{

	/** The parent spatial. */
	public Spatial parentSpatial;

	/** The scroll bar listeners. */
	protected List<ScrollBarListener> scrollBarListeners = new ArrayList<ScrollBarListener>();
	
	/** The length. */
	protected int length;
	
	/** The content lenght. */
	protected int contentLenght;
	
	/** The orgin. */
	protected int orgin;
	
	/** The bar length. */
	protected int barLength;
	
	/** The direction. */
	protected Direction direction;
	
	/**
	 * Instantiates a new scroll bar control.
	 *
	 * @param pickingSpatial the picking spatial
	 * @param targetSpatial the target spatial
	 * @param length the length
	 * @param barLength the bar length
	 * @param contentLength the content length
	 * @param orgin the orgin
	 * @param direction the direction
	 */
	public ScrollBarControl(Spatial pickingSpatial, Spatial targetSpatial, int length, int barLength, int contentLength, int orgin, Direction direction) {
		super(pickingSpatial, pickingSpatial);
		parentSpatial = targetSpatial;
		this.targetSpatial = pickingSpatial;
		this.length = length;
		this.contentLenght = contentLength;
		this.orgin = orgin;
		this.barLength = barLength;		
		this.direction = direction;
	}


	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.elements.twod.OrthoControlPointRotateTranslateScale#applySingleCursorTransform()
	 */
	protected void applySingleCursorTransform() {

		Vector2f posChange = cursor1Pos.subtract(cursor1OldPos);
			
		if (direction==Direction.H){
			float newX = targetSpatial.getLocalTranslation().x + posChange.x;
		
			if (newX<orgin) newX = orgin;
			if ((newX+barLength/2)>length/2 ) newX = length/2-barLength/2;

				targetSpatial.getLocalTranslation().x = newX;
				worldLocations.add(new WorldCursorRecord(targetSpatial.getWorldTranslation().clone(),  new Quaternion(targetSpatial.getWorldRotation()).clone(), targetSpatial.getWorldScale().clone(), System.nanoTime()));

				for (ScrollBarListener l: scrollBarListeners)
					l.moved(newX-orgin, (newX-orgin)*contentLenght/length);
			
		}
		else{
			float newY = targetSpatial.getLocalTranslation().y + posChange.y;
			
			if (newY>orgin) newY=orgin;
			if ((newY-barLength/2)<-length/2 ) newY=-length/2+barLength/2;
			
				
				targetSpatial.getLocalTranslation().y=newY;
				worldLocations.add(new WorldCursorRecord(targetSpatial.getWorldTranslation().clone(),  new Quaternion(targetSpatial.getWorldRotation()).clone(), targetSpatial.getWorldScale().clone(), System.nanoTime()));

				for (ScrollBarListener l: scrollBarListeners)
					l.moved(newY-orgin, (newY-orgin)*contentLenght/length);

		}
	}

	/**
	 * Gets the current element2 d coords for cursor.
	 *
	 * @param x the x
	 * @param y the y
	 * @return the current element2 d coords for cursor
	 */
	private Vector2f getCurrentElement2DCoordsForCursor(float x, float y) {
		Vector3f pos = new Vector3f(x, y,0);
		Vector3f selectionLocal = new Vector3f();
		parentSpatial.worldToLocal(pos, selectionLocal);
		return new Vector2f(selectionLocal.x, selectionLocal.y);		
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.elements.twod.OrthoControlPointRotateTranslateScale#updateCursor1()
	 */
	@Override
	protected void updateCursor1() {

		Vector2f cc1 = getCurrentElement2DCoordsForCursor(getScreenCursorByIndex(0).getCurrentCursorScreenPosition().x, getScreenCursorByIndex(0).getCurrentCursorScreenPosition().y);
		Vector2f rotatedPosition = this.screenToTable(cc1.x, cc1.y);
		cursor1Pos.x = rotatedPosition.x;
		cursor1Pos.y = rotatedPosition.y;

		Vector2f cc1Old = getCurrentElement2DCoordsForCursor(getScreenCursorByIndex(0).getOldCursorScreenPosition().x, getScreenCursorByIndex(0).getOldCursorScreenPosition().y);
		rotatedPosition = this.screenToTable(cc1Old.x, cc1Old.y);
		cursor1OldPos.x = rotatedPosition.x;
		cursor1OldPos.y = rotatedPosition.y;
		
		
	}

	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.elements.twod.OrthoControlPointRotateTranslateScale#updateCursor2()
	 */
	@Override
	protected void updateCursor2() {

		Vector2f cc1 = getCurrentElement2DCoordsForCursor(getScreenCursorByIndex(1).getCurrentCursorScreenPosition().x, getScreenCursorByIndex(1).getCurrentCursorScreenPosition().y);
		Vector2f rotatedPosition = this.screenToTable(cc1.x, cc1.y);
		cursor2Pos.x = rotatedPosition.x;
		cursor2Pos.y = rotatedPosition.y;

		Vector2f cc2 = getCurrentElement2DCoordsForCursor(getScreenCursorByIndex(1).getOldCursorScreenPosition().x, getScreenCursorByIndex(1).getOldCursorScreenPosition().y);
		rotatedPosition = this.screenToTable(cc2.x, cc2.y);
		cursor2OldPos.x = rotatedPosition.x;
		cursor2OldPos.y = rotatedPosition.y;
	}
	
	/**
	 * Screen to table.
	 *
	 * @param x the x
	 * @param y the y
	 * @return the vector2f
	 */
	private Vector2f screenToTable(float x, float y) {

		if (targetSpatial.getParent()==null)
			return new Vector2f(x, y);

		Vector2f screenPosition = new Vector2f(x, y);
		float parentAngle = targetSpatial.getParent().getLocalRotation().toAngleAxis(Vector3f.UNIT_Z);
		Vector2f currentCenter = new Vector2f(targetSpatial.getParent().getLocalTranslation().x, targetSpatial.getParent().getLocalTranslation().y);
		Vector2f currentCenterToPoint = screenPosition.subtract(currentCenter);
		float newAngle = -(currentCenterToPoint.getAngle()-parentAngle);
		float length = currentCenterToPoint.length() / targetSpatial.getParent().getLocalScale().x;
		float newX = FastMath.cos(newAngle)*length;
		float newY = FastMath.sin(newAngle)*length;

		return new Vector2f(newX, newY);

	}


	/**
	 * Adds the scroll bar listener.
	 *
	 * @param l the l
	 */
	public void addScrollBarListener(ScrollBarListener l){
		scrollBarListeners.add(l);
	}

	/**
	 * Removes the scroll bar listener.
	 *
	 * @param l the l
	 */
	public void removeScrollBarListener(ScrollBarListener l){
		if (scrollBarListeners.contains(l))
			scrollBarListeners.remove(l);
	}

	/**
	 * The listener interface for receiving scrollBar events.
	 * The class that is interested in processing a scrollBar
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addScrollBarListener<code> method. When
	 * the scrollBar event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see ScrollBarEvent
	 */
	public interface ScrollBarListener {
		
		/**
		 * Moved.
		 *
		 * @param scrollBarMovedDistance the scroll bar moved distance
		 * @param contentMovedDistance the content moved distance
		 */
		public void moved(float scrollBarMovedDistance, float contentMovedDistance);
	}

}
