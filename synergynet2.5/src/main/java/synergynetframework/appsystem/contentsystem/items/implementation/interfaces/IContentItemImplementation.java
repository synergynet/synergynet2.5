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

package synergynetframework.appsystem.contentsystem.items.implementation.interfaces;

import java.awt.geom.Point2D;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.utils.Background;
import synergynetframework.appsystem.contentsystem.items.utils.Border;
import synergynetframework.appsystem.contentsystem.items.utils.Direction;
import synergynetframework.appsystem.contentsystem.items.utils.Location;


/**
 * The Interface IContentItemImplementation.
 */
public interface IContentItemImplementation {
	
	/**
	 * Sets the local location.
	 *
	 * @param location the new local location
	 */
	public void setLocalLocation(Location location);
	
	/**
	 * Sets the location.
	 *
	 * @param location the new location
	 */
	public void setLocation(Location location);
	
	/**
	 * Sets the angle.
	 *
	 * @param angle the new angle
	 */
	public void setAngle(float angle);	
	
	/**
	 * Sets the scale.
	 *
	 * @param scaleFactor the new scale
	 */
	public void setScale(float scaleFactor);	
	
	/**
	 * Sets the scale.
	 *
	 * @param scaleFactor the scale factor
	 * @param direction the direction
	 */
	public void setScale(float scaleFactor, Direction direction);	
	
	/**
	 * Sets the back ground.
	 *
	 * @param backGround the new back ground
	 */
	public void setBackGround(Background backGround);	
	
	/**
	 * Sets the border.
	 *
	 * @param border the new border
	 */
	public void setBorder(Border border);	
	
	/**
	 * Sets the visible.
	 *
	 * @param isVisible the new visible
	 */
	public void setVisible(boolean isVisible);
	
	/**
	 * Sets the visible.
	 *
	 * @param isVisible the is visible
	 * @param isUntouchable the is untouchable
	 */
	public void setVisible(boolean isVisible, boolean isUntouchable);
	
	/**
	 * Sets the manipulate.
	 *
	 * @param manipulate the new manipulate
	 */
	public void setManipulate(boolean manipulate);	
	
	/**
	 * Sets the boundary enabled.
	 *
	 * @param isBoundaryEnabled the new boundary enabled
	 */
	public void setBoundaryEnabled(boolean isBoundaryEnabled);
	
	/**
	 * Contains.
	 *
	 * @param point the point
	 * @return true, if successful
	 */
	public boolean contains(Point2D.Float point);
	
	/**
	 * Gets the implementation object.
	 *
	 * @return the implementation object
	 */
	public Object getImplementationObject();
	
	/**
	 * Adds the multitouch listener.
	 */
	public void addMultitouchListener();
	
	/**
	 * Inits the.
	 */
	public void init();
	
	/**
	 * Checks for collision.
	 *
	 * @param contentItem the content item
	 * @return true, if successful
	 */
	public boolean hasCollision(ContentItem contentItem);
	
	/**
	 * Update.
	 */
	public void update();
	
	/**
	 * Update.
	 *
	 * @param interpolation the interpolation
	 */
	public void update(float interpolation);
	
	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name);
	
	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(String id);
}
