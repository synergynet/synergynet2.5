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


package apps.mysteries.client.restore;

public class ContentItemState 
{
	private String name;
	private float location_x;
	private float location_y;
	private float location_z;
	private float scale_x = 1;
	private float scale_y = 1;
	private float scale_z = 1;
	private float rotation_x;
	private float rotation_y;
	private float rotation_z;
	private float rotation_w;
	private int zOrder;
	
	public ContentItemState()
	{}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setX(float location_x){
		this.location_x = location_x;
	}
	
	public void setY(float location_x){
		this.location_x = location_x;
	}
	
	public void setZ(float location_x){
		this.location_x = location_x;
	}
	
	public void setLocation(float location_x, float location_y, float location_z) {
		this.location_x = location_x;
		this.location_y = location_y;
		this.location_z = location_z;
	}
	
	public float getX(){
		return location_x;
	}
	
	public float getY(){
		return location_y;
	}
	
	public float getZ(){
		return location_z;
	}
	
	public void setScale(float scaleFactor){
		this.scale_x= scaleFactor;
		this.scale_y= scaleFactor;
		this.scale_z= scaleFactor;
	}
	
	public void setScale(float scale_x, float scale_y, float scale_z){
		this.scale_x= scale_x;
		this.scale_y= scale_y;
		this.scale_z= scale_z;
	}
	
	public float getScaleX(){
		return scale_x;
	}
	
	public float getScaleY() {
		return scale_y;
	}
	
	public float getScaleZ() {
		return scale_z;
	}
	
	public void setRotation(float rotation_x, float rotation_y, float rotation_z, float rotation_w){
		this.rotation_x = rotation_x;
		this.rotation_y = rotation_y;
		this.rotation_z = rotation_z;
		this.rotation_w = rotation_w;
	}
	
	public void setRotationX(float rotation_x)	{
		this.rotation_x = rotation_x;
	}
	
	public float getRotationX() {
		return rotation_x;
	}

	public void setRotationY(float rotation_y)	{
		this.rotation_y = rotation_y;
	}
	
	public float getRotationY() {
		return rotation_y;
	}
	
	public void setRotationZ(float rotation_z)	{
		this.rotation_z = rotation_z;
	}
	
	public float getRotationZ() {
		return rotation_z;
	}
	
	public void setRotationW(float rotation_w)	{
		this.rotation_w = rotation_w;
	}
	
	public float getRotationW() {
		return rotation_w;
	}
	
	public void setZOrder(int zOrder) {
		this.zOrder = zOrder;
	}
	
	public int getZOrder() {
		return zOrder;
	}
}
