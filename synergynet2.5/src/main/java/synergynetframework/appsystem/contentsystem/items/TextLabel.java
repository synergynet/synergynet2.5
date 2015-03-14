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
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.ITextLabelImplementation;

public class TextLabel extends Frame implements Serializable, Cloneable{
	private static final long serialVersionUID = -8279443252885792979L;

	protected String text ="";
	protected Font font = new Font("Arial", Font.PLAIN, 16);
	protected Color textColour = Color.black;
	public enum Alignment{LEFT, RIGHT, CENTER};
	protected Alignment textAlignment = Alignment.CENTER;
	
	public TextLabel(ContentSystem contentSystem, String name) {
		super(contentSystem, name);
	}		
	
	public void setText(String text) {
		this.text = text;		
		((ITextLabelImplementation)this.contentItemImplementation).setText(text);
	}
	
	public String getText() {
		return text;
	}

	public void setAlignment(Alignment alignment){
		textAlignment = alignment;
		((ITextLabelImplementation)this.contentItemImplementation).setAlignment(textAlignment);

	}
	

	public Alignment getAlignment() {
		return textAlignment;
	}
	
	public void setFont(Font font) {
		this.font = font;
		((ITextLabelImplementation)this.contentItemImplementation).setFont(font);
	}
	
	public Font getFont() {
		return font;
	}

	public void setTextColour(Color textColour) {
		this.textColour = textColour;
		((ITextLabelImplementation)this.contentItemImplementation).setTextColour(textColour);
	}
	
	public Color getTextColour() {
		return textColour;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		TextLabel clonedItem = (TextLabel)super.clone();
		clonedItem.text = text;
		clonedItem.font = font;
		clonedItem.textColour = textColour;
		return clonedItem;
		
	}
}
