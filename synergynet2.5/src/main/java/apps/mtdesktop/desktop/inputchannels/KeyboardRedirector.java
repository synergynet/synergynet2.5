package apps.mtdesktop.desktop.inputchannels;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.IOException;

import apps.mtdesktop.desktop.DesktopClient;
import apps.mtdesktop.desktop.DesktopFrame;
import apps.mtdesktop.messages.KeyEventMessage;
import apps.mtdesktop.messages.KeyboardRedirectMessage;
import apps.mtdesktop.tabletop.MTTableClient;

import synergynetframework.appsystem.services.net.rapidnetworkmanager.RapidNetworkManager;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.handlers.MessageProcessor;

public class KeyboardRedirector implements MessageProcessor{
	
	public boolean keyboardRedirectionEnabled = true;
	
	private DesktopFrame frame;
	public KeyboardRedirector(DesktopFrame frame){
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
	    manager.addKeyEventDispatcher(new MyDispatcher());
	    this.frame = frame;
	}

	public boolean isKeyboardRedirectionEnabled(){
		return keyboardRedirectionEnabled;
	}
	
	public void enableKeyboardRedirection(boolean keyboardRedirectionEnabled){
		this.keyboardRedirectionEnabled = keyboardRedirectionEnabled;
	}

	class MyDispatcher implements KeyEventDispatcher {
	    
		@Override
	    public boolean dispatchKeyEvent(KeyEvent e) {
			if(e.getID() == KeyEvent.KEY_RELEASED)	
				postKeyEvent(e);
	        return false;
	    }
	}

	private void postKeyEvent(KeyEvent keyEvent){
		frame.requestFocusInWindow();
		try {
			if(RapidNetworkManager.getTableCommsClientService() != null){
				RapidNetworkManager.getTableCommsClientService().sendMessage(new KeyEventMessage(MTTableClient.class, DesktopClient.tableId, keyEvent));
				Thread.sleep(100);
			}
		} catch (IOException e) {
			 
			e.printStackTrace();
		} catch (InterruptedException e) {
			 
			e.printStackTrace();
		}
	}

	@Override
	public void process(Object obj) {
		if(obj instanceof KeyboardRedirectMessage){
			KeyboardRedirectMessage msg = (KeyboardRedirectMessage) obj; 
			this.enableKeyboardRedirection(msg.isKeyboardRedirectionEnabled());
		}
	}
}
