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

package apps.teapots;


import com.jme.bounding.BoundingBox;
import com.jme.bounding.BoundingSphere;
import com.jme.image.Texture;
import com.jme.image.Texture2D;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.TextureRenderer;
import com.jme.scene.CameraNode;
import com.jme.scene.Node;
import com.jme.scene.Spatial.LightCombineMode;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Quad;
import com.jme.scene.shape.Teapot;
import com.jme.scene.state.LightState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;

import synergynetframework.appsystem.table.SynergyNetAppUtils;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.appsystem.table.appregistry.menucontrol.HoldTopRightExit;
import synergynetframework.jme.config.AppConfig;
import synergynetframework.jme.cursorsystem.elements.threed.ControlPointRotateTranslateScale;
import synergynetframework.jme.cursorsystem.elements.threed.MultiTouchMoveZController;
import synergynetframework.jme.cursorsystem.elements.threed.MultiTouchRotateXYController;
import synergynetframework.jme.cursorsystem.flicksystem.FlickSystem;
import synergynetframework.jme.sysutils.CameraUtility;


/**
 * The Class TeapotsApp.
 */
public class TeapotsApp extends DefaultSynergyNetApp {

	  /** The monitor node. */
  	private Node monitorNode;
	  
  	/** The cam node. */
  	private CameraNode camNode;

	  /** The t renderer. */
  	private TextureRenderer tRenderer;
	  
  	/** The fake tex. */
  	private Texture2D fakeTex;
	  
  	/** The last rend. */
  	private float lastRend = 1;
	  
  	/** The throttle. */
  	private float throttle = 1/30f;
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#cleanup()
	 */
	public void cleanup() {
		super.cleanup();
		tRenderer.cleanup();
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#stateRender(float)
	 */
	@Override
	protected void stateRender(float tpf) {
		super.stateRender(tpf);
		    lastRend += tpf;
		    if (lastRend > throttle ) {
		      tRenderer.render(worldNode, fakeTex);
		      lastRend = 0;
		    }
		  }
	
	/**
	 * Instantiates a new teapots app.
	 *
	 * @param info the info
	 */
	public TeapotsApp(ApplicationInfo info) {
		super(info);
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#addContent()
	 */
	@Override
	public void addContent() {
		setMenuController(new HoldTopRightExit());
		SynergyNetAppUtils.addTableOverlay(this);
		
		final Teapot tp1 = new Teapot("tp1");
		tp1.setLocalTranslation(new Vector3f(10f, 0f, 10f));
		tp1.setModelBound(new BoundingSphere());
		tp1.updateModelBound();
		ControlPointRotateTranslateScale cprts1 = new ControlPointRotateTranslateScale(tp1);
		cprts1.setPickMeOnly(true);
		worldNode.attachChild(tp1);
		
		final Teapot tp2 = new Teapot("tp2");
		tp2.setLocalTranslation(new Vector3f(-10f, 0f, 10f));
		tp2.setModelBound(new BoundingSphere());
		tp2.updateModelBound();
		ControlPointRotateTranslateScale cprts2 = new ControlPointRotateTranslateScale(tp2);
		cprts2.setPickMeOnly(true);
		worldNode.attachChild(tp2);	
		
		Vector3f min = new Vector3f(-2, -2, -2);
		Vector3f max = new Vector3f(2, 2, 2);
		final Box box = new Box("box1", min, max);
		box.setLocalTranslation(new Vector3f(-15f, -5f, 10f));
		box.setModelBound(new BoundingBox());
		box.updateModelBound();
		//MultiTouchMoveZController mtrz = new MultiTouchMoveZController(box, tp2);
		MultiTouchRotateXYController mtrz = new MultiTouchRotateXYController(box, tp2);
		mtrz.setPickMeOnly(true);
		worldNode.attachChild(box);	
		
		Vector3f min1 = new Vector3f(-2, -2, -2);
		Vector3f max1 = new Vector3f(2, 2, 2);
		final Box box1 = new Box("box2", min1, max1);
		box1.setLocalTranslation(new Vector3f(-5f, -5f, 10f));
		box1.setModelBound(new BoundingBox());
		box1.updateModelBound();
		MultiTouchMoveZController mtrz1 = new MultiTouchMoveZController(box1, tp2);
		//MultiTouchRotateXYController mtrz = new MultiTouchRotateXYController(box, tp2);
		mtrz1.setPickMeOnly(true);
		worldNode.attachChild(box1);	
		
		//FlickSystem flicking = FlickSystem.getInstance();		
		//flicking.makeFlickable(tp1, cprts1, 2f);
		//flicking.makeFlickable(tp2, cprts2, 1f);
		
		getCamera().setLocation(new Vector3f(0f, 10f, 50f));
		getCamera().lookAt(new Vector3f(), new Vector3f( 0, 0, -1 ));
		getCamera().update();
		
		setupLighting();
		
		
		tRenderer = DisplaySystem.getDisplaySystem().createTextureRenderer(256, 256, TextureRenderer.Target.Texture2D);
		camNode = new CameraNode("Camera Node", tRenderer.getCamera());
		tRenderer.getCamera().lookAt(new Vector3f(), new Vector3f(0, 0,-1));
		tRenderer.getCamera().update();
		camNode.lookAt(new Vector3f(0, 0, -10), new Vector3f(0, 1 ,0));
		camNode.setLocalTranslation(new Vector3f(0, 0, 20));
		camNode.updateGeometricState(0, true);
		camNode.updateRenderState();
		 
		 
		//camNode.attachChild(box1);
		//box1.setLocalTranslation(new Vector3f(0f, 0f, 10f));
		 
		 
		worldNode.attachChild(camNode);
		 
		monitorNode = new Node("Monitor Node");
		Quad quad = new Quad("Monitor");
		quad.updateGeometry(3, 3);
		quad.setLocalTranslation(new Vector3f(15f, 10f, 15));
		monitorNode.attachChild(quad);

		Quad quad2 = new Quad("Monitor");
		quad2.updateGeometry(3.4f, 3.4f);
		quad2.setLocalTranslation(new Vector3f(3.95f, 52.6f, 89.5f));
		//monitorNode.attachChild(quad2);

		    // Setup our params for the depth buffer
		ZBufferState buf = DisplaySystem.getDisplaySystem().getRenderer().createZBufferState();
		buf.setEnabled(true);
		buf.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);

		monitorNode.setRenderState(buf);

		    // Ok, now lets create the Texture object that our scene will be rendered to.
		tRenderer.setBackgroundColor(new ColorRGBA(1f, 0f, 0f, 1f));
		fakeTex = new Texture2D();
		fakeTex.setRenderToTextureType(Texture.RenderToTextureType.RGBA);
		tRenderer.setupTexture(fakeTex);
		TextureState screen = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		screen.setTexture(fakeTex);
		screen.setEnabled(true);
		quad.setRenderState(screen);

		monitorNode.updateGeometricState(0.0f, true);
		monitorNode.updateRenderState();
		monitorNode.setLightCombineMode(LightCombineMode.Off);
		worldNode.attachChild(monitorNode);
		
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp#stateUpdate(float)
	 */
	public void stateUpdate(float tpf) {
		super.stateUpdate(tpf);
		FlickSystem.getInstance().update(tpf);
	}

	/**
	 * Setup lighting.
	 */
	protected void setupLighting() {
		LightState lightState = DisplaySystem.getDisplaySystem().getRenderer().createLightState();
		worldNode.setRenderState(lightState);
		lightState.setEnabled(AppConfig.useLighting);	
		
		PointLight pointlight = new PointLight();
		pointlight.setLocation(new Vector3f(50f, 20f, 10f));
		pointlight.setAmbient(ColorRGBA.red);
		pointlight.setAttenuate(true);
		pointlight.setEnabled(true);
		lightState.attach(pointlight);

		pointlight = new PointLight();
		pointlight.setLocation(new Vector3f(-100f, 20f, -10f));
		pointlight.setAmbient(ColorRGBA.blue);
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
			cam.setLocation(new Vector3f(0f, 10f, 100f));
			cam.lookAt(new Vector3f(0, 0, 50), new Vector3f( 0, 0, -1 ));
			cam.update();
		}		
		return cam;
	}
}
