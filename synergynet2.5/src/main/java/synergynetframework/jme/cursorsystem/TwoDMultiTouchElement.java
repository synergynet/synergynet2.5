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

package synergynetframework.jme.cursorsystem;

import synergynetframework.jme.cursorsystem.cursordata.ScreenCursor;
import synergynetframework.jme.pickingsystem.data.OrthogonalPickResultData;

import com.jme.math.Vector2f;
import com.jme.scene.Spatial;

public abstract class TwoDMultiTouchElement extends MultiTouchElement {

	public TwoDMultiTouchElement(Spatial pickingSpatial, Spatial targetSpatial) {
		super(pickingSpatial, targetSpatial);
	}
	
	public TwoDMultiTouchElement(Spatial pickingAndTargetSpatial) {
		this(pickingAndTargetSpatial, pickingAndTargetSpatial);
	}
	
	// utility methods that simply do type casting on parent methods	
	public void registerScreenCursor(ScreenCursor c, OrthogonalPickResultData nodeloc) {
		super.registerScreenCursor(c, nodeloc);
	}
	
	public OrthogonalPickResultData getPickDataForCursorID(long id) {
		return (OrthogonalPickResultData)super.getPickDataForCursorID(id);
	}
	
	public OrthogonalPickResultData getPickResultFromCursorIndex(int index) {
		return (OrthogonalPickResultData) super.getPickResultFromCursorIndex(index);
	}
	
	public Vector2f getSpatial2DWorldPositionAtPick(int cursorIndex) {
		OrthogonalPickResultData prd = getPickResultFromCursorIndex(cursorIndex);
		return new Vector2f(prd.getSpatialWorldPositionAtPick().x, prd.getSpatialWorldPositionAtPick().y);		
	}
	
	public Vector2f getCursorScreenPositionAtPick(int cursorIndex) {
		OrthogonalPickResultData prd = getPickResultFromCursorIndex(cursorIndex);
		return prd.getCursorScreenPositionAtPick();
	}
}
