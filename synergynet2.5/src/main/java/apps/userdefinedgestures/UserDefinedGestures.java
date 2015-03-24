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

package apps.userdefinedgestures;

import apps.userdefinedgestures.scene.Background;
import apps.userdefinedgestures.taskengine.TaskEngine;
import apps.userdefinedgestures.taskreader.RandomTaskReader;

import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.pass.DirectionalShadowMapPass;
import com.jme.renderer.pass.RenderPass;
import com.jme.scene.state.LightState;
import com.jme.system.DisplaySystem;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.table.SynergyNetAppUtils;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.appsystem.table.appregistry.menucontrol.HoldTopRightConfirmVisualExit;
import synergynetframework.jme.config.AppConfig;
import synergynetframework.jme.sysutils.CameraUtility;


/**
 * The Class UserDefinedGestures.
 */
public class UserDefinedGestures extends DefaultSynergyNetApp {

	/** The content system. */
	protected ContentSystem contentSystem;
	
	/** The s pass. */
	private static DirectionalShadowMapPass sPass;
	
	/** The task engine. */
	private TaskEngine taskEngine;

  
	/**
	 * Instantiates a new user defined gestures.
	 *
	 * @param info the info
	 */
	public UserDefinedGestures(ApplicationInfo info) {
		super(info);
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#addContent()
	 */
	@Override
	public void addContent() {
		
		SynergyNetAppUtils.addTableOverlay(this);		
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(this);	
		setMenuController(new HoldTopRightConfirmVisualExit(this));
		getCamera();
		setupLighting();
				
		RenderPass rPass = new RenderPass();
	    rPass.add(worldNode);
	    pManager.add(rPass);

	    sPass = new DirectionalShadowMapPass(new Vector3f(0, -2, -1));
	    sPass.setViewDistance(800);
	    sPass.add(worldNode);     
	    pManager.add(sPass);
		
		buildSence();
		
		KeyBindingManager.getKeyBindingManager().set( "toggle_setting", KeyInput.KEY_S );
		KeyBindingManager.getKeyBindingManager().set( "Previous_setting", KeyInput.KEY_K );
		KeyBindingManager.getKeyBindingManager().set( "nextTask_setting", KeyInput.KEY_J );

		RandomTaskReader randomTaskReader = new RandomTaskReader();
		taskEngine = new TaskEngine(worldNode, orthoNode, sPass, cam, randomTaskReader.getTaskList());

	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp#stateUpdate(float)
	 */
	public void stateUpdate(float tpf) {
		super.stateUpdate(tpf);
		
		if ( KeyBindingManager.getKeyBindingManager().isValidCommand("toggle_setting", false ) ) {
			if (taskEngine.getCurrentTransformer()==null) return;
			if (taskEngine.getCurrentTransformer().isActive()){
				taskEngine.getCurrentTransformer().setActive(false);
				taskEngine.resetPosition();
			}
			else{
				taskEngine.getCurrentTransformer().setActive(true);
				taskEngine.resetPosition();
			}
		}
		
		if ( KeyBindingManager.getKeyBindingManager().isValidCommand("Previous_setting", false ) ) {			
			taskEngine.resetPosition();
			taskEngine.moveToNextTask();
			
		}
		
		if ( KeyBindingManager.getKeyBindingManager().isValidCommand("nextTask_setting", false ) ) {	
			taskEngine.resetPosition();
			taskEngine.moveToPreviousTask();
			
		}
	}

	/**
	 * Setup lighting.
	 */
	protected void setupLighting() {
		LightState lightState = DisplaySystem.getDisplaySystem().getRenderer().createLightState();
		worldNode.setRenderState(lightState);
		lightState.setEnabled(AppConfig.useLighting);	
		
		PointLight pointlight = new PointLight();
		pointlight.setLocation(new Vector3f(-20f, 15f, 0f));
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

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp#getCamera()
	 */
	protected Camera getCamera() {
		if(cam == null) {
			cam = CameraUtility.getCamera();
			//cam.setLocation(new Vector3f(0f, 10f, 50f));
			cam.setLocation(new Vector3f(0f, 80f, 40f));
			cam.lookAt(new Vector3f(0, 10, 0), new Vector3f( 0, 1, 0 ));
			cam.update();
		}		
		return cam;

	}
	
	/**
	 * Builds the sence.
	 */
	private void buildSence() {
        
        Background background = new Background("background", 100, 80, 100, null, new Vector3f(50, 30f, 10 ), null, new Vector3f(12, 1.5f, 1 ));
        background.setLocalTranslation(new Vector3f(0, 0, 0));
        worldNode.attachChild(background);
        
        
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#cleanup()
	 */
	public void cleanup() {
		super.cleanup();		
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#stateRender(float)
	 */
	@Override
	protected void stateRender(float tpf) {
		super.stateRender(tpf);	    
	}
	
}
