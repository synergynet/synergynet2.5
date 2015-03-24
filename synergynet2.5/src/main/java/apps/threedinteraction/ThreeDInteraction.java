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

package apps.threedinteraction;

import java.util.Date;

import apps.threedinteraction.button.ButtonNode;
import apps.threedinteraction.button.KeyListener;
import apps.threedinteraction.button.RoundButton;
import apps.threedinteraction.calculator.CalculatorNode;
import apps.threedinteraction.clock.Clock;
import apps.threedinteraction.desktop.Desktop;
import apps.threedinteraction.desktop.FloatDesktop;
import apps.threedinteraction.tv.TV;
import apps.threedmanipulation.ThreeDManipulation;

import com.jme.light.PointLight;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.state.LightState;
import com.jme.system.DisplaySystem;

import common.CommonResources;
import core.SynergyNetDesktop;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.table.SynergyNetAppUtils;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.jme.config.AppConfig;
import synergynetframework.jme.sysutils.CameraUtility;


/**
 * The Class ThreeDInteraction.
 */
public class ThreeDInteraction extends DefaultSynergyNetApp {

	/** The Constant Status_NORMAL. */
	public static final String Status_NORMAL = "normal";
	
	/** The Constant Status_TRANSFORMTO. */
	public static final String Status_TRANSFORMTO = "transformto";
	
	/** The Constant Status_TRANSFORMBACK. */
	public static final String Status_TRANSFORMBACK = "transformback";
	
	/** The Constant Status_TRANSFORMED. */
	public static final String Status_TRANSFORMED = "transformed";
	
	/** The calculator. */
	protected CalculatorNode calculator;
	
	/** The tv. */
	protected TV tv;
	
	/** The clock. */
	protected Clock clock;
	
	/** The tv buttons. */
	protected Node tvButtons;
	
	/** The content system. */
	protected ContentSystem contentSystem;
	
	/** The scene status. */
	protected String sceneStatus = ThreeDInteraction.Status_NORMAL;
	
	/** The date. */
	protected Date date = new Date();

	
	/**
	 * Instantiates a new three d interaction.
	 *
	 * @param info the info
	 */
	public ThreeDInteraction(ApplicationInfo info) {
		super(info);
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#addContent()
	 */
	@Override
	public void addContent() {
		SynergyNetAppUtils.addTableOverlay(this);
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(this);		
		setupLighting();
		buildScence();
		buildClock();
	}

	
	/**
	 * Builds the scence.
	 */
	private void buildScence(){
		buildCalculator();
		buildTV();
		
		final Desktop desktop = new Desktop("yard", 27,36.7f, 30, ThreeDManipulation.class.getResource(
    	"wallpaper.png"), new Vector3f(1, 1f, 1 ), ThreeDManipulation.class.getResource(
    	"sidewallpaper.jpg"), new Vector3f(1, 1f, 1 ));
		desktop.setLocalTranslation(new Vector3f(0, 0, 101.7f));
        worldNode.attachChild(desktop); 
         
        final FloatDesktop floatDesktop = new FloatDesktop("float desktop", new Vector3f(0, 1, 100), 37, 32, worldNode );
        floatDesktop.showDesktop(false);
        
        final ButtonNode clockButton = new ButtonNode("clock button"+name, 1.2f, 1.2f, 1f, 0.1f, ThreeDManipulation.class.getResource(
    	"buttonbody.png"),  ThreeDManipulation.class.getResource(
    	"clockfack.png"));
        clockButton.setZvalueOfButtonLabel(0.1f);
        clockButton.setButtonBodyVisability(false);
        clockButton.addKeyListener(new KeyListener(){
			@Override
			public void keyPressed(String key) {
				if (sceneStatus != ThreeDInteraction.Status_NORMAL )
					return;
				
				if (clock.getLocalTranslation().x>1000){
					clock.setLocalTranslation(0, 5, 100);
					calculator.setLocalTranslation(10000, 10000, 10000);
					tv.setLocalTranslation(10000, 10000, 10000);
				}
				else
					clock.setLocalTranslation(10000, 10000, 10000);
			}			
		}); 
        
        ButtonNode calculatorButton = new ButtonNode("Calculator"+name, 1.2f, 1.2f, 1f, 0.1f, ThreeDManipulation.class.getResource(
    	"buttonbody.png"),  ThreeDManipulation.class.getResource(
    	"calculator.png"));
        calculatorButton.setZvalueOfButtonLabel(0.1f);
        calculatorButton.setLocalTranslation(0, -3, 0);
        calculatorButton.setButtonBodyVisability(false);
        calculatorButton.addKeyListener(new KeyListener(){
			@Override
			public void keyPressed(String key) {
						

				if (sceneStatus != ThreeDInteraction.Status_NORMAL )
					return;
				
					if (calculator.getLocalTranslation().x>1000){
						calculator.setLocalTranslation(0, 2, 100);
						clock.setLocalTranslation(10000, 10000, 10000);
						tv.setLocalTranslation(10000, 10000, 10000);
					}
					else
						calculator.setLocalTranslation(10000, 10000, 10000);
				
			}			
		});
              
        ButtonNode wmpButton = new ButtonNode("MediaPlayer"+name, 1.2f, 1.2f, 1f, 0.1f, ThreeDManipulation.class.getResource(
    	"buttonbody.png"),  ThreeDManipulation.class.getResource(
    	"wmp.png"));
        wmpButton.setZvalueOfButtonLabel(0.1f);
        wmpButton.setButtonBodyVisability(false);
        wmpButton.setLocalTranslation(0, -6, 0);
        wmpButton.addKeyListener(new KeyListener(){
			@Override
			public void keyPressed(String key) {
				cam.lookAt(new Vector3f(0, 5, 100), new Vector3f( 0, 1, 0 ));
				cam.update();
				tv.setLocalTranslation(0, 5f, 100);
				sceneStatus = ThreeDInteraction.Status_TRANSFORMTO;
				calculator.setLocalTranslation(10000, 10000, 10000);
				clock.setLocalTranslation(10000, 10000, 10000);
			}			
		});    
        
        final ButtonNode weatherValueButton = new ButtonNode("weather value"+name, 10f, 10f, 1f, 0.1f, ThreeDManipulation.class.getResource(
    	"buttonbody.png"),  ThreeDManipulation.class.getResource(
    	"weathervalue.png"));
        weatherValueButton.setZvalueOfButtonLabel(0.1f);
        weatherValueButton.setLocalTranslation(10000, 10000, 10000);
        weatherValueButton.setButtonBodyVisability(false);
        worldNode.attachChild(weatherValueButton);
        
        ButtonNode weatherButton = new ButtonNode("weather"+name, 1.2f, 1.2f, 1f, 0.1f, ThreeDManipulation.class.getResource(
    	"buttonbody.png"),  ThreeDManipulation.class.getResource(
    	"weatherbutton.png"));
        weatherButton.setZvalueOfButtonLabel(0.1f);
        weatherButton.setLocalTranslation(0, -9, 0);
        weatherButton.setButtonBodyVisability(false);
        weatherButton.addKeyListener(new KeyListener(){
			@Override
			public void keyPressed(String key) {
						
				if (!floatDesktop.hasDesktopShown())
				{				
					desktop.showDesktop(false);
					floatDesktop.showDesktop(true);
					weatherValueButton.setLocalTranslation(new Vector3f(0, 10, 100));	
				}
				else{
					desktop.showDesktop(true);
					floatDesktop.showDesktop(false);
					weatherValueButton.setLocalTranslation(new Vector3f(10000, 10000, 10000));
				}
				
			}			
		});
        
       
        
        ButtonNode exitButton = new ButtonNode("Exit "+name, 1.2f, 1.2f, 1f, 0.1f, ThreeDManipulation.class.getResource(
    	"buttonbody.png"),  ThreeDManipulation.class.getResource(
    	"exitsign.png"));
        exitButton.setZvalueOfButtonLabel(0.1f);
        exitButton.setButtonBodyVisability(false);
        exitButton.setLocalTranslation(0, -12, 0);
        exitButton.addKeyListener(new KeyListener(){
			@Override
			public void keyPressed(String key) {
				try {
					SynergyNetDesktop.getInstance().showMainMenu();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}			
		});  
        
        final Node boxButton = new Node();
        boxButton.attachChild(calculatorButton);
		boxButton.attachChild(wmpButton);	
		boxButton.attachChild(weatherButton);
		boxButton.attachChild(clockButton);
		boxButton.attachChild(exitButton);
		Quaternion tq1 = new Quaternion();
		tq1.fromAngleAxis(-FastMath.PI/2f, new Vector3f(1, 0, 0));
		boxButton.setLocalRotation(tq1);	
        boxButton.setLocalTranslation(new Vector3f(10000, 10000, 10000));		
		worldNode.attachChild(boxButton);
		
        
        RoundButton startButton = new RoundButton("Start Button", 0.9f, 0.6f, ThreeDManipulation.class.getResource(
    	"taskbar.png"),  ThreeDManipulation.class.getResource(
    	"startbuttonlabel.png"));
        startButton.setLocalTranslation(new Vector3f(-15.5f, 1, 114.3f));
        worldNode.attachChild(startButton);    
        startButton.addKeyListener(new KeyListener(){
			@Override
			public void keyPressed(String key) {
				
					if (boxButton.getLocalTranslation().x>1000)
						boxButton.setLocalTranslation(-14f, 2, 93f);
					else{
						boxButton.setLocalTranslation(10000, 10000, 10000);
					}
				
			}			
		});    
        
         /*     
        ButtonNode timeButton = new ButtonNode("time button"+name, 1.6f, 1.0f, 2f, 0.1f, ThreeDManipulation.class.getResource(
    	"taskbar.png"),  ThreeDManipulation.class.getResource(
    	"startbuttonlabel.png"));
        timeButton.setZvalueOfButtonLabel(1f);
        timeButton.setButtonBodyVisability(true);
        timeButton.setLocalTranslation(0, 0, 0);
        timeButton.addKeyListener(new KeyListener(){
			@Override
			public void keyPressed(String key) {

			}			
		});  
		*/  
		
		final RoundButton close = new RoundButton("Close TV Button", 0.7f, 0.7f, ThreeDManipulation.class.getResource(
    	"taskbar.png"),  ThreeDManipulation.class.getResource(
    	"onbutton.jpg"));
        
       // Quaternion tq3 = new Quaternion();
		//tq3.fromAngleAxis(3*FastMath.PI/2f, new Vector3f(1, 0, 0));
		//close.setLocalRotation(tq3);
		tvButtons = new Node();
		
        worldNode.attachChild(close);    
        close.addKeyListener(new KeyListener(){
			@Override
			public void keyPressed(String key) {
				sceneStatus = ThreeDInteraction.Status_TRANSFORMBACK;
				tv.tvShow(false);
				tvButtons.setLocalTranslation(10000, 10000, 10000);
				
			}			
		});    
        
        

        @SuppressWarnings("unused")
		final Node boxButton1 = new Node();		
		tvButtons.attachChild(close);		
		Quaternion tq2 = new Quaternion();
		tq2.fromAngleAxis(FastMath.PI/2f, new Vector3f(1, 0, 0));
		tvButtons.setLocalRotation(tq2);	
        
        tvButtons.setLocalTranslation(10000, 10000, 10000);	
		worldNode.attachChild(tvButtons);
				
		
	}
	
	/**
	 * Builds the calculator.
	 */
	private void buildCalculator(){
		calculator = new CalculatorNode("calculator");
		worldNode.attachChild(calculator);
		
		Quaternion tq = new Quaternion();
		tq.fromAngleAxis(-FastMath.PI/2f, new Vector3f(1, 0, 0));
		calculator.setLocalRotation(tq);				
		calculator.setLocalScale(0.4f);
		calculator.updateGeometricState(0f, false);	
		calculator.setLocalTranslation(10000, 10000, 10000);
		
		
		/*
		 * 
		CoolCalculatorNode coolNode = new CoolCalculatorNode("Cool");
		worldNode.attachChild(coolNode);
		
		coolNode.setLocalRotation(tq);		
		coolNode.updateGeometricState(0f, false);
		
		coolNode.setLocalTranslation(55, 60, 90);
		coolNode.setLocalScale(1f);
		
		MenuNode menuNode = new MenuNode("Menu Node");
		
		menuNode.setLocalRotation(tq);		
		menuNode.updateGeometricState(0f, false);
		
		menuNode.setLocalTranslation(15, 50, 90);
		menuNode.setLocalScale(1f);
		
		worldNode.attachChild(menuNode);
		*/	
	}
	
	/**
	 * Builds the tv.
	 */
	private void buildTV(){
		tv = new TV(contentSystem, CommonResources.class.getResource("smallvid.mp4"), 2, ThreeDManipulation.class.getResource(
    	"tvskin.png"));
		tv.setLocalTranslation(10000, 10000f, 10000);
		worldNode.attachChild(tv);
    	
	}
	
	/**
	 * Builds the clock.
	 */
	private void buildClock(){
		clock = new Clock("clock "+name, 10f, 3f, ThreeDManipulation.class.getResource(
    	"clockfack.png"),  ThreeDManipulation.class.getResource(
    	"hourhand.png"), ThreeDManipulation.class.getResource(
    	"minhand.png"),ThreeDManipulation.class.getResource(
    	"secondhand.png"));
		clock.setLocalTranslation(10000, 10000f, 1000);
		worldNode.attachChild(clock);
		    	
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp#stateUpdate(float)
	 */
	@SuppressWarnings("deprecation")
	public void stateUpdate(float tpf) {
		super.stateUpdate(tpf);
		tv.update(tpf);
		if (sceneStatus.equals(ThreeDInteraction.Status_TRANSFORMTO)){	
			if (cam.getLocation().y>=5){
				cam.setLocation(new Vector3f(cam.getLocation().x, cam.getLocation().y -tpf*4, cam.getLocation().z));
				cam.lookAt(new Vector3f(0, 5, 100), new Vector3f( 0, 1, 0 ));
				cam.update();
			}
			else{
				getCamera().lookAt(new Vector3f(0, 5, 100), new Vector3f( 0, 1, 0 ));
				cam.update();
				sceneStatus = ThreeDInteraction.Status_TRANSFORMED;
				tv.tvShow(true);
				tvButtons.setLocalTranslation(0f, 2.5f, 99);	
			}
		}
		else if (sceneStatus.equals(ThreeDInteraction.Status_TRANSFORMBACK)){
			if (cam.getLocation().y<=35){
				cam.setLocation(new Vector3f(cam.getLocation().x, cam.getLocation().y +tpf*4, cam.getLocation().z));
				getCamera().lookAt(new Vector3f(0, 5, 100), new Vector3f( 0, 1, 0 ));
				cam.update();
			}
			else{
				getCamera().lookAt(new Vector3f(0, 0, 100), new Vector3f( 0, 1, 0 ));
				cam.update();
				sceneStatus = ThreeDInteraction.Status_NORMAL;
				tv.setLocalTranslation(10000, 10000f, 1000);
			}
		}
		
		if (clock.getLocalTranslation().x<1000){
			Date date = new Date();
			clock.setTime(date.getHours(), date.getMinutes(), date.getSeconds());
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

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp#getCamera()
	 */
	protected Camera getCamera() {
		if(cam == null) {
			cam = CameraUtility.getCamera();
			getCamera().setLocation(new Vector3f(0f, 35f, 118f));
			getCamera().lookAt(new Vector3f(0, 0, 100), new Vector3f( 0, 1, 0 ));
			getCamera().update();
		}		
		return cam;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#onActivate()
	 */
	@Override
	public void onActivate() {
		super.onActivate();
	
	}
	
}
