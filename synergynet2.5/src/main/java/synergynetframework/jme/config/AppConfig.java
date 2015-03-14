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

package synergynetframework.jme.config;

import java.io.File;

import com.jme.renderer.ColorRGBA;

public class AppConfig {
	
	// for Application
	private static final String DEFAULT_APP_TITLE = "SynergyNet Application";
	public static String appTitle = DEFAULT_APP_TITLE;	
	public static boolean showFPS = true;
	public static boolean useLighting = true;
	
	// for Table Input
	public static final int TABLE_TYPE_JME_DIRECT_SIMULATOR = 0;
	public static final int TABLE_TYPE_JME_TUIO_SIMULATOR = 1;			
	public static final int TABLE_TYPE_REMOTE_TUIO = 2;
	public static final int TABLE_TYPE_EVOLUCE_JAVA = 3;	

	// must follow index of above values
	public static final String[] TABLE_TYPE_LABELS = {"JME Direct Simulator", "JME TUIO Simulator", "TUIO Table", "Evoluce Java" };	
	public static int tableType = TABLE_TYPE_JME_DIRECT_SIMULATOR;

	// for instrumentation/recording data
	public static final int RECORD_TABLE_INPUT_OFF = 0;
	public static final int RECORD_TABLE_INPUT_ON = 1;
	public static int recordTableInput = RECORD_TABLE_INPUT_OFF;
	public static File recordTableDir = new File("log");
		
	// for DisplayUtility
	public static final int DEFAULT_ALPHA_BITS = 0;
    public static int alphaBits = DEFAULT_ALPHA_BITS;
    public static final int DEFAULT_DEPTH_BITS = 8;
    public static int depthBits = DEFAULT_DEPTH_BITS;
    public static final int DEFAULT_STENCIL_BITS = 0;
    public static final int STENCIL_BITS_SHADOWSUPPORT = 4;
    public static int stencilBits = DEFAULT_STENCIL_BITS;
    public static final int DEFAULT_SAMPLES = 0;
    public static int samples = DEFAULT_SAMPLES;
    public static final ColorRGBA DEFAULT_BACKGROUND_COLOUR = ColorRGBA.black;
    public static ColorRGBA backgroundColour = DEFAULT_BACKGROUND_COLOUR;
	
    // for CameraUtility
	public static final int CAMERA_TYPE_PERSPECTIVE = 0;
	public static final int CAMERA_TYPE_PARALLEL = 1;
	public static final int DEFAULT_CAMERA_TYPE = CAMERA_TYPE_PERSPECTIVE;
	public static int cameraType = DEFAULT_CAMERA_TYPE;
	
	
	// for InputUtility
	public static final int INPUT_STYLE_KEYBOARDLOOK = 0;
	public static final int INPUT_STYLE_FIRSTPERSON = 1;
	public static final int INPUT_STYLE_NOMOVEMENT = 2;
	public static final int DEFAULT_INPUT_STYLE = INPUT_STYLE_NOMOVEMENT;
	public static int inputStyle = DEFAULT_INPUT_STYLE;
	
	public static final int INPUT_DEBUGTOOLS_OFF = 0;
	public static final int INPUT_DEBUGTOOLS_ON = 1;
	public static int debugToolsFlag = INPUT_DEBUGTOOLS_OFF;

	// for render passes
	public static final int RENDERPASS_STANDARD = 0;
	public static final int RENDERPASS_BLOOM = 1;
	public static final int RENDERPASS_SKETCH = 2;
	public static final int RENDERPASS_SHADOW = 3;
	public static final int DEFAULT_RENDERPASS = RENDERPASS_STANDARD;
	public static int[] renderPasses = { DEFAULT_RENDERPASS };		
	public static void setRenderPasses(int... newRenderPasses) { renderPasses = newRenderPasses;}
	
	// for plugins
	public static String[] eventPlugins = {};
	
	public static void setEventPluginList(String... pluginClassNames) { eventPlugins = pluginClassNames;};
	
	// for filters
	public static final int SINGLE_INPUT_ONLY_ON = 1;
	public static final int SINGLE_INPUT_ONLY_OFF = 0;
	public static int singleInputOnly = SINGLE_INPUT_ONLY_OFF;
}
