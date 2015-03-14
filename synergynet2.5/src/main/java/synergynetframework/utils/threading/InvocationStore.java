/*
 * Copyright (c) 2008 University of Durham, England
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

package synergynetframework.utils.threading;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Allows for the passing of responsibility of the queueing and subsequent
 * executing of methods in one thread to the control of another thread.
 * Particularly useful when getting threaded events to be executed during
 * a gameloop.
 * 
 * @author dcs0ah1
 *
 */

public class InvocationStore {
	
	private List<InvocationData> calls = new ArrayList<InvocationData>();
	
	public void addMethodInvocation(Method m, Object obj, Object[] args) {
		InvocationData invocationData = new InvocationData(m, obj, args);
		synchronized(calls) {
			calls.add(invocationData);
		}
	}
	
	public void executeAll() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		synchronized(calls) {
			Iterator<InvocationData> i = calls.iterator();
			InvocationData invocationData;
			while(i.hasNext()) {
				invocationData = i.next();
				invocationData.method.invoke(invocationData.obj, invocationData.args);
				i.remove();
			}
		}
	}
	
	private class InvocationData {
		public Method method;
		public Object obj;
		public Object[] args;
		
		public InvocationData(Method m, Object obj, Object[] args) {
			this.method = m;
			this.obj = obj;
			this.args = args;
		}
	}

	public void reset() {
		synchronized(calls) {
			calls.clear();		
		}
	}
}
