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
package apps.mathpadapp.networkmanager.messages.fromcontroller.unicast.touser;

import synergynetframework.appsystem.services.net.tablecomms.messages.application.UnicastApplicationMessage;
import apps.mathpadapp.networkmanager.utils.UserIdentity;

/**
 * The Class ControllerToUserMessage.
 */
public abstract class ControllerToUserMessage extends UnicastApplicationMessage {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5370070301710892935L;
	
	/** The user id. */
	protected UserIdentity userId;

	/**
	 * Instantiates a new controller to user message.
	 *
	 * @param targetClass
	 *            the target class
	 */
	public ControllerToUserMessage(Class<?> targetClass) {
		super(targetClass);
	}

	/**
	 * Gets the recipient user identity.
	 *
	 * @return the recipient user identity
	 */
	public UserIdentity getRecipientUserIdentity() {
		return userId;
	}

	/**
	 * Sets the recipient user.
	 *
	 * @param userId
	 *            the new recipient user
	 */
	public void setRecipientUser(UserIdentity userId) {
		this.userId = userId;
	}
}
