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
	
	/** The Constant AXIS_Z. */
	public static final Vector3f AXIS_Z = new Vector3f(0,0,1).normalize();
	
	/** The Constant DIRECTION_UP. */
	public static final Vector2f DIRECTION_UP = new Vector2f(0, 1);

	/** The name. */
	protected String name;

	/** The picking spatial. */
	protected Spatial pickingSpatial;
	
	/** The target spatial. */
	protected Spatial targetSpatial;

	/** The pick result data origins. */
	protected Map<Long,PickResultData> pickResultDataOrigins = new HashMap<Long,PickResultData>();
	
	/** The screen cursors. */
	protected Vector<ScreenCursor> screenCursors = new Vector<ScreenCursor>();
	
	/** The screen cursor origins. */
	protected Map<Long,ScreenCursorRecord> screenCursorOrigins = new HashMap<Long,ScreenCursorRecord>();

	/** The world locations. */
	protected List<WorldCursorRecord> worldLocations = new ArrayList<WorldCursorRecord>();

	/** The target spatial translation at origin. */
	protected Vector3f targetSpatialTranslationAtOrigin;
	
	/** The target spatial scale at origin. */
	protected Vector3f targetSpatialScaleAtOrigin;
	
	/** The target spatial rotation at origin. */
	protected Quaternion targetSpatialRotationAtOrigin;
	
	/** The screen cursor angle at origin. */
	protected float screenCursorAngleAtOrigin;

	/** The pick me only. */
	protected boolean pickMeOnly = false;
	
	/** The active. */
	protected boolean active = true;

	/**
	 * Instantiates a new multi touch element.
	 *
	 * @param pickingAndTargetSpatial the picking and target spatial
	 */
	public MultiTouchElement(Spatial pickingAndTargetSpatial) {
		this(pickingAndTargetSpatial, pickingAndTargetSpatial);
	}

	/**
	 * Instantiates a new multi touch element.
	 *
	 * @param pickingSpatial the picking spatial
	 * @param targetSpatial the target spatial
	 */
	public MultiTouchElement(Spatial pickingSpatial, Spatial targetSpatial) {
		if(pickingSpatial.getWorldBound() == null) Log.error(pickingSpatial.getName() + " is UNPICKABLE!");
		this.name = pickingSpatial.getName();
		this.pickingSpatial = pickingSpatial;
		this.targetSpatial = targetSpatial;
		MultiTouchElementRegistry.getInstance().register(this);
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name){
		MultiTouchElementRegistry.getInstance().unregister(this);
		this.name = name;
		MultiTouchElementRegistry.getInstance().register(this);
	}

	/**
	 * Gets the picking spatial.
	 *
	 * @return the picking spatial
	 */
	public Spatial getPickingSpatial() {
		return pickingSpatial;
	}

	/**
	 * Gets the target spatial.
	 *
	 * @return the target spatial
	 */
	public Spatial getTargetSpatial() {
		return targetSpatial;
	}





	/**
	 * Register a cursor with this multi touch element.
	 *
	 * @param c the c
	 * @param nodeloc the nodeloc
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
	 *
	 * @param index the index
	 * @return the screen cursor by index
	 */
	public ScreenCursor getScreenCursorByIndex(int index) {
		return screenCursors.elementAt(index);
	}

	/**
	 * Get a screen cursor registered with this MultiTouchElement by its id.
	 *
	 * @param id the id
	 * @return the screen cursor by id
	 */
	public ScreenCursor getScreenCursorByID(long id) {
		for(int i = 0; i < screenCursors.size(); i++) {
			if(screenCursors.elementAt(i).getID() == id) return screenCursors.elementAt(i);
		}
		return null;
	}

	/**
	 * Removes the screen cursor association with this MultiTouchElement.
	 *
	 * @param c the c
	 */
	public void unregisterScreenCursor(ScreenCursor c) {
		pickResultDataOrigins.remove(c.getID());
		screenCursorOrigins.remove(c.getID());
		screenCursors.remove(c);
	}

	/**
	 * Gets the pick result from cursor index.
	 *
	 * @param index the index
	 * @return the pick result from cursor index
	 */
	public PickResultData getPickResultFromCursorIndex(int index) {
		return getScreenCursorByIndex(index).getPickResult();
	}


	/**
	 * Sets the pick me only.
	 *
	 * @param pickMeOnly the new pick me only
	 */
	public void setPickMeOnly(boolean pickMeOnly) {
		this.pickMeOnly = pickMeOnly;
	}

	/**
	 * Checks if is pick me only.
	 *
	 * @return true, if is pick me only
	 */
	public boolean isPickMeOnly() {
		return pickMeOnly;
	}

	/**
	 * Sets the active.
	 *
	 * @param b the new active
	 */
	public void setActive(boolean b) {
		this.active = b;
	}

	/**
	 * Checks if is active.
	 *
	 * @return true, if is active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Gets the current screen angle difference from origin.
	 *
	 * @return the current screen angle difference from origin
	 */
	public float getCurrentScreenAngleDifferenceFromOrigin() {
		return getCurrentScreenCursorsAngle() - screenCursorAngleAtOrigin;
	}

	/**
	 * Gets the screen cursor origin for cursor id.
	 *
	 * @param id the id
	 * @return the screen cursor origin for cursor id
	 */
	public ScreenCursorRecord getScreenCursorOriginForCursorID(long id) {
		return screenCursorOrigins.get(id);
	}

	/**
	 * Gets the translation at origin.
	 *
	 * @return the translation at origin
	 */
	public Vector3f getTranslationAtOrigin() {
		return targetSpatialTranslationAtOrigin;
	}

	/**
	 * Gets the scale at origin.
	 *
	 * @return the scale at origin
	 */
	public Vector3f getScaleAtOrigin() {
		return targetSpatialScaleAtOrigin;
	}

	/**
	 * Gets the pick data for cursor id.
	 *
	 * @param id the id
	 * @return the pick data for cursor id
	 */
	public PickResultData getPickDataForCursorID(long id) {
		return pickResultDataOrigins.get(id);
	}

	/**
	 * Gets the num registered cursors.
	 *
	 * @return the num registered cursors
	 */
	public int getNumRegisteredCursors() {
		return screenCursors.size();
	}

	/**
	 * Adds the world cursor record.
	 *
	 * @param worldCursorRecord the world cursor record
	 */
	public synchronized void addWorldCursorRecord(WorldCursorRecord worldCursorRecord) {
		worldLocations.add(worldCursorRecord);
	}

	/**
	 * Gets the world locations.
	 *
	 * @return the world locations
	 */
	public List<WorldCursorRecord> getWorldLocations() {
		return worldLocations;
	}

	/**
	 * Gets the current target spatial rotation from cursor change.
	 *
	 * @return the current target spatial rotation from cursor change
	 */
	public Quaternion getCurrentTargetSpatialRotationFromCursorChange() {
		Quaternion tq = new Quaternion();
		tq.fromAngleAxis(getOriginScreenCursorsAngle() - getCurrentScreenCursorsAngle(), AXIS_Z);
		return tq.mult(targetSpatialRotationAtOrigin);
	}

	/**
	 * Gets the origin screen cursors angle.
	 *
	 * @return the origin screen cursors angle
	 */
	public float getOriginScreenCursorsAngle() {
		Vector2f originalScreenPosCursor1 = getScreenCursorByIndex(0).getCursorOrigin().getPosition();
		Vector2f originalScreenPosCursor2 = getScreenCursorByIndex(1).getCursorOrigin().getPosition();
		return originalScreenPosCursor2.subtract(originalScreenPosCursor1).getAngle();
	}

	/**
	 * Gets the current screen cursors angle.
	 *
	 * @return the current screen cursors angle
	 */
	public float getCurrentScreenCursorsAngle() {
		Vector2f screenPosCursor1 = getScreenCursorByIndex(0).getCurrentCursorScreenPosition().getPosition();
		Vector2f screenPosCursor2 = getScreenCursorByIndex(1).getCurrentCursorScreenPosition().getPosition();
		return screenPosCursor2.subtract(screenPosCursor1).getAngle();
	}

	// ******** abstract methods *********
	/**
	 * Cursor changed.
	 *
	 * @param c the c
	 * @param event the event
	 */
	public abstract void cursorChanged(ScreenCursor c, MultiTouchCursorEvent event);
	
	/**
	 * Cursor released.
	 *
	 * @param c the c
	 * @param event the event
	 */
	public abstract void cursorReleased(ScreenCursor c, MultiTouchCursorEvent event);
	
	/**
	 * Cursor pressed.
	 *
	 * @param c the c
	 * @param event the event
	 */
	public abstract void cursorPressed(ScreenCursor c, MultiTouchCursorEvent event);
	
	/**
	 * Cursor clicked.
	 *
	 * @param c the c
	 * @param event the event
	 */
	public abstract void cursorClicked(ScreenCursor c, MultiTouchCursorEvent event);

	// ********* private methods ********

	/**
	 * Record screen cursor origin.
	 *
	 * @param sc the sc
	 */
	private void recordScreenCursorOrigin(ScreenCursor sc) {
		screenCursorOrigins.put(sc.getID(), new ScreenCursorRecord(sc.getCurrentCursorScreenPosition()));
	}

}