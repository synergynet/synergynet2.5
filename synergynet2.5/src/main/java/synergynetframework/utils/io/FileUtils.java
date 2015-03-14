package synergynetframework.utils.io;

import java.io.File;

public class FileUtils {

	public static String getExtension(File file) {
		return getExtension(file.getName());
	}

	public static void main(String[] args) {
		System.out.println(getExtension(new File("abc.jpg")));
	}

	public static String getExtension(String fileName) {
		int lastdot = fileName.lastIndexOf('.');
		if(lastdot < 0) return null;
		return fileName.substring(lastdot+1, fileName.length());
	}
}
