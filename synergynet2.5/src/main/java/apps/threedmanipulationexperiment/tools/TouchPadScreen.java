package apps.threedmanipulationexperiment.tools;

import java.util.ArrayList;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.jme.cursorsystem.elements.twod.OrthoBringToTop;
import apps.threedmanipulation.ThreeDManipulation;
import apps.threedmanipulationexperiment.gestures.ControlPanelMoveRotateScale;
import apps.threedmanipulationexperiment.gestures.OjbectManipulation;

import com.jme.bounding.OrthogonalBoundingBox;
import com.jme.image.Texture;
import com.jme.image.Texture.ApplyMode;
import com.jme.image.Texture.WrapMode;
import com.jme.scene.Line;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

/**
 * The Class TouchPadScreen.
 */
public class TouchPadScreen extends Node {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5768509222221148746L;

	/** The content system. */
	protected ContentSystem contentSystem;

	/** The line. */
	protected Line line;

	/** The manipulated ojbect. */
	protected Spatial manipulatedOjbect;

	/** The rotation speed. */
	protected int rotationSpeed;

	/** The screen frame. */
	protected Quad screenFrame;

	/** The screen quad. */
	protected Quad screenQuad;

	/** The telescope manipulate ojbect. */
	protected OjbectManipulation telescopeManipulateOjbect;

	/** The width. */
	protected float width;
	
	/**
	 * Instantiates a new touch pad screen.
	 *
	 * @param name
	 *            the name
	 * @param contentSystem
	 *            the content system
	 * @param width
	 *            the width
	 * @param manipulatabledOjbect
	 *            the manipulatabled ojbect
	 * @param line
	 *            the line
	 */
	public TouchPadScreen(String name, ContentSystem contentSystem,
			float width, Spatial manipulatabledOjbect, Line line) {
		super(name);
		this.contentSystem = contentSystem;
		this.width = width;
		this.manipulatedOjbect = manipulatabledOjbect;
		this.line = line;
		
		buildScreenQuad();
		buildScreenFrame();
	}

	/**
	 * Builds the screen frame.
	 */
	public void buildScreenFrame() {
		screenFrame = new Quad(name + "screenFrame", width + 60, width + 30);
		screenFrame.setModelBound(new OrthogonalBoundingBox());
		screenFrame.updateModelBound();
		this.attachChild(screenFrame);

		screenQuad.setLocalTranslation(0, -10, 0);

		TextureState ts;
		Texture texture;
		ts = DisplaySystem.getDisplaySystem().getRenderer()
				.createTextureState();
		ts.setCorrectionType(TextureState.CorrectionType.Perspective);
		texture = TextureManager.loadTexture(
				ThreeDManipulation.class.getResource("touchpadbg.png"),
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

		ControlPanelMoveRotateScale monitorScreenMoveRotateScale = new ControlPanelMoveRotateScale(
				screenFrame, this, null, telescopeManipulateOjbect, null);
		monitorScreenMoveRotateScale.setPickMeOnly(true);
		
		@SuppressWarnings("unused")
		OrthoBringToTop bringToTop = new OrthoBringToTop(screenFrame, this);
	}

	/**
	 * Builds the screen quad.
	 */
	public void buildScreenQuad() {
		screenQuad = new Quad(name + "screenQuad", width + 50, width);
		screenQuad.setModelBound(new OrthogonalBoundingBox());
		screenQuad.updateModelBound();
		this.attachChild(screenQuad);

		TextureState ts;
		Texture texture;
		ts = DisplaySystem.getDisplaySystem().getRenderer()
				.createTextureState();
		ts.setCorrectionType(TextureState.CorrectionType.Perspective);
		texture = TextureManager.loadTexture(
				ThreeDManipulation.class.getResource("touchpad.png"),
				Texture.MinificationFilter.Trilinear,
				Texture.MagnificationFilter.Bilinear);
		texture.setWrap(WrapMode.Repeat);
		texture.setApply(ApplyMode.Replace);
		ts.setTexture(texture);
		ts.apply();
		screenQuad.setRenderState(ts);
		screenQuad.updateRenderState();

		BlendState alpha = DisplaySystem.getDisplaySystem().getRenderer()
				.createBlendState();
		alpha.setEnabled(true);
		alpha.setBlendEnabled(true);
		alpha.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		alpha.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
		alpha.setTestEnabled(true);
		alpha.setTestFunction(BlendState.TestFunction.GreaterThan);
		screenQuad.setRenderState(alpha);
		screenQuad.updateRenderState();
		
		telescopeManipulateOjbect = new OjbectManipulation(screenQuad,
				new ArrayList<Spatial>());
		telescopeManipulateOjbect.setPickMeOnly(true);
		telescopeManipulateOjbect.setControlledSpatial(this.manipulatedOjbect);

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
	 * Reset touch number.
	 */
	public void resetTouchNumber() {
		telescopeManipulateOjbect.resetTouchNumber();
	}
	
	/**
	 * Sets the rotation speed.
	 *
	 * @param rotationSpeed
	 *            the new rotation speed
	 */
	public void setRotationSpeed(int rotationSpeed) {
		telescopeManipulateOjbect.setRotationSpeed(rotationSpeed);
	}
	
}
