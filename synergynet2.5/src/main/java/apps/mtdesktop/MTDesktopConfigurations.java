package apps.mtdesktop;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;

public class MTDesktopConfigurations {
	public static final int SERVER_PORT = 8080;
	public static String SERVER_URL;
	public static final String SITE_PATH = "/FileServer";
	public static final String FTP_SERVLET_PATH = "/ftpServlet";	
	public static String FTP_SERVLET_URL;
	public static String OutboxFolder = System.getProperty("user.dir") + "/src/main/java/apps/mtdesktop/tabletop/outbox";
	
	public static int TABLE_SEARCH_DELAY = 5000;
	public static String inboxFolder = System.getProperty("user.dir") + "/src/main/java/apps/mtdesktop/desktop/inbox"; 
	public static int vncPort = 5900;
	public static String vncPassword = "multitouch";
	
	public static String tabletopTempFolder = System.getProperty("user.dir") + "/src/main/java/apps/mtdesktop/tabletop/temp";
	public static String desktopTempFolder = System.getProperty("user.dir") + "/src/main/java/apps/mtdesktop/desktop/temp";

	public static float defaultRadarImageScale = 0.3f;
	public static int CLINET_CONNECTION_UPDATE = 0;
	public static int SNAPSHOT_UPDATE_DELAY = 1500;
	public static TableIdentity tableId = new TableIdentity("green");
}
