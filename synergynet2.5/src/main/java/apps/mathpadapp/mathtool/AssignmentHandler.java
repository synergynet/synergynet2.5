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
package apps.mathpadapp.mathtool;

import java.util.HashMap;
import java.util.List;

import apps.mathpadapp.controllerapp.assignmentbuilder.Assignment;

import synergynetframework.appsystem.contentsystem.items.MathPad;
import synergynetframework.appsystem.contentsystem.jme.items.utils.DrawData;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;


/**
 * The Class AssignmentHandler.
 */
public class AssignmentHandler {
	
	/** The math tool. */
	protected MathTool mathTool;
	
	/** The assignment. */
	protected Assignment assignment;
	
	/** The assignment sender. */
	protected TableIdentity assignmentSender;
	
	/**
	 * Instantiates a new assignment handler.
	 *
	 * @param mathTool the math tool
	 */
	public AssignmentHandler(MathTool mathTool){
		this.mathTool = mathTool;
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
	 * Draw assignment.
	 */
	public void drawAssignment(){
		HashMap<Integer, List<DrawData>> drawData = assignment.getDrawData();
		for(int i: drawData.keySet()){
			if(i<mathTool.getAllPads().size()){
				mathTool.getAllPads().get(i).draw(drawData.get(i));
			}else{
				mathTool.addNewPad().draw(drawData.get(i));
			}
		}
		mathTool.showPad(0);
		mathTool.getControlPanel().getSolutionButton().setVisible(true);
	}
	
	/**
	 * Delete assignment.
	 */
	public void deleteAssignment(){
		for(MathPad pad: mathTool.getAllPads()) pad.clearAll();
		mathTool.getAnswerDialog().getAnswerPad().clearAll();
		mathTool.getControlPanel().getSolutionButton().setVisible(false);
		assignment = null;
	}

	/**
	 * Gets the assignment sender.
	 *
	 * @return the assignment sender
	 */
	public TableIdentity getAssignmentSender() {
		return assignmentSender;
	}

	/**
	 * Sets the assignment sender.
	 *
	 * @param assignmentSender the new assignment sender
	 */
	public void setAssignmentSender(TableIdentity assignmentSender) {
		this.assignmentSender = assignmentSender;
	}
}
