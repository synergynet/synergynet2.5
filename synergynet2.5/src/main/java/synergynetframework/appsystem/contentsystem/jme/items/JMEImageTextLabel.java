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

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

import synergynetframework.appsystem.contentsystem.ContentSystemUtils;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.ImageTextLabel;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IImageTextLabelImplementation;

public class JMEImageTextLabel extends JMEMultiLineTextLabel implements IImageTextLabelImplementation{

	private ImageTextLabel item;
	private int textHeight;
	private int textWidth;
	private int innerImageHeight;
	private int innerImageWidth; 
	private int labelHeight;	
	private int labelWidth;
	
	public JMEImageTextLabel(ContentItem contentItem) {
		super(contentItem);
		this.item = (ImageTextLabel)contentItem;			
	}
	
	@Override
	public void setImageInfo(URL imageResource) {
		resize();		
	}
	
	protected void draw() {
					
		gfx.setColor(item.getTextColour());		
		gfx.setFont(item.getFont());
		int y = innerImageHeight+ item.getBorderSize()+ this.textHeight/2+ 6;
		for(int i = 0; i < item.getLines().size(); i++) {			
			gfx.drawString(item.getLines().get(i),  this.labelWidth/2 - textWidth/2, y);
			y += textHeight;
		}
		if(item.getImageInfo().getImageResource() != null){
			Image image = new ImageIcon(item.getImageInfo().getImageResource()).getImage();
			gfx.drawImage(image, this.labelWidth/2 - innerImageWidth/2, item.getBorderSize() , innerImageWidth, innerImageHeight, null);
		}
	}

	public void resize(){
		
		this.innerImageHeight = item.getImageInfo().getHeight();
		this.innerImageWidth = item.getImageInfo().getWidth();
		this.labelHeight = item.getHeight();
		this.labelWidth = item.getWidth();
		
		int maxWidth = 1;
		for(int i = 0; i < item.getLines().size(); i++) {
			int tw = ContentSystemUtils.getStringWidth(item.getFont(), item.getLines().get(i));
			if(tw > maxWidth) maxWidth = tw;
		}
		
		textWidth =  maxWidth;
		textHeight = ContentSystemUtils.getFontHeight(item.getFont());
		
		if(item.isAutoFitSize()){
			this.innerImageHeight = item.getImageInfo().getHeight();
			this.innerImageWidth = item.getImageInfo().getWidth();
			
			this.labelHeight = textHeight * item.getLines().size() + innerImageHeight + item.getBorderSize()*2;
			this.labelWidth =  textWidth> innerImageWidth? textWidth + 2*item.getBorderSize(): innerImageWidth + 2*item.getBorderSize();
		}
		else{		
			this.labelHeight = item.getHeight();
			this.labelWidth = item.getWidth();
			
			innerImageHeight = this.labelHeight - item.getBorderSize()*2 - this.textHeight * item.getLines().size();
			if(item.getImageInfo().getImageResource() != null){
				Image image = new ImageIcon(item.getImageInfo().getImageResource()).getImage();
				if (image != null)
					innerImageWidth = innerImageHeight *image.getWidth(null)/image.getHeight(null) - item.getBorderSize()*2;
			}
		}
		
		int w = this.labelWidth;
		int h = this.labelHeight;		
		this.graphicsImageQuad.updateGeometry(w, h);	
		if(w < 2) w = 2;
		if(h < 2) h = 2;
		this.graphicsImageQuad.recreateImageForSize(w, h);
		
		item.setHeight(this.labelHeight);
		item.setWidth(labelWidth);
		
		gfx = this.graphicsImageQuad.getImageGraphics();	
		
		render();
	}


}
