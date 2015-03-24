/*
 * Copyright (c) 2009 University of Durham, England All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of 'SynergyNet' nor the names of
 * its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission. THIS SOFTWARE IS PROVIDED
 * BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package apps.mathpadapp.networkmanager.handlers.controller;

import java.util.List;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.tablecomms.messages.TableMessage;
import synergynetframework.appsystem.services.net.tablecomms.messages.application.BroadcastApplicationMessage;
import apps.mathpadapp.networkmanager.handlers.DefaultMessageHandler;
import apps.mathpadapp.networkmanager.managers.ControllerManager;
import apps.mathpadapp.networkmanager.messages.common.BroadcastMathPadSyncMessage;
import apps.mathpadapp.networkmanager.messages.common.UnicastMathPadSyncMessage;
import apps.mathpadapp.networkmanager.messages.fromclient.fromtable.PostMathPadItemsFromTableMessage;
import apps.mathpadapp.networkmanager.messages.fromclient.fromtable.PostRemoteDesktopMessage;
import apps.mathpadapp.networkmanager.messages.fromclient.fromtable.PostTableIdMessage;
import apps.mathpadapp.networkmanager.messages.fromclient.fromtable.PostUserIdsFromTableMessage;
import apps.mathpadapp.networkmanager.messages.fromclient.fromtable.TableToControllerMessage;
import apps.mathpadapp.networkmanager.messages.fromclient.fromuser.PostAssignmentInfoFromUserMessage;
import apps.mathpadapp.networkmanager.messages.fromclient.fromuser.PostMathPadItemFromUserMessage;
import apps.mathpadapp.networkmanager.messages.fromclient.fromuser.RegisterUserMessage;
import apps.mathpadapp.networkmanager.messages.fromclient.fromuser.UnregisterUserMessage;
import apps.mathpadapp.networkmanager.messages.fromclient.fromuser.UserToControllerMessage;
import apps.mathpadapp.networkmanager.messages.fromprojector.ProjectorResponse;
import apps.mathpadapp.networkmanager.messages.fromprojector.ProjectorToControllerMessage;
import apps.mathpadapp.networkmanager.utils.UserIdentity;

/**
 * The Class ControllerMessageHandler.
 */
public class ControllerMessageHandler extends DefaultMessageHandler {
	
	/** The controller manager. */
	private ControllerManager controllerManager;

	/**
	 * Instantiates a new controller message handler.
	 *
	 * @param controllerManager
	 *            the controller manager
	 */
	public ControllerMessageHandler(ControllerManager controllerManager) {
		super(controllerManager);
		this.controllerManager = controllerManager;
	}

	/**
	 * Gets the controller manager.
	 *
	 * @return the controller manager
	 */
	public ControllerManager getControllerManager() {
		return controllerManager;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * apps.mathpadapp.networkmanager.handlers.DefaultMessageHandler#messageReceived
	 * (java.lang.Object)
	 */
	@Override
	public void messageReceived(Object obj) {
		
		if (TableIdentity.getTableIdentity().hashCode() == ((TableMessage) obj)
				.getSender().hashCode()) {
			return;
		}
		super.messageReceived(obj);

		if (obj instanceof UnicastMathPadSyncMessage) {
			controllerManager.fireSyncDataReceived(
					((TableMessage) obj).getSender(),
					((UnicastMathPadSyncMessage) obj).getMathPadSyncData());
		} else if (obj instanceof BroadcastMathPadSyncMessage) {
			controllerManager.fireSyncDataReceived(
					((TableMessage) obj).getSender(),
					((BroadcastMathPadSyncMessage) obj).getMathPadSyncData());
		}

		if (obj instanceof BroadcastApplicationMessage) {
			if (obj instanceof RegisterUserMessage) {
				controllerManager.registerTableUser(
						((TableMessage) obj).getSender(),
						((RegisterUserMessage) obj).getSenderUserIdentity());
			} else if (obj instanceof UnregisterUserMessage) {
				controllerManager.unregisterTableUser(
						((TableMessage) obj).getSender(),
						((UnregisterUserMessage) obj).getSenderUserIdentity());
			}
		} else if (obj instanceof UserToControllerMessage) {
			if (obj instanceof PostMathPadItemFromUserMessage) {
				controllerManager.mathPadItemReceivedFromUser(
						((TableMessage) obj).getSender(),
						((PostMathPadItemFromUserMessage) obj)
								.getSenderUserIdentity(),
						((PostMathPadItemFromUserMessage) obj)
								.getMathPadInitSettings());
			} else if (obj instanceof PostAssignmentInfoFromUserMessage) {
				controllerManager.assignmentInfoReceivedFromUser(
						((TableMessage) obj).getSender(),
						((PostAssignmentInfoFromUserMessage) obj)
								.getSenderUserIdentity(),
						((PostAssignmentInfoFromUserMessage) obj)
								.getAssignmentInfo());
			}
		} else if (obj instanceof TableToControllerMessage) {
			if (obj instanceof PostUserIdsFromTableMessage) {
				TableIdentity tableId = ((TableMessage) obj).getSender();
				List<UserIdentity> userIds = ((PostUserIdsFromTableMessage) obj)
						.getUserIds();
				controllerManager.userIdsReceivedFromTable(tableId, userIds);
			} else if (obj instanceof PostTableIdMessage) {
				controllerManager.tableIdReceived(((TableMessage) obj)
						.getSender());
			} else if (obj instanceof PostMathPadItemsFromTableMessage) {
				controllerManager.mathPadItemsReceivedFromTable(
						((PostMathPadItemsFromTableMessage) obj).getSender(),
						((PostMathPadItemsFromTableMessage) obj).getItems());
			} else if (obj instanceof PostRemoteDesktopMessage) {
				controllerManager.remoteDesktopReceived(
						((TableMessage) obj).getSender(),
						((PostRemoteDesktopMessage) obj).getItems());
			}
		} else if (obj instanceof ProjectorToControllerMessage) {
			if (obj instanceof ProjectorResponse) {
				controllerManager.projectorFound(
						((TableMessage) obj).getSender(),
						((ProjectorResponse) obj).isLeaseSuccessful());
			}
		}
	}
}
