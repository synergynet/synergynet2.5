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

package apps.projectmanagement.component.workflowchart.core.graphcomponents.links;

import java.awt.geom.Point2D;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.LineItem;
import synergynetframework.appsystem.contentsystem.items.listener.ItemListener;
import synergynetframework.appsystem.contentsystem.items.utils.Location;
import apps.projectmanagement.GraphConfig;
import apps.projectmanagement.component.workflowchart.core.GraphManager;
import apps.projectmanagement.component.workflowchart.core.graphcomponents.GraphComponent;
import apps.projectmanagement.component.workflowchart.core.graphcomponents.nodes.GraphNode;
import apps.projectmanagement.component.workflowchart.core.graphcomponents.nodes.KeyboardNode;
import apps.projectmanagement.component.workflowchart.core.graphcomponents.nodes.TextNode;

import com.jme.scene.Spatial;

/**
 * The Class GraphLink.
 */
public class GraphLink extends GraphComponent {

	/** The dummy node. */
	private TextNode dummyNode;

	/** The keyboard node. */
	private KeyboardNode keyboardNode;

	/** The line. */
	protected LineItem line;

	/** The menu. */
	private LinkMenu menu;

	/** The menu enabled. */
	private boolean menuEnabled = true;

	/** The position ratio. */
	private double positionRatio;

	/** The source node. */
	protected GraphNode sourceNode;

	/** The target node. */
	protected GraphNode targetNode;

	/**
	 * Instantiates a new graph link.
	 *
	 * @param contentSystem
	 *            the content system
	 * @param gManager
	 *            the g manager
	 */
	public GraphLink(final ContentSystem contentSystem,
			final GraphManager gManager) {
		super(contentSystem, gManager);
		line = (LineItem) contentSystem.createContentItem(LineItem.class);
		line.setArrowMode(LineItem.ARROW_TO_TARGET);
		line.setRotateTranslateScalable(false);
		line.setBringToTopable(false);
		line.setWidth(GraphConfig.linkWidth);
		line.setLineColour(GraphConfig.linkColor);
		line.setTextColour(GraphConfig.linkTextColor);
		line.setTextFont(GraphConfig.linkTextFont);
		line.addItemListener(new ItemListener() {
			
			@Override
			public void cursorChanged(ContentItem item, long id, float x,
					float y, float pressure) {
			}
			
			@Override
			public void cursorClicked(ContentItem item, long id, float x,
					float y, float pressure) {
			}
			
			@Override
			public void cursorDoubleClicked(ContentItem item, long id, float x,
					float y, float pressure) {

				if (isMenuEnabled()) {
					if (menu == null) {
						menu = new LinkMenu(contentSystem, gManager,
								GraphLink.this);
					}
					menu.setLocation(x, y);
					menu.setVisible(true);
					Point2D.Float position = new Point2D.Float(x, y);
					Point2D.Float sourcePos = new Point2D.Float(GraphLink.this
							.getSourceLocation().x, GraphLink.this
							.getSourceLocation().y);
					Point2D.Float targetPos = new Point2D.Float(GraphLink.this
							.getTargetLocation().x, GraphLink.this
							.getTargetLocation().y);
					positionRatio = sourcePos.distance(position)
							/ sourcePos.distance(targetPos);
				}
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
		graphManager.addGraphLink(this);
	}

	/**
	 * Creates the link point.
	 *
	 * @return the graph node
	 */
	private GraphNode createLinkPoint() {
		if (dummyNode == null) {
			dummyNode = new TextNode(contentSystem, graphManager);
			dummyNode.setText("");
			dummyNode.setVisible(false);
			dummyNode.setLinkButtonLocation(GraphNode.MIDDLE);
			updateLinkPoint();
		}
		return dummyNode;
	}

	/**
	 * Gets the arrow mode.
	 *
	 * @return the arrow mode
	 */
	public int getArrowMode() {
		return line.getArrowMode();
	}

	/**
	 * Gets the keyboard node.
	 *
	 * @return the keyboard node
	 */
	public KeyboardNode getKeyboardNode() {
		return keyboardNode;
	}

	/**
	 * Gets the line item.
	 *
	 * @return the line item
	 */
	public LineItem getLineItem() {
		return line;
	}
	
	/**
	 * Gets the link mode.
	 *
	 * @return the link mode
	 */
	public int getLinkMode() {
		return line.getLineMode();
	}

	/**
	 * Gets the link point.
	 *
	 * @return the link point
	 */
	public GraphNode getLinkPoint() {
		return createLinkPoint();
	}

	/*
	 * (non-Javadoc)
	 * @see apps.projectmanagement.component.workflowchart.core.graphcomponents.
	 * GraphComponent#getName()
	 */
	public String getName() {
		return line.getName();
	}

	/**
	 * Gets the order.
	 *
	 * @return the order
	 */
	public int getOrder() {
		return line.getOrder();
	}

	/**
	 * Gets the source location.
	 *
	 * @return the source location
	 */
	public Location getSourceLocation() {
		return line.getSourceLocation();
	}

	/**
	 * Gets the source node.
	 *
	 * @return the source node
	 */
	public GraphNode getSourceNode() {
		return sourceNode;
	}
	
	/**
	 * Gets the target location.
	 *
	 * @return the target location
	 */
	public Location getTargetLocation() {
		return line.getTargetLocation();
	}

	/**
	 * Gets the target node.
	 *
	 * @return the target node
	 */
	public GraphNode getTargetNode() {
		return targetNode;
	}

	/**
	 * Gets the text.
	 *
	 * @return the text
	 */
	public String getText() {
		return line.getText();
	}

	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public float getWidth() {
		return line.getWidth();
	}

	/**
	 * Checks if is menu enabled.
	 *
	 * @return true, if is menu enabled
	 */
	public boolean isMenuEnabled() {
		return menuEnabled;
	}

	/**
	 * Position menu.
	 *
	 * @param sourceLocation
	 *            the source location
	 * @param targetLocation
	 *            the target location
	 */
	private void positionMenu(Location sourceLocation, Location targetLocation) {
		Point2D.Float sourcePoint = new Point2D.Float(sourceLocation.x,
				sourceLocation.y);
		Point2D.Float targetPoint = new Point2D.Float(targetLocation.x,
				targetLocation.y);
		double distanceSourceToTarget = sourcePoint.distance(targetPoint);
		double distanceSourceToMenu = distanceSourceToTarget * positionRatio;
		double cosAngle = (targetPoint.getX() - sourcePoint.getX())
				/ distanceSourceToTarget;
		double sinAngle = (targetPoint.getY() - sourcePoint.getY())
				/ distanceSourceToTarget;
		double xm = (cosAngle * distanceSourceToMenu) + sourcePoint.getX();
		double ym = (sinAngle * distanceSourceToMenu) + sourcePoint.getY();
		menu.setLocation((float) xm, (float) ym);
	}

	/**
	 * Removes the.
	 */
	public void remove() {
		contentSystem.removeContentItem(line);
		if (menu != null) {
			menu.remove();
		}
	}

	/**
	 * Removes the menu.
	 */
	public void removeMenu() {
		if (menu != null) {
			menu.remove();
			menu = null;
		}
	}

	/**
	 * Sets the arrow mode.
	 *
	 * @param arrowMode
	 *            the new arrow mode
	 */
	public void setArrowMode(int arrowMode) {
		line.setArrowMode(arrowMode);
	}

	/**
	 * Sets the editable.
	 *
	 * @param b
	 *            the new editable
	 */
	public void setEditable(boolean b) {
		
		((Spatial) line.getImplementationObject()).setIsCollidable(b);
	}
	
	/**
	 * Sets the keyboard node.
	 *
	 * @param keyboardNode
	 *            the new keyboard node
	 */
	public void setKeyboardNode(KeyboardNode keyboardNode) {
		this.keyboardNode = keyboardNode;
	}

	/**
	 * Sets the link mode.
	 *
	 * @param lineMode
	 *            the new link mode
	 */
	public void setLinkMode(int lineMode) {
		line.setLineMode(lineMode);
	}

	/**
	 * Sets the menu enabled.
	 *
	 * @param isEnabled
	 *            the new menu enabled
	 */
	public void setMenuEnabled(boolean isEnabled) {
		this.menuEnabled = isEnabled;
	}

	/**
	 * Sets the source location.
	 *
	 * @param sourceLocation
	 *            the new source location
	 */
	public void setSourceLocation(Location sourceLocation) {
		line.setSourceLocation(sourceLocation);
		this.updateLinkPoint();
		if (menu != null) {
			positionMenu(line.getSourceLocation(), line.getTargetLocation());
		}
	}

	/**
	 * Sets the source node.
	 *
	 * @param sourceNode
	 *            the new source node
	 */
	public void setSourceNode(GraphNode sourceNode) {
		this.sourceNode = sourceNode;
		if (sourceNode != null) {
			line.setSourceItem(sourceNode.getNodeContainer());
			line.setSourceLocation(sourceNode.getLocation()/*
															 * sourceNode.
															 * getLinkPointLocation
															 * ()
															 */);
			sourceNode.registerOutgoingLink(this);
			this.updateLinkPoint();
			if (menu != null) {
				positionMenu(line.getSourceLocation(), line.getTargetLocation());
			}
		}
	}

	/**
	 * Sets the target location.
	 *
	 * @param targetLocation
	 *            the new target location
	 */
	public void setTargetLocation(Location targetLocation) {
		line.setTargetLocation(targetLocation);
		this.updateLinkPoint();
		if (menu != null) {
			positionMenu(line.getSourceLocation(), line.getTargetLocation());
		}
	}
	
	/**
	 * Sets the target node.
	 *
	 * @param targetNode
	 *            the new target node
	 */
	public void setTargetNode(GraphNode targetNode) {
		this.targetNode = targetNode;
		if (targetNode != null) {
			line.setTargetItem(targetNode.getNodeContainer());
			line.setTargetLocation(targetNode.getLocation()/*
															 * targetNode.
															 * getLinkPointLocation
															 * ()
															 */);
			targetNode.registerIncomingLink(this);
			this.updateLinkPoint();
			if (menu != null) {
				positionMenu(line.getSourceLocation(), line.getTargetLocation());
			}
		}
	}

	/**
	 * Sets the text.
	 *
	 * @param text
	 *            the new text
	 */
	public void setText(String text) {
		line.setText(text);
	}

	/**
	 * Sets the visiblity.
	 *
	 * @param visible
	 *            the new visiblity
	 */
	public void setVisiblity(boolean visible) {
		line.setVisible(visible);
		if (!visible) {
			removeMenu();
		}
		
	}

	/**
	 * Sets the width.
	 *
	 * @param lineWidth
	 *            the new width
	 */
	public void setWidth(float lineWidth) {
		line.setWidth(lineWidth);
	}

	/**
	 * Update link point.
	 */
	private void updateLinkPoint() {
		if ((dummyNode != null) && (sourceNode != null) && (targetNode != null)) {
			Point2D.Float lineCenter = new Point2D.Float();
			lineCenter.x = ((line.getTargetLocation().x - line
					.getSourceLocation().x) / 2) + line.getSourceLocation().x;
			lineCenter.y = ((line.getTargetLocation().y - line
					.getSourceLocation().y) / 2) + line.getSourceLocation().y;
			dummyNode.setLocation(lineCenter.x, lineCenter.y);
			dummyNode.updateConnectionPoints();
		}
	}
}
