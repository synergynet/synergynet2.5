/*
 * Copyright (c) 2009 University of Durham, England
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

package apps.dominos;

import java.util.ArrayList;
import java.util.List;


import com.jme.bounding.BoundingBox;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Line;
import com.jme.scene.SharedMesh;
import com.jme.scene.Spatial;
import com.jme.scene.TriMesh;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Extrusion;
import com.jme.scene.state.LightState;
import com.jme.system.DisplaySystem;
import com.jme.util.geom.BufferUtils;
import com.jmetest.physics.Utils;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.StaticPhysicsNode;

import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.appsystem.table.appregistry.menucontrol.HoldTopRightExit;
import synergynetframework.jme.config.AppConfig;
import synergynetframework.jme.cursorsystem.elements.threed.MultiTouchMoveableXYPlaneWithPhysics;
import synergynetframework.jme.sysutils.CameraUtility;


/**
 * The Class DominosApp.
 */
public class DominosApp extends DefaultSynergyNetApp {


	/** The physics space. */
	protected PhysicsSpace physicsSpace;
	
	/** The physics speed. */
	protected float physicsSpeed = 2f;
	
	/**
	 * Instantiates a new dominos app.
	 *
	 * @param info the info
	 */
	public DominosApp(ApplicationInfo info) {
		super(info);
	}

	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#addContent()
	 */
	@Override
	public void addContent() {
		
		this.setMenuController(new HoldTopRightExit());
		
		physicsSpace = PhysicsSpace.create();
		physicsSpace.setAutoRestThreshold( 0.2f );
		Spatial floorVisual = new Box( "floor", new Vector3f(), 1000, 0.1f, 1000 );
		floorVisual.setModelBound( new BoundingBox(new Vector3f(), 1000, 0.1f ,1000) );
		floorVisual.updateModelBound();
		StaticPhysicsNode floor = physicsSpace.createStaticNode();
		floor.attachChild( floorVisual );
		floor.setModelBound( new BoundingBox(new Vector3f(), 1000, 0.1f ,1000) );
		floor.generatePhysicsGeometry();
		floor.setLocalTranslation( new Vector3f( 0, -0.1f, 0 ) );
		worldNode.attachChild( floor );

		TriMesh dominoBrickVisual[] = new TriMesh[4];
		for ( int i = 0; i < dominoBrickVisual.length; i++ ) {
			dominoBrickVisual[i] = new Box( "brick", new Vector3f(), 1f, 2, 0.2f );
			dominoBrickVisual[i].setModelBound( new BoundingBox(new Vector3f(), 1f, 2, 0.2f ));
			dominoBrickVisual[i].updateModelBound();
			dominoBrickVisual[i].lockMeshes();
			//color them blue, red, green, yellow, ...
			Utils.color( dominoBrickVisual[i], new ColorRGBA( i&1, ( i & 2 ) >> 1, i==0?1:0, 1 ), 128 );
			dominoBrickVisual[i].updateRenderState();
			
		}

		List<Vector3f> points = new ArrayList<Vector3f>();
		points.add( new Vector3f( 0, 0, 0 ) );
		points.add( new Vector3f( 0, 0, 50 ) );
		points.add( new Vector3f( 50, 0, 100 ) );
		points.add( new Vector3f( 50, 0, 150 ) );
		points.add( new Vector3f( 0, 0, 175 ) );
		points.add( new Vector3f( -50, 0, 200 ) );
		points.add( new Vector3f( -30, 0, 250 ) );
		points.add( new Vector3f( 0, 0, 300 ) );
		points.add( new Vector3f( 0, 0, 300 ) );
		Line dot = new Line();
		dot.appendCircle( 0.3f, 0, 0, 1, false );
		Extrusion track = new Extrusion( "track" );
		Vector3f up = new Vector3f( 0, 1, 0 );
		track.updateGeometry( dot, points, 25, up );

		Vector3f[] trackpoints = BufferUtils.getVector3Array( track.getVertexBuffer() );
		Vector3f last = null;
		Vector3f dir = new Vector3f();

		// iterate over the extruded points, taking every second vertex
		for ( int i = 0; i < trackpoints.length; i += 2 ) {
			Vector3f trackpoint = trackpoints[i];
			DynamicPhysicsNode dominoBrick = physicsSpace.createDynamicNode();
			dominoBrick.setName("brick" + i);
			SharedMesh sharedMesh = new SharedMesh("sharedbrick" + i, dominoBrickVisual[i/2%dominoBrickVisual.length] );
			sharedMesh.setModelBound( new BoundingBox(new Vector3f(), 1f, 2, 0.2f ));
			dominoBrick.attachChild( sharedMesh );
			dominoBrick.generatePhysicsGeometry();
			dominoBrick.setLocalScale(1f);
			dominoBrick.getLocalTranslation().set( trackpoint ).addLocal( 0, 2, 0 );
			if ( last != null ) {
				dir.set( last ).subtractLocal( trackpoint );
				dominoBrick.getLocalRotation().lookAt( dir, up );
			}
			worldNode.attachChild( dominoBrick );
            dominoBrick.rest();
            
            MultiTouchMoveableXYPlaneWithPhysics mtm = new MultiTouchMoveableXYPlaneWithPhysics(sharedMesh, dominoBrick, dominoBrick);
            mtm.setPickMeOnly(true);
			
            last = trackpoint;
		}
		
		getCamera().setLocation(new Vector3f(0f, 10f, 50f));
		getCamera().lookAt(new Vector3f(), new Vector3f( 0, 0, -1 ));
		getCamera().update();
		
		setupLighting();
		
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#onDeactivate()
	 */
	protected void onDeactivate() {	
		
		super.onDeactivate();
		
		physicsSpace.delete();
	}
		
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp#getCamera()
	 */
	protected Camera getCamera() {
		if(cam == null) {
			cam = CameraUtility.getCamera();			
		}	
		cam.setLocation(new Vector3f(0f, 10f, 50f));
		cam.lookAt(new Vector3f(), new Vector3f( 0, 0, -1 ));
		cam.update();
		return cam;
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp#stateUpdate(float)
	 */
	public void stateUpdate(float tpf) {
		super.stateUpdate(tpf);
		physicsSpace.update( tpf * physicsSpeed );
	}

	/**
	 * Setup lighting.
	 */
	private void setupLighting() {
		LightState lightState = DisplaySystem.getDisplaySystem().getRenderer().createLightState();
		worldNode.setRenderState(lightState);
		lightState.setEnabled(AppConfig.useLighting);	
		
		PointLight pointlight = new PointLight();
		pointlight.setLocation(new Vector3f(50f, 20f, 50f));
		pointlight.setAmbient(ColorRGBA.white);
		pointlight.setAttenuate(true);
		pointlight.setEnabled(true);
		lightState.attach(pointlight);
		
		pointlight = new PointLight();
		pointlight.setAttenuate(true);
		pointlight.setLocation(new Vector3f(-50f, 20f, 50f));
		pointlight.setAmbient(ColorRGBA.white);
		pointlight.setEnabled(true);
		lightState.attach(pointlight);	
	}
}
