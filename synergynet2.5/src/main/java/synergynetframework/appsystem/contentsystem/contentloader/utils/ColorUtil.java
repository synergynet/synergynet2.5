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

package synergynetframework.appsystem.contentsystem.contentloader.utils;

import java.awt.Color;

public class ColorUtil {
	public static Color GetColor(String colorName){
				
		if (colorName.toLowerCase().equals("blue"))
			return Color.blue;
		if (colorName.toLowerCase().equals("white"))
			return Color.white;
		if (colorName.toLowerCase().equals("black"))
			return Color.black;
		if (colorName.toLowerCase().equals("cyan"))
			return Color.cyan;
		if (colorName.toLowerCase().equals("darkgray"))
			return Color.darkGray;
		if (colorName.toLowerCase().equals("gray"))
			return Color.gray;
		if (colorName.toLowerCase().equals("green"))
			return Color.green;
		if (colorName.toLowerCase().equals("lightGray"))
			return Color.lightGray;
		if (colorName.toLowerCase().equals("magenta"))
			return Color.magenta;
		if (colorName.toLowerCase().equals("orange"))
			return Color.orange;
		if (colorName.toLowerCase().equals("pink"))
			return Color.pink;
		if (colorName.toLowerCase().equals("red"))
			return Color.red;
		if (colorName.toLowerCase().equals("yellow"))
			return Color.yellow;
		
		return null;		
	}
}
