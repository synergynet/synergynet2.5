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

package apps.dissection;

import java.awt.Color;
import java.io.IOException;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ImageTextLabel;
import synergynetframework.appsystem.contentsystem.items.TwoDShape;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.shapes.TwoDShapeGeometry;
import synergynetframework.appsystem.contentsystem.items.utils.Background;
import synergynetframework.appsystem.contentsystem.items.utils.Border;
import synergynetframework.appsystem.table.SynergyNetAppUtils;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.appsystem.table.appregistry.menucontrol.HoldTopRightConfirmVisualExit;
import synergynetframework.mtinput.filters.SingleInputFilter;

import com.jme.scene.TriMesh;

import core.SynergyNetDesktop;


/**
 * The Class DissectionApp.
 */
public class DissectionApp extends DefaultSynergyNetApp {

	/**
	 * Instantiates a new dissection app.
	 *
	 * @param info the info
	 */
	public DissectionApp(ApplicationInfo info) {
		super(info);
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#addContent()
	 */
	@Override
	public void addContent() {
		ContentSystem csys = ContentSystem.getContentSystemForSynergyNetApp(this);
		SynergyNetAppUtils.addTableOverlay(this);
		setMenuController(new HoldTopRightConfirmVisualExit(this));

		SolutionFinder sf = new SolutionFinder("SF");
		DoubleClickFlip doubleClickFlip = new DoubleClickFlip();

		DissectionSolution[] sols = new DissectionSolution[sf.getShapeNames().length];
		
		for(int i = 0; i < sf.getShapeNames().length; i++) {
			String shapeName = sf.getShapeNames()[i];
			ImageTextLabel graphic = (ImageTextLabel) csys.createContentItem(ImageTextLabel.class);
			graphic.setImageInfo(DissectionApp.class.getResource(shapeName + ".png"));
			graphic.setBorder(new Border(null,0));
			graphic.setBackGround(new Background(Color.black));
			graphic.setRotateTranslateScalable(false);
			graphic.setBringToTopable(false);
			graphic.setLocalLocation(-100, 70, 0);
			
			DissectionSolution ds = new DissectionSolution(shapeName, graphic);

			sols[i] = ds;
		}
		
		sf.setSolutions(sols);
		
		try {
			TwoDShape tri = (TwoDShape) csys.createContentItem(TwoDShape.class);
			TwoDShapeGeometry trig = TwoDShapeGeometry.read(DissectionApp.class.getResourceAsStream("tri.shape"));
			tri.setShapeGeometry(trig);
			tri.placeRandom();			
			sf.setTt((TriMesh)tri.getImplementationObject());
			tri.addItemListener(sf);
			tri.addItemListener(doubleClickFlip);
			
			TwoDShape quad = (TwoDShape) csys.createContentItem(TwoDShape.class);
			TwoDShapeGeometry quadg = TwoDShapeGeometry.read(DissectionApp.class.getResourceAsStream("quad.shape"));
			quad.setShapeGeometry(quadg);
			quad.placeRandom();
			sf.setTq((TriMesh)quad.getImplementationObject());
			quad.addItemListener(sf);
			quad.addItemListener(doubleClickFlip);
			
			if(SynergyNetDesktop.getInstance().getMultiTouchInputComponent().isFilterActive(SingleInputFilter.class)) {			
				// single input mode
				try {
					tri.setRotateTranslateScalable(false);
					tri.setSingleTouchRotateTranslate(true);					
					quad.setRotateTranslateScalable(false);
					quad.setSingleTouchRotateTranslate(true);					
				} catch (Exception e) {
					e.printStackTrace();
				}				
			}else{
				// multi touch mode
				tri.setScaleLimit(1f, 1f);
				quad.setScaleLimit(1f, 1f);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp#stateUpdate(float)
	 */
	public void stateUpdate(float tpf) {
		super.stateUpdate(tpf);
	}


}

