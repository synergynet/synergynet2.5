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

package apps.lightrays.gfxlib;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The Class DrawableStack.
 */
public class DrawableStack extends DrawableElement {
	
	/** The name_stack. */
	protected Map<String, DrawableElement> name_stack;

	/** The stack. */
	protected List<DrawableElement> stack;

	/**
	 * Instantiates a new drawable stack.
	 */
	public DrawableStack() {
		stack = new ArrayList<DrawableElement>();
		name_stack = new HashMap<String, DrawableElement>();
	}

	/*
	 * (non-Javadoc)
	 * @see apps.lightrays.gfxlib.DrawableElement#draw(java.awt.Graphics2D,
	 * long)
	 */
	public void draw(Graphics2D gfx, long tick_count) {
		Iterator<DrawableElement> i = stack.iterator();
		DrawableElement de;
		while (i.hasNext()) {
			de = i.next();
			// System.out.println("Drawing " + de);
			de.draw(gfx, tick_count);
		}
	}

	/**
	 * Gets the element.
	 *
	 * @param name
	 *            the name
	 * @return the element
	 */
	public DrawableElement getElement(String name) {
		return name_stack.get(name);
	}

	/**
	 * Push element.
	 *
	 * @param name
	 *            the name
	 * @param el
	 *            the el
	 */
	public void pushElement(String name, DrawableElement el) {
		if (name_stack.containsKey(name)) {
			Object removed = name_stack.remove(name);
			int indx = stack.indexOf(removed);
			stack.remove(indx);
		}

		stack.add(el);
		name_stack.put(name, el);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		Iterator<DrawableElement> i = stack.iterator();
		DrawableElement de;
		while (i.hasNext()) {
			de = i.next();
			sb.append(de.toString() + ",");
		}
		return sb.toString();
	}
	
}
