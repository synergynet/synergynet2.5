package apps.projectmanagement.component.tools;

import apps.threedmanipulation.ThreeDManipulation;
import apps.threedmanipulation.gestures.ControlPanelMoveRotateScale;

import com.jme.bounding.OrthogonalBoundingBox;
import com.jme.image.Texture;
import com.jme.image.Texture.ApplyMode;
import com.jme.image.Texture.WrapMode;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;


/**
 * The Class TouchPadScreen.
 */
public class TouchPadScreen extends Node{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5768509222221148746L;
	
	/** The width. */
	protected float width;
	
	/** The screen quad. */
	protected Quad screenQuad;
	
	/** The screen frame. */
	protected Quad screenFrame;
		
	/**
	 * Instantiates a new touch pad screen.
	 *
	 * @param name the name
	 * @param width the width
	 */
	public TouchPadScreen(String name, float width){
		super(name);
		this.width = width;
		
		buildScreenQuad();
		buildScreenFrame();
			
	}
	
	/**
	 * Builds the screen quad.
	 */
	public void buildScreenQuad(){
		screenQuad = new Quad(name+"screenQuad", width+50, width);
		screenQuad.setModelBound(new OrthogonalBoundingBox());
		screenQuad.updateModelBound();
		this.attachChild(screenQuad);
		
		TextureState ts;
		Texture texture;	
		ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		ts.setCorrectionType(TextureState.CorrectionType.Perspective);		
		texture  = TextureManager.loadTexture(ThreeDManipulation.class.getResource(
		    	"touchpad.png"),  Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);
		texture.setWrap(WrapMode.Repeat);
		texture.setApply(ApplyMode.Replace);
		ts.setTexture( texture );	
		ts.apply();
		screenQuad.setRenderState(ts);
		screenQuad.updateRenderState();
		
		BlendState alpha = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
		alpha.setEnabled( true );
		alpha.setBlendEnabled( true );
		alpha.setSourceFunction( BlendState.SourceFunction.SourceAlpha);
		alpha.setDestinationFunction( BlendState.DestinationFunction.OneMinusSourceAlpha);
		alpha.setTestEnabled( true );
		alpha.setTestFunction( BlendState.TestFunction.GreaterThan);
		screenQuad.setRenderState(alpha );
		screenQuad.updateRenderState();	
		
		screenQuad.setLocalTranslation(0, -10, 0);
		
	}
	
	/**
	 * Builds the screen frame.
	 */
	public void buildScreenFrame(){
		screenFrame = new Quad(name+"screenFrame", width+60, width+30);
		screenFrame.setModelBound(new OrthogonalBoundingBox());
		screenFrame.updateModelBound();
		this.attachChild(screenFrame);
		
		TextureState ts;
		Texture texture;	
		ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		ts.setCorrectionType(TextureState.CorrectionType.Perspective);		
		texture  = TextureManager.loadTexture(ThreeDManipulation.class.getResource(
		    	"touchpadbg.png"),  Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);
		texture.setWrap(WrapMode.Repeat);
		texture.setApply(ApplyMode.Replace);
		ts.setTexture( texture );	
		ts.apply();
		
		screenFrame.setRenderState(ts);
		screenFrame.updateRenderState();
		
		BlendState alpha = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
		alpha.setEnabled( true );
		alpha.setBlendEnabled( true );
		alpha.setSourceFunction( BlendState.SourceFunction.SourceAlpha);
		alpha.setDestinationFunction( BlendState.DestinationFunction.OneMinusSourceAlpha);
		alpha.setTestEnabled( true );
		alpha.setTestFunction( BlendState.TestFunction.GreaterThan);
		screenFrame.setRenderState( alpha );

		screenFrame.updateRenderState();
		
		ControlPanelMoveRotateScale monitorScreenMoveRotateScale = new ControlPanelMoveRotateScale(screenFrame, this);
		monitorScreenMoveRotateScale.setPickMeOnly(true);
		
		//@SuppressWarnings("unused")
		//OrthoBringToTop bringToTop = new OrthoBringToTop(screenFrame, this);
	}
		
	/**
	 * Gets the screen quad.
	 *
	 * @return the screen quad
	 */
	public Quad getScreenQuad(){
		return screenQuad;
	}
	
	
	
}
