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

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import synergynetframework.appsystem.contentsystem.jme.items.utils.DrawData;

/**
 * The Class Assignment.
 */
public class Assignment implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2221232214814894336L;
	
	/** The assignment id. */
	protected String assignmentId;

	/** The draw data. */
	protected HashMap<Integer, List<DrawData>> drawData;

	/** The instructions. */
	protected String instructions;

	/** The time. */
	protected int time = -1;

	/**
	 * Instantiates a new assignment.
	 *
	 * @param assignmentId
	 *            the assignment id
	 */
	public Assignment(String assignmentId) {
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
	 * Gets the draw data.
	 *
	 * @return the draw data
	 */
	public HashMap<Integer, List<DrawData>> getDrawData() {
		return drawData;
	}
	
	/**
	 * Gets the instructions.
	 *
	 * @return the instructions
	 */
	public String getInstructions() {
		return instructions;
	}
	
	/**
	 * Gets the time.
	 *
	 * @return the time
	 */
	public int getTime() {
		return time;
	}
	
	/**
	 * Sets the assignment id.
	 *
	 * @param assignmentId
	 *            the new assignment id
	 */
	public void setAssignmentId(String assignmentId) {
		this.assignmentId = assignmentId;
	}
	
	/**
	 * Sets the draw data.
	 *
	 * @param drawData
	 *            the draw data
	 */
	public void setDrawData(HashMap<Integer, List<DrawData>> drawData) {
		this.drawData = drawData;
	}
	
	/**
	 * Sets the instructions.
	 *
	 * @param instructions
	 *            the new instructions
	 */
	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}
	
	/**
	 * Sets the time.
	 *
	 * @param time
	 *            the new time
	 */
	public void setTime(int time) {
		this.time = time;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return assignmentId;
	}
}
