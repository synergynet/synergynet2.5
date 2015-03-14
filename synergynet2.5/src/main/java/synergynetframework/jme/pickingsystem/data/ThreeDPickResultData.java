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

package synergynetframework.jme.pickingsystem.data;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.system.DisplaySystem;

public class ThreeDPickResultData extends PickResultData {
	private Vector3f fullCursorScreenPositionAtPick = new Vector3f();
	protected Vector2f cursorScreenPositionAtPick = new Vector2f();
	protected Vector2f spatialScreenLocationAtPick = new Vector2f();
	protected Vector2f cursorToSpatialScreenOffset = new Vector2f();
	protected Vector3f cursorToSpatialWorldOffset = new Vector3f();
	private Vector3f pointOfSelection;
	
	public ThreeDPickResultData(long originatingCursorID, Vector2f cursorScreenPositionAtPick, Spatial pickedSpatial) {
		super(originatingCursorID, pickedSpatial);
		this.cursorScreenPositionAtPick = cursorScreenPositionAtPick;
		storeAdditionalInfo();
	}
	
	private void storeAdditionalInfo() {
		// store screen location information
		DisplaySystem.getDisplaySystem().getScreenCoordinates(pickedSpatial.getWorldTranslation(), fullCursorScreenPositionAtPick);
		this.spatialScreenLocationAtPick = new Vector2f(fullCursorScreenPositionAtPick.x, fullCursorScreenPositionAtPick.y);
		this.cursorToSpatialScreenOffset = cursorScreenPositionAtPick.subtract(spatialScreenLocationAtPick);
		
		// store world location information
		DisplaySystem.getDisplaySystem().getWorldCoordinates(cursorScreenPositionAtPick, fullCursorScreenPositionAtPick.z, this.cursorToSpatialWorldOffset);
		cursorToSpatialWorldOffset.subtractLocal(pickedSpatial.getWorldTranslation());
	}
	
	/**
	 * Position of the cursor when the spatial was picked
	 * @return
	 */
	public Vector2f getCursorScreenPositionAtPick() {
		return cursorScreenPositionAtPick;
	}

	/**
	 * Screen position of the picked spatial when it was picked
	 * @return
	 */
	public Vector2f getSpatialScreenLocationAtPick() {
		return spatialScreenLocationAtPick;
	}

	/**
	 * Difference between the screen location of the spatial
	 * and the screen location of the cursor
	 * @return
	 */
	public Vector2f getCursorToSpatialScreenOffset() {
		return cursorToSpatialScreenOffset;
	}
	
	/**
	 * Where, in world coordinates, the pick was made
	 * @param pointOfSelection
	 */
	public void setPointOfSelection(Vector3f pointOfSelection) {
		this.pointOfSelection = pointOfSelection;		
	}
	
	/**
	 * Where, in world coordinates, the pick was made
	 * @return
	 */
	public Vector3f getPointOfSelection() {
		return this.pointOfSelection;
	}
}
