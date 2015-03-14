package apps.threedmanipulation.tools;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import apps.threedmanipulation.ThreeDManipulation;
import apps.threedmanipulation.gestures.ControlPanelMoveRotateScale;
import apps.threedmanipulation.gestures.OjbectManipulation;
import apps.threedmanipulation.gestures.ControlPanelMoveRotateScale.RotateTranslateScaleListener;

import com.jme.bounding.OrthogonalBoundingBox;
import com.jme.image.Texture;
import com.jme.image.Texture.ApplyMode;
import com.jme.image.Texture.WrapMode;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Line;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jme.util.geom.BufferUtils;

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
		monitorScreenMoveRotateScale.addRotateTranslateScaleListener(new RotateTranslateScaleListener(){

			@Override
			public void itemMoved(
					ControlPanelMoveRotateScale multiTouchElement,
					Spatial targetSpatial, float newLocationX,
					float newLocationY, float oldLocationX, float oldLocationY) {
				Vector3f cursorWorldStart = DisplaySystem.getDisplaySystem().getWorldCoordinates(new Vector2f(newLocationX, newLocationY), 0.9f);
				
				if(cursorWorldStart != null && manipulatedOjbect != null){

					FloatBuffer vectorBuff = BufferUtils.createVector3Buffer(2);
					FloatBuffer colorBuff = BufferUtils.createFloatBuffer(new ColorRGBA[]{ColorRGBA.gray, ColorRGBA.white});
					BufferUtils.setInBuffer(cursorWorldStart, vectorBuff, 0);
					BufferUtils.setInBuffer(manipulatedOjbect.getLocalTranslation(), vectorBuff, 1);
					line.setLineWidth(2f);
					line.reconstruct(vectorBuff, null, colorBuff, null);
					line.updateRenderState();
					line.updateGeometricState(0f, false);			
				}				
			}

			@Override
			public void itemRotated(
					ControlPanelMoveRotateScale multiTouchElement,
					Spatial targetSpatial, float newAngle, float oldAngle) {				
			}

			@Override
			public void itemScaled(
					ControlPanelMoveRotateScale multiTouchElement,
					Spatial targetSpatial, float scaleChange) {				
			}
			
		});
		
		@SuppressWarnings("unused")
		OrthoBringToTop bringToTop = new OrthoBringToTop(screenFrame, this);
	}
	
	
	public Quad getScreenQuad(){
		return screenQuad;
	}

	
}
