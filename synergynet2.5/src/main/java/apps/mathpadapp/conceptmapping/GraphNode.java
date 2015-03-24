package apps.mathpadapp.conceptmapping;

import java.util.ArrayList;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.OrthoContentItem;
import synergynetframework.appsystem.contentsystem.items.listener.ItemListener;
import synergynetframework.appsystem.contentsystem.items.listener.OrthoControlPointRotateTranslateScaleListener;
import synergynetframework.appsystem.contentsystem.items.listener.ScreenCursorListener;
import synergynetframework.appsystem.contentsystem.items.utils.Location;

/**
 * The Class GraphNode.
 */
public class GraphNode {

	/**
	 * The listener interface for receiving conceptMap events. The class that is
	 * interested in processing a conceptMap event implements this interface,
	 * and the object created with that class is registered with a component
	 * using the component's <code>addConceptMapListener<code> method. When
	 * the conceptMap event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see ConceptMapEvent
	 */
	public interface ConceptMapListener {

		/**
		 * Node connected.
		 *
		 * @param link
		 *            the link
		 */
		public void nodeConnected(GraphLink link);

		/**
		 * Node disconnected.
		 *
		 * @param link
		 *            the link
		 */
		public void nodeDisconnected(GraphLink link);
	}

	/** The content system. */
	protected ContentSystem contentSystem;

	/** The graph manager. */
	protected GraphManager graphManager;

	/** The incoming links. */
	protected ArrayList<GraphLink> incomingLinks = new ArrayList<GraphLink>();

	/** The is linkable. */
	protected boolean isLinkable = true;

	/** The close point. */
	protected OrthoContentItem linkPoint, closePoint;

	/** The listeners. */
	protected transient ArrayList<ConceptMapListener> listeners = new ArrayList<ConceptMapListener>();
	
	/** The node item. */
	protected OrthoContentItem nodeItem;
	
	/** The outgoing links. */
	protected ArrayList<GraphLink> outgoingLinks = new ArrayList<GraphLink>();

	/**
	 * Instantiates a new graph node.
	 *
	 * @param graphManager
	 *            the graph manager
	 * @param nodeItem
	 *            the node item
	 */
	public GraphNode(GraphManager graphManager, OrthoContentItem nodeItem) {
		this.nodeItem = nodeItem;
		this.graphManager = graphManager;
		if (graphManager != null) {
			graphManager.addGraphNode(this);
			nodeItem.addOrthoControlPointRotateTranslateScaleListener(new OrthoControlPointRotateTranslateScaleListener() {
				
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
				public void itemTranslated(ContentItem item,
						float newLocationX, float newLocationY,
						float oldLocationX, float oldLocationY) {
					GraphNode.this.updateConnectionPoints();
				}
			});
		}
	}

	/**
	 * Adds the concept map listener.
	 *
	 * @param listener
	 *            the listener
	 */
	public void addConceptMapListener(ConceptMapListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	/**
	 * Fire node connected.
	 *
	 * @param link
	 *            the link
	 */
	public void fireNodeConnected(GraphLink link) {
		for (ConceptMapListener listener : listeners) {
			listener.nodeConnected(link);
		}
	}

	/**
	 * Fire node disconnected.
	 *
	 * @param link
	 *            the link
	 */
	public void fireNodeDisconnected(GraphLink link) {
		for (ConceptMapListener listener : listeners) {
			listener.nodeDisconnected(link);
		}
	}

	/**
	 * Gets the close point.
	 *
	 * @return the close point
	 */
	public OrthoContentItem getClosePoint() {
		return this.closePoint;
	}

	/**
	 * Gets the graph manager.
	 *
	 * @return the graph manager
	 */
	public GraphManager getGraphManager() {
		return this.graphManager;
	}

	/**
	 * Gets the incoming links.
	 *
	 * @return the incoming links
	 */
	public ArrayList<GraphLink> getIncomingLinks() {
		return incomingLinks;
	}

	/**
	 * Gets the link point.
	 *
	 * @return the link point
	 */
	public OrthoContentItem getLinkPoint() {
		return this.linkPoint;
	}

	/**
	 * Gets the location.
	 *
	 * @return the location
	 */
	public Location getLocation() {
		return nodeItem.getLocalLocation();
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return nodeItem.getName();
	}

	/**
	 * Gets the node item.
	 *
	 * @return the node item
	 */
	public OrthoContentItem getNodeItem() {
		return nodeItem;
	}

	/**
	 * Gets the outgoing links.
	 *
	 * @return the outgoing links
	 */
	public ArrayList<GraphLink> getOutgoingLinks() {
		return outgoingLinks;
	}

	/**
	 * Checks if is linkable.
	 *
	 * @return true, if is linkable
	 */
	public boolean isLinkable() {
		return isLinkable;
	}
	
	/**
	 * Register incoming link.
	 *
	 * @param link
	 *            the link
	 */
	public void registerIncomingLink(GraphLink link) {
		incomingLinks.add(link);
	}

	/**
	 * Register outgoing link.
	 *
	 * @param link
	 *            the link
	 */
	public void registerOutgoingLink(GraphLink link) {
		outgoingLinks.add(link);
	}

	/**
	 * Removes the.
	 */
	public void remove() {
		nodeItem.getContentSystem().removeContentItem(nodeItem);
	}

	/**
	 * Removes the concept map listeners.
	 */
	public void removeConceptMapListeners() {
		listeners.clear();
	}

	/**
	 * Sets the close point.
	 *
	 * @param closePoint
	 *            the new close point
	 */
	public void setClosePoint(OrthoContentItem closePoint) {
		this.closePoint = closePoint;
		closePoint.addItemListener(new ItemListener() {

			@Override
			public void cursorChanged(ContentItem item, long id, float x,
					float y, float pressure) {
				
			}
			
			@Override
			public void cursorClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				if (graphManager != null) {
					graphManager.detachGraphNode(GraphNode.this);
				}
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

	/**
	 * Sets the linkable.
	 *
	 * @param isLinkable
	 *            the new linkable
	 */
	public void setLinkable(boolean isLinkable) {
		this.isLinkable = isLinkable;
	}

	/**
	 * Sets the link point.
	 *
	 * @param linkPoint
	 *            the new link point
	 */
	public void setLinkPoint(OrthoContentItem linkPoint) {
		this.linkPoint = linkPoint;
		linkPoint.addScreenCursorListener(new ScreenCursorListener() {

			@Override
			public void screenCursorChanged(ContentItem item, long id, float x,
					float y, float pressure) {
				nodeItem.setRotateTranslateScalable(false);
				if (graphManager != null) {
					graphManager.linkPointDragged(GraphNode.this, id, x, y);
				}
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
				if (graphManager != null) {
					graphManager.linkPointPressed(GraphNode.this, id, x, y);
				}
				nodeItem.setRotateTranslateScalable(true);
			}
			
			@Override
			public void screenCursorReleased(ContentItem item, long id,
					float x, float y, float pressure) {
				if (graphManager != null) {
					graphManager.linkPointReleased(GraphNode.this, id, x, y);
				}
			}
		});
	}

	/**
	 * Sets the location.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public void setLocation(float x, float y) {
		nodeItem.setLocalLocation(x, y);
		updateConnectionPoints();
	}

	/**
	 * Unregister all links.
	 */
	public void unregisterAllLinks() {
		outgoingLinks.clear();
		incomingLinks.clear();
	}
	
	/**
	 * Unregister link.
	 *
	 * @param link
	 *            the link
	 * @return true, if successful
	 */
	public boolean unregisterLink(GraphLink link) {
		boolean removed = false;
		removed = outgoingLinks.remove(link);
		removed = incomingLinks.remove(link);
		return removed;
	}

	/**
	 * Update connection points.
	 */
	public void updateConnectionPoints() {
		for (GraphLink link : incomingLinks) {
			link.getLineItem().setTargetLocation(this.getLocation()/*
																	 * this.
																	 * getLinkPointLocation
																	 * ()
																	 */);
		}
		for (GraphLink link : outgoingLinks) {
			link.getLineItem().setSourceLocation(this.getLocation()/*
																	 * this.
																	 * getLinkPointLocation
																	 * ()
																	 */);
		}
	}

}
