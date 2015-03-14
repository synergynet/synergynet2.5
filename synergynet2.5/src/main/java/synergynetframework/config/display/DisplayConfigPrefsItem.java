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

package synergynetframework.config.display;

import java.util.prefs.Preferences;

import javax.swing.JPanel;

import org.lwjgl.opengl.DisplayMode;

import core.ConfigurationSystem;


import synergynetframework.config.PreferencesItem;

public class DisplayConfigPrefsItem implements PreferencesItem {

	private static Preferences prefs = ConfigurationSystem.getPreferences(DisplayConfigPrefsItem.class);

	private static final String DISPLAY_WIDTH = "DISPLAY_WIDTH";
	private static final String DISPLAY_HEIGHT = "DISPLAY_HEIGHT";
	private static final String DISPLAY_FREQ = "DISPLAY_FREQ";
	private static final String DISPLAY_DEPTH = "DISPLAY_DEPTH";
	private static final String DISPLAY_THREEDEE = "DISPLAY_3D";
	private static final String DISPLAY_FULLSCREEN = "DISPLAY_FULLSCREEN";
	private static final String DISPLAY_MIN_AA_SAMPLES = "DISPLAY_MIN_AA_SAMPLES";
	private static final String PREFS_SCENE_MONITOR = "PREFS_SCENE_MONITOR";

	public DisplayConfigPrefsItem() {}

	@Override
	public String getName() {
		return "Display";
	}

	@Override
	public JPanel getConfigurationPanel() {
		return new DisplayConfigPrefsPanel(this);
	}

	public int getCurrentDisplayModeIndex(DisplayMode[] modes) {
		for(int i = 0; i < modes.length; i++) {
			DisplayMode m = modes[i];
			if(m.getHeight() == getHeight() &&
				m.getWidth() == getWidth() &&
				m.getBitsPerPixel() == getBitDepth() &&
				m.getFrequency() == getFrequency())
			{
				return i;
			}
		}

		return -1;
	}

	public DisplayMode getCurrentDisplayMode(DisplayMode[] modes) {
		for(DisplayMode m : modes) {
			if(m.getHeight() == getHeight() &&
				m.getWidth() == getWidth() &&
				m.getBitsPerPixel() == getBitDepth() &&
				m.getFrequency() == getFrequency())
			{
				return m;
			}
		}

		return null;
	}

	public void setWidth(int w) {
		prefs.putInt(DISPLAY_WIDTH, w);
	}

	public int getWidth() {
		return prefs.getInt(DISPLAY_WIDTH, 1024);
	}

	public int getHeight() {
		return prefs.getInt(DISPLAY_HEIGHT, 768);
	}

	public void setHeight(int h) {
		prefs.putInt(DISPLAY_HEIGHT, h);
	}

	public int getBitDepth() {
		return prefs.getInt(DISPLAY_DEPTH, 16);
	}

	public void setBitDepth(int b) {
		prefs.putInt(DISPLAY_DEPTH, b);
	}

	public int getFrequency() {
		return prefs.getInt(DISPLAY_FREQ, -1);
	}

	public void setFrequency(int f) {
		prefs.putInt(DISPLAY_FREQ, f);
	}

	public boolean getFullScreen() {
		return prefs.getBoolean(DISPLAY_FULLSCREEN, false);
	}
	
	public void setThreeDee(String s) {
		prefs.put(DISPLAY_THREEDEE, s);
	}

	public String getThreeDee() {
		return prefs.get(DISPLAY_THREEDEE, "NONE");
	}
	
	public void setFullScreen(boolean fs) {
		prefs.putBoolean(DISPLAY_FULLSCREEN, fs);
	}

	public int getMinimumAntiAliasSamples() {
		return prefs.getInt(DISPLAY_MIN_AA_SAMPLES, 0);
	}
	
	public void setMinimumAntiAliasSamples(int samples) {
		prefs.putInt(DISPLAY_MIN_AA_SAMPLES, samples);
	}
	
	public boolean getShowSceneMonitor() {
		return prefs.get(PREFS_SCENE_MONITOR, "false").equals("true");
	}

	public void setShowSceneMonitor(boolean enabled) {
		prefs.put(PREFS_SCENE_MONITOR, "" + enabled);
	}
	
}