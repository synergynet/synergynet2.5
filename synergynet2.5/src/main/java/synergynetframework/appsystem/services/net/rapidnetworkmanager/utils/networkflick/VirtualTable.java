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
package synergynetframework.appsystem.services.net.rapidnetworkmanager.utils.networkflick;

import com.jme.scene.shape.Quad;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;

public class VirtualTable extends Quad{
	
	private static final long serialVersionUID = 3055508502964905444L;
	private TableIdentity tableId;
	
	public VirtualTable(VirtualTable otherTable){
		super(otherTable.getTableId().toString(), otherTable.getWidth(), otherTable.getHeight());
		this.tableId = otherTable.getTableId();
	}
	
	public VirtualTable(TableInfo remoteTableInfo){
		super(remoteTableInfo.getTableId().toString(), remoteTableInfo.getWidth(), remoteTableInfo.getHeight());
		this.tableId = remoteTableInfo.getTableId();
	}
	
	public void setTableId(TableIdentity tableId){
		this.tableId = tableId;
	}
	
	public TableIdentity getTableId(){
		return tableId;
	}
}
