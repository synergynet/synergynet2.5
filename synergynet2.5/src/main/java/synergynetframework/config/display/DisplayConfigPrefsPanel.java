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

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import synergynetframework.jme.sysutils.StereoRenderPass.StereoMode;

public class DisplayConfigPrefsPanel extends JPanel {

	private static final long serialVersionUID = 3103711516544011549L;
	private DisplayConfigPrefsItem prefsItem;

	private JComboBox jcb = new JComboBox();
	private JComboBox jcbd = new JComboBox();
	private JCheckBox fullscreen;
	private JLabel jLabelDisplaySize = new JLabel();
	private JLabel jLabelThreeDeeMode = new JLabel();
    private JLabel lblAntiAlias = new JLabel();
    private JTextField txtAntiAliasSamples = new JTextField();
    private JCheckBox cbEnableSceneMonitor = new JCheckBox();

	/** Creates new form DeveloperConfigPanel
	 * @param developerConfigPrefsItem */
    public DisplayConfigPrefsPanel(DisplayConfigPrefsItem prefsItem) {
    	this.prefsItem = prefsItem;
        initComponents();
    }

    private void initComponents() {

		jLabelDisplaySize.setText("Display Size:");

		DisplayMode[] modes;
		try {
			modes = Display.getAvailableDisplayModes();
			Arrays.sort(modes, new DisplayModeComparator());
			Display.destroy();

			jcb = new JComboBox(modes);
			jcb.setSelectedIndex(prefsItem.getCurrentDisplayModeIndex(modes));
			jcb.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == ItemEvent.SELECTED) {
						DisplayMode m = (DisplayMode) jcb.getSelectedItem();
						prefsItem.setWidth(m.getWidth());
						prefsItem.setHeight(m.getHeight());
						prefsItem.setBitDepth(m.getBitsPerPixel());
						prefsItem.setFrequency(m.getFrequency());
					}
				}

			});

		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		fullscreen = new JCheckBox("Full Screen", prefsItem.getFullScreen());
		fullscreen.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
		fullscreen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				prefsItem.setFullScreen(fullscreen.isSelected());
			}
		});

        lblAntiAlias.setText("Anti-alias min samples:");
        txtAntiAliasSamples.setText(""+prefsItem.getMinimumAntiAliasSamples());
        txtAntiAliasSamples.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				store();
			}			

			@Override
			public void keyTyped(KeyEvent e) {
				store();
			}			
			
			private void store() {
				if(txtAntiAliasSamples.getText().length() > 0) {
					try{
						prefsItem.setMinimumAntiAliasSamples(Integer.parseInt(txtAntiAliasSamples.getText()));
						txtAntiAliasSamples.setForeground(Color.black);
					}catch(NumberFormatException e){
						txtAntiAliasSamples.setForeground(Color.red);
					}
				}
			}
		});
        
        jLabelThreeDeeMode.setText("Stereo Mode:");
        
        String[] threeDModes = {"NONE", StereoMode.ANAGLYPH.toString(), StereoMode.SIDE_BY_SIDE.toString(), StereoMode.STEREO_BUFFER.toString()};
        
        int choice = 0;
        String current = prefsItem.getThreeDee();
        
        for (int i = 0; i < threeDModes.length; i++){
        	if (current.equals(threeDModes[i])){
        		choice = i;
        	}
        }
        
        jcbd = new JComboBox(threeDModes);
		jcbd.setSelectedIndex(choice);  
		jcbd.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					prefsItem.setThreeDee((String)jcbd.getSelectedItem());
				}
			}

		});
			
        cbEnableSceneMonitor = new javax.swing.JCheckBox();

        cbEnableSceneMonitor.setText("Enable Scene Monitor"); // NOI18N
        cbEnableSceneMonitor.setSelected(prefsItem.getShowSceneMonitor());
        cbEnableSceneMonitor.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        cbEnableSceneMonitor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	prefsItem.setShowSceneMonitor(cbEnableSceneMonitor.isSelected());
            }
        });
		
		setLayout(null);

		jLabelDisplaySize.setBounds(new Rectangle(30, 30, 200, 24));
		lblAntiAlias.setBounds(new Rectangle(30, 60, 200, 24));
		txtAntiAliasSamples.setBounds(new Rectangle(210, 60, 50, 24));
		jcb.setBounds(new Rectangle(210, 30, 200, 24));
		fullscreen.setBounds(new Rectangle(420, 30, 125, 24));
		jLabelThreeDeeMode.setBounds(new Rectangle(30, 120, 150, 24));
		jcbd.setBounds(new Rectangle(210, 120, 150, 24));
		cbEnableSceneMonitor.setBounds(new Rectangle(25, 175, 200, 24));


		add(jLabelDisplaySize);
		add(jcb);
		add(fullscreen);
		add(lblAntiAlias);
		add(txtAntiAliasSamples);
		add(jLabelThreeDeeMode);
		add(jcbd);
		add(cbEnableSceneMonitor);

	}
	
}