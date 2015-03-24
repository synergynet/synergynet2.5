package apps.threedmanipulationexperiment.tools;

import java.util.ArrayList;
import java.util.List;

import synergynetframework.appsystem.contentsystem.ContentSystem;

import apps.threedmanipulation.listener.ToolListener;

import com.jme.math.Vector2f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.Line;


/**
 * The Class TouchPad.
 */
public class TouchPad {

	/** The monitor screen. */
	private TouchPadScreen monitorScreen;
	
	/** The tool listeners. */
	protected List<ToolListener> toolListeners = new ArrayList<ToolListener>();  
	
	/** The world node. */
	private Node worldNode;
	
	/** The ortho node. */
	private Node orthoNode;
	
	/** The line. */
	private Line line;
	  
	/**
	 * Cleanup.
	 */
	public void cleanup() {
		orthoNode.detachChild(monitorScreen);
		orthoNode.updateGeometricState(0f, false);
		
		worldNode.detachChild(line);
		worldNode.updateGeometricState(0f, false);
	}
	
	/**
	 * Instantiates a new touch pad.
	 *
	 * @param name the name
	 * @param contentSystem the content system
	 * @param worldNode the world node
	 * @param orthoNode the ortho node
	 * @param monitorWidth the monitor width
	 * @param manipulatabledOjbect the manipulatabled ojbect
	 * @param initMonitorPosition the init monitor position
	 */
	public TouchPad(String name, ContentSystem contentSystem, Node worldNode, Node orthoNode, float monitorWidth, Spatial manipulatabledOjbect, Vector2f initMonitorPosition){
		
		this.worldNode = worldNode;
		this.orthoNode = orthoNode;
		
		
		/*
		line =  new Line(name+"line");
			
		Vector3f cursorWorldStart = DisplaySystem.getDisplaySystem().getWorldCoordinates(new Vector2f(initMonitorPosition.x, initMonitorPosition.y), 0.9f);
		
		if(cursorWorldStart != null && manipulatabledOjbect != null){

			FloatBuffer vectorBuff = BufferUtils.createVector3Buffer(2);
			FloatBuffer colorBuff = BufferUtils.createFloatBuffer(new ColorRGBA[]{ColorRGBA.gray, ColorRGBA.white});
			BufferUtils.setInBuffer(cursorWorldStart, vectorBuff, 0);
			BufferUtils.setInBuffer(manipulatabledOjbect.getLocalTranslation(), vectorBuff, 1);
			line.setLineWidth(1f);
			line.reconstruct(vectorBuff, null, colorBuff, null);
			line.updateRenderState();
			line.updateGeometricState(0f, false);
		
		}
		
		this.worldNode.attachChild(line);
		worldNode.updateGeometricState(0f, false);
		*/
		 
		monitorScreen = new TouchPadScreen(name+"monitorScreen", contentSystem, monitorWidth, manipulatabledOjbect, line);
		orthoNode.attachChild(monitorScreen);
		monitorScreen.setLocalTranslation(initMonitorPosition.x, initMonitorPosition.y, 0);
		
		orthoNode.updateGeometricState(0f, false);
		orthoNode.updateRenderState();
				
	}
	
	/**
	 * Sets the rotation speed.
	 *
	 * @param rotationSpeed the new rotation speed
	 */
	public void setRotationSpeed(int rotationSpeed){
		monitorScreen.setRotationSpeed(rotationSpeed);
	}
	
	/**
	 * Reset touch number.
	 */
	public void resetTouchNumber(){
		monitorScreen.resetTouchNumber();
	}
	
	/**
	 * Update line.
	 */
	public void updateLine(){
		
		/*
		Vector3f cursorWorldStart = DisplaySystem.getDisplaySystem().getWorldCoordinates(new Vector2f(monitorScreen.getLocalTranslation().x, monitorScreen.getLocalTranslation().y), 0.9f);
	
		if(cursorWorldStart != null && manipulatabledOjbect != null){

			FloatBuffer vectorBuff = BufferUtils.createVector3Buffer(2);
			FloatBuffer colorBuff = BufferUtils.createFloatBuffer(new ColorRGBA[]{ColorRGBA.gray, ColorRGBA.white});
			BufferUtils.setInBuffer(cursorWorldStart, vectorBuff, 0);
			BufferUtils.setInBuffer(manipulatabledOjbect.getLocalTranslation(), vectorBuff, 1);
			line.setLineWidth(1f);
			line.reconstruct(vectorBuff, null, colorBuff, null);
			line.updateRenderState();
			line.updateGeometricState(0f, false);
		
		}
		*/
	}
	
	/**
	 * Sets the location.
	 *
	 * @param location the new location
	 */
	public void setLocation(Vector2f location){
		monitorScreen.setLocalTranslation(location.x, location.y, 0);
		orthoNode.updateGeometricState(0f, false);
	}
	
	/**
	 * Gets the touch pad.
	 *
	 * @return the touch pad
	 */
	public Spatial getTouchPad(){
		return monitorScreen;
	}
	
}
