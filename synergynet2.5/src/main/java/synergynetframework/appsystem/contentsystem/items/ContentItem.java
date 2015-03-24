/*
 * Copyright (c) 2009 University of Durham, England All rights reserved.
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

package synergynetframework.appsystem.contentsystem.items;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.UUID;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.implementation.IImplementationItemFactory;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IContentItemImplementation;
import synergynetframework.appsystem.contentsystem.items.utils.Background;
import synergynetframework.appsystem.contentsystem.items.utils.Border;
import synergynetframework.appsystem.contentsystem.items.utils.Direction;
import synergynetframework.appsystem.contentsystem.items.utils.Location;

/**
 * The Class ContentItem.
 */
public class ContentItem implements Serializable, IContentItemImplementation {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6128005678748844738L;
	
	/** The angle. */
	protected float angle = 0;

	/** The back ground. */
	protected Background backGround = new Background(Color.lightGray);

	/** The border. */
	protected Border border = new Border(Color.white, 4);
	
	/** The content item implementation. */
	protected transient IContentItemImplementation contentItemImplementation;

	/** The content system. */
	protected transient ContentSystem contentSystem;

	/** The id. */
	protected String id;

	/** The implementation item factory. */
	protected IImplementationItemFactory implementationItemFactory;

	/** The is boundary enabled. */
	protected boolean isBoundaryEnabled = true;

	/** The is visible. */
	protected boolean isVisible = true;

	/** The location. */
	protected Location location = new Location(0, 0, 0);

	/** The manipulate. */
	protected boolean manipulate = false;

	/** The name. */
	public String name;

	/** The note. */
	protected String note = "";

	/** The resource. */
	protected String resource = "";

	/** The scale factor. */
	protected float scaleFactor = 1;
	
	/**
	 * Instantiates a new content item.
	 *
	 * @param contentSystem
	 *            the content system
	 * @param name
	 *            the name
	 */
	public ContentItem(ContentSystem contentSystem, String name) {
		this.contentSystem = contentSystem;
		this.name = name;
		this.implementationItemFactory = this.contentSystem
				.getImplementationItemFactory();
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

	/**
	 * Bind implementation ojbect.
	 */
	public void bindImplementationOjbect() {
		this.contentItemImplementation = this.implementationItemFactory
				.create(this);
		this.contentItemImplementation.addMultitouchListener();
	}
	
	/**
	 * Can manipulate.
	 *
	 * @return true, if successful
	 */
	public boolean canManipulate() {
		return manipulate;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		ContentItem clonedItem = (ContentItem) super.clone();
		clonedItem.name = name;
		clonedItem.location = (Location) location.clone();
		clonedItem.angle = angle;
		clonedItem.scaleFactor = scaleFactor;
		clonedItem.backGround = (Background) backGround.clone();
		clonedItem.border = (Border) border.clone();
		clonedItem.isVisible = isVisible;
		clonedItem.manipulate = manipulate;
		// TODO: need support container in future
		clonedItem.isBoundaryEnabled = isBoundaryEnabled;
		clonedItem.note = note;
		clonedItem.contentSystem = contentSystem;
		clonedItem.contentItemImplementation = contentItemImplementation;
		clonedItem.implementationItemFactory = implementationItemFactory;
		clonedItem.resource = resource;
		return clonedItem;
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IContentItemImplementation#contains(java.awt.geom.Point2D.Float)
	 */
	public boolean contains(Point2D.Float point) {
		return this.contentItemImplementation.contains(point);
	}
	
	/**
	 * Generate unique name.
	 *
	 * @return the string
	 */
	public String generateUniqueName() {
		return UUID.randomUUID().toString();
	}
	
	/**
	 * Gets the angle.
	 *
	 * @return the angle
	 */
	public float getAngle() {
		return angle;
	}
	
	/**
	 * Gets the back ground.
	 *
	 * @return the back ground
	 */
	public Background getBackGround() {
		return backGround;
	}
	
	/**
	 * Gets the background colour.
	 *
	 * @return the background colour
	 */
	public Color getBackgroundColour() {
		if (backGround == null) {
			return new Color(0, 0, 0, 0);
		}
		return this.backGround.getColour();
	}
	
	/**
	 * Gets the border.
	 *
	 * @return the border
	 */
	public Border getBorder() {
		return border;
	}

	/**
	 * Gets the border colour.
	 *
	 * @return the border colour
	 */
	public Color getBorderColour() {
		return this.border.getBorderColour();
	}

	/**
	 * Gets the border size.
	 *
	 * @return the border size
	 */
	public int getBorderSize() {
		return this.border.getBorderSize();
	}
	
	/**
	 * Gets the content system.
	 *
	 * @return the content system
	 */
	public ContentSystem getContentSystem() {
		return contentSystem;
	}
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IContentItemImplementation#getImplementationObject()
	 */
	public Object getImplementationObject() {
		return this.contentItemImplementation.getImplementationObject();
	}
	
	/**
	 * Gets the local location.
	 *
	 * @return the local location
	 */
	public Location getLocalLocation() {
		return location;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the note.
	 *
	 * @return the note
	 */
	public String getNote() {
		return note;
	}
	
	/**
	 * Gets the resource.
	 *
	 * @return the resource
	 */
	public String getResource() {
		return resource;
	}
	
	/**
	 * Gets the scale.
	 *
	 * @return the scale
	 */
	public float getScale() {
		return scaleFactor;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IContentItemImplementation#hasCollision(synergynetframework.appsystem.
	 * contentsystem.items.ContentItem)
	 */
	public boolean hasCollision(ContentItem otherItem) {
		return this.contentItemImplementation.hasCollision(otherItem);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IContentItemImplementation#init()
	 */
	public void init() {
	}
	
	/**
	 * Inits the implementation objet.
	 */
	public void initImplementationObjet() {
		this.contentItemImplementation.init();
	}
	
	/**
	 * Checks if is boundary enabled.
	 *
	 * @return true, if is boundary enabled
	 */
	public boolean isBoundaryEnabled() {
		return isBoundaryEnabled;
	}
	
	/**
	 * Checks if is visible.
	 *
	 * @return true, if is visible
	 */
	public boolean isVisible() {
		return isVisible;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IContentItemImplementation#setAngle(float)
	 */
	public void setAngle(float angle) {
		this.angle = angle;
		this.contentItemImplementation.setAngle(angle);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IContentItemImplementation#setBackGround(synergynetframework.appsystem.
	 * contentsystem.items.utils.Background)
	 */
	public void setBackGround(Background backGround) {
		this.backGround = backGround;
		this.contentItemImplementation.setBackGround(backGround);
	}
	
	/**
	 * Sets the background colour.
	 *
	 * @param color
	 *            the new background colour
	 */
	public void setBackgroundColour(Color color) {
		this.backGround.setBgColour(color);
		this.contentItemImplementation.setBackGround(this.backGround);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IContentItemImplementation#setBorder(synergynetframework.appsystem.
	 * contentsystem.items.utils.Border)
	 */
	public void setBorder(Border border) {
		this.border = border;
		this.contentItemImplementation.setBorder(border);
	}
	
	/**
	 * Sets the border colour.
	 *
	 * @param color
	 *            the new border colour
	 */
	public void setBorderColour(Color color) {
		this.border.setBorderColour(color);
		this.contentItemImplementation.setBorder(this.border);
	}
	
	/**
	 * Sets the border size.
	 *
	 * @param size
	 *            the new border size
	 */
	public void setBorderSize(int size) {
		this.border.setBorderSize(size);
		this.contentItemImplementation.setBorder(this.border);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IContentItemImplementation#setBoundaryEnabled(boolean)
	 */
	public void setBoundaryEnabled(boolean isBoundaryEnabled) {
		this.isBoundaryEnabled = isBoundaryEnabled;
		this.contentItemImplementation.setBoundaryEnabled(isBoundaryEnabled);
	}
	
	/**
	 * Sets the content system.
	 *
	 * @param contentSystem
	 *            the new content system
	 */
	public void setContentSystem(ContentSystem contentSystem) {
		this.contentSystem = contentSystem;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IContentItemImplementation#setId(java.lang.String)
	 */
	@Override
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Sets the local location.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public void setLocalLocation(float x, float y) {
		setLocalLocation(x, y, this.location.getZ());
	}
	
	/**
	 * Sets the local location.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param z
	 *            the z
	 */
	public void setLocalLocation(float x, float y, float z) {
		this.location.setX(x);
		this.location.setY(y);
		this.location.setZ(z);
		this.contentItemImplementation.setLocalLocation(this.location);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .
	 * IContentItemImplementation#setLocalLocation(synergynetframework.appsystem
	 * .contentsystem.items.utils.Location)
	 */
	public void setLocalLocation(Location location) {
		this.location = location;
		this.location.setX(location.x);
		this.location.setY(location.y);
		this.location.setZ(location.z);

		this.contentItemImplementation.setLocalLocation(location);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IContentItemImplementation#setLocation(synergynetframework.appsystem.
	 * contentsystem.items.utils.Location)
	 */
	public void setLocation(Location location) {
		this.setLocalLocation(location);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IContentItemImplementation#setManipulate(boolean)
	 */
	public void setManipulate(boolean manipulate) {
		this.manipulate = manipulate;
		this.contentItemImplementation.setManipulate(manipulate);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IContentItemImplementation#setName(java.lang.String)
	 */
	public void setName(String newName) {
		contentSystem.setItemName(this, newName);
		this.name = newName;
	}
	
	/**
	 * Sets the note.
	 *
	 * @param note
	 *            the new note
	 */
	public void setNote(String note) {
		this.note = note;
	}
	
	/**
	 * Sets the resource.
	 *
	 * @param resource
	 *            the new resource
	 */
	public void setResource(String resource) {
		this.resource = resource;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IContentItemImplementation#setScale(float)
	 */
	public void setScale(float scale) {
		scaleFactor = scale;
		this.contentItemImplementation.setScale(scale);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IContentItemImplementation#setScale(float,
	 * synergynetframework.appsystem.contentsystem.items.utils.Direction)
	 */
	@Override
	public void setScale(float scale, Direction direction) {
		scaleFactor = scale;
		this.contentItemImplementation.setScale(scale, direction);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IContentItemImplementation#setVisible(boolean)
	 */
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
		this.contentItemImplementation.setVisible(isVisible);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IContentItemImplementation#setVisible(boolean, boolean)
	 */
	public void setVisible(boolean isVisible, boolean isUntouchable) {
		this.isVisible = isVisible;
		this.contentItemImplementation.setVisible(isVisible, isUntouchable);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IContentItemImplementation#update()
	 */
	public void update() {
		this.contentItemImplementation.update();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IContentItemImplementation#update(float)
	 */
	public void update(float interpolation) {
		this.contentItemImplementation.update(interpolation);
	}
	
}
