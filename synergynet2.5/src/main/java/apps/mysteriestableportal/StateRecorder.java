package apps.mysteriestableportal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.OrthoContentItem;
import synergynetframework.appsystem.contentsystem.items.listener.BringToTopListener;
import synergynetframework.appsystem.contentsystem.items.listener.OrthoControlPointRotateTranslateScaleListener;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;


/**
 * The Class StateRecorder.
 */
public class StateRecorder {

	/** The restore folder. */
	private File restoreFolder;
	
	/** The restore file name. */
	private String restoreFileName = "restore"+TableIdentity.getTableIdentity();
	
	/** The content system. */
	private ContentSystem contentSystem;
	
	/**
	 * Instantiates a new state recorder.
	 *
	 * @param app the app
	 */
	public StateRecorder(DefaultSynergyNetApp app){
		restoreFolder = new File(app.getApplicationDataDirectory(), "restore"+app.getName());
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(app);
	}
	
	/**
	 * Reset.
	 */
	public void reset(){
		if(!restoreFolder.exists()) restoreFolder.mkdir();
		File restoreFile = new File(restoreFolder, restoreFileName);
		PrintWriter pw;
		try {
			pw = new PrintWriter(new FileOutputStream(restoreFile));
			pw.println("");
			pw.close();
		} catch (FileNotFoundException e) {
			 
			e.printStackTrace();
		}
	}
	
	/**
	 * Load mystery state.
	 *
	 * @return the list
	 */
	public List<ContentItem> loadMysteryState(){
		List<ContentItem> items = new ArrayList<ContentItem>();
		if(!restoreFolder.exists()) return items;
		File restoreFile = new File(restoreFolder, restoreFileName);
		if(!restoreFile.exists()) return items;
		String mysteryPath = null;
		FileReader input;
	    try {
	    	input = new FileReader(restoreFile);
	    	BufferedReader bufRead = new BufferedReader(input);;
	    	String line = null;
 
	    	line = bufRead.readLine();
	    	while(line != null){
	    		System.out.println("line : "+line);
	    		if(line.startsWith("#")){
	    		
	    		}else if(line.startsWith("path")){
	    			String sub[] = line.split(":");
	    			mysteryPath = sub[1];
	    			if(mysteryPath.equals("data/mysteries/dinnerdisaster2/dinnerdisaster2.xml"))
	    				items.addAll(new DinnerDisasterBuilder().build(contentSystem));
	    			else
	    				items.addAll(contentSystem.loadContentItems(mysteryPath));
	    		}else{
		    		processItemLine(line, items);
	    		}
		    	line = bufRead.readLine(); 
	    	}
	    	bufRead.close();
	    	input.close();
	    } catch (FileNotFoundException e) {
	      e.printStackTrace();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	    registerMysteryContentItems(mysteryPath, items);
	    return items;
	}

	/**
	 * Register mystery content items.
	 *
	 * @param mysteryPath the mystery path
	 * @param items the items
	 */
	public void registerMysteryContentItems(final String mysteryPath, final List<ContentItem> items) {
		logMysteryState(mysteryPath, items);
		for(ContentItem item: items){
			OrthoContentItem orthoItem = (OrthoContentItem) item;
			orthoItem.addOrthoControlPointRotateTranslateScaleListener(new OrthoControlPointRotateTranslateScaleListener(){

				@Override
				public void itemRotated(ContentItem item, float newAngle,	float oldAngle) {
					logMysteryState(mysteryPath, items);
				}

				@Override
				public void itemScaled(ContentItem item, float newScaleFactor,	float oldScaleFactor) {
					logMysteryState(mysteryPath, items);
				}

				@Override
				public void itemTranslated(ContentItem item, float newLocationX, float newLocationY, float oldLocationX, float oldLocationY) {
					logMysteryState(mysteryPath, items);
				}
				
			});
			
			orthoItem.addBringToTopListener(new BringToTopListener(){

				@Override
				public void itemBringToToped(ContentItem item) {
					logMysteryState(mysteryPath, items);
				}
				
			});
		}
	}
	
	/**
	 * Process item line.
	 *
	 * @param line the line
	 * @param items the items
	 */
	private void processItemLine(String line, List<ContentItem> items) {
		if(line == null) return;
		String[] itemAttrs = line.split(",");
		if(itemAttrs.length <6) return;
		for(ContentItem item:items){
			if(item.getId().equalsIgnoreCase(itemAttrs[0])){
				float x = Float.parseFloat(itemAttrs[1]);
				float y = Float.parseFloat(itemAttrs[2]);
				float scale = Float.parseFloat(itemAttrs[3]);
				float angle = Float.parseFloat(itemAttrs[4]);
				int order = Integer.parseInt(itemAttrs[5]);
				item.setLocalLocation(x, y);
				item.setScale(scale);
				item.setAngle(angle);
				((OrthoContentItem)item).setOrder(order);
				((OrthoContentItem)item).setScaleLimit(ControllerApp.minScaleLimit, ControllerApp.maxScaleLimit);
			}
		}
	}

	/**
	 * Log mystery state.
	 *
	 * @param mysteryPath the mystery path
	 * @param items the items
	 */
	private void logMysteryState(String mysteryPath, List<ContentItem> items){
		if(mysteryPath == null) return;
		try{
			if(!restoreFolder.exists()) restoreFolder.mkdir();
			File restoreFile = new File(restoreFolder, restoreFileName);
			PrintWriter pw = new PrintWriter(new FileOutputStream(restoreFile));
			pw.println("# Last state for app ID:  mysteries");
			pw.println("# Storing started at " + new Date().toString());
			pw.println("# Format is as follows:");
			pw.println("# content Item name, location x, location y, location z, scale x, scale y, scale z, rotation x, rotation y, rotation z, rotation w, zOrder");
			pw.println("path:"+mysteryPath);
			for(ContentItem item: items) {
				pw.print(item.getId() + ",");
				pw.print(item.getLocalLocation().x + ",");
				pw.print(item.getLocalLocation().y + ",");
				pw.print(item.getScale() + ",");
				pw.print(item.getAngle() + ",");
				pw.println(((OrthoContentItem)item).getOrder());
			}
			pw.close();
		}catch(Exception exp){
			exp.printStackTrace();
		}
	}
	
}
