package apps.mtdesktop.desktop;


import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.Map;

import apps.mtdesktop.MTDesktopConfigurations;
import apps.mtdesktop.desktop.DesktopClient;
import apps.mtdesktop.desktop.tree.TabletopTree;
import apps.mtdesktop.fileutility.AssetRegistry;
import apps.mtdesktop.messages.BasketItemMessage;
import apps.mtdesktop.tabletop.basket.BasketManager;

import synergynetframework.appsystem.services.net.rapidnetworkmanager.handlers.MessageProcessor;

public class DesktopInboxContentManager implements MessageProcessor{

	protected TabletopTree tree;
	
	public DesktopInboxContentManager(TabletopTree tree){
		this.tree = tree;
	}
	
	@Override
	public void process(Object obj) {
		if(obj instanceof BasketItemMessage){
			System.out.println("basket item message received...........");
			BasketItemMessage msg = (BasketItemMessage) obj;
			final Map<String, Object> itemInfo = msg.getItemInfo();
			new Thread(new Runnable(){

				@Override
				public void run() {
					try{
						for(String info: itemInfo.keySet()){
							if(info.equals(BasketManager.PDF_ITEM) || info.equals(BasketManager.IMAGE_ITEM) || info.equals(BasketManager.VIDEO_ITEM)){
								URL fileURL = (URL) itemInfo.get(info);
								if(fileURL == null) return;
								String fileName = fileURL.toString().substring(fileURL.toString().lastIndexOf("/")+1);
								System.out.println("downloading asset: "+DesktopClient.File_SERVER_URL+ MTDesktopConfigurations.SITE_PATH);
								AssetRegistry.getInstance().getAsset(DesktopClient.File_SERVER_URL+ MTDesktopConfigurations.SITE_PATH, fileName, MTDesktopConfigurations.inboxFolder);
								File downloadedFile = new File(MTDesktopConfigurations.inboxFolder, fileName);
								 int delay = 0;
								 while(delay < 10000 && !downloadedFile.exists()) {
									 Thread.sleep(1000);
									 delay+=1000;
								 }
								 tree.getController().updateInboxNode();
							}else if(info.equals(BasketManager.TEXT_ITEM)){
								String txt = (String) itemInfo.get(info);
								if(txt == null || txt.trim().length() == 0) return;
								String fileName = txt;
								if(txt.length() > 7) fileName = txt.substring(0,7);
								fileName+=".txt";
								File txtFile = new File(MTDesktopConfigurations.inboxFolder, fileName);
								FileOutputStream fos = new FileOutputStream(txtFile);
								PrintStream  ps=new PrintStream(fos);
								ps.println (txt);
								ps.close();
								fos.close();
								tree.getController().updateInboxNode();
							}
						}
					}catch(Exception exp){
						exp.printStackTrace();
					}
				}
				
			}).start();
		}
	}
}
