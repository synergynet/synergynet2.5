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

package apps.groove;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.Frame;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.appsystem.table.appregistry.menucontrol.HoldTopRightConfirmVisualExit;


/**
 * The Class GrooveApp.
 */
public class GrooveApp extends DefaultSynergyNetApp{	
	
	/** The content. */
	private ContentSystem content;
	
	/** The synthesizer. */
	private Synthesizer synthesizer;
	
	/** The instruments. */
	private Instrument[] instruments;
	
	/** The current instrument. */
	private Instrument currentInstrument;
	
	/** The current channel. */
	private MidiChannel currentChannel;
	
	/** The all midi channels. */
	private MidiChannel[] allMidiChannels;	
	
	/**
	 * Instantiates a new groove app.
	 *
	 * @param info the info
	 */
	public GrooveApp(ApplicationInfo info) {
		super(info);
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#addContent()
	 */
	@Override
	public void addContent() {
		content = ContentSystem.getContentSystemForSynergyNetApp(this);
		setMenuController(new HoldTopRightConfirmVisualExit(this));

		try {
			synthesizer = MidiSystem.getSynthesizer();
			synthesizer.open();
			instruments = synthesizer.getDefaultSoundbank().getInstruments();
			currentInstrument = instruments[0];
			synthesizer.loadInstrument(currentInstrument);						
			allMidiChannels = synthesizer.getChannels();
	        currentChannel = allMidiChannels[0];
	        currentChannel.setMono(false);
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
		
		Frame f = (Frame) content.createContentItem(Frame.class);
		f.setWidth(480);
		f.setHeight(150);
		f.setRotateTranslateScalable(false);
		f.setScale(1.8f);
		f.centerItem();
		Graphics2D gfx = f.getGraphicsContext();
		gfx.setColor(Color.black);
		gfx.fillRect(0, 0, 680, 150);
		GrooveInstrument pianoInstrument = new GrooveInstrument("Piano", currentChannel);
		Piano p = new Piano(gfx);
		f.addItemListener(new PianoInteraction(p, pianoInstrument));	
		f.flushGraphics();
	}
}
