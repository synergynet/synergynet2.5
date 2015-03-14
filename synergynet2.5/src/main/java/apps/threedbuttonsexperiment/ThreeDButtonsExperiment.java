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

package apps.threedbuttonsexperiment;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import apps.threedbuttonsexperiment.calculator.ColorHighlightedCalculator;
import apps.threedbuttonsexperiment.calculator.NormalCalculator;
import apps.threedbuttonsexperiment.calculator.ThreeDCalculator;
import apps.threedbuttonsexperiment.calculator.TwoDCalculator;
import apps.threedbuttonsexperiment.calculator.component.TaskListener;
import apps.threedbuttonsexperiment.logger.DateTextWritter;
import apps.threedbuttonsexperiment.utils.ConditionsReader;

import com.jme.bounding.OrientedBoundingBox;
import com.jme.light.PointLight;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.state.LightState;
import com.jme.system.DisplaySystem;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ListContainer;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.TextLabel;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;
import synergynetframework.appsystem.table.SynergyNetAppUtils;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.jme.config.AppConfig;
import synergynetframework.jme.sysutils.CameraUtility;

public class ThreeDButtonsExperiment extends DefaultSynergyNetApp {

	public final static Vector3f DISPLAYLOCATION_3D = new Vector3f(0, -2, -10);
	public final static Vector3f DISPLAYLOCATION_3D_LEFT = new Vector3f(-10, -2, -10);
	public final static Vector3f DISPLAYLOCATION_3D_RIGHT = new Vector3f(10, -2, -10);
	public final static Vector3f HIDDENLOCATION_3D = new Vector3f(10000, 10000, 10000);
	public final static Vector2f DISPLAYLOCATION_2D = new Vector2f(512, 350);
	public final static Vector2f HIDDENLOCATION_2D = new Vector2f(10000, 10000);
	
	public final static String TASKGROUP_MOUSE = "Mouse";
	public final static String TASKGROUP_MT = "MT";
	public final static String TASKGROUP_MTANGLE = "MTAngle";
	
	protected ContentSystem contentSystem;
	protected ColorHighlightedCalculator colorHighlightedCalculator;
	protected NormalCalculator calculator;
	protected ThreeDCalculator threeDCalculator;
	protected TwoDCalculator twoDCalculator;
	
	protected SimpleButton NormalCalculatorStartButton;
	protected SimpleButton ColorCalculatorStartButton;
	protected SimpleButton TwoDCalculatorStartButton;
	protected SimpleButton ThreeDCalculatorStartButton;
	
	protected String taskGroup="";
	protected List<String> conditions = new ArrayList<String>();
	protected List<String> pcConditions = new ArrayList<String>();
	protected List<String> mtConditions = new ArrayList<String>();
	protected List<String> mtViewConditions = new ArrayList<String>();
	
	protected TextLabel targetStringDisplayer;
	protected TextLabel targetTitleDisplayer;
	
	protected ListContainer controlMenu;
	protected DateTextWritter logWritter;
	
	public ThreeDButtonsExperiment(ApplicationInfo info) {
		super(info);
	}

	@Override
	public void addContent() {
		SynergyNetAppUtils.addTableOverlay(this);
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(this);		
		setupLighting();
		
		logWritter = new DateTextWritter();
		
		buildScence();		
		loadConditions();	
	}
	
	private void loadConditions(){
		ConditionsReader.readConditions(pcConditions, mtConditions, mtViewConditions);
	}

	private void clearQuestionLabel(){
		targetStringDisplayer.setText("");
	}
	
	private void buildScence(){
		buildTargetStringLabel();
		buildCalculator();   			
		buildMenu();
		buildStartButtons();
		
		this.hideAll();
		controlMenu.setVisible(true);
	}
	
	private void buildStartButtons(){
		NormalCalculatorStartButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		NormalCalculatorStartButton.setAutoFitSize(true);
		NormalCalculatorStartButton.setText("Start");
		NormalCalculatorStartButton.setBackgroundColour(Color.lightGray);
		NormalCalculatorStartButton.setLocalLocation(512, 100);
		NormalCalculatorStartButton.setVisible(false);
		NormalCalculatorStartButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonClicked(SimpleButton b, long id, float x, float y, float pressure) {			
				NormalCalculatorStartButton.setVisible(false);
				calculator.resetTask();
			}			
		});	
		
		ColorCalculatorStartButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		ColorCalculatorStartButton.setAutoFitSize(true);
		ColorCalculatorStartButton.setText("Start");
		ColorCalculatorStartButton.setBackgroundColour(Color.lightGray);
		ColorCalculatorStartButton.setLocalLocation(512, 100);
		ColorCalculatorStartButton.setVisible(false);
		ColorCalculatorStartButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonClicked(SimpleButton b, long id, float x, float y, float pressure) {			
				ColorCalculatorStartButton.setVisible(false);

				colorHighlightedCalculator.resetTask();
			}			
		});	
		
		ThreeDCalculatorStartButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		ThreeDCalculatorStartButton.setAutoFitSize(true);
		ThreeDCalculatorStartButton.setText("Start");
		ThreeDCalculatorStartButton.setBackgroundColour(Color.lightGray);
		ThreeDCalculatorStartButton.setLocalLocation(512, 100);
		ThreeDCalculatorStartButton.setVisible(false);
		ThreeDCalculatorStartButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonClicked(SimpleButton b, long id, float x, float y, float pressure) {			
				ThreeDCalculatorStartButton.setVisible(false);

				threeDCalculator.resetTask();
			}			
		});
		
		TwoDCalculatorStartButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		TwoDCalculatorStartButton.setAutoFitSize(true);
		TwoDCalculatorStartButton.setText("Start");
		TwoDCalculatorStartButton.setBackgroundColour(Color.lightGray);
		TwoDCalculatorStartButton.setLocalLocation(512, 100);
		TwoDCalculatorStartButton.setVisible(false);
		TwoDCalculatorStartButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonClicked(SimpleButton b, long id, float x, float y, float pressure) {			
				TwoDCalculatorStartButton.setVisible(false);
			
				twoDCalculator.resetTask();
			}			
		});
	}
	
	private void hideAllStartButtons(){
		NormalCalculatorStartButton.setVisible(false);
		ColorCalculatorStartButton.setVisible(false);
		ThreeDCalculatorStartButton.setVisible(false);
		TwoDCalculatorStartButton.setVisible(false);		
	}
	
	private void buildMenu(){
		controlMenu = (ListContainer)contentSystem.createContentItem(ListContainer.class);
		controlMenu.setBackgroundColour(Color.BLUE);
		controlMenu.setWidth(200);
		controlMenu.setItemHeight(30);
		controlMenu.setLocalLocation(200, 200);
		
		final SimpleButton mouseModelButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		mouseModelButton.setAutoFitSize(false);
		mouseModelButton.setText("Mouse Model");
		mouseModelButton.setBackgroundColour(Color.lightGray);
		mouseModelButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonClicked(SimpleButton b, long id, float x, float y, float pressure) {			
				loadConditions();
				conditions = pcConditions;
				taskGroup = "Mouse";
				moveToNextTask();
				controlMenu.setVisible(false);
				//controlMenu.setLocalLocation(HIDDENLOCATION_2D.x, HIDDENLOCATION_2D.y);
			}			
		});	
		
		final SimpleButton mtButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		mtButton.setAutoFitSize(false);
		mtButton.setText("MT Model");
		mtButton.setBackgroundColour(Color.lightGray);
		mtButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonClicked(SimpleButton b, long id, float x, float y, float pressure) {	
				loadConditions();
				conditions = mtConditions;
				taskGroup = "MT";
				moveToNextTask();
				controlMenu.setVisible(false);
				//controlMenu.setLocalLocation(HIDDENLOCATION_2D.x, HIDDENLOCATION_2D.y);
			}			
		});	
		
		final SimpleButton mtAngleButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		mtAngleButton.setAutoFitSize(false);
		mtAngleButton.setText("MT Angle Model");
		mtAngleButton.setBackgroundColour(Color.lightGray);
		mtAngleButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonClicked(SimpleButton b, long id, float x, float y, float pressure) {	
				loadConditions();
				conditions = mtViewConditions;
				taskGroup = "MT ANGLE";
				moveToNextTask();
				controlMenu.setVisible(false);
				//controlMenu.setLocalLocation(HIDDENLOCATION_2D.x, HIDDENLOCATION_2D.y);
			}			
		});	
		
		controlMenu.addSubItem(mouseModelButton);
		controlMenu.addSubItem(mtButton);
		controlMenu.addSubItem(mtAngleButton);
	} 
	
	private void buildTargetStringLabel(){
		targetTitleDisplayer = (TextLabel)contentSystem.createContentItem(TextLabel.class);
		targetTitleDisplayer.setLocalLocation(400, 700);
		targetTitleDisplayer.setBackgroundColour(Color.black);
		targetTitleDisplayer.setBorderSize(0);
		targetTitleDisplayer.setTextColour(Color.green);	
		targetTitleDisplayer.setFont(new Font("Arial", Font.PLAIN, 24));
		targetTitleDisplayer.setText("Target Number: ");
		
		targetStringDisplayer = (TextLabel)contentSystem.createContentItem(TextLabel.class);
		targetStringDisplayer.setLocalLocation(550, 700);
		targetStringDisplayer.setTextColour(Color.black);	
		targetStringDisplayer.setFont(new Font("Arial", Font.PLAIN, 30));
		targetStringDisplayer.setText("");
	}
	
	private void moveToNextTask(){
		this.hideAll();
		this.hideAllStartButtons();
		if (conditions.size()<=0){
			controlMenu.setVisible(true);
		}
		else{
			String condition = conditions.get(0);
			conditions.remove(0);
			logWritter.createFile(condition+" - "+taskGroup, "");
			runTask(condition);
		}
	}
	
	private void buildCalculator(){
		
		colorHighlightedCalculator = new ColorHighlightedCalculator("colorHighlightedcalculator", targetStringDisplayer, logWritter);
		colorHighlightedCalculator.setModelBound(new OrientedBoundingBox());
		colorHighlightedCalculator.updateModelBound();
		worldNode.attachChild(colorHighlightedCalculator);			
		colorHighlightedCalculator.setLocalScale(0.4f);
		colorHighlightedCalculator.updateGeometricState(0f, false);	
		colorHighlightedCalculator.setLocalTranslation(HIDDENLOCATION_3D);	
		colorHighlightedCalculator.addTaskListener(new TaskListener(){
			@Override
			public void taskCompleted() {
				moveToNextTask();
				
			}			
		});
		
		
		calculator = new NormalCalculator("calculator", targetStringDisplayer, logWritter);
		calculator.setModelBound(new OrientedBoundingBox());
		calculator.updateModelBound();
		worldNode.attachChild(calculator);			
		calculator.setLocalScale(0.4f);
		calculator.updateGeometricState(0f, false);	
		calculator.setLocalTranslation(HIDDENLOCATION_3D);
		calculator.addTaskListener(new TaskListener(){

			@Override
			public void taskCompleted() {
				moveToNextTask();				
			}			
		});
		
		threeDCalculator = new ThreeDCalculator("3D calculator", targetStringDisplayer, logWritter);
		threeDCalculator.setModelBound(new OrientedBoundingBox());
		threeDCalculator.updateModelBound();
		worldNode.attachChild(threeDCalculator);			
		threeDCalculator.setLocalScale(0.4f);
		threeDCalculator.updateGeometricState(0f, false);	
		threeDCalculator.setLocalTranslation(HIDDENLOCATION_3D);
		threeDCalculator.addTaskListener(new TaskListener(){

			@Override
			public void taskCompleted() {
				moveToNextTask();				
			}			
		});
			
		twoDCalculator = new TwoDCalculator(contentSystem, targetStringDisplayer, this.logWritter);
		twoDCalculator.setLocation(HIDDENLOCATION_2D);	
		twoDCalculator.addTaskListener(new TaskListener(){
			@Override
			public void taskCompleted() {
				moveToNextTask();			
			}			
		});
	}
	
	private void setThreeDCalculatorAngle(float angle, Vector3f axis){
		Quaternion tq = new Quaternion();
		tq.fromAngleAxis(angle, axis);
		tq.multLocal(threeDCalculator.getLocalRotation());
		threeDCalculator.setLocalRotation(tq);				
		threeDCalculator.updateGeometricState(0f, false);
	}
	
	
	public void hideAll(){
		
		showLabel(false);
		colorHighlightedCalculator.setLocalTranslation(HIDDENLOCATION_3D);
		calculator.setLocalTranslation(HIDDENLOCATION_3D);	
		threeDCalculator.setLocalTranslation(HIDDENLOCATION_3D);
		twoDCalculator.setLocation(HIDDENLOCATION_2D);
		
		clearQuestionLabel();
	}
	
	public void showLabel(boolean b){
		if (b){
			targetTitleDisplayer.setLocalLocation(400, 700);
			targetStringDisplayer.setLocalLocation(550, 700);
		}
		else{
			targetTitleDisplayer.setLocalLocation(HIDDENLOCATION_2D.x, HIDDENLOCATION_2D.y);
			targetStringDisplayer.setLocalLocation(HIDDENLOCATION_2D.x, HIDDENLOCATION_2D.y);
		}
	}
	
	public void stateUpdate(float tpf) {
		super.stateUpdate(tpf);		
	}

	protected void setupLighting() {
		LightState lightState = DisplaySystem.getDisplaySystem().getRenderer().createLightState();
		worldNode.setRenderState(lightState);
		lightState.setEnabled(AppConfig.useLighting);	
		
		PointLight pointlight = new PointLight();
		pointlight.setLocation(new Vector3f(50f, 50f, 10f));
		pointlight.setAmbient(ColorRGBA.red);
		pointlight.setAttenuate(true);
		pointlight.setEnabled(true);
		lightState.attach(pointlight);

		pointlight = new PointLight();
		pointlight.setLocation(new Vector3f(100f, 50f, 50f));
		pointlight.setAmbient(ColorRGBA.blue);
		pointlight.setAttenuate(true);
		pointlight.setEnabled(true);
		lightState.attach(pointlight);	
		
		worldNode.updateRenderState();
	}

	protected Camera getCamera() {
		if(cam == null) {
			cam = CameraUtility.getCamera();
			//getCamera().setLocation(new Vector3f(0f, 35f, 118f));
			//getCamera().lookAt(new Vector3f(0, 0, 100), new Vector3f( 0, 1, 0 ));
			getCamera().update();
		}		
		return cam;
	}

	@Override
	public void onActivate() {
		super.onActivate();
	
	}
	
	
	private void runTask(String condition){
		this.hideAll();
		this.hideAllStartButtons();
		this.showLabel(true);
		
		Quaternion tq = new Quaternion();
		tq.fromAngleAxis(0, new Vector3f(1,0,0));
		threeDCalculator.setLocalRotation(tq);				
		threeDCalculator.updateGeometricState(0f, false);
		
		if (condition.equals("Normal")){
			NormalCalculatorStartButton.setVisible(true);
			this.calculator.setLocalTranslation(DISPLAYLOCATION_3D);
		}
		else if (condition.equals("Color")){
			ColorCalculatorStartButton.setVisible(true);
			this.colorHighlightedCalculator.setLocalTranslation(DISPLAYLOCATION_3D);
		}
		else if (condition.equals("TwoD")){
			TwoDCalculatorStartButton.setVisible(true);
			this.twoDCalculator.setLocation(DISPLAYLOCATION_2D);
			this.twoDCalculator.update();
		}
		else if (condition.equals("ThreeD")){
			ThreeDCalculatorStartButton.setVisible(true);
			ThreeDCalculatorStartButton.setLocalLocation(800, 100);
			this.threeDCalculator.setLocalTranslation(DISPLAYLOCATION_3D_RIGHT);			
			this.setThreeDCalculatorAngle(-FastMath.PI/6f, new Vector3f(1, 0, 0));		
		}
		else if (condition.equals("M0")){
			ThreeDCalculatorStartButton.setVisible(true);
			ThreeDCalculatorStartButton.setLocalLocation(512, 100);
			this.threeDCalculator.setLocalTranslation(DISPLAYLOCATION_3D);
			
		}
		else if (condition.equals("M30")){
			ThreeDCalculatorStartButton.setVisible(true);
			ThreeDCalculatorStartButton.setLocalLocation(512, 100);
			this.threeDCalculator.setLocalTranslation(DISPLAYLOCATION_3D);
			this.setThreeDCalculatorAngle(-FastMath.PI/6f, new Vector3f(1, 0, 0));	
		}
		else if (condition.equals("M45")){
			ThreeDCalculatorStartButton.setVisible(true);
			ThreeDCalculatorStartButton.setLocalLocation(512, 100);
			this.threeDCalculator.setLocalTranslation(DISPLAYLOCATION_3D);
			this.setThreeDCalculatorAngle(-FastMath.PI/4f, new Vector3f(1, 0, 0));	
		}
		else if (condition.equals("L0")){
			ThreeDCalculatorStartButton.setVisible(true);
			ThreeDCalculatorStartButton.setLocalLocation(200, 100);
			this.threeDCalculator.setLocalTranslation(DISPLAYLOCATION_3D_LEFT);

		}
		else if (condition.equals("L30")){
			ThreeDCalculatorStartButton.setVisible(true);
			ThreeDCalculatorStartButton.setLocalLocation(200, 100);
			this.threeDCalculator.setLocalTranslation(DISPLAYLOCATION_3D_LEFT);
			this.setThreeDCalculatorAngle(-FastMath.PI/6f, new Vector3f(1, 0, 0));	
		}
		else if (condition.equals("L45")){
			ThreeDCalculatorStartButton.setVisible(true);
			ThreeDCalculatorStartButton.setLocalLocation(200, 100);
			this.threeDCalculator.setLocalTranslation(DISPLAYLOCATION_3D_LEFT);
			this.setThreeDCalculatorAngle(-FastMath.PI/4f, new Vector3f(1, 0, 0));	
		}
		else if (condition.equals("R0")){
			ThreeDCalculatorStartButton.setVisible(true);
			ThreeDCalculatorStartButton.setLocalLocation(800, 100);
			this.threeDCalculator.setLocalTranslation(DISPLAYLOCATION_3D_RIGHT);
		}
		else if (condition.equals("R30")){
			ThreeDCalculatorStartButton.setVisible(true);
			ThreeDCalculatorStartButton.setLocalLocation(800, 100);
			this.threeDCalculator.setLocalTranslation(DISPLAYLOCATION_3D_RIGHT);
			this.setThreeDCalculatorAngle(-FastMath.PI/6f, new Vector3f(1, 0, 0));	
		}
		else if (condition.equals("R45")){
			ThreeDCalculatorStartButton.setVisible(true);
			ThreeDCalculatorStartButton.setLocalLocation(800, 100);
			this.threeDCalculator.setLocalTranslation(DISPLAYLOCATION_3D_RIGHT);
			this.setThreeDCalculatorAngle(-FastMath.PI/4f, new Vector3f(1, 0, 0));	
		}
		
	}

}
