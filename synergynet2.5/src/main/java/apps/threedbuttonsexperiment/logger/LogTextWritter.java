package apps.threedbuttonsexperiment.logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The Class LogTextWritter.
 */
public class LogTextWritter {

	/** The Constant DEVICE_CALCULATOR. */
	public static final String DEVICE_CALCULATOR = "calculator";

	/** The Constant DEVICE_KEYBOARD. */
	public static final String DEVICE_KEYBOARD = "keyboard";

	/** The Constant FEEDBACK_MODE_3D. */
	public static final String FEEDBACK_MODE_3D = "3d";

	/** The Constant FEEDBACK_MODE_COLORHIGHLIGHTED. */
	public static final String FEEDBACK_MODE_COLORHIGHLIGHTED = "color";

	/** The Constant FEEDBACK_MODE_NONE. */
	public static final String FEEDBACK_MODE_NONE = "none";

	/** The file name. */
	protected String fileName;

	/**
	 * Adds the record.
	 *
	 * @param target
	 *            the target
	 * @param inputString
	 *            the input string
	 * @param startTime
	 *            the start time
	 * @param endTime
	 *            the end time
	 * @param effectiveTimeCost
	 *            the effective time cost
	 * @param corrects
	 *            the corrects
	 */
	public void addRecord(String target, String inputString, String startTime,
			String endTime, String effectiveTimeCost, String corrects) {

		try {
			FileWriter out = new FileWriter(fileName, true);
			BufferedWriter writer = new BufferedWriter(out);
			writer.write(target + "#" + inputString + "#" + startTime + "#"
					+ endTime + "#" + effectiveTimeCost + "#" + corrects + "\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Creates the file.
	 *
	 * @param fileName
	 *            the file name
	 */
	public void createFile(String fileName) {

		try {
			FileWriter out = new FileWriter(fileName);
			BufferedWriter writer = new BufferedWriter(out);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
