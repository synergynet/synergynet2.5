package apps.threedbuttonsexperiment.calculator;

import apps.threedbuttonsexperiment.logger.DateTextWritter;
import synergynetframework.appsystem.contentsystem.items.TextLabel;

public class NormalCalculator extends Calculator {

	private static final long serialVersionUID = 2429175967783608868L;
	
	public NormalCalculator(String name, TextLabel targetNumberLabel, DateTextWritter logWritter) {
		super(name, targetNumberLabel, logWritter);
	}

	@Override
	protected void setButtonProperty() {
		
	}

	


}