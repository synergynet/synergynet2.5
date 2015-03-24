package apps.mtdesktop;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;

/**
 * The Class MTDesktopConfigurations.
 */
public class MTDesktopConfigurations {

	/** The clinet connection update. */
	public static int CLINET_CONNECTION_UPDATE = 0;

	/** The default radar image scale. */
	public static float defaultRadarImageScale = 0.3f;

	/** The desktop temp folder. */
	public static String desktopTempFolder = System.getProperty("user.dir")
			+ "/src/main/java/apps/mtdesktop/desktop/temp";

	/** The Constant FTP_SERVLET_PATH. */
	public static final String FTP_SERVLET_PATH = "/ftpServlet";
	
	/** The ftp servlet url. */
	public static String FTP_SERVLET_URL;

	/** The inbox folder. */
	public static String inboxFolder = System.getProperty("user.dir")
			+ "/src/main/java/apps/mtdesktop/desktop/inbox";

	/** The Outbox folder. */
	public static String OutboxFolder = System.getProperty("user.dir")
			+ "/src/main/java/apps/mtdesktop/tabletop/outbox";

	/** The Constant SERVER_PORT. */
	public static final int SERVER_PORT = 8080;
	
	/** The server url. */
	public static String SERVER_URL;

	/** The Constant SITE_PATH. */
	public static final String SITE_PATH = "/FileServer";

	/** The snapshot update delay. */
	public static int SNAPSHOT_UPDATE_DELAY = 1500;

	/** The table search delay. */
	public static int TABLE_SEARCH_DELAY = 5000;
	
	/** The table id. */
	public static TableIdentity tableId = new TableIdentity("green");

	/** The tabletop temp folder. */
	public static String tabletopTempFolder = System.getProperty("user.dir")
			+ "/src/main/java/apps/mtdesktop/tabletop/temp";

	/** The vnc password. */
	public static String vncPassword = "multitouch";

	/** The vnc port. */
	public static int vncPort = 5900;
}
