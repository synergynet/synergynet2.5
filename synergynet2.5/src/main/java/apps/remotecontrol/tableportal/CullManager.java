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
package apps.remotecontrol.tableportal;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.Frame;
import synergynetframework.appsystem.contentsystem.items.OrthoContentItem;
import synergynetframework.appsystem.contentsystem.items.listener.ItemListener;
import synergynetframework.jme.cursorsystem.elements.twod.ClipRegistry;

import com.jme.scene.Spatial;

public class CullManager{
	
	private float framerate;
	private float delay = 0.5f;
	
	private ClipRectangleHud crh;
	
	public CullManager(TablePortal portal){
		Frame frame = (Frame) portal.displayPanel.contentSystem.createContentItem(Frame.class);
		frame.setWidth(GraphConfig.MAIN_WINDOW_WIDTH - portal.controlPanel.getWidth() - 30);
		frame.setHeight(portal.controlPanel.getHeight() - 10);
		frame.setLocalLocation(portal.displayPanel.getWindow().getLocalLocation());
		frame.setVisible(false);
		portal.getWindow().addSubItem(frame);
		
		Spatial spat = ((Spatial)(frame.getImplementationObject()));
		crh = new ClipRectangleHud(spat,frame.getWidth(),frame.getHeight());
	}

	public void registerItemForClipping(ContentItem item) {
			crh.setSpatialClip((Spatial)item.getImplementationObject(), true);
			ClipRegistry.getInstance().registerClipRegion((Spatial)item.getImplementationObject(), crh);
			((OrthoContentItem)item).addItemListener(new ItemListener(){

				@Override
				public void cursorChanged(ContentItem item, long id, float x,float y, float pressure) {
					crh.setSpatialClip((Spatial)item.getImplementationObject(), false);
					crh.updateEquations();
				}

				@Override
				public void cursorClicked(ContentItem item, long id, float x,float y, float pressure) {	}
				
				@Override
				public void cursorDoubleClicked(ContentItem item, long id,float x, float y, float pressure) {}
				
				@Override
				public void cursorLongHeld(ContentItem item, long id, float x,	float y, float pressure) {}
				
				@Override
				public void cursorPressed(ContentItem item, long id, float x,float y, float pressure) {
					crh.setSpatialClip((Spatial)item.getImplementationObject(), false);
					crh.updateEquations();
				}
				
				@Override
				public void cursorReleased(ContentItem item, long id, float x, float y, float pressure) {
					crh.setSpatialClip((Spatial)item.getImplementationObject(), true);
					crh.updateEquations();
				}
				@Override
				public void cursorRightClicked(ContentItem item, long id, float x, float y, float pressure) {}
			});
	}
	
	
	public void update(float interpolation){
		if((framerate - interpolation) > 0){
			framerate-= interpolation;
			return;
		}
		framerate = delay;
		crh.updateEquations();
	}
	
	
}