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

package apps.mathpadapp.networkmanager.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.tablecomms.client.TableCommsClientService;
import apps.mathpadapp.clientapp.MathPadClient;
import apps.mathpadapp.conceptmapping.GraphManager;
import apps.mathpadapp.controllerapp.assignmentbuilder.AssignmentManager;
import apps.mathpadapp.controllerapp.assignmentcontroller.AssignmentInfo;
import apps.mathpadapp.controllerapp.assignmentcontroller.AssignmentSession;
import apps.mathpadapp.controllerapp.usercontroller.UserInfo;
import apps.mathpadapp.mathtool.MathToolInitSettings;
import apps.mathpadapp.networkmanager.managers.remotedesktopmanager.RemoteDesktopManager;
import apps.mathpadapp.networkmanager.managers.syncmanager.SyncManager;
import apps.mathpadapp.networkmanager.messages.fromcontroller.unicast.touser.ResponseMessage;
import apps.mathpadapp.networkmanager.utils.UserIdentity;

/**
 * The Class ControllerManager.
 */
public class ControllerManager extends NetworkedContentManager {
	
	/**
	 * The listener interface for receiving controllerNetwork events. The class
	 * that is interested in processing a controllerNetwork event implements
	 * this interface, and the object created with that class is registered with
	 * a component using the component's
	 * <code>addControllerNetworkListener<code> method. When
	 * the controllerNetwork event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see ControllerNetworkEvent
	 */
	public interface ControllerNetworkListener extends NetworkListener {

		/**
		 * Projector found.
		 *
		 * @param tableId
		 *            the table id
		 * @param isLeaseSuccessful
		 *            the is lease successful
		 */
		public void projectorFound(TableIdentity tableId,
				boolean isLeaseSuccessful);

		/**
		 * Remote desktop content received.
		 *
		 * @param tableId
		 *            the table id
		 * @param items
		 *            the items
		 */
		public void remoteDesktopContentReceived(TableIdentity tableId,
				HashMap<UserIdentity, MathToolInitSettings> items);

		/**
		 * Results received from user.
		 *
		 * @param tableId
		 *            the table id
		 * @param userId
		 *            the user id
		 * @param assignInfo
		 *            the assign info
		 */
		public void resultsReceivedFromUser(TableIdentity tableId,
				UserIdentity userId, AssignmentInfo assignInfo);

		/**
		 * Sync data received.
		 *
		 * @param sender
		 *            the sender
		 * @param mathPadSyncData
		 *            the math pad sync data
		 */
		public void syncDataReceived(TableIdentity sender,
				HashMap<UserIdentity, HashMap<Short, Object>> mathPadSyncData);

		/**
		 * Table id received.
		 *
		 * @param tableId
		 *            the table id
		 */
		public void tableIdReceived(TableIdentity tableId);

		/**
		 * User ids received.
		 *
		 * @param tableId
		 *            the table id
		 * @param userIds
		 *            the user ids
		 */
		public void userIdsReceived(TableIdentity tableId,
				List<UserIdentity> userIds);

		/**
		 * User math pad received.
		 *
		 * @param tableId
		 *            the table id
		 * @param userId
		 *            the user id
		 * @param mathToolSettings
		 *            the math tool settings
		 */
		public void userMathPadReceived(TableIdentity tableId,
				UserIdentity userId, MathToolInitSettings mathToolSettings);

		/**
		 * User registration received.
		 *
		 * @param tableId
		 *            the table id
		 * @param userId
		 *            the user id
		 */
		public void userRegistrationReceived(TableIdentity tableId,
				UserIdentity userId);

		/**
		 * User unregistration received.
		 *
		 * @param tableId
		 *            the table id
		 * @param userId
		 *            the user id
		 */
		public void userUnregistrationReceived(TableIdentity tableId,
				UserIdentity userId);
	}

	/** The graph manager. */
	protected GraphManager graphManager;

	/** The remote desktop manager. */
	protected RemoteDesktopManager remoteDesktopManager;
	
	/** The table users. */
	protected HashMap<TableIdentity, List<UserIdentity>> tableUsers = new HashMap<TableIdentity, List<UserIdentity>>();
	
	/**
	 * Instantiates a new controller manager.
	 *
	 * @param contentSystem
	 *            the content system
	 * @param comms
	 *            the comms
	 * @param receiverClasses
	 *            the receiver classes
	 */
	public ControllerManager(ContentSystem contentSystem,
			TableCommsClientService comms, ArrayList<Class<?>> receiverClasses) {
		super(contentSystem, comms, receiverClasses);
		super.setSyncManager(new SyncManager(this));
	}

	/**
	 * Assignment info received from user.
	 *
	 * @param senderTableIdentity
	 *            the sender table identity
	 * @param senderUserIdentity
	 *            the sender user identity
	 * @param assignmentInfo
	 *            the assignment info
	 */
	public void assignmentInfoReceivedFromUser(
			TableIdentity senderTableIdentity, UserIdentity senderUserIdentity,
			AssignmentInfo assignmentInfo) {
		for (AssignmentSession session : AssignmentManager.getManager()
				.getAssignmentSessions().values()) {
			if (session.getAssignment().getAssignmentId()
					.equals(assignmentInfo.getAssignmentId())) {
				UserInfo userInfo = new UserInfo(senderUserIdentity);
				userInfo.setTableIdentity(senderTableIdentity);
				session.getReceivedData().put(userInfo, assignmentInfo);
			}
		}
		for (NetworkListener listener : listeners) {
			((ControllerNetworkListener) listener).resultsReceivedFromUser(
					senderTableIdentity, senderUserIdentity, assignmentInfo);
		}
	}

	/**
	 * Creates the remote desktop manager.
	 *
	 * @param controllerClasses
	 *            the controller classes
	 * @param targetClasses
	 *            the target classes
	 */
	public void createRemoteDesktopManager(
			ArrayList<Class<?>> controllerClasses,
			ArrayList<Class<?>> targetClasses) {
		remoteDesktopManager = new RemoteDesktopManager(this,
				controllerClasses, targetClasses);
	}
	
	/**
	 * Fire sync data received.
	 *
	 * @param sender
	 *            the sender
	 * @param mathPadSyncData
	 *            the math pad sync data
	 */
	public void fireSyncDataReceived(TableIdentity sender,
			HashMap<UserIdentity, HashMap<Short, Object>> mathPadSyncData) {
		for (NetworkListener listener : listeners) {
			((ControllerNetworkListener) listener).syncDataReceived(sender,
					mathPadSyncData);
		}
	}

	/**
	 * Gets the graph manager.
	 *
	 * @return the graph manager
	 */
	public GraphManager getGraphManager() {
		return graphManager;
	}
	
	/**
	 * Gets the remote desktop manager.
	 *
	 * @return the remote desktop manager
	 */
	public RemoteDesktopManager getRemoteDesktopManager() {
		return remoteDesktopManager;
	}

	/**
	 * Gets the table users.
	 *
	 * @return the table users
	 */
	public HashMap<TableIdentity, List<UserIdentity>> getTableUsers() {
		return tableUsers;
	}

	/**
	 * Math pad item received from user.
	 *
	 * @param senderTable
	 *            the sender table
	 * @param senderUser
	 *            the sender user
	 * @param padSettings
	 *            the pad settings
	 */
	public void mathPadItemReceivedFromUser(TableIdentity senderTable,
			UserIdentity senderUser, MathToolInitSettings padSettings) {
		for (NetworkListener listener : listeners) {
			((ControllerNetworkListener) listener).userMathPadReceived(
					senderTable, senderUser, padSettings);
		}
	}

	/**
	 * Math pad items received from table.
	 *
	 * @param tableId
	 *            the table id
	 * @param items
	 *            the items
	 */
	public void mathPadItemsReceivedFromTable(TableIdentity tableId,
			HashMap<UserIdentity, MathToolInitSettings> items) {
		for (UserIdentity userId : items.keySet()) {
			for (NetworkListener listener : listeners) {
				((ControllerNetworkListener) listener).userMathPadReceived(
						tableId, userId, items.get(userId));
			}
		}
	}

	/**
	 * Projector found.
	 *
	 * @param projectorId
	 *            the projector id
	 * @param isLeaseSuccessful
	 *            the is lease successful
	 */
	public void projectorFound(TableIdentity projectorId,
			boolean isLeaseSuccessful) {
		for (NetworkListener listener : listeners) {
			((ControllerNetworkListener) listener).projectorFound(projectorId,
					isLeaseSuccessful);
		}
	}

	/**
	 * Register table user.
	 *
	 * @param tableId
	 *            the table id
	 * @param userId
	 *            the user id
	 */
	public void registerTableUser(TableIdentity tableId, UserIdentity userId) {
		if (tableUsers.containsKey(tableId)) {
			if (!tableUsers.get(tableId).contains(userId)) {
				tableUsers.get(tableId).add(userId);
			} else {
				ResponseMessage msg = new ResponseMessage(MathPadClient.class,
						tableId, userId);
				msg.setMessage("A user with the same\n name is already registered.");
				this.sendMessage(msg);
			}
		} else {
			List<UserIdentity> users = new ArrayList<UserIdentity>();
			users.add(userId);
			tableUsers.put(tableId, users);
		}

		for (NetworkListener listener : listeners) {
			((ControllerNetworkListener) listener).userRegistrationReceived(
					tableId, userId);
		}
	}
	
	/**
	 * Remote desktop received.
	 *
	 * @param sender
	 *            the sender
	 * @param items
	 *            the items
	 */
	public void remoteDesktopReceived(TableIdentity sender,
			HashMap<UserIdentity, MathToolInitSettings> items) {
		if ((remoteDesktopManager != null)
				&& remoteDesktopManager.getMathPadRemoteDesktops().containsKey(
						sender)) {
			for (NetworkListener listener : listeners) {
				((ControllerNetworkListener) listener)
						.remoteDesktopContentReceived(sender, items);
			}
		}
	}
	
	/**
	 * Sets the graph manager.
	 *
	 * @param graphManager
	 *            the new graph manager
	 */
	public void setGraphManager(GraphManager graphManager) {
		this.graphManager = graphManager;
	}
	
	/**
	 * Table id received.
	 *
	 * @param tableId
	 *            the table id
	 */
	public void tableIdReceived(TableIdentity tableId) {
		for (NetworkListener listener : listeners) {
			((ControllerNetworkListener) listener).tableIdReceived(tableId);
		}
	}
	
	/**
	 * Unregister table user.
	 *
	 * @param tableId
	 *            the table id
	 * @param userId
	 *            the user id
	 */
	public void unregisterTableUser(TableIdentity tableId, UserIdentity userId) {
		if (tableUsers.containsKey(tableId)
				&& (tableUsers.get(tableId) != null)) {
			tableUsers.get(tableId).remove(userId);
		}
		if (this.mathPads.containsKey(userId)) {
			this.mathPads.get(userId).terminate();
			this.unregisterMathPad(userId);
		}

		for (NetworkListener listener : listeners) {
			((ControllerNetworkListener) listener).userUnregistrationReceived(
					tableId, userId);
		}
	}
	
	/**
	 * User ids received from table.
	 *
	 * @param tableId
	 *            the table id
	 * @param userIds
	 *            the user ids
	 */
	public void userIdsReceivedFromTable(TableIdentity tableId,
			List<UserIdentity> userIds) {
		tableUsers.remove(tableId);
		tableUsers.put(tableId, userIds);

		for (NetworkListener listener : listeners) {
			((ControllerNetworkListener) listener).userIdsReceived(tableId,
					userIds);
		}
	}
}
