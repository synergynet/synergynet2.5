package apps.threedbuttonsexperiment.calculator;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import synergynetframework.appsystem.contentsystem.items.TextLabel;
import synergynetframework.jme.cursorsystem.elements.threed.ControlPointRotateTranslateScale;
import apps.threedbuttonsexperiment.calculator.button.ButtonNode;
import apps.threedbuttonsexperiment.calculator.button.KeyListener;
import apps.threedbuttonsexperiment.calculator.component.ControllerBodyNode;
import apps.threedbuttonsexperiment.calculator.component.DisplayNode;
import apps.threedbuttonsexperiment.calculator.component.TaskListener;
import apps.threedbuttonsexperiment.logger.DateTextWritter;
import apps.threedbuttonsexperiment.logger.LogListener;
import apps.threedbuttonsexperiment.logger.TrialLog;
import apps.threedbuttonsexperiment.utils.StringGenerator;
import apps.threedmanipulation.ThreeDManipulation;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;

/**
 * The Class Calculator.
 */
public abstract class Calculator extends Node {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2123175967783608868L;

	/** The angle. */
	protected float angle = 0;

	/** The button node0. */
	protected ButtonNode buttonNode0;

	/** The button node1. */
	protected ButtonNode buttonNode1;

	/** The button node2. */
	protected ButtonNode buttonNode2;

	/** The button node3. */
	protected ButtonNode buttonNode3;

	/** The button node4. */
	protected ButtonNode buttonNode4;

	/** The button node5. */
	protected ButtonNode buttonNode5;

	/** The button node6. */
	protected ButtonNode buttonNode6;

	/** The button node7. */
	protected ButtonNode buttonNode7;

	/** The button node8. */
	protected ButtonNode buttonNode8;

	/** The button node9. */
	protected ButtonNode buttonNode9;

	/** The button node delete. */
	protected ButtonNode buttonNodeDelete;

	/** The button node dot. */
	protected ButtonNode buttonNodeDot;

	/** The button node plus. */
	protected ButtonNode buttonNodePlus;

	/** The button node result. */
	protected ButtonNode buttonNodeResult;

	/** The button node substract. */
	protected ButtonNode buttonNodeSubstract;

	/** The button z offset. */
	protected float buttonZOffset = 0.05f;

	/** The current trail number. */
	protected int currentTrailNumber = 0;

	/** The display node. */
	protected DisplayNode displayNode;

	/** The left number. */
	protected String leftNumber = "";

	/** The log listener. */
	protected LogListener logListener;

	/** The log writter. */
	protected DateTextWritter logWritter;

	/** The operator. */
	protected String operator = "";

	/** The right number. */
	protected String rightNumber = "";

	/** The skin url. */
	protected URL skinURL = ThreeDManipulation.class
			.getResource("calculator/body2D.png");

	/** The target number label. */
	protected TextLabel targetNumberLabel;

	/** The task listeners. */
	protected List<TaskListener> taskListeners = new ArrayList<TaskListener>();

	/** The text string. */
	protected String textString = "";

	/** The trail count. */
	protected int trailCount = 3;
	
	/**
	 * Instantiates a new calculator.
	 *
	 * @param name
	 *            the name
	 * @param targetNumberLabel
	 *            the target number label
	 * @param logWritter
	 *            the log writter
	 */
	public Calculator(String name, TextLabel targetNumberLabel,
			DateTextWritter logWritter) {
		super(name);
		setCalculatorProperty();
		init();
		setButtonProperty();

		this.targetNumberLabel = targetNumberLabel;
		this.logWritter = logWritter;

	}

	/**
	 * Adds the task listener.
	 *
	 * @param l
	 *            the l
	 */
	public void addTaskListener(TaskListener l) {
		taskListeners.add(l);
	}

	/**
	 * Gets the current trail number.
	 *
	 * @return the current trail number
	 */
	public int getCurrentTrailNumber() {
		return currentTrailNumber;
	}
	
	/**
	 * Gets the trail count.
	 *
	 * @return the trail count
	 */
	public int getTrailCount() {
		return trailCount;
	}

	/**
	 * Inits the.
	 */
	protected void init() {

		float width = 20f;
		float length = 30f;
		float height = 2f;

		float buttonWidth = 4f;
		float buttonLength = 4f;
		float buttonHeight = 2f;
		
		ControllerBodyNode bn = new ControllerBodyNode("Body" + name,
				width / 2, length / 2, height / 2, 0.1f, skinURL);
		this.attachChild(bn);
		
		float buttonZ = bn.getLocalTranslation().z + this.buttonZOffset;
		float horizontalSpace = (width - (buttonWidth * 4)) / 7;
		float verticalSpace = (length - (buttonLength * 1.5f) - (buttonLength * 4f)) / 9f;

		final DisplayNode dn = new DisplayNode("Display" + name,
				(width * 0.9f) / 2, (length * 7) / 60, height * 1.5f, 0.1f,
				skinURL);
		dn.setLocalTranslation(
				0,
				((8.5f * verticalSpace) + (4 * buttonLength) + (buttonLength / 2))
						- (length / 2), bn.getLocalTranslation().z + 0.5f);
		this.attachChild(dn);
		displayNode = dn;

		Quaternion tq = new Quaternion();
		tq.fromAngleAxis(angle, new Vector3f(1, 0, 0));
		dn.setLocalRotation(tq);
		dn.updateGeometricState(0f, false);

		// row 1
		buttonNode0 = new ButtonNode(
				"0" + name,
				buttonWidth / 2,
				buttonLength / 2,
				buttonHeight / 2,
				0.05f,
				ThreeDManipulation.class.getResource("calculator/buttonBg.png"),
				ThreeDManipulation.class.getResource("calculator/button0.png"),
				ThreeDManipulation.class
						.getResource("calculator/highlightedbutton0.png"));

		buttonNode0.setLocalTranslation(
				((2 * horizontalSpace) + (buttonWidth / 2)) - (width / 2),
				((2 * verticalSpace) + (buttonLength / 2)) - (length / 2),
				buttonZ);
		buttonNode0.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(String key) {
				textString += "0";
				dn.setText(textString);
				if (operator.equals("")) {
					leftNumber = leftNumber + "0";
				} else {
					rightNumber = rightNumber + "0";
				}

				logListener.keyPressed("0");
			}
		});
		this.attachChild(buttonNode0);

		buttonNodeDot = new ButtonNode(
				"." + name,
				buttonWidth / 2,
				buttonLength / 2,
				buttonHeight / 2,
				0.05f,
				ThreeDManipulation.class.getResource("calculator/buttonBg.png"),
				ThreeDManipulation.class
						.getResource("calculator/buttondot.png"),
				ThreeDManipulation.class
						.getResource("calculator/highlightedbuttondot.png"));
		buttonNodeDot.setLocalTranslation(
				((3 * horizontalSpace) + buttonWidth + (buttonWidth / 2))
						- (width / 2),
				((2 * verticalSpace) + (buttonLength / 2)) - (length / 2),
				buttonZ);
		buttonNodeDot.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(String key) {
				textString += ".";
				dn.setText(textString);
				if (operator.equals("")) {
					leftNumber = leftNumber + ".";
				} else {
					rightNumber = rightNumber + ".";
				}

				logListener.keyPressed(".");
			}
		});
		this.attachChild(buttonNodeDot);

		// row 2

		buttonNode1 = new ButtonNode(
				"1" + name,
				buttonWidth / 2,
				buttonLength / 2,
				buttonHeight / 2,
				0.05f,
				ThreeDManipulation.class.getResource("calculator/buttonBg.png"),
				ThreeDManipulation.class.getResource("calculator/button1.png"),
				ThreeDManipulation.class
						.getResource("calculator/highlightedbutton1.png"));
		buttonNode1.setLocalTranslation(
				((2 * horizontalSpace) + (buttonWidth / 2)) - (width / 2),
				((3 * verticalSpace) + buttonLength + (buttonLength / 2))
						- (length / 2), buttonZ);
		buttonNode1.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(String key) {
				textString += "1";
				dn.setText(textString);
				if (operator.equals("")) {
					leftNumber = leftNumber + "1";
				} else {
					rightNumber = rightNumber + "1";
				}

				logListener.keyPressed("1");
			}
		});
		this.attachChild(buttonNode1);

		buttonNode2 = new ButtonNode(
				"2" + name,
				buttonWidth / 2,
				buttonLength / 2,
				buttonHeight / 2,
				0.05f,
				ThreeDManipulation.class.getResource("calculator/buttonBg.png"),
				ThreeDManipulation.class.getResource("calculator/button2.png"),
				ThreeDManipulation.class
						.getResource("calculator/highlightedbutton2.png"));
		buttonNode2.setLocalTranslation(
				((3 * horizontalSpace) + buttonWidth + (buttonWidth / 2))
						- (width / 2),
				((3 * verticalSpace) + buttonLength + (buttonLength / 2))
						- (length / 2), buttonZ);
		buttonNode2.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(String key) {
				textString += "2";
				dn.setText(textString);
				if (operator.equals("")) {
					leftNumber = leftNumber + "2";
				} else {
					rightNumber = rightNumber + "2";
				}

				logListener.keyPressed("2");
			}
		});
		this.attachChild(buttonNode2);

		buttonNode3 = new ButtonNode(
				"3" + name,
				buttonWidth / 2,
				buttonLength / 2,
				buttonHeight / 2,
				0.05f,
				ThreeDManipulation.class.getResource("calculator/buttonBg.png"),
				ThreeDManipulation.class.getResource("calculator/button3.png"),
				ThreeDManipulation.class
						.getResource("calculator/highlightedbutton3.png"));
		buttonNode3.setLocalTranslation(((4 * horizontalSpace)
				+ (2 * buttonWidth) + (buttonWidth / 2))
				- (width / 2),
				((3 * verticalSpace) + buttonLength + (buttonLength / 2))
						- (length / 2), buttonZ);
		buttonNode3.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(String key) {
				textString += "3";
				dn.setText(textString);
				if (operator.equals("")) {
					leftNumber = leftNumber + "3";
				} else {
					rightNumber = rightNumber + "3";
				}

				logListener.keyPressed("3");
			}
		});
		this.attachChild(buttonNode3);

		// row 3
		buttonNode4 = new ButtonNode(
				"4" + name,
				buttonWidth / 2,
				buttonLength / 2,
				buttonHeight / 2,
				0.05f,
				ThreeDManipulation.class.getResource("calculator/buttonBg.png"),
				ThreeDManipulation.class.getResource("calculator/button4.png"),
				ThreeDManipulation.class
						.getResource("calculator/highlightedbutton4.png"));
		buttonNode4.setLocalTranslation(
				((2 * horizontalSpace) + (buttonWidth / 2)) - (width / 2),
				((4 * verticalSpace) + (2 * buttonLength) + (buttonLength / 2))
						- (length / 2), buttonZ);
		buttonNode4.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(String key) {
				textString += "4";
				dn.setText(textString);
				if (operator.equals("")) {
					leftNumber = leftNumber + "4";
				} else {
					rightNumber = rightNumber + "4";
				}

				logListener.keyPressed("4");
			}
		});
		this.attachChild(buttonNode4);

		buttonNode5 = new ButtonNode(
				"5" + name,
				buttonWidth / 2,
				buttonLength / 2,
				buttonHeight / 2,
				0.05f,
				ThreeDManipulation.class.getResource("calculator/buttonBg.png"),
				ThreeDManipulation.class.getResource("calculator/button5.png"),
				ThreeDManipulation.class
						.getResource("calculator/highlightedbutton5.png"));
		buttonNode5.setLocalTranslation(
				((3 * horizontalSpace) + buttonWidth + (buttonWidth / 2))
						- (width / 2), ((4 * verticalSpace)
						+ (2 * buttonLength) + (buttonLength / 2))
						- (length / 2), buttonZ);
		buttonNode5.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(String key) {
				textString += "5";
				dn.setText(textString);
				if (operator.equals("")) {
					leftNumber = leftNumber + "5";
				} else {
					rightNumber = rightNumber + "5";
				}

				logListener.keyPressed("5");
			}
		});
		this.attachChild(buttonNode5);

		buttonNode6 = new ButtonNode(
				"6" + name,
				buttonWidth / 2,
				buttonLength / 2,
				buttonHeight / 2,
				0.05f,
				ThreeDManipulation.class.getResource("calculator/buttonBg.png"),
				ThreeDManipulation.class.getResource("calculator/button6.png"),
				ThreeDManipulation.class
						.getResource("calculator/highlightedbutton6.png"));
		buttonNode6.setLocalTranslation(((4 * horizontalSpace)
				+ (2 * buttonWidth) + (buttonWidth / 2))
				- (width / 2),
				((4 * verticalSpace) + (2 * buttonLength) + (buttonLength / 2))
						- (length / 2), buttonZ);
		buttonNode6.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(String key) {
				textString += "6";
				dn.setText(textString);
				if (operator.equals("")) {
					leftNumber = leftNumber + "6";
				} else {
					rightNumber = rightNumber + "6";
				}

				logListener.keyPressed("6");
			}
		});
		this.attachChild(buttonNode6);
		
		// row 4
		buttonNode7 = new ButtonNode(
				"7" + name,
				buttonWidth / 2,
				buttonLength / 2,
				buttonHeight / 2,
				0.05f,
				ThreeDManipulation.class.getResource("calculator/buttonBg.png"),
				ThreeDManipulation.class.getResource("calculator/button7.png"),
				ThreeDManipulation.class
						.getResource("calculator/highlightedbutton7.png"));
		buttonNode7.setLocalTranslation(
				((2 * horizontalSpace) + (buttonWidth / 2)) - (width / 2),
				((5 * verticalSpace) + (3 * buttonLength) + (buttonLength / 2))
						- (length / 2), buttonZ);
		buttonNode7.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(String key) {
				textString += "7";
				dn.setText(textString);
				if (operator.equals("")) {
					leftNumber = leftNumber + "7";
				} else {
					rightNumber = rightNumber + "7";
				}

				logListener.keyPressed("7");
			}
		});
		this.attachChild(buttonNode7);

		buttonNode8 = new ButtonNode(
				"8" + name,
				buttonWidth / 2,
				buttonLength / 2,
				buttonHeight / 2,
				0.05f,
				ThreeDManipulation.class.getResource("calculator/buttonBg.png"),
				ThreeDManipulation.class.getResource("calculator/button8.png"),
				ThreeDManipulation.class
						.getResource("calculator/highlightedbutton8.png"));
		buttonNode8.setLocalTranslation(
				((3 * horizontalSpace) + buttonWidth + (buttonWidth / 2))
						- (width / 2), ((5 * verticalSpace)
						+ (3 * buttonLength) + (buttonLength / 2))
						- (length / 2), buttonZ);
		buttonNode8.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(String key) {
				textString += "8";
				dn.setText(textString);
				if (operator.equals("")) {
					leftNumber = leftNumber + "8";
				} else {
					rightNumber = rightNumber + "8";
				}

				logListener.keyPressed("8");
			}
		});
		this.attachChild(buttonNode8);

		buttonNode9 = new ButtonNode(
				"9" + name,
				buttonWidth / 2,
				buttonLength / 2,
				buttonHeight / 2,
				0.05f,
				ThreeDManipulation.class.getResource("calculator/buttonBg.png"),
				ThreeDManipulation.class.getResource("calculator/button9.png"),
				ThreeDManipulation.class
						.getResource("calculator/highlightedbutton9.png"));
		buttonNode9.setLocalTranslation(((4 * horizontalSpace)
				+ (2 * buttonWidth) + (buttonWidth / 2))
				- (width / 2),
				((5 * verticalSpace) + (3 * buttonLength) + (buttonLength / 2))
						- (length / 2), buttonZ);
		buttonNode9.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(String key) {
				textString += "9";
				dn.setText(textString);
				if (operator.equals("")) {
					leftNumber = leftNumber + "9";
				} else {
					rightNumber = rightNumber + "9";
				}

				logListener.keyPressed("9");
			}
		});
		this.attachChild(buttonNode9);
		
		buttonNodeDelete = new ButtonNode(
				"remove" + name,
				buttonWidth / 2,
				buttonLength / 2,
				buttonHeight / 2,
				0.05f,
				ThreeDManipulation.class.getResource("calculator/buttonBg.png"),
				ThreeDManipulation.class
						.getResource("calculator/buttondelete.png"),
				ThreeDManipulation.class
						.getResource("calculator/highlightedbuttondelete.png"));
		buttonNodeDelete.setLocalTranslation(((5 * horizontalSpace)
				+ (3 * buttonWidth) + (buttonWidth / 2))
				- (width / 2),
				((5 * verticalSpace) + (3 * buttonLength) + (buttonLength / 2))
						- (length / 2), buttonZ);
		buttonNodeDelete.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(String key) {
				if (textString.length() <= 0) {
					return;
				}
				textString = textString.substring(0, textString.length() - 1);
				dn.setText(textString);
				
				logListener.deleteKeyPressed();
			}
			
		});
		this.attachChild(buttonNodeDelete);

		buttonNodePlus = new ButtonNode(
				"+" + name,
				buttonWidth / 2,
				buttonLength / 2,
				buttonHeight / 2,
				0.05f,
				ThreeDManipulation.class.getResource("calculator/buttonBg.png"),
				ThreeDManipulation.class
						.getResource("calculator/buttonplus.png"),
				ThreeDManipulation.class
						.getResource("calculator/highlightedbuttonplus.png"));
		buttonNodePlus.setLocalTranslation(((5 * horizontalSpace)
				+ (3 * buttonWidth) + (buttonWidth / 2))
				- (width / 2),
				((4 * verticalSpace) + (2 * buttonLength) + (buttonLength / 2))
						- (length / 2), buttonZ);
		buttonNodePlus.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(String key) {
				textString += "+";
				dn.setText(textString);
				operator = "+";

			}
			
		});
		this.attachChild(buttonNodePlus);

		buttonNodeSubstract = new ButtonNode(
				"-" + name,
				buttonWidth / 2,
				buttonLength / 2,
				buttonHeight / 2,
				0.05f,
				ThreeDManipulation.class.getResource("calculator/buttonBg.png"),
				ThreeDManipulation.class
						.getResource("calculator/buttonsubstract.png"),
				ThreeDManipulation.class
						.getResource("calculator/highlightedbuttonsubstract.png"));
		buttonNodeSubstract.setLocalTranslation(((5 * horizontalSpace)
				+ (3 * buttonWidth) + (buttonWidth / 2))
				- (width / 2),
				((3 * verticalSpace) + buttonLength + (buttonLength / 2))
						- (length / 2), buttonZ);
		buttonNodeSubstract.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(String key) {
				textString += "-";
				dn.setText(textString);
				if (!leftNumber.equals("")) {
					operator = "-";
				} else {
					leftNumber = leftNumber + "-";
				}
			}
		});
		this.attachChild(buttonNodeSubstract);

		buttonNodeResult = new ButtonNode(
				"Submit" + name,
				buttonWidth + (horizontalSpace / 2),
				buttonLength / 2,
				buttonHeight / 2,
				0.05f,
				ThreeDManipulation.class.getResource("calculator/buttonBg.png"),
				ThreeDManipulation.class.getResource("calculator/buttonOK.png"),
				ThreeDManipulation.class
						.getResource("calculator/highlightedbuttonOK.png"));
		buttonNodeResult.setLocalTranslation(((4 * horizontalSpace)
				+ (2.58f * buttonWidth) + (buttonWidth / 2))
				- (width / 2), ((2 * verticalSpace) + (buttonLength / 2))
				- (length / 2), buttonZ);
		buttonNodeResult.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(String key) {
				textString = "";
				dn.setText(textString);

				logListener.submitKeyPressed();

				logListener = null;

				if (currentTrailNumber < (trailCount - 1)) {
					startNewTrail();
					currentTrailNumber++;
				} else {
					for (TaskListener l : taskListeners) {
						l.taskCompleted();
					}
				}
			}
		});
		this.attachChild(buttonNodeResult);
		
		/**
		 * ButtonNode buttonNodePlus = new ButtonNode("+"+name,
		 * buttonWidth/2,buttonLength/2, buttonHeight/2, 0.05f,
		 * ThreeDManipulation.class.getResource("calculator/buttonBg.png"),
		 * ThreeDManipulation.class.getResource("calculator/buttonplus.png"),
		 * ThreeDManipulation
		 * .class.getResource("calculator/highlightedbuttonplus.png"));
		 * buttonNodePlus
		 * .setFeedbackMode(ButtonNode.FEEDBACK_MODE_COLORHIGHLIGHTED);
		 * buttonNodePlus
		 * .setLocalTranslation(5*horizontalSpace+3*buttonWidth+buttonWidth
		 * /2-width/2, 5*verticalSpace+3*buttonLength+buttonLength/2-length/2,
		 * buttonZ); buttonNodePlus.addKeyListener(new KeyListener(){
		 * 
		 * @Override public void keyPressed(String key) { textString+="+";
		 *           dn.setText(textString); operator = "+";
		 *           logListener.deleteKeyPressed(); } });
		 *           this.attachChild(buttonNodePlus); ButtonNode
		 *           buttonNodeSubstract = new ButtonNode("-"+name,
		 *           buttonWidth/2,buttonLength/2, buttonHeight/2, 0.05f,
		 *           ThreeDManipulation
		 *           .class.getResource("calculator/buttonBg.png"),
		 *           ThreeDManipulation
		 *           .class.getResource("calculator/buttonsubstract.png"),
		 *           ThreeDManipulation.class.getResource(
		 *           "calculator/highlightedbuttonsubstract.png"));
		 *           buttonNodeSubstract
		 *           .setFeedbackMode(ButtonNode.FEEDBACK_MODE_COLORHIGHLIGHTED
		 *           );
		 *           buttonNodeSubstract.setLocalTranslation(5*horizontalSpace
		 *           +3*buttonWidth+buttonWidth/2-width/2,
		 *           4*verticalSpace+2*buttonLength+buttonLength/2-length/2,
		 *           buttonZ); buttonNodeSubstract.addKeyListener(new
		 *           KeyListener(){
		 * @Override public void keyPressed(String key) { textString+="-";
		 *           dn.setText(textString); if (!leftNumber.equals(""))
		 *           operator = "-"; else { leftNumber = leftNumber+"-"; } } });
		 *           this.attachChild(buttonNodeSubstract); ButtonNode
		 *           buttonNodeResult = new ButtonNode("="+name,
		 *           buttonWidth/2,buttonLength/2, buttonHeight/2, 0.05f,
		 *           ThreeDManipulation
		 *           .class.getResource("calculator/buttonBg.png"),
		 *           ThreeDManipulation
		 *           .class.getResource("calculator/buttonresult.png"),
		 *           ThreeDManipulation.class.getResource(
		 *           "calculator/highlightedbuttonresult.png"));
		 *           buttonNodeResult
		 *           .setFeedbackMode(ButtonNode.FEEDBACK_MODE_COLORHIGHLIGHTED
		 *           );
		 *           buttonNodeResult.setLocalTranslation(4*horizontalSpace+2*
		 *           buttonWidth+buttonWidth/2-width/2,
		 *           2*verticalSpace+buttonLength/2-length/2, buttonZ);
		 *           buttonNodeResult.addKeyListener(new KeyListener(){
		 * @Override public void keyPressed(String key) {
		 *           logListener.submitKeyPressed(); } });
		 *           this.attachChild(buttonNodeResult); ButtonNode
		 *           buttonNodeMultiply = new ButtonNode("*"+name,
		 *           buttonWidth/2,buttonLength/2, buttonHeight/2, 0.05f,
		 *           ThreeDManipulation
		 *           .class.getResource("calculator/buttonBg.png"),
		 *           ThreeDManipulation
		 *           .class.getResource("calculator/buttonmultiply.png"),
		 *           ThreeDManipulation.class.getResource(
		 *           "calculator/highlightedbuttonmulitply.png"));
		 *           buttonNodeMultiply
		 *           .setFeedbackMode(ButtonNode.FEEDBACK_MODE_COLORHIGHLIGHTED
		 *           ); buttonNodeMultiply
		 *           .setLocalTranslation(5*horizontalSpace
		 *           +3*buttonWidth+buttonWidth/2-width/2,
		 *           3*verticalSpace+buttonLength+buttonLength/2-length/2,
		 *           buttonZ); buttonNodeMultiply.addKeyListener(new
		 *           KeyListener(){
		 * @Override public void keyPressed(String key) { textString+="*";
		 *           dn.setText(textString); operator = "*"; } });
		 *           this.attachChild(buttonNodeMultiply); ButtonNode
		 *           buttonNodeDivide = new ButtonNode("/"+name,
		 *           buttonWidth/2,buttonLength/2, buttonHeight/2, 0.05f,
		 *           ThreeDManipulation
		 *           .class.getResource("calculator/buttondivide.png"),
		 *           ThreeDManipulation.class.getResource(
		 *           "calculator/highlightedbuttondivide.png"));
		 *           buttonNodeDivide
		 *           .setFeedbackMode(ButtonNode.FEEDBACK_MODE_COLORHIGHLIGHTED
		 *           );
		 *           buttonNodeDivide.setLocalTranslation(5*horizontalSpace+3*
		 *           buttonWidth+buttonWidth/2-width/2,
		 *           2*verticalSpace+buttonLength/2-length/2, buttonZ);
		 *           buttonNodeDivide.addKeyListener(new KeyListener(){
		 * @Override public void keyPressed(String key) { textString+="/";
		 *           dn.setText(textString); operator = "/"; logListener = new
		 *           TrialLog("123456", new Date().getTime()); } });
		 *           this.attachChild(buttonNodeDivide);
		 */

		ControlPointRotateTranslateScale cprts = new ControlPointRotateTranslateScale(
				dn.getDisplayQuad(), this);
		cprts.setPickMeOnly(true);

	}

	/**
	 * Reset task.
	 */
	public void resetTask() {
		currentTrailNumber = 0;
		startNewTrail();
	}
	
	/**
	 * Sets the button property.
	 */
	protected abstract void setButtonProperty();
	
	/**
	 * Sets the calculator property.
	 */
	protected void setCalculatorProperty() {
	}
	
	/**
	 * Sets the current trail number.
	 *
	 * @param currentTrailNumber
	 *            the new current trail number
	 */
	public void setCurrentTrailNumber(int currentTrailNumber) {
		this.currentTrailNumber = currentTrailNumber;
	}
	
	/**
	 * Sets the trail count.
	 *
	 * @param trailCount
	 *            the new trail count
	 */
	public void setTrailCount(int trailCount) {
		this.trailCount = trailCount;
	}

	/**
	 * Start new trail.
	 */
	public void startNewTrail() {
		textString = "";
		displayNode.setText(textString);
		String targetNumber = StringGenerator.generateNumber(6);
		logListener = new TrialLog(targetNumber, new Date().getTime(),
				logWritter);
		targetNumberLabel.setText(targetNumber);
	}

}
