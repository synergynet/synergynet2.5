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

package apps.mathpadapp.networkmanager.handlers.projector;

import apps.mathpadapp.networkmanager.handlers.DefaultMessageHandler;
import apps.mathpadapp.networkmanager.managers.ProjectorManager;
import apps.mathpadapp.networkmanager.messages.fromcontroller.broadcast.BroadcastSearchProjectorMessage;
import apps.mathpadapp.networkmanager.messages.fromcontroller.unicast.toprojector.ClearProjector;
import apps.mathpadapp.networkmanager.messages.fromcontroller.unicast.toprojector.ControllerToProjectorMessage;
import apps.mathpadapp.networkmanager.messages.fromcontroller.unicast.toprojector.PostMathPadItemsFromControllerMessage;
import apps.mathpadapp.networkmanager.messages.fromcontroller.unicast.toprojector.ReleaseProjectorMessage;
import apps.mathpadapp.networkmanager.messages.fromcontroller.unicast.toprojector.UnregisterUserMessage;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.tablecomms.messages.TableMessage;
import synergynetframework.appsystem.services.net.tablecomms.messages.application.BroadcastApplicationMessage;

public class ProjectorMessageHandler extends DefaultMessageHandler{

	private ProjectorManager projectorManager;
	
	public ProjectorMessageHandler(ProjectorManager projectorManager){
		super(projectorManager);
		this.projectorManager = projectorManager;
	}
	
	@Override
	public void messageReceived(Object obj) {
		
		if (TableIdentity.getTableIdentity().hashCode()==((TableMessage)obj).getSender().hashCode()) return;
		super.messageReceived(obj);
		if(obj instanceof BroadcastApplicationMessage){
			if(obj instanceof BroadcastSearchProjectorMessage){
				projectorManager.leaseProjector(((TableMessage)obj).getSender());
			}
		}else if(obj instanceof ControllerToProjectorMessage){
			if(obj instanceof ReleaseProjectorMessage){
				projectorManager.releaseProjector(((TableMessage)obj).getSender());
			}else if(obj instanceof PostMathPadItemsFromControllerMessage){
				projectorManager.mathPadItemsReceivedFromTable(((TableMessage)obj).getSender(), ((PostMathPadItemsFromControllerMessage)obj).getItems());
			}else if(obj instanceof UnregisterUserMessage){
				projectorManager.unregisterMathPad(((UnregisterUserMessage)obj).getUserIdentity());
			}else if(obj instanceof ClearProjector){
				projectorManager.clearProjector();
			}
		}
	}
	
	public ProjectorManager getProjectorManager(){
		return projectorManager;
	}
}
