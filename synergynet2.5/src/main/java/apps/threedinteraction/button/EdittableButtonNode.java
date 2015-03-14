package apps.threedinteraction.button;

import java.awt.Color;
import java.awt.Font;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import synergynetframework.appsystem.contentsystem.ContentSystemUtils;
import synergynetframework.jme.cursorsystem.elements.threed.MultiTouchButtonPress;
import synergynetframework.jme.cursorsystem.elements.threed.MultiTouchButtonPress.FreeButtonListener;
import synergynetframework.jme.gfx.twod.utils.GraphicsImageQuad;

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
import com.jmex.awt.swingui.ImageGraphics;

public class EdittableButtonNode extends Node {

	private static final long serialVersionUID = 2429175967783608868L;
	
	protected float width = 1, length = 1, height =1, slope = 0.5f;
	protected URL textureURL;
	protected String text;
	protected RoundedBox rb;
	protected List<KeyListener> listeners = new ArrayList<KeyListener>();
	protected String keyName="";
	protected GraphicsImageQuad quad;
	protected ImageGraphics gfx;
	
	public EdittableButtonNode(String name, float width, float length, float height, float slope, URL bgTexture, String text){
		super(name);
		this.keyName = name;
		this.width = width;
		this.length = length;
		this.height = height;
		this.slope = slope;
		this.textureURL = bgTexture;
		this.text = text;
		init();		
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
	
	private Quad createTextQuad(){
		Quad quad = new Quad("button "+this.name, this.width*(1.5f), this.length*(1.5f));
		
		BlendState alpha = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
		alpha.setEnabled( true );
		alpha.setBlendEnabled( true );
		alpha.setSourceFunction( BlendState.SourceFunction.SourceAlpha);
		alpha.setDestinationFunction( BlendState.DestinationFunction.OneMinusSourceAlpha);
		alpha.setTestEnabled( true );
		alpha.setTestFunction( BlendState.TestFunction.GreaterThan);
		
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
				
		render(this.text);	
		
		
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
	
		gfx.setColor(new Color(36, 36, 36));
		gfx.fillRect(0, 0, 300, 200);
		gfx.setColor(Color.white);
		Font font = new Font("Arial Narrow", Font.PLAIN,32);
		gfx.setFont(font);
		
	
		gfx.setColor(Color.white);
		int textWidth =  ContentSystemUtils.getStringWidth(font, text);		
		gfx.drawString(text, textWidth/2-this.width, 40);
		quad.updateGraphics();
		
		quad.updateGeometricState(0f, false);
		
	}
	
	public void setText(String text){
		this.text = text;
		render(text);
	}
	
	public Quad getDisplayQuad(){
		return quad;
	}
	
	public void addKeyListener(KeyListener listener){
		this.listeners.add(listener);
	}
	
}
