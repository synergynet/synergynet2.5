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


public class WebServer implements Runnable{

	private FtpServerServlet ftpservlet;
	public Server server;
	public ServletContextHandler servletcontext;
	
	public WebServer() throws UnknownHostException {
		MTDesktopConfigurations.SERVER_URL = "http://"+InetAddress.getLocalHost().getHostAddress()+":"+ MTDesktopConfigurations.SERVER_PORT;
		MTDesktopConfigurations.FTP_SERVLET_URL = MTDesktopConfigurations.SERVER_URL + MTDesktopConfigurations.FTP_SERVLET_PATH;

		
		server = new Server();
        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(MTDesktopConfigurations.SERVER_PORT);
        connector.setThreadPool(new QueuedThreadPool(20));
        server.setConnectors(new Connector[]{connector});
     
        WebAppContext webappcontext = new WebAppContext();
		webappcontext.setContextPath(MTDesktopConfigurations.SITE_PATH);
		webappcontext.setResourceBase(MTDesktopConfigurations.OutboxFolder);
		Map<String,String> params = new HashMap<String, String>();
        params.put("dirAllowed", "false");
        webappcontext.setInitParams(params);
        
        
        servletcontext = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletcontext.setContextPath("/");
        ftpservlet = new FtpServerServlet();
        servletcontext.addServlet(new ServletHolder(ftpservlet), MTDesktopConfigurations.FTP_SERVLET_PATH);
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{webappcontext, servletcontext});
        server.setHandler(handlers);
        
		File outboxFolder = new File(MTDesktopConfigurations.OutboxFolder);
		if(!outboxFolder.exists()) outboxFolder.mkdir();
	}
	
	
	
	public FtpServerServlet getFtpServerServlet(){
		return ftpservlet;
	}
	
	public static void main(String args[]){

	}



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
