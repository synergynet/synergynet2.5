package apps.threedmanipulation.tools;

import java.util.ArrayList;
import java.util.List;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.jme.cursorsystem.elements.twod.OrthoBringToTop;
import apps.threedmanipulation.ThreeDManipulation;
import apps.threedmanipulation.gestures.OjbectManipulation;
import apps.threedmanipulation.gestures.TelescopeFrameMoveZoom;
import apps.threedmanipulation.listener.ToolListener;

import com.jme.bounding.OrthogonalBoundingBox;
import com.jme.image.Texture;
import com.jme.image.Texture.ApplyMode;
import com.jme.image.Texture.WrapMode;
import com.jme.scene.CameraNode;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Disk;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

/**
 * The Class TelescopeLen.
 */
public class TelescopeLen extends Node {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5768509228421748746L;

	/** The cam node. */
	protected CameraNode camNode;

	/** The content system. */
	protected ContentSystem contentSystem;

	/** The screen disk. */
	protected Disk screenDisk;

	/** The screen frame. */
	protected Disk screenFrame;

	/** The telescope radius. */
	protected float telescopeRadius;

	/** The tool listeners. */
	protected List<ToolListener> toolListeners = new ArrayList<ToolListener>();

	/**
	 * Instantiates a new telescope len.
	 *
	 * @param name
	 *            the name
	 * @param contentSystem
	 *            the content system
	 * @param telescopeRadius
	 *            the telescope radius
	 * @param camNode
	 *            the cam node
	 * @param manipulatableOjbects
	 *            the manipulatable ojbects
	 */
	public TelescopeLen(String name, ContentSystem contentSystem,
			float telescopeRadius, final CameraNode camNode,
			List<Spatial> manipulatableOjbects) {
		super(name);
		this.contentSystem = contentSystem;
		this.telescopeRadius = telescopeRadius;
		this.camNode = camNode;
		
		screenDisk = new Disk(name + "screenDisk", 16, 32, telescopeRadius);
		screenDisk.setModelBound(new OrthogonalBoundingBox());
		screenDisk.updateModelBound();
		this.attachChild(screenDisk);

		OjbectManipulation telescopeManipulateOjbect = new OjbectManipulation(
				screenDisk, new ArrayList<Spatial>());
		telescopeManipulateOjbect.setPickMeOnly(true);
		
		screenFrame = new Disk(name + "screenFrame", 16, 32,
				telescopeRadius + 30);
		screenFrame.setModelBound(new OrthogonalBoundingBox());
		screenFrame.updateModelBound();
		this.attachChild(screenFrame);

		TextureState ts;
		Texture texture;
		ts = DisplaySystem.getDisplaySystem().getRenderer()
				.createTextureState();
		ts.setCorrectionType(TextureState.CorrectionType.Perspective);
		texture = TextureManager.loadTexture(
				ThreeDManipulation.class.getResource("telescope1.png"),
				Texture.MinificationFilter.Trilinear,
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

		TelescopeFrameMoveZoom telescopeFrameMoveZoom = new TelescopeFrameMoveZoom(
				screenFrame, this, camNode, telescopeManipulateOjbect,
				manipulatableOjbects);
		telescopeFrameMoveZoom.setPickMeOnly(true);
		telescopeFrameMoveZoom.addToolListener(new ToolListener() {
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
	 * Adds the tool listener.
	 *
	 * @param l
	 *            the l
	 */
	public void addToolListener(ToolListener l) {
		toolListeners.add(l);
	}
	
	/**
	 * Gets the screen disk.
	 *
	 * @return the screen disk
	 */
	public Disk getScreenDisk() {
		return screenDisk;
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

}
