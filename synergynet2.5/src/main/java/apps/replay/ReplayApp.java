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

package apps.replay;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Callable;



import com.jme.scene.Geometry;
import com.jme.util.GameTaskQueue;
import com.jme.util.GameTaskQueueManager;

import core.SynergyNetDesktop;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;
import synergynetframework.appsystem.table.SynergyNetAppUtils;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.appsystem.table.appregistry.ApplicationControlError;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.appsystem.table.appregistry.ApplicationRegistry;
import synergynetframework.appsystem.table.appregistry.ApplicationTaskManager;
import synergynetframework.appsystem.table.appregistry.menucontrol.HoldTopRightConfirmVisualExit;
import synergynetframework.mtinput.dataplayback.DataPlaybackInput;

public class ReplayApp extends DefaultSynergyNetApp {	

	protected List<Geometry> menuItems;
	private ContentSystem content;
	private final DataPlaybackInput dpi = new DataPlaybackInput();

	public ReplayApp(ApplicationInfo info) {
		super(info);
	}

	@Override
	public void addContent() {
		SynergyNetAppUtils.addTableOverlay(this);

		content = ContentSystem.getContentSystemForSynergyNetApp(this);
		setMenuController(new HoldTopRightConfirmVisualExit(this));
		
		SimpleButton newNodeBtn = (SimpleButton)content.createContentItem(SimpleButton.class);
		newNodeBtn.setText("Start");
		newNodeBtn.setBackgroundColour(Color.black);
		newNodeBtn.setBorderColour(Color.white);
		newNodeBtn.setFont(new Font("Arial", Font.PLAIN, 14));
		newNodeBtn.setTextColour(Color.white);
		newNodeBtn.setBorderSize(8);
		newNodeBtn.setLocalLocation(60, 40);
		newNodeBtn.addButtonListener(new SimpleButtonAdapter(){


			public void buttonPressed(SimpleButton b, long id, float x,	float y, float pressure) {
				Thread t = new Thread(new Runnable(){
					public void run() {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						GameTaskQueueManager.getManager().getQueue(GameTaskQueue.UPDATE).enqueue(new Callable<Object>() {
							public Object call() {
								try {
									ApplicationTaskManager.getInstance().setActiveApplication(ApplicationRegistry.getInstance().getDefaultApp());
								} catch (ApplicationControlError e) {
									e.printStackTrace();
								}
								return null;
							}
						});
						
						File inputFile = new File("log/2009-07-06 16.22.46.185");
						InputStream is;
						try {
							is = new FileInputStream(inputFile);
							dpi.setInputStream(is);

							try {
								SynergyNetDesktop.getInstance().getMultiTouchInputComponent().setSource(dpi);
								dpi.start();
							} catch (SecurityException e) {
								e.printStackTrace();
							}

						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}

					}
				}, "later button");
				t.start();
			}

		});
	}


	@Override
	protected void stateUpdate(float tpf) {
		super.stateUpdate(tpf);
	}	

	@Override
	public void onActivate() {
		super.onActivate();
	}
}
