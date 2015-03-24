package apps.threedbuttonsexperiment.calculator;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import apps.threedbuttonsexperiment.calculator.component.TaskListener;
import apps.threedbuttonsexperiment.calculator.component.TwoDDisplayNode;
import apps.threedbuttonsexperiment.logger.DateTextWritter;
import apps.threedbuttonsexperiment.logger.LogListener;
import apps.threedbuttonsexperiment.logger.TrialLog;
import apps.threedbuttonsexperiment.utils.ButtonRenderer;
import apps.threedbuttonsexperiment.utils.StringGenerator;

import com.jme.math.Vector2f;
import com.jme.scene.Node;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.Frame;
import synergynetframework.appsystem.contentsystem.items.TextLabel;
import synergynetframework.appsystem.contentsystem.items.Window;
import synergynetframework.appsystem.contentsystem.items.listener.ItemEventAdapter;


/**
 * The Class TwoDCalculator.
 */
public class TwoDCalculator {

	/** The text string. */
	protected String textString="";
	
	/** The left number. */
	protected String leftNumber ="";
	
	/** The right number. */
	protected String rightNumber = "";
	
	/** The operator. */
	protected String operator ="";
	
	/** The log listener. */
	protected LogListener logListener;
	
	/** The task listeners. */
	protected List<TaskListener> taskListeners = new ArrayList<TaskListener>();
	
	/** The calculator frame. */
	protected Window calculatorFrame;
	
	/** The content system. */
	protected ContentSystem contentSystem;
	
	/** The width. */
	protected float width = 212;
	
	/** The length. */
	protected float length = 320;
	
	/** The button width. */
	protected float buttonWidth = 43f;
	
	/** The button length. */
	protected float buttonLength = 43f;
	
	/** The trail count. */
	protected int trailCount=3;
	
	/** The current trail number. */
	protected int currentTrailNumber = 0;
	
	/** The displayer. */
	protected TwoDDisplayNode displayer;
	
	/** The target number label. */
	protected TextLabel targetNumberLabel;
	
	/** The log writter. */
	protected DateTextWritter logWritter;
	
	/** The location. */
	protected Vector2f location = new Vector2f(512, 350);
	
	/**
	 * Instantiates a new two d calculator.
	 *
	 * @param contentSystem the content system
	 * @param targetNumberLabel the target number label
	 * @param logWritter the log writter
	 */
	public TwoDCalculator(ContentSystem contentSystem,  TextLabel targetNumberLabel, DateTextWritter logWritter){
		this.contentSystem = contentSystem;
		init();
		this.calculatorFrame.setRotateTranslateScalable(false);
		this.targetNumberLabel=targetNumberLabel;
		this.logWritter = logWritter;
				
	}
	
	/**
	 * Reset task.
	 */
	public void resetTask(){
		currentTrailNumber =0;
		startNewTrail();
		
		
	}
	
	/**
	 * Start new trail.
	 */
	public void startNewTrail(){
		String targetNumber = StringGenerator.generateNumber(6);
		logListener = new TrialLog(targetNumber, new Date().getTime(), logWritter);
		targetNumberLabel.setText(targetNumber);
	}
	
	/**
	 * Inits the.
	 */
	protected void init(){
	
		float horizontalSpace = (width-buttonWidth*4)/7;
		float verticalSpace = (length - buttonLength*1.5f - buttonLength*4f)/9f;
		
		this.calculatorFrame=(Window)contentSystem.createContentItem(Window.class);
		calculatorFrame.setBorderSize(0);
		calculatorFrame.setBackgroundColour(new Color(208, 212, 217));
		calculatorFrame.setWidth(212);
		calculatorFrame.setHeight(320);
		calculatorFrame.setLocalLocation(location.x, location.y);
		calculatorFrame.getBackgroundFrame().setBringToTopable(false);
		calculatorFrame.setBringToTopable(true);
		
		displayer = new TwoDDisplayNode("2D displayer", width*0.85f, (float) (length*6/30));
		displayer.setLocalLocation(0, 8.5f*verticalSpace+4*buttonLength+buttonLength/2-length/2);
		displayer.setText("");
		displayer.getDisplayQuad().setZOrder(Integer.MAX_VALUE);
		displayer.getDisplayQuad().updateGeometricState(0f, false);
		((Node)calculatorFrame.getImplementationObject()).attachChild(displayer.getDisplayQuad());
		
		
		final Frame buttonNode0 = (Frame)contentSystem.createContentItem(Frame.class);
		buttonNode0.setLocalLocation(2*horizontalSpace+buttonWidth/2-width/2, 2*verticalSpace+buttonLength/2-length/2, 0);
		buttonNode0.setWidth((int) this.buttonWidth);
		buttonNode0.setHeight((int) this.buttonLength);
		ButtonRenderer.RenderButton(buttonNode0, "0", (int)buttonWidth, false);	
		calculatorFrame.addSubItem(buttonNode0);
		buttonNode0.addItemListener(new ItemEventAdapter(){		
			@Override
			public void cursorPressed(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorPressed(item, id, x, y, pressure);
				textString+="0";
				displayer.setText(textString);
				logListener.keyPressed("0");
				
				displayer.getDisplayQuad().updateGeometricState(0f, false);
				((Node)calculatorFrame.getImplementationObject()).attachChild(displayer.getDisplayQuad());
				
				ButtonRenderer.RenderButton(buttonNode0, "0", (int)buttonWidth, true);	
			}

			@Override
			public void cursorReleased(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorReleased(item, id, x, y, pressure);
				
				ButtonRenderer.RenderButton(buttonNode0, "0", (int)buttonWidth, false);	
			}
						
		});
		
		final Frame buttonNodeDot = (Frame)contentSystem.createContentItem(Frame.class);
		buttonNodeDot.setLocalLocation(3*horizontalSpace+buttonWidth+buttonWidth/2-width/2, 2*verticalSpace+buttonLength/2-length/2, 0);
		buttonNodeDot.setWidth((int) this.buttonWidth);
		buttonNodeDot.setHeight((int) this.buttonLength);
		ButtonRenderer.RenderButton(buttonNodeDot, ".", (int)buttonWidth, false);	
		calculatorFrame.addSubItem(buttonNodeDot);
		buttonNodeDot.addItemListener(new ItemEventAdapter(){		
			@Override
			public void cursorPressed(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorPressed(item, id, x, y, pressure);
				textString+=".";
				displayer.setText(textString);
				logListener.keyPressed(".");
				
				displayer.getDisplayQuad().updateGeometricState(0f, false);
				((Node)calculatorFrame.getImplementationObject()).attachChild(displayer.getDisplayQuad());
				
				ButtonRenderer.RenderButton(buttonNodeDot, ".", (int)buttonWidth, true);	
			}

			@Override
			public void cursorReleased(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorReleased(item, id, x, y, pressure);
				
				ButtonRenderer.RenderButton(buttonNodeDot, ".", (int)buttonWidth, false);		
			}
			
		});
		
		final Frame buttonNode1 = (Frame)contentSystem.createContentItem(Frame.class);
		buttonNode1.setLocalLocation(2*horizontalSpace+buttonWidth/2-width/2, 3*verticalSpace+buttonLength+buttonLength/2-length/2, 0);
		buttonNode1.setWidth((int) this.buttonWidth);
		buttonNode1.setHeight((int) this.buttonLength);
		ButtonRenderer.RenderButton(buttonNode1, "1", (int)buttonWidth, false);	
		calculatorFrame.addSubItem(buttonNode1);
		buttonNode1.addItemListener(new ItemEventAdapter(){		
			@Override
			public void cursorPressed(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorPressed(item, id, x, y, pressure);
				textString+="1";
				displayer.setText(textString);
				logListener.keyPressed("1");
				
				displayer.getDisplayQuad().updateGeometricState(0f, false);
				((Node)calculatorFrame.getImplementationObject()).attachChild(displayer.getDisplayQuad());
				
				ButtonRenderer.RenderButton(buttonNode1, "1", (int)buttonWidth, true);	
			}

			@Override
			public void cursorReleased(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorReleased(item, id, x, y, pressure);
				
				ButtonRenderer.RenderButton(buttonNode1, "1", (int)buttonWidth, false);			
			}
			
		});
		
		final Frame buttonNode2 = (Frame)contentSystem.createContentItem(Frame.class);
		buttonNode2.setLocalLocation(3*horizontalSpace+buttonWidth+buttonWidth/2-width/2, 3*verticalSpace+buttonLength+buttonLength/2-length/2, 0);
		buttonNode2.setWidth((int) this.buttonWidth);
		buttonNode2.setHeight((int) this.buttonLength);
		ButtonRenderer.RenderButton(buttonNode2, "2", (int)buttonWidth, false);	
		calculatorFrame.addSubItem(buttonNode2);
		buttonNode2.addItemListener(new ItemEventAdapter(){		
			@Override
			public void cursorPressed(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorPressed(item, id, x, y, pressure);
				textString+="2";
				displayer.setText(textString);
				logListener.keyPressed("2");
				
				displayer.getDisplayQuad().updateGeometricState(0f, false);
				((Node)calculatorFrame.getImplementationObject()).attachChild(displayer.getDisplayQuad());
				
				ButtonRenderer.RenderButton(buttonNode2, "2", (int)buttonWidth, true);	
			}

			@Override
			public void cursorReleased(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorReleased(item, id, x, y, pressure);
				
				ButtonRenderer.RenderButton(buttonNode2, "2", (int)buttonWidth, false);			
			}
			
		});
			
		
		final Frame buttonNode3 = (Frame)contentSystem.createContentItem(Frame.class);
		buttonNode3.setLocalLocation(4*horizontalSpace+2*buttonWidth+buttonWidth/2-width/2, 3*verticalSpace+buttonLength+buttonLength/2-length/2, 0);
		buttonNode3.setWidth((int) this.buttonWidth);
		buttonNode3.setHeight((int) this.buttonLength);
		ButtonRenderer.RenderButton(buttonNode3, "3", (int)buttonWidth, false);	
		calculatorFrame.addSubItem(buttonNode3);
		buttonNode3.addItemListener(new ItemEventAdapter(){		
			@Override
			public void cursorPressed(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorPressed(item, id, x, y, pressure);
				textString+="3";
				displayer.setText(textString);
				logListener.keyPressed("3");
				
				displayer.getDisplayQuad().updateGeometricState(0f, false);
				((Node)calculatorFrame.getImplementationObject()).attachChild(displayer.getDisplayQuad());
				
				ButtonRenderer.RenderButton(buttonNode3, "3", (int)buttonWidth, true);	
			}

			@Override
			public void cursorReleased(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorReleased(item, id, x, y, pressure);
				
				ButtonRenderer.RenderButton(buttonNode3, "3", (int)buttonWidth, false);		
			}
			
		});
		
		final Frame buttonNode4 = (Frame)contentSystem.createContentItem(Frame.class);
		buttonNode4.setLocalLocation(2*horizontalSpace+buttonWidth/2-width/2, 4*verticalSpace+2*buttonLength+buttonLength/2-length/2, 0);
		buttonNode4.setWidth((int) this.buttonWidth);
		buttonNode4.setHeight((int) this.buttonLength);
		ButtonRenderer.RenderButton(buttonNode4, "4", (int)buttonWidth, false);	
		calculatorFrame.addSubItem(buttonNode4);
		buttonNode4.addItemListener(new ItemEventAdapter(){		
			@Override
			public void cursorPressed(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorPressed(item, id, x, y, pressure);
				textString+="4";
				displayer.setText(textString);
				logListener.keyPressed("4");
				
				displayer.getDisplayQuad().updateGeometricState(0f, false);
				((Node)calculatorFrame.getImplementationObject()).attachChild(displayer.getDisplayQuad());
				
				ButtonRenderer.RenderButton(buttonNode4, "4", (int)buttonWidth, true);		
			}

			@Override
			public void cursorReleased(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorReleased(item, id, x, y, pressure);
				
				ButtonRenderer.RenderButton(buttonNode4, "4", (int)buttonWidth, false);			
			}
			
		});
		
		final Frame buttonNode5 = (Frame)contentSystem.createContentItem(Frame.class);
		buttonNode5.setLocalLocation(3*horizontalSpace+buttonWidth+buttonWidth/2-width/2, 4*verticalSpace+2*buttonLength+buttonLength/2-length/2, 0);
		buttonNode5.setWidth((int) this.buttonWidth);
		buttonNode5.setHeight((int) this.buttonLength);
		ButtonRenderer.RenderButton(buttonNode5, "5", (int)buttonWidth, false);	
		calculatorFrame.addSubItem(buttonNode5);
		buttonNode5.addItemListener(new ItemEventAdapter(){		
			@Override
			public void cursorPressed(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorPressed(item, id, x, y, pressure);
				textString+="5";
				displayer.setText(textString);
				logListener.keyPressed("5");
				
				displayer.getDisplayQuad().updateGeometricState(0f, false);
				((Node)calculatorFrame.getImplementationObject()).attachChild(displayer.getDisplayQuad());
				
				ButtonRenderer.RenderButton(buttonNode5, "5", (int)buttonWidth, true);	
			}

			@Override
			public void cursorReleased(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorReleased(item, id, x, y, pressure);
				
				ButtonRenderer.RenderButton(buttonNode5, "5", (int)buttonWidth, false);				
			}
			
		});
		
		final Frame buttonNode6 = (Frame)contentSystem.createContentItem(Frame.class);
		buttonNode6.setLocalLocation(4*horizontalSpace+2*buttonWidth+buttonWidth/2-width/2, 4*verticalSpace+2*buttonLength+buttonLength/2-length/2, 0);
		buttonNode6.setWidth((int) this.buttonWidth);
		buttonNode6.setHeight((int) this.buttonLength);
		ButtonRenderer.RenderButton(buttonNode6, "6", (int)buttonWidth, false);	
		calculatorFrame.addSubItem(buttonNode6);
		buttonNode6.addItemListener(new ItemEventAdapter(){		
			@Override
			public void cursorPressed(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorPressed(item, id, x, y, pressure);
				textString+="6";
				displayer.setText(textString);
				logListener.keyPressed("6");
				
				displayer.getDisplayQuad().updateGeometricState(0f, false);
				((Node)calculatorFrame.getImplementationObject()).attachChild(displayer.getDisplayQuad());
				
				ButtonRenderer.RenderButton(buttonNode6, "6", (int)buttonWidth, true);	
			}

			@Override
			public void cursorReleased(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorReleased(item, id, x, y, pressure);
				
				ButtonRenderer.RenderButton(buttonNode6, "6", (int)buttonWidth, false);				
			}
			
		});
		
		final Frame buttonNode7 = (Frame)contentSystem.createContentItem(Frame.class);
		buttonNode7.setLocalLocation(2*horizontalSpace+buttonWidth/2-width/2, 5*verticalSpace+3*buttonLength+buttonLength/2-length/2, 0);
		buttonNode7.setWidth((int) this.buttonWidth);
		buttonNode7.setHeight((int) this.buttonLength);
		ButtonRenderer.RenderButton(buttonNode7, "7", (int)buttonWidth, false);	
		calculatorFrame.addSubItem(buttonNode7);
		buttonNode7.addItemListener(new ItemEventAdapter(){		
			@Override
			public void cursorPressed(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorPressed(item, id, x, y, pressure);
				textString+="7";
				displayer.setText(textString);
				logListener.keyPressed("7");
				
				displayer.getDisplayQuad().updateGeometricState(0f, false);
				((Node)calculatorFrame.getImplementationObject()).attachChild(displayer.getDisplayQuad());
				
				ButtonRenderer.RenderButton(buttonNode7, "7", (int)buttonWidth, true);	
			}

			@Override
			public void cursorReleased(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorReleased(item, id, x, y, pressure);
				
				ButtonRenderer.RenderButton(buttonNode7, "7", (int)buttonWidth, false);			
			}
			
		});
		
		final Frame buttonNode8 = (Frame)contentSystem.createContentItem(Frame.class);
		buttonNode8.setLocalLocation(3*horizontalSpace+buttonWidth+buttonWidth/2-width/2, 5*verticalSpace+3*buttonLength+buttonLength/2-length/2, 0);
		buttonNode8.setWidth((int) this.buttonWidth);
		buttonNode8.setHeight((int) this.buttonLength);
		ButtonRenderer.RenderButton(buttonNode8, "8", (int)buttonWidth, false);	
		calculatorFrame.addSubItem(buttonNode8);
		buttonNode8.addItemListener(new ItemEventAdapter(){		
			@Override
			public void cursorPressed(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorPressed(item, id, x, y, pressure);
				textString+="8";
				displayer.setText(textString);
				logListener.keyPressed("8");
				
				displayer.getDisplayQuad().updateGeometricState(0f, false);
				((Node)calculatorFrame.getImplementationObject()).attachChild(displayer.getDisplayQuad());
				
				ButtonRenderer.RenderButton(buttonNode8, "8", (int)buttonWidth, true);		
			}

			@Override
			public void cursorReleased(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorReleased(item, id, x, y, pressure);
				
				ButtonRenderer.RenderButton(buttonNode8, "8", (int)buttonWidth, false);			
			}
			
		});
		
		final Frame buttonNode9 = (Frame)contentSystem.createContentItem(Frame.class);
		buttonNode9.setLocalLocation(4*horizontalSpace+2*buttonWidth+buttonWidth/2-width/2, 5*verticalSpace+3*buttonLength+buttonLength/2-length/2, 0);
		buttonNode9.setWidth((int) this.buttonWidth);
		buttonNode9.setHeight((int) this.buttonLength);
		ButtonRenderer.RenderButton(buttonNode9, "9", (int)buttonWidth, false);	
		calculatorFrame.addSubItem(buttonNode9);
		buttonNode9.addItemListener(new ItemEventAdapter(){		
			@Override
			public void cursorPressed(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorPressed(item, id, x, y, pressure);
				textString+="9";
				displayer.setText(textString);
				logListener.keyPressed("9");
				
				displayer.getDisplayQuad().updateGeometricState(0f, false);
				((Node)calculatorFrame.getImplementationObject()).attachChild(displayer.getDisplayQuad());
				
				ButtonRenderer.RenderButton(buttonNode9, "9", (int)buttonWidth, true);	
			}

			@Override
			public void cursorReleased(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorReleased(item, id, x, y, pressure);
				
				ButtonRenderer.RenderButton(buttonNode9, "9", (int)buttonWidth, false);				
			}
			
		});
		
		final Frame buttonNodeDelete = (Frame)contentSystem.createContentItem(Frame.class);
		buttonNodeDelete.setLocalLocation(5*horizontalSpace+3*buttonWidth+buttonWidth/2-width/2, 5*verticalSpace+3*buttonLength+buttonLength/2-length/2, 0);
		buttonNodeDelete.setWidth((int) this.buttonWidth);
		buttonNodeDelete.setHeight((int) this.buttonLength);
		ButtonRenderer.RenderButton(buttonNodeDelete, "d", (int)buttonWidth, false);	
		calculatorFrame.addSubItem(buttonNodeDelete);
		buttonNodeDelete.addItemListener(new ItemEventAdapter(){		
			@Override
			public void cursorPressed(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorPressed(item, id, x, y, pressure);
				if (textString.length()<=0) return;
					textString = textString.substring(0, textString.length()-1);
				displayer.setText(textString);			
				logListener.deleteKeyPressed();
				
				displayer.getDisplayQuad().updateGeometricState(0f, false);
				((Node)calculatorFrame.getImplementationObject()).attachChild(displayer.getDisplayQuad());
				
				ButtonRenderer.RenderButton(buttonNodeDelete, "d", (int)buttonWidth, true);		
			}

			@Override
			public void cursorReleased(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorReleased(item, id, x, y, pressure);
				
				ButtonRenderer.RenderButton(buttonNodeDelete, "d", (int)buttonWidth, false);				
			}
			
		});
		
		final Frame buttonNodePlus = (Frame)contentSystem.createContentItem(Frame.class);
		buttonNodePlus.setLocalLocation(5*horizontalSpace+3*buttonWidth+buttonWidth/2-width/2, 4*verticalSpace+2*buttonLength+buttonLength/2-length/2, 0);
		buttonNodePlus.setWidth((int) this.buttonWidth);
		buttonNodePlus.setHeight((int) this.buttonLength);
		ButtonRenderer.RenderButton(buttonNodePlus, "+", (int)buttonWidth, false);	
		calculatorFrame.addSubItem(buttonNodePlus);
		buttonNodePlus.addItemListener(new ItemEventAdapter(){		
			@Override
			public void cursorPressed(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorPressed(item, id, x, y, pressure);
				textString+="+";
				displayer.setText(textString);
				logListener.keyPressed("+");
				
				displayer.getDisplayQuad().updateGeometricState(0f, false);
				((Node)calculatorFrame.getImplementationObject()).attachChild(displayer.getDisplayQuad());
				
				ButtonRenderer.RenderButton(buttonNodePlus, "+", (int)buttonWidth, true);	
			}

			@Override
			public void cursorReleased(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorReleased(item, id, x, y, pressure);
				
				ButtonRenderer.RenderButton(buttonNodePlus, "+", (int)buttonWidth, false);			
			}
			
		});
		
		final Frame buttonNodeSubstract = (Frame)contentSystem.createContentItem(Frame.class);
		buttonNodeSubstract.setLocalLocation(5*horizontalSpace+3*buttonWidth+buttonWidth/2-width/2, 3*verticalSpace+buttonLength+buttonLength/2-length/2, 0);
		buttonNodeSubstract.setWidth((int) this.buttonWidth);
		buttonNodeSubstract.setHeight((int) this.buttonLength);
		ButtonRenderer.RenderButton(buttonNodeSubstract, "-", (int)buttonWidth, false);	
		calculatorFrame.addSubItem(buttonNodeSubstract);
		buttonNodeSubstract.addItemListener(new ItemEventAdapter(){		
			@Override
			public void cursorPressed(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorPressed(item, id, x, y, pressure);
				textString+="-";
				displayer.setText(textString);
				logListener.keyPressed("-");
				
				displayer.getDisplayQuad().updateGeometricState(0f, false);
				((Node)calculatorFrame.getImplementationObject()).attachChild(displayer.getDisplayQuad());
				
				ButtonRenderer.RenderButton(buttonNodeSubstract, "-", (int)buttonWidth, true);	
			}

			@Override
			public void cursorReleased(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorReleased(item, id, x, y, pressure);
				
				ButtonRenderer.RenderButton(buttonNodeSubstract, "-", (int)buttonWidth, false);			
			}
			
		});
		
		final Frame buttonNodeResult = (Frame)contentSystem.createContentItem(Frame.class);
		buttonNodeResult.setLocalLocation(4*horizontalSpace+2.58f*buttonWidth+buttonWidth/2-width/2, 2*verticalSpace+buttonLength/2-length/2, 0);
		buttonNodeResult.setWidth((int) (this.buttonWidth*2+horizontalSpace+1f));
		buttonNodeResult.setHeight((int) this.buttonLength);
		ButtonRenderer.RenderButton(buttonNodeResult, "s", (int) this.buttonWidth, false);	
		calculatorFrame.addSubItem(buttonNodeResult);
		buttonNodeResult.addItemListener(new ItemEventAdapter(){		
			@Override
			public void cursorPressed(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorPressed(item, id, x, y, pressure);
				textString = "";
				logListener.submitKeyPressed();
				
				logListener=null;
				
				if (currentTrailNumber<trailCount-1){
					startNewTrail();
					currentTrailNumber++;
				}
				else{
					for (TaskListener l: taskListeners){
						l.taskCompleted();
					}
				}
				
				displayer.getDisplayQuad().updateGeometricState(0f, false);
				((Node)calculatorFrame.getImplementationObject()).attachChild(displayer.getDisplayQuad());
				
				ButtonRenderer.RenderButton(buttonNodeResult, "s", (int) buttonWidth, true);
			}

			@Override
			public void cursorReleased(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorReleased(item, id, x, y, pressure);
				
				ButtonRenderer.RenderButton(buttonNodeResult, "s", (int) buttonWidth, false);		
			}
			
		});	
		
	}

	/**
	 * Gets the location.
	 *
	 * @return the location
	 */
	public Vector2f getLocation() {
		return location;
	}

	/**
	 * Sets the location.
	 *
	 * @param location the new location
	 */
	public void setLocation(Vector2f location) {
		this.location = location;
		calculatorFrame.setLocalLocation(location.x, location.y);
	}
	
	/**
	 * Adds the task listener.
	 *
	 * @param l the l
	 */
	public void addTaskListener(TaskListener l){
		taskListeners.add(l);
	}
	
	/**
	 * Update.
	 */
	public void update(){
		displayer.setText("");
		displayer.getDisplayQuad().updateGeometricState(0f, false);
		((Node)calculatorFrame.getImplementationObject()).attachChild(displayer.getDisplayQuad());
	}
}
