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

package apps.mathpadapp.networkmanager.messages.fromcontroller.broadcast;

import synergynetframework.appsystem.services.net.tablecomms.messages.application.BroadcastApplicationMessage;


/**
 * The Class BroadcastEnableRemoteDesktopMessage.
 */
public class BroadcastEnableRemoteDesktopMessage extends BroadcastApplicationMessage {	

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 259614974289184624L;
	
	/** The is remote desktop enabled. */
	private boolean isRemoteDesktopEnabled = false;
	
	/**
	 * Instantiates a new broadcast enable remote desktop message.
	 */
	public BroadcastEnableRemoteDesktopMessage() {
		super();
	}
	
	/**
	 * Instantiates a new broadcast enable remote desktop message.
	 *
	 * @param targetClass the target class
	 * @param isRemoteDesktopEnabled the is remote desktop enabled
	 */
	public BroadcastEnableRemoteDesktopMessage(Class<?> targetClass, boolean isRemoteDesktopEnabled) {
		super(targetClass);
		this.isRemoteDesktopEnabled = isRemoteDesktopEnabled;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "Broadcast Enable Remote Desktop";
	}
	
	/**
	 * Checks if is remote desktop enabled.
	 *
	 * @return true, if is remote desktop enabled
	 */
	public boolean isRemoteDesktopEnabled() {
		return isRemoteDesktopEnabled;
	}

	/**
	 * Sets the remote desktop enabled.
	 *
	 * @param isRemoteDesktopEnabled the new remote desktop enabled
	 */
	public void setRemoteDesktopEnabled(boolean isRemoteDesktopEnabled) {
		this.isRemoteDesktopEnabled = isRemoteDesktopEnabled;
	}
	
}

