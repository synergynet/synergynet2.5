package apps.threedinteraction.calculator;

import java.awt.Color;
import java.awt.Font;
import java.net.URL;

import synergynetframework.appsystem.contentsystem.ContentSystemUtils;
import synergynetframework.jme.gfx.twod.utils.GraphicsImageQuad;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.image.Texture.ApplyMode;
import com.jme.image.Texture.WrapMode;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.scene.shape.RoundedBox;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.awt.swingui.ImageGraphics;


/**
 * The Class DisplayNode.
 */
public class DisplayNode extends Node {

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
	
	/** The quad. */
	protected GraphicsImageQuad quad;
	
	/** The gfx. */
	protected ImageGraphics gfx;
	
	/** The is cool mode. */
	protected boolean isCoolMode = false;
	
	/**
	 * Instantiates a new display node.
	 *
	 * @param name the name
	 * @param width the width
	 * @param length the length
	 * @param height the height
	 * @param slope the slope
	 * @param bgTexture the bg texture
	 */
	public DisplayNode(String name, float width, float length, float height, float slope, URL bgTexture){
		super(name);
		this.width = width;
		this.length = length;
		this.height = height;
		this.slope = slope;
		this.textureURL = bgTexture;
		
		init();		
	}

	/**
	 * Inits the.
	 */
	protected void init(){
		
		Vector3f min = new Vector3f(this.width, this.length, this.height);
		Vector3f max = new Vector3f(0.1f, 0.1f, 0.1f);
		Vector3f slopeV = new Vector3f(slope, slope, slope);
			
		rb = new RoundedBox("Body RoundedBox", min, max, slopeV);
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
				
		render("");	
		
	}
	
	/**
	 * Show cool mode.
	 */
	public void showCoolMode(){
		this.detachChild(rb);
		this.updateGeometricState(0f, false);
		this.isCoolMode = true;
		render("");
	}
	
	/**
	 * Render.
	 *
	 * @param text the text
	 */
	protected void render(String text){
		
		if (quad!=null)
			this.detachChild(quad);
		
		quad = new GraphicsImageQuad("Display "+this.name, (int)(this.width*1.8f*10), (int)(this.length*1.6f*10), 10);
		this.attachChild(quad);
		quad.setTextureApplyMode(ApplyMode.Replace);
		

		gfx = quad.getImageGraphics();	
		
		quad.getLocalTranslation().z = rb.getLocalTranslation().z+this.height+0.1f;
		quad.setModelBound(new BoundingBox());
		quad.updateModelBound();
	
		
		gfx = quad.getImageGraphics();	
		if (isCoolMode)
			gfx.setColor(Color.black);	
		else
			gfx.setColor(new Color(51, 0, 102));
		gfx.fillRect(0, 0, 300, 200);
		gfx.setColor(Color.white);
		Font font = new Font("Arial Narrow", Font.PLAIN,40);
		gfx.setFont(font);
		
		if (isCoolMode)
			gfx.setColor(Color.green);	
		else
			gfx.setColor(Color.white);
		int textWidth =  ContentSystemUtils.getStringWidth(font, text);		
		gfx.drawString(text, 150-textWidth, 40);
		
		quad.updateGeometricState(0f, false);
		quad.updateGraphics();
	}
	
	/**
	 * Sets the text.
	 *
	 * @param text the new text
	 */
	public void setText(String text){
		render(text);
	}
	
	/**
	 * Gets the display quad.
	 *
	 * @return the display quad
	 */
	public Quad getDisplayQuad(){
		return quad;
	}
	
}
