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

package synergynetframework.mtinput.simulator;

import java.awt.Point;
import java.awt.geom.Point2D;



/**
 * Behaviour: right-click + hold with a drag then release to define
 * the first finger and second finger.  Hold shift to rotate. Hold
 * CTRL to scale.  Left-click to finish.
 * 
 * @author dcs3ash
 *
 */

public class DoubleFingerSimCursor extends AbstractSimCursor {
	
	/** The Constant MODE_MOVE. */
	public static final int MODE_MOVE = 0;
	
	/** The Constant MODE_ROT. */
	public static final int MODE_ROT = 1;
	
	/** The Constant MODE_SCALE. */
	public static final int MODE_SCALE = 2;
	
	/** The Constant MODE_INITIAL_DISTANCE. */
	public static final int MODE_INITIAL_DISTANCE = 3;

	/** The mouse screen x. */
	protected int mouseScreenX;
	
	/** The mouse screen y. */
	protected int mouseScreenY;
	
	/** The radius. */
	protected int radius;
	
	/** The angle. */
	protected double angle = Math.toRadians(20.0);

	/** The scaled finger1. */
	Point2D.Float scaledFinger1 = new Point2D.Float();
	
	/** The scaled finger2. */
	Point2D.Float scaledFinger2 = new Point2D.Float();

	/** The mode. */
	protected int mode = MODE_MOVE;
	
	/** The id1. */
	private int id1;
	
	/** The id2. */
	private int id2;

	/** The cursor info. */
	IndividualCursor[] cursorInfo = new IndividualCursor[2];
	
	/** The dy. */
	private float dy = 1;
	
	/** The dx. */
	private float dx = 1;
	
	/** The first cursor position. */
	private Point firstCursorPosition;
	
	/** The second cursor position. */
	private Point secondCursorPosition;
	
	/** The central point. */
	private Point centralPoint = new Point();
	
	/** The screen width. */
	private int screenWidth;
	
	/** The screen height. */
	private int screenHeight;
	
	/** The simulator. */
	private IMultiTouchSimulator simulator;

	/**
	 * Instantiates a new double finger sim cursor.
	 *
	 * @param simulator the simulator
	 * @param id1 the id1
	 * @param id2 the id2
	 * @param screenWidth the screen width
	 * @param screenHeight the screen height
	 */
	public DoubleFingerSimCursor(IMultiTouchSimulator simulator, int id1, int id2, int screenWidth, int screenHeight) {
		this.simulator = simulator;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.id1 = id1;
		this.id2 = id2;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.simulator.AbstractSimCursor#mouseDragged(int, int, int)
	 */
	@Override
	public void mouseDragged(int x, int y, int button) {
		mouseScreenX = x;
		mouseScreenY = y;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.simulator.AbstractSimCursor#mouseMoved(int, int)
	 */
	@Override
	public void mouseMoved(int x, int y) {
		mouseScreenX = x;
		mouseScreenY = y;
		if(mode == MODE_MOVE) {
			updatePositionInfo();
		}else if(mode == MODE_ROT) {
			updateRotation(true);
		}else if(mode == MODE_SCALE) {
			updateScaling();
		}
		simulator.updateTwoCursors(id1, scaledFinger1.x, scaledFinger1.y, id2, scaledFinger2.x, scaledFinger2.y);
	}

	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.simulator.AbstractSimCursor#mousePressed(int, int, int)
	 */
	@Override
	public void mousePressed(int x, int y, int button) {
		mouseScreenX = x;
		mouseScreenY = y;

		switch(button) {
		case AbstractSimCursor.MOUSE_BUTTON_LEFT: {
			break;
		}
		case AbstractSimCursor.MOUSE_BUTTON_RIGHT: {
			if(mode != MODE_INITIAL_DISTANCE) {
				this.firstCursorPosition = new Point(x, y);
				switchMode(MODE_INITIAL_DISTANCE);
			}
			break;
		}
		}
	}

	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.simulator.AbstractSimCursor#mouseReleased(int, int, int)
	 */
	@Override
	public void mouseReleased(int x, int y, int buttonNumber) {
		mouseScreenX = x;
		mouseScreenY = y;
		if(buttonNumber == AbstractSimCursor.MOUSE_BUTTON_RIGHT && mode == MODE_INITIAL_DISTANCE) {
			this.secondCursorPosition = new Point(x, y);
			radius = (int) (firstCursorPosition.distance(secondCursorPosition)/2f);
			if(radius == 0) return;
			updatePositionInfo();
			angle = -Math.atan(dy/dx) + Math.PI/2;
			switchMode(MODE_MOVE);
			simulator.newCursor(id1, scaledFinger1.x, scaledFinger1.y);
			simulator.newCursor(id2, scaledFinger2.x, scaledFinger2.y);
			simulator.updateTwoCursors(id1, scaledFinger1.x, scaledFinger1.y, id2, scaledFinger2.x, scaledFinger2.y);
		}else if(buttonNumber == AbstractSimCursor.MOUSE_BUTTON_LEFT && mode == MODE_MOVE) {
			simulator.deleteTwoCursors(id1, scaledFinger1.x, scaledFinger1.y, id2, scaledFinger2.x, scaledFinger2.y);
			simulator.clearCursor();
		}
	}



	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.simulator.AbstractSimCursor#keyPressed(java.lang.String)
	 */
	@Override
	public void keyPressed(String key) {
		if(key.equals(AbstractSimCursor.KEY_SHIFT)) {
			switchMode(MODE_ROT);
		}else if(key.equals(AbstractSimCursor.KEY_CONTROL)) {
			switchMode(MODE_SCALE);
		}
	}

	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.simulator.AbstractSimCursor#keyReleased(java.lang.String)
	 */
	@Override
	public void keyReleased(String key) {
		if(key.equals(AbstractSimCursor.KEY_SHIFT)) {
			switchMode(MODE_MOVE);
		}else if(key.equals(AbstractSimCursor.KEY_CONTROL)) {
			switchMode(MODE_MOVE);
		}
	}

	/**
	 * Update rotation.
	 *
	 * @param updateAngle the update angle
	 */
	private void updateRotation(boolean updateAngle) {
		float deltaX = centralPoint.x - mouseScreenX;
		float deltaY = centralPoint.y - mouseScreenY;
		if(updateAngle) 
			angle = -Math.atan(deltaY/deltaX) + Math.PI/2;
		if(deltaX >= 0) {

			firstCursorPosition.y = (int)(centralPoint.y + radius * Math.cos(angle));
			firstCursorPosition.x = (int)(centralPoint.x + radius * Math.sin(angle));

			secondCursorPosition.y = (int)(centralPoint.y - radius * Math.cos(angle));
			secondCursorPosition.x = (int)(centralPoint.x - radius * Math.sin(angle));
		}else{
			secondCursorPosition.y = (int)(centralPoint.y + radius * Math.cos(angle));
			secondCursorPosition.x = (int)(centralPoint.x + radius * Math.sin(angle));

			firstCursorPosition.y = (int)(centralPoint.y - radius * Math.cos(angle));
			firstCursorPosition.x = (int)(centralPoint.x - radius * Math.sin(angle));			
		}
		scaledFinger1.x = getScaledX(firstCursorPosition.x, screenWidth);
		scaledFinger1.y = getScaledY(firstCursorPosition.y, screenHeight);

		scaledFinger2.x = getScaledX(secondCursorPosition.x, screenWidth);
		scaledFinger2.y = getScaledY(secondCursorPosition.y, screenHeight);
	}
	
	/**
	 * Update position info.
	 */
	private void updatePositionInfo() {
		int deltaX = mouseScreenX - secondCursorPosition.x;
		int deltaY = mouseScreenY - secondCursorPosition.y;
		firstCursorPosition.x += deltaX;
		firstCursorPosition.y += deltaY;
		secondCursorPosition.x += deltaX;
		secondCursorPosition.y += deltaY;
		
		dx = secondCursorPosition.x - firstCursorPosition.x;
		dy = secondCursorPosition.y - firstCursorPosition.y;
		centralPoint = new Point(
				(int)(firstCursorPosition.x + dx/2f),
				(int)(firstCursorPosition.y + dy/2f)
				);
		
		scaledFinger1.x = getScaledX(firstCursorPosition.x, screenWidth);
		scaledFinger1.y = getScaledY(firstCursorPosition.y, screenHeight);

		scaledFinger2.x = getScaledX(secondCursorPosition.x, screenWidth);
		scaledFinger2.y = getScaledY(secondCursorPosition.y, screenHeight);
	}
	
	/**
	 * Update scaling.
	 */
	private void updateScaling() {
		radius = (int) new Point(mouseScreenX, mouseScreenY).distance(centralPoint);
		updateRotation(false);
	}

	

	/**
	 * Switch mode.
	 *
	 * @param newMode the new mode
	 */
	private void switchMode(int newMode) {
		this.mode = newMode;		
	}

	/**
	 * Gets the mode.
	 *
	 * @return the mode
	 */
	public int getMode() {
		return mode;
	}

	/**
	 * Gets the first cursor position.
	 *
	 * @return the first cursor position
	 */
	public Point getFirstCursorPosition() {
		return firstCursorPosition;
	}

	/**
	 * Gets the mouse x.
	 *
	 * @return the mouse x
	 */
	public int getMouseX() {
		return mouseScreenX;
	}

	/**
	 * Gets the mouse y.
	 *
	 * @return the mouse y
	 */
	public int getMouseY() {
		return mouseScreenY;
	}

	/**
	 * Gets the central point.
	 *
	 * @return the central point
	 */
	public Point getCentralPoint() {
		return centralPoint;
	}

	/**
	 * Gets the second cursor position.
	 *
	 * @return the second cursor position
	 */
	public Point getSecondCursorPosition() {
		return secondCursorPosition;
	}

	/**
	 * Gets the ID for cursor1.
	 *
	 * @return the ID for cursor1
	 */
	public int getIDForCursor1() {
		return id1;
	}

	/**
	 * Gets the ID for cursor2.
	 *
	 * @return the ID for cursor2
	 */
	public int getIDForCursor2() {
		return id2;
	}
	
}
