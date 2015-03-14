package apps.threedmanipulation.scene;

import apps.threedmanipulation.ThreeDManipulation;

import com.jme.image.Texture;
import com.jme.scene.Skybox;
import com.jme.util.TextureManager;

public class DefaultSkyBox extends Skybox {

	private static final long serialVersionUID = 4131281165000957490L;
	
	public DefaultSkyBox(String name, float size){
		super(name, size, size, size);
		
		Texture north = TextureManager.loadTexture(
				ThreeDManipulation.class.getResource(
		        "north.jpg"),
		        Texture.MinificationFilter.BilinearNearestMipMap,
		        Texture.MagnificationFilter.Bilinear);
		    Texture south = TextureManager.loadTexture(
		    		ThreeDManipulation.class.getResource(
		        "south.jpg"),
		        Texture.MinificationFilter.BilinearNearestMipMap,
		        Texture.MagnificationFilter.Bilinear);
		    Texture east = TextureManager.loadTexture(
		    		ThreeDManipulation.class.getResource(
		        "east.jpg"),
		        Texture.MinificationFilter.BilinearNearestMipMap,
		        Texture.MagnificationFilter.Bilinear);
		    Texture west = TextureManager.loadTexture(
		    		ThreeDManipulation.class.getResource(
		        "west.jpg"),
		        Texture.MinificationFilter.BilinearNearestMipMap,
		        Texture.MagnificationFilter.Bilinear);
		    Texture up = TextureManager.loadTexture(
		    		ThreeDManipulation.class.getResource(
		        "top.jpg"),
		        Texture.MinificationFilter.BilinearNearestMipMap,
		        Texture.MagnificationFilter.Bilinear);
		    Texture down = TextureManager.loadTexture(
		    		ThreeDManipulation.class.getResource(
		        "bottom.jpg"),
		        Texture.MinificationFilter.BilinearNearestMipMap,
		        Texture.MagnificationFilter.Bilinear);

		    this.setTexture(Skybox.Face.North, north);
		    this.setTexture(Skybox.Face.West, west);
		    this.setTexture(Skybox.Face.South, south);
		    this.setTexture(Skybox.Face.East, east);
		    this.setTexture(Skybox.Face.Up, up);
		    this.setTexture(Skybox.Face.Down, down);
		    this.preloadTextures();
	}
	
}
