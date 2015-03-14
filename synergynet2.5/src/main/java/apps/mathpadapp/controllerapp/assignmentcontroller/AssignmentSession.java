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


public class AssignmentSession {

	protected Assignment assignment;
	protected List<UserInfo> recipients;
	protected HashMap<UserInfo, AssignmentInfo> receivedData = new HashMap<UserInfo, AssignmentInfo>();
	protected AssignmentInfo solution;
	
	public AssignmentSession(String sessionId, Assignment assignment, List<UserInfo> recipients) {
		super();
		this.assignment = assignment;
		this.recipients = recipients;
	}

	public AssignmentSession(){
		
	}
	
	public Assignment getAssignment() {
		return assignment;
	}
	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}
	public List<UserInfo> getRecipients() {
		return recipients;
	}
	public void setRecipients(List<UserInfo> receipents) {
		this.recipients = receipents;
	}

	public HashMap<UserInfo, AssignmentInfo> getReceivedData() {
		return receivedData;
	}

	public void setReceivedData(HashMap<UserInfo, AssignmentInfo> receivedData) {
		this.receivedData = receivedData;
	}
	
	public String toString(){
		return assignment.getAssignmentId();
	}

	public void setSolution(AssignmentInfo solution) {
		this.solution = solution;
	}
	
	public AssignmentInfo getSolution(){
		return solution;
	}
}
