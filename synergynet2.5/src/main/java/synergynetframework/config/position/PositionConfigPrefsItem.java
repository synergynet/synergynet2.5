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

package synergynetframework.config.position;

import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JPanel;

import synergynetframework.config.PreferencesItem;
import core.ConfigurationSystem;

/**
 * The Class PositionConfigPrefsItem.
 */
public class PositionConfigPrefsItem implements PreferencesItem {
	
	/** The Constant CURRENT_CONNECTIONS. */
	public static final String CURRENT_CONNECTIONS = "CURRENT_CONNECTIONS";
	
	/** The Constant GRID_DISTANCE_X. */
	public static final String GRID_DISTANCE_X = "GRID_DISTANCE_X";

	/** The Constant GRID_DISTANCE_Y. */
	public static final String GRID_DISTANCE_Y = "GRID_DISTANCE_Y";

	/** The Constant GRID_LIMIT_X. */
	public static final String GRID_LIMIT_X = "GRID_LIMIT_X";

	/** The Constant GRID_LIMIT_Y. */
	public static final String GRID_LIMIT_Y = "GRID_LIMIT_Y";

	/** The Constant HORIZONTAL_PLACEMENT. */
	public static final String HORIZONTAL_PLACEMENT = "HORIZONTAL_PLACEMENT";

	/** The Constant prefs. */
	private static final Preferences prefs = ConfigurationSystem
			.getPreferences(PositionConfigPrefsItem.class);

	/** The Constant PREFS_ANGLE. */
	public static final String PREFS_ANGLE = "DISPLAY_ANGLE";

	/** The Constant PREFS_LOCATION_X. */
	public static final String PREFS_LOCATION_X = "DISPLAY_LOCATION_X";

	/** The Constant PREFS_LOCATION_Y. */
	public static final String PREFS_LOCATION_Y = "DISPLAY_LOCATION_Y";

	/** The Constant PREFS_POS_DEVELOPER_MODE. */
	public static final String PREFS_POS_DEVELOPER_MODE = "POS_DEVELOPER_MODE";

	/** The Constant REFERENCE_DISTANCE. */
	public static final String REFERENCE_DISTANCE = "REFERENCE_DISTANCE";

	/** The Constant TABLE_WIDTH. */
	public static final String TABLE_WIDTH = "TABLE_WIDTH";
	
	/**
	 * Gets the angle.
	 *
	 * @return the angle
	 */
	public float getAngle() {
		return prefs.getFloat(PREFS_ANGLE, 0);
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.config.PreferencesItem#getConfigurationPanel()
	 */
	@Override
	public JPanel getConfigurationPanel() {
		JPanel panel = new JPanel();
		panel.add(new JButton("Position panel"));
		return new PositionConfigPanel(this);
	}
	
	/**
	 * Gets the developer mode.
	 *
	 * @return the developer mode
	 */
	public boolean getDeveloperMode() {
		return prefs.get(PREFS_POS_DEVELOPER_MODE, "false").equals("true");
	}

	/**
	 * Gets the grid distance x.
	 *
	 * @return the grid distance x
	 */
	public int getGridDistanceX() {
		return prefs.getInt(GRID_DISTANCE_X, 1200);
	}
	
	/**
	 * Gets the grid distance y.
	 *
	 * @return the grid distance y
	 */
	public int getGridDistanceY() {
		return prefs.getInt(GRID_DISTANCE_Y, 0);
	}
	
	/**
	 * Gets the grid limit x.
	 *
	 * @return the grid limit x
	 */
	public int getGridLimitX() {
		return prefs.getInt(GRID_LIMIT_X, 0);
	}
	
	/**
	 * Gets the grid limit y.
	 *
	 * @return the grid limit y
	 */
	public int getGridLimitY() {
		return prefs.getInt(GRID_LIMIT_Y, 0);
	}
	
	/**
	 * Gets the horizontal placement.
	 *
	 * @return the horizontal placement
	 */
	public boolean getHorizontalPlacement() {
		return prefs.get(HORIZONTAL_PLACEMENT, "false").equals("true");
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.config.PreferencesItem#getName()
	 */
	@Override
	public String getName() {
		return "Position";
	}
	
	/**
	 * Gets the x pos.
	 *
	 * @return the x pos
	 */
	public int getXPos() {
		return prefs.getInt(PREFS_LOCATION_X, 0);
	}
	
	/**
	 * Gets the y pos.
	 *
	 * @return the y pos
	 */
	public int getYPos() {
		return prefs.getInt(PREFS_LOCATION_Y, 0);
	}
	
	/**
	 * Sets the angle.
	 *
	 * @param angle
	 *            the new angle
	 */
	public void setAngle(float angle) {
		prefs.putFloat(PREFS_ANGLE, angle);
	}
	
	/**
	 * Sets the developer mode.
	 *
	 * @param selected
	 *            the new developer mode
	 */
	public void setDeveloperMode(boolean selected) {
		prefs.put(PREFS_POS_DEVELOPER_MODE, "" + selected);
		
	}

	/**
	 * Sets the horizontal placement.
	 *
	 * @param selected
	 *            the new horizontal placement
	 */
	public void setHorizontalPlacement(boolean selected) {
		prefs.put(HORIZONTAL_PLACEMENT, "" + selected);
		
	}
	
	/**
	 * Sets the x distance.
	 *
	 * @param xDis
	 *            the new x distance
	 */
	public void setXDistance(int xDis) {
		prefs.putInt(GRID_DISTANCE_X, xDis);
	}
	
	/**
	 * Sets the x limit.
	 *
	 * @param xLimit
	 *            the new x limit
	 */
	public void setXLimit(int xLimit) {
		prefs.putInt(GRID_LIMIT_X, xLimit);
	}
	
	/**
	 * Sets the x pos.
	 *
	 * @param xPos
	 *            the new x pos
	 */
	public void setXPos(int xPos) {
		prefs.putInt(PREFS_LOCATION_X, xPos);
	}
	
	/**
	 * Sets the y distance.
	 *
	 * @param yDis
	 *            the new y distance
	 */
	public void setYDistance(int yDis) {
		prefs.putInt(GRID_DISTANCE_Y, yDis);
	}
	
	/**
	 * Sets the y limit.
	 *
	 * @param yLimit
	 *            the new y limit
	 */
	public void setYLimit(int yLimit) {
		prefs.putInt(GRID_LIMIT_Y, yLimit);
	}
	
	/**
	 * Sets the y pos.
	 *
	 * @param yPos
	 *            the new y pos
	 */
	public void setYPos(int yPos) {
		prefs.putInt(PREFS_LOCATION_Y, yPos);
	}
	
}
