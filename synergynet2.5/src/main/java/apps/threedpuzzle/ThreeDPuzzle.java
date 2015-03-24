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

package apps.threedpuzzle;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.table.SynergyNetAppUtils;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.appsystem.table.appregistry.menucontrol.HoldTopRightExit;
import synergynetframework.jme.config.AppConfig;
import synergynetframework.jme.sysutils.CameraUtility;
import apps.threedmanipulation.ThreeDManipulation;
import apps.threedpuzzle.TetrisLoader.FileTetrisLoader;
import apps.threedpuzzle.scene.Yard;
import apps.threedpuzzle.tetrisfactory.TetrisCollection;

import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.light.PointLight;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.state.LightState;
import com.jme.system.DisplaySystem;

/**
 * The Class ThreeDPuzzle.
 */
public class ThreeDPuzzle extends DefaultSynergyNetApp {
	
	/** The content system. */
	protected ContentSystem contentSystem;
	
	/**
	 * Instantiates a new three d puzzle.
	 *
	 * @param info
	 *            the info
	 */
	public ThreeDPuzzle(ApplicationInfo info) {
		super(info);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#addContent
	 * ()
	 */
	@Override
	public void addContent() {
		
		setMenuController(new HoldTopRightExit());
		SynergyNetAppUtils.addTableOverlay(this);
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(this);
		
		getCamera();
		setupLighting();
		
		buildSence();
		buildTargetObject();
		
		KeyBindingManager.getKeyBindingManager().set("toggle_setting",
				KeyInput.KEY_S);
		KeyBindingManager.getKeyBindingManager().set("cameraPosition_setting",
				KeyInput.KEY_A);
		KeyBindingManager.getKeyBindingManager().set("mode_setting",
				KeyInput.KEY_Q);
		
	}

	/**
	 * Builds the sence.
	 */
	private void buildSence() {
		
		// build yard
		Yard yard = new Yard("yard", 92, 95, 2,
				ThreeDManipulation.class.getResource("floor1.jpg"),
				new Vector3f(15, 15f, 10),
				ThreeDManipulation.class.getResource("wall.jpg"), new Vector3f(
						12, 1.5f, 1));
		yard.setLocalTranslation(new Vector3f(0, 0, 50));
		
		Quaternion tq = new Quaternion();
		tq.fromAngleAxis(FastMath.PI / 2f, new Vector3f(1, 0, 0));
		yard.setLocalRotation(tq);
		yard.updateGeometricState(0f, false);
		
		worldNode.attachChild(yard);
		
	}
	
	/**
	 * Builds the target object.
	 */
	private void buildTargetObject() {

		@SuppressWarnings("unused")
		TetrisCollection tetris = new TetrisCollection(worldNode,
				new FileTetrisLoader());
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#cleanup
	 * ()
	 */
	public void cleanup() {
		super.cleanup();
		
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp
	 * #getCamera()
	 */
	protected Camera getCamera() {
		if (cam == null) {
			cam = CameraUtility.getCamera();
			cam.setLocation(new Vector3f(0f, 0f, 300f));
			cam.lookAt(new Vector3f(0, 0, 0), new Vector3f(0, 1, 0));
			cam.update();
		}
		return cam;
		
	}

	/**
	 * Setup lighting.
	 */
	protected void setupLighting() {
		LightState lightState = DisplaySystem.getDisplaySystem().getRenderer()
				.createLightState();
		worldNode.setRenderState(lightState);
		lightState.setEnabled(AppConfig.useLighting);
		
		PointLight pointlight = new PointLight();
		pointlight.setLocation(new Vector3f(50f, 20f, 150f));
		pointlight.setAmbient(ColorRGBA.white);
		pointlight.setAttenuate(true);
		pointlight.setEnabled(true);
		lightState.attach(pointlight);
		
		pointlight = new PointLight();
		pointlight.setLocation(new Vector3f(-50f, 20f, 100f));
		pointlight.setAmbient(ColorRGBA.white);
		pointlight.setAttenuate(true);
		pointlight.setEnabled(true);
		lightState.attach(pointlight);
		
		worldNode.updateRenderState();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#stateRender
	 * (float)
	 */
	@Override
	protected void stateRender(float tpf) {
		super.stateRender(tpf);
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp
	 * #stateUpdate(float)
	 */
	public void stateUpdate(float tpf) {
		super.stateUpdate(tpf);
		
		if (KeyBindingManager.getKeyBindingManager().isValidCommand(
				"toggle_setting", false)) {
			
		}

		if (KeyBindingManager.getKeyBindingManager().isValidCommand(
				"cameraPosition_setting", false)) {
			
		}

		if (KeyBindingManager.getKeyBindingManager().isValidCommand(
				"mode_setting", false)) {
			
		}
	}
	
}
