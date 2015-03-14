package apps.mtdesktop.tabletop;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.swing.ImageIcon;

import apps.mtdesktop.messages.ContentMessage;
import apps.mtdesktop.messages.DeleteContentMessage;
import apps.mtdesktop.messages.HtmlContentMessage;
import apps.mtdesktop.messages.KeyEventMessage;
import apps.mtdesktop.messages.LaunchVncMessage;
import apps.mtdesktop.messages.MouseEventsMessage;
import apps.mtdesktop.messages.MouseRedirectMessage;
import apps.mtdesktop.messages.ShowMultiPadMessage;
import apps.mtdesktop.messages.util.MouseEventInfo;
import apps.mtdesktop.tabletop.basket.BasketManager;
import apps.mtdesktop.tabletop.basket.JmeNetworkedBasket;
import apps.mtdesktop.tabletop.fileserver.FtpServerServlet.FtpServletListener;
import apps.mtdesktop.tabletop.mouse.MouseCursor;
import apps.mtdesktop.tabletop.notepad.MultiUserNotepad;

import com.jme.util.GameTaskQueueManager;

import core.SynergyNetDesktop;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.HQPDFViewer;
import synergynetframework.appsystem.contentsystem.items.HtmlFrame;
import synergynetframework.appsystem.contentsystem.items.LightImageLabel;
import synergynetframework.appsystem.contentsystem.items.MediaPlayer;
import synergynetframework.appsystem.contentsystem.items.VncFrame;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.RapidNetworkManager;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.handlers.MessageProcessor;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;

public class TabletopContentManager implements FtpServletListener, MessageProcessor{
	
	private BasketManager basketManager;

	private TableSnapshotDispatcher snapshotDispatcher;
	
	public static float MAX_SCALE = 3f;
	public static float MIN_SCALE = 0.2f;
	protected ContentSystem contentSystem;
	protected Map<TableIdentity, List<ContentItem>> peerItems = new HashMap<TableIdentity, List<ContentItem>>();
	protected Map<TableIdentity, VncFrame> vncItems = new HashMap<TableIdentity, VncFrame>();
	protected List<TabletopContentManagerListener> listeners = new ArrayList<TabletopContentManagerListener>();
	protected Map<TableIdentity, MouseCursor> cursorMap = new HashMap<TableIdentity, MouseCursor>();
	protected List<DesktopKeyboardListener> keyboardListeners = new ArrayList<DesktopKeyboardListener>();
	protected List<DesktopMouseListener> mouseListeners = new ArrayList<DesktopMouseListener>();
	protected MultiUserNotepad pad;
	private int cursorId = 30;

	List<TableIdentity> temp = new ArrayList<TableIdentity>();

	protected float checkDelay = 2f;
	protected float frameRate = checkDelay;
	
	public TabletopContentManager(ContentSystem contentSystem, DefaultSynergyNetApp app){
		this.contentSystem = contentSystem;
		basketManager = new BasketManager(app);
		this.addListener(basketManager);
		snapshotDispatcher = new TableSnapshotDispatcher();
		snapshotDispatcher.enable(true);
		pad = new MultiUserNotepad(contentSystem);
		this.addDesktopKeyboardListener(pad);
		pad.getWindow().setVisible(false);
	}

	public void addListener(TabletopContentManagerListener listener){
		if(!listeners.contains(listener)) listeners.add(listener);
	}
	
	@Override
	public void fileUploaded(TableIdentity tableId, String assetId, File f) {
		processNewFile(new FileRecord(tableId, assetId, f));
	}

	@Override
	public void fileDownloaded(File file) {
		 
		
	}
	
	public void update(float tpf){
		if(RapidNetworkManager.isConnected) 
			basketManager.update(tpf);
			snapshotDispatcher.update(tpf);
		
		if((frameRate - tpf) > 0){
			frameRate-= tpf;
			return;
		}
		checkConnectivity();
	}
	
	private void checkConnectivity() {
		if(RapidNetworkManager.getTableCommsClientService() == null) temp.addAll(this.basketManager.getBaskets().keySet());
		if(RapidNetworkManager.getTableCommsClientService() != null){
			List<TableIdentity> onlineTables = RapidNetworkManager.getTableCommsClientService().getCurrentlyOnline();
			for(TableIdentity tableId: basketManager.getBaskets().keySet()){
				if(!onlineTables.contains(tableId)){
					temp.add(tableId);
				}
			}
		}

		for(TableIdentity tableId: temp){
			if(vncItems.containsKey(tableId)){
				VncFrame vnc = vncItems.get(tableId);
				vnc.disconnect();
				for(TabletopContentManagerListener l: listeners) l.vncClientClosed(tableId, vnc);
				contentSystem.removeContentItem(vncItems.get(tableId));
				vncItems.remove(tableId);
			}
			this.basketManager.unregisterBasket(tableId);
			this.unregisterMouseCursor(tableId);
		}
		temp.clear();
	}

	@Override
	public void process(Object obj) {
		if(obj instanceof ContentMessage){
			processContentMessage((ContentMessage)obj);
		}else if(obj instanceof KeyEventMessage){
			KeyEventMessage msg = (KeyEventMessage)obj;
			KeyEvent evt = msg.getKeyEvent();
			for(DesktopKeyboardListener l: keyboardListeners){
				if(evt.getID() == KeyEvent.KEY_PRESSED)
					l.keyPressed(msg.getSender(), evt);
				else if(evt.getID() == KeyEvent.KEY_RELEASED)
					l.keyReleased(msg.getSender(), evt);
				else if(evt.getID() == KeyEvent.KEY_TYPED)
					l.keyTyped(msg.getSender(), evt);
			}	
		}else if(obj instanceof MouseEventsMessage){
			MouseEventsMessage msg = (MouseEventsMessage)obj;
			List<MouseEventInfo> evts = msg.getMouseEvents();
			for(MouseEventInfo evt: evts){
				for(DesktopMouseListener l: mouseListeners){
					if(evt.getID() == MouseEvent.MOUSE_PRESSED)
						l.mousePressed(msg.getSender(), evt);
					else if(evt.getID() == MouseEvent.MOUSE_RELEASED)
						l.mouseReleased(msg.getSender(), evt);
					else if(evt.getID() == MouseEvent.MOUSE_CLICKED)
						l.mouseClicked(msg.getSender(), evt);
					else if(evt.getID() == MouseEvent.MOUSE_MOVED)
						l.mouseMoved(msg.getSender(), evt);
					else if(evt.getID() == MouseEvent.MOUSE_DRAGGED)
						l.mouseDragged(msg.getSender(), evt);
				}
			}
		}else if(obj instanceof MouseRedirectMessage){
			MouseRedirectMessage msg = (MouseRedirectMessage)obj;
			if(!cursorMap.containsKey(msg.getSender())){
				this.registerMouseCursor(msg.getSender());
			}
			cursorMap.get(msg.getSender()).getCursor().setVisible(msg.isMouseRedirectionEnabled());
		}else if(obj instanceof ShowMultiPadMessage){
			ShowMultiPadMessage msg = (ShowMultiPadMessage)obj;
			pad.getWindow().setVisible(msg.isShow());
		}
	}
	
	private void processNewFile(final FileRecord fileRecord){
		GameTaskQueueManager.getManager().update(new Callable<Object>() {
			public Object call() throws Exception {
				File f = fileRecord.file;
				int mid= f.getName().lastIndexOf(".");
				String ext=f.getName().substring(mid+1);
				if(ext.equalsIgnoreCase("pdf")){
					HQPDFViewer pdf = (HQPDFViewer) contentSystem.createContentItem(HQPDFViewer.class);
					pdf.setPdfURL(f.toURI().toURL());
					pdf.setId(fileRecord.assetId);
				  	pdf.setWidth(200);
					pdf.setHeight(350);
					pdf.setAsTopObject();
				  	pdf.setBringToTopable(true);
				  	registerItem(fileRecord.tableId, pdf);
			  }else if(ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("bmp") || ext.equalsIgnoreCase("png") || ext.equalsIgnoreCase("jpeg") || ext.equalsIgnoreCase("gif") || ext.equalsIgnoreCase("tiff")){
				  	LightImageLabel image = (LightImageLabel) contentSystem.createContentItem(LightImageLabel.class);
				  	image.drawImage(f.toURI().toURL());
				  	image.setId(fileRecord.assetId);
				  	ImageIcon icon = new ImageIcon(f.toURI().toURL());
				  	image.setWidth(icon.getIconWidth());
				  	image.setHeight(icon.getIconHeight());
				  	image.setAsTopObject();
				  	image.setBringToTopable(true);
				  	registerItem(fileRecord.tableId, image);
			  }else if(ext.equalsIgnoreCase("mpg") || ext.equalsIgnoreCase("mpeg") || ext.equalsIgnoreCase("gif") || ext.equalsIgnoreCase("avi")){
				  	MediaPlayer video = (MediaPlayer) contentSystem.createContentItem(MediaPlayer.class);
				  	video.setMediaURL(f.toURI().toURL());
				  	video.setId(fileRecord.assetId);
				  	video.setAsTopObject();
				  	video.setBringToTopable(true);
				  	registerItem(fileRecord.tableId, video);
			  }else if(ext.equalsIgnoreCase("txt")){
					String txt = readTxtFile(f);
					HtmlFrame html = (HtmlFrame) contentSystem.createContentItem(HtmlFrame.class);
					html.setHtmlContent(txt);
					html.setId(fileRecord.assetId);
				  	html.setAsTopObject();
				  	html.setBringToTopable(true);
		 			registerItem(fileRecord.tableId, html);
			  }
			  return null;
			}
		});
	}
	
	
	private void processContentMessage(final ContentMessage msg){
		GameTaskQueueManager.getManager().update(new Callable<Object>() {
			public Object call() throws Exception {
				if(msg instanceof DeleteContentMessage){
					DeleteContentMessage delMsg = (DeleteContentMessage) msg;
					List<ContentItem> items = peerItems.get(msg.getSender());
					if(items != null){
						Iterator<ContentItem> itemIter = items.iterator();
						while(itemIter.hasNext()){
							ContentItem item = itemIter.next();
							boolean itemRemoved = false;
							if(item.getId().equals(delMsg.getContentId())){
								for(JmeNetworkedBasket basket: basketManager.getBaskets().values()){
									if(basket.getWindow().contains(item)){
										basket.removeItem(item);
										itemRemoved = true;
									}
								}
								if(!itemRemoved)
									contentSystem.removeContentItem(item);
								itemIter.remove();
							}
						}
					}
				}else if(msg instanceof HtmlContentMessage){
					HtmlFrame html = (HtmlFrame) contentSystem.createContentItem(HtmlFrame.class);
					html.setHtmlContent(((HtmlContentMessage)msg).getHtmlContent());
					html.setId(((HtmlContentMessage)msg).getContentId());
				  	html.setAsTopObject();
				  	html.setBringToTopable(true);
		 			registerItem(msg.getSender(), html);
				}else if(msg instanceof LaunchVncMessage){
					LaunchVncMessage vncMsg = (LaunchVncMessage) msg;
					if(vncMsg.isEnabled() && !vncItems.containsKey(vncMsg.getSender())){
						VncFrame vnc = (VncFrame) contentSystem.createContentItem(VncFrame.class);
						vnc.setConnectionSettings(vncMsg.getIpAddress(), vncMsg.getPort(), vncMsg.getPassword());
						vnc.connect();
						vnc.centerItem();
					  	vnc.setBringToTopable(true);
					  	vnc.setAsTopObject();
						vncItems.put(vncMsg.getSender(), vnc);
						for(TabletopContentManagerListener l: listeners) l.vncClientLaunched(msg.getSender(), vnc);
				}else{
					if(vncItems.containsKey(vncMsg.getSender())){
						VncFrame vnc = vncItems.get(vncMsg.getSender());
						vnc.disconnect();
						for(TabletopContentManagerListener l: listeners) l.vncClientClosed(msg.getSender(), vnc);
						contentSystem.removeContentItem(vncItems.get(vncMsg.getSender()));
						vncItems.remove(vncMsg.getSender());
						}
					}
				}
				return null;
			}});
	}
	
	
	private void registerItem(TableIdentity peerId, ContentItem item){
		if(!peerItems.containsKey(peerId))
			peerItems.put(peerId, new ArrayList<ContentItem>());
		peerItems.get(peerId).add(item);
		for(TabletopContentManagerListener l: listeners) l.itemReceived(peerId, item);
	}
	
	public Map<TableIdentity, List<ContentItem>> getTableItems(){
		return peerItems;
	}
	
	public BasketManager getBasketManager(){
		return basketManager;
	}
	
	private class FileRecord{
		public TableIdentity tableId;
		public String assetId;
		public File file;
		
		public FileRecord(TableIdentity tableId, String assetId, File file){
			this.tableId = tableId;
			this.assetId = assetId;
			this.file = file;
		}
	}
	
	public void registerMouseCursor(TableIdentity tableId) {
		JmeNetworkedBasket basket = basketManager.getBaskets().get(tableId);
		if(basket == null) return;
		MouseCursor cursor = new MouseCursor(contentSystem, SynergyNetDesktop.getInstance().getMultiTouchInputComponent(), tableId, this.getCursorId(), basketManager.getBaskets().get(tableId).getWindow().getBackgroundColour());
		cursor.setOrientation(basket.getPosition());
		this.addDesktopMouseListener(cursor);
		cursorMap.put(tableId, cursor);
	}
	
	public void unregisterMouseCursor(TableIdentity tableId) {
		MouseCursor cursor = cursorMap.get(tableId);
		if(cursor != null){
			contentSystem.removeContentItem(cursor.getCursor());
			cursorMap.remove(tableId);
		}
	}
	
	public void addDesktopKeyboardListener(DesktopKeyboardListener l) {
		if(!keyboardListeners.contains(l))
			keyboardListeners.add(l);
	}
	
	public void removeDesktopKeyboardListener(DesktopKeyboardListener l){
		if(keyboardListeners.contains(l))
			keyboardListeners.remove(l);
	}
	
	public void removeDesktopKeyboardListeners(){
		keyboardListeners.clear();
	}
	
	public void addDesktopMouseListener(DesktopMouseListener l) {
		if(!mouseListeners.contains(l))
			mouseListeners.add(l);
	}
	
	public void removeDesktopMouseListener(DesktopMouseListener l){
		if(mouseListeners.contains(l))
			mouseListeners.remove(l);
	}
	
	public void removeDesktopMouseListeners(){
		mouseListeners.clear();
	}
	
	public interface TabletopContentManagerListener{
		public void itemReceived(TableIdentity tableId, ContentItem item);
		public void vncClientLaunched(TableIdentity tableId, VncFrame vnc);
		public void vncClientClosed(TableIdentity sender, VncFrame vnc);
	}
	
	public interface DesktopKeyboardListener{
		public void keyPressed(TableIdentity tableId, KeyEvent evt);
		public void keyReleased(TableIdentity tableId, KeyEvent evt);
		public void keyTyped(TableIdentity tableId, KeyEvent evt);
	}
	
	public interface DesktopMouseListener{
		public void mousePressed(TableIdentity tableId, MouseEventInfo evt);
		public void mouseReleased(TableIdentity tableId, MouseEventInfo evt);
		public void mouseClicked(TableIdentity tableId, MouseEventInfo evt);
		public void mouseDragged(TableIdentity tableId, MouseEventInfo evt);
		public void mouseMoved(TableIdentity tableId, MouseEventInfo evt);

	}
	
	public MultiUserNotepad getPad(){
		return pad;
	}
	
	private String readTxtFile(File txtFile) throws IOException{
		 String str = null;
		 FileReader input = new FileReader(txtFile);
		 BufferedReader bufRead = new BufferedReader(input);
         String line;    // String that holds current file line
         line = bufRead.readLine();
         while (line != null){
        	 str+=line+"\n";
        	 line = bufRead.readLine();
         }
         bufRead.close();
         input.close();
         return str;
	}
	
	private int getCursorId(){
		cursorId++;
		return cursorId;
	}
}
