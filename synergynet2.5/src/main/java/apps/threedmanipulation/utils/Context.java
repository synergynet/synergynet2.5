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

public class Context {

	protected List<Telescope> telescopes;
	protected List<Monitor> monitors;
	protected List<Spatial> manipulatableOjbects;
	protected Map<String, TouchPad> touchPads;
	protected Map<String, TwinObject> twinObjects;		
	protected ContentSystem contentSystem;		
	protected String indirectManipulationMode = "";
	protected Node worldNode;
	protected Node orthoNode;

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

	public List<Telescope> getTelescopes() {
		return telescopes;
	}

	public void setTelescopes(List<Telescope> telescopes) {
		this.telescopes = telescopes;
	}

	public List<Monitor> getMonitors() {
		return monitors;
	}

	public void setMonitors(List<Monitor> monitors) {
		this.monitors = monitors;
	}

	public List<Spatial> getManipulatableOjbects() {
		return manipulatableOjbects;
	}

	public void setManipulatableOjbects(List<Spatial> manipulatableOjbects) {
		this.manipulatableOjbects = manipulatableOjbects;
	}

	public Map<String, TouchPad> getTouchPads() {
		return touchPads;
	}

	public void setTouchPads(Map<String, TouchPad> touchPads) {
		this.touchPads = touchPads;
	}

	public Map<String, TwinObject> getTwinObjects() {
		return twinObjects;
	}

	public void setTwinObjects(Map<String, TwinObject> twinObjects) {
		this.twinObjects = twinObjects;
	}

	public ContentSystem getContentSystem() {
		return contentSystem;
	}

	public void setContentSystem(ContentSystem contentSystem) {
		this.contentSystem = contentSystem;
	}

	public String getIndirectManipulationMode() {
		return indirectManipulationMode;
	}

	public void setIndirectManipulationMode(String indirectManipulationMode) {
		this.indirectManipulationMode = indirectManipulationMode;
	}

	public Node getWorldNode() {
		return worldNode;
	}

	public void setWorldNode(Node worldNode) {
		this.worldNode = worldNode;
	}

	public Node getOrthoNode() {
		return orthoNode;
	}

	public void setOrthoNode(Node orthoNode) {
		this.orthoNode = orthoNode;
	}
		
}
