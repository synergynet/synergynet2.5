/*
 * Copyright (c) 2008 University of Durham, England
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

package synergynetframework.jme.gfx.twod;

import com.jme.scene.Spatial;


/**
 * The Interface DrawableSpatialImage.
 */
public interface DrawableSpatialImage {
	
	/**
	 * Draw.
	 */
	public abstract void draw();
	
	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public abstract float getWidth();
	
	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public abstract float getHeight();
	
	/**
	 * Gets the image width.
	 *
	 * @return the image width
	 */
	public abstract int getImageWidth();
	
	/**
	 * Gets the image height.
	 *
	 * @return the image height
	 */
	public abstract int getImageHeight();
	
	/**
	 * Gets the spatial.
	 *
	 * @return the spatial
	 */
	public abstract Spatial getSpatial();
	
	/**
	 * Cursor pressed.
	 *
	 * @param cursorID the cursor id
	 * @param x the x
	 * @param y the y
	 */
	public abstract void cursorPressed(long cursorID, int x, int y);
	
	/**
	 * Cursor released.
	 *
	 * @param cursorID the cursor id
	 * @param x the x
	 * @param y the y
	 */
	public abstract void cursorReleased(long cursorID, int x, int y);
	
	/**
	 * Cursor clicked.
	 *
	 * @param cursorID the cursor id
	 * @param x the x
	 * @param y the y
	 */
	public abstract void cursorClicked(long cursorID, int x, int y);
	
	/**
	 * Cursor dragged.
	 *
	 * @param id the id
	 * @param x the x
	 * @param y the y
	 */
	public abstract void cursorDragged(long id, int x, int y);
}
