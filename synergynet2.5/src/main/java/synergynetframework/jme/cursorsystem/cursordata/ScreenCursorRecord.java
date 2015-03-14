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

package synergynetframework.jme.cursorsystem.cursordata;

import com.jme.math.Vector2f;

/**
 * The coordinate system for this class is jme screen coordinates
 * 
 * @author dcs0ah1
 *
 */

public class ScreenCursorRecord {
	public float x;
	public float y;
	public float z;
	public float a;
	public float dx;
	public float dy;
	public float dz;
	public float da;
	public long time;
	private Vector2f pos;
	
	public ScreenCursorRecord(ScreenCursorRecord r) {
		this(r.x, r.y, r.z, r.dx, r.dy, r.dz, r.da, r.time);
	}

	public ScreenCursorRecord(float xpos, float ypos, float zpos, float x_speed, float y_speed, float z_speed, float m_accel, long l) {
		this.x = xpos;
		this.y = ypos;
		this.z = zpos;
		this.dx = x_speed;
		this.dy = y_speed;
		this.dz = z_speed;
		this.da = m_accel;
		this.time = l;
	}
	
	public ScreenCursorRecord(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public ScreenCursorRecord(Vector2f position, Vector2f velocity) {
		this.x = position.x;
		this.y = position.y;
		this.dx = velocity.x;
		this.dy = velocity.y;
	}

	public Vector2f getPosition() {
		if(pos == null) pos = new Vector2f(x, y);
		return pos;
	}

	public String toString() {
		return this.x + "," + this.y;
	}
	
	@Override
	public Object clone() {
		ScreenCursorRecord rec = new ScreenCursorRecord(x, y, z, dx, dy, dz, da, time);
		return rec;
	}
}
