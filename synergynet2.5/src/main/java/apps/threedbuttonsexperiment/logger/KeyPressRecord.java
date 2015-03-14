package apps.threedbuttonsexperiment.logger;

public class KeyPressRecord {
	
	protected long pressTime;
	protected long duration;
	protected String charactor;
	
	public KeyPressRecord(long pressTime, String charactor, long lastKeyPressTime) {
		super();
		this.pressTime = pressTime;
		this.charactor = charactor;
		this.duration = pressTime-lastKeyPressTime;
	}
	public long getPressTime() {
		return pressTime;
	}
	public String getCharactor() {
		return charactor;
	}
	
	public long getDurationTime(){
		return duration;
	}
	
}
