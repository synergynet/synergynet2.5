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

import com.jme.scene.Spatial;

public class RadarCullManager{
	
	private ClipRectangleHud crh;
	
	public RadarCullManager(TablePortal portal,Radar radar){
		Frame frame = (Frame) radar.getRadarWindow().getContentSystem().createContentItem(Frame.class);
		frame.setWidth((int)(radar.getRadarWindow().getWidth()* radar.getRadarWindow().getScale()));
		frame.setHeight((int)(radar.getRadarWindow().getHeight()* radar.getRadarWindow().getScale()));
		portal.getWindow().addSubItem(frame);
		frame.setLocation(radar.getRadarWindow().getLocation());
		frame.setVisible(false);
		Spatial spat = ((Spatial)(frame.getImplementationObject()));
		crh = new ClipRectangleHud(spat,frame.getWidth(),frame.getHeight());
	}

	public void registerItemForClipping(ContentItem item) {
			crh.setSpatialClip((Spatial)item.getImplementationObject(), true);
	}
	
	
	public void update(){
			crh.updateEquations();
	}
	
}