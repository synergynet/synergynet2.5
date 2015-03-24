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

package apps.mysteries.client.restore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * The Class RestoreFileReader.
 */
public class RestoreFileReader {

	/** The mystery item states. */
	private HashMap<String, ContentItemState> mysteryItemStates = new HashMap<String, ContentItemState>();

	/** The restore folder. */
	private File restoreFolder;
	
	/**
	 * Instantiates a new restore file reader.
	 *
	 * @param restoreFolder
	 *            the restore folder
	 */
	public RestoreFileReader(File restoreFolder) {
		this.restoreFolder = restoreFolder;
	}

	/**
	 * Gets the last app state.
	 *
	 * @param appID
	 *            the app id
	 * @return the last app state
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public HashMap<String, ContentItemState> getLastAppState(String appID)
			throws IOException {
		mysteryItemStates.clear();
		File restoreFile = new File(restoreFolder, appID);
		if (restoreFile.exists()) {
			InputStream is = new FileInputStream(restoreFile);
			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			String str;
			while ((str = in.readLine()) != null) {
				if (!str.startsWith("#")) {
					processLine(str);
				}
			}
			in.close();
		}
		return mysteryItemStates;
	}

	/**
	 * Process line.
	 *
	 * @param str
	 *            the str
	 */
	private void processLine(String str) {
		String[] temp;
		ContentItemState istate = new ContentItemState();
		temp = str.split(",");
		if ((temp == null) || (temp.length < 12)) {
			return;
		}
		istate.setName(temp[0]);
		istate.setLocation(Float.parseFloat(temp[1]),
				Float.parseFloat(temp[2]), Float.parseFloat(temp[3]));
		istate.setScale(Float.parseFloat(temp[4]), Float.parseFloat(temp[5]),
				Float.parseFloat(temp[6]));
		istate.setRotation(Float.parseFloat(temp[7]),
				Float.parseFloat(temp[8]), Float.parseFloat(temp[9]),
				Float.parseFloat(temp[10]));
		istate.setZOrder(Integer.parseInt(temp[11]));
		mysteryItemStates.put(istate.getName(), istate);
	}
}
