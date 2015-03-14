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

import synergynetframework.appsystem.contentsystem.items.MathPad;
import synergynetframework.jme.cursorsystem.elements.twod.OrthoBringToTop;

public class MathToolInitialiser {
	
	private MathTool mathTool;
	
	public MathToolInitialiser(MathTool mathTool){
		this.mathTool = mathTool;
	}
	
	protected void init(MathToolInitSettings settings){
		if(settings.getNoOfPads()>1){
			for(int i=1; i<settings.getNoOfPads(); i++)
				mathTool.addNewPad();
		}
		mathTool.showPad(settings.getCurrentPadIndex());
		if(settings.getCurrentTextColor() != null){
			for(MathPad pad: mathTool.mathPadList) pad.setTextColor(settings.getCurrentTextColor());
		}
		if(settings.getBackgroundColor() != null) {
			for(MathPad pad: mathTool.mathPadList) pad.setBackgroundColour(settings.getBackgroundColor());
		}
		if(settings.getWritingState() != null) mathTool.setWritingState(settings.getWritingState());
		if(settings.getTitle() != null) mathTool.setTitle(settings.getTitle());
		if(settings.getSeparatorState() != null) mathTool.setSeparatorState(settings.getSeparatorState());
		if(!(new Float(settings.getLocationX()).isNaN()) && !(new Float(settings.getLocationX()).isNaN())) mathTool.getWindow().setLocation(settings.getLocationX(), settings.getLocationY());
		if(settings.getDrawData() != null){
			for(int i: settings.getDrawData().keySet())
				mathTool.mathPadList.get(i).draw(settings.getDrawData().get(i));
		}
		if(!(new Float(settings.getLineWidth()).isNaN())) {
			for(MathPad pad: mathTool.mathPadList) pad.setLineWidth(settings.getLineWidth());
		}
		if(!(new Float(settings.getScale()).isNaN())) mathTool.getWindow().setScale(settings.getScale());
		if(!(new Float(settings.getAngle()).isNaN())) mathTool.getWindow().setAngle(settings.getAngle());
		if(!(new Float(settings.getOrder()).isNaN())) {
			int order = (int)settings.getOrder();
			if(order == OrthoBringToTop.topMost) mathTool.getWindow().setAsTopObject();
			else mathTool.getWindow().setOrder(order);
		}
		mathTool.controlPanel.setWritingState(settings.getWritingState());
		mathTool.controlPanel.setLineWidth(settings.getLineWidth());
		
		mathTool.controlPanel.getSolutionButton().setVisible(false);
	}
	
	public MathToolInitSettings getInitSettings(){
		MathToolInitSettings settings = new MathToolInitSettings();
		settings.setNoOfPads(mathTool.mathPadList.size());
		settings.setCurrentPadIndex(mathTool.getCurrentPadIndex());
		settings.setLineWidth(mathTool.currentMathPad.getLineWidth());
		settings.setTextColor(mathTool.currentMathPad.getTextColor());
		settings.setWritingState(mathTool.currentWritingState);
		settings.setTitle(mathTool.getTitle());
		settings.setSeparatorState(settings.getSeparatorState());
		settings.setBackgroundColor(mathTool.currentMathPad.getBackgroundColour());
		settings.setDrawData(mathTool.getDrawData());
		settings.setLocationX(mathTool.getWindow().getLocation().x);
		settings.setLocationY(mathTool.getWindow().getLocation().y);
		settings.setScale(mathTool.getWindow().getScale());
		settings.setAngle(mathTool.getWindow().getAngle());
		settings.setOrder(mathTool.getWindow().getOrder());
		return settings;
	}
}
