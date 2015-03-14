package apps.threedbuttonsexperiment.calculator;

import apps.threedbuttonsexperiment.calculator.button.ButtonNode;
import apps.threedbuttonsexperiment.logger.DateTextWritter;
import synergynetframework.appsystem.contentsystem.items.TextLabel;

public class ColorHighlightedCalculator extends Calculator {

	private static final long serialVersionUID = 2429175967783608868L;
	
	public ColorHighlightedCalculator(String name, TextLabel targetNumberLabel, DateTextWritter logWritter) {
		super(name, targetNumberLabel, logWritter);
	}

	@Override
	protected void setButtonProperty() {
		this.buttonNode0.setFeedbackMode(ButtonNode.FEEDBACK_MODE_COLORHIGHLIGHTED);
		this.buttonNode1.setFeedbackMode(ButtonNode.FEEDBACK_MODE_COLORHIGHLIGHTED);
		this.buttonNode2.setFeedbackMode(ButtonNode.FEEDBACK_MODE_COLORHIGHLIGHTED);
		this.buttonNode3.setFeedbackMode(ButtonNode.FEEDBACK_MODE_COLORHIGHLIGHTED);
		this.buttonNode4.setFeedbackMode(ButtonNode.FEEDBACK_MODE_COLORHIGHLIGHTED);
		this.buttonNode5.setFeedbackMode(ButtonNode.FEEDBACK_MODE_COLORHIGHLIGHTED);
		this.buttonNode6.setFeedbackMode(ButtonNode.FEEDBACK_MODE_COLORHIGHLIGHTED);
		this.buttonNode7.setFeedbackMode(ButtonNode.FEEDBACK_MODE_COLORHIGHLIGHTED);
		this.buttonNode8.setFeedbackMode(ButtonNode.FEEDBACK_MODE_COLORHIGHLIGHTED);
		this.buttonNode9.setFeedbackMode(ButtonNode.FEEDBACK_MODE_COLORHIGHLIGHTED);
		this.buttonNodeDelete.setFeedbackMode(ButtonNode.FEEDBACK_MODE_COLORHIGHLIGHTED);
		this.buttonNodePlus.setFeedbackMode(ButtonNode.FEEDBACK_MODE_COLORHIGHLIGHTED);
		this.buttonNodeSubstract.setFeedbackMode(ButtonNode.FEEDBACK_MODE_COLORHIGHLIGHTED);
		this.buttonNodeResult.setFeedbackMode(ButtonNode.FEEDBACK_MODE_COLORHIGHLIGHTED);
		this.buttonNodeDot.setFeedbackMode(ButtonNode.FEEDBACK_MODE_COLORHIGHLIGHTED);
		
	}

	


}