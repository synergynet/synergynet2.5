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

package synergynetframework.appsystem.contentsystem.items;

import java.util.List;

import mit.ai.nl.core.Expression;
import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IMathPadImplementation;

public class MathPad extends SketchPad implements IMathPadImplementation{

	private static final long serialVersionUID = 3971797394081038623L;
	
	public MathPad(ContentSystem contentSystem, String name) {
		super(contentSystem, name);
	}

	@Override
	public void addMathHandwritingListener(MathHandwritingListener listener) {
		((IMathPadImplementation)this.contentItemImplementation).addMathHandwritingListener(listener);
	}
	
	@Override
	public void clearMathExpressions() {
		((IMathPadImplementation)this.contentItemImplementation).clearMathExpressions();
		
	}

	@Override
	public void startNewExpression() {
		((IMathPadImplementation)this.contentItemImplementation).startNewExpression();
		
	}
	
	@Override
	public void removeHandwritingListener(MathHandwritingListener listener) {
		((IMathPadImplementation)this.contentItemImplementation).removeHandwritingListener(listener);
	}

	@Override
	public void removeHandwritingListeners() {
		((IMathPadImplementation)this.contentItemImplementation).removeHandwritingListeners();
	}

	public interface MathHandwritingListener{
		public void expressionsWritten(List<Expression> expressions);
		
	}

	@Override
	public Expression getCurrentExpression() {
		return ((IMathPadImplementation)this.contentItemImplementation).getCurrentExpression();
	}

	@Override
	public List<Expression> getMathExpressions() {
		return ((IMathPadImplementation)this.contentItemImplementation).getMathExpressions();
	}

	@Override
	public void setMathEngineEnabled(boolean isEnabled) {
		((IMathPadImplementation)this.contentItemImplementation).setMathEngineEnabled(isEnabled);
	}
}
