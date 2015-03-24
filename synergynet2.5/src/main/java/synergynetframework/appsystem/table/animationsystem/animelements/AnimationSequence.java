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


/**
 * The Class AnimationSequence.
 */
public class AnimationSequence extends AnimationElement {
	
	/** The elements. */
	protected List<AnimationElement> elements = new ArrayList<AnimationElement>();
	
	/** The current element. */
	protected AnimationElement currentElement = null;
	
	/** The repeating. */
	protected boolean repeating = false;
	
	/** The current index. */
	protected int currentIndex = 0;
	
	/** The finished. */
	protected boolean finished = false;
	
	/**
	 * Instantiates a new animation sequence.
	 */
	public AnimationSequence() {
	}
	
	/**
	 * Instantiates a new animation sequence.
	 *
	 * @param elems the elems
	 */
	public AnimationSequence(AnimationElement... elems) {
		for(AnimationElement elem : elems) {
			addAnimationElement(elem);
		}
	}
	
	/**
	 * Adds the animation element.
	 *
	 * @param elem the elem
	 */
	public void addAnimationElement(AnimationElement elem) {
		if(currentElement == null) { 
			currentElement = elem;
			currentIndex = 0;
		}
		elements.add(elem);
	}
	
	/**
	 * Sets the repeating.
	 *
	 * @param b the new repeating
	 */
	public void setRepeating(boolean b) {
		repeating = b;
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.animationsystem.AnimationElement#elementStart(float)
	 */
	@Override
	public void elementStart(float tpf) {}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.animationsystem.AnimationElement#isFinished()
	 */
	@Override
	public boolean isFinished() {
		return finished ;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.animationsystem.AnimationElement#updateAnimationState(float)
	 */
	@Override
	public void updateAnimationState(float tpf) {
		if(currentElement == null) return;
		if(currentElement.isFinished()) {
			currentElement = getNextElement();
			if(currentElement != null) currentElement.reset();
		}
		if(currentElement == null) {
			finished = true;			
		}else{
			currentElement.updateState(tpf);
		}
	}

	/**
	 * Gets the next element.
	 *
	 * @return the next element
	 */
	private AnimationElement getNextElement() {
		if(repeating) {			
			currentIndex++;
			currentIndex = currentIndex % elements.size();
			return elements.get(currentIndex);
		}else{
			currentIndex++;
			if(currentIndex > elements.size() - 1) {
				return null;
			}else{				
				return elements.get(currentIndex);
			}
		}		
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.animationsystem.AnimationElement#reset()
	 */
	@Override
	public void reset() {
		currentIndex = 0;
		currentElement = elements.get(currentIndex);
	}

	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		AnimationSequence seq = new AnimationSequence();
		seq.setRepeating(false);
		seq.addAnimationElement(new TestAnimationElement("One"));
		seq.addAnimationElement(new TestAnimationElement("Two"));
		seq.addAnimationElement(new TestAnimationElement("Three"));
		int i = 0;
		while(!seq.isFinished()) {
			System.out.println("Call " + i);
			seq.updateAnimationState(1f);
			i++;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Done, seq finished.");
	}



}
