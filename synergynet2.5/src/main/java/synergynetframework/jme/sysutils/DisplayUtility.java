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

import java.awt.geom.Point2D.Float;
import java.util.logging.Logger;

import synergynetframework.jme.config.AppConfig;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.system.DisplaySystem;
import com.jme.system.PropertiesIO;

/**
 * The Class DisplayUtility.
 */
public class DisplayUtility {

	/**
	 * Display driver info.
	 *
	 * @param logger
	 *            the logger
	 */
	public static void displayDriverInfo(Logger logger) {
		DisplaySystem display = DisplaySystem.getDisplaySystem();

		logger.info("Running on: " + display.getAdapter()
				+ "\nDriver version: " + display.getDriverVersion() + "\n"
				+ display.getDisplayVendor() + " - "
				+ display.getDisplayRenderer() + " - "
				+ display.getDisplayAPIVersion());
	}
	
	/**
	 * Gets the display.
	 *
	 * @param properties
	 *            the properties
	 * @return the display
	 */
	public static DisplaySystem getDisplay(PropertiesIO properties) {
		DisplaySystem display = DisplaySystem.getDisplaySystem(properties
				.getRenderer());
		display.setMinDepthBits(AppConfig.depthBits);
		display.setMinStencilBits(AppConfig.stencilBits);
		display.setMinAlphaBits(AppConfig.alphaBits);
		display.setMinSamples(AppConfig.samples);
		display.createWindow(properties.getWidth(), properties.getHeight(),
				properties.getDepth(), properties.getFreq(),
				properties.getFullscreen());
		display.getRenderer().setBackgroundColor(ColorRGBA.black);
		display.getRenderer().setBackgroundColor(AppConfig.backgroundColour);
		display.setTitle(AppConfig.appTitle);
		return display;
	}

	/**
	 * Table to screen.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param store
	 *            the store
	 */
	public static void tableToScreen(float x, float y, Vector2f store) {
		int dh = DisplaySystem.getDisplaySystem().getRenderer().getHeight();
		int dw = DisplaySystem.getDisplaySystem().getRenderer().getWidth();
		store.x = dw * x;
		store.y = dh - (dh * y);
	}

	/**
	 * Table to screen.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param store
	 *            the store
	 */
	public static void tableToScreen(float x, float y, Vector3f store) {
		int dh = DisplaySystem.getDisplaySystem().getRenderer().getHeight();
		int dw = DisplaySystem.getDisplaySystem().getRenderer().getWidth();
		store.x = dw * x;
		store.y = dh - (dh * y);
	}
	
	/**
	 * Table to screen.
	 *
	 * @param position
	 *            the position
	 */
	public static void tableToScreen(Float position) {
		int dh = DisplaySystem.getDisplaySystem().getRenderer().getHeight();
		int dw = DisplaySystem.getDisplaySystem().getRenderer().getWidth();
		float x = dw * position.x;
		float y = dh - (dh * position.y);
		position.x = x;
		position.y = y;
	}
	
	/**
	 * Table to screen.
	 *
	 * @param in
	 *            the in
	 * @param out
	 *            the out
	 */
	public static void tableToScreen(Vector2f in, Vector2f out) {
		int dh = DisplaySystem.getDisplaySystem().getRenderer().getHeight();
		int dw = DisplaySystem.getDisplaySystem().getRenderer().getWidth();
		out.x = dw * in.x;
		out.y = dh - (dh * in.y);
	}
	
	/**
	 * Table to screen.
	 *
	 * @param position
	 *            the position
	 * @param out
	 *            the out
	 */
	public static void tableToScreen(Vector2f position, Vector3f out) {
		int dh = DisplaySystem.getDisplaySystem().getRenderer().getHeight();
		int dw = DisplaySystem.getDisplaySystem().getRenderer().getWidth();
		out.x = dw * position.x;
		out.y = dh - (dh * position.y);
	}
}
