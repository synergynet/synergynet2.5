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

package synergynetframework.config.identity;

import java.util.UUID;
import java.util.prefs.Preferences;

import javax.swing.JPanel;

import core.ConfigurationSystem;


import synergynetframework.config.PreferencesItem;


/**
 * The Class IdentityConfigPrefsItem.
 */
public class IdentityConfigPrefsItem implements PreferencesItem {

	/** The Constant prefs. */
	private static final Preferences prefs = ConfigurationSystem.getPreferences(IdentityConfigPrefsItem.class);
	
	/** The Constant PREFS_TABLE_ID. */
	private static final String PREFS_TABLE_ID = "TABLE_ID";
	
	/** The Constant PREFS_DEFINE_TABLE_ID. */
	private static final String PREFS_DEFINE_TABLE_ID = "FALSE";
	
	/* (non-Javadoc)
	 * @see synergynetframework.config.PreferencesItem#getConfigurationPanel()
	 */
	@Override
	public JPanel getConfigurationPanel() {
		return new IdentityConfigPrefsPanel(this);
	}

	/* (non-Javadoc)
	 * @see synergynetframework.config.PreferencesItem#getName()
	 */
	@Override
	public String getName() {
		return "Identity";
	}
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getID() {
		if(!isTableIdDefined()) setID(UUID.randomUUID().toString());
		return prefs.get(PREFS_TABLE_ID, "<no identity>");
	}

	/**
	 * Sets the id.
	 *
	 * @param uid the new id
	 */
	public void setID(String uid) {
		prefs.put(PREFS_TABLE_ID, uid);		
	}
	
	/**
	 * Checks if is table id defined.
	 *
	 * @return true, if is table id defined
	 */
	public boolean isTableIdDefined(){
		return prefs.getBoolean(PREFS_DEFINE_TABLE_ID, false);
	}
	
	/**
	 * Sets the table id defined.
	 *
	 * @param isTableIdDefined the new table id defined
	 */
	public void setTableIdDefined(boolean isTableIdDefined){
		prefs.putBoolean(PREFS_DEFINE_TABLE_ID, isTableIdDefined);
	}

}
