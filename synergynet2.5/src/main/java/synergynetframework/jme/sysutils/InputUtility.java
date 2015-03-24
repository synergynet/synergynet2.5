/*
 * Copyright (c) 2008 University of Durham, England
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'SynergyNet' nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package synergynetframework.jme.sysutils;

import java.util.logging.Logger;

import synergynetframework.jme.config.AppConfig;

import com.jme.input.FirstPersonHandler;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.KeyboardLookHandler;
import com.jme.input.MouseInput;
import com.jme.renderer.Camera;
import com.jme.system.DisplaySystem;


/**
 * The Class InputUtility.
 */
public class InputUtility {

	/** The pause. */
	public static boolean pause = false;
	
	/** The wireframe mode. */
	public static boolean wireframeMode = false;
	
	/** The use lighting. */
	public static boolean useLighting = AppConfig.useLighting;
	
	/** The show bounds. */
	public static boolean showBounds = false;
	
	/** The show depth. */
	public static boolean showDepth = false;
	
	/** The show normals. */
	public static boolean showNormals = false;
	
	/** The should finish. */
	public static boolean shouldFinish = false;

	/**
	 * Setup keys.
	 */
	public static void setupKeys() {
//		if(AppConfig.debugToolsFlag == AppConfig.INPUT_DEBUGTOOLS_ON) {
			KeyBindingManager.getKeyBindingManager().set( "toggle_pause", KeyInput.KEY_P );
			KeyBindingManager.getKeyBindingManager().set( "toggle_wire", KeyInput.KEY_T );
			KeyBindingManager.getKeyBindingManager().set( "toggle_lights", KeyInput.KEY_L );
			KeyBindingManager.getKeyBindingManager().set( "toggle_bounds", KeyInput.KEY_B );
			KeyBindingManager.getKeyBindingManager().set( "toggle_normals", KeyInput.KEY_N );
			KeyBindingManager.getKeyBindingManager().set( "camera_out", KeyInput.KEY_C );
			KeyBindingManager.getKeyBindingManager().set( "screen_shot", KeyInput.KEY_F1 );
			KeyBindingManager.getKeyBindingManager().set( "exit", KeyInput.KEY_ESCAPE );
			KeyBindingManager.getKeyBindingManager().set( "parallel_projection", KeyInput.KEY_F2 );
			KeyBindingManager.getKeyBindingManager().set( "toggle_depth", KeyInput.KEY_F3 );
			KeyBindingManager.getKeyBindingManager().set("mem_report", KeyInput.KEY_R);
//		}
	}


	/**
	 * Gets the input handler.
	 *
	 * @param cam the cam
	 * @return the input handler
	 */
	public static InputHandler getInputHandler(Camera cam) {
		InputHandler handler = null;
		if(AppConfig.inputStyle == AppConfig.INPUT_STYLE_NOMOVEMENT)
			handler = new InputHandler();
		else if(AppConfig.inputStyle == AppConfig.INPUT_STYLE_KEYBOARDLOOK)
			handler = getKeyboardLookingHandler(cam);
		else if(AppConfig.inputStyle == AppConfig.INPUT_STYLE_FIRSTPERSON)
			handler = getFirstPersonHandler(cam);
		return handler;
	}

	/**
	 * Gets the first person handler.
	 *
	 * @param cam the cam
	 * @return the first person handler
	 */
	private static InputHandler getFirstPersonHandler(Camera cam) {
		return new FirstPersonHandler(cam, 50, 1);
	}


	/**
	 * Gets the keyboard looking handler.
	 *
	 * @param cam the cam
	 * @return the keyboard looking handler
	 */
	public static InputHandler getKeyboardLookingHandler(Camera cam) {
		return new KeyboardLookHandler(cam, 50, 1);
	}

	/**
	 * Check keys.
	 *
	 * @param logger the logger
	 * @param cam the cam
	 */
	public static void checkKeys(Logger logger, Camera cam) {
		if ( KeyBindingManager.getKeyBindingManager().isValidCommand("toggle_pause", false ) ) {
			pause = !pause;
		}

		/** If toggle_wire is a valid command (via key T), change wirestates. */
		if ( KeyBindingManager.getKeyBindingManager().isValidCommand("toggle_wire", false ) ) {
			wireframeMode = !wireframeMode;
		}

		/** If toggle_lights is a valid command (via key L), change lightstate. */
		if ( KeyBindingManager.getKeyBindingManager().isValidCommand("toggle_lights", false ) ) {
			useLighting = !useLighting;
		}
		/** If toggle_bounds is a valid command (via key B), change bounds. */
		if ( KeyBindingManager.getKeyBindingManager().isValidCommand("toggle_bounds", false ) ) {
			showBounds = !showBounds;
		}

		/** If toggle_depth is a valid command (via key F3), change depth. */
		if ( KeyBindingManager.getKeyBindingManager().isValidCommand("toggle_depth", false ) ) {
			showDepth = !showDepth;
		}

		if ( KeyBindingManager.getKeyBindingManager().isValidCommand("toggle_normals", false ) ) {
			showNormals = !showNormals;
		}
		/** If camera_out is a valid command (via key C), show camera location. */
		if ( KeyBindingManager.getKeyBindingManager().isValidCommand("camera_out", false ) ) {
			logger.info( "Camera at: " + DisplaySystem.getDisplaySystem().getRenderer().getCamera().getLocation() );
			logger.info(" Camera direction " + DisplaySystem.getDisplaySystem().getRenderer().getCamera().getDirection());
		}

		if ( KeyBindingManager.getKeyBindingManager().isValidCommand("screen_shot", false ) ) {
			DisplaySystem.getDisplaySystem().getRenderer().takeScreenShot( "SimpleGameScreenShot" );
		}

		if ( KeyBindingManager.getKeyBindingManager().isValidCommand("parallel_projection", false ) ) {
			if ( cam.isParallelProjection() ) {
				CameraUtility.cameraPerspective(cam);
			}
			else {
				CameraUtility.cameraParallel(cam);
			}
		}

		if ( KeyBindingManager.getKeyBindingManager().isValidCommand("mem_report", false ) ) {
			long totMem = Runtime.getRuntime().totalMemory();
			long freeMem = Runtime.getRuntime().freeMemory();
			long maxMem = Runtime.getRuntime().maxMemory();
			logger.info("|*|*|  Memory Stats  |*|*|");
			logger.info("Total memory: "+(totMem>>10)+" kb");
			logger.info("Free memory: "+(freeMem>>10)+" kb");
			logger.info("Max memory: "+(maxMem>>10)+" kb");
		}

		if ( KeyBindingManager.getKeyBindingManager().isValidCommand( "exit", false ) ) {
			shouldFinish  = true;
		}
	}	

	/**
	 * Setup mouse input.
	 */
	public static void setupMouseInput() {
		if( AppConfig.tableType == AppConfig.TABLE_TYPE_JME_DIRECT_SIMULATOR ||
			AppConfig.tableType == AppConfig.TABLE_TYPE_JME_TUIO_SIMULATOR) {			
			MouseInput.get().setCursorVisible(true);
		}
	}


}
