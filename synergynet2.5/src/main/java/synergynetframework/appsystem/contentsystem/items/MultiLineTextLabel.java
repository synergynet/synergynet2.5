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
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.contentloader.utils.TextUtil;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IMultiLineTextLabelImplementation;

/**
 * The Class MultiLineTextLabel.
 */
public class MultiLineTextLabel extends TextLabel implements
		IMultiLineTextLabelImplementation, Serializable, Cloneable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7970788844569917633L;

	/** The lines. */
	protected List<String> lines = new ArrayList<String>();

	/**
	 * Instantiates a new multi line text label.
	 *
	 * @param contentSystem
	 *            the content system
	 * @param name
	 *            the name
	 */
	public MultiLineTextLabel(ContentSystem contentSystem, String name) {
		super(contentSystem, name);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.TextLabel#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		MultiLineTextLabel clonedItem = (MultiLineTextLabel) super.clone();
		clonedItem.lines = new ArrayList<String>();
		for (String s : this.lines) {
			clonedItem.lines.add(s);
		}
		return clonedItem;

	}

	/**
	 * Gets the first line.
	 *
	 * @return the first line
	 */
	public String getFirstLine() {
		if (lines.size() > 0) {
			return lines.get(0);
		}
		return "";
	}
	
	/**
	 * Gets the lines.
	 *
	 * @return the lines
	 */
	public List<String> getLines() {
		return lines;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .
	 * IMultiLineTextLabelImplementation#setCRLFSeparatedString(java.lang.String
	 * )
	 */
	public void setCRLFSeparatedString(String s) {
		this.text = s;
		lines.clear();
		StringTokenizer st = new StringTokenizer(s, "\n");
		while (st.hasMoreTokens()) {
			lines.add(st.nextToken());
		}
		if (s.endsWith("\n")) {
			lines.add("");
		}

		((IMultiLineTextLabelImplementation) this.contentItemImplementation)
				.setCRLFSeparatedString(s);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IMultiLineTextLabelImplementation#setLines(java.util.List)
	 */
	public void setLines(List<String> lines) {
		this.lines = lines;
		((IMultiLineTextLabelImplementation) this.contentItemImplementation)
				.setLines(lines);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IMultiLineTextLabelImplementation#setLines(java.lang.String, int)
	 */
	public void setLines(String s, int charsPerLine) {
		this.lines = TextUtil.wrapAt(s, charsPerLine);
		((IMultiLineTextLabelImplementation) this.contentItemImplementation)
				.setLines(s, charsPerLine);
	}
	
}
