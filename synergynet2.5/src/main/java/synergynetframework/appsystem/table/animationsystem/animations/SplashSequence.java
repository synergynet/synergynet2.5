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

package synergynetframework.appsystem.table.animationsystem.animations;

import java.util.List;

import com.jme.scene.Geometry;

import synergynetframework.appsystem.table.animationsystem.AnimationElement;
import synergynetframework.appsystem.table.animationsystem.animelements.AnimationSequence;
import synergynetframework.appsystem.table.animationsystem.animelements.Fader;


/**
 * The Class SplashSequence.
 */
public class SplashSequence extends AnimationElement {

	/** The geoms. */
	protected List<Geometry> geoms;
	
	/** The seq. */
	protected AnimationSequence seq;

	/**
	 * Instantiates a new splash sequence.
	 *
	 * @param geoms the geoms
	 * @param durationPerImage the duration per image
	 * @param thenWaitSeconds the then wait seconds
	 */
	public SplashSequence(List<Geometry> geoms, float durationPerImage, float thenWaitSeconds) {
		this.geoms = geoms;
		seq = new AnimationSequence();

		for(Geometry g : geoms) {
			Fader fadeOut = new Fader(g, Fader.MODE_FADE_OUT, durationPerImage);
			Fader fadeIn = new Fader(g, Fader.MODE_FADE_IN, durationPerImage, thenWaitSeconds);
			seq.addAnimationElement(fadeIn);
			seq.addAnimationElement(fadeOut);
		}
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.animationsystem.AnimationElement#elementStart(float)
	 */
	@Override
	public void elementStart(float tpf) {
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.animationsystem.AnimationElement#isFinished()
	 */
	@Override
	public boolean isFinished() {
		return seq.isFinished();
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.animationsystem.AnimationElement#reset()
	 */
	@Override
	public void reset() {
		seq.reset();		
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.animationsystem.AnimationElement#updateAnimationState(float)
	 */
	@Override
	public void updateAnimationState(float tpf) {
		seq.updateAnimationState(tpf);
	}

}
