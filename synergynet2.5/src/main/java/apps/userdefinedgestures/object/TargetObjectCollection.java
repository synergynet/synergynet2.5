package apps.userdefinedgestures.object;

import apps.threedmanipulation.ThreeDManipulation;

import com.jme.image.Texture;
import com.jme.image.Texture.ApplyMode;
import com.jme.image.Texture.WrapMode;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.pass.DirectionalShadowMapPass;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.Spatial.CullHint;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Cylinder;
import com.jme.scene.shape.Quad;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

public class TargetObjectCollection {

	
    private static final ColorRGBA NO_COLOR = ColorRGBA.black;
    @SuppressWarnings("unused")
	private static final ColorRGBA AMBIENT_GRAY_COLOR = new ColorRGBA(0.7f, 0.7f, 0.7f, 1.0f);
    @SuppressWarnings("unused")
	private static final ColorRGBA AMBIENT_COLORED_COLOR = new ColorRGBA(0.8f, 0.8f, 0.2f, 1.0f);
    @SuppressWarnings("unused")
	private static final ColorRGBA SPECULAR_COLOR = ColorRGBA.white;
    private static final ColorRGBA DIFFUSE_COLOR = new ColorRGBA(0.1f, 0.5f, 0.8f, 1.0f);
    @SuppressWarnings("unused")
	private static final ColorRGBA EMISSIVE_COLOR = new ColorRGBA(0.3f, 0.2f, 0.2f, 0.0f);
    private static final float NO_SHININESS = 0.0f;
    @SuppressWarnings("unused")
	private static final float LOW_SHININESS = 5.0f;
    @SuppressWarnings("unused")
	private static final float HIGH_SHININESS = 100.0f;
	
	private Spatial cube;
	private Spatial tube;
	private Spatial ball;
	private Spatial sheet;
	
	public TargetObjectCollection(Node worldNode, DirectionalShadowMapPass pass){
	
		cube = new Box("cube", new Vector3f(0, 0, 0), 3f, 3f, 3f);
		cube.setLocalTranslation(0, 7, 0);
		
		tube = new Node();
		
		Cylinder tubeObject = new Cylinder("tube", 20, 20, 0.5f, 10, true);
		Quaternion q = new Quaternion();
		q.fromAngleAxis(FastMath.PI/2, new Vector3f(0,1,0));
		tubeObject.setLocalRotation(q);
		((Node)tube).attachChild(tubeObject);
		
		tube.setLocalTranslation(0, 7, 0);
		
		ball = new Sphere("Sphere", 20, 20, 3);
		ball.setLocalTranslation(0, 7, 0);
		
		sheet = new Node();

		Quad sheetObject = new Quad("quad", 10, 10);
		q = new Quaternion();
		q.fromAngleAxis(FastMath.PI/2, new Vector3f(1,0,0));
		sheetObject.setLocalRotation(q);
		((Node)sheet).attachChild(sheetObject);
		
		sheet.setLocalTranslation(0, 7, 0);
		
		MaterialState materialState = DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
        materialState.setAmbient(NO_COLOR);
        materialState.setDiffuse(DIFFUSE_COLOR);
        materialState.setSpecular(NO_COLOR);
        materialState.setShininess(NO_SHININESS);
        materialState.setEmissive(NO_COLOR);
        materialState.setEnabled(true);
        
        TextureState ts;
		Texture texture;	
		ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		ts.setCorrectionType(TextureState.CorrectionType.Perspective);		
		texture  = TextureManager.loadTexture(ThreeDManipulation.class.getResource(
    	"texture.jpg"),  Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);
		texture.setWrap(WrapMode.Repeat);
		texture.setApply(ApplyMode.Replace);
		texture.setScale( new Vector3f(3, 2, 1 ));
		ts.setTexture( texture );	
		ts.apply();
		
		ball.setRenderState( ts );	
		tube.setRenderState( ts );	
		cube.setRenderState( ts );	
		sheet.setRenderState( ts );	
 
        cube.setRenderState(materialState);
        cube.updateRenderState();
        
        tube.setRenderState(materialState);
        tube.updateRenderState();
        
        ball.setRenderState(materialState);
        ball.updateRenderState();
        
        sheet.setRenderState(materialState);
        sheet.updateRenderState();

		worldNode.attachChild(cube);
		worldNode.attachChild(tube);
		worldNode.attachChild(ball);
		worldNode.attachChild(sheet);
		
		pass.addOccluder(cube);
		pass.addOccluder(tube);
		pass.addOccluder(ball);
		pass.addOccluder(sheet);
		
		cube.updateGeometricState(0, false);
		tube.updateGeometricState(0, false);
		ball.updateGeometricState(0, false);
		sheet.updateGeometricState(0, false);
		
	}
	
	public Spatial getObject(String objectName){
		if (objectName.equals("cube"))
			return cube;
		else if (objectName.equals("tube"))
			return tube;
		else if (objectName.equals("ball"))
			return ball;
		else
			return sheet;
	}
	
	
	public void hideAllObjects(){
		cube.setCullHint(CullHint.Always);
		tube.setCullHint(CullHint.Always);
		ball.setCullHint(CullHint.Always);
		sheet.setCullHint(CullHint.Always);
	}
	
}
