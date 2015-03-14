package apps.threedmanipulationexperiment.tools;

import java.util.ArrayList;

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

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.jme.cursorsystem.elements.twod.OrthoBringToTop;

public class TouchPadScreen extends Node{

	private static final long serialVersionUID = 5768509222221148746L;
	protected ContentSystem contentSystem;
	protected float width;
	protected Quad screenQuad;
	protected Quad screenFrame;
	protected Spatial manipulatedOjbect;
	protected OjbectManipulation telescopeManipulateOjbect;
	protected Line line;
	protected int rotationSpeed;
		
	public TouchPadScreen(String name, ContentSystem contentSystem, float width, Spatial manipulatabledOjbect, Line line){
		super(name);
		this.contentSystem = contentSystem;
		this.width = width;
		this.manipulatedOjbect = manipulatabledOjbect;
		this.line = line;
			
		buildScreenQuad();
		buildScreenFrame();				
	}
	
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
			
		telescopeManipulateOjbect = new OjbectManipulation(screenQuad, new ArrayList<Spatial>());
		telescopeManipulateOjbect.setPickMeOnly(true);
		telescopeManipulateOjbect.setControlledSpatial(this.manipulatedOjbect);
		
	}
	
	public void setRotationSpeed(int rotationSpeed){
		telescopeManipulateOjbect.setRotationSpeed(rotationSpeed);
	}
	
	public void resetTouchNumber(){
		telescopeManipulateOjbect.resetTouchNumber();
	}
	
	public void buildScreenFrame(){
		screenFrame = new Quad(name+"screenFrame", width+60, width+30);
		screenFrame.setModelBound(new OrthogonalBoundingBox());
		screenFrame.updateModelBound();
		this.attachChild(screenFrame);
		
		screenQuad.setLocalTranslation(0, -10, 0);
		
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
		
		ControlPanelMoveRotateScale monitorScreenMoveRotateScale = new ControlPanelMoveRotateScale(screenFrame, this, null, telescopeManipulateOjbect, null);
		monitorScreenMoveRotateScale.setPickMeOnly(true);
		
		
		@SuppressWarnings("unused")
		OrthoBringToTop bringToTop = new OrthoBringToTop(screenFrame, this);
	}
	
	
	public Quad getScreenQuad(){
		return screenQuad;
	}

	
}
