package apps.basketapp.controller.mysteries;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import apps.basketapp.BasketAppResources;
import apps.basketapp.controller.ContentSubMenu;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.LightImageLabel;

public class TableMystery {
		public static float  MIN_SCALE = 0.25f, MAX_SCALE = 1.5f, INITIAL_SCALE= 0.4f; 
	
		public List<ContentItem> getMystery(ContentSystem contentSystem, String appName){
			List<ContentItem> items = new ArrayList<ContentItem>();

			String folder = BasketAppResources.class.getResource("tables").getFile() + File.separatorChar;			
			
			if(appName.equalsIgnoreCase(ContentSubMenu.TABLE_RED)){
				folder += "red";
			}	
			else if(appName.equalsIgnoreCase(ContentSubMenu.TABLE_GREEN)){
				folder += "green";
			}
			else if(appName.equalsIgnoreCase(ContentSubMenu.TABLE_BLUE)){
				folder += "blue";
			}
			else if(appName.equalsIgnoreCase(ContentSubMenu.TABLE_YELLOW)){
				folder += "yellow";
			}
			
			File directory = new File(folder);
			if (directory.isDirectory()){
				File[] files = directory.listFiles();
				Collections.shuffle(Arrays.asList(files));
				for (File file: files){					
					try {
						LightImageLabel item = (LightImageLabel) contentSystem.createContentItem(LightImageLabel.class);
						item.drawImage(file.toURI().toURL());
						item.setScale(INITIAL_SCALE);
						item.setScaleLimit(MIN_SCALE, MAX_SCALE);
						item.centerItem();
						item.rotateRandom();
						item.setNote(ContentSubMenu.TABLE);
						items.add(item);					
					}catch (Exception e) {}
				}
			}
			
			Collections.shuffle(items);
			
			return items;
		}

}
