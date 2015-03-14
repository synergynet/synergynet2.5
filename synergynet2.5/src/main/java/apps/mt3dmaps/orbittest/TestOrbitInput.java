package apps.mt3dmaps.orbittest;

/**
 * Copyright (c) 2009, Andrew Carter All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. Neither the name of Andrew Carter nor the names of
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */


import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingBox;
import com.jme.image.Image;
import com.jme.image.Texture;
import com.jme.input.MouseInput;
import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Skybox;
import com.jme.scene.Spatial;
import com.jme.scene.Text;
import com.jme.scene.VBOInfo;
import com.jme.scene.Spatial.TextureCombineMode;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Sphere;
import com.jme.scene.shape.Torus;
import com.jme.util.TextureManager;

public class TestOrbitInput extends SimpleGame {

	InputManager inputManager = new InputManager();
	
	Skybox skybox;

	public static void main(String[] args) {

		TestOrbitInput app = new TestOrbitInput();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();
	}

	@Override
	protected void initSystem() {

		super.initSystem();
	}

	@Override
	protected void simpleInitGame() {

		display.setTitle("Orbit Camera");

		// Replace the first person handler in BaseSimpleGame
		super.input = inputManager;

		display.setTitle("Orbit Camera");
		MouseInput.get().setCursorVisible(true);
		
		cam.setLocation(new Vector3f(100, -100, 200));
		cam.update();

		inputManager.setup(cam);
		inputManager.getOrbit().setWheelDirection(true);
		// The target position is the point the camera orbits around
		inputManager.getOrbit().setTargetPosition(new Vector3f(), true);

		Node pickNode = new Node();
		rootNode.attachChild(pickNode);
		
		// Set the node used when picking
		inputManager.getCameraFocus().setNode(pickNode);
		
	    // Pop a few objects into our scene.
	    Torus t = new Torus("Torus", 50, 50, 5, 10);
	    t.setModelBound(new BoundingBox());
	    t.updateModelBound();
	    t.setLocalTranslation(new Vector3f(-40, 0, 10));
	    t.setVBOInfo(new VBOInfo(true));
	    pickNode.attachChild(t);
	    t.setRenderQueueMode(Renderer.QUEUE_OPAQUE);

	    Sphere s = new Sphere("Sphere", 63, 50, 25);
	    s.setModelBound(new BoundingBox());
	    s.updateModelBound();
	    s.setLocalTranslation(new Vector3f(40, 0, -10));
	    pickNode.attachChild(s);
	    s.setVBOInfo(new VBOInfo(true));
	    s.setRenderQueueMode(Renderer.QUEUE_OPAQUE);

	    Box b = new Box("box", new Vector3f(-25, 70, -45), 20, 20, 20);
	    b.setModelBound(new BoundingBox());
	    b.updateModelBound();
	    b.setVBOInfo(new VBOInfo(true));
	    pickNode.attachChild(b);
	    b.setRenderQueueMode(Renderer.QUEUE_OPAQUE);

	    // Create a skybox
	    skybox = new Skybox("skybox", 10, 10, 10);

	    Texture north = TextureManager.loadTexture(
	        TestOrbitInput.class.getClassLoader().getResource(
	        "jmetest/data/texture/north.jpg"),
	        Texture.MinificationFilter.BilinearNearestMipMap,
	        Texture.MagnificationFilter.Bilinear, Image.Format.GuessNoCompression, 0.0f, true);
	    Texture south = TextureManager.loadTexture(
	    		TestOrbitInput.class.getClassLoader().getResource(
	        "jmetest/data/texture/south.jpg"),
	        Texture.MinificationFilter.BilinearNearestMipMap,
	        Texture.MagnificationFilter.Bilinear, Image.Format.GuessNoCompression, 0.0f, true);
	    Texture east = TextureManager.loadTexture(
	    		TestOrbitInput.class.getClassLoader().getResource(
	        "jmetest/data/texture/east.jpg"),
	        Texture.MinificationFilter.BilinearNearestMipMap,
	        Texture.MagnificationFilter.Bilinear, Image.Format.GuessNoCompression, 0.0f, true);
	    Texture west = TextureManager.loadTexture(
	    		TestOrbitInput.class.getClassLoader().getResource(
	        "jmetest/data/texture/west.jpg"),
	        Texture.MinificationFilter.BilinearNearestMipMap,
	        Texture.MagnificationFilter.Bilinear, Image.Format.GuessNoCompression, 0.0f, true);
	    Texture up = TextureManager.loadTexture(
	    		TestOrbitInput.class.getClassLoader().getResource(
	        "jmetest/data/texture/top.jpg"),
	        Texture.MinificationFilter.BilinearNearestMipMap,
	        Texture.MagnificationFilter.Bilinear, Image.Format.GuessNoCompression, 0.0f, true);
	    Texture down = TextureManager.loadTexture(
	    		TestOrbitInput.class.getClassLoader().getResource(
	        "jmetest/data/texture/bottom.jpg"),
	        Texture.MinificationFilter.BilinearNearestMipMap,
	        Texture.MagnificationFilter.Bilinear, Image.Format.GuessNoCompression, 0.0f, true);

	    skybox.setTexture(Skybox.Face.North, north);
	    skybox.setTexture(Skybox.Face.West, west);
	    skybox.setTexture(Skybox.Face.South, south);
	    skybox.setTexture(Skybox.Face.East, east);
	    skybox.setTexture(Skybox.Face.Up, up);
	    skybox.setTexture(Skybox.Face.Down, down);
	    skybox.preloadTextures();
	    
	    // Attach the skybox to the root node, not our pick node
	    rootNode.attachChild(skybox);
	    
	    // Finally lets add some instructions
	    Text text = Text.createDefaultTextLabel("Test Label", "Mouse Wheel: Zoom");
        text.setCullHint(Spatial.CullHint.Never);
        text.setTextureCombineMode(TextureCombineMode.Replace);
        text.setLocalTranslation(new Vector3f(1, 60, 0));
        
        statNode.attachChild(text);
        
        text = Text.createDefaultTextLabel("Test Label", "Hold RMB: Orbit");
        text.setCullHint(Spatial.CullHint.Never);
        text.setTextureCombineMode(TextureCombineMode.Replace);
        text.setLocalTranslation(new Vector3f(1, 45, 0));
        
        statNode.attachChild(text);
        
        text = Text.createDefaultTextLabel("Test Label", "Shift + RMB: Translate");
        text.setCullHint(Spatial.CullHint.Never);
        text.setTextureCombineMode(TextureCombineMode.Replace);
        text.setLocalTranslation(new Vector3f(1, 30, 0));
        
        statNode.attachChild(text);
        
        text = Text.createDefaultTextLabel("Test Label", "Double Click RMB: Focus on Object");
        text.setCullHint(Spatial.CullHint.Never);
        text.setTextureCombineMode(TextureCombineMode.Replace);
        text.setLocalTranslation(new Vector3f(1, 15, 0));
        
        statNode.attachChild(text);
	}

	@Override
	protected void simpleUpdate() {
		
		skybox.setLocalTranslation(cam.getLocation());
	}
}