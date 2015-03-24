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

import synergynetframework.jme.config.AppConfig;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.system.DisplaySystem;


/**
 * The Class CameraUtility.
 */
public class CameraUtility {
	
	/**
	 * Gets the camera.
	 *
	 * @return the camera
	 */
	public static Camera getCamera() {	
		DisplaySystem display = DisplaySystem.getDisplaySystem();
		Camera cam = display.getRenderer().createCamera(display.getWidth(),display.getHeight());
        Vector3f loc = new Vector3f( 0.0f, 0.0f, 25f );
        Vector3f left = new Vector3f( -1.0f, 0.0f, 0.0f );
        Vector3f up = new Vector3f( 0.0f, 1.0f, 0.0f );
        Vector3f dir = new Vector3f( 0.0f, 0f, -1.0f );
        cam.setFrame(loc, left, up, dir);        		
		if(AppConfig.cameraType == AppConfig.CAMERA_TYPE_PERSPECTIVE) {
			cameraPerspective(cam);
		}else if(AppConfig.cameraType == AppConfig.CAMERA_TYPE_PARALLEL) {
			cameraParallel(cam);
		}
		cam.update();
		return cam;
	}
	
	/**
	 * Camera perspective.
	 *
	 * @param cam the cam
	 */
	public static void cameraPerspective(Camera cam) {
		cam.setFrustumPerspective( 45.0f, (float) DisplaySystem.getDisplaySystem().getWidth() / (float) DisplaySystem.getDisplaySystem().getHeight(), 1, 1000 );
		cam.setParallelProjection(false);
		cam.update();
	}

	/**
	 * Camera parallel.
	 *
	 * @param cam the cam
	 */
	public static void cameraParallel(Camera cam) {
		cam.setParallelProjection( true );
//		float aspect = (float) DisplaySystem.getDisplaySystem().getWidth() / DisplaySystem.getDisplaySystem().getHeight();
		
        // I really want to use the next line, but if I do, I get a "This matrix cannot be inverted"
        // problem, as also found by mangokiwi in the jME forums: 
        // http://www.jmonkeyengine.com/jmeforum/index.php?PHPSESSID=2c41fc48e4bdfe00b89649c102cb5406&topic=2895.msg38559#msg38559
        // so, instead, I have to use a much smaller scale value so that there are 10 units to a pixel
        //cam.setFrustum( -100, 1000, -display.getWidth()/2f, display.getWidth()/2f, -display.getHeight()/2f, display.getHeight()/2f);
		
		// note after reading http://www.opengl.org/resources/faq/technical/depthbuffer.htm
		// frustrum zNear cannot be negative.  Haven't tried fixing the line below in light of these findings, other than
		// now the zNear is 1, not -100.

		cam.setFrustum( 1, 1000, -DisplaySystem.getDisplaySystem().getWidth()/12.5f, DisplaySystem.getDisplaySystem().getWidth()/12.5f, -DisplaySystem.getDisplaySystem().getHeight()/12.5f, DisplaySystem.getDisplaySystem().getHeight()/12.5f);

		//		cam.setFrustum( -100, 1000, -50 * aspect, 50 * aspect, -50, 50 );
		cam.update();
	}
}
