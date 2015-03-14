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

package apps.conceptmap.graphcomponents.link;

import java.awt.geom.Point2D;

import apps.conceptmap.GraphConfig;
import apps.conceptmap.graphcomponents.GraphComponent;
import apps.conceptmap.graphcomponents.nodes.GraphNode;
import apps.conceptmap.graphcomponents.nodes.KeyboardNode;
import apps.conceptmap.graphcomponents.nodes.TextNode;
import apps.conceptmap.utility.GraphManager;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.LineItem;
import synergynetframework.appsystem.contentsystem.items.listener.ItemListener;
import synergynetframework.appsystem.contentsystem.items.utils.Location;

public class GraphLink extends GraphComponent{
	
	protected LineItem line;
	protected GraphNode sourceNode;
	protected GraphNode targetNode;
	private LinkMenu menu;
	private double positionRatio;
	private TextNode dummyNode;
	private boolean menuEnabled = true;
	private KeyboardNode keyboardNode;
	
	public GraphLink(final ContentSystem contentSystem, final GraphManager gManager){
		super(contentSystem, gManager);
		line = (LineItem) contentSystem.createContentItem(LineItem.class);
		line.setRotateTranslateScalable(false);
		line.setBringToTopable(false);
		line.setWidth(GraphConfig.linkWidth);
		line.setLineColour(GraphConfig.linkColor);
		line.setTextColour(GraphConfig.linkTextColor);
		line.setTextFont(GraphConfig.linkTextFont);
		line.addItemListener(new ItemListener(){

			@Override
			public void cursorChanged(ContentItem item, long id, float x, float y, float pressure) {}
			@Override
			public void cursorClicked(ContentItem item, long id, float x, float y, float pressure) {}
			@Override
			public void cursorPressed(ContentItem item, long id, float x, float y, float pressure) {}
			@Override
			public void cursorReleased(ContentItem item, long id, float x,
					float y, float pressure) {
				if(isMenuEnabled()){
					if(menu == null)	
						menu = new LinkMenu(contentSystem, gManager, GraphLink.this);
					menu.setLocation(x, y);
					menu.setVisible(true);
					Point2D.Float position = new Point2D.Float(x,y);
					Point2D.Float sourcePos = new Point2D.Float(GraphLink.this.getSourceLocation().x, GraphLink.this.getSourceLocation().y);
					Point2D.Float targetPos = new Point2D.Float(GraphLink.this.getTargetLocation().x, GraphLink.this.getTargetLocation().y);
					positionRatio = sourcePos.distance(position)/ sourcePos.distance(targetPos);
				}
			}
			@Override
			public void cursorRightClicked(ContentItem item, long id, float x, float y, float pressure) {}
			@Override
			public void cursorLongHeld(ContentItem item, long id, float x, float y, float pressure) {}
			@Override
			public void cursorDoubleClicked(ContentItem item, long id, float x,	float y, float pressure) {}
			
		});
		graphManager.addGraphLink(this);
	}
	
	public String getName(){
		return line.getName();
	}
	
	public LineItem getLineItem(){
		return line;
	}
	
	public void setSourceLocation(Location sourceLocation){
		line.setSourceLocation(sourceLocation);
		this.updateLinkPoint();
		if(menu != null) positionMenu(line.getSourceLocation(), line.getTargetLocation());
	}
	
	public Location getSourceLocation(){
		return line.getSourceLocation();
	}

	public void setTargetLocation(Location targetLocation){
		line.setTargetLocation(targetLocation);
		this.updateLinkPoint();
		if(menu != null) positionMenu(line.getSourceLocation(), line.getTargetLocation());
	}
	
	public Location getTargetLocation(){
		return line.getTargetLocation();
	}
	
	public void setSourceNode(GraphNode sourceNode){
		this.sourceNode = sourceNode;
		if(sourceNode != null){
			line.setSourceItem(sourceNode.getNodeContainer());
			line.setSourceLocation(sourceNode.getLocation()/*sourceNode.getLinkPointLocation()*/);
			sourceNode.registerOutgoingLink(this);
			this.updateLinkPoint();
			if(menu != null) positionMenu(line.getSourceLocation(), line.getTargetLocation());
		}
	}
	
	public void setTargetNode(GraphNode targetNode){
		this.targetNode = targetNode;
		if(targetNode != null){
			line.setTargetItem(targetNode.getNodeContainer());
			line.setTargetLocation(targetNode.getLocation()/*targetNode.getLinkPointLocation()*/);
			targetNode.registerIncomingLink(this);
			this.updateLinkPoint();
			if(menu != null) positionMenu(line.getSourceLocation(), line.getTargetLocation());
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
		if(menu != null) menu.remove();
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
	
	private void positionMenu(Location sourceLocation, Location targetLocation){
		Point2D.Float sourcePoint = new Point2D.Float(sourceLocation.x, sourceLocation.y);
		Point2D.Float targetPoint = new Point2D.Float(targetLocation.x, targetLocation.y);
		double distanceSourceToTarget = sourcePoint.distance(targetPoint);
		double distanceSourceToMenu = distanceSourceToTarget * positionRatio;
		double cosAngle = (targetPoint.getX()-sourcePoint.getX())/distanceSourceToTarget;
		double sinAngle = (targetPoint.getY()-sourcePoint.getY())/distanceSourceToTarget;
		double xm = (cosAngle * distanceSourceToMenu) + sourcePoint.getX();
		double ym = (sinAngle * distanceSourceToMenu) + sourcePoint.getY();		
		menu.setLocation((float)xm, (float)ym);
	}
	
	
	private GraphNode createLinkPoint(){
		if(dummyNode == null){
			dummyNode = new TextNode(contentSystem, graphManager);
			dummyNode.setText("");
			dummyNode.setVisible(false);
			dummyNode.setLinkButtonLocation(GraphNode.MIDDLE);
			updateLinkPoint();
		}
		return dummyNode;
	}
	
	private void updateLinkPoint(){
		if(dummyNode != null && sourceNode != null && targetNode != null){
			Point2D.Float lineCenter = new Point2D.Float();
			lineCenter.x = (line.getTargetLocation().x - line.getSourceLocation().x)/2 + line.getSourceLocation().x;
			lineCenter.y = (line.getTargetLocation().y - line.getSourceLocation().y)/2 + line.getSourceLocation().y;
			dummyNode.setLocation(lineCenter.x, lineCenter.y);
			dummyNode.updateConnectionPoints();
		}
	}
	
	public GraphNode getLinkPoint(){
		return createLinkPoint();
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

	public void removeMenu() {
		if(menu != null){
			menu.remove();
			menu = null;
		}
	}
	
	public void setKeyboardNode(KeyboardNode keyboardNode){
		this.keyboardNode = keyboardNode;
	}
	
	public KeyboardNode getKeyboardNode(){
		return keyboardNode;
	}
}
