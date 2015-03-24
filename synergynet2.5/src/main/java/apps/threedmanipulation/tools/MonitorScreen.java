package apps.threedmanipulation.tools;

import java.util.ArrayList;
import java.util.List;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.RoundImageLabel;
import synergynetframework.appsystem.contentsystem.items.listener.ItemEventAdapter;
import synergynetframework.jme.cursorsystem.elements.twod.OrthoBringToTop;
import apps.threedmanipulation.ThreeDManipulation;
import apps.threedmanipulation.gestures.ControlPanelMoveRotateScale;
import apps.threedmanipulation.gestures.MonitorCameraRotateTranslateZoom;
import apps.threedmanipulation.gestures.OjbectManipulation;
import apps.threedmanipulation.gestures.OjbectManipulationforCCTV;
import apps.threedmanipulation.listener.ToolListener;

import com.jme.bounding.OrthogonalBoundingBox;
import com.jme.image.Texture;
import com.jme.image.Texture.ApplyMode;
import com.jme.image.Texture.WrapMode;
import com.jme.scene.CameraNode;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Disk;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

/**
 * The Class MonitorScreen.
 */
public class MonitorScreen extends Node {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5768509228111148746L;

	/** The camera operation mode. */
	protected String cameraOperationMode = MonitorCameraRotateTranslateZoom.MODE_REMOTECONTROL;

	/** The cam node. */
	protected CameraNode camNode;

	/** The content system. */
	protected ContentSystem contentSystem;

	/** The manipulatable ojbects. */
	protected List<Spatial> manipulatableOjbects;

	/** The mode. */
	protected String mode = OjbectManipulation.MODE_OBJECTMANIPULATION;

	/** The move in. */
	private boolean moveIn = false;

	/** The move out. */
	private boolean moveOut = false;

	/** The screen frame. */
	protected Quad screenFrame;

	/** The screen quad. */
	protected Quad screenQuad;

	/** The skin color. */
	protected String skinColor = "blue";

	/** The telescope manipulate ojbect. */
	protected OjbectManipulationforCCTV telescopeManipulateOjbect;

	/** The tool listeners. */
	protected List<ToolListener> toolListeners = new ArrayList<ToolListener>();

	/** The width. */
	protected float width;

	/** The zoom in. */
	private boolean zoomIn = false;

	/** The zoom out. */
	private boolean zoomOut = false;

	/**
	 * Instantiates a new monitor screen.
	 *
	 * @param name
	 *            the name
	 * @param contentSystem
	 *            the content system
	 * @param width
	 *            the width
	 * @param camNode
	 *            the cam node
	 * @param manipulatableOjbects
	 *            the manipulatable ojbects
	 * @param skinColor
	 *            the skin color
	 * @param cameraOperationMode
	 *            the camera operation mode
	 */
	public MonitorScreen(String name, ContentSystem contentSystem, float width,
			final CameraNode camNode, List<Spatial> manipulatableOjbects,
			String skinColor, String cameraOperationMode) {
		super(name);
		this.contentSystem = contentSystem;
		this.width = width;
		this.camNode = camNode;
		this.manipulatableOjbects = manipulatableOjbects;
		this.skinColor = skinColor;
		this.cameraOperationMode = cameraOperationMode;
		if (this.cameraOperationMode
				.equals(MonitorCameraRotateTranslateZoom.MODE_REMOTECONTROL)) {
			mode = OjbectManipulation.MODE_CAMERAMANIPULATION;
			
		} else {
			mode = OjbectManipulation.MODE_OBJECTMANIPULATION;
			
		}
		
		buildScreenQuad();
		buildbuttons();
		buildScreenFrame();
		
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
	 * Buildbuttons.
	 */
	public void buildbuttons() {
		final RoundImageLabel zoomInLabel = (RoundImageLabel) contentSystem
				.createContentItem(RoundImageLabel.class);
		zoomInLabel.setImageInfo(ThreeDManipulation.class
				.getResource("zoominbuttonnormal.png"));
		zoomInLabel.addItemListener(new ItemEventAdapter() {
			@Override
			public void cursorPressed(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorPressed(item, id, x, y, pressure);
				zoomInLabel.setImageInfo(ThreeDManipulation.class
						.getResource("zoominbutton.png"));
				zoomIn = true;
			}
			
			@Override
			public void cursorReleased(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorReleased(item, id, x, y, pressure);
				zoomInLabel.setImageInfo(ThreeDManipulation.class
						.getResource("zoominbuttonnormal.png"));
				zoomIn = false;
			}

		});

		Disk disk = (Disk) (zoomInLabel.getImplementationObject());
		BlendState alpha = DisplaySystem.getDisplaySystem().getRenderer()
				.createBlendState();
		alpha.setEnabled(true);
		alpha.setBlendEnabled(true);
		alpha.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		alpha.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
		alpha.setTestEnabled(true);
		alpha.setTestFunction(BlendState.TestFunction.GreaterThan);
		disk.setRenderState(alpha);

		this.attachChild(disk);

		zoomInLabel.setRotateTranslateScalable(false);
		zoomInLabel.setBringToTopable(false);
		zoomInLabel.setLocalLocation(100, 70, 0);
		zoomInLabel.setBorderSize(0);
		zoomInLabel.setRadius(15);

		final RoundImageLabel zoomOutLabel = (RoundImageLabel) contentSystem
				.createContentItem(RoundImageLabel.class);
		zoomOutLabel.setImageInfo(ThreeDManipulation.class
				.getResource("zoomoutbuttonnormal.png"));
		zoomOutLabel.addItemListener(new ItemEventAdapter() {
			@Override
			public void cursorPressed(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorPressed(item, id, x, y, pressure);
				zoomOutLabel.setImageInfo(ThreeDManipulation.class
						.getResource("zoomoutbutton.png"));
				zoomOut = true;
			}
			
			@Override
			public void cursorReleased(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorReleased(item, id, x, y, pressure);
				zoomOutLabel.setImageInfo(ThreeDManipulation.class
						.getResource("zoomoutbuttonnormal.png"));
				zoomOut = false;
			}

		});

		disk = (Disk) (zoomOutLabel.getImplementationObject());
		disk.setRenderState(alpha);

		this.attachChild(disk);

		zoomOutLabel.setRotateTranslateScalable(false);
		zoomOutLabel.setBringToTopable(false);
		zoomOutLabel.setLocalLocation(100, 30, 0);
		zoomOutLabel.setBorderSize(0);
		zoomOutLabel.setRadius(15);

		final RoundImageLabel zInLabel = (RoundImageLabel) contentSystem
				.createContentItem(RoundImageLabel.class);
		zInLabel.setImageInfo(ThreeDManipulation.class
				.getResource("zinbuttonnormal.png"));
		zInLabel.addItemListener(new ItemEventAdapter() {
			@Override
			public void cursorPressed(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorPressed(item, id, x, y, pressure);
				zInLabel.setImageInfo(ThreeDManipulation.class
						.getResource("zinbutton.png"));
				moveIn = true;
			}
			
			@Override
			public void cursorReleased(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorReleased(item, id, x, y, pressure);
				zInLabel.setImageInfo(ThreeDManipulation.class
						.getResource("zinbuttonnormal.png"));
				moveIn = false;
			}

		});

		disk = (Disk) (zInLabel.getImplementationObject());
		disk.setRenderState(alpha);

		this.attachChild(disk);

		zInLabel.setRotateTranslateScalable(false);
		zInLabel.setBringToTopable(false);
		zInLabel.setLocalLocation(100, -10, 0);
		zInLabel.setBorderSize(0);
		zInLabel.setRadius(15);
		
		final RoundImageLabel zOutLabel = (RoundImageLabel) contentSystem
				.createContentItem(RoundImageLabel.class);
		zOutLabel.setImageInfo(ThreeDManipulation.class
				.getResource("zoutbuttonnormal.png"));
		zOutLabel.addItemListener(new ItemEventAdapter() {
			@Override
			public void cursorPressed(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorPressed(item, id, x, y, pressure);
				zOutLabel.setImageInfo(ThreeDManipulation.class
						.getResource("zioutbutton.png"));
				moveOut = true;
			}
			
			@Override
			public void cursorReleased(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorReleased(item, id, x, y, pressure);
				zOutLabel.setImageInfo(ThreeDManipulation.class
						.getResource("zoutbuttonnormal.png"));
				moveOut = false;
			}

		});

		disk = (Disk) (zOutLabel.getImplementationObject());
		disk.setRenderState(alpha);

		this.attachChild(disk);

		zOutLabel.setRotateTranslateScalable(false);
		zOutLabel.setBringToTopable(false);
		zOutLabel.setLocalLocation(100, -50, 0);
		zOutLabel.setBorderSize(0);
		zOutLabel.setRadius(15);
		
		final RoundImageLabel modeLabel = (RoundImageLabel) contentSystem
				.createContentItem(RoundImageLabel.class);
		modeLabel.setImageInfo(ThreeDManipulation.class
				.getResource("manipulationbutton.png"));
		modeLabel.addItemListener(new ItemEventAdapter() {
			@Override
			public void cursorClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorClicked(item, id, x, y, pressure);

				if (mode.equals(OjbectManipulation.MODE_CAMERAMANIPULATION)) {
					mode = OjbectManipulation.MODE_OBJECTMANIPULATION;
					telescopeManipulateOjbect
							.setMode(OjbectManipulation.MODE_OBJECTMANIPULATION);
					modeLabel.setImageInfo(ThreeDManipulation.class
							.getResource("manipulationbuttonnormal.png"));
				} else {
					mode = OjbectManipulation.MODE_CAMERAMANIPULATION;
					telescopeManipulateOjbect
							.setMode(OjbectManipulation.MODE_CAMERAMANIPULATION);
					modeLabel.setImageInfo(ThreeDManipulation.class
							.getResource("manipulationbutton.png"));
				}

			}

		});

		disk = (Disk) (modeLabel.getImplementationObject());
		disk.setRenderState(alpha);

		this.attachChild(disk);

		modeLabel.setRotateTranslateScalable(false);
		modeLabel.setBringToTopable(false);
		modeLabel.setLocalLocation(100, -90, 0);
		modeLabel.setBorderSize(0);
		modeLabel.setRadius(15);

		if (!this.cameraOperationMode
				.equals(MonitorCameraRotateTranslateZoom.MODE_REMOTECONTROL)) {
			modeLabel.setVisible(false);
		}
	}

	/**
	 * Builds the screen frame.
	 */
	public void buildScreenFrame() {
		screenFrame = new Quad(name + "screenFrame", width + 50, width + 35);
		screenFrame.setModelBound(new OrthogonalBoundingBox());
		screenFrame.updateModelBound();
		this.attachChild(screenFrame);

		screenQuad.setLocalTranslation(-20, -10, 0);

		TextureState ts;
		Texture texture;
		ts = DisplaySystem.getDisplaySystem().getRenderer()
				.createTextureState();
		ts.setCorrectionType(TextureState.CorrectionType.Perspective);
		texture = TextureManager.loadTexture(
				ThreeDManipulation.class.getResource("camerascreen" + skinColor
						+ ".png"), Texture.MinificationFilter.Trilinear,
				Texture.MagnificationFilter.Bilinear);
		texture.setWrap(WrapMode.Repeat);
		texture.setApply(ApplyMode.Replace);
		ts.setTexture(texture);
		ts.apply();

		screenFrame.setRenderState(ts);
		screenFrame.updateRenderState();

		BlendState alpha = DisplaySystem.getDisplaySystem().getRenderer()
				.createBlendState();
		alpha.setEnabled(true);
		alpha.setBlendEnabled(true);
		alpha.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		alpha.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
		alpha.setTestEnabled(true);
		alpha.setTestFunction(BlendState.TestFunction.GreaterThan);
		screenFrame.setRenderState(alpha);
		
		screenFrame.updateRenderState();
		
		ControlPanelMoveRotateScale monitorScreenMoveRotateScale = new ControlPanelMoveRotateScale(
				screenFrame, this, camNode, telescopeManipulateOjbect,
				manipulatableOjbects);
		monitorScreenMoveRotateScale.setPickMeOnly(true);
		monitorScreenMoveRotateScale.addToolListener(new ToolListener() {
			@Override
			public void disposeTool(float x, float y) {
				for (ToolListener l : toolListeners) {
					l.disposeTool(x, y);
				}
			}
		});
		@SuppressWarnings("unused")
		OrthoBringToTop bringToTop = new OrthoBringToTop(screenFrame, this);
	}

	/**
	 * Builds the screen quad.
	 */
	public void buildScreenQuad() {
		screenQuad = new Quad(name + "screenQuad", width, width);
		screenQuad.setModelBound(new OrthogonalBoundingBox());
		screenQuad.updateModelBound();
		this.attachChild(screenQuad);

		telescopeManipulateOjbect = new OjbectManipulationforCCTV(screenQuad,
				manipulatableOjbects);
		telescopeManipulateOjbect.setPickMeOnly(true);
		telescopeManipulateOjbect.setCamNode(camNode);

		if (this.cameraOperationMode
				.equals(MonitorCameraRotateTranslateZoom.MODE_REMOTECONTROL)) {
			telescopeManipulateOjbect
					.setMode(OjbectManipulation.MODE_CAMERAMANIPULATION);
		} else {
			telescopeManipulateOjbect
					.setMode(OjbectManipulation.MODE_OBJECTMANIPULATION);
		}
	}
	
	/**
	 * Gets the focused object.
	 *
	 * @return the focused object
	 */
	public OjbectManipulation getFocusedObject() {
		return telescopeManipulateOjbect;
	}

	/**
	 * Gets the screen quad.
	 *
	 * @return the screen quad
	 */
	public Quad getScreenQuad() {
		return screenQuad;
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
	 * Sets the mode.
	 *
	 * @param mode
	 *            the new mode
	 */
	public void setMode(String mode) {
		if (this.cameraOperationMode
				.equals(MonitorCameraRotateTranslateZoom.MODE_REMOTECONTROL)) {
			mode = OjbectManipulation.MODE_CAMERAMANIPULATION;
			
		} else {
			mode = OjbectManipulation.MODE_OBJECTMANIPULATION;
			
		}
	}

	/**
	 * Update.
	 *
	 * @param tpf
	 *            the tpf
	 */
	public void update(float tpf) {
		if (zoomIn) {
			if (camNode.getCamera().getFrustumNear() < 30) {
				camNode.getCamera().setFrustumNear(
						camNode.getCamera().getFrustumNear() + (tpf * 4));
			}
		}

		if (zoomOut) {
			if (camNode.getCamera().getFrustumNear() > 2) {
				camNode.getCamera().setFrustumNear(
						camNode.getCamera().getFrustumNear() - (tpf * 4));
			}
		}

		if (moveIn) {
			if (camNode.getLocalTranslation().z > 50) {
				camNode.setLocalTranslation(camNode.getLocalTranslation().x,
						camNode.getLocalTranslation().y,
						camNode.getLocalTranslation().z - (tpf * 4));
			}
		}

		if (moveOut) {
			if (camNode.getLocalTranslation().z < 160) {
				camNode.setLocalTranslation(camNode.getLocalTranslation().x,
						camNode.getLocalTranslation().y,
						camNode.getLocalTranslation().z + (tpf * 4));
			}
		}
	}

}
