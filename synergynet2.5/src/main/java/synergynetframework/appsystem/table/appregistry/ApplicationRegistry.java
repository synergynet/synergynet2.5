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

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import synergynetframework.appsystem.table.appdefinitions.SynergyNetApp;



public class ApplicationRegistry {
	private static final Logger log = Logger.getLogger(ApplicationRegistry.class.getName());

	private static ApplicationRegistry instance;

	protected Map<String,ApplicationInfo> clientApplications = new HashMap<String,ApplicationInfo>();	
	protected Map<String,ApplicationInfo> controllerApplications = new HashMap<String,ApplicationInfo>();
	protected Map<String,ApplicationInfo> projectorApplications = new HashMap<String,ApplicationInfo>();
	protected Map<String,SynergyNetApp> clientInstances = new HashMap<String,SynergyNetApp>();
	protected Map<String,SynergyNetApp> controllerInstances = new HashMap<String,SynergyNetApp>();
	protected Map<String,SynergyNetApp> projectorInstances = new HashMap<String,SynergyNetApp>();
	
	private ApplicationInfo defaultApp;

	public static ApplicationRegistry getInstance() {
		if(instance == null) instance = new ApplicationRegistry();
		return instance;
	}



	private ApplicationRegistry() {}

	public void register(ApplicationInfo info) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		log.info("Attempting to register " + info.getApplicationName());
		if(info.getApplicationType().equals(ApplicationInfo.APPLICATION_TYPE_CLIENT)) {
			clientApplications.put(info.getApplicationName(), info);
			log.info("Succesfully registered " + info.getApplicationName() + " as a client application.");
		}else if(info.getApplicationType().equals(ApplicationInfo.APPLICATION_TYPE_CONTROLLER)) {
			controllerApplications.put(info.getApplicationName(), info);
			log.info("Succesfully registered " + info.getApplicationName() + " as a controller application");
		}else if(info.getApplicationType().equals(ApplicationInfo.APPLICATION_TYPE_PROJECTOR)) {
			projectorApplications.put(info.getApplicationName(), info);
			log.info("Succesfully registered " + info.getApplicationName() + " as a projector application");
		}
		
	}

	public ApplicationInfo getInfoForClassName(String classname) {
		for(ApplicationInfo ai : clientApplications.values()) {
			if(ai.getTheClassName().equals(classname)) return ai;
		}
		for(ApplicationInfo ai : controllerApplications.values()) {
			if(ai.getTheClassName().equals(classname)) return ai;
		}
		for(ApplicationInfo ai : projectorApplications.values()) {
			if(ai.getTheClassName().equals(classname)) return ai;
		}
		return null;
	}

	public void setDefault(String classname) {
		ApplicationInfo ai = getInfoForClassName(classname);
		this.setDefaultApp(ai);		
	}

	public void setDefaultApp(ApplicationInfo defaultApp) {
		this.defaultApp = defaultApp;
	}

	public ApplicationInfo getDefaultApp() {
		return defaultApp;
	}

	public ApplicationInfo getInfoForName(String appName, String type) {
		if(type.equals(ApplicationInfo.APPLICATION_TYPE_CLIENT)) {
			return clientApplications.get(appName);
		}		
		else if(type.equals(ApplicationInfo.APPLICATION_TYPE_CONTROLLER)) {
			return controllerApplications.get(appName);
		}else{
			return projectorApplications.get(appName);
		}
	}

	public Collection<ApplicationInfo> getAllInfo() {
		Set<ApplicationInfo> s = new HashSet<ApplicationInfo>();
		s.addAll(clientApplications.values());
		s.addAll(controllerApplications.values());
		s.addAll(projectorApplications.values());
		return s;
	}

	public SynergyNetApp getAppInstance(ApplicationInfo ai) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SecurityException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException {
		if(ai.getApplicationType().equals(ApplicationInfo.APPLICATION_TYPE_CLIENT)) {
			return getClientAppInstance(ai);
		}
		else if(ai.getApplicationType().equals(ApplicationInfo.APPLICATION_TYPE_CONTROLLER)) {
			return getControllerAppInstance(ai);
		} else
			return getProjectorAppInstance(ai);
	}
	
	public SynergyNetApp getClientAppInstance(ApplicationInfo info) throws SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException {
		return getAppInstance(clientInstances, info);
	}
	
	public SynergyNetApp getControllerAppInstance(ApplicationInfo info) throws SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException {
		return getAppInstance(controllerInstances, info);
	}
	
	public SynergyNetApp getProjectorAppInstance(ApplicationInfo info) throws SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException {
		return getAppInstance(projectorInstances, info);
	}
	
	public SynergyNetApp getAppInstance(Map<String,SynergyNetApp> map, ApplicationInfo info) throws SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException {
		if(map == null) return null;
		SynergyNetApp app = map.get(info.getApplicationName());
		if(app == null) {
			app = info.getNewInstance();
			app.setName(info.getApplicationName());
			clientInstances.put(app.getName(), app);
		}
		return app;
	}

	public SynergyNetApp getFreshAppInstance(ApplicationInfo ai) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SecurityException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException {
		removeInstance(ai);
		SynergyNetApp app = ai.getNewInstance();
		app.setName(ai.getApplicationName());
		if(ai.getApplicationType().equals(ApplicationInfo.APPLICATION_TYPE_CLIENT))
			clientInstances.put(app.getName(), app);
		else if(ai.getApplicationType().equals(ApplicationInfo.APPLICATION_TYPE_CONTROLLER))
			controllerInstances.put(app.getName(), app);
		else
			projectorInstances.put(app.getName(), app);
		return app;
	}

	public void removeInstance(ApplicationInfo info) {
		if(info.getApplicationType().equals(ApplicationInfo.APPLICATION_TYPE_CLIENT))
			clientInstances.remove(info);
		if(info.getApplicationType().equals(ApplicationInfo.APPLICATION_TYPE_CONTROLLER))
			controllerInstances.remove(info);
		else
			projectorInstances.remove(info);
		System.gc();
	}

	public String toString() {
		return clientApplications.toString() + " AND " + controllerApplications.toString();
	}
}
