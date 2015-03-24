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

package apps.presence;

import java.awt.Color;
import java.awt.RenderingHints;
import java.util.List;

import synergynetframework.appsystem.services.ServiceManager;
import synergynetframework.appsystem.services.exceptions.CouldNotStartServiceException;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.tablecomms.client.TableCommsClientService;
import synergynetframework.appsystem.table.gfx.FullScreenCanvas;
import synergynetframework.jme.gfx.twod.DrawableSpatialImage;

import com.jme.scene.Spatial;
import com.jmex.awt.swingui.ImageGraphics;

/**
 * The Class TablePresenceView.
 */
public class TablePresenceView extends FullScreenCanvas implements
		DrawableSpatialImage {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6495026220114034607L;

	/** The comms. */
	private TableCommsClientService comms;

	/** The gfx. */
	private ImageGraphics gfx;
	
	/**
	 * Instantiates a new table presence view.
	 *
	 * @param name
	 *            the name
	 */
	public TablePresenceView(String name) {
		super(name);
		gfx = getGraphics();
		try {
			comms = (TableCommsClientService) ServiceManager.getInstance().get(
					TableCommsClientService.class);
		} catch (CouldNotStartServiceException e) {
			e.printStackTrace();
		}
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
		gfx.setColor(Color.DARK_GRAY);
		gfx.fillRect(0, 0, pixelWidth, pixelHeight);
		gfx.setColor(Color.white);
		if (comms != null) {
			List<TableIdentity> online = comms.getCurrentlyOnline();
			int y = 50;
			for (TableIdentity s : online) {
				gfx.drawString(s.toString(), 30, y);
				y += 50;
			}
		} else {
			gfx.drawString("Table Communications Client Service not found", 50,
					30);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.jme.gfx.twod.DrawableSpatialImage#getSpatial()
	 */
	public Spatial getSpatial() {
		return this;
	}
	
}
