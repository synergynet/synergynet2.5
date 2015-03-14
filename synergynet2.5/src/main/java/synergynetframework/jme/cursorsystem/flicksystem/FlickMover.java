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

package synergynetframework.jme.cursorsystem.flicksystem;

import java.util.ArrayList;
import java.util.List;


import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;

import core.SynergyNetDesktop;

import synergynetframework.jme.cursorsystem.MultiTouchElement;
import synergynetframework.jme.cursorsystem.ThreeDMultiTouchElement;
import synergynetframework.jme.cursorsystem.cursordata.ScreenCursor;
import synergynetframework.jme.cursorsystem.cursordata.WorldCursorRecord;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;


public class FlickMover extends ThreeDMultiTouchElement {

	public static float lowerThreshold = 0.2f;
	public static float speedLimit = 3000f;

	private boolean flickEnabled = false;
	public boolean toBeTransferred = false;
	private boolean elementReleased  = false;
	
	private boolean networkFlickMode = false;

	// units per second
	private Vector3f linearVelocity = Vector3f.ZERO;

	// units per second per second
	private float deceleration = 1f;

	private MultiTouchElement movingElement;
	protected List<FlickListener> listeners = new ArrayList<FlickListener>();


	public FlickMover(Spatial pickSpatial, MultiTouchElement movingElement, float deceleration, boolean networkFlickMode) {
		this(pickSpatial, pickSpatial, movingElement, deceleration, networkFlickMode);
	}

	public FlickMover(Spatial pickSpatial, Spatial targetSpatial, MultiTouchElement movingElement, float deceleration, boolean networkFlickMode) {
		super(pickSpatial, targetSpatial);
		this.movingElement = movingElement;
		this.deceleration = deceleration;
		this.networkFlickMode = networkFlickMode;
	}

	@Override
	public void cursorClicked(ScreenCursor c, MultiTouchCursorEvent event) {}

	@Override
	public void cursorPressed(ScreenCursor c, MultiTouchCursorEvent event) {
		switchFlickOff();
		elementReleased = false;
		toBeTransferred = false;
		linearVelocity = new Vector3f(0,0,0);
		movingElement.getWorldLocations().clear();
	}


	@Override
	public void cursorReleased(ScreenCursor c, MultiTouchCursorEvent event) {
		if(screenCursors.size() > 1) return;
		setLinearVelocity();
		switchFlickOn();
		elementReleased = true;
		movingElement.getWorldLocations().clear();
	}

	@Override
	public void cursorChanged(ScreenCursor c, MultiTouchCursorEvent event) {}


	private void switchFlickOn() {
		flickEnabled = true;
	}

	private void switchFlickOff() {
		flickEnabled = false;
	}
	


	public void update(float tpf) {

		if(flickEnabled) {
			moveToNewPosition(tpf);
			applyFriction(tpf);			
			if (!networkFlickMode)bounce();
			
		}
	}	

	public boolean elementReleased(){
		return elementReleased;
	}

	private void setLinearVelocity() {
		if(movingElement.getWorldLocations().size() < 4) return;

		List<WorldCursorRecord> positions = movingElement.getWorldLocations();
		int lastIndex = positions.size() - 1;
		int nextLastIndex = lastIndex - 2;
		Vector3f lastPosition = positions.get(lastIndex).worldLocation;
		long lastTime = positions.get(lastIndex).time;
		Vector3f nextLastPosition = positions.get(nextLastIndex).worldLocation;
		long nextLastTime = positions.get(nextLastIndex).time;
		float diffTimeMS = (lastTime - nextLastTime) / 1000000f;
		linearVelocity = lastPosition.subtract(nextLastPosition).mult(1f/diffTimeMS * 1000f);
		linearVelocity.setZ(0);
	}

	private void applyFriction(float tpf) {
		if(linearVelocity == null) return;
		
		if (targetSpatial.getWorldTranslation().x > SynergyNetDesktop.getInstance().getDisplayWidth() || targetSpatial.getWorldTranslation().x < 0 ||
				targetSpatial.getWorldTranslation().y > SynergyNetDesktop.getInstance().getDisplayHeight() || targetSpatial.getWorldTranslation().y < 0)return;
			
		Vector3f reduceBy = linearVelocity.normalize().mult(getDeceleration()* tpf);
		linearVelocity.subtractLocal(reduceBy);
		
		linearVelocity.subtractLocal(linearVelocity.mult(tpf * deceleration));
		
		if (FastMath.abs(linearVelocity.x) > FastMath.abs(linearVelocity.y)){
			if (FastMath.abs(linearVelocity.x) > speedLimit){
				float difference = FastMath.abs(linearVelocity.x)/speedLimit;
				if (linearVelocity.x < 0){
					linearVelocity.setX(-speedLimit);
				}else{
					linearVelocity.setX(speedLimit);
				}
				linearVelocity.setY(linearVelocity.y/difference);
			}
		}else{
			if (FastMath.abs(linearVelocity.y) > speedLimit){
				float difference = FastMath.abs(linearVelocity.y)/speedLimit;
				if (linearVelocity.y < 0){
					linearVelocity.setY(-speedLimit);
				}else{
					linearVelocity.setY(speedLimit);
				}
				linearVelocity.setX(linearVelocity.x/difference);
			}
		}
		if(linearVelocity.length() < lowerThreshold){
			flickEnabled = false;
		}
		
	}
	
	private void moveToNewPosition(float tpf) {
		if(linearVelocity == null) return;
		Vector3f previousSpeed = linearVelocity;
		Vector3f pos = targetSpatial.getLocalTranslation().clone();
		pos.addLocal(linearVelocity.mult(tpf));
		float oldX = targetSpatial.getLocalTranslation().x;
		float oldY = targetSpatial.getLocalTranslation().y;
		targetSpatial.setLocalTranslation(pos);
		targetSpatial.updateWorldVectors();
		float newX = targetSpatial.getLocalTranslation().x;
		float newY = targetSpatial.getLocalTranslation().y;

        if(FastMath.abs(previousSpeed.x) < FastMath.abs(linearVelocity.x)){
        	if (linearVelocity.x < 0){
        		linearVelocity.setX(-FastMath.abs(previousSpeed.x));
        	}else{
        		linearVelocity.setX(FastMath.abs(previousSpeed.x));
        	}
        }

        if(FastMath.abs(previousSpeed.y) < FastMath.abs(linearVelocity.y)){
        	if (linearVelocity.y < 0){
        		linearVelocity.setY(-FastMath.abs(previousSpeed.y));
        	}else{
        		linearVelocity.setY(FastMath.abs(previousSpeed.y));
        	}
        }
        if (listeners !=null){
			for (int i = 0; i < listeners.size(); i++){
		        if (listeners.get(i) !=null){
		        	listeners.get(i).itemFlicked(this, targetSpatial, newX, newY, oldX, oldY);
		        }
			}
        }
	}
	
	public void bounce() {

		if(movingElement != null){
			
			if (targetSpatial.getWorldTranslation().x > SynergyNetDesktop.getInstance().getDisplayWidth()){
				if (linearVelocity.x > 0)linearVelocity.setX(-linearVelocity.getX());				
			}else if (targetSpatial.getWorldTranslation().x < 0){
				if (linearVelocity.x < 0)linearVelocity.setX(-linearVelocity.getX());	
			}
			
			if (targetSpatial.getWorldTranslation().y > SynergyNetDesktop.getInstance().getDisplayHeight()){
				if (linearVelocity.y > 0)linearVelocity.setY(-linearVelocity.getY());				
			}else if (targetSpatial.getWorldTranslation().y < 0){
				if (linearVelocity.y < 0)linearVelocity.setY(-linearVelocity.getY());	
			}
		}
	}
	
	
	public void setDeceleration(float deceleration)	{
		this.deceleration = deceleration;
	}

	public void setLinearVelocity(Vector3f linearVelocity)	{
		this.linearVelocity = linearVelocity;
		flickEnabled = true;
	}

	public float getDeceleration()	{
		return deceleration;
	}

	public Vector3f getLinearVelocity()	{
		return linearVelocity;
	}

	public MultiTouchElement getMovingElement()	{
		return movingElement;
	}

	public void addFlickListener(FlickListener l){
		listeners.add(l);
	}

	public void removeFlickListener(FlickListener l){
		if (listeners.contains(l))
			listeners.remove(l);
	}

	public interface FlickListener{
		public void itemFlicked(FlickMover multiTouchElement, Spatial targetSpatial,  float newLocationX, float newLocationY, float oldLocationX, float oldLocationY);
	}

	public float getSpeed() {
		return FastMath.sqrt(FastMath.sqr(linearVelocity.x)+FastMath.sqr(linearVelocity.y));
	}

	public void setNetworkFlickMode(boolean networkFlickMode) {
		this.networkFlickMode = networkFlickMode;		
	}	
}

