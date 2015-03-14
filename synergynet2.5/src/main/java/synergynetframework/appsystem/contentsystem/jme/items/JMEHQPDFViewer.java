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

package synergynetframework.appsystem.contentsystem.jme.items;

import java.awt.Image;
import java.awt.Rectangle;

import com.jme.image.Texture;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.HQPDFViewer;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IHqPDFViewerImplementation;

public class JMEHQPDFViewer extends JMEPDFViewer implements IHqPDFViewerImplementation {

	protected HQPDFViewer item;
	protected TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();

	public JMEHQPDFViewer(ContentItem item) {
		super(item);
		this.item = (HQPDFViewer)item;
	}

	@Override
	protected void draw() {
		if (page!=null){
			Rectangle rect = new Rectangle(0,0,
				(int)page.getBBox().getWidth(),
				(int)page.getBBox().getHeight());

			//generate the image
			Image img = page.getImage(
				rect.width, rect.height, //width & height
				rect, // clip rect
				null, // null for the ImageObserver
				true, // fill background with white
				true  // block until drawing is done
			);
			
			Texture t = TextureManager.loadTexture(img, Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear, true);
			ts.setTexture(t,0);
			
			BlendState as = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
			as.setBlendEnabled(true);
			as.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
			as.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
			as.setTestEnabled(true);
			as.setTestFunction(BlendState.TestFunction.GreaterThan);
			spatial.setRenderState(as);
			
			((Quad)spatial).setRenderState(ts);
			((Quad)spatial).updateRenderState();
		}
		
	}	
	@Override
	protected void resizePDFView() {
		((Quad)spatial).updateGeometry(width, height);
		((Quad)spatial).updateRenderState();
		((Quad)spatial).updateGeometricState(0f, true);
		((Quad)spatial).updateModelBound();
	}
	
}
