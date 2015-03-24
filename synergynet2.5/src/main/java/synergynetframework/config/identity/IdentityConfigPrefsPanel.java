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

import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.UUID;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;


/**
 * The Class IdentityConfigPrefsPanel.
 */
public class IdentityConfigPrefsPanel extends JPanel{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7919638840040288563L;
	
	/** The prefs item. */
	private IdentityConfigPrefsItem prefsItem;	

    /**
     * Instantiates a new identity config prefs panel.
     *
     * @param prefsItem the prefs item
     */
    public IdentityConfigPrefsPanel(IdentityConfigPrefsItem prefsItem) {
    	this.prefsItem = prefsItem;
        initComponents();
    }

    /**
     * Inits the components.
     */
    private void initComponents() {
		ButtonGroup group = new ButtonGroup();
		final JRadioButton defineIdBox = new JRadioButton();
		final JRadioButton autoGenerateBox = new JRadioButton();

		final JTextField tableId = new JTextField(30);
		tableId.setText(prefsItem.getID());
		tableId.setEditable(false);
		tableId.addKeyListener(new KeyAdapter(){
			public void keyReleased(KeyEvent e){
				prefsItem.setID(tableId.getText().trim());
			}
		});
		
		defineIdBox.setText("Define Table ID:");
		defineIdBox.setSelected(prefsItem.isTableIdDefined());
		if(defineIdBox.isSelected()) tableId.setEditable(true);
		defineIdBox.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent event) {
				tableId.setEditable(defineIdBox.isSelected());
				prefsItem.setTableIdDefined(defineIdBox.isSelected());
			}
			
		});
 
		autoGenerateBox.setText("Automatically Generate ID");
		autoGenerateBox.setSelected(!prefsItem.isTableIdDefined());
		if(autoGenerateBox.isSelected()) tableId.setEditable(false);
		autoGenerateBox.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent event) {
				prefsItem.setTableIdDefined(!autoGenerateBox.isSelected());
				if(autoGenerateBox.isSelected()){
					prefsItem.setID(UUID.randomUUID().toString());
					tableId.setEditable(!autoGenerateBox.isSelected());
					tableId.setText(prefsItem.getID());
				}
			}
		});
		
	    group.add(defineIdBox);
	    group.add(autoGenerateBox);
		
		setLayout(null);
		
		defineIdBox.setBounds(new Rectangle(30, 30, 150, 24));
		tableId.setBounds(new Rectangle(180, 30, 300, 24));
		autoGenerateBox.setBounds(new Rectangle(30, 60, 240, 24));
		
		add(defineIdBox);
		add(autoGenerateBox);
		add(tableId);	
	}
}