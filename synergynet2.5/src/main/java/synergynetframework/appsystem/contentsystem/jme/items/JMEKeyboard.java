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

package synergynetframework.appsystem.contentsystem.jme.items;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.Keyboard;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IKeyboardImplementation;
import synergynetframework.appsystem.contentsystem.items.utils.Background;
import synergynetframework.appsystem.contentsystem.items.utils.Border;
import synergynetframework.appsystem.contentsystem.jme.items.utils.KeyboardWrapper;
import synergynetframework.jme.cursorsystem.MultiTouchElementRegistry;
import synergynetframework.jme.cursorsystem.cursordata.ScreenCursor;
import synergynetframework.jme.cursorsystem.elements.twod.OrthoCursorEventDispatcher;
import synergynetframework.jme.gfx.twod.keyboard.Key;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;

public class JMEKeyboard extends JMEQuadContentItem implements IKeyboardImplementation{

	protected Keyboard keyboard;
	protected KeyboardWrapper keyboardWrapper;
	
	private int imgWidth;
	private int imgHeight;
	private ImageIcon keyboardImage;
	
	protected Map<Integer,Key> currentKeysPressed = new HashMap<Integer,Key>();
	protected List<Key> keys;
	protected boolean capsLockOn;
	protected JFrame fakeComponent;
	
	protected float pixelsPerUnit = 2f;
	
	public JMEKeyboard(ContentItem contentItem) {
		super(contentItem, new Quad(contentItem.getName(),1,1));
		keyboard = (Keyboard) contentItem;
		fakeComponent = new JFrame();
		fakeComponent.setSize(10,10);
		fakeComponent.setVisible(false);
		fakeComponent.setUndecorated(true);
	}
	
	public void setRotateTranslateScalable(boolean isEnabled, boolean overRide){
		if (overRide){
			super.setRotateTranslateScalable(isEnabled);
		}else{
			setRotateTranslateScalable(isEnabled);
		}
	}

	public void setRotateTranslateScalable(boolean isEnable){
		if (this.orthoControlPointRotateTranslateScale != null && MultiTouchElementRegistry.getInstance().isRegistered(orthoControlPointRotateTranslateScale)){
			MultiTouchElementRegistry.getInstance().unregister(this.orthoControlPointRotateTranslateScale);
			this.orthoControlPointRotateTranslateScale = null;
			this.rotateTranslateScalable = false;
		}
		Spatial targetSpatial;
		if(keyboard.getParent() != null)
			targetSpatial = spatial.getParent();
		else
			targetSpatial = spatial;
		keyboardWrapper = new KeyboardWrapper(this, targetSpatial,new Rectangle(12, 108, 1002, 228));
		keyboardWrapper.addRotateTranslateScaleListener(this);
		keyboardWrapper.setScaleLimits(0.1f, 5f);
	}
	
	public void cursorDragged(long id, int x, int y) {
	}

	
	
	@Override
	public void cursorPressed(
			OrthoCursorEventDispatcher commonCursorEventDispatcher,
			ScreenCursor c, MultiTouchCursorEvent event) {
		
		super.cursorPressed(commonCursorEventDispatcher, c, event);
		Point p = this.getCurrentKeyboard2DCoordsForCursor(c);
		Key k = getKeyAtLocation(p.x, p.y);
		if(k != null) {
			char keyChar = k.getKeyChar(upperCaseModeOn());
			KeyEvent keyEvent = new KeyEvent(fakeComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), getModifiers(), k.key, keyChar);
			keyboard.fireKeyPressed(keyEvent);
			this.currentKeysPressed.put(k.key, k);
		}
		
	}

	@Override
	public void cursorReleased(
			OrthoCursorEventDispatcher commonCursorEventDispatcher,
			ScreenCursor c, MultiTouchCursorEvent event) {
		
		super.cursorReleased(commonCursorEventDispatcher, c, event);
		Point p = this.getCurrentKeyboard2DCoordsForCursor(c);
		Key k = getKeyAtLocation(p.x, p.y);
		if(k != null) {
			
			if(k.key == KeyEvent.VK_CAPS_LOCK) {
				toggleCapsLock();
			}
			
			char keyChar = k.getKeyChar(upperCaseModeOn());
			KeyEvent keyEvent = new KeyEvent(fakeComponent, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), getModifiers(), k.key, keyChar);
			keyboard.fireKeyReleased(keyEvent);
			this.currentKeysPressed.remove(k.key);
		}
		
	}
/*
	public void cursorPressed(long cursorID, int x, int y) {
		
		Key k = getKeyAtLocation(x, y);
		if(k != null) {
			char keyChar = k.getKeyChar(upperCaseModeOn());
			KeyEvent event = new KeyEvent(fakeComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), getModifiers(), k.key, keyChar);
			keyboard.fireKeyPressed(event);
			this.currentKeysPressed.put(k.key, k);
		}
		
	}

	public void cursorReleased(long cursorID, int x, int y) {
		
		Key k = getKeyAtLocation(x, y);
		if(k != null) {
			
			if(k.key == KeyEvent.VK_CAPS_LOCK) {
				toggleCapsLock();
			}
			
			char keyChar = k.getKeyChar(upperCaseModeOn());
			KeyEvent event = new KeyEvent(fakeComponent, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), getModifiers(), k.key, keyChar);
			keyboard.fireKeyReleased(event);
			this.currentKeysPressed.remove(k.key);
		}
		
	}
*/
	
	@Override
	public void setAutoFitSize(boolean isEnabled) {
		 
		
	}

	public int getImageHeight() {
		return imgHeight;
	}

	public int getImageWidth() {
		return imgWidth;
	}

	@Override
	public void setBackGround(Background backGround) {
		 
		
	}

	@Override
	public void setBorder(Border border) {
		 
		
	}

	@Override
	public boolean isKeyPressed(int vk) {
		return currentKeysPressed.keySet().contains(vk);
	}

	private boolean upperCaseModeOn() {
		// xor of shift pressed and caps lock on
		return isKeyPressed(KeyEvent.VK_SHIFT) ^ capsLockOn;
	}

	@Override
	public void setKeyDefinitions(List<Key> keyDefinitions) {
		keys = keyDefinitions;
	}

	@Override
	public void setKeyboardImageResource(URL keyboardImageResource) {
		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		Texture t = TextureManager.loadTexture(keyboardImageResource, Texture.MinificationFilter.NearestNeighborLinearMipMap, Texture.MagnificationFilter.NearestNeighbor, com.jme.image.Image.Format.GuessNoCompression,1,true);
		ts.setTexture(t);
		this.spatial.setRenderState(ts);
		keyboardImage = new ImageIcon(keyboardImageResource);
		render();
	}

	@Override
	public ArrayList<Key> getCurrentKeysPressed() {
		return (ArrayList<Key>)currentKeysPressed.values();
	}

	@Override
	public void setPixelsPerUnit(float pixelsPerUnit) {
		this.pixelsPerUnit = pixelsPerUnit;
		render();
	}
	
	private void render(){
		if(keyboardImage != null){
			imgWidth = (int)(keyboardImage.getIconWidth());
			imgHeight = (int)(keyboardImage.getIconHeight());		
			int w = (int)(imgWidth/pixelsPerUnit);
			int h = (int)(imgHeight/pixelsPerUnit);
			((Quad)this.spatial).updateGeometry(w, h);
			this.spatial.updateRenderState();
			keyboard.setWidth(w);
			keyboard.setHeight(h);
		}
	}
	
	private void toggleCapsLock() {
		capsLockOn = !capsLockOn;
	}

	private Key getKeyAtLocation(int x, int y) {
		for(Key k : keys) {
			if(k.area.contains(x, y)) {
				return k;
			}
		}
		return null;
	}

	private int getModifiers() {
		int modifiers = 0;
		if(isKeyPressed(KeyEvent.VK_SHIFT)){
			modifiers &= KeyEvent.VK_SHIFT;
		}
		return modifiers;
	}

	@Override
	public void setHeight(int height) {
		 
		
	}

	@Override
	public void setWidth(int width) {
		 
		
	}
	
	private Point getCurrentKeyboard2DCoordsForCursor(ScreenCursor cursor) {
		if(cursor == null) return null;
		Vector3f cursorPosition = new Vector3f(cursor.getCurrentCursorScreenPosition().x, cursor.getCurrentCursorScreenPosition().y, 0f);
		Vector3f selectionLocal = new Vector3f();
		this.spatial.worldToLocal(cursorPosition, selectionLocal);
		selectionLocal.addLocal(new Vector3f(((Quad)this.spatial).getWidth()/2f, ((Quad)this.spatial).getHeight()/2f, 0f));		
		Point p = new Point();
		p.x = (int)(selectionLocal.x / keyboard.getWidth() * imgWidth);
		p.y = imgHeight - ((int)(selectionLocal.y / keyboard.getHeight() * imgHeight));
		return p;		
	}


}
