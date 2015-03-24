package apps.threedinteraction.calculator;

import synergynetframework.jme.cursorsystem.elements.threed.ControlPointRotateTranslateScale;
import apps.threedinteraction.button.ButtonNode;
import apps.threedinteraction.button.KeyListener;
import apps.threedmanipulation.ThreeDManipulation;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;

/**
 * The Class CalculatorNode.
 */
public class CalculatorNode extends Node {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2429175967783608868L;

	/** The display node. */
	protected DisplayNode displayNode;

	/** The left number. */
	protected String leftNumber = "";

	/** The operator. */
	protected String operator = "";

	/** The right number. */
	protected String rightNumber = "";

	/** The text string. */
	protected String textString = "";

	/**
	 * Instantiates a new calculator node.
	 *
	 * @param name
	 *            the name
	 */
	public CalculatorNode(String name) {
		super(name);
		init();

	}
	
	/**
	 * Clear.
	 */
	private void clear() {
		textString = "";
		displayNode.setText(textString);

		leftNumber = textString;
		rightNumber = "";
		operator = "";
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
				width / 2, length / 2, height / 2, 0.1f,
				ThreeDManipulation.class.getResource("calculator/body.png"));
		this.attachChild(bn);
		
		float buttonZ = bn.getLocalTranslation().z + height;
		float horizontalSpace = (width - (buttonWidth * 4)) / 7;
		float verticalSpace = (length - (buttonLength * 1.5f) - (buttonLength * 4f)) / 9f;

		final DisplayNode dn = new DisplayNode("Display" + name,
				(width * 0.9f) / 2, (length * 7) / 60, height * 1.5f, 0.1f,
				ThreeDManipulation.class.getResource("calculator/body.png"));
		dn.setLocalTranslation(
				0,
				((8.5f * verticalSpace) + (4 * buttonLength) + (buttonLength / 2))
						- (length / 2), bn.getLocalTranslation().z + 0.5f);
		this.attachChild(dn);
		displayNode = dn;

		Quaternion tq = new Quaternion();
		tq.fromAngleAxis(FastMath.PI / 12f, new Vector3f(1, 0, 0));
		dn.setLocalRotation(tq);
		dn.updateGeometricState(0f, false);

		// row 1
		ButtonNode buttonNode0 = new ButtonNode(
				"0" + name,
				buttonWidth / 2,
				buttonLength / 2,
				buttonHeight / 2,
				0.05f,
				ThreeDManipulation.class.getResource("calculator/buttonBg.png"),
				ThreeDManipulation.class.getResource("calculator/button0.png"));
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
			}
		});
		this.attachChild(buttonNode0);

		ButtonNode buttonNodeDot = new ButtonNode(
				"." + name,
				buttonWidth / 2,
				buttonLength / 2,
				buttonHeight / 2,
				0.05f,
				ThreeDManipulation.class.getResource("calculator/buttonBg.png"),
				ThreeDManipulation.class
						.getResource("calculator/buttondot.png"));
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
			}
		});
		this.attachChild(buttonNodeDot);

		ButtonNode buttonNodeResult = new ButtonNode(
				"=" + name,
				buttonWidth / 2,
				buttonLength / 2,
				buttonHeight / 2,
				0.05f,
				ThreeDManipulation.class.getResource("calculator/buttonBg.png"),
				ThreeDManipulation.class
						.getResource("calculator/buttonresult.png"));
		buttonNodeResult.setLocalTranslation(((4 * horizontalSpace)
				+ (2 * buttonWidth) + (buttonWidth / 2))
				- (width / 2), ((2 * verticalSpace) + (buttonLength / 2))
				- (length / 2), buttonZ);
		buttonNodeResult.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(String key) {
				if (leftNumber.equals("")) {
					clear();
					return;
				}
				if (rightNumber.equals("")) {
					clear();
					return;
				}
				if (operator.equals("")) {
					clear();
					return;
				}

				double left = 0;
				double right = 0;
				double result = 0;

				if (isFloat(leftNumber) || isInteger(leftNumber)) {
					left = Double.parseDouble(leftNumber);
				} else {
					clear();
					return;
				}

				if (isFloat(rightNumber) || isInteger(rightNumber)) {
					right = Double.parseDouble(rightNumber);
				} else {
					clear();
					return;
				}

				if (operator.equals("+")) {
					result = left + right;
				} else if (operator.equals("-")) {
					
					result = left - right;
				} else if (operator.equals("*")) {
					result = left * right;
				} else if (operator.equals("/")) {
					if (right == 0) {
						clear();
						return;
					}
					result = left / right;
				}

				result = Math.round(result * 100);
				result = result / 100;

				clear();

				textString = "" + result;
				leftNumber = textString;
				dn.setText(textString);
				
			}
		});
		this.attachChild(buttonNodeResult);

		ButtonNode buttonNodeDivide = new ButtonNode(
				"/" + name,
				buttonWidth / 2,
				buttonLength / 2,
				buttonHeight / 2,
				0.05f,
				ThreeDManipulation.class.getResource("calculator/buttonBg.png"),
				ThreeDManipulation.class
						.getResource("calculator/buttondivide.png"));
		buttonNodeDivide.setLocalTranslation(((5 * horizontalSpace)
				+ (3 * buttonWidth) + (buttonWidth / 2))
				- (width / 2), ((2 * verticalSpace) + (buttonLength / 2))
				- (length / 2), buttonZ);
		buttonNodeDivide.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(String key) {
				textString += "/";
				dn.setText(textString);
				operator = "/";

			}
		});
		this.attachChild(buttonNodeDivide);

		// row 2

		ButtonNode buttonNode1 = new ButtonNode(
				"1" + name,
				buttonWidth / 2,
				buttonLength / 2,
				buttonHeight / 2,
				0.05f,
				ThreeDManipulation.class.getResource("calculator/buttonBg.png"),
				ThreeDManipulation.class.getResource("calculator/button1.png"));
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
			}
		});
		this.attachChild(buttonNode1);

		ButtonNode buttonNode2 = new ButtonNode(
				"2" + name,
				buttonWidth / 2,
				buttonLength / 2,
				buttonHeight / 2,
				0.05f,
				ThreeDManipulation.class.getResource("calculator/buttonBg.png"),
				ThreeDManipulation.class.getResource("calculator/button2.png"));
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
			}
		});
		this.attachChild(buttonNode2);

		ButtonNode buttonNode3 = new ButtonNode(
				"3" + name,
				buttonWidth / 2,
				buttonLength / 2,
				buttonHeight / 2,
				0.05f,
				ThreeDManipulation.class.getResource("calculator/buttonBg.png"),
				ThreeDManipulation.class.getResource("calculator/button3.png"));
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
			}
		});
		this.attachChild(buttonNode3);

		ButtonNode buttonNodeMultiply = new ButtonNode(
				"*" + name,
				buttonWidth / 2,
				buttonLength / 2,
				buttonHeight / 2,
				0.05f,
				ThreeDManipulation.class.getResource("calculator/buttonBg.png"),
				ThreeDManipulation.class
						.getResource("calculator/buttonmultiply.png"));
		buttonNodeMultiply.setLocalTranslation(((5 * horizontalSpace)
				+ (3 * buttonWidth) + (buttonWidth / 2))
				- (width / 2),
				((3 * verticalSpace) + buttonLength + (buttonLength / 2))
						- (length / 2), buttonZ);
		buttonNodeMultiply.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(String key) {
				textString += "*";
				dn.setText(textString);
				operator = "*";
			}
		});
		this.attachChild(buttonNodeMultiply);

		// row 3

		ButtonNode buttonNode4 = new ButtonNode(
				"4" + name,
				buttonWidth / 2,
				buttonLength / 2,
				buttonHeight / 2,
				0.05f,
				ThreeDManipulation.class.getResource("calculator/buttonBg.png"),
				ThreeDManipulation.class.getResource("calculator/button4.png"));
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
			}
		});
		this.attachChild(buttonNode4);

		ButtonNode buttonNode5 = new ButtonNode(
				"5" + name,
				buttonWidth / 2,
				buttonLength / 2,
				buttonHeight / 2,
				0.05f,
				ThreeDManipulation.class.getResource("calculator/buttonBg.png"),
				ThreeDManipulation.class.getResource("calculator/button5.png"));
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
			}
		});
		this.attachChild(buttonNode5);

		ButtonNode buttonNode6 = new ButtonNode(
				"6" + name,
				buttonWidth / 2,
				buttonLength / 2,
				buttonHeight / 2,
				0.05f,
				ThreeDManipulation.class.getResource("calculator/buttonBg.png"),
				ThreeDManipulation.class.getResource("calculator/button6.png"));
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
			}
		});
		this.attachChild(buttonNode6);

		ButtonNode buttonNodeSubstract = new ButtonNode(
				"-" + name,
				buttonWidth / 2,
				buttonLength / 2,
				buttonHeight / 2,
				0.05f,
				ThreeDManipulation.class.getResource("calculator/buttonBg.png"),
				ThreeDManipulation.class
						.getResource("calculator/buttonsubstract.png"));
		buttonNodeSubstract.setLocalTranslation(((5 * horizontalSpace)
				+ (3 * buttonWidth) + (buttonWidth / 2))
				- (width / 2),
				((4 * verticalSpace) + (2 * buttonLength) + (buttonLength / 2))
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

		// row 4

		ButtonNode buttonNode7 = new ButtonNode(
				"7" + name,
				buttonWidth / 2,
				buttonLength / 2,
				buttonHeight / 2,
				0.05f,
				ThreeDManipulation.class.getResource("calculator/buttonBg.png"),
				ThreeDManipulation.class.getResource("calculator/button7.png"));
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
			}
		});
		this.attachChild(buttonNode7);

		ButtonNode buttonNode8 = new ButtonNode(
				"8" + name,
				buttonWidth / 2,
				buttonLength / 2,
				buttonHeight / 2,
				0.05f,
				ThreeDManipulation.class.getResource("calculator/buttonBg.png"),
				ThreeDManipulation.class.getResource("calculator/button8.png"));
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
			}
		});
		this.attachChild(buttonNode8);

		ButtonNode buttonNode9 = new ButtonNode(
				"9" + name,
				buttonWidth / 2,
				buttonLength / 2,
				buttonHeight / 2,
				0.05f,
				ThreeDManipulation.class.getResource("calculator/buttonBg.png"),
				ThreeDManipulation.class.getResource("calculator/button9.png"));
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
			}
		});
		this.attachChild(buttonNode9);

		ButtonNode buttonNodePlus = new ButtonNode(
				"+" + name,
				buttonWidth / 2,
				buttonLength / 2,
				buttonHeight / 2,
				0.05f,
				ThreeDManipulation.class.getResource("calculator/buttonBg.png"),
				ThreeDManipulation.class
						.getResource("calculator/buttonplus.png"));
		buttonNodePlus.setLocalTranslation(((5 * horizontalSpace)
				+ (3 * buttonWidth) + (buttonWidth / 2))
				- (width / 2),
				((5 * verticalSpace) + (3 * buttonLength) + (buttonLength / 2))
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
		
		ControlPointRotateTranslateScale cprts = new ControlPointRotateTranslateScale(
				dn.getDisplayQuad(), this);
		cprts.setPickMeOnly(true);

	}

	/**
	 * Checks if is float.
	 *
	 * @param number
	 *            the number
	 * @return true, if is float
	 */
	private boolean isFloat(String number) {
		try {
			Double.parseDouble(number);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * Checks if is integer.
	 *
	 * @param number
	 *            the number
	 * @return true, if is integer
	 */
	private boolean isInteger(String number) {
		try {
			Integer.parseInt(number);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

}
