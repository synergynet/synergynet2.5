/*
 * Copyright (c) 2008 University of Durham, England
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
import java.util.Iterator;
import java.util.List;

import synergynetframework.jme.cursorsystem.MultiTouchElement;
import synergynetframework.jme.cursorsystem.cursordata.ScreenCursor;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;

import com.jme.scene.Spatial;


/**
 * The Class OrthoBringToTop.
 */
public class OrthoBringToTop extends MultiTouchElement {

	/** The managed objects. */
	private static List<OrthoBringToTop> managedObjects = new ArrayList<OrthoBringToTop>();
	
	/** The target spatials. */
	private static List<Spatial> targetSpatials = new ArrayList<Spatial>();
	
	/** The bottom most. */
	public static int bottomMost = 0;
	
	/** The top most. */
	public static int topMost = 1000;
	
	/** The listeners. */
	protected List<OrthoBringToTopListener> listeners = new ArrayList<OrthoBringToTopListener>();
	
	/**
	 * Sets the top most.
	 *
	 * @param i the new top most
	 */
	public static void setTopMost(int i) {
		topMost = i;
	}
	
	/**
	 * Sets the bottom most.
	 *
	 * @param i the new bottom most
	 */
	public static void setBottomMost(int i ) {
		bottomMost = i;
	}	

	/**
	 * Instantiates a new ortho bring to top.
	 *
	 * @param spatial the spatial
	 */
	public OrthoBringToTop(Spatial spatial) {
		this(spatial, spatial);
	}
	
	/**
	 * Instantiates a new ortho bring to top.
	 *
	 * @param pickingSpatial the picking spatial
	 * @param targetSpatial the target spatial
	 */
	public OrthoBringToTop(Spatial pickingSpatial, Spatial targetSpatial) {
		super(pickingSpatial, targetSpatial);
		Iterator<OrthoBringToTop> iter = managedObjects.iterator();
		while(iter.hasNext()){
			OrthoBringToTop obTop = iter.next();
			if(obTop.getPickingSpatial().getName().equals(pickingSpatial.getName())){
				obTop.setActive(false);
				iter.remove();
			}
		}
		
		managedObjects.add(this);
		targetSpatials.clear();
		for(OrthoBringToTop obTop: managedObjects){
			if(!targetSpatials.contains(obTop.getTargetSpatial()))
				targetSpatials.add(obTop.getTargetSpatial());
		}
		
		this.setPickMeOnly(true);
	}
	
	/**
	 * Gets the all top level containers with ortho bring to top.
	 *
	 * @return the all top level containers with ortho bring to top
	 */
	public static List<Spatial> getAllTopLevelContainersWithOrthoBringToTop(){
		return targetSpatials;
	}
	
	/**
	 * Adds the ortho bring to top listener.
	 *
	 * @param l the l
	 */
	public void addOrthoBringToTopListener(OrthoBringToTopListener l){
		if (!listeners.contains(l))
			this.listeners.add(l);
	}
	
	/**
	 * Removes the ortho bring to top listener.
	 *
	 * @param l the l
	 */
	public void removeOrthoBringToTopListener(OrthoBringToTopListener l){
		this.listeners.remove(l);
	}

	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.MultiTouchElement#cursorChanged(synergynetframework.jme.cursorsystem.cursordata.ScreenCursor, synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorChanged(ScreenCursor c, MultiTouchCursorEvent event) {
	}

	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.MultiTouchElement#cursorClicked(synergynetframework.jme.cursorsystem.cursordata.ScreenCursor, synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorClicked(ScreenCursor c, MultiTouchCursorEvent event) {
	}

	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.MultiTouchElement#cursorPressed(synergynetframework.jme.cursorsystem.cursordata.ScreenCursor, synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorPressed(ScreenCursor c, MultiTouchCursorEvent event) {
		setTopObject(this, true);
	}

	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.MultiTouchElement#cursorReleased(synergynetframework.jme.cursorsystem.cursordata.ScreenCursor, synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorReleased(ScreenCursor c, MultiTouchCursorEvent event) {
	}
	

	/**
	 * Re sort by z order.
	 */
	private void reSortByZOrder(){
			
		int maxIndex=0;
		for (int i=0; i<targetSpatials.size()-1; i++){		
			maxIndex = i;
			for (int j=i; j<targetSpatials.size()-1; j++){
				if (targetSpatials.get(j).getZOrder()>targetSpatials.get(maxIndex).getZOrder()){
					maxIndex = j;
				}			
			}
			Spatial temp = targetSpatials.get(maxIndex);
			targetSpatials.remove(maxIndex);
			targetSpatials.add(i, temp);
		}
	}
	
	/**
	 * Sets the top object.
	 *
	 * @param orthoBringToTop the ortho bring to top
	 * @param enableBroadCast the enable broad cast
	 */
	public void setTopObject(OrthoBringToTop orthoBringToTop, boolean enableBroadCast) {
		
		reSortByZOrder();	
		targetSpatials.remove(orthoBringToTop.getTargetSpatial());
		targetSpatials.add(0, orthoBringToTop.getTargetSpatial());
		
		int z = topMost;
		for(Spatial s : targetSpatials) {
			if(z < bottomMost) z = bottomMost;
			s.setZOrder(z, true);
			z--;
		}
		
		if (!enableBroadCast)
			return;
		
		for (OrthoBringToTopListener l: this.listeners){
			l.ItemBringToTop();
		}
		
	}
	
	/**
	 * Sets the bottom object.
	 *
	 * @param orthoBringToTop the ortho bring to top
	 * @param enableBroadCast the enable broad cast
	 */
	public void setBottomObject(OrthoBringToTop orthoBringToTop, boolean enableBroadCast) {
		
		reSortByZOrder();	
		targetSpatials.remove(orthoBringToTop.getTargetSpatial());
		targetSpatials.add(orthoBringToTop.getTargetSpatial());
		
		int z = topMost;
		for(Spatial s : targetSpatials) {
			if(z < bottomMost) z = bottomMost;
			s.setZOrder(z, true);
			z--;
		}
		
		if (!enableBroadCast)
			return;
		
		for (OrthoBringToTopListener l: this.listeners){
			l.ItemBringToTop();
		}
		
	}
	
	/**
	 * The listener interface for receiving orthoBringToTop events.
	 * The class that is interested in processing a orthoBringToTop
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addOrthoBringToTopListener<code> method. When
	 * the orthoBringToTop event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see OrthoBringToTopEvent
	 */
	public interface OrthoBringToTopListener {
		
		/**
		 * Item bring to top.
		 */
		public void ItemBringToTop();			
	}
	
	/**
	 * Un register.
	 *
	 * @param orthoBringToTop the ortho bring to top
	 */
	public static void unRegister(OrthoBringToTop orthoBringToTop){
		if (managedObjects.contains(orthoBringToTop))
			managedObjects.remove(orthoBringToTop);
	}
	
	/**
	 * Un register.
	 *
	 * @param spatial the spatial
	 */
	public static void unRegister(Spatial spatial){
		
		Iterator<OrthoBringToTop> iter = managedObjects.iterator();
		while(iter.hasNext()){
			OrthoBringToTop obTop = iter.next();
			if(obTop.pickingSpatial.hashCode()==spatial.hashCode()){
				iter.remove();
				return;
			}
		}
	}
	
	
}
