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

package synergynetframework.appsystem.contentsystem.items.utils;

import java.awt.Image;
import java.io.Serializable;
import java.net.URL;

import javax.swing.ImageIcon;

public class ImageInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 210773506843993131L;
	private URL imageResource;
	private int x =0, y =0, width = 150, height = 100;
	private boolean isAutoRatioByImageHeight = false;
	
	public ImageInfo(URL imageResource, int x, int y, int width, int height){
		this.imageResource = imageResource;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public URL getImageResource(){
		return imageResource;
	}
	
	public void setImageResource(URL imageResource) {
		this.imageResource = imageResource;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		if (imageResource != null && this.isAutoRatioByImageHeight){
			Image image = new ImageIcon(imageResource).getImage();
			return this.height * image.getHeight(null)/ image.getWidth(null);
		}
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isAutoRatioByImageHeight() {
		return isAutoRatioByImageHeight;
	}

	public void setAutoRatioByImageHeight(boolean isAutoRatioByImageHeight) {
		this.isAutoRatioByImageHeight = isAutoRatioByImageHeight;
	}
}
