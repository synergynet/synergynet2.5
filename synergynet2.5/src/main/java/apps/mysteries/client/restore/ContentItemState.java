/*
 * Copyright (c) 2009 University of Durham, England All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of 'SynergyNet' nor the names of
 * its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission. THIS SOFTWARE IS PROVIDED
 * BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package apps.mysteries.client.restore;

/**
 * The Class ContentItemState.
 */
public class ContentItemState {

	/** The location_x. */
	private float location_x;

	/** The location_y. */
	private float location_y;

	/** The location_z. */
	private float location_z;

	/** The name. */
	private String name;

	/** The rotation_w. */
	private float rotation_w;

	/** The rotation_x. */
	private float rotation_x;

	/** The rotation_y. */
	private float rotation_y;

	/** The rotation_z. */
	private float rotation_z;

	/** The scale_x. */
	private float scale_x = 1;

	/** The scale_y. */
	private float scale_y = 1;

	/** The scale_z. */
	private float scale_z = 1;

	/** The z order. */
	private int zOrder;

	/**
	 * Instantiates a new content item state.
	 */
	public ContentItemState() {
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
	 * Gets the rotation w.
	 *
	 * @return the rotation w
	 */
	public float getRotationW() {
		return rotation_w;
	}

	/**
	 * Gets the rotation x.
	 *
	 * @return the rotation x
	 */
	public float getRotationX() {
		return rotation_x;
	}

	/**
	 * Gets the rotation y.
	 *
	 * @return the rotation y
	 */
	public float getRotationY() {
		return rotation_y;
	}

	/**
	 * Gets the rotation z.
	 *
	 * @return the rotation z
	 */
	public float getRotationZ() {
		return rotation_z;
	}

	/**
	 * Gets the scale x.
	 *
	 * @return the scale x
	 */
	public float getScaleX() {
		return scale_x;
	}

	/**
	 * Gets the scale y.
	 *
	 * @return the scale y
	 */
	public float getScaleY() {
		return scale_y;
	}

	/**
	 * Gets the scale z.
	 *
	 * @return the scale z
	 */
	public float getScaleZ() {
		return scale_z;
	}

	/**
	 * Gets the x.
	 *
	 * @return the x
	 */
	public float getX() {
		return location_x;
	}

	/**
	 * Gets the y.
	 *
	 * @return the y
	 */
	public float getY() {
		return location_y;
	}

	/**
	 * Gets the z.
	 *
	 * @return the z
	 */
	public float getZ() {
		return location_z;
	}

	/**
	 * Gets the z order.
	 *
	 * @return the z order
	 */
	public int getZOrder() {
		return zOrder;
	}

	/**
	 * Sets the location.
	 *
	 * @param location_x
	 *            the location_x
	 * @param location_y
	 *            the location_y
	 * @param location_z
	 *            the location_z
	 */
	public void setLocation(float location_x, float location_y, float location_z) {
		this.location_x = location_x;
		this.location_y = location_y;
		this.location_z = location_z;
	}

	/**
	 * Sets the name.
	 *
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the rotation.
	 *
	 * @param rotation_x
	 *            the rotation_x
	 * @param rotation_y
	 *            the rotation_y
	 * @param rotation_z
	 *            the rotation_z
	 * @param rotation_w
	 *            the rotation_w
	 */
	public void setRotation(float rotation_x, float rotation_y,
			float rotation_z, float rotation_w) {
		this.rotation_x = rotation_x;
		this.rotation_y = rotation_y;
		this.rotation_z = rotation_z;
		this.rotation_w = rotation_w;
	}

	/**
	 * Sets the rotation w.
	 *
	 * @param rotation_w
	 *            the new rotation w
	 */
	public void setRotationW(float rotation_w) {
		this.rotation_w = rotation_w;
	}

	/**
	 * Sets the rotation x.
	 *
	 * @param rotation_x
	 *            the new rotation x
	 */
	public void setRotationX(float rotation_x) {
		this.rotation_x = rotation_x;
	}
	
	/**
	 * Sets the rotation y.
	 *
	 * @param rotation_y
	 *            the new rotation y
	 */
	public void setRotationY(float rotation_y) {
		this.rotation_y = rotation_y;
	}

	/**
	 * Sets the rotation z.
	 *
	 * @param rotation_z
	 *            the new rotation z
	 */
	public void setRotationZ(float rotation_z) {
		this.rotation_z = rotation_z;
	}

	/**
	 * Sets the scale.
	 *
	 * @param scaleFactor
	 *            the new scale
	 */
	public void setScale(float scaleFactor) {
		this.scale_x = scaleFactor;
		this.scale_y = scaleFactor;
		this.scale_z = scaleFactor;
	}

	/**
	 * Sets the scale.
	 *
	 * @param scale_x
	 *            the scale_x
	 * @param scale_y
	 *            the scale_y
	 * @param scale_z
	 *            the scale_z
	 */
	public void setScale(float scale_x, float scale_y, float scale_z) {
		this.scale_x = scale_x;
		this.scale_y = scale_y;
		this.scale_z = scale_z;
	}

	/**
	 * Sets the x.
	 *
	 * @param location_x
	 *            the new x
	 */
	public void setX(float location_x) {
		this.location_x = location_x;
	}

	/**
	 * Sets the y.
	 *
	 * @param location_x
	 *            the new y
	 */
	public void setY(float location_x) {
		this.location_x = location_x;
	}

	/**
	 * Sets the z.
	 *
	 * @param location_x
	 *            the new z
	 */
	public void setZ(float location_x) {
		this.location_x = location_x;
	}

	/**
	 * Sets the z order.
	 *
	 * @param zOrder
	 *            the new z order
	 */
	public void setZOrder(int zOrder) {
		this.zOrder = zOrder;
	}
}
