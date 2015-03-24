/*
 * Copyright (c) 2009 University of Durham, England All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of 'SynergyNet' nor the names of
 * its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission. THIS SOFTWARE IS PROVIDED
 * BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package apps.mt3dmaps;

import java.awt.Image;

import javax.swing.ImageIcon;

import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.scene.PassNode;
import com.jme.scene.PassNodeState;
import com.jme.scene.Skybox;
import com.jme.scene.Spatial;
import com.jme.scene.Spatial.TextureCombineMode;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.CullState;
import com.jme.scene.state.FogState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.terrain.TerrainPage;
import com.jmex.terrain.util.ImageBasedHeightMap;

/**
 * The Class IslandUtils.
 */
public class IslandUtils {

	/** The global splat scale. */
	private static float globalSplatScale = 90.0f;

	/** The Constant resourcePath. */
	private static final String resourcePath = "";

	/**
	 * Adds the alpha splat.
	 *
	 * @param ts
	 *            the ts
	 * @param alpha
	 *            the alpha
	 */
	private static void addAlphaSplat(TextureState ts, String alpha) {
		Texture t1 = TextureManager.loadTexture(
				IslandUtils.class.getResource(alpha),
				Texture.MinificationFilter.Trilinear,
				Texture.MagnificationFilter.Bilinear);
		t1.setWrap(Texture.WrapMode.Repeat);
		t1.setApply(Texture.ApplyMode.Combine);
		t1.setCombineFuncRGB(Texture.CombinerFunctionRGB.Replace);
		t1.setCombineSrc0RGB(Texture.CombinerSource.Previous);
		t1.setCombineOp0RGB(Texture.CombinerOperandRGB.SourceColor);
		t1.setCombineFuncAlpha(Texture.CombinerFunctionAlpha.Replace);
		ts.setTexture(t1, ts.getNumberOfSetTextures());
	}

	/**
	 * Creates the lightmap texture state.
	 *
	 * @param texture
	 *            the texture
	 * @return the texture state
	 */
	private static TextureState createLightmapTextureState(String texture) {
		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer()
				.createTextureState();
		
		Texture t0 = TextureManager.loadTexture(
				IslandUtils.class.getResource(texture),
				Texture.MinificationFilter.Trilinear,
				Texture.MagnificationFilter.Bilinear);
		t0.setWrap(Texture.WrapMode.Repeat);
		ts.setTexture(t0, 0);
		
		return ts;
	}

	/**
	 * Creates the reflection terrain.
	 *
	 * @return the spatial
	 */
	public static Spatial createReflectionTerrain() {
		// RawHeightMap heightMap = new RawHeightMap(TestIsland.class
		// .getResource(
		// resourcePath + "heights.raw"),
		// 129, RawHeightMap.FORMAT_16BITLE, false);

		Image img = new ImageIcon(IslandUtils.class.getResource(resourcePath
				+ "height129.png")).getImage();
		ImageBasedHeightMap heightMap = new ImageBasedHeightMap(img);
		
		Vector3f terrainScale = new Vector3f(5, 0.003f, 6);
		heightMap.setHeightScale(0.001f);
		TerrainPage page = new TerrainPage("Terrain", 33, heightMap.getSize(),
				terrainScale, heightMap.getHeightMap());
		page.getLocalTranslation().set(0, -9.5f, 0);
		page.setDetailTexture(1, 1);
		
		// create some interesting texturestates for splatting
		TextureState ts1 = DisplaySystem.getDisplaySystem().getRenderer()
				.createTextureState();
		Texture t0 = TextureManager.loadTexture(
				IslandUtils.class.getResource(resourcePath + "terrainlod.jpg"),
				Texture.MinificationFilter.Trilinear,
				Texture.MagnificationFilter.Bilinear);
		t0.setWrap(Texture.WrapMode.Repeat);
		t0.setApply(Texture.ApplyMode.Modulate);
		t0.setScale(new Vector3f(1.0f, 1.0f, 1.0f));
		ts1.setTexture(t0, 0);
		
		// //////////////////// PASS STUFF START
		// try out a passnode to use for splatting
		PassNode splattingPassNode = new PassNode("SplatPassNode");
		splattingPassNode.attachChild(page);
		
		PassNodeState passNodeState = new PassNodeState();
		passNodeState.setPassState(ts1);
		splattingPassNode.addPass(passNodeState);
		// //////////////////// PASS STUFF END
		
		// lock some things to increase the performance
		splattingPassNode.lockBounds();
		splattingPassNode.lockTransforms();
		splattingPassNode.lockShadows();
		
		initSpatial(splattingPassNode);
		return splattingPassNode;
	}

	/**
	 * Creates the sky box.
	 *
	 * @return the skybox
	 */
	public static Skybox createSkyBox() {
		Skybox skybox = new Skybox("skybox", 10, 10, 10);
		
		Texture north = TextureManager.loadTexture(
				IslandUtils.class.getResource(resourcePath + "1.jpg"),
				Texture.MinificationFilter.BilinearNearestMipMap,
				Texture.MagnificationFilter.Bilinear);
		Texture south = TextureManager.loadTexture(
				IslandUtils.class.getResource(resourcePath + "3.jpg"),
				Texture.MinificationFilter.BilinearNearestMipMap,
				Texture.MagnificationFilter.Bilinear);
		Texture east = TextureManager.loadTexture(
				IslandUtils.class.getResource(resourcePath + "2.jpg"),
				Texture.MinificationFilter.BilinearNearestMipMap,
				Texture.MagnificationFilter.Bilinear);
		Texture west = TextureManager.loadTexture(
				IslandUtils.class.getResource(resourcePath + "4.jpg"),
				Texture.MinificationFilter.BilinearNearestMipMap,
				Texture.MagnificationFilter.Bilinear);
		Texture up = TextureManager.loadTexture(
				IslandUtils.class.getResource(resourcePath + "6.jpg"),
				Texture.MinificationFilter.BilinearNearestMipMap,
				Texture.MagnificationFilter.Bilinear);
		Texture down = TextureManager.loadTexture(
				IslandUtils.class.getResource(resourcePath + "5.jpg"),
				Texture.MinificationFilter.BilinearNearestMipMap,
				Texture.MagnificationFilter.Bilinear);
		
		skybox.setTexture(Skybox.Face.North, north);
		skybox.setTexture(Skybox.Face.West, west);
		skybox.setTexture(Skybox.Face.South, south);
		skybox.setTexture(Skybox.Face.East, east);
		skybox.setTexture(Skybox.Face.Up, up);
		skybox.setTexture(Skybox.Face.Down, down);
		skybox.preloadTextures();
		
		CullState cullState = DisplaySystem.getDisplaySystem().getRenderer()
				.createCullState();
		cullState.setCullFace(CullState.Face.None);
		cullState.setEnabled(true);
		skybox.setRenderState(cullState);
		
		// ZBufferState zState =
		// DisplaySystem.getDisplaySystem().getRenderer().createZBufferState();
		// zState.setEnabled(false);
		// skybox.setRenderState(zState);
		
		FogState fs = DisplaySystem.getDisplaySystem().getRenderer()
				.createFogState();
		fs.setEnabled(false);
		skybox.setRenderState(fs);
		
		skybox.setLightCombineMode(Spatial.LightCombineMode.Off);
		skybox.setCullHint(Spatial.CullHint.Never);
		skybox.setTextureCombineMode(TextureCombineMode.Replace);
		skybox.updateRenderState();
		
		skybox.lockBounds();
		skybox.lockMeshes();

		return skybox;
	}
	
	/**
	 * Creates the splat terrain.
	 *
	 * @return the spatial
	 */
	public static Spatial createSplatTerrain() {
		// RawHeightMap heightMap = new RawHeightMap(TestIsland.class
		// .getResource(resourcePath + "test.raw"), 512,
		// RawHeightMap.FORMAT_16BITLE, false);
		Image img = new ImageIcon(IslandUtils.class.getResource(resourcePath
				+ "height129.png")).getImage();
		ImageBasedHeightMap heightMap = new ImageBasedHeightMap(img);
		
		Vector3f terrainScale = new Vector3f(5, 0.003f, 6);
		heightMap.setHeightScale(0.001f);
		TerrainPage page = new TerrainPage("Terrain", 129, heightMap.getSize(),
				terrainScale, heightMap.getHeightMap());
		page.getLocalTranslation().set(0, -9.5f, 0);
		page.setDetailTexture(1, 1);
		
		// create some interesting texturestates for splatting
		TextureState ts1 = createSplatTextureState(resourcePath
				+ "baserock.jpg", null);
		TextureState ts2 = createSplatTextureState(resourcePath
				+ "darkrock.jpg", resourcePath + "darkrockalpha.png");
		TextureState ts3 = createSplatTextureState(resourcePath
				+ "deadgrass.jpg", resourcePath + "deadalpha.png");
		TextureState ts4 = createSplatTextureState(resourcePath
				+ "nicegrass.jpg", resourcePath + "grassalpha.png");
		TextureState ts5 = createSplatTextureState(resourcePath + "road.jpg",
				resourcePath + "roadalpha.png");
		TextureState ts6 = createLightmapTextureState(resourcePath
				+ "lightmap.jpg");
		
		// alpha used for blending the passnodestates together
		BlendState as = DisplaySystem.getDisplaySystem().getRenderer()
				.createBlendState();
		as.setBlendEnabled(true);
		as.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		as.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
		as.setTestEnabled(true);
		as.setTestFunction(BlendState.TestFunction.GreaterThan);
		as.setEnabled(true);
		
		// alpha used for blending the lightmap
		BlendState as2 = DisplaySystem.getDisplaySystem().getRenderer()
				.createBlendState();
		as2.setBlendEnabled(true);
		as2.setSourceFunction(BlendState.SourceFunction.DestinationColor);
		as2.setDestinationFunction(BlendState.DestinationFunction.SourceColor);
		as2.setTestEnabled(true);
		as2.setTestFunction(BlendState.TestFunction.GreaterThan);
		as2.setEnabled(true);
		
		// //////////////////// PASS STUFF START
		// try out a passnode to use for splatting
		PassNode splattingPassNode = new PassNode("SplatPassNode");
		splattingPassNode.attachChild(page);
		
		PassNodeState passNodeState = new PassNodeState();
		passNodeState.setPassState(ts1);
		splattingPassNode.addPass(passNodeState);
		
		passNodeState = new PassNodeState();
		passNodeState.setPassState(ts2);
		passNodeState.setPassState(as);
		splattingPassNode.addPass(passNodeState);
		
		passNodeState = new PassNodeState();
		passNodeState.setPassState(ts3);
		passNodeState.setPassState(as);
		splattingPassNode.addPass(passNodeState);
		
		passNodeState = new PassNodeState();
		passNodeState.setPassState(ts4);
		passNodeState.setPassState(as);
		splattingPassNode.addPass(passNodeState);
		
		passNodeState = new PassNodeState();
		passNodeState.setPassState(ts5);
		passNodeState.setPassState(as);
		splattingPassNode.addPass(passNodeState);
		
		passNodeState = new PassNodeState();
		passNodeState.setPassState(ts6);
		passNodeState.setPassState(as2);
		splattingPassNode.addPass(passNodeState);
		// //////////////////// PASS STUFF END
		
		// lock some things to increase the performance
		splattingPassNode.lockBounds();
		splattingPassNode.lockTransforms();
		splattingPassNode.lockShadows();
		
		splattingPassNode.setCullHint(Spatial.CullHint.Dynamic);

		return splattingPassNode;
		
	}

	/**
	 * Creates the splat texture state.
	 *
	 * @param texture
	 *            the texture
	 * @param alpha
	 *            the alpha
	 * @return the texture state
	 */
	private static TextureState createSplatTextureState(String texture,
			String alpha) {
		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer()
				.createTextureState();
		
		Texture t0 = TextureManager.loadTexture(
				IslandUtils.class.getResource(texture),
				Texture.MinificationFilter.Trilinear,
				Texture.MagnificationFilter.Bilinear);
		t0.setWrap(Texture.WrapMode.Repeat);
		t0.setApply(Texture.ApplyMode.Modulate);
		t0.setScale(new Vector3f(globalSplatScale, globalSplatScale, 1.0f));
		ts.setTexture(t0, 0);
		
		if (alpha != null) {
			addAlphaSplat(ts, alpha);
		}
		
		return ts;
	}
	
	/**
	 * Inits the spatial.
	 *
	 * @param spatial
	 *            the spatial
	 */
	private static void initSpatial(Spatial spatial) {
		ZBufferState buf = DisplaySystem.getDisplaySystem().getRenderer()
				.createZBufferState();
		buf.setEnabled(true);
		buf.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
		spatial.setRenderState(buf);
		
		CullState cs = DisplaySystem.getDisplaySystem().getRenderer()
				.createCullState();
		cs.setCullFace(CullState.Face.Back);
		spatial.setRenderState(cs);
		
		spatial.setCullHint(Spatial.CullHint.Never);
		
		spatial.updateGeometricState(0.0f, true);
		spatial.updateRenderState();
	}
	
}
