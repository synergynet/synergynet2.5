package apps.mt3dmaps.orbittest;

/**
 * Copyright (c) 2009, Andrew Carter All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. Neither the name of Andrew Carter nor the names of
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

import com.jme.bounding.BoundingSphere;
import com.jme.bounding.BoundingVolume;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.intersection.TrianglePickResults;
import com.jme.math.Ray;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.scene.Controller;
import com.jme.scene.Geometry;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.system.DisplaySystem;

/**
 * Uses the mouse to select a scene object, and smoothly move the camera towards 
 * the selected object focusing at its center. This is used with the OrbitAction.
 * 
 * @author Andrew Carter
 */
public class CameraFocusInputAction extends InputAction {

	// Max time(ms) for double click
	private long doubleClick = 500;

	private long clickTime = doubleClick;

	private Camera camera = null;

	private Vector2f screen = null;
	
	private Vector3f temp = null;
	
	private Node node = null;

	private CameraController controller = null;

	private boolean enabled = true;
	
	/**
	 * Constructor.
	 */
	public CameraFocusInputAction(Camera camera, OrbitAction orbit) {

		this.camera = camera;
		screen = new Vector2f();
		temp = new Vector3f();
		controller = new CameraController(camera, orbit);
	}

	/**
	 * Returns the camera controller that is used to focus the camera on a specific object in the scene.
	 * 
	 * @return CameraController
	 */
	public CameraController getController() {

		return controller;
	}

	/**
	 * @see com.jme.input.action.InputActionInterface#performAction(com.jme.input.action.InputActionEvent)
	 */
	public void performAction(InputActionEvent evt) {

		if(!enabled)
			return;
		
		if(evt.getTriggerName().compareToIgnoreCase("X AXIS") == 0) {
        	screen.x = evt.getTriggerPosition() * DisplaySystem.getDisplaySystem().getWidth();
		}
		else if(evt.getTriggerName().compareToIgnoreCase("Y AXIS") == 0) {
        	screen.y = evt.getTriggerPosition() * DisplaySystem.getDisplaySystem().getHeight();
		}
		else if((evt.getTriggerName().compareToIgnoreCase("MOUSE1") == 0) || (evt.getTriggerName().compareToIgnoreCase("BUTTON1") == 0)) {

			if(evt.getTriggerPressed()) {

				// check if its a valid double click
				if((System.currentTimeMillis() - clickTime < doubleClick)) {

					Geometry geometry = pickGeometry(screen);
					moveCameraToSpatial(geometry);

					clickTime = doubleClick;
				}
				else
					clickTime = System.currentTimeMillis();
			}
		}
	}

	/**
	 * Performs a mouse pick in the scene with supplied screen coordinates of
	 * the mouse. The screen coordinates are converted into a ray in the scene
	 * and used for triangle accuracy picking. The geometry closest to the
	 * camera is returned.
	 * 
	 * @param screenPos
	 *            Screen coordinate of the mouse cursor
	 * @return The geometry nearest the camera that was picked, or null
	 */
	public Geometry pickGeometry(Vector2f screenPos) {

		temp = camera.getWorldCoordinates(screenPos, 1.0f);
		temp.subtractLocal(camera.getLocation());
		
		final Ray mouseRay = new Ray(camera.getLocation(), temp);
		mouseRay.getDirection().normalizeLocal();

		TrianglePickResults results = new TrianglePickResults();
		results.setCheckDistance(true);

		if(node != null)
			node.findPick(mouseRay, results);

		Geometry geometry = null;

		if(results.getNumber() > 0)
			geometry = results.getPickData(0).getTargetMesh();

		results.clear();

		return geometry;
	}

	/**
	 * Calculates the distance from the object the camera will move to, and 
	 * initializes and starts the camera controller to move towards the given 
	 * spatial.
	 * 
	 * @param spatial
	 */
	public void moveCameraToSpatial(Spatial spatial) {

		// Magic number
		float distance = 3.0f;
		
		Vector3f target = null;

		if(spatial != null) {

			BoundingVolume worldBound = spatial.getWorldBound();

			if(worldBound != null) {
				
				target = worldBound.getCenter();
				BoundingSphere boundingSphere = new BoundingSphere(0.0f, target);
				boundingSphere.mergeLocal(worldBound);
				
				// This puts the camera at just the right distance to fill the screen
				distance *= boundingSphere.getRadius();
			}
		}

		if(controller.initialize(target, distance))
			controller.setActive(true);
	}
	
	/**
	 * Returns true if this input action is enabled.
	 * @return
	 */
	public boolean isEnabled() {
		
		return enabled;
	}

	/**
	 * Enable or disable this input action.
	 * @param enabled
	 */
	public void setEnabled(boolean enabled) {
		
		this.enabled = enabled;
	}
	
	/**
	 * Set the node that is used when picking in the scene.
	 * 
	 * @param node
	 */
	public void setNode(Node node) {
		
		this.node = node;
	}

	/**
	 * Controller used in conjuction with the Orbit handler to smoothly move the 
	 * camera towards a scene object.
	 * 
	 * @author Andrew Carter
	 */
	public class CameraController extends Controller {

		private static final long serialVersionUID = 1L;

		private Camera camera;

		private OrbitAction orbit;

		private float distance = 0.0f;

		private Vector3f cameraStart = new Vector3f();

		private Vector3f cameraEnd = new Vector3f();

		private Vector3f targetStart = new Vector3f();

		private Vector3f targetEnd = new Vector3f();

		private Vector3f temp1 = new Vector3f();

		private Vector3f temp2 = new Vector3f();

		private float elapsedTime = 0.0f;

		private boolean initialized;

		/**
		 * Constructor.
		 * 
		 * @param camera
		 * @param orbit
		 */
		public CameraController(Camera camera, OrbitAction orbit) {

			this.camera = camera;
			this.orbit = orbit;

			float time = 1.5f;

			setMaxTime(time);
			initialized = false;
		}

		/**
		 * Initialize the controller.
		 * 
		 * @param target The new focus point
		 * @param distance Move until this far from the target
		 * @return True if the controller is initialized
		 */
		public boolean initialize(Vector3f target, float distance) {

			if(target != null) {

				this.distance = distance;
				elapsedTime = 0.0f;

				calculateCoordinates(target);

				initialized = true;
			}

			return initialized;
		}

		/**
		 * Calculates path locations used for moving the camera.
		 */
		private void calculateCoordinates(Vector3f target) {

			cameraStart.set(camera.getLocation().clone());
			temp1.set(target.subtract(cameraStart));

			float delta = 1 - (distance / temp1.length());
			temp1.multLocal(delta);

			cameraEnd.set(cameraStart.add(temp1));

			targetStart.set(orbit.getTargetPosition());
			targetEnd.set(target);
		}

		/**
		 * @see com.jme.scene.Controller#update(float)
		 */
		@Override
		public void update(float tpf) {

			if(initialized && isActive()) {

				elapsedTime += tpf;
				float delta = elapsedTime / getMaxTime();

				if(delta > 1.0f)
					delta = 1.0f;

				temp1.interpolate(cameraStart, cameraEnd, delta);
				camera.setLocation(temp1);

				camera.update();

				temp2.interpolate(targetStart, targetEnd, delta);
				orbit.setTargetPosition(temp2, true);

				if(delta == 1) {

					setActive(false);
					initialized = false;
				}
			}
		}
	}

}