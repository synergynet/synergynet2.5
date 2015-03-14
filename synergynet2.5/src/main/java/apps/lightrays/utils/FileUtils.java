package apps.lightrays.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtils {

	public static String readDocAsString(String file) {
		File f = new File(file);
		StringBuffer buffer = new StringBuffer();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			String line;
			while((line = br.readLine()) != null) {
				buffer.append(line);				
			}
			br.close();
		} catch (FileNotFoundException e) {		
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
		return buffer.toString();
	}

}
