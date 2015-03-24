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

package apps.realgravity;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The Class Universe.
 */
public class Universe {
	
	/** The Constant zero. */
	private static final Double zero = new Point.Double();
	// universal gravitational constant  (m3 kg-1 s-2)
	/** The g. */
	public static double G = 6.67300E-11;

	/** The entities. */
	protected Map<String,MassEntity> entities = new HashMap<String,MassEntity>();

	/**
	 * Update motion.
	 *
	 * @param elapsedTime the elapsed time
	 */
	public void updateMotion(double elapsedTime) {
		// work with cloned list to avoid ordering issues in which changes are made
		List<MassEntity> tempEntities = new ArrayList<MassEntity>();

		synchronized(this) {
			for(MassEntity e : entities.values()) {
				tempEntities.add((MassEntity)e.clone());
			}
		}

		for(MassEntity e : tempEntities) {			
			Point.Double combinedForce = getForces(e, entities.values());
			Point.Double accel = new Point.Double(combinedForce.x / e.getMass(), combinedForce.y / e.getMass());						
			Point.Double velocityChange = new Point.Double(accel.x * elapsedTime, accel.y * elapsedTime);			
			e.getVel().x = e.getVel().x - velocityChange.x;
			e.getVel().y = e.getVel().y - velocityChange.y;
			e.getPos().x = e.getPos().x + e.getVel().x * elapsedTime;
			e.getPos().y = e.getPos().y + e.getVel().y * elapsedTime;
		}		

		synchronized(this) {
			// now we've made the changes, copy back
			entities.clear();		
			for(MassEntity e : tempEntities) {
				entities.put(e.getName(), e);
			}
		}

	}

	/**
	 * Gets the forces.
	 *
	 * @param e the e
	 * @param localEntities the local entities
	 * @return the forces
	 */
	private synchronized Double getForces(MassEntity e, Collection<MassEntity> localEntities) {
		Point.Double force = new Point.Double();
		for(MassEntity x : localEntities) {
			if(e.equals(x)) continue;
			double fEX = getForce(e, x);
			double dist = e.getPos().distance(x.getPos());
			double dx = e.getPos().x - x.getPos().x;
			double dy = e.getPos().y - x.getPos().y;

			force.x += (fEX/dist) * dx;
			force.y += (fEX/dist) * dy;
		}
		return force;
	}

	/**
	 * Gets the force.
	 *
	 * @param e the e
	 * @param x the x
	 * @return the force
	 */
	public static double getForce(MassEntity e, MassEntity x) {
		// G . M1 . M2 / d^2
		return (G * e.getMass() * x.getMass()) / Math.pow(e.getPos().distance(x.getPos()), 2);		
	}

	/**
	 * Adds the.
	 *
	 * @param p the p
	 */
	public synchronized void add(MassEntity p) {
		entities.put(p.getName(), p);		
	}

	/**
	 * Render.
	 *
	 * @param g2d the g2d
	 * @param screenWidth the screen width
	 * @param screenHeight the screen height
	 * @param metersPerPixel the meters per pixel
	 */
	public void render(Graphics2D g2d, int screenWidth, int screenHeight, double metersPerPixel) {
		synchronized(this) {
			for(MassEntity e : entities.values())
				e.render(g2d, screenWidth/2, screenHeight/2, metersPerPixel);
		}
	}

	/**
	 * Cull far away.
	 *
	 * @param viewableUniverseWidth the viewable universe width
	 */
	public void cullFarAway(double viewableUniverseWidth) {
		
		synchronized(this) {
			List<String> toRemove = new ArrayList<String>();
			for(MassEntity e : entities.values()) {
				if(e.getPos().distance(zero) > viewableUniverseWidth/1.8) {
					toRemove.add(e.getName());
				}
			}
			for(String s : toRemove) {
				entities.remove(s);
			}
		}
	}

	/**
	 * Gets the entity count.
	 *
	 * @return the entity count
	 */
	public int getEntityCount() {
		return entities.size();
	}
}
