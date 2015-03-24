package apps.threedmanipulation.tools;

import java.nio.FloatBuffer;

import synergynetframework.appsystem.contentsystem.ContentSystem;

import com.jme.bounding.BoundingSphere;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.Line;
import com.jme.util.geom.BufferUtils;


/**
 * The Class TwinObject.
 */
public class TwinObject {

	/** The twin object. */
	private Spatial twinObject;
	
	/** The world node. */
	private Node worldNode;
	
	/** The line. */
	private Line line;
	
	/** The manipulated ojbect. */
	private Spatial manipulatedOjbect;
	  
	/**
	 * Cleanup.
	 */
	public void cleanup() {	
		//remove line from scene
		worldNode.detachChild(line);
		
		//remove twin object from scene
		twinObject.setLocalTranslation(-10000, -10000, -10000);
		twinObject.setModelBound(null);
		twinObject.updateModelBound();
		
		worldNode.updateGeometricState(0f, false);			
	}
	
	/**
	 * Instantiates a new twin object.
	 *
	 * @param name the name
	 * @param contentSystem the content system
	 * @param worldNode the world node
	 * @param manipulatedOjbect the manipulated ojbect
	 * @param twinObject the twin object
	 * @param initPosition the init position
	 */
	public TwinObject(String name, ContentSystem contentSystem, Node worldNode,  Spatial manipulatedOjbect, Spatial twinObject, Vector3f initPosition){
		
		this.worldNode = worldNode;		
		this.manipulatedOjbect = manipulatedOjbect;
		this.twinObject = twinObject;
		
		//initialise twin object
		twinObject.setLocalTranslation(initPosition.x, initPosition.y, initPosition.z);
		twinObject.setModelBound(new BoundingSphere());
		twinObject.updateModelBound();
		
		//initialise line
		line =  new Line(name+"line");				
		if(twinObject != null && manipulatedOjbect != null){

			FloatBuffer vectorBuff = BufferUtils.createVector3Buffer(2);
			FloatBuffer colorBuff = BufferUtils.createFloatBuffer(new ColorRGBA[]{ColorRGBA.gray, ColorRGBA.white});
			BufferUtils.setInBuffer(twinObject.getLocalTranslation(), vectorBuff, 0);
			BufferUtils.setInBuffer(manipulatedOjbect.getLocalTranslation(), vectorBuff, 1);
			line.setLineWidth(2f);
			line.reconstruct(vectorBuff, null, colorBuff, null);
			line.updateRenderState();
			line.updateGeometricState(0f, false);
		
		}	
		this.worldNode.attachChild(line);
		worldNode.updateGeometricState(0f, false);
						
	}
	
	/**
	 * Update line.
	 */
	public void updateLine(){
				
		if(twinObject != null && manipulatedOjbect != null){
			FloatBuffer vectorBuff = BufferUtils.createVector3Buffer(2);
			FloatBuffer colorBuff = BufferUtils.createFloatBuffer(new ColorRGBA[]{ColorRGBA.gray, ColorRGBA.white});
			BufferUtils.setInBuffer(twinObject.getLocalTranslation(), vectorBuff, 0);
			BufferUtils.setInBuffer(manipulatedOjbect.getLocalTranslation(), vectorBuff, 1);
			line.setLineWidth(2f);
			line.reconstruct(vectorBuff, null, colorBuff, null);
			line.updateRenderState();
			line.updateGeometricState(0f, false);	
		}
	}
	

}
