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

import java.awt.Color;
import java.io.Serializable;

/**
 * The Class Border.
 */
public class Border implements Serializable, Cloneable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7008375275087161863L;

	/** The border colour. */
	protected Color borderColour = Color.red;

	/** The border size. */
	protected int borderSize = 4;
	
	/**
	 * Instantiates a new border.
	 *
	 * @param borderColour
	 *            the border colour
	 * @param borderSize
	 *            the border size
	 */
	public Border(Color borderColour, int borderSize) {
		this.borderColour = borderColour;
		this.borderSize = borderSize;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		Border clonedItem = (Border) super.clone();
		clonedItem.borderColour = borderColour;
		clonedItem.borderSize = borderSize;
		return clonedItem;
	}

	/**
	 * Gets the border colour.
	 *
	 * @return the border colour
	 */
	public Color getBorderColour() {
		return borderColour;
	}

	/**
	 * Gets the border size.
	 *
	 * @return the border size
	 */
	public int getBorderSize() {
		return borderSize;
	}

	/**
	 * Sets the border colour.
	 *
	 * @param borderColour
	 *            the new border colour
	 */
	public void setBorderColour(Color borderColour) {
		this.borderColour = borderColour;
	}

	/**
	 * Sets the border size.
	 *
	 * @param borderSize
	 *            the new border size
	 */
	public void setBorderSize(int borderSize) {
		this.borderSize = borderSize;
	}

}
