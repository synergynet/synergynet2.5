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
import apps.mtdesktop.desktop.DesktopClient;

/**
 * The Class UnicastSearchTableMessage.
 */
public class UnicastSearchTableMessage extends UnicastApplicationMessage {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3611717853521792797L;

	/** The position. */
	private DesktopClient.Position position;

	/**
	 * Instantiates a new unicast search table message.
	 */
	public UnicastSearchTableMessage() {
		super();
	}

	/**
	 * Instantiates a new unicast search table message.
	 *
	 * @param targetClass
	 *            the target class
	 * @param targetTableId
	 *            the target table id
	 * @param position
	 *            the position
	 */
	public UnicastSearchTableMessage(Class<?> targetClass,
			TableIdentity targetTableId, DesktopClient.Position position) {
		super(targetClass);
		this.setRecipient(targetTableId);
		this.setClientPosition(position);
	}

	/**
	 * Gets the client position.
	 *
	 * @return the client position
	 */
	public DesktopClient.Position getClientPosition() {
		return position;
	}

	/**
	 * Sets the client position.
	 *
	 * @param position
	 *            the new client position
	 */
	public void setClientPosition(DesktopClient.Position position) {
		this.position = position;
	}
}
