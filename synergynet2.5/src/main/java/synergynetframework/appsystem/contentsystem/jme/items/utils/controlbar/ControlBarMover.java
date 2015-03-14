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


public class ControlBarMover extends TwoDMultiTouchElement{

	protected Vector2f cursor1Pos = new Vector2f(); 	
	protected Vector2f cursor1OldPos = new Vector2f(); 	
	protected float barLength =150f;
	protected boolean enabled = true;

	protected List<ControlBarMoverListener> listeners = new ArrayList<ControlBarMoverListener>();
	
	public ControlBarMover(Spatial pickingAndTargetSpatial) {
		this(pickingAndTargetSpatial, pickingAndTargetSpatial);
		this.pickMeOnly = true;
	}

	public ControlBarMover(Spatial pickingSpatial, Spatial targetSpatial) {
		super(pickingSpatial, targetSpatial);
		
	}
	
	@Override
	public void cursorChanged(ScreenCursor c, MultiTouchCursorEvent event) {
	
		if (!enabled) return;
		updateCursor1();			
		applySingleCursorTransform();
		setOldCursor();
	}

	@Override
	public void cursorPressed(ScreenCursor c, MultiTouchCursorEvent event) {
		if (!enabled) return;
		cursor1OldPos.x = c.getCurrentCursorScreenPosition().x;
		cursor1OldPos.y = c.getCurrentCursorScreenPosition().y;	
		
		for (ControlBarMoverListener l: listeners){			
			l.cursorPressed();
		}
		
	}

	@Override
	public void cursorReleased(ScreenCursor c, MultiTouchCursorEvent event) {
		for (ControlBarMoverListener l: listeners){			
			l.cursorReleased();
		}
	}

	@Override
	public void cursorClicked(ScreenCursor c, MultiTouchCursorEvent event) {}	
	
	public float getBarLength() {
		return barLength;
	}

	public void setBarLength(float barLength) {
		this.barLength = barLength;
	}

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

	
	protected void updateCursor1() {
		cursor1Pos.x = getScreenCursorByIndex(0).getCurrentCursorScreenPosition().x;
		cursor1Pos.y = getScreenCursorByIndex(0).getCurrentCursorScreenPosition().y;

		cursor1OldPos.x = getScreenCursorByIndex(0).getOldCursorScreenPosition().x;
		cursor1OldPos.y = getScreenCursorByIndex(0).getOldCursorScreenPosition().y;
	}

	private void setOldCursor(){
		for (ScreenCursor c:screenCursors){			
			ScreenCursorRecord s = new ScreenCursorRecord(c.getCurrentCursorScreenPosition().x, c.getCurrentCursorScreenPosition().y );
			c.setOldCursorScreenPosition(s);
		}
	}
	
	public void addControlBarMoverListener(ControlBarMoverListener l){
		listeners.add(l);
	}
	
	public void removeControlBarMoverListener(ControlBarMoverListener l){
		if (listeners.contains(l))
			listeners.remove(l);
	}
	
	public interface ControlBarMoverListener {
		public void controlBarChanged(float oldPosition, float newPosition);
		public void cursorPressed();
		public void cursorReleased();
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	

}