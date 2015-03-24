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

package synergynetframework.appsystem.services.net.rapidnetworkmanager.messages;

import java.util.HashMap;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.services.net.tablecomms.messages.application.BroadcastApplicationMessage;

/**
 * The Class BroadcastItemConstructionMessage.
 */
public class BroadcastItemConstructionMessage extends
		BroadcastApplicationMessage {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7039740871298771630L;

	/** The construct info. */
	HashMap<ContentItem, HashMap<String, Object>> constructInfo;

	/**
	 * Instantiates a new broadcast item construction message.
	 */
	public BroadcastItemConstructionMessage() {
		super();
	}

	/**
	 * Instantiates a new broadcast item construction message.
	 *
	 * @param targetClass
	 *            the target class
	 * @param constructInfo
	 *            the construct info
	 */
	public BroadcastItemConstructionMessage(Class<?> targetClass,
			HashMap<ContentItem, HashMap<String, Object>> constructInfo) {
		super(targetClass);
		this.constructInfo = constructInfo;
	}
	
	/**
	 * Gets the construction info.
	 *
	 * @return the construction info
	 */
	public HashMap<ContentItem, HashMap<String, Object>> getConstructionInfo() {
		return constructInfo;
	}

	/**
	 * Sets the construction info.
	 *
	 * @param constructInfo
	 *            the construct info
	 */
	public void setConstructionInfo(
			HashMap<ContentItem, HashMap<String, Object>> constructInfo) {
		this.constructInfo = constructInfo;
	}
}
