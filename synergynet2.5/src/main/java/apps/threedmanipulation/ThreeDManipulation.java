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

package apps.threedmanipulation;


//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.net.URISyntaxException;
//import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import apps.threedmanipulation.gestures.MonitorCameraRotateTranslateZoom;
import apps.threedmanipulation.listener.ToolListener;
import apps.threedmanipulation.scene.DefaultSkyBox;
import apps.threedmanipulation.scene.TV;
import apps.threedmanipulation.scene.Yard;
import apps.threedmanipulation.tools.Monitor;
import apps.threedmanipulation.tools.Telescope;
import apps.threedmanipulation.tools.TouchPad;
import apps.threedmanipulation.tools.TwinObject;
import apps.threedmanipulation.utils.Context;
import apps.threedmanipulation.utils.ManipulatableObjectBuilder;

import com.jme.bounding.BoundingSphere;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.light.PointLight;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.pass.DirectionalShadowMapPass;
import com.jme.renderer.pass.RenderPass;
//import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.Spatial.CullHint;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Teapot;
import com.jme.scene.state.LightState;
import com.jme.system.DisplaySystem;
//import com.jme.util.export.binary.BinaryImporter;
//import com.jme.util.resource.ResourceLocatorTool;
//import com.jme.util.resource.SimpleResourceLocator;
import com.jmex.effects.particles.ParticleSystem;
//import com.jmex.model.converters.MilkToJme;
import common.CommonResources;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.LightImageLabel;
import synergynetframework.appsystem.contentsystem.items.listener.ItemEventAdapter;
import synergynetframework.appsystem.table.SynergyNetAppUtils;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.appsystem.table.appregistry.menucontrol.HoldTopRightExit;
import synergynetframework.jme.config.AppConfig;
import synergynetframework.jme.sysutils.CameraUtility;


/**
 * The Class ThreeDManipulation.
 */
public class ThreeDManipulation extends DefaultSynergyNetApp {

	/** The content system. */
	protected ContentSystem contentSystem;
	  
	/** The telescopes. */
	protected List<Telescope> telescopes = new ArrayList<Telescope>();
	
	/** The monitors. */
	protected List<Monitor> monitors = new ArrayList<Monitor>();	
	
	/** The touch pads. */
	protected Map<String, TouchPad> touchPads = new HashMap<String, TouchPad>();
	
	/** The twin objects. */
	protected Map<String, TwinObject> twinObjects = new HashMap<String, TwinObject>();
	
	/** The manipulatable ojbects. */
	protected List<Spatial> manipulatableOjbects = new ArrayList<Spatial>();	
	
	/** The indirect manipulation mode. */
	private String indirectManipulationMode = "";

	/** The main explosion. */
	protected ParticleSystem mainExplosion;

	/** The context. */
	protected Context context;
	
	/** The tv. */
	protected TV tv;
	
	/** The tv1. */
	protected TV tv1;
	
	/** The tp1. */
	protected Teapot tp1;
	
	/** The tp1 clone. */
	protected Teapot tp1Clone;
	
	/** The pre defined angles. */
	protected List<Float> preDefinedAngles = new ArrayList<Float>();
	
	/** The pre defined angle index. */
	protected int preDefinedAngleIndex=0;
	
	/** The pre defined camera position index. */
	protected int preDefinedCameraPositionIndex=1;
	
	/** The target object. */
	protected Box targetObject;
	
	/** The s pass. */
	private static DirectionalShadowMapPass sPass;
	
	/** The last rend. */
	private float lastRend = 1;
	
	/** The throttle. */
	private float throttle = 1/30f;
	
	/** The camera manipulation mode. */
	private String cameraManipulationMode = MonitorCameraRotateTranslateZoom.MODE_REMOTECONTROL;
	  
	/**
	 * Instantiates a new three d manipulation.
	 *
	 * @param info the info
	 */
	public ThreeDManipulation(ApplicationInfo info) {
		super(info);
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#addContent()
	 */
	@Override
	public void addContent() {
	
		setMenuController(new HoldTopRightExit());
		
		SynergyNetAppUtils.addTableOverlay(this);
		
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(this);	
		
		getCamera();
		setupLighting();
				
		RenderPass rPass = new RenderPass();
	    rPass.add(worldNode);
	    pManager.add(rPass);

	    sPass = new DirectionalShadowMapPass(new Vector3f(-1, -2, -1));
	    sPass.setViewDistance(800);
	    sPass.add(worldNode);
	     
	    pManager.add(sPass);
		
		buildSence();
		this.buildTargetObject();
		
		worldNode.updateRenderState();
		worldNode.updateGeometricState(0f, true);	
		
		KeyBindingManager.getKeyBindingManager().set( "toggle_setting", KeyInput.KEY_S );
		KeyBindingManager.getKeyBindingManager().set( "cameraPosition_setting", KeyInput.KEY_A );
		KeyBindingManager.getKeyBindingManager().set( "mode_setting", KeyInput.KEY_Q );
		
		this.preDefinedAngles.add((float) (2.8*FastMath.PI/2f));
		this.preDefinedAngles.add((float) (2.1*FastMath.PI/2f));
		this.preDefinedAngles.add((float) (1.4*FastMath.PI/2f));
		
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp#stateUpdate(float)
	 */
	public void stateUpdate(float tpf) {
		super.stateUpdate(tpf);
		for (Monitor monitor:monitors)
			monitor.update(tpf);
		
		if ( KeyBindingManager.getKeyBindingManager().isValidCommand("toggle_setting", false ) ) {
			
			Quaternion tq = new Quaternion();
			tq.fromAngleAxis((float) (this.preDefinedAngles.get(this.preDefinedAngleIndex)), new Vector3f(1, 1, 0));
			tp1.setLocalRotation(tq);	
			tp1Clone.setLocalRotation(tq);
			
			if (this.preDefinedAngleIndex>=this.preDefinedAngles.size()-1) 
				this.preDefinedAngleIndex=0;
			else
				this.preDefinedAngleIndex++;
		}
		
		if ( KeyBindingManager.getKeyBindingManager().isValidCommand("cameraPosition_setting", false ) ) {			
			Vector3f cameraTargetPosition;
			switch (this.preDefinedCameraPositionIndex){
			case 0: cameraTargetPosition = new Vector3f(10000, 10000, 10000); break;
			case 1: cameraTargetPosition = new Vector3f(20, 7, 95); break;
			case 2: cameraTargetPosition = new Vector3f(-18, 10, 93); break;
			default:  cameraTargetPosition = new Vector3f(-18, 7, 60); break;
			
			}
			
			this.targetObject.setLocalTranslation(cameraTargetPosition);
			this.targetObject.updateGeometricState(0f, false);
			
			if (this.preDefinedCameraPositionIndex>=3)
				this.preDefinedCameraPositionIndex=0;
			else
				this.preDefinedCameraPositionIndex++;
			
		}
		
		if ( KeyBindingManager.getKeyBindingManager().isValidCommand("mode_setting", false ) ) {	
			if (cameraManipulationMode.equals(MonitorCameraRotateTranslateZoom.MODE_REMOTECONTROL)){
				cameraManipulationMode = MonitorCameraRotateTranslateZoom.MODE_CAMERAMANIPULABLE;
			}
			else{
				cameraManipulationMode=MonitorCameraRotateTranslateZoom.MODE_REMOTECONTROL;
			}
			
			for (Monitor monitor:monitors){
				monitor.setMode(cameraManipulationMode);
				
			}
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

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp#getCamera()
	 */
	protected Camera getCamera() {
		if(cam == null) {
			cam = CameraUtility.getCamera();
			cam.setLocation(new Vector3f(0f, 10f, 140f));
			cam.lookAt(new Vector3f(0, -10, 50), new Vector3f( 0, 1, 0 ));
			cam.update();
		}		
		return cam;

	}
	
	/**
	 * Builds the sence.
	 */
	private void buildSence() {
        
		//build skybox
		DefaultSkyBox skybox = new DefaultSkyBox("skybox",200);   
        worldNode.attachChild(skybox);
        
        //build yard
        Yard yard = new Yard("yard", 140, 80, 10, ThreeDManipulation.class.getResource(
    	"floor1.jpg"), new Vector3f(50, 30f, 10 ), ThreeDManipulation.class.getResource(
    	"wall.jpg"), new Vector3f(12, 1.5f, 1 ));
        yard.setLocalTranslation(new Vector3f(0, 0, 100));
        worldNode.attachChild(yard);
        
        
		tv = new TV(contentSystem, CommonResources.class.getResource("smallvid.mp4"), 2, ThreeDManipulation.class.getResource(
    	"floor1.jpg"));
		tv.setLocalTranslation(-39, 5.5f, 45);
		//worldNode.attachChild(tv);
		
		/*
		tv = new TV(contentSystem, Resources.getResource(
    	"a.mp4"), 2, Resources.getResource(
    	"floor1.jpg"));
		tv.setLocalTranslation(38.5f, 5.5f, 60);
		tv.setLocalScale(new Vector3f(0.08f, 0.04f, 0.02f));
		worldNode.attachChild(tv);
		*/
		
		      
        updateContext();  
       // buildCharactor();    
        buildManipulatedObjects();				
		buildButtons();
	}
	
	/**
	 * Builds the buttons.
	 */
	private void buildButtons(){    
			
		LightImageLabel telescopeButton = (LightImageLabel)contentSystem.createContentItem(LightImageLabel.class);
        telescopeButton.drawImage(ThreeDManipulation.class.getResource("telescopebutton.png"));
        telescopeButton.setImageLabelHeight(80);
        telescopeButton.setLocalLocation(850, 720);
        telescopeButton.setBorderSize(0);
        
        telescopeButton.addItemListener(new ItemEventAdapter(){
			public void cursorDoubleClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				final Telescope telescope = new Telescope(UUID.randomUUID().toString()+"telescope", contentSystem, worldNode, orthoNode, manipulatableOjbects, new Vector3f(0f, 10f, 140f), new Vector2f(400, 400), 15, 80);
				telescopes.add(telescope);
				telescope.addToolListener(new ToolListener(){
					@Override
					public void disposeTool(float x, float y) {
						
							telescope.cleanup();
							telescopes.remove(telescope);
							boom(x, y);
					}		
				});

			}	
        });     
 
        LightImageLabel cameraButton = (LightImageLabel)contentSystem.createContentItem(LightImageLabel.class);
        cameraButton.drawImage(ThreeDManipulation.class.getResource("camerabutton.png"));
        cameraButton.setImageLabelHeight(80);
        cameraButton.setLocalLocation(950, 720);
        
        cameraButton.addItemListener(new ItemEventAdapter(){
			public void cursorDoubleClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				
				int monitorsNumber = monitors.size()%4;
				String skinColor = "blue";
				if (monitorsNumber == 0)
					skinColor = "blue";
				else if (monitorsNumber ==1)
					skinColor = "red";
				else if (monitorsNumber ==2)
					skinColor = "yellow";
				else if (monitorsNumber ==3)
					skinColor = "green";
				
				final Monitor monitor = new Monitor(UUID.randomUUID().toString()+"monitor", contentSystem, worldNode, orthoNode, manipulatableOjbects, new Vector3f(0f, 10f, 140f), new Vector2f(400, 400), 5, 200, skinColor, 
						cameraManipulationMode);
				monitors.add(monitor);
				sPass.addOccluder(monitor.getCameraModel());
				monitor.addToolListener(new ToolListener(){
					@Override
					public void disposeTool(float x, float y) {
						
							monitor.cleanup();
							monitors.remove(monitor);
							boom(x, y);
					}		
				});
			}	
        });
        
        final LightImageLabel touchpadButton = (LightImageLabel)contentSystem.createContentItem(LightImageLabel.class);
        touchpadButton.drawImage(ThreeDManipulation.class.getResource("touchpadbutton.png"));
        touchpadButton.setImageLabelHeight(80);
        touchpadButton.setLocalLocation(650, 720);
        
        final LightImageLabel twinObjectButton = (LightImageLabel)contentSystem.createContentItem(LightImageLabel.class);
        twinObjectButton.drawImage(ThreeDManipulation.class.getResource("twinobjectbutton.png"));
        twinObjectButton.setImageLabelHeight(80);
        twinObjectButton.setLocalLocation(750, 720);
        
        touchpadButton.addItemListener(new ItemEventAdapter(){
			public void cursorClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				
				if (!indirectManipulationMode.equals("touchpad")){
					indirectManipulationMode = "touchpad";
					context.setIndirectManipulationMode(indirectManipulationMode);
					touchpadButton.drawImage(ThreeDManipulation.class.getResource("touchpadbuttonselected.png"));
					touchpadButton.setImageLabelHeight(80);
					twinObjectButton.drawImage(ThreeDManipulation.class.getResource("twinObjectbutton.png"));
					twinObjectButton.setImageLabelHeight(80);
					for (TwinObject twinObject: twinObjects.values()){
						twinObject.cleanup();
					}
					twinObjects.clear();
				}
				else {
					indirectManipulationMode = "";
					context.setIndirectManipulationMode(indirectManipulationMode);
					touchpadButton.drawImage(ThreeDManipulation.class.getResource("touchpadbutton.png"));
					touchpadButton.setImageLabelHeight(80);
					for (TouchPad touchPad: touchPads.values()){
						touchPad.cleanup();
					}
					touchPads.clear();
				}
			}	
        });
     
     
        
        
        twinObjectButton.addItemListener(new ItemEventAdapter(){
			public void cursorClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				
				if (!indirectManipulationMode.equals("twinobject")){
					indirectManipulationMode = "twinobject";
					context.setIndirectManipulationMode(indirectManipulationMode);
					twinObjectButton.drawImage(ThreeDManipulation.class.getResource("twinObjectbuttonselected.png"));
					twinObjectButton.setImageLabelHeight(80);
					touchpadButton.drawImage(ThreeDManipulation.class.getResource("touchpadbutton.png"));
					touchpadButton.setImageLabelHeight(80);
					for (TouchPad touchPad: touchPads.values()){
						touchPad.cleanup();
					}
					touchPads.clear();
				}
				else {
					indirectManipulationMode = "";
					context.setIndirectManipulationMode(indirectManipulationMode);
					twinObjectButton.drawImage(ThreeDManipulation.class.getResource("twinObjectbutton.png"));
					twinObjectButton.setImageLabelHeight(80);
					for (TwinObject twinObject: twinObjects.values()){
						twinObject.cleanup();
					}
					twinObjects.clear();
				}
			}	
        });

        
	}
	
//	public void buildCharactor(){
//		try {
//	        ResourceLocatorTool.addResourceLocator(
//	                    ResourceLocatorTool.TYPE_TEXTURE,
//	                    new SimpleResourceLocator(ThreeDManipulation.class.getResource("")));
//	    } catch (URISyntaxException e1) {
//	        e1.printStackTrace();
//	    }
//		
//		MilkToJme converter=new MilkToJme();
//	    URL MSFile=ThreeDManipulation.class.getResource("run.ms3d");
//	    ByteArrayOutputStream BO=new ByteArrayOutputStream();
//
//	    try {
//	        converter.convert(MSFile.openStream(),BO);
//	    } catch (IOException e) {
//	       e.printStackTrace();
//	    }
//	    
//	    Node model=null;
//	    try {
//	        model=(Node)BinaryImporter.getInstance().load(new ByteArrayInputStream(BO.toByteArray()));
//	    } catch (IOException e) {
//	        e.printStackTrace();
//	    }
//	    model.getController(0).setActive(false);
//	    
//	    worldNode.attachChild(model);
//	    
//	    Quaternion tq = new Quaternion();
//		tq.fromAngleAxis((float) (2.8*FastMath.PI/2f), new Vector3f(0, 1, 0));
//		model.setLocalRotation(tq);		
//
//	    
//	    model.setName("charactor1");
//	    model.setLocalTranslation(-20, 1f, 70);
//	    model.setLocalScale(0.08f);
//	    model.updateGeometricState(0f, false);
//	    model.updateRenderState();   
//	    model.setModelBound(new BoundingSphere());
//	    model.updateModelBound();
//	    
//	    
//	    Node modelClone=null;
//	    try {
//	        modelClone=(Node)BinaryImporter.getInstance().load(new ByteArrayInputStream(BO.toByteArray()));
//	    } catch (IOException e) {
//	        e.printStackTrace();
//	    }
//	    modelClone.setLocalRotation(tq);	
//	    modelClone.setName("charactor1Clone");
//	    modelClone.getChild(0).setName(modelClone.getName()+modelClone.getChild(0).getName());
//	    modelClone.setLocalScale(model.getLocalScale());
//	    modelClone.getController(0).setActive(false);
//	    modelClone.updateGeometricState(0f, false);
//	    
//	    worldNode.attachChild(modelClone);
//	    
//		ManipulatableObjectBuilder.buildManipulatedObject(context, model, modelClone, false, true);
//		
//		sPass.addOccluder(model);
//		sPass.addOccluder(modelClone);
//		
//			    
//	}
	
	/**
 * Builds the target object.
 */
private void buildTargetObject(){
		targetObject = new Box("targetObject", new Vector3f(0, 0, 0), 0.5f, 0.5f, 0.5f);
		targetObject.setLocalTranslation(10000, 10000, 10000);
		worldNode.attachChild(targetObject);
		
		sPass.addOccluder(targetObject);
		sPass.addOccluder(targetObject);
		
		
		//this.targetObject.updateGeometricState(0f, false);
	}
	
	/**
	 * Builds the manipulated objects.
	 */
	private void buildManipulatedObjects(){
		   tp1 = new Teapot("tp1");
			tp1.setLocalTranslation(new Vector3f(30f, 5f, 50f));
			tp1.setModelBound(new BoundingSphere());
			tp1.updateModelBound();
			tp1.setLocalScale(0.5f);
			worldNode.attachChild(tp1);
			
			//Spatial class doesn't provide a clone method, so a duplicated object is created as twin object
	        tp1Clone = new Teapot("tp1Clone");
	        worldNode.attachChild(tp1Clone);
	       
	        
			ManipulatableObjectBuilder.buildManipulatedObject(context, tp1, tp1Clone, true, false);
			
			sPass.addOccluder(tp1);
			sPass.addOccluder(tp1Clone);
			
			/*
			final Teapot tp2 = new Teapot("tp2");
			tp2.setLocalTranslation(new Vector3f(17f, 5f, 50f));
			tp2.setModelBound(new BoundingSphere());
			tp2.updateModelBound();
			tp2.setLocalScale(0.5f);
			worldNode.attachChild(tp2);
			
			//Spatial class doesn't provide a clone method, so a duplicated object is created as twin object
	        final Teapot tp2Clone = new Teapot("tp1Clone");
	        worldNode.attachChild(tp2Clone);
	       
	        
			ManipulatableObjectBuilder.buildManipulatedObject(context, tp2, tp2Clone, true, false);
			
			sPass.addOccluder(tp2);
			sPass.addOccluder(tp2Clone);
			*/
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#cleanup()
	 */
	public void cleanup() {
		super.cleanup();
		for (Telescope telescope:telescopes)
			telescope.cleanup();
		for (Monitor monitor:monitors){
			monitor.cleanup();
			
		}
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#stateRender(float)
	 */
	@Override
	protected void stateRender(float tpf) {
		super.stateRender(tpf);
		    lastRend += tpf;
		    if (lastRend > throttle ) {
		    	for (Telescope telescope:telescopes){
		    		telescope.render(worldNode);
		    	}
		    	for (Monitor monitor:monitors)
					monitor.render(worldNode);
		      lastRend = 0;
		    }
		 tv.update(tpf);  
		    
	}
		
	/**
	 * Boom.
	 *
	 * @param x the x
	 * @param y the y
	 */
	private void boom(float x, float y){
		
		Vector3f cursorWorldStart = DisplaySystem.getDisplaySystem().getWorldCoordinates(new Vector2f(x, y), 0.9f);
		mainExplosion.setCullHint(CullHint.Dynamic);
		mainExplosion.setLocalTranslation(cursorWorldStart.x, cursorWorldStart.y, cursorWorldStart.z);		
		mainExplosion.updateGeometricState(0f, true);
		mainExplosion.updateWorldVectors();
		mainExplosion.updateRenderState();		
		mainExplosion.setInitialVelocity(0.002f);
		mainExplosion.setStartSize(0.3f);
		mainExplosion.setEndSize(0.001f);
		mainExplosion.forceRespawn();
		
	}
	
	/**
	 * Update context.
	 */
	private void updateContext(){
		context = new Context(telescopes, monitors, manipulatableOjbects, touchPads, twinObjects, contentSystem, indirectManipulationMode, worldNode, orthoNode);
	}
	
	/**
	 * Gets the context.
	 *
	 * @return the context
	 */
	public Context getContext() {
		return context;
	}

	/**
	 * Sets the context.
	 *
	 * @param context the new context
	 */
	public void setContext(Context context) {
		this.context = context;
	}
	
}
