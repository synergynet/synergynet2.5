/* Copyright (c) 2008 University of Durham, England
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

package synergynetframework.appsystem.contentsystem.items.implementation.interfaces;

import java.awt.Color;
import java.awt.Font;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.utils.Location;


/**
 * The Interface ILineImplementation.
 */
public interface ILineImplementation extends IOrthoContentItemImplementation{
	
	/**
	 * Sets the source location.
	 *
	 * @param sourceLocation the new source location
	 */
	public void setSourceLocation(Location sourceLocation);
	
	/**
	 * Sets the target location.
	 *
	 * @param targetPoint the new target location
	 */
	public void setTargetLocation(Location targetPoint);
	
	/**
	 * Sets the source item.
	 *
	 * @param sourceItem the new source item
	 */
	public void setSourceItem(ContentItem sourceItem);
	
	/**
	 * Sets the target item.
	 *
	 * @param targetItem the new target item
	 */
	public void setTargetItem(ContentItem targetItem);
	
	/**
	 * Sets the line colour.
	 *
	 * @param lineColour the new line colour
	 */
	public void setLineColour(Color lineColour);
	
	/**
	 * Sets the width.
	 *
	 * @param lineWidth the new width
	 */
	public void setWidth(float lineWidth);
	
	/**
	 * Sets the arrows enabled.
	 *
	 * @param isEnabled the new arrows enabled
	 */
	public void setArrowsEnabled(boolean isEnabled);
	
	/**
	 * Sets the arrow mode.
	 *
	 * @param arrowMode the new arrow mode
	 */
	public void setArrowMode(int arrowMode);
	
	/**
	 * Sets the line mode.
	 *
	 * @param lineMode the new line mode
	 */
	public void setLineMode(int lineMode);
	
	/**
	 * Sets the annotation enabled.
	 *
	 * @param isEnabled the new annotation enabled
	 */
	public void setAnnotationEnabled(boolean isEnabled);
	
	/**
	 * Sets the text.
	 *
	 * @param text the new text
	 */
	public void setText(String text);
	
	/**
	 * Sets the text colour.
	 *
	 * @param textColour the new text colour
	 */
	public void setTextColour(Color textColour);
	
	/**
	 * Sets the text font.
	 *
	 * @param textFont the new text font
	 */
	public void setTextFont(Font textFont);
}
