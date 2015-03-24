package apps.threedbuttonsexperiment.logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;


/**
 * The Class DateTextWritter.
 */
public class DateTextWritter {
	
	/** The Constant DEVICE_CALCULATOR. */
	public static final String DEVICE_CALCULATOR = "calculator";
	
	/** The Constant DEVICE_KEYBOARD. */
	public static final String DEVICE_KEYBOARD = "keyboard";
	
	/** The Constant FEEDBACK_MODE_NONE. */
	public static final String FEEDBACK_MODE_NONE = "none";
	
	/** The Constant FEEDBACK_MODE_COLORHIGHLIGHTED. */
	public static final String FEEDBACK_MODE_COLORHIGHLIGHTED = "color";
	
	/** The Constant FEEDBACK_MODE_3D. */
	public static final String FEEDBACK_MODE_3D = "3d";
	
	/** The file name. */
	protected String fileName;
	
	/**
	 * Creates the file.
	 *
	 * @param device the device
	 * @param type the type
	 * @return the string
	 */
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
	
	/**
	 * Adds the record.
	 *
	 * @param target the target
	 * @param input the input
	 * @param result the result
	 * @param effectiveTime the effective time
	 * @param corrections the corrections
	 */
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
