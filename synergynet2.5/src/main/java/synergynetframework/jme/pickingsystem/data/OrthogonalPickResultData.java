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


/**
 * The Class OrthogonalPickResultData.
 */
public class OrthogonalPickResultData extends PickResultData {
	
	/** The cursor screen position at pick. */
	protected Vector2f cursorScreenPositionAtPick;
	
	/** The spatial position at pick. */
	private Vector3f spatialPositionAtPick;

	/**
	 * Instantiates a new orthogonal pick result data.
	 *
	 * @param originatingCursorID the originating cursor id
	 * @param cursorScreenPositionAtPick the cursor screen position at pick
	 * @param pickedSpatial the picked spatial
	 */
	public OrthogonalPickResultData(long originatingCursorID, Vector2f cursorScreenPositionAtPick, Spatial pickedSpatial) {
		super(originatingCursorID, pickedSpatial);
		this.cursorScreenPositionAtPick = cursorScreenPositionAtPick;
		this.spatialPositionAtPick = pickedSpatial.getWorldTranslation().clone();
	}

	/**
	 * Gets the cursor screen position at pick.
	 *
	 * @return the cursor screen position at pick
	 */
	public Vector2f getCursorScreenPositionAtPick() {
		return cursorScreenPositionAtPick;
	}

	/**
	 * Gets the spatial world position at pick.
	 *
	 * @return the spatial world position at pick
	 */
	public Vector3f getSpatialWorldPositionAtPick() {
		return spatialPositionAtPick;
	}


}
