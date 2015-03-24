package apps.threedbuttonsexperiment.calculator;

import apps.threedbuttonsexperiment.calculator.button.ButtonNode;
import apps.threedbuttonsexperiment.logger.DateTextWritter;
import apps.threedmanipulation.ThreeDManipulation;

import com.jme.math.FastMath;

import synergynetframework.appsystem.contentsystem.items.TextLabel;


/**
 * The Class ThreeDCalculator.
 */
public class ThreeDCalculator extends Calculator {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2429175967783608868L;
	
	/**
	 * Instantiates a new three d calculator.
	 *
	 * @param name the name
	 * @param targetNumberLabel the target number label
	 * @param logWritter the log writter
	 */
	public ThreeDCalculator(String name, TextLabel targetNumberLabel, DateTextWritter logWritter) {
		super(name, targetNumberLabel, logWritter);
	}

	/* (non-Javadoc)
	 * @see apps.threedbuttonsexperiment.calculator.Calculator#setButtonProperty()
	 */
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
	
	/* (non-Javadoc)
	 * @see apps.threedbuttonsexperiment.calculator.Calculator#setCalculatorProperty()
	 */
	protected void setCalculatorProperty(){
		buttonZOffset=2f;
		angle=FastMath.PI/12f;
		skinURL= ThreeDManipulation.class.getResource("/calculator/body.png");
	}

}