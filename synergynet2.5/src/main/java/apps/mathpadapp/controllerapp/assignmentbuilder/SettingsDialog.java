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

package apps.mathpadapp.controllerapp.assignmentbuilder;

import java.awt.Color;
import java.awt.Font;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.DropDownList;
import synergynetframework.appsystem.contentsystem.items.DropDownList.DropDownListItem;
import synergynetframework.appsystem.contentsystem.items.DropDownList.DropDownListListener;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.TextLabel;
import synergynetframework.appsystem.contentsystem.items.TextLabel.Alignment;
import synergynetframework.appsystem.contentsystem.items.Window;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;
import apps.mathpadapp.MathPadResources;

/**
 * The Class SettingsDialog.
 */
public class SettingsDialog {

	/** The duration list. */
	private DropDownList durationList;

	/** The options. */
	private SimpleButton[] options = new SimpleButton[2];

	/** The time duration. */
	private int timeDuration = -1;

	/** The top panel. */
	private Window topPanel;

	/** The window. */
	private Window window;
	
	/**
	 * Instantiates a new settings dialog.
	 *
	 * @param assignmentBuilder
	 *            the assignment builder
	 * @param contentSystem
	 *            the content system
	 */
	public SettingsDialog(final AssignmentBuilder assignmentBuilder,
			final ContentSystem contentSystem) {
		window = (Window) contentSystem.createContentItem(Window.class);
		window.setWidth(400);
		window.setHeight(300);

		topPanel = (Window) contentSystem.createContentItem(Window.class);
		topPanel.setBackgroundColour(Color.DARK_GRAY);
		topPanel.setWidth(window.getWidth());
		topPanel.setHeight(30);
		topPanel.setLocalLocation(0,
				((window.getHeight() / 2) + (topPanel.getHeight() / 2))
						- topPanel.getBorderSize());

		window.addSubItem(topPanel);

		SimpleButton closeButton = (SimpleButton) contentSystem
				.createContentItem(SimpleButton.class);
		closeButton.setAutoFitSize(false);
		closeButton.setWidth(18);
		closeButton.setHeight(18);
		closeButton.addButtonListener(new SimpleButtonAdapter() {
			
			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				contentSystem.removeContentItem(window);
			}

		});
		closeButton.setBorderSize(2);
		closeButton.setLocalLocation(
				(topPanel.getWidth() / 2) - (closeButton.getWidth() / 2)
						- topPanel.getBorderSize() - 3,
				(topPanel.getHeight() / 2) - (closeButton.getHeight() / 2) - 6);
		topPanel.addSubItem(closeButton);

		TextLabel label01 = (TextLabel) contentSystem
				.createContentItem(TextLabel.class);
		label01.setBorderSize(0);
		label01.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		label01.setBackgroundColour(window.getBackgroundColour());
		label01.setTextColour(Color.black);
		label01.setText("Assignment Duration:");
		label01.setLocalLocation((-window.getWidth() / 2)
				+ (label01.getWidth() / 2) + 10, 100);
		window.addSubItem(label01);

		int y = 60;
		for (int i = 0; i < options.length; i++) {
			options[i] = (SimpleButton) contentSystem
					.createContentItem(SimpleButton.class);
			options[i].setNote(String.valueOf(i));
			options[i].setAutoFitSize(false);
			options[i].setWidth(100);
			options[i].setHeight(20);
			options[i].drawImage(
					MathPadResources.class.getResource("buttons/radio.jpg"), 0,
					0, 20, 20);
			options[i].setFont(new Font("Times New Roman", Font.PLAIN, 12));
			options[i].setBorderSize(0);
			options[i].setAlignment(Alignment.LEFT);
			options[i].setBorderSize(0);
			options[i].addButtonListener(new SimpleButtonAdapter() {
				
				@Override
				public void buttonReleased(SimpleButton b, long id, float x,
						float y, float pressure) {
					SettingsDialog.this.setSelection(Integer.parseInt(b
							.getNote()));
				}
			});
			options[i]
					.setLocalLocation(
							(-window.getWidth() / 2)
									+ (options[i].getWidth() / 2) + 20, y);
			window.addSubItem(options[i]);
			y -= 30;
		}

		options[0].setText("               Open ");
		options[1].setText("               Specified ");

		durationList = (DropDownList) contentSystem
				.createContentItem(DropDownList.class);
		durationList.setWidth(100);
		durationList.setBorderColour(Color.black);
		durationList.setBorderSize(1);
		durationList.setItemHeight(25);
		durationList.addListItem("1 min", "1");
		durationList.addListItem("2 min", "2");
		durationList.addListItem("3 min", "3");
		durationList.addListItem("5 min", "5");
		durationList.addListItem("10 min", "10");
		durationList.addListItem("15 min", "15");
		durationList.addListItem("30 min", "30");
		durationList.addListItem("45 min", "45");
		durationList.addListItem("1 hour", "60");
		durationList.setSelectedItem(durationList.getListItems().get(3));
		durationList.addDropDownListListener(new DropDownListListener() {
			
			@Override
			public void itemSelected(DropDownListItem item) {
				SettingsDialog.this.setSelection(1);
			}

		});
		durationList.setLocalLocation(options[1].getLocalLocation().x + 200,
				y + 30);
		window.addSubItem(durationList);

		final SimpleButton okBtn = (SimpleButton) contentSystem
				.createContentItem(SimpleButton.class);
		okBtn.setText(" OK ");
		okBtn.setBackgroundColour(Color.DARK_GRAY);
		okBtn.setTextColour(Color.WHITE);
		okBtn.setLocalLocation(0, (-window.getHeight() / 2) + okBtn.getWidth());
		okBtn.addButtonListener(new SimpleButtonAdapter() {
			
			@Override
			public void buttonPressed(SimpleButton b, long id, float x,
					float y, float pressure) {
				okBtn.setBackgroundColour(Color.WHITE);
				okBtn.setTextColour(Color.DARK_GRAY);
			}
			
			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				okBtn.setBackgroundColour(Color.DARK_GRAY);
				okBtn.setTextColour(Color.WHITE);

				assignmentBuilder.getAssignmentHandler().getAssignment()
						.setTime(timeDuration);
			}
		});
		window.addSubItem(okBtn);
		window.setOrder(-1);
		this.setSelection(0);
	}

	/**
	 * Gets the window.
	 *
	 * @return the window
	 */
	public Window getWindow() {
		return window;
	}
	
	/**
	 * Sets the selection.
	 *
	 * @param selectedOption
	 *            the new selection
	 */
	protected void setSelection(int selectedOption) {
		if (selectedOption == 0) {
			timeDuration = -1;
			durationList.setRotateTranslateScalable(false);
			for (ContentItem item : durationList
					.getAllItemsIncludeSystemItems()) {
				item.setBackgroundColour(Color.LIGHT_GRAY);
			}
		} else if (selectedOption == 1) {
			durationList.setRotateTranslateScalable(true);
			for (ContentItem item : durationList
					.getAllItemsIncludeSystemItems()) {
				item.setBackgroundColour(Color.white);
			}
			timeDuration = Integer.parseInt(durationList.getSelectedValue());
		}

		for (SimpleButton option : options) {
			option.removeAllImages();
			if (Integer.parseInt(option.getNote()) == selectedOption) {
				option.drawImage(MathPadResources.class
						.getResource("buttons/radio_selected.jpg"), 0, 0, 20,
						20);
			} else {
				option.drawImage(
						MathPadResources.class.getResource("buttons/radio.jpg"),
						0, 0, 20, 20);
			}
		}
		System.out.println(timeDuration);
	}
}
