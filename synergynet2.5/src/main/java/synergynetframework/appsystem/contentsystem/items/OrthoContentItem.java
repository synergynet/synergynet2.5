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

package synergynetframework.appsystem.contentsystem.items;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IOrthoContentItemImplementation;
import synergynetframework.appsystem.contentsystem.items.listener.BringToTopListener;
import synergynetframework.appsystem.contentsystem.items.listener.ItemListener;
import synergynetframework.appsystem.contentsystem.items.listener.OrthoControlPointRotateTranslateScaleListener;
import synergynetframework.appsystem.contentsystem.items.listener.OrthoFlickListener;
import synergynetframework.appsystem.contentsystem.items.listener.OrthoScaleMotionListener;
import synergynetframework.appsystem.contentsystem.items.listener.OrthoSnapListener;
import synergynetframework.appsystem.contentsystem.items.listener.OrthoSpinListener;
import synergynetframework.appsystem.contentsystem.items.listener.ScreenCursorListener;
import synergynetframework.appsystem.contentsystem.items.utils.Location;
import synergynetframework.jme.cursorsystem.fixutils.FixLocationStatus;

public class OrthoContentItem extends ContentItem implements IOrthoContentItemImplementation, Cloneable, Serializable, Comparable<OrthoContentItem> , ItemListener, OrthoControlPointRotateTranslateScaleListener, BringToTopListener, OrthoSnapListener, OrthoFlickListener, OrthoSpinListener, OrthoScaleMotionListener{

	private static final long serialVersionUID = 3271026022212734168L;

	protected transient List<ItemListener> itemListeners = new ArrayList<ItemListener>();
	protected transient List<ScreenCursorListener> screenCursorListeners = new ArrayList<ScreenCursorListener>();
	protected transient List<OrthoControlPointRotateTranslateScaleListener> orthoControlPointRotateTranslateScaleListeners = new ArrayList<OrthoControlPointRotateTranslateScaleListener>();
	protected transient List<BringToTopListener> bringToTopListeners = new ArrayList<BringToTopListener>();
	protected transient List<OrthoSnapListener> orthoSnapListeners = new ArrayList<OrthoSnapListener>();
	protected transient List<OrthoFlickListener> flickListeners = new ArrayList<OrthoFlickListener>();
	protected transient List<OrthoSpinListener> spinListeners = new ArrayList<OrthoSpinListener>();
	protected transient List<OrthoScaleMotionListener> scaleMotionListeners = new ArrayList<OrthoScaleMotionListener>();
	
	public boolean removable = true;

	protected OrthoContainer parent=null;
	protected int zOrder;
	protected boolean rotateTranslateScalable = true;
	protected boolean bringToTopable = true;
	protected boolean snapable = true;
	
	protected float flickDeceleration, spinDeceleration, scaleMotionDeceleration;
	private boolean isSingleTouchRotateTranslate;

	public OrthoContentItem(ContentSystem contentSystem, String name) {
		super(contentSystem, name);
	}

	public void bindImplementationOjbect(){
		super.bindImplementationOjbect();
		this.enableMultiTouchElementListeners();
	}

	@Override
	public void reset() {
		parent = null;
		itemListeners = null;
		screenCursorListeners = null;
		orthoControlPointRotateTranslateScaleListeners = null;
		bringToTopListeners = null;
		orthoSnapListeners = null;
		flickListeners = null;
		rotateTranslateScalable = true;
		bringToTopable = true;
		snapable = true;
	}
	
	public void setName(String name){
		super.setName(name);
		((IOrthoContentItemImplementation)this.contentItemImplementation).setName(name);
	}

	public void enableMultiTouchElementListeners(){
		if(itemListeners ==null) itemListeners = new ArrayList<ItemListener>();
		if(screenCursorListeners == null) screenCursorListeners = new ArrayList<ScreenCursorListener>();
		if(orthoControlPointRotateTranslateScaleListeners == null) orthoControlPointRotateTranslateScaleListeners = new ArrayList<OrthoControlPointRotateTranslateScaleListener>();
		if(bringToTopListeners == null) bringToTopListeners = new ArrayList<BringToTopListener>();
		if(orthoSnapListeners == null) orthoSnapListeners = new ArrayList<OrthoSnapListener>();
		if(flickListeners == null) flickListeners = new ArrayList<OrthoFlickListener>();

		((IOrthoContentItemImplementation)this.contentItemImplementation).addBringToTopListener(this);
		((IOrthoContentItemImplementation)this.contentItemImplementation).addItemListener(this);
		((IOrthoContentItemImplementation)this.contentItemImplementation).addOrthoControlPointRotateTranslateScaleListener(this);
		((IOrthoContentItemImplementation)this.contentItemImplementation).addSnapListener(this);
		((IOrthoContentItemImplementation)this.contentItemImplementation).addFlickListener(this);
	}

	public void setOrder(int zOrder) {
		this.zOrder = zOrder;
		((IOrthoContentItemImplementation)this.contentItemImplementation).setOrder(zOrder);
	}

	public int getOrder() {
		return zOrder;
	}

	public void setRotateTranslateScalable(boolean isEnabled){
		this.rotateTranslateScalable = isEnabled;
		((IOrthoContentItemImplementation)this.contentItemImplementation).setRotateTranslateScalable(isEnabled);
	}

	public boolean isRotateTranslateScaleEnabled(){
		return this.rotateTranslateScalable;
	}

	public void allowMoreThanTwoToRotateAndScale(boolean b){
		((IOrthoContentItemImplementation)this.contentItemImplementation).allowMoreThanTwoToRotateAndScale(b);
	}

	public void allowSingleTouchFreeMove(boolean b){
		((IOrthoContentItemImplementation)this.contentItemImplementation).allowSingleTouchFreeMove(b);
	}

	public void setScaleLimit(float min, float max){
		((IOrthoContentItemImplementation)this.contentItemImplementation).setScaleLimit(min, max);
	}
	
	public float getMinScale(){
		return ((IOrthoContentItemImplementation)this.contentItemImplementation).getMinScale();
	}

	public float getMaxScale(){
		return ((IOrthoContentItemImplementation)this.contentItemImplementation).getMaxScale();
	}
	
	public void setZRotateLimit(float min, float max) {
		((IOrthoContentItemImplementation)this.contentItemImplementation).setZRotateLimit(min, max);
	}

	public void setBringToTopable(boolean isEnabled){
		this.bringToTopable = isEnabled;
		((IOrthoContentItemImplementation)this.contentItemImplementation).setBringToTopable(isEnabled);
	}

	public void setAsTopObject(){
		if (this.isBringToTopEnabled())
			((IOrthoContentItemImplementation)this.contentItemImplementation).setAsTopObject();
	}

	public void setAsBottomObject(){
		if (this.isBringToTopEnabled())
			((IOrthoContentItemImplementation)this.contentItemImplementation).setAsBottomObject();
	}

	public void setAsTopObjectAndBroadCastEvent(){
		if (this.isBringToTopEnabled())
			((IOrthoContentItemImplementation)this.contentItemImplementation).setAsTopObjectAndBroadCastEvent();
	}

	public boolean isBringToTopEnabled(){
		return this.bringToTopable;
	}

	public void setSingleTouchRotateTranslate(boolean isEnabled) throws Exception {
		this.isSingleTouchRotateTranslate = isEnabled;
		((IOrthoContentItemImplementation)this.contentItemImplementation).setSingleTouchRotateTranslate(isEnabled);
	}

	public boolean isSingleTouchRotateTranslate() {
		return isSingleTouchRotateTranslate;
	}

	public void turnOffEventDispatcher(){
		((IOrthoContentItemImplementation)this.contentItemImplementation).turnOffEventDispatcher();
	}

	public boolean isSnapEnabled() {
		return snapable;
	}

	public void setSnapable(boolean snapable) {
		this.snapable = snapable;
		((IOrthoContentItemImplementation)this.contentItemImplementation).setSnapable(snapable);
	}

	public void setFixLocations(List<FixLocationStatus> fixLocations){
		((IOrthoContentItemImplementation)this.contentItemImplementation).setFixLocations(fixLocations);
	}

	public void setTolerance(float tolerance) {
		((IOrthoContentItemImplementation)this.contentItemImplementation).setTolerance(tolerance);
	}

	public void setRightClickDistance(float distance){
		((IOrthoContentItemImplementation)this.contentItemImplementation).setRightClickDistance(distance);
	}

	public void allowSnapToOccupiedLocation(boolean allowSnapToOccupiedLocation) {
		((IOrthoContentItemImplementation)this.contentItemImplementation).allowSnapToOccupiedLocation(allowSnapToOccupiedLocation);
	}

	public void centerItem() {
		((IOrthoContentItemImplementation)this.contentItemImplementation).centerItem();
	}

	public void placeRandom() {
		((IOrthoContentItemImplementation)this.contentItemImplementation).placeRandom();
	}

	public void rotateRandom() {
		((IOrthoContentItemImplementation)this.contentItemImplementation).rotateRandom();
	}

	//item listener
	public void addItemListener(ItemListener itemListener) {
		if(itemListeners == null) itemListeners = new ArrayList<ItemListener>();
		if(!itemListeners.contains(itemListener)) itemListeners.add(itemListener);
	}

	public void removeItemListerner(ItemListener itemListener){
		itemListeners.remove(itemListener);
	}

		public void removeItemListerners(){
		itemListeners.clear();
	}

	public void fireCursorPressed(long id, float x, float y, float pressure) {
		for(ItemListener l : itemListeners) {
			l.cursorPressed(this, id, x, y, pressure);
		}
	}

	public void fireCursorChanged(long id, float x, float y, float pressure) {
		for(ItemListener l : itemListeners) {
			l.cursorChanged(this, id, x, y, pressure);
		}
	}

	public void fireCursorReleased(long id, float x, float y, float pressure) {
		for(ItemListener l : itemListeners) {
			l.cursorReleased(this, id, x, y, pressure);
		}
	}

	public void fireCursorClicked(long id, float x, float y, float pressure) {
		for(ItemListener l : itemListeners) {
			l.cursorClicked(this, id, x, y, pressure);
		}
	}

	public void fireCursorDoubleClicked(long id, float x, float y, float pressure) {
		for(ItemListener l : itemListeners) {
			l.cursorDoubleClicked(this, id, x, y, pressure);
		}
	}

	public void fireCursorRightClicked(long id, float x, float y, float pressure) {
		for(ItemListener l : itemListeners) {
			l.cursorRightClicked(this, id, x, y, pressure);
		}
	}


	public void fireCursorLongHeld(long id, float x, float y, float pressure) {
		for(ItemListener l : itemListeners) {
			l.cursorLongHeld(this, id, x, y, pressure);
		}
	}

	@Override
	public void cursorChanged(ContentItem item, long id, float x, float y,
			float pressure) {
		this.fireCursorChanged(id, x, y, pressure);
	}

	@Override
	public void cursorClicked(ContentItem item, long id, float x, float y,
			float pressure) {
		this.fireCursorClicked(id, x, y, pressure);
	}

	@Override
	public void cursorPressed(ContentItem item, long id, float x, float y,
			float pressure) {
		this.fireCursorPressed(id, x, y, pressure);
	}

	@Override
	public void cursorReleased(ContentItem item, long id, float x, float y,
			float pressure) {
		this.fireCursorReleased(id, x, y, pressure);
	}


	@Override
	public void cursorRightClicked(ContentItem item, long id, float x, float y,
			float pressure) {
		this.fireCursorRightClicked(id, x, y, pressure);
	}

	@Override
	public void cursorLongHeld(ContentItem item, long id, float x, float y,
			float pressure) {
		this.fireCursorLongHeld(id, x, y, pressure);
	}

	@Override
	public void cursorDoubleClicked(ContentItem item, long id, float x,
			float y, float pressure) {
		this.fireCursorDoubleClicked(id, x, y, pressure);
	}

	//orthoControlPointRotateTranslateScale listener
	public void addOrthoControlPointRotateTranslateScaleListener(OrthoControlPointRotateTranslateScaleListener l) {
		if(orthoControlPointRotateTranslateScaleListeners == null) orthoControlPointRotateTranslateScaleListeners = new ArrayList<OrthoControlPointRotateTranslateScaleListener>();
		if(!orthoControlPointRotateTranslateScaleListeners.contains(l))
			orthoControlPointRotateTranslateScaleListeners.add(l);
	}

	public void removeOrthoControlPointRotateTranslateScaleListeners(){
		orthoControlPointRotateTranslateScaleListeners.clear();
	}

	public void removeOrthoControlPointRotateTranslateScaleListeners(OrthoControlPointRotateTranslateScaleListener l){
		orthoControlPointRotateTranslateScaleListeners.remove(l);
	}

	@Override
	public void itemRotated(ContentItem item, float newAngle, float oldAngle) {
		for (OrthoControlPointRotateTranslateScaleListener l: this.orthoControlPointRotateTranslateScaleListeners)
			l.itemRotated(this, newAngle, oldAngle);
	}

	@Override
	public void itemScaled(ContentItem item, float newScaleFactor,
			float oldScaleFactor) {
		for (OrthoControlPointRotateTranslateScaleListener l: this.orthoControlPointRotateTranslateScaleListeners)
			l.itemScaled(this, newScaleFactor, oldScaleFactor);
	}

	@Override
	public void itemTranslated(ContentItem item, float newLocationX,
			float newLocationY, float oldLocationX, float oldLocationY) {
		for (OrthoControlPointRotateTranslateScaleListener l: this.orthoControlPointRotateTranslateScaleListeners)
			l.itemTranslated(this, newLocationX, newLocationY, oldLocationX, oldLocationY);
	}


	//BringToTop listener
	public void addBringToTopListener(BringToTopListener l) {
		if(bringToTopListeners ==null) bringToTopListeners = new ArrayList<BringToTopListener>();
		if(!this.bringToTopListeners.contains(l))
			bringToTopListeners.add(l);
	}

	public void removeBringToTopListeners(){
		bringToTopListeners.clear();
	}

	public void removeBringToTopListeners(BringToTopListener l){
		bringToTopListeners.remove(l);
	}

	@Override
	public void itemBringToToped(ContentItem item) {
		for (BringToTopListener l: this.bringToTopListeners)
			l.itemBringToToped(item);
	}

	//Snap listener
	public void addSnapListener(OrthoSnapListener l) {
		if(orthoSnapListeners ==null) orthoSnapListeners = new ArrayList<OrthoSnapListener>();
		if(!this.orthoSnapListeners.contains(l))
			orthoSnapListeners.add(l);
	}

	public void removeSnapListeners(){
		orthoSnapListeners.clear();
	}

	public void removeSnapListeners(OrthoSnapListener l){
		orthoSnapListeners.remove(l);
	}

	@Override
	public void itemSnapped(ContentItem item, FixLocationStatus fixLocationStatus) {

		for (OrthoSnapListener l: this.orthoSnapListeners)
			l.itemSnapped(item, fixLocationStatus);

	}

	// Flick Listener

	public void addFlickListener(OrthoFlickListener l){
		if(flickListeners ==null) flickListeners = new ArrayList<OrthoFlickListener>();
		if(!this.flickListeners.contains(l))
			flickListeners.add(l);
	}

	public void removeFlickListeners(){
		flickListeners.clear();
	}

	public void removeFlickListeners(OrthoFlickListener l){
		flickListeners.remove(l);
	}

	// screen cursor Listener

	public void addScreenCursorListener(ScreenCursorListener l){
		if(screenCursorListeners ==null) screenCursorListeners = new ArrayList<ScreenCursorListener>();
		((IOrthoContentItemImplementation)this.contentItemImplementation).addScreenCursorListener(l);
		if(!screenCursorListeners.contains(l)) screenCursorListeners.add(l);
	}

	public void removeScreenCursorListeners(){
		((IOrthoContentItemImplementation)this.contentItemImplementation).removeScreenCursorListeners();
		screenCursorListeners.clear();
	}

	public void fireScreenCursorPressed(long id, float x, float y, float pressure) {
		for(ScreenCursorListener l : screenCursorListeners) {
			l.screenCursorPressed(this, id, x, y, pressure);
		}
	}

	public void fireScreenCursorChanged(long id, float x, float y, float pressure) {
		for(ScreenCursorListener l : screenCursorListeners) {
			l.screenCursorChanged(this, id, x, y, pressure);
		}
	}

	public void fireScreenCursorReleased(long id, float x, float y, float pressure) {
		for(ScreenCursorListener l : screenCursorListeners) {
			l.screenCursorReleased(this, id, x, y, pressure);
		}
	}

	public void fireScreenCursorClicked(long id, float x, float y, float pressure) {
		for(ScreenCursorListener l : screenCursorListeners) {
			l.screenCursorClicked(this, id, x, y, pressure);
		}
	}
	@Override
	public void itemFlicked(ContentItem item, float x, float y){
		for (OrthoFlickListener l: flickListeners)
			l.itemFlicked(this, x, y);
	}

	@Override
	public int compareTo(OrthoContentItem arg0) {
		if (this.zOrder == ((OrthoContentItem) arg0).zOrder)
            return 0;
        else if ((this.zOrder) > ((OrthoContentItem) arg0).zOrder)
            return 1;
        else
            return -1;

	}

	public void makeFlickable(float deceleration){
		this.flickDeceleration = deceleration;
		((IOrthoContentItemImplementation)this.contentItemImplementation).makeFlickable(deceleration);
	}

	public void makeUnflickable(){
		((IOrthoContentItemImplementation)this.contentItemImplementation).makeUnflickable();
	}

	public boolean isFlickable() {
		return ((IOrthoContentItemImplementation)this.contentItemImplementation).isFlickable();
	}

	public float getFlickDeceleration(){
		return flickDeceleration;
	}
	
	public void flick(float velocityX, float velocityY, float deceleration) {
		((IOrthoContentItemImplementation)this.contentItemImplementation).flick(velocityX, velocityY, deceleration);
	}

	public float getScaleMotionDeceleration(){
		return scaleMotionDeceleration;
	}
	
	@Override
	public void itemSpun(ContentItem item, float rot) {
		for (OrthoSpinListener l: spinListeners)
			l.itemSpun(this, rot);
	}

	@Override
	public void itemScaleMotioned(ContentItem item, float scale) {
		for (OrthoScaleMotionListener l: scaleMotionListeners)
			l.itemScaleMotioned(this, scale);
	}

	public List<ItemListener> getItemListeners() {
		return itemListeners;
	}

	public void setItemListeners(List<ItemListener> itemListeners) {
		this.itemListeners = itemListeners;
	}

	public List<OrthoControlPointRotateTranslateScaleListener> getOrthoControlPointRotateTranslateScaleListeners() {
		return orthoControlPointRotateTranslateScaleListeners;
	}

	public void setOrthoControlPointRotateTranslateScaleListeners(
			List<OrthoControlPointRotateTranslateScaleListener> orthoControlPointRotateTranslateScaleListeners) {
		this.orthoControlPointRotateTranslateScaleListeners = orthoControlPointRotateTranslateScaleListeners;
	}

	public List<BringToTopListener> getBringToTopListeners() {
		return bringToTopListeners;
	}

	public void setBringToTopListeners(List<BringToTopListener> bringToTopListeners) {
		this.bringToTopListeners = bringToTopListeners;
	}

	public List<OrthoSnapListener> getOrthoSnapListeners() {
		return orthoSnapListeners;
	}

	public void setOrthoSnapListeners(List<OrthoSnapListener> orthoSnapListeners) {
		this.orthoSnapListeners = orthoSnapListeners;
	}

	public List<OrthoFlickListener> getFlickListeners() {
		return flickListeners;
	}

	public void setFlickListeners(List<OrthoFlickListener> flickListeners) {
		this.flickListeners = flickListeners;
	}

	public List<OrthoSpinListener> getSpinListeners() {
		return spinListeners;
	}

	public void setSpinListeners(List<OrthoSpinListener> spinListeners) {
		this.spinListeners = spinListeners;
	}

	public List<OrthoScaleMotionListener> getScaleMotionListeners() {
		return scaleMotionListeners;
	}

	public void setScaleMotionListeners(List<OrthoScaleMotionListener> scaleMotionListeners) {
		this.scaleMotionListeners = scaleMotionListeners;
	}

	public OrthoContainer getParent(){
		return parent;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		OrthoContentItem clonedItem = (OrthoContentItem)super.clone();
		clonedItem.zOrder = zOrder;
		clonedItem.rotateTranslateScalable = rotateTranslateScalable;
		clonedItem.bringToTopable = bringToTopable;
		clonedItem.snapable = snapable;
		clonedItem.parent = null;
		return clonedItem;

	}

	public void setLocation(float x, float y){
		this.setLocation(new Location(x,y,0));
	}

	@Override
	public void setLocation(Location location){
		if(this.getParent() != null && this.getParent() instanceof Window){
			this.setLocalLocation(location.x- ((Window)this.getParent()).getWidth()/2, location.y- ((Window)this.getParent()).getHeight()/2);
		}else{
			this.setLocalLocation(location);
		}
	}

	public Location getLocation(){
		if(this.getParent() != null && this.getParent() instanceof Window){
			return new Location(this.getLocalLocation().x+ ((Window)this.getParent()).getWidth()/2, this.getLocalLocation().y+ ((Window)this.getParent()).getHeight()/2,0);
		}
		else
			return this.getLocalLocation();
	}

	@Override
	public void setRotateTranslateScalable(boolean isEnabled, boolean attachToParent, ContentItem targetItem) {
		this.rotateTranslateScalable = isEnabled;
		((IOrthoContentItemImplementation)this.contentItemImplementation).setRotateTranslateScalable(isEnabled, attachToParent, targetItem);
	}
	
}