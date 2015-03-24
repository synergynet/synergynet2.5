package apps.basketapp.client;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.imageio.ImageIO;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.RapidNetworkManager;

import apps.basketapp.controller.ControllerApp;
import apps.basketapp.messages.SnapshotMessage;

import com.jme.system.DisplaySystem;


/**
 * The Class SnapshotDispatcher.
 */
public class SnapshotDispatcher {
	
    /** The baos. */
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();
    
    /** The snapshot path. */
    private String snapshotPath = System.getProperty("user.dir");
	
    /**
     * Instantiates a new snapshot dispatcher.
     */
    public SnapshotDispatcher(){}
	
	/**
	 * Dispatch snapshot.
	 *
	 * @param tableId the table id
	 */
	public void dispatchSnapshot(final TableIdentity tableId){
		if(DisplaySystem.getDisplaySystem().getRenderer() != null){
			DisplaySystem.getDisplaySystem().getRenderer().takeScreenShot(snapshotPath);
			new Thread(new Runnable(){

				@Override
				public void run() {
					try{
			            byte[] bytes = null;
						File file = new File(snapshotPath+".png");
			            while(!file.exists()){
			                  Thread.sleep(1000);
			            }
			            BufferedImage src = ImageIO.read(file);
			            baos.reset();
			            ImageIO.write( src, "png", baos );
			            baos.flush();
			            bytes = baos.toByteArray();
			            baos.close();
			            file.delete();
			            if(bytes != null && bytes.length != 0 && RapidNetworkManager.getTableCommsClientService() != null){
			                    RapidNetworkManager.getTableCommsClientService().sendMessage(new SnapshotMessage(ControllerApp.class, tableId, bytes));
			            }
					}catch(Exception exp){
						exp.printStackTrace();
					}
				}
				
			}).start();
		}

	}
}
