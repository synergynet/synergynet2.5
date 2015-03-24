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

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IRoundFrameImplementation;
import synergynetframework.appsystem.contentsystem.items.utils.ImageInfo;


/**
 * The Class RoundFrame.
 */
public class RoundFrame extends RoundContentItem implements IRoundFrameImplementation{
			
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -9030421443289334813L;

	/** The URL to image map. */
	protected LinkedHashMap<URL,ImageInfo> URLToImageMap = new LinkedHashMap<URL, ImageInfo>();

	/**
	 * Instantiates a new round frame.
	 *
	 * @param contentSystem the content system
	 * @param name the name
	 */
	public RoundFrame(ContentSystem contentSystem, String name) {
		super(contentSystem, name);
		
		//frameImplementation = (IFrameImplementation)this.contentItemImplementation;
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IRoundFrameImplementation#drawImage(java.net.URL)
	 */
	public void drawImage(URL imageResource){
		if (imageResource!=null){
			URLToImageMap.put(imageResource, new ImageInfo(imageResource, getBorderSize(),getBorderSize(), ((int)(2*getRadius()))-2*getBorderSize(), ((int)(2*getRadius()))-2*getBorderSize()));
			((IRoundFrameImplementation)this.contentItemImplementation).drawImage(imageResource);
		}
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IRoundFrameImplementation#drawImage(java.net.URL, int, int, int, int)
	 */
	public void drawImage(URL imageResource, int x, int y, int width, int height){
		if (imageResource!=null){
			URLToImageMap.put(imageResource, new ImageInfo(imageResource,x,y,width,height));
			((IRoundFrameImplementation)this.contentItemImplementation).drawImage(imageResource, x, y, width, height);
		}
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IRoundFrameImplementation#removeImage(java.net.URL)
	 */
	public void removeImage(URL imageResource){
		URLToImageMap.remove(imageResource);
		((IRoundFrameImplementation)this.contentItemImplementation).removeImage(imageResource);
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IRoundFrameImplementation#getImages()
	 */
	public HashMap<URL, ImageInfo> getImages(){
		return URLToImageMap;
	}

	/**
	 * Gets the image resources.
	 *
	 * @return the image resources
	 */
	public LinkedHashMap<URL,ImageInfo> getImageResources(){
		return URLToImageMap;
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IRoundFrameImplementation#removeAllImages()
	 */
	@Override
	public void removeAllImages() {
		URLToImageMap.clear();
		((IRoundFrameImplementation)this.contentItemImplementation).removeAllImages();
	}
}
