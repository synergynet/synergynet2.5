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

public interface IContentItemImplementation {
	
	public void setLocalLocation(Location location);
	public void setLocation(Location location);
	public void setAngle(float angle);	
	public void setScale(float scaleFactor);	
	public void setScale(float scaleFactor, Direction direction);	
	public void setBackGround(Background backGround);	
	public void setBorder(Border border);	
	public void setVisible(boolean isVisible);
	public void setVisible(boolean isVisible, boolean isUntouchable);
	public void setManipulate(boolean manipulate);	
	public void setBoundaryEnabled(boolean isBoundaryEnabled);
	public boolean contains(Point2D.Float point);
	public Object getImplementationObject();
	public void addMultitouchListener();
	public void init();
	public boolean hasCollision(ContentItem contentItem);
	public void update();
	public void update(float interpolation);
	public void setName(String name);
	public void setId(String id);
}
