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

package synergynetframework.appsystem.contentsystem.items.implementation.interfaces;

import java.util.List;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.listener.BringToTopListener;
import synergynetframework.appsystem.contentsystem.items.listener.ItemListener;
import synergynetframework.appsystem.contentsystem.items.listener.OrthoControlPointRotateTranslateScaleListener;
import synergynetframework.appsystem.contentsystem.items.listener.OrthoFlickListener;
import synergynetframework.appsystem.contentsystem.items.listener.OrthoSnapListener;
import synergynetframework.appsystem.contentsystem.items.listener.ScreenCursorListener;
import synergynetframework.jme.cursorsystem.fixutils.FixLocationStatus;

/**
 * The Interface IOrthoContentItemImplementation.
 */
public interface IOrthoContentItemImplementation extends
		IContentItemImplementation {

	/**
	 * Adds the bring to top listener.
	 *
	 * @param l
	 *            the l
	 */
	public void addBringToTopListener(BringToTopListener l);
	
	/**
	 * Adds the flick listener.
	 *
	 * @param l
	 *            the l
	 */
	public void addFlickListener(OrthoFlickListener l);

	/**
	 * Adds the item listener.
	 *
	 * @param itemListener
	 *            the item listener
	 */
	public void addItemListener(ItemListener itemListener);
	
	/**
	 * Adds the ortho control point rotate translate scale listener.
	 *
	 * @param l
	 *            the l
	 */
	public void addOrthoControlPointRotateTranslateScaleListener(
			OrthoControlPointRotateTranslateScaleListener l);

	/**
	 * Adds the screen cursor listener.
	 *
	 * @param l
	 *            the l
	 */
	public void addScreenCursorListener(ScreenCursorListener l);

	/**
	 * Adds the snap listener.
	 *
	 * @param l
	 *            the l
	 */
	public void addSnapListener(OrthoSnapListener l);

	/**
	 * Allow more than two to rotate and scale.
	 *
	 * @param b
	 *            the b
	 */
	public void allowMoreThanTwoToRotateAndScale(boolean b);
	
	/**
	 * Allow single touch free move.
	 *
	 * @param b
	 *            the b
	 */
	public void allowSingleTouchFreeMove(boolean b);

	/**
	 * Allow snap to occupied location.
	 *
	 * @param allowSnapToOccupiedLocation
	 *            the allow snap to occupied location
	 */
	public void allowSnapToOccupiedLocation(boolean allowSnapToOccupiedLocation);

	/**
	 * Center item.
	 */
	public void centerItem();
	
	/**
	 * Flick.
	 *
	 * @param velocityX
	 *            the velocity x
	 * @param velocityY
	 *            the velocity y
	 * @param deceleration
	 *            the deceleration
	 */
	public void flick(float velocityX, float velocityY, float deceleration);
	
	/**
	 * Gets the max scale.
	 *
	 * @return the max scale
	 */
	public float getMaxScale();
	
	/**
	 * Gets the min scale.
	 *
	 * @return the min scale
	 */
	public float getMinScale();

	/**
	 * Checks if is flickable.
	 *
	 * @return true, if is flickable
	 */
	public boolean isFlickable();

	/**
	 * Make flickable.
	 *
	 * @param deceleration
	 *            the deceleration
	 */
	public void makeFlickable(float deceleration);

	/**
	 * Make unflickable.
	 */
	public void makeUnflickable();

	/**
	 * Place random.
	 */
	public void placeRandom();
	
	/**
	 * Removes the bring to top listeners.
	 *
	 * @param l
	 *            the l
	 */
	public void removeBringToTopListeners(BringToTopListener l);

	/**
	 * Removes the flick listeners.
	 *
	 * @param l
	 *            the l
	 */
	public void removeFlickListeners(OrthoFlickListener l);

	/**
	 * Removes the item listerner.
	 *
	 * @param itemListener
	 *            the item listener
	 */
	public void removeItemListerner(ItemListener itemListener);
	
	/**
	 * Removes the ortho control point rotate translate scale listeners.
	 *
	 * @param l
	 *            the l
	 */
	public void removeOrthoControlPointRotateTranslateScaleListeners(
			OrthoControlPointRotateTranslateScaleListener l);

	/**
	 * Removes the screen cursor listeners.
	 */
	public void removeScreenCursorListeners();

	/**
	 * Removes the snap listeners.
	 *
	 * @param l
	 *            the l
	 */
	public void removeSnapListeners(OrthoSnapListener l);

	/**
	 * Reset.
	 */
	public void reset();

	/**
	 * Rotate random.
	 */
	public void rotateRandom();

	/**
	 * Sets the as bottom object.
	 */
	public void setAsBottomObject();

	/**
	 * Sets the as top object.
	 */
	public void setAsTopObject();

	/**
	 * Sets the as top object and broad cast event.
	 */
	public void setAsTopObjectAndBroadCastEvent();

	/**
	 * Sets the bring to topable.
	 *
	 * @param isEnabled
	 *            the new bring to topable
	 */
	public void setBringToTopable(boolean isEnabled);

	/**
	 * Sets the fix locations.
	 *
	 * @param fixLocations
	 *            the new fix locations
	 */
	public void setFixLocations(List<FixLocationStatus> fixLocations);
	
	/**
	 * Sets the order.
	 *
	 * @param zOrder
	 *            the new order
	 */
	public void setOrder(int zOrder);

	/**
	 * Sets the right click distance.
	 *
	 * @param distance
	 *            the new right click distance
	 */
	public void setRightClickDistance(float distance);

	/**
	 * Sets the rotate translate scalable.
	 *
	 * @param isEnabled
	 *            the new rotate translate scalable
	 */
	public void setRotateTranslateScalable(boolean isEnabled);

	/**
	 * Sets the rotate translate scalable.
	 *
	 * @param isEnabled
	 *            the is enabled
	 * @param attachToParent
	 *            the attach to parent
	 * @param targetItem
	 *            the target item
	 */
	public void setRotateTranslateScalable(boolean isEnabled,
			boolean attachToParent, ContentItem targetItem);
	
	/**
	 * Sets the scale limit.
	 *
	 * @param min
	 *            the min
	 * @param max
	 *            the max
	 */
	public void setScaleLimit(float min, float max);

	/**
	 * Sets the single touch rotate translate.
	 *
	 * @param isEnabled
	 *            the new single touch rotate translate
	 * @throws Exception
	 *             the exception
	 */
	public void setSingleTouchRotateTranslate(boolean isEnabled)
			throws Exception;

	/**
	 * Sets the snapable.
	 *
	 * @param isEnabled
	 *            the new snapable
	 */
	public void setSnapable(boolean isEnabled);
	
	/**
	 * Sets the tolerance.
	 *
	 * @param tolerance
	 *            the new tolerance
	 */
	public void setTolerance(float tolerance);
	
	/**
	 * Sets the z rotate limit.
	 *
	 * @param min
	 *            the min
	 * @param max
	 *            the max
	 */
	public void setZRotateLimit(float min, float max);
	
	/**
	 * Turn off event dispatcher.
	 */
	public void turnOffEventDispatcher();
	
}
