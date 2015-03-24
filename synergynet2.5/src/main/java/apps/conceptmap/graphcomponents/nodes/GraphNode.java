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

package apps.conceptmap.graphcomponents.nodes;

import java.awt.geom.Point2D;
import java.net.URL;
import java.util.ArrayList;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.OrthoContainer;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.listener.ScreenCursorListener;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;
import synergynetframework.appsystem.contentsystem.items.utils.Location;
import apps.conceptmap.GraphConfig;
import apps.conceptmap.graphcomponents.GraphComponent;
import apps.conceptmap.graphcomponents.OptionMessage;
import apps.conceptmap.graphcomponents.link.GraphLink;
import apps.conceptmap.utility.ConceptMapListener;
import apps.conceptmap.utility.GraphManager;
import apps.conceptmap.utility.MessageFactory;

/**
 * The Class GraphNode.
 */
public abstract class GraphNode extends GraphComponent {
	
	/** The Constant BOTTOM_LEFT_CORNER. */
	public static final String BOTTOM_LEFT_CORNER = "BOTTOM_LEFT";

	/** The Constant BOTTOM_RIGHT_CORNER. */
	public static final String BOTTOM_RIGHT_CORNER = "BOTTOM_RIGHT";

	/** The Constant MIDDLE. */
	public static final String MIDDLE = "MIDDLE";

	/** The Constant TOP_LEFT_CORNER. */
	public static final String TOP_LEFT_CORNER = "TOP_LEFT";

	/** The Constant TOP_RIGHT_CORNER. */
	public static final String TOP_RIGHT_CORNER = "TOP_RIGHT";

	/** The close point location. */
	protected String closePointLocation;

	/** The container. */
	protected OrthoContainer container;

	/** The incoming links. */
	protected ArrayList<GraphLink> incomingLinks = new ArrayList<GraphLink>();
	
	/** The is linkable. */
	protected boolean isLinkable = true;

	/** The is msg visible. */
	private boolean isMsgVisible = false;

	/** The close point. */
	protected SimpleButton linkPoint, closePoint;

	/** The link point location. */
	protected String linkPointLocation;

	/** The listeners. */
	protected transient ArrayList<ConceptMapListener> listeners = new ArrayList<ConceptMapListener>();

	/** The outgoing links. */
	protected ArrayList<GraphLink> outgoingLinks = new ArrayList<GraphLink>();

	/**
	 * Instantiates a new graph node.
	 *
	 * @param contentSystem
	 *            the content system
	 * @param gManager
	 *            the g manager
	 */
	public GraphNode(final ContentSystem contentSystem,
			final GraphManager gManager) {
		super(contentSystem, gManager);
		container = (OrthoContainer) contentSystem
				.createContentItem(OrthoContainer.class);
		linkPoint = this
				.createButtonWithImage(GraphConfig.nodeLinkImageResource);
		closePoint = this
				.createButtonWithImage(GraphConfig.nodeCloseImageResource);
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
			public void screenCursorReleased(ContentItem item, long id,
					float x, float y, float pressure) {
				graphManager.linkPointReleased(GraphNode.this, id, x, y);
			}
		});
		closePoint.addButtonListener(new SimpleButtonAdapter() {

			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				if (!isMsgVisible) {
					isMsgVisible = true;
					OptionMessage msg = MessageFactory
							.getInstance()
							.createOptionMessage(
									contentSystem,
									gManager,
									GraphNode.this,
									"Are you sure you want to delete this node?",
									MessageFactory.OK_CANCEL_MESSAGE);
					msg.setLocation(GraphNode.this.getLocation().x,
							GraphNode.this.getLocation().y);
					msg.setOrder(GraphNode.this.getOrder() + 1);
					GraphNode.this
							.addOptionMessageListener(new OptionMessageListener() {
								
								@Override
								public void messageProcessed(OptionMessage msg) {
									if (msg.getParentComponent().getName()
											.equals(GraphNode.this.getName())) {
										if (msg.getSelectedOption() == 0) {
											msg.remove();
											graphManager.detachGraphNode(msg);
											GraphNode.this.remove();
											graphManager
													.detachGraphNode(GraphNode.this);
											// if(keyboardNode != null){
											// keyboardNode.remove();
											// graphManager.detachGraphNode(keyboardNode);
											// }
										} else if (msg.getSelectedOption() == 1) {
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
	 * Contains.
	 *
	 * @param point
	 *            the point
	 * @return true, if successful
	 */
	public boolean contains(Point2D.Float point) {
		return container.contains(point);
	}

	/**
	 * Creates the button with image.
	 *
	 * @param imageResource
	 *            the image resource
	 * @return the simple button
	 */
	protected SimpleButton createButtonWithImage(URL imageResource) {
		SimpleButton btn = (SimpleButton) contentSystem
				.createContentItem(SimpleButton.class);
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
	 * Gets the close button.
	 *
	 * @return the close button
	 */
	public SimpleButton getCloseButton() {
		return this.closePoint;
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
	 * Gets the link button.
	 *
	 * @return the link button
	 */
	public SimpleButton getLinkButton() {
		return this.linkPoint;
	}

	/**
	 * Gets the location.
	 *
	 * @return the location
	 */
	public Location getLocation() {
		return container.getLocalLocation();
	}

	/*
	 * (non-Javadoc)
	 * @see apps.conceptmap.graphcomponents.GraphComponent#getName()
	 */
	@Override
	public String getName() {
		return container.getName();
	}
	
	/**
	 * Gets the node container.
	 *
	 * @return the node container
	 */
	public ContentItem getNodeContainer() {
		return container;
	}

	/**
	 * Gets the order.
	 *
	 * @return the order
	 */
	public int getOrder() {
		return container.getOrder();
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
	 * Checks if is visible.
	 *
	 * @return true, if is visible
	 */
	public boolean isVisible() {
		return container.isVisible();
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
		contentSystem.removeContentItem(container);
	}

	/**
	 * Removes the concept map listeners.
	 */
	public void removeConceptMapListeners() {
		listeners.clear();
	}

	/**
	 * Sets the close button location.
	 *
	 * @param location
	 *            the new close button location
	 */
	public abstract void setCloseButtonLocation(String location);

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
	 * Sets the link button location.
	 *
	 * @param location
	 *            the new link button location
	 */
	public abstract void setLinkButtonLocation(String location);

	/**
	 * Sets the location.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public void setLocation(float x, float y) {
		container.setLocalLocation(x, y);
		updateConnectionPoints();
	}

	/**
	 * Sets the node content.
	 *
	 * @param nodeItem
	 *            the new node content
	 */
	protected abstract void setNodeContent(ContentItem nodeItem);

	/**
	 * Sets the order.
	 *
	 * @param zOrder
	 *            the new order
	 */
	public void setOrder(int zOrder) {
		container.setOrder(zOrder);
	}

	/**
	 * Sets the scale.
	 *
	 * @param scaleFactor
	 *            the new scale
	 */
	public void setScale(float scaleFactor) {
		container.setScale(scaleFactor);
	}

	/**
	 * Sets the visible.
	 *
	 * @param isVisible
	 *            the new visible
	 */
	public void setVisible(boolean isVisible) {
		container.setVisible(isVisible);
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
			link.setTargetLocation(this.getLocation()/*
													 * this.getLinkPointLocation(
													 * )
													 */);
		}
		for (GraphLink link : outgoingLinks) {
			link.setSourceLocation(this.getLocation()/*
													 * this.getLinkPointLocation(
													 * )
													 */);
		}
	}

	/**
	 * Update node.
	 */
	public void updateNode() {
		if (linkPointLocation != null) {
			this.setLinkButtonLocation(this.linkPointLocation);
		}
		if (closePointLocation != null) {
			this.setCloseButtonLocation(this.closePointLocation);
		}
		this.updateConnectionPoints();
	}
}
