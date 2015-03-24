/*
 * Copyright (c) 2003-2009 jMonkeyEngine All rights reserved. Redistribution and
 * use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met: * Redistributions of source
 * code must retain the above copyright notice, this list of conditions and the
 * following disclaimer. * Redistributions in binary form must reproduce the
 * above copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the distribution. *
 * Neither the name of 'jMonkeyEngine' nor the names of its contributors may be
 * used to endorse or promote products derived from this software without
 * specific prior written permission. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT
 * HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package apps.mt3dmaps;

import java.awt.Color;
import java.nio.FloatBuffer;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.TextLabel;
import synergynetframework.appsystem.contentsystem.items.Window;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonListener;
import synergynetframework.appsystem.table.SynergyNetAppUtils;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.jme.sysutils.CameraUtility;

import com.jme.bounding.BoundingBox;
import com.jme.math.Plane;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Skybox;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.CullState;
import com.jme.scene.state.FogState;
import com.jme.system.DisplaySystem;
import com.jmex.effects.water.WaterRenderPass;

/**
 * TestIsland shows multipass texturesplatting(6 passes) through usage of the
 * PassNode together with jME's water effect and a skybox. A simpler version of
 * the terrain without splatting is created and used for rendering into the
 * reflection/refraction of the water.
 *
 * @author Heightmap and textures originally from Jadestone(but heavily
 *         downsampled)
 * @author Rikard Herlitz (MrCoder)
 */

public class TestIsland extends DefaultSynergyNetApp {
	
	/** The far plane. */
	private float farPlane = 10000.0f;

	/** The reflection terrain. */
	private Spatial reflectionTerrain;

	/** The skybox. */
	private Skybox skybox;

	/** The splat terrain. */
	private Spatial splatTerrain;

	/** The texture scale. */
	private float textureScale = 0.07f;
	
	/** The water effect render pass. */
	private WaterRenderPass waterEffectRenderPass;

	/** The water quad. */
	private Quad waterQuad;
	
	/**
	 * Instantiates a new test island.
	 *
	 * @param info
	 *            the info
	 */
	public TestIsland(ApplicationInfo info) {
		super(info);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#addContent
	 * ()
	 */
	@Override
	public void addContent() {
		ContentSystem csys = ContentSystem
				.getContentSystemForSynergyNetApp(this);
		TextLabel text = (TextLabel) csys.createContentItem(TextLabel.class);
		text.setText("Place to go fishing");
		text.setTextColour(Color.black);
		text.setLocalLocation(100, 50);
		
		Window navigatorWindow = (Window) csys.createContentItem(Window.class,
				"navigatorwindow");
		navigatorWindow.setLocalLocation(200, 200);
		navigatorWindow.setWidth(128);
		navigatorWindow.setHeight(128);
		navigatorWindow.setScaleLimit(1f, 1f);
		navigatorWindow.setZRotateLimit(0f, 0f);
		navigatorWindow.getBackgroundFrame().setBackgroundColour(
				new Color(0, 0, 0, 0));
		navigatorWindow.getBackgroundFrame().drawImage(
				IslandUtils.class.getResource("controls/3dnavigator.png"), 0,
				0, 128, 128);
		SimpleButton simpleButton = (SimpleButton) csys.createContentItem(
				SimpleButton.class, "button");
		navigatorWindow.addSubItem(simpleButton);
		simpleButton.setLocation(-58, -58);
		simpleButton.setWidth(32);
		simpleButton.setHeight(32);
		simpleButton.addButtonListener(new SimpleButtonListener() {

			@Override
			public void buttonClicked(SimpleButton b, long id, float x,
					float y, float pressure) {
				
			}

			@Override
			public void buttonDragged(SimpleButton b, long id, float x,
					float y, float pressure) {
				
			}

			@Override
			public void buttonPressed(SimpleButton b, long id, float x,
					float y, float pressure) {
				
			}

			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				System.out.println("button pressed.");
			}
		});

		// need to change order of render passes, so remove
		// the world and ortho render passes and add them later
		pManager.remove(getWorldRenderPass());
		pManager.remove(getOrthoRenderPass());

		SynergyNetAppUtils.addTableOverlay(this);
		
		setupEnvironment();
		
		splatTerrain = IslandUtils.createSplatTerrain();
		reflectionTerrain = IslandUtils.createReflectionTerrain();
		skybox = IslandUtils.createSkyBox();
		
		splatTerrain.setModelBound(new BoundingBox());
		splatTerrain.updateModelBound();
		
		worldNode.attachChild(skybox);
		worldNode.attachChild(splatTerrain);
		
		waterEffectRenderPass = new WaterRenderPass(cam, 6, false, true);
		waterEffectRenderPass.setWaterPlane(new Plane(new Vector3f(0.0f, 1.0f,
				0.0f), 0.0f));
		waterEffectRenderPass.setClipBias(-1.0f);
		waterEffectRenderPass.setReflectionThrottle(0.0f);
		waterEffectRenderPass.setRefractionThrottle(0.0f);
		
		waterQuad = new Quad("waterQuad", 1, 1);
		FloatBuffer normBuf = waterQuad.getNormalBuffer();
		normBuf.clear();
		normBuf.put(0).put(1).put(0);
		normBuf.put(0).put(1).put(0);
		normBuf.put(0).put(1).put(0);
		normBuf.put(0).put(1).put(0);
		
		waterEffectRenderPass.setWaterEffectOnSpatial(waterQuad);
		worldNode.attachChild(waterQuad);
		
		waterEffectRenderPass.setReflectedScene(skybox);
		waterEffectRenderPass.addReflectedScene(reflectionTerrain);
		waterEffectRenderPass.setSkybox(skybox);
		pManager.add(waterEffectRenderPass);
		
		pManager.add(super.getWorldRenderPass());
		pManager.add(super.getOrthoRenderPass());
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp
	 * #getCamera()
	 */
	@Override
	protected Camera getCamera() {
		if (cam == null) {
			cam = CameraUtility.getCamera();
		}
		cam.setFrustumPerspective(45.0f, (float) DisplaySystem
				.getDisplaySystem().getWidth()
				/ (float) DisplaySystem.getDisplaySystem().getHeight(), 1f,
				farPlane);
		cam.setLocation(new Vector3f(0, 100, 0f));
		cam.lookAt(new Vector3f(0, 0, 0), Vector3f.UNIT_Y);
		cam.update();
		return cam;
	}
	
	/**
	 * Sets the texture coords.
	 *
	 * @param buffer
	 *            the buffer
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param textureScale
	 *            the texture scale
	 */
	private void setTextureCoords(int buffer, float x, float y,
			float textureScale) {
		x *= textureScale * 0.5f;
		y *= textureScale * 0.5f;
		textureScale = farPlane * textureScale;
		FloatBuffer texBuf;
		texBuf = waterQuad.getTextureCoords(buffer).coords;
		texBuf.clear();
		texBuf.put(x).put(textureScale + y);
		texBuf.put(x).put(y);
		texBuf.put(textureScale + x).put(y);
		texBuf.put(textureScale + x).put(textureScale + y);
	}

	/**
	 * Setup environment.
	 */
	private void setupEnvironment() {
		CullState cs = DisplaySystem.getDisplaySystem().getRenderer()
				.createCullState();
		cs.setCullFace(CullState.Face.Back);
		worldNode.setRenderState(cs);
		
		// lightState.detachAll();
		worldNode.setLightCombineMode(Spatial.LightCombineMode.Off);
		
		FogState fogState = DisplaySystem.getDisplaySystem().getRenderer()
				.createFogState();
		fogState.setDensity(1.0f);
		fogState.setEnabled(true);
		fogState.setColor(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
		fogState.setEnd(farPlane);
		fogState.setStart(farPlane / 10.0f);
		fogState.setDensityFunction(FogState.DensityFunction.Linear);
		fogState.setQuality(FogState.Quality.PerVertex);
		worldNode.setRenderState(fogState);
	}
	
	/**
	 * Sets the vertex coords.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param z
	 *            the z
	 */
	private void setVertexCoords(float x, float y, float z) {
		FloatBuffer vertBuf = waterQuad.getVertexBuffer();
		vertBuf.clear();
		
		vertBuf.put(x - farPlane).put(y).put(z - farPlane);
		vertBuf.put(x - farPlane).put(y).put(z + farPlane);
		vertBuf.put(x + farPlane).put(y).put(z + farPlane);
		vertBuf.put(x + farPlane).put(y).put(z - farPlane);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp
	 * #stateUpdate(float)
	 */
	protected void stateUpdate(float tpf) {
		skybox.getLocalTranslation().set(cam.getLocation());
		skybox.updateGeometricState(0.0f, true);
		Vector3f transVec = new Vector3f(cam.getLocation().x,
				waterEffectRenderPass.getWaterHeight(), cam.getLocation().z);
		setTextureCoords(0, transVec.x, -transVec.z, textureScale);
		setVertexCoords(transVec.x, transVec.y, transVec.z);
	}
	
}
