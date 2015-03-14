/*
 * Copyright (c) 2009 University of Durham, England
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

package synergynetframework.appsystem.table;

import synergynetframework.appsystem.table.appdefinitions.SynergyNetApp;
import synergynetframework.jme.gfx.TableOverlayNode;



import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.renderer.Renderer;
import com.jme.scene.Spatial.LightCombineMode;

import core.SynergyNetDesktop;

public class SynergyNetAppUtils {
	public static void addTableOverlay(SynergyNetApp app) {
		TableOverlayNode tableOverlay = new TableOverlayNode(-10f);
		tableOverlay.setRenderQueueMode(Renderer.QUEUE_ORTHO);
		tableOverlay.setLightCombineMode(LightCombineMode.Off);
		tableOverlay.updateRenderState();
		SynergyNetDesktop.getInstance().getMultiTouchInputComponent().registerMultiTouchEventListener(tableOverlay);
		app.getOrthoNode().attachChild(tableOverlay);
		app.getOrthoNode().attachChild(tableOverlay);
	}

	public static void addEscapeKeyToExit(InputHandler inputHandler) {
        KeyBindingManager.getKeyBindingManager().set("exit", KeyInput.KEY_ESCAPE);
        inputHandler.addAction(
           new InputAction() {        	
              public void performAction( InputActionEvent evt ) {
                 SynergyNetDesktop.getInstance().finish();
              }            
           }
           , "exit", false );
		
	}
}
