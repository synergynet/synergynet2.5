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

package apps.mathpadapp.networkmanager.managers.remotedesktopmanager;

import java.util.ArrayList;
import java.util.HashMap;

import apps.mathpadapp.clientapp.MathPadClient;
import apps.mathpadapp.controllerapp.tablecontroller.MathPadRemoteDesktop;
import apps.mathpadapp.controllerapp.tablecontroller.RemoteDesktop;
import apps.mathpadapp.networkmanager.managers.ControllerManager;
import apps.mathpadapp.networkmanager.managers.NetworkedContentManager;
import apps.mathpadapp.networkmanager.messages.fromcontroller.unicast.totable.UnicastEnableRemoteDesktopMessage;

import com.jme.system.DisplaySystem;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;


public class RemoteDesktopManager {

	protected ControllerManager controllerManager;
	protected HashMap<TableIdentity, MathPadRemoteDesktop> remoteDesktops = new HashMap<TableIdentity, MathPadRemoteDesktop>();
	protected ArrayList<Class<?>> controllerClasses;
	protected ArrayList<Class<?>> targetClasses;
	protected ArrayList<RemoteDesktop> remoteDektops = new ArrayList<RemoteDesktop>();
	
	public RemoteDesktopManager(NetworkedContentManager networkedContentManager, ArrayList<Class<?>> controllerClasses, ArrayList<Class<?>> targetClasses){
		this.controllerManager = (ControllerManager)networkedContentManager;
		this.controllerClasses = controllerClasses;
		this.targetClasses = targetClasses;
		
	}
	
	public MathPadRemoteDesktop createRemoteDesktopNode(TableIdentity tableId){
		MathPadRemoteDesktop rd = null;
		if(remoteDesktops.containsKey(tableId)){
			rd = remoteDesktops.get(tableId);
		}else{
			rd = new MathPadRemoteDesktop(controllerManager.getContentSystem(), controllerManager);
			rd.setTableId(tableId);
			rd.setTitle("Table: "+tableId);
			rd.setLocation(DisplaySystem.getDisplaySystem().getWidth()/2, DisplaySystem.getDisplaySystem().getHeight()/2);
			remoteDesktops.put(tableId, rd);
			controllerManager.addNetworkListener(rd);
		}
		return rd;
	}

	
	public void unregisterRemoteDesktop(TableIdentity tableId) {
		if(remoteDesktops.containsKey(tableId)){
			controllerManager.removeNetworkListener(remoteDesktops.remove(tableId));
			remoteDesktops.remove(tableId);
			UnicastEnableRemoteDesktopMessage msg = new UnicastEnableRemoteDesktopMessage(MathPadClient.class, tableId, false);
			controllerManager.sendMessage(msg);
		}
	}
	
	public HashMap<TableIdentity, MathPadRemoteDesktop> getMathPadRemoteDesktops(){
		return this.remoteDesktops;
	}
	
	
}

