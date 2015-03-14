/*
 * Copyright (c) 2009 University of Durham, England
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'SynergyNet' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package synergynetframework.appsystem.services.net.networkedcontentmanager.utils.networkedflick;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.ImageTextLabel;
import synergynetframework.appsystem.contentsystem.items.OrthoContentItem;
import synergynetframework.appsystem.contentsystem.items.PPTViewer;
import synergynetframework.appsystem.contentsystem.items.VideoPlayer;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.networkedcontentmanager.NetworkedContentManager;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.networkedflick.RegisterTableMessage;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.networkedflick.TransferableContentItem;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.networkedflick.UnregisterTableMessage;
import synergynetframework.appsystem.services.net.tablecomms.client.TableCommsClientService;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.jme.cursorsystem.flicksystem.FlickMover;
import synergynetframework.jme.cursorsystem.flicksystem.FlickSystem;
import synergynetframework.jme.sysutils.SpatialUtility;



import com.jme.bounding.BoundingBox;
import com.jme.bounding.OrthogonalBoundingBox;
import com.jme.intersection.IntersectionRecord;
import com.jme.math.Quaternion;
import com.jme.math.Ray;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.Spatial.CullHint;
import com.jme.system.DisplaySystem;
import com.jme.util.GameTaskQueueManager;

import core.SynergyNetDesktop;

public class TransferController{
	
	private static final Logger log = Logger.getLogger(TransferController.class.getName());
	
	protected ArrayList<VirtualTable> virtualTables  = new ArrayList<VirtualTable>();
	protected TableInfo localTableInfo = null;
	protected TableCommsClientService comms;
	protected DefaultSynergyNetApp app;

	private ContentSystem content;
	private Node orthoNode;

	private NetworkedContentManager networkedContentManager;

	public TransferController(DefaultSynergyNetApp app, TableCommsClientService comms,NetworkedContentManager networkedContentManager){		
		this.app = app;
		this.content = ContentSystem.getContentSystemForSynergyNetApp(app);
		this.orthoNode = app.getOrthoNode();
		this.comms = comms;
		this.networkedContentManager = networkedContentManager;
		FlickSystem.setNetworkFlickMode(true);
		log.info("Transfer controller created.");
	}

	public void setLocalTableInfo(TableInfo localTableInfo){
		this.localTableInfo = localTableInfo;
	}

	public TableInfo getLocalTableInfo(){
		return localTableInfo;
	}
	
	private Vector2f arrivalLocationStats;
	private VirtualTable targetTable;

	public boolean isDestinationTableAvailable(Spatial s, FlickMover fm){
		 Ray ray = new Ray(s.getLocalTranslation(), fm.getLinearVelocity().normalize());
         for(VirtualTable virtualTable : virtualTables){
             IntersectionRecord record = virtualTable.getWorldBound().intersectsWhere(ray);
             if(record != null && record.getQuantity() > 0){
            	 if (withinStoppingDistance(s, fm)){
            		 	targetTable = virtualTable;
	 					
	 					Vector3f intersectionLocation = record.getIntersectionPoint(record.getClosestPoint());
	 					intersectionLocation = intersectionLocation.add(ray.origin.subtract(intersectionLocation).normalize().mult(SpatialUtility.getMaxDimension(s)));
	 					arrivalLocationStats = 
	 							new Vector2f(intersectionLocation.x - virtualTable.getWorldTranslation().x, intersectionLocation.y - virtualTable.getWorldTranslation().y);				    				    	

	 					arrivalLocationStats.rotateAroundOrigin(localTableInfo.getAngle(), true);		    	
	
	                    return true;
            	 }
             }
         }
         return false;
	}

	private boolean withinStoppingDistance(Spatial s, FlickMover fm) {
		
		float maxDimension = SpatialUtility.getMaxDimension(s);
		
		float stoppingDistance = -fm.getSpeed()*2/(2*-fm.getDeceleration());
				
		if (stoppingDistance > maxDimension * 2){
			return true;
		}else{
			return false;
		}
	}
	
	public ContentItem applyTransferableContentItem(TransferableContentItem message){
		ContentItem item = message.getContentItem();
		item.name = content.generateUniqueName();
		content.addContentItem(item);
		item.setName(content.generateUniqueName());
		item.init();
		item.setScale(message.getScale() / (DisplaySystem.getDisplaySystem().getWidth()/1024f));
		try {
			if(item instanceof ImageTextLabel){
				
				ImageTextLabel itemImplementation = (ImageTextLabel)item;
				
				String address = toLocalString(itemImplementation.getImageInfo().getImageResource().toString());
				
				URL add = new URL(address);
				itemImplementation.setImageInfo(add);						
				
			}else if(item instanceof VideoPlayer){
				
				VideoPlayer itemImplementation = (VideoPlayer)item;
				double time = itemImplementation.getVideoTime();
				String address = toLocalString(itemImplementation.getVideoURL().toString());
				
				URL add = new URL(address);
				itemImplementation.setVideoURL(add);						
				
				itemImplementation.setVideoTime(time);
				
			}else if(item instanceof PPTViewer){
				
				PPTViewer itemImplementation = (PPTViewer)item;
				int page = itemImplementation.getCurrentPageIndex();
				String address = toLocalString(itemImplementation.getPPTFile().toString());
				
				URL add = new URL(address);
				itemImplementation.setPPTFile(add);		
				itemImplementation.gotoPage(page);			
			}
		} catch (Exception e) {
			log.warning(e.toString());
		}
				
		((OrthoContentItem)item).setRotateTranslateScalable(true);
		((OrthoContentItem)item).setScaleLimit(message.getMinScale(), message.getMaxScale());
		((OrthoContentItem)item).makeFlickable(message.getDeceleration());
		
		
		Vector2f rotatedFlick = new Vector2f(message.getLinearVelocityX(), message.getLinearVelocityY());
		rotatedFlick.rotateAroundOrigin(localTableInfo.getAngle(), false);
				
		Vector2f collisionPosition = new Vector2f(message.getLocationStats()[0], message.getLocationStats()[1]);
		collisionPosition.rotateAroundOrigin(localTableInfo.getAngle(), false);	
		
		collisionPosition.setX(collisionPosition.x + SynergyNetDesktop.getInstance().getDisplayWidth()/2);
		collisionPosition.setY(collisionPosition.y + SynergyNetDesktop.getInstance().getDisplayHeight()/2);
		
		((OrthoContentItem)item).setLocation(collisionPosition.x, collisionPosition.y);
		
		((OrthoContentItem)item).setAngle(((OrthoContentItem)item).getAngle() + (message.getRoation() + localTableInfo.getAngle()));	
		
		((OrthoContentItem)item).flick(rotatedFlick.x, rotatedFlick.y, message.getDeceleration());
		
		log.info("Apply transferable content item: "+item.getClass().getName()+"-"+item.getName());
		
		return item;
	}

	private String toLocalString(String s) throws Exception{
		String result = "";
		
		String localAddress = TransferController.class.getResource("TransferController.class").toString();
		
		String[] remoteAddParts = s.split("/bin/");
		String[] localAddParts = localAddress.split("/bin/");
			
		result = localAddParts[0] + "/bin/" + remoteAddParts[1];		
		
		return result;
	}
		
	public void registerRemoteTable(final TableInfo remoteTableInfo)	{
		
		VirtualTable temp = findTableById(remoteTableInfo.getTableId());
		if(temp != null) virtualTables.remove(temp);
		if(remoteTableInfo.getTableId().equals(localTableInfo.getTableId())) return;

		VirtualTable virtualTable = new VirtualTable(remoteTableInfo);

		Node localRotNode = new Node("Root of carpet");
        localRotNode.attachChild(virtualTable);
        localRotNode.setModelBound(new BoundingBox());
        localRotNode.updateModelBound();

        Quaternion quat = new Quaternion().fromAngleAxis(remoteTableInfo.getAngle(), Vector3f.UNIT_Z);
        virtualTable.setLocalRotation(quat);

		Node pivotNode = new Node("Pivot_"+virtualTable.getTableId().toString());
		pivotNode.setLocalTranslation(localTableInfo.getWidth()/2, localTableInfo.getHeight()/2,0);
		pivotNode.attachChild(localRotNode);
		
		Vector2f remoteTablePos = new Vector2f(remoteTableInfo.getTablePositionX(), remoteTableInfo.getTablePositionY());
		Vector2f localTablePos = new Vector2f(localTableInfo.getTablePositionX(), localTableInfo.getTablePositionY());
		Vector2f relativePosition = remoteTablePos.subtract(localTablePos);
		virtualTable.setLocalTranslation(relativePosition.x, relativePosition.y,0);

		Quaternion q = new Quaternion();
		q.fromAngleAxis(localTableInfo.getAngle(), Vector3f.UNIT_Z);
		pivotNode.setLocalRotation(q);
		pivotNode.setCullHint(CullHint.Always);
	
		if(!checkLocationConflict(virtualTable)){
   			orthoNode.attachChild(pivotNode);
			virtualTables.add(virtualTable);
			log.info("Register remote table-"+remoteTableInfo.getTableId().toString()+" for flicking items");
		}

	}
	
	public void cleanUpUnregisteredTable(UnregisterTableMessage msg){

		if(msg.getSender().equals(localTableInfo.getTableId())){
			for(VirtualTable virtualTable : virtualTables)
				detachVirtualTable(virtualTable);
			virtualTables.clear();
			localTableInfo = null;
		}
		else{
			VirtualTable virtualTable = findTableById(msg.getSender());
			if(virtualTable != null){
				virtualTables.remove(virtualTable);
				detachVirtualTable(virtualTable);
			}
		}
		
		log.info("Unregister remote table-"+msg.getSender().toString()+" from flicking operation");

	}

	private void detachVirtualTable(final VirtualTable table){
        GameTaskQueueManager.getManager().update(new Callable<Object>() {
  		    public Object call() throws Exception {
  		    		if(orthoNode != null)
  		    			orthoNode.detachChildNamed("Pivot_"+ table.getTableId().toString());
  		    	return null;
  		    }
        });
	}

	private VirtualTable findTableById(TableIdentity tableId){
		for(VirtualTable table : virtualTables)
			if(table.getTableId().equals(tableId))
				return table;
		return null;
	}
	
	public void update(){
		Iterator<FlickMover> iter = FlickSystem.getInstance().getMovingElements().iterator();
		while(iter.hasNext()){
			
			FlickMover fm = iter.next();
			Spatial s = fm.getTargetSpatial();
			if (!fm.toBeTransferred){
				if (leavingTable(s, fm)){
					if( virtualTables.isEmpty() || !isDestinationTableAvailable(s, fm)){
						fm.bounce();
					}else{
						if (!virtualTables.isEmpty()){
	
							fm.toBeTransferred = true;
							
							float[] thisArrivalLocation = {arrivalLocationStats.x,arrivalLocationStats.y, 0};

							VirtualTable vt = new VirtualTable(targetTable);
							Runnable r = new CustomThread(fm, thisArrivalLocation, vt);
							new Thread(r).start();

						}				
					}
				}
			}
		}
	}

	private boolean leavingTable(Spatial targetSpatial, FlickMover fm ) {
		if (targetSpatial.getWorldTranslation().x > SynergyNetDesktop.getInstance().getDisplayWidth()){
			if (fm.getLinearVelocity().x > 0)return true;				
		}else if (targetSpatial.getWorldTranslation().x < 0){
			if (fm.getLinearVelocity().x < 0)return true;
		}
		
		if (targetSpatial.getWorldTranslation().y > SynergyNetDesktop.getInstance().getDisplayHeight()){
			if (fm.getLinearVelocity().y > 0)return true;			
		}else if (targetSpatial.getWorldTranslation().y < 0){
			if (fm.getLinearVelocity().y < 0)return true;
		}
		return false;
	}

	public void sendRegistrationMessage(TableIdentity targeTableId) {
		try {
			for(Class<?> receiverClass: networkedContentManager.getReceiverClasses()){
				comms.sendMessage(new RegisterTableMessage(receiverClass, localTableInfo, targeTableId));
				log.info("Send table registration message to table-"+targeTableId.toString()+" for flicking operation.");
			}
		} catch (IOException e) {
			log.info(e.toString());
		}
	}
	
	private boolean checkLocationConflict(VirtualTable virtualRemoteTable) {
		VirtualTable virtualLocalTable = new VirtualTable(localTableInfo);
		virtualLocalTable.setModelBound(new OrthogonalBoundingBox());
		virtualLocalTable.updateModelBound();
		if(virtualLocalTable.hasCollision(virtualRemoteTable, true)){
			log.info("***** Conflict position *****");
			return true;
		}
		return false;
	}
	
	public void transfer(FlickMover fm, Spatial s, float[] thisArrivalLocationStats, VirtualTable table) {		
		if (fm.toBeTransferred && leavingTable(s, fm)){
		
			ContentItem item = content.getContentItem(s.getName());
			
			((OrthoContentItem)item).makeUnflickable();
						
			Vector2f rotatedFlick = new Vector2f(fm.getLinearVelocity().x, fm.getLinearVelocity().y);
			rotatedFlick.rotateAroundOrigin(localTableInfo.getAngle(), true);
			
			for(Class<?> receiverClass: networkedContentManager.getReceiverClasses()){
				TransferableContentItem msg = new TransferableContentItem(receiverClass, item, table.getTableId());
				msg.setDeceleration(fm.getDeceleration());
				msg.setLinearVelocity(rotatedFlick.x, rotatedFlick.y);
				msg.setLocationStats(thisArrivalLocationStats);
				msg.setRoation(-localTableInfo.getAngle());
				msg.setScale((((OrthoContentItem)item).getScale() * (DisplaySystem.getDisplaySystem().getWidth()/1024f)));
				msg.setMinScale((((OrthoContentItem)item).getMinScale()));
				msg.setMaxScale((((OrthoContentItem)item).getMaxScale()));
				networkedContentManager.sendMessage(msg);
				log.info("Send flicked item: "+item.getClass().getName()+"-"+item.getName()+" to table-"+table.getTableId().toString());
			}
			networkedContentManager.removeContentItem(item);
		}
		
	}
	
	public class CustomThread implements Runnable {

		   private FlickMover fm;
		   private float[] thisArrivalLocationStats;
		   private Vector3f initialPos;
		   private VirtualTable vt;
		   private float maxDistance;
		   	   
		   public CustomThread(FlickMover fm, float[] thisArrivalLocationStats, VirtualTable vt) {
			   this.vt = vt;
			   setup(fm, thisArrivalLocationStats);
		   }
		
		   private void setup(FlickMover fm, float[] thisArrivalLocationStats) {
		       this.fm = fm;
		       this.thisArrivalLocationStats = thisArrivalLocationStats;
		       this.maxDistance = SpatialUtility.getMaxDimension(fm.getTargetSpatial());
		       this.maxDistance *= 1.5;
		       this.initialPos = new Vector3f(fm.getTargetSpatial().getWorldTranslation().x, fm.getTargetSpatial().getWorldTranslation().y, 0);	
		   }
		   

			public void run() {
				   
				   while (fm.getTargetSpatial().getWorldTranslation().distance(initialPos) < maxDistance){	 
					   try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
				    }
				   transfer(fm, fm.getTargetSpatial(), thisArrivalLocationStats, vt);
					  
			   }
		}


}
