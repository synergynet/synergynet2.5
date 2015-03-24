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

package synergynetframework.appsystem.contentsystem.jme.items.utils.controlbar;

import java.util.ArrayList;
import java.util.List;

import com.jme.math.Vector2f;
import com.jme.scene.Spatial;

import synergynetframework.jme.cursorsystem.TwoDMultiTouchElement;
import synergynetframework.jme.cursorsystem.cursordata.ScreenCursor;
import synergynetframework.jme.cursorsystem.cursordata.ScreenCursorRecord;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;



/**
 * The Class ControlBarMover.
 */
public class ControlBarMover extends TwoDMultiTouchElement{

	/** The cursor1 pos. */
	protected Vector2f cursor1Pos = new Vector2f(); 	
	
	/** The cursor1 old pos. */
	protected Vector2f cursor1OldPos = new Vector2f(); 	
	
	/** The bar length. */
	protected float barLength =150f;
	
	/** The enabled. */
	protected boolean enabled = true;

	/** The listeners. */
	protected List<ControlBarMoverListener> listeners = new ArrayList<ControlBarMoverListener>();
	
	/**
	 * Instantiates a new control bar mover.
	 *
	 * @param pickingAndTargetSpatial the picking and target spatial
	 */
	public ControlBarMover(Spatial pickingAndTargetSpatial) {
		this(pickingAndTargetSpatial, pickingAndTargetSpatial);
		this.pickMeOnly = true;
	}

	/**
	 * Instantiates a new control bar mover.
	 *
	 * @param pickingSpatial the picking spatial
	 * @param targetSpatial the target spatial
	 */
	public ControlBarMover(Spatial pickingSpatial, Spatial targetSpatial) {
		super(pickingSpatial, targetSpatial);
		
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.MultiTouchElement#cursorChanged(synergynetframework.jme.cursorsystem.cursordata.ScreenCursor, synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorChanged(ScreenCursor c, MultiTouchCursorEvent event) {
	
		if (!enabled) return;
		updateCursor1();			
		applySingleCursorTransform();
		setOldCursor();
	}

	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.MultiTouchElement#cursorPressed(synergynetframework.jme.cursorsystem.cursordata.ScreenCursor, synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorPressed(ScreenCursor c, MultiTouchCursorEvent event) {
		if (!enabled) return;
		cursor1OldPos.x = c.getCurrentCursorScreenPosition().x;
		cursor1OldPos.y = c.getCurrentCursorScreenPosition().y;	
		
		for (ControlBarMoverListener l: listeners){			
			l.cursorPressed();
		}
		
	}

	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.MultiTouchElement#cursorReleased(synergynetframework.jme.cursorsystem.cursordata.ScreenCursor, synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorReleased(ScreenCursor c, MultiTouchCursorEvent event) {
		for (ControlBarMoverListener l: listeners){			
			l.cursorReleased();
		}
	}

	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.MultiTouchElement#cursorClicked(synergynetframework.jme.cursorsystem.cursordata.ScreenCursor, synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorClicked(ScreenCursor c, MultiTouchCursorEvent event) {}	
	
	/**
	 * Gets the bar length.
	 *
	 * @return the bar length
	 */
	public float getBarLength() {
		return barLength;
	}

	/**
	 * Sets the bar length.
	 *
	 * @param barLength the new bar length
	 */
	public void setBarLength(float barLength) {
		this.barLength = barLength;
	}

	/**
	 * Apply single cursor transform.
	 */
	protected void applySingleCursorTransform() {
		float minLocation = 0 - this.barLength/2;
		float maxLocation = this.barLength/2;
		float oldPosition = (targetSpatial.getLocalTranslation().x - (- this.barLength/2))/this.barLength;
		
		Vector2f posChange = cursor1Pos.subtract(cursor1OldPos);	
		Vector2f newPos = new Vector2f(targetSpatial.getLocalTranslation().x+posChange.x, targetSpatial.getLocalTranslation().y);
		if (newPos.x<minLocation)
			targetSpatial.getLocalTranslation().x = minLocation;
		else if (newPos.x>maxLocation)
			targetSpatial.getLocalTranslation().x = maxLocation;
		else 
			targetSpatial.getLocalTranslation().x = newPos.x;
		
		float newPosition = (targetSpatial.getLocalTranslation().x - (- this.barLength/2))/this.barLength;
		for (ControlBarMoverListener l: listeners){			
			l.controlBarChanged(oldPosition, newPosition);
		}	
	}

	
	/**
	 * Update cursor1.
	 */
	protected void updateCursor1() {
		cursor1Pos.x = getScreenCursorByIndex(0).getCurrentCursorScreenPosition().x;
		cursor1Pos.y = getScreenCursorByIndex(0).getCurrentCursorScreenPosition().y;

		cursor1OldPos.x = getScreenCursorByIndex(0).getOldCursorScreenPosition().x;
		cursor1OldPos.y = getScreenCursorByIndex(0).getOldCursorScreenPosition().y;
	}

	/**
	 * Sets the old cursor.
	 */
	private void setOldCursor(){
		for (ScreenCursor c:screenCursors){			
			ScreenCursorRecord s = new ScreenCursorRecord(c.getCurrentCursorScreenPosition().x, c.getCurrentCursorScreenPosition().y );
			c.setOldCursorScreenPosition(s);
		}
	}
	
	/**
	 * Adds the control bar mover listener.
	 *
	 * @param l the l
	 */
	public void addControlBarMoverListener(ControlBarMoverListener l){
		listeners.add(l);
	}
	
	/**
	 * Removes the control bar mover listener.
	 *
	 * @param l the l
	 */
	public void removeControlBarMoverListener(ControlBarMoverListener l){
		if (listeners.contains(l))
			listeners.remove(l);
	}
	
	/**
	 * The listener interface for receiving controlBarMover events.
	 * The class that is interested in processing a controlBarMover
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addControlBarMoverListener<code> method. When
	 * the controlBarMover event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see ControlBarMoverEvent
	 */
	public interface ControlBarMoverListener {
		
		/**
		 * Control bar changed.
		 *
		 * @param oldPosition the old position
		 * @param newPosition the new position
		 */
		public void controlBarChanged(float oldPosition, float newPosition);
		
		/**
		 * Cursor pressed.
		 */
		public void cursorPressed();
		
		/**
		 * Cursor released.
		 */
		public void cursorReleased();
	}

	/**
	 * Checks if is enabled.
	 *
	 * @return true, if is enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Sets the enabled.
	 *
	 * @param enabled the new enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	

}