/* Copyright (c) 2008 University of Durham, England
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

package synergynetframework.appsystem.contentsystem.jme.items;

import java.awt.geom.Point2D.Float;

import com.jme.bounding.OrthogonalBoundingBox;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.scene.Spatial.CullHint;
import com.jme.system.DisplaySystem;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IContentItemImplementation;
import synergynetframework.appsystem.contentsystem.items.utils.Background;
import synergynetframework.appsystem.contentsystem.items.utils.Border;
import synergynetframework.appsystem.contentsystem.items.utils.Direction;
import synergynetframework.appsystem.contentsystem.items.utils.Location;

public class JMEContentItem implements IContentItemImplementation {

	protected int screenWidth = DisplaySystem.getDisplaySystem().getWidth();
	protected int screenHeigth = DisplaySystem.getDisplaySystem().getHeight();
	protected ContentItem contentItem;
	protected Spatial spatial;
	protected boolean manipulate; 
	protected boolean isBoundaryEnabled;
	protected float scaleValue = 1f;
	protected float angleValue = 0f;
	
	public JMEContentItem(ContentItem contentItem, Spatial spatial){
		this.contentItem = contentItem;
		this.spatial = spatial;
		this.initSpatial();	
	}
	
	public void init(){
		this.initSpatial();
	}

	@Override
	public void addMultitouchListener() {
		
	}
	
	@Override
	public void setName(String name){
		this.spatial.setName(name);
	}
	

	@Override
	public boolean contains(Float point) {
		if(this.spatial.getWorldBound().contains(new Vector3f(point.x, point.y, 0)))
			return true;	
		return false;
	}

	@Override
	public void setAngle(float angle) {	
		float orientation = angle;
		Quaternion q = new Quaternion();
		float[] angles = new float[3];
		angles[2] = orientation;
		q.fromAngles(angles);
		this.spatial.setLocalRotation(q);
		this.spatial.updateGeometricState(0f, true);	
		angleValue = orientation;
	}

	@Override
	public void setBackGround(Background backGround) {	}

	@Override
	public void setBorder(Border border) {	}
	
	@Override
	public void setBoundaryEnabled(boolean isBoundaryEnabled) {
		this.isBoundaryEnabled = isBoundaryEnabled;
	}
	
	@Override
	public void setLocalLocation(Location location) {
		float x = location.getX();
		float y = location.getY();
		float z = location.getZ();		
		this.spatial.setLocalTranslation(x, y, z);
		this.spatial.updateGeometricState(0f, true);	
	}

	@Override
	public void setManipulate(boolean manipulate) {
		this.manipulate = manipulate;		
	}

	@Override
	public void setScale(float scaleFactor) {
		float scale = scaleFactor;
		this.spatial.setLocalScale(scale);
		this.spatial.updateGeometricState(0f, true);		
		scaleValue = scale;
	}

	@Override
	public void setScale(float scaleFactor, Direction direction) {
		float scale = scaleFactor;
		if (direction ==Direction.X){
			this.spatial.setLocalScale(new Vector3f(scale, this.spatial.getLocalScale().y, this.spatial.getLocalScale().z));
		}
		else if (direction ==Direction.Y){
			this.spatial.setLocalScale(new Vector3f(this.spatial.getLocalScale().x, scale, this.spatial.getLocalScale().z));
		}
		else if (direction ==Direction.Z){
			this.spatial.setLocalScale(new Vector3f(this.spatial.getLocalScale().x, this.spatial.getLocalScale().y, scale));
		}
		this.spatial.updateGeometricState(0f, true);		
		scaleValue = scale;
	}

	@Override
	public void setVisible(boolean isVisible) {
		if(isVisible){
			this.spatial.setCullHint(CullHint.Never);
			this.spatial.setIsCollidable(true);
		}
		else{
			this.spatial.setIsCollidable(false);
			this.spatial.setCullHint(CullHint.Always);		
		}
	}

	
	@Override  
	public void setVisible(boolean isVisible, boolean isUntouchable) {  
		if(isVisible){  
			this.spatial.setCullHint(CullHint.Never);
			this.spatial.setIsCollidable(true);  }
		else{  
			if (isUntouchable)  
				this.spatial.setIsCollidable(false);  
			this.spatial.setCullHint(CullHint.Always);  
			}  
		} 
	

	public Object getImplementationObject(){
		return this.spatial;
	}
		
	protected void initSpatial(){
		this.setLocalLocation(contentItem.getLocalLocation());
		this.setAngle(contentItem.getAngle());
		this.setScale(contentItem.getScale());
		
		this.spatial.setModelBound(new OrthogonalBoundingBox());
		this.spatial.updateGeometricState(0f, true);
		this.spatial.updateModelBound();
	}
	
	@Override
	public boolean hasCollision(ContentItem contentItem){

		return this.spatial.hasCollision((Spatial)contentItem.getImplementationObject(), true);
	}
	
	public void update(){
		setVisible(this.contentItem.isVisible());
		setScale(this.contentItem.getScale());
		setAngle(this.contentItem.getAngle());
		setLocalLocation(this.contentItem.getLocalLocation());
		this.setManipulate(this.contentItem.canManipulate());
	}
	
	public void update(float interpolation) {
		
	}

	@Override
	public void setLocation(Location location) {
		Location loc = location;
		this.setLocalLocation(loc);
	}
	
	public Location getLocalLocation() {
		Vector3f loc = this.spatial.getLocalTranslation();
		return new Location(loc.x, loc.y, loc.z);
	}

	@Override
	public void setId(String id) {
		 
		
	}
	
	
}
