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

package synergynetframework.appsystem.services.net.networkedcontentmanager.messages.networkedflick;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.tablecomms.messages.application.UnicastApplicationMessage;


/**
 * The Class TransferableContentItem.
 */
public class TransferableContentItem extends UnicastApplicationMessage{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -86970287707283307L;
	
	/** The item. */
	private ContentItem item;
	
	/** The deceleration. */
	private float deceleration;
	
	/** The velocity y. */
	private float velocityX, velocityY;
	
	/** The arrival location stats. */
	private float[] arrivalLocationStats;
	
	/** The rotation. */
	private float rotation;
	
	/** The scale. */
	private float scale;
	
	/** The max scale. */
	private float minScale, maxScale;
	
	/**
	 * Instantiates a new transferable content item.
	 *
	 * @param targetClass the target class
	 * @param item the item
	 * @param targetTableId the target table id
	 */
	public TransferableContentItem(Class<?> targetClass, ContentItem item, TableIdentity targetTableId){
		super(targetClass);
		this.item = item;
		this.setRecipient(targetTableId);
	}
	
	/**
	 * Gets the content item.
	 *
	 * @return the content item
	 */
	public ContentItem getContentItem(){
		return item;
	}
	
	/**
	 * Gets the deceleration.
	 *
	 * @return the deceleration
	 */
	public float getDeceleration(){
		return deceleration;
	}
	
	/**
	 * Sets the deceleration.
	 *
	 * @param deceleration the new deceleration
	 */
	public void setDeceleration(float deceleration){
		this.deceleration = deceleration;
	}
	
	/**
	 * Gets the linear velocity x.
	 *
	 * @return the linear velocity x
	 */
	public float getLinearVelocityX(){
		return velocityX;
	}
	
	/**
	 * Gets the linear velocity y.
	 *
	 * @return the linear velocity y
	 */
	public float getLinearVelocityY(){
		return velocityY;
	}
	
	/**
	 * Sets the linear velocity.
	 *
	 * @param velocityX the velocity x
	 * @param velocityY the velocity y
	 */
	public void setLinearVelocity(float velocityX, float velocityY){
		this.velocityX = velocityX;
		this.velocityY = velocityY;		
	}
	
	/**
	 * Sets the location stats.
	 *
	 * @param arrivalLocationStats the new location stats
	 */
	public void setLocationStats(float[] arrivalLocationStats) {
		this.arrivalLocationStats = arrivalLocationStats;
	}
	
	/**
	 * Gets the location stats.
	 *
	 * @return the location stats
	 */
	public float[] getLocationStats() {
		return arrivalLocationStats;
	}
	
	/**
	 * Sets the roation.
	 *
	 * @param rotation the new roation
	 */
	public void setRoation(float rotation) {
		this.rotation = rotation;
	}
	
	/**
	 * Gets the roation.
	 *
	 * @return the roation
	 */
	public float getRoation() {
		return rotation;
	}
	
	/**
	 * Sets the scale.
	 *
	 * @param scale the new scale
	 */
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	/**
	 * Gets the scale.
	 *
	 * @return the scale
	 */
	public float getScale() {
		return scale;
	}
	
	/**
	 * Sets the min scale.
	 *
	 * @param minScale the new min scale
	 */
	public void setMinScale(float minScale){
		this.minScale = minScale;
	}
	
	/**
	 * Gets the min scale.
	 *
	 * @return the min scale
	 */
	public float getMinScale(){
		return minScale;
	}
	
	/**
	 * Sets the max scale.
	 *
	 * @param maxScale the new max scale
	 */
	public void setMaxScale(float maxScale){
		this.maxScale = maxScale;
	}
	
	/**
	 * Gets the max scale.
	 *
	 * @return the max scale
	 */
	public float getMaxScale(){
		return maxScale;
	}

}
