package apps.threedpuzzle.scene;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;

import apps.threedmanipulation.ThreeDManipulation;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.image.Texture.ApplyMode;
import com.jme.image.Texture.WrapMode;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.model.converters.FormatConverter;
import com.jmex.model.converters.ObjToJme;

public class Yard extends Node{

	private static final long serialVersionUID = -7253106690571705792L;
	
	protected String name;
	protected float length, width,height;
	protected URL floorTexture, wallTexture;
	protected Vector3f floorTextureScale, wallTextureScale;
	
	public Yard(String name, float length, float width, float height,
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
		buildWall(0, width+1, length+1, height);

	}
	
	private void buildFloor(){
		Vector3f min = new Vector3f(-width/2, -1, -length/2);
		Vector3f max = new Vector3f(width/2, 1, length/2);
		final Box floor = new Box(name+"floor", min, max);
		floor.setLocalTranslation(new Vector3f(0f, -1f, 0f));
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
		ts.setTexture( texture );	
		ts.apply();
		
		floor.setRenderState( ts );	
		floor.updateRenderState();
		
		this.attachChild(floor);
				
	}
	
	private void buildWall(float floorCenter, float floorWidth, float floorLength, float wallHeight){	

		   Spatial frame=null;
		   URL model = ThreeDManipulation.class.getClassLoader().getResource("frame.obj");
		   FormatConverter converter=new ObjToJme();
		   String folder= ThreeDManipulation.class.getClassLoader().getResource("ThreeDManipulationResources.class")
				   .toString().replace("ThreeDManipulationResources.class", "");
	       converter.setProperty("mtllib", folder);

		 
		        ByteArrayOutputStream BO=new ByteArrayOutputStream();
		        try {
		            converter.convert(model.openStream(), BO);
		            frame=(Spatial) BinaryImporter.getInstance().load(new ByteArrayInputStream(BO.toByteArray()));
		            frame.setLocalScale(9f);
		            frame.setLocalTranslation(new Vector3f(0,1, 1));

		            this.attachChild(frame);
		        } catch (Exception e) {
		            e.printStackTrace();
		            System.exit(0);
		        }
		        
		
	}
	
	
}