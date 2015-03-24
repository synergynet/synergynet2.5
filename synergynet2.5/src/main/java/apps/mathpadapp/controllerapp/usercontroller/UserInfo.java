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

package apps.mathpadapp.controllerapp.usercontroller;

import apps.mathpadapp.networkmanager.utils.UserIdentity;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;


/**
 * The Class UserInfo.
 */
public class UserInfo {
	
	/**
	 * The Enum UserStatus.
	 */
	public enum UserStatus{/** The online. */
ONLINE, /** The offline. */
 OFFLINE, /** The blocked. */
 BLOCKED};
	
	/** The user status. */
	protected UserStatus userStatus = UserStatus.ONLINE;
	
	/** The user id. */
	protected UserIdentity userId;
	
	/** The table id. */
	protected TableIdentity tableId;
	
	/**
	 * Instantiates a new user info.
	 *
	 * @param userId the user id
	 */
	public UserInfo(UserIdentity userId){
		this.userId = userId;
	}
	
	/**
	 * Gets the user status.
	 *
	 * @return the user status
	 */
	public UserStatus getUserStatus() {
		return userStatus;
	}
	
	/**
	 * Sets the user status.
	 *
	 * @param userStatus the new user status
	 */
	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}
	
	/**
	 * Gets the user identity.
	 *
	 * @return the user identity
	 */
	public UserIdentity getUserIdentity() {
		return userId;
	}
	
	/**
	 * Sets the user identity.
	 *
	 * @param userId the new user identity
	 */
	public void setUserIdentity(UserIdentity userId) {
		this.userId = userId;
	}
	
	/**
	 * Gets the table identity.
	 *
	 * @return the table identity
	 */
	public TableIdentity getTableIdentity(){
		return tableId;
	}
	
	/**
	 * Sets the table identity.
	 *
	 * @param tableId the new table identity
	 */
	public void setTableIdentity(TableIdentity tableId){
		this.tableId = tableId;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return userId.toString();
	}
}
