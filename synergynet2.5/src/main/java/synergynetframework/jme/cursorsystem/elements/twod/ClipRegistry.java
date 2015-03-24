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

package synergynetframework.jme.cursorsystem.elements.twod;

import java.util.HashMap;

import com.jme.scene.Spatial;

/**
 * The Class ClipRegistry.
 */
public class ClipRegistry {

	/** The instance. */
	private static ClipRegistry instance;

	/**
	 * Gets the single instance of ClipRegistry.
	 *
	 * @return single instance of ClipRegistry
	 */
	public static ClipRegistry getInstance() {
		if (instance == null) {
			instance = new ClipRegistry();
		}
		return instance;
	}

	/** The spatial clip map. */
	private HashMap<Spatial, ClipRegion> spatialClipMap;

	/**
	 * Instantiates a new clip registry.
	 */
	private ClipRegistry() {
		spatialClipMap = new HashMap<Spatial, ClipRegion>();
	}

	/**
	 * Gets the clip region for spatial.
	 *
	 * @param spatial
	 *            the spatial
	 * @return the clip region for spatial
	 */
	public ClipRegion getClipRegionForSpatial(Spatial spatial) {
		return spatialClipMap.get(spatial);
	}

	/**
	 * Gets the clip regions.
	 *
	 * @return the clip regions
	 */
	public HashMap<Spatial, ClipRegion> getClipRegions() {
		return spatialClipMap;
	}

	/**
	 * Checks if is registered.
	 *
	 * @param spatial
	 *            the spatial
	 * @return true, if is registered
	 */
	public boolean isRegistered(Spatial spatial) {
		return spatialClipMap.containsKey(spatial);
	}

	/**
	 * Register clip region.
	 *
	 * @param spatial
	 *            the spatial
	 * @param clipRegion
	 *            the clip region
	 */
	public void registerClipRegion(Spatial spatial, ClipRegion clipRegion) {
		spatialClipMap.put(spatial, clipRegion);
	}

	/**
	 * Unregister.
	 *
	 * @param spatial
	 *            the spatial
	 */
	public void unregister(Spatial spatial) {
		spatialClipMap.remove(spatial);
	}
}
