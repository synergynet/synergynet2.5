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

import java.lang.reflect.InvocationTargetException;

import synergynetframework.appsystem.table.appdefinitions.SynergyNetApp;

import com.jmex.game.state.GameStateManager;

/**
 * The Class ApplicationTaskManager.
 */
public class ApplicationTaskManager {
	
	/** The instance. */
	private static ApplicationTaskManager instance;

	/**
	 * Gets the single instance of ApplicationTaskManager.
	 *
	 * @return single instance of ApplicationTaskManager
	 */
	public static ApplicationTaskManager getInstance() {
		synchronized (ApplicationTaskManager.class) {
			if (instance == null) {
				instance = new ApplicationTaskManager();
			}
			return instance;
		}
	}
	
	/** The current app. */
	protected SynergyNetApp currentApp;
	
	/** The registry. */
	protected ApplicationRegistry registry = ApplicationRegistry.getInstance();
	
	/**
	 * Instantiates a new application task manager.
	 */
	private ApplicationTaskManager() {
	}
	
	/**
	 * Activate.
	 *
	 * @param app
	 *            the app
	 */
	private void activate(SynergyNetApp app) {
		if (GameStateManager.getInstance().getChild(app.getName()) == null) {
			GameStateManager.getInstance().attachChild(app);
		}
		app.getMenuController().enableForApplication(app);
		app.setActive(true);
		currentApp = app;
	}
	
	/**
	 * Deactivate.
	 *
	 * @param app
	 *            the app
	 */
	private void deactivate(SynergyNetApp app) {
		if (currentApp == null) {
			return;
		}
		
		app.getMenuController().applicationFinishing();
		app.setActive(false);
		GameStateManager.getInstance().detachChild(app);
		ApplicationInfo info = app.getInfo();
		if (info.getReactivatePolicy().equals(
				ApplicationInfo.REACTIVATEPOLICY_RESTART)) {
			registry.removeInstance(info);
		}
	}
	
	/**
	 * Sets the active application.
	 *
	 * @param info
	 *            the new active application
	 * @throws ApplicationControlError
	 *             the application control error
	 */
	public void setActiveApplication(ApplicationInfo info)
			throws ApplicationControlError {
		if (info == null) {
			throw new ApplicationControlError(
					"Must not supply null application info");
		}
		
		deactivate(currentApp);
		
		try {
			if (info.reactivatePolicy
					.equals(ApplicationInfo.REACTIVATEPOLICY_RESUME)) {
				SynergyNetApp app = registry.getAppInstance(info);
				activate(app);
			} else if (info.reactivatePolicy
					.equals(ApplicationInfo.REACTIVATEPOLICY_RESTART)) {
				SynergyNetApp app = registry.getAppInstance(info);
				if (app.getActivationCount() < 1) {
					activate(app);
				} else {
					app = registry.getFreshAppInstance(info);
					activate(app);
				}
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
