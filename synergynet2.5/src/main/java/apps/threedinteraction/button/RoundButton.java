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
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.shape.Cylinder;
import com.jme.scene.shape.Disk;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

public class RoundButton extends Node {

	private static final long serialVersionUID = 2429175967783111868L;
	
	protected float radius =2, height = 3;
	protected URL textureURL;
	protected URL textTextureURL;
	protected Cylinder button;
	protected List<KeyListener> listeners = new ArrayList<KeyListener>();
	protected String keyName="";
	protected Node buttonNode;
	
	public RoundButton(String name, float radius, float height, URL bgTexture, URL textTexture){
		super(name);
		this.keyName = name;
		this.radius = radius;
		this.height = height;
		this.textureURL = bgTexture;
		this.textTextureURL = textTexture;
		
		buttonNode = new Node();
		this.attachChild(buttonNode);
		
		init();		
	}

	protected void init(){
		
		button  = new Cylinder(name + "button", 20, 50, radius, 3, true);
		button.setModelBound(new BoundingBox());
		button.updateModelBound();
		button.getLocalTranslation().set(0, 0, 0);
		
		buttonNode.attachChild(button);
		
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
		button.setRenderState( ts );	
		button.updateRenderState();
		
		buttonNode.attachChild(createTextLabel());
		
		Quaternion tq = new Quaternion();
		tq.fromAngleAxis(3*FastMath.PI/2f, new Vector3f(1, 0, 0));
		this.setLocalRotation(tq);
		
		
	}
	
	private Disk createTextLabel(){
		Disk disk = new Disk(this.name +"buttonLabel", 16, 32, radius);
		
		BlendState alpha = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
		alpha.setEnabled( true );
		alpha.setBlendEnabled( true );
		alpha.setSourceFunction( BlendState.SourceFunction.SourceAlpha);
		alpha.setDestinationFunction( BlendState.DestinationFunction.OneMinusSourceAlpha);
		alpha.setTestEnabled( true );
		alpha.setTestFunction( BlendState.TestFunction.GreaterThan);
		disk.setRenderState( alpha );
		disk.updateRenderState();
		
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
		disk.setRenderState( ts );	
		disk.updateRenderState();		
		disk.getLocalTranslation().z = disk.getLocalTranslation().z+(this.height+1)/2+0.71f;	
		disk.setModelBound(new BoundingBox());
		disk.updateModelBound();
		
		MultiTouchButtonPress ButtonTopPress = new MultiTouchButtonPress(disk, buttonNode);
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
		
		MultiTouchButtonPress multiTouchButtonPress = new MultiTouchButtonPress(button, buttonNode);
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
		
		return disk;
		
	}
	
	public void addKeyListener(KeyListener listener){
		this.listeners.add(listener);
	}
	
}
