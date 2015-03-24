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

package apps.remotecontrol.tableportal;

import java.awt.Color;

import apps.mathpadapp.util.MTDialog;
import apps.remotecontrol.networkmanager.managers.syncmanager.SyncManager;
import apps.remotecontrol.networkmanager.messages.RequestSyncItemsPortalMessage;

import com.jme.math.FastMath;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.DropDownList;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.TextLabel;
import synergynetframework.appsystem.contentsystem.items.DropDownList.DropDownListItem;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;


/**
 * The Class CustomiseViewDialog.
 */
public class CustomiseViewDialog extends MTDialog{

	/** The angle list. */
	protected DropDownList angleList;
	
	/** The scale factor list. */
	protected DropDownList scaleFactorList;
	
	/** The portal. */
	protected TablePortal portal;
	
	/**
	 * Instantiates a new customise view dialog.
	 *
	 * @param portal the portal
	 * @param contentSystem the content system
	 */
	public CustomiseViewDialog(final TablePortal portal, ContentSystem contentSystem) {
		super(portal, contentSystem);
		this.setModal(true);
		this.setTitle("View Config Dialog");
		this.setWidth(350);
		this.setHeight(260);
		this.portal = portal;
		TextLabel label1 = (TextLabel) contentSystem.createContentItem(TextLabel.class);
		label1.setBorderSize(0);
		label1.setBackgroundColour(this.getWindow().getBackgroundColour());
		label1.setText("Viewing Options:");
		label1.setLocation(- this.getWindow().getWidth()/2+ label1.getWidth()/2 + 2*this.getWindow().getBorderSize(), 80);

		TextLabel label2 = (TextLabel) contentSystem.createContentItem(TextLabel.class);
		label2.setBorderSize(0);
		label2.setBackgroundColour(this.getWindow().getBackgroundColour());
		label2.setText("View Angle");
		label2.setLocation(- this.getWindow().getWidth()/2+ label1.getWidth()/2 + 2*this.getWindow().getBorderSize()+10, 40);

		TextLabel label3 = (TextLabel) contentSystem.createContentItem(TextLabel.class);
		label3.setBorderSize(0);
		label3.setBackgroundColour(this.getWindow().getBackgroundColour());
		label3.setText("Scale Factor");
		label3.setLocation(- this.getWindow().getWidth()/2+ label1.getWidth()/2 + 2*this.getWindow().getBorderSize()+15, -5);

		SimpleButton clearTraces = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		clearTraces.setText("Clear Traces");
		clearTraces.setLocation(- this.getWindow().getWidth()/2+ label1.getWidth()/2 + 2*this.getWindow().getBorderSize()+15, -50);
		clearTraces.addButtonListener(new SimpleButtonAdapter(){

			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				portal.getDisplayPanel().clearTraces();
			}
			
		});
		
		angleList = (DropDownList) contentSystem.createContentItem(DropDownList.class);
		angleList.setLocation(30,40);
		angleList.setWidth(130);
		angleList.addListItem("Default", "Default");
		angleList.addListItem("0 degrees", "0");
		angleList.addListItem("90 degrees", "90");
		angleList.addListItem("180 degrees", "180");
		angleList.addListItem("270 degrees", "270");
		float angleMaskValue = CustomiseViewDialog.this.portal.displayPanel.syncRenderer.getMaskValue(SyncManager.SYNC_ITEM_ANGLE);
		if(angleMaskValue != -1.0f){	
			for(DropDownListItem listItem: angleList.getListItems()){
				if(!listItem.getValue().equalsIgnoreCase("Default") && Float.parseFloat(listItem.getValue()) == FastMath.RAD_TO_DEG * angleMaskValue){
					angleList.setSelectedItem(listItem);
				}
			}
		}
		else{
			angleList.setSelectedItem(angleList.getListItems().get(0));
		}		
		scaleFactorList = (DropDownList) contentSystem.createContentItem(DropDownList.class);
		scaleFactorList.setLocation(30,-5);
		scaleFactorList.setWidth(130);
		scaleFactorList.addListItem("Default", "Default");
		scaleFactorList.addListItem("1:1", "1");
		scaleFactorList.addListItem("2:1", "2");
		scaleFactorList.addListItem("3:1", "3");
		scaleFactorList.addListItem("5:1", "5");
		scaleFactorList.addListItem("0.5:1", "0.5");
		
		float scaleMaskValue = CustomiseViewDialog.this.portal.displayPanel.syncRenderer.getMaskValue(SyncManager.SYNC_ITEM_SCALE);
		if(scaleMaskValue != -1.0f){	
			for(DropDownListItem listItem: scaleFactorList.getListItems()){
				if(!listItem.getValue().equalsIgnoreCase("Default") && Float.parseFloat(listItem.getValue()) == scaleMaskValue){
					scaleFactorList.setSelectedItem(listItem);
				}
			}
		}
		else{
			scaleFactorList.setSelectedItem(scaleFactorList.getListItems().get(0));
		}
		
		
		this.getWindow().addSubItem(label1);
		this.getWindow().addSubItem(label2);
		this.getWindow().addSubItem(label3);
		this.getWindow().addSubItem(angleList);
		this.getWindow().addSubItem(scaleFactorList);
		this.getWindow().addSubItem(clearTraces);
		
		SimpleButton okBtn = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		okBtn.setBorderSize(1);
		okBtn.setBorderColour(Color.black);
		okBtn.setBackgroundColour(Color.LIGHT_GRAY);
		okBtn.setTextColour(Color.black);
		okBtn.setAutoFitSize(false);
		okBtn.setWidth(65);
		okBtn.setHeight(25);
		okBtn.setText("OK");
		okBtn.setLocalLocation(-40, -100);
		okBtn.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonReleased(SimpleButton b, long id, float x, float y,
					float pressure) {
				String value = angleList.getSelectedValue();
				if(value.equalsIgnoreCase("Default")){
					CustomiseViewDialog.this.portal.displayPanel.syncRenderer.removeDataMask(SyncManager.SYNC_ITEM_ANGLE);
					for(Class<?> targetClass: CustomiseViewDialog.this.portal.networkManager.getReceiverClasses())
						CustomiseViewDialog.this.portal.networkManager.sendMessage(new RequestSyncItemsPortalMessage(targetClass, CustomiseViewDialog.this.portal.remoteTableId));
				}else{
					float radAngle = FastMath.DEG_TO_RAD * Float.parseFloat(value);
					CustomiseViewDialog.this.portal.displayPanel.syncRenderer.setDataMask(SyncManager.SYNC_ITEM_ANGLE, radAngle);
					for(ContentItem item: CustomiseViewDialog.this.portal.displayPanel.onlineItemsList.values()) item.setAngle(radAngle);
				}

				value = scaleFactorList.getSelectedValue();
				if(value.equalsIgnoreCase("Default")){
					CustomiseViewDialog.this.portal.displayPanel.syncRenderer.removeDataMask(SyncManager.SYNC_ITEM_SCALE);
					if(CustomiseViewDialog.this.portal.networkManager != null){	
						for(Class<?> targetClass: CustomiseViewDialog.this.portal.networkManager.getReceiverClasses())
							CustomiseViewDialog.this.portal.networkManager.sendMessage(new RequestSyncItemsPortalMessage(targetClass, CustomiseViewDialog.this.portal.remoteTableId));
					}
				}else{
					float scaleFactor = Float.parseFloat(value);
					CustomiseViewDialog.this.portal.displayPanel.syncRenderer.setDataMask(SyncManager.SYNC_ITEM_SCALE, scaleFactor);
					for(ContentItem item: CustomiseViewDialog.this.portal.displayPanel.onlineItemsList.values()) item.setScale(scaleFactor);
				}
				
				CustomiseViewDialog.this.close();
				
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
		cancelBtn.setLocalLocation(40, -100);
		cancelBtn.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonReleased(SimpleButton b, long id, float x, float y,
					float pressure) {
				CustomiseViewDialog.this.close();
			}
		});
		this.getWindow().addSubItem(cancelBtn);
		
		this.closeButton.removeButtonListeners();
		this.closeButton.addButtonListener(new SimpleButtonAdapter(){
			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				CustomiseViewDialog.this.close();
			}
		});
	}
}
