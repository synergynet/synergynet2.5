package apps.mt3dmaps.orbittest;

/**
 * Copyright (c) 2009, Andrew Carter All rights reserved. Redistribution and use
 * in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met: Redistributions of source
 * code must retain the above copyright notice, this list of conditions and the
 * following disclaimer. Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution. Neither
 * the name of Andrew Carter nor the names of contributors may be used to
 * endorse or promote products derived from this software without specific prior
 * written permission. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

/**
 * Orbits the camera around a specified target position and also handles
 * translation of the target position.
 *
 * @author Andrew Carter
 */
public class OrbitAction extends InputAction {
	
	/** The camera. */
	private Camera camera = null;

	// Temp variables
	/** The dif temp. */
	private final Vector3f difTemp = new Vector3f();
	
	/** The enabled. */
	private boolean enabled = true;

	/** The key modifier pressed. */
	private boolean keyModifierPressed = false;
	
	/** The max zenith. */
	private float maxZenith = 89.5f * FastMath.DEG_TO_RAD;

	/** The mouse button pressed. */
	private boolean mouseButtonPressed = false;

	/** The mouse roll multiplier. */
	private float mouseRollMultiplier = 0.1f;

	/** The mouse x multiplier. */
	private float mouseXMultiplier = 2.0f;

	/** The mouse x speed. */
	private float mouseXSpeed = 1.0f;

	/** The mouse y multiplier. */
	private float mouseYMultiplier = 2.0f;

	/** The mouse y speed. */
	private float mouseYSpeed = 1.0f;

	/** The move down multiplier. */
	private float moveDownMultiplier = 0.5f;

	/** The move left multiplier. */
	private float moveLeftMultiplier = 0.5f;

	/** The radial speed. */
	private float radialSpeed = 1.0f;

	/** The right temp. */
	private final Vector3f rightTemp = new Vector3f();
	
	/** The sphere temp. */
	private final Vector3f sphereTemp = new Vector3f();

	/** The target position. */
	private Vector3f targetPosition = null;

	/** The translate horizontal speed. */
	private float translateHorizontalSpeed = 1.0f;
	
	/** The translate vertical speed. */
	private float translateVerticalSpeed = 1.0f;

	/** The updated. */
	private boolean updated = false;
	
	/**
	 * Constructor.
	 *
	 * @param camera
	 *            That camera that will be controlled by this input handler.
	 */
	public OrbitAction(Camera camera) {
		
		this.camera = camera;
		targetPosition = new Vector3f();
		
		setSpeed(1);
	}
	
	/**
	 * <code>clampZenith</code> limits the rotation of the polar angle.
	 *
	 * @param zenith
	 *            float
	 * @return float
	 */
	private float clampZenith(float zenith) {
		
		if (Float.isInfinite(zenith) || Float.isNaN(zenith)) {
			return zenith;
		}
		
		if (zenith > maxZenith) {
			zenith = maxZenith;
		} else if (zenith < -maxZenith) {
			zenith = -maxZenith;
		}
		
		return zenith;
	}
	
	/**
	 * Returns a scaler based on the camera distance from the target. Used to
	 * increase sensitivity as the distance form the camera to the target
	 * increases.
	 *
	 * @return float
	 */
	private float getDistanceModifier() {
		
		return camera.getLocation().distance(targetPosition);
	}
	
	/**
	 * Returns the enabled state of this input action.
	 *
	 * @return True if enabled
	 */
	public boolean getEnabled() {
		return enabled;
	}
	
	/**
	 * Returns a copy of the target position in world coordinates.
	 *
	 * @return Cloned target position
	 */
	public Vector3f getTargetPosition() {
		
		return targetPosition.clone();
	}
	
	/**
	 * Handles camera updates based on input events.
	 *
	 * @param evt
	 *            the evt
	 */
	public void performAction(InputActionEvent evt) {
		
		if (!enabled) {
			return;
		}
		
		updated = false;

		if ((evt.getTriggerName().compareToIgnoreCase("MOUSE1") == 0)
				|| (evt.getTriggerName().compareToIgnoreCase("BUTTON1") == 0)) {
			mouseButtonPressed = evt.getTriggerPressed();
		}
		// If the SHIFT key is pressed
		else if ((evt.getTriggerName().compareToIgnoreCase("key") == 0)
				&& ((evt.getTriggerIndex() == 42) || (evt.getTriggerIndex() == 54))) {
			keyModifierPressed = evt.getTriggerPressed();
		} else if (evt.getTriggerName().compareToIgnoreCase("X Axis") == 0) {
			
			if (mouseButtonPressed) {
				
				if (keyModifierPressed) {
					translateHorizontal(evt.getTriggerDelta());
				} else {
					rotateAzimuth(evt.getTriggerDelta());
				}
				
				updated = true;
			}
		} else if (evt.getTriggerName().compareToIgnoreCase("Y Axis") == 0) {
			
			if (mouseButtonPressed) {
				
				if (keyModifierPressed) {
					translateVertically(evt.getTriggerDelta());
				} else {
					rotateZenith(evt.getTriggerDelta());
				}
				
				updated = true;
			}
		} else if (evt.getTriggerName().compareToIgnoreCase("Wheel") == 0) {
			
			translateRadial(evt.getTriggerDelta());
			updated = true;
		}
		
		// If we have moved the camera in any way, update it
		if (updated) {
			camera.update();
		}
	}
	
	/**
	 * <code>rotateAzimuth</code> updates the azimuth value of the camera's
	 * spherical coordinates.
	 *
	 * @param amount
	 *            the amount
	 */
	private void rotateAzimuth(float amount) {
		
		Vector3f cameraPosition = camera.getLocation();
		
		float azimuthAccel = (amount * mouseXSpeed);
		difTemp.set(cameraPosition).subtractLocal(targetPosition);
		
		FastMath.cartesianToSpherical(difTemp, sphereTemp);
		sphereTemp.y = FastMath.normalize(sphereTemp.y + (azimuthAccel),
				-FastMath.TWO_PI, FastMath.TWO_PI);
		FastMath.sphericalToCartesian(sphereTemp, rightTemp);
		
		rightTemp.addLocal(targetPosition);
		cameraPosition.set(rightTemp);
		
		camera.lookAt(targetPosition, Vector3f.UNIT_Y);
	}
	
	/**
	 * <code>rotateZenith</code> updates the zenith/polar value of the camera's
	 * spherical coordinates.
	 *
	 * @param amount
	 *            the amount
	 */
	private void rotateZenith(float amount) {
		
		Vector3f cameraPosition = camera.getLocation();
		
		float thetaAccel = (-amount * mouseYSpeed);
		difTemp.set(cameraPosition).subtractLocal(targetPosition);
		
		FastMath.cartesianToSpherical(difTemp, sphereTemp);
		sphereTemp.z = FastMath.normalize(clampZenith(sphereTemp.z
				+ (thetaAccel)), -FastMath.TWO_PI, FastMath.TWO_PI);
		FastMath.sphericalToCartesian(sphereTemp, rightTemp);
		
		rightTemp.addLocal(targetPosition);
		cameraPosition.set(rightTemp);
		
		camera.lookAt(targetPosition, Vector3f.UNIT_Y);
	}
	
	/**
	 * Sets the enabled state of this input action.
	 *
	 * @param enabled
	 *            True to enable
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	/**
	 * <code>setSpeed</code> sets the sensitivity of the input.
	 *
	 * @param speed
	 *            sensitivity of the input.
	 */
	public void setSpeed(float speed) {
		super.setSpeed(speed);
		
		translateHorizontalSpeed = moveLeftMultiplier * speed;
		translateVerticalSpeed = moveDownMultiplier * speed;
		mouseXSpeed = mouseXMultiplier * speed;
		mouseYSpeed = mouseYMultiplier * speed;
		radialSpeed = mouseRollMultiplier * speed;
	}
	
	/**
	 * Sets the orbit target position in world coordinates.
	 *
	 * @param position
	 *            Values are set from this position
	 * @param updateCamera
	 *            Forces the camera to update its direction
	 */
	public void setTargetPosition(Vector3f position, boolean updateCamera) {
		
		targetPosition.set(position);
		
		if (updateCamera) {
			camera.lookAt(targetPosition, Vector3f.UNIT_Y);
		}
	}
	
	/**
	 * Reverse the direction of the mouse wheel input. The mouse wheel trigger
	 * delta is opposite when used with a canvas.
	 *
	 * @param reversed
	 *            the new wheel direction
	 */
	public void setWheelDirection(boolean reversed) {
		
		if (reversed) {
			radialSpeed = Math.abs(radialSpeed) * -1.0f;
		} else {
			radialSpeed = Math.abs(radialSpeed);
		}
	}
	
	/**
	 * <code>translateHorizontal</code> updates the camera and target position
	 * along a horizontal plane.
	 *
	 * @param amount
	 *            the amount
	 */
	private void translateHorizontal(float amount) {
		
		Vector3f cameraPosition = camera.getLocation();
		cameraPosition.addLocal(camera.getLeft().mult(
				translateHorizontalSpeed * amount * getDistanceModifier(),
				difTemp));
		targetPosition.addLocal(difTemp);
	}
	
	/**
	 * <code>translateRadius</code> updates the radius value of the camera's
	 * spherical coordinates.
	 *
	 * @param amount
	 *            the amount
	 */
	private void translateRadial(float amount) {
		
		Vector3f cameraPosition = camera.getLocation();
		cameraPosition.subtractLocal(camera.getDirection().mult(
				radialSpeed * amount * getDistanceModifier(), difTemp));
	}
	
	/**
	 * <code>translateVertically</code> updates the camera and target position
	 * along a vertical plane.
	 *
	 * @param amount
	 *            the amount
	 */
	private void translateVertically(float amount) {
		
		Vector3f cameraPosition = camera.getLocation();
		cameraPosition.subtractLocal(camera.getUp().mult(
				translateVerticalSpeed * amount * getDistanceModifier(),
				difTemp));
		targetPosition.subtractLocal(difTemp);
	}
}