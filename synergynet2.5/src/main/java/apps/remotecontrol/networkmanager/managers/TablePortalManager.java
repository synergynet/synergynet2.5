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
package apps.remotecontrol.networkmanager.managers;

import java.util.ArrayList;
import java.util.List;

import apps.mysteriestableportal.messages.AnnounceTableMessage;
import apps.remotecontrol.networkmanager.managers.NetworkedContentManager.NetworkListener;
import apps.remotecontrol.tableportal.TablePortal;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;

public class TablePortalManager implements NetworkListener{
	protected List<TablePortal> tablePortals;
	private static TablePortalManager instance;
	
	public static TablePortalManager getInstance() {
		synchronized(TablePortalManager.class) {
			if(instance == null) instance = new TablePortalManager();
			return instance;
		}
	}

	private TablePortalManager() {				
		tablePortals = new ArrayList<TablePortal>();
	}
	
	public void registerTablePortal(TablePortal tablePortal){
		if(tablePortal!= null && !tablePortals.contains(tablePortal)) tablePortals.add(tablePortal);
	}
	
	public void unregisterTablePortal(TablePortal tablePortal){
		if(tablePortal!= null && tablePortals.contains(tablePortal)) tablePortals.remove(tablePortal);
	}
	
	public void update(float tpf){
		for(TablePortal tablePortal: tablePortals) tablePortal.update(tpf);
	}
	
	public List<TablePortal> getTablePortals(){
		return tablePortals;
	}
	
	public TablePortal getTablePortal(String id){
		for(TablePortal tablePortal: tablePortals){
			if(tablePortal.getPortalId() != null && tablePortal.getPortalId().equals(id))
				return tablePortal;
		}
		return null;
	}
	
	public TablePortal getTablePortalForTable(TableIdentity tableId){
		for(TablePortal tablePortal: tablePortals){
			if(tablePortal.getTableId() != null && tablePortal.getTableId().equals(tableId))
				return tablePortal;
		}
		return null;
	}

	@Override
	public void messageReceived(Object obj) {
		if(obj instanceof AnnounceTableMessage){
			TableIdentity tableId = ((AnnounceTableMessage)obj).getSender();
			TablePortal portal = this.getTablePortalForTable(tableId);
			if(portal != null) portal.connect(tableId);
		}
	}

	public void setNetworkedContentManager(NetworkedContentManager manager) {
		manager.addNetworkListener(this);
		
		for(TablePortal portal: this.tablePortals){
			if(portal != null){
				portal.setNetworkedContentManager(manager);
			}
		}
		for(TableIdentity tableId: manager.getOnlineTables()){
			TablePortal portal = this.getTablePortalForTable(tableId);
			if(portal != null) portal.connect(tableId);
			
		}
	}

	public void closeAll() {
		for(TablePortal portal: tablePortals) portal.close();
		tablePortals.clear();
	}
	
	public void hideAll(){
		for(TablePortal portal: tablePortals) portal.getWindow().setVisible(false);
	}
}
