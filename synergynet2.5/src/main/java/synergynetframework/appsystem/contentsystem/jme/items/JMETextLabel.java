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

import java.awt.Color;
import java.awt.Font;
import java.net.URL;

import synergynetframework.appsystem.contentsystem.ContentSystemUtils;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.TextLabel;
import synergynetframework.appsystem.contentsystem.items.TextLabel.Alignment;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.ITextLabelImplementation;

/**
 * The Class JMETextLabel.
 */
public class JMETextLabel extends JMEFrame implements ITextLabelImplementation {

	/** The item. */
	private TextLabel item;

	/** The text. */
	protected StringBuffer text = new StringBuffer();

	/** The text descent. */
	private int textDescent;

	/** The text height. */
	private int textHeight;
	
	/** The text width. */
	private int textWidth;

	/**
	 * Instantiates a new JME text label.
	 *
	 * @param contentItem
	 *            the content item
	 */
	public JMETextLabel(ContentItem contentItem) {
		super(contentItem);
		item = (TextLabel) contentItem;
		text.setLength(0);
		text.append(item.getText());
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMEFrame#draw()
	 */
	@Override
	protected void draw() {
		super.draw();
		
		gfx.setColor(item.getTextColour());
		gfx.setFont(item.getFont());
		if (item.getAlignment().equals(Alignment.CENTER)) {
			gfx.drawString(text.toString(), (item.getWidth() / 2)
					- (textWidth / 2),
					((item.getHeight() / 2) + (textHeight / 2)) - textDescent);
		} else if (item.getAlignment().equals(Alignment.LEFT)) {
			gfx.drawString(text.toString(), 0,
					((item.getHeight() / 2) + (textHeight / 2)) - textDescent);
		} else if (item.getAlignment().equals(Alignment.RIGHT)) {
			gfx.drawString(text.toString(), item.getWidth() - (textWidth / 2),
					((item.getHeight() / 2) + (textHeight / 2)) - textDescent);
		}

		this.graphicsImageQuad.updateGraphics();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMEFrame#drawImage
	 * (java.net.URL)
	 */
	@Override
	public void drawImage(URL imageResource) {
		super.drawImage(imageResource);
		this.setFont(new Font("Times New Roman", Font.PLAIN, 12));
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .ITextLabelImplementation#getAlignment()
	 */
	@Override
	public Alignment getAlignment() {
		return item.getAlignment();
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.jme.items.JMEFrame#
	 * removeAllImages()
	 */
	@Override
	public void removeAllImages() {
		super.removeAllImages();
		this.setFont(new Font("Times New Roman", Font.PLAIN, 12));
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMEFrame#resize()
	 */
	protected void resize() {
		
		text.setLength(0);
		text.append(item.getText());
		textHeight = ContentSystemUtils.getFontHeight(item.getFont());
		textWidth = ContentSystemUtils.getStringWidth(item.getFont(),
				text.toString());
		textDescent = ContentSystemUtils.getFontDescent(item.getFont());
		
		int w = item.getWidth();
		int h = item.getHeight();

		if (item.isAutoFitSize()) {
			w = textWidth + (2 * item.getBorderSize()) + textDescent;
			h = textHeight + (2 * item.getBorderSize()) + textDescent;
		}
		this.graphicsImageQuad.updateGeometry(w, h);

		if (w < 2) {
			w = 2;
		}
		if (h < 2) {
			h = 2;
		}

		item.setWidth(w);
		item.setHeight(h);
		
		this.graphicsImageQuad.recreateImageForSize(w, h);
		gfx = this.graphicsImageQuad.getImageGraphics();
		
		this.render();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .ITextLabelImplementation#setAlignment(synergynetframework.appsystem.
	 * contentsystem.items.TextLabel.Alignment)
	 */
	@Override
	public void setAlignment(Alignment textAlignment) {
		resize();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMEFrame#setAutoFitSize
	 * (boolean)
	 */
	@Override
	public void setAutoFitSize(boolean isEnabled) {
		resize();

	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .ITextLabelImplementation#setFont(java.awt.Font)
	 */
	@Override
	public void setFont(Font font) {
		resize();

	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .ITextLabelImplementation#setText(java.lang.String)
	 */
	@Override
	public void setText(String text) {
		resize();

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .ITextLabelImplementation#setTextColour(java.awt.Color)
	 */
	@Override
	public void setTextColour(Color textColour) {
		resize();

	}
}
