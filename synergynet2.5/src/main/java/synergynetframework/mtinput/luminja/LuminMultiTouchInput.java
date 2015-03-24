/*
 * Copyright (c) 2008 University of Durham, England
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

package synergynetframework.mtinput.luminja;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.safehaus.uuid.Logger;

import com.jme.math.Vector2f;

import de.evoluce.multitouch.adapter.java.BlobJ;
import de.evoluce.multitouch.adapter.java.JavaAdapter;

import synergynetframework.config.table.TableConfigPrefsItem;
import synergynetframework.mtinput.ClickDetector;
import synergynetframework.mtinput.IMultiTouchEventListener;
import synergynetframework.mtinput.IMultiTouchInputSource;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;
import synergynetframework.mtinput.exceptions.MultiTouchInputException;


/**
 * Support for the Lumin multi-touch table.  This implementation currently
 * only supports cursor information - it does not support objects/fiducials.
 * @author dcs0ah1
 *
 */
public class LuminMultiTouchInput implements IMultiTouchInputSource {
	
	/** The Constant BLOB_CHANGED_THRESHOLD. */
	private final static float BLOB_CHANGED_THRESHOLD = 0.003f;
	
	/** The listeners. */
	protected List<IMultiTouchEventListener> listeners = new ArrayList<IMultiTouchEventListener>();
	
	/** The ja. */
	protected JavaAdapter ja;	
	
	/** The current blobs. */
	protected BlobJ[] currentBlobs = new BlobJ[0];

	/** The current blob registry. */
	protected Map<Integer, BlobJ> currentBlobRegistry = new HashMap<Integer,BlobJ>();
	
	/** The last known position. */
	protected Map<Integer, Vector2f> lastKnownPosition = new HashMap<Integer,Vector2f>();
	
	/** The same position tolerance. */
	protected float samePositionTolerance = 0.002f;
	
	/** The click detector. */
	protected ClickDetector clickDetector = new ClickDetector(500, 2f);
	
	/** The executor. */
	ExecutorService executor = Executors.newCachedThreadPool();

	/** The table config. */
	protected TableConfigPrefsItem tableConfig = new TableConfigPrefsItem();
	
	/** The task. */
	private GetBlobsTask task;

	/**
	 * Instantiates a new lumin multi touch input.
	 */
	public LuminMultiTouchInput() {
		ja = new JavaAdapter("localhost");
		task = new GetBlobsTask(ja);
	}




	/**
	 * Process.
	 *
	 * @throws MultiTouchInputException the multi touch input exception
	 */
	private void process() throws MultiTouchInputException {
		try {
			Future<BlobJ[]> future = executor.submit(task);
			BlobJ[] result = future.get(5, TimeUnit.SECONDS);
			currentBlobs = result;
		} catch (Exception e) {	
			Logger.logError(e.toString());
		}

		Vector2f pos = new Vector2f();
		Vector2f vel = new Vector2f();

		Map<Integer, BlobJ> newRegistry = new HashMap<Integer,BlobJ>();
		// notify for new blobs or changes to existing blobs
		for(BlobJ blob : currentBlobs) {
			newRegistry.put(blob.mID, blob);
			pos.x = blob.mX;
			pos.y = blob.mY;

			MultiTouchCursorEvent event = new MultiTouchCursorEvent(blob.mID, new Point2D.Float(pos.x, 1-pos.y), new Point2D.Float(vel.x, vel.y), blob.mWidth, 0f);

			if(currentBlobRegistry.containsKey(blob.mID)) {
				if (pos.distance(new Vector2f(currentBlobRegistry.get(blob.mID).mX,
						currentBlobRegistry.get(blob.mID).mY)) > BLOB_CHANGED_THRESHOLD){
					for(IMultiTouchEventListener listener : listeners) {					
						listener.cursorChanged(event);
					}					
					lastKnownPosition.put(blob.mID, new Vector2f(blob.mX, 1-blob.mY));
				}else{
					newRegistry.put(blob.mID, currentBlobRegistry.get(blob.mID));
				}
			}else{
				clickDetector.newCursorPressed(blob.mID, new Point2D.Float(pos.x, 1-pos.y));
				for(IMultiTouchEventListener listener : listeners) {					
					listener.cursorPressed(event);
				}				
			}
		}


		// notify if blobs have disappeared
		for(Integer i : currentBlobRegistry.keySet()) {
			if(!newRegistry.keySet().contains(i)) {
				BlobJ blob = currentBlobRegistry.get(i);
				pos.x = blob.mX;
				pos.y = blob.mY;			
				MultiTouchCursorEvent event = new MultiTouchCursorEvent(blob.mID, new Point2D.Float(pos.x, 1-pos.y), new Point2D.Float(vel.x, vel.y), blob.mWidth, 0f);

				for(IMultiTouchEventListener l : listeners) {
					int clickCount = clickDetector.cursorReleasedGetClickCount(blob.mID, new Point2D.Float(pos.x, 1-pos.y));
					if(clickCount > 0) {
						event.setClickCount(clickCount);
						l.cursorClicked(event);
					}
					l.cursorReleased(event);
				}
				lastKnownPosition.remove(blob.mID);
			}
		}

		currentBlobRegistry = newRegistry;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchInputSource#setClickSensitivity(long, float)
	 */
	public void setClickSensitivity(long time, float distance) {
		clickDetector.setSensitivity(time, distance);
	}

	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchInputSource#registerMultiTouchEventListener(synergynetframework.mtinput.IMultiTouchEventListener)
	 */
	public void registerMultiTouchEventListener(IMultiTouchEventListener listener) {
		if(!listeners.contains(listener)) listeners.add(listener);	
	}

	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchInputSource#registerMultiTouchEventListener(synergynetframework.mtinput.IMultiTouchEventListener, int)
	 */
	public void registerMultiTouchEventListener(IMultiTouchEventListener listener, int index) {
		if(!listeners.contains(listener)) listeners.add(index, listener);
	}

	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchInputSource#unregisterMultiTouchEventListener(synergynetframework.mtinput.IMultiTouchEventListener)
	 */
	public void unregisterMultiTouchEventListener(IMultiTouchEventListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Sets the same position tolerance.
	 *
	 * @param samePositionTolerance the new same position tolerance
	 */
	public void setSamePositionTolerance(float samePositionTolerance) {
		this.samePositionTolerance = samePositionTolerance;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchInputSource#update(float)
	 */
	@Override
	public void update(float tpf) throws MultiTouchInputException {
		process();
	}

}

