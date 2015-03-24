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

import java.awt.Graphics2D;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IBackgroundControllerImplementation;
import synergynetframework.appsystem.contentsystem.items.utils.ImageInfo;

public class BackgroundController extends QuadContentItem implements IBackgroundControllerImplementation, Serializable {
		
	private static final long serialVersionUID = -2782160211110663419L;	
	
	public BackgroundController(ContentSystem contentSystem, String name) {
		super(contentSystem, name);		
	}
	
	public void setBackgroundRotateTranslateScalable(boolean isEnabled){
		this.setRotateTranslateScalable(isEnabled);
		((IBackgroundControllerImplementation)this.contentItemImplementation).setBackgroundRotateTranslateScalable(isEnabled);
	}

	@Override
	public void drawImage(URL imageResource) {
		 
		
	}

	@Override
	public void drawImage(URL imageResource, int x, int y, int width, int height) {
		 
		
	}

	@Override
	public HashMap<URL, ImageInfo> getImages() {
		 
		return null;
	}

	@Override
	public void removeImage(URL imageResource) {
		 
	}

	@Override
	public void removeAllImages() {
		 
		
	}

	@Override
	public Graphics2D getGraphicsContext() {
		 
		return null;
	}

	@Override
	public void flushGraphics() {
		 
		
	}
	
}