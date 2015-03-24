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

import java.util.List;

import synergynetframework.appsystem.contentsystem.ContentSystemUtils;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.MultiLineTextLabel;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IMultiLineTextLabelImplementation;

/**
 * The Class JMEMultiLineTextLabel.
 */
public class JMEMultiLineTextLabel extends JMETextLabel implements
		IMultiLineTextLabelImplementation {
	
	/** The item. */
	private MultiLineTextLabel item;

	/** The text descent. */
	private int textDescent;

	/** The text height. */
	private int textHeight;

	/** The text width. */
	private int textWidth;

	/**
	 * Instantiates a new JME multi line text label.
	 *
	 * @param contentItem
	 *            the content item
	 */
	public JMEMultiLineTextLabel(ContentItem contentItem) {
		super(contentItem);
		this.item = (MultiLineTextLabel) contentItem;
		
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMETextLabel#draw()
	 */
	@Override
	protected void draw() {
		
		gfx.setColor(item.getTextColour());
		gfx.setFont(item.getFont());
		int y = (item.getBorderSize() + textHeight) - textDescent;
		for (int i = 0; i < item.getLines().size(); i++) {
			gfx.drawString(item.getLines().get(i), item.getBorderSize(), y);
			y += textHeight;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMEFrame#init()
	 */
	@Override
	public void init() {
		super.init();
		resize();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMETextLabel#resize
	 * ()
	 */
	@Override
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

		int w = item.getWidth();
		int h = item.getHeight();

		if (item.isAutoFitSize()) {
			w = textWidth + item.getBorderSize() + item.getBorderSize()
					+ textDescent;
			h = (textHeight * item.getLines().size()) + item.getBorderSize()
					+ item.getBorderSize();
		}
		this.graphicsImageQuad.updateGeometry(w, h);
		
		if (w < 2) {
			w = 2;
		}
		if (h < 2) {
			h = 2;
		}

		this.graphicsImageQuad.recreateImageForSize(w, h);
		gfx = this.graphicsImageQuad.getImageGraphics();
		
		this.graphicsImageQuad.updateModelBound();

		item.setWidth(w);
		item.setHeight(h);

		this.render();
		
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .
	 * IMultiLineTextLabelImplementation#setCRLFSeparatedString(java.lang.String
	 * )
	 */
	@Override
	public void setCRLFSeparatedString(String s) {
		this.resize();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IMultiLineTextLabelImplementation#setLines(java.util.List)
	 */
	@Override
	public void setLines(List<String> lines) {
		this.resize();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IMultiLineTextLabelImplementation#setLines(java.lang.String, int)
	 */
	@Override
	public void setLines(String s, int charsPerLine) {
		this.resize();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMETextLabel#setText
	 * (java.lang.String)
	 */
	@Override
	public void setText(String s) {
		this.setCRLFSeparatedString(s);
	}

}
