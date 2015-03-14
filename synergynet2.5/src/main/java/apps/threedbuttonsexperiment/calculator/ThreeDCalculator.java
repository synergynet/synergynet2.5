package apps.threedbuttonsexperiment.calculator;

import apps.threedbuttonsexperiment.calculator.button.ButtonNode;
import apps.threedbuttonsexperiment.logger.DateTextWritter;
import apps.threedmanipulation.ThreeDManipulation;

import com.jme.math.FastMath;

import synergynetframework.appsystem.contentsystem.items.TextLabel;

public class ThreeDCalculator extends Calculator {

	private static final long serialVersionUID = 2429175967783608868L;
	
	public ThreeDCalculator(String name, TextLabel targetNumberLabel, DateTextWritter logWritter) {
		super(name, targetNumberLabel, logWritter);
	}

	@Override
	protected void setButtonProperty() {
		this.buttonNode0.setFeedbackMode(ButtonNode.FEEDBACK_MODE_3D);
		this.buttonNode1.setFeedbackMode(ButtonNode.FEEDBACK_MODE_3D);
		this.buttonNode2.setFeedbackMode(ButtonNode.FEEDBACK_MODE_3D);
		this.buttonNode3.setFeedbackMode(ButtonNode.FEEDBACK_MODE_3D);
		this.buttonNode4.setFeedbackMode(ButtonNode.FEEDBACK_MODE_3D);
		this.buttonNode5.setFeedbackMode(ButtonNode.FEEDBACK_MODE_3D);
		this.buttonNode6.setFeedbackMode(ButtonNode.FEEDBACK_MODE_3D);
		this.buttonNode7.setFeedbackMode(ButtonNode.FEEDBACK_MODE_3D);
		this.buttonNode8.setFeedbackMode(ButtonNode.FEEDBACK_MODE_3D);
		this.buttonNode9.setFeedbackMode(ButtonNode.FEEDBACK_MODE_3D);
		this.buttonNodeDelete.setFeedbackMode(ButtonNode.FEEDBACK_MODE_3D);
		this.buttonNodePlus.setFeedbackMode(ButtonNode.FEEDBACK_MODE_3D);
		this.buttonNodeSubstract.setFeedbackMode(ButtonNode.FEEDBACK_MODE_3D);
		this.buttonNodeResult.setFeedbackMode(ButtonNode.FEEDBACK_MODE_3D);
		this.buttonNodeDot.setFeedbackMode(ButtonNode.FEEDBACK_MODE_3D);
		
	}
	
	protected void setCalculatorProperty(){
		buttonZOffset=2f;
		angle=FastMath.PI/12f;
		skinURL= ThreeDManipulation.class.getResource("/calculator/body.png");
	}

}