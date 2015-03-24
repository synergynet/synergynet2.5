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

package synergynetframework.config.display;

import java.util.prefs.Preferences;

import javax.swing.JPanel;

import org.lwjgl.opengl.DisplayMode;

import synergynetframework.config.PreferencesItem;
import core.ConfigurationSystem;

/**
 * The Class DisplayConfigPrefsItem.
 */
public class DisplayConfigPrefsItem implements PreferencesItem {
	
	/** The Constant DISPLAY_DEPTH. */
	private static final String DISPLAY_DEPTH = "DISPLAY_DEPTH";
	
	/** The Constant DISPLAY_FREQ. */
	private static final String DISPLAY_FREQ = "DISPLAY_FREQ";

	/** The Constant DISPLAY_FULLSCREEN. */
	private static final String DISPLAY_FULLSCREEN = "DISPLAY_FULLSCREEN";

	/** The Constant DISPLAY_HEIGHT. */
	private static final String DISPLAY_HEIGHT = "DISPLAY_HEIGHT";

	/** The Constant DISPLAY_MIN_AA_SAMPLES. */
	private static final String DISPLAY_MIN_AA_SAMPLES = "DISPLAY_MIN_AA_SAMPLES";

	/** The Constant DISPLAY_THREEDEE. */
	private static final String DISPLAY_THREEDEE = "DISPLAY_3D";

	/** The Constant DISPLAY_WIDTH. */
	private static final String DISPLAY_WIDTH = "DISPLAY_WIDTH";

	/** The prefs. */
	private static Preferences prefs = ConfigurationSystem
			.getPreferences(DisplayConfigPrefsItem.class);

	/** The Constant PREFS_SCENE_MONITOR. */
	private static final String PREFS_SCENE_MONITOR = "PREFS_SCENE_MONITOR";
	
	/**
	 * Instantiates a new display config prefs item.
	 */
	public DisplayConfigPrefsItem() {
	}
	
	/**
	 * Gets the bit depth.
	 *
	 * @return the bit depth
	 */
	public int getBitDepth() {
		return prefs.getInt(DISPLAY_DEPTH, 16);
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.config.PreferencesItem#getConfigurationPanel()
	 */
	@Override
	public JPanel getConfigurationPanel() {
		return new DisplayConfigPrefsPanel(this);
	}
	
	/**
	 * Gets the current display mode.
	 *
	 * @param modes
	 *            the modes
	 * @return the current display mode
	 */
	public DisplayMode getCurrentDisplayMode(DisplayMode[] modes) {
		for (DisplayMode m : modes) {
			if ((m.getHeight() == getHeight()) && (m.getWidth() == getWidth())
					&& (m.getBitsPerPixel() == getBitDepth())
					&& (m.getFrequency() == getFrequency())) {
				return m;
			}
		}
		
		return null;
	}
	
	/**
	 * Gets the current display mode index.
	 *
	 * @param modes
	 *            the modes
	 * @return the current display mode index
	 */
	public int getCurrentDisplayModeIndex(DisplayMode[] modes) {
		for (int i = 0; i < modes.length; i++) {
			DisplayMode m = modes[i];
			if ((m.getHeight() == getHeight()) && (m.getWidth() == getWidth())
					&& (m.getBitsPerPixel() == getBitDepth())
					&& (m.getFrequency() == getFrequency())) {
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * Gets the frequency.
	 *
	 * @return the frequency
	 */
	public int getFrequency() {
		return prefs.getInt(DISPLAY_FREQ, -1);
	}
	
	/**
	 * Gets the full screen.
	 *
	 * @return the full screen
	 */
	public boolean getFullScreen() {
		return prefs.getBoolean(DISPLAY_FULLSCREEN, false);
	}
	
	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public int getHeight() {
		return prefs.getInt(DISPLAY_HEIGHT, 768);
	}
	
	/**
	 * Gets the minimum anti alias samples.
	 *
	 * @return the minimum anti alias samples
	 */
	public int getMinimumAntiAliasSamples() {
		return prefs.getInt(DISPLAY_MIN_AA_SAMPLES, 0);
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.config.PreferencesItem#getName()
	 */
	@Override
	public String getName() {
		return "Display";
	}
	
	/**
	 * Gets the show scene monitor.
	 *
	 * @return the show scene monitor
	 */
	public boolean getShowSceneMonitor() {
		return prefs.get(PREFS_SCENE_MONITOR, "false").equals("true");
	}
	
	/**
	 * Gets the three dee.
	 *
	 * @return the three dee
	 */
	public String getThreeDee() {
		return prefs.get(DISPLAY_THREEDEE, "NONE");
	}
	
	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public int getWidth() {
		return prefs.getInt(DISPLAY_WIDTH, 1024);
	}
	
	/**
	 * Sets the bit depth.
	 *
	 * @param b
	 *            the new bit depth
	 */
	public void setBitDepth(int b) {
		prefs.putInt(DISPLAY_DEPTH, b);
	}

	/**
	 * Sets the frequency.
	 *
	 * @param f
	 *            the new frequency
	 */
	public void setFrequency(int f) {
		prefs.putInt(DISPLAY_FREQ, f);
	}
	
	/**
	 * Sets the full screen.
	 *
	 * @param fs
	 *            the new full screen
	 */
	public void setFullScreen(boolean fs) {
		prefs.putBoolean(DISPLAY_FULLSCREEN, fs);
	}

	/**
	 * Sets the height.
	 *
	 * @param h
	 *            the new height
	 */
	public void setHeight(int h) {
		prefs.putInt(DISPLAY_HEIGHT, h);
	}
	
	/**
	 * Sets the minimum anti alias samples.
	 *
	 * @param samples
	 *            the new minimum anti alias samples
	 */
	public void setMinimumAntiAliasSamples(int samples) {
		prefs.putInt(DISPLAY_MIN_AA_SAMPLES, samples);
	}

	/**
	 * Sets the show scene monitor.
	 *
	 * @param enabled
	 *            the new show scene monitor
	 */
	public void setShowSceneMonitor(boolean enabled) {
		prefs.put(PREFS_SCENE_MONITOR, "" + enabled);
	}

	/**
	 * Sets the three dee.
	 *
	 * @param s
	 *            the new three dee
	 */
	public void setThreeDee(String s) {
		prefs.put(DISPLAY_THREEDEE, s);
	}
	
	/**
	 * Sets the width.
	 *
	 * @param w
	 *            the new width
	 */
	public void setWidth(int w) {
		prefs.putInt(DISPLAY_WIDTH, w);
	}

}