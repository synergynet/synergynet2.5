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

package synergynetframework.jme.cursorsystem;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import synergynetframework.jme.cursorsystem.cursordata.ScreenCursor;


/**
 * Utility class used predominantly by the cursor system to maintain
 * a record of all currently active cursors.  A singleton. The cursors
 * are in JME screen coordinates.
 * 
 * @author dcs0ah1
 */

public class CursorRegistry {
	
	/** The instance. */
	private static CursorRegistry instance;
	
	/** The cursors. */
	private Hashtable<Long, ScreenCursor> cursors;
	
	
	static {
		synchronized(CursorRegistry.class) {
			instance = new CursorRegistry();
		}
	}
	
	/**
	 * Gets the single instance of CursorRegistry.
	 *
	 * @return single instance of CursorRegistry
	 */
	public static CursorRegistry getInstance() {
		return instance;
	}
	
	/**
	 * Instantiates a new cursor registry.
	 */
	private CursorRegistry() {
		cursors = new Hashtable<Long,ScreenCursor>();
	}


	/**
	 * Contains key.
	 *
	 * @param id the id
	 * @return true, if successful
	 */
	public boolean containsKey(long id) {
		return cursors.containsKey(id);
	}
	
	/**
	 * Returns a cloned List whose contents are the current
	 * cursors at the time of asking.
	 *
	 * @return the current registered cursors
	 */
	public List<ScreenCursor> getCurrentRegisteredCursors() {
		return new ArrayList<ScreenCursor>(cursors.values());
	}

	/**
	 * Adds the cursor.
	 *
	 * @param cursor the cursor
	 */
	public void addCursor(ScreenCursor cursor) {
		cursors.put(cursor.getID(), cursor);
	}

	/**
	 * Removes the cursor.
	 *
	 * @param id the id
	 */
	public void removeCursor(long id) {
		cursors.remove(id);		
	}

	/**
	 * Num cursors.
	 *
	 * @return the int
	 */
	public int numCursors() {
		return cursors.size();
	}

	/**
	 * Gets the cursor.
	 *
	 * @param id the id
	 * @return the cursor
	 */
	public ScreenCursor getCursor(long id) {
		return cursors.get(id);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "CursorRegistry has " + numCursors();
	}
}
