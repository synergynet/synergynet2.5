package apps.threedbuttonsexperiment.calculator.component;

import java.net.URL;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.image.Texture.ApplyMode;
import com.jme.image.Texture.WrapMode;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.shape.RoundedBox;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;


/**
 * The Class ControllerBodyNode.
 */
public class ControllerBodyNode extends Node {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2429175967783608868L;
	
	/** The slope. */
	protected float width = 1, length = 1, height =1, slope = 0.5f;
	
	/** The texture url. */
	protected URL textureURL;
	
	/**
	 * Instantiates a new controller body node.
	 *
	 * @param name the name
	 * @param width the width
	 * @param length the length
	 * @param height the height
	 * @param slope the slope
	 * @param texture the texture
	 */
	public ControllerBodyNode(String name, float width, float length, float height, float slope, URL texture){
		super(name);
		this.width = width;
		this.length = length;
		this.height = height;
		this.slope = slope;
		this.textureURL = texture;
		
		init();
		
	}

	/**
	 * Inits the.
	 */
	protected void init(){
		
		Vector3f min = new Vector3f(this.width, this.length, this.height);
		Vector3f max = new Vector3f(0.1f, 0.1f, 0.1f);
		Vector3f slopeV = new Vector3f(slope, slope, slope);
		
		
		RoundedBox rb = new RoundedBox("Body RoundedBox", min, max, slopeV);
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
	}
	
}
