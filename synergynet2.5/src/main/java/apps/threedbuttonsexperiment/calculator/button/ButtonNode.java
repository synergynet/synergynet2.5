package apps.threedbuttonsexperiment.calculator.button;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import apps.threedbuttonsexperiment.gesture.MultiTouchButtonPress;
import apps.threedbuttonsexperiment.gesture.MultiTouchButtonPress.FreeButtonListener;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.image.Texture.ApplyMode;
import com.jme.image.Texture.WrapMode;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.scene.shape.RoundedBox;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

public class ButtonNode extends Node {

	private static final long serialVersionUID = 2429175967783608868L;
	
	public static final String FEEDBACK_MODE_NONE = "none";
	public static final String FEEDBACK_MODE_COLORHIGHLIGHTED = "colorhighlighted";
	public static final String FEEDBACK_MODE_3D = "3d";
	
	protected float width = 1, length = 1, height =1, slope = 0.5f;
	protected URL textureURL;
	protected URL textTextureURL;
	protected RoundedBox rb;
	protected List<KeyListener> listeners = new ArrayList<KeyListener>();
	protected String keyName="";
	protected float zvalueOfButtonLabel=0;
	protected ButtonNode self;
	protected String feedbackMode = FEEDBACK_MODE_NONE;
	protected URL highlightedTextureURL;
	
	
	public ButtonNode(String name, float width, float length, float height, float slope, URL bgTexture, URL textTexture, URL highlightedTextureURL){
		super(name);
		this.keyName = name;
		this.width = width;
		this.length = length;
		this.height = height;
		this.slope = slope;
		this.textureURL = bgTexture;
		this.textTextureURL = textTexture;	
		this.highlightedTextureURL = highlightedTextureURL;
		self = this;		
		init();		
	}
	
	public void setFeedbackMode(String feedbackMode){
		this.feedbackMode = feedbackMode;
	}
	
	
	public float getZvalueOfButtonLabel() {
		return zvalueOfButtonLabel;
	}


	public void setZvalueOfButtonLabel(float zvalueOfButtonLabel) {
		this.zvalueOfButtonLabel = zvalueOfButtonLabel;
	}

	protected void init(){
		
		Vector3f min = new Vector3f(this.width, this.length, this.height);
		Vector3f max = new Vector3f(0.1f, 0.1f, 0.1f);
		Vector3f slopeV = new Vector3f(slope, slope, slope);
				
		rb = new RoundedBox("Body RoundedBox"+name, min, max, slopeV);
		rb.setModelBound(new BoundingBox());
		rb.updateModelBound();
	
		this.attachChild(rb);
		
		TextureState ts;
		Texture texture;	
		ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		ts.setCorrectionType(TextureState.CorrectionType.Perspective);		
		texture = TextureManager.loadTexture(textureURL, Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);
		texture.setWrap(WrapMode.Repeat);
		texture.setApply(ApplyMode.Replace);
		texture.setScale( new Vector3f( 1, 1, 1 ) );
		ts.setTexture( texture );	
		ts.apply();
		rb.setRenderState( ts );	
		rb.updateRenderState();
		
		this.attachChild(createTextQuad());
		
			
	}
	
	@SuppressWarnings("static-access")
	private Quad createTextQuad(){
		
		float scaleOffSet = 0f;
		if (this.width!=this.length){
			scaleOffSet = 0.3f;
		}
			
			
		final Quad quad = new Quad("button "+this.name, this.width*(1.5f+scaleOffSet), this.length*(1.5f+scaleOffSet));

		
		final Quad highlightedQuad = new Quad("highlighted button "+this.name, this.width*(1.9f), this.length*(1.8f));
		highlightedQuad.setCullHint(cullHint.Always);
		
		BlendState alpha = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
		alpha.setEnabled( true );
		alpha.setBlendEnabled( true );
		alpha.setSourceFunction( BlendState.SourceFunction.SourceAlpha);
		alpha.setDestinationFunction( BlendState.DestinationFunction.OneMinusSourceAlpha);
		alpha.setTestEnabled( true );
		alpha.setTestFunction( BlendState.TestFunction.GreaterThan);
		quad.setRenderState( alpha );
		quad.updateRenderState();
		highlightedQuad.setRenderState( alpha );
		highlightedQuad.updateRenderState();
		
		TextureState ts;
		Texture texture;	
		ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		ts.setCorrectionType(TextureState.CorrectionType.Perspective);		
		texture  = TextureManager.loadTexture(textTextureURL, Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);
		texture.setWrap(WrapMode.Repeat);
		texture.setApply(ApplyMode.Replace);
		texture.setScale( new Vector3f( 1, 1, 1 ) );
		ts.setTexture( texture );	
		ts.apply();
		quad.setRenderState( ts );	
		quad.updateRenderState();	
		
		if (highlightedTextureURL!= null){
			TextureState tsHighlighted;
			Texture textureHighlighted;	
			tsHighlighted = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
			tsHighlighted.setCorrectionType(TextureState.CorrectionType.Perspective);	
			textureHighlighted  = TextureManager.loadTexture(highlightedTextureURL, Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);				
			textureHighlighted.setWrap(WrapMode.Repeat);
			textureHighlighted.setApply(ApplyMode.Replace);
			textureHighlighted.setScale( new Vector3f( 1, 1, 1 ) );
			tsHighlighted.setTexture( textureHighlighted );	
			tsHighlighted.apply();
			highlightedQuad.setCullHint(cullHint.Always);
			highlightedQuad.setRenderState( tsHighlighted );	
			highlightedQuad.updateRenderState();
		}
		
		if (zvalueOfButtonLabel!=0){
			quad.getLocalTranslation().z = rb.getLocalTranslation().z+(this.height+1)/2+zvalueOfButtonLabel;
			highlightedQuad.getLocalTranslation().z = rb.getLocalTranslation().z+(this.height+1)/2+zvalueOfButtonLabel;
		}
		else{
			quad.getLocalTranslation().z = rb.getLocalTranslation().z+(this.height+1)/2+0.1f;	
			highlightedQuad.getLocalTranslation().z = rb.getLocalTranslation().z+(this.height+1)/2+0.1f;	
		}
		quad.setModelBound(new BoundingBox());
		quad.updateModelBound();
		highlightedQuad.setModelBound(new BoundingBox());
		highlightedQuad.updateModelBound();
		
		MultiTouchButtonPress ButtonTopPress = new MultiTouchButtonPress(quad, this);
		ButtonTopPress.setButtonHeight(this.height/2);
		ButtonTopPress.setPickMeOnly(true);
		ButtonTopPress.addButtonListener(new FreeButtonListener(){	
			
			@Override
			public void buttonReleased() {	
		
				if (self.feedbackMode.equals(self.FEEDBACK_MODE_3D))
					self.getLocalTranslation().z +=self.height/2*3f;	
				else if (self.feedbackMode.equals(self.FEEDBACK_MODE_COLORHIGHLIGHTED)){
					highlightedQuad.setCullHint(cullHint.Always);
					quad.setCullHint(cullHint.Never);
					highlightedQuad.updateRenderState();
					quad.updateRenderState();
				}
			}

			@Override
			public void buttonPressed() {
				if (self.feedbackMode.equals(self.FEEDBACK_MODE_3D))
					self.getLocalTranslation().z -=self.height/2*3f;
				else if (self.feedbackMode.equals(self.FEEDBACK_MODE_COLORHIGHLIGHTED)){
					quad.setCullHint(cullHint.Always);
					highlightedQuad.setCullHint(cullHint.Never);
					highlightedQuad.updateRenderState();
					quad.updateRenderState();
				}
				for (KeyListener l:listeners){
					l.keyPressed(keyName);
				}		
			}	
		});
		
		MultiTouchButtonPress multiTouchButtonPress = new MultiTouchButtonPress(rb, this);
		multiTouchButtonPress.setButtonHeight(this.height/2);
		multiTouchButtonPress.setPickMeOnly(true);
		multiTouchButtonPress.addButtonListener(new FreeButtonListener(){
			@Override
			public void buttonPressed() {
				if (self.feedbackMode.equals(self.FEEDBACK_MODE_3D))
					self.getLocalTranslation().z -=self.height/2*3f;
				else if (self.feedbackMode.equals(self.FEEDBACK_MODE_COLORHIGHLIGHTED)){
					quad.setCullHint(cullHint.Always);
					highlightedQuad.setCullHint(cullHint.Never);
					highlightedQuad.updateRenderState();
					quad.updateRenderState();
				}
				for (KeyListener l:listeners){
					l.keyPressed(keyName);
				}		
			}

			@Override
			public void buttonReleased() {
				if (self.feedbackMode.equals(ButtonNode.FEEDBACK_MODE_3D))
					self.getLocalTranslation().z +=self.height/2*3f;	
				else if (self.feedbackMode.equals(self.FEEDBACK_MODE_COLORHIGHLIGHTED)){
					highlightedQuad.setCullHint(cullHint.Always);
					quad.setCullHint(cullHint.Never);
					highlightedQuad.updateRenderState();
					quad.updateRenderState();
				}
				
			}	
		});
		
		this.attachChild(highlightedQuad);
		
		return quad;	
	}
	
	public void addKeyListener(KeyListener listener){
		this.listeners.add(listener);
	}
	
	public void setButtonBodyVisability(boolean b){
		if (!b){
			rb.setCullHint(CullHint.Always);
		}
		else{
			rb.setCullHint(CullHint.Never);
		}
	}
	
}
