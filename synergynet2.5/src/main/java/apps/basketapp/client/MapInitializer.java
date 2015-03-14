package apps.basketapp.client;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apps.basketapp.BasketAppResources;

import com.jme.system.DisplaySystem;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.LightImageLabel;
import synergynetframework.appsystem.contentsystem.items.MultiLineTextLabel;
import synergynetframework.appsystem.contentsystem.items.RoundFrame;
import synergynetframework.appsystem.contentsystem.items.listener.ItemListener;
import synergynetframework.appsystem.contentsystem.items.listener.ScreenCursorListener;
import synergynetframework.appsystem.contentsystem.items.utils.Location;

public class MapInitializer {
		
		private int mapWidth = 820, mapHeight = 520;
		private LightImageLabel map;
		private Map<Location, String> textMap = new HashMap<Location, String>();
		private Map<Location, String> lightMap = new HashMap<Location, String>();
		private Map<Location, String> cameraMap = new HashMap<Location, String>();
		private Map<Location, String> dogMap = new HashMap<Location, String>();

		public MapInitializer(final LightImageLabel map){
			this.map = map;
			map.setWidth(mapWidth);
			map.setHeight(mapHeight);
			map.setLocation(DisplaySystem.getDisplaySystem().getWidth()/2, 290);
			map.setOrder(-99999);
			map.setBringToTopable(false);
			map.setRotateTranslateScalable(false);
			map.addScreenCursorListener(new ScreenCursorListener(){

				@Override
				public void screenCursorPressed(ContentItem item, long id,
						float x, float y, float pressure) {
					 
					System.out.println("x = "+x + " , y = " +y);
				}

				@Override
				public void screenCursorChanged(ContentItem item, long id,
						float x, float y, float pressure) {
					 
					
				}

				@Override
				public void screenCursorReleased(ContentItem item, long id,
						float x, float y, float pressure) {
					 
					
				}

				@Override
				public void screenCursorClicked(ContentItem item, long id,
						float x, float y, float pressure) {
					 
					
				}
				
			});
			
			textMap.put(new Location(619, 495,0), "The river bank is steep and the bungalow \n is inaccessible from the rear of the building.");
			textMap.put(new Location(378 , 371,0), "The bathroom has two sinks \n, “His” and “Hers”.");
			textMap.put(new Location(445 , 354,0), "Inside the child’s bedroom is \n a train set, which whistles when \n it moves.");
			textMap.put(new Location(580 , 347,0), "There is a bowl of healthy fruit \n on the sideboard in the dining room.");
			textMap.put(new Location(553 , 253,0), "The children have been cooking \n and there is flour on the kitchen \n floor.");
			textMap.put(new Location(579 , 153,0), "The kitchen has a narrow, \n high window above the sink.");
			textMap.put(new Location(774 , 170,0), "There is a playful puppy in this \n part of the garden, he is friendly \n  but very noisy!  You will need to kill \n these dogs to pass through this part \n of the garden.");
			textMap.put(new Location(413 , 217,0), "The rug is a family heirloom \n, it is over 100 years old.");
			textMap.put(new Location(255 , 295,0), "A wardrobe in the hallway is used \n to store the family’s winter coats and \n shoes during the summer months.");
			textMap.put(new Location(192 , 314,0), "There are two vicious guard dogs \n in this part of the garden, you \n will need to kill these dogs \n to pass through this area.");
			textMap.put(new Location(680 , 366, 0), "The drug is kept in a safe\n in the study.");
			textMap.put(new Location(114 , 257, 0), "There is a 3’ (1m) wooden fence \n around the border of the property.");
			textMap.put(new Location(239 , 62, 0), "There is a 3’ (1m) wooden fence \n around the border of the property.");
			textMap.put(new Location(698 , 50, 0), "There is a 3’ (1m) wooden fence \n around the border of the property.");
			textMap.put(new Location(880 , 248, 0), "There is a 3’ (1m) wooden fence \n around the border of the property.");
			textMap.put(new Location(886 , 391, 0), "There is a 3’ (1m) wooden fence \n around the border of the property.");
			textMap.put(new Location(745 , 358, 0), "Underneath the study window is \n the children’s sandpit.");
			textMap.put(new Location(220 , 385, 0), "The master bedroom has a large \n window that looks out onto the garden.");
			textMap.put(new Location(454 , 57, 0), "The hedge is dense, and 5’ \n high, as the guard dogs \n and puppy do not get on and\n must be kept separately.");
			textMap.put(new Location(177 , 170, 0), "Where there are bushes and foliage\n in the garden, the cameras’ view\n is obscured.");
			textMap.put(new Location(821 , 353, 0), "The garden contains children’s \ntoys, including a slide and some\n swings.");
			textMap.put(new Location(321 , 198, 0), "The living room has patio doors\n, which are made entirely of glass\n.  At night, they are covered by\n curtains.");
			textMap.put(new Location(114 , 380, 0), "There is a 3’ (1m) wooden fence \n around the border of the property.");
			textMap.put(new Location(817, 116, 0), "Where there are bushes and foliage\n in the garden, the cameras’ view\n is obscured.");

			cameraMap.put(new Location(705 , 102,0), "These are security cameras, which \n are activated when they detect movement \n nearby.  If you are caught on the camera \n , you will almost certainly be charged \n with burglary, should you choose to \n steal the drug from the house.");
			cameraMap.put(new Location(430 , 94,0), "These are security cameras, which \n are activated when they detect movement \n nearby.  If you are caught on the camera \n , you will almost certainly be charged \n with burglary, should you choose to \n steal the drug from the house.");
			cameraMap.put(new Location(332 , 100, 0), "These are security cameras, which \n are activated when they detect movement \n nearby.  If you are caught on the camera \n , you will almost certainly be charged \n with burglary, should you choose to \n steal the drug from the house.");
			cameraMap.put(new Location(217 , 274, 0), "These are security cameras, which \n are activated when they detect movement \n nearby.  If you are caught on the camera \n , you will almost certainly be charged \n with burglary, should you choose to \n steal the drug from the house.");
			cameraMap.put(new Location(230 , 420, 0), "These are security cameras, which \n are activated when they detect movement \n nearby.  If you are caught on the camera \n , you will almost certainly be charged \n with burglary, should you choose to \n steal the drug from the house.");
			cameraMap.put(new Location(721 , 414, 0), "These are security cameras, which \n are activated when they detect movement \n nearby.  If you are caught on the camera \n , you will almost certainly be charged \n with burglary, should you choose to \n steal the drug from the house.");


			
			lightMap.put(new Location(365 , 92,0), "These are security lights, which \n illuminate the area 2 metres \n around them when they detect \n movement nearby.");
			lightMap.put(new Location(321 , 254, 0), "These are security lights, which \n illuminate the area 2 metres \n around them when they detect \n movement nearby.");
			lightMap.put(new Location(231 , 445, 0), "These are security lights, which \n illuminate the area 2 metres \n around them when they detect \n movement nearby.");
			lightMap.put(new Location(721 , 385, 0), "These are security lights, which \n illuminate the area 2 metres \n around them when they detect \n movement nearby.");

			dogMap.put(new Location(178 , 239, 0), "wild");
			dogMap.put(new Location(262 , 137,0), "wild");
			dogMap.put(new Location(798 , 248, 0), "cute");

		}
		
		public List<ContentItem> getMystery(ContentSystem contentSystem){
			
			List<ContentItem> items = new ArrayList<ContentItem>();
			
			for(final Location loc: textMap.keySet()){
				
				final MultiLineTextLabel label = (MultiLineTextLabel) contentSystem.createContentItem(MultiLineTextLabel.class);
				label.setBackgroundColour(Color.white);
				label.setBorderSize(1);
				label.setBorderColour(Color.black);
				label.setCRLFSeparatedString(textMap.get(loc));
				label.setLocalLocation(loc);
				label.setVisible(false);
				items.add(label);
				label.setNote(loc.x+","+loc.y);
				final RoundFrame frame = (RoundFrame) map.getContentSystem().createContentItem(RoundFrame.class);
				frame.setBackgroundColour(Color.red);
				frame.setOrder(map.getOrder()+1);
				frame.setRadius(15);
				frame.setLocation(loc);
				frame.setRotateTranslateScalable(false);
				frame.setBringToTopable(false);
				frame.addItemListener(new ItemListener(){

					@Override
					public void cursorPressed(ContentItem item, long id,
							float x, float y, float pressure) {
						 
						
					}

					@Override
					public void cursorChanged(ContentItem item, long id,
							float x, float y, float pressure) {
						 
						
					}

					@Override
					public void cursorReleased(ContentItem item, long id,
							float x, float y, float pressure) {
						label.setVisible(true);
						label.setOrder(frame.getOrder()+1);
					}

					@Override
					public void cursorClicked(ContentItem item, long id,
							float x, float y, float pressure) {
						 
						
					}

					@Override
					public void cursorRightClicked(ContentItem item, long id,
							float x, float y, float pressure) {
						 
						
					}

					@Override
					public void cursorLongHeld(ContentItem item, long id,
							float x, float y, float pressure) {
						 
						
					}

					@Override
					public void cursorDoubleClicked(ContentItem item, long id,
							float x, float y, float pressure) {
						 
						
					}
					
				});
				frame.addScreenCursorListener(new ScreenCursorListener(){

					@Override
					public void screenCursorPressed(ContentItem item, long id,
							float x, float y, float pressure) {
						 
						
					}

					@Override
					public void screenCursorChanged(ContentItem item, long id,
							float x, float y, float pressure) {
						 
						
					}

					@Override
					public void screenCursorReleased(ContentItem item, long id,
							float x, float y, float pressure) {
						if(label.getParent() == null)		
							label.setLocalLocation(x, y);
					}

					@Override
					public void screenCursorClicked(ContentItem item, long id,
							float x, float y, float pressure) {
						 
						
					}
					
				});
				items.add(frame);
			}
			
			
			for(final Location loc: cameraMap.keySet()){
				
				final MultiLineTextLabel label = (MultiLineTextLabel) contentSystem.createContentItem(MultiLineTextLabel.class);
				label.setBackgroundColour(Color.white);
				label.setBorderSize(1);
				label.setBorderColour(Color.black);
				label.setCRLFSeparatedString(cameraMap.get(loc));
				label.setLocalLocation(loc);
				label.setVisible(false);
				items.add(label);
				
				final LightImageLabel frame = (LightImageLabel) map.getContentSystem().createContentItem(LightImageLabel.class);
				frame.setAutoFit(false);
				frame.drawImage(BasketAppResources.class.getResource("camera.jpeg"));
				frame.setWidth(25);
				frame.setHeight(25);
				frame.setOrder(map.getOrder()+1);
				frame.setLocation(loc);
				frame.setRotateTranslateScalable(false);
				frame.setBringToTopable(false);
				frame.addItemListener(new ItemListener(){

					@Override
					public void cursorPressed(ContentItem item, long id,
							float x, float y, float pressure) {
						 
						
					}

					@Override
					public void cursorChanged(ContentItem item, long id,
							float x, float y, float pressure) {
						 
						
					}

					@Override
					public void cursorReleased(ContentItem item, long id,
							float x, float y, float pressure) {
						label.setVisible(true);
						label.setOrder(frame.getOrder()+1);

					}

					@Override
					public void cursorClicked(ContentItem item, long id,
							float x, float y, float pressure) {
						 
						
					}

					@Override
					public void cursorRightClicked(ContentItem item, long id,
							float x, float y, float pressure) {
						 
						
					}

					@Override
					public void cursorLongHeld(ContentItem item, long id,
							float x, float y, float pressure) {
						 
						
					}

					@Override
					public void cursorDoubleClicked(ContentItem item, long id,
							float x, float y, float pressure) {
						 
						
					}
					
				});
				
				frame.addScreenCursorListener(new ScreenCursorListener(){

					@Override
					public void screenCursorPressed(ContentItem item, long id,
							float x, float y, float pressure) {
						 
						
					}

					@Override
					public void screenCursorChanged(ContentItem item, long id,
							float x, float y, float pressure) {
						 
						
					}

					@Override
					public void screenCursorReleased(ContentItem item, long id,
							float x, float y, float pressure) {
						label.setLocalLocation(x, y);
					}

					@Override
					public void screenCursorClicked(ContentItem item, long id,
							float x, float y, float pressure) {
						 
						
					}
					
				});
				items.add(frame);
			}
			
			for(final Location loc: lightMap.keySet()){
				
				final MultiLineTextLabel label = (MultiLineTextLabel) contentSystem.createContentItem(MultiLineTextLabel.class);
				label.setBackgroundColour(Color.white);
				label.setBorderSize(1);
				label.setBorderColour(Color.black);
				label.setCRLFSeparatedString(lightMap.get(loc));
				label.setLocalLocation(loc);
				label.setVisible(false);
				items.add(label);
				
				final LightImageLabel frame = (LightImageLabel) map.getContentSystem().createContentItem(LightImageLabel.class);
				frame.setAutoFit(false);
				frame.drawImage(BasketAppResources.class.getResource("light.jpg"));
				frame.setWidth(25);
				frame.setHeight(25);
				frame.setOrder(map.getOrder()+1);
				frame.setLocation(loc);
				frame.setRotateTranslateScalable(false);
				frame.setBringToTopable(false);
				frame.addItemListener(new ItemListener(){

					@Override
					public void cursorPressed(ContentItem item, long id,
							float x, float y, float pressure) {
						 
						
					}

					@Override
					public void cursorChanged(ContentItem item, long id,
							float x, float y, float pressure) {
						 
						
					}

					@Override
					public void cursorReleased(ContentItem item, long id,
							float x, float y, float pressure) {
						label.setVisible(true);
						label.setOrder(frame.getOrder()+1);
					}

					@Override
					public void cursorClicked(ContentItem item, long id,
							float x, float y, float pressure) {
						 
						
					}

					@Override
					public void cursorRightClicked(ContentItem item, long id,
							float x, float y, float pressure) {
						 
						
					}

					@Override
					public void cursorLongHeld(ContentItem item, long id,
							float x, float y, float pressure) {
						 
						
					}

					@Override
					public void cursorDoubleClicked(ContentItem item, long id,
							float x, float y, float pressure) {
						 
						
					}
					
				});
				
				frame.addScreenCursorListener(new ScreenCursorListener(){

					@Override
					public void screenCursorPressed(ContentItem item, long id,
							float x, float y, float pressure) {
						 
						
					}

					@Override
					public void screenCursorChanged(ContentItem item, long id,
							float x, float y, float pressure) {
						 
						
					}

					@Override
					public void screenCursorReleased(ContentItem item, long id,
							float x, float y, float pressure) {
						label.setLocalLocation(x, y);
					}

					@Override
					public void screenCursorClicked(ContentItem item, long id,
							float x, float y, float pressure) {
						 
						
					}
					
				});
				items.add(frame);
			}
			
			for(Location loc: dogMap.keySet()){
				final LightImageLabel frame = (LightImageLabel) map.getContentSystem().createContentItem(LightImageLabel.class);
				frame.setAutoFit(false);
				if(dogMap.get(loc).equalsIgnoreCase("wild"))	
					frame.drawImage(BasketAppResources.class.getResource("mean_dog.png"));
				else
					frame.drawImage(BasketAppResources.class.getResource("cute_dog.jpg"));
				frame.setWidth(30);
				frame.setHeight(30);
				frame.setOrder(map.getOrder()+1);
				frame.setLocation(loc);
				frame.setRotateTranslateScalable(false);
				frame.setBringToTopable(false);
				items.add(frame);
			}
			
			return items;
		}
}
