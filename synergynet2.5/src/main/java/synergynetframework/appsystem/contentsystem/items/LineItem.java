/*
 * Copyright (c) 2009 University of Durham, England
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

package synergynetframework.appsystem.contentsystem.items;

import java.awt.Color;
import java.awt.Font;
import java.io.Serializable;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.ILineImplementation;
import synergynetframework.appsystem.contentsystem.items.utils.Location;

public class LineItem extends OrthoContainer implements Serializable, ILineImplementation{

	private static final long serialVersionUID = 1L;



	public static final int SEGMENT_LINE = 0;	
	public static final int CONNECTED_LINE = 1;
	public static final int DOTTED_LINE = 2;
	public static final int ANIMATION = 3;
	
	public static final int BIDIRECTIONAL_ARROWS = 0;
	public static final int ARROW_TO_TARGET = 1;
	public static final int ARROW_TO_SOURCE = 2;
	public static final int NO_ARROWS = 3;
	
	protected boolean arrowsEnabled = true;
	protected int lineMode = CONNECTED_LINE;
	protected int arrowMode = BIDIRECTIONAL_ARROWS;
	
	protected Location sourceLocation, targetLocation;
	protected ContentItem sourceItem, targetItem;
	
	protected Color lineColour = Color.white;
	protected float lineWidth = 1f;
	
	protected boolean textEnabled = true;
	protected StringBuffer text = new StringBuffer();
	protected Color textColour = Color.white;
	protected Font textFont = new Font("Arial", Font.PLAIN, 16);
	
	public LineItem(ContentSystem contentSystem, String name) {
		super(contentSystem, name);
	}
	
	public LineItem(ContentSystem contentSystem, String name, Location sourceLocation, Location targetLocation) {
		super(contentSystem, name);
		this.sourceLocation = sourceLocation;
		this.targetLocation = targetLocation;
	}
	
	public void setSourceLocation(Location sourceLocation){
		this.sourceLocation = sourceLocation;
		((ILineImplementation)this.contentItemImplementation).setSourceLocation(sourceLocation);
	}
	
	public Location getSourceLocation(){
		return sourceLocation;
	}

	public void setTargetLocation(Location targetLocation){
		this.targetLocation = targetLocation;
		((ILineImplementation)this.contentItemImplementation).setTargetLocation(targetLocation);
	}
	
	public Location getTargetLocation(){
		return targetLocation;
	}
	
	public void setSourceItem(ContentItem sourceItem){
		this.sourceItem = sourceItem;
		((ILineImplementation)this.contentItemImplementation).setSourceItem(sourceItem);
	}
	
	public ContentItem getSourceItem(){
		return sourceItem;
	}
	
	public void setTargetItem(ContentItem targetItem){
		this.targetItem = targetItem;
		((ILineImplementation)this.contentItemImplementation).setTargetItem(targetItem);
	}

	public ContentItem getTargetItem(){
		return targetItem;
	}
	
	public void setLineColour(Color lineColour){
		this.lineColour = lineColour;
		((ILineImplementation)this.contentItemImplementation).setLineColour(lineColour);
	}
	
	public Color getLineColour(){
		return lineColour;
	}
	
	public void setWidth(float lineWidth){
		this.lineWidth = lineWidth;
		((ILineImplementation)this.contentItemImplementation).setWidth(lineWidth);
	}
	
	public float getWidth(){
		return lineWidth;
	}
	
	public void setArrowsEnabled(boolean isEnabled){
		this.arrowsEnabled = isEnabled;
		((ILineImplementation)this.contentItemImplementation).setArrowsEnabled(isEnabled);
	}
	
	public boolean isArrowEnabled(){
		return arrowsEnabled;
	}
	
	public void setArrowMode(int arrowMode){
		this.arrowMode = arrowMode;
		((ILineImplementation)this.contentItemImplementation).setArrowMode(arrowMode);
	}
	
	public int getArrowMode(){
		return arrowMode;
	}
	
	public void setLineMode(int lineMode){
		this.lineMode = lineMode;
		((ILineImplementation)this.contentItemImplementation).setLineMode(lineMode);
	}
	
	public int getLineMode(){
		return lineMode;
	}
	
	public void setAnnotationEnabled(boolean isEnabled){
		this.textEnabled = isEnabled;
		((ILineImplementation)this.contentItemImplementation).setAnnotationEnabled(isEnabled);
	}
	
	public boolean isAnnotationEnabled(){
		return textEnabled;
	}
	
	public void setText(String text){
		this.text.setLength(0);
		this.text.append(text);
		((ILineImplementation)this.contentItemImplementation).setText(text);
	}
	
	public String getText(){
		return text.toString();
	}
	
	public void setTextColour(Color textColour){
		this.textColour = textColour;
		((ILineImplementation)this.contentItemImplementation).setTextColour(textColour);
	}
	
	public Color getTextColour(){
		return textColour;
	}
	
	public void setTextFont(Font textFont){
		this.textFont = textFont;
		((ILineImplementation)this.contentItemImplementation).setTextFont(textFont);
	}
	
	public Font getTextFont(){
		return textFont;
	}
}
