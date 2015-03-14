package apps.threedbuttonsexperiment.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class ConditionsReader {
	
	public static final String DEVICE_CALCULATOR = "calculator";
	public static final String DEVICE_KEYBOARD = "keyboard";
	public static final String FEEDBACK_MODE_NONE = "none";
	public static final String FEEDBACK_MODE_COLORHIGHLIGHTED = "color";
	public static final String FEEDBACK_MODE_3D = "3d";
	
	protected String fileName;
	
	public static String readConditions(List<String> pcConditions, List<String> mtConditions, List<String> mtViewConditions){
	
		String fileName = "apps/threeDButtonsExperiment/conditionsConfig.txt";
		
		try {
			
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String strLine = br.readLine();
			String[] conditions = strLine.trim().split(",");
			pcConditions.clear();
			for (String condition:conditions){
				
				pcConditions.add(condition);
			}
			
			strLine = br.readLine();
			conditions = strLine.trim().split(",");
			mtConditions.clear();
			for (String condition:conditions){
				
				mtConditions.add(condition);
			}
			
			strLine = br.readLine();
			conditions = strLine.trim().split(",");
			mtViewConditions.clear();
			for (String condition:conditions){
				
				mtViewConditions.add(condition);
			}
			
			br.close();
			
				
				  	
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return fileName;
				
	}
	
	public static String readConditions(List<String> experimentConditions){
		
		String fileName = "apps/threeDManipulationExperiment/conditionsConfig.txt";
		
		try {
			
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String strLine = br.readLine();
			String[] conditions = strLine.trim().split(",");
			experimentConditions.clear();
			for (String condition:conditions){
				
				experimentConditions.add(condition);
			}
			
			br.close();
			
				  	
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return fileName;
				
	}
	
}
