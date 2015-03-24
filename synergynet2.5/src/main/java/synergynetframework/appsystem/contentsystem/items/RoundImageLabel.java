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

import java.net.URL;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IRoundImageLabelImplementation;
import synergynetframework.appsystem.contentsystem.items.utils.ImageInfo;

/**
 * The Class RoundImageLabel.
 */
public class RoundImageLabel extends RoundFrame implements
		IRoundImageLabelImplementation {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3227704681807528749L;

	/** The auto fit. */
	protected boolean autoFit = true;

	/** The image info. */
	protected ImageInfo imageInfo;
	
	/**
	 * Instantiates a new round image label.
	 *
	 * @param contentSystem
	 *            the content system
	 * @param name
	 *            the name
	 */
	public RoundImageLabel(ContentSystem contentSystem, String name) {
		super(contentSystem, name);
		this.imageInfo = new ImageInfo(null, 0, 0, 100, 100);
	}

	/**
	 * Gets the image info.
	 *
	 * @return the image info
	 */
	public ImageInfo getImageInfo() {
		return imageInfo;
	}

	/**
	 * Checks if is auto fit.
	 *
	 * @return true, if is auto fit
	 */
	public boolean isAutoFit() {
		return autoFit;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IRoundImageLabelImplementation#setAutoFit(boolean)
	 */
	public void setAutoFit(boolean autoFit) {
		this.autoFit = autoFit;
		((IRoundImageLabelImplementation) this.contentItemImplementation)
				.setAutoFit(autoFit);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IRoundImageLabelImplementation#setImageInfo(java.net.URL)
	 */
	public void setImageInfo(URL imageResource) {
		this.imageInfo.setImageResource(imageResource);

		((IRoundImageLabelImplementation) this.contentItemImplementation)
				.setImageInfo(imageResource);
	}
	
}
