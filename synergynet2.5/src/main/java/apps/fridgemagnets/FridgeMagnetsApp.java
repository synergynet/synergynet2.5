/*
 * Copyright (c) 2008 University of Durham, England All rights reserved.
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

package apps.fridgemagnets;

import java.awt.Color;
import java.awt.Font;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.TextLabel;
import synergynetframework.appsystem.table.SynergyNetAppUtils;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.appsystem.table.appregistry.menucontrol.HoldTopRightConfirmVisualExit;

import com.sun.java.util.collections.Random;

/**
 * The Class FridgeMagnetsApp.
 */
public class FridgeMagnetsApp extends DefaultSynergyNetApp {
	
	/** The alphabet. */
	private String[] alphabet = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z" };
	
	/** The content system. */
	private ContentSystem contentSystem;

	/** The random. */
	Random random = new Random();

	/** The vowels. */
	private String[] vowels = { "A", "E", "I", "O", "U" };

	/**
	 * Instantiates a new fridge magnets app.
	 *
	 * @param info
	 *            the info
	 */
	public FridgeMagnetsApp(ApplicationInfo info) {
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
		SynergyNetAppUtils.addTableOverlay(this);
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(this);
		setMenuController(new HoldTopRightConfirmVisualExit(this));
	}
	
	/**
	 * Adds the letters.
	 *
	 * @param current
	 *            the current
	 */
	private void addLetters(int current) {
		
		TextLabel letter = ((TextLabel) this.contentSystem
				.createContentItem(TextLabel.class));
		if (current < 26) {
			letter.setText(alphabet[current]);
		} else if (current < (26 * 2)) {
			letter.setText(alphabet[current / 2]);
		} else if (current < (26 * 3)) {
			letter.setText(alphabet[(random.nextInt(26))]);
		} else {
			letter.setText(vowels[(current - (26 * 3)) % 5]);
		}
		letter.setFont(new Font("Arial", Font.BOLD, 50));
		letter.placeRandom();
		letter.rotateRandom();
		
		letter.setBorderSize(2);
		letter.setBorderColour(Color.white);
		letter.setBackgroundColour(Color.black);
		letter.setTextColour(Color.getHSBColor(
				(float) ((Math.random() / 2) + 0.5f),
				(float) ((Math.random() / 2) + 0.5f),
				(float) ((Math.random() / 2) + 0.5f)));
		letter.setScaleLimit(1f, 1f);
		
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#onActivate
	 * ()
	 */
	@Override
	public void onActivate() {
		contentSystem.removeAllContentItems();

		int limit = (26 * 3) + (5 * 3);

		for (int i = 0; i < limit; i++) {
			addLetters(i);
		}

	}
	
}
