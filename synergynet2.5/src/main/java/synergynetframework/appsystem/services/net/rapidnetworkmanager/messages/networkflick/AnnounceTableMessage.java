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

package synergynetframework.appsystem.services.net.rapidnetworkmanager.messages.networkflick;

import synergynetframework.appsystem.services.net.rapidnetworkmanager.utils.networkflick.TableInfo;
import synergynetframework.appsystem.services.net.tablecomms.messages.application.BroadcastApplicationMessage;

/**
 * The Class AnnounceTableMessage.
 */
public class AnnounceTableMessage extends BroadcastApplicationMessage {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6445490437873640139L;

	/** The table info. */
	TableInfo tableInfo;

	/**
	 * Instantiates a new announce table message.
	 *
	 * @param targetClass
	 *            the target class
	 */
	public AnnounceTableMessage(Class<?> targetClass) {
		super(targetClass);
		tableInfo = new TableInfo();
	}

	/**
	 * Instantiates a new announce table message.
	 *
	 * @param targetClass
	 *            the target class
	 * @param tableInfo
	 *            the table info
	 */
	public AnnounceTableMessage(Class<?> targetClass, TableInfo tableInfo) {
		super(targetClass);
		this.tableInfo = tableInfo;
	}
	
	/**
	 * Gets the table info.
	 *
	 * @return the table info
	 */
	public TableInfo getTableInfo() {
		return tableInfo;
	}

	/**
	 * Sets the table info.
	 *
	 * @param tableInfo
	 *            the new table info
	 */
	public void setTableInfo(TableInfo tableInfo) {
		this.tableInfo = tableInfo;
	}
}
