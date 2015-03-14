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

package apps.livingmaps;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;


import com.jme.bounding.OrthogonalBoundingBox;
import com.jme.image.Texture;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jmex.awt.swingui.JMEDesktop;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.MultiLineTextLabel;
import synergynetframework.appsystem.contentsystem.items.OrthoContentItem;
import synergynetframework.appsystem.contentsystem.items.listener.ItemListener;
import synergynetframework.appsystem.contentsystem.items.utils.Location;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.appsystem.table.appregistry.menucontrol.HoldTopRightConfirmVisualExit;
import synergynetframework.jme.cursorsystem.elements.twod.OrthoBringToTop;
import synergynetframework.jme.sysutils.CameraUtility;

public class LivingMapsApp extends DefaultSynergyNetApp {
	
	private ContentSystem contentSystem;
	private int defaultZoomLevel = 13;
	private int currentZoomLevel = defaultZoomLevel;
	
	private String mapInfoPath = "C:/test/file.txt";

	TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
	Texture t;
	
	Map<ContentItem, ItemMapInfo> mapItems = new HashMap<ContentItem, ItemMapInfo>();
	
    public LivingMapsApp(ApplicationInfo info) {
		super(info);		
	}

	@Override
	public void addContent() {
		
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(this);		
		setMenuController(new HoldTopRightConfirmVisualExit(this));

	
		MultiLineTextLabel tl1 = (MultiLineTextLabel) contentSystem.createContentItem(MultiLineTextLabel.class);
		tl1.setCRLFSeparatedString("You can do fishing here");
		tl1.setLocation(DisplaySystem.getDisplaySystem().getWidth()/2, DisplaySystem.getDisplaySystem().getHeight()/2);

		MultiLineTextLabel tl2 = (MultiLineTextLabel) contentSystem.createContentItem(MultiLineTextLabel.class);
		tl2.setCRLFSeparatedString("You can do shopping here");
		tl2.setLocation(200, 100);
		
		mapItems.put(tl1, new ItemMapInfo(new Location(tl1.getLocation().x, tl1.getLocation().y, 0), currentZoomLevel));
		mapItems.put(tl2, new ItemMapInfo(new Location(tl2.getLocation().x, tl2.getLocation().y, 0), currentZoomLevel));
		
		Node desktopNode = new Node( "desktop node" );
	    JMEDesktop jmeDesktop = new JMEDesktop( "test internalFrame" );
        jmeDesktop.setup( DisplaySystem.getDisplaySystem().getWidth(), DisplaySystem.getDisplaySystem().getHeight(), false, input );
        jmeDesktop.setLightCombineMode( Spatial.LightCombineMode.Off );
        desktopNode.attachChild( jmeDesktop );
        JPanel mapPanel = createContent();
        mapPanel.setSize((int)jmeDesktop.getWidth(), (int)jmeDesktop.getHeight());
        mapPanel.setVisible(true);
        jmeDesktop.getJDesktop().add( mapPanel );
        desktopNode.setModelBound(new OrthogonalBoundingBox());
        desktopNode.updateModelBound();
        desktopNode.setLocalTranslation(DisplaySystem.getDisplaySystem().getWidth()/2, DisplaySystem.getDisplaySystem().getHeight()/2,0);
        OrthoBringToTop o = new OrthoBringToTop(desktopNode);
        o.setBottomObject(o, false);
        orthoNode.attachChild( desktopNode );
        
        clearStackInfo();
        addMultiTouchListeners();
	}

	private void addMultiTouchListeners() {
		for(ContentItem item: mapItems.keySet()){
			((OrthoContentItem)item).addItemListener(new ItemListener(){

				@Override
				public void cursorChanged(ContentItem item, long id, float x,
						float y, float pressure) {
					mapItems.put(item, new ItemMapInfo(new Location(item.getLocalLocation().x, item.getLocalLocation().y, 0), currentZoomLevel));
			        clearStackInfo();
				}

				@Override
				public void cursorClicked(ContentItem item, long id, float x,
						float y, float pressure) {
				}

				@Override
				public void cursorDoubleClicked(ContentItem item, long id,
						float x, float y, float pressure) {
				}

				@Override
				public void cursorLongHeld(ContentItem item, long id, float x,
						float y, float pressure) {
					 
					
				}

				@Override
				public void cursorPressed(ContentItem item, long id, float x,
						float y, float pressure) {
					 
					
				}

				@Override
				public void cursorReleased(ContentItem item, long id, float x,
						float y, float pressure) {
					 
					
				}

				@Override
				public void cursorRightClicked(ContentItem item, long id,
						float x, float y, float pressure) {
					 
					
				}});
		}
	}

	private void clearStackInfo() {
	    try {
	        BufferedWriter out = new BufferedWriter(new FileWriter(mapInfoPath));
	        out.write("");
	        out.close();
	    } catch (IOException e) {
	    }

	}

	@Override
	protected void onDeactivate() {
		//video.stop();
		//contentSystem.remove(video);
		//super.onDeactivate();		
	}

	protected Camera getCamera() {
		if(cam == null) {
			cam = CameraUtility.getCamera();
			cam.setLocation(new Vector3f(0f, 10f, 50f));
			cam.lookAt(new Vector3f(), new Vector3f( 0, 0, -1 ));
			cam.update();
		}		
		return cam;
	}

	@Override
	protected void stateUpdate(float tpf) {
		super.stateUpdate(tpf);
		if(contentSystem != null) contentSystem.update(tpf);
		processMap();
		
	}
	
	private void processMap(){
	    try {
	        BufferedReader in = new BufferedReader(new FileReader(mapInfoPath));
	        String str;
	        if ((str = in.readLine()) != null) {
	        	String[] params = str.split("&");   
           	    Map<String, String> map = new HashMap<String, String>();   
           	    for (String param : params)   
            	{  
            	   String name = param.split("=")[0];   
            	   String value = param.split("=")[1];
            	   map.put(name, value);   
            	}
            	int x = Integer.parseInt(map.get("x"));
            	int y = Integer.parseInt(map.get("y"));
            	currentZoomLevel = Integer.parseInt(map.get("zoom"));
            	float factor = 1;
            	if(currentZoomLevel == defaultZoomLevel){
            		factor = 0;
            	}else if(currentZoomLevel <=defaultZoomLevel){
            		factor = 1-(1.0f/FastMath.pow(2, defaultZoomLevel - currentZoomLevel));
            	}
            	else{
            		factor = FastMath.pow(2, currentZoomLevel - defaultZoomLevel);
            	}
            	float shiftX = x - (DisplaySystem.getDisplaySystem().getWidth()/2);
            	float shiftY = y - (DisplaySystem.getDisplaySystem().getHeight()/2);

            	for(ContentItem item: mapItems.keySet()){
            		float newX = mapItems.get(item).getLocation().x + shiftX;
            		float newY = mapItems.get(item).getLocation().y + shiftY;
            		if(factor<=1){
	            		float locX = ((x - newX)* factor)+newX;
	            		float locY = ((y - newY)* factor)+newY;
	            		item.setLocalLocation(new Location(locX, locY, 0));
            		}else{
	            		float locX = newX -(FastMath.abs(x - newX)* (factor-1));
	            		float locY = newY - (FastMath.abs(y - newY)* (factor-1));
	            		item.setLocalLocation(new Location(locX, locY, 0));
            		}
            		item.setScale(((float)currentZoomLevel)/defaultZoomLevel);
         	   	}
            	
	        }
	        in.close();
	        clearStackInfo();
	    } catch (IOException e) {
	    }

	}

	public JPanel createContent() {
		return null;
    }
	
	

}
