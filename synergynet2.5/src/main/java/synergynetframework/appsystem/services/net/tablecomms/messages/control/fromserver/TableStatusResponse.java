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

package synergynetframework.appsystem.services.net.tablecomms.messages.control.fromserver;

import java.util.HashMap;
import java.util.Map;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.tablecomms.messages.control.FromServerTableControlMessage;

/**
 * The Class TableStatusResponse.
 */
public class TableStatusResponse extends FromServerTableControlMessage {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -9011198980322160944L;

	/** The Constant STATUS_OFFLINE. */
	public static final String STATUS_OFFLINE = "STATUS_OFFLINE";

	/** The Constant STATUS_ONLINE. */
	public static final String STATUS_ONLINE = "STATUS_ONLINE";

	/** The statuses. */
	protected Map<TableIdentity, String> statuses = new HashMap<TableIdentity, String>();

	/**
	 * Instantiates a new table status response.
	 */
	public TableStatusResponse() {
		super();
	}

	/**
	 * Adds the status.
	 *
	 * @param id
	 *            the id
	 * @param status
	 *            the status
	 */
	public void addStatus(TableIdentity id, String status) {
		statuses.put(id, status);
	}
	
	/**
	 * Gets the statuses.
	 *
	 * @return the statuses
	 */
	public Map<TableIdentity, String> getStatuses() {
		return statuses;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return TableStatusResponse.class.getName() + " from " + getSender()
				+ ": " + statuses;
	}
}
