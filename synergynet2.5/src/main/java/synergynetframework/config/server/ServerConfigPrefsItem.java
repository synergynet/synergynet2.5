package synergynetframework.config.server;

import java.util.prefs.Preferences;

import javax.swing.JPanel;

import synergynetframework.config.PreferencesItem;
import core.ConfigurationSystem;

/**
 * The Class ServerConfigPrefsItem.
 */
public class ServerConfigPrefsItem implements PreferencesItem {
	
	/** The Constant prefs. */
	private static final Preferences prefs = ConfigurationSystem
			.getPreferences(ServerConfigPrefsItem.class);

	/** The Constant WEB_SERVER_DIR. */
	private static final String WEB_SERVER_DIR = "WEB_SERVER_DIR";

	/** The Constant WEB_SERVER_PORT. */
	private static final String WEB_SERVER_PORT = "WEB_SERVER_PORT";

	/*
	 * (non-Javadoc)
	 * @see synergynetframework.config.PreferencesItem#getConfigurationPanel()
	 */
	@Override
	public JPanel getConfigurationPanel() {
		return new ServerConfigPrefsPanel(this);
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.config.PreferencesItem#getName()
	 */
	@Override
	public String getName() {
		return "Server";
	}

	/**
	 * Gets the port.
	 *
	 * @return the port
	 */
	public int getPort() {
		return prefs.getInt(WEB_SERVER_PORT, 8080);
	}

	/**
	 * Gets the web directory.
	 *
	 * @return the web directory
	 */
	public String getWebDirectory() {
		return prefs.get(WEB_SERVER_DIR, "/");
	}

	/**
	 * Sets the port.
	 *
	 * @param port
	 *            the new port
	 */
	public void setPort(int port) {
		prefs.putInt(WEB_SERVER_PORT, port);
	}

	/**
	 * Sets the web directory.
	 *
	 * @param s
	 *            the new web directory
	 */
	public void setWebDirectory(String s) {
		prefs.put(WEB_SERVER_DIR, s);
	}
	
}
