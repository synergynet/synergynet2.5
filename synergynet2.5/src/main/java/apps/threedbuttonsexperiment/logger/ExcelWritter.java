package apps.threedbuttonsexperiment.logger;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


/**
 * The Class ExcelWritter.
 */
public class ExcelWritter {
	
	/**
	 * Txt to excel file.
	 *
	 * @param txtFile the txt file
	 */
	public static void txtToExcelFile(String txtFile){
	
		WritableWorkbook workbook;
		WritableSheet sheet;
		
		String excelFileName = txtFile.substring(0, txtFile.lastIndexOf(".txt")-1)+".xls";
		
		try {
			workbook = Workbook.createWorkbook(new File(excelFileName));
			sheet = workbook.createSheet("First Sheet", 0);
			
			
			FileInputStream fstream = new FileInputStream(txtFile);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
					
			//add title of this file
			Label label = new Label(0, 0, br.readLine()); 
			sheet.addCell(label); 
			
			//add column title
			label = new Label(0, 2, "Target"); 
			sheet.addCell(label); 
			label = new Label(1, 2, "Input"); 
			sheet.addCell(label); 
			label = new Label(2, 2, "Result"); 
			sheet.addCell(label);
			label = new Label(3, 2, "EffectiveTime"); 
			sheet.addCell(label); 
			label = new Label(4, 2, "Corrections"); 
			sheet.addCell(label); 
			
			

			
			String strLine;
			br.readLine();
			int i=3;
			while ((strLine = br.readLine()) != null)   {
				  String[] record = strLine.trim().split(" ");
				  
				//add column title
				label = new Label(0, i, record[0]); 
				sheet.addCell(label); 
				label = new Label(1, i, record[1]); 
				sheet.addCell(label); 
				label = new Label(2, i, record[2]); 
				sheet.addCell(label);
				label = new Label(3, i, record[3]); 
				sheet.addCell(label); 
				label = new Label(4, i, record[4]); 
				sheet.addCell(label); 
				i++;
			}
			
			in.close();
			
			
			
			workbook.write(); 
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}
				
	}

	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		ExcelWritter.txtToExcelFile("log//3DExperimentLog//Excel//1-28 [16~13~33] (Keyboard-3D).txt");
	}

}
