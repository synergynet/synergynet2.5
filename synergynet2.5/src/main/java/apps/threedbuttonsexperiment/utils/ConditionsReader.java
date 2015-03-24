package apps.threedbuttonsexperiment.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * The Class ConditionsReader.
 */
public class ConditionsReader {

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

	/**
	 * Read conditions.
	 *
	 * @param experimentConditions
	 *            the experiment conditions
	 * @return the string
	 */
	public static String readConditions(List<String> experimentConditions) {

		String fileName = "apps/threeDManipulationExperiment/conditionsConfig.txt";

		try {

			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			String strLine = br.readLine();
			String[] conditions = strLine.trim().split(",");
			experimentConditions.clear();
			for (String condition : conditions) {

				experimentConditions.add(condition);
			}

			br.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return fileName;
		
	}

	/**
	 * Read conditions.
	 *
	 * @param pcConditions
	 *            the pc conditions
	 * @param mtConditions
	 *            the mt conditions
	 * @param mtViewConditions
	 *            the mt view conditions
	 * @return the string
	 */
	public static String readConditions(List<String> pcConditions,
			List<String> mtConditions, List<String> mtViewConditions) {
		
		String fileName = "apps/threeDButtonsExperiment/conditionsConfig.txt";

		try {

			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			String strLine = br.readLine();
			String[] conditions = strLine.trim().split(",");
			pcConditions.clear();
			for (String condition : conditions) {

				pcConditions.add(condition);
			}

			strLine = br.readLine();
			conditions = strLine.trim().split(",");
			mtConditions.clear();
			for (String condition : conditions) {

				mtConditions.add(condition);
			}

			strLine = br.readLine();
			conditions = strLine.trim().split(",");
			mtViewConditions.clear();
			for (String condition : conditions) {

				mtViewConditions.add(condition);
			}

			br.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return fileName;
		
	}

	/** The file name. */
	protected String fileName;

}
