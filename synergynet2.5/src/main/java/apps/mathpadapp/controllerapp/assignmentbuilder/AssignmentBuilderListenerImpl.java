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

package apps.mathpadapp.controllerapp.assignmentbuilder;

import java.util.ArrayList;
import java.util.List;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import apps.mathpadapp.MathPadResources;
import apps.mathpadapp.clientapp.MathPadClient;
import apps.mathpadapp.controllerapp.ControlBar;
import apps.mathpadapp.controllerapp.assignmentbuilder.AssignmentBuilder.AssignmentBuilderListener;
import apps.mathpadapp.controllerapp.assignmentcontroller.AssignmentInfo;
import apps.mathpadapp.controllerapp.assignmentcontroller.AssignmentSession;
import apps.mathpadapp.controllerapp.usercontroller.UserInfo;
import apps.mathpadapp.controllerapp.usercontroller.UserInfo.UserStatus;
import apps.mathpadapp.mathtool.MathTool;
import apps.mathpadapp.mathtool.MathTool.SeparatorState;
import apps.mathpadapp.networkmanager.managers.ControllerManager;
import apps.mathpadapp.networkmanager.messages.fromcontroller.broadcast.BroadcastMathAssignmentMessage;
import apps.mathpadapp.networkmanager.messages.fromcontroller.unicast.totable.PostMathAssignmentToTableMessage;
import apps.mathpadapp.networkmanager.messages.fromcontroller.unicast.touser.CancelMathAssignmentMessage;
import apps.mathpadapp.networkmanager.messages.fromcontroller.unicast.touser.PostMathAssignmentToUserMessage;
import apps.mathpadapp.networkmanager.utils.UserIdentity;

/**
 * The Class AssignmentBuilderListenerImpl.
 */
public class AssignmentBuilderListenerImpl implements AssignmentBuilderListener {
	
	/** The controller manager. */
	protected ControllerManager controllerManager;

	/**
	 * Instantiates a new assignment builder listener impl.
	 *
	 * @param controllerManager
	 *            the controller manager
	 */
	public AssignmentBuilderListenerImpl(ControllerManager controllerManager) {
		this.controllerManager = controllerManager;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * apps.mathpadapp.mathtool.MathTool.MathToolListener#assignmentAnswerReady
	 * (apps.mathpadapp.controllerapp.assignmentcontroller.AssignmentInfo)
	 */
	@Override
	public void assignmentAnswerReady(AssignmentInfo info) {
		if (AssignmentManager.getManager().getAssignmentSessions()
				.containsKey(info.getAssignmentId())) {
			AssignmentManager.getManager().getAssignmentSessions()
					.get(info.getAssignmentId()).setSolution(info);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see apps.mathpadapp.controllerapp.assignmentbuilder.AssignmentBuilder.
	 * AssignmentBuilderListener
	 * #assignmentSendRequest(apps.mathpadapp.controllerapp
	 * .assignmentbuilder.Assignment, java.util.List,
	 * apps.mathpadapp.controllerapp.assignmentcontroller.AssignmentInfo)
	 */
	@Override
	public void assignmentSendRequest(Assignment assignment,
			List<Object> receipents, AssignmentInfo solution) {
		if (AssignmentManager.getManager().getAssignmentSessions()
				.containsKey(assignment.getAssignmentId())) {
			AssignmentSession session = AssignmentManager.getManager()
					.getAssignmentSessions()
					.remove(assignment.getAssignmentId());
			for (UserInfo userInfo : session.getRecipients()) {
				CancelMathAssignmentMessage msg = new CancelMathAssignmentMessage(
						MathPadClient.class, userInfo.getTableIdentity(),
						userInfo.getUserIdentity());
				controllerManager.sendMessage(msg);
			}
		}
		List<UserInfo> receipentInfos = new ArrayList<UserInfo>();
		if ((receipents == null) || receipents.isEmpty()) {
			return;
		}
		if ((receipents.size() == 1)
				&& receipents.get(0).toString().equalsIgnoreCase("ALL")) {
			BroadcastMathAssignmentMessage msg = new BroadcastMathAssignmentMessage(
					MathPadClient.class, assignment);
			controllerManager.sendMessage(msg);
			for (TableIdentity tableId : controllerManager.getTableUsers()
					.keySet()) {
				for (UserIdentity userId : controllerManager.getTableUsers()
						.get(tableId)) {
					UserInfo userInfo = new UserInfo(userId);
					userInfo.setTableIdentity(tableId);
					receipentInfos.add(userInfo);
				}
			}
		} else {
			for (Object receipent : receipents) {
				if (receipent instanceof TableIdentity) {
					TableIdentity tableId = (TableIdentity) receipent;
					PostMathAssignmentToTableMessage msg = new PostMathAssignmentToTableMessage(
							MathPadClient.class, assignment,
							((TableIdentity) receipent));
					controllerManager.sendMessage(msg);
					if (controllerManager.getTableUsers().containsKey(tableId)) {
						for (UserIdentity userId : controllerManager
								.getTableUsers().get(tableId)) {
							UserInfo userInfo = new UserInfo(userId);
							userInfo.setTableIdentity(tableId);
							receipentInfos.add(userInfo);
						}
					}
				} else if (receipent instanceof UserInfo) {
					UserInfo userInfo = (UserInfo) receipent;
					PostMathAssignmentToUserMessage msg = new PostMathAssignmentToUserMessage(
							MathPadClient.class, assignment,
							userInfo.getTableIdentity(),
							userInfo.getUserIdentity());
					controllerManager.sendMessage(msg);
					receipentInfos.add(userInfo);
				}
			}
		}
		AssignmentSession session = new AssignmentSession();
		session.setAssignment(assignment);
		session.setSolution(solution);
		session.setRecipients(receipentInfos);
		AssignmentManager.getManager().getAssignmentSessions()
				.put(assignment.getAssignmentId(), session);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * apps.mathpadapp.mathtool.MathTool.MathToolListener#mathPadClosed(apps
	 * .mathpadapp.mathtool.MathTool)
	 */
	@Override
	public void mathPadClosed(MathTool tool) {
		if (ControlBar.currentNoOfPads > 0) {
			ControlBar.currentNoOfPads--;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see apps.mathpadapp.controllerapp.assignmentbuilder.AssignmentBuilder.
	 * AssignmentBuilderListener
	 * #sendToDialogDisplayed(apps.mathpadapp.controllerapp
	 * .assignmentbuilder.SendToDialog)
	 */
	@Override
	public void sendToDialogDisplayed(SendToDialog sendToDialog) {
		// List<TableIdentity> tableIds = controllerManager.getOnlineTables();
		// tableIds.remove(TableIdentity.getTableIdentity());
		int i = 0;
		for (TableIdentity tableId : controllerManager.getTableUsers().keySet()) {
			sendToDialog.getTableListPanel().getManager()
					.addItem("Table " + (++i), tableId);
			sendToDialog
					.getTableListPanel()
					.getManager()
					.setIcon(
							tableId,
							MathPadResources.class
									.getResource("tablestatus/online.jpg"));
		}
		for (TableIdentity tableId : controllerManager.getTableUsers().keySet()) {
			List<UserIdentity> userIds = controllerManager.getTableUsers().get(
					tableId);
			for (UserIdentity userId : userIds) {
				UserInfo uInfo = new UserInfo(userId);
				uInfo.setTableIdentity(tableId);
				uInfo.setUserStatus(UserStatus.ONLINE);
				sendToDialog.getUserListPanel().getManager()
						.addItem(userId.getUserIdentity(), uInfo);
				sendToDialog
						.getUserListPanel()
						.getManager()
						.setIcon(
								uInfo,
								MathPadResources.class
										.getResource("userstatus/online.jpg"));
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * apps.mathpadapp.mathtool.MathTool.MathToolListener#separatorChanged(apps
	 * .mathpadapp.mathtool.MathTool.SeparatorState)
	 */
	@Override
	public void separatorChanged(SeparatorState newState) {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * apps.mathpadapp.mathtool.MathTool.MathToolListener#userLogin(java.lang
	 * .String, java.lang.String)
	 */
	@Override
	public void userLogin(String userName, String password) {
		
	}
	
}
