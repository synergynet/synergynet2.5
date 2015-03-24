package apps.threedmanipulation.tools;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import apps.threedmanipulation.listener.ToolListener;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Line;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.system.DisplaySystem;
import com.jme.util.geom.BufferUtils;

/**
 * The Class TouchPad.
 */
public class TouchPad {
	
	/** The line. */
	private Line line;

	/** The manipulatabled ojbect. */
	private Spatial manipulatabledOjbect;
	
	/** The monitor screen. */
	private TouchPadScreen monitorScreen;

	/** The ortho node. */
	private Node orthoNode;

	/** The tool listeners. */
	protected List<ToolListener> toolListeners = new ArrayList<ToolListener>();

	/** The world node. */
	private Node worldNode;
	
	/**
	 * Instantiates a new touch pad.
	 *
	 * @param name
	 *            the name
	 * @param contentSystem
	 *            the content system
	 * @param worldNode
	 *            the world node
	 * @param orthoNode
	 *            the ortho node
	 * @param monitorWidth
	 *            the monitor width
	 * @param manipulatabledOjbect
	 *            the manipulatabled ojbect
	 * @param initMonitorPosition
	 *            the init monitor position
	 */
	public TouchPad(String name, ContentSystem contentSystem, Node worldNode,
			Node orthoNode, float monitorWidth, Spatial manipulatabledOjbect,
			Vector2f initMonitorPosition) {

		this.worldNode = worldNode;
		this.orthoNode = orthoNode;

		this.manipulatabledOjbect = manipulatabledOjbect;

		line = new Line(name + "line");

		Vector3f cursorWorldStart = DisplaySystem.getDisplaySystem()
				.getWorldCoordinates(
						new Vector2f(initMonitorPosition.x,
								initMonitorPosition.y), 0.9f);

		if ((cursorWorldStart != null) && (manipulatabledOjbect != null)) {
			
			FloatBuffer vectorBuff = BufferUtils.createVector3Buffer(2);
			FloatBuffer colorBuff = BufferUtils
					.createFloatBuffer(new ColorRGBA[] { ColorRGBA.gray,
							ColorRGBA.white });
			BufferUtils.setInBuffer(cursorWorldStart, vectorBuff, 0);
			BufferUtils.setInBuffer(manipulatabledOjbect.getLocalTranslation(),
					vectorBuff, 1);
			line.setLineWidth(2f);
			line.reconstruct(vectorBuff, null, colorBuff, null);
			line.updateRenderState();
			line.updateGeometricState(0f, false);
			
		}

		this.worldNode.attachChild(line);
		worldNode.updateGeometricState(0f, false);
		
		monitorScreen = new TouchPadScreen(name + "monitorScreen",
				contentSystem, monitorWidth, manipulatabledOjbect, line);
		orthoNode.attachChild(monitorScreen);
		monitorScreen.setLocalTranslation(initMonitorPosition.x,
				initMonitorPosition.y, 0);

		orthoNode.updateGeometricState(0f, false);
		orthoNode.updateRenderState();
		
	}

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
	 * Update line.
	 */
	public void updateLine() {

		Vector3f cursorWorldStart = DisplaySystem.getDisplaySystem()
				.getWorldCoordinates(
						new Vector2f(monitorScreen.getLocalTranslation().x,
								monitorScreen.getLocalTranslation().y), 0.9f);
		
		if ((cursorWorldStart != null) && (manipulatabledOjbect != null)) {
			
			FloatBuffer vectorBuff = BufferUtils.createVector3Buffer(2);
			FloatBuffer colorBuff = BufferUtils
					.createFloatBuffer(new ColorRGBA[] { ColorRGBA.gray,
							ColorRGBA.white });
			BufferUtils.setInBuffer(cursorWorldStart, vectorBuff, 0);
			BufferUtils.setInBuffer(manipulatabledOjbect.getLocalTranslation(),
					vectorBuff, 1);
			line.setLineWidth(2f);
			line.reconstruct(vectorBuff, null, colorBuff, null);
			line.updateRenderState();
			line.updateGeometricState(0f, false);
			
		}
	}
	
}
