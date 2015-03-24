package apps.projectmanagement.controller;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import apps.projectmanagement.component.ganttchart.GanttChart;
import apps.projectmanagement.component.workflowchart.core.WorkflowTool;
import apps.projectmanagement.io.StaffNodesLoader;
import apps.projectmanagement.registry.StaffNodeRegistry;

import com.jme.scene.Node;

import core.SynergyNetDesktop;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ListContainer;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;
import synergynetframework.appsystem.table.appdefinitions.SynergyNetApp;
import synergynetframework.appsystem.table.appregistry.menucontrol.MenuController;
import synergynetframework.mtinput.IMultiTouchEventListener;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;
import synergynetframework.mtinput.events.MultiTouchObjectEvent;


/**
 * The Class ProjectManagementMenu.
 */
public class ProjectManagementMenu extends MenuController implements IMultiTouchEventListener{
	
	/** The corner distance. */
	protected float cornerDistance = 40f;
	
	/** The cursor timing. */
	protected Map<Long,Long> cursorTiming = new HashMap<Long,Long>();
	
	/** The interval. */
	protected long interval = 1000;
	
	/** The enabled. */
	private boolean enabled;
	
	/** The menu container. */
	private ListContainer menuContainer;
	
	/** The content system. */
	private ContentSystem contentSystem;
	
	/** The workflow control buttons. */
	protected WorkflowTool workflowControlButtons;	
	
	/** The gantt chart. */
	protected GanttChart ganttChart;
	
	/** The visible interval. */
	private long startVisibleTime = -1,visibleInterval = 10000;
	
	/**
	 * Instantiates a new project management menu.
	 *
	 * @param contentSystem the content system
	 * @param orthoNode the ortho node
	 */
	public ProjectManagementMenu(ContentSystem contentSystem, Node orthoNode) {
		this.contentSystem = contentSystem;
		ganttChart = new GanttChart(contentSystem, 700, 520);
		workflowControlButtons = new WorkflowTool(contentSystem);	
		orthoNode.attachChild(ganttChart);
		
		String fileName = "apps/projectmanagement/predefinedstaffnodes.txt";
		StaffNodesLoader.loadNode(fileName, contentSystem);
		
		createMenu();
		

	}
	
	
	/**
	 * Creates the menu.
	 */
	public void createMenu() {
		menuContainer = (ListContainer)contentSystem.createContentItem(ListContainer.class);
		menuContainer.setVisible(false);
		menuContainer.setLocalLocation(800, 550);
		
		final SimpleButton workflow = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);
		workflow.setAutoFitSize(false);
		workflow.setText("Project Workflow");
		workflow.setHeight(15);
		workflow.setWidth(100);
		workflow.setBackgroundColour(Color.lightGray);
		workflow.addButtonListener(new SimpleButtonAdapter(){
			public void buttonClicked(SimpleButton b, long id, float x, float y, float pressure) {
				
				workflowControlButtons.setvisibility(true);
				workflowControlButtons.setEditable(true);
		    	StaffNodeRegistry.getInstance().hideNodes();
		    	ganttChart.setVisibility(false);
		    	
		    	menuContainer.setVisible(false);
		    	
			}
		});
		
		final SimpleButton laborAllocation = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);
		laborAllocation.setAutoFitSize(false);
		laborAllocation.setText("Labor Allocation");
		laborAllocation.setHeight(15);
		laborAllocation.setWidth(100);
		laborAllocation.setBackgroundColour(Color.lightGray);
		laborAllocation.addButtonListener(new SimpleButtonAdapter(){
			public void buttonClicked(SimpleButton b, long id, float x, float y, float pressure) {
				workflowControlButtons.setvisibility(true);
				workflowControlButtons.setEditable(false);
		    	StaffNodeRegistry.getInstance().showNodes();
		    	ganttChart.setVisibility(false);
		    	
		    	menuContainer.setVisible(false);
			}
		});
		
		final SimpleButton ganttChartButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);
		ganttChartButton.setAutoFitSize(false);
		ganttChartButton.setText("Gantt Chart");
		ganttChartButton.setHeight(15);
		ganttChartButton.setWidth(100);
		ganttChartButton.setBackgroundColour(Color.lightGray);
		ganttChartButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonClicked(SimpleButton b, long id, float x, float y, float pressure) {
				workflowControlButtons.setvisibility(false);
				workflowControlButtons.setEditable(false);
		    	StaffNodeRegistry.getInstance().hideNodes();
		    	ganttChart.setVisibility(true);
		    	
		    	menuContainer.setVisible(false);
			}
		});
		
		final SimpleButton exit = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);
		exit.setAutoFitSize(false);
		exit.setText("Exit");
		exit.setHeight(15);
		exit.setWidth(100);
		exit.setBackgroundColour(Color.lightGray);
		exit.addButtonListener(new SimpleButtonAdapter(){
			public void buttonClicked(SimpleButton b, long id, float x, float y, float pressure) {
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

		
		menuContainer.setAutoFitSize(true);
		menuContainer.setWidth(180);
		menuContainer.setItemWidth(150);
		menuContainer.addSubItem(workflow);
		menuContainer.addSubItem(laborAllocation);
		menuContainer.addSubItem(ganttChartButton);
		menuContainer.addSubItem(exit);
		menuContainer.setBackgroundColour(Color.ORANGE);
		
	}


	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appregistry.menucontrol.MenuController#enableForApplication(synergynetframework.appsystem.table.appdefinitions.SynergyNetApp)
	 */
	@Override
	public void enableForApplication(SynergyNetApp app) {
		SynergyNetDesktop.getInstance().getMultiTouchInputComponent().registerMultiTouchEventListener(this);
		setEnabled(true);	
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appregistry.menucontrol.MenuController#applicationFinishing()
	 */
	@Override
	public void applicationFinishing() {
		if(menuContainer != null) menuContainer.setVisible(false);
		setEnabled(false);
	}
	
	
	
	/**
	 * Checks if is enabled.
	 *
	 * @return true, if is enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Sets the enabled.
	 *
	 * @param enabled the new enabled
	 */
	private void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#cursorChanged(synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	public void cursorChanged(MultiTouchCursorEvent event) {
	}

	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#cursorClicked(synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	public void cursorClicked(MultiTouchCursorEvent event) {
	}

	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#cursorPressed(synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	public void cursorPressed(MultiTouchCursorEvent event) {
		if(isInCorner(event)) {
			synchronized(cursorTiming) {
				cursorTiming.put(event.getCursorID(), System.currentTimeMillis());
			}
		}
	}

	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#cursorReleased(synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	public void cursorReleased(MultiTouchCursorEvent event) {
		synchronized(cursorTiming) {
			cursorTiming.remove(event.getCursorID());
		}
	}

	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#objectAdded(synergynetframework.mtinput.events.MultiTouchObjectEvent)
	 */
	public void objectAdded(MultiTouchObjectEvent event) {
	}

	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#objectChanged(synergynetframework.mtinput.events.MultiTouchObjectEvent)
	 */
	public void objectChanged(MultiTouchObjectEvent event) {
	}

	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#objectRemoved(synergynetframework.mtinput.events.MultiTouchObjectEvent)
	 */
	public void objectRemoved(MultiTouchObjectEvent event) {
	}

	/**
	 * Checks if is in corner.
	 *
	 * @param event the event
	 * @return true, if is in corner
	 */
	protected boolean isInCorner(MultiTouchCursorEvent event) {
		int x = SynergyNetDesktop.getInstance().tableToScreenX(event.getPosition().x);
		int y = SynergyNetDesktop.getInstance().tableToScreenY(event.getPosition().y);
		
		return
		x > SynergyNetDesktop.getInstance().getDisplayWidth() - cornerDistance &&
		y > SynergyNetDesktop.getInstance().getDisplayHeight() - cornerDistance;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.Updateable#update(float)
	 */
	public void update(float interpolation) {
		
		if (ganttChart.isCollidable())
			ganttChart.update();
		
		
		if(menuContainer.isVisible()){
			if((System.currentTimeMillis() - startVisibleTime)> visibleInterval) menuContainer.setVisible(false);
		}
		long endTime = System.currentTimeMillis();
		for(long id : cursorTiming.keySet()){
			long startTime = cursorTiming.get(id);
			if(endTime - startTime > interval ) {
					synchronized(cursorTiming) {
						cursorTiming.remove(id);
					}
					if(enabled){
						menuContainer.setVisible(true);
						startVisibleTime = System.currentTimeMillis();
					}
			}
		}
	}
}
