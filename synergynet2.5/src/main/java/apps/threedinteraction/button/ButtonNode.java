package apps.threedinteraction.button;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import synergynetframework.jme.cursorsystem.elements.threed.MultiTouchButtonPress;
import synergynetframework.jme.cursorsystem.elements.threed.MultiTouchButtonPress.FreeButtonListener;

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


/**
 * The Class ButtonNode.
 */
public class ButtonNode extends Node {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2429175967783608868L;
	
	/** The slope. */
	protected float width = 1, length = 1, height =1, slope = 0.5f;
	
	/** The texture url. */
	protected URL textureURL;
	
	/** The text texture url. */
	protected URL textTextureURL;
	
	/** The rb. */
	protected RoundedBox rb;
	
	/** The listeners. */
	protected List<KeyListener> listeners = new ArrayList<KeyListener>();
	
	/** The key name. */
	protected String keyName="";
	
	/** The zvalue of button label. */
	protected float zvalueOfButtonLabel=0;
	
	/**
	 * Instantiates a new button node.
	 *
	 * @param name the name
	 * @param width the width
	 * @param length the length
	 * @param height the height
	 * @param slope the slope
	 * @param bgTexture the bg texture
	 * @param textTexture the text texture
	 */
	public ButtonNode(String name, float width, float length, float height, float slope, URL bgTexture, URL textTexture){
		super(name);
		this.keyName = name;
		this.width = width;
		this.length = length;
		this.height = height;
		this.slope = slope;
		this.textureURL = bgTexture;
		this.textTextureURL = textTexture;
		
		init();		
	}
	
	/**
	 * Gets the zvalue of button label.
	 *
	 * @return the zvalue of button label
	 */
	public float getZvalueOfButtonLabel() {
		return zvalueOfButtonLabel;
	}


	/**
	 * Sets the zvalue of button label.
	 *
	 * @param zvalueOfButtonLabel the new zvalue of button label
	 */
	public void setZvalueOfButtonLabel(float zvalueOfButtonLabel) {
		this.zvalueOfButtonLabel = zvalueOfButtonLabel;
	}

	/**
	 * Inits the.
	 */
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
		texture  = TextureManager.loadTexture(textureURL, Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);
		texture.setWrap(WrapMode.Repeat);
		texture.setApply(ApplyMode.Replace);
		texture.setScale( new Vector3f( 1, 1, 1 ) );
		ts.setTexture( texture );	
		ts.apply();
		rb.setRenderState( ts );	
		rb.updateRenderState();
		
		this.attachChild(createTextQuad());
			
	}
	
	/**
	 * Creates the text quad.
	 *
	 * @return the quad
	 */
	private Quad createTextQuad(){
		Quad quad = new Quad("button "+this.name, this.width*(1.5f), this.length*(1.5f));
		
		BlendState alpha = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
		alpha.setEnabled( true );
		alpha.setBlendEnabled( true );
		alpha.setSourceFunction( BlendState.SourceFunction.SourceAlpha);
		alpha.setDestinationFunction( BlendState.DestinationFunction.OneMinusSourceAlpha);
		alpha.setTestEnabled( true );
		alpha.setTestFunction( BlendState.TestFunction.GreaterThan);
		quad.setRenderState( alpha );
		quad.updateRenderState();
		
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
		
		if (zvalueOfButtonLabel!=0)
			quad.getLocalTranslation().z = rb.getLocalTranslation().z+(this.height+1)/2+zvalueOfButtonLabel;
		else
			quad.getLocalTranslation().z = rb.getLocalTranslation().z+(this.height+1)/2+0.1f;	
		quad.setModelBound(new BoundingBox());
		quad.updateModelBound();
		
		MultiTouchButtonPress ButtonTopPress = new MultiTouchButtonPress(quad, this);
		ButtonTopPress.setButtonHeight(this.height/2);
		ButtonTopPress.setPickMeOnly(true);
		ButtonTopPress.addButtonListener(new FreeButtonListener(){
			@Override
			public void buttonPressed() {
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
				for (KeyListener l:listeners){
					l.keyPressed(keyName);
				}		
			}	
		});
		
		return quad;
		
	}
	
	/**
	 * Adds the key listener.
	 *
	 * @param listener the listener
	 */
	public void addKeyListener(KeyListener listener){
		this.listeners.add(listener);
	}
	
	/**
	 * Sets the button body visability.
	 *
	 * @param b the new button body visability
	 */
	public void setButtonBodyVisability(boolean b){
		if (!b){
			rb.setCullHint(CullHint.Always);
		}
		else{
			rb.setCullHint(CullHint.Never);
		}
	}
	
}
