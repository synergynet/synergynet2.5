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

package apps.mathpadapp.networkmanager.managers.syncmanager;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import apps.mathpadapp.controllerapp.assignmentcontroller.AssignmentInfo;
import apps.mathpadapp.mathtool.MathTool;
import apps.mathpadapp.mathtool.MathTool.MathToolListener;
import apps.mathpadapp.mathtool.MathTool.SeparatorState;
import apps.mathpadapp.mathtool.MathTool.WritingState;
import apps.mathpadapp.mathtool.MathToolControlPanel.ControlPanelListener;
import apps.mathpadapp.networkmanager.managers.NetworkedContentManager;
import apps.mathpadapp.networkmanager.messages.common.BroadcastMathPadSyncMessage;
import apps.mathpadapp.networkmanager.messages.common.UnicastMathPadSyncMessage;
import apps.mathpadapp.networkmanager.utils.UserIdentity;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.MathPad;
import synergynetframework.appsystem.contentsystem.items.SketchPad.DrawListener;
import synergynetframework.appsystem.contentsystem.items.listener.BringToTopListener;
import synergynetframework.appsystem.contentsystem.items.listener.OrthoControlPointRotateTranslateScaleListener;
import synergynetframework.appsystem.contentsystem.jme.items.utils.DrawData;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.jme.cursorsystem.elements.twod.OrthoBringToTop;

public class SyncManager {

	private HashMap<UserIdentity, HashMap<Short,Object>> syncData = new HashMap<UserIdentity, HashMap<Short,Object>>();
	
	protected List<TableIdentity> syncTables = new ArrayList<TableIdentity>();
	protected HashMap<UserIdentity, TableIdentity> syncUsers = new HashMap<UserIdentity, TableIdentity>();

	protected boolean isBroadcastSynchronisationOn = false;

	protected NetworkedContentManager networkManager;
	protected MathToolSyncRenderer syncRenderer;
	
	public SyncManager(NetworkedContentManager networkManager){
		this.networkManager = networkManager;
		syncRenderer = new MathToolSyncRenderer(this);
	}
	
	public void addSyncListeners(final UserIdentity userId) {
		if(!networkManager.getRegisteredMathPads().containsKey(userId)) return;
		MathTool tool = networkManager.getRegisteredMathPads().get(userId);
		for(MathPad pad: tool.getAllPads()) this.registerPad(userId, pad);
		tool.getWindow().addOrthoControlPointRotateTranslateScaleListener(new OrthoControlPointRotateTranslateScaleListener(){
			@Override
			public void itemRotated(ContentItem item, float newAngle, float oldAngle) {
				if(syncData.containsKey(userId)){
					syncData.get(userId).put(SyncRenderer.SYNC_ANGLE, newAngle);
				}else{
					HashMap<Short, Object> list = new HashMap<Short, Object>();
					list.put(SyncRenderer.SYNC_ANGLE, newAngle);
					syncData.put(userId, list);
				}			
			}
			@Override
			public void itemScaled(ContentItem item, float newScaleFactor,	float oldScaleFactor) {
				if(syncData.containsKey(userId)){
					syncData.get(userId).put(SyncRenderer.SYNC_SCALE, newScaleFactor);
				}else{
					HashMap<Short, Object> list = new HashMap<Short, Object>();
					list.put(SyncRenderer.SYNC_SCALE, newScaleFactor);
					syncData.put(userId, list);
				}
			}
			@Override
			public void itemTranslated(ContentItem item, float newLocationX,
					float newLocationY, float oldLocationX, float oldLocationY) {
				if(syncData.containsKey(userId)){
					syncData.get(userId).put(SyncRenderer.SYNC_LOCATION_X, newLocationX);
					syncData.get(userId).put(SyncRenderer.SYNC_LOCATION_Y, newLocationY);
				}else{
					HashMap<Short, Object> list = new HashMap<Short, Object>();
					list.put(SyncRenderer.SYNC_LOCATION_X, newLocationX);
					list.put(SyncRenderer.SYNC_LOCATION_Y, newLocationY);
					syncData.put(userId, list);
				}
			}
		});
		
		tool.getWindow().addBringToTopListener(new BringToTopListener(){
			@Override
			public void itemBringToToped(ContentItem item) {
				if(syncData.containsKey(userId)){
					syncData.get(userId).put(SyncRenderer.SYNC_ORDER, OrthoBringToTop.topMost);
				}else{
					HashMap<Short, Object> list = new HashMap<Short, Object>();
					list.put(SyncRenderer.SYNC_ORDER, OrthoBringToTop.topMost);
					syncData.put(userId, list);
				}
			}
		});

		tool.addMathToolListener(new MathToolListener(){
			@Override
			public void separatorChanged(SeparatorState newState) {
				if(syncData.containsKey(userId)){
					syncData.get(userId).put(MathToolSyncRenderer.SYNC_SEPARATOR_STATE, newState);
				}else{
					HashMap<Short, Object> list = new HashMap<Short, Object>();
					list.put(MathToolSyncRenderer.SYNC_SEPARATOR_STATE, newState);
					syncData.put(userId, list);
				}
			}

			@Override
			public void mathPadClosed(MathTool tool) {
				tool.getWindow().removeOrthoControlPointRotateTranslateScaleListeners();
				tool.getWindow().removeBringToTopListeners();
				tool.removeMathToolListeners();
				tool.getControlPanel().removeControlPanelListeners();
				networkManager.unregisterMathPad(userId);
			}

			@Override
			public void assignmentAnswerReady(AssignmentInfo info) {
				 
				
			}

			@Override
			public void userLogin(String userName, String password) {
				 
				
			}
		});
		
		tool.getControlPanel().addControlPanelListener(new ControlPanelListener(){
			@Override
			public void lineWidthChanged(float lineWidth) {
				if(syncData.containsKey(userId)){
					syncData.get(userId).put(MathToolSyncRenderer.SYNC_LINE_WIDTH, lineWidth);
				}else{
					HashMap<Short, Object> list = new HashMap<Short, Object>();
					list.put(MathToolSyncRenderer.SYNC_LINE_WIDTH, lineWidth);
					syncData.put(userId, list);
				}
			}
			@Override
			public void padCleared() {
				if(syncData.containsKey(userId)){
					syncData.get(userId).put(MathToolSyncRenderer.SYNC_PAD_CLEARED, null);
				}else{
					HashMap<Short, Object> list = new HashMap<Short, Object>();
					list.put(MathToolSyncRenderer.SYNC_PAD_CLEARED, null);
					syncData.put(userId, list);
				}
			}
			@Override
			public void textColorChanged(Color textColor) {
				if(syncData.containsKey(userId)){
					syncData.get(userId).put(MathToolSyncRenderer.SYNC_TEXT_COLOR, textColor);
				}else{
					HashMap<Short, Object> list = new HashMap<Short, Object>();
					list.put(MathToolSyncRenderer.SYNC_TEXT_COLOR, textColor);
					syncData.put(userId, list);
				}
			}
			@Override
			public void writingStateChanged(WritingState writingState) {
				if(syncData.containsKey(userId)){
					syncData.get(userId).put(MathToolSyncRenderer.SYNC_WRITING_STATE, writingState);
				}else{
					HashMap<Short, Object> list = new HashMap<Short, Object>();
					list.put(MathToolSyncRenderer.SYNC_WRITING_STATE, writingState);
					syncData.put(userId, list);
				}
			}
			@Override
			public void padCreated(MathPad pad) {
				SyncManager.this.registerPad(userId, pad);
				if(syncData.containsKey(userId)){
					syncData.get(userId).put(MathToolSyncRenderer.SYNC_NEW_PAD, null);
				}else{
					HashMap<Short, Object> list = new HashMap<Short, Object>();
					list.put(MathToolSyncRenderer.SYNC_NEW_PAD, null);
					syncData.put(userId, list);
				}
			}
			@Override
			public void padChanged(int padIndex) {
				if(syncData.containsKey(userId)){
					syncData.get(userId).put(MathToolSyncRenderer.SYNC_PAD_CHANGED, padIndex);
				}else{
					HashMap<Short, Object> list = new HashMap<Short, Object>();
					list.put(MathToolSyncRenderer.SYNC_PAD_CHANGED, padIndex);
					syncData.put(userId, list);
				}
			}
			@Override
			public void answerPadDisplayed() {
				
			}
			@Override
			public void padRemoved(int padIndex) {
				if(syncData.containsKey(userId)){
					syncData.get(userId).put(MathToolSyncRenderer.SYNC_PAD_REMOVED, padIndex);
				}else{
					HashMap<Short, Object> list = new HashMap<Short, Object>();
					list.put(MathToolSyncRenderer.SYNC_PAD_REMOVED, padIndex);
					syncData.put(userId, list);
				}
			}
		});
	}
	
	public void registerPad(final UserIdentity userId, MathPad pad){
		pad.addDrawListener(new DrawListener(){
			@Override
			public void itemDrawn(DrawData drawData) {
					if(syncData.containsKey(userId)){
						syncData.get(userId).put(MathToolSyncRenderer.SYNC_DRAW_DATA, drawData);
					}else{
						HashMap<Short, Object> list = new HashMap<Short, Object>();
						list.put(MathToolSyncRenderer.SYNC_DRAW_DATA, drawData);
						syncData.put(userId, list);
					}
			}

			@Override
			public void padCleared() {
				 
				
			}
		});
	}
	
	public void enableBroadcastSync(boolean isBroadcastSynchronisationOn) {
		this.isBroadcastSynchronisationOn = isBroadcastSynchronisationOn;
	}

	
	public boolean isBroadcastSynchronisationOn(){
		return isBroadcastSynchronisationOn;
	}
	
	public void enableUnicastTableSync(TableIdentity syncWithTable, boolean isUnicastSynchronisationOn) {
		if(isUnicastSynchronisationOn){
			if(!syncTables.contains(syncWithTable)) syncTables.add(syncWithTable);
		}
		else
			syncTables.remove(syncWithTable);
	}
	
	public void enableUnicastUserSync(TableIdentity syncWithTable, UserIdentity syncWithUser, boolean isUnicastSynchronisationOn) {
		if(isUnicastSynchronisationOn){
			if(!syncUsers.containsKey(syncWithUser)) syncUsers.put(syncWithUser, syncWithTable);
		}
		else
			syncUsers.remove(syncWithUser);
	}
	
	public boolean isUnicastTableSynchronisationOn(TableIdentity syncWithTable){
		return syncTables.contains(syncWithTable);
	}
	
	public boolean isUnicastUserSynchronisationOn(UserIdentity syncWithUser){
		return syncUsers.containsKey(syncWithUser);
	}
	
	public void syncContent(TableIdentity sender, HashMap<UserIdentity, HashMap<Short, Object>> syncData) {
		for(UserIdentity userId: syncData.keySet()){
				MathTool tool = networkManager.getRegisteredMathPads().get(userId);
				if(tool != null) syncRenderer.renderSyncData(userId, tool, syncData.get(userId));
			}
	}
	
	public void update(float tpf) {
		if(this.isBroadcastSynchronisationOn && !syncData.isEmpty()){
			for(Class<?> receiverClass: networkManager.getReceiverClasses()){
				BroadcastMathPadSyncMessage msg = new BroadcastMathPadSyncMessage(receiverClass, syncData);
				networkManager.sendMessage(msg);
			}
		}else{ 
			if(!syncTables.isEmpty() && !syncData.isEmpty()){
				for(TableIdentity tableId: syncTables){
					for(Class<?> receiverClass: networkManager.getReceiverClasses()){
						UnicastMathPadSyncMessage msg = new UnicastMathPadSyncMessage(receiverClass, syncData, tableId);
						networkManager.sendMessage(msg);
					}
				}
			}
			if(!syncUsers.isEmpty() && !syncData.isEmpty()){
				for(UserIdentity userId: syncUsers.keySet()){
					if(syncData.containsKey(userId) && !syncTables.contains(syncUsers.get(userId))){
						for(Class<?> receiverClass: networkManager.getReceiverClasses()){
							UnicastMathPadSyncMessage msg = new UnicastMathPadSyncMessage(receiverClass, syncData, syncUsers.get(userId));
							networkManager.sendMessage(msg);
						}
					}
				}
			}
		}
		syncData.clear();
	}
}
