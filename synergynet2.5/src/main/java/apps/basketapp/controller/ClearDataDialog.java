/*
 * Copyright (c) 2009 University of Durham, England
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'SynergyNet' nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package apps.basketapp.controller;

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;

import apps.basketapp.client.BasketApp;
import apps.basketapp.messages.ClearMessage;
import apps.basketapp.messages.UnicastClearMessage;
import apps.mathpadapp.util.MTFrame;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.DropDownList;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.TextLabel;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.RapidNetworkManager;


/**
 * The Class ClearDataDialog.
 */
public class ClearDataDialog extends MTFrame{

	/** The table ids. */
	protected HashMap<String,TableIdentity> tableIds;
	
	/** The table list. */
	protected DropDownList tableList;
	
	/**
	 * Instantiates a new clear data dialog.
	 *
	 * @param contentSystem the content system
	 */
	public ClearDataDialog(ContentSystem contentSystem) {
		super(contentSystem);
		this.setTitle("Clear Data Dialog");
		this.setWidth(400);
		this.setHeight(170);
		tableIds = new HashMap<String,TableIdentity>();
		TextLabel label = (TextLabel) contentSystem.createContentItem(TextLabel.class);
		label.setBorderSize(0);
		label.setBackgroundColour(this.getWindow().getBackgroundColour());
		label.setText("Clear Data on:");
		label.setLocation(- this.getWindow().getWidth()/2+ label.getWidth()/2 + 2*this.getWindow().getBorderSize(), 50);
		tableList = (DropDownList) contentSystem.createContentItem(DropDownList.class);
		tableList.setLocation(0,15);
		if(RapidNetworkManager.getTableCommsClientService() != null && !RapidNetworkManager.getTableCommsClientService().getCurrentlyOnline().isEmpty()){
			tableList.addListItem("All Tables", "All Tables");
			for(TableIdentity tableId: RapidNetworkManager.getTableCommsClientService().getCurrentlyOnline()){
				if(!tableId.equals(TableIdentity.getTableIdentity())){	
					tableList.addListItem(tableId.toString(), tableId.toString());
					tableIds.put(tableId.toString(), tableId);
				}
				tableList.setSelectedItem(tableList.getListItems().get(0));
			}
		}
		this.getWindow().addSubItem(tableList);
		SimpleButton okBtn = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		okBtn.setBorderSize(1);
		okBtn.setBorderColour(Color.black);
		okBtn.setBackgroundColour(Color.LIGHT_GRAY);
		okBtn.setTextColour(Color.black);
		okBtn.setAutoFitSize(false);
		okBtn.setWidth(65);
		okBtn.setHeight(25);
		okBtn.setText("OK");
		okBtn.setLocalLocation(-40, -50);
		okBtn.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonReleased(SimpleButton b, long id, float x, float y,
					float pressure) {
				String value = tableList.getSelectedValue();
				if(RapidNetworkManager.getTableCommsClientService() != null && value != null && value.equalsIgnoreCase("All Tables")){
					try {
						RapidNetworkManager.getTableCommsClientService().sendMessage(new ClearMessage(BasketApp.class));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else if(value != null && tableIds.get(value) != null && RapidNetworkManager.getTableCommsClientService() != null){
					try {
						RapidNetworkManager.getTableCommsClientService().sendMessage(new UnicastClearMessage(tableIds.get(value), BasketApp.class));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				ClearDataDialog.this.close();

			}
		});
		this.getWindow().addSubItem(okBtn);
		
		SimpleButton cancelBtn = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		cancelBtn.setBorderSize(1);
		cancelBtn.setBorderColour(Color.black);
		cancelBtn.setBackgroundColour(Color.LIGHT_GRAY);
		cancelBtn.setTextColour(Color.black);
		cancelBtn.setAutoFitSize(false);
		cancelBtn.setWidth(65);
		cancelBtn.setHeight(25);
		cancelBtn.setText("Cancel");
		cancelBtn.setLocalLocation(40, -50);
		cancelBtn.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonReleased(SimpleButton b, long id, float x, float y,
					float pressure) {
				ClearDataDialog.this.close();
			}
		});
		this.getWindow().addSubItem(cancelBtn);
		
		this.closeButton.removeButtonListeners();
		this.closeButton.addButtonListener(new SimpleButtonAdapter(){
			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				ClearDataDialog.this.close();
			}
		});
	}
}

