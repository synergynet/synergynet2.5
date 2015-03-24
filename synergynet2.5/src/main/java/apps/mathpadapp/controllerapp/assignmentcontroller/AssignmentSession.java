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

package apps.mathpadapp.controllerapp.assignmentcontroller;

import java.util.HashMap;
import java.util.List;

import apps.mathpadapp.controllerapp.assignmentbuilder.Assignment;
import apps.mathpadapp.controllerapp.usercontroller.UserInfo;



/**
 * The Class AssignmentSession.
 */
public class AssignmentSession {

	/** The assignment. */
	protected Assignment assignment;
	
	/** The recipients. */
	protected List<UserInfo> recipients;
	
	/** The received data. */
	protected HashMap<UserInfo, AssignmentInfo> receivedData = new HashMap<UserInfo, AssignmentInfo>();
	
	/** The solution. */
	protected AssignmentInfo solution;
	
	/**
	 * Instantiates a new assignment session.
	 *
	 * @param sessionId the session id
	 * @param assignment the assignment
	 * @param recipients the recipients
	 */
	public AssignmentSession(String sessionId, Assignment assignment, List<UserInfo> recipients) {
		super();
		this.assignment = assignment;
		this.recipients = recipients;
	}

	/**
	 * Instantiates a new assignment session.
	 */
	public AssignmentSession(){
		
	}
	
	/**
	 * Gets the assignment.
	 *
	 * @return the assignment
	 */
	public Assignment getAssignment() {
		return assignment;
	}
	
	/**
	 * Sets the assignment.
	 *
	 * @param assignment the new assignment
	 */
	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}
	
	/**
	 * Gets the recipients.
	 *
	 * @return the recipients
	 */
	public List<UserInfo> getRecipients() {
		return recipients;
	}
	
	/**
	 * Sets the recipients.
	 *
	 * @param receipents the new recipients
	 */
	public void setRecipients(List<UserInfo> receipents) {
		this.recipients = receipents;
	}

	/**
	 * Gets the received data.
	 *
	 * @return the received data
	 */
	public HashMap<UserInfo, AssignmentInfo> getReceivedData() {
		return receivedData;
	}

	/**
	 * Sets the received data.
	 *
	 * @param receivedData the received data
	 */
	public void setReceivedData(HashMap<UserInfo, AssignmentInfo> receivedData) {
		this.receivedData = receivedData;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return assignment.getAssignmentId();
	}

	/**
	 * Sets the solution.
	 *
	 * @param solution the new solution
	 */
	public void setSolution(AssignmentInfo solution) {
		this.solution = solution;
	}
	
	/**
	 * Gets the solution.
	 *
	 * @return the solution
	 */
	public AssignmentInfo getSolution(){
		return solution;
	}
}
