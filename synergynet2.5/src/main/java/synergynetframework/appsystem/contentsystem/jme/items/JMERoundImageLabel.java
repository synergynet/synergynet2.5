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

package synergynetframework.appsystem.contentsystem.jme.items;

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.RoundImageLabel;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IRoundImageLabelImplementation;

/**
 * The Class JMERoundImageLabel.
 */
public class JMERoundImageLabel extends JMERoundFrame implements
		IRoundImageLabelImplementation {

	/** The inner image height. */
	private int innerImageHeight;

	/** The inner image width. */
	private int innerImageWidth;

	/** The item. */
	private RoundImageLabel item;
	
	/** The label width. */
	private int labelWidth;

	/**
	 * Instantiates a new JME round image label.
	 *
	 * @param contentItem
	 *            the content item
	 */
	public JMERoundImageLabel(ContentItem contentItem) {
		super(contentItem);
		this.item = (RoundImageLabel) contentItem;
		
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMERoundFrame#draw
	 * ()
	 */
	protected void draw() {
		if (item.getImageInfo().getImageResource() != null) {
			Image image = new ImageIcon(item.getImageInfo().getImageResource())
					.getImage();
			gfx.drawImage(image, (this.labelWidth / 2) - (innerImageWidth / 2),
					(this.labelWidth / 2) - (innerImageHeight / 2),
					innerImageHeight, innerImageHeight, null);
		}

	}
	
	/**
	 * Resize.
	 */
	public void resize() {
		
		if (item.isAutoFit()) {
			item.getImageInfo().setHeight((int) (item.getRadius() * 2));
		}

		this.innerImageHeight = item.getImageInfo().getHeight();
		this.item.getImageInfo().setAutoRatioByImageHeight(true);
		this.innerImageWidth = item.getImageInfo().getWidth();
		this.labelWidth = (int) item.getRadius() * 2;

		this.updateSize();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IRoundImageLabelImplementation#setAutoFit(boolean)
	 */
	@Override
	public void setAutoFit(boolean autoFit) {
		resize();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IRoundImageLabelImplementation#setImageInfo(java.net.URL)
	 */
	@Override
	public void setImageInfo(URL imageResource) {
		resize();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMERoundFrame#setRadius
	 * (float)
	 */
	public void setRadius(float radius) {
		resize();
	}

}
