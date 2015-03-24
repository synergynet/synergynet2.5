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

package apps.photosorting;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.LightImageLabel;
import synergynetframework.appsystem.table.SynergyNetAppUtils;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.appsystem.table.appregistry.menucontrol.HoldTopRightConfirmVisualExit;


/**
 * The Class PhotoSortingApp.
 */
public class PhotoSortingApp extends DefaultSynergyNetApp {
	
	/** The content system. */
	private ContentSystem contentSystem;

	/**
	 * Instantiates a new photo sorting app.
	 *
	 * @param info the info
	 */
	public PhotoSortingApp(ApplicationInfo info) {
		super(info);		
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#addContent()
	 */
	@Override
	public void addContent() {
		
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(this);	
		setMenuController(new HoldTopRightConfirmVisualExit(this));
		SynergyNetAppUtils.addTableOverlay(this);
	
		LightImageLabel photo1 = (LightImageLabel)contentSystem.createContentItem(LightImageLabel.class);
		photo1.drawImage(PhotoSortingApp.class.getResource("1.jpg"));
		photo1.setImageLabelHeight(80);
		photo1.setScale(1.5f);
		photo1.setLocalLocation(200, 500);
		
		LightImageLabel photo2 = (LightImageLabel)contentSystem.createContentItem(LightImageLabel.class);
		photo2.drawImage(PhotoSortingApp.class.getResource("2.jpg"));
		photo2.setImageLabelHeight(120);
		photo2.setScale(1.5f);
		photo2.setLocalLocation(300, 300);
		
		LightImageLabel photo3 = (LightImageLabel)contentSystem.createContentItem(LightImageLabel.class);
		photo3.drawImage(PhotoSortingApp.class.getResource("3.jpg"));
		photo3.setImageLabelHeight(80);
		photo3.setScale(1.5f);
		photo3.setLocalLocation(700, 400);
		
		LightImageLabel photo4 = (LightImageLabel)contentSystem.createContentItem(LightImageLabel.class);
		photo4.drawImage(PhotoSortingApp.class.getResource("4.jpg"));
		photo4.setImageLabelHeight(120);
		photo4.setScale(1.5f);
		photo4.setLocalLocation(600, 200);

	}

}