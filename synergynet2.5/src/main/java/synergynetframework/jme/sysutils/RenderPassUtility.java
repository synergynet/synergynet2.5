/*
 * Copyright (c) 2008 University of Durham, England All rights reserved.
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

package synergynetframework.jme.sysutils;

import java.util.logging.Logger;

import synergynetframework.jme.config.AppConfig;

import com.jme.renderer.Camera;
import com.jme.renderer.pass.BasicPassManager;
import com.jme.renderer.pass.RenderPass;
import com.jme.renderer.pass.ShadowedRenderPass;
import com.jme.renderer.pass.ShadowedRenderPass.LightingMethod;
import com.jme.scene.Node;
import com.jmex.effects.glsl.BloomRenderPass;
import com.jmex.effects.glsl.SketchRenderPass;

/**
 * The Class RenderPassUtility.
 */
public class RenderPassUtility {

	/** The logger. */
	private static Logger logger = Logger.getLogger(RenderPassUtility.class
			.getName());

	/**
	 * Adds the bloom render pass.
	 *
	 * @param cam
	 *            the cam
	 * @param node
	 *            the node
	 * @param passManager
	 *            the pass manager
	 */
	public static void addBloomRenderPass(Camera cam, Node node,
			BasicPassManager passManager) {
		BloomRenderPass bloomRenderPass = new BloomRenderPass(cam, 4);
		if (bloomRenderPass.isSupported()) {
			bloomRenderPass.add(node);
			bloomRenderPass.setUseCurrentScene(true);
			passManager.add(bloomRenderPass);
		}
	}

	/**
	 * Adds the default render pass.
	 *
	 * @param cam
	 *            the cam
	 * @param node
	 *            the node
	 * @param passManager
	 *            the pass manager
	 */
	private static void addDefaultRenderPass(Camera cam, Node node,
			BasicPassManager passManager) {
		RenderPass rootPass = new RenderPass();
		rootPass.add(node);
		passManager.add(rootPass);
	}

	/**
	 * Adds the render passes from app config.
	 *
	 * @param cam
	 *            the cam
	 * @param node
	 *            the node
	 * @param passManager
	 *            the pass manager
	 */
	public static void addRenderPassesFromAppConfig(Camera cam, Node node,
			BasicPassManager passManager) {
		for (int i = 0; i < AppConfig.renderPasses.length; i++) {
			switch (AppConfig.renderPasses[i]) {
			
				case AppConfig.RENDERPASS_STANDARD: {
					addDefaultRenderPass(cam, node, passManager);
					break;
				}
				
				case AppConfig.RENDERPASS_BLOOM: {
					addBloomRenderPass(cam, node, passManager);
					break;
				}
				
				case AppConfig.RENDERPASS_SKETCH: {
					addSketchRenderPass(cam, node, passManager);
					break;
				}
				
				case AppConfig.RENDERPASS_SHADOW: {
					if (AppConfig.stencilBits < AppConfig.STENCIL_BITS_SHADOWSUPPORT) {
						logger.warning("AppConfig not currently set to use STENCIL_BITS_SHADOWSUPPORT for stencilBits");
					}
					addShadowRenderPass(cam, node, passManager);
				}
			}
		}
	}

	/**
	 * Adds the shadow render pass.
	 *
	 * @param cam
	 *            the cam
	 * @param node
	 *            the node
	 * @param passManager
	 *            the pass manager
	 */
	public static void addShadowRenderPass(Camera cam, Node node,
			BasicPassManager passManager) {
		ShadowedRenderPass shadowPass = new ShadowedRenderPass();
		shadowPass.setRenderShadows(true);
		shadowPass.setLightingMethod(LightingMethod.Additive);
		passManager.add(shadowPass);
	}

	/**
	 * Adds the sketch render pass.
	 *
	 * @param cam
	 *            the cam
	 * @param node
	 *            the node
	 * @param passManager
	 *            the pass manager
	 */
	public static void addSketchRenderPass(Camera cam, Node node,
			BasicPassManager passManager) {
		SketchRenderPass sketchRenderPass = new SketchRenderPass(cam, 2);
		if (sketchRenderPass.isSupported()) {
			sketchRenderPass.add(node);
			passManager.add(sketchRenderPass);
		}
	}
}
