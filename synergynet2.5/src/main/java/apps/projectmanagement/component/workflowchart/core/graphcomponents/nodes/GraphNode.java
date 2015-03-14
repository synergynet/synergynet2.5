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

import java.awt.geom.Point2D;
import java.net.URL;
import java.util.ArrayList;

import apps.projectmanagement.GraphConfig;
import apps.projectmanagement.component.workflowchart.core.ConceptMapListener;
import apps.projectmanagement.component.workflowchart.core.GraphManager;
import apps.projectmanagement.component.workflowchart.core.MessageFactory;
import apps.projectmanagement.component.workflowchart.core.graphcomponents.GraphComponent;
import apps.projectmanagement.component.workflowchart.core.graphcomponents.OptionMessage;
import apps.projectmanagement.component.workflowchart.core.graphcomponents.links.GraphLink;

import com.jme.scene.Node;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.OrthoContainer;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.listener.ScreenCursorListener;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;
import synergynetframework.appsystem.contentsystem.items.utils.Location;

public abstract class GraphNode extends GraphComponent{

	public static final String TOP_LEFT_CORNER = "TOP_LEFT";
	public static final String TOP_RIGHT_CORNER = "TOP_RIGHT";
	public static final String BOTTOM_LEFT_CORNER = "BOTTOM_LEFT";
	public static final String BOTTOM_RIGHT_CORNER = "BOTTOM_RIGHT";
	public static final String MIDDLE = "MIDDLE";
	
	protected OrthoContainer container;
	protected ArrayList<GraphLink> incomingLinks = new ArrayList<GraphLink>();
	protected ArrayList<GraphLink> outgoingLinks = new ArrayList<GraphLink>();

	protected SimpleButton linkPoint, closePoint;
	protected String linkPointLocation;
	protected String closePointLocation;
	protected boolean isLinkable = true;
	
	protected transient ArrayList<ConceptMapListener> listeners = new ArrayList<ConceptMapListener>();
	
	private boolean isMsgVisible = false;
	
	public GraphNode(final ContentSystem contentSystem, final GraphManager gManager) {
		super(contentSystem, gManager);
		container = (OrthoContainer) contentSystem.createContentItem(OrthoContainer.class);
		container.setRotateTranslateScalable(true);
		linkPoint = this.createButtonWithImage(GraphConfig.nodeLinkImageResource);
		closePoint = this.createButtonWithImage(GraphConfig.nodeCloseImageResource);
		container.addSubItem(linkPoint);
		container.addSubItem(closePoint);
		linkPoint.setOrder(2);
		closePoint.setOrder(2);
		graphManager.addGraphNode(this);
		linkPoint.addScreenCursorListener(new ScreenCursorListener() {
			
			@Override
			public void screenCursorChanged(ContentItem item, long id, float x,
					float y, float pressure) {
				container.setRotateTranslateScalable(false);
				graphManager.linkPointDragged(GraphNode.this, id, x, y);
				container.setRotateTranslateScalable(true);
			}

			@Override
			public void screenCursorClicked(ContentItem item, long id, float x,
					float y, float pressure) {
			}

			@Override
			public void screenCursorPressed(ContentItem item, long id, float x,
					float y, float pressure) {
				container.setRotateTranslateScalable(false);
				graphManager.linkPointPressed(GraphNode.this, id, x, y);
				container.setRotateTranslateScalable(true);
			}

			@Override
			public void screenCursorReleased(ContentItem item, long id, float x,
					float y, float pressure) {
				graphManager.linkPointReleased(GraphNode.this, id, x, y);
			}
		});
		closePoint.addButtonListener(new SimpleButtonAdapter() {
			
			public void buttonReleased(SimpleButton b, long id, float x,	float y, float pressure) {
				if(!isMsgVisible){
					isMsgVisible = true;
					OptionMessage msg = MessageFactory.getInstance().createOptionMessage(contentSystem, gManager,GraphNode.this, "Are you sure you want to delete this node?", MessageFactory.OK_CANCEL_MESSAGE);
					msg.setLocation(GraphNode.this.getLocation().x, GraphNode.this.getLocation().y);
					msg.setOrder(GraphNode.this.getOrder()+1);
					GraphNode.this.addOptionMessageListener(new OptionMessageListener(){

					@Override
						public void messageProcessed(OptionMessage msg) {
							if(msg.getParentComponent().getName().equals(GraphNode.this.getName())){
								if(msg.getSelectedOption() == 0){
									msg.remove();
									graphManager.detachGraphNode(msg);
									GraphNode.this.remove();
									graphManager.detachGraphNode(GraphNode.this);
									//if(keyboardNode != null){
									//	keyboardNode.remove();
									//	graphManager.detachGraphNode(keyboardNode);
									//}
								}
								else if(msg.getSelectedOption() == 1){
									msg.remove();
									graphManager.detachGraphNode(msg);
								}
								isMsgVisible = false;
							}
						}
				
					});
				}
			}
		});
	}

	public void addConceptMapListener(ConceptMapListener listener){
		if(!listeners.contains(listener)) listeners.add(listener);
	}
	
	public void fireNodeConnected(GraphLink link){
		for(ConceptMapListener listener: listeners){
			listener.nodeConnected(link);
		}
	}
	
	public void fireNodeDisconnected(GraphLink link){
		for(ConceptMapListener listener: listeners){
			listener.nodeDisconnected(link);
		}
	}
	
	public void removeConceptMapListeners(){
		listeners.clear();
	}
	
	protected abstract void setNodeContent(ContentItem nodeItem);
	
	public abstract void setLinkButtonLocation(String location);
	
	protected SimpleButton createButtonWithImage(URL imageResource){
		SimpleButton btn = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		btn.setBackgroundColour(GraphConfig.nodePointBgColor);
		btn.setAutoFitSize(false);
		btn.setWidth(GraphConfig.nodePointSize);
		btn.setHeight(GraphConfig.nodePointSize);
		btn.setBorderSize(GraphConfig.nodePointBorderSize);
		btn.setBorderColour(GraphConfig.nodePointBorderColor);
		btn.drawImage(imageResource, 0, 0, btn.getWidth(), btn.getHeight());
		btn.setRotateTranslateScalable(false);
		return btn;
	}
	
	public abstract void setCloseButtonLocation(String location);
	
	public void updateConnectionPoints(){
		for(GraphLink link: incomingLinks){
			link.setTargetLocation(this.getLocation()/*this.getLinkPointLocation()*/);
		}
		for(GraphLink link: outgoingLinks){
			link.setSourceLocation(this.getLocation()/*this.getLinkPointLocation()*/);
		}
	}
	
	@Override
	public String getName() {
		return container.getName();
	}

	public void setLocation(float x, float y){
		container.setLocalLocation(x, y);
		updateConnectionPoints();
	}
	
	public Location getLocation(){
		return container.getLocalLocation();
	}
	
	public void registerOutgoingLink(GraphLink link){
		outgoingLinks.add(link);
	}
	
	public void registerIncomingLink(GraphLink link){
		incomingLinks.add(link);
	}
	
	public ArrayList<GraphLink> getOutgoingLinks(){
		return outgoingLinks;
	}
	
	public ArrayList<GraphLink> getIncomingLinks(){
		return incomingLinks;
	}
	
	public void unregisterAllLinks(){
		outgoingLinks.clear();
		incomingLinks.clear();
	}
	
	public boolean unregisterLink(GraphLink link){
		boolean removed = false;
		removed = outgoingLinks.remove(link);
		removed = incomingLinks.remove(link);
		return removed;
	}
	
	public OrthoContainer getNodeContainer(){
		return container;
	}
	
	public boolean contains(Point2D.Float point){
		return container.contains(point);
	}
	
	public void setOrder(int zOrder){
		container.setOrder(zOrder);
	}
	
	public int getOrder(){
		return container.getOrder();
	}
	
	public void setScale(float scaleFactor){
		container.setScale(scaleFactor);
	}
	
	public void setVisible(boolean isVisible){
		container.setVisible(isVisible);
	}
	
	public boolean isVisible(){
		return container.isVisible();
	}
	
	public void remove(){
		contentSystem.removeContentItem(container);
	}
	
	public void setLinkable(boolean isLinkable){
		this.isLinkable = isLinkable;
	}
	
	public boolean isLinkable(){
		return isLinkable;
	}
	
	public SimpleButton getCloseButton(){
		return this.closePoint;
	}
	
	public SimpleButton getLinkButton(){
		return this.linkPoint;
	}
	
	public void updateNode(){
		if(linkPointLocation != null)
			this.setLinkButtonLocation(this.linkPointLocation);
		if(closePointLocation != null)
			this.setCloseButtonLocation(this.closePointLocation);
		this.updateConnectionPoints();
	}
	
	public void setEditable(boolean b){
		
		((Node)container.getImplementationObject()).setIsCollidable(b);
	}
}
