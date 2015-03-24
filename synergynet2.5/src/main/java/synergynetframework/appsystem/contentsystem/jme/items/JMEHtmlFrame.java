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



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;


import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.HtmlFrame;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IHtmlFrameImplementation;



/**
 * The Class JMEHtmlFrame.
 */
public class JMEHtmlFrame extends JMEFrame implements IHtmlFrameImplementation {

	/** The item. */
	protected HtmlFrame item;
	
	/** The frame. */
	protected JFrame frame;
	
	/** The pane. */
	protected JTextPane pane;
	
	/** The image. */
	protected BufferedImage image;
	
	/** The g. */
	protected Graphics2D g;

	/**
	 * Instantiates a new JME html frame.
	 *
	 * @param contentItem the content item
	 */
	public JMEHtmlFrame(ContentItem contentItem){
		super(contentItem);
		item = (HtmlFrame) contentItem;
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.jme.items.JMEFrame#init()
	 */
	@Override
	public void init(){
		frame = new JFrame("jframe");
		frame.getContentPane().setLayout(new BorderLayout());
		frame.setSize(item.getWidth(), item.getHeight());
		pane = new JTextPane();
		pane.setContentType("text/html");
		pane.setEditable(false);
		pane.setOpaque( true );
		frame.getContentPane().add(pane, BorderLayout.CENTER);
		frame.getContentPane().setBackground(Color.blue);
		image   = new BufferedImage((int)frame.getSize().getWidth(), (int)frame.getSize().getHeight(),  BufferedImage.TYPE_INT_RGB);
		g = image.createGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, image.getWidth(), image.getHeight());
		item.setMaxWidth(item.getMaxWidth());
		if(item.getHtmlContent() != null) item.setHtmlContent(item.getHtmlContent());
		spatial.updateModelBound();
		spatial.updateRenderState();
		super.init();
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.jme.items.JMEFrame#resize()
	 */
	@Override
	public void resize(){
		super.resize();
		frame.setSize(item.getWidth()+frame.getInsets().left+frame.getInsets().right, item.getHeight()+frame.getInsets().top+frame.getInsets().bottom);
		image   = new BufferedImage((int)item.getWidth()- 2* item.getBorderSize(), (int)item.getHeight()- 2* item.getBorderSize(),  BufferedImage.TYPE_INT_RGB);
		g = image.createGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, image.getWidth(), image.getHeight());
		pane.setSize(image.getWidth(), image.getHeight());
		this.render();
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IHtmlFrameImplementation#setHtmlContent(java.lang.String)
	 */
	@Override
	public void setHtmlContent(String html) {
		pane.setText(html);
		validate();
	}
	
	/**
	 * Validate.
	 */
	protected void validate(){
		pane.validate();
		frame.validate();
		frame.pack();
		if(item.isAutoFitSize()){
			int width = Math.min((int)pane.getPreferredSize().getWidth(), item.getMaxWidth());
			item.setWidth(width + 2* item.getBorderSize());
			item.setHeight((int)pane.getPreferredSize().getHeight()+2*item.getBorderSize());
		}else{
			this.resize();
		}
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.jme.items.JMEFrame#render()
	 */
	@Override
	protected void render(){
		super.render();
		pane.paint( g );
		g.dispose();

		if(image != null && item != null){
			this.getGraphicsContext().drawImage(image,item.getBorderSize() ,item.getBorderSize(), item.getWidth()-2*item.getBorderSize(), item.getHeight()-2*item.getBorderSize(), null);
			this.flushGraphics();
		}
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IHtmlFrameImplementation#setMaxWidth(int)
	 */
	@Override
	public void setMaxWidth(int maxWidth) {
		
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IHtmlFrameImplementation#getPane()
	 */
	@Override
	public JTextPane getPane() {
		return pane;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IHtmlFrameImplementation#insertString(int, java.lang.String, javax.swing.text.AttributeSet)
	 */
	@Override
	public void insertString(int offset, String str, AttributeSet attr) {
		try {
			pane.getDocument().insertString(offset, str, attr);
			validate();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}		
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IHtmlFrameImplementation#remove(int, int)
	 */
	@Override
	public void remove(int offset, int length) {
		try {
			pane.getDocument().remove(offset, length);
			validate();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}			
	}
}
