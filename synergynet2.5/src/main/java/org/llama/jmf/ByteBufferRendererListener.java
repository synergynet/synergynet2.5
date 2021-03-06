/******************************************************************************
 * JMF/FOBS/jME renderer. Copyright (c) 2006 Tijl Houtbeckers Coded by Tijl
 * Houtbeckers This file is part of the JMF/FOBS/jME renderer. The JMF/FOBS/jME
 * renderer is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version. The JMF/FOBS/jME renderer is distributed in the hope that
 * it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details. You should have received a
 * copy of the GNU Lesser General Public License along with FOBS; if not, write
 * to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 ******************************************************************************/

/**
 * @author Tijl Houtbeckers Initial release: 23-02-2006 jME: jmonkeyengine.com
 *         FOBS: http://fobs.sourceforge.net/ JMF:
 *         http://java.sun.com/products/java-media/jmf/
 */

package org.llama.jmf;

import java.nio.ByteBuffer;

import javax.media.format.RGBFormat;

/**
 * The listener interface for receiving byteBufferRenderer events. The class
 * that is interested in processing a byteBufferRenderer event implements this
 * interface, and the object created with that class is registered with a
 * component using the component's
 * <code>addByteBufferRendererListener<code> method. When
 * the byteBufferRenderer event occurs, that object's appropriate
 * method is invoked.
 *
 * @see ByteBufferRendererEvent
 */
public interface ByteBufferRendererListener {

	/**
	 * Frame.
	 *
	 * @param buffer
	 *            the buffer
	 */
	public void frame(ByteBuffer buffer);

	/**
	 * Sets the size.
	 *
	 * @param videowidth
	 *            the videowidth
	 * @param videoheight
	 *            the videoheight
	 * @param format
	 *            the format
	 */
	public void setSize(int videowidth, int videoheight, RGBFormat format);
	
}
