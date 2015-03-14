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

package apps.mathpadapp.mathtool;

import java.awt.Color;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import apps.mathpadapp.mathtool.MathTool.SeparatorState;
import apps.mathpadapp.mathtool.MathTool.WritingState;

import synergynetframework.appsystem.contentsystem.jme.items.utils.DrawData;

public class MathToolInitSettings implements Serializable{
	private static final long serialVersionUID = 531818103121346516L;

	private int noOfPads = 1;
	private int currentPadIndex;
	private WritingState writingState = null;
	private SeparatorState separatorState = null;
	private Color textColor = null;
	private Color backgroundColor = null;
	private String title = null;

	private float lineWidth = Float.NaN;
	
	private float locationX = Float.NaN;
	private float locationY = Float.NaN;
	private float scale = Float.NaN;
	private float angle = Float.NaN;
	private float order = Float.NaN;
	
	private HashMap<Integer,List<DrawData>> drawData = new HashMap<Integer,List<DrawData>>();
	
	public MathToolInitSettings(){}

	public void setNoOfPads(int noOfPads){
		this.noOfPads = noOfPads;
	}
	
	public int getNoOfPads(){
		return noOfPads;
	}
	
	public void setCurrentPadIndex(int currentPadIndex){
		this.currentPadIndex = currentPadIndex;
	}
	
	public int getCurrentPadIndex(){
		return currentPadIndex;
	}
	
	public WritingState getWritingState() {
		return writingState;
	}

	public void setWritingState(WritingState initWritingState) {
		this.writingState = initWritingState;
	}
	
	public SeparatorState getSeparatorState() {
		return separatorState;
	}

	public void setSeparatorState(SeparatorState separatorState) {
		this.separatorState = separatorState;
	}

	public Color getCurrentTextColor() {
		return textColor;
	}

	public void setTextColor(Color currentTextColor) {
		this.textColor = currentTextColor;
	}

	public float getLineWidth() {
		return lineWidth;
	}

	public void setLineWidth(float currentLineWidth) {
		this.lineWidth = currentLineWidth;
	}
	
	public void setBackgroundColor(Color backgroundColor){
		this.backgroundColor = backgroundColor;
	}
	
	public Color getBackgroundColor(){
		return backgroundColor;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float rotationAngle) {
		this.angle = rotationAngle;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public void setOrder(float order){
		this.order = order;
	}
	
	public float getOrder(){
		return order;
	}
	
	public void setDrawData(HashMap<Integer,List<DrawData>> drawData){
		this.drawData = drawData;
	}
	
	public HashMap<Integer,List<DrawData>> getDrawData(){
		return drawData;
	}

	public float getLocationX() {
		return locationX;
	}

	public void setLocationX(float locationX) {
		this.locationX = locationX;
	}

	public float getLocationY() {
		return locationY;
	}

	public void setLocationY(float locationY) {
		this.locationY = locationY;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public String getTitle(){
		return title;
	}
}
