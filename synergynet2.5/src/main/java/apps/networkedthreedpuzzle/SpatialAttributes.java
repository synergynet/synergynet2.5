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

package apps.networkedthreedpuzzle;

import java.io.Serializable;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;



/**
 * The Class SpatialAttributes.
 */
public class SpatialAttributes implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 544444444812321234L;
	
	/** The spatial name. */
	private String spatialName;
	
	/**
	 * Gets the spatial name.
	 *
	 * @return the spatial name
	 */
	public String getSpatialName() {
		return spatialName;
	}

	/**
	 * Sets the spatial name.
	 *
	 * @param spatialName the new spatial name
	 */
	public void setSpatialName(String spatialName) {
		this.spatialName = spatialName;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	public Vector3f getPosition() {
		return position;
	}

	/**
	 * Sets the position.
	 *
	 * @param position the new position
	 */
	public void setPosition(Vector3f position) {
		this.position = position;
	}

	/**
	 * Gets the rotation.
	 *
	 * @return the rotation
	 */
	public Quaternion getRotation() {
		return rotation;
	}

	/**
	 * Sets the rotation.
	 *
	 * @param rotation the new rotation
	 */
	public void setRotation(Quaternion rotation) {
		this.rotation = rotation;
	}

	/** The type. */
	private String type;
	
	/** The position. */
	private Vector3f position;
	
	/** The rotation. */
	private Quaternion rotation;

	/**
	 * Instantiates a new spatial attributes.
	 *
	 * @param spatialName the spatial name
	 * @param type the type
	 * @param position the position
	 * @param rotation the rotation
	 */
	public SpatialAttributes(String spatialName, String type, Vector3f position,
			Quaternion rotation) {
		this.spatialName = spatialName;
		this.type = type;
		this.position = position;
		this.rotation = rotation;
	}


}
