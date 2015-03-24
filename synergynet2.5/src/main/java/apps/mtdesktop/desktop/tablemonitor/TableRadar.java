package apps.mtdesktop.desktop.tablemonitor;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.imageio.ImageIO;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import apps.mtdesktop.MTDesktopConfigurations;
import apps.mtdesktop.desktop.DesktopClient;
import apps.mtdesktop.fileutility.AssetRegistry;
import apps.mtdesktop.fileutility.FileTransferListener;
import apps.mtdesktop.messages.MouseEventsMessage;
import apps.mtdesktop.messages.MouseRedirectMessage;
import apps.mtdesktop.messages.util.MouseEventInfo;
import apps.mtdesktop.tabletop.MTTableClient;

import synergynetframework.appsystem.services.net.rapidnetworkmanager.RapidNetworkManager;

public class TableRadar extends JDialog implements MouseListener, MouseMotionListener, FileTransferListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected JMenuBar menuBar;
	protected JMenu menu;
	protected JMenuItem menuItem;
	
	private BufferedImage bImageFromConvert;
	private boolean dispatchMouseState = false;
	private boolean enableSnapShotDownload = true;
	public static final int DISPATCH_MOUSE_DELAY = 100;
	
	public static int TOP_HEIGHT = 55;
	public static int BORDER_WIDTH = 10;

	private Queue<MouseEventInfo> mouseEvents = new ConcurrentLinkedQueue<MouseEventInfo>();
	public TableRadar(){
		this.setTitle("Tabletop");
		this.setModal(false);
		int width = (int) (MTDesktopConfigurations.defaultRadarImageScale * 1024); 
		int height = (int)(MTDesktopConfigurations.defaultRadarImageScale * 768);
		if(DesktopClient.currentPosition.equals(DesktopClient.Position.EAST) || DesktopClient.currentPosition.equals(DesktopClient.Position.WEST)){
			int temp = width;
			width = height;
			height = temp;
		}
		this.setSize(width+BORDER_WIDTH, height+TOP_HEIGHT);
		this.setResizable(false);
		DrawComponent drawComponent = new DrawComponent();
		this.getContentPane().add(drawComponent);
		
		menuBar = new JMenuBar();
		menu = new JMenu("Input");
		
		menuItem = new JCheckBoxMenuItem("Enable Mouse on Table");
		menu.add(menuItem);
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
		
		menuItem.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent e) {
				MouseRedirectMessage msg = new MouseRedirectMessage(MTTableClient.class, DesktopClient.tableId, false);
				if(e.getStateChange() == ItemEvent.SELECTED){
					msg.enableMouseRedirection(true);
					dispatchMouseState = true;
				}else{
					msg.enableMouseRedirection(false);
					dispatchMouseState = false;
				}
				try {
					if(RapidNetworkManager.getTableCommsClientService() != null)	
						RapidNetworkManager.getTableCommsClientService().sendMessage(msg);
				} catch (IOException e1) {
					 
					e1.printStackTrace();
				}
			}
			
		});
		
	    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	    int w = this.getSize().width;
	    int h = this.getSize().height;
	    int x = (dim.width-w)/2;
	    int y = (dim.height-h)/2;
	    
	    this.setLocation(x, y);
	    
		this.setVisible(true);
		
	    WindowListener wndCloser = new WindowAdapter(){
		      public void windowClosing(WindowEvent e){
		    	  enableSnapShotDownload = false;
		    	  dispatchMouseState = false;
		    	  AssetRegistry.getInstance().removeFileTransferListener(TableRadar.this);
		    	  TableRadar.this.dispose();
		    }};
		
		this.getContentPane().addMouseListener(this);
		this.getContentPane().addMouseMotionListener(this);
		AssetRegistry.getInstance().addFileTransferListener(this);
		new Thread(new MouseEventDispatcher()).start();
		new Thread(new SnapshotDownloader()).start();
		addWindowListener(wndCloser);
		
	}
	
	  public class DrawComponent extends JComponent{
	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void paint(Graphics g){
	    	if(bImageFromConvert != null){
	    		BufferedImage rotatedBI = null;
	    		if(DesktopClient.currentPosition.equals(DesktopClient.Position.SOUTH)){
	    			rotatedBI = bImageFromConvert;
	    		}else if(DesktopClient.currentPosition.equals(DesktopClient.Position.EAST)){
	    			rotatedBI = rotate(bImageFromConvert, 90 * Math.PI/180);
	    		}else if(DesktopClient.currentPosition.equals(DesktopClient.Position.NORTH)){
	    			rotatedBI = rotate(bImageFromConvert, 90 * Math.PI/180);
	    			rotatedBI = rotate(rotatedBI, 90 * Math.PI/180);
	    		}else if(DesktopClient.currentPosition.equals(DesktopClient.Position.WEST)){
	    			rotatedBI = rotate(bImageFromConvert, 90 * Math.PI/180);
	    			rotatedBI = rotate(rotatedBI, 90 * Math.PI/180);
	    			rotatedBI = rotate(rotatedBI, 90 * Math.PI/180);
	    		}
	    		if(rotatedBI != null){
	    		    g.drawImage(rotatedBI, 0, 0, null);
	    		    rotatedBI.flush();
	    		}
    			bImageFromConvert.flush();
		    	bImageFromConvert = null;
	    	}
	   }
	}


	@Override
	public void mouseDragged(MouseEvent e) {
		Point.Float p = desktopToTable(e.getX(), e.getY());
		mouseEvents.add(new MouseEventInfo(e.getID(), p.x, p.y));
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Point.Float p = desktopToTable(e.getX(), e.getY());
		mouseEvents.add(new MouseEventInfo(e.getID(), p.x, p.y));
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
		 
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		Point.Float p = desktopToTable(e.getX(), e.getY());
		mouseEvents.add(new MouseEventInfo(e.getID(), p.x, p.y));
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		Point.Float p = desktopToTable(e.getX(), e.getY());
		mouseEvents.add(new MouseEventInfo(e.getID(), p.x, p.y));
	}
	
	private Point.Float desktopToTable(int x, int y) {
		float tx = x / MTDesktopConfigurations.defaultRadarImageScale;
		float ty = y / MTDesktopConfigurations.defaultRadarImageScale;
		ty = 768 - ty;
		
		if(DesktopClient.currentPosition.equals(DesktopClient.Position.SOUTH)){
		}else if(DesktopClient.currentPosition.equals(DesktopClient.Position.NORTH)){
			tx = 1024 - tx;
			ty = 768 - ty;
		}else if(DesktopClient.currentPosition.equals(DesktopClient.Position.EAST)){
			float temp = ty;
			ty = tx;
			tx = 768-temp;
		}else if(DesktopClient.currentPosition.equals(DesktopClient.Position.WEST)){
			float temp = ty;
			ty = 768-tx;
			tx = temp + 1024 - 768;
		}
		return new Point.Float(tx, ty);
	}
	
	class MouseEventDispatcher implements Runnable{

		@Override
		public void run() {
			while(true){
				try {
					Thread.sleep(DISPATCH_MOUSE_DELAY);
				} catch (InterruptedException e) {
					 
					e.printStackTrace();
				}
				if(!mouseEvents.isEmpty()){
					if(dispatchMouseState){
						try {
							if(RapidNetworkManager.getTableCommsClientService() != null)
								RapidNetworkManager.getTableCommsClientService().sendMessage(new MouseEventsMessage(MTTableClient.class, DesktopClient.tableId, new ArrayList<MouseEventInfo>(mouseEvents)));
						} catch (IOException e) {
							 
							e.printStackTrace();
						}
					}
					mouseEvents.clear();
				}
			}
		}
		
	}

	class SnapshotDownloader implements Runnable{

		@Override
		public void run() {
				try {
					Thread.sleep(MTDesktopConfigurations.SNAPSHOT_UPDATE_DELAY);
					AssetRegistry.getInstance().getAsset(DesktopClient.File_SERVER_URL+ MTDesktopConfigurations.SITE_PATH, "snapShotTabletop.gif", MTDesktopConfigurations.desktopTempFolder);
				} catch (MalformedURLException e) {
					 
					e.printStackTrace();
				} catch (IOException e) {
					 
					e.printStackTrace();
				} catch (InterruptedException e) {
					 
					e.printStackTrace();
				}
		}
		
	}
	
	private BufferedImage rotate(BufferedImage bi, double angle){
	    AffineTransform transform = new AffineTransform();
	    transform.rotate(angle, bi.getWidth()/2, bi.getHeight()/2);
	    
	    AffineTransform translationTransform;
	    translationTransform = findTranslation(transform, bi);
	    transform.preConcatenate(translationTransform);
	    AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
	    return op.filter(bi, null);
	}
	
	private AffineTransform findTranslation(AffineTransform at, BufferedImage bi) {
	    Point2D p2din, p2dout;

	    p2din = new Point2D.Double(0.0, 0.0);
	    p2dout = at.transform(p2din, null);
	    double ytrans = p2dout.getY();

	    p2din = new Point2D.Double(0, bi.getHeight());
	    p2dout = at.transform(p2din, null);
	    double xtrans = p2dout.getX();

	    AffineTransform tat = new AffineTransform();
	    tat.translate(-xtrans, -ytrans);
	    return tat;
	  }

	@Override
	public void fileUploadStarted(File file) {
		 
		
	}

	@Override
	public void fileUploadCompleted(File file) {
		 
		
	}

	@Override
	public void fileDownloadStarted(File file) {
		 
		
	}

	@Override
	public void fileDownloadCompleted(File file) {
		try {
			bImageFromConvert = ImageIO.read(file);
		} catch (IOException e) {
			 
			e.printStackTrace();
		}
		this.repaint();
		if(enableSnapShotDownload)	
			new Thread(new SnapshotDownloader()).start();
	}

	@Override
	public void fileUploadFailed(File file, String responseMessage) {
		 
		
	}

	@Override
	public void fileDownloadFailed(File file, String responseMessage) {
		if(enableSnapShotDownload)	
			new Thread(new SnapshotDownloader()).start();
	}
	

}