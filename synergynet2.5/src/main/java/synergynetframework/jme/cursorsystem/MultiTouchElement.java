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

package synergynetframework.jme.cursorsystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import synergynetframework.jme.cursorsystem.cursordata.ScreenCursor;
import synergynetframework.jme.cursorsystem.cursordata.ScreenCursorRecord;
import synergynetframework.jme.cursorsystem.cursordata.WorldCursorRecord;
import synergynetframework.jme.pickingsystem.data.PickResultData;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;

import com.jme.math.Quaternion;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.sun.media.Log;



/**
 * Super class for elements that provide multi-touch interaction on a particular
 * JME Spatial.  A MultiTouch element allows the picking element to be different
 * from the actual object manipulated, but for most applications, the picking
 * elements will be the same as the manipulated object.
 *
 * @author dcs0ah1
 *
 */
public abstract class MultiTouchElement {
	public static final Vector3f AXIS_Z = new Vector3f(0,0,1).normalize();
	public static final Vector2f DIRECTION_UP = new Vector2f(0, 1);

	protected String name;

	protected Spatial pickingSpatial;
	protected Spatial targetSpatial;

	protected Map<Long,PickResultData> pickResultDataOrigins = new HashMap<Long,PickResultData>();
	protected Vector<ScreenCursor> screenCursors = new Vector<ScreenCursor>();
	protected Map<Long,ScreenCursorRecord> screenCursorOrigins = new HashMap<Long,ScreenCursorRecord>();

	protected List<WorldCursorRecord> worldLocations = new ArrayList<WorldCursorRecord>();

	protected Vector3f targetSpatialTranslationAtOrigin;
	protected Vector3f targetSpatialScaleAtOrigin;
	protected Quaternion targetSpatialRotationAtOrigin;
	protected float screenCursorAngleAtOrigin;

	protected boolean pickMeOnly = false;
	protected boolean active = true;

	public MultiTouchElement(Spatial pickingAndTargetSpatial) {
		this(pickingAndTargetSpatial, pickingAndTargetSpatial);
	}

	public MultiTouchElement(Spatial pickingSpatial, Spatial targetSpatial) {
		if(pickingSpatial.getWorldBound() == null) Log.error(pickingSpatial.getName() + " is UNPICKABLE!");
		this.name = pickingSpatial.getName();
		this.pickingSpatial = pickingSpatial;
		this.targetSpatial = targetSpatial;
		MultiTouchElementRegistry.getInstance().register(this);
	}

	public String getName() {
		return name;
	}

	public void setName(String name){
		MultiTouchElementRegistry.getInstance().unregister(this);
		this.name = name;
		MultiTouchElementRegistry.getInstance().register(this);
	}

	public Spatial getPickingSpatial() {
		return pickingSpatial;
	}

	public Spatial getTargetSpatial() {
		return targetSpatial;
	}





	/**
	 * Register a cursor with this multi touch element.
	 */
	public void registerScreenCursor(ScreenCursor c, PickResultData nodeloc) {
		pickResultDataOrigins.put(c.getID(), nodeloc);
		if(screenCursors.contains(c)) return;
		screenCursors.add(c);
		recordScreenCursorOrigin(c);

		targetSpatialTranslationAtOrigin = targetSpatial.getLocalTranslation().clone();
		if(screenCursors.size() == 2) {
			targetSpatialRotationAtOrigin = new Quaternion(targetSpatial.getLocalRotation());
			screenCursorAngleAtOrigin = getCurrentScreenCursorsAngle();
			targetSpatialScaleAtOrigin = targetSpatial.getLocalScale().clone();
		}
	}

	/**
	 * Get a screen cursor registered with this MultiTouchElement by its index.
	 * @param index
	 * @return
	 */
	public ScreenCursor getScreenCursorByIndex(int index) {
		return screenCursors.elementAt(index);
	}

	/**
	 * Get a screen cursor registered with this MultiTouchElement by its id.
	 * @param id
	 * @return
	 */
	public ScreenCursor getScreenCursorByID(long id) {
		for(int i = 0; i < screenCursors.size(); i++) {
			if(screenCursors.elementAt(i).getID() == id) return screenCursors.elementAt(i);
		}
		return null;
	}

	/**
	 * Removes the screen cursor association with this MultiTouchElement
	 * @param c
	 */
	public void unregisterScreenCursor(ScreenCursor c) {
		pickResultDataOrigins.remove(c.getID());
		screenCursorOrigins.remove(c.getID());
		screenCursors.remove(c);
	}

	public PickResultData getPickResultFromCursorIndex(int index) {
		return getScreenCursorByIndex(index).getPickResult();
	}


	public void setPickMeOnly(boolean pickMeOnly) {
		this.pickMeOnly = pickMeOnly;
	}

	public boolean isPickMeOnly() {
		return pickMeOnly;
	}

	public void setActive(boolean b) {
		this.active = b;
	}

	public boolean isActive() {
		return active;
	}

	public float getCurrentScreenAngleDifferenceFromOrigin() {
		return getCurrentScreenCursorsAngle() - screenCursorAngleAtOrigin;
	}

	public ScreenCursorRecord getScreenCursorOriginForCursorID(long id) {
		return screenCursorOrigins.get(id);
	}

	public Vector3f getTranslationAtOrigin() {
		return targetSpatialTranslationAtOrigin;
	}

	public Vector3f getScaleAtOrigin() {
		return targetSpatialScaleAtOrigin;
	}

	public PickResultData getPickDataForCursorID(long id) {
		return pickResultDataOrigins.get(id);
	}

	public int getNumRegisteredCursors() {
		return screenCursors.size();
	}

	public synchronized void addWorldCursorRecord(WorldCursorRecord worldCursorRecord) {
		worldLocations.add(worldCursorRecord);
	}

	public List<WorldCursorRecord> getWorldLocations() {
		return worldLocations;
	}

	public Quaternion getCurrentTargetSpatialRotationFromCursorChange() {
		Quaternion tq = new Quaternion();
		tq.fromAngleAxis(getOriginScreenCursorsAngle() - getCurrentScreenCursorsAngle(), AXIS_Z);
		return tq.mult(targetSpatialRotationAtOrigin);
	}

	public float getOriginScreenCursorsAngle() {
		Vector2f originalScreenPosCursor1 = getScreenCursorByIndex(0).getCursorOrigin().getPosition();
		Vector2f originalScreenPosCursor2 = getScreenCursorByIndex(1).getCursorOrigin().getPosition();
		return originalScreenPosCursor2.subtract(originalScreenPosCursor1).getAngle();
	}

	public float getCurrentScreenCursorsAngle() {
		Vector2f screenPosCursor1 = getScreenCursorByIndex(0).getCurrentCursorScreenPosition().getPosition();
		Vector2f screenPosCursor2 = getScreenCursorByIndex(1).getCurrentCursorScreenPosition().getPosition();
		return screenPosCursor2.subtract(screenPosCursor1).getAngle();
	}

	// ******** abstract methods *********
	public abstract void cursorChanged(ScreenCursor c, MultiTouchCursorEvent event);
	public abstract void cursorReleased(ScreenCursor c, MultiTouchCursorEvent event);
	public abstract void cursorPressed(ScreenCursor c, MultiTouchCursorEvent event);
	public abstract void cursorClicked(ScreenCursor c, MultiTouchCursorEvent event);

	// ********* private methods ********

	private void recordScreenCursorOrigin(ScreenCursor sc) {
		screenCursorOrigins.put(sc.getID(), new ScreenCursorRecord(sc.getCurrentCursorScreenPosition()));
	}

}