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

public class ProjectManagementMenu extends MenuController implements IMultiTouchEventListener{
	
	protected float cornerDistance = 40f;
	protected Map<Long,Long> cursorTiming = new HashMap<Long,Long>();
	protected long interval = 1000;
	private boolean enabled;
	private ListContainer menuContainer;
	private ContentSystem contentSystem;
	
	protected WorkflowTool workflowControlButtons;	
	protected GanttChart ganttChart;
	
	private long startVisibleTime = -1,visibleInterval = 10000;
	
	public ProjectManagementMenu(ContentSystem contentSystem, Node orthoNode) {
		this.contentSystem = contentSystem;
		ganttChart = new GanttChart(contentSystem, 700, 520);
		workflowControlButtons = new WorkflowTool(contentSystem);	
		orthoNode.attachChild(ganttChart);
		
		String fileName = "apps/projectmanagement/predefinedstaffnodes.txt";
		StaffNodesLoader.loadNode(fileName, contentSystem);
		
		createMenu();
		

	}
	
	
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


	@Override
	public void enableForApplication(SynergyNetApp app) {
		SynergyNetDesktop.getInstance().getMultiTouchInputComponent().registerMultiTouchEventListener(this);
		setEnabled(true);	
	}
	
	@Override
	public void applicationFinishing() {
		if(menuContainer != null) menuContainer.setVisible(false);
		setEnabled(false);
	}
	
	
	
	public boolean isEnabled() {
		return enabled;
	}

	private void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void cursorChanged(MultiTouchCursorEvent event) {
	}

	public void cursorClicked(MultiTouchCursorEvent event) {
	}

	public void cursorPressed(MultiTouchCursorEvent event) {
		if(isInCorner(event)) {
			synchronized(cursorTiming) {
				cursorTiming.put(event.getCursorID(), System.currentTimeMillis());
			}
		}
	}

	public void cursorReleased(MultiTouchCursorEvent event) {
		synchronized(cursorTiming) {
			cursorTiming.remove(event.getCursorID());
		}
	}

	public void objectAdded(MultiTouchObjectEvent event) {
	}

	public void objectChanged(MultiTouchObjectEvent event) {
	}

	public void objectRemoved(MultiTouchObjectEvent event) {
	}

	protected boolean isInCorner(MultiTouchCursorEvent event) {
		int x = SynergyNetDesktop.getInstance().tableToScreenX(event.getPosition().x);
		int y = SynergyNetDesktop.getInstance().tableToScreenY(event.getPosition().y);
		
		return
		x > SynergyNetDesktop.getInstance().getDisplayWidth() - cornerDistance &&
		y > SynergyNetDesktop.getInstance().getDisplayHeight() - cornerDistance;
	}

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
