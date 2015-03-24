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

package synergynetframework.appsystem.services.net.rapidnetworkmanager.handlers;

import java.util.ArrayList;
import java.util.List;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.tablecomms.client.TableCommsApplicationListener;
import synergynetframework.appsystem.services.net.tablecomms.messages.TableMessage;



/**
 * The Class DefaultMessageHandler.
 */
public class DefaultMessageHandler implements TableCommsApplicationListener{

	/** The message processors. */
	protected List<MessageProcessor> messageProcessors = new ArrayList<MessageProcessor>();
	
	/**
	 * Instantiates a new default message handler.
	 */
	public DefaultMessageHandler() {}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.services.net.tablecomms.client.TableCommsApplicationListener#messageReceived(java.lang.Object)
	 */
	@Override
	public void messageReceived(Object obj) {
		if (TableIdentity.getTableIdentity().hashCode()==((TableMessage)obj).getSender().hashCode()) return;
		try {
			for(MessageProcessor messageProcessor: messageProcessors) messageProcessor.process(obj);
		} catch (Exception e) {
			 
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.services.net.tablecomms.client.TableCommsApplicationListener#tableDisconnected()
	 */
	@Override
	public void tableDisconnected() {
		 
		
	}
	
	/**
	 * Register message processor.
	 *
	 * @param messageProcessor the message processor
	 */
	public void registerMessageProcessor(MessageProcessor messageProcessor){
		if(!messageProcessors.contains(messageProcessor)) messageProcessors.add(messageProcessor);
	}
	
	/**
	 * Removes the message processor.
	 *
	 * @param messageProcessor the message processor
	 */
	public void removeMessageProcessor(MessageProcessor messageProcessor){
		messageProcessors.remove(messageProcessor);
	}
	
	/**
	 * Removes the message processors.
	 */
	public void removeMessageProcessors(){
		messageProcessors.clear();
	}
	
	/**
	 * Gets the message processors.
	 *
	 * @return the message processors
	 */
	public List<MessageProcessor> getMessageProcessors(){
		return messageProcessors;
	}

}
