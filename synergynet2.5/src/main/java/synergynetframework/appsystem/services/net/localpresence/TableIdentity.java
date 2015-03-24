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

package synergynetframework.appsystem.services.net.localpresence;

import java.io.Serializable;

import synergynetframework.config.identity.IdentityConfigPrefsItem;


/**
 * The Class TableIdentity.
 */
public class TableIdentity implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7074157729446323520L;

	/** The instance. */
	private static TableIdentity instance;
	
	/** The uid. */
	protected String uid;

	/**
	 * Gets the table identity.
	 *
	 * @return the table identity
	 */
	public static TableIdentity getTableIdentity() {
		synchronized(TableIdentity.class) {
			if(instance == null) instance = new TableIdentity();
			return instance;
		}
	}

	/**
	 * Instantiates a new table identity.
	 */
	private TableIdentity() {
		uid = new IdentityConfigPrefsItem().getID();
	}
	
	/**
	 * Instantiates a new table identity.
	 *
	 * @param uid the uid
	 */
	public TableIdentity(String uid) {
		this.uid = uid;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if(obj instanceof TableIdentity) {
			TableIdentity t = (TableIdentity) obj;
			return t.hashCode() == this.hashCode(); 
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return uid.hashCode();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return uid;
	}
}
