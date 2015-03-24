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
import java.util.ArrayList;
import java.util.List;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.TextLabel.Alignment;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import apps.mathpadapp.MathPadResources;
import apps.mathpadapp.controllerapp.usercontroller.UserInfo;
import apps.mathpadapp.util.MTDialog;
import apps.mathpadapp.util.MTList;

/**
 * The Class SendToDialog.
 */
public class SendToDialog extends MTDialog {

	/** The options. */
	private SimpleButton[] options = new SimpleButton[3];

	/** The selected option. */
	private int selectedOption = 0;

	/** The table list panel. */
	private MTList tableListPanel;

	/** The user list panel. */
	private MTList userListPanel;

	/**
	 * Instantiates a new send to dialog.
	 *
	 * @param assignmentBuilder
	 *            the assignment builder
	 * @param contentSystem
	 *            the content system
	 */
	public SendToDialog(final AssignmentBuilder assignmentBuilder,
			final ContentSystem contentSystem) {
		super(assignmentBuilder, contentSystem);
		this.setTitle("Send To");
		this.setWidth(450);
		this.setHeight(560);
		this.setModal(true);
		
		tableListPanel = new MTList(contentSystem);
		this.getWindow().addSubItem(tableListPanel.getContainer());
		
		int y = (window.getHeight() / 2) - 30;
		
		for (int i = 0; i < options.length; i++) {
			options[i] = (SimpleButton) contentSystem
					.createContentItem(SimpleButton.class);
			options[i].setNote(String.valueOf(i));
			options[i].setAutoFitSize(false);
			options[i].setWidth(200);
			options[i].setHeight(20);
			options[i].drawImage(
					MathPadResources.class.getResource("buttons/radio.jpg"), 0,
					0, 20, 20);
			options[i].setFont(new Font("Times New Roman", Font.PLAIN, 12));
			options[i].setBorderSize(0);
			options[i].setText("               Option " + i);
			options[i].setAlignment(Alignment.LEFT);
			options[i].setBorderSize(0);
			options[i].addButtonListener(new SimpleButtonAdapter() {
				
				@Override
				public void buttonReleased(SimpleButton b, long id, float x,
						float y, float pressure) {
					SendToDialog.this.setSelection(Integer.parseInt(b.getNote()));
				}
			});
			options[i]
					.setLocalLocation(
							(-window.getWidth() / 2)
									+ (options[i].getWidth() / 2) + 20, y);

			this.getWindow().addSubItem(options[i]);
			y -= 30;
		}

		options[0].setText("             Whole class");
		options[1].setText("             Selected Tables");
		tableListPanel.setWidth(300);
		tableListPanel.setHeight(200);
		tableListPanel.getContainer().setLocalLocation(0, 70);
		options[2].setText("             Selected Students");
		options[2].setLocalLocation(
				(-window.getWidth() / 2) + (options[2].getWidth() / 2) + 20,
				-15);

		userListPanel = new MTList(contentSystem);
		window.addSubItem(userListPanel.getContainer());
		userListPanel.setWidth(300);
		userListPanel.setHeight(200);

		userListPanel.getContainer().setLocalLocation(0, -165);

		final SimpleButton sendBtn = (SimpleButton) contentSystem
				.createContentItem(SimpleButton.class);
		sendBtn.setText(" Send ");
		sendBtn.setBackgroundColour(Color.DARK_GRAY);
		sendBtn.setTextColour(Color.WHITE);
		sendBtn.setLocalLocation(0, -255);
		sendBtn.addButtonListener(new SimpleButtonAdapter() {
			@Override
			public void buttonPressed(SimpleButton b, long id, float x,
					float y, float pressure) {
				sendBtn.setBackgroundColour(Color.WHITE);
				sendBtn.setTextColour(Color.DARK_GRAY);
			}
			
			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				sendBtn.setBackgroundColour(Color.DARK_GRAY);
				sendBtn.setTextColour(Color.WHITE);

				List<Object> receipents = new ArrayList<Object>();
				if (SendToDialog.this.selectedOption == 0) {
					receipents.add("ALL");
				} else if (SendToDialog.this.selectedOption == 1) {
					for (Object item : tableListPanel.getManager()
							.getSelectedItems()) {
						TableIdentity tableId = (TableIdentity) item;
						if (!receipents.contains(tableId)) {
							receipents.add(tableId);
						}
					}
				} else if (SendToDialog.this.selectedOption == 2) {
					for (Object item : userListPanel.getManager()
							.getSelectedItems()) {
						UserInfo userInfo = (UserInfo) item;
						if (!receipents.contains(userInfo.getUserIdentity())) {
							receipents.add(userInfo);
						}
					}
				}
				SendToDialog.this.close();
				assignmentBuilder.fireSend(receipents);
			}
			
		});

		userListPanel.getManager().addItem("temp", "temp");
		userListPanel.getManager().deleteAllItems();
		tableListPanel.getManager().addItem("temp", "temp");
		tableListPanel.getManager().deleteAllItems();

		this.getWindow().addSubItem(sendBtn);
		this.setSelection(0);
	}

	/**
	 * Gets the table list panel.
	 *
	 * @return the table list panel
	 */
	public MTList getTableListPanel() {
		return tableListPanel;
	}
	
	/**
	 * Gets the user list panel.
	 *
	 * @return the user list panel
	 */
	public MTList getUserListPanel() {
		return userListPanel;
	}

	/**
	 * Sets the selection.
	 *
	 * @param selection
	 *            the new selection
	 */
	private void setSelection(int selection) {
		selectedOption = selection;
		if (selectedOption == 0) {
			userListPanel.getManager().setEnabled(false);
			tableListPanel.getManager().setEnabled(false);
		} else if (selectedOption == 1) {
			tableListPanel.getManager().setEnabled(true);
			userListPanel.getManager().setEnabled(false);
		} else if (selectedOption == 2) {
			userListPanel.getManager().setEnabled(true);
			tableListPanel.getManager().setEnabled(false);
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
	}
}
