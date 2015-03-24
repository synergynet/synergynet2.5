package apps.threedmanipulation.utils;

import java.util.List;
import java.util.Map;

import synergynetframework.appsystem.contentsystem.ContentSystem;

import apps.threedmanipulation.tools.Monitor;
import apps.threedmanipulation.tools.Telescope;
import apps.threedmanipulation.tools.TouchPad;
import apps.threedmanipulation.tools.TwinObject;

import com.jme.scene.Node;
import com.jme.scene.Spatial;


/**
 * The Class Context.
 */
public class Context {

	/** The telescopes. */
	protected List<Telescope> telescopes;
	
	/** The monitors. */
	protected List<Monitor> monitors;
	
	/** The manipulatable ojbects. */
	protected List<Spatial> manipulatableOjbects;
	
	/** The touch pads. */
	protected Map<String, TouchPad> touchPads;
	
	/** The twin objects. */
	protected Map<String, TwinObject> twinObjects;		
	
	/** The content system. */
	protected ContentSystem contentSystem;		
	
	/** The indirect manipulation mode. */
	protected String indirectManipulationMode = "";
	
	/** The world node. */
	protected Node worldNode;
	
	/** The ortho node. */
	protected Node orthoNode;

	/**
	 * Instantiates a new context.
	 *
	 * @param telescopes the telescopes
	 * @param monitors the monitors
	 * @param manipulatableOjbects the manipulatable ojbects
	 * @param touchPads the touch pads
	 * @param twinObjects the twin objects
	 * @param contentSystem the content system
	 * @param indirectManipulationMode the indirect manipulation mode
	 * @param worldNode the world node
	 * @param orthoNode the ortho node
	 */
	public Context(List<Telescope> telescopes, List<Monitor> monitors,
			List<Spatial> manipulatableOjbects,
			Map<String, TouchPad> touchPads,
			Map<String, TwinObject> twinObjects, ContentSystem contentSystem,
			String indirectManipulationMode, Node worldNode, Node orthoNode) {
		super();
		this.telescopes = telescopes;
		this.monitors = monitors;
		this.manipulatableOjbects = manipulatableOjbects;
		this.touchPads = touchPads;
		this.twinObjects = twinObjects;
		this.contentSystem = contentSystem;
		this.indirectManipulationMode = indirectManipulationMode;
		this.worldNode = worldNode;
		this.orthoNode = orthoNode;
	}

	/**
	 * Gets the telescopes.
	 *
	 * @return the telescopes
	 */
	public List<Telescope> getTelescopes() {
		return telescopes;
	}

	/**
	 * Sets the telescopes.
	 *
	 * @param telescopes the new telescopes
	 */
	public void setTelescopes(List<Telescope> telescopes) {
		this.telescopes = telescopes;
	}

	/**
	 * Gets the monitors.
	 *
	 * @return the monitors
	 */
	public List<Monitor> getMonitors() {
		return monitors;
	}

	/**
	 * Sets the monitors.
	 *
	 * @param monitors the new monitors
	 */
	public void setMonitors(List<Monitor> monitors) {
		this.monitors = monitors;
	}

	/**
	 * Gets the manipulatable ojbects.
	 *
	 * @return the manipulatable ojbects
	 */
	public List<Spatial> getManipulatableOjbects() {
		return manipulatableOjbects;
	}

	/**
	 * Sets the manipulatable ojbects.
	 *
	 * @param manipulatableOjbects the new manipulatable ojbects
	 */
	public void setManipulatableOjbects(List<Spatial> manipulatableOjbects) {
		this.manipulatableOjbects = manipulatableOjbects;
	}

	/**
	 * Gets the touch pads.
	 *
	 * @return the touch pads
	 */
	public Map<String, TouchPad> getTouchPads() {
		return touchPads;
	}

	/**
	 * Sets the touch pads.
	 *
	 * @param touchPads the touch pads
	 */
	public void setTouchPads(Map<String, TouchPad> touchPads) {
		this.touchPads = touchPads;
	}

	/**
	 * Gets the twin objects.
	 *
	 * @return the twin objects
	 */
	public Map<String, TwinObject> getTwinObjects() {
		return twinObjects;
	}

	/**
	 * Sets the twin objects.
	 *
	 * @param twinObjects the twin objects
	 */
	public void setTwinObjects(Map<String, TwinObject> twinObjects) {
		this.twinObjects = twinObjects;
	}

	/**
	 * Gets the content system.
	 *
	 * @return the content system
	 */
	public ContentSystem getContentSystem() {
		return contentSystem;
	}

	/**
	 * Sets the content system.
	 *
	 * @param contentSystem the new content system
	 */
	public void setContentSystem(ContentSystem contentSystem) {
		this.contentSystem = contentSystem;
	}

	/**
	 * Gets the indirect manipulation mode.
	 *
	 * @return the indirect manipulation mode
	 */
	public String getIndirectManipulationMode() {
		return indirectManipulationMode;
	}

	/**
	 * Sets the indirect manipulation mode.
	 *
	 * @param indirectManipulationMode the new indirect manipulation mode
	 */
	public void setIndirectManipulationMode(String indirectManipulationMode) {
		this.indirectManipulationMode = indirectManipulationMode;
	}

	/**
	 * Gets the world node.
	 *
	 * @return the world node
	 */
	public Node getWorldNode() {
		return worldNode;
	}

	/**
	 * Sets the world node.
	 *
	 * @param worldNode the new world node
	 */
	public void setWorldNode(Node worldNode) {
		this.worldNode = worldNode;
	}

	/**
	 * Gets the ortho node.
	 *
	 * @return the ortho node
	 */
	public Node getOrthoNode() {
		return orthoNode;
	}

	/**
	 * Sets the ortho node.
	 *
	 * @param orthoNode the new ortho node
	 */
	public void setOrthoNode(Node orthoNode) {
		this.orthoNode = orthoNode;
	}
		
}
