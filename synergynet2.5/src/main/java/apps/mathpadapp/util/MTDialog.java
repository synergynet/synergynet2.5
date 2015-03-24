package apps.mathpadapp.util;

import apps.mathpadapp.conceptmapping.GraphManager;
import synergynetframework.appsystem.contentsystem.ContentSystem;


/**
 * The Class MTDialog.
 */
public abstract class MTDialog extends MTFrame{

	/** The parent frame. */
	protected MTFrame parentFrame;
	
	/** The is modal. */
	protected boolean isModal = false;
	
	/**
	 * Instantiates a new MT dialog.
	 *
	 * @param parentFrame the parent frame
	 * @param contentSystem the content system
	 */
	public MTDialog(MTFrame parentFrame, ContentSystem contentSystem) {
		this(parentFrame, contentSystem, null);
	}

	/**
	 * Instantiates a new MT dialog.
	 *
	 * @param parentFrame the parent frame
	 * @param contentSystem the content system
	 * @param graphManager the graph manager
	 */
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
	
	/**
	 * Sets the modal state.
	 */
	private void setModalState() {
		if(parentFrame != null && contentSystem.getAllContentItems().containsValue(parentFrame.getWindow())){
			parentFrame.getWindow().setBringToTopable(!isModal);
			parentFrame.getWindow().setRotateTranslateScalable(!isModal);
		}
	}

	/**
	 * Checks if is modal.
	 *
	 * @return true, if is modal
	 */
	public boolean isModal(){
		return isModal;
	}
	
	/**
	 * Sets the modal.
	 *
	 * @param isModal the new modal
	 */
	public void setModal(boolean isModal){
		this.isModal = isModal;
		setModalState();
	}
	
	/* (non-Javadoc)
	 * @see apps.mathpadapp.util.MTFrame#close()
	 */
	@Override
	public void close(){
		if(parentFrame != null && contentSystem.getAllContentItems().containsValue(parentFrame.getWindow())){
			parentFrame.getWindow().setOrder(-1);
			parentFrame.getWindow().setBringToTopable(true);
			parentFrame.getWindow().setRotateTranslateScalable(true);
		}
		super.close();
	}
	
	/**
	 * Sets the visible.
	 *
	 * @param isVisible the new visible
	 */
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
	
	/**
	 * Gets the parent.
	 *
	 * @return the parent
	 */
	public MTFrame getParent(){
		return parentFrame;
	}
}
