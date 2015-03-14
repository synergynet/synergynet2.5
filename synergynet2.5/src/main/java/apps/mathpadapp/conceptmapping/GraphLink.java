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

package apps.mathpadapp.conceptmapping;

import apps.conceptmap.GraphConfig;
import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.LineItem;


public class GraphLink{
	
	protected ContentSystem contentSystem;
	protected GraphManager graphManager;
	protected LineItem line;
	protected GraphNode sourceNode;
	protected GraphNode targetNode;
	private boolean menuEnabled = true;
	
	public GraphLink(final ContentSystem contentSystem, final GraphManager graphManager){
		this.contentSystem = contentSystem;
		this.graphManager = graphManager;
		line = (LineItem) contentSystem.createContentItem(LineItem.class);
		line.setRotateTranslateScalable(false);
		line.setBringToTopable(false);
		line.setWidth(GraphConfig.linkWidth);
		line.setLineColour(GraphConfig.linkColor);
		line.setTextColour(GraphConfig.linkTextColor);
		line.setTextFont(GraphConfig.linkTextFont);
		graphManager.addGraphLink(this);
	}
	
	public String getName(){
		return line.getName();
	}
	
	public LineItem getLineItem(){
		return line;
	}
	
	public void setSourceNode(GraphNode sourceNode){
		this.sourceNode = sourceNode;
		if(sourceNode != null){
			line.setSourceItem(sourceNode.getNodeItem());
			line.setSourceLocation(sourceNode.getLocation()/*sourceNode.getLinkPointLocation()*/);
			sourceNode.registerOutgoingLink(this);
		}
	}
	
	public void setTargetNode(GraphNode targetNode){
		this.targetNode = targetNode;
		if(targetNode != null){
			line.setTargetItem(targetNode.getNodeItem());
			line.setTargetLocation(targetNode.getLocation()/*targetNode.getLinkPointLocation()*/);
			targetNode.registerIncomingLink(this);
		}
	}
	
	public GraphNode getSourceNode(){
		return sourceNode;
	}
	
	public GraphNode getTargetNode(){
		return targetNode;
	}

	public void remove() {
		contentSystem.removeContentItem(line);
	}
	
	public int getLinkMode(){
		return line.getLineMode();
	}
	
	public void setLinkMode(int lineMode){
		line.setLineMode(lineMode);
	}
	
	public void setArrowMode(int arrowMode){
		line.setArrowMode(arrowMode);
	}
	
	public int getArrowMode(){
		return line.getArrowMode();
	}
	
	public void setText(String text){
		line.setText(text);
	}
	
	public String getText(){
		return line.getText();
	}
	
	public void setWidth(float lineWidth){
		line.setWidth(lineWidth);
	}
	
	public float getWidth(){
		return line.getWidth();
	}
	
	
	
	public void setMenuEnabled(boolean isEnabled){
		this.menuEnabled = isEnabled;
	}
	
	public boolean isMenuEnabled(){
		return menuEnabled;
	}
	
	public int getOrder(){
		return line.getOrder();
	}
}
