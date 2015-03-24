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

package apps.mathpadapp.networkmanager.handlers.client;


import apps.mathpadapp.networkmanager.handlers.DefaultMessageHandler;
import apps.mathpadapp.networkmanager.managers.ClientManager;
import apps.mathpadapp.networkmanager.messages.fromcontroller.broadcast.BroadcastBlockTableMessage;
import apps.mathpadapp.networkmanager.messages.fromcontroller.broadcast.BroadcastEnableRemoteDesktopMessage;
import apps.mathpadapp.networkmanager.messages.fromcontroller.broadcast.BroadcastEnableSyncMessage;
import apps.mathpadapp.networkmanager.messages.fromcontroller.broadcast.BroadcastHideMathPadsOnTableMessage;
import apps.mathpadapp.networkmanager.messages.fromcontroller.broadcast.BroadcastMathAssignmentMessage;
import apps.mathpadapp.networkmanager.messages.fromcontroller.broadcast.RequestAllMathPadItemsMessage;
import apps.mathpadapp.networkmanager.messages.fromcontroller.broadcast.RequestAllTableIdsMessage;
import apps.mathpadapp.networkmanager.messages.fromcontroller.broadcast.RequestAllUserIdsMessage;
import apps.mathpadapp.networkmanager.messages.fromcontroller.unicast.totable.BlockTableMessage;
import apps.mathpadapp.networkmanager.messages.fromcontroller.unicast.totable.ControllerToTableMessage;
import apps.mathpadapp.networkmanager.messages.fromcontroller.unicast.totable.PostMathAssignmentToTableMessage;
import apps.mathpadapp.networkmanager.messages.fromcontroller.unicast.totable.UnicastEnableRemoteDesktopMessage;
import apps.mathpadapp.networkmanager.messages.fromcontroller.unicast.totable.UnicastSyncWithTableMessage;
import apps.mathpadapp.networkmanager.messages.fromcontroller.unicast.touser.BlockUserMathPadMessage;
import apps.mathpadapp.networkmanager.messages.fromcontroller.unicast.touser.CancelMathAssignmentMessage;
import apps.mathpadapp.networkmanager.messages.fromcontroller.unicast.touser.ControllerToUserMessage;
import apps.mathpadapp.networkmanager.messages.fromcontroller.unicast.touser.HideUserMathPadMessage;
import apps.mathpadapp.networkmanager.messages.fromcontroller.unicast.touser.PostMathAssignmentToUserMessage;
import apps.mathpadapp.networkmanager.messages.fromcontroller.unicast.touser.RequestMathPadItemFromUserMessage;
import apps.mathpadapp.networkmanager.messages.fromcontroller.unicast.touser.RequestResultsFromUserMessage;
import apps.mathpadapp.networkmanager.messages.fromcontroller.unicast.touser.ResponseMessage;
import apps.mathpadapp.networkmanager.messages.fromcontroller.unicast.touser.TerminateUserMathPadMessage;
import apps.mathpadapp.networkmanager.messages.fromcontroller.unicast.touser.UnicastSyncWithUserMessage;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.tablecomms.messages.TableMessage;
import synergynetframework.appsystem.services.net.tablecomms.messages.application.BroadcastApplicationMessage;


/**
 * The Class ClientMessageHandler.
 */
public class ClientMessageHandler extends DefaultMessageHandler{
	
	/** The client manager. */
	private ClientManager clientManager;
	
	/**
	 * Instantiates a new client message handler.
	 *
	 * @param clientManager the client manager
	 */
	public ClientMessageHandler(ClientManager clientManager){
		super(clientManager);
		this.clientManager = clientManager;
	}
	
	/* (non-Javadoc)
	 * @see apps.mathpadapp.networkmanager.handlers.DefaultMessageHandler#messageReceived(java.lang.Object)
	 */
	@Override
	public void messageReceived(Object obj) {
		if (TableIdentity.getTableIdentity().hashCode()==((TableMessage)obj).getSender().hashCode()) return;

		super.messageReceived(obj);
		

		if(obj instanceof BroadcastApplicationMessage){
			if(obj instanceof BroadcastBlockTableMessage){
				clientManager.blockTable(((BroadcastBlockTableMessage)obj).isTableBlockEnabled());
			}else if(obj instanceof BroadcastEnableSyncMessage){ 
				clientManager.getSyncManager().enableBroadcastSync(((BroadcastEnableSyncMessage)obj).isSynchronisationOn());
			}else if(obj instanceof BroadcastHideMathPadsOnTableMessage){ 
				clientManager.hideMathPads(((BroadcastHideMathPadsOnTableMessage)obj).isHideMathPadsEnabled());
			}else if(obj instanceof BroadcastMathAssignmentMessage){ 
				clientManager.assignmentReceivedToTable(((TableMessage)obj).getSender(), ((BroadcastMathAssignmentMessage)obj).getAssignment());
			}else if(obj instanceof RequestAllMathPadItemsMessage){
				clientManager.postAllMathPadsToTable(((TableMessage)obj).getSender());
			}else if(obj instanceof RequestAllUserIdsMessage){
				clientManager.postAllUserIdsToTable(((TableMessage)obj).getSender());
			}else if(obj instanceof RequestAllTableIdsMessage){
				clientManager.postTableId(((TableMessage)obj).getSender());
			}else if(obj instanceof BroadcastEnableRemoteDesktopMessage){
				clientManager.setRemoteDesktopWithTableEnabled(((TableMessage)obj).getSender(), ((BroadcastEnableRemoteDesktopMessage)obj).isRemoteDesktopEnabled());
			}
		}else if(obj instanceof ControllerToTableMessage){
			if(obj instanceof UnicastEnableRemoteDesktopMessage){
				clientManager.setRemoteDesktopWithTableEnabled(((TableMessage)obj).getSender(), ((UnicastEnableRemoteDesktopMessage)obj).isRemoteDesktopEnabled());
			}else if(obj instanceof PostMathAssignmentToTableMessage){
				clientManager.assignmentReceivedToTable(((TableMessage)obj).getSender(), ((PostMathAssignmentToTableMessage)obj).getAssignment());
			}else if(obj instanceof BlockTableMessage){
				clientManager.enabledTableBlock(((TableMessage)obj).getSender(), ((BlockTableMessage)obj).isBlockEnabled());
			}else if(obj instanceof UnicastSyncWithTableMessage){
				clientManager.getSyncManager().enableUnicastTableSync(((TableMessage)obj).getSender(),((UnicastSyncWithTableMessage)obj).isSynchronisationOn());
			}
		}else if(obj instanceof ControllerToUserMessage){
			if(obj instanceof BlockUserMathPadMessage){
				clientManager.blockMathPadForUser(((BlockUserMathPadMessage)obj).getRecipientUserIdentity(), ((BlockUserMathPadMessage)obj).isBlockPad());
			}else if(obj instanceof HideUserMathPadMessage){
				clientManager.hideMathPadForUser(((HideUserMathPadMessage)obj).getRecipientUserIdentity(), ((HideUserMathPadMessage)obj).isHidePad());
			}else if(obj instanceof TerminateUserMathPadMessage){
				clientManager.terminatePadForUser(((TerminateUserMathPadMessage)obj).getRecipientUserIdentity());
			}else if(obj instanceof RequestMathPadItemFromUserMessage){
				clientManager.postMathPadItemToTable(((TableMessage)obj).getSender(), ((RequestMathPadItemFromUserMessage)obj).getRecipientUserIdentity());
			}else if(obj instanceof PostMathAssignmentToUserMessage){
				clientManager.assignmentReceivedToUser(((TableMessage)obj).getSender(), ((PostMathAssignmentToUserMessage)obj).getRecipientUserIdentity(), ((PostMathAssignmentToUserMessage)obj).getAssignment());
			}else if(obj instanceof RequestResultsFromUserMessage){
				clientManager.postAssignmentResults(((TableMessage)obj).getSender(), ((RequestResultsFromUserMessage)obj).getRecipientUserIdentity());
			}else if(obj instanceof CancelMathAssignmentMessage){
				clientManager.cancelAssignment(((TableMessage)obj).getSender(), ((CancelMathAssignmentMessage)obj).getRecipientUserIdentity());
			}else if(obj instanceof ResponseMessage){
				clientManager.messageReceivedFromServer(((TableMessage)obj).getSender(), ((ResponseMessage)obj).getRecipientUserIdentity(), ((ResponseMessage)obj).getMessage());
			}else if(obj instanceof UnicastSyncWithUserMessage){
				clientManager.getSyncManager().enableUnicastUserSync(((TableMessage)obj).getSender(),((UnicastSyncWithUserMessage)obj).getRecipientUserIdentity() , ((UnicastSyncWithUserMessage)obj).isSynchronisationOn());
			}
		}
	}
	
	/**
	 * Gets the client manager.
	 *
	 * @return the client manager
	 */
	public ClientManager getClientManager(){
		return clientManager;
	}
	
	
}
