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

package apps.splasher;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import synergynetframework.appsystem.table.animationsystem.AnimationSystem;
import synergynetframework.appsystem.table.animationsystem.animations.SplashSequence;
import synergynetframework.appsystem.table.animationsystem.animelements.AnimationSequence;
import synergynetframework.appsystem.table.animationsystem.animelements.ApplicationActivator;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.appsystem.table.gfx.GFXUtils;
import synergynetframework.jme.gfx.twod.ImageQuadFactory;

import com.jme.scene.Geometry;
import com.jme.scene.shape.Quad;

/**
 * The Class SplasherApp.
 */
public class SplasherApp extends DefaultSynergyNetApp {

	/** The image url. */
	protected URL imageURL = SplasherApp.class
			.getResource("synergynetlogo.png");

	/** The img width. */
	protected float imgWidth = 200;

	/** The next app. */
	protected ApplicationInfo nextApp;

	/** The seq. */
	protected AnimationSequence seq;

	/** The splash. */
	protected Quad splash;

	/**
	 * Instantiates a new splasher app.
	 *
	 * @param info
	 *            the info
	 */
	public SplasherApp(ApplicationInfo info) {
		super(info);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#addContent
	 * ()
	 */
	@Override
	public void addContent() {
	}

	/**
	 * Sets the next app.
	 *
	 * @param nextApp
	 *            the new next app
	 */
	public void setNextApp(ApplicationInfo nextApp) {
		this.nextApp = nextApp;
	}

	/**
	 * Sets the splash image url.
	 *
	 * @param imgURL
	 *            the img url
	 * @param imgWidth
	 *            the img width
	 */
	public void setSplashImageURL(URL imgURL, float imgWidth) {
		this.imageURL = imgURL;
		this.imgWidth = imgWidth;
	}

	/**
	 * Splash.
	 */
	public void splash() {
		seq = new AnimationSequence();
		List<Geometry> splashings = new ArrayList<Geometry>();
		splash = ImageQuadFactory.createQuadWithUncompressedImageResource(
				"splash", imgWidth, imageURL, ImageQuadFactory.ALPHA_ENABLE);
		GFXUtils.centerOrthogonalSpatial(splash);
		splashings.add(splash);
		orthoNode.attachChild(splash);
		seq.addAnimationElement(new SplashSequence(splashings, 2, 3f));
		seq.addAnimationElement(new ApplicationActivator(nextApp));

		AnimationSystem.getInstance().add(seq);
	}
	
}
