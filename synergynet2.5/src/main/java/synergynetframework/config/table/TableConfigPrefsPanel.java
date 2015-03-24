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

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import synergynetframework.config.table.TableConfigPrefsItem.TableType;


/**
 * The Class TableConfigPrefsPanel.
 */
public class TableConfigPrefsPanel extends JPanel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7908065805513630850L;
	
	/** The prefs item. */
	private TableConfigPrefsItem prefsItem;
	
	/**
	 *  Creates new form DeveloperConfigPanel.
	 *
	 * @param prefsItem the prefs item
	 */
    public TableConfigPrefsPanel(TableConfigPrefsItem prefsItem) {
    	this.prefsItem = prefsItem;
        initComponents();
    }
    
	/** The table label. */
	private JLabel tableLabel = new JLabel();
	
	/** The jcb. */
	private JComboBox jcb = new JComboBox(TableConfigPrefsItem.TableType.values());
	
	/** The tuio label. */
	private JLabel tuioLabel = new JLabel();
    
    /** The tuio textbox. */
    private JTextField tuioTextbox = new JTextField();
    
    /** The menu label. */
    private JLabel menuLabel = new JLabel();
    
    /** The jcb two. */
    private JComboBox jcbTwo = new JComboBox(TableConfigPrefsItem.MenuType.values());

    /**
     * Inits the components.
     */
    private void initComponents() {


		tableLabel.setText("Table Type: ");
		
		jcb.setSelectedItem(prefsItem.getTableType());
		jcb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setTableType(TableConfigPrefsItem.TableType.valueOf(jcb.getSelectedItem().toString()));
				updateTuioOptionsVisibility();
			}			
		});


		tuioLabel.setText("TUIO Port: ");
		tuioTextbox.setText(""+prefsItem.getTuioPort());
		tuioTextbox.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				store();
			}			

			@Override
			public void keyTyped(KeyEvent e) {
				store();
			}			
			
			private void store() {
				if(tuioTextbox.getText().length() > 0) {
					try{
						prefsItem.setTuioPort(Integer.parseInt(tuioTextbox.getText()));
						tuioTextbox.setForeground(Color.black);
					}catch(NumberFormatException e){
						tuioTextbox.setForeground(Color.red);
					}
				}
			}
		});

		menuLabel.setText("Menu Type: ");
		
		jcbTwo.setSelectedItem(prefsItem.getMenuType());
		jcbTwo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				prefsItem.setMenuType(TableConfigPrefsItem.MenuType.valueOf(jcbTwo.getSelectedItem().toString()));
			}			
		});

		setTableType(prefsItem.getTableType());
		
		setLayout(null);
		
		tableLabel.setBounds(new Rectangle(30, 30, 250, 24));
		jcb.setBounds(new Rectangle(130, 30, 150, 24));
		tuioLabel.setBounds(new Rectangle(30, 60, 250, 24));
		tuioTextbox.setBounds(new Rectangle(130, 60, 150, 24));
		menuLabel.setBounds(new Rectangle(30, 120, 200, 24));
		jcbTwo.setBounds(new Rectangle(130, 120, 150, 24));
		
		updateTuioOptionsVisibility();
		
		add(tableLabel);
		add(jcb);
		add(tuioLabel);
		add(tuioTextbox);
		add(menuLabel);
		add(jcbTwo);
	}
    
    /**
     * Update tuio options visibility.
     */
    private void updateTuioOptionsVisibility(){
		tuioLabel.setVisible(prefsItem.getTableType() == TableType.TUIO);
		tuioTextbox.setVisible(prefsItem.getTableType() == TableType.TUIO);
    }

	/* (non-Javadoc)
	 * @see java.awt.Component#getName()
	 */
	@Override
	public String getName() {
		return "Table Type";
	}
	
	/**
	 * Sets the table type.
	 *
	 * @param type the new table type
	 */
	public void setTableType(TableConfigPrefsItem.TableType type) {
		prefsItem.setTableType(type);
	}
	
}
