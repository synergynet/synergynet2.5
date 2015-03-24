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

package synergynetframework.appsystem.table.appregistry;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.w3c.dom.Document;

import synergynetframework.appsystem.table.appdefinitions.SynergyNetApp;

/**
 * The Class ApplicationInfo.
 */
public class ApplicationInfo {
	
	/** The Constant APPLICATION_TYPE_CLIENT. */
	public static final String APPLICATION_TYPE_CLIENT = "client";

	/** The Constant APPLICATION_TYPE_CONTROLLER. */
	public static final String APPLICATION_TYPE_CONTROLLER = "controller";

	/** The Constant APPLICATION_TYPE_PROJECTOR. */
	public static final String APPLICATION_TYPE_PROJECTOR = "projector";

	/** The Constant REACTIVATEPOLICY_RESTART. */
	public static final String REACTIVATEPOLICY_RESTART = "restart";

	/** The Constant REACTIVATEPOLICY_RESUME. */
	public static final String REACTIVATEPOLICY_RESUME = "resume";
	
	/** The application name. */
	protected String applicationName;

	/** The application type. */
	protected String applicationType;

	/** The document. */
	protected Document document;

	/** The icon resource. */
	private String iconResource;

	/** The reactivate policy. */
	protected String reactivatePolicy = REACTIVATEPOLICY_RESTART;

	/** The show icon. */
	protected boolean showIcon = true;

	/** The class. */
	protected Class<?> theClass;

	/** The uuid. */
	protected String uuid;

	/** The version string. */
	private String versionString;

	/**
	 * Instantiates a new application info.
	 *
	 * @param classname
	 *            the classname
	 * @param applicationName
	 *            the application name
	 * @param versionString
	 *            the version string
	 * @param reactivatePolicy
	 *            the reactivate policy
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 */
	public ApplicationInfo(String classname, String applicationName,
			String versionString, String reactivatePolicy)
			throws ClassNotFoundException {
		this.theClass = Class.forName(classname);
		this.applicationName = applicationName;
		this.reactivatePolicy = reactivatePolicy;
		this.versionString = versionString;
	}
	
	/**
	 * Gets the application name.
	 *
	 * @return the application name
	 */
	public String getApplicationName() {
		return applicationName;
	}
	
	/**
	 * Gets the application type.
	 *
	 * @return the application type
	 */
	public String getApplicationType() {
		return applicationType;
	}
	
	/**
	 * Gets the application version.
	 *
	 * @return the application version
	 */
	public String getApplicationVersion() {
		return versionString;
	}
	
	/**
	 * Gets the icon resource name.
	 *
	 * @return the icon resource name
	 */
	public String getIconResourceName() {
		return iconResource;
	}
	
	/**
	 * Gets the new instance.
	 *
	 * @return the new instance
	 * @throws InstantiationException
	 *             the instantiation exception
	 * @throws IllegalAccessException
	 *             the illegal access exception
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 * @throws SecurityException
	 *             the security exception
	 * @throws NoSuchMethodException
	 *             the no such method exception
	 * @throws IllegalArgumentException
	 *             the illegal argument exception
	 * @throws InvocationTargetException
	 *             the invocation target exception
	 */
	public SynergyNetApp getNewInstance() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			InvocationTargetException {
		Constructor<?> c = theClass.getConstructor(ApplicationInfo.class);
		SynergyNetApp app = (SynergyNetApp) c.newInstance(this);
		app.init();
		return app;
	}
	
	/**
	 * Gets the reactivate policy.
	 *
	 * @return the reactivate policy
	 */
	public String getReactivatePolicy() {
		return reactivatePolicy;
	}
	
	/**
	 * The class for the SynergyNet application this ApplicationInfo represents.
	 *
	 * @return the the class
	 */
	public Class<?> getTheClass() {
		return theClass;
	}
	
	/**
	 * Equivalent to calling getTheClass().getName()
	 *
	 * @return the the class name
	 */
	public String getTheClassName() {
		return theClass.getName();
	}
	
	/**
	 * Sets the application type.
	 *
	 * @param applicationType
	 *            the new application type
	 */
	public void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
	}
	
	/**
	 * Sets the icon resource.
	 *
	 * @param iconResource
	 *            the new icon resource
	 */
	public void setIconResource(String iconResource) {
		this.iconResource = iconResource;
	}
	
	/**
	 * Sets the show icon.
	 *
	 * @param b
	 *            the new show icon
	 */
	public void setShowIcon(boolean b) {
		showIcon = b;
	}
	
	/**
	 * Sets the uuid.
	 *
	 * @param uuid
	 *            the new uuid
	 */
	public void setUUID(String uuid) {
		this.uuid = uuid;
	}
	
	/**
	 * Show in menus.
	 *
	 * @return true, if successful
	 */
	public boolean showInMenus() {
		return showIcon;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getTheClassName() + ":\"" + getApplicationName() + "\"";
	}
}
