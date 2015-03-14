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

package synergynetframework.appsystem.table.animationsystem.animelements;

import java.util.ArrayList;
import java.util.List;

import synergynetframework.appsystem.table.animationsystem.AnimationElement;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Geometry;
import com.jme.system.DisplaySystem;

public class MoveInConcentricCircles extends AnimationElement {

	protected List<Geometry> spatials;
	protected int n;
	protected float rpm = 1f;
	protected float angleOffset = 0f;
	protected float negAngleOffset = 0f;
	protected float itemWidth;
	private int mode = 0;
	private float dragAngle = 0f;
	private float lastDragAngle = 0f;
	
	protected int[] countPerLevel = {1, 5, 9, 15};
	
	protected List<List<Geometry>> spatialsLevels = new ArrayList<List<Geometry>>();

	public MoveInConcentricCircles(List<Geometry> spatials, float itemWidth) {
		this.spatials = spatials;
		n = spatials.size();
		this.itemWidth = itemWidth;
		populateSpatialLevels();
	}
	
	protected void populateSpatialLevels() {
		int level = 0;
		int count = 0;
		int max = countPerLevel[level];
		List<Geometry> currentLevel = new ArrayList<Geometry>();
		spatialsLevels.add(currentLevel);
		for(Geometry s : spatials) {
			currentLevel.add(s);
			count++;
			if(count >= max) {
				currentLevel = new ArrayList<Geometry>();
				spatialsLevels.add(currentLevel);		
				level++;
				count = 0;
				max = countPerLevel[level];				
			}
		}
	}
	
	public void setRPM(float rpm) {
		this.rpm = rpm;
	}
	
	public void setGeometries(int level, List<Geometry> cs) {
		float r = level * itemWidth * 1.5f;
		float currentAngle = angleOffset;	
		float negAngle = negAngleOffset;
		for(Geometry s : cs) {
			if(level %2 ==0) {
				setGeometry(s, currentAngle, r);
			}else{
				setGeometry(s, negAngle, r);	
			}
			currentAngle += FastMath.TWO_PI/cs.size();
			negAngle -= FastMath.TWO_PI / cs.size();
		}
	}

	protected void setGeometry(Geometry q, float currentAngle, float r) {
		Vector3f location = new Vector3f(r * FastMath.sin(currentAngle), r * FastMath.cos(currentAngle), 0f);
		location.x += DisplaySystem.getDisplaySystem().getRenderer().getWidth() / 2;
		location.y += DisplaySystem.getDisplaySystem().getRenderer().getHeight() / 2;
		q.setLocalTranslation(location);
		Quaternion a = new Quaternion();
		float[] angles = { 0f, 0f, -currentAngle + FastMath.PI};
		a.fromAngles(angles);
		q.setLocalRotation(a);	
	}
	
	public void setMode(int mode){
		this.mode = mode;
	}
	
	public void setDragAngle(float dragAngle){
		if (mode == 2){
			this.dragAngle = dragAngle - lastDragAngle;
			lastDragAngle = dragAngle;			
		}else{
			this.dragAngle = dragAngle;		
		}
	}
	
	@Override
	public void updateAnimationState(float tpf) {
				
		switch(mode){
		case 0:
			angleOffset += (rpm / 60 * FastMath.TWO_PI * tpf) % FastMath.TWO_PI;
			negAngleOffset -= (rpm / 60 * FastMath.TWO_PI * tpf) % FastMath.TWO_PI;
			break;
		case 1:
			angleOffset = dragAngle;
			negAngleOffset = dragAngle;
			break;
		case 2:
			angleOffset += (rpm / 60 * FastMath.TWO_PI * tpf) % FastMath.TWO_PI + dragAngle;
			negAngleOffset -= (rpm / 60 * FastMath.TWO_PI * tpf) % FastMath.TWO_PI - dragAngle;
			break;
		}
		
		int level = 0;
		for(List<Geometry> l : spatialsLevels) {
			setGeometries(level, l);
			level++;
		}
	}

	@Override
	public boolean isFinished() {
		return false;
	}

	@Override
	public void elementStart(float tpf) {}

	@Override
	public void reset() {
		int level = 0;
		for(List<Geometry> l : spatialsLevels) {
			setGeometries(level, l);
			level++;
		}
	}

}
