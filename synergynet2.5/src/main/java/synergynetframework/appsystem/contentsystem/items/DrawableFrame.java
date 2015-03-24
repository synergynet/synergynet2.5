/*
 * Copyright (c) 2009 University of Durham, England All rights reserved.
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

import java.io.Serializable;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IDrawableFrameImplementation;
import synergynetframework.appsystem.contentsystem.items.utils.IDrawableContent;

/**
 * The Class DrawableFrame.
 */
public class DrawableFrame extends Frame implements Serializable, Cloneable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8212343252885792979L;

	/** The drawable content. */
	protected IDrawableContent drawableContent;

	/**
	 * Instantiates a new drawable frame.
	 *
	 * @param contentSystem
	 *            the content system
	 * @param name
	 *            the name
	 */
	public DrawableFrame(ContentSystem contentSystem, String name) {
		super(contentSystem, name);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.QuadContentItem#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		DrawableFrame clonedItem = (DrawableFrame) super.clone();
		return clonedItem;

	}

	/**
	 * Gets the drawable content.
	 *
	 * @return the drawable content
	 */
	public IDrawableContent getDrawableContent() {
		return drawableContent;
	}
	
	/**
	 * Sets the drawable content.
	 *
	 * @param drawableContent
	 *            the new drawable content
	 */
	public void setDrawableContent(IDrawableContent drawableContent) {
		this.drawableContent = drawableContent;
		((IDrawableFrameImplementation) this.contentItemImplementation)
				.setDrawableContent(drawableContent);
	}
}
