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

import java.util.ArrayList;
import java.util.List;

import com.jme.math.Quaternion;
import com.jme.math.Vector2f;
import com.jme.scene.Spatial;

import synergynetframework.jme.cursorsystem.TwoDMultiTouchElement;
import synergynetframework.jme.cursorsystem.cursordata.ScreenCursor;
import synergynetframework.jme.cursorsystem.fixutils.FixLocationStatus;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;

public class OrthoSnap extends TwoDMultiTouchElement {

	protected List<FixLocationStatus> fixLocations=null;
	protected float tolerance = 50;
	protected boolean allowSnapToOccupiedLocation = false;
	
	protected List<SnapListener> listeners = new ArrayList<SnapListener>();
	
	public OrthoSnap(Spatial pickingAndTargetSpatial) {
		super(pickingAndTargetSpatial);
	}
	
	public OrthoSnap(Spatial pickingSpatial, Spatial targetSpatial){
		super(pickingSpatial, targetSpatial);
	}
	
	public void setFixLocations(List<FixLocationStatus> fixLocations) {
		this.fixLocations = fixLocations;
	}

	public float getTolerance() {
		return tolerance;
	}

	public void setTolerance(float tolerance) {
		this.tolerance = tolerance;
	}

	public boolean isAllowSnapToOccupiedLocation() {
		return allowSnapToOccupiedLocation;
	}

	public void allowSnapToOccupiedLocation(boolean allowSnapToOccupiedLocation) {
		this.allowSnapToOccupiedLocation = allowSnapToOccupiedLocation;
	}
	
	@Override
	public void cursorChanged(ScreenCursor c, MultiTouchCursorEvent event) {	
	}
	
	@Override
	public void cursorClicked(ScreenCursor c, MultiTouchCursorEvent event) {		
	}

	@Override
	public void cursorPressed(ScreenCursor c, MultiTouchCursorEvent event) {
	}

	@Override
	public void cursorReleased(ScreenCursor c, MultiTouchCursorEvent event) {
		if (this.fixLocations==null)
			return;
		unSnap();
		Snap();

	}
	
	private void Snap() {

		Vector2f spatialLocationVector = new Vector2f(targetSpatial.getLocalTranslation().x, targetSpatial.getLocalTranslation().y);
		for (FixLocationStatus fixLocation: fixLocations){
			Vector2f fixLocationVector = new Vector2f(fixLocation.getLocation().x, fixLocation.getLocation().y);
			if (fixLocationVector.subtract(spatialLocationVector).length()<tolerance){
			
				if (!this.isAllowSnapToOccupiedLocation() && fixLocation.getOccupiedObjectsCount()>0 && !fixLocation.getOccupiedObjects().contains(targetSpatial))
					break;
				
				targetSpatial.setLocalTranslation(fixLocationVector.x, fixLocationVector.y, targetSpatial.getLocalTranslation().z);		
				fixLocation.getOccupiedObjects().add(targetSpatial);
				
				targetSpatial.setLocalScale(1);
				targetSpatial.setLocalRotation(new Quaternion().fromAngleAxis(0, AXIS_Z));
				
				for (SnapListener l: listeners){
					l.itemSnapped(this, targetSpatial, fixLocation);
				}
				
			}
		}	
	}
	
	private void unSnap(){
		
		FixLocationStatus unfixedLocation=null;
		Object unFixedObject = null;
		
		for (FixLocationStatus fixLocation: fixLocations){
			for (Object occupiedObject: fixLocation.getOccupiedObjects()){
				Vector2f fixLocationVector = new Vector2f(fixLocation.getLocation().x, fixLocation.getLocation().y);
				Spatial occupiedSpatial =(Spatial)occupiedObject;
				Vector2f spatialLocationVector = new Vector2f(occupiedSpatial.getLocalTranslation().x, occupiedSpatial.getLocalTranslation().y);
				if (fixLocationVector.subtract(spatialLocationVector).length()>tolerance){
					unfixedLocation = fixLocation;
					unFixedObject = occupiedObject;
				}
			}
			if (unfixedLocation!=null && unFixedObject!=null)
				unfixedLocation.getOccupiedObjects().remove(unFixedObject);
		}
		
	}

	public interface SnapListener{
		public void itemSnapped(OrthoSnap multiTouchElement, Spatial targetSpatial, FixLocationStatus fixLocationStatus);		

	}

	public void addSnapListener(SnapListener l){
		if (!listeners.contains(l))
			listeners.add(l);
	}
	
	public void removeSnapListener(SnapListener l){
		if (listeners.contains(l))
			listeners.remove(l);
	}


}
