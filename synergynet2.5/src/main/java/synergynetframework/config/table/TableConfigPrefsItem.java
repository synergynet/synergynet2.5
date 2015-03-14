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

package synergynetframework.config.table;

import java.util.prefs.Preferences;

import javax.swing.JPanel;

import core.ConfigurationSystem;


import synergynetframework.config.PreferencesItem;

public class TableConfigPrefsItem implements PreferencesItem {

	private static final Preferences prefs = ConfigurationSystem.getPreferences(TableConfigPrefsItem.class);
	
	public static final String PREFS_TABLE_TYPE = "TABLE_TYPE";
	public static final String PREFS_MENU_TYPE = "MENU_TYPE";
	public static final String TUIO_PORT = "TUIO_PORT";
	
	public static enum TableType {
		JMEDIRECT, TUIOSIM, TUIO, EVOLUCE, WIN7, WIN7_64bitJava
	}
	
	public static enum MenuType {
		COMBO, AUTOMATIC, MANUAL
	}
	
	@Override
	public JPanel getConfigurationPanel() {
		return new TableConfigPrefsPanel(this);
	}

	@Override
	public String getName() {
		return "Table Type";
	}
	
	public void setTableType(TableType type) {
		prefs.put(PREFS_TABLE_TYPE, type.name());
	}

	public TableType getTableType() {			
		return TableType.valueOf(prefs.get(PREFS_TABLE_TYPE, TableType.JMEDIRECT.name()));
	}
	
	public void setMenuType(MenuType type) {
		prefs.put(PREFS_MENU_TYPE, type.name());
	}

	public MenuType getMenuType() {			
		return MenuType.valueOf(prefs.get(PREFS_MENU_TYPE, MenuType.COMBO.name()));
	}
	
	public void setTuioPort(int port) {
		prefs.putInt(TUIO_PORT, port);
	}

	public int getTuioPort() {			
		return prefs.getInt(TUIO_PORT, 3333);
	}
	
}
