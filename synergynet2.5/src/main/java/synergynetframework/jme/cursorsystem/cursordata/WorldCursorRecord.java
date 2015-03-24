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

package synergynetframework.jme.cursorsystem.cursordata;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

/**
 * The Class WorldCursorRecord.
 */
public class WorldCursorRecord {

	/** The time. */
	public long time;

	/** The world location. */
	public Vector3f worldLocation;

	/** The world rotation. */
	public Quaternion worldRotation;

	/** The world scale. */
	public Vector3f worldScale;
	
	/**
	 * Instantiates a new world cursor record.
	 *
	 * @param worldTranslation
	 *            the world translation
	 * @param nanoTime
	 *            the nano time
	 */
	public WorldCursorRecord(Vector3f worldTranslation, long nanoTime) {
		this(worldTranslation, null, null, nanoTime);
	}
	
	/**
	 * Instantiates a new world cursor record.
	 *
	 * @param worldTranslation
	 *            the world translation
	 * @param worldRotation
	 *            the world rotation
	 * @param worldScale
	 *            the world scale
	 * @param nanoTime
	 *            the nano time
	 */
	public WorldCursorRecord(Vector3f worldTranslation,
			Quaternion worldRotation, Vector3f worldScale, long nanoTime) {
		this.worldLocation = worldTranslation;
		this.worldRotation = worldRotation;
		this.worldScale = worldScale;
		this.time = nanoTime;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return worldLocation.toString() + "@" + time + "\n";
	}
}
