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

package synergynetframework.config.logging;

import java.awt.Rectangle;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * The Class LoggingConfigPanel.
 */
public class LoggingConfigPanel extends JPanel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1959347964561111506L;
	
	/** The prefs item. */
	private LoggingConfigPrefsItem prefsItem;
	
	/** The logging level combo box. */
	private JComboBox loggingLevelComboBox;

    /**
     * Instantiates a new logging config panel.
     *
     * @param loggingConfigPrefsItem the logging config prefs item
     */
    public LoggingConfigPanel(LoggingConfigPrefsItem loggingConfigPrefsItem) {
    	this.prefsItem = loggingConfigPrefsItem;
        initComponents();
    }

	/**
	 * Inits the components.
	 */
	private void initComponents() {

    	final JLabel loggingLevelText = new JLabel();
    	loggingLevelText.setText("Logging Level: ");
		
    	
    	loggingLevelComboBox  = new JComboBox(LoggingConfigPrefsItem.LoggingLevel.values());
    	loggingLevelComboBox.setSelectedItem(this.prefsItem.getLoggingLevel());   	
    	loggingLevelComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prefsItem.setLoggingLevel(LoggingConfigPrefsItem.LoggingLevel.valueOf(loggingLevelComboBox.getSelectedItem().toString()));
            }
        });
    	
		setLayout(null);
		
		loggingLevelText.setBounds(new Rectangle(30, 30, 200, 24));
		loggingLevelComboBox.setBounds(new Rectangle(150, 30, 100, 24));
    	
    	add(loggingLevelText);
    	add(loggingLevelComboBox);
  
    }


}
