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

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import synergynetframework.mtinput.IMultiTouchInputSource;
import synergynetframework.mtinput.awtmousesim.AWTMouseMultiTouchInput;
import synergynetframework.mtinput.luminja.LuminMultiTouchInput;
import synergynetframework.mtinput.tuio.TUIOMultiTouchInput;


/**
 * Utility class that creates and launches an AWT/Swing based
 * multi-touch application.  Create a subclass of AWTAppRenderer
 * and pass this into the constructor.
 * @author dcs0ah1
 *
 */
public class AWTMultiTouch implements Runnable {

	/** The frame. */
	protected JFrame frame;
	
	/** The buffers. */
	protected BufferStrategy buffers;
	
	/** The mt input. */
	protected IMultiTouchInputSource mtInput;
	
	/** The renderer. */
	protected AWTAppRenderer renderer;
	
	/** The fps. */
	protected int fps = 25;
	
	/**
	 * Instantiates a new AWT multi touch.
	 *
	 * @param c the c
	 */
	public AWTMultiTouch(AWTAppRenderer c) {
		this(c, true);
	}

	/**
	 * Instantiates a new AWT multi touch.
	 *
	 * @param c the c
	 * @param fullscreen the fullscreen
	 */
	public AWTMultiTouch(AWTAppRenderer c, boolean fullscreen) {
		renderer = c;
		frame = new JFrame();
		frame.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					System.exit(0);
				}
			}
		});
		
		if(fullscreen) {
			Dimension size = Toolkit.getDefaultToolkit().getScreenSize(); 
			frame.setSize(size);	
			renderer.setSize(size);
			frame.setUndecorated(true);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice device = ge.getDefaultScreenDevice();
			setupMultiTouchTable();
			frame.setVisible(true);
			device.setFullScreenWindow(frame);
		}else{
			Dimension size = new Dimension(1024,768);
			frame.setSize(size);
			renderer.setSize(size);
			setupMultiTouchTable();
			frame.setVisible(true);
		}
		
		Thread t = new Thread(this);
		t.start();
	}



	/**
	 * Don't call this directly.
	 */
	public void run() {
		long startTime, endTime, frameTime;
		long fps = getFPS();
		long maxWait = 1000/fps;

		frame.createBufferStrategy(2);
		buffers = frame.getBufferStrategy();
		renderer.setSize(frame.getSize());

		while(true) {
			startTime = System.currentTimeMillis();

			render();

			endTime = System.currentTimeMillis();
			frameTime = endTime - startTime;

			try {
				Thread.sleep(Math.max(0, maxWait - frameTime));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
		}
	}


	/**
	 * Get the current calculated frames per second.
	 *
	 * @return the fps
	 */
	public long getFPS() {
		return fps;
	}

	/**
	 * Limit the number of frames per second.
	 *
	 * @param i the new fps
	 */
	public void setFPS(int i) {
		this.fps  = i;		
	}


	// ****** private methods ******

	/**
	 * Render.
	 */
	private void render() {
		Graphics2D g = (Graphics2D)buffers.getDrawGraphics();
		renderer.render(g);
		g.dispose();
		buffers.show();
	}

	/**
	 * Setup multi touch table.
	 */
	private void setupMultiTouchTable() {		

		Object[] possibilities = {"Mouse Simulation", "TUIO Table", "Evoluce Table"};
		String option = (String)JOptionPane.showInputDialog(
				new JFrame(),
				"Select table type:",
				"Table type",
				JOptionPane.PLAIN_MESSAGE,
				null, //icon
				possibilities,
		"?");

		if(option == null) System.exit(0);

		if(option.equals(possibilities[0])) { // mouse
			mtInput = new AWTMouseMultiTouchInput(frame);
		}else if(option.equals(possibilities[1])) { // tuio			
			mtInput = new TUIOMultiTouchInput();
		}else if(option.equals(possibilities[2])) { // lumin
			mtInput = new LuminMultiTouchInput();
		}

		mtInput.registerMultiTouchEventListener(renderer);
		mtInput.setClickSensitivity(500, 0.001f);
	}
}
