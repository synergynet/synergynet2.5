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

package synergynetframework.appsystem.contentsystem.items.implementation.interfaces;

import java.awt.Image;
import java.net.URL;

/**
 * The Interface ILightImageLabelImplementation.
 */
public interface ILightImageLabelImplementation extends
		IQuadContentItemImplementation {

	/**
	 * Draw image.
	 *
	 * @param image
	 *            the image
	 */
	public void drawImage(Image image);

	/**
	 * Draw image.
	 *
	 * @param imageResource
	 *            the image resource
	 */
	public void drawImage(URL imageResource);

	/**
	 * Enable aspect ratio.
	 *
	 * @param isAspectRationEnabled
	 *            the is aspect ration enabled
	 */
	public void enableAspectRatio(boolean isAspectRationEnabled);

	/**
	 * Gets the image resource.
	 *
	 * @return the image resource
	 */
	public URL getImageResource();

	/**
	 * Checks if is apla enabled.
	 *
	 * @return true, if is apla enabled
	 */
	public boolean isAplaEnabled();

	/**
	 * Checks if is aspect ratio enabled.
	 *
	 * @return true, if is aspect ratio enabled
	 */
	public boolean isAspectRatioEnabled();

	/**
	 * Use alpha.
	 *
	 * @param useAlpha
	 *            the use alpha
	 */
	public void useAlpha(boolean useAlpha);
}
