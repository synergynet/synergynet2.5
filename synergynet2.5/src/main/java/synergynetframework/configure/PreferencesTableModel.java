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

package synergynetframework.configure;

import java.util.prefs.BackingStoreException;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;

import javax.swing.table.AbstractTableModel;

public class PreferencesTableModel extends AbstractTableModel implements PreferenceChangeListener {

	private static final long serialVersionUID = 2200658451282335653L;
	private Preferences prefs;

	public PreferencesTableModel(Preferences p) {
		this.prefs = p;
	}

	public int getColumnCount() {
		return 2;
	}

	public int getRowCount() {
		try {
			return prefs.keys().length;
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {		
		if(columnIndex == 0) { // keys
			try {
				return prefs.keys()[rowIndex];
			} catch (BackingStoreException e) {
				e.printStackTrace();
			}
			return "Error!";
		}

		if(columnIndex == 1) { // values			
			try {
				return prefs.get(prefs.keys()[rowIndex], "Error!");
			} catch (BackingStoreException e) {
				e.printStackTrace();
			}			
		}

		return "Error!";
	}
	
	 public boolean isCellEditable(int row, int col) {
		return col == 1; 
	 }
	 
	 public void setValueAt(Object obj, int row, int col) {
		if(isCellEditable(row, col)) {
			try {
				prefs.put(prefs.keys()[row], obj.toString());
			} catch (BackingStoreException e) {
				e.printStackTrace();
			}
		}
	 }

	public void preferenceChange(PreferenceChangeEvent evt) {
		fireTableDataChanged();		
	}

}
