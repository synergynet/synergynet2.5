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

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Hashtable;
import java.util.logging.Logger;

import javax.media.Buffer;
import javax.media.Format;
import javax.media.ResourceUnavailableException;
import javax.media.format.RGBFormat;
import javax.media.renderer.VideoRenderer;

public class ByteBufferRenderer implements VideoRenderer {

	private static final Logger log = Logger.getLogger(ByteBufferRenderer.class.getName()); 
	
	public java.awt.Component getComponent() {
		return null;
	}

	public boolean setComponent(java.awt.Component arg0) {
		return false;
	}

	public void setBounds(java.awt.Rectangle arg0) {
	}

	public java.awt.Rectangle getBounds() {
		return null;
	}

	public Format[] getSupportedInputFormats() {
		return new Format[] { new RGBFormat() };
	}

	private RGBFormat vf;
	private int[] pixels;

	public static ByteBufferRendererListener listener = null;
	public static boolean useFOBSOptimization = false;
	public static boolean useFOBSPatch = false;
	public static boolean printframes = false;

	private ByteBufferRendererListener mylistener;

	@SuppressWarnings("unchecked")
	public Format setInputFormat(Format format) {
		mylistener = listener;

		vf = (RGBFormat) format;
		int formatWidth = (int) vf.getSize().getWidth();
		int formatHeight = (int) vf.getSize().getHeight();
		log.info("Format (RGB masks, pixelstride, linestride, bbp, flipped");
		log.info(Integer.toHexString(vf.getRedMask()));
		log.info(Integer.toHexString(vf.getBlueMask()));
		log.info(Integer.toHexString(vf.getGreenMask()));
		log.info(""+vf.getPixelStride());
		log.info(""+vf.getLineStride());
		log.info(""+vf.getBitsPerPixel());
		log.info(""+(vf.getFlipped() == RGBFormat.TRUE));

		int needDataSize = formatWidth * formatHeight;

		if (mylistener != null) {
			mylistener.setSize(formatWidth, formatHeight, vf);
		}

		try {
			if (useFOBSOptimization) {
				// Use reflection so that FOBS is not required.

				Class<?> fobs = Class.forName("com.omnividea.FobsConfiguration");
				Hashtable<String,Object> props = (Hashtable<String,Object>) fobs.getField("properties").get(null);
				fobs.getField("videoFrameFormat").setInt(null, 0);
				Class<?> omniVidParser = Class.forName("com.omnividea.media.parser.video.Parser");
				Method m = omniVidParser.getMethod("avIsBigEndian", (Class<?>[])null);
				boolean bigendian = ((Boolean) m.invoke(null, (Object[])null)).booleanValue();

				if (useFOBSPatch) {

					// FOBS always uses 32 bit it seems.
					nativebuffer = ByteBuffer.allocateDirect(4 * needDataSize);

					if (bigendian == false)
						nativebuffer.order(ByteOrder.LITTLE_ENDIAN);

					props.put("ByteBuffer", nativebuffer);
					pixels = new int[0];
				} else {
					BufferedImage bufferedImage = new BufferedImage(formatWidth, formatHeight,
							BufferedImage.TYPE_INT_RGB);
					pixels = ((DataBufferInt) bufferedImage.getRaster().getDataBuffer()).getData();

					props.put("BufferedImage", bufferedImage);
					props.put("BufferedImageIntBuffer", pixels);
				}

				fobs.getField("useNativeBuffers").setBoolean(null, true);
				props.put("BufferedImageIntBuffer", pixels);

				log.info("FOBS found");
			}

		} catch (ClassNotFoundException notfound) {
			// no fobs found
			log.info("FOBS not found");

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (nativebuffer == null) {
			int bytes = vf.getBitsPerPixel() / 8;
			nativebuffer = ByteBuffer.allocateDirect(bytes * needDataSize);
			nativebuffer.order(ByteOrder.nativeOrder()); // TODO: test on
															// linux/mac
		}
		log.info("Frame buffer size (bytes): " + (needDataSize * 4));

		return format;
	}

	public void start() {
	}

	public void stop() {
	}

	private long time = 0;
	private long frames = 0;

	private ByteBuffer nativebuffer;

	public int process(Buffer buf) {
		frames++;
		if (time == 0) {
			time = System.currentTimeMillis();
		}
		long passed = (System.currentTimeMillis() - time);
		Object o = buf.getData();

		if (o instanceof int[]) {
			int[] data = (int[]) o;

			if (data != null && data.length * 4 == nativebuffer.limit()) {
				nativebuffer.rewind();
				nativebuffer.asIntBuffer().put(data);
			}
		}

		if (o instanceof byte[]) {
			byte[] data = (byte[]) o;

			if (data != null && data.length == nativebuffer.limit()) {
				nativebuffer.rewind();
				nativebuffer.put(data);
			}
		}

		if (o instanceof short[]) {
			short[] data = (short[]) o;

			if (data != null && data.length * 2 == nativebuffer.limit()) {
				nativebuffer.rewind();
				nativebuffer.asShortBuffer().put(data);
			}
		}

		if (printframes)
			log.info("frame data:" + buf.getLength() + " frame: " + frames + " time: " + passed + " fps:"
					+ (frames / (passed / 1000f)) + " type: " + o.getClass().getSimpleName());

		nativebuffer.rewind();

		if (mylistener != null) {
			mylistener.frame(nativebuffer);
		}

		return BUFFER_PROCESSED_OK;
	}

	public String getName() {
		return "ByteBuffer Renderer";
	}

	public void open() throws ResourceUnavailableException {
	}

	public void close() {
	}

	public void reset() {
	}

	public Object[] getControls() {
		return null;
	}

	public Object getControl(String arg0) {
		return null;
	}
}
