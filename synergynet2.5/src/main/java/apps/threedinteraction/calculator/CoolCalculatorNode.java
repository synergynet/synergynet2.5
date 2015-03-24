package apps.threedinteraction.calculator;

import synergynetframework.jme.cursorsystem.elements.threed.ControlPointRotateTranslateScale;
import apps.threedinteraction.button.ButtonNode;
import apps.threedinteraction.button.KeyListener;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;

import data.DataResources;

/**
 * The Class CoolCalculatorNode.
 */
public class CoolCalculatorNode extends Node {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2429175967783608868L;

	/** The text string. */
	protected String textString = "";

	/**
	 * Instantiates a new cool calculator node.
	 *
	 * @param name
	 *            the name
	 */
	public CoolCalculatorNode(String name) {
		super(name);
		init();

	}
	
	/**
	 * Inits the.
	 */
	protected void init() {

		float width = 20f;
		float length = 30f;
		float height = 1f;

		float buttonWidth = 4f;
		float buttonLength = 4f;
		float buttonHeight = 1f;
		
		ControllerBodyNode bn = new ControllerBodyNode("Body Node" + this.name,
				width / 2, length / 2, height / 2, 0.1f,
				DataResources.class
						.getResource("diamondranking/imagetest/image/body.png"));
		this.attachChild(bn);
		bn.setCullHint(CullHint.Always);
		
		float buttonZ = bn.getLocalTranslation().z + ((height * 3) / 2);
		float horizontalSpace = (width - (buttonWidth * 4)) / 7;
		float verticalSpace = (length - (buttonLength * 1.5f) - (buttonLength * 4f)) / 9f;

		final DisplayNode dn = new DisplayNode("Display", (width * 0.9f) / 2,
				(length * 7) / 60, height * 1.5f, 0.1f,
				DataResources.class
						.getResource("diamondranking/imagetest/image/body.png"));
		dn.setLocalTranslation(
				0,
				((8.5f * verticalSpace) + (4 * buttonLength) + (buttonLength / 2))
						- (length / 2), bn.getLocalTranslation().z);
		this.attachChild(dn);
		dn.showCoolMode();

		Quaternion tq = new Quaternion();
		tq.fromAngleAxis(FastMath.PI / 8f, new Vector3f(1, 0, 0));
		dn.setLocalRotation(tq);
		dn.updateGeometricState(0f, false);

		// row 1
		ButtonNode buttonNode0 = new ButtonNode(
				"0" + name,
				buttonWidth / 2,
				buttonLength / 2,
				buttonHeight / 2,
				0.05f,
				DataResources.class
						.getResource("diamondranking/imagetest/image/buttonBg.png"),
				DataResources.class
						.getResource("diamondranking/imagetest/image/button0.png"));
		buttonNode0.setLocalTranslation(
				((2 * horizontalSpace) + (buttonWidth / 2)) - (width / 2),
				((2 * verticalSpace) + (buttonLength / 2)) - (length / 2),
				buttonZ);
		buttonNode0.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(String key) {
				textString += "0";
				dn.setText(textString);
			}
		});
		this.attachChild(buttonNode0);

		ButtonNode buttonNodeDot = new ButtonNode(
				"." + name,
				buttonWidth / 2,
				buttonLength / 2,
				buttonHeight / 2,
				0.05f,
				DataResources.class
						.getResource("diamondranking/imagetest/image/buttonBg.png"),
				DataResources.class
						.getResource("diamondranking/imagetest/image/buttondot.png"));
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
			}
		});
		this.attachChild(buttonNodeDot);

		ButtonNode buttonNodeResult = new ButtonNode(
				"=" + name,
				buttonWidth / 2,
				buttonLength / 2,
				buttonHeight / 2,
				0.05f,
				DataResources.class
						.getResource("diamondranking/imagetest/image/buttonBg.png"),
				DataResources.class
						.getResource("diamondranking/imagetest/image/buttonresult.png"));
		buttonNodeResult.setLocalTranslation(((4 * horizontalSpace)
				+ (2 * buttonWidth) + (buttonWidth / 2))
				- (width / 2), ((2 * verticalSpace) + (buttonLength / 2))
				- (length / 2), buttonZ);
		buttonNodeResult.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(String key) {
				textString = "";
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
				DataResources.class
						.getResource("diamondranking/imagetest/image/buttonBg.png"),
				DataResources.class
						.getResource("diamondranking/imagetest/image/buttondivide.png"));
		buttonNodeDivide.setLocalTranslation(((5 * horizontalSpace)
				+ (3 * buttonWidth) + (buttonWidth / 2))
				- (width / 2), ((2 * verticalSpace) + (buttonLength / 2))
				- (length / 2), buttonZ);
		buttonNodeDivide.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(String key) {
				textString += "/";
				dn.setText(textString);
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
				DataResources.class
						.getResource("diamondranking/imagetest/image/buttonBg.png"),
				DataResources.class
						.getResource("diamondranking/imagetest/image/button1.png"));
		buttonNode1.setLocalTranslation(
				((2 * horizontalSpace) + (buttonWidth / 2)) - (width / 2),
				((3 * verticalSpace) + buttonLength + (buttonLength / 2))
						- (length / 2), buttonZ);
		buttonNode1.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(String key) {
				textString += "1";
				dn.setText(textString);
			}
		});
		this.attachChild(buttonNode1);

		ButtonNode buttonNode2 = new ButtonNode(
				"2" + name,
				buttonWidth / 2,
				buttonLength / 2,
				buttonHeight / 2,
				0.05f,
				DataResources.class
						.getResource("diamondranking/imagetest/image/buttonBg.png"),
				DataResources.class
						.getResource("diamondranking/imagetest/image/button2.png"));
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
			}
		});
		this.attachChild(buttonNode2);

		ButtonNode buttonNode3 = new ButtonNode(
				"3" + name,
				buttonWidth / 2,
				buttonLength / 2,
				buttonHeight / 2,
				0.05f,
				DataResources.class
						.getResource("diamondranking/imagetest/image/buttonBg.png"),
				DataResources.class
						.getResource("diamondranking/imagetest/image/button3.png"));
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
			}
		});
		this.attachChild(buttonNode3);

		ButtonNode buttonNodeMultiply = new ButtonNode(
				"*" + name,
				buttonWidth / 2,
				buttonLength / 2,
				buttonHeight / 2,
				0.05f,
				DataResources.class
						.getResource("diamondranking/imagetest/image/buttonBg.png"),
				DataResources.class
						.getResource("diamondranking/imagetest/image/buttonmultiply.png"));
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
				DataResources.class
						.getResource("diamondranking/imagetest/image/buttonBg.png"),
				DataResources.class
						.getResource("diamondranking/imagetest/image/button4.png"));
		buttonNode4.setLocalTranslation(
				((2 * horizontalSpace) + (buttonWidth / 2)) - (width / 2),
				((4 * verticalSpace) + (2 * buttonLength) + (buttonLength / 2))
						- (length / 2), buttonZ);
		buttonNode4.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(String key) {
				textString += "4";
				dn.setText(textString);
			}
		});
		this.attachChild(buttonNode4);

		ButtonNode buttonNode5 = new ButtonNode(
				"5" + name,
				buttonWidth / 2,
				buttonLength / 2,
				buttonHeight / 2,
				0.05f,
				DataResources.class
						.getResource("diamondranking/imagetest/image/buttonBg.png"),
				DataResources.class
						.getResource("diamondranking/imagetest/image/button5.png"));
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
			}
		});
		this.attachChild(buttonNode5);

		ButtonNode buttonNode6 = new ButtonNode(
				"6" + name,
				buttonWidth / 2,
				buttonLength / 2,
				buttonHeight / 2,
				0.05f,
				DataResources.class
						.getResource("diamondranking/imagetest/image/buttonBg.png"),
				DataResources.class
						.getResource("diamondranking/imagetest/image/button6.png"));
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
			}
		});
		this.attachChild(buttonNode6);

		ButtonNode buttonNodeSubstract = new ButtonNode(
				"-" + name,
				buttonWidth / 2,
				buttonLength / 2,
				buttonHeight / 2,
				0.05f,
				DataResources.class
						.getResource("diamondranking/imagetest/image/buttonBg.png"),
				DataResources.class
						.getResource("diamondranking/imagetest/image/buttonsubstract.png"));
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
				DataResources.class
						.getResource("diamondranking/imagetest/image/buttonBg.png"),
				DataResources.class
						.getResource("diamondranking/imagetest/image/button7.png"));
		buttonNode7.setLocalTranslation(
				((2 * horizontalSpace) + (buttonWidth / 2)) - (width / 2),
				((5 * verticalSpace) + (3 * buttonLength) + (buttonLength / 2))
						- (length / 2), buttonZ);
		buttonNode7.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(String key) {
				textString += "7";
				dn.setText(textString);
			}
		});
		this.attachChild(buttonNode7);

		ButtonNode buttonNode8 = new ButtonNode(
				"8" + name,
				buttonWidth / 2,
				buttonLength / 2,
				buttonHeight / 2,
				0.05f,
				DataResources.class
						.getResource("diamondranking/imagetest/image/buttonBg.png"),
				DataResources.class
						.getResource("diamondranking/imagetest/image/button8.png"));
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
			}
		});
		this.attachChild(buttonNode8);

		ButtonNode buttonNode9 = new ButtonNode(
				"9" + name,
				buttonWidth / 2,
				buttonLength / 2,
				buttonHeight / 2,
				0.05f,
				DataResources.class
						.getResource("diamondranking/imagetest/image/buttonBg.png"),
				DataResources.class
						.getResource("diamondranking/imagetest/image/button9.png"));
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
			}
		});
		this.attachChild(buttonNode9);

		ButtonNode buttonNodePlus = new ButtonNode(
				"+" + name,
				buttonWidth / 2,
				buttonLength / 2,
				buttonHeight / 2,
				0.05f,
				DataResources.class
						.getResource("diamondranking/imagetest/image/buttonBg.png"),
				DataResources.class
						.getResource("diamondranking/imagetest/image/buttonplus.png"));
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
			}
		});
		this.attachChild(buttonNodePlus);
		
		ControlPointRotateTranslateScale cprts = new ControlPointRotateTranslateScale(
				dn.getDisplayQuad(), this);
		cprts.setPickMeOnly(true);

	}

}
