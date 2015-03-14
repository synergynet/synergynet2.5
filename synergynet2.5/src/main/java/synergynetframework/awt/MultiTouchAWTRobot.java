/*
 * Copyright (c) 2008 University of Durham, England
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

package synergynetframework.awt;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

import synergynetframework.mtinput.IMultiTouchEventListener;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;
import synergynetframework.mtinput.events.MultiTouchObjectEvent;

/**
 * Experimental class to let a multi-touch cursor control the mouse.
 * @author dcs0ah1
 *
 */
public class MultiTouchAWTRobot implements IMultiTouchEventListener {

	private JFrame frame;
	private Robot robot;

	public MultiTouchAWTRobot(JFrame frame) {
		this.frame = frame;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	public void cursorChanged(MultiTouchCursorEvent evt) {
		int x = (int)(evt.getPosition().x * frame.getSize().width);
		int y = (int)(evt.getPosition().y * frame.getSize().height);
		robot.mouseMove(x, y);
	}

	public void cursorClicked(MultiTouchCursorEvent evt) {
		int x = (int)(evt.getPosition().x * frame.getSize().width);
		int y = (int)(evt.getPosition().y * frame.getSize().height);				
		robot.mouseMove(x, y);
		robot.mousePress(MouseEvent.BUTTON1_MASK);
		robot.mouseRelease(MouseEvent.BUTTON1_MASK);
	}

	public void cursorPressed(MultiTouchCursorEvent evt) {
		int x = (int)(evt.getPosition().x * frame.getSize().width);
		int y = (int)(evt.getPosition().y * frame.getSize().height);				
		robot.mouseMove(x, y);
		robot.mousePress(MouseEvent.BUTTON1_MASK);
	}

	public void cursorReleased(MultiTouchCursorEvent evt) {
		int x = (int)(evt.getPosition().x * frame.getSize().width);
		int y = (int)(evt.getPosition().y * frame.getSize().height);
		robot.mouseMove(x, y);
		robot.mouseRelease(MouseEvent.BUTTON1_MASK);
	}

	public void objectAdded(MultiTouchObjectEvent arg0) {}
	public void objectChanged(MultiTouchObjectEvent arg0) {}
	public void objectRemoved(MultiTouchObjectEvent arg0) {}

}
