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

package synergynetframework.appsystem.contentsystem.items.utils;

import java.awt.Image;
import java.io.Serializable;
import java.net.URL;

import javax.swing.ImageIcon;

/**
 * The Class ImageInfo.
 */
public class ImageInfo implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 210773506843993131L;

	/** The image resource. */
	private URL imageResource;

	/** The is auto ratio by image height. */
	private boolean isAutoRatioByImageHeight = false;

	/** The height. */
	private int x = 0, y = 0, width = 150, height = 100;

	/**
	 * Instantiates a new image info.
	 *
	 * @param imageResource
	 *            the image resource
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 */
	public ImageInfo(URL imageResource, int x, int y, int width, int height) {
		this.imageResource = imageResource;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Gets the image resource.
	 *
	 * @return the image resource
	 */
	public URL getImageResource() {
		return imageResource;
	}

	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public int getWidth() {
		if ((imageResource != null) && this.isAutoRatioByImageHeight) {
			Image image = new ImageIcon(imageResource).getImage();
			return (this.height * image.getHeight(null)) / image.getWidth(null);
		}
		return width;
	}
	
	/**
	 * Gets the x.
	 *
	 * @return the x
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Gets the y.
	 *
	 * @return the y
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Checks if is auto ratio by image height.
	 *
	 * @return true, if is auto ratio by image height
	 */
	public boolean isAutoRatioByImageHeight() {
		return isAutoRatioByImageHeight;
	}
	
	/**
	 * Sets the auto ratio by image height.
	 *
	 * @param isAutoRatioByImageHeight
	 *            the new auto ratio by image height
	 */
	public void setAutoRatioByImageHeight(boolean isAutoRatioByImageHeight) {
		this.isAutoRatioByImageHeight = isAutoRatioByImageHeight;
	}
	
	/**
	 * Sets the height.
	 *
	 * @param height
	 *            the new height
	 */
	public void setHeight(int height) {
		this.height = height;
	}
	
	/**
	 * Sets the image resource.
	 *
	 * @param imageResource
	 *            the new image resource
	 */
	public void setImageResource(URL imageResource) {
		this.imageResource = imageResource;
	}
	
	/**
	 * Sets the width.
	 *
	 * @param width
	 *            the new width
	 */
	public void setWidth(int width) {
		this.width = width;
	}
	
	/**
	 * Sets the x.
	 *
	 * @param x
	 *            the new x
	 */
	public void setX(int x) {
		this.x = x;
	}
	
	/**
	 * Sets the y.
	 *
	 * @param y
	 *            the new y
	 */
	public void setY(int y) {
		this.y = y;
	}
}
