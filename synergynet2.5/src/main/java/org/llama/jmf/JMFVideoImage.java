/******************************************************************************
 * JMF/FOBS/jME renderer.
 * 
 * Copyright (c) 2006 Tijl Houtbeckers
 * Coded by Tijl Houtbeckers
 *
 *    This file is part of the JMF/FOBS/jME renderer.
 *
 *    The JMF/FOBS/jME renderer is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as
 *    published by the Free Software Foundation; either version 2.1 
 *    of the License, or (at your option) any later version.
 *
 *    The JMF/FOBS/jME renderer is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public
 *    License along with FOBS; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 ******************************************************************************/

/**
 * @author Tijl Houtbeckers
 * 
 * Initial release: 23-02-2006
 * 
 * jME: jmonkeyengine.com
 * FOBS: http://fobs.sourceforge.net/
 * JMF: http://java.sun.com/products/java-media/jmf/ 
 */

package org.llama.jmf;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Logger;

import javax.media.CannotRealizeException;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.EndOfMediaEvent;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.ResourceUnavailableEvent;
import javax.media.Time;
import javax.media.control.FramePositioningControl;
import javax.media.format.RGBFormat;
import javax.media.protocol.DataSource;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.OpenGLException;
import org.lwjgl.opengl.Util;

import com.jme.image.Image;
import com.jme.image.Texture;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;


/**
 * The Class JMFVideoImage.
 */
public class JMFVideoImage extends Image implements ByteBufferRendererListener, ControllerListener {

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(JMFVideoImage.class.getName());	
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8413968528763966076L;
	
	/** The Constant SCALE_NONE. */
	public final static int SCALE_NONE = 0;
	
	/** The Constant SCALE_MAXIMIZE. */
	public final static int SCALE_MAXIMIZE = 1;
	
	/** The Constant SCALE_FIT. */
	public final static int SCALE_FIT = 2;

	/** The jmfplayer. */
	private Player jmfplayer;
	
	/** The videoheight. */
	private int videowidth, videoheight; // frame dimensions
	
	/** The inittexture. */
	private transient boolean failed = false, ready = false, inittexture = false;	
	
	/** The fpc. */
	private FramePositioningControl fpc;

	/**
	 * Instantiates a new JMF video image.
	 *
	 * @param filename the filename
	 * @param loop the loop
	 * @param scalemethod the scalemethod
	 * @throws NoPlayerException the no player exception
	 * @throws CannotRealizeException the cannot realize exception
	 * @throws MalformedURLException the malformed url exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public JMFVideoImage(String filename, boolean loop, int scalemethod) throws NoPlayerException, CannotRealizeException, MalformedURLException, IOException {
		this(new URL("file:" + filename), loop, scalemethod);
	}
	
	
	
	/** The scalemethod. */
	private int scalemethod;
	
	/**
	 * Instantiates a new JMF video image.
	 *
	 * @param url the url
	 * @param loop the loop
	 * @param scalemethod the scalemethod
	 * @throws NoPlayerException the no player exception
	 * @throws CannotRealizeException the cannot realize exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public JMFVideoImage(URL url, boolean loop, int scalemethod) throws NoPlayerException, CannotRealizeException, IOException {
	
		this.scalemethod = scalemethod;
		

		Manager.setHint(Manager.PLUGIN_PLAYER, new Boolean(true));
		ByteBufferRenderer.listener = this;

		try {
	
			String address = url.toString();
			
			
			address = address.replaceAll("file:/", "file:");
			address = address.replaceAll("%20", " ");
			
		    MediaLocator locator = new MediaLocator(address);
		    
		    DataSource ds = Manager.createDataSource(locator);
	
			jmfplayer = Manager.createRealizedPlayer(ds);
			log.info("Created player for: " + url.toString());
	
			jmfplayer.addControllerListener(this);
			
		    fpc = (FramePositioningControl)jmfplayer.getControl("javax.media.control.FramePositioningControl");    
		} catch (Exception e) {
				e.printStackTrace();
		}
	}

	/**
	 * Start movie.
	 */
	public void startMovie() {
		active = true;
		jmfplayer.start();
	}
	
	/**
	 * Sets the time.
	 *
	 * @param time the new time
	 */
	public void setTime(Time time){
		jmfplayer.setMediaTime(time);
	}
	
	/**
	 * Seek.
	 *
	 * @param time the time
	 */
	public void seek(Time time){
		if (fpc!=null){
			fpc.seek(fpc.mapTimeToFrame(time));
		}
	}
	
	/**
	 * Gets the duration.
	 *
	 * @return the duration
	 */
	public Time getDuration(){
		return jmfplayer.getDuration();
	}
	
	/**
	 * Sets the speed.
	 *
	 * @param rate the new speed
	 */
	public void setSpeed(float rate){
		jmfplayer.setRate(rate);
	}
	
	/**
	 * Gets the speed.
	 *
	 * @param rate the rate
	 * @return the speed
	 */
	public void getSpeed(float rate){
		jmfplayer.getRate();		
	}
	
	/**
	 * Sets the mute.
	 *
	 * @param isMute the new mute
	 */
	public void setMute(boolean isMute){
		if (jmfplayer.getGainControl()!=null)
			jmfplayer.getGainControl().setMute(isMute);
	}
	
	/**
	 * Checks if is mute.
	 *
	 * @return true, if is mute
	 */
	public boolean isMute(){
		if (jmfplayer.getGainControl()!=null)
			return jmfplayer.getGainControl().getMute();
		return false;
	}
	
	/**
	 * Sets the sound volume level.
	 *
	 * @param level the new sound volume level
	 */
	public void setSoundVolumeLevel(float level){
		if (jmfplayer.getGainControl()!=null)
			jmfplayer.getGainControl().setLevel(level);
	}
	
	/**
	 * Gets the sound volume level.
	 *
	 * @return the sound volume level
	 */
	public float getSoundVolumeLevel(){
		if (jmfplayer.getGainControl()!=null)
			return jmfplayer.getGainControl().getLevel();
		return 0;
	}
	
	/**
	 * Gets the media time.
	 *
	 * @return the media time
	 */
	public Time getMediaTime(){
		return jmfplayer.getMediaTime();
	}

	/**
	 * Stop movie.
	 */
	synchronized public void stopMovie() {
		active = false;
		try {
			jmfplayer.stop();
			//jmfplayer.close();
		} catch (Exception ignored) {
			ignored.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see javax.media.ControllerListener#controllerUpdate(javax.media.ControllerEvent)
	 */
	public void controllerUpdate(ControllerEvent evt)
	// respond to events
	{
		if (evt instanceof ResourceUnavailableEvent) {
			failed = false;
			synchronized (this) {
				this.notifyAll();
			}
		} else if (evt instanceof EndOfMediaEvent) { // make the movie loop
			log.info("loop the movie");
			//jmfplayer.setMediaTime(new Time(0));
			//jmfplayer.start();
			restart();
		}
	}
	
	/**
	 * Restart.
	 */
	public void restart(){		
		jmfplayer.setMediaTime(new Time(0));
		jmfplayer.start();
	}
	
	/**
	 * Starts from.
	 *
	 * @param seconds the seconds
	 */
	public void startsFrom(final double seconds){
		new Thread(new Runnable(){

			@Override
			public void run() {
				jmfplayer.setMediaTime(new Time(seconds));			

			}
		}).start();
	}

	/**
	 * Gets the video width.
	 *
	 * @return the video width
	 */
	public int getVideoWidth() {
		return videowidth;
	}

	/**
	 * Gets the video height.
	 *
	 * @return the video height
	 */
	public int getVideoHeight() {
		return videoheight;
	}
	
	/** The flipped. */
	private boolean flipped = false;
	
	/** The dataformat. */
	private int pixelformat, dataformat;
	

	/* (non-Javadoc)
	 * @see org.llama.jmf.ByteBufferRendererListener#setSize(int, int, javax.media.format.RGBFormat)
	 */
	public void setSize(int videowidth, int videoheight, RGBFormat format) {		
		log.info("set Size: " + videowidth + " " + videoheight);
		if (ready)
			return;
		
		// TODO: support more formats, test on linux/mac (might need ARGB?)
		
		switch (format.getBitsPerPixel()) {
			case 32:
				pixelformat = GL12.GL_BGRA;
				this.setFormat(Format.RGBA8);
				break;
			case 16:
				pixelformat = GL12.GL_BGRA;
				this.setFormat(Format.RGB5A1);
				break;
			case 24:
			default:
				pixelformat = GL12.GL_BGR;
				this.setFormat(Format.RGB8);
		}
		
		// TODO: not actually used yet.
		switch(format.getPixelStride()) {
			case 1:
				if (this.getFormat() == Format.RGB5A1) {
					/*
					 * setFormat will be a jME method to set the native texture image format.
					 * (currently always GL11.GL_UNSIGNED_BYTE)
					 * Using formats with the same packing size will speed up texture updates. 
					 */					
					// TODO: this.setFormat(GL12.GL_UNSIGNED_SHORT_1_5_5_5);
					dataformat = GL12.GL_UNSIGNED_SHORT_1_5_5_5_REV;
					log.info("texture format: GL_UNSIGNED_SHORT_1_5_5_5_REV");
				}
				else {
					// TODO: this.setFormat(GL12.GL_INT_8_8_8_8_REV);
					dataformat = GL12.GL_UNSIGNED_INT_8_8_8_8_REV;
					log.info("texture format: GL_UNSIGNED_INT_8_8_8_8_REV");
				}
				
				break;
			case 3:
			case 4:
			default:
				dataformat = GL11.GL_UNSIGNED_BYTE;
				log.info("texture format: GL_UNSIGNED_BYTE");
		}
		
		if (format.getFlipped() == RGBFormat.FALSE) 
			flipped=true;
		
		this.videowidth = videowidth;
		this.videoheight = videoheight;

		try {
			int size = Math.max(videoheight, videowidth);

			if (!FastMath.isPowerOfTwo(size)) {
				int newsize = 2;
				do {
					newsize <<= 1;
				} while (newsize < size);
				size = newsize;
			}
			this.width = size;
			this.height = size;

            data.clear();
			data.add( ByteBuffer.allocateDirect(size*size*4).order(ByteOrder.nativeOrder()) );

			ready = true;
			inittexture = true;
		} catch (Exception e) {
			e.printStackTrace();
			failed = true;
		}
		synchronized (this) {
			this.notifyAll();
		}

	}

	/** The framecounter. */
	long framecounter = 0;
	
	/** The lastupdated. */
	long lastupdated = 0;
	
	/** The buffer. */
	private ByteBuffer buffer;

	/** The active. */
	private boolean active;
	
	/**
	 * Update.
	 *
	 * @param texture the texture
	 * @return true, if successful
	 */
	public boolean update(Texture texture) {
		return update(texture, false);
	}
	
	/**
	 * Update.
	 *
	 * @param texture            Texture to update
	 * @param syncToFrameRate            Wait till the frame is updated before updating the texture.
	 * @return true if the texture was updated
	 */
	public boolean update(Texture texture, boolean syncToFrameRate) {
		if(!active) return false;
		if (syncToFrameRate) { 
			synchronized(this) {
				while (lastupdated >= framecounter) {
					try {
						this.wait();
					} catch (InterruptedException e) {
					}
				}
			}
		}

		if (lastupdated >= framecounter) {
			return false;
		}
		
		if (lastupdated+1 < framecounter ) {
			log.info("missed frames: "+(framecounter -lastupdated+1));
		}

		if (data == null)
			return false;

		if (inittexture && this.scalemethod > 0) {
			inittexture = false;
			// clear current textures.
			TextureState state = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
			state.setEnabled(true);
			state.apply();

			log.info("rescaling texture");
			
			if (scalemethod == SCALE_FIT) {
			float scale;
			if (this.videowidth > this.videoheight) {
				scale = videowidth * (1f / this.width);
			} else {
				scale = videoheight * (1f / this.height);
			}

			texture.setScale(new Vector3f(scale, scale, scale));
			}
			
			if (scalemethod == SCALE_MAXIMIZE) {
//				texture.setScale(new Vector3f(1f,videowidth* (1f / this.width), videoheight * (1f / this.height)));
				texture.setScale(new Vector3f(videowidth* (1f / this.width),videoheight * (1f / this.height),1f));
			}

		}

		synchronized (this) {

//            long d = System.currentTimeMillis();

			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureId());
			// TODO: use this.dataformat
			GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, videowidth, videoheight,pixelformat,
					dataformat, buffer);

			try {
				Util.checkGLError();
			} catch (OpenGLException e) {
				log.info("Error rendering video to texture. No glTexSubImage2D/OpenGL 1.2 support?");
			}

			lastupdated = framecounter;
			this.notifyAll();
		}
		return false;

	}
	
	/**
	 * Wait some.
	 *
	 * @param time the time
	 */
	public synchronized void waitSome(int time) {
		try {
			this.wait(time);
		} catch (InterruptedException e) {
		}
	}

	/* (non-Javadoc)
	 * @see org.llama.jmf.ByteBufferRendererListener#frame(java.nio.ByteBuffer)
	 */
	public void frame(ByteBuffer buffer) {
		try {
			synchronized (this) {
				this.buffer = buffer;
				framecounter++;
				// guard against wrapping around
				if (framecounter < 0) {
					framecounter = 1;
					lastupdated = 0;
				}
				this.notifyAll();
				while (!failed && lastupdated < framecounter) {
					this.wait();
				}

			}
			
		} catch (Exception ignored) {
		}
	}

	/**
	 * Checks if is flipped.
	 *
	 * @return true, if is flipped
	 */
	public boolean isFlipped() {
		return flipped;
	}	
}
