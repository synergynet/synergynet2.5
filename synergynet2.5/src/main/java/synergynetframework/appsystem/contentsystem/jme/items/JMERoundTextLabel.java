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
import java.util.List;

import synergynetframework.appsystem.contentsystem.ContentSystemUtils;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.RoundTextLabel;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IRoundTextLabelImplementation;

/**
 * The Class JMERoundTextLabel.
 */
public class JMERoundTextLabel extends JMERoundFrame implements
		IRoundTextLabelImplementation {
	
	/** The item. */
	private RoundTextLabel item;

	/** The text block height. */
	private int textBlockHeight;

	/** The text descent. */
	private int textDescent;

	/** The text height. */
	private int textHeight;

	/** The text width. */
	private int textWidth;

	/**
	 * Instantiates a new JME round text label.
	 *
	 * @param contentItem
	 *            the content item
	 */
	public JMERoundTextLabel(ContentItem contentItem) {
		super(contentItem);
		this.item = (RoundTextLabel) contentItem;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMERoundFrame#draw
	 * ()
	 */
	@Override
	protected void draw() {
		super.draw();
		gfx.setColor(item.getTextColour());
		gfx.setFont(item.getFont());
		int y = ((int) (item.getRadius() - (textBlockHeight / 2)) + textHeight)
				- textDescent;
		for (int i = 0; i < item.getLines().size(); i++) {
			gfx.drawString(
					item.getLines().get(i),
					(item.getBorderSize() - textDescent)
							+ ((int) (item.getRadius() - (textWidth / 2))), y);
			y += textHeight;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMERoundFrame#init
	 * ()
	 */
	@Override
	public void init() {
		super.init();
		resize();
	}
	
	/**
	 * Resize.
	 */
	protected void resize() {

		int maxWidth = 1;
		for (int i = 0; i < item.getLines().size(); i++) {
			int tw = ContentSystemUtils.getStringWidth(item.getFont(), item
					.getLines().get(i));
			if (tw > maxWidth) {
				maxWidth = tw;
			}
		}

		textWidth = maxWidth;
		textHeight = ContentSystemUtils.getFontHeight(item.getFont());
		textDescent = ContentSystemUtils.getFontDescent(item.getFont());
		textBlockHeight = textHeight * item.getLines().size();

		float r = item.getRadius();

		if (item.isAutoFitSize()) {
			r = ((float) Math.sqrt((textWidth * textWidth)
					+ (textBlockHeight * textBlockHeight)) / 2)
					+ item.getBorderSize() + textDescent;
		}
		this.graphicsImageDisc.updateGeometry(r);
		
		if (r < 1) {
			r = 2;
		}

		this.graphicsImageDisc.recreateImageForSize(r);
		gfx = this.graphicsImageDisc.getImageGraphics();
		
		item.setRadius(r);

		this.render();
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IRoundTextLabelImplementation#setAutoFitSize(boolean)
	 */
	@Override
	public void setAutoFitSize(boolean isEnabled) {
		this.resize();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IRoundTextLabelImplementation#setCRLFSeparatedString(java.lang.String)
	 */
	@Override
	public void setCRLFSeparatedString(String s) {
		this.resize();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IRoundTextLabelImplementation#setFont(java.awt.Font)
	 */
	@Override
	public void setFont(Font font) {
		this.resize();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IRoundTextLabelImplementation#setLines(java.util.List)
	 */
	@Override
	public void setLines(List<String> lines) {
		this.resize();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IRoundTextLabelImplementation#setLines(java.lang.String, int)
	 */
	@Override
	public void setLines(String s, int charsPerLine) {
		this.resize();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMERoundFrame#setRadius
	 * (float)
	 */
	@Override
	public void setRadius(float radius) {
		this.resize();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IRoundTextLabelImplementation#setText(java.lang.String)
	 */
	@Override
	public void setText(String text) {
		this.setCRLFSeparatedString(text);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IRoundTextLabelImplementation#setTextColour(java.awt.Color)
	 */
	@Override
	public void setTextColour(Color textColour) {
		this.resize();
	}
	
}
