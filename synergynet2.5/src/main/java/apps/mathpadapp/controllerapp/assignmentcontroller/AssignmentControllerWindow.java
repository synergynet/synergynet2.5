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

package apps.mathpadapp.controllerapp.assignmentcontroller;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.jme.cursorsystem.elements.twod.OrthoBringToTop;
import apps.mathpadapp.MathPadResources;
import apps.mathpadapp.controllerapp.assignmentbuilder.AssignmentManager;
import apps.mathpadapp.networkmanager.managers.ControllerManager;
import apps.mathpadapp.util.MTFrame;
import apps.mathpadapp.util.MTList;

/**
 * The Class AssignmentControllerWindow.
 */
public class AssignmentControllerWindow extends MTFrame {

	/** The Constant windowHeight. */
	public static final int windowHeight = 440;

	/** The Constant windowWidth. */
	public static final int windowWidth = 570;

	/** The assign list panel. */
	public MTList assignListPanel;

	/** The control panel. */
	private AssignmentListControlPanel controlPanel;

	/**
	 * Instantiates a new assignment controller window.
	 *
	 * @param contentSystem
	 *            the content system
	 * @param controllerManager
	 *            the controller manager
	 */
	public AssignmentControllerWindow(final ContentSystem contentSystem,
			final ControllerManager controllerManager) {
		super(contentSystem);
		this.setWidth(windowWidth);
		this.setHeight(windowHeight);

		assignListPanel = new MTList(contentSystem);
		this.getWindow().addSubItem(assignListPanel.getContainer());
		// assignListPanel.setHeight(350);
		// assignListPanel.setWidth(550);
		assignListPanel.getContainer().setLocalLocation(0, -10);

		controlPanel = new AssignmentListControlPanel(contentSystem,
				assignListPanel, controllerManager);
		controlPanel.getContainer().setLocalLocation(
				controlPanel.getContainer().getLocalLocation().x, -190);
		
		this.getWindow().addSubItem(controlPanel.getContainer());
		this.getWindow().setOrder(OrthoBringToTop.bottomMost);

		this.setTitle("Online Assignments");
		if (AssignmentManager.getManager().getAssignmentSessions().isEmpty()) {
			assignListPanel.getManager().addItem("temp", "temp");
			assignListPanel.getManager().deleteAllItems();
		} else {
			int i = 1;
			for (AssignmentSession session : AssignmentManager.getManager()
					.getAssignmentSessions().values()) {
				assignListPanel.getManager().addItem(
						"session " + i + "   , No. of Participants = "
								+ session.getRecipients().size(), session);
				assignListPanel.getManager().setIcon(
						session,
						MathPadResources.class
								.getResource("assignment_icon.jpg"));
				i++;
			}
		}
	}
	
	/**
	 * Gets the assignment session list.
	 *
	 * @return the assignment session list
	 */
	public MTList getAssignmentSessionList() {
		return this.assignListPanel;
	}
}
