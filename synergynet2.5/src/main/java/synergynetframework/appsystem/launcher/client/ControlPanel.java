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

package synergynetframework.appsystem.launcher.client;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ControlPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 2131856892054887302L;
	private static final Logger log = Logger.getLogger(ControlPanel.class.getName());
	private static final String CMD_START = "CMD_START";
	private static final String CMD_DISPLAYMODE = "CMD_DISPLAYMODE";
	private static final String CMD_REGISTRYEDIT = "CMD_REGISTRYEDIT";
	private JButton startButton;
	private JButton displayModeButton;
	private JButton registryButton;
	
	public ControlPanel() {
		super();
		init();
	}

	private void init() {
		setLayout(new FlowLayout());
		
		displayModeButton = new JButton("Display Mode");
		displayModeButton.setActionCommand(CMD_DISPLAYMODE);
		displayModeButton.setEnabled(false);
		displayModeButton.addActionListener(this);
		add(displayModeButton);
		
		registryButton = new JButton("Edit registry");
		registryButton.setActionCommand(CMD_REGISTRYEDIT);
		registryButton.setEnabled(true);
		registryButton.addActionListener(this);
		add(registryButton);
	
		startButton = new JButton("Start");
		startButton.setEnabled(false);
		startButton.addActionListener(this);
		startButton.setActionCommand(CMD_START);		
		add(startButton);
		
		
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals(CMD_START)) {
			try {
				ProcessStarter.start("synergynet.opts");
			} catch (IOException e1) {
				log.severe("IOException when accessing synergynet.opts file for launching Synergynet App.\n"+e1.toString());
				LogWindow.getInstance().log(e1.toString());
			}
		}else if(e.getActionCommand().equals(CMD_DISPLAYMODE)) {
			try {			
				ProcessStarter.start("pickdisplaymode.opts");
			} catch (IOException e1) {
				log.severe("IOException when accessing pickdisplaymode.opts file for launching DisplayMode picker app.\n"+e1.toString());
				LogWindow.getInstance().log(e1.toString());
			}
		}else if(e.getActionCommand().equals(CMD_REGISTRYEDIT)) {
			try {
				ProcessStarter.start("registryeditor.opts");
			} catch (IOException e1) {
				log.severe("IOException when accessing registryeditor.opts file for launching RegistryEditor app.\n"+e1.toString());
				LogWindow.getInstance().log(e1.toString());
			}
		}
		
	}

	public void enableLaunch() {
		displayModeButton.setEnabled(true);
		startButton.setEnabled(true);		
	}
	
	
}
