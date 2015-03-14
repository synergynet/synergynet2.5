package apps.projectmanagement.io;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import apps.projectmanagement.component.staffnode.StaffModel;
import apps.projectmanagement.component.staffnode.StaffNode;
import apps.projectmanagement.registry.StaffNodeRegistry;

import synergynetframework.appsystem.contentsystem.ContentSystem;

public class StaffNodesLoader {
	
	public static String loadNode(String fileName, ContentSystem contentSystem){
		
		try {			
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
	
			String strLine;
			StaffNode staffNode;
			
			while ((strLine = br.readLine()) != null)   {
			
				String[] attributes = strLine.trim().split(",");
				List<String> notes = new ArrayList<String>();
				for (int i=4; i<attributes.length; i++){
					notes.add(attributes[i]);
				}
				
				StaffModel staffModel = new StaffModel(attributes[0], attributes[1], attributes[2], attributes[3], notes);
				
				staffNode = new StaffNode(contentSystem, staffModel, 80, 100);
				StaffNodeRegistry.getInstance().addNode(staffNode);
			
			}	
			br.close();
				  	
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return fileName;
				
	}

}
