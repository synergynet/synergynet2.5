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

package apps.mysteriestableportal;

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;

import apps.mathpadapp.util.MTFrame;
import apps.mysteriestableportal.messages.ClearMessage;
import apps.mysteriestableportal.messages.TableDiscoveryMessage;
import apps.mysteriestableportal.messages.UnicastClearMessage;
import apps.remotecontrol.networkmanager.managers.NetworkedContentManager;
import apps.remotecontrol.networkmanager.managers.NetworkedContentManager.NetworkListener;
import apps.remotecontrol.networkmanager.messages.UnicastAlivePortalMessage;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.DropDownList;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.TextLabel;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.RapidNetworkManager;

public class ClearDataDialog extends MTFrame  implements NetworkListener{

	protected HashMap<String,TableIdentity> tableIds;
	protected DropDownList tableList;
	protected NetworkedContentManager manager;
	
	public ClearDataDialog(NetworkedContentManager manager, ContentSystem contentSystem) {
		super(contentSystem);
		this.manager = manager;
		this.setTitle("Clear Data Dialog");
		this.setWidth(400);
		this.setHeight(170);
		tableIds = new HashMap<String,TableIdentity>();
		if(manager != null) manager.addNetworkListener(this);
		for(Class<?> targetClass: RapidNetworkManager.getReceiverClasses())
			try {
				RapidNetworkManager.getTableCommsClientService().sendMessage(new TableDiscoveryMessage(targetClass));
			} catch (IOException e) {
				 
				e.printStackTrace();
			}
		TextLabel label = (TextLabel) contentSystem.createContentItem(TextLabel.class);
		label.setBorderSize(0);
		label.setBackgroundColour(this.getWindow().getBackgroundColour());
		label.setText("Clear Data on:");
		label.setLocation(- this.getWindow().getWidth()/2+ label.getWidth()/2 + 2*this.getWindow().getBorderSize(), 50);
		tableList = (DropDownList) contentSystem.createContentItem(DropDownList.class);
		tableList.setLocation(0,15);
		if(manager != null && !manager.getOnlineTables().isEmpty()){
			tableList.addListItem("All Tables", "All Tables");
			tableList.setSelectedItem(tableList.getListItems().get(0));
		}		this.getWindow().addSubItem(label);
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
				if(value != null && value.equalsIgnoreCase("All Tables")){
					try {
						RapidNetworkManager.getTableCommsClientService().sendMessage(new ClearMessage(MysteriesPortalClientApp.class));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else if(value != null && tableIds.get(value) != null && ClearDataDialog.this.manager != null){
					try {
						RapidNetworkManager.getTableCommsClientService().sendMessage(new UnicastClearMessage(tableIds.get(value), MysteriesPortalClientApp.class));
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

	@Override
	public void close(){
		if(manager != null) manager.removeNetworkListener(ClearDataDialog.this);
		super.close();
	}
	
	@Override
	public void messageReceived(Object obj) {
		
		if(obj instanceof UnicastAlivePortalMessage){
			TableIdentity tableId = ((UnicastAlivePortalMessage)obj).getSender();
			if(!tableList.containsValue(tableId.toString())) tableList.addListItem(tableId.toString(), tableId.toString());
			if(!tableIds.containsKey(tableId.toString())) tableIds.put(tableId.toString(),tableId);
		}
	}

}

