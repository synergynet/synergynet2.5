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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;


/**
 * The Class Configure.
 */
public abstract class Configure extends JFrame {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8861853020575988727L;
	
	/** The table. */
	protected JTable table;
	
	/**
	 * Instantiates a new configure.
	 *
	 * @throws ClassNotFoundException the class not found exception
	 */
	public Configure() throws ClassNotFoundException {
		final JFrame frame = this;
		
		JDialog.setDefaultLookAndFeelDecorated(true);
	    String[] selectionValues = getSelectionValues();
	    String initialSelection = selectionValues[0];
	    Object selection = JOptionPane.showInputDialog(null, "Select class to configure",
	        "Preferences", JOptionPane.QUESTION_MESSAGE, null, selectionValues, initialSelection);	    

	    notifyPackageSelected((String) selection);
	    
	    final Preferences prefs = Preferences.userNodeForPackage(Class.forName((String) selection));
	    		
		table = new JTable(new PreferencesTableModel(prefs));
		prefs.addPreferenceChangeListener((PreferencesTableModel)table.getModel());
		table.setPreferredSize(new Dimension(640,480));
		getContentPane().setLayout(new BorderLayout());
		add(table, BorderLayout.CENTER);
		
		JPanel controls = new JPanel();
		JButton add = new JButton("Add");
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String key = JOptionPane.showInputDialog("Key:");
				String value = JOptionPane.showInputDialog("Value:");
				prefs.put(key, value);
			}			
		});
		controls.add(add);
		
		JButton del = new JButton("Delete");
		del.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					String key = prefs.keys()[table.getSelectedRow()];
					if(key != null) {
						int result = JOptionPane.showConfirmDialog(frame, "Are you sure?", "Delete key " + key, JOptionPane.OK_CANCEL_OPTION);
						if(result == JOptionPane.OK_OPTION) {
							prefs.remove(key);
						}
					}
				}catch(Exception ex) {
					System.out.println("Problem deleting key.");
				}
			}			
		});
		controls.add(del);
		
		JButton exit = new JButton("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);	
			}			
		});		
		controls.add(exit);

		
		add(controls, BorderLayout.SOUTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
	}

	/**
	 * Notify package selected.
	 *
	 * @param selection the selection
	 */
	public abstract void notifyPackageSelected(String selection);
	
	/**
	 * Gets the selection values.
	 *
	 * @return the selection values
	 */
	public abstract String[] getSelectionValues();
	
}
