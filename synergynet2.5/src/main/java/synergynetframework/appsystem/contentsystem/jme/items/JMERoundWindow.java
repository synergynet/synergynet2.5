/* Copyright (c) 2008 University of Durham, England
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

package synergynetframework.appsystem.contentsystem.jme.items;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.RoundFrame;
import synergynetframework.appsystem.contentsystem.items.RoundWindow;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IRoundWindowImplementation;
import synergynetframework.appsystem.contentsystem.items.utils.Background;
import synergynetframework.appsystem.contentsystem.items.utils.Border;


/**
 * The Class JMERoundWindow.
 */
public class JMERoundWindow extends JMEOrthoContainer implements IRoundWindowImplementation {

	/** The background frame. */
	protected RoundFrame backgroundFrame;
	
	/**
	 * Instantiates a new JME round window.
	 *
	 * @param contentItem the content item
	 */
	public JMERoundWindow(ContentItem contentItem) {
		super(contentItem);		
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.jme.items.JMEContentItem#init()
	 */
	public void init(){
		super.init();
		backgroundFrame = (RoundFrame)contentItem.getContentSystem().createContentItem(RoundFrame.class);
		RoundWindow window = (RoundWindow)contentItem;	
		backgroundFrame.setRadius(window.getRadius());
		backgroundFrame.setBackGround(window.getBackGround());
		backgroundFrame.setBorder(window.getBorder());	
		backgroundFrame.setOrder(-99999999);
		window.addSubItem(backgroundFrame);
	}

	
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.jme.items.JMEOrthoContainer#setBackGround(synergynetframework.appsystem.contentsystem.items.utils.Background)
	 */
	@Override
	public void setBackGround(Background backGround) {	
		backgroundFrame.setBackGround(backGround);
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.jme.items.JMEOrthoContainer#setBorder(synergynetframework.appsystem.contentsystem.items.utils.Border)
	 */
	@Override
	public void setBorder(Border border) {
		backgroundFrame.setBorder(border);
	}
	
	/**
	 * Lower index.
	 */
	public void lowerIndex(){
		backgroundFrame.setOrder(-999999999);
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IRoundWindowImplementation#setRadius(int)
	 */
	@Override
	public void setRadius(int radius) {
		backgroundFrame.setRadius(radius);
		
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IRoundWindowImplementation#getBackgroundFrame()
	 */
	public RoundFrame getBackgroundFrame(){
		return backgroundFrame;
	}
	
}