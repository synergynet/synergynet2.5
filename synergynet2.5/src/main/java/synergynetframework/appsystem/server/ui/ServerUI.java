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

package synergynetframework.appsystem.server.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import synergynetframework.appsystem.server.ui.model.AppListModel;
import synergynetframework.appsystem.server.ui.model.MessageListModel;
import synergynetframework.appsystem.server.ui.model.OnlineListModel;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.tablecomms.server.TableCommsServerService;


public class ServerUI extends JFrame {
	private static final long serialVersionUID = -8970026431538865245L;
	private static final Logger log = Logger.getLogger(ServerUI.class.getName());

	private static ServerUI instance;

	public static void start(TableCommsServerService server) {
		if(instance == null) instance = new ServerUI(server);
		instance.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		instance.setPreferredSize(new Dimension(640,480));
		instance.pack();
		instance.setVisible(true);		
		log.info("ServerUI started");
	}

	private TableCommsServerService server;

	public ServerUI(TableCommsServerService server) {
		this.server = server;
		init2();		
	}

	private void init2() {
		JDesktopPane desktop = new JDesktopPane();
		setContentPane(desktop);
		addApplicationMappingView(desktop);
		addMessageLoggerView(desktop);
	}

	@SuppressWarnings({ })
	private void addApplicationMappingView(JDesktopPane desktop) {
		JSplitPane splitter = new JSplitPane();

		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.add(new JLabel("Applications"), BorderLayout.NORTH);
		final AppListModel appModel = new AppListModel(server);
		JList tableApps = new JList(appModel);
		p.add(tableApps, BorderLayout.CENTER);
		splitter.setRightComponent(p);

		p = new JPanel();
		p.setLayout(new BorderLayout());
		JLabel online = new JLabel("Online:");
		p.add(online, BorderLayout.NORTH);
		final ListModel onlineModel = new OnlineListModel(server);
		final JList tablesOnline = new JList(onlineModel );
		p.add(tablesOnline, BorderLayout.CENTER);
		tablesOnline.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				int indx = e.getFirstIndex();
				TableIdentity id = (TableIdentity) onlineModel.getElementAt(indx);
				appModel.setSelectedTableIdentity(id);
			}			
		});
		tablesOnline.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 1) {
					int indx = tablesOnline.getSelectedIndex();
					if(indx != -1) {
						TableIdentity id = (TableIdentity) onlineModel.getElementAt(indx);
						appModel.setSelectedTableIdentity(id);					
					}
				}				
			}			
		});
		splitter.setLeftComponent(p);
		addInternalWindow(desktop, "Application Mapping", splitter);
	}

	@SuppressWarnings({ })
	private void addMessageLoggerView(JDesktopPane desktop) {
		JPanel pnl = new JPanel();
		pnl.setLayout(new BorderLayout());
		JScrollPane logScroller = new JScrollPane();
		pnl.add(logScroller, BorderLayout.CENTER);		
		JList messageLog = new JList(new MessageListModel(server, 30));
		pnl.add(logScroller);
		logScroller.setPreferredSize(new Dimension(200, 50));
		logScroller.getViewport().add(messageLog);
		addInternalWindow(desktop, "Message Log", pnl);
	}
	
	private void addInternalWindow(JDesktopPane desktop, String title, Container container) {
		JInternalFrame jif = new JInternalFrame(title);
		jif.setMaximizable(true);
		jif.setResizable(true);
		jif.setIconifiable(false);
		jif.setClosable(false);
		jif.getContentPane().setLayout(new BorderLayout());
		jif.add(container);
		jif.setBounds(100,150,300,100);
		jif.setVisible(true);
		jif.pack();
		desktop.add(jif, JLayeredPane.DEFAULT_LAYER);		
	}
}
