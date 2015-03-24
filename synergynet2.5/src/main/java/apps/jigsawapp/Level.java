package apps.jigsawapp;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * The Class Level.
 */
public class Level {
	/** The Columns. */
	public int Columns;

	/** The Image file. */
	public File ImageFile;

	/** The Image file location. */
	public String ImageFileLocation; // the folder that represents the table
										// that the image will be displayed on
	
	/** The Image file name. */
	public String ImageFileName;

	/** The Image height. */
	public int ImageHeight;

	/** The Image width. */
	public int ImageWidth;

	// making member variables public is not good!
	/** The Level number. */
	public int LevelNumber;

	/** The Rows. */
	public int Rows;

	/**
	 * Instantiates a new level.
	 *
	 * @param pLevelNumber
	 *            the level number
	 * @param pImageFileLocation
	 *            the image file location
	 * @param pImageFileName
	 *            the image file name
	 * @param pRows
	 *            the rows
	 * @param pColumns
	 *            the columns
	 */
	public Level(int pLevelNumber, String pImageFileLocation,
			String pImageFileName, int pRows, int pColumns) {
		LevelNumber = pLevelNumber;
		ImageFileName = pImageFileName;
		ImageFileLocation = pImageFileLocation;
		Rows = pRows;
		Columns = pColumns;

		ImageFile = new File(
				"./src/main/resources/apps/jigsawapp/Original Images/"
						+ ImageFileLocation + "/" + ImageFileName);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(ImageFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		// put the image file into an image buffer
		BufferedImage image = null;
		try {
			image = ImageIO.read(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ImageHeight = image.getHeight();
		ImageWidth = image.getWidth();
	}
}
