/*
 * Copyright (c) 2008 University of Durham, England All rights reserved.
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

package synergynetframework.jme.cursorsystem;

import java.awt.geom.Point2D;
import java.util.List;

import synergynetframework.jme.cursorsystem.cursordata.ScreenCursor;
import synergynetframework.jme.cursorsystem.cursordata.ScreenCursorRecord;
import synergynetframework.jme.pickingsystem.IJMEMultiTouchPicker;
import synergynetframework.jme.pickingsystem.PickSystemException;
import synergynetframework.jme.pickingsystem.data.PickRequest;
import synergynetframework.jme.pickingsystem.data.PickResultData;
import synergynetframework.mtinput.IMultiTouchEventListener;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;
import synergynetframework.mtinput.events.MultiTouchObjectEvent;

import com.jme.math.Vector2f;
import com.jme.system.DisplaySystem;

/**
 * Relies on MultiTouch table input events being supplied with the coordinate
 * mode as CoordinateMode.ZERO_TO_ONE_YFLIP
 *
 * @author dcs0ah1
 */

public class MultiTouchCursorSystem implements IMultiTouchEventListener {
	
	// private static Logger log =
	// Logger.getLogger(MultiTouchCursorSystemImpl.class.getName());
	
	/**
	 * Screen to table.
	 *
	 * @param v
	 *            the v
	 * @return the point2 d. float
	 */
	public static Point2D.Float screenToTable(Vector2f v) {
		return new Point2D.Float(v.x
				/ DisplaySystem.getDisplaySystem().getWidth(), v.y
				/ DisplaySystem.getDisplaySystem().getHeight());
	}

	/**
	 * Table to screen.
	 *
	 * @param f
	 *            the f
	 * @return the vector2f
	 */
	public static Vector2f tableToScreen(Point2D.Float f) {
		return new Vector2f(f.x * DisplaySystem.getDisplaySystem().getWidth(),
				f.y * DisplaySystem.getDisplaySystem().getHeight());
	}

	/** The cursor registry. */
	protected CursorRegistry cursorRegistry = CursorRegistry.getInstance();
	
	/** The pick system. */
	protected IJMEMultiTouchPicker pickSystem;
	
	/** The spatial registry. */
	protected MultiTouchElementRegistry spatialRegistry = MultiTouchElementRegistry
			.getInstance();
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#cursorChanged(
	 * synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	public void cursorChanged(MultiTouchCursorEvent event) {
		ScreenCursor c = cursorRegistry.getCursor(event.getCursorID());
		c.addPositionItem(new ScreenCursorRecord(tableToScreen(event
				.getPosition()), tableToScreen(event.getVelocity())));
		List<MultiTouchElement> items = spatialRegistry
				.getRegisteredElementsForCursorID(c.getID());
		for (MultiTouchElement item : items) {
			if (item.isActive()) {
				item.cursorChanged(c, event);
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#cursorClicked(
	 * synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	public void cursorClicked(MultiTouchCursorEvent event) {
		ScreenCursor c = cursorRegistry.getCursor(event.getCursorID());
		List<MultiTouchElement> items = spatialRegistry
				.getRegisteredElementsForCursorID(c.getID());
		for (MultiTouchElement item : items) {
			if (item.isActive()) {
				item.cursorClicked(c, event);
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#cursorPressed(
	 * synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	public void cursorPressed(MultiTouchCursorEvent event) {
		ScreenCursor c = new ScreenCursor(event.getCursorID());
		cursorRegistry.addCursor(c);
		
		PickRequest req = new PickRequest(event.getCursorID(),
				tableToScreen(event.getPosition()));
		List<PickResultData> pickResults;
		try {
			pickResults = pickSystem.doPick(req);
			MultiTouchCursorEvent newEvent = new MultiTouchCursorEvent(
					req.getRequestingCursorID(),
					screenToTable(req.getCursorPosition()));
			
			boolean multiTouchElementConsumedEvent = false;
			
			// pick results are ordered, so when pickMeOnly is set in a node,
			// there is no need to continue processing
			for (PickResultData pr : pickResults) {
				if (spatialRegistry.isRegistered(pr.getPickedSpatial())) {
					spatialRegistry.associateCursorIDToName(pr);
					List<MultiTouchElement> nodes = spatialRegistry
							.getRegisteredElementsForCursorID(pr
									.getOriginatingCursorID());
					ScreenCursor sc = CursorRegistry.getInstance().getCursor(
							pr.getOriginatingCursorID());
					if (sc != null) {
						sc.setPickResult(pr);
						for (MultiTouchElement n : nodes) {
							sc.addPositionItem(new ScreenCursorRecord(
									tableToScreen(newEvent.getPosition()),
									tableToScreen(newEvent.getVelocity())));
							n.registerScreenCursor(sc, pr);
							if (n.isActive()) {
								n.cursorPressed(sc, newEvent);
							}
							
							if (n.isPickMeOnly()) {
								multiTouchElementConsumedEvent = true;
							}
						}
						
					}
					if (multiTouchElementConsumedEvent) {
						break;
					}
				}
			}
		} catch (PickSystemException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#cursorReleased(
	 * synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	public void cursorReleased(MultiTouchCursorEvent event) {
		List<MultiTouchElement> nodes = spatialRegistry
				.getRegisteredElementsForCursorID(event.getCursorID());
		ScreenCursor c = CursorRegistry.getInstance().getCursor(
				event.getCursorID());
		for (MultiTouchElement n : nodes) {
			if (n.isActive()) {
				n.cursorReleased(c, event);
			}
			n.unregisterScreenCursor(c);
		}
		spatialRegistry.removeCursorIDToNamesAssociations(event.getCursorID());
		cursorRegistry.removeCursor(c.getID());
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#objectAdded(
	 * synergynetframework.mtinput.events.MultiTouchObjectEvent)
	 */
	public void objectAdded(MultiTouchObjectEvent event) {
		// TODO: Fiducials/objects
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#objectChanged(
	 * synergynetframework.mtinput.events.MultiTouchObjectEvent)
	 */
	public void objectChanged(MultiTouchObjectEvent event) {
		// TODO: Fiducials/objects
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#objectRemoved(
	 * synergynetframework.mtinput.events.MultiTouchObjectEvent)
	 */
	public void objectRemoved(MultiTouchObjectEvent event) {
		// TODO: Fiducials/objects
	}
	
	/**
	 * Sets the pick system.
	 *
	 * @param pickSystem
	 *            the new pick system
	 */
	public void setPickSystem(IJMEMultiTouchPicker pickSystem) {
		this.pickSystem = pickSystem;
	}
}
