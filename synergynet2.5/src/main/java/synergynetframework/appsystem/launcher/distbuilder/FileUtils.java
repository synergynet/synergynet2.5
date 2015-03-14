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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {
	public static void copyFiles(File src, File dest) throws IOException {
		if (!src.exists()) {
			throw new IOException("Resource does not exist: " + src.getAbsolutePath());
		} else if (!src.canRead()) {
			throw new IOException("Insufficient privileges to open: " + src.getAbsolutePath());
		}
		if (src.isDirectory()) 	{
			if (!dest.exists() || !dest.mkdirs()) {
				throw new IOException("Cannot create: " + dest.getAbsolutePath());				
			}
			String list[] = src.list();
			for (int i = 0; i < list.length; i++)
			{
				File to = new File(dest, list[i]);
				File from = new File(src, list[i]);
				copyFiles(from, to);
			}
		} else { 
			streamCopy(new FileInputStream(src), new FileOutputStream (dest));
		}
	}

	public static void streamCopy(InputStream is, OutputStream os) throws IOException {
		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = is.read(buffer)) >= 0) {
			os.write(buffer,0,bytesRead);
		}
		is.close();
		os.close();
	}
}
