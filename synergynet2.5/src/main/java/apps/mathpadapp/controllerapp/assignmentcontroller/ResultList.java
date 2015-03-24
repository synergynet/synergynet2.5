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

package apps.mathpadapp.controllerapp.assignmentcontroller;

import java.awt.Color;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.ListContainer;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.TextLabel;
import synergynetframework.appsystem.contentsystem.items.TextLabel.Alignment;
import synergynetframework.appsystem.contentsystem.items.Window;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;
import apps.mathpadapp.util.MTList;

/**
 * The Class ResultList.
 */
public class ResultList extends MTList {
	
	/** The view answer dialog. */
	private ViewAnswerDialog viewAnswerDialog;

	/**
	 * Instantiates a new result list.
	 *
	 * @param contentSystem
	 *            the content system
	 */
	public ResultList(ContentSystem contentSystem) {
		super(contentSystem);
	}
	
	/*
	 * (non-Javadoc)
	 * @see apps.mathpadapp.util.MTList#createListItem(java.lang.String,
	 * java.lang.Object,
	 * synergynetframework.appsystem.contentsystem.items.ListContainer)
	 */
	@Override
	protected ContentItem createListItem(String str, Object item,
			final ListContainer targetList) {

		targetList.setOrder(-1);
		Window resultEntry = (Window) contentSystem
				.createContentItem(Window.class);
		resultEntry.setWidth(this.listItemWidth - 10);
		resultEntry.setHeight(this.listItemHeight + 4);
		resultEntry.setBorderSize(3);
		resultEntry.setBorderColour(Color.white);
		
		TextLabel userName = (TextLabel) contentSystem
				.createContentItem(TextLabel.class);
		userName.setNote("userName");
		userName.setBorderSize(0);
		userName.setBackgroundColour(Color.white);
		userName.setAutoFitSize(false);
		userName.setHeight(this.listItemHeight);
		userName.setWidth((this.listItemWidth / 3) - 4);
		userName.setAlignment(Alignment.LEFT);
		userName.setLocalLocation(-userName.getWidth(), 0);
		resultEntry.addSubItem(userName);

		TextLabel resultBox = (TextLabel) contentSystem
				.createContentItem(TextLabel.class);
		resultBox.setNote("resultBox");
		resultBox.setBorderSize(0);
		// resultBox.setBorderColour(Color.white);
		resultBox.setBackgroundColour(Color.white);
		resultBox.setAutoFitSize(false);
		resultBox.setHeight(this.listItemHeight);
		resultBox.setWidth((this.listItemWidth / 3) - 4);
		resultEntry.addSubItem(resultBox);
		
		SimpleButton viewBtn = (SimpleButton) contentSystem
				.createContentItem(SimpleButton.class);
		viewBtn.setNote("viewBtn");
		viewBtn.setBorderSize(0);
		viewBtn.setAutoFitSize(false);
		viewBtn.setHeight(this.listItemHeight);
		viewBtn.setWidth((this.listItemWidth / 3) - 4);
		viewBtn.setText("View");
		resultEntry.addSubItem(viewBtn);
		viewBtn.setLocalLocation(viewBtn.getWidth(), 0);
		viewBtn.addButtonListener(new SimpleButtonAdapter() {
			
			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				for (Object item : listManager.getAllItems()) {
					ContentItem contentItem = listManager.getListItem(item);
					if ((contentItem != null) && (b.getParent() != null)) {
						if (contentItem.getName().equals(
								b.getParent().getName())) {
							if (item instanceof AssignmentInfo) {
								AssignmentInfo info = (AssignmentInfo) item;
								if (info.getHandwritingResult() != null) {
									viewAnswerDialog = new ViewAnswerDialog(
											contentSystem);
									viewAnswerDialog
											.getWindow()
											.setLocalLocation(
													targetList.getParent()
															.getParent()
															.getLocalLocation());
									viewAnswerDialog.getSketchPad().draw(
											info.getHandwritingResult());
									viewAnswerDialog.getWindow()
											.setAsTopObject();
								}
							}
							return;
						}
					}
				}
			}
		});

		int yShift = this.listHeight - 90;
		for (ContentItem contentItem : targetList
				.getAllItemsIncludeSystemItems()) {
			if (contentItem instanceof Window) {
				yShift -= ((Window) contentItem).getHeight();
			}
		}
		resultEntry.setBackgroundColour(Color.red);
		resultEntry.setLocalLocation((targetList.getWidth() / 2) + 30, yShift);
		targetList.addSubItem(resultEntry);
		resultEntry.setAsTopObject();
		return resultEntry;
	}
}
