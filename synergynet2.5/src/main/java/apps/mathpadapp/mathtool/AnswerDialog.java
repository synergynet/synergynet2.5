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
import java.awt.Rectangle;
import java.util.List;

import apps.mathpadapp.mathtool.MathTool.MathToolListener;
import apps.mathpadapp.util.MTDialog;

import mit.ai.nl.core.Expression;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.MathPad;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.MathPad.MathHandwritingListener;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;


/**
 * The Class AnswerDialog.
 */
public class AnswerDialog extends MTDialog{
	
	/** The math pad. */
	protected MathPad mathPad;

	/**
	 * Instantiates a new answer dialog.
	 *
	 * @param contentSystem the content system
	 * @param parentTool the parent tool
	 */
	public AnswerDialog(final ContentSystem contentSystem, final MathTool parentTool){
		super(parentTool, contentSystem);
		this.getWindow().setBackgroundColour(Color.red);
		
		this.setWidth(270);
		this.setHeight(200);
		this.setModal(true);
		mathPad = (MathPad) contentSystem.createContentItem(MathPad.class);
		mathPad.setWidth(250);
		mathPad.setHeight(150);
		mathPad.setSketchArea(new Rectangle(0,0,mathPad.getWidth(),mathPad.getHeight()));
		mathPad.setLocalLocation(0, 17);
		mathPad.setMathEngineEnabled(true);
		mathPad.addMathHandwritingListener(new MathHandwritingListener(){

			@Override
			public void expressionsWritten(List<Expression> expressions) {
				for(Expression exp: expressions)
					if(exp != null) System.out.println("Expression: "+exp.getTeX());
				System.out.println("--------------------------");
			}
			
		});
		this.getWindow().addSubItem(mathPad);

		final SimpleButton eraseBtn = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		eraseBtn.setText(" Delete ");
		eraseBtn.setBackgroundColour(Color.DARK_GRAY);
		eraseBtn.setTextColour(Color.WHITE);
		eraseBtn.setLocalLocation(-30, -77);
		eraseBtn.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonReleased(SimpleButton b, long id, float x,	float y, float pressure) {
				eraseBtn.setBackgroundColour(Color.DARK_GRAY);
				eraseBtn.setTextColour(Color.WHITE);
				
				mathPad.clearAll();
			}
			
			@Override
			public void buttonPressed(SimpleButton b, long id, float x,	float y, float pressure) {
				eraseBtn.setBackgroundColour(Color.WHITE);
				eraseBtn.setTextColour(Color.DARK_GRAY);
			}
		});
		this.getWindow().addSubItem(eraseBtn);

		
		final SimpleButton okBtn = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		okBtn.setText(" OK ");
		okBtn.setBackgroundColour(Color.DARK_GRAY);
		okBtn.setTextColour(Color.WHITE);
		okBtn.setLocalLocation(30, -77);
		okBtn.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonReleased(SimpleButton b, long id, float x,	float y, float pressure) {
				okBtn.setBackgroundColour(Color.DARK_GRAY);
				okBtn.setTextColour(Color.WHITE);
				
				if(parentTool != null){	
					for(MathToolListener listener :parentTool.mathToolListeners){
						if(parentTool.getCurrentAssignmentInfo() != null)
							listener.assignmentAnswerReady(parentTool.getCurrentAssignmentInfo());
					}
				}
				AnswerDialog.this.setVisible(false);
			}
			
			@Override
			public void buttonPressed(SimpleButton b, long id, float x,	float y, float pressure) {
				okBtn.setBackgroundColour(Color.WHITE);
				okBtn.setTextColour(Color.DARK_GRAY);
			}
		});
		this.getWindow().addSubItem(okBtn);

		closeButton.removeButtonListeners();
		closeButton.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				AnswerDialog.this.setVisible(false);
			}
			
		});
		closeButton.setBorderSize(2);
	}

	
	/**
	 * Gets the answer pad.
	 *
	 * @return the answer pad
	 */
	public MathPad getAnswerPad(){
		return this.mathPad;
	}
}
