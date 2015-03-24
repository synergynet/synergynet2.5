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

package apps.control;

import java.io.IOException;
import java.util.Collection;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.services.ServiceManager;
import synergynetframework.appsystem.services.exceptions.CouldNotStartServiceException;
import synergynetframework.appsystem.services.net.tablecomms.client.TableCommsApplicationListener;
import synergynetframework.appsystem.services.net.tablecomms.client.TableCommsClientService;
import synergynetframework.appsystem.services.tablecontrolclient.messages.ChangeApplicationMessage;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.appsystem.table.appregistry.ApplicationControlError;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.appsystem.table.appregistry.ApplicationRegistry;
import synergynetframework.appsystem.table.appregistry.ApplicationTaskManager;
import synergynetframework.appsystem.table.appregistry.menucontrol.HoldTopRightConfirmVisualExit;

/**
 * The Class ControlApp.
 */
public class ControlApp extends DefaultSynergyNetApp
		implements
		synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonListener,
		TableCommsApplicationListener {
	
	/** The comms. */
	private TableCommsClientService comms;
	// private Map<String,SimpleButton> buttons = new
	// HashMap<String,SimpleButton>();

	/** The content. */
	private ContentSystem content;
	
	/**
	 * Instantiates a new control app.
	 *
	 * @param info
	 *            the info
	 */
	public ControlApp(ApplicationInfo info) {
		super(info);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#addContent
	 * ()
	 */
	@Override
	public void addContent() {
		
		content = ContentSystem.getContentSystemForSynergyNetApp(this);

		setMenuController(new HoldTopRightConfirmVisualExit(this));

		int y = 50;
		int x = 100;

		Collection<ApplicationInfo> apps = ApplicationRegistry.getInstance()
				.getAllInfo();
		for (ApplicationInfo i : apps) {
			SimpleButton b = (SimpleButton) content
					.createContentItem(SimpleButton.class);
			b.setText(i.getApplicationName());
			b.addButtonListener(this);
			b.setLocalLocation(x, y);
			y += 30;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.listener.
	 * SimpleButtonListener
	 * #buttonClicked(synergynetframework.appsystem.contentsystem
	 * .items.SimpleButton, long, float, float, float)
	 */
	public void buttonClicked(SimpleButton button, long id, float x, float y,
			float pressure) {
		String appName = button.getText();

		try {
			comms.sendMessage(new ChangeApplicationMessage(appName));
		} catch (IOException e) {
			e.printStackTrace();
		}

		ApplicationInfo controller = ApplicationRegistry.getInstance()
				.getInfoForName(appName,
						ApplicationInfo.APPLICATION_TYPE_CONTROLLER);
		if (controller != null) {
			try {
				ApplicationTaskManager.getInstance().setActiveApplication(
						controller);
			} catch (ApplicationControlError e) {
				
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.listener.
	 * SimpleButtonListener
	 * #buttonDragged(synergynetframework.appsystem.contentsystem
	 * .items.SimpleButton, long, float, float, float)
	 */
	@Override
	public void buttonDragged(SimpleButton b, long id, float x, float y,
			float pressure) {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.listener.
	 * SimpleButtonListener
	 * #buttonPressed(synergynetframework.appsystem.contentsystem
	 * .items.SimpleButton, long, float, float, float)
	 */
	@Override
	public void buttonPressed(SimpleButton b, long id, float x, float y,
			float pressure) {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.listener.
	 * SimpleButtonListener
	 * #buttonReleased(synergynetframework.appsystem.contentsystem
	 * .items.SimpleButton, long, float, float, float)
	 */
	@Override
	public void buttonReleased(SimpleButton b, long id, float x, float y,
			float pressure) {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.services.net.tablecomms.client.
	 * TableCommsApplicationListener#messageReceived(java.lang.Object)
	 */
	public void messageReceived(Object obj) {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#onActivate
	 * ()
	 */
	@Override
	public void onActivate() {
		if (comms == null) {
			try {
				comms = (TableCommsClientService) ServiceManager.getInstance()
						.get(TableCommsClientService.class);
			} catch (CouldNotStartServiceException e1) {
				e1.printStackTrace();
			}
			try {
				comms.register(ControlApp.class.getName(), this);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.services.net.tablecomms.client.
	 * TableCommsApplicationListener#tableDisconnected()
	 */
	@Override
	public void tableDisconnected() {
		
	}
}
