/*
 * Copyright (c) 2009 University of Durham, England
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'SynergyNet' nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package synergynetframework.appsystem.launcher.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;

import synergynetframework.appsystem.launcher.client.SynergyNetLauncher.OSName;

public class ProcessStarter {
	public static void start(String resource) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(SynergyNetLauncher.class.getResourceAsStream(resource)));
		String cmd = br.readLine();
		br.close();
		String java6 = "java";

		switch(getOsName()) {
			case WINDOWS: {
				cmd = cmd.replace('/', '\\');
				cmd = cmd.replace(':', ';');
				break;
			}
	
			case MAC: {			
				java6 = "/System/Library/Frameworks/JavaVM.framework/Versions/1.6.0/Home/bin/java";	
				break;
			}
			
			default: {
				break;
			}
		
		}



		File pwd = new File(new File(".").getCanonicalPath());
		Process p = Runtime.getRuntime().exec(java6 + " " + cmd, null, pwd);

		LogWindow.getInstance().log(java6 + " " + cmd);
		LogWindow.getInstance().log(pwd.toString());
		LogWindow.getInstance().log(getClasspathString());

		InputStream is = p.getErrorStream();
		String line;
		br = new BufferedReader(new InputStreamReader(is));
		while((line = br.readLine()) != null) {
			LogWindow.getInstance().log(line);
		}
	}

	public static String getClasspathString() {
		StringBuffer classpath = new StringBuffer();
		ClassLoader applicationClassLoader = SynergyNetLauncher.class.getClassLoader();
		if (applicationClassLoader == null) {
			applicationClassLoader = ClassLoader.getSystemClassLoader();
		}
		URL[] urls = ((URLClassLoader)applicationClassLoader).getURLs();
		for(int i=0; i < urls.length; i++) {
			classpath.append(urls[i].getFile()).append("\r\n");
		}    

		return classpath.toString();
	}



	public static OSName getOsName() {
		OSName os = null;
		if (System.getProperty("os.name").toLowerCase().indexOf("windows") > -1) {
			os = OSName.WINDOWS;
		} else if (System.getProperty("os.name").toLowerCase().indexOf("linux") > -1) {
			os = OSName.LINUX;
		} else if (System.getProperty("os.name").toLowerCase().indexOf("mac") > -1) {
			os = OSName.MAC;
		}		 
		return os;
	}

}
