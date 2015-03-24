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

package apps.projectmanagement.component.workflowchart.core.graphcomponents.nodes;

import java.awt.Color;

import apps.projectmanagement.component.workflowchart.core.GraphManager;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.MultiLineTextLabel;


/**
 * The Class TextNode.
 */
public class TextNode extends EditableQuadNode{

	/** The mlt label. */
	protected MultiLineTextLabel mltLabel;
	
	/**
	 * Instantiates a new text node.
	 *
	 * @param contentSystem the content system
	 * @param gManager the g manager
	 */
	public TextNode(ContentSystem contentSystem, GraphManager gManager) {
		super(contentSystem, gManager);
		mltLabel = (MultiLineTextLabel) contentSystem.createContentItem(MultiLineTextLabel.class);
		mltLabel.setBackgroundColour(Color.black);
		mltLabel.setTextColour(Color.white);
		mltLabel.setAutoFitSize(false);
		mltLabel.setWidth(200);
		mltLabel.setHeight(60);
		mltLabel.setLocation(0, 0);
		super.setNodeContent(mltLabel);
	}

	/* (non-Javadoc)
	 * @see apps.projectmanagement.component.workflowchart.core.graphcomponents.nodes.EditableQuadNode#setText(java.lang.String)
	 */
	public void setText(String text){
		mltLabel.setCRLFSeparatedString(text);
		updateNode();
	}
	
	/* (non-Javadoc)
	 * @see apps.projectmanagement.component.workflowchart.core.graphcomponents.nodes.EditableQuadNode#getText()
	 */
	public String getText(){
		String text = "";
		for(String line: mltLabel.getLines())
			text+=line+"\n";
		if(text != null && !text.equals(""))
			text = text.substring(0, text.lastIndexOf("\n"));
		return text;
	}
	
	/**
	 * Gets the multi line text label.
	 *
	 * @return the multi line text label
	 */
	public MultiLineTextLabel getMultiLineTextLabel(){
		return mltLabel;
	}
}

