package apps.mathpadapp.conceptmapping;

import java.util.ArrayList;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.OrthoContentItem;
import synergynetframework.appsystem.contentsystem.items.listener.ItemListener;
import synergynetframework.appsystem.contentsystem.items.listener.OrthoControlPointRotateTranslateScaleListener;
import synergynetframework.appsystem.contentsystem.items.listener.ScreenCursorListener;
import synergynetframework.appsystem.contentsystem.items.utils.Location;

public class GraphNode {
	
	protected ContentSystem contentSystem;
	protected GraphManager graphManager;
	protected OrthoContentItem nodeItem;
	protected ArrayList<GraphLink> incomingLinks = new ArrayList<GraphLink>();
	protected ArrayList<GraphLink> outgoingLinks = new ArrayList<GraphLink>();
	protected OrthoContentItem linkPoint, closePoint;
	protected boolean isLinkable = true;

	protected transient ArrayList<ConceptMapListener> listeners = new ArrayList<ConceptMapListener>();

	public GraphNode(GraphManager graphManager, OrthoContentItem nodeItem){
		this.nodeItem = nodeItem;
		this.graphManager = graphManager;
		if(graphManager != null){
			graphManager.addGraphNode(this);
			nodeItem.addOrthoControlPointRotateTranslateScaleListener(new OrthoControlPointRotateTranslateScaleListener(){
	
				@Override
				public void itemRotated(ContentItem item, float newAngle,
						float oldAngle) {
					GraphNode.this.updateConnectionPoints();
				}
	
				@Override
				public void itemScaled(ContentItem item, float newScaleFactor,
						float oldScaleFactor) {
					GraphNode.this.updateConnectionPoints();
				}
	
				@Override
				public void itemTranslated(ContentItem item, float newLocationX,
						float newLocationY, float oldLocationX, float oldLocationY) {
					GraphNode.this.updateConnectionPoints();
				}
			});
		}
	}
	
	public OrthoContentItem getNodeItem(){
		return nodeItem;
	}
	
	public void remove(){
		nodeItem.getContentSystem().removeContentItem(nodeItem);
	}
	
	public void setLinkPoint(OrthoContentItem linkPoint){
		this.linkPoint = linkPoint;
		linkPoint.addScreenCursorListener(new ScreenCursorListener() {
			
			@Override
			public void screenCursorChanged(ContentItem item, long id, float x,
					float y, float pressure) {
				nodeItem.setRotateTranslateScalable(false);
				if(graphManager != null) graphManager.linkPointDragged(GraphNode.this, id, x, y);
				nodeItem.setRotateTranslateScalable(true);
			}

			@Override
			public void screenCursorClicked(ContentItem item, long id, float x,
					float y, float pressure) {
			}

			@Override
			public void screenCursorPressed(ContentItem item, long id, float x,
					float y, float pressure) {
				nodeItem.setRotateTranslateScalable(false);
				if(graphManager != null) graphManager.linkPointPressed(GraphNode.this, id, x, y);
				nodeItem.setRotateTranslateScalable(true);
			}

			@Override
			public void screenCursorReleased(ContentItem item, long id, float x,
					float y, float pressure) {
				if(graphManager != null) graphManager.linkPointReleased(GraphNode.this, id, x, y);
			}
		});
	}
	
	public void setClosePoint(OrthoContentItem closePoint){
		this.closePoint = closePoint;
		closePoint.addItemListener(new ItemListener() {
			
			@Override
			public void cursorChanged(ContentItem item, long id, float x,
					float y, float pressure) {
				 
				
			}

			@Override
			public void cursorClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				if(graphManager != null) graphManager.detachGraphNode(GraphNode.this);
				GraphNode.this.remove();				
			}

			@Override
			public void cursorDoubleClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				 
				
			}

			@Override
			public void cursorLongHeld(ContentItem item, long id, float x,
					float y, float pressure) {
				 
				
			}

			@Override
			public void cursorPressed(ContentItem item, long id, float x,
					float y, float pressure) {
				 
				
			}

			@Override
			public void cursorReleased(ContentItem item, long id, float x,
					float y, float pressure) {
				 
				
			}

			@Override
			public void cursorRightClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				 
				
			}
		});		
	}
	
	public OrthoContentItem getClosePoint(){
		return this.closePoint;
	}
	
	public OrthoContentItem getLinkPoint(){
		return this.linkPoint;
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
	
	public String getName() {
		return nodeItem.getName();
	}

	public void setLocation(float x, float y){
		nodeItem.setLocalLocation(x, y);
		updateConnectionPoints();
	}
	
	public Location getLocation(){
		return nodeItem.getLocalLocation();
	}
	
	public void updateConnectionPoints(){
		for(GraphLink link: incomingLinks){
			link.getLineItem().setTargetLocation(this.getLocation()/*this.getLinkPointLocation()*/);
		}
		for(GraphLink link: outgoingLinks){
			link.getLineItem().setSourceLocation(this.getLocation()/*this.getLinkPointLocation()*/);
		}
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
	
	public void setLinkable(boolean isLinkable){
		this.isLinkable = isLinkable;
	}
	
	public boolean isLinkable(){
		return isLinkable;
	}

	
	public interface ConceptMapListener {
		public void nodeConnected(GraphLink link);
		public void nodeDisconnected(GraphLink link);
	}
	
	public GraphManager getGraphManager(){
		return this.graphManager;
	}
	
}
