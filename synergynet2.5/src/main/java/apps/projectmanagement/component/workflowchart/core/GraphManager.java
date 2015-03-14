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

package apps.projectmanagement.component.workflowchart.core;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

import apps.projectmanagement.component.workflowchart.core.graphcomponents.links.GraphLink;
import apps.projectmanagement.component.workflowchart.core.graphcomponents.nodes.GraphNode;
import apps.projectmanagement.registry.WorkflowRegistry;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.utils.Location;

public class GraphManager{
	
	protected ContentSystem contentSystem;

	protected ArrayList<GraphNode> graphNodes = new ArrayList<GraphNode>();
	protected ArrayList<GraphLink> graphLinks = new ArrayList<GraphLink>();
	
	private HashMap<Long, GraphLink> cursorIdToLink = new HashMap<Long, GraphLink>();
	
	public GraphManager(ContentSystem contentSystem){
		this.contentSystem = contentSystem;
	}
	
	public void linkPointPressed(GraphNode sourceNode, long id, float x, float y) {
		if(sourceNode.isVisible()){
			GraphLink link = new GraphLink(contentSystem, this);
			link.setSourceNode(sourceNode);
			cursorIdToLink.put(id, link);
		}
	}
	public void linkPointDragged(GraphNode sourceNode, long id, float x, float y) {
		if(sourceNode.isVisible()){
			GraphLink link = cursorIdToLink.get(id);
			if(link != null)
				link.setTargetLocation(new Location(x,y,0));
		}
	}
	public void linkPointReleased(GraphNode sourceNode, long id, float x, float y) {
		GraphLink link = cursorIdToLink.get(id);
		if(link != null){
			for(GraphNode targetNode: graphNodes){
				if(targetNode.isVisible() && !targetNode.getName().equals(sourceNode.getName()) && targetNode.isLinkable() && targetNode.contains(new Point2D.Float(x,y))){
					link.setTargetNode(targetNode);
					sourceNode.fireNodeConnected(link);
					targetNode.fireNodeConnected(link);
					return;
				}
			}
			link.remove();
			this.detachGraphLink(link);
		}
	}	


	public void graphNodeDragged(GraphNode graphNode, long id, float x, float y) {
		graphNode.updateConnectionPoints();
	}

	public void graphNodePressed(GraphNode graphNode, long id, float x, float y) {}
	public void graphNodeReleased(GraphNode graphNode, long id, float x, float y) {}
	public void graphNodeClicked(GraphNode graphNode, long id, float x, float y) {}
	
	public void addGraphNode(GraphNode node){
		graphNodes.add(node);
		WorkflowRegistry.getInstance().addNode(node);
	}
	
	public void detachGraphNode(GraphNode node){
		for(GraphLink link: node.getIncomingLinks()){
			link.remove();
			graphLinks.remove(link);
			node.fireNodeDisconnected(link);
		}
		for(GraphLink link: node.getOutgoingLinks()){
			link.remove();
			graphLinks.remove(link);
			node.fireNodeDisconnected(link);
		}
		node.unregisterAllLinks();
		graphNodes.remove(node);
		WorkflowRegistry.getInstance().removeNode(node);
	}
	
	public ArrayList<GraphNode> getGraphNodes(){
		return graphNodes;
	}
	
	public void addGraphLink(GraphLink link){
		graphLinks.add(link);
		WorkflowRegistry.getInstance().addLink(link);
	}
	
	public void detachGraphLink(GraphLink link){
		if(link.getSourceNode() != null){
			link.getSourceNode().unregisterLink(link);
			link.getSourceNode().fireNodeDisconnected(link);
		}
		if(link.getTargetNode() != null){
			link.getTargetNode().unregisterLink(link);
			link.getTargetNode().fireNodeDisconnected(link);
		}
		graphLinks.remove(link);
		WorkflowRegistry.getInstance().removeLink(link);
	}
	
	public ArrayList<GraphLink> getGraphLinks(){
		return graphLinks;
	}

	public void clearGraph() {
		
		for(GraphNode node: graphNodes)
			node.remove();
		for(GraphLink link: graphLinks)
			link.remove();
		
		graphNodes.clear();
		graphLinks.clear();
	}
}
