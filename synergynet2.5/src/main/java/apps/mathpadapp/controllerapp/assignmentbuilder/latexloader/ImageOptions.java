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

package apps.mathpadapp.controllerapp.assignmentbuilder.latexloader;

import java.io.File;
import java.io.OutputStream;

import uk.ac.ed.ph.snuggletex.jeuclid.SimpleMathMLImageSavingCallback;


/**
 * The Class ImageOptions.
 */
public class ImageOptions extends SimpleMathMLImageSavingCallback {

	/** The out. */
	OutputStream out;
	
	/**
	 * Instantiates a new image options.
	 *
	 * @param out the out
	 */
	public ImageOptions(OutputStream out){
		this.out = out;
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.ed.ph.snuggletex.jeuclid.SimpleMathMLImageSavingCallback#getImageOutputFile(int)
	 */
	@Override
	public File getImageOutputFile(int arg0) {
		return null;
	}

	/* (non-Javadoc)
	 * @see uk.ac.ed.ph.snuggletex.jeuclid.SimpleMathMLImageSavingCallback#getImageOutputStream(int)
	 */
	@Override
	public OutputStream getImageOutputStream(int arg0) {
		return out;
	}

	/* (non-Javadoc)
	 * @see uk.ac.ed.ph.snuggletex.jeuclid.SimpleMathMLImageSavingCallback#getImageURL(int)
	 */
	@Override
	public String getImageURL(int arg0) {
		return null;
	}


	/* (non-Javadoc)
	 * @see uk.ac.ed.ph.snuggletex.jeuclid.MathMLImageSavingCallback#imageSavingFailed(java.lang.Object, int, java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void imageSavingFailed(Object arg0, int arg1, String arg2,
			Throwable arg3) {
		 
		
	}

	/* (non-Javadoc)
	 * @see uk.ac.ed.ph.snuggletex.jeuclid.SimpleMathMLImageSavingCallback#imageSavingSucceeded(java.lang.Object, int, java.lang.String)
	 */
	@Override
	public void imageSavingSucceeded(Object arg0, int arg1, String arg2) {
		 
		
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.ed.ph.snuggletex.jeuclid.SimpleMathMLImageSavingCallback#setFontSize(java.lang.String)
	 */
	@Override
	public void setFontSize(String size){
		super.setFontSize("48");
	}
}
