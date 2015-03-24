package apps.threedpuzzle.buildingblock;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;

import apps.threedmanipulation.ThreeDManipulation;

import com.jme.bounding.BoundingBox;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.model.converters.FormatConverter;
import com.jmex.model.converters.ObjToJme;

public class TBuildingBlock  extends Node {
	
	private static final long serialVersionUID = -7752237123118348139L;
	Spatial map;
	
	public TBuildingBlock(String name){
		super(name);
		buildBlock();
	}
	
	private void buildBlock(){

		
		
	   URL model = ThreeDManipulation.class.getClassLoader().getResource("LTetris.obj");
	   FormatConverter converter=new ObjToJme();
	   String folder= ThreeDManipulation.class.getClassLoader().getResource("ThreeDManipulationResources.class")
			   .toString().replace("ThreeDManipulationResources.class", "");
       converter.setProperty("mtllib", folder);

	 
	        ByteArrayOutputStream BO=new ByteArrayOutputStream();
	        try {
	            converter.convert(model.openStream(), BO);
	            map=(Spatial) BinaryImporter.getInstance().load(new ByteArrayInputStream(BO.toByteArray()));
	            map.setLocalScale(8f);
	 
	            map.setModelBound(new BoundingBox());
	            map.updateModelBound();
	            this.attachChild(map);
	        } catch (Exception e) {
	            System.out.println("Damn exceptions! O_o \n" + e);
	            e.printStackTrace();
	            System.exit(0);
	        }

		
	}
	
	public Spatial getSpatial(){
		return map;
	}
	
}