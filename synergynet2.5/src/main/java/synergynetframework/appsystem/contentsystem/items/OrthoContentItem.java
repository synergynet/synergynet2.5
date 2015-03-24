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

/**
 * The Class OrthoContentItem.
 */
public class OrthoContentItem extends ContentItem implements
		IOrthoContentItemImplementation, Cloneable, Serializable,
		Comparable<OrthoContentItem>, ItemListener,
		OrthoControlPointRotateTranslateScaleListener, BringToTopListener,
		OrthoSnapListener, OrthoFlickListener, OrthoSpinListener,
		OrthoScaleMotionListener {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3271026022212734168L;
	
	/** The bring to topable. */
	protected boolean bringToTopable = true;

	/** The bring to top listeners. */
	protected transient List<BringToTopListener> bringToTopListeners = new ArrayList<BringToTopListener>();

	/** The scale motion deceleration. */
	protected float flickDeceleration, spinDeceleration,
			scaleMotionDeceleration;

	/** The flick listeners. */
	protected transient List<OrthoFlickListener> flickListeners = new ArrayList<OrthoFlickListener>();

	/** The is single touch rotate translate. */
	private boolean isSingleTouchRotateTranslate;

	/** The item listeners. */
	protected transient List<ItemListener> itemListeners = new ArrayList<ItemListener>();

	/** The ortho control point rotate translate scale listeners. */
	protected transient List<OrthoControlPointRotateTranslateScaleListener> orthoControlPointRotateTranslateScaleListeners = new ArrayList<OrthoControlPointRotateTranslateScaleListener>();

	/** The ortho snap listeners. */
	protected transient List<OrthoSnapListener> orthoSnapListeners = new ArrayList<OrthoSnapListener>();

	/** The parent. */
	protected OrthoContainer parent = null;
	
	/** The removable. */
	public boolean removable = true;

	/** The rotate translate scalable. */
	protected boolean rotateTranslateScalable = true;

	/** The scale motion listeners. */
	protected transient List<OrthoScaleMotionListener> scaleMotionListeners = new ArrayList<OrthoScaleMotionListener>();

	/** The screen cursor listeners. */
	protected transient List<ScreenCursorListener> screenCursorListeners = new ArrayList<ScreenCursorListener>();

	/** The snapable. */
	protected boolean snapable = true;

	/** The spin listeners. */
	protected transient List<OrthoSpinListener> spinListeners = new ArrayList<OrthoSpinListener>();

	/** The z order. */
	protected int zOrder;
	
	/**
	 * Instantiates a new ortho content item.
	 *
	 * @param contentSystem
	 *            the content system
	 * @param name
	 *            the name
	 */
	public OrthoContentItem(ContentSystem contentSystem, String name) {
		super(contentSystem, name);
	}
	
	// BringToTop listener
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .
	 * IOrthoContentItemImplementation#addBringToTopListener(synergynetframework
	 * .appsystem.contentsystem.items.listener.BringToTopListener)
	 */
	public void addBringToTopListener(BringToTopListener l) {
		if (bringToTopListeners == null) {
			bringToTopListeners = new ArrayList<BringToTopListener>();
		}
		if (!this.bringToTopListeners.contains(l)) {
			bringToTopListeners.add(l);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IOrthoContentItemImplementation#addFlickListener(synergynetframework.
	 * appsystem.contentsystem.items.listener.OrthoFlickListener)
	 */
	public void addFlickListener(OrthoFlickListener l) {
		if (flickListeners == null) {
			flickListeners = new ArrayList<OrthoFlickListener>();
		}
		if (!this.flickListeners.contains(l)) {
			flickListeners.add(l);
		}
	}

	// item listener
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .
	 * IOrthoContentItemImplementation#addItemListener(synergynetframework.appsystem
	 * .contentsystem.items.listener.ItemListener)
	 */
	public void addItemListener(ItemListener itemListener) {
		if (itemListeners == null) {
			itemListeners = new ArrayList<ItemListener>();
		}
		if (!itemListeners.contains(itemListener)) {
			itemListeners.add(itemListener);
		}
	}
	
	// orthoControlPointRotateTranslateScale listener
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IOrthoContentItemImplementation#
	 * addOrthoControlPointRotateTranslateScaleListener
	 * (synergynetframework.appsystem
	 * .contentsystem.items.listener.OrthoControlPointRotateTranslateScaleListener
	 * )
	 */
	public void addOrthoControlPointRotateTranslateScaleListener(
			OrthoControlPointRotateTranslateScaleListener l) {
		if (orthoControlPointRotateTranslateScaleListeners == null) {
			orthoControlPointRotateTranslateScaleListeners = new ArrayList<OrthoControlPointRotateTranslateScaleListener>();
		}
		if (!orthoControlPointRotateTranslateScaleListeners.contains(l)) {
			orthoControlPointRotateTranslateScaleListeners.add(l);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .
	 * IOrthoContentItemImplementation#addScreenCursorListener(synergynetframework
	 * .appsystem.contentsystem.items.listener.ScreenCursorListener)
	 */
	public void addScreenCursorListener(ScreenCursorListener l) {
		if (screenCursorListeners == null) {
			screenCursorListeners = new ArrayList<ScreenCursorListener>();
		}
		((IOrthoContentItemImplementation) this.contentItemImplementation)
				.addScreenCursorListener(l);
		if (!screenCursorListeners.contains(l)) {
			screenCursorListeners.add(l);
		}
	}
	
	// Snap listener
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .
	 * IOrthoContentItemImplementation#addSnapListener(synergynetframework.appsystem
	 * .contentsystem.items.listener.OrthoSnapListener)
	 */
	public void addSnapListener(OrthoSnapListener l) {
		if (orthoSnapListeners == null) {
			orthoSnapListeners = new ArrayList<OrthoSnapListener>();
		}
		if (!this.orthoSnapListeners.contains(l)) {
			orthoSnapListeners.add(l);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .
	 * IOrthoContentItemImplementation#allowMoreThanTwoToRotateAndScale(boolean)
	 */
	public void allowMoreThanTwoToRotateAndScale(boolean b) {
		((IOrthoContentItemImplementation) this.contentItemImplementation)
				.allowMoreThanTwoToRotateAndScale(b);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IOrthoContentItemImplementation#allowSingleTouchFreeMove(boolean)
	 */
	public void allowSingleTouchFreeMove(boolean b) {
		((IOrthoContentItemImplementation) this.contentItemImplementation)
				.allowSingleTouchFreeMove(b);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IOrthoContentItemImplementation#allowSnapToOccupiedLocation(boolean)
	 */
	public void allowSnapToOccupiedLocation(boolean allowSnapToOccupiedLocation) {
		((IOrthoContentItemImplementation) this.contentItemImplementation)
				.allowSnapToOccupiedLocation(allowSnapToOccupiedLocation);
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.ContentItem#
	 * bindImplementationOjbect()
	 */
	public void bindImplementationOjbect() {
		super.bindImplementationOjbect();
		this.enableMultiTouchElementListeners();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IOrthoContentItemImplementation#centerItem()
	 */
	public void centerItem() {
		((IOrthoContentItemImplementation) this.contentItemImplementation)
				.centerItem();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.ContentItem#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		OrthoContentItem clonedItem = (OrthoContentItem) super.clone();
		clonedItem.zOrder = zOrder;
		clonedItem.rotateTranslateScalable = rotateTranslateScalable;
		clonedItem.bringToTopable = bringToTopable;
		clonedItem.snapable = snapable;
		clonedItem.parent = null;
		return clonedItem;
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(OrthoContentItem arg0) {
		if (this.zOrder == arg0.zOrder) {
			return 0;
		} else if ((this.zOrder) > arg0.zOrder) {
			return 1;
		} else {
			return -1;
		}
		
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.listener.ItemListener
	 * #cursorChanged
	 * (synergynetframework.appsystem.contentsystem.items.ContentItem, long,
	 * float, float, float)
	 */
	@Override
	public void cursorChanged(ContentItem item, long id, float x, float y,
			float pressure) {
		this.fireCursorChanged(id, x, y, pressure);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.listener.ItemListener
	 * #cursorClicked
	 * (synergynetframework.appsystem.contentsystem.items.ContentItem, long,
	 * float, float, float)
	 */
	@Override
	public void cursorClicked(ContentItem item, long id, float x, float y,
			float pressure) {
		this.fireCursorClicked(id, x, y, pressure);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.listener.ItemListener
	 * #cursorDoubleClicked
	 * (synergynetframework.appsystem.contentsystem.items.ContentItem, long,
	 * float, float, float)
	 */
	@Override
	public void cursorDoubleClicked(ContentItem item, long id, float x,
			float y, float pressure) {
		this.fireCursorDoubleClicked(id, x, y, pressure);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.listener.ItemListener
	 * #cursorLongHeld
	 * (synergynetframework.appsystem.contentsystem.items.ContentItem, long,
	 * float, float, float)
	 */
	@Override
	public void cursorLongHeld(ContentItem item, long id, float x, float y,
			float pressure) {
		this.fireCursorLongHeld(id, x, y, pressure);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.listener.ItemListener
	 * #cursorPressed
	 * (synergynetframework.appsystem.contentsystem.items.ContentItem, long,
	 * float, float, float)
	 */
	@Override
	public void cursorPressed(ContentItem item, long id, float x, float y,
			float pressure) {
		this.fireCursorPressed(id, x, y, pressure);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.listener.ItemListener
	 * #cursorReleased
	 * (synergynetframework.appsystem.contentsystem.items.ContentItem, long,
	 * float, float, float)
	 */
	@Override
	public void cursorReleased(ContentItem item, long id, float x, float y,
			float pressure) {
		this.fireCursorReleased(id, x, y, pressure);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.listener.ItemListener
	 * #cursorRightClicked
	 * (synergynetframework.appsystem.contentsystem.items.ContentItem, long,
	 * float, float, float)
	 */
	@Override
	public void cursorRightClicked(ContentItem item, long id, float x, float y,
			float pressure) {
		this.fireCursorRightClicked(id, x, y, pressure);
	}
	
	/**
	 * Enable multi touch element listeners.
	 */
	public void enableMultiTouchElementListeners() {
		if (itemListeners == null) {
			itemListeners = new ArrayList<ItemListener>();
		}
		if (screenCursorListeners == null) {
			screenCursorListeners = new ArrayList<ScreenCursorListener>();
		}
		if (orthoControlPointRotateTranslateScaleListeners == null) {
			orthoControlPointRotateTranslateScaleListeners = new ArrayList<OrthoControlPointRotateTranslateScaleListener>();
		}
		if (bringToTopListeners == null) {
			bringToTopListeners = new ArrayList<BringToTopListener>();
		}
		if (orthoSnapListeners == null) {
			orthoSnapListeners = new ArrayList<OrthoSnapListener>();
		}
		if (flickListeners == null) {
			flickListeners = new ArrayList<OrthoFlickListener>();
		}
		
		((IOrthoContentItemImplementation) this.contentItemImplementation)
				.addBringToTopListener(this);
		((IOrthoContentItemImplementation) this.contentItemImplementation)
				.addItemListener(this);
		((IOrthoContentItemImplementation) this.contentItemImplementation)
				.addOrthoControlPointRotateTranslateScaleListener(this);
		((IOrthoContentItemImplementation) this.contentItemImplementation)
				.addSnapListener(this);
		((IOrthoContentItemImplementation) this.contentItemImplementation)
				.addFlickListener(this);
	}
	
	/**
	 * Fire cursor changed.
	 *
	 * @param id
	 *            the id
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param pressure
	 *            the pressure
	 */
	public void fireCursorChanged(long id, float x, float y, float pressure) {
		for (ItemListener l : itemListeners) {
			l.cursorChanged(this, id, x, y, pressure);
		}
	}
	
	/**
	 * Fire cursor clicked.
	 *
	 * @param id
	 *            the id
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param pressure
	 *            the pressure
	 */
	public void fireCursorClicked(long id, float x, float y, float pressure) {
		for (ItemListener l : itemListeners) {
			l.cursorClicked(this, id, x, y, pressure);
		}
	}
	
	/**
	 * Fire cursor double clicked.
	 *
	 * @param id
	 *            the id
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param pressure
	 *            the pressure
	 */
	public void fireCursorDoubleClicked(long id, float x, float y,
			float pressure) {
		for (ItemListener l : itemListeners) {
			l.cursorDoubleClicked(this, id, x, y, pressure);
		}
	}
	
	/**
	 * Fire cursor long held.
	 *
	 * @param id
	 *            the id
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param pressure
	 *            the pressure
	 */
	public void fireCursorLongHeld(long id, float x, float y, float pressure) {
		for (ItemListener l : itemListeners) {
			l.cursorLongHeld(this, id, x, y, pressure);
		}
	}
	
	/**
	 * Fire cursor pressed.
	 *
	 * @param id
	 *            the id
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param pressure
	 *            the pressure
	 */
	public void fireCursorPressed(long id, float x, float y, float pressure) {
		for (ItemListener l : itemListeners) {
			l.cursorPressed(this, id, x, y, pressure);
		}
	}
	
	/**
	 * Fire cursor released.
	 *
	 * @param id
	 *            the id
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param pressure
	 *            the pressure
	 */
	public void fireCursorReleased(long id, float x, float y, float pressure) {
		for (ItemListener l : itemListeners) {
			l.cursorReleased(this, id, x, y, pressure);
		}
	}
	
	/**
	 * Fire cursor right clicked.
	 *
	 * @param id
	 *            the id
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param pressure
	 *            the pressure
	 */
	public void fireCursorRightClicked(long id, float x, float y, float pressure) {
		for (ItemListener l : itemListeners) {
			l.cursorRightClicked(this, id, x, y, pressure);
		}
	}
	
	/**
	 * Fire screen cursor changed.
	 *
	 * @param id
	 *            the id
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param pressure
	 *            the pressure
	 */
	public void fireScreenCursorChanged(long id, float x, float y,
			float pressure) {
		for (ScreenCursorListener l : screenCursorListeners) {
			l.screenCursorChanged(this, id, x, y, pressure);
		}
	}
	
	/**
	 * Fire screen cursor clicked.
	 *
	 * @param id
	 *            the id
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param pressure
	 *            the pressure
	 */
	public void fireScreenCursorClicked(long id, float x, float y,
			float pressure) {
		for (ScreenCursorListener l : screenCursorListeners) {
			l.screenCursorClicked(this, id, x, y, pressure);
		}
	}
	
	/**
	 * Fire screen cursor pressed.
	 *
	 * @param id
	 *            the id
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param pressure
	 *            the pressure
	 */
	public void fireScreenCursorPressed(long id, float x, float y,
			float pressure) {
		for (ScreenCursorListener l : screenCursorListeners) {
			l.screenCursorPressed(this, id, x, y, pressure);
		}
	}
	
	/**
	 * Fire screen cursor released.
	 *
	 * @param id
	 *            the id
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param pressure
	 *            the pressure
	 */
	public void fireScreenCursorReleased(long id, float x, float y,
			float pressure) {
		for (ScreenCursorListener l : screenCursorListeners) {
			l.screenCursorReleased(this, id, x, y, pressure);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IOrthoContentItemImplementation#flick(float, float, float)
	 */
	public void flick(float velocityX, float velocityY, float deceleration) {
		((IOrthoContentItemImplementation) this.contentItemImplementation)
				.flick(velocityX, velocityY, deceleration);
	}
	
	/**
	 * Gets the bring to top listeners.
	 *
	 * @return the bring to top listeners
	 */
	public List<BringToTopListener> getBringToTopListeners() {
		return bringToTopListeners;
	}
	
	/**
	 * Gets the flick deceleration.
	 *
	 * @return the flick deceleration
	 */
	public float getFlickDeceleration() {
		return flickDeceleration;
	}
	
	/**
	 * Gets the flick listeners.
	 *
	 * @return the flick listeners
	 */
	public List<OrthoFlickListener> getFlickListeners() {
		return flickListeners;
	}
	
	/**
	 * Gets the item listeners.
	 *
	 * @return the item listeners
	 */
	public List<ItemListener> getItemListeners() {
		return itemListeners;
	}
	
	/**
	 * Gets the location.
	 *
	 * @return the location
	 */
	public Location getLocation() {
		if ((this.getParent() != null) && (this.getParent() instanceof Window)) {
			return new Location(this.getLocalLocation().x
					+ (((Window) this.getParent()).getWidth() / 2),
					this.getLocalLocation().y
							+ (((Window) this.getParent()).getHeight() / 2), 0);
		} else {
			return this.getLocalLocation();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IOrthoContentItemImplementation#getMaxScale()
	 */
	public float getMaxScale() {
		return ((IOrthoContentItemImplementation) this.contentItemImplementation)
				.getMaxScale();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IOrthoContentItemImplementation#getMinScale()
	 */
	public float getMinScale() {
		return ((IOrthoContentItemImplementation) this.contentItemImplementation)
				.getMinScale();
	}
	
	/**
	 * Gets the order.
	 *
	 * @return the order
	 */
	public int getOrder() {
		return zOrder;
	}
	
	/**
	 * Gets the ortho control point rotate translate scale listeners.
	 *
	 * @return the ortho control point rotate translate scale listeners
	 */
	public List<OrthoControlPointRotateTranslateScaleListener> getOrthoControlPointRotateTranslateScaleListeners() {
		return orthoControlPointRotateTranslateScaleListeners;
	}
	
	/**
	 * Gets the ortho snap listeners.
	 *
	 * @return the ortho snap listeners
	 */
	public List<OrthoSnapListener> getOrthoSnapListeners() {
		return orthoSnapListeners;
	}
	
	/**
	 * Gets the parent.
	 *
	 * @return the parent
	 */
	public OrthoContainer getParent() {
		return parent;
	}
	
	/**
	 * Gets the scale motion deceleration.
	 *
	 * @return the scale motion deceleration
	 */
	public float getScaleMotionDeceleration() {
		return scaleMotionDeceleration;
	}
	
	/**
	 * Gets the scale motion listeners.
	 *
	 * @return the scale motion listeners
	 */
	public List<OrthoScaleMotionListener> getScaleMotionListeners() {
		return scaleMotionListeners;
	}
	
	/**
	 * Gets the spin listeners.
	 *
	 * @return the spin listeners
	 */
	public List<OrthoSpinListener> getSpinListeners() {
		return spinListeners;
	}
	
	/**
	 * Checks if is bring to top enabled.
	 *
	 * @return true, if is bring to top enabled
	 */
	public boolean isBringToTopEnabled() {
		return this.bringToTopable;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IOrthoContentItemImplementation#isFlickable()
	 */
	public boolean isFlickable() {
		return ((IOrthoContentItemImplementation) this.contentItemImplementation)
				.isFlickable();
	}
	
	/**
	 * Checks if is rotate translate scale enabled.
	 *
	 * @return true, if is rotate translate scale enabled
	 */
	public boolean isRotateTranslateScaleEnabled() {
		return this.rotateTranslateScalable;
	}
	
	/**
	 * Checks if is single touch rotate translate.
	 *
	 * @return true, if is single touch rotate translate
	 */
	public boolean isSingleTouchRotateTranslate() {
		return isSingleTouchRotateTranslate;
	}
	
	/**
	 * Checks if is snap enabled.
	 *
	 * @return true, if is snap enabled
	 */
	public boolean isSnapEnabled() {
		return snapable;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.listener.BringToTopListener
	 * #itemBringToToped(synergynetframework.appsystem.contentsystem.items.
	 * ContentItem)
	 */
	@Override
	public void itemBringToToped(ContentItem item) {
		for (BringToTopListener l : this.bringToTopListeners) {
			l.itemBringToToped(item);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.listener.OrthoFlickListener
	 * #
	 * itemFlicked(synergynetframework.appsystem.contentsystem.items.ContentItem
	 * , float, float)
	 */
	@Override
	public void itemFlicked(ContentItem item, float x, float y) {
		for (OrthoFlickListener l : flickListeners) {
			l.itemFlicked(this, x, y);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.listener.
	 * OrthoControlPointRotateTranslateScaleListener
	 * #itemRotated(synergynetframework
	 * .appsystem.contentsystem.items.ContentItem, float, float)
	 */
	@Override
	public void itemRotated(ContentItem item, float newAngle, float oldAngle) {
		for (OrthoControlPointRotateTranslateScaleListener l : this.orthoControlPointRotateTranslateScaleListeners) {
			l.itemRotated(this, newAngle, oldAngle);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.listener.
	 * OrthoControlPointRotateTranslateScaleListener
	 * #itemScaled(synergynetframework
	 * .appsystem.contentsystem.items.ContentItem, float, float)
	 */
	@Override
	public void itemScaled(ContentItem item, float newScaleFactor,
			float oldScaleFactor) {
		for (OrthoControlPointRotateTranslateScaleListener l : this.orthoControlPointRotateTranslateScaleListeners) {
			l.itemScaled(this, newScaleFactor, oldScaleFactor);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.listener.
	 * OrthoScaleMotionListener
	 * #itemScaleMotioned(synergynetframework.appsystem.contentsystem
	 * .items.ContentItem, float)
	 */
	@Override
	public void itemScaleMotioned(ContentItem item, float scale) {
		for (OrthoScaleMotionListener l : scaleMotionListeners) {
			l.itemScaleMotioned(this, scale);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.listener.OrthoSnapListener
	 * #
	 * itemSnapped(synergynetframework.appsystem.contentsystem.items.ContentItem
	 * , synergynetframework.jme.cursorsystem.fixutils.FixLocationStatus)
	 */
	@Override
	public void itemSnapped(ContentItem item,
			FixLocationStatus fixLocationStatus) {
		
		for (OrthoSnapListener l : this.orthoSnapListeners) {
			l.itemSnapped(item, fixLocationStatus);
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.listener.OrthoSpinListener
	 * #itemSpun(synergynetframework.appsystem.contentsystem.items.ContentItem,
	 * float)
	 */
	@Override
	public void itemSpun(ContentItem item, float rot) {
		for (OrthoSpinListener l : spinListeners) {
			l.itemSpun(this, rot);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.listener.
	 * OrthoControlPointRotateTranslateScaleListener
	 * #itemTranslated(synergynetframework
	 * .appsystem.contentsystem.items.ContentItem, float, float, float, float)
	 */
	@Override
	public void itemTranslated(ContentItem item, float newLocationX,
			float newLocationY, float oldLocationX, float oldLocationY) {
		for (OrthoControlPointRotateTranslateScaleListener l : this.orthoControlPointRotateTranslateScaleListeners) {
			l.itemTranslated(this, newLocationX, newLocationY, oldLocationX,
					oldLocationY);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IOrthoContentItemImplementation#makeFlickable(float)
	 */
	public void makeFlickable(float deceleration) {
		this.flickDeceleration = deceleration;
		((IOrthoContentItemImplementation) this.contentItemImplementation)
				.makeFlickable(deceleration);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IOrthoContentItemImplementation#makeUnflickable()
	 */
	public void makeUnflickable() {
		((IOrthoContentItemImplementation) this.contentItemImplementation)
				.makeUnflickable();
	}
	
	// Flick Listener
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IOrthoContentItemImplementation#placeRandom()
	 */
	public void placeRandom() {
		((IOrthoContentItemImplementation) this.contentItemImplementation)
				.placeRandom();
	}
	
	/**
	 * Removes the bring to top listeners.
	 */
	public void removeBringToTopListeners() {
		bringToTopListeners.clear();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .
	 * IOrthoContentItemImplementation#removeBringToTopListeners(synergynetframework
	 * .appsystem.contentsystem.items.listener.BringToTopListener)
	 */
	public void removeBringToTopListeners(BringToTopListener l) {
		bringToTopListeners.remove(l);
	}
	
	// screen cursor Listener
	
	/**
	 * Removes the flick listeners.
	 */
	public void removeFlickListeners() {
		flickListeners.clear();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .
	 * IOrthoContentItemImplementation#removeFlickListeners(synergynetframework.
	 * appsystem.contentsystem.items.listener.OrthoFlickListener)
	 */
	public void removeFlickListeners(OrthoFlickListener l) {
		flickListeners.remove(l);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IOrthoContentItemImplementation#removeItemListerner(synergynetframework.
	 * appsystem.contentsystem.items.listener.ItemListener)
	 */
	public void removeItemListerner(ItemListener itemListener) {
		itemListeners.remove(itemListener);
	}
	
	/**
	 * Removes the item listerners.
	 */
	public void removeItemListerners() {
		itemListeners.clear();
	}
	
	/**
	 * Removes the ortho control point rotate translate scale listeners.
	 */
	public void removeOrthoControlPointRotateTranslateScaleListeners() {
		orthoControlPointRotateTranslateScaleListeners.clear();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IOrthoContentItemImplementation#
	 * removeOrthoControlPointRotateTranslateScaleListeners
	 * (synergynetframework.appsystem
	 * .contentsystem.items.listener.OrthoControlPointRotateTranslateScaleListener
	 * )
	 */
	public void removeOrthoControlPointRotateTranslateScaleListeners(
			OrthoControlPointRotateTranslateScaleListener l) {
		orthoControlPointRotateTranslateScaleListeners.remove(l);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IOrthoContentItemImplementation#removeScreenCursorListeners()
	 */
	public void removeScreenCursorListeners() {
		((IOrthoContentItemImplementation) this.contentItemImplementation)
				.removeScreenCursorListeners();
		screenCursorListeners.clear();
	}
	
	/**
	 * Removes the snap listeners.
	 */
	public void removeSnapListeners() {
		orthoSnapListeners.clear();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IOrthoContentItemImplementation#removeSnapListeners(synergynetframework.
	 * appsystem.contentsystem.items.listener.OrthoSnapListener)
	 */
	public void removeSnapListeners(OrthoSnapListener l) {
		orthoSnapListeners.remove(l);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IOrthoContentItemImplementation#reset()
	 */
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
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IOrthoContentItemImplementation#rotateRandom()
	 */
	public void rotateRandom() {
		((IOrthoContentItemImplementation) this.contentItemImplementation)
				.rotateRandom();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IOrthoContentItemImplementation#setAsBottomObject()
	 */
	public void setAsBottomObject() {
		if (this.isBringToTopEnabled()) {
			((IOrthoContentItemImplementation) this.contentItemImplementation)
					.setAsBottomObject();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IOrthoContentItemImplementation#setAsTopObject()
	 */
	public void setAsTopObject() {
		if (this.isBringToTopEnabled()) {
			((IOrthoContentItemImplementation) this.contentItemImplementation)
					.setAsTopObject();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IOrthoContentItemImplementation#setAsTopObjectAndBroadCastEvent()
	 */
	public void setAsTopObjectAndBroadCastEvent() {
		if (this.isBringToTopEnabled()) {
			((IOrthoContentItemImplementation) this.contentItemImplementation)
					.setAsTopObjectAndBroadCastEvent();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IOrthoContentItemImplementation#setBringToTopable(boolean)
	 */
	public void setBringToTopable(boolean isEnabled) {
		this.bringToTopable = isEnabled;
		((IOrthoContentItemImplementation) this.contentItemImplementation)
				.setBringToTopable(isEnabled);
	}
	
	/**
	 * Sets the bring to top listeners.
	 *
	 * @param bringToTopListeners
	 *            the new bring to top listeners
	 */
	public void setBringToTopListeners(
			List<BringToTopListener> bringToTopListeners) {
		this.bringToTopListeners = bringToTopListeners;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IOrthoContentItemImplementation#setFixLocations(java.util.List)
	 */
	public void setFixLocations(List<FixLocationStatus> fixLocations) {
		((IOrthoContentItemImplementation) this.contentItemImplementation)
				.setFixLocations(fixLocations);
	}
	
	/**
	 * Sets the flick listeners.
	 *
	 * @param flickListeners
	 *            the new flick listeners
	 */
	public void setFlickListeners(List<OrthoFlickListener> flickListeners) {
		this.flickListeners = flickListeners;
	}
	
	/**
	 * Sets the item listeners.
	 *
	 * @param itemListeners
	 *            the new item listeners
	 */
	public void setItemListeners(List<ItemListener> itemListeners) {
		this.itemListeners = itemListeners;
	}
	
	/**
	 * Sets the location.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public void setLocation(float x, float y) {
		this.setLocation(new Location(x, y, 0));
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.ContentItem#setLocation
	 * (synergynetframework.appsystem.contentsystem.items.utils.Location)
	 */
	@Override
	public void setLocation(Location location) {
		if ((this.getParent() != null) && (this.getParent() instanceof Window)) {
			this.setLocalLocation(
					location.x - (((Window) this.getParent()).getWidth() / 2),
					location.y - (((Window) this.getParent()).getHeight() / 2));
		} else {
			this.setLocalLocation(location);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.ContentItem#setName
	 * (java.lang.String)
	 */
	public void setName(String name) {
		super.setName(name);
		((IOrthoContentItemImplementation) this.contentItemImplementation)
				.setName(name);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IOrthoContentItemImplementation#setOrder(int)
	 */
	public void setOrder(int zOrder) {
		this.zOrder = zOrder;
		((IOrthoContentItemImplementation) this.contentItemImplementation)
				.setOrder(zOrder);
	}
	
	/**
	 * Sets the ortho control point rotate translate scale listeners.
	 *
	 * @param orthoControlPointRotateTranslateScaleListeners
	 *            the new ortho control point rotate translate scale listeners
	 */
	public void setOrthoControlPointRotateTranslateScaleListeners(
			List<OrthoControlPointRotateTranslateScaleListener> orthoControlPointRotateTranslateScaleListeners) {
		this.orthoControlPointRotateTranslateScaleListeners = orthoControlPointRotateTranslateScaleListeners;
	}
	
	/**
	 * Sets the ortho snap listeners.
	 *
	 * @param orthoSnapListeners
	 *            the new ortho snap listeners
	 */
	public void setOrthoSnapListeners(List<OrthoSnapListener> orthoSnapListeners) {
		this.orthoSnapListeners = orthoSnapListeners;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IOrthoContentItemImplementation#setRightClickDistance(float)
	 */
	public void setRightClickDistance(float distance) {
		((IOrthoContentItemImplementation) this.contentItemImplementation)
				.setRightClickDistance(distance);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IOrthoContentItemImplementation#setRotateTranslateScalable(boolean)
	 */
	public void setRotateTranslateScalable(boolean isEnabled) {
		this.rotateTranslateScalable = isEnabled;
		((IOrthoContentItemImplementation) this.contentItemImplementation)
				.setRotateTranslateScalable(isEnabled);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IOrthoContentItemImplementation#setRotateTranslateScalable(boolean,
	 * boolean, synergynetframework.appsystem.contentsystem.items.ContentItem)
	 */
	@Override
	public void setRotateTranslateScalable(boolean isEnabled,
			boolean attachToParent, ContentItem targetItem) {
		this.rotateTranslateScalable = isEnabled;
		((IOrthoContentItemImplementation) this.contentItemImplementation)
				.setRotateTranslateScalable(isEnabled, attachToParent,
						targetItem);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IOrthoContentItemImplementation#setScaleLimit(float, float)
	 */
	public void setScaleLimit(float min, float max) {
		((IOrthoContentItemImplementation) this.contentItemImplementation)
				.setScaleLimit(min, max);
	}
	
	/**
	 * Sets the scale motion listeners.
	 *
	 * @param scaleMotionListeners
	 *            the new scale motion listeners
	 */
	public void setScaleMotionListeners(
			List<OrthoScaleMotionListener> scaleMotionListeners) {
		this.scaleMotionListeners = scaleMotionListeners;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IOrthoContentItemImplementation#setSingleTouchRotateTranslate(boolean)
	 */
	public void setSingleTouchRotateTranslate(boolean isEnabled)
			throws Exception {
		this.isSingleTouchRotateTranslate = isEnabled;
		((IOrthoContentItemImplementation) this.contentItemImplementation)
				.setSingleTouchRotateTranslate(isEnabled);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IOrthoContentItemImplementation#setSnapable(boolean)
	 */
	public void setSnapable(boolean snapable) {
		this.snapable = snapable;
		((IOrthoContentItemImplementation) this.contentItemImplementation)
				.setSnapable(snapable);
	}
	
	/**
	 * Sets the spin listeners.
	 *
	 * @param spinListeners
	 *            the new spin listeners
	 */
	public void setSpinListeners(List<OrthoSpinListener> spinListeners) {
		this.spinListeners = spinListeners;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IOrthoContentItemImplementation#setTolerance(float)
	 */
	public void setTolerance(float tolerance) {
		((IOrthoContentItemImplementation) this.contentItemImplementation)
				.setTolerance(tolerance);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IOrthoContentItemImplementation#setZRotateLimit(float, float)
	 */
	public void setZRotateLimit(float min, float max) {
		((IOrthoContentItemImplementation) this.contentItemImplementation)
				.setZRotateLimit(min, max);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IOrthoContentItemImplementation#turnOffEventDispatcher()
	 */
	public void turnOffEventDispatcher() {
		((IOrthoContentItemImplementation) this.contentItemImplementation)
				.turnOffEventDispatcher();
	}

}