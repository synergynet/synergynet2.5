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

package apps.networkedthreedpuzzle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import com.jme.scene.Spatial;
import com.jme.scene.state.LightState;
import com.jme.system.DisplaySystem;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.services.ServiceManager;
import synergynetframework.appsystem.services.exceptions.CouldNotStartServiceException;
import synergynetframework.appsystem.services.net.tablecomms.client.TableCommsClientService;
import synergynetframework.appsystem.services.net.tablecomms.messages.control.fromclient.ApplicationCommsRequest;
import synergynetframework.appsystem.table.SynergyNetAppUtils;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.appsystem.table.appregistry.menucontrol.HoldTopRightExit;
import synergynetframework.jme.config.AppConfig;
import synergynetframework.jme.sysutils.CameraUtility;

public class ThreeDPuzzle extends DefaultSynergyNetApp {

	private TableCommsClientService comms;
	protected MessageHandler messageHandler;
	
	protected ContentSystem contentSystem;	 	
	private List<SpatialAttributes> updatedSpatialAttributes = new ArrayList<SpatialAttributes>(); 
	private HashMap<String, Integer> remoteControlledTetris = new HashMap<String, Integer>(); 
	private List<String> tempList= new ArrayList<String>();
	private float currenttpf;
	  
	public ThreeDPuzzle(ApplicationInfo info) {
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
		buildTargetObject();
				
		KeyBindingManager.getKeyBindingManager().set( "toggle_setting", KeyInput.KEY_S );
		KeyBindingManager.getKeyBindingManager().set( "cameraPosition_setting", KeyInput.KEY_A );
		KeyBindingManager.getKeyBindingManager().set( "mode_setting", KeyInput.KEY_Q );
	
	}


	public void onActivate() {
	  super.onActivate();
		if(comms == null) {
			
			messageHandler = new MessageHandler(this);
			try {
				comms = (TableCommsClientService) ServiceManager.getInstance().get(TableCommsClientService.class);
			} catch (CouldNotStartServiceException e1) {
				e1.printStackTrace();
			}
			try {
				comms.register(this, messageHandler);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			comms.sendMessage(new ApplicationCommsRequest(ThreeDPuzzle.class.getName()));
		} catch (IOException e) {
			e.printStackTrace();
		}	
	
	}


	

	public void stateUpdate(float tpf) {
		super.stateUpdate(tpf);
		System.out.println("tpf: "+tpf);
		this.currenttpf = tpf;
		if(comms != null) comms.update();
		if(contentSystem != null) contentSystem.update(tpf);
		
		if (updatedSpatialAttributes.size()>0){
			this.sendMessage(new BroadcastData(updatedSpatialAttributes));
			this.updatedSpatialAttributes.clear();
		}
		
		tempList.clear();
		for (String name:this.remoteControlledTetris.keySet()){

			if (remoteControlledTetris.get(name)>2000){
				tempList.add(name);
								
			}
			else {
				this.remoteControlledTetris.put(name, (remoteControlledTetris.get(name)+(int)(tpf*1000)));
			}
		}
		
		for (String name:tempList){
			this.remoteControlledTetris.remove(name);
			this.unOccupyTetris(name);
		}
		
		if ( KeyBindingManager.getKeyBindingManager().isValidCommand("toggle_setting", false ) ) {
			
		
		}
		
		if ( KeyBindingManager.getKeyBindingManager().isValidCommand("cameraPosition_setting", false ) ) {			
			
			
		}
		
		if ( KeyBindingManager.getKeyBindingManager().isValidCommand("mode_setting", false ) ) {	
			
		}
	}
	
	private void sendMessage(Object obj) {
		if(comms != null) {
			try {
				comms.sendMessage(obj);					
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
	}

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

	protected Camera getCamera() {
		if(cam == null) {
			cam = CameraUtility.getCamera();
			cam.setLocation(new Vector3f(0f, 0f, 300f));
			cam.lookAt(new Vector3f(0, 0, 0), new Vector3f( 0, 1, 0 ));
			cam.update();
		}		
		return cam;

	}
	
	private void buildSence() {
        
        
        //build yard
        Yard yard = new Yard("yard", 92, 95, 2, ThreeDManipulation.class.getResource(
    	"floor1.jpg"), new Vector3f(15, 15f, 10 ), ThreeDManipulation.class.getResource(
    	"wall.jpg"), new Vector3f(12, 1.5f, 1 ));
        yard.setLocalTranslation(new Vector3f(0, 0, 50));
        
        Quaternion tq = new Quaternion();
		tq.fromAngleAxis(FastMath.PI/2f, new Vector3f(1, 0, 0));
		yard.setLocalRotation(tq);		
		yard.updateGeometricState(0f, false);
        
        worldNode.attachChild(yard);
        
	}
	
	private void buildTargetObject(){
		
		TetrisCollection tetris= new TetrisCollection(worldNode, new FileTetrisLoader());
		tetris.addRotateTranslateScaleListener(new TetrisCollection.ObjectRotateTranslateScaleListener(){

			@Override
			public void itemMoved(Spatial targetSpatial, float newLocationX,
					float newLocationY, float newLocationZ) {		
				if (remoteControlledTetris.containsKey(targetSpatial.getName())) return;
				updatedSpatialAttributes.add(new SpatialAttributes(targetSpatial.getName(), "position", new Vector3f(newLocationX, newLocationY, newLocationZ), null));
				
			}

			@Override
			public void itemRotated(Spatial targetSpatial, Quaternion quaternion) {
				if (remoteControlledTetris.containsKey(targetSpatial.getName())) return;
				updatedSpatialAttributes.add(new SpatialAttributes(targetSpatial.getName(), "rotation", null, quaternion));			
			}
			
		});

	}
	
	public void syncTetris(List<SpatialAttributes> attributesList){
		
		for (SpatialAttributes attribute:attributesList){
			String name =attribute.getSpatialName();
			Spatial tetris = worldNode.getChild(attribute.getSpatialName());
				
			
			
			if (this.remoteControlledTetris.containsKey(name)){
				this.remoteControlledTetris.put(name, remoteControlledTetris.get(name)-(int)(this.currenttpf*1000));
			}
			else{
				this.remoteControlledTetris.put(name, 0);
				this.occupyTetris(name);
			}
			
			if (attribute.getType().equals("position")){
				tetris.setLocalTranslation(attribute.getPosition());
			}
			else{
				tetris.setLocalRotation(attribute.getRotation());
			}
			
		}
		
	}
	
	public void occupyTetris(String name){
		Spatial tetris = worldNode.getChild(name);
		tetris.setLocalScale(3);
		tetris.updateGeometricState(0, true);
	}
	
	public void unOccupyTetris(String name){
		Spatial tetris = worldNode.getChild(name);
		tetris.setLocalScale(10);
		tetris.updateGeometricState(0, true);
	}
	
	public void cleanup() {
		super.cleanup();
	}



	
}
