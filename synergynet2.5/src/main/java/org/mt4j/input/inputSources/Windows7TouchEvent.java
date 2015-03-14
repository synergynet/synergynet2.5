/*
 * mt4j Copyright (c) 2008 - 2010 Christopher Ruff, Fraunhofer-Gesellschaft All rights reserved.
 *  
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
*/

package org.mt4j.input.inputSources;

public class Windows7TouchEvent{

	/** The Constant TOUCH_DOWN. */
    public static final int TOUCH_DOWN = 0;
    
    /** The Constant TOUCH_MOVE. */
    public static final int TOUCH_MOVE = 1;
    
    /** The Constant TOUCH_UP. */
    public static final int TOUCH_UP = 2;
    
    /** The type. */
    public int type;
    
    /** The id. */
    public int id;
    
    /** The x value. */
    public int x;
    
    /** The y value. */
    public int y;
    
    /** The contact size area X dimension */
    public int contactSizeX;
    
    /** The contact size area Y dimension */
    public int contactSizeY;
}