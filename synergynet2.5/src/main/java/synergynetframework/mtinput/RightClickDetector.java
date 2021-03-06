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

package synergynetframework.mtinput;

import java.util.Vector;

import synergynetframework.jme.cursorsystem.cursordata.ScreenCursor;

import com.jme.math.Vector2f;

/**
 * The Class RightClickDetector.
 */
public class RightClickDetector {
	
	/**
	 * Checks if is double click.
	 *
	 * @param screenCursors
	 *            the screen cursors
	 * @param rightCursor
	 *            the right cursor
	 * @param distance
	 *            the distance
	 * @return true, if is double click
	 */
	public static boolean isDoubleClick(Vector<ScreenCursor> screenCursors,
			ScreenCursor rightCursor, float distance) {
		Vector2f screenCursorPosition = new Vector2f();
		Vector2f rightCursorPosition = new Vector2f();
		for (ScreenCursor screenCursor : screenCursors) {
			if (screenCursor.getID() == rightCursor.getID()) {
				continue;
			}
			screenCursorPosition.x = screenCursor
					.getCurrentCursorScreenPosition().x;
			screenCursorPosition.y = screenCursor
					.getCurrentCursorScreenPosition().y;

			rightCursorPosition.x = rightCursor
					.getCurrentCursorScreenPosition().x;
			rightCursorPosition.y = rightCursor
					.getCurrentCursorScreenPosition().y;
			
			if (Math.abs(screenCursorPosition.subtract(rightCursorPosition)
					.length()) < distance) {
				return true;
			}

		}

		return false;
	}
	
}
