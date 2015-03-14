package apps.basketapp.controller;

import java.awt.Color;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Random;

import javax.swing.JFrame;

import apps.basketapp.client.BasketApp;
import apps.basketapp.messages.SnapshotMessage;
import apps.basketapp.messages.UnicastCaptureTableMessage;

import com.jme.system.DisplaySystem;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.LightImageLabel;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.Window;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;
import synergynetframework.appsystem.contentsystem.items.utils.Direction;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.RapidNetworkManager;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.handlers.MessageProcessor;

public class SnapshotContainer implements MessageProcessor{
	
	private Window window;
	private LightImageLabel imgLabel;
	private float defaultScale = 0.3f;
	private TableIdentity tableId;
	private JFrame tempFrame = new JFrame();
	
	public SnapshotContainer(final ContentSystem contentSystem, final TableIdentity tableId){
		this.tableId = tableId;
		window = (Window) contentSystem.createContentItem(Window.class);
		window.setWidth((int)(DisplaySystem.getDisplaySystem().getWidth() * defaultScale));
		window.setHeight((int)(DisplaySystem.getDisplaySystem().getHeight() * defaultScale));
		this.setColor(tableId);
		
		imgLabel = (LightImageLabel) contentSystem.createContentItem(LightImageLabel.class);
		imgLabel.setAutoFitSize(false);
		imgLabel.setWidth(DisplaySystem.getDisplaySystem().getWidth());
		imgLabel.setHeight(DisplaySystem.getDisplaySystem().getHeight());
		imgLabel.setScale(defaultScale * 0.7f, Direction.Y);
		imgLabel.setScale(defaultScale * 0.8f, Direction.X);
		
		SimpleButton refreshBtn = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		refreshBtn.setText("Refresh");
		refreshBtn.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				refresh();
			}
		});
		
		SimpleButton closeBtn = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		closeBtn.setText("Close");
		closeBtn.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				window.setVisible(false);
			}
		});
		window.addSubItem(imgLabel);
		imgLabel.setLocalLocation(0, 20);
		window.addSubItem(refreshBtn);
		refreshBtn.setLocalLocation(35, -90);
		window.addSubItem(closeBtn);
		closeBtn.setLocalLocation(-35, -90);
		window.centerItem();
		
		RapidNetworkManager.registerMessageProcessor(this);
	}
	
	private void setColor(TableIdentity tableId) {
		Color color = null;
		try {
			    Field field = Class.forName("java.awt.Color").getField(tableId.toString());
			    color = (Color)field.get(null);
			} catch (Exception e) {
				color = Color.getHSBColor( (new Random()).nextFloat(), 1.0F, 1.0F );
			}		
		window.setBackgroundColour(color);	
	}

	@Override
	public void process(Object obj) {
		if(obj instanceof SnapshotMessage){
			SnapshotMessage msg = (SnapshotMessage) obj;
			if(!(msg.getSender().equals(tableId))) return;
            if(msg.getImageBytes() != null){
                Image image = Toolkit.getDefaultToolkit().createImage(msg.getImageBytes());
                MediaTracker mt = new MediaTracker(tempFrame);
                mt.addImage(image, 1);
                try {
                    	mt.waitForAll();
                } catch (Exception e) {
                }
                imgLabel.drawImage(image);
            }
		}
	}

	public void refresh() {
		if(RapidNetworkManager.getTableCommsClientService() != null)
			try {
				RapidNetworkManager.getTableCommsClientService().sendMessage(new UnicastCaptureTableMessage(tableId, BasketApp.class));
			} catch (IOException e) {
				 
				e.printStackTrace();
			}
	}
	
	public Window getWindow(){
		return window;
	}
}
