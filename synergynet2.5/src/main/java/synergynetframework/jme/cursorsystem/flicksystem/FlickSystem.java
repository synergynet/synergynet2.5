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

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.jme.math.Vector3f;
import com.jme.scene.Spatial;

import synergynetframework.jme.Updateable;
import synergynetframework.jme.cursorsystem.MultiTouchElement;
import synergynetframework.jme.cursorsystem.MultiTouchElementRegistry;

public class FlickSystem implements Updateable {
	private static FlickSystem instance = new FlickSystem();
	protected static Queue<FlickMover> movableElements = new ConcurrentLinkedQueue<FlickMover>();
	private static boolean networkFlickMode = false;

	public static FlickSystem getInstance() {
		return instance;
	}

	private FlickSystem() {}

	public void makeFlickable(Spatial s, MultiTouchElement movingElement, float deceleration) {
		this.makeFlickable(s, s, movingElement, deceleration);
	}

	public void makeFlickable(Spatial pickingSpatial, Spatial targetSpatial, MultiTouchElement movingElement, float deceleration) {
		FlickMover fm = new FlickMover(pickingSpatial, targetSpatial, movingElement, deceleration, networkFlickMode);
		movableElements.add(fm);
	}

	public void update(float timePerFrame) {
		for(FlickMover fm : movableElements) {
			fm.update(timePerFrame);
		}
	}

	public void makeUnflickable(Spatial s){
		for(Iterator<FlickMover> iter = movableElements.iterator(); iter.hasNext();){
			FlickMover flickMover = iter.next();

			if(flickMover.getTargetSpatial().getName().equals(s.getName())){
				iter.remove();
				if(MultiTouchElementRegistry.getInstance().isRegistered(flickMover)) {
					MultiTouchElementRegistry.getInstance().unregister(flickMover);
				}
			}
		}
	}

	public boolean isFlickable(Spatial s){
		for(FlickMover fm: movableElements){
			if(fm.getTargetSpatial().getName().equals(s.getName()))
				return true;
		}
		return false;
	}


	public void flick(Spatial s, Vector3f linearVelocity, float deceleration){
		for(FlickMover fm : movableElements){
			if(fm.getTargetSpatial().getName().equals(s.getName()))	{
				fm.setDeceleration(deceleration);
				fm.setLinearVelocity(linearVelocity);
			}
		}


	}

	public FlickMover getMovingElement(Spatial s){
		for(FlickMover fm : movableElements){
			if(fm.getTargetSpatial().getName().equals(s.getName()))	{
				return fm;
			}
		}
		return null;
	}

	public Queue<FlickMover> getMovingElements(){
		return movableElements;
	}

	public static FlickMover getFlickMover(Spatial s){
		for(Iterator<FlickMover> iter = movableElements.iterator(); iter.hasNext();){
			FlickMover flickMover = iter.next();
			if(flickMover.getTargetSpatial().getName().equals(s.getName())){
				return flickMover;
			}
		}
		return null;
	}

	/**
	 * @return the networkFlickMode
	 */
	public static boolean isNetworkFlickMode() {
		return networkFlickMode;
	}

	/**
	 * @param networkFlickMode the networkFlickMode to set
	 */
	public static void setNetworkFlickMode(boolean networkFlickMode) {
		FlickSystem.networkFlickMode = networkFlickMode;
		for(FlickMover fm : movableElements){
			fm.setNetworkFlickMode(networkFlickMode);
		}
		
	}
	
}
