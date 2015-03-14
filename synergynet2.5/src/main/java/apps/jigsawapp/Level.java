package apps.jigsawapp;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Level
{
	// making member variables public is not good!
	public int LevelNumber;
	public String ImageFileName;
	public String ImageFileLocation; // the folder that represents the table that the image will be displayed on 
	public int ImageHeight;
	public int ImageWidth;
	public int Rows;
	public int Columns;
	public File ImageFile;
	
	public Level (int pLevelNumber, String pImageFileLocation, String pImageFileName, int pRows, int pColumns)
	{
		LevelNumber= pLevelNumber;
		ImageFileName= pImageFileName;
		ImageFileLocation= pImageFileLocation;
		Rows= pRows;
		Columns= pColumns;
		
		ImageFile = new File("./src/main/resources/apps/jigsawapp/Original Images/" + ImageFileLocation + "/" + ImageFileName);  
        FileInputStream fis = null;
		try
		{
			fis = new FileInputStream (ImageFile);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
        // put the image file into an image buffer
		BufferedImage image = null;
		try
		{
			image = ImageIO.read (fis);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}  
		
		ImageHeight= image.getHeight();
		ImageWidth= image.getWidth();		
	}
}
