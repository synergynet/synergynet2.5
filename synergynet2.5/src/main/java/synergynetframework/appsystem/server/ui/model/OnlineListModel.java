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

package synergynetframework.appsystem.server.ui.model;

import java.util.ArrayList;

import javax.swing.DefaultListModel;

import synergynetframework.appsystem.server.ServerMonitor;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.tablecomms.server.TableCommsServerService;


/**
 * The Class OnlineListModel.
 */
public class OnlineListModel extends DefaultListModel implements ServerMonitor {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1549976236904153875L;
	
	/** The server. */
	private TableCommsServerService server;

	/**
	 * Instantiates a new online list model.
	 *
	 * @param server the server
	 */
	public OnlineListModel(TableCommsServerService server) {
		this.server = server;
		server.registerServerMonitor(this);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.DefaultListModel#getElementAt(int)
	 */
	public Object getElementAt(int index) {
		if(server.getReceivers().size() == 0) return null;
		java.util.List<TableIdentity> l = new ArrayList<TableIdentity>();
		l.addAll(server.getReceivers().keySet());
		return l.get(index);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.DefaultListModel#getSize()
	 */
	public int getSize() {
		return server.getReceivers().size();
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.server.ServerMonitor#tableConnected(synergynetframework.appsystem.services.net.localpresence.TableIdentity)
	 */
	public void tableConnected(TableIdentity identity) {
		this.fireContentsChanged(this, 0, getSize());
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.server.ServerMonitor#tableDisconnected(synergynetframework.appsystem.services.net.localpresence.TableIdentity)
	 */
	public void tableDisconnected(TableIdentity identity) {
		this.fireContentsChanged(this, 0, getSize());
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.server.ServerMonitor#serverReceivedMessage(java.lang.Object)
	 */
	public void serverReceivedMessage(Object obj) {}
}
