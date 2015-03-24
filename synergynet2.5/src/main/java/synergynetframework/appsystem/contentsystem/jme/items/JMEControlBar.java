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

import java.net.URL;

import com.jme.bounding.OrthogonalBoundingBox;
import com.jme.image.Texture;
import com.jme.image.Texture.WrapMode;
import com.jme.scene.Node;
import com.jme.scene.shape.Disk;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.ControlBar;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IControlBarImplementation;
import synergynetframework.appsystem.contentsystem.jme.items.utils.controlbar.ControlBarMover;
import synergynetframework.appsystem.contentsystem.jme.items.utils.controlbar.ControlBarMover.ControlBarMoverListener;


/**
 * The Class JMEControlBar.
 */
public class JMEControlBar extends JMEOrthoContainer implements IControlBarImplementation, ControlBarMoverListener {

	/** The control bar. */
	protected ControlBar controlBar;
	
	/** The bar. */
	protected Quad bar;
	
	/** The cursor. */
	protected Disk cursor;
	
	/** The finished bar. */
	protected Quad finishedBar;
	
	//protected BlendState alpha;
	/** The control bar mover. */
	protected ControlBarMover controlBarMover;
	
	/**
	 * Instantiates a new JME control bar.
	 *
	 * @param contentItem the content item
	 */
	public JMEControlBar(ContentItem contentItem) {
		super(contentItem);
		controlBar = (ControlBar)contentItem;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IControlBarImplementation#setControlBarWidth(float)
	 */
	@Override
	public void setControlBarWidth(float controlBarWidth) {
		render();
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IControlBarImplementation#setControlBarLength(float)
	 */
	@Override
	public void setControlBarLength(float controlBarLength) {
		render();
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IControlBarImplementation#setCurrentPosition(float)
	 */
	@Override
	public void setCurrentPosition(float currentPosition) {
		cursor.setLocalTranslation((float)((currentPosition-0.5)*controlBar.getControlBarLength()), bar.getLocalTranslation().y, bar.getLocalTranslation().z);
		finishedBar.setLocalTranslation((currentPosition-1)*controlBar.getControlBarLength()/2, bar.getLocalTranslation().y, bar.getLocalTranslation().z);
		finishedBar.updateGeometry(currentPosition*controlBar.getControlBarLength(), controlBar.getControlBarWidth());
	
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.jme.items.JMEContentItem#init()
	 */
	public void init(){
		super.init();
		
		/*
		alpha = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
		alpha.setEnabled( true );
		alpha.setBlendEnabled( true );
		alpha.setSourceFunction( BlendState.SourceFunction.SourceAlpha);
		alpha.setDestinationFunction( BlendState.DestinationFunction.OneMinusSourceAlpha);
		alpha.setTestEnabled( true );
		alpha.setTestFunction( BlendState.TestFunction.GreaterThan);
		*/
		buildBar();
		buildFinishedBar();
		buildCursor();
		
		cursor.setLocalTranslation(bar.getLocalTranslation().x-controlBar.getControlBarLength()/2, bar.getLocalTranslation().y, bar.getLocalTranslation().z);
		finishedBar.setLocalTranslation(bar.getLocalTranslation().x-controlBar.getControlBarLength()/2, bar.getLocalTranslation().y, bar.getLocalTranslation().z);
		
		controlBarMover = new ControlBarMover(cursor);	
		controlBarMover.addControlBarMoverListener(this);
		controlBarMover.setBarLength(controlBar.getControlBarLength());
		controlBarMover.setEnabled(controlBar.isControlBarMoverEnabled());
		
		Node controlBarNode =(Node)controlBar.getImplementationObject();
		controlBarNode.attachChild(cursor);
		controlBarNode.attachChild(finishedBar);
		controlBarNode.attachChild(bar);
				
		controlBarNode.updateGeometricState(0f, false);
		controlBarNode.updateModelBound();
	}
	
	/**
	 * Render.
	 */
	public void render(){
		bar.updateGeometry(controlBar.getControlBarLength(), controlBar.getControlBarWidth());
		cursor.setLocalTranslation((float) ((controlBar.getCurrentPosition()-0.5)*controlBar.getControlBarLength()), bar.getLocalTranslation().y, bar.getLocalTranslation().z);
		finishedBar.setLocalTranslation((controlBar.getCurrentPosition()-1)*controlBar.getControlBarLength()/2, bar.getLocalTranslation().y, bar.getLocalTranslation().z);
		controlBarMover.setBarLength( controlBar.getControlBarLength());
		
		this.setCurrentPosition(controlBar.getCurrentPosition());
		setBarTexture();
		setFinishedBarTexture();
		setCursorTexture();
	}
	
	/**
	 * Builds the bar.
	 */
	private void buildBar(){
		bar = new Quad(controlBar.getName()+"-ProgressBar", controlBar.getControlBarLength(), controlBar.getControlBarWidth());
		
		setBarTexture();
		bar.setModelBound(new OrthogonalBoundingBox());
		bar.updateGeometricState(0f, false);
		bar.updateModelBound();
	}
	
	/**
	 * Sets the bar texture.
	 */
	private void setBarTexture(){
		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		ts.setCorrectionType(TextureState.CorrectionType.Perspective);		
		Texture texture = TextureManager.loadTexture(controlBar.getBarImageResource(), Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);
		texture.setWrap(WrapMode.Repeat);
		ts.setTexture( texture );
		ts.apply();
		
		bar.setRenderState(ts );	
		bar.updateRenderState();		
	}
	
	/**
	 * Builds the cursor.
	 */
	private void buildCursor(){
		cursor = new Disk(controlBar.getName()+"-Cursor", 16, 32, (float) (controlBar.getControlBarWidth()));
		
		setCursorTexture();
		cursor.setModelBound(new OrthogonalBoundingBox());
		cursor.updateGeometricState(0f, false);
		cursor.updateModelBound();
	}
	
	/**
	 * Sets the cursor texture.
	 */
	private void setCursorTexture(){
		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		ts.setCorrectionType(TextureState.CorrectionType.Perspective);		
		Texture texture = TextureManager.loadTexture(controlBar.getCursorImageResource(), Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);
		texture.setWrap(WrapMode.Repeat);
		ts.setTexture( texture );
		ts.apply();
		
		cursor.setRenderState(ts );	
		cursor.updateRenderState();
	}
	
	/**
	 * Builds the finished bar.
	 */
	private void buildFinishedBar(){
		finishedBar = new Quad(controlBar.getName()+"-FinishedBar", 1, controlBar.getControlBarWidth());
		
		setFinishedBarTexture();
		finishedBar.setModelBound(new OrthogonalBoundingBox());
		finishedBar.updateGeometricState(0f, false);
		finishedBar.updateModelBound();
	}
	
	/**
	 * Sets the finished bar texture.
	 */
	private void setFinishedBarTexture(){
		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		ts.setCorrectionType(TextureState.CorrectionType.Perspective);		
		Texture texture = TextureManager.loadTexture(controlBar.getFinishedBarImageResource(), Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);
		texture.setWrap(WrapMode.Repeat);
		ts.setTexture( texture );
		ts.apply();
		
		finishedBar.setRenderState(ts );	
		finishedBar.updateRenderState();		
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.jme.items.utils.controlbar.ControlBarMover.ControlBarMoverListener#controlBarChanged(float, float)
	 */
	@Override
	public void controlBarChanged(float oldPosition,
			float newPosition) {
		finishedBar.setLocalTranslation((newPosition-1)*controlBar.getControlBarLength()/2, bar.getLocalTranslation().y, bar.getLocalTranslation().z);
		finishedBar.updateGeometry(newPosition*controlBar.getControlBarLength(), controlBar.getControlBarWidth());
		controlBar.setCurrentPosition(newPosition);
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IControlBarImplementation#addControlBarMoverListener(synergynetframework.appsystem.contentsystem.jme.items.utils.controlbar.ControlBarMover.ControlBarMoverListener)
	 */
	public void addControlBarMoverListener(ControlBarMoverListener l){
		this.controlBarMover.addControlBarMoverListener(l);
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IControlBarImplementation#setControlBarMoverEnabled(boolean)
	 */
	@Override
	public void setControlBarMoverEnabled(boolean controlBarMoverEnabled) {
		controlBarMover.setEnabled(controlBarMoverEnabled);		
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.jme.items.utils.controlbar.ControlBarMover.ControlBarMoverListener#cursorPressed()
	 */
	@Override
	public void cursorPressed() {
		cursor.setLocalScale(3f);
		
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.jme.items.utils.controlbar.ControlBarMover.ControlBarMoverListener#cursorReleased()
	 */
	@Override
	public void cursorReleased() {
		cursor.setLocalScale(1f);
		
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IControlBarImplementation#updateControlBar()
	 */
	@Override
	public void updateControlBar() {
		render();
		
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IControlBarImplementation#setFinishedBarImageResource(java.net.URL)
	 */
	@Override
	public void setFinishedBarImageResource(URL imageResource) {
		render();
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IControlBarImplementation#setBarImageResource(java.net.URL)
	 */
	@Override
	public void setBarImageResource(URL imageResource) {
		render();
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IControlBarImplementation#setCursorImageResource(java.net.URL)
	 */
	@Override
	public void setCursorImageResource(URL imageResource) {
		render();
	}
}
