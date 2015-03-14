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

public class Launch {
	
	public static enum OSName {
		WINDOWS,
		LINUX,
		MAC
	}
	
	public static void main(String[] args) throws IOException {
		
		String java6 = "java";
		
		switch(getOsName()) {
			case WINDOWS: {
				// ?
				break;
			}
			
			case MAC: {
				java6 = "/System/Library/Frameworks/JavaVM.framework/Versions/1.6.0/Home/bin/java";	
				break;
			}
			
			case LINUX: {
				// ?
				break;
			}
		}
		
		
		File f = new File(".");
		System.out.println(f.getCanonicalPath());
		
		BufferedReader br = new BufferedReader(new InputStreamReader(Launch.class.getResourceAsStream(args[0])));
		String cmd = br.readLine();
		br.close();
		
		Process p = Runtime.getRuntime().exec(java6 + " " + cmd);
		InputStream is = p.getErrorStream();
		
		br = new BufferedReader(new InputStreamReader(is));
		String line;
		while((line = br.readLine()) != null) {
			System.out.println(line);
		}
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
