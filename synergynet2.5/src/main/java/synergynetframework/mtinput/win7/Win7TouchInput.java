/*
 * Copyright (c) 2012 University of Durham, England
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
package synergynetframework.mtinput.win7;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import synergynetframework.mtinput.ClickDetector;
import synergynetframework.mtinput.IMultiTouchEventListener;
import synergynetframework.mtinput.IMultiTouchInputSource;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;
import synergynetframework.mtinput.exceptions.MultiTouchInputException;

import org.mt4j.input.inputSources.Windows7TouchEvent;
import org.mt4j.input.inputSources.Win7NativeTouchSource;


/**
 * Input source for native Windows 7 WM_TOUCH messages for single/multi-touch.
 */
public class Win7TouchInput implements IMultiTouchInputSource {
	
	protected Map<Long,Win7Cursor> fingerCursors = new HashMap<Long,Win7Cursor>();

	protected List<IMultiTouchEventListener> listeners = new ArrayList<IMultiTouchEventListener>();
	protected ClickDetector clickDetector = new ClickDetector(500, 2f);
	
	protected List<Callable<Object>> callingList = new ArrayList<Callable<Object>>();
		
	private float width = 1024f;	
	private float height = 768f;
	private boolean sixtyFourBit = false;
	
	private Win7NativeTouchSource win7NativeTouchSource;
	
	/**
	 * Instantiates a new win7 native touch source.
	 */
	public Win7TouchInput(float width, float height, boolean sixtyFourBit) {
		this.width = width;
		this.height = height;
		this.sixtyFourBit = sixtyFourBit;
		start();
	}
		
	public void start() {
		synchronized(this) {			
			win7NativeTouchSource = new Win7NativeTouchSource(this);			
		}
	}
	

	public void addTouchCursor(final Windows7TouchEvent wmTouchEvent){

		final long touchID = wmTouchEvent.id;
		Win7Cursor fingerCursor = fingerCursors.get(touchID);
		if(fingerCursor == null) {
			fingerCursor = new Win7Cursor();			
			fingerCursors.put(touchID, fingerCursor);
						
			Callable<Object> c = new Callable<Object>() {

				float xpos = wmTouchEvent.x/width;
				float ypos = wmTouchEvent.y/height;							
				float xContactSize = wmTouchEvent.contactSizeX;
				float yContactSize = wmTouchEvent.contactSizeY;
				
				@Override
				public Object call() throws Exception {
					
					final Win7Cursor fingerCursor = fingerCursors.get(touchID);
					if(fingerCursor != null) {	
						fingerCursor.setPosition(new Point2D.Float(xpos, 1-ypos));
						fingerCursor.setContactSize(new Point2D.Float(xContactSize, yContactSize));
						for(IMultiTouchEventListener listener : listeners) {
							clickDetector.newCursorPressed(fingerCursor.getId(), fingerCursor.getPosition());
							MultiTouchCursorEvent evt = 
									new MultiTouchCursorEvent(fingerCursor.getId(), fingerCursor.getPosition());
							listener.cursorPressed(evt);
						}
					}
					return null;
				}
			};
			synchronized(callingList) {
				callingList.add(c);
			}
		}
	}
					
	public void updateTouchCursor(final Windows7TouchEvent wmTouchEvent){
			
		Callable<Object> c = new Callable<Object>() {
			
			float xpos = wmTouchEvent.x/width;
			float ypos = wmTouchEvent.y/height;							
			float xContactSize = wmTouchEvent.contactSizeX;
			float yContactSize = wmTouchEvent.contactSizeY;
			
			long touchID = wmTouchEvent.id;		
			
			@Override
			public Object call() throws Exception {
				final Win7Cursor fingerCursor = fingerCursors.get(touchID);
				if(fingerCursor != null) {
					fingerCursor.setPosition(new Point2D.Float(xpos, 1-ypos));
					fingerCursor.setContactSize(new Point2D.Float(xContactSize, yContactSize));
					for(IMultiTouchEventListener listener : listeners) {
						MultiTouchCursorEvent evt =
								new MultiTouchCursorEvent(fingerCursor.getId(), fingerCursor.getPosition());
						listener.cursorChanged(evt);
					}
				}
				return null;
			}
		};
		
		synchronized(callingList) {
			callingList.add(c);
		}
	}
	
	public void removeTouchCursor(final Windows7TouchEvent wmTouchEvent){
		Callable<Object> c = new Callable<Object>() {
			
			long touchID = wmTouchEvent.id;

			@Override
			public Object call() throws Exception {
				final Win7Cursor fingerCursor = fingerCursors.get(touchID);

				if(fingerCursor != null) {

					for(IMultiTouchEventListener l : listeners) {
						MultiTouchCursorEvent event = 
								new MultiTouchCursorEvent(fingerCursor.getId(), fingerCursor.getPosition());
						int clickCount = clickDetector.cursorReleasedGetClickCount(
								fingerCursor.getId(), fingerCursor.getPosition());
						if(clickCount > 0) {
							event.setClickCount(clickCount);
							l.cursorClicked(event);
						}
						l.cursorReleased(event);
						
					}
					fingerCursors.remove(touchID);

				}
				return null;
			}
		};
		synchronized(callingList) {
			callingList.add(c);
		}
	}
	
	@Override
	public void registerMultiTouchEventListener(IMultiTouchEventListener listener) {
		if(!listeners.contains(listener)) listeners.add(listener);			
	}

	@Override
	public void registerMultiTouchEventListener(IMultiTouchEventListener listener, int index) {
		if(!listeners.contains(listener)) listeners.add(index, listener);
	}

	@Override
	public void unregisterMultiTouchEventListener(IMultiTouchEventListener listener) {
		listeners.remove(listener);		
	}

	@Override
	public void setClickSensitivity(long time, float distance) {
		this.clickDetector = new ClickDetector(time, distance);
	}

	@Override
	public void update(float tpf) throws MultiTouchInputException {
		win7NativeTouchSource.pollEvents();
		synchronized(callingList) {
			for(Callable<Object> c : callingList) {
				try {
					c.call();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			callingList.clear();
		}
	}

	public boolean isSixtyFourBit() {
		return sixtyFourBit;
	}

	public void setSixtyFourBit(boolean sixtyFourBit) {
		this.sixtyFourBit = sixtyFourBit;
	}

}
