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

package apps.threedmanipulationexperiment;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import apps.threedbuttonsexperiment.utils.ConditionsReader;
import apps.threedmanipulationexperiment.gestures.ObjectRotateTranslate;
import apps.threedmanipulationexperiment.gestures.ObjectRotateTranslate.ExperimentEventListener;
import apps.threedmanipulationexperiment.tools.TouchPad;

import com.jme.bounding.BoundingSphere;
import com.jme.bounding.OrthogonalBoundingBox;
import com.jme.light.PointLight;
import com.jme.math.Quaternion;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.shape.Teapot;
import com.jme.scene.state.LightState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.RenderState.StateType;
import com.jme.system.DisplaySystem;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.TextLabel;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;
import synergynetframework.appsystem.table.SynergyNetAppUtils;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.appsystem.table.appregistry.menucontrol.HoldTopRightExit;
import synergynetframework.jme.config.AppConfig;
import synergynetframework.jme.sysutils.CameraUtility;

public class ThreeDManipulationExperiment extends DefaultSynergyNetApp {

	public final static Vector3f DISPLAYLOCATION_MANIPULATIONOBJECT_MODE_INDIRECT = new Vector3f(-10f, 5f, 100f);
	public final static Vector3f DISPLAYLOCATION_MANIPULATIONOBJECT_MODE_DIRECT = new Vector3f(-10f, -5f, 100f);
	public final static Vector2f DISPLAYLOCATION_TOUCHPAD = new Vector2f(250, 200);
	public final static Vector3f HIDDENLOCATION = new Vector3f(10000, 10000, 10000);

	public final static String MODE_DIRECTMANIPULATION = "Direct";
	public final static String MODE_INDIRECTMANIPULATION = "Indirect";
	
	public final static String SIZE_BIG = "big";
	public final static String SIZE_MIDDIUM = "middium";
	public final static String SIZE_SMALL = "small";
	
	public final static String RATE_FAST = "fast";
	public final static String RATE_MIDDIUM = "ratemiddium";
	public final static String RATE_SLOW = "slow";
	
	protected List<String> conditions = new ArrayList<String>();
	
	protected ContentSystem contentSystem;
	
	protected SimpleButton nextTrailButton;
	
	protected TouchPad touchPad;
	protected Teapot tp1;
	protected Teapot target;
	  
	private String manipulationMode = "";
	
	private int trailIndex =0;
	private boolean isExperimentCompleted=false;
	private ObjectRotateTranslate objectRotateTranslate;
	  
	public ThreeDManipulationExperiment(ApplicationInfo info) {
		super(info);
	}

	@Override
	public void addContent() {
	
		setMenuController(new HoldTopRightExit());
		
		SynergyNetAppUtils.addTableOverlay(this);
		
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(this);	

		getCamera();
		setupLighting();
	
		buildSence();	
		
	}
	
	public void stateUpdate(float tpf) {
		super.stateUpdate(tpf);
	}

	protected void setupLighting() {
		LightState lightState = DisplaySystem.getDisplaySystem().getRenderer().createLightState();
		worldNode.setRenderState(lightState);
		lightState.setEnabled(AppConfig.useLighting);	
		
		PointLight pointlight = new PointLight();
		pointlight.setLocation(new Vector3f(50f, 20f, 150f));
		pointlight.setAmbient(ColorRGBA.blue);
		pointlight.setAttenuate(true);
		pointlight.setEnabled(true);
		lightState.attach(pointlight);

		pointlight = new PointLight();
		pointlight.setLocation(new Vector3f(-50f, 20f, 100f));
		pointlight.setAmbient(ColorRGBA.blue);
		pointlight.setAttenuate(true);
		pointlight.setEnabled(true);
		lightState.attach(pointlight);	
		
		worldNode.updateRenderState();
	}

	protected Camera getCamera() {
		if(cam == null) {
			cam = CameraUtility.getCamera();
			cam.setLocation(new Vector3f(0f, 10f, 140f));
			cam.lookAt(new Vector3f(0, -10, 50), new Vector3f( 0, 1, 0 ));
			cam.update();
		}		
		return cam;
	}
	
	private void buildSence() {       
        buildManipulatedObjects();	
        buildTargetObject();
		buildButtons();
		
		ConditionsReader.readConditions(conditions);
		
	}
	
	private void buildButtons(){    
       
		nextTrailButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		nextTrailButton.setAutoFitSize(true);
		nextTrailButton.setText("Go To Next Trail");
		nextTrailButton.setBackgroundColour(Color.lightGray);
		nextTrailButton.setLocalLocation(712, 100);
		nextTrailButton.setVisible(false);
		nextTrailButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonClicked(SimpleButton b, long id, float x, float y, float pressure) {			
				if (trailIndex>=5) moveToNextCondition();
				
				if (isExperimentCompleted) return;
				nextTrailButton.setVisible(false);
				resetTrail();
							
			}			
		});	
		
		final SimpleButton startTrailButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		startTrailButton.setAutoFitSize(true);
		startTrailButton.setText("Start");
		startTrailButton.setBackgroundColour(Color.lightGray);
		startTrailButton.setLocalLocation(712, 100);
		startTrailButton.setVisible(true);
		startTrailButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonClicked(SimpleButton b, long id, float x, float y, float pressure) {			
				startTrailButton.setVisible(false);
				moveToNextCondition();
				resetTrail();
			}			
		});	
      
	}
	
	
	private void buildTargetObject(){

		TextLabel targetTitle = (TextLabel)contentSystem.createContentItem(TextLabel.class);
		targetTitle.setLocalLocation(750, 680);
		targetTitle.setBackgroundColour(Color.black);
		targetTitle.setBorderSize(0);
		targetTitle.setTextColour(Color.green);	
		targetTitle.setFont(new Font("Arial", Font.PLAIN, 30));
		targetTitle.setText("Target Object");
		
		target = new Teapot("tp2");
		target.setLocalTranslation(new Vector3f(10f, 5f, 100f));
		target.setModelBound(new BoundingSphere());
		target.updateModelBound();
		target.setLocalScale(1.5f);
		worldNode.attachChild(target);
		
		MaterialState materialState = DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
        materialState.setAmbient(ColorRGBA.red);
        materialState.setDiffuse(ColorRGBA.white);
        materialState.setSpecular(ColorRGBA.white);
        materialState.setShininess(80f);
        materialState.setEmissive(ColorRGBA.black);
        materialState.setEnabled(true);
        
        target.setRenderState(materialState);
		target.updateRenderState();   
     
	}
	
	private void buildManipulatedObjects(){
		
			TextLabel manipulateObjectTitle = (TextLabel)contentSystem.createContentItem(TextLabel.class);
			manipulateObjectTitle.setLocalLocation(280, 680);
			manipulateObjectTitle.setBackgroundColour(Color.black);
			manipulateObjectTitle.setBorderSize(0);
			manipulateObjectTitle.setTextColour(Color.green);	
			manipulateObjectTitle.setFont(new Font("Arial", Font.PLAIN, 30));
			manipulateObjectTitle.setText("Manipulated Object");
		
		    tp1 = new Teapot("tp1");
			tp1.setLocalTranslation(new Vector3f(-10f, 5f, 100f));
			tp1.setModelBound(new BoundingSphere());
			tp1.updateModelBound();
			tp1.setLocalScale(1f);
			worldNode.attachChild(tp1);
		
			MaterialState materialState = DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
	        materialState.setAmbient(ColorRGBA.white);
	        materialState.setDiffuse(ColorRGBA.white);
	        materialState.setSpecular(ColorRGBA.white);
	        materialState.setShininess(80f);
	        materialState.setEmissive(ColorRGBA.black);
	        materialState.setEnabled(true);
	        
	        tp1.setRenderState(materialState);
	        tp1.updateRenderState();  
	       	        		
			objectRotateTranslate = new ObjectRotateTranslate(tp1, tp1, tp1);
			objectRotateTranslate.setPickMeOnly(true);
			objectRotateTranslate.addExperimentEventListener(new ExperimentEventListener (){

				@Override
				public void taskCompleted(int touchNumber) {
					System.out.println("Task ends");
					endTrail(touchNumber);
					
				}
		    	
		    });
		
			touchPad = new TouchPad(UUID.randomUUID().toString()+"touchPad", contentSystem, worldNode, orthoNode, 150, tp1, new Vector2f(400, 400));
		    
		    tp1.setModelBound(null);
			tp1.updateModelBound();
			
			touchPad.getTouchPad().setModelBound(null);
			touchPad.getTouchPad().updateModelBound();
			touchPad.setLocation(new Vector2f(HIDDENLOCATION.x, HIDDENLOCATION.y));
	}
	
	private void setManipulationMode(String mode){
		this.manipulationMode=mode;
		if (mode.equals(MODE_DIRECTMANIPULATION)){
			touchPad.setLocation(new Vector2f(HIDDENLOCATION.x, HIDDENLOCATION.y));
			tp1.setLocalTranslation(DISPLAYLOCATION_MANIPULATIONOBJECT_MODE_DIRECT);
		}
		else{
			touchPad.setLocation(new Vector2f(DISPLAYLOCATION_TOUCHPAD.x, DISPLAYLOCATION_TOUCHPAD.y));
			tp1.setLocalTranslation(DISPLAYLOCATION_MANIPULATIONOBJECT_MODE_INDIRECT);
		}
	}
	
	private void setSize(String size){
		if (size.equals(SIZE_BIG)){
			tp1.setLocalScale(2f);
			this.objectRotateTranslate.setRotationSpeed(200);
			this.touchPad.setRotationSpeed(200);
		}
		else if (size.equals(SIZE_MIDDIUM)){
			tp1.setLocalScale(1.5f);
			this.objectRotateTranslate.setRotationSpeed(150);
			this.touchPad.setRotationSpeed(150);
		}
		else if (size.equals(SIZE_SMALL)){
			tp1.setLocalScale(0.5f);
			this.objectRotateTranslate.setRotationSpeed(50);
			this.touchPad.setRotationSpeed(50);
		}
		
		tp1.updateGeometricState(0f, false);
	}
	
	private void setRate(String rate){
		if (rate.equals(RATE_FAST)){
	
		}
		else if (rate.equals(RATE_MIDDIUM)){
		
		}
		else if (rate.equals(RATE_SLOW)){
			
		}
	}
	
	private void endTrail(int touchNumber){
				
		MaterialState materialState = (MaterialState)tp1.getRenderState(StateType.Material);
        materialState.setAmbient(ColorRGBA.red);     
        tp1.updateRenderState();
        
        nextTrailButton.setVisible(true);
        
        tp1.setModelBound(null);
		tp1.updateModelBound();
		
		touchPad.getTouchPad().setModelBound(null);
		touchPad.getTouchPad().updateModelBound();
		
		trailIndex++;
		
	}
	
	public void cleanup() {
		super.cleanup();
	}

	@Override
	protected void stateRender(float tpf) {
		super.stateRender(tpf);
		    
	}
	
	
	private void moveToNextCondition(){

		if (conditions.size()<=0){
			nextTrailButton.setFont(new Font("Arial", Font.PLAIN, 100));
			nextTrailButton.setText("Finished Thanks!!!");
			nextTrailButton.setBackgroundColour(Color.black);
			nextTrailButton.setTextColour(Color.green);
			nextTrailButton.setLocalLocation(512, 300);
			nextTrailButton.setVisible(true);
			this.isExperimentCompleted=true;
		}
		else{
			String condition = conditions.get(0);
			conditions.remove(0);
			setupCondition(condition);
			trailIndex=0;
		}
	}
	
	private void resetTrail(){
		Quaternion tq = new Quaternion();
		tq.fromAngleAxis(2.36f, Vector3f.UNIT_XYZ);
		tp1.setLocalRotation(tq);
		
		MaterialState materialState = (MaterialState)tp1.getRenderState(StateType.Material);
        materialState.setAmbient(ColorRGBA.white);     
        tp1.updateRenderState();
        
        this.setManipulationMode(manipulationMode);
        if (this.manipulationMode.equals(MODE_DIRECTMANIPULATION)){
        	tp1.setModelBound(new BoundingSphere());
        	tp1.updateModelBound();
        	
        }
        else{
        	tp1.setModelBound(null);
        	tp1.updateModelBound();
        }
		
		touchPad.getTouchPad().setModelBound(new OrthogonalBoundingBox());
		touchPad.getTouchPad().updateModelBound();
		
		objectRotateTranslate.resetTouchNumber();
		this.touchPad.resetTouchNumber();
           
	}
	
	private void setupCondition(String condition){
			
		if (condition.equals("DirectNormalNormal")){
			this.setManipulationMode(MODE_DIRECTMANIPULATION);
			this.setSize(SIZE_MIDDIUM);
			this.setRate(RATE_MIDDIUM);
		}
		else if (condition.equals("DirectNormalSlow")){
			this.setManipulationMode(MODE_DIRECTMANIPULATION);
			this.setSize(SIZE_MIDDIUM);
			this.setRate(RATE_SLOW);
		}
		else if (condition.equals("DirectNormalFast")){
			this.setManipulationMode(MODE_DIRECTMANIPULATION);
			this.setSize(SIZE_MIDDIUM);
			this.setRate(RATE_FAST);
		}
		else if (condition.equals("DirectBigNormal")){
			this.setManipulationMode(MODE_DIRECTMANIPULATION);
			this.setSize(SIZE_BIG);
			this.setRate(RATE_MIDDIUM);
		}
		else if (condition.equals("DirectBigSlow")){
			this.setManipulationMode(MODE_DIRECTMANIPULATION);
			this.setSize(SIZE_BIG);
			this.setRate(RATE_SLOW);
		}
		else if (condition.equals("DirectBigFast")){
			this.setManipulationMode(MODE_DIRECTMANIPULATION);
			this.setSize(SIZE_BIG);
			this.setRate(RATE_FAST);
		}
		else if (condition.equals("DirectSmallNormal")){
			this.setManipulationMode(MODE_DIRECTMANIPULATION);
			this.setSize(SIZE_SMALL);
			this.setRate(RATE_MIDDIUM);
		}
		else if (condition.equals("DirectSmallSlow")){
			this.setManipulationMode(MODE_DIRECTMANIPULATION);
			this.setSize(SIZE_SMALL);
			this.setRate(RATE_SLOW);
		}
		else if (condition.equals("DirectSmallFast")){
			this.setManipulationMode(MODE_DIRECTMANIPULATION);
			this.setSize(SIZE_SMALL);
			this.setRate(RATE_FAST);
		}
		else if (condition.equals("IndirectNormalNormal")){
			this.setManipulationMode(MODE_INDIRECTMANIPULATION);
			this.setSize(SIZE_MIDDIUM);
			this.setRate(RATE_MIDDIUM);
		}
		else if (condition.equals("IndirectNormalSlow")){
			this.setManipulationMode(MODE_INDIRECTMANIPULATION);
			this.setSize(SIZE_MIDDIUM);
			this.setRate(RATE_SLOW);
		}
		else if (condition.equals("IndirectNormalFast")){
			this.setManipulationMode(MODE_INDIRECTMANIPULATION);
			this.setSize(SIZE_MIDDIUM);
			this.setRate(RATE_FAST);
		}
		else if (condition.equals("IndirectBigNormal")){
			this.setManipulationMode(MODE_INDIRECTMANIPULATION);
			this.setSize(SIZE_BIG);
			this.setRate(RATE_MIDDIUM);
		}
		else if (condition.equals("IndirectBigSlow")){
			this.setManipulationMode(MODE_INDIRECTMANIPULATION);
			this.setSize(SIZE_BIG);
			this.setRate(RATE_SLOW);
		}
		else if (condition.equals("IndirectBigFast")){
			this.setManipulationMode(MODE_INDIRECTMANIPULATION);
			this.setSize(SIZE_BIG);
			this.setRate(RATE_FAST);
		}
		else if (condition.equals("IndirectSmallNormal")){
			this.setManipulationMode(MODE_INDIRECTMANIPULATION);
			this.setSize(SIZE_SMALL);
			this.setRate(RATE_MIDDIUM);
		}
		else if (condition.equals("IndirectSmallSlow")){
			this.setManipulationMode(MODE_INDIRECTMANIPULATION);
			this.setSize(SIZE_SMALL);
			this.setRate(RATE_SLOW);
		}
		else if (condition.equals("IndirectSmallFast")){
			this.setManipulationMode(MODE_INDIRECTMANIPULATION);
			this.setSize(SIZE_SMALL);
			this.setRate(RATE_FAST);
		}
		
		
		
	}
	
}
