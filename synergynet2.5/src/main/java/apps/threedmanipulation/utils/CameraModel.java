package apps.threedmanipulation.utils;

import apps.threedmanipulation.ThreeDManipulation;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.light.PointLight;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Cylinder;
import com.jme.scene.state.LightState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;


/**
 * The Class CameraModel.
 */
public class CameraModel extends Node {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5885506028867024173L;

	/** The name. */
	protected String name;
	
	/** The texture url. */
	protected String textureURL;
	
	/** The body. */
	protected Cylinder body;
	
	/**
	 * Instantiates a new camera model.
	 *
	 * @param name the name
	 * @param textureURL the texture url
	 */
	public CameraModel(String name, String textureURL){
		this.name = name;
		this.textureURL = textureURL;
		
		this.buildCamera();
	}
	
	/**
	 * Builds the camera.
	 */
	private void buildCamera(){
		body  = new Cylinder(name + "body", 20, 50, 3, 10, true);
		body.setModelBound(new BoundingBox());
		body.updateModelBound();
		body.getLocalTranslation().set( 0, 0, 3 );
		
		this.attachChild(body);
		
		Cylinder len1  = new Cylinder(name + "len1", 20, 50, 2.5f, 1, true);
		len1.getLocalTranslation().set( 0, 0, 8 );
		this.attachChild(len1);
		
		Cylinder len2  = new Cylinder(name + "len2", 20, 50, 1.5f, 2, true);
		len2.getLocalTranslation().set( 0, 0, 9.5f );
		this.attachChild(len2);
		
		Cylinder len3  = new Cylinder(name + "len3", 20, 50, 2f, 1, true);

		len3.getLocalTranslation().set( 0, 0, 11 );
		this.attachChild(len3);
		
		Cylinder tape  = new Cylinder(name + "tape", 20, 50, 2f, 1, true);
		tape.getLocalTranslation().set( 0, 3,3 );
		this.attachChild(tape);
		
		Quaternion tq = new Quaternion();
		tq.fromAngleAxis(FastMath.PI/2f, new Vector3f(0, 1, 0));
		tape.setLocalRotation(tq);		
		tape.updateGeometricState(0f, false);
		
	
		TextureState ts;
		Texture texture;	
		ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		ts.setCorrectionType(TextureState.CorrectionType.Perspective);	
		texture  = TextureManager.loadTexture(ThreeDManipulation.class.getResource(textureURL),  Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);
		texture.setScale( new Vector3f(1, 1f, 1 ) );
		ts.setTexture(texture );	
		ts.apply();
		
		this.setRenderState( ts );	
		this.updateRenderState();
		

		MaterialState materialState = DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
        materialState.setAmbient(ColorRGBA.red);
        materialState.setDiffuse(ColorRGBA.blue);
        materialState.setSpecular(ColorRGBA.blue);
        materialState.setShininess(50f);
        materialState.setEmissive(ColorRGBA.yellow);
        materialState.setEnabled(true);
        //this.setRenderState(materialState);
        // this.updateRenderState();
	
		LightState lightState = DisplaySystem.getDisplaySystem().getRenderer().createLightState();
		this.setRenderState(lightState);
		lightState.setEnabled(true);	
		
		PointLight pointlight = new PointLight();
		pointlight.setLocation(new Vector3f(300f, 50f, 150f));
		pointlight.setAmbient(ColorRGBA.white);
		pointlight.setAttenuate(true);
		pointlight.setEnabled(true);
		lightState.attach(pointlight);
		
		PointLight pointlight1 = new PointLight();
		pointlight1.setLocation(new Vector3f(-300f, 50f, 150f));
		pointlight1.setAmbient(ColorRGBA.white);
		pointlight1.setAttenuate(true);
		pointlight1.setEnabled(true);
		lightState.attach(pointlight1);
	
		this.updateRenderState();
	
		ZBufferState zstate = DisplaySystem.getDisplaySystem().getRenderer().createZBufferState();
		zstate.setEnabled(true);
		zstate.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
		this.setRenderState(zstate);
		this.updateRenderState();
	}
	
	/**
	 * Gets the camera body.
	 *
	 * @return the camera body
	 */
	public Spatial getCameraBody(){
		return body;
	}
}
