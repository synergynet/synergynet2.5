package apps.mtdesktop.tabletop.fileserver;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

import apps.mtdesktop.MTDesktopConfigurations;

/**
 * The Class WebServer.
 */
public class WebServer implements Runnable {
	
	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String args[]) {
		
	}

	/** The ftpservlet. */
	private FtpServerServlet ftpservlet;

	/** The server. */
	public Server server;

	/** The servletcontext. */
	public ServletContextHandler servletcontext;
	
	/**
	 * Instantiates a new web server.
	 *
	 * @throws UnknownHostException
	 *             the unknown host exception
	 */
	public WebServer() throws UnknownHostException {
		MTDesktopConfigurations.SERVER_URL = "http://"
				+ InetAddress.getLocalHost().getHostAddress() + ":"
				+ MTDesktopConfigurations.SERVER_PORT;
		MTDesktopConfigurations.FTP_SERVLET_URL = MTDesktopConfigurations.SERVER_URL
				+ MTDesktopConfigurations.FTP_SERVLET_PATH;
		
		server = new Server();
		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setPort(MTDesktopConfigurations.SERVER_PORT);
		connector.setThreadPool(new QueuedThreadPool(20));
		server.setConnectors(new Connector[] { connector });
		
		WebAppContext webappcontext = new WebAppContext();
		webappcontext.setContextPath(MTDesktopConfigurations.SITE_PATH);
		webappcontext.setResourceBase(MTDesktopConfigurations.OutboxFolder);
		Map<String, String> params = new HashMap<String, String>();
		params.put("dirAllowed", "false");
		webappcontext.setInitParams(params);
		
		servletcontext = new ServletContextHandler(
				ServletContextHandler.SESSIONS);
		servletcontext.setContextPath("/");
		ftpservlet = new FtpServerServlet();
		servletcontext.addServlet(new ServletHolder(ftpservlet),
				MTDesktopConfigurations.FTP_SERVLET_PATH);
		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { webappcontext, servletcontext });
		server.setHandler(handlers);
		
		File outboxFolder = new File(MTDesktopConfigurations.OutboxFolder);
		if (!outboxFolder.exists()) {
			outboxFolder.mkdir();
		}
	}

	/**
	 * Gets the ftp server servlet.
	 *
	 * @return the ftp server servlet
	 */
	public FtpServerServlet getFtpServerServlet() {
		return ftpservlet;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			server.start();
			server.join();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
}
