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

package apps.projectmanagement.component.workflowchart.core.graphcomponents.nodes;

import java.awt.event.KeyEvent;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.LineItem;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;
import synergynetframework.appsystem.contentsystem.items.utils.Location;
import apps.projectmanagement.GraphConfig;
import apps.projectmanagement.component.workflowchart.core.GraphManager;
import apps.projectmanagement.component.workflowchart.core.graphcomponents.links.GraphLink;
import apps.projectmanagement.component.workflowchart.core.graphcomponents.nodes.KeyboardNode.KeyboardListener;

/**
 * The Class EditableQuadNode.
 */
public abstract class EditableQuadNode extends QuadNode {
	
	/** The edit point. */
	protected SimpleButton editPoint;

	/** The edit point location. */
	protected String editPointLocation;

	/** The keyboard node. */
	protected KeyboardNode keyboardNode;

	/**
	 * Instantiates a new editable quad node.
	 *
	 * @param contentSystem
	 *            the content system
	 * @param gManager
	 *            the g manager
	 */
	public EditableQuadNode(ContentSystem contentSystem, GraphManager gManager) {
		super(contentSystem, gManager);
		editPoint = this
				.createButtonWithImage(GraphConfig.nodeEditImageResource);
		container.addSubItem(editPoint);
		editPoint.setOrder(2);
		editPoint.addButtonListener(new SimpleButtonAdapter() {

			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				EditableQuadNode.this
						.showAndLinkKeyboard(EditableQuadNode.this);
			}
		});
	}

	/**
	 * Gets the edits the button.
	 *
	 * @return the edits the button
	 */
	public SimpleButton getEditButton() {
		return this.editPoint;
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
	 * Gets the text.
	 *
	 * @return the text
	 */
	public abstract String getText();
	
	/**
	 * Sets the edits the point location.
	 *
	 * @param location
	 *            the new edits the point location
	 */
	public void setEditPointLocation(String location) {
		if (contentItem != null) {
			editPointLocation = location;
			if (location.equals(GraphNode.TOP_RIGHT_CORNER)) {
				Location newLocation = new Location(contentItem.getWidth() / 2,
						contentItem.getHeight() / 2, 0);
				newLocation.x -= editPoint.getWidth() / 2;
				newLocation.y -= editPoint.getHeight() / 2;
				editPoint.setLocalLocation(newLocation.x, newLocation.y);
			} else if (location.equals(GraphNode.TOP_LEFT_CORNER)) {
				Location newLocation = new Location(
						-contentItem.getWidth() / 2,
						contentItem.getHeight() / 2, 0);
				newLocation.x += (editPoint.getWidth() / 2)
						+ closePoint.getWidth();
				newLocation.y -= editPoint.getHeight() / 2;
				editPoint.setLocalLocation(newLocation.x, newLocation.y);
			} else if (location.equals(GraphNode.BOTTOM_RIGHT_CORNER)) {
				Location newLocation = new Location(contentItem.getWidth() / 2,
						-contentItem.getHeight() / 2, 0);
				newLocation.x -= editPoint.getWidth() / 2;
				newLocation.y += editPoint.getHeight() / 2;
				editPoint.setLocalLocation(newLocation.x, newLocation.y);
			} else if (location.equals(GraphNode.BOTTOM_LEFT_CORNER)) {
				Location newLocation = new Location(
						-contentItem.getWidth() / 2,
						-contentItem.getHeight() / 2, 0);
				newLocation.x += (editPoint.getWidth() / 2)
						+ closePoint.getWidth();
				newLocation.y += editPoint.getHeight() / 2;
				editPoint.setLocalLocation(newLocation.x, newLocation.y);
			} else if (location.equals(GraphNode.MIDDLE)) {
				editPoint.setLocalLocation(contentItem.getLocalLocation());
			}
		}
	}

	/**
	 * Sets the text.
	 *
	 * @param text
	 *            the new text
	 */
	public abstract void setText(String text);

	/**
	 * Show and link keyboard.
	 *
	 * @param edittedNode
	 *            the editted node
	 */
	public void showAndLinkKeyboard(final EditableQuadNode edittedNode) {
		if (keyboardNode == null) {
			keyboardNode = new KeyboardNode(contentSystem, graphManager);
			keyboardNode.setLinkButtonLocation(GraphNode.MIDDLE);
			keyboardNode.setCloseButtonLocation(GraphNode.TOP_LEFT_CORNER);
			keyboardNode.setLocation(edittedNode.getLocation().x,
					edittedNode.getLocation().y);
			keyboardNode.getLinkButton().setVisible(false);
			keyboardNode.setLinkable(false);
			keyboardNode.setOrder(edittedNode.getOrder() + 1);
			keyboardNode.setScale(0.8f);
			keyboardNode.getCloseButton().removeButtonListeners();
			keyboardNode.getCloseButton().addButtonListener(
					new SimpleButtonAdapter() {
						public void buttonClicked(SimpleButton b, long id,
								float x, float y, float pressure) {
							keyboardNode.remove();
							graphManager.detachGraphNode(keyboardNode);
							keyboardNode = null;
						}
					});
			keyboardNode.addKeyListener(new KeyboardListener() {
				@Override
				public void keyPressed(KeyEvent evt) {
				}
				
				@Override
				public void keyReleased(KeyEvent evt) {
					String text = edittedNode.getText();
					if (text == null) {
						text = "";
					}
					if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
						edittedNode.setText(text + evt.getKeyChar());
					} else if (evt.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
						if (text.length() > 0) {
							text = text.substring(0, text.length() - 1);
							edittedNode.setText(text);
						}
					} else if (evt.getKeyChar() != KeyEvent.VK_CAPS_LOCK) {
						edittedNode.setText(text + evt.getKeyChar());
					}
				}
			});
			final GraphLink linkToKeyboard = new GraphLink(contentSystem,
					graphManager);
			linkToKeyboard.setSourceNode(edittedNode);
			linkToKeyboard.setTargetNode(keyboardNode);
			linkToKeyboard.setLinkMode(LineItem.SEGMENT_LINE);
			linkToKeyboard.setArrowMode(LineItem.NO_ARROWS);
			linkToKeyboard.setMenuEnabled(false);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * apps.projectmanagement.component.workflowchart.core.graphcomponents.nodes
	 * .GraphNode#updateNode()
	 */
	public void updateNode() {
		super.updateNode();
		if (editPointLocation != null) {
			this.setEditPointLocation(this.editPointLocation);
		}
	}
}
