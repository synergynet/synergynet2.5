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

package synergynetframework.appsystem.table;

import java.util.HashMap;
import java.util.Map;

import core.SynergyNetDesktop;


import synergynetframework.mtinput.IMultiTouchEventListener;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;
import synergynetframework.mtinput.events.MultiTouchObjectEvent;

public class DesktopActionListener implements IMultiTouchEventListener {

	protected float cornerDistance = 0.06f;
	protected Map<Long,Long> cursorTiming = new HashMap<Long,Long>();
	protected long interval = 1000;
	private boolean enabled;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void cursorChanged(MultiTouchCursorEvent event) {
	}

	public void cursorClicked(MultiTouchCursorEvent event) {
	}

	public void cursorPressed(MultiTouchCursorEvent event) {
		if(isInCorner(event)) {
			synchronized(cursorTiming) {
				cursorTiming.put(event.getCursorID(), System.currentTimeMillis());
			}
		}
	}

	public void cursorReleased(MultiTouchCursorEvent event) {
		synchronized(cursorTiming) {
			cursorTiming.remove(event.getCursorID());
		}
	}

	public void objectAdded(MultiTouchObjectEvent event) {
	}

	public void objectChanged(MultiTouchObjectEvent event) {
	}

	public void objectRemoved(MultiTouchObjectEvent event) {
	}

	protected boolean isInCorner(MultiTouchCursorEvent event) {
		return
		event.getPosition().x < cornerDistance &&
		event.getPosition().y < cornerDistance;
	}

	public void update(float interpolation) {
		long endTime = System.currentTimeMillis();
		for(long id : cursorTiming.keySet()){
			long startTime = cursorTiming.get(id);
			if(endTime - startTime > interval ) {


				try {
					synchronized(cursorTiming) {
						cursorTiming.remove(id);
					}
					if(enabled) {
						SynergyNetDesktop.getInstance().showMainMenu();
					}
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

			}
		}
	}


}
