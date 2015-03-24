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

package synergynetframework.jme.mtinputbridge.filters;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import synergynetframework.jme.config.AppConfig;
import synergynetframework.jme.cursorsystem.MultiTouchCursorSystem;
import synergynetframework.jme.mtinputbridge.MultiTouchInputFilterManager;
import synergynetframework.jme.pickingsystem.IJMEMultiTouchPicker;
import synergynetframework.jme.pickingsystem.PickSystemException;
import synergynetframework.jme.pickingsystem.data.PickRequest;
import synergynetframework.jme.pickingsystem.data.PickResultData;
import synergynetframework.mtinput.IMultiTouchEventListener;
import synergynetframework.mtinput.IMultiTouchInputFilter;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;
import synergynetframework.mtinput.events.MultiTouchObjectEvent;

import com.jme.math.Vector2f;
import com.jme.scene.Spatial;

/**
 * Logs all actions made on spatials to a log file.
 *
 * @author dcs2ima
 */
public class ElementLoggingFilter implements IMultiTouchInputFilter {
	
	/** The Constant CURSOR_CHANGED. */
	public static final String CURSOR_CHANGED = "CURSOR_CHANGED";

	/** The Constant CURSOR_CLICKED. */
	public static final String CURSOR_CLICKED = "CURSOR_CLICKED";

	/** The Constant CURSOR_PRESSED. */
	public static final String CURSOR_PRESSED = "CURSOR_PRESSED";

	/** The Constant CURSOR_RELEASED. */
	public static final String CURSOR_RELEASED = "CURSOR_RELEASED";

	/** The Constant OBJECT_ADDED. */
	public static final String OBJECT_ADDED = "OBJECT_ADDED";

	/** The Constant OBJECT_CHANGED. */
	public static final String OBJECT_CHANGED = "OBJECT_CHANGED";

	/** The Constant OBJECT_REMOVED. */
	public static final String OBJECT_REMOVED = "OBJECT_REMOVED";

	/** The next. */
	private IMultiTouchEventListener next;

	/** The pick system. */
	private IJMEMultiTouchPicker pickSystem;

	/** The pw. */
	private PrintWriter pw;

	/** The record file. */
	private File recordFile;

	/**
	 * Instantiates a new element logging filter.
	 *
	 * @throws FileNotFoundException
	 *             the file not found exception
	 */
	public ElementLoggingFilter() throws FileNotFoundException {
		if (!AppConfig.recordTableDir.exists()) {
			AppConfig.recordTableDir.mkdir();
		}
		recordFile = new File(AppConfig.recordTableDir, getFileNameFromDate());
		pw = new PrintWriter(new FileOutputStream(recordFile));
		writeFileHeader(MultiTouchInputFilterManager.getInstance()
				.getLoggingClass());
		this.pickSystem = MultiTouchInputFilterManager.getInstance()
				.getPickingSystem();
	}

	/*
	 * (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#cursorChanged(
	 * synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	public void cursorChanged(MultiTouchCursorEvent event) {
		Spatial spatial = getPickedSpatial(event);
		if (spatial != null) {
			pw.print(System.nanoTime() + ",");
			pw.print(CURSOR_CHANGED + ",");
			Vector2f eventPosition = MultiTouchCursorSystem.tableToScreen(event
					.getPosition());
			pw.print(eventPosition.x + ",");
			pw.print(eventPosition.y + ",");
			pw.print(spatial.getName() + ",");
			pw.print(spatial.getLocalTranslation().x + ",");
			pw.print(spatial.getLocalTranslation().y + ",");
			pw.print(spatial.getLocalTranslation().z + ",");
			pw.print(spatial.getLocalScale().x + ",");
			pw.print(spatial.getLocalScale().y + ",");
			pw.print(spatial.getLocalScale().z + ",");
			pw.print(spatial.getLocalRotation().x + ",");
			pw.print(spatial.getLocalRotation().y + ",");
			pw.print(spatial.getLocalRotation().z + ",");
			pw.println(spatial.getLocalRotation().w);
			pw.flush();
		}
		next.cursorChanged(event);
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#cursorClicked(
	 * synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	public void cursorClicked(MultiTouchCursorEvent event) {
		Spatial spatial = getPickedSpatial(event);
		if (spatial != null) {
			pw.print(System.nanoTime() + ",");
			pw.print(CURSOR_CLICKED + ",");
			Vector2f eventPosition = MultiTouchCursorSystem.tableToScreen(event
					.getPosition());
			pw.print(eventPosition.x + ",");
			pw.print(eventPosition.y + ",");
			pw.print(spatial.getName() + ",");
			pw.print(spatial.getLocalTranslation().x + ",");
			pw.print(spatial.getLocalTranslation().y + ",");
			pw.print(spatial.getLocalTranslation().z + ",");
			pw.print(spatial.getLocalScale().x + ",");
			pw.print(spatial.getLocalScale().y + ",");
			pw.print(spatial.getLocalScale().z + ",");
			pw.print(spatial.getLocalRotation().x + ",");
			pw.print(spatial.getLocalRotation().y + ",");
			pw.print(spatial.getLocalRotation().z + ",");
			pw.println(spatial.getLocalRotation().w);
			pw.flush();
		}
		next.cursorClicked(event);
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#cursorPressed(
	 * synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	public void cursorPressed(MultiTouchCursorEvent event) {
		Spatial spatial = getPickedSpatial(event);
		if (spatial != null) {
			pw.print(System.nanoTime() + ",");
			pw.print(CURSOR_PRESSED + ",");
			Vector2f eventPosition = MultiTouchCursorSystem.tableToScreen(event
					.getPosition());
			pw.print(eventPosition.x + ",");
			pw.print(eventPosition.y + ",");
			pw.print(spatial.getName() + ",");
			pw.print(spatial.getLocalTranslation().x + ",");
			pw.print(spatial.getLocalTranslation().y + ",");
			pw.print(spatial.getLocalTranslation().z + ",");
			pw.print(spatial.getLocalScale().x + ",");
			pw.print(spatial.getLocalScale().y + ",");
			pw.print(spatial.getLocalScale().z + ",");
			pw.print(spatial.getLocalRotation().x + ",");
			pw.print(spatial.getLocalRotation().y + ",");
			pw.print(spatial.getLocalRotation().z + ",");
			pw.println(spatial.getLocalRotation().w);
			pw.flush();
		}
		next.cursorPressed(event);
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#cursorReleased(
	 * synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	public void cursorReleased(MultiTouchCursorEvent event) {
		Spatial spatial = getPickedSpatial(event);
		if (spatial != null) {
			pw.print(System.nanoTime() + ",");
			pw.print(CURSOR_RELEASED + ",");
			Vector2f eventPosition = MultiTouchCursorSystem.tableToScreen(event
					.getPosition());
			pw.print(eventPosition.x + ",");
			pw.print(eventPosition.y + ",");
			pw.print(spatial.getName() + ",");
			pw.print(spatial.getLocalTranslation().x + ",");
			pw.print(spatial.getLocalTranslation().y + ",");
			pw.print(spatial.getLocalTranslation().z + ",");
			pw.print(spatial.getLocalScale().x + ",");
			pw.print(spatial.getLocalScale().y + ",");
			pw.print(spatial.getLocalScale().z + ",");
			pw.print(spatial.getLocalRotation().x + ",");
			pw.print(spatial.getLocalRotation().y + ",");
			pw.print(spatial.getLocalRotation().z + ",");
			pw.println(spatial.getLocalRotation().w);
			pw.flush();
		}
		next.cursorReleased(event);
	}
	
	/**
	 * Gets the file name from date.
	 *
	 * @return the file name from date
	 */
	private String getFileNameFromDate() {
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd HH.mm.ss.S");
		return "Element_Log_" + formatter.format(new Date());
	}
	
	/**
	 * Gets the picked spatial.
	 *
	 * @param event
	 *            the event
	 * @return the picked spatial
	 */
	private Spatial getPickedSpatial(MultiTouchCursorEvent event) {
		PickRequest req = new PickRequest(event.getCursorID(),
				MultiTouchCursorSystem.tableToScreen(event.getPosition()));
		List<PickResultData> pickResults;
		try {
			pickResults = pickSystem.doPick(req);
			for (PickResultData pr : pickResults) {
				return pr.getPickedSpatial();
			}
		} catch (PickSystemException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#objectAdded(
	 * synergynetframework.mtinput.events.MultiTouchObjectEvent)
	 */
	public void objectAdded(MultiTouchObjectEvent event) {
		next.objectAdded(event);
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#objectChanged(
	 * synergynetframework.mtinput.events.MultiTouchObjectEvent)
	 */
	public void objectChanged(MultiTouchObjectEvent event) {
		next.objectChanged(event);
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#objectRemoved(
	 * synergynetframework.mtinput.events.MultiTouchObjectEvent)
	 */
	public void objectRemoved(MultiTouchObjectEvent event) {
		next.objectRemoved(event);
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchInputFilter#setNext(
	 * synergynetframework.mtinput.IMultiTouchEventListener)
	 */
	public void setNext(IMultiTouchEventListener el) {
		this.next = el;
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchInputFilter#update(float)
	 */
	public void update(float tpf) {
	}

	/**
	 * Write file header.
	 *
	 * @param appClass
	 *            the app class
	 */
	private void writeFileHeader(Class<?> appClass) {
		pw.println("# Recorded from ElementLoggingFilter v0.1");
		pw.println("# App: " + appClass.getName());
		pw.println("# Recording started at " + new Date().toString());
		pw.println("# Format is as follows:");
		pw.println("# System.nanoTime(), event type, screen_x , screen_y, picked spatial name, translation_x, translation_y, translation_z, scale_x, scale_y, scale_z, rotation_x, rotation_y, rotation_z, rotation_w");
	}
}
