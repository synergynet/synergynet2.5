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

package apps.remotecontrol.networkmanager.messages;

import java.util.HashMap;

import synergynetframework.appsystem.contentsystem.items.utils.Location;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.tablecomms.messages.application.UnicastApplicationMessage;


/**
 * The Class RequestItemsPortalMessage.
 */
public class RequestItemsPortalMessage extends UnicastApplicationMessage {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -546746344864227474L;
	
	/** The item names. */
	private HashMap<String, Location> itemNames =new HashMap<String, Location>();
	
	/** The delete. */
	private boolean delete = false;
	
	/** The target table id. */
	private TableIdentity targetTableId;
	
	/**
	 * Instantiates a new request items portal message.
	 */
	public RequestItemsPortalMessage(){
		super();
	}
	
	/**
	 * Instantiates a new request items portal message.
	 *
	 * @param targetClass the target class
	 * @param tableId the table id
	 * @param itemNames the item names
	 * @param targetTableId the target table id
	 * @param deleteItems the delete items
	 */
	public RequestItemsPortalMessage(Class<?> targetClass, TableIdentity tableId, HashMap<String, Location> itemNames, TableIdentity targetTableId, boolean deleteItems) {
		super(targetClass);
		this.itemNames = itemNames;
		this.targetTableId = targetTableId;
		this.delete = deleteItems;
		this.setRecipient(tableId);
	}

	/**
	 * Gets the item names.
	 *
	 * @return the item names
	 */
	public HashMap<String, Location> getItemNames() {
		return itemNames;
	}
	
	/**
	 * Delete items.
	 *
	 * @return true, if successful
	 */
	public boolean deleteItems(){
		return delete;
	}
	
	/**
	 * Gets the target table id.
	 *
	 * @return the target table id
	 */
	public TableIdentity getTargetTableId(){
		return this.targetTableId;
	}
}

