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



/**
 * The Class FlickMover.
 */
public class FlickMover extends ThreeDMultiTouchElement {

	/** The lower threshold. */
	public static float lowerThreshold = 0.2f;
	
	/** The speed limit. */
	public static float speedLimit = 3000f;

	/** The flick enabled. */
	private boolean flickEnabled = false;
	
	/** The to be transferred. */
	public boolean toBeTransferred = false;
	
	/** The element released. */
	private boolean elementReleased  = false;
	
	/** The network flick mode. */
	private boolean networkFlickMode = false;

	// units per second
	/** The linear velocity. */
	private Vector3f linearVelocity = Vector3f.ZERO;

	// units per second per second
	/** The deceleration. */
	private float deceleration = 1f;

	/** The moving element. */
	private MultiTouchElement movingElement;
	
	/** The listeners. */
	protected List<FlickListener> listeners = new ArrayList<FlickListener>();


	/**
	 * Instantiates a new flick mover.
	 *
	 * @param pickSpatial the pick spatial
	 * @param movingElement the moving element
	 * @param deceleration the deceleration
	 * @param networkFlickMode the network flick mode
	 */
	public FlickMover(Spatial pickSpatial, MultiTouchElement movingElement, float deceleration, boolean networkFlickMode) {
		this(pickSpatial, pickSpatial, movingElement, deceleration, networkFlickMode);
	}

	/**
	 * Instantiates a new flick mover.
	 *
	 * @param pickSpatial the pick spatial
	 * @param targetSpatial the target spatial
	 * @param movingElement the moving element
	 * @param deceleration the deceleration
	 * @param networkFlickMode the network flick mode
	 */
	public FlickMover(Spatial pickSpatial, Spatial targetSpatial, MultiTouchElement movingElement, float deceleration, boolean networkFlickMode) {
		super(pickSpatial, targetSpatial);
		this.movingElement = movingElement;
		this.deceleration = deceleration;
		this.networkFlickMode = networkFlickMode;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.MultiTouchElement#cursorClicked(synergynetframework.jme.cursorsystem.cursordata.ScreenCursor, synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorClicked(ScreenCursor c, MultiTouchCursorEvent event) {}

	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.MultiTouchElement#cursorPressed(synergynetframework.jme.cursorsystem.cursordata.ScreenCursor, synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorPressed(ScreenCursor c, MultiTouchCursorEvent event) {
		switchFlickOff();
		elementReleased = false;
		toBeTransferred = false;
		linearVelocity = new Vector3f(0,0,0);
		movingElement.getWorldLocations().clear();
	}


	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.MultiTouchElement#cursorReleased(synergynetframework.jme.cursorsystem.cursordata.ScreenCursor, synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorReleased(ScreenCursor c, MultiTouchCursorEvent event) {
		if(screenCursors.size() > 1) return;
		setLinearVelocity();
		switchFlickOn();
		elementReleased = true;
		movingElement.getWorldLocations().clear();
	}

	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.MultiTouchElement#cursorChanged(synergynetframework.jme.cursorsystem.cursordata.ScreenCursor, synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorChanged(ScreenCursor c, MultiTouchCursorEvent event) {}


	/**
	 * Switch flick on.
	 */
	private void switchFlickOn() {
		flickEnabled = true;
	}

	/**
	 * Switch flick off.
	 */
	private void switchFlickOff() {
		flickEnabled = false;
	}
	


	/**
	 * Update.
	 *
	 * @param tpf the tpf
	 */
	public void update(float tpf) {

		if(flickEnabled) {
			moveToNewPosition(tpf);
			applyFriction(tpf);			
			if (!networkFlickMode)bounce();
			
		}
	}	

	/**
	 * Element released.
	 *
	 * @return true, if successful
	 */
	public boolean elementReleased(){
		return elementReleased;
	}

	/**
	 * Sets the linear velocity.
	 */
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

	/**
	 * Apply friction.
	 *
	 * @param tpf the tpf
	 */
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
	
	/**
	 * Move to new position.
	 *
	 * @param tpf the tpf
	 */
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
	
	/**
	 * Bounce.
	 */
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
	
	
	/**
	 * Sets the deceleration.
	 *
	 * @param deceleration the new deceleration
	 */
	public void setDeceleration(float deceleration)	{
		this.deceleration = deceleration;
	}

	/**
	 * Sets the linear velocity.
	 *
	 * @param linearVelocity the new linear velocity
	 */
	public void setLinearVelocity(Vector3f linearVelocity)	{
		this.linearVelocity = linearVelocity;
		flickEnabled = true;
	}

	/**
	 * Gets the deceleration.
	 *
	 * @return the deceleration
	 */
	public float getDeceleration()	{
		return deceleration;
	}

	/**
	 * Gets the linear velocity.
	 *
	 * @return the linear velocity
	 */
	public Vector3f getLinearVelocity()	{
		return linearVelocity;
	}

	/**
	 * Gets the moving element.
	 *
	 * @return the moving element
	 */
	public MultiTouchElement getMovingElement()	{
		return movingElement;
	}

	/**
	 * Adds the flick listener.
	 *
	 * @param l the l
	 */
	public void addFlickListener(FlickListener l){
		listeners.add(l);
	}

	/**
	 * Removes the flick listener.
	 *
	 * @param l the l
	 */
	public void removeFlickListener(FlickListener l){
		if (listeners.contains(l))
			listeners.remove(l);
	}

	/**
	 * The listener interface for receiving flick events.
	 * The class that is interested in processing a flick
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addFlickListener<code> method. When
	 * the flick event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see FlickEvent
	 */
	public interface FlickListener{
		
		/**
		 * Item flicked.
		 *
		 * @param multiTouchElement the multi touch element
		 * @param targetSpatial the target spatial
		 * @param newLocationX the new location x
		 * @param newLocationY the new location y
		 * @param oldLocationX the old location x
		 * @param oldLocationY the old location y
		 */
		public void itemFlicked(FlickMover multiTouchElement, Spatial targetSpatial,  float newLocationX, float newLocationY, float oldLocationX, float oldLocationY);
	}

	/**
	 * Gets the speed.
	 *
	 * @return the speed
	 */
	public float getSpeed() {
		return FastMath.sqrt(FastMath.sqr(linearVelocity.x)+FastMath.sqr(linearVelocity.y));
	}

	/**
	 * Sets the network flick mode.
	 *
	 * @param networkFlickMode the new network flick mode
	 */
	public void setNetworkFlickMode(boolean networkFlickMode) {
		this.networkFlickMode = networkFlickMode;		
	}	
}

