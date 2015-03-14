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

package apps.mysteriestableportal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.OrthoContentItem;
import synergynetframework.appsystem.contentsystem.items.listener.OrthoControlPointRotateTranslateScaleListener;
import synergynetframework.jme.config.AppConfig;
import synergynetframework.jme.mtinputbridge.MultiTouchInputFilterManager;

/**
 * Logs all actions to a log file.
 * 
 * @author dcs0ah1
 *
 */
public class MysteriesLogging {

	public static final String ITEM_ROTATED = "ITEM_ROTATED";
	public static final String ITEM_TRANSLATED = "ITEM_TRANSLATED";
	public static final String ITEM_SCALED = "ITEM_SCALED";
	
	private File recordFile;
	private PrintWriter pw;
	

	public MysteriesLogging(Class<?> appClass) throws FileNotFoundException {
		if(!AppConfig.recordTableDir.exists()) AppConfig.recordTableDir.mkdir();
		recordFile = new File(AppConfig.recordTableDir, getFileNameFromDate());
		pw = new PrintWriter(new FileOutputStream(recordFile));
		writeFileHeader(appClass);
	}
	
	public MysteriesLogging() throws FileNotFoundException {
		this(MultiTouchInputFilterManager.getInstance().getLoggingClass());
	}
	
	private String getFileNameFromDate() {		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss.S");
		return formatter.format(new Date());	
	}
	

	
	private void writeFileHeader(Class<?> appClass) {
		pw.println("# Recorded from LoggingFilter v0.1");
		if(appClass != null)
			pw.println("# App: " + appClass.getName());
		else
			pw.println("# App: " + MultiTouchInputFilterManager.getInstance().getLoggingClass().getName());
		pw.println("# Recording started at " + new Date().toString());
		pw.println("# Format is as follows:");
		pw.println("# event type, time, item, x, y, angle, scale");
	}

	public void registerItemsForLogging(List<ContentItem> items){
		for(ContentItem item: items){
			this.registerItemForLogging(item);
		}
	}
	
	public void registerItemForLogging(ContentItem item){
		((OrthoContentItem)item).addOrthoControlPointRotateTranslateScaleListener(new OrthoControlPointRotateTranslateScaleListener(){

			
			private String getTime(){
				SimpleDateFormat formatter = new SimpleDateFormat("HH.mm.ss.S");
				return formatter.format(new Date());		
			}

			@Override
			public void itemRotated(ContentItem item, float newAngle, float oldAngle) {
				pw.print(ITEM_ROTATED + ",");
				pw.print(this.getTime() + ",");
				pw.print(item.getId() + ",");
				pw.print(item.getLocalLocation().x + ",");
				pw.print(item.getLocalLocation().y + ",");
				pw.print(item.getAngle() + ",");
				pw.println(item.getScale() + ",");
				pw.flush();	
			}

			@Override
			public void itemScaled(ContentItem item, float newScaleFactor, float oldScaleFactor) {
				pw.print(ITEM_SCALED + ",");
				pw.print(this.getTime() + ",");
				pw.print(item.getId() + ",");
				pw.print(item.getLocalLocation().x + ",");
				pw.print(item.getLocalLocation().y + ",");
				pw.print(item.getAngle() + ",");
				pw.println(item.getScale() + ",");
				pw.flush();					
			}

			@Override
			public void itemTranslated(ContentItem item, float newLocationX, float newLocationY, float oldLocationX, float oldLocationY) {
				pw.print(ITEM_TRANSLATED + ",");
				pw.print(this.getTime() + ",");
				pw.print(item.getId() + ",");
				pw.print(item.getLocalLocation().x + ",");
				pw.print(item.getLocalLocation().y + ",");
				pw.print(item.getAngle() + ",");
				pw.println(item.getScale() + ",");
				pw.flush();						
			}
			
		});
	}

	
}

