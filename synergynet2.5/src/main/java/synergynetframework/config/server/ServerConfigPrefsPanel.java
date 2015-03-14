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

package synergynetframework.config.server;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ServerConfigPrefsPanel extends JPanel {
	
	private static final long serialVersionUID = -8701347662757286944L;
	private ServerConfigPrefsItem prefs;
	
    private JButton btnSelectDir;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JTextField txtWebServerDir;
    private JTextField txtWebServerPort;

    public ServerConfigPrefsPanel(ServerConfigPrefsItem serverConfigPrefsItem) {
    	this.prefs = serverConfigPrefsItem;
    	initComponents();
	}

	private void initComponents() {
		final JPanel instance = this;
        jLabel1 = new javax.swing.JLabel();
        txtWebServerPort = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtWebServerDir = new javax.swing.JTextField();
        btnSelectDir = new javax.swing.JButton();

        setName("Form"); // NOI18N

        jLabel1.setText("Web Server Directory:");
        txtWebServerPort.setText(prefs.getPort() + "");
        txtWebServerPort.addKeyListener(new KeyAdapter() {
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
					prefs.setPort(Integer.parseInt(txtWebServerPort.getText()));
					txtWebServerPort.setForeground(Color.black);
				}catch(NumberFormatException e){
					txtWebServerPort.setForeground(Color.red);
				}
			}
		});
        
        
        jLabel2.setText("Web Server Port:");
        txtWebServerDir.setText(prefs.getWebDirectory());
        txtWebServerDir.setEditable(false);

        btnSelectDir.setText("Browse...");
        btnSelectDir.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fc = new JFileChooser(new File(prefs.getWebDirectory()));
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = fc.showOpenDialog(instance);

		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            txtWebServerDir.setText(file.getAbsolutePath());
		            prefs.setWebDirectory(file.getAbsolutePath());
		        }	
			}
        	
        });

		setLayout(null);
		
		jLabel1.setBounds(new Rectangle(30, 30, 200, 24));
		txtWebServerDir.setBounds(new Rectangle(190, 30, 250, 24));
		btnSelectDir.setBounds(new Rectangle(445, 30, 100, 24));
		jLabel2.setBounds(new Rectangle(30, 60, 200, 24));
		txtWebServerPort.setBounds(new Rectangle(190, 60, 50, 24));

		
		add(jLabel1);
		add(txtWebServerDir);
		add(jLabel2);
		add(txtWebServerPort);
		add(btnSelectDir);
	}

}