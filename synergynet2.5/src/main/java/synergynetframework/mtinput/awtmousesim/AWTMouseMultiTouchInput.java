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

package synergynetframework.mtinput.awtmousesim;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;

import synergynetframework.mtinput.IMultiTouchInputSource;
import synergynetframework.mtinput.simulator.AbstractDirectMultiTouchSimulator;
import synergynetframework.mtinput.simulator.AbstractSimCursor;


/**
 * The Class AWTMouseMultiTouchInput.
 *
 * @author dcs0ah1
 */
public class AWTMouseMultiTouchInput extends AbstractDirectMultiTouchSimulator implements IMultiTouchInputSource, MouseListener, MouseMotionListener, KeyListener {
	
	/** The frame. */
	protected JFrame frame;
	
	/** The width. */
	protected int width;
	
	/** The height. */
	protected int height;

	/**
	 * Instantiates a new AWT mouse multi touch input.
	 *
	 * @param frame the frame
	 */
	public AWTMouseMultiTouchInput(JFrame frame) {
		super(frame.getWidth(), frame.getHeight());
		this.frame = frame;
		this.width = frame.getWidth();
		this.height = frame.getHeight();
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.simulator.AbstractMultiTouchSimulator#start()
	 */
	public void start() {
		frame.addMouseListener(this);
		frame.addMouseMotionListener(this);
		frame.addKeyListener(this);
	}

	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.simulator.AbstractMultiTouchSimulator#stop()
	 */
	public void stop() {
		frame.removeMouseListener(this);
		frame.removeMouseListener(this);
		frame.removeKeyListener(this);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		super.mousePressed(e.getX(), e.getY(), getAbstractSimCursorMouseButton(e));
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
		super.mouseReleased(e.getX(), e.getY(), getAbstractSimCursorMouseButton(e));
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(MouseEvent e) {
		super.mouseDragged(e.getX(), e.getY(), getAbstractSimCursorMouseButton(e));	
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent e) {
		super.mouseMoved(e.getX(), e.getY());
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		// do something here?
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {}
	
	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SHIFT)
			super.keyPressed(AbstractSimCursor.KEY_SHIFT);
		if(e.getKeyCode() == KeyEvent.VK_CONTROL)
			super.keyPressed(AbstractSimCursor.KEY_CONTROL);
		if(e.getKeyCode() == KeyEvent.VK_SPACE)
			super.keyPressed(AbstractSimCursor.KEY_SPACE);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SHIFT)
			super.keyReleased(AbstractSimCursor.KEY_SHIFT);
		if(e.getKeyCode() == KeyEvent.VK_CONTROL)
			super.keyReleased(AbstractSimCursor.KEY_CONTROL);
		if(e.getKeyCode() == KeyEvent.VK_SPACE)
			super.keyReleased(AbstractSimCursor.KEY_SPACE);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	public void keyTyped(KeyEvent e) {}
	
	/**
	 * Gets the abstract sim cursor mouse button.
	 *
	 * @param evt the evt
	 * @return the abstract sim cursor mouse button
	 */
	public int getAbstractSimCursorMouseButton(MouseEvent evt) {
		int button = AbstractSimCursor.MOUSE_BUTTON_LEFT;
		if(evt.getButton() == MouseEvent.BUTTON1) {
			button = AbstractSimCursor.MOUSE_BUTTON_LEFT;
		}
		if(evt.getButton() == MouseEvent.BUTTON2) {
			button = AbstractSimCursor.MOUSE_BUTTON_MIDDLE;
		}
		if(evt.getButton() == MouseEvent.BUTTON3) {
			button = AbstractSimCursor.MOUSE_BUTTON_RIGHT;
		}
		return button;
	}


	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchInputSource#update(float)
	 */
	public void update(float tpf) {}




}
