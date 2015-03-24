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

package synergynetframework.appsystem.services.net.networkedcontentmanager.messages.remotedesktop;

import java.util.HashMap;
import java.util.Map;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.objectmessaging.messages.UDPMessage;
import synergynetframework.appsystem.services.net.tablecomms.messages.application.UnicastApplicationMessage;

/**
 * The Class SynchroniseRemoteDesktopData.
 */
public class SynchroniseRemoteDesktopData extends UnicastApplicationMessage
		implements UDPMessage {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -546746341111227474L;

	/** The items. */
	protected Map<String, Map<String, String>> items = new HashMap<String, Map<String, String>>();
	
	/**
	 * Instantiates a new synchronise remote desktop data.
	 *
	 * @param targetClass
	 *            the target class
	 * @param items
	 *            the items
	 * @param id
	 *            the id
	 */
	public SynchroniseRemoteDesktopData(Class<?> targetClass,
			Map<String, Map<String, String>> items, TableIdentity id) {
		super(targetClass);
		this.items.clear();
		for (String name : items.keySet()) {
			Map<String, String> attrs = items.get(name);
			Map<String, String> newAttrs = new HashMap<String, String>();
			for (String key : attrs.keySet()) {
				newAttrs.put(key, attrs.get(key));
			}
			this.items.put(name, newAttrs);
		}
		this.setRecipient(id);
	}
	
	/**
	 * Gets the items.
	 *
	 * @return the items
	 */
	public Map<String, Map<String, String>> getItems() {
		return items;
	}
	
}
