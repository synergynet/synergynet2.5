package apps.projectmanagement.component.ganttchart;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import apps.projectmanagement.ProjectManagementApp;
import apps.projectmanagement.component.ganttchart.Menu.MenuCommandListener;
import apps.projectmanagement.component.tools.TouchPadScreen;
import apps.projectmanagement.gesture.PeriodBarControl;
import apps.projectmanagement.gesture.ScheduleBarControl;
import apps.projectmanagement.gesture.ScheduleBarControl.ScheduleBarListener;

import com.jme.math.Vector2f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.Frame;
import synergynetframework.appsystem.contentsystem.items.LightImageLabel;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.TextLabel;
import synergynetframework.appsystem.contentsystem.items.Window;

public class TaskRow {

	private float y;
	private float lengthPerDay;
	private ContentSystem contentSystem;
	private Window periodBarExtra;
	private Frame periodBar;
	private LightImageLabel milestone;
	private TouchPadScreen touchPad;
	private float periodBarWidth;
	private InputBox inputBox;
	private float rowHeigth;
	private int rowIndex=0;
	private Vector2f orgin;
	private Window periodBarParent;
	private Window taskBarParent;
	private Menu menu;
	private TextLabel periodString;
	private Node rootNode;
	
	protected List<TaskRowCommandListener> taskRowCommandListeners = new ArrayList<TaskRowCommandListener>();
	
	public TaskRow(Node rootNode, ContentSystem contentSystem, Window periodBarParent, Window taskBarParent, float rowHeigth, float lengthPerDay, Vector2f orgin){
		
		this.rowHeigth = rowHeigth;
		this.lengthPerDay = lengthPerDay;
		this.contentSystem = contentSystem;
		this.periodBarParent = periodBarParent;
		this.taskBarParent = taskBarParent;
		this.orgin = orgin;
		this.rootNode = rootNode;
		
		periodBarWidth = lengthPerDay*5;
		
		this.buildMenu();
		this.buildInputBox();
		this.buildPeriodBar();
		
	}
	
	public void clear(){
		periodBarParent.removeSubItem(periodBar, true);
		periodBarParent.removeSubItem(periodBarExtra, true);
		periodBar = null;
		periodBarExtra = null;
		
		inputBox.clear();
		inputBox = null;
		
		menu.clear();
		menu = null;
		
		
	}
	
	protected void buildInputBox(){
		inputBox = new InputBox(contentSystem, 136, (int) (rowHeigth*0.6));
			
		this.taskBarParent.addSubItem(inputBox.getInputBox());
		
		inputBox.setInputBoxLocation(-this.taskBarParent.getWidth()/2+136/2+5, orgin.y);
		
		update();
		
		Spatial inputBoxSpatial = ((Spatial)(this.inputBox.getInputBox().getImplementationObject()));
		inputBoxSpatial.setZOrder(10001);
		this.inputBox.getInputBox().setBringToTopable(false);
	}
	
	protected void buildMenu(){
		menu = new Menu(contentSystem);		
		this.taskBarParent.addSubItem(menu.getMenuNode());
		menu.setMenuLocation(65, orgin.y);
		update();
		
		menu.addMenuCommandListener(new MenuCommandListener(){

			@Override
			public void addMilestone(SimpleButton senderButton) {
				for (TaskRowCommandListener l: taskRowCommandListeners)
					l.addMilestone();	
				
				if (milestone.isVisible()){
					milestone.setVisible(false);
					senderButton.setText("Add Milestone");
					
				}
				else{
					milestone.setVisible(true);
					senderButton.setText("Remove Milestone");
				}
				
				menu.setVisiable(false);
			}

			@Override
			public void addSequenceLine(SimpleButton senderButton) {
				for (TaskRowCommandListener l: taskRowCommandListeners)
					l.addSequenceLine();			
			}

			@Override
			public void delete(SimpleButton senderButton) {
				
				menu.setVisiable(false);
				
				for (TaskRowCommandListener l: taskRowCommandListeners)
					l.delete();
				
			}

			@Override
			public void editTask(SimpleButton senderButton) {
				if (inputBox.isKeyboradOn()){
					inputBox.hideKeyborad();
					senderButton.setText("Show Keyborad");
					menu.setVisiable(false);
				}
				else{
					inputBox.showKeyborad();
					senderButton.setText("Hide Keyborad");
				}
				
				for (TaskRowCommandListener l: taskRowCommandListeners)
					l.editTask();
				
			}

			@Override
			public void selectTask(SimpleButton senderButton) {
				for (TaskRowCommandListener l: taskRowCommandListeners)
					l.selectTask();
				
			}

			@Override
			public void toggleControlPanel(SimpleButton senderButton) {
				
				for (TaskRowCommandListener l: taskRowCommandListeners)
					l.toggleControlPanel();
				
				menu.setVisiable(false);
				
			}
			
		});
	}
	
	protected void buildPeriodBar(){
		
		
		periodBar = (Frame) contentSystem.createContentItem(Frame.class);
		periodBar.setHeight((int) (this.rowHeigth/2));
		periodBar.setWidth(70);
		periodBar.setBorderSize(1);
		periodBar.setBorderColour(Color.blue);
		periodBar.setLocalLocation(orgin.x+this.periodBar.getWidth()/2, orgin.y);
		

		periodBarExtra = (Window) contentSystem.createContentItem(Window.class);
		periodBarExtra.setHeight((int) (this.rowHeigth/2));
		periodBarExtra.setWidth(40);
		periodBarExtra.setBorderSize(0);
		periodBarExtra.setBorderColour(Color.red);
		periodBarExtra.setBackgroundColour(Color.white);
		periodBarExtra.setLocalLocation(periodBar.getLocalLocation().x+periodBar.getWidth()/2+periodBarWidth/2+1, orgin.y);
		
		
		
		periodString = (TextLabel) contentSystem.createContentItem(TextLabel.class);
		periodString.setHeight((int) (this.rowHeigth/2));
		periodString.setWidth(30);
		periodString.setBorderSize(0);
		periodString.setBorderColour(Color.GRAY);
		periodString.setBackgroundColour(Color.white);
		Font font = new Font("Arial Narrow", Font.PLAIN, 12);
		periodString.setTextColour(Color.LIGHT_GRAY);
		periodString.setFont(font);
		periodString.setText((getStartDay()+1)+" - "+(getStartDay()+getDays()));
		periodString.setLocalLocation(5, 0);
		
		milestone = (LightImageLabel)contentSystem.createContentItem(LightImageLabel.class);
		milestone.drawImage(ProjectManagementApp.class.getResource("milestone.png"));
		milestone.setImageLabelHeight(11);
		milestone.setLocalLocation(-15, 0);
		milestone.setBorderSize( 0);
		milestone.setVisible(false);
		
		periodBarExtra.addSubItem(periodString);
		periodBarExtra.addSubItem(milestone);
		
		this.periodBarParent.addSubItem(periodBar);
		this.periodBarParent.addSubItem(periodBarExtra);

		
		update();
		
		Spatial periodBarSpatial = ((Spatial)(this.periodBar.getImplementationObject()));
		periodBarSpatial.setZOrder(10000);
		periodBar.setBringToTopable(false);
		
		Spatial periodBarExtraSpatial = ((Spatial)(this.periodBarExtra.getImplementationObject()));
		periodBarExtraSpatial.setZOrder(10000, true);
		periodBarExtra.setBringToTopable(false);
		
		ScheduleBarControl scheduleBarControl = new ScheduleBarControl(periodBarSpatial, (Spatial)(periodBarParent.getImplementationObject()), 70*15, 70);
		scheduleBarControl.addScheduleBarListener(new ScheduleBarListener(){

			@Override
			public void moved(float startPointX, float length) {
				periodBar.setLocalLocation(startPointX+length/2, periodBarExtra.getLocalLocation().y);
				periodBarWidth = length;
				periodBarExtra.setLocalLocation(startPointX+length+periodBarExtra.getWidth()/2+1, periodBarExtra.getLocalLocation().y);
				periodString.setText((getStartDay()+1)+" - "+(getStartDay()+getDays()));
			}
			
		});
		
		touchPad = new TouchPadScreen(periodBar.name+"touch pad", 100);
		
		PeriodBarControl touchPadControl = new PeriodBarControl(touchPad.getScreenQuad(), (Spatial)(periodBar.getImplementationObject()), 70*15, 70);
		touchPadControl.setPickMeOnly(true);
		touchPadControl.addScheduleBarListener(new PeriodBarControl.ScheduleBarListener(){

			@Override
			public void moved(float startPointX, float length) {
				periodBar.setLocalLocation(startPointX+length/2, periodBarExtra.getLocalLocation().y);
				periodBarWidth = length;
				periodBarExtra.setLocalLocation(startPointX+length+periodBarExtra.getWidth()/2+1, periodBarExtra.getLocalLocation().y);
				periodString.setText((getStartDay()+1)+" - "+(getStartDay()+getDays()));
			}
			
		});
		
		rootNode.getParent().attachChild(touchPad);
		touchPad.getScreenQuad().setZOrder(10008);
		touchPad.setZOrder(10007);
		touchPad.setLocalTranslation(1024/2, 300, 0);
		rootNode.updateGeometricState(0, false);
		
		
		
	}
	
	public float getPeriodBarWidth(){
		return periodBarWidth;
	}
	
	public Frame getPeriodBar(){
		return periodBar;
	}
	
	public void setStartDay(int startDay){
		float startPoint = this.lengthPerDay*startDay;
		float periodBarX = startPoint+periodBarWidth/2;
		periodBar.setLocalLocation(orgin.x+periodBarX, periodBar.getLocalLocation().y);
		periodBarExtra.setLocalLocation(orgin.x+startPoint+periodBarWidth+periodBarExtra.getWidth()/2+1, periodBar.getLocalLocation().y);
		periodString.setText((getStartDay()+1)+" - "+(getStartDay()+getDays()));
	}
	
	public int getStartDay(){
		float periodBarX = periodBarParent.getWidth()/2 + periodBar.getLocalLocation().x-periodBarWidth/2;
		return (int) (periodBarX/lengthPerDay);
	}
	
	public int getDays(){
		return (int) (periodBarWidth/lengthPerDay);
	}
	
	public void setRowIndex(int rowIndex){
		this.rowIndex = rowIndex;
		
		update();
	}
	
	private void update(){
		this.y = -(this.rowHeigth/2+rowIndex*rowHeigth)+orgin.y;
		
		if (periodBar!=null)
			periodBar.setLocalLocation(this.periodBar.getLocalLocation().x, this.y);
		if (periodBarExtra!=null)
			periodBarExtra.setLocalLocation(this.periodBarExtra.getLocalLocation().x, this.y);
		if (inputBox!=null)
			inputBox.setInputBoxLocation(inputBox.getInputBox().getLocalLocation().x, this.y);
		if (menu!=null)
			menu.setMenuLocation(menu.getMenuNode().getLocalLocation().x, this.y);
			
	}
	
	public void addTaskRowCommandListener(TaskRowCommandListener l){
		taskRowCommandListeners.add(l);
	}

	public void removeMenuCommandListener(MenuCommandListener l){
		if (taskRowCommandListeners.contains(l))
			taskRowCommandListeners.remove(l);
	}
	
	public interface TaskRowCommandListener {
		public void selectTask();
		public void editTask();
		public void toggleControlPanel();
		public void addSequenceLine();
		public void addMilestone();
		public void delete();
		
	}
	
}