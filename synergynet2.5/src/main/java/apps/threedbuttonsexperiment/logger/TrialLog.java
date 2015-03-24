package apps.threedbuttonsexperiment.logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The Class TrialLog.
 */
public class TrialLog implements LogListener {
	
	/** The target string. */
	protected String targetString;
	
	/** The start time. */
	protected long startTime;
	
	/** The end time. */
	protected long endTime;
	
	/** The input string. */
	protected List<KeyPressRecord> inputString;
	
	/** The input history. */
	protected List<KeyPressRecord> inputHistory;
	
	/** The number of correction. */
	protected int numberOfCorrection=0;
	
	/** The effective duration. */
	protected long effectiveDuration;
	
	/** The last key press time. */
	protected long lastKeyPressTime;
	
	/** The result. */
	protected int result;
	
	/** The log writter. */
	protected DateTextWritter logWritter;
	
	/**
	 * Instantiates a new trial log.
	 *
	 * @param targetString the target string
	 * @param startTime the start time
	 * @param logWritter the log writter
	 */
	public TrialLog(String targetString, long startTime, DateTextWritter logWritter) {
		super();
		this.targetString = targetString;
		this.startTime = startTime;
		this.logWritter = logWritter;
		inputString = new ArrayList<KeyPressRecord>();
		inputHistory = new ArrayList<KeyPressRecord>();
		lastKeyPressTime = this.startTime;
		
	}

	/**
	 * Gets the target string.
	 *
	 * @return the target string
	 */
	public String getTargetString() {
		return targetString;
	}

	/**
	 * Gets the start time.
	 *
	 * @return the start time
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * Gets the end time.
	 *
	 * @return the end time
	 */
	public long getEndTime() {
		return endTime;
	}

	/**
	 * Gets the input string.
	 *
	 * @return the input string
	 */
	public List<KeyPressRecord> getInputString() {
		return inputString;
	}

	/**
	 * Gets the input history.
	 *
	 * @return the input history
	 */
	public List<KeyPressRecord> getInputHistory() {
		return inputHistory;
	}

	/**
	 * Gets the number of correction.
	 *
	 * @return the number of correction
	 */
	public int getNumberOfCorrection() {
		return numberOfCorrection;
	}

	/* (non-Javadoc)
	 * @see apps.threedbuttonsexperiment.logger.LogListener#deleteKeyPressed()
	 */
	@Override
	public void deleteKeyPressed() {
		long currentKeyPressTime = new Date().getTime();
		
		this.numberOfCorrection+=1;
		if (inputString.size()<=0) return;
		inputString.remove(inputString.size()-1);
		
		inputHistory.add(new KeyPressRecord(currentKeyPressTime, "D", this.lastKeyPressTime));
		
		System.out.print("input String: ");
		for (KeyPressRecord k:inputString){
			System.out.print(k.getCharactor()+" ");
		}
		System.out.println();
		
	}

	/* (non-Javadoc)
	 * @see apps.threedbuttonsexperiment.logger.LogListener#keyPressed(java.lang.String)
	 */
	@Override
	public void keyPressed(String key) {
		
		long currentKeyPressTime = new Date().getTime();
			
		inputString.add(new KeyPressRecord(currentKeyPressTime, key, this.lastKeyPressTime));
		inputHistory.add(new KeyPressRecord(currentKeyPressTime, key, this.lastKeyPressTime));
		
		System.out.println("Key is: "+key+", time is: "+inputString.get(inputString.size()-1).getPressTime());
		System.out.println("slot is: "+key+", slot is: "+inputString.get(inputString.size()-1).getDurationTime());
		System.out.print("input String: ");
		
		for (KeyPressRecord k:inputString){
			System.out.print(k.getCharactor()+" ");
		}
		System.out.println();
		
		this.lastKeyPressTime = new Date().getTime();
	}

	/* (non-Javadoc)
	 * @see apps.threedbuttonsexperiment.logger.LogListener#submitKeyPressed()
	 */
	@Override
	public void submitKeyPressed() {
		this.endTime = new Date().getTime();
		
		for (KeyPressRecord k:inputString){
			this.effectiveDuration +=k.getDurationTime();
		}
		
		String input="";
		for (KeyPressRecord c:inputString){
			input=input+c.getCharactor();
		}
		
		if (targetString.trim().equals(input.trim()))
			this.result=1;
		else
			this.result=0;
		
		logWritter.addRecord(targetString, input, result, this.effectiveDuration, this.numberOfCorrection);
		
		System.out.println("effective Duration is:"+this.effectiveDuration);
		System.out.println("correction time is:"+this.numberOfCorrection);
		
		System.out.print("input String: ");
		
		for (KeyPressRecord k:inputString){
			System.out.print(k.getCharactor()+" ");
		}
		System.out.println();
	}
	
	
	
}
