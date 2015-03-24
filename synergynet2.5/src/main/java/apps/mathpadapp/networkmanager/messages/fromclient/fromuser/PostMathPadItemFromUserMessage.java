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

package apps.mathpadapp.networkmanager.messages.fromclient.fromuser;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import apps.mathpadapp.mathtool.MathToolInitSettings;
import apps.mathpadapp.networkmanager.utils.UserIdentity;

/**
 * The Class PostMathPadItemFromUserMessage.
 */
public class PostMathPadItemFromUserMessage extends UserToControllerMessage {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7600576282368122789L;

	/** The pad settings. */
	private MathToolInitSettings padSettings;

	/**
	 * Instantiates a new post math pad item from user message.
	 *
	 * @param targetClass
	 *            the target class
	 * @param padSettings
	 *            the pad settings
	 * @param tableId
	 *            the table id
	 * @param userId
	 *            the user id
	 */
	public PostMathPadItemFromUserMessage(Class<?> targetClass,
			MathToolInitSettings padSettings, TableIdentity tableId,
			UserIdentity userId) {
		super(targetClass);
		this.padSettings = padSettings;
		this.userId = userId;
		this.setRecipient(tableId);
	}
	
	/**
	 * Gets the math pad init settings.
	 *
	 * @return the math pad init settings
	 */
	public MathToolInitSettings getMathPadInitSettings() {
		return padSettings;
	}
}
