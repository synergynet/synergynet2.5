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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jme.renderer.ColorRGBA;
import com.jme.scene.Geometry;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.RenderState.StateType;

import synergynetframework.appsystem.table.animationsystem.AnimationElement;

public class Fader extends AnimationElement {

	public static final int MODE_FADE_IN = 0;
	public static final int MODE_FADE_OUT = 1;

	protected float fadeDurationSeconds = 1f;
	protected List<Geometry> spatials;	
	protected Map<Integer,BlendState> alphas;
	protected float alpha;
	private int mode;
	protected float waitTime;
	protected boolean willWait = true;
	protected boolean waiting = false;
	protected float waitTimeElapsed = 0f;
	
	public Fader(Geometry g, int modeFadeIn, float durationSeconds, float thenWaitSeconds) {
		this(createListFromGeometry(g), modeFadeIn, durationSeconds, thenWaitSeconds);
	}

	public Fader(Geometry g, int modeFadeIn, float durationSeconds) {
		this(createListFromGeometry(g), modeFadeIn, durationSeconds, 0f);
	}
	
	public Fader(List<Geometry> spatials, int fadeMode, float durationSeconds) {
		this(spatials, fadeMode, durationSeconds, 0f);
	}
	
	public Fader(List<Geometry> spatials, int fadeMode, float durationSeconds, float thenWaitSeconds) {
		this.spatials = spatials;
		if(thenWaitSeconds > 0f) {
			waitTime = thenWaitSeconds;
			willWait = true;
		}
		setFadeDuration(durationSeconds);
		getAlphaStates();
		mode = fadeMode;
		if(mode == MODE_FADE_IN) {
			alpha = 0f;
		}else if(mode == MODE_FADE_OUT) {
			alpha = 1f;
		}
		setAlphas();
	}

	public static List<Geometry> createListFromGeometry(Geometry... g) {
		List<Geometry> list = new ArrayList<Geometry>();
		for(Geometry t : g) {
			list.add(t);
		}
		return list;
	}

	public void setFadeDuration(float seconds) {
		this.fadeDurationSeconds = seconds;
	}

	private void setAlphas() {
		ColorRGBA c = new ColorRGBA(alpha, alpha, alpha, alpha);
		for(int i = 0; i < spatials.size(); i++) {
			spatials.get(i).setDefaultColor(c);
		}		
	}

	private void getAlphaStates() {
		alphas = new HashMap<Integer,BlendState>();
		for(int i = 0; i < spatials.size(); i++) {
			BlendState as = (BlendState)spatials.get(i).getRenderState(StateType.Blend);
			alphas.put(i, as);
		}
	}

	@Override
	public void elementStart(float tpf) {
	}

	@Override
	public boolean isFinished() {
		if(mode == MODE_FADE_IN) {
			if(willWait) {
				if(alpha >=1f && waitTimeElapsed > waitTime) {
					return true;
				}else{
					return false;
				}
			}else{
				return alpha >= 1f;	
			}			
		}else{
			if(willWait) {
				if(alpha <= 0f && waitTimeElapsed > waitTime) {
					return true;
				}else{
					return false;
				}
			}
			return alpha <= 0f;
		}
	}

	@Override
	public void reset() {
		if(mode == MODE_FADE_IN) {
			alpha = 0f;
		}else{
			alpha = 1f;
		}
	}

	@Override
	public void updateAnimationState(float tpf) {
		if(mode == MODE_FADE_IN) {
			alpha += tpf * 1 / fadeDurationSeconds;
			if(!waiting && alpha >= 1f) waiting = true;
		}else{
			alpha -= tpf * 1 / fadeDurationSeconds;
			if(!waiting && alpha <= 0f) waiting = true;
		}
		if(waiting) waitTimeElapsed += tpf;
		setAlphas();
	}

}
