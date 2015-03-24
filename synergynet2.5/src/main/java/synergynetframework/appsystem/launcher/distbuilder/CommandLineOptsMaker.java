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

package synergynetframework.appsystem.launcher.distbuilder;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;


/**
 * The Class CommandLineOptsMaker.
 */
public class CommandLineOptsMaker extends Task {

	/** The ignore dirs. */
	protected String[] ignoreDirs = {".svn", "igesture-1.1"};
	
	/** The ignore libs. */
	protected String[] ignoreLibs = {"swt.jar", "swt_fake.jar"};
	
	/** The libdir. */
	private File libdir;
	
	/** The outfile. */
	private File outfile;
	
	/** The classname. */
	private String classname;
	
	/** The arguments. */
	private String arguments;
	
	/**
	 * Sets the libdir.
	 *
	 * @param libdir the new libdir
	 */
	public void setLibdir(File libdir) {
		this.libdir = libdir;
	}
	
	/**
	 * Sets the outfile.
	 *
	 * @param outfile the new outfile
	 */
	public void setOutfile(File outfile) {
		this.outfile = outfile;
	}
	
	/**
	 * Sets the classname.
	 *
	 * @param classname the new classname
	 */
	public void setClassname(String classname) {
		this.classname = classname;
	}
	
	/**
	 * Sets the arguments.
	 *
	 * @param arguments the new arguments
	 */
	public void setArguments(String arguments) {
		this.arguments = arguments;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.tools.ant.Task#execute()
	 */
	public void execute() throws BuildException {
		List<String> opts = new ArrayList<String>();
		opts.add("-Xmx512M");
		opts.add("-Djava.library.path=" + generateNativeRefs(libdir));
		opts.add("-cp");
		opts.add(generateLibRefs(libdir) + "synergynet.jar");
		opts.add(classname);
		if(arguments != null) {
			opts.add(arguments);
		}
		String line = "";
		for(String s : opts) {
			line += s;
			line += " ";
		}
		try {
			PrintWriter pw = new PrintWriter(new FileOutputStream(outfile));
			pw.println(line);
			pw.close();
		} catch (FileNotFoundException e) {
//			e.printStackTrace();
			throw new BuildException(e);
		}
	}

	/**
	 * Generate native refs.
	 *
	 * @param d the d
	 * @return the string
	 */
	private String generateNativeRefs(File d) {
		StringBuffer line = new StringBuffer();
		File[] dirs = d.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}			
		});
		
		File[] nativelibs = d.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				File f = new File(dir, name);
				if(f.isDirectory()) return false;				
				if(isValidNativeLib(name)) return true;				
				return false;
			}

			private boolean isValidNativeLib(String name) {
				if(name.endsWith(".dll")) return true;
				if(name.endsWith(".so")) return true;
				if(name.endsWith(".jnilib")) return true;
				return false;
			}			
		});
		
		for(File dir : dirs) {
			line.append(generateNativeRefs(dir));
		}
		
		
		if(nativelibs.length > 0) {
			line.append("lib" + d.toString().substring(libdir.toString().length()) + ':');
		}
		
		return line.toString();
	}

	/**
	 * Generate lib refs.
	 *
	 * @param d the d
	 * @return the string
	 */
	private String generateLibRefs(File d) {
		
		StringBuffer line = new StringBuffer();
		
		File[] dirs = d.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}			
		});

		File[] jars = d.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				File f = new File(dir, name);
				if(f.isDirectory()) return false;				
				if(name.endsWith(".jar")) return true;				
				return false;
			}			
		});

		for(File dir : dirs) {
			if(!shouldIgnoreDir(dir)) {
				line.append(generateLibRefs(dir));
			}
		}

		for(File jar : jars) {			
			line.append("lib" + jar.toString().substring(libdir.toString().length()) + ':');
		}

		return line.toString();
	}

	/**
	 * Should ignore dir.
	 *
	 * @param dir the dir
	 * @return true, if successful
	 */
	private boolean shouldIgnoreDir(File dir) {
		for(String s : ignoreDirs) {
			if(dir.getName().endsWith(s)) return true;
		}
		return false;
	}
}
