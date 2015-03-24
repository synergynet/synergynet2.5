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

package synergynetframework.jme.config;

import java.io.File;

import com.jme.renderer.ColorRGBA;

/**
 * The Class AppConfig.
 */
public class AppConfig {

	
	/** The Constant CAMERA_TYPE_PARALLEL. */
	public static final int CAMERA_TYPE_PARALLEL = 1;

	// for CameraUtility
	/** The Constant CAMERA_TYPE_PERSPECTIVE. */
	public static final int CAMERA_TYPE_PERSPECTIVE = 0;
	
	// for DisplayUtility
	/** The Constant DEFAULT_ALPHA_BITS. */
	public static final int DEFAULT_ALPHA_BITS = 0;

	/** The alpha bits. */
	public static int alphaBits = DEFAULT_ALPHA_BITS;
	
	// for Application
	/** The Constant DEFAULT_APP_TITLE. */
	private static final String DEFAULT_APP_TITLE = "SynergyNet Application";
	
	/** The Constant DEFAULT_BACKGROUND_COLOUR. */
	public static final ColorRGBA DEFAULT_BACKGROUND_COLOUR = ColorRGBA.black;
	
	/** The Constant DEFAULT_CAMERA_TYPE. */
	public static final int DEFAULT_CAMERA_TYPE = CAMERA_TYPE_PERSPECTIVE;

	/** The Constant DEFAULT_DEPTH_BITS. */
	public static final int DEFAULT_DEPTH_BITS = 8;
	
	/** The Constant INPUT_DEBUGTOOLS_OFF. */
	public static final int INPUT_DEBUGTOOLS_OFF = 0;
	
	/** The Constant INPUT_DEBUGTOOLS_ON. */
	public static final int INPUT_DEBUGTOOLS_ON = 1;
	
	/** The Constant INPUT_STYLE_FIRSTPERSON. */
	public static final int INPUT_STYLE_FIRSTPERSON = 1;
	
	// for InputUtility
	/** The Constant INPUT_STYLE_KEYBOARDLOOK. */
	public static final int INPUT_STYLE_KEYBOARDLOOK = 0;
	
	/** The Constant INPUT_STYLE_NOMOVEMENT. */
	public static final int INPUT_STYLE_NOMOVEMENT = 2;

	/** The Constant DEFAULT_INPUT_STYLE. */
	public static final int DEFAULT_INPUT_STYLE = INPUT_STYLE_NOMOVEMENT;
	
	/** The Constant RENDERPASS_BLOOM. */
	public static final int RENDERPASS_BLOOM = 1;
	
	/** The Constant RENDERPASS_SHADOW. */
	public static final int RENDERPASS_SHADOW = 3;

	/** The Constant RENDERPASS_SKETCH. */
	public static final int RENDERPASS_SKETCH = 2;

	// for render passes
	/** The Constant RENDERPASS_STANDARD. */
	public static final int RENDERPASS_STANDARD = 0;

	/** The Constant SINGLE_INPUT_ONLY_OFF. */
	public static final int SINGLE_INPUT_ONLY_OFF = 0;

	// for filters
	/** The Constant SINGLE_INPUT_ONLY_ON. */
	public static final int SINGLE_INPUT_ONLY_ON = 1;

	/** The Constant STENCIL_BITS_SHADOWSUPPORT. */
	public static final int STENCIL_BITS_SHADOWSUPPORT = 4;

	/** The Constant TABLE_TYPE_EVOLUCE_JAVA. */
	public static final int TABLE_TYPE_EVOLUCE_JAVA = 3;

	// for Table Input
	/** The Constant TABLE_TYPE_JME_DIRECT_SIMULATOR. */
	public static final int TABLE_TYPE_JME_DIRECT_SIMULATOR = 0;

	/** The Constant TABLE_TYPE_JME_TUIO_SIMULATOR. */
	public static final int TABLE_TYPE_JME_TUIO_SIMULATOR = 1;
	
	// must follow index of above values
	/** The Constant TABLE_TYPE_LABELS. */
	public static final String[] TABLE_TYPE_LABELS = { "JME Direct Simulator",
			"JME TUIO Simulator", "TUIO Table", "Evoluce Java" };

	/** The Constant TABLE_TYPE_REMOTE_TUIO. */
	public static final int TABLE_TYPE_REMOTE_TUIO = 2;

	/** The Constant DEFAULT_RENDERPASS. */
	public static final int DEFAULT_RENDERPASS = RENDERPASS_STANDARD;
	
	/** The Constant DEFAULT_SAMPLES. */
	public static final int DEFAULT_SAMPLES = 0;
	
	/** The Constant DEFAULT_STENCIL_BITS. */
	public static final int DEFAULT_STENCIL_BITS = 0;

	/** The app title. */
	public static String appTitle = DEFAULT_APP_TITLE;
	
	/** The background colour. */
	public static ColorRGBA backgroundColour = DEFAULT_BACKGROUND_COLOUR;

	/** The camera type. */
	public static int cameraType = DEFAULT_CAMERA_TYPE;
	
	/** The debug tools flag. */
	public static int debugToolsFlag = INPUT_DEBUGTOOLS_OFF;
	
	/** The depth bits. */
	public static int depthBits = DEFAULT_DEPTH_BITS;
	
	// for plugins
	/** The event plugins. */
	public static String[] eventPlugins = {};
	
	/** The input style. */
	public static int inputStyle = DEFAULT_INPUT_STYLE;
	
	// for instrumentation/recording data
	/** The Constant RECORD_TABLE_INPUT_OFF. */
	public static final int RECORD_TABLE_INPUT_OFF = 0;
	
	/** The Constant RECORD_TABLE_INPUT_ON. */
	public static final int RECORD_TABLE_INPUT_ON = 1;

	/** The record table dir. */
	public static File recordTableDir = new File("log");

	/** The record table input. */
	public static int recordTableInput = RECORD_TABLE_INPUT_OFF;

	/** The render passes. */
	public static int[] renderPasses = { DEFAULT_RENDERPASS };

	/** The samples. */
	public static int samples = DEFAULT_SAMPLES;

	/** The show fps. */
	public static boolean showFPS = true;
	
	/** The single input only. */
	public static int singleInputOnly = SINGLE_INPUT_ONLY_OFF;

	/** The stencil bits. */
	public static int stencilBits = DEFAULT_STENCIL_BITS;

	/** The table type. */
	public static int tableType = TABLE_TYPE_JME_DIRECT_SIMULATOR;;

	/** The use lighting. */
	public static boolean useLighting = true;

	/**
	 * Sets the event plugin list.
	 *
	 * @param pluginClassNames
	 *            the new event plugin list
	 */
	public static void setEventPluginList(String... pluginClassNames) {
		eventPlugins = pluginClassNames;
	}

	/**
	 * Sets the render passes.
	 *
	 * @param newRenderPasses
	 *            the new render passes
	 */
	public static void setRenderPasses(int... newRenderPasses) {
		renderPasses = newRenderPasses;
	}
}
