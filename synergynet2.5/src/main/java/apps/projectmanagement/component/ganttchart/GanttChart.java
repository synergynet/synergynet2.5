package apps.projectmanagement.component.ganttchart;

import java.awt.Color;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.DrawableFrame;
import synergynetframework.appsystem.contentsystem.items.ImageTextLabel;
import synergynetframework.appsystem.contentsystem.items.LightImageLabel;
import synergynetframework.appsystem.contentsystem.items.Window;
import synergynetframework.appsystem.contentsystem.items.listener.ItemEventAdapter;
import synergynetframework.jme.cursorsystem.elements.twod.ClipRegistry;
import synergynetframework.jme.cursorsystem.elements.twod.OrthoControlPointRotateTranslateScale;
import apps.projectmanagement.ProjectManagementApp;
import apps.projectmanagement.component.ganttchart.ScrollBar.Direction;
import apps.projectmanagement.component.ganttchart.ScrollBar.ScrollListener;
import apps.projectmanagement.component.ganttchart.TaskRow.TaskRowCommandListener;
import apps.projectmanagement.component.utils.ClipRectangleHud;

import com.jme.math.Vector2f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;

/**
 * The Class GanttChart.
 */
public class GanttChart extends Node {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7525345654371240326L;

	/** The content system. */
	protected ContentSystem contentSystem;

	/** The crh. */
	protected ClipRectangleHud crh;

	/** The gantt chart. */
	protected GanttChart ganttChart;

	/** The heigth. */
	protected int heigth;

	/** The panel. */
	protected Window panel;

	/** The period length. */
	private int periodLength = 70;

	/** The periods. */
	private int periods = 15;

	/** The row height. */
	private int rowHeight = 30;

	/** The schedule. */
	protected DrawableFrame schedule;

	/** The schedule area. */
	protected Window scheduleArea;

	/** The schedule crh. */
	protected ClipRectangleHud scheduleCRH;

	/** The schedule height offside. */
	private int scheduleHeightOffside = 22;

	/** The schedule height ratio. */
	private float scheduleHeightRatio = 0.85f;

	/** The schedule panel. */
	protected Window schedulePanel;

	/** The schedule panel orgin. */
	private Vector2f schedulePanelOrgin = new Vector2f();

	/** The schedule width offside. */
	private int scheduleWidthOffside = 13;

	/** The schedule width ratio. */
	private float scheduleWidthRatio = 0.75f;

	/** The task area. */
	protected Window taskArea;

	/** The task area panel. */
	protected Window taskAreaPanel;

	/** The task collection. */
	protected TaskCollection taskCollection;

	/** The task crh. */
	protected ClipRectangleHud taskCRH;

	/** The task panel. */
	protected DrawableFrame taskPanel;

	/** The task rows. */
	private int taskRows = 20;

	/** The task width ratio. */
	private float taskWidthRatio = 0.25f;

	/** The timebar. */
	protected DrawableFrame timebar;

	/** The timebar area. */
	protected Window timebarArea;

	/** The width. */
	protected int width;

	/** The x. */
	private int x;

	/** The y. */
	private int y;

	/**
	 * Instantiates a new gantt chart.
	 *
	 * @param contentSystem
	 *            the content system
	 * @param width
	 *            the width
	 * @param heigth
	 *            the heigth
	 */
	public GanttChart(ContentSystem contentSystem, int width, int heigth) {
		this.contentSystem = contentSystem;
		this.width = width;
		this.heigth = heigth;
		taskCollection = new TaskCollection();

		init();
		ganttChart = this;

		this.setVisibility(false);
	}

	/**
	 * Builds the buttons.
	 */
	protected void buildButtons() {

		LightImageLabel addbutton = (LightImageLabel) contentSystem
				.createContentItem(LightImageLabel.class);
		addbutton.drawImage(ProjectManagementApp.class
				.getResource("addbutton.png"));
		addbutton.setImageLabelHeight(20);
		addbutton.setLocalLocation((-width / 2) + 85, (heigth / 2) - 43);
		addbutton.setBorderSize(0);
		addbutton.setBringToTopable(false);
		addbutton.addItemListener(new ItemEventAdapter() {
			
			@Override
			public void cursorDoubleClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorDoubleClicked(item, id, x, y, pressure);

				final TaskRow row = new TaskRow(ganttChart, contentSystem,
						schedulePanel, taskAreaPanel, rowHeight,
						periodLength / 5, schedulePanelOrgin);
				ClipRegistry.getInstance()
						.registerClipRegion(
								(Spatial) (row.getPeriodBar()
										.getImplementationObject()),
								scheduleCRH);
				taskCollection.addTaskRow(row);
				row.addTaskRowCommandListener(new TaskRowCommandListener() {
					
					@Override
					public void addMilestone() {
						
					}
					
					@Override
					public void addSequenceLine() {
						
					}
					
					@Override
					public void delete() {
						taskCollection.removeTaskRow(row);
					}
					
					@Override
					public void editTask() {
						
					}
					
					@Override
					public void selectTask() {
						
					}
					
					@Override
					public void toggleControlPanel() {
						
					}

				});

				taskCRH.setSpatialClip(
						(Spatial) (taskAreaPanel.getImplementationObject()),
						true);
				scheduleCRH.setSpatialClip(
						(Spatial) (schedulePanel.getImplementationObject()),
						true);
				
			}

		});

		panel.addSubItem(addbutton);

		ImageTextLabel titleBar = (ImageTextLabel) contentSystem
				.createContentItem(ImageTextLabel.class);
		titleBar.setImageInfo(ProjectManagementApp.class
				.getResource("titlebar.png"));
		titleBar.setAutoFit(false);
		titleBar.setHeight(38);
		titleBar.setWidth(width - 8);
		titleBar.setLocalLocation(0, (heigth / 2) - 20);
		titleBar.setBorderSize(0);
		titleBar.setBorderColour(Color.red);
		panel.addSubItem(titleBar);

		new OrthoControlPointRotateTranslateScale(
				(Spatial) (titleBar.getImplementationObject()),
				(Spatial) (this));
		
	}

	/**
	 * Builds the schedule area.
	 */
	protected void buildScheduleArea() {

		scheduleArea = (Window) contentSystem.createContentItem(Window.class);
		scheduleArea.setBackgroundColour(Color.WHITE);
		scheduleArea.setWidth((int) ((width * scheduleWidthRatio)
				- scheduleWidthOffside - 3));
		scheduleArea.setHeight((int) ((heigth * scheduleHeightRatio) - 3));
		scheduleArea.setLocation(
				(int) ((width - (width * scheduleWidthRatio)) / 2)
						- scheduleWidthOffside - 3,
				(int) ((-heigth + (heigth * scheduleHeightRatio)) / 2)
						+ scheduleHeightOffside + 3);
		scheduleArea.setBorderSize(1);
		scheduleArea.setBorderColour(Color.black);
		scheduleArea.setBringToTopable(false);
		scheduleArea.setRotateTranslateScalable(false);

		schedulePanel = (Window) contentSystem.createContentItem(Window.class);
		schedulePanel.setLocalLocation(x, y);
		schedulePanel.setWidth(periods * periodLength);
		schedulePanel.setHeight(rowHeight * taskRows);

		schedule = (DrawableFrame) contentSystem
				.createContentItem(DrawableFrame.class);
		schedule.setLocalLocation(0, 0);
		schedule.setWidth(periods * periodLength);
		schedule.setHeight(rowHeight * taskRows);
		schedule.setBorderSize(1);
		schedule.setBorderColour(Color.black);
		schedule.setDrawableContent(new ScheduleDrawableContent(taskRows,
				periods * periodLength, rowHeight));
		schedule.setBringToTopable(false);
		schedule.setRotateTranslateScalable(false);

		schedulePanel.addSubItem(schedule);
		schedulePanelOrgin.x = (-periods * periodLength) / 2;
		schedulePanelOrgin.y = (rowHeight * taskRows) / 2;

		scheduleArea.addSubItem(schedulePanel);
		
		panel.addSubItem(scheduleArea);

		Spatial scheduleSpatial = ((Spatial) (scheduleArea
				.getImplementationObject()));
		scheduleCRH = new ClipRectangleHud(scheduleSpatial,
				scheduleArea.getWidth() - 3, scheduleArea.getHeight() - 3);
		scheduleCRH.setSpatialClip(
				(Spatial) (schedulePanel.getImplementationObject()), true);
		
	}

	/**
	 * Builds the scroll bars.
	 */
	protected void buildScrollBars() {
		ScrollBar scrollBar = new ScrollBar((int) ((width * scheduleWidthRatio)
				- scheduleWidthOffside - 3), 20, periods * periodLength,
				contentSystem, Direction.H);
		scrollBar.getScrollBarContentItem().setLocalLocation(
				(int) ((width - (width * scheduleWidthRatio)) / 2)
						- scheduleWidthOffside - 3, (-heigth / 2) + 15);
		scrollBar.addScrollBarListener(new ScrollListener() {
			
			@Override
			public void moved(float scrollBarMovedDistance,
					float contentMovedDistance) {
				timebar.setLocalLocation(x - contentMovedDistance, 0);
				schedulePanel.setLocalLocation(x - contentMovedDistance,
						schedulePanel.getLocalLocation().y);
			}

		});

		panel.addSubItem(scrollBar.getScrollBarContentItem());

		ScrollBar scrollBarV = new ScrollBar(
				(int) ((heigth * scheduleHeightRatio) - 3), 20, rowHeight
						* taskRows, contentSystem, Direction.V);
		scrollBarV.getScrollBarContentItem().setLocalLocation(
				(width / 2) - 15,
				(int) ((-heigth + (heigth * scheduleHeightRatio)) / 2)
						+ scheduleHeightOffside + 3);
		scrollBarV.addScrollBarListener(new ScrollListener() {
			
			@Override
			public void moved(float scrollBarMovedDistance,
					float contentMovedDistance) {
				taskAreaPanel.setLocalLocation(
						taskAreaPanel.getLocalLocation().x, y
								- contentMovedDistance);
				schedulePanel.setLocalLocation(
						schedulePanel.getLocalLocation().x, y
								- contentMovedDistance);
			}

		});

		panel.addSubItem(scrollBarV.getScrollBarContentItem());
	}

	/**
	 * Builds the task area.
	 */
	protected void buildTaskArea() {

		taskArea = (Window) contentSystem.createContentItem(Window.class);
		taskArea.setBackgroundColour(Color.WHITE);
		taskArea.setWidth((int) ((width * taskWidthRatio) - 3));
		taskArea.setHeight((int) ((heigth * scheduleHeightRatio) - 3));
		taskArea.setLocation(
				(int) (-(width - (width * taskWidthRatio)) / 2) + 3,
				(int) ((-heigth + (heigth * scheduleHeightRatio)) / 2)
						+ scheduleHeightOffside + 3);
		taskArea.setBorderSize(1);
		taskArea.setBorderColour(Color.black);
		taskArea.setBringToTopable(false);
		taskArea.setRotateTranslateScalable(false);

		taskAreaPanel = (Window) contentSystem.createContentItem(Window.class);
		taskAreaPanel.setLocalLocation(-2, y);
		taskAreaPanel.setWidth((int) ((taskWidthRatio * width) - 3));
		taskAreaPanel.setHeight(rowHeight * taskRows);

		taskPanel = (DrawableFrame) contentSystem
				.createContentItem(DrawableFrame.class);
		taskPanel.setLocalLocation(0, 0);
		taskPanel.setWidth((int) ((taskWidthRatio * width) - 3));
		taskPanel.setHeight(rowHeight * taskRows);
		taskPanel.setBorderSize(1);
		taskPanel.setBorderColour(Color.black);
		taskPanel.setDrawableContent(new ScheduleDrawableContent(taskRows, 170,
				rowHeight));
		taskPanel.setBringToTopable(false);
		taskPanel.setRotateTranslateScalable(false);

		taskAreaPanel.addSubItem(taskPanel);

		taskArea.addSubItem(taskAreaPanel);

		panel.addSubItem(taskArea);

		Spatial taskPanelSpatial = ((Spatial) (taskArea
				.getImplementationObject()));
		taskCRH = new ClipRectangleHud(taskPanelSpatial,
				taskArea.getWidth() - 3, taskArea.getHeight() - 3);
		taskCRH.setSpatialClip(
				(Spatial) (taskAreaPanel.getImplementationObject()), true);
	}

	/**
	 * Builds the time bar area.
	 */
	protected void buildTimeBarArea() {
		timebarArea = (Window) contentSystem.createContentItem(Window.class);
		timebarArea.setWidth((int) ((width * scheduleWidthRatio)
				- scheduleWidthOffside - 3));
		timebarArea.setHeight(30);
		timebarArea.setLocation(
				(int) ((width - (width * scheduleWidthRatio)) / 2)
						- scheduleWidthOffside - 3,
				(int) ((heigth * scheduleHeightRatio) - (heigth / 2))
						+ scheduleHeightOffside + 12);
		timebarArea.setBorderSize(0);
		timebarArea.setBorderColour(Color.red);
		timebarArea.setRotateTranslateScalable(false);

		timebar = (DrawableFrame) contentSystem
				.createContentItem(DrawableFrame.class);
		timebar.setLocalLocation(x, 0);
		timebar.setWidth(periods * periodLength);
		timebar.setHeight(25);
		timebar.setBorderSize(1);
		timebar.setBorderColour(Color.black);
		timebar.setDrawableContent(new TimebarDrawableContent(periods, "Week",
				periodLength, 25));
		timebar.setRotateTranslateScalable(false);

		timebarArea.addSubItem(timebar);
		
		panel.addSubItem(timebarArea);

		Spatial spat = ((Spatial) (timebarArea.getImplementationObject()));
		crh = new ClipRectangleHud(spat, timebarArea.getWidth(),
				timebarArea.getHeight());
		crh.setSpatialClip((Spatial) (timebar.getImplementationObject()), true);
		
	}

	/**
	 * Inits the.
	 */
	protected void init() {

		x = -(((int) ((width * scheduleWidthRatio) - scheduleWidthOffside - 3) / 2) - ((periods * periodLength) / 2));
		y = ((int) ((((heigth * scheduleHeightRatio) - 3) / 2)
				- ((rowHeight * taskRows) / 2) - 2));

		panel = (Window) contentSystem.createContentItem(Window.class);
		panel.setLocalLocation(512, 384);
		panel.setWidth(width);
		panel.setHeight(heigth);
		panel.setBringToTopable(false);
		panel.setRotateTranslateScalable(false);

		this.buildButtons();
		this.buildScrollBars();
		this.buildScheduleArea();
		this.buildTaskArea();
		this.buildTimeBarArea();

		this.attachChild((Node) (panel.getImplementationObject()));
		
	}

	/**
	 * Checks if is visible.
	 *
	 * @return true, if is visible
	 */
	public boolean isVisible() {
		return this.isCollidable();
	}

	/**
	 * Sets the visibility.
	 *
	 * @param b
	 *            the new visibility
	 */
	public void setVisibility(boolean b) {
		if (b) {
			this.setCullHint(CullHint.Never);
		} else {
			this.setCullHint(CullHint.Always);
		}

		this.setIsCollidable(b);
	}

	/**
	 * Update.
	 */
	public void update() {
		crh.updateEquations();
		taskCRH.updateEquations();
		scheduleCRH.updateEquations();

	}
	
}
