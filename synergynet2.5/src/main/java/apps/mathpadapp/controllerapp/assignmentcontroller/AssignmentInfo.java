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

import java.io.Serializable;
import java.util.List;

import synergynetframework.appsystem.contentsystem.jme.items.utils.DrawData;

/**
 * The Class AssignmentInfo.
 */
public class AssignmentInfo implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8020438669289884988L;
	
	/** The assignment id. */
	String assignmentId;

	/** The handwriting expression. */
	List<DrawData> handwritingExpression;

	/** The math expression. */
	List<String> mathExpression;

	/** The messages. */
	List<String> messages;

	/** The task duration. */
	int taskDuration;

	/**
	 * Instantiates a new assignment info.
	 *
	 * @param assignmentId
	 *            the assignment id
	 */
	public AssignmentInfo(String assignmentId) {
		this.assignmentId = assignmentId;
	}

	/**
	 * Gets the assignment id.
	 *
	 * @return the assignment id
	 */
	public String getAssignmentId() {
		return assignmentId;
	}

	/**
	 * Gets the expression result.
	 *
	 * @return the expression result
	 */
	public List<String> getExpressionResult() {
		return mathExpression;
	}

	/**
	 * Gets the handwriting result.
	 *
	 * @return the handwriting result
	 */
	public List<DrawData> getHandwritingResult() {
		return handwritingExpression;
	}

	/**
	 * Gets the messages.
	 *
	 * @return the messages
	 */
	public List<String> getMessages() {
		return messages;
	}

	/**
	 * Gets the task duration.
	 *
	 * @return the task duration
	 */
	public int getTaskDuration() {
		return taskDuration;
	}

	/**
	 * Sets the expression result.
	 *
	 * @param mathExpressions
	 *            the new expression result
	 */
	public void setExpressionResult(List<String> mathExpressions) {
		this.mathExpression = mathExpressions;
	}

	/**
	 * Sets the handwriting result.
	 *
	 * @param handwritingExpression
	 *            the new handwriting result
	 */
	public void setHandwritingResult(List<DrawData> handwritingExpression) {
		this.handwritingExpression = handwritingExpression;
	}

	/**
	 * Sets the messages.
	 *
	 * @param messages
	 *            the new messages
	 */
	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	/**
	 * Sets the task duration.
	 *
	 * @param taskDuration
	 *            the new task duration
	 */
	public void setTaskDuration(int taskDuration) {
		this.taskDuration = taskDuration;
	}
	
}
