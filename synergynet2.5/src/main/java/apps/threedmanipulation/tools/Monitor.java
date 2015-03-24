package apps.threedmanipulation.tools;

import java.util.ArrayList;
import java.util.List;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import apps.threedmanipulation.gestures.MonitorCameraRotateTranslateZoom;
import apps.threedmanipulation.listener.ToolListener;
import apps.threedmanipulation.utils.CameraModel;

import com.jme.bounding.BoundingSphere;
import com.jme.image.Texture;
import com.jme.image.Texture2D;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.TextureRenderer;
import com.jme.scene.CameraNode;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;

/**
 * The Class Monitor.
 */
public class Monitor {
	
	/** The camera model. */
	private CameraModel cameraModel;
	
	/** The cam node. */
	private CameraNode camNode;

	/** The fake tex. */
	private Texture2D fakeTex;

	/** The monitor screen. */
	private MonitorScreen monitorScreen;

	/** The ortho node. */
	private Node orthoNode;
	
	/** The tool listeners. */
	protected List<ToolListener> toolListeners = new ArrayList<ToolListener>();

	/** The t renderer. */
	private TextureRenderer tRenderer;

	/** The world node. */
	private Node worldNode;
	
	/**
	 * Instantiates a new monitor.
	 *
	 * @param name
	 *            the name
	 * @param contentSystem
	 *            the content system
	 * @param worldNode
	 *            the world node
	 * @param orthoNode
	 *            the ortho node
	 * @param manipulatableOjbects
	 *            the manipulatable ojbects
	 * @param mainCameraPosition
	 *            the main camera position
	 * @param initMonitorPosition
	 *            the init monitor position
	 * @param initCameraZoom
	 *            the init camera zoom
	 * @param monitorWidth
	 *            the monitor width
	 * @param skinColor
	 *            the skin color
	 * @param mode
	 *            the mode
	 */
	public Monitor(String name, ContentSystem contentSystem, Node worldNode,
			Node orthoNode, List<Spatial> manipulatableOjbects,
			Vector3f mainCameraPosition, Vector2f initMonitorPosition,
			float initCameraZoom, float monitorWidth, String skinColor,
			String mode) {

		this.worldNode = worldNode;
		this.orthoNode = orthoNode;

		tRenderer = DisplaySystem.getDisplaySystem().createTextureRenderer(256,
				256, TextureRenderer.Target.Texture2D);
		camNode = new CameraNode(name + "Camera Node", tRenderer.getCamera());
		tRenderer.getCamera().lookAt(new Vector3f(), new Vector3f(0, 1, 0));
		tRenderer.getCamera().update();

		camNode.setLocalTranslation(mainCameraPosition);
		camNode.getCamera().setFrustumNear(initCameraZoom);
		camNode.updateGeometricState(0, true);
		camNode.updateRenderState();
		
		cameraModel = new CameraModel(name + "camera",
				"data/threedmanipulation/camera" + skinColor + ".png");
		cameraModel.setLocalTranslation(new Vector3f(0f, 0f, -6f));
		cameraModel.setModelBound(new BoundingSphere());
		cameraModel.updateModelBound();
		cameraModel.setLocalScale(0.5f);
		camNode.attachChild(cameraModel);
		
		worldNode.attachChild(camNode);
		
		monitorScreen = new MonitorScreen(name + "monitorScreen",
				contentSystem, monitorWidth, camNode, manipulatableOjbects,
				skinColor, mode);
		orthoNode.attachChild(monitorScreen);
		monitorScreen.setLocalTranslation(initMonitorPosition.x,
				initMonitorPosition.y, 0);
		monitorScreen.addToolListener(new ToolListener() {
			@Override
			public void disposeTool(float x, float y) {
				for (ToolListener l : toolListeners) {
					l.disposeTool(x, y);
				}
			}
		});

		MonitorCameraRotateTranslateZoom cprts1 = new MonitorCameraRotateTranslateZoom(
				cameraModel.getCameraBody(), camNode,
				this.monitorScreen.getFocusedObject(), manipulatableOjbects);
		cprts1.setPickMeOnly(true);
		cprts1.setMode(mode);

		tRenderer.setBackgroundColor(new ColorRGBA(1f, 0f, 0f, 1f));
		fakeTex = new Texture2D();
		fakeTex.setRenderToTextureType(Texture.RenderToTextureType.RGBA);
		tRenderer.setupTexture(fakeTex);
		TextureState screen = DisplaySystem.getDisplaySystem().getRenderer()
				.createTextureState();
		screen.setTexture(fakeTex);
		screen.setEnabled(true);
		
		monitorScreen.getScreenQuad().setRenderState(screen);
		
		camNode.setLocalTranslation(15, 10, 90);
		camNode.lookAt(new Vector3f(15, 0, 0), new Vector3f(0, 1, 0));
		
		orthoNode.updateGeometricState(0f, false);
		orthoNode.updateRenderState();
		
	}
	
	/**
	 * Adds the tool listener.
	 *
	 * @param l
	 *            the l
	 */
	public void addToolListener(ToolListener l) {
		toolListeners.add(l);
	}

	/**
	 * Cleanup.
	 */
	public void cleanup() {
		tRenderer.cleanup();
		worldNode.detachChild(camNode);
		worldNode.updateGeometricState(0f, false);
		orthoNode.detachChild(monitorScreen);
		orthoNode.updateGeometricState(0f, false);
	}

	/**
	 * Gets the camera model.
	 *
	 * @return the camera model
	 */
	public CameraModel getCameraModel() {
		return cameraModel;
	}
	
	/**
	 * Gets the monitor screen.
	 *
	 * @return the monitor screen
	 */
	public MonitorScreen getMonitorScreen() {
		return this.monitorScreen;
	}

	/**
	 * Removes the tool listener.
	 *
	 * @param l
	 *            the l
	 */
	public void removeToolListener(ToolListener l) {
		if (toolListeners.contains(l)) {
			toolListeners.remove(l);
		}
	}

	/**
	 * Render.
	 *
	 * @param renderedNode
	 *            the rendered node
	 */
	public void render(Node renderedNode) {
		tRenderer.render(renderedNode, fakeTex);
	}

	/**
	 * Sets the mode.
	 *
	 * @param mode
	 *            the new mode
	 */
	public void setMode(String mode) {
		monitorScreen.setMode(mode);
	}

	/**
	 * Update.
	 *
	 * @param tpf
	 *            the tpf
	 */
	public void update(float tpf) {
		monitorScreen.update(tpf);
	}
	
}
