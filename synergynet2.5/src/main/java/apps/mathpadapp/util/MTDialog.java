package apps.mathpadapp.util;

import apps.mathpadapp.conceptmapping.GraphManager;
import synergynetframework.appsystem.contentsystem.ContentSystem;

public abstract class MTDialog extends MTFrame{

	protected MTFrame parentFrame;
	protected boolean isModal = false;
	
	public MTDialog(MTFrame parentFrame, ContentSystem contentSystem) {
		this(parentFrame, contentSystem, null);
	}

	public MTDialog(MTFrame parentFrame, final ContentSystem contentSystem, GraphManager graphManager){
		super(contentSystem, graphManager);
		this.parentFrame = parentFrame;
		this.getWindow().setAsTopObject();
		if(parentFrame != null && contentSystem.getAllContentItems().containsValue(parentFrame.getWindow())){
			parentFrame.getWindow().setOrder(-1);
			this.getWindow().setAngle(parentFrame.getWindow().getAngle());
			this.getWindow().setScale(parentFrame.getWindow().getScale());
			this.getWindow().setLocalLocation(parentFrame.getWindow().getLocalLocation());
		}
		setModalState();
	}
	
	private void setModalState() {
		if(parentFrame != null && contentSystem.getAllContentItems().containsValue(parentFrame.getWindow())){
			parentFrame.getWindow().setBringToTopable(!isModal);
			parentFrame.getWindow().setRotateTranslateScalable(!isModal);
		}
	}

	public boolean isModal(){
		return isModal;
	}
	
	public void setModal(boolean isModal){
		this.isModal = isModal;
		setModalState();
	}
	
	@Override
	public void close(){
		if(parentFrame != null && contentSystem.getAllContentItems().containsValue(parentFrame.getWindow())){
			parentFrame.getWindow().setOrder(-1);
			parentFrame.getWindow().setBringToTopable(true);
			parentFrame.getWindow().setRotateTranslateScalable(true);
		}
		super.close();
	}
	
	public void setVisible(boolean isVisible){
		if(isVisible){
			if(parentFrame != null && contentSystem.getAllContentItems().containsValue(parentFrame.getWindow())){
				this.getWindow().setAsTopObject();
				this.getWindow().setAngle(parentFrame.getWindow().getAngle());
				this.getWindow().setLocalLocation(parentFrame.getWindow().getLocalLocation());
				this.getWindow().setScale(parentFrame.getWindow().getScale());

			}
			setModalState();
		}
		else{
			if(this.isModal() && this.getParent()!= null && contentSystem.getAllContentItems().containsValue(parentFrame.getWindow())){
					this.getParent().getWindow().setBringToTopable(true);
					this.getParent().getWindow().setRotateTranslateScalable(true);
				}
		}
		this.getWindow().setVisible(isVisible);
	}
	
	public MTFrame getParent(){
		return parentFrame;
	}
}
