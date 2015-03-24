package apps.mtdesktop.desktop.inputchannels;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.IOException;

import synergynetframework.appsystem.services.net.rapidnetworkmanager.RapidNetworkManager;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.handlers.MessageProcessor;
import apps.mtdesktop.desktop.DesktopClient;
import apps.mtdesktop.desktop.DesktopFrame;
import apps.mtdesktop.messages.KeyEventMessage;
import apps.mtdesktop.messages.KeyboardRedirectMessage;
import apps.mtdesktop.tabletop.MTTableClient;

/**
 * The Class KeyboardRedirector.
 */
public class KeyboardRedirector implements MessageProcessor {

	/**
	 * The Class MyDispatcher.
	 */
	class MyDispatcher implements KeyEventDispatcher {
		
		/*
		 * (non-Javadoc)
		 * @see
		 * java.awt.KeyEventDispatcher#dispatchKeyEvent(java.awt.event.KeyEvent)
		 */
		@Override
		public boolean dispatchKeyEvent(KeyEvent e) {
			if (e.getID() == KeyEvent.KEY_RELEASED) {
				postKeyEvent(e);
			}
			return false;
		}
	}

	/** The frame. */
	private DesktopFrame frame;

	/** The keyboard redirection enabled. */
	public boolean keyboardRedirectionEnabled = true;
	
	/**
	 * Instantiates a new keyboard redirector.
	 *
	 * @param frame
	 *            the frame
	 */
	public KeyboardRedirector(DesktopFrame frame) {
		KeyboardFocusManager manager = KeyboardFocusManager
				.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(new MyDispatcher());
		this.frame = frame;
	}

	/**
	 * Enable keyboard redirection.
	 *
	 * @param keyboardRedirectionEnabled
	 *            the keyboard redirection enabled
	 */
	public void enableKeyboardRedirection(boolean keyboardRedirectionEnabled) {
		this.keyboardRedirectionEnabled = keyboardRedirectionEnabled;
	}
	
	/**
	 * Checks if is keyboard redirection enabled.
	 *
	 * @return true, if is keyboard redirection enabled
	 */
	public boolean isKeyboardRedirectionEnabled() {
		return keyboardRedirectionEnabled;
	}
	
	/**
	 * Post key event.
	 *
	 * @param keyEvent
	 *            the key event
	 */
	private void postKeyEvent(KeyEvent keyEvent) {
		frame.requestFocusInWindow();
		try {
			if (RapidNetworkManager.getTableCommsClientService() != null) {
				RapidNetworkManager.getTableCommsClientService().sendMessage(
						new KeyEventMessage(MTTableClient.class,
								DesktopClient.tableId, keyEvent));
				Thread.sleep(100);
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.services.net.rapidnetworkmanager.handlers
	 * .MessageProcessor#process(java.lang.Object)
	 */
	@Override
	public void process(Object obj) {
		if (obj instanceof KeyboardRedirectMessage) {
			KeyboardRedirectMessage msg = (KeyboardRedirectMessage) obj;
			this.enableKeyboardRedirection(msg.isKeyboardRedirectionEnabled());
		}
	}
}
