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

public class ContentItem implements Serializable, IContentItemImplementation{
	private static final long serialVersionUID = -6128005678748844738L;

	protected transient ContentSystem contentSystem;
	protected transient IContentItemImplementation contentItemImplementation;
	protected IImplementationItemFactory implementationItemFactory;

	public String name;
	protected String id;
	protected Location location = new Location(0, 0, 0);
	protected float angle = 0;
	protected float scaleFactor = 1;
	protected Background backGround = new Background(Color.lightGray);
	protected Border border = new Border(Color.white, 4);
	protected boolean isVisible = true;
	protected boolean manipulate = false;
	protected boolean isBoundaryEnabled = true;
	protected String note="";
	protected String resource="";

	
	public String getResource(){
		return resource;
	}
	
	public void setResource(String resource){
		this.resource = resource;
	}
	
	public ContentItem(ContentSystem contentSystem, String name) {
		this.contentSystem = contentSystem;
		this.name = name;		
		this.implementationItemFactory = this.contentSystem.getImplementationItemFactory();
	}

	public void bindImplementationOjbect(){
		this.contentItemImplementation = this.implementationItemFactory.create(this);
		this.contentItemImplementation.addMultitouchListener();
	}

	public void init(){}

	public void initImplementationObjet(){
		this.contentItemImplementation.init();
	}

	public ContentSystem getContentSystem() {
		return contentSystem;
	}

	public void setContentSystem(ContentSystem contentSystem) {
		this.contentSystem = contentSystem;
	}

	public void setName(String newName){
		contentSystem.setItemName(this, newName);
		this.name = newName;
	}

	public String getName() {
		return name;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}
	
	public String getId(){
		return id;
	}
	public Location getLocalLocation() {
		return location;
	}

	public void setLocalLocation(Location location) {
		this.location = location;
		this.location.setX(location.x);
		this.location.setY(location.y);
		this.location.setZ(location.z);
		
		this.contentItemImplementation.setLocalLocation(location);
	}

	public void setLocalLocation(float x, float y, float z) {
		this.location.setX(x);
		this.location.setY(y);
		this.location.setZ(z);
		this.contentItemImplementation.setLocalLocation(this.location);
	}

	public void setLocalLocation(float x, float y) {
		setLocalLocation(x, y, this.location.getZ());
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
		this.contentItemImplementation.setAngle(angle);
	}

	public float getScale() {
		return scaleFactor;
	}

	public void setScale(float scale) {
		scaleFactor = scale;
		this.contentItemImplementation.setScale(scale);
	}

	@Override
	public void setScale(float scale, Direction direction){
		scaleFactor = scale;
		this.contentItemImplementation.setScale(scale, direction);
	}

	public Background getBackGround() {
		return backGround;
	}

	public Color getBackgroundColour(){
		if(backGround == null) return new Color(0, 0, 0, 0);
		return this.backGround.getColour();
	}

	public void setBackGround(Background backGround) {
		this.backGround = backGround;
		this.contentItemImplementation.setBackGround(backGround);
	}

	public void setBackgroundColour(Color color){
		this.backGround.setBgColour(color);
		this.contentItemImplementation.setBackGround(this.backGround);
	}

	public Border getBorder() {
		return border;
	}

	public int getBorderSize(){
		return this.border.getBorderSize();
	}

	public Color getBorderColour(){
		return this.border.getBorderColour();
	}

	public void setBorder(Border border) {
		this.border = border;
		this.contentItemImplementation.setBorder(border);
	}

	public void setBorderSize(int size){
		this.border.setBorderSize(size);
		this.contentItemImplementation.setBorder(this.border);
	}

	public void setBorderColour(Color color){
		this.border.setBorderColour(color);
		this.contentItemImplementation.setBorder(this.border);
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
		this.contentItemImplementation.setVisible(isVisible);
	}

	public void setVisible(boolean isVisible, boolean isUntouchable){
		this.isVisible = isVisible;
		this.contentItemImplementation.setVisible(isVisible, isUntouchable);
	}

	public boolean isVisible() {
		return isVisible;
	}

	public boolean canManipulate() {
		return manipulate;
	}

	public void setManipulate(boolean manipulate) {
		this.manipulate = manipulate;
		this.contentItemImplementation.setManipulate(manipulate);
	}

	public boolean isBoundaryEnabled() {
		return isBoundaryEnabled;
	}

	public void setBoundaryEnabled(boolean isBoundaryEnabled) {
		this.isBoundaryEnabled = isBoundaryEnabled;
		this.contentItemImplementation.setBoundaryEnabled(isBoundaryEnabled);
	}

	public boolean contains(Point2D.Float point){
		return this.contentItemImplementation.contains(point);
	}

	public boolean hasCollision(ContentItem otherItem){
		return this.contentItemImplementation.hasCollision(otherItem);
	}

	public String generateUniqueName() {
		return UUID.randomUUID().toString();
	}

	public Object getImplementationObject(){
		return this.contentItemImplementation.getImplementationObject();
	}

	public void update(){
		this.contentItemImplementation.update();
	}

	public void update(float interpolation) {
		this.contentItemImplementation.update(interpolation);
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	@Override
	public void addMultitouchListener() {

	}

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

	public void setLocation(Location location) {
		this.setLocalLocation(location);
	}

}
