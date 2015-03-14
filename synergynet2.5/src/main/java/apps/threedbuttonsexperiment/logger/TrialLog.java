package apps.threedbuttonsexperiment.logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TrialLog implements LogListener {
	protected String targetString;
	protected long startTime;
	protected long endTime;
	protected List<KeyPressRecord> inputString;
	protected List<KeyPressRecord> inputHistory;
	protected int numberOfCorrection=0;
	protected long effectiveDuration;
	protected long lastKeyPressTime;
	protected int result;
	protected DateTextWritter logWritter;
	
	public TrialLog(String targetString, long startTime, DateTextWritter logWritter) {
		super();
		this.targetString = targetString;
		this.startTime = startTime;
		this.logWritter = logWritter;
		inputString = new ArrayList<KeyPressRecord>();
		inputHistory = new ArrayList<KeyPressRecord>();
		lastKeyPressTime = this.startTime;
		
	}

	public String getTargetString() {
		return targetString;
	}

	public long getStartTime() {
		return startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public List<KeyPressRecord> getInputString() {
		return inputString;
	}

	public List<KeyPressRecord> getInputHistory() {
		return inputHistory;
	}

	public int getNumberOfCorrection() {
		return numberOfCorrection;
	}

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
