/*
 * Copyright (c) 2008 University of Durham, England All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of 'SynergyNet' nor the names of
 * its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission. THIS SOFTWARE IS PROVIDED
 * BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package synergynetframework.appsystem.contentsystem.jme.items;

import java.awt.geom.Point2D.Float;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IContentItemImplementation;
import synergynetframework.appsystem.contentsystem.items.utils.Background;
import synergynetframework.appsystem.contentsystem.items.utils.Border;
import synergynetframework.appsystem.contentsystem.items.utils.Direction;
import synergynetframework.appsystem.contentsystem.items.utils.Location;

import com.jme.bounding.OrthogonalBoundingBox;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.scene.Spatial.CullHint;
import com.jme.system.DisplaySystem;

/**
 * The Class JMEContentItem.
 */
public class JMEContentItem implements IContentItemImplementation {
	
	/** The angle value. */
	protected float angleValue = 0f;

	/** The content item. */
	protected ContentItem contentItem;

	/** The is boundary enabled. */
	protected boolean isBoundaryEnabled;

	/** The manipulate. */
	protected boolean manipulate;

	/** The scale value. */
	protected float scaleValue = 1f;
	
	/** The screen heigth. */
	protected int screenHeigth = DisplaySystem.getDisplaySystem().getHeight();

	/** The screen width. */
	protected int screenWidth = DisplaySystem.getDisplaySystem().getWidth();

	/** The spatial. */
	protected Spatial spatial;

	/**
	 * Instantiates a new JME content item.
	 *
	 * @param contentItem
	 *            the content item
	 * @param spatial
	 *            the spatial
	 */
	public JMEContentItem(ContentItem contentItem, Spatial spatial) {
		this.contentItem = contentItem;
		this.spatial = spatial;
		this.initSpatial();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IContentItemImplementation#addMultitouchListener()
	 */
	@Override
	public void addMultitouchListener() {

	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IContentItemImplementation#contains(java.awt.geom.Point2D.Float)
	 */
	@Override
	public boolean contains(Float point) {
		if (this.spatial.getWorldBound().contains(
				new Vector3f(point.x, point.y, 0))) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IContentItemImplementation#getImplementationObject()
	 */
	public Object getImplementationObject() {
		return this.spatial;
	}
	
	/**
	 * Gets the local location.
	 *
	 * @return the local location
	 */
	public Location getLocalLocation() {
		Vector3f loc = this.spatial.getLocalTranslation();
		return new Location(loc.x, loc.y, loc.z);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IContentItemImplementation#hasCollision(synergynetframework.appsystem.
	 * contentsystem.items.ContentItem)
	 */
	@Override
	public boolean hasCollision(ContentItem contentItem) {
		
		return this.spatial.hasCollision(
				(Spatial) contentItem.getImplementationObject(), true);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IContentItemImplementation#init()
	 */
	public void init() {
		this.initSpatial();
	}
	
	/**
	 * Inits the spatial.
	 */
	protected void initSpatial() {
		this.setLocalLocation(contentItem.getLocalLocation());
		this.setAngle(contentItem.getAngle());
		this.setScale(contentItem.getScale());

		this.spatial.setModelBound(new OrthogonalBoundingBox());
		this.spatial.updateGeometricState(0f, true);
		this.spatial.updateModelBound();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IContentItemImplementation#setAngle(float)
	 */
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

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IContentItemImplementation#setBackGround(synergynetframework.appsystem.
	 * contentsystem.items.utils.Background)
	 */
	@Override
	public void setBackGround(Background backGround) {
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IContentItemImplementation#setBorder(synergynetframework.appsystem.
	 * contentsystem.items.utils.Border)
	 */
	@Override
	public void setBorder(Border border) {
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IContentItemImplementation#setBoundaryEnabled(boolean)
	 */
	@Override
	public void setBoundaryEnabled(boolean isBoundaryEnabled) {
		this.isBoundaryEnabled = isBoundaryEnabled;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IContentItemImplementation#setId(java.lang.String)
	 */
	@Override
	public void setId(String id) {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .
	 * IContentItemImplementation#setLocalLocation(synergynetframework.appsystem
	 * .contentsystem.items.utils.Location)
	 */
	@Override
	public void setLocalLocation(Location location) {
		float x = location.getX();
		float y = location.getY();
		float z = location.getZ();
		this.spatial.setLocalTranslation(x, y, z);
		this.spatial.updateGeometricState(0f, true);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IContentItemImplementation#setLocation(synergynetframework.appsystem.
	 * contentsystem.items.utils.Location)
	 */
	@Override
	public void setLocation(Location location) {
		Location loc = location;
		this.setLocalLocation(loc);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IContentItemImplementation#setManipulate(boolean)
	 */
	@Override
	public void setManipulate(boolean manipulate) {
		this.manipulate = manipulate;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IContentItemImplementation#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.spatial.setName(name);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IContentItemImplementation#setScale(float)
	 */
	@Override
	public void setScale(float scaleFactor) {
		float scale = scaleFactor;
		this.spatial.setLocalScale(scale);
		this.spatial.updateGeometricState(0f, true);
		scaleValue = scale;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IContentItemImplementation#setScale(float,
	 * synergynetframework.appsystem.contentsystem.items.utils.Direction)
	 */
	@Override
	public void setScale(float scaleFactor, Direction direction) {
		float scale = scaleFactor;
		if (direction == Direction.X) {
			this.spatial.setLocalScale(new Vector3f(scale, this.spatial
					.getLocalScale().y, this.spatial.getLocalScale().z));
		} else if (direction == Direction.Y) {
			this.spatial.setLocalScale(new Vector3f(this.spatial
					.getLocalScale().x, scale, this.spatial.getLocalScale().z));
		} else if (direction == Direction.Z) {
			this.spatial.setLocalScale(new Vector3f(this.spatial
					.getLocalScale().x, this.spatial.getLocalScale().y, scale));
		}
		this.spatial.updateGeometricState(0f, true);
		scaleValue = scale;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IContentItemImplementation#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean isVisible) {
		if (isVisible) {
			this.spatial.setCullHint(CullHint.Never);
			this.spatial.setIsCollidable(true);
		} else {
			this.spatial.setIsCollidable(false);
			this.spatial.setCullHint(CullHint.Always);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IContentItemImplementation#setVisible(boolean, boolean)
	 */
	@Override
	public void setVisible(boolean isVisible, boolean isUntouchable) {
		if (isVisible) {
			this.spatial.setCullHint(CullHint.Never);
			this.spatial.setIsCollidable(true);
		} else {
			if (isUntouchable) {
				this.spatial.setIsCollidable(false);
			}
			this.spatial.setCullHint(CullHint.Always);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IContentItemImplementation#update()
	 */
	public void update() {
		setVisible(this.contentItem.isVisible());
		setScale(this.contentItem.getScale());
		setAngle(this.contentItem.getAngle());
		setLocalLocation(this.contentItem.getLocalLocation());
		this.setManipulate(this.contentItem.canManipulate());
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IContentItemImplementation#update(float)
	 */
	public void update(float interpolation) {

	}
	
}
