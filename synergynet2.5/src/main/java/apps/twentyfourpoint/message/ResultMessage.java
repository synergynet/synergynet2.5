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

package apps.twentyfourpoint.message;

import apps.twentyfourpoint.TwentyFourPointApp;
import synergynetframework.appsystem.services.net.tablecomms.messages.application.BroadcastApplicationMessage;


/**
 * The Class ResultMessage.
 */
public class ResultMessage extends BroadcastApplicationMessage {	

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1301733331207865934L;
	
	/** The win. */
	private boolean win;
	
	/** The result string. */
	private String resultString;
	
	/**
	 * Instantiates a new result message.
	 *
	 * @param win the win
	 * @param resultString the result string
	 */
	public ResultMessage(boolean win, String resultString) {
		super(TwentyFourPointApp.class);
		this.win = win;
		this.resultString = resultString;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "Send result";
	}

	/**
	 * Checks if is win.
	 *
	 * @return true, if is win
	 */
	public boolean isWin() {
		return win;
	}

	/**
	 * Sets the win.
	 *
	 * @param win the new win
	 */
	public void setWin(boolean win) {
		this.win = win;
	}

	/**
	 * Gets the result string.
	 *
	 * @return the result string
	 */
	public String getResultString() {
		return resultString;
	}

	/**
	 * Sets the result string.
	 *
	 * @param resultString the new result string
	 */
	public void setResultString(String resultString) {
		this.resultString = resultString;
	}
		
}
