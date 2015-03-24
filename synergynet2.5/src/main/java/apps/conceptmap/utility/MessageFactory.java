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

package apps.conceptmap.utility;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import apps.conceptmap.graphcomponents.GraphComponent;
import apps.conceptmap.graphcomponents.OptionMessage;

/**
 * A factory for creating Message objects.
 */
public class MessageFactory {
	
	/** The instance. */
	private static MessageFactory instance;

	/** The ok cancel message. */
	public static int OK_CANCEL_MESSAGE = 0;
	
	/** The yes no message. */
	public static int YES_NO_MESSAGE = 1;

	/**
	 * Gets the single instance of MessageFactory.
	 *
	 * @return single instance of MessageFactory
	 */
	public static MessageFactory getInstance() {
		if (instance == null) {
			instance = new MessageFactory();
		}
		return instance;
	}

	/**
	 * Instantiates a new message factory.
	 */
	private MessageFactory() {
	}

	/**
	 * Creates a new Message object.
	 *
	 * @param contentSystem
	 *            the content system
	 * @param gManager
	 *            the g manager
	 * @param parentComponent
	 *            the parent component
	 * @param text
	 *            the text
	 * @param messageType
	 *            the message type
	 * @return the option message
	 */
	public OptionMessage createOptionMessage(ContentSystem contentSystem,
			GraphManager gManager, GraphComponent parentComponent, String text,
			int messageType) {
		OptionMessage oMsg = new OptionMessage(contentSystem, gManager,
				parentComponent, text, messageType);
		return oMsg;
	}
	
}
