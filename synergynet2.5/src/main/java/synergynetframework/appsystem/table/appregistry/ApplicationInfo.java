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

package synergynetframework.appsystem.table.appregistry;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.w3c.dom.Document;

import synergynetframework.appsystem.table.appdefinitions.SynergyNetApp;



public class ApplicationInfo {

	public static final String REACTIVATEPOLICY_RESTART = "restart";
	public static final String REACTIVATEPOLICY_RESUME = "resume";
	
	public static final String APPLICATION_TYPE_CLIENT = "client";
	public static final String APPLICATION_TYPE_CONTROLLER = "controller";
	public static final String APPLICATION_TYPE_PROJECTOR = "projector";

	protected Class<?> theClass;
	protected Document document;
	protected String reactivatePolicy = REACTIVATEPOLICY_RESTART;
	private String iconResource;
	protected boolean showIcon = true;
	protected String applicationName;
	protected String uuid;
	protected String applicationType;
	private String versionString;
	
	public ApplicationInfo(String classname, String applicationName, String versionString, String reactivatePolicy) throws ClassNotFoundException {
		this.theClass = Class.forName(classname);		
		this.applicationName = applicationName;
		this.reactivatePolicy = reactivatePolicy;
		this.versionString = versionString;
	}

	public String getApplicationType() {
		return applicationType;
	}

	public void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
	}

	public SynergyNetApp getNewInstance() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
		Constructor<?> c = theClass.getConstructor(ApplicationInfo.class);
		SynergyNetApp app = (SynergyNetApp) c.newInstance(this);
		app.init();
		return app;
	}

	/**
	 * The class for the SynergyNet application this ApplicationInfo represents.
	 * @return
	 */
	public Class<?> getTheClass() {
		return theClass;
	}

	/**
	 * Equivalent to calling getTheClass().getName()
	 * @return
	 */
	public String getTheClassName() {
		return theClass.getName();
	}

	public String getApplicationName() {		
		return applicationName;
	}

	public String getIconResourceName() {
		return iconResource;
	}

	public boolean showInMenus() {
		return showIcon;
	}

	public String getReactivatePolicy() {
		return reactivatePolicy;
	}

	public void setShowIcon(boolean b) {
		showIcon = b;
	}

	public void setIconResource(String iconResource) {
		this.iconResource = iconResource;
	}


	public void setUUID(String uuid) {
		this.uuid = uuid;		
	}

	public String toString() {
		return getTheClassName() + ":\"" + getApplicationName() + "\"";
	}

	public String getApplicationVersion() {
		return versionString;
	}
}
