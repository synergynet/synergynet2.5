package apps.basketapp.controller;


import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import core.SynergyNetDesktop;


import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ListContainer;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;


/**
 * The Class ControlMenu.
 */
public class ControlMenu {
	
	/** The content system. */
	protected ContentSystem contentSystem;
	
	/** The content sub menu. */
	protected ContentSubMenu contentSubMenu;
	
	/** The control menu. */
	protected ListContainer controlMenu;
	
	/** The listeners. */
	protected List<BasketControlMenuListener> listeners = new ArrayList<BasketControlMenuListener>();
	
	/** The are tables locked. */
	private boolean areTablesLocked = false;
	
	/**
	 * Instantiates a new control menu.
	 *
	 * @param contentSystem the content system
	 * @param contentSubMenu the content sub menu
	 */
	public ControlMenu(ContentSystem contentSystem, ContentSubMenu contentSubMenu){
		this.contentSystem = contentSystem;
		this.contentSubMenu = contentSubMenu;
		LoadControlMenu();
	}

	/**
	 * Load control menu.
	 *
	 * @return the list container
	 */
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
	
	/**
	 * Sets the location.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public void setLocation(float x, float y){
		controlMenu.setLocalLocation(x, y);
	}
	
	/**
	 * Adds the control menu listener.
	 *
	 * @param l the l
	 */
	public void addControlMenuListener(BasketControlMenuListener l){
		listeners.add(l);
	}
	
	/**
	 * Sets the visible.
	 *
	 * @param isVisible the new visible
	 */
	public void setVisible(boolean isVisible){
		controlMenu.setVisible(isVisible); 
	}
	
	
	/**
	 * The listener interface for receiving basketControlMenu events.
	 * The class that is interested in processing a basketControlMenu
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addBasketControlMenuListener<code> method. When
	 * the basketControlMenu event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see BasketControlMenuEvent
	 */
	public interface BasketControlMenuListener{
		
		/**
		 * Send desktop data.
		 */
		public void sendDesktopData();
		
		/**
		 * Capture student tables.
		 */
		public void captureStudentTables();
		
		/**
		 * Swap baskets.
		 */
		public void swapBaskets();
		
		/**
		 * Clear local table.
		 */
		public void clearLocalTable();
		
		/**
		 * Clear student tables.
		 */
		public void clearStudentTables();
		
		/**
		 * Lock student tables.
		 *
		 * @param lock the lock
		 */
		public void lockStudentTables(boolean lock);
	}
}


