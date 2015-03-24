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

package synergynetframework.appsystem.services.net.tablecomms.common;

import synergynetframework.appsystem.services.net.objectmessaging.connections.ConnectionHandler;


/**
 * The Class ObjectQueueEntry.
 */
public class ObjectQueueEntry {

	/** The handler. */
	private ConnectionHandler handler;
	
	/** The obj. */
	private Object obj;

	/**
	 * Instantiates a new object queue entry.
	 *
	 * @param obj the obj
	 * @param handler the handler
	 */
	public ObjectQueueEntry(Object obj, ConnectionHandler handler) {
		this.setObj(obj);
		this.setHandler(handler);
	}

	/**
	 * Sets the handler.
	 *
	 * @param handler the new handler
	 */
	public void setHandler(ConnectionHandler handler) {
		this.handler = handler;
	}

	/**
	 * Gets the handler.
	 *
	 * @return the handler
	 */
	public ConnectionHandler getHandler() {
		return handler;
	}

	/**
	 * Sets the obj.
	 *
	 * @param obj the new obj
	 */
	public void setObj(Object obj) {
		this.obj = obj;
	}

	/**
	 * Gets the obj.
	 *
	 * @return the obj
	 */
	public Object getObj() {
		return obj;
	}

}
