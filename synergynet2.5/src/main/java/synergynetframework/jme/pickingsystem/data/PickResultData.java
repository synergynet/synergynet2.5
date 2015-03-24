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

import com.jme.scene.Spatial;


/**
 * The Class PickResultData.
 */
public class PickResultData {
	
	/** The picked spatial. */
	protected Spatial pickedSpatial;
	
	/** The originating cursor id. */
	protected long originatingCursorID;
	
	/** The distance. */
	protected float distance;
	
	/**
	 * Instantiates a new pick result data.
	 *
	 * @param originatingCursorID the originating cursor id
	 * @param pickedSpatial the picked spatial
	 */
	public PickResultData(long originatingCursorID, Spatial pickedSpatial) {
		this.originatingCursorID = originatingCursorID;
		this.pickedSpatial = pickedSpatial;
	}
	
	/**
	 * Retrieve the ID of the cursor that caused the pick.
	 *
	 * @return the originating cursor id
	 */
	public long getOriginatingCursorID() {
		return originatingCursorID;
	}
	
	/**
	 * The jME spatial that was picked.
	 *
	 * @return the picked spatial
	 */
	public Spatial getPickedSpatial() {
		return pickedSpatial;
	}
	
	/**
	 * Utility method to get the name of the spatial.
	 *
	 * @return the picked spatial name
	 */
	public String getPickedSpatialName() {
		return pickedSpatial.getName();
	}
	
	/**
	 * Sets the distance when picked.
	 *
	 * @param distance the new distance when picked
	 */
	public void setDistanceWhenPicked(float distance) {
		this.distance = distance;		
	}
	
	/**
	 * Gets the distance when picked.
	 *
	 * @return the distance when picked
	 */
	public float getDistanceWhenPicked() {
		return distance;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return this.getClass().getName() + ": " + pickedSpatial.getName();
	}

}
