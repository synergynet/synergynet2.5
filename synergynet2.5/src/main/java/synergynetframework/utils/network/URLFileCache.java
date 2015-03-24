/*
 * Copyright (c) 2009 University of Durham, England All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of 'SynergyNet' nor the names of
 * its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission. THIS SOFTWARE IS PROVIDED
 * BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package synergynetframework.utils.network;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

import synergynetframework.utils.crypto.CryptoUtils;

/**
 * The Class URLFileCache.
 */
public class URLFileCache {

	/** The cache dir. */
	private File cacheDir;
	
	/**
	 * Instantiates a new URL file cache.
	 */
	public URLFileCache() {
		String userHome = System.getProperty("user.home");
		cacheDir = new File(userHome, ".synergynet");
		if (!cacheDir.exists()) {
			cacheDir.mkdir();
		}
	}
	
	/**
	 * Download.
	 *
	 * @param url
	 *            the url
	 * @return the file
	 */
	public File download(URL url) {
		String cacheFileName;
		File f;
		try {
			cacheFileName = CryptoUtils.md5(url.toString());
			f = new File(cacheDir, cacheFileName);
			if (!f.exists()) {
				Downloader.download(f, url);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return f;
	}
	
	/**
	 * Download.
	 *
	 * @param url
	 *            the url
	 * @param listener
	 *            the listener
	 */
	public void download(URL url, DownloadListener listener) {
		try {
			String cacheFileName = CryptoUtils.md5(url.toString());
			File f = new File(cacheDir, cacheFileName);
			if (f.exists()) {
				listener.downloadComplete(url, f);
			} else {
				new DownloadWorker(url, f, listener);
			}
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
	}
}
