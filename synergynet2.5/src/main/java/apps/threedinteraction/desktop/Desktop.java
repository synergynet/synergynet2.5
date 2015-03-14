package apps.threedinteraction.desktop;

import java.net.URL;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.image.Texture.ApplyMode;
import com.jme.image.Texture.WrapMode;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

public class Desktop extends Node{

	private static final long serialVersionUID = -7253111110571705792L;
	
	protected String name;
	protected float length, width,height;
	protected URL floorTexture, wallTexture;
	protected Vector3f floorTextureScale, wallTextureScale;
	protected Box floor;
	
	public Desktop(String name, float length, float width, float height,
			URL floorTexture, Vector3f floorTextureScale, URL wallTexture, Vector3f wallTextureScale) {
		super();
		this.name = name;
		this.length = length;
		this.width = width;
		this.height = height;
		this.floorTexture = floorTexture;
		this.wallTexture = wallTexture;
		this.floorTextureScale = floorTextureScale;
		this.wallTextureScale = wallTextureScale;
		
		buildFloor();		
		buildWall(0, width, length, height);
		
	}

	
	private void buildFloor(){
		Vector3f min = new Vector3f(-width/2, -1, -length/2);
		Vector3f max = new Vector3f(width/2, 1, length/2);
		floor = new Box(name+"floor", min, max);
		floor.setLocalTranslation(new Vector3f(0f, 0f, 0f));
		floor.setModelBound(new BoundingBox());
		floor.updateModelBound();
		
		TextureState ts;
		Texture texture;	
		ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		ts.setCorrectionType(TextureState.CorrectionType.Perspective);		
		texture  = TextureManager.loadTexture(floorTexture,  Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);
		texture.setWrap(WrapMode.Repeat);
		texture.setApply(ApplyMode.Replace);
		texture.setScale(this.floorTextureScale);
		Quaternion tq = new Quaternion();
		tq.fromAngleAxis(FastMath.PI/2f, new Vector3f(0, 0, 1));
		texture.setRotation(tq);
		ts.setTexture( texture );	
		ts.apply();
		
		floor.setRenderState( ts );	
		floor.updateRenderState();
		
		this.attachChild(floor);
		
	}
	
	private void buildWall(float floorCenter, float floorWidth, float floorLength, float wallHeight){	
		Vector3f min = new Vector3f(-floorWidth/2, -wallHeight/2, -1);
		Vector3f max = new Vector3f(floorWidth/2, wallHeight/2, 1);
		final Box north = new Box(name+"north wall", min, max);
		north.setLocalTranslation(new Vector3f(0f, wallHeight/2, floorCenter-floorLength/2));
		north.setModelBound(new BoundingBox());
		north.updateModelBound();
		this.attachChild(north);	
		
		final Box south = new Box(name+"south wall", min, max);
		south.setLocalTranslation(new Vector3f(0f, wallHeight/2, floorCenter+floorLength/2));
		south.setModelBound(new BoundingBox());
		south.updateModelBound();
		//this.attachChild(south);
		
		min = new Vector3f(-1, -wallHeight/2, -floorLength/2);
		max = new Vector3f(1, wallHeight/2, floorLength/2);
		
		final Box east = new Box(name+"east wall", min, max);
		east.setLocalTranslation(new Vector3f(floorWidth/2, wallHeight/2, floorCenter));
		east.setModelBound(new BoundingBox());
		east.updateModelBound();
		this.attachChild(east);
		
		final Box west = new Box(name+"west wall", min, max);
		west.setLocalTranslation(new Vector3f(-floorWidth/2, wallHeight/2, floorCenter ));
		west.setModelBound(new BoundingBox());
		west.updateModelBound();
		this.attachChild(west);
		
		TextureState ts;
		Texture texture;	
		ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		ts.setCorrectionType(TextureState.CorrectionType.Perspective);		
		texture  = TextureManager.loadTexture(wallTexture, Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);
		texture.setWrap(WrapMode.Repeat);
		texture.setApply(ApplyMode.Replace);
		texture.setScale(wallTextureScale);
		ts.setTexture( texture );	
		ts.apply();
		
		north.setRenderState( ts );	
		north.updateRenderState();
		
		south.setRenderState( ts );	
		south.updateRenderState();
		
		east.setRenderState( ts );	
		east.updateRenderState();
		
		west.setRenderState( ts );	
		west.updateRenderState();
		
	}
	
	public void showDesktop(boolean b){
		if (b)
			floor.setLocalTranslation(0, 0, 0);
		else
			floor.setLocalTranslation(10000, 10000, 10000);
	}

}
