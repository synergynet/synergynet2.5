package apps.mtdesktop.messages;

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

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.tablecomms.messages.application.UnicastApplicationMessage;

/**
 * The Class MouseRedirectMessage.
 */
public class MouseRedirectMessage extends UnicastApplicationMessage {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3611717853521792797L;

	/** The mouse redirection enabled. */
	public boolean mouseRedirectionEnabled = false;
	
	/**
	 * Instantiates a new mouse redirect message.
	 */
	public MouseRedirectMessage() {
		super();
	}

	/**
	 * Instantiates a new mouse redirect message.
	 *
	 * @param targetClass
	 *            the target class
	 * @param targetTableId
	 *            the target table id
	 * @param mouseRedirectionEnabled
	 *            the mouse redirection enabled
	 */
	public MouseRedirectMessage(Class<?> targetClass,
			TableIdentity targetTableId, boolean mouseRedirectionEnabled) {
		super(targetClass);
		this.setRecipient(targetTableId);
		this.mouseRedirectionEnabled = mouseRedirectionEnabled;
	}

	/**
	 * Enable mouse redirection.
	 *
	 * @param mouseRedirectionEnabled
	 *            the mouse redirection enabled
	 */
	public void enableMouseRedirection(boolean mouseRedirectionEnabled) {
		this.mouseRedirectionEnabled = mouseRedirectionEnabled;
	}

	/**
	 * Checks if is mouse redirection enabled.
	 *
	 * @return true, if is mouse redirection enabled
	 */
	public boolean isMouseRedirectionEnabled() {
		return mouseRedirectionEnabled;
	}
}
