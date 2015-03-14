package apps.threedbuttonsexperiment.logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class DateTextWritter {
	
	public static final String DEVICE_CALCULATOR = "calculator";
	public static final String DEVICE_KEYBOARD = "keyboard";
	public static final String FEEDBACK_MODE_NONE = "none";
	public static final String FEEDBACK_MODE_COLORHIGHLIGHTED = "color";
	public static final String FEEDBACK_MODE_3D = "3d";
	
	protected String fileName;
	
	@SuppressWarnings("deprecation")
	public String createFile(String device, String type){
	
		Date currentDate = new Date();
		String dateString = currentDate.getMonth()+1+"-"+currentDate.getDate()+" ["+currentDate.getHours()+"~"+currentDate.getMinutes()+"~"+
			currentDate.getSeconds()+"]";
		fileName = "log//3DExperimentLog/Excel/"+dateString+" ("+device+").txt";
		
		try {
			
			FileWriter out = new FileWriter(fileName);
			BufferedWriter writer = new BufferedWriter(out);
			writer.write(currentDate.toString()+" ("+device+"-"+type+")\n");
			writer.write("Target Input Result EffectiveTime Corrections\n");
			writer.close();		
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return fileName;
				
	}
	
	public void addRecord( String target, String input, int result, double effectiveTime, int corrections){
		
		try {		
			FileWriter out = new FileWriter(fileName, true);
			BufferedWriter writer = new BufferedWriter(out);
			writer.write(target+" "+input+" "+result+" "+effectiveTime+" "+corrections+"\n");
			writer.close();		
		} catch (IOException e) {
			e.printStackTrace();
		} 
				
	}
	

}
