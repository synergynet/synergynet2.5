/*
 * Copyright (c) 2008 University of Durham, England All rights reserved.
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

package synergynetframework.mtinput.events;

import java.awt.geom.Point2D;

/**
 * Base class for multi-touch cursor events whose coordinate system is based on
 * the input service that created it, and its mode. Supports position, rotation,
 * velocity and pressure, although this values will only be present for tables
 * that provide that information.
 *
 * @author dcs0ah1
 */
public abstract class MultiTouchEvent {

	/** The angle. */
	protected double angle;

	/** The cursor id. */
	protected long cursorID;

	/** The position. */
	protected Point2D.Float position;

	/** The pressure. */
	protected float pressure;

	/** The time of creation nanos. */
	protected long timeOfCreationNanos;

	/** The velocity. */
	protected Point2D.Float velocity;
	
	/**
	 * Instantiates a new multi touch event.
	 *
	 * @param id
	 *            the id
	 * @param position
	 *            the position
	 */
	public MultiTouchEvent(long id, Point2D.Float position) {
		this(id, position, new Point2D.Float());
	}

	/**
	 * Instantiates a new multi touch event.
	 *
	 * @param id
	 *            the id
	 * @param position
	 *            the position
	 * @param velocity
	 *            the velocity
	 */
	public MultiTouchEvent(long id, Point2D.Float position,
			Point2D.Float velocity) {
		this(id, position, velocity, 1f, 0d);
	}

	/**
	 * Instantiates a new multi touch event.
	 *
	 * @param id
	 *            the id
	 * @param position
	 *            the position
	 * @param velocity
	 *            the velocity
	 * @param pressure
	 *            the pressure
	 * @param angle
	 *            the angle
	 */
	public MultiTouchEvent(long id, Point2D.Float position,
			Point2D.Float velocity, float pressure, double angle) {
		this.cursorID = id;
		this.position = position;
		this.velocity = velocity;
		this.pressure = pressure;
		this.angle = angle;
	}
	
	/**
	 * Gets the angle.
	 *
	 * @return the angle
	 */
	public double getAngle() {
		return angle;
	}

	/**
	 * Gets the cursor id.
	 *
	 * @return the cursor id
	 */
	public long getCursorID() {
		return cursorID;
	}
	
	/**
	 * Position in the coordinate system of the table.
	 *
	 * @return the position
	 */
	public Point2D.Float getPosition() {
		return position;
	}

	/**
	 * Gets the pressure.
	 *
	 * @return the pressure
	 */
	public float getPressure() {
		return pressure;
	}
	
	/**
	 * Gets the time of creation nanos.
	 *
	 * @return the time of creation nanos
	 */
	public long getTimeOfCreationNanos() {
		return timeOfCreationNanos;
	}

	/**
	 * Gets the velocity.
	 *
	 * @return the velocity
	 */
	public Point2D.Float getVelocity() {
		return velocity;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getClass().getName() + " " + cursorID + " @" + position
				+ " vel: " + velocity;
	}
}
