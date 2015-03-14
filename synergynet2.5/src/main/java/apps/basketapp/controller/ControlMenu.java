package apps.basketapp.controller;


import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import core.SynergyNetDesktop;


import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ListContainer;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;

public class ControlMenu {
	protected ContentSystem contentSystem;
	protected ContentSubMenu contentSubMenu;
	protected ListContainer controlMenu;
	protected List<BasketControlMenuListener> listeners = new ArrayList<BasketControlMenuListener>();
	
	private boolean areTablesLocked = false;
	
	public ControlMenu(ContentSystem contentSystem, ContentSubMenu contentSubMenu){
		this.contentSystem = contentSystem;
		this.contentSubMenu = contentSubMenu;
		LoadControlMenu();
	}

	private ListContainer LoadControlMenu(){
		
		controlMenu = (ListContainer)contentSystem.createContentItem(ListContainer.class);
		controlMenu.setBackgroundColour(Color.BLUE);
		controlMenu.setWidth(200);
		controlMenu.setItemHeight(30);
		controlMenu.setVisible(true);
		
		
		SimpleButton sendDataButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		sendDataButton.setAutoFitSize(false);
		sendDataButton.setText("Send Data");
		sendDataButton.setBackgroundColour(Color.lightGray);
		sendDataButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {
				for(BasketControlMenuListener l: listeners) l.sendDesktopData();
			}			
		});

		final SimpleButton clearLocalTableButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		clearLocalTableButton.setAutoFitSize(false);
		clearLocalTableButton.setText("Clear Local Table");
		clearLocalTableButton.setBackgroundColour(Color.lightGray);
		clearLocalTableButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {			
				for(BasketControlMenuListener l: listeners) l.clearLocalTable();
			}			
		});	
		
		final SimpleButton swapBasketsButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		swapBasketsButton.setAutoFitSize(false);
		swapBasketsButton.setText("Swap Baskets");
		swapBasketsButton.setBackgroundColour(Color.lightGray);
		swapBasketsButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {			
				for(BasketControlMenuListener l: listeners) l.swapBaskets();
			}			
		});	

		
		final SimpleButton clearTableButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		clearTableButton.setAutoFitSize(false);
		clearTableButton.setText("Clear Student Tables");
		clearTableButton.setBackgroundColour(Color.lightGray);
		clearTableButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {			
				for(BasketControlMenuListener l: listeners) l.clearStudentTables();
			}			
		});
		
		
		final SimpleButton lockTablesButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		lockTablesButton.setAutoFitSize(false);
		lockTablesButton.setText("Lock Student Tables");
		lockTablesButton.setBackgroundColour(Color.lightGray);
		lockTablesButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {
				areTablesLocked = !areTablesLocked;
				for(BasketControlMenuListener l: listeners) l.lockStudentTables(areTablesLocked);
				if(areTablesLocked)
					lockTablesButton.setText("Unlock Student Tables");
				else
					lockTablesButton.setText("Lock Student Tables");
			}			
		});
			
		
		final SimpleButton captureTablesButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		captureTablesButton.setAutoFitSize(false);
		captureTablesButton.setText("Capture Student Tables");
		captureTablesButton.setBackgroundColour(Color.lightGray);
		captureTablesButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {
				for(BasketControlMenuListener l: listeners) l.captureStudentTables();
			}			
		});
		
		SimpleButton backToMainMenuButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);		
		backToMainMenuButton.setAutoFitSize(false);
		backToMainMenuButton.setText("Back To Main Menu");
		backToMainMenuButton.setBackgroundColour(Color.lightGray);
		backToMainMenuButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {
				try {
					SynergyNetDesktop.getInstance().showMainMenu();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}			
		});	
		backToMainMenuButton.setVisible(false);	
		if (contentSubMenu!=null){
			controlMenu.addSubMenu(contentSubMenu.getSubAppMenu(), "Choose Content");
		}
		controlMenu.setLocalLocation(200, 200);
		controlMenu.addSubItem(sendDataButton);
		controlMenu.addSubItem(swapBasketsButton);
		controlMenu.addSubItem(clearTableButton);
		controlMenu.addSubItem(clearLocalTableButton);
		controlMenu.addSubItem(lockTablesButton);
		controlMenu.addSubItem(captureTablesButton);
		controlMenu.addSubItem(backToMainMenuButton);
		
		
		return controlMenu;
	}
	
	public void setLocation(float x, float y){
		controlMenu.setLocalLocation(x, y);
	}
	
	public void addControlMenuListener(BasketControlMenuListener l){
		listeners.add(l);
	}
	
	public void setVisible(boolean isVisible){
		controlMenu.setVisible(isVisible); 
	}
	
	
	public interface BasketControlMenuListener{
		public void sendDesktopData();
		public void captureStudentTables();
		public void swapBaskets();
		public void clearLocalTable();
		public void clearStudentTables();
		public void lockStudentTables(boolean lock);
	}
}


