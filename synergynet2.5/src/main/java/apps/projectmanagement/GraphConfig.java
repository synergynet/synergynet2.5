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

package apps.projectmanagement;

import java.awt.Color;
import java.awt.Font;
//import java.io.InputStream;
import java.net.URL;

import apps.projectmanagement.ProjectManagementApp;

import synergynetframework.appsystem.contentsystem.items.LineItem;

public class GraphConfig {
	public static Color nodeBackgroundColor = Color.white;
	public static Color nodeForegroundColor = Color.black;
	public static int nodeTopBorderSize = 30;
	public static int nodeDefaultBorderSize = 20;
	public static Color nodeBorderColor = Color.white;
	public static Color linkColor = Color.white;
	public static float linkWidth = 1f;
	public static Font nodeTextFont = new Font("Arial", Font.PLAIN, 20);
	public static Font linkTextFont = new Font("Arial", Font.BOLD, 16);
	public static Color linkTextColor = Color.white;
	public static int nodePointSize = 20;
	public static Color nodePointBgColor = Color.white;
	public static int nodePointBorderSize = 2;
	public static Color nodePointBorderColor = Color.black;
	public static Font messageTextFont = new Font("Arial", Font.PLAIN, 16);
	public static Color messageTextColor = Color.white;
	public static Color messageBackgroundColor = Color.black;
	public static Color messageBorderColor = Color.white;
	public static URL nodeCloseImageResource = ProjectManagementApp.class.getResource("close.png");
	public static URL nodeLinkImageResource = ProjectManagementApp.class.getResource("Link24.gif");
	public static URL nodeEditImageResource = ProjectManagementApp.class.getResource("edit.png");
	public static URL nodeKeyboardImageResource = ProjectManagementApp.class.getResource("keyboard.jpg");
	public static URL nodeNextPageImageResource = ProjectManagementApp.class.getResource("Forward24.gif");
	public static URL nodePreviousPageImageResource = ProjectManagementApp.class.getResource("Back24.gif");
	public static int linkMode = LineItem.CONNECTED_LINE;
	public static int arrowMode = LineItem.BIDIRECTIONAL_ARROWS;

	//public static InputStream keyboardDef = CommonResources.class.getResourceAsStream("keyboard.def");
	
}

