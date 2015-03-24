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
public class ContentItem implements Serializable, IContentItemImplementation{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6128005678748844738L;

	/** The content system. */
	protected transient ContentSystem contentSystem;
	
	/** The content item implementation. */
	protected transient IContentItemImplementation contentItemImplementation;
	
	/** The implementation item factory. */
	protected IImplementationItemFactory implementationItemFactory;

	/** The name. */
	public String name;
	
	/** The id. */
	protected String id;
	
	/** The location. */
	protected Location location = new Location(0, 0, 0);
	
	/** The angle. */
	protected float angle = 0;
	
	/** The scale factor. */
	protected float scaleFactor = 1;
	
	/** The back ground. */
	protected Background backGround = new Background(Color.lightGray);
	
	/** The border. */
	protected Border border = new Border(Color.white, 4);
	
	/** The is visible. */
	protected boolean isVisible = true;
	
	/** The manipulate. */
	protected boolean manipulate = false;
	
	/** The is boundary enabled. */
	protected boolean isBoundaryEnabled = true;
	
	/** The note. */
	protected String note="";
	
	/** The resource. */
	protected String resource="";

	
	/**
	 * Gets the resource.
	 *
	 * @return the resource
	 */
	public String getResource(){
		return resource;
	}
	
	/**
	 * Sets the resource.
	 *
	 * @param resource the new resource
	 */
	public void setResource(String resource){
		this.resource = resource;
	}
	
	/**
	 * Instantiates a new content item.
	 *
	 * @param contentSystem the content system
	 * @param name the name
	 */
	public ContentItem(ContentSystem contentSystem, String name) {
		this.contentSystem = contentSystem;
		this.name = name;		
		this.implementationItemFactory = this.contentSystem.getImplementationItemFactory();
	}

	/**
	 * Bind implementation ojbect.
	 */
	public void bindImplementationOjbect(){
		this.contentItemImplementation = this.implementationItemFactory.create(this);
		this.contentItemImplementation.addMultitouchListener();
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IContentItemImplementation#init()
	 */
	public void init(){}

	/**
	 * Inits the implementation objet.
	 */
	public void initImplementationObjet(){
		this.contentItemImplementation.init();
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
	 * Sets the content system.
	 *
	 * @param contentSystem the new content system
	 */
	public void setContentSystem(ContentSystem contentSystem) {
		this.contentSystem = contentSystem;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IContentItemImplementation#setName(java.lang.String)
	 */
	public void setName(String newName){
		contentSystem.setItemName(this, newName);
		this.name = newName;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IContentItemImplementation#setId(java.lang.String)
	 */
	@Override
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId(){
		return id;
	}
	
	/**
	 * Gets the local location.
	 *
	 * @return the local location
	 */
	public Location getLocalLocation() {
		return location;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IContentItemImplementation#setLocalLocation(synergynetframework.appsystem.contentsystem.items.utils.Location)
	 */
	public void setLocalLocation(Location location) {
		this.location = location;
		this.location.setX(location.x);
		this.location.setY(location.y);
		this.location.setZ(location.z);
		
		this.contentItemImplementation.setLocalLocation(location);
	}

	/**
	 * Sets the local location.
	 *
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 */
	public void setLocalLocation(float x, float y, float z) {
		this.location.setX(x);
		this.location.setY(y);
		this.location.setZ(z);
		this.contentItemImplementation.setLocalLocation(this.location);
	}

	/**
	 * Sets the local location.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public void setLocalLocation(float x, float y) {
		setLocalLocation(x, y, this.location.getZ());
	}

	/**
	 * Gets the angle.
	 *
	 * @return the angle
	 */
	public float getAngle() {
		return angle;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IContentItemImplementation#setAngle(float)
	 */
	public void setAngle(float angle) {
		this.angle = angle;
		this.contentItemImplementation.setAngle(angle);
	}

	/**
	 * Gets the scale.
	 *
	 * @return the scale
	 */
	public float getScale() {
		return scaleFactor;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IContentItemImplementation#setScale(float)
	 */
	public void setScale(float scale) {
		scaleFactor = scale;
		this.contentItemImplementation.setScale(scale);
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IContentItemImplementation#setScale(float, synergynetframework.appsystem.contentsystem.items.utils.Direction)
	 */
	@Override
	public void setScale(float scale, Direction direction){
		scaleFactor = scale;
		this.contentItemImplementation.setScale(scale, direction);
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
	public Color getBackgroundColour(){
		if(backGround == null) return new Color(0, 0, 0, 0);
		return this.backGround.getColour();
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IContentItemImplementation#setBackGround(synergynetframework.appsystem.contentsystem.items.utils.Background)
	 */
	public void setBackGround(Background backGround) {
		this.backGround = backGround;
		this.contentItemImplementation.setBackGround(backGround);
	}

	/**
	 * Sets the background colour.
	 *
	 * @param color the new background colour
	 */
	public void setBackgroundColour(Color color){
		this.backGround.setBgColour(color);
		this.contentItemImplementation.setBackGround(this.backGround);
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
	 * Gets the border size.
	 *
	 * @return the border size
	 */
	public int getBorderSize(){
		return this.border.getBorderSize();
	}

	/**
	 * Gets the border colour.
	 *
	 * @return the border colour
	 */
	public Color getBorderColour(){
		return this.border.getBorderColour();
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IContentItemImplementation#setBorder(synergynetframework.appsystem.contentsystem.items.utils.Border)
	 */
	public void setBorder(Border border) {
		this.border = border;
		this.contentItemImplementation.setBorder(border);
	}

	/**
	 * Sets the border size.
	 *
	 * @param size the new border size
	 */
	public void setBorderSize(int size){
		this.border.setBorderSize(size);
		this.contentItemImplementation.setBorder(this.border);
	}

	/**
	 * Sets the border colour.
	 *
	 * @param color the new border colour
	 */
	public void setBorderColour(Color color){
		this.border.setBorderColour(color);
		this.contentItemImplementation.setBorder(this.border);
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IContentItemImplementation#setVisible(boolean)
	 */
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
		this.contentItemImplementation.setVisible(isVisible);
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IContentItemImplementation#setVisible(boolean, boolean)
	 */
	public void setVisible(boolean isVisible, boolean isUntouchable){
		this.isVisible = isVisible;
		this.contentItemImplementation.setVisible(isVisible, isUntouchable);
	}

	/**
	 * Checks if is visible.
	 *
	 * @return true, if is visible
	 */
	public boolean isVisible() {
		return isVisible;
	}

	/**
	 * Can manipulate.
	 *
	 * @return true, if successful
	 */
	public boolean canManipulate() {
		return manipulate;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IContentItemImplementation#setManipulate(boolean)
	 */
	public void setManipulate(boolean manipulate) {
		this.manipulate = manipulate;
		this.contentItemImplementation.setManipulate(manipulate);
	}

	/**
	 * Checks if is boundary enabled.
	 *
	 * @return true, if is boundary enabled
	 */
	public boolean isBoundaryEnabled() {
		return isBoundaryEnabled;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IContentItemImplementation#setBoundaryEnabled(boolean)
	 */
	public void setBoundaryEnabled(boolean isBoundaryEnabled) {
		this.isBoundaryEnabled = isBoundaryEnabled;
		this.contentItemImplementation.setBoundaryEnabled(isBoundaryEnabled);
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IContentItemImplementation#contains(java.awt.geom.Point2D.Float)
	 */
	public boolean contains(Point2D.Float point){
		return this.contentItemImplementation.contains(point);
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IContentItemImplementation#hasCollision(synergynetframework.appsystem.contentsystem.items.ContentItem)
	 */
	public boolean hasCollision(ContentItem otherItem){
		return this.contentItemImplementation.hasCollision(otherItem);
	}

	/**
	 * Generate unique name.
	 *
	 * @return the string
	 */
	public String generateUniqueName() {
		return UUID.randomUUID().toString();
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IContentItemImplementation#getImplementationObject()
	 */
	public Object getImplementationObject(){
		return this.contentItemImplementation.getImplementationObject();
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IContentItemImplementation#update()
	 */
	public void update(){
		this.contentItemImplementation.update();
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IContentItemImplementation#update(float)
	 */
	public void update(float interpolation) {
		this.contentItemImplementation.update(interpolation);
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
	 * Sets the note.
	 *
	 * @param note the new note
	 */
	public void setNote(String note) {
		this.note = note;
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IContentItemImplementation#addMultitouchListener()
	 */
	@Override
	public void addMultitouchListener() {

	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		ContentItem clonedItem = (ContentItem)super.clone();
		clonedItem.name = name;
		clonedItem.location = (Location)location.clone();
		clonedItem.angle = angle;
		clonedItem.scaleFactor = scaleFactor;
		clonedItem.backGround = (Background)backGround.clone();
		clonedItem.border = (Border)border.clone();
		clonedItem.isVisible = isVisible;
		clonedItem.manipulate = manipulate;
		//TODO: need support container in future
		clonedItem.isBoundaryEnabled = isBoundaryEnabled;
		clonedItem.note = note;
		clonedItem.contentSystem = contentSystem;
		clonedItem.contentItemImplementation = contentItemImplementation;
		clonedItem.implementationItemFactory = implementationItemFactory;
		clonedItem.resource = resource;
		return clonedItem;

	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IContentItemImplementation#setLocation(synergynetframework.appsystem.contentsystem.items.utils.Location)
	 */
	public void setLocation(Location location) {
		this.setLocalLocation(location);
	}

}
