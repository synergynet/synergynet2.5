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

public class TransferableContentItem extends UnicastApplicationMessage{
	
	private static final long serialVersionUID = -86970287707283307L;
	private ContentItem item;
	private float deceleration;
	private float velocityX, velocityY;
	private float[] arrivalLocationStats;
	private float rotation;
	private float scale;
	private float minScale, maxScale;
	
	public TransferableContentItem(Class<?> targetClass, ContentItem item, TableIdentity targetTableId){
		super(targetClass);
		this.item = item;
		this.setRecipient(targetTableId);
	}
	
	public ContentItem getContentItem(){
		return item;
	}
	
	public float getDeceleration(){
		return deceleration;
	}
	
	public void setDeceleration(float deceleration){
		this.deceleration = deceleration;
	}
	
	public float getLinearVelocityX(){
		return velocityX;
	}
	
	public float getLinearVelocityY(){
		return velocityY;
	}
	
	public void setLinearVelocity(float velocityX, float velocityY){
		this.velocityX = velocityX;
		this.velocityY = velocityY;		
	}
	
	public void setLocationStats(float[] arrivalLocationStats) {
		this.arrivalLocationStats = arrivalLocationStats;
	}
	
	public float[] getLocationStats() {
		return arrivalLocationStats;
	}
	
	public void setRoation(float rotation) {
		this.rotation = rotation;
	}
	
	public float getRoation() {
		return rotation;
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public float getScale() {
		return scale;
	}
	
	public void setMinScale(float minScale){
		this.minScale = minScale;
	}
	
	public float getMinScale(){
		return minScale;
	}
	
	public void setMaxScale(float maxScale){
		this.maxScale = maxScale;
	}
	
	public float getMaxScale(){
		return maxScale;
	}

}
