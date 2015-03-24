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

import java.io.Serializable;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.config.display.DisplayConfigPrefsItem;


/**
 * The Class TableInfo.
 */
public class TableInfo implements Serializable{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2158376162046543629L;
	
	/** The table id. */
	protected TableIdentity tableId;
	
	/** The position x. */
	protected int positionX;
	
	/** The position y. */
	protected int positionY;
	
	/** The angle. */
	protected float angle;
	
	/** The width. */
	private float width;
	
	/** The height. */
	private float height;
	
	/**
	 * Instantiates a new table info.
	 */
	public TableInfo()
	{}
	
	/**
	 * Instantiates a new table info.
	 *
	 * @param tableId the table id
	 * @param positionX the position x
	 * @param positionY the position y
	 * @param angle the angle
	 */
	public TableInfo(TableIdentity tableId, int positionX, int positionY, float angle)	{
		this.tableId = tableId;
		this.positionX = positionX;
		this.positionY = positionY;
		this.angle = angle;
		
		float scaleX = new DisplayConfigPrefsItem().getWidth();
		float scaleY = new DisplayConfigPrefsItem().getHeight();
		this.width = scaleX;
		this.height = scaleY;
	}
	
	/**
	 * Sets the table position.
	 *
	 * @param positionX the position x
	 * @param positionY the position y
	 */
	public void setTablePosition(int positionX, int positionY)	{
		this.positionX = positionX;
		this.positionY = positionY;
	}
	
	/**
	 * Gets the table position x.
	 *
	 * @return the table position x
	 */
	public int getTablePositionX()	{
		return positionX;
	}
	
	/**
	 * Gets the table position y.
	 *
	 * @return the table position y
	 */
	public int getTablePositionY()	{
		return positionY;
	}
	
	/**
	 * Sets the angle.
	 *
	 * @param angle the new angle
	 */
	public void setAngle(float angle){
		this.angle = angle;
	}
	
	/**
	 * Gets the angle.
	 *
	 * @return the angle
	 */
	public float getAngle(){
		return angle;
	}
	
	/**
	 * Sets the table id.
	 *
	 * @param tableId the new table id
	 */
	public void setTableId(TableIdentity tableId)	{
		this.tableId = tableId;
	}
	
	/**
	 * Gets the table id.
	 *
	 * @return the table id
	 */
	public TableIdentity getTableId(){
		return tableId;
	}

	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public float getWidth() {
		return width;
	}
	
	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public float getHeight() {
		return height;
	}
	
}
