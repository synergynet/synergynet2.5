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

package apps.mathpadapp.conceptmapping;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.LineItem;
import apps.conceptmap.GraphConfig;

/**
 * The Class GraphLink.
 */
public class GraphLink {

	/** The content system. */
	protected ContentSystem contentSystem;

	/** The graph manager. */
	protected GraphManager graphManager;

	/** The line. */
	protected LineItem line;

	/** The menu enabled. */
	private boolean menuEnabled = true;

	/** The source node. */
	protected GraphNode sourceNode;

	/** The target node. */
	protected GraphNode targetNode;

	/**
	 * Instantiates a new graph link.
	 *
	 * @param contentSystem
	 *            the content system
	 * @param graphManager
	 *            the graph manager
	 */
	public GraphLink(final ContentSystem contentSystem,
			final GraphManager graphManager) {
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

	/**
	 * Gets the arrow mode.
	 *
	 * @return the arrow mode
	 */
	public int getArrowMode() {
		return line.getArrowMode();
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
	 * Gets the name.
	 *
	 * @return the name
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
	 * Gets the source node.
	 *
	 * @return the source node
	 */
	public GraphNode getSourceNode() {
		return sourceNode;
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
	 * Removes the.
	 */
	public void remove() {
		contentSystem.removeContentItem(line);
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
	 * Sets the source node.
	 *
	 * @param sourceNode
	 *            the new source node
	 */
	public void setSourceNode(GraphNode sourceNode) {
		this.sourceNode = sourceNode;
		if (sourceNode != null) {
			line.setSourceItem(sourceNode.getNodeItem());
			line.setSourceLocation(sourceNode.getLocation()/*
															 * sourceNode.
															 * getLinkPointLocation
															 * ()
															 */);
			sourceNode.registerOutgoingLink(this);
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
			line.setTargetItem(targetNode.getNodeItem());
			line.setTargetLocation(targetNode.getLocation()/*
															 * targetNode.
															 * getLinkPointLocation
															 * ()
															 */);
			targetNode.registerIncomingLink(this);
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
	 * Sets the width.
	 *
	 * @param lineWidth
	 *            the new width
	 */
	public void setWidth(float lineWidth) {
		line.setWidth(lineWidth);
	}
}
