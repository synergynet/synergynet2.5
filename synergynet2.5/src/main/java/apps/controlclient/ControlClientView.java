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

package apps.controlclient;

import java.awt.Color;
import java.awt.Font;
import java.awt.RenderingHints;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.table.gfx.FullScreenCanvas;
import synergynetframework.jme.gfx.twod.DrawableSpatialImage;

import com.jme.scene.Spatial;
import com.jmex.awt.swingui.ImageGraphics;

/**
 * The Class ControlClientView.
 */
public class ControlClientView extends FullScreenCanvas implements
		DrawableSpatialImage {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1473078189191810104L;

	/** The gfx. */
	private ImageGraphics gfx;

	/** The table id font. */
	private Font tableIDFont = new Font("Arial", Font.PLAIN, 18);

	/** The title font. */
	private Font titleFont = new Font("Arial", Font.BOLD, 36);
	
	/**
	 * Instantiates a new control client view.
	 *
	 * @param name
	 *            the name
	 */
	public ControlClientView(String name) {
		super(name);
		gfx = getGraphics();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.jme.gfx.twod.DrawableSpatialImage#cursorClicked(long,
	 * int, int)
	 */
	@Override
	public void cursorClicked(long cursorID, int x, int y) {
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.jme.gfx.twod.DrawableSpatialImage#cursorDragged(long,
	 * int, int)
	 */
	@Override
	public void cursorDragged(long id, int x, int y) {
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.jme.gfx.twod.DrawableSpatialImage#cursorPressed(long,
	 * int, int)
	 */
	@Override
	public void cursorPressed(long cursorID, int x, int y) {
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.jme.gfx.twod.DrawableSpatialImage#cursorReleased(
	 * long, int, int)
	 */
	@Override
	public void cursorReleased(long cursorID, int x, int y) {
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.jme.gfx.twod.DrawableSpatialImage#draw()
	 */
	public void draw() {
		gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		gfx.setColor(Color.darkGray);
		gfx.fillRect(0, 0, pixelWidth, pixelHeight);
		gfx.setColor(Color.black);
		gfx.setFont(titleFont);
		gfx.drawString("Please wait", 401, 401);
		gfx.setColor(Color.lightGray);
		gfx.drawString("Please wait", 400, 400);
		gfx.setFont(tableIDFont);
		gfx.drawString(TableIdentity.getTableIdentity().toString(), 10, 30);
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.jme.gfx.twod.DrawableSpatialImage#getSpatial()
	 */
	public Spatial getSpatial() {
		return null;
	}
	
}
