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



import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;


import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.Frame;
import synergynetframework.appsystem.contentsystem.items.MTKeyboard;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.VncFrame;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IVncFrameImplementation;
import synergynetframework.appsystem.contentsystem.items.listener.ItemListener;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;
import synergynetframework.appsystem.contentsystem.items.utils.vnc.VncViewer;
import synergynetframework.appsystem.contentsystem.items.utils.vnc.VncCanvas.VncImageListener;
import synergynetframework.jme.gfx.twod.keyboard.MTKeyListener;



/**
 * The Class JMEVncFrame.
 */
public class JMEVncFrame extends JMEWindow implements IVncFrameImplementation, VncImageListener, MTKeyListener {
	
	/** The item. */
	protected VncFrame item;
	
	/** The inner frame. */
	protected Frame innerFrame;
	
	/** The vncviewer. */
	protected VncViewer vncviewer;
	
	/** The framerate. */
	private float framerate;
	
	/** The delay. */
	private float delay = 0.5f;
	
	/** The screen width. */
	private int screenWidth = 1024 ;
	
	/** The screen height. */
	private int screenHeight = 768 ;
	
	/** The initial scale. */
	private float initialScale = 0.3f;
	
	/** The vnc border width. */
	private int vncBorderWidth = 70;
	
	/** The listener added. */
	private boolean listenerAdded = false;
	
	/** The image. */
	private Image image = null;
	
	/** The host. */
	private String host = null;
	
	/** The port. */
	private int port;
	
	/** The password. */
	private String password = null;
	
	/** The keyboard. */
	private MTKeyboard keyboard;
	
	/** The keyboard btn. */
	private SimpleButton keyboardBtn;
	
	/**
	 * Instantiates a new JME vnc frame.
	 *
	 * @param contentItem the content item
	 */
	public JMEVncFrame(ContentItem contentItem){
		super(contentItem);
		item = (VncFrame) contentItem;
		innerFrame = (Frame)item.getContentSystem().createContentItem(Frame.class);
		innerFrame.setBorderColour(Color.black);
		innerFrame.setWidth(screenWidth);
		innerFrame.setHeight(screenHeight);
		innerFrame.setScale(initialScale);
		framerate = delay;
	
		innerFrame.addItemListener(new ItemListener(){

			@Override
			public void cursorPressed(ContentItem item, long id, float x,
					float y, float pressure) {
				if(vncviewer != null && vncviewer.vc != null)	
					Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(new MouseEvent( vncviewer.vc,MouseEvent.MOUSE_PRESSED,0, 0,(int)x,(int)y, 2,false ) );
			}

			@Override
			public void cursorChanged(ContentItem item, long id, float x,
					float y, float pressure) {
				if(vncviewer != null && vncviewer.vc != null)	
					Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(new MouseEvent( vncviewer.vc,MouseEvent.MOUSE_MOVED,0, 0,(int)x,(int)y, 2,false ) );

			}

			@Override
			public void cursorReleased(ContentItem item, long id, float x,
					float y, float pressure) {
				if(vncviewer != null && vncviewer.vc != null)	
					Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(new MouseEvent( vncviewer.vc,MouseEvent.MOUSE_RELEASED,0, 0,(int)x,(int)y, 2,false ) );
			}

			@Override
			public void cursorClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				 
				
			}

			@Override
			public void cursorRightClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				 
				
			}

			@Override
			public void cursorLongHeld(ContentItem item, long id, float x,
					float y, float pressure) {
				 
				
			}

			@Override
			public void cursorDoubleClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				 
				
			}
		});
		
		keyboard = (MTKeyboard) item.getContentSystem().createContentItem(MTKeyboard.class);
		keyboard.setScale(0.4f);
		keyboard.setBorderSize(1);
		keyboard.centerItem();
		keyboard.addKeyListener(this);
		keyboard.setVisible(false);
		
		keyboardBtn = (SimpleButton) keyboard.getContentSystem().createContentItem(SimpleButton.class);
		keyboardBtn.setAutoFitSize(false);
		keyboardBtn.setWidth(23);
		keyboardBtn.setHeight(23);
		keyboardBtn.setLocalLocation((item.getWidth()-keyboardBtn.getWidth()-vncBorderWidth)/2 , -item.getHeight()/2 + keyboardBtn.getHeight());
		keyboardBtn.setBackgroundColour(Color.white);
		keyboardBtn.drawImage(JMEVncFrame.class.getResource("utils/arrow-down.jpg"),0,0,23,23);
		keyboardBtn.addButtonListener(new SimpleButtonAdapter(){
			
			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				if(!keyboard.isVisible()){
					item.addSubItem(keyboard);
					keyboard.setScale(0.4f);
					keyboard.setBorderSize(1);
					keyboard.setAngle(0);
					keyboard.setLocalLocation(0, -220);
					keyboard.setVisible(true);
					keyboardBtn.removeAllImages();
					keyboardBtn.drawImage(JMEVncFrame.class.getResource("utils/arrow-up.jpg"),0,0,23,23);
				}else{
					item.detachSubItem(keyboard);
					keyboard.setVisible(false);
					keyboardBtn.removeAllImages();
					keyboardBtn.drawImage(JMEVncFrame.class.getResource("utils/arrow-down.jpg"),0,0,23,23);
				}
				innerFrame.setRotateTranslateScalable(false);
			}
			
		});
		keyboardBtn.setOrder(999999);
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.jme.items.JMEWindow#init()
	 */
	@Override
	public void init(){
		super.init();
		item.getBackgroundFrame().setBorderColour(Color.BLACK);
		item.getBackgroundFrame().setBackgroundColour(Color.red);
		item.addSubItem(innerFrame);
		item.addSubItem(keyboardBtn);
		item.setWidth((int)(this.screenWidth*this.initialScale)+this.vncBorderWidth);
		item.setHeight((int)(this.screenHeight*this.initialScale)+this.vncBorderWidth);
		innerFrame.setRotateTranslateScalable(false);
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.jme.items.JMEOrthoContentItem#update(float)
	 */
	public void update(float interpolation) {
		
		if((framerate - interpolation) > 0){
			framerate-= interpolation;
			return;
		}
		framerate = delay;
		
		if(!listenerAdded && vncviewer != null && vncviewer.vc != null && this != null){
			vncviewer.vc.addVncImageListener(this);
			listenerAdded = true;
		}
		
		if(image != null && vncviewer != null && vncviewer.vc != null){
			innerFrame.getGraphicsContext().drawImage(image,innerFrame.getBorderSize() ,innerFrame.getBorderSize(), innerFrame.getWidth()-2*innerFrame.getBorderSize(), innerFrame.getHeight()-2*innerFrame.getBorderSize(), null);
			innerFrame.flushGraphics();
			image = null;
		}
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.utils.vnc.VncCanvas.VncImageListener#imageCreated(java.awt.Image)
	 */
	@Override
	public void imageCreated(Image image) {
		if(image != null){
			synchronized(this){
				this.image = image;
			}
		}
	}

	/* (non-Javadoc)
	 * @see synergynetframework.jme.gfx.twod.keyboard.MTKeyListener#keyPressedEvent(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyPressedEvent(KeyEvent evt) {
		System.out.println(evt.getKeyChar());
		vncviewer.vc.keyPressed(evt);
	}

	/* (non-Javadoc)
	 * @see synergynetframework.jme.gfx.twod.keyboard.MTKeyListener#keyReleasedEvent(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyReleasedEvent(KeyEvent evt) {
		vncviewer.vc.keyReleased(evt);
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IVncFrameImplementation#setConnectionSettings(java.lang.String, int, java.lang.String)
	 */
	@Override
	public void setConnectionSettings(String host, int port, String password) {
		this.host = host;
		this.port = port;
		this.password = password;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IVncFrameImplementation#connect()
	 */
	@Override
	public void connect() {
			if(vncviewer == null){
				vncviewer = new VncViewer(host, port, password);
			}
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IVncFrameImplementation#disconnect()
	 */
	@Override
	public void disconnect() {
		if(vncviewer != null){ 
			vncviewer.disconnect();
			vncviewer = null;
		}
		
	}
}
