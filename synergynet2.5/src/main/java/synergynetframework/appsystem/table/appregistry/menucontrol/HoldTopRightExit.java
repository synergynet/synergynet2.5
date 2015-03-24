package synergynetframework.appsystem.table.appregistry.menucontrol;

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


import java.util.HashMap;
import java.util.Map;

import core.SynergyNetDesktop;



import synergynetframework.appsystem.table.appdefinitions.SynergyNetApp;
import synergynetframework.mtinput.IMultiTouchEventListener;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;
import synergynetframework.mtinput.events.MultiTouchObjectEvent;


/**
 * The Class HoldTopRightExit.
 */
public class HoldTopRightExit extends MenuController implements IMultiTouchEventListener{
	
	/** The corner distance. */
	protected float cornerDistance = 40f;
	
	/** The cursor timing. */
	protected Map<Long,Long> cursorTiming = new HashMap<Long,Long>();
	
	/** The interval. */
	protected long interval = 1000;
	
	/** The enabled. */
	private boolean enabled;
	
	/**
	 * Instantiates a new hold top right exit.
	 */
	public HoldTopRightExit() {
		
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appregistry.menucontrol.MenuController#enableForApplication(synergynetframework.appsystem.table.appdefinitions.SynergyNetApp)
	 */
	@Override
	public void enableForApplication(SynergyNetApp app) {
		SynergyNetDesktop.getInstance().getMultiTouchInputComponent().registerMultiTouchEventListener(this);
		setEnabled(true);	
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appregistry.menucontrol.MenuController#applicationFinishing()
	 */
	@Override
	public void applicationFinishing() {
		SynergyNetDesktop.getInstance().getMultiTouchInputComponent().unregisterMultiTouchEventListener(this);
		setEnabled(false);
	}
	
	
	
	/**
	 * Checks if is enabled.
	 *
	 * @return true, if is enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Sets the enabled.
	 *
	 * @param enabled the new enabled
	 */
	private void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#cursorChanged(synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	public void cursorChanged(MultiTouchCursorEvent event) {
	}

	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#cursorClicked(synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	public void cursorClicked(MultiTouchCursorEvent event) {
	}

	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#cursorPressed(synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	public void cursorPressed(MultiTouchCursorEvent event) {
		if(isInCorner(event)) {
			synchronized(cursorTiming) {
				cursorTiming.put(event.getCursorID(), System.currentTimeMillis());
			}
		}
	}

	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#cursorReleased(synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	public void cursorReleased(MultiTouchCursorEvent event) {
		synchronized(cursorTiming) {
			cursorTiming.remove(event.getCursorID());
		}
	}

	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#objectAdded(synergynetframework.mtinput.events.MultiTouchObjectEvent)
	 */
	public void objectAdded(MultiTouchObjectEvent event) {
	}

	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#objectChanged(synergynetframework.mtinput.events.MultiTouchObjectEvent)
	 */
	public void objectChanged(MultiTouchObjectEvent event) {
	}

	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#objectRemoved(synergynetframework.mtinput.events.MultiTouchObjectEvent)
	 */
	public void objectRemoved(MultiTouchObjectEvent event) {
	}

	/**
	 * Checks if is in corner.
	 *
	 * @param event the event
	 * @return true, if is in corner
	 */
	protected boolean isInCorner(MultiTouchCursorEvent event) {
		int x = SynergyNetDesktop.getInstance().tableToScreenX(event.getPosition().x);
		int y = SynergyNetDesktop.getInstance().tableToScreenY(event.getPosition().y);
		
		return
			x > SynergyNetDesktop.getInstance().getDisplayWidth() - cornerDistance &&
			y > SynergyNetDesktop.getInstance().getDisplayHeight() - cornerDistance;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.Updateable#update(float)
	 */
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

