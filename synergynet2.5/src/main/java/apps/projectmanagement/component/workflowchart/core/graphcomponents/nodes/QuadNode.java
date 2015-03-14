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

import apps.projectmanagement.component.workflowchart.core.GraphManager;
import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.QuadContentItem;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.listener.ScreenCursorListener;
import synergynetframework.appsystem.contentsystem.items.utils.Location;

public abstract class QuadNode extends GraphNode{

	protected QuadContentItem contentItem;
	
	public QuadNode(ContentSystem contentSystem, GraphManager gManager) {
		super(contentSystem, gManager);
	}

	protected void setNodeContent(ContentItem nodeItem){
		this.contentItem = (QuadContentItem)nodeItem;
		container.addSubItem(contentItem);
		if(contentItem != null){
			contentItem.addScreenCursorListener(new ScreenCursorListener(){

				@Override
				public void screenCursorChanged(ContentItem item, long id,
						float x, float y, float pressure) {
					graphManager.graphNodeDragged(QuadNode.this, id, x, y);
				}

				@Override
				public void screenCursorClicked(ContentItem item, long id,
						float x, float y, float pressure) {
					graphManager.graphNodeClicked(QuadNode.this, id, x, y);
				}

				@Override
				public void screenCursorPressed(ContentItem item, long id,
						float x, float y, float pressure) {
					graphManager.graphNodePressed(QuadNode.this, id, x, y);

				}

				@Override
				public void screenCursorReleased(ContentItem item, long id,
						float x, float y, float pressure) {
					graphManager.graphNodeReleased(QuadNode.this, id, x, y);
				}
			});
		}
	}
	
	@Override
	public void setLinkButtonLocation(String location) {
		if(contentItem != null){
			linkPointLocation = location;
			positionButton(linkPoint, contentItem, linkPointLocation);
		}
	}

	@Override
	public void setCloseButtonLocation(String location) {
		if(contentItem != null){
			closePointLocation = location;
			positionButton(closePoint, contentItem, closePointLocation);
		}	
	}
	
	protected void positionButton(SimpleButton button, QuadContentItem contentItem, String location){
		if(location.equals(GraphNode.TOP_RIGHT_CORNER)){
			Location newLocation = new Location(contentItem.getWidth()/2, contentItem.getHeight()/2,0);
			newLocation.x-= button.getWidth()/2;
			newLocation.y-= button.getHeight()/2;				
			button.setLocalLocation(newLocation.x, newLocation.y);
		}
		else if(location.equals(GraphNode.TOP_LEFT_CORNER)){
			Location newLocation = new Location(-contentItem.getWidth()/2, contentItem.getHeight()/2,0);
			newLocation.x+= button.getWidth()/2;
			newLocation.y-= button.getHeight()/2;
			button.setLocalLocation(newLocation.x, newLocation.y);				
		}
		else if(location.equals(GraphNode.BOTTOM_RIGHT_CORNER)){
			Location newLocation = new Location(contentItem.getWidth()/2, -contentItem.getHeight()/2, 0);
			newLocation.x-= button.getWidth()/2;
			newLocation.y+= button.getHeight()/2;
			button.setLocalLocation(newLocation.x, newLocation.y);				
		}
		else if(location.equals(GraphNode.BOTTOM_LEFT_CORNER)){
			Location newLocation = new Location(-contentItem.getWidth()/2, -contentItem.getHeight()/2, 0);
			newLocation.x+= button.getWidth()/2;
			newLocation.y+= button.getHeight()/2;
			button.setLocalLocation(newLocation.x, newLocation.y);
		}
		else if(location.equals(GraphNode.MIDDLE)){
			button.setLocalLocation(contentItem.getLocalLocation());
		}
	}

	public QuadContentItem getNodeContent(){
		return contentItem;
	}
}
