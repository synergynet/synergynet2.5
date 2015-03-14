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

import java.io.Serializable;
import java.util.List;

import synergynetframework.appsystem.contentsystem.jme.items.utils.DrawData;

public class AssignmentInfo implements Serializable{
	
	private static final long serialVersionUID = 8020438669289884988L;

	String assignmentId;
	List<DrawData> handwritingExpression;
	List<String> mathExpression;
	
	int taskDuration;
	List<String> messages;
	
	public AssignmentInfo(String assignmentId){
		this.assignmentId = assignmentId;
	}
	
	public String getAssignmentId(){
		return assignmentId;
	}
	
	public List<DrawData> getHandwritingResult() {
		return handwritingExpression;
	}
	public void setHandwritingResult(List<DrawData> handwritingExpression) {
		this.handwritingExpression = handwritingExpression;
	}
	
	public List<String> getExpressionResult(){
		return mathExpression;
	}
	
	public void setExpressionResult(List<String> mathExpressions){
		this.mathExpression = mathExpressions;
	}
	
	public int getTaskDuration() {
		return taskDuration;
	}
	public void setTaskDuration(int taskDuration) {
		this.taskDuration = taskDuration;
	}
	public List<String> getMessages() {
		return messages;
	}
	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

}
