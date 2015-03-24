package synergynetframework.utils.io;

import java.io.File;

/**
 * The Class FileUtils.
 */
public class FileUtils {
	
	/**
	 * Gets the extension.
	 *
	 * @param file
	 *            the file
	 * @return the extension
	 */
	public static String getExtension(File file) {
		return getExtension(file.getName());
	}
	
	/**
	 * Gets the extension.
	 *
	 * @param fileName
	 *            the file name
	 * @return the extension
	 */
	public static String getExtension(String fileName) {
		int lastdot = fileName.lastIndexOf('.');
		if (lastdot < 0) {
			return null;
		}
		return fileName.substring(lastdot + 1, fileName.length());
	}
	
	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		System.out.println(getExtension(new File("abc.jpg")));
	}
}
