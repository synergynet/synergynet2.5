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
import java.lang.reflect.Field;
import java.util.HashMap;

import apps.mathpadapp.mathtool.MathTool;
import apps.mathpadapp.mathtool.MathTool.SeparatorState;
import apps.mathpadapp.mathtool.MathTool.WritingState;
import apps.mathpadapp.networkmanager.utils.UserIdentity;

import synergynetframework.appsystem.contentsystem.items.MathPad;
import synergynetframework.appsystem.contentsystem.jme.items.utils.DrawData;


/**
 * The Class MathToolSyncRenderer.
 */
public class MathToolSyncRenderer extends SyncRenderer{
	
	/** The Constant SYNC_DRAW_DATA. */
	public static final short SYNC_DRAW_DATA = 5;
	
	/** The Constant SYNC_WRITING_STATE. */
	public static final short SYNC_WRITING_STATE = 6;
	
	/** The Constant SYNC_SEPARATOR_STATE. */
	public static final short SYNC_SEPARATOR_STATE = 7;
	
	/** The Constant SYNC_TEXT_COLOR. */
	public static final short SYNC_TEXT_COLOR = 8;
	
	/** The Constant SYNC_BACKGROUND_COLOR. */
	public static final short SYNC_BACKGROUND_COLOR = 9;
	
	/** The Constant SYNC_LINE_WIDTH. */
	public static final short SYNC_LINE_WIDTH = 10;
	
	/** The Constant SYNC_PAD_CLEARED. */
	public static final short SYNC_PAD_CLEARED = 11;
	
	/** The Constant SYNC_NEW_PAD. */
	public static final short SYNC_NEW_PAD = 12;
	
	/** The Constant SYNC_PAD_CHANGED. */
	public static final short SYNC_PAD_CHANGED = 13;
	
	/** The Constant SYNC_PAD_DELETED. */
	public static final short SYNC_PAD_DELETED = 14;
	
	/** The Constant SYNC_ANSWER_PAD_DISPLAYED. */
	public static final short SYNC_ANSWER_PAD_DISPLAYED = 15;
	
	/** The Constant SYNC_PAD_REMOVED. */
	public static final short SYNC_PAD_REMOVED = 16;

	
	/** The sync manager. */
	protected SyncManager syncManager;
	
	/**
	 * Instantiates a new math tool sync renderer.
	 *
	 * @param syncManager the sync manager
	 */
	public MathToolSyncRenderer(SyncManager syncManager){
		super();
		this.syncManager = syncManager;
		Field[] fields = MathToolSyncRenderer.class.getFields();
		for(Field field: fields){
			try {
				if(field.getName().startsWith("SYNC")){
					if(!allowedSync.contains(field.getShort(null))) allowedSync.add(field.getShort(null));
				}
			} catch (IllegalArgumentException e) {
				 
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				 
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Render sync data.
	 *
	 * @param userId the user id
	 * @param tool the tool
	 * @param syncData the sync data
	 */
	public void renderSyncData(UserIdentity userId, MathTool tool, HashMap<Short, Object> syncData){
		super.renderSyncData(tool.getWindow(), syncData);
		for(short paramName: syncData.keySet()){
			if(!allowedSync.contains(paramName)) continue;
			if(paramName == SYNC_DRAW_DATA)
				tool.getCurrentPad().draw((DrawData)syncData.get(paramName));
			else if(paramName == SYNC_LINE_WIDTH)
				tool.setLineWidth((Float)syncData.get(paramName));
			else if(paramName == SYNC_TEXT_COLOR)
				tool.setTextColor((Color)syncData.get(paramName));
			else if(paramName == SYNC_BACKGROUND_COLOR){
				for(MathPad pad: tool.getAllPads())
					pad.setBackgroundColour((Color)syncData.get(paramName));
			}else if(paramName == SYNC_WRITING_STATE)
				tool.setWritingState((WritingState)syncData.get(paramName));
			else if(paramName == SYNC_SEPARATOR_STATE)
				tool.setSeparatorState((SeparatorState)syncData.get(paramName));
			else if(paramName == SYNC_PAD_CLEARED){
				for(MathPad pad: tool.getAllPads())
					pad.clearAll();
			}else if(paramName == SYNC_PAD_REMOVED){
				tool.removePad((Integer)syncData.get(paramName));
			}else if(paramName == SYNC_NEW_PAD){
				syncManager.registerPad(userId, tool.addNewPad());
			}
			else if(paramName == SYNC_PAD_CHANGED)
				tool.showPad((Integer)syncData.get(paramName));
			else if(paramName == SYNC_ANSWER_PAD_DISPLAYED){}
		}
	}
}
