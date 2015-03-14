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

package synergynetframework.mtinput.dataplayback;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import synergynetframework.mtinput.IMultiTouchEventListener;
import synergynetframework.mtinput.IMultiTouchInputSource;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;
import synergynetframework.mtinput.filters.LoggingFilter;

public class DataPlaybackInput implements IMultiTouchInputSource {

	private BufferedReader br;
	private String line;
	private long startTime;
	private long logStartTime;

	private List<IMultiTouchEventListener> listeners = new ArrayList<IMultiTouchEventListener>();

	public DataPlaybackInput() {
	}
	
	public synchronized void setInputStream(InputStream is) {
		br = new BufferedReader(new InputStreamReader(is));
		init();
	}

	public void registerMultiTouchEventListener(IMultiTouchEventListener listener) {
		if(!listeners.contains(listener)) listeners.add(listener);
	}
	
	public void registerMultiTouchEventListener(IMultiTouchEventListener listener, int index) {
		if(!listeners.contains(listener)) listeners.add(index, listener);
	}

	public void unregisterMultiTouchEventListener(IMultiTouchEventListener listener) {
		listeners.remove(listener);
	}

	public void setClickSensitivity(long time, float distance) {
	}

	public void start() {
	}

	private void init() {
		nextLine();
		consumeComments();			
		startTime = System.nanoTime();
		String[] parts = line.split(",");
		logStartTime = Long.parseLong(parts[0]);	
	}

	private void close() {
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stop() {
	}

	public void update(float tpf) {
		if(line != null) processLine();
	}

	private void processLine() {
		String[] parts = line.split(",");
		long t = Long.parseLong(parts[0]);
		String evt = parts[1];
		long cursor = Long.parseLong(parts[2]);
		float x = Float.parseFloat(parts[3]);
		float y = Float.parseFloat(parts[4]);
		float vx = Float.parseFloat(parts[5]);
		float vy = Float.parseFloat(parts[6]);
		float p = Float.parseFloat(parts[7]);
		long currentTime = System.nanoTime();

		if((currentTime - startTime) >= t - logStartTime) {
			MultiTouchCursorEvent e = new MultiTouchCursorEvent(cursor, new Point2D.Float(x, y), new Point2D.Float(vx, vy), p, 0);
			
			if(evt.equals(LoggingFilter.CURSOR_CHANGED)) {
				for(IMultiTouchEventListener l : listeners) {
					l.cursorChanged(e);
				}
			}else if(evt.equals(LoggingFilter.CURSOR_PRESSED)) {
				for(IMultiTouchEventListener l : listeners) {
					l.cursorPressed(e);
				}				
			}else if(evt.equals(LoggingFilter.CURSOR_CLICKED)) {
				for(IMultiTouchEventListener l : listeners) {
					l.cursorClicked(e);
				}								
			}else if(evt.equals(LoggingFilter.CURSOR_RELEASED)) {
				for(IMultiTouchEventListener l : listeners) {
					l.cursorReleased(e);
				}				
			}

			nextLine();
			if(line == null) {
				close();
			}
		}		
	}

	private void consumeComments() {		
		while(line.startsWith("#")) { 
			nextLine();
		}
	}

	private void nextLine() {
		try {
			line = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}
