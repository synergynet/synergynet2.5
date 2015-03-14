/* Copyright (c) 2008 University of Durham, England
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

package synergynetframework.appsystem.contentsystem.jme.items;

import java.util.ArrayList;
import java.util.List;

import com.jme.scene.Geometry;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.RoundContentItem;
import synergynetframework.appsystem.contentsystem.items.RoundListContainer;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IRoundListContainerImplementation;
import synergynetframework.appsystem.table.animationsystem.AnimationSystem;
import synergynetframework.appsystem.table.animationsystem.animelements.Fader;
import synergynetframework.appsystem.table.animationsystem.animelements.MoveInCircleInContainer;

public class JMERoundListContainer extends JMERoundWindow implements IRoundListContainerImplementation{
	
	protected RoundListContainer list;
	protected List<RoundContentItem> listItems;	
	protected List<Geometry> listGeometries = new ArrayList<Geometry>();
	
	public JMERoundListContainer(ContentItem contentItem) {
		super(contentItem);
		list = (RoundListContainer)contentItem;
		listItems = list.getListItems();	
		refreshListGeometries();
	}
	
	@Override
	public void init(){
		super.init();
		render();
	}

	@Override
	public void addSubItem(int index, RoundContentItem item) {
		refreshListGeometries();
		render();
	}

	@Override
	public void addSubItem(RoundContentItem item) {
		refreshListGeometries();
		render();
	}

	@Override
	public void removeSubItem(RoundContentItem item) {
		refreshListGeometries();
		render();
	}
	
	public void run(){
		MoveInCircleInContainer moveCircle = new MoveInCircleInContainer(listGeometries, this.getMaxRadiusOfAllItems()*2 - 30, 0, 0 );
		moveCircle.setRPM(0.5f);
		moveCircle.reset();				
		Fader fadeIn = new Fader(listGeometries, Fader.MODE_FADE_IN, 2);
		
		AnimationSystem.getInstance().add(moveCircle);
		AnimationSystem.getInstance().add(fadeIn);
	}
	
	public void render(){
			
	}
	
	private void refreshListGeometries(){
		this.listGeometries.clear();
		for (RoundContentItem roundContentItem: listItems){
			this.listGeometries.add((Geometry)roundContentItem.getImplementationObject());
		}
	}
	
	public float getMaxRadiusOfAllItems(){
		float maxRadius =0f;
		for (RoundContentItem roundContentItem: listItems){
			if (roundContentItem.getRadius()>maxRadius)
				maxRadius = roundContentItem.getRadius();	
		}
		return maxRadius;
	}
	
}
