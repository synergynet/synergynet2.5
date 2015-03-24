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

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;



/**
 * The Interface IHtmlFrameImplementation.
 */
public interface IHtmlFrameImplementation extends IFrameImplementation {
	
	/**
	 * Sets the html content.
	 *
	 * @param html the new html content
	 */
	public void setHtmlContent(String html);
	
	/**
	 * Sets the max width.
	 *
	 * @param maxWidth the new max width
	 */
	public void setMaxWidth(int maxWidth);
	
	/**
	 * Gets the pane.
	 *
	 * @return the pane
	 */
	public JTextPane getPane();
	
	/**
	 * Insert string.
	 *
	 * @param offset the offset
	 * @param str the str
	 * @param attr the attr
	 */
	public void insertString(int offset, String str, AttributeSet attr);
	
	/**
	 * Removes the.
	 *
	 * @param offset the offset
	 * @param length the length
	 */
	public void remove(int offset, int length);
}
