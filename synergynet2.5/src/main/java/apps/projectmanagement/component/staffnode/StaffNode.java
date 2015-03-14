package apps.projectmanagement.component.staffnode;

import java.awt.Color;
import java.net.URL;
import java.util.Random;

import apps.projectmanagement.ProjectManagementApp;
import apps.projectmanagement.registry.StaffNodeRegistry;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.DrawableFrame;
import synergynetframework.appsystem.contentsystem.items.LightImageLabel;
import synergynetframework.appsystem.contentsystem.items.OrthoContainer;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;
import synergynetframework.appsystem.contentsystem.items.utils.Location;

public class StaffNode {

	protected ContentSystem contentSystem;
	protected int width = 80;
	protected int heigth = 100;
	
	protected int detailPageWidth =150;
	protected int detailPageHeight =150;
	
	protected DrawableFrame detailPage;
	protected LightImageLabel briefPage;
	
	protected OrthoContainer container;
	protected SimpleButton switchButton;
	protected SimpleButton copyButton;
	protected SimpleButton closeButton;
	
	protected boolean closable;
	protected StaffModel staffModel; 
	
	public StaffNode(ContentSystem contentSystem, StaffModel staffModel, int width, int heigth, boolean closable){
		this.contentSystem = contentSystem;
		this.width = width;
		this.heigth = heigth;
		this.staffModel = staffModel;
		this.closable = closable;
		init();
	}
	
	public StaffNode(ContentSystem contentSystem, StaffModel staffModel, int width, int heigth){
		this(contentSystem, staffModel, width, heigth, false);
	}
	
	protected void init(){
		
		container = (OrthoContainer)contentSystem.createContentItem(OrthoContainer.class);
		Random r = new Random();
		container.setLocation(r.nextInt(700)+100, r.nextInt(600)+100);
		container.setScale(1f);
		
		buildPages();
		buildButtons();
		showDetailPage(false);
		
		this.setVisibility(false);
	
	}
	
	public void setLocation(float x, float y){
		container.setLocation(x, y);
	}
	
	public Location getLocation(){
		return container.getLocalLocation();
	}
	
	protected void buildPages(){
		
		briefPage = (LightImageLabel)contentSystem.createContentItem(LightImageLabel.class);
		briefPage.drawImage(ProjectManagementApp.class.getResource(staffModel.getPosition()+".png"));
		briefPage.setImageLabelHeight(heigth);
		briefPage.setLocalLocation(0, 0);
		container.addSubItem(briefPage);
		
		this.detailPage = (DrawableFrame) contentSystem.createContentItem(DrawableFrame.class);
		detailPage.setLocalLocation(0, 0);
		detailPage.setWidth(detailPageWidth);
		detailPage.setHeight(detailPageHeight);
		detailPage.setBorderSize(2);
		detailPage.setBorderColour(Color.LIGHT_GRAY);
		detailPage.setDrawableContent(new StaffNodeDrawableContent(staffModel, detailPageWidth, detailPageHeight));
		container.addSubItem(detailPage);
			
	}
	
	public void showDetailPage(boolean show){
		if (show){
			detailPage.setVisible(true);
			briefPage.setVisible(false);
			switchButton.setLocation(-detailPageWidth/2+10, detailPageHeight/2-10);
			copyButton.setLocation(detailPageWidth/2-10, detailPageHeight/2-10);
			if (closeButton!=null)
				closeButton.setLocation(detailPageWidth/2-30, detailPageHeight/2-10);
		}
		else {
			detailPage.setVisible(false);
			briefPage.setVisible(true);
			
			switchButton.setLocation(-width/2+10, heigth/2-10);
			copyButton.setLocation(width/2-10, heigth/2-10);
			if (closeButton!=null)
				closeButton.setLocation(width/2-30, heigth/2-10);
		}
	}
	
	public boolean isDetailPageShown(){
		if (detailPage.isVisible())
			return true;
		else
			return false;
	}
	
	protected void buildButtons(){
		switchButton = this.createButtonWithImage(ProjectManagementApp.class.getResource("paste.png"));
		switchButton.setBackgroundColour(Color.white);
		switchButton.setBorderSize(0);
		container.addSubItem(switchButton);
		switchButton.setOrder(2);
		switchButton.addButtonListener(new SimpleButtonAdapter() {
			@Override
			public void buttonPressed(SimpleButton b, long id, float x,
					float y, float pressure) {
				super.buttonPressed(b, id, x, y, pressure);
				
				if (isDetailPageShown()){
					showDetailPage(false);
				}
				else 
					showDetailPage(true);
				
			}
			
		});
		
		copyButton = this.createButtonWithImage(ProjectManagementApp.class.getResource("copy.png"));	
		copyButton.setBackgroundColour(Color.white);
		copyButton.setBorderSize(0);
		container.addSubItem(copyButton);		
		copyButton.setOrder(2);
		copyButton.addButtonListener(new SimpleButtonAdapter() {
			
			public void buttonPressed(SimpleButton b, long id, float x,	float y, float pressure) {
				
				StaffNode staffNode = new StaffNode(contentSystem, staffModel, 80, 100, true);
				staffNode.setLocation(getLocation().x+20, getLocation().y+20);
				StaffNodeRegistry.getInstance().addNode(staffNode);
			}
		});
		
		if (!closable) return;
		
		closeButton = this.createButtonWithImage(ProjectManagementApp.class.getResource("delete_01.png"));	
		closeButton.setBackgroundColour(Color.white);
		closeButton.setBorderSize(0);
		container.addSubItem(closeButton);		
		closeButton.setOrder(2);
		closeButton.addButtonListener(new SimpleButtonAdapter() {
			
			public void buttonPressed(SimpleButton b, long id, float x,	float y, float pressure) {
				//clear();
				container.setVisible(false);
				StaffNodeRegistry.getInstance().removeNode(StaffNode.this);
				
			}
		});
	}
	
	
	protected SimpleButton createButtonWithImage(URL imageResource){
		SimpleButton btn = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		btn.setBackgroundColour(Color.white);
		btn.setAutoFitSize(false);
		btn.setWidth(15);
		btn.setHeight(15);
		btn.setBorderSize(2);
		btn.setBorderColour(Color.LIGHT_GRAY);
		btn.drawImage(imageResource, 0, 0, btn.getWidth(), btn.getHeight());
		btn.setRotateTranslateScalable(false);
		return btn;
	}
	
	public void setVisibility(boolean visible){
		container.setVisible(visible);
	}
	
	public void clear(){
		switchButton.removeButtonListeners();
		copyButton.removeButtonListeners();
		closeButton.removeButtonListeners();
		contentSystem.removeContentItem(this.container);
		this.container =null;
		
	}
	
}
