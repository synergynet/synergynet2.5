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

package synergynetframework.appsystem.contentsystem.items;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.contentloader.utils.TextUtil;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IRoundTextLabelImplementation;


/**
 * The Class RoundTextLabel.
 */
public class RoundTextLabel extends RoundFrame implements IRoundTextLabelImplementation{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5182039460663205696L;
	
	/** The text. */
	protected String text;
	
	/** The font. */
	protected Font font = new Font("Arial", Font.PLAIN, 16);
	
	/** The text colour. */
	protected Color textColour = Color.white;
	
	/** The lines. */
	protected List<String> lines = new ArrayList<String>();

	/**
	 * Instantiates a new round text label.
	 *
	 * @param contentSystem the content system
	 * @param name the name
	 */
	public RoundTextLabel(ContentSystem contentSystem, String name) {
		super(contentSystem, name);
	}	
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IRoundTextLabelImplementation#setText(java.lang.String)
	 */
	public void setText(String text) {
		this.text = text;		
		((IRoundTextLabelImplementation)this.contentItemImplementation).setText(text);
	}
	
	/**
	 * Gets the text.
	 *
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IRoundTextLabelImplementation#setFont(java.awt.Font)
	 */
	public void setFont(Font font) {
		this.font = font;
		((IRoundTextLabelImplementation)this.contentItemImplementation).setFont(font);
	}
	
	/**
	 * Gets the font.
	 *
	 * @return the font
	 */
	public Font getFont() {
		return font;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IRoundTextLabelImplementation#setTextColour(java.awt.Color)
	 */
	public void setTextColour(Color textColour) {
		this.textColour = textColour;
		((IRoundTextLabelImplementation)this.contentItemImplementation).setTextColour(textColour);
	}
	
	/**
	 * Gets the text colour.
	 *
	 * @return the text colour
	 */
	public Color getTextColour() {
		return textColour;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IRoundTextLabelImplementation#setAutoFitSize(boolean)
	 */
	public void setAutoFitSize(boolean isEnabled){
		autoFit = isEnabled;
		((IRoundTextLabelImplementation)this.contentItemImplementation).setAutoFitSize(isEnabled);
	}
	
	/**
	 * Checks if is auto fit size.
	 *
	 * @return true, if is auto fit size
	 */
	public boolean isAutoFitSize(){
		return autoFit;
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IRoundTextLabelImplementation#setLines(java.util.List)
	 */
	public void setLines(List<String> lines) {
		this.lines = lines;
		((IRoundTextLabelImplementation)this.contentItemImplementation).setLines(lines);
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IRoundTextLabelImplementation#setLines(java.lang.String, int)
	 */
	public void setLines(String s, int charsPerLine){
		this.lines = TextUtil.wrapAt(s, charsPerLine);
		((IRoundTextLabelImplementation)this.contentItemImplementation).setLines(s, charsPerLine);
	}
		
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IRoundTextLabelImplementation#setCRLFSeparatedString(java.lang.String)
	 */
	public void setCRLFSeparatedString(String s) {
		lines.clear();
		StringTokenizer st = new StringTokenizer(s, "\n");
		while(st.hasMoreTokens()) {
			lines.add(st.nextToken());
		}
		if(s.endsWith("\n"))
			lines.add("");
		
		((IRoundTextLabelImplementation)this.contentItemImplementation).setCRLFSeparatedString(s);
	}
	
	/**
	 * Gets the lines.
	 *
	 * @return the lines
	 */
	public List<String> getLines() {
		return lines;
	}
	
	/**
	 * Gets the first line.
	 *
	 * @return the first line
	 */
	public String getFirstLine() {
		if(lines.size() > 0) return lines.get(0);
		return "";
	}

}
