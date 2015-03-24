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

package apps.conceptmap.graphcomponents;

import java.util.ArrayList;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import apps.conceptmap.utility.GraphManager;

/**
 * The Class GraphComponent.
 */
public abstract class GraphComponent {
	
	/**
	 * The listener interface for receiving optionMessage events. The class that
	 * is interested in processing a optionMessage event implements this
	 * interface, and the object created with that class is registered with a
	 * component using the component's
	 * <code>addOptionMessageListener<code> method. When
	 * the optionMessage event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see OptionMessageEvent
	 */
	public interface OptionMessageListener {

		/**
		 * Message processed.
		 *
		 * @param msg
		 *            the msg
		 */
		public void messageProcessed(OptionMessage msg);
	}

	/** The content system. */
	protected ContentSystem contentSystem;
	
	/** The graph manager. */
	protected GraphManager graphManager;

	/** The listeners. */
	private transient ArrayList<OptionMessageListener> listeners = new ArrayList<OptionMessageListener>();
	
	/**
	 * Instantiates a new graph component.
	 *
	 * @param contentSystem
	 *            the content system
	 * @param gManager
	 *            the g manager
	 */
	public GraphComponent(ContentSystem contentSystem, GraphManager gManager) {
		this.contentSystem = contentSystem;
		this.graphManager = gManager;
	}

	/**
	 * Adds the option message listener.
	 *
	 * @param l
	 *            the l
	 */
	public void addOptionMessageListener(OptionMessageListener l) {
		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}

	/**
	 * Fire message processed.
	 *
	 * @param msg
	 *            the msg
	 */
	public void fireMessageProcessed(OptionMessage msg) {
		for (OptionMessageListener l : listeners) {
			l.messageProcessed(msg);
		}
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public abstract String getName();

	/**
	 * Removes the option message listeners.
	 */
	public void removeOptionMessageListeners() {
		listeners.clear();
	}
}