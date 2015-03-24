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

package apps.conceptmap.graphcomponents;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.LineItem;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;
import apps.conceptmap.GraphConfig;
import apps.conceptmap.graphcomponents.link.GraphLink;
import apps.conceptmap.graphcomponents.nodes.GraphNode;
import apps.conceptmap.graphcomponents.nodes.TextNode;
import apps.conceptmap.utility.GraphManager;
import apps.conceptmap.utility.MessageFactory;

/**
 * The Class OptionMessage.
 */
public class OptionMessage extends TextNode {
	
	/** The parent component. */
	private GraphComponent parentComponent;

	/** The return value. */
	private int returnValue = -1;

	/**
	 * Instantiates a new option message.
	 *
	 * @param contentSystem
	 *            the content system
	 * @param gManager
	 *            the g manager
	 * @param parentComponent
	 *            the parent component
	 * @param text
	 *            the text
	 * @param messageType
	 *            the message type
	 */
	public OptionMessage(ContentSystem contentSystem, GraphManager gManager,
			GraphComponent parentComponent, String text, int messageType) {
		super(contentSystem, gManager);
		this.parentComponent = parentComponent;
		this.getEditButton().setVisible(false);
		this.getLinkButton().setVisible(false);
		this.setLinkable(false);
		this.setCloseButtonLocation(GraphNode.TOP_LEFT_CORNER);
		closePoint.removeButtonListeners();
		closePoint.addButtonListener(new SimpleButtonAdapter() {

			public void buttonPressed(SimpleButton b, long id, float x,
					float y, float pressure) {
				OptionMessage.this.remove();
				graphManager.detachGraphNode(OptionMessage.this);
			}
		});
		this.setOrder(101);
		this.getMultiLineTextLabel().setBorderSize(40);
		this.getMultiLineTextLabel().setFont(GraphConfig.messageTextFont);
		this.getMultiLineTextLabel()
				.setTextColour(GraphConfig.messageTextColor);
		this.getMultiLineTextLabel().setBackgroundColour(
				GraphConfig.messageBackgroundColor);
		this.getMultiLineTextLabel().setBorderColour(
				GraphConfig.messageBorderColor);
		this.setText(text);
		
		this.setMessageType(messageType);

		if ((parentComponent != null) && (parentComponent instanceof GraphNode)) {
			GraphNode parentNode = (GraphNode) parentComponent;
			final GraphLink linkToMessage = new GraphLink(contentSystem,
					gManager);
			linkToMessage.setSourceNode(parentNode);
			linkToMessage.setTargetNode(this);
			linkToMessage.setLinkMode(LineItem.SEGMENT_LINE);
			linkToMessage.setArrowMode(LineItem.NO_ARROWS);
			linkToMessage.setMenuEnabled(false);
		} else if ((parentComponent != null)
				&& (parentComponent instanceof GraphLink)) {
			GraphLink parentLink = (GraphLink) parentComponent;
			final GraphLink linkToMessage = new GraphLink(contentSystem,
					gManager);
			linkToMessage.setSourceNode(parentLink.getLinkPoint());
			linkToMessage.setTargetNode(this);
			linkToMessage.setLinkMode(LineItem.SEGMENT_LINE);
			linkToMessage.setArrowMode(LineItem.NO_ARROWS);
			linkToMessage.setMenuEnabled(false);
		}

	}

	/**
	 * Gets the parent component.
	 *
	 * @return the parent component
	 */
	public GraphComponent getParentComponent() {
		return parentComponent;
	}

	/**
	 * Gets the selected option.
	 *
	 * @return the selected option
	 */
	public int getSelectedOption() {
		return returnValue;
	}

	/**
	 * Sets the message type.
	 *
	 * @param messageType
	 *            the new message type
	 */
	private void setMessageType(int messageType) {
		if (messageType == MessageFactory.OK_CANCEL_MESSAGE) {
			SimpleButton btn_OK = (SimpleButton) contentSystem
					.createContentItem(SimpleButton.class);
			btn_OK.setAutoFitSize(false);
			btn_OK.setWidth(60);
			btn_OK.setHeight(25);
			btn_OK.setText("OK");
			btn_OK.setLocalLocation(-34, (-mltLabel.getHeight() / 2)
					+ (mltLabel.getBorderSize() / 2));
			btn_OK.setTextColour(GraphConfig.messageTextColor);
			btn_OK.setBackgroundColour(GraphConfig.messageBackgroundColor);
			btn_OK.setBorderColour(GraphConfig.messageBorderColor);
			btn_OK.setBorderSize(2);
			btn_OK.addButtonListener(new SimpleButtonAdapter() {
				
				public void buttonPressed(SimpleButton b, long id, float x,
						float y, float pressure) {
					returnValue = 0;
					if (parentComponent != null) {
						parentComponent
								.fireMessageProcessed(OptionMessage.this);
					}
				}
			});
			container.addSubItem(btn_OK);
			btn_OK.setOrder(2);
			SimpleButton btn_Cancel = (SimpleButton) contentSystem
					.createContentItem(SimpleButton.class);
			btn_Cancel.setAutoFitSize(false);
			btn_Cancel.setWidth(60);
			btn_Cancel.setHeight(25);
			btn_Cancel.setText("Cancel");
			btn_Cancel.setLocalLocation(+34, (-mltLabel.getHeight() / 2)
					+ (mltLabel.getBorderSize() / 2));
			btn_Cancel.setTextColour(GraphConfig.messageTextColor);
			btn_Cancel.setBackgroundColour(GraphConfig.messageBackgroundColor);
			btn_Cancel.setBorderColour(GraphConfig.messageBorderColor);
			btn_Cancel.setBorderSize(2);
			btn_Cancel.addButtonListener(new SimpleButtonAdapter() {
				public void buttonPressed(SimpleButton b, long id, float x,
						float y, float pressure) {
					returnValue = 1;
					if (parentComponent != null) {
						parentComponent
								.fireMessageProcessed(OptionMessage.this);
					}
					
				}
			});
			container.addSubItem(btn_Cancel);
			btn_Cancel.setOrder(2);
			// contentSystem.createSimpleButton(contentSystem.generateUniqueName());
		}
	}
}