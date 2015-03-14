/*
 * Copyright (c) 2012 University of Durham, England
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

package synergynetframework.config.network;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class NetworkConfigPrefsPanel extends javax.swing.JPanel {

	private static final long serialVersionUID = 1808994055508198383L;
	private NetworkConfigPrefsItem prefs;
	
	private JCheckBox jCheckBox1;
	private JLabel jLabel1;
	private JLabel jLabel2;
	private JTextField jTextField1;
	private JTextField jTextField2;
	
	public NetworkConfigPrefsPanel(NetworkConfigPrefsItem networkConfigPrefsItem) {
		this.prefs = networkConfigPrefsItem;
		initComponents();
	}

	private void initComponents() {

		jLabel1 = new JLabel();
		jLabel2 = new JLabel();
		jTextField1 = new JTextField();
		jTextField2 = new JTextField();
		jCheckBox1 = new JCheckBox();

		setName("Form"); // NOI18N

		jLabel1.setText("HTTP Proxy Host:");
		jLabel2.setText("HTTP Proxy Port:");

		jTextField1.setText(prefs.getProxyHost());
		jTextField1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				store();
			}			

			@Override
			public void keyTyped(KeyEvent e) {
				store();
			}			
			
			private void store() {
				prefs.setProxyHost(jTextField1.getText());
			}
		});
		
		jTextField2.setText(prefs.getProxyPort() + "");
		jTextField2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				store();
			}			

			@Override
			public void keyTyped(KeyEvent e) {
				store();
			}			
			
			private void store() {
				try{
					prefs.setProxyPort(Integer.parseInt(jTextField2.getText()));
					jTextField2.setForeground(Color.black);
				}catch(NumberFormatException e){
					jTextField2.setForeground(Color.red);
				}
			}
		});

		jCheckBox1.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
		jCheckBox1.setText("HTTP Proxy enabled");
		jCheckBox1.setSelected(prefs.getProxyEnabled());
		jCheckBox1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				prefs.setProxyEnabled(jCheckBox1.isSelected());
			}
			
		});

		setLayout(null);
		
		jLabel1.setBounds(new Rectangle(30, 30, 200, 24));
		jTextField1.setBounds(new Rectangle(160, 30, 200, 24));
		jLabel2.setBounds(new Rectangle(30, 60, 200, 24));
		jTextField2.setBounds(new Rectangle(160, 60, 50, 24));
		jCheckBox1.setBounds(new Rectangle(25, 90, 200, 24));
		
		add(jLabel1);
		add(jLabel2);
		add(jTextField1);
		add(jTextField2);
		add(jCheckBox1);
		
	}

}