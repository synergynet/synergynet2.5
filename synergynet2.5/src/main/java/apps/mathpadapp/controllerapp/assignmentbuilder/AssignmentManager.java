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

import java.util.HashMap;

import apps.mathpadapp.controllerapp.assignmentcontroller.AssignmentSession;

/**
 * The Class AssignmentManager.
 */
public class AssignmentManager {

	/** The instance. */
	private static AssignmentManager instance;

	/**
	 * Gets the manager.
	 *
	 * @return the manager
	 */
	public static AssignmentManager getManager() {
		synchronized (AssignmentManager.class) {
			if (instance == null) {
				instance = new AssignmentManager();
			}
			return instance;
		}
	}

	/** The assignment sessions. */
	private HashMap<String, AssignmentSession> assignmentSessions;
	
	/**
	 * Instantiates a new assignment manager.
	 */
	private AssignmentManager() {
		assignmentSessions = new HashMap<String, AssignmentSession>();
	}

	/**
	 * Gets the assignment sessions.
	 *
	 * @return the assignment sessions
	 */
	public HashMap<String, AssignmentSession> getAssignmentSessions() {
		return assignmentSessions;
	}
}
