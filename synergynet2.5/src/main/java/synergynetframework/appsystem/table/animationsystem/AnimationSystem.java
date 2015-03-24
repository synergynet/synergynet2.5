/*
 * Copyright (c) 2009 University of Durham, England All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of 'SynergyNet' nor the names of
 * its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission. THIS SOFTWARE IS PROVIDED
 * BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package synergynetframework.appsystem.table.animationsystem;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class AnimationSystem.
 */
public class AnimationSystem {
	
	/** The instance. */
	private static AnimationSystem instance;
	
	/**
	 * Gets the single instance of AnimationSystem.
	 *
	 * @return single instance of AnimationSystem
	 */
	public static AnimationSystem getInstance() {
		synchronized (AnimationSystem.class) {
			if (instance == null) {
				instance = new AnimationSystem();
			}
			return instance;
		}
	}
	
	/** The elements. */
	private List<AnimationElement> elements = new ArrayList<AnimationElement>();
	
	/**
	 * Instantiates a new animation system.
	 */
	private AnimationSystem() {
	}
	
	/**
	 * Adds the.
	 *
	 * @param element
	 *            the element
	 */
	public void add(AnimationElement element) {
		elements.add(element);
	}
	
	/**
	 * Update.
	 *
	 * @param tpf
	 *            the tpf
	 */
	public void update(float tpf) {
		List<AnimationElement> scheduledForRemoval = new ArrayList<AnimationElement>();

		// not sure why iterator can't be used here
		AnimationElement t;
		for (int i = 0; i < elements.size(); i++) {
			t = elements.get(i);
			if (t.isFinished()) {
				scheduledForRemoval.add(t);
			} else {
				t.updateState(tpf);
			}
		}
		
		if (scheduledForRemoval.size() > 0) {
			elements.removeAll(scheduledForRemoval);
		}
	}
}
