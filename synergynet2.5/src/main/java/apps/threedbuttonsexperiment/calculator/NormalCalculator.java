package apps.threedbuttonsexperiment.calculator;

import synergynetframework.appsystem.contentsystem.items.TextLabel;
import apps.threedbuttonsexperiment.logger.DateTextWritter;

/**
 * The Class NormalCalculator.
 */
public class NormalCalculator extends Calculator {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2429175967783608868L;

	/**
	 * Instantiates a new normal calculator.
	 *
	 * @param name
	 *            the name
	 * @param targetNumberLabel
	 *            the target number label
	 * @param logWritter
	 *            the log writter
	 */
	public NormalCalculator(String name, TextLabel targetNumberLabel,
			DateTextWritter logWritter) {
		super(name, targetNumberLabel, logWritter);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * apps.threedbuttonsexperiment.calculator.Calculator#setButtonProperty()
	 */
	@Override
	protected void setButtonProperty() {

	}
	
}