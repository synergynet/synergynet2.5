package apps.threedbuttonsexperiment.logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class LogTextWritter {
	
	public static final String DEVICE_CALCULATOR = "calculator";
	public static final String DEVICE_KEYBOARD = "keyboard";
	public static final String FEEDBACK_MODE_NONE = "none";
	public static final String FEEDBACK_MODE_COLORHIGHLIGHTED = "color";
	public static final String FEEDBACK_MODE_3D = "3d";
	
	protected String fileName;
	
	public void createFile(String fileName){
		
		try {			
			FileWriter out = new FileWriter(fileName);
			BufferedWriter writer = new BufferedWriter(out);
			writer.close();		
		} catch (IOException e) {
			e.printStackTrace();
		} 
				
	}
	
	public void addRecord(String target, String inputString, String startTime, String endTime, String effectiveTimeCost, String corrects){
		
		try {		
			FileWriter out = new FileWriter(fileName, true);
			BufferedWriter writer = new BufferedWriter(out);
			writer.write(target+"#"+inputString+"#"+startTime+"#"+endTime+"#"+effectiveTimeCost+"#"+corrects+"\n");
			writer.close();		
		} catch (IOException e) {
			e.printStackTrace();
		} 
				
	}
	
	

}
