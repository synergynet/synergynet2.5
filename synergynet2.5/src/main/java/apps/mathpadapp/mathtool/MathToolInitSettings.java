/*
 * Copyright (c) 2009 University of Durham, England All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of 'SynergyNet' nor the names of
 * its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission. THIS SOFTWARE IS PROVIDED
 * BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package apps.mathpadapp.mathtool;

import java.awt.Color;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import synergynetframework.appsystem.contentsystem.jme.items.utils.DrawData;
import apps.mathpadapp.mathtool.MathTool.SeparatorState;
import apps.mathpadapp.mathtool.MathTool.WritingState;

/**
 * The Class MathToolInitSettings.
 */
public class MathToolInitSettings implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 531818103121346516L;
	
	/** The angle. */
	private float angle = Float.NaN;

	/** The background color. */
	private Color backgroundColor = null;

	/** The current pad index. */
	private int currentPadIndex;

	/** The draw data. */
	private HashMap<Integer, List<DrawData>> drawData = new HashMap<Integer, List<DrawData>>();

	/** The line width. */
	private float lineWidth = Float.NaN;

	/** The location x. */
	private float locationX = Float.NaN;

	/** The location y. */
	private float locationY = Float.NaN;
	
	/** The no of pads. */
	private int noOfPads = 1;

	/** The order. */
	private float order = Float.NaN;

	/** The scale. */
	private float scale = Float.NaN;

	/** The separator state. */
	private SeparatorState separatorState = null;

	/** The text color. */
	private Color textColor = null;

	/** The title. */
	private String title = null;

	/** The writing state. */
	private WritingState writingState = null;

	/**
	 * Instantiates a new math tool init settings.
	 */
	public MathToolInitSettings() {
	}
	
	/**
	 * Gets the angle.
	 *
	 * @return the angle
	 */
	public float getAngle() {
		return angle;
	}

	/**
	 * Gets the background color.
	 *
	 * @return the background color
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * Gets the current pad index.
	 *
	 * @return the current pad index
	 */
	public int getCurrentPadIndex() {
		return currentPadIndex;
	}

	/**
	 * Gets the current text color.
	 *
	 * @return the current text color
	 */
	public Color getCurrentTextColor() {
		return textColor;
	}

	/**
	 * Gets the draw data.
	 *
	 * @return the draw data
	 */
	public HashMap<Integer, List<DrawData>> getDrawData() {
		return drawData;
	}
	
	/**
	 * Gets the line width.
	 *
	 * @return the line width
	 */
	public float getLineWidth() {
		return lineWidth;
	}

	/**
	 * Gets the location x.
	 *
	 * @return the location x
	 */
	public float getLocationX() {
		return locationX;
	}
	
	/**
	 * Gets the location y.
	 *
	 * @return the location y
	 */
	public float getLocationY() {
		return locationY;
	}
	
	/**
	 * Gets the no of pads.
	 *
	 * @return the no of pads
	 */
	public int getNoOfPads() {
		return noOfPads;
	}
	
	/**
	 * Gets the order.
	 *
	 * @return the order
	 */
	public float getOrder() {
		return order;
	}
	
	/**
	 * Gets the scale.
	 *
	 * @return the scale
	 */
	public float getScale() {
		return scale;
	}
	
	/**
	 * Gets the separator state.
	 *
	 * @return the separator state
	 */
	public SeparatorState getSeparatorState() {
		return separatorState;
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Gets the writing state.
	 *
	 * @return the writing state
	 */
	public WritingState getWritingState() {
		return writingState;
	}
	
	/**
	 * Sets the angle.
	 *
	 * @param rotationAngle
	 *            the new angle
	 */
	public void setAngle(float rotationAngle) {
		this.angle = rotationAngle;
	}
	
	/**
	 * Sets the background color.
	 *
	 * @param backgroundColor
	 *            the new background color
	 */
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	
	/**
	 * Sets the current pad index.
	 *
	 * @param currentPadIndex
	 *            the new current pad index
	 */
	public void setCurrentPadIndex(int currentPadIndex) {
		this.currentPadIndex = currentPadIndex;
	}
	
	/**
	 * Sets the draw data.
	 *
	 * @param drawData
	 *            the draw data
	 */
	public void setDrawData(HashMap<Integer, List<DrawData>> drawData) {
		this.drawData = drawData;
	}

	/**
	 * Sets the line width.
	 *
	 * @param currentLineWidth
	 *            the new line width
	 */
	public void setLineWidth(float currentLineWidth) {
		this.lineWidth = currentLineWidth;
	}

	/**
	 * Sets the location x.
	 *
	 * @param locationX
	 *            the new location x
	 */
	public void setLocationX(float locationX) {
		this.locationX = locationX;
	}

	/**
	 * Sets the location y.
	 *
	 * @param locationY
	 *            the new location y
	 */
	public void setLocationY(float locationY) {
		this.locationY = locationY;
	}

	/**
	 * Sets the no of pads.
	 *
	 * @param noOfPads
	 *            the new no of pads
	 */
	public void setNoOfPads(int noOfPads) {
		this.noOfPads = noOfPads;
	}
	
	/**
	 * Sets the order.
	 *
	 * @param order
	 *            the new order
	 */
	public void setOrder(float order) {
		this.order = order;
	}
	
	/**
	 * Sets the scale.
	 *
	 * @param scale
	 *            the new scale
	 */
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	/**
	 * Sets the separator state.
	 *
	 * @param separatorState
	 *            the new separator state
	 */
	public void setSeparatorState(SeparatorState separatorState) {
		this.separatorState = separatorState;
	}
	
	/**
	 * Sets the text color.
	 *
	 * @param currentTextColor
	 *            the new text color
	 */
	public void setTextColor(Color currentTextColor) {
		this.textColor = currentTextColor;
	}

	/**
	 * Sets the title.
	 *
	 * @param title
	 *            the new title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Sets the writing state.
	 *
	 * @param initWritingState
	 *            the new writing state
	 */
	public void setWritingState(WritingState initWritingState) {
		this.writingState = initWritingState;
	}
}
