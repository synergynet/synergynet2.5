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

import java.io.Serializable;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;


import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IHtmlFrameImplementation;


public class HtmlFrame extends Frame implements IHtmlFrameImplementation, Serializable, Cloneable{
	
	private String html;
	private int maxWidth = 750;
	private static final long serialVersionUID = -8279443252885792111L;

	public HtmlFrame(ContentSystem contentSystem, String name) {
		super(contentSystem, name);
	}

	@Override
	public void setHtmlContent(String html) {
		this.html = html;
		((IHtmlFrameImplementation)this.contentItemImplementation).setHtmlContent(html);
	}
	
	public String getHtmlContent(){
		return html;
	}

	@Override
	public void setMaxWidth(int maxWidth) {
		this.maxWidth = maxWidth;
		((IHtmlFrameImplementation)this.contentItemImplementation).setMaxWidth(maxWidth);
	}
	
	public int getMaxWidth(){
		return maxWidth;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		HtmlFrame clonedItem = (HtmlFrame)super.clone();
		clonedItem.html = html;
		clonedItem.maxWidth = maxWidth;
		return clonedItem;
	}

	@Override
	public JTextPane getPane() {
		return ((IHtmlFrameImplementation)this.contentItemImplementation).getPane();
	}

	@Override
	public void insertString(int offset, String str, AttributeSet attr) {
		((IHtmlFrameImplementation)this.contentItemImplementation).insertString(offset, str, attr);
	}

	@Override
	public void remove(int offset, int length) {
		((IHtmlFrameImplementation)this.contentItemImplementation).remove(offset, length);
	}
}