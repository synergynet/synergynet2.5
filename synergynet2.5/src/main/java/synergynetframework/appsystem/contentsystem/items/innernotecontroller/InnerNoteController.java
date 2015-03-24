package synergynetframework.appsystem.contentsystem.items.innernotecontroller;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.OrthoContentItem;
import synergynetframework.appsystem.contentsystem.items.QuadContentItem;
import synergynetframework.appsystem.contentsystem.items.listener.InnerNoteEditListener;
import synergynetframework.appsystem.contentsystem.items.listener.ItemEventAdapter;


/**
 * The Class InnerNoteController.
 */
public class InnerNoteController implements InnerNoteEventListener{

	/** The items. */
	protected List<ContentItem> items = new ArrayList<ContentItem>();
	
	/** The note editors. */
	protected Map<QuadContentItem, InnerNoteEditor> noteEditors = new HashMap<QuadContentItem, InnerNoteEditor> ();
	
	/** The self. */
	protected InnerNoteController self;
	
	/** The inner note event listeners. */
	protected transient List<InnerNoteEventListener> innerNoteEventListeners = new ArrayList<InnerNoteEventListener>();
	
	/**
	 * Instantiates a new inner note controller.
	 */
	public InnerNoteController(){
		self = this;
	}
	
	/**
	 * Adds the note controller.
	 *
	 * @param collection the collection
	 */
	public void addNoteController(Collection<ContentItem> collection){
		this.items.clear();
		for (ContentItem item: collection)
			this.items.add(item);
		noteEditors.clear();
		this.addNoteController();
	}
	
	/**
	 * Adds the note controller.
	 *
	 * @param item the item
	 */
	public void addNoteController(ContentItem item){
		this.items.add(item);
		final QuadContentItem quadItem = (QuadContentItem)item;
		quadItem.addItemListener(new ItemEventAdapter(){

			@Override
			public void cursorClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorClicked(item, id, x, y, pressure);
				QuadContentItem contentItem = (QuadContentItem)item;
				Rectangle rightBottomCorner = new Rectangle(contentItem.getWidth()-20, contentItem.getHeight()-20, contentItem.getWidth(), contentItem.getHeight());
				if (rightBottomCorner.contains(x, y)){
					for (InnerNoteEventListener l: innerNoteEventListeners)
						l.noteLabelOn(quadItem, true);
					addNoteEditor(quadItem);	
					
				}
			}				
		});
	}
	
	/**
	 * Adds the note controller.
	 *
	 * @param parentItem the parent item
	 * @param mainSubItem the main sub item
	 */
	public void addNoteController(final ContentItem parentItem, final QuadContentItem mainSubItem){
		this.items.add(parentItem);
		mainSubItem.addItemListener(new ItemEventAdapter(){
			@Override
			public void cursorDoubleClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorClicked(item, id, x, y, pressure);
				Rectangle rightBottomCorner = new Rectangle(mainSubItem.getWidth()-40, mainSubItem.getHeight()-40, mainSubItem.getWidth(), mainSubItem.getHeight());
				if (rightBottomCorner.contains(x, mainSubItem.getHeight()-y)){
					for (InnerNoteEventListener l: innerNoteEventListeners)
						l.noteLabelOn(item, true);
					addNoteEditor(mainSubItem, parentItem);					
				}
			}				
		});
	}
	
	/**
	 * Adds the note controller.
	 */
	private void addNoteController(){
		for (final ContentItem ci: items){
			final QuadContentItem quadItem = (QuadContentItem)ci;
			quadItem.addItemListener(new ItemEventAdapter(){

				@Override
				public void cursorClicked(ContentItem item, long id, float x,
						float y, float pressure) {
					super.cursorClicked(item, id, x, y, pressure);
					QuadContentItem contentItem = (QuadContentItem)item;
					Rectangle rightBottomCorner = new Rectangle(contentItem.getWidth()-20, contentItem.getHeight()-20, contentItem.getWidth(), contentItem.getHeight());
					if (rightBottomCorner.contains(x, y)){
						for (InnerNoteEventListener l: innerNoteEventListeners)
							l.noteLabelOn(quadItem, true);
						addNoteEditor(quadItem);	
						
					}
				}				
			});
		}
	}
	
	/**
	 * Adds the note editor.
	 *
	 * @param quadItem the quad item
	 */
	public void addNoteEditor(QuadContentItem quadItem){
		if (noteEditors.containsKey(quadItem))
			return;
		noteEditors.put(quadItem, new InnerNoteEditor(self, quadItem));
		quadItem.setRotateTranslateScalable(false);
	}
	
	/**
	 * Adds the note editor.
	 *
	 * @param quadItem the quad item
	 * @param parentItem the parent item
	 */
	public void addNoteEditor(QuadContentItem quadItem, ContentItem parentItem){
		if (noteEditors.containsKey(quadItem))
			return;
		noteEditors.put(quadItem, new InnerNoteEditor(self, quadItem, parentItem));
		((OrthoContentItem)parentItem).setRotateTranslateScalable(false);
	}
	
	/**
	 * Removes the note editor.
	 *
	 * @param item the item
	 */
	public void removeNoteEditor(QuadContentItem item){

		if (noteEditors.containsKey(item)){
			noteEditors.get(item).clearNoteEditor();
			noteEditors.remove(item);
			item.setRotateTranslateScalable(true);
		}
	}
	
	/**
	 * Removes the note editor.
	 *
	 * @param item the item
	 * @param parentItem the parent item
	 */
	public void removeNoteEditor(QuadContentItem item, ContentItem parentItem){

		if (noteEditors.containsKey(item)){
			noteEditors.get(item).clearNoteEditor();
			noteEditors.remove(item);
			((OrthoContentItem)parentItem).setRotateTranslateScalable(true);
		}
	}
	
	/**
	 * Removes the all note editors.
	 */
	public void removeAllNoteEditors(){
		List<QuadContentItem> items = new ArrayList<QuadContentItem>();
		for (QuadContentItem item:noteEditors.keySet()){
			items.add(item);
		}
		
		for (QuadContentItem item:items){
			this.removeNoteEditor(item);
		}
	}
	
	/**
	 * Gets the node editors.
	 *
	 * @return the node editors
	 */
	public Map<QuadContentItem, InnerNoteEditor> getNodeEditors(){
		return this.noteEditors;
	}
	
	/**
	 * Gets the node editor.
	 *
	 * @param item the item
	 * @return the node editor
	 */
	public InnerNoteEditor getNodeEditor(QuadContentItem item){
		if (!this.noteEditors.containsKey(item)){
			return null;
		}
		else{
			return noteEditors.get(item);
		}
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.innernotecontroller.InnerNoteEventListener#noteBringToTop(synergynetframework.appsystem.contentsystem.items.ContentItem)
	 */
	@Override
	public void noteBringToTop(ContentItem edittedItem) {
		for (InnerNoteEventListener l: innerNoteEventListeners)
			l.noteBringToTop(edittedItem);			
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.innernotecontroller.InnerNoteEventListener#noteChanged(synergynetframework.appsystem.contentsystem.items.ContentItem, java.lang.String)
	 */
	@Override
	public void noteChanged(ContentItem item, String text) {
		for (InnerNoteEventListener l: innerNoteEventListeners)
			l.noteChanged(item, text);
	
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.innernotecontroller.InnerNoteEventListener#noteLabelOn(synergynetframework.appsystem.contentsystem.items.ContentItem, boolean)
	 */
	@Override
	public void noteLabelOn(ContentItem item, boolean noteLabelOn) {
		for (InnerNoteEventListener l: innerNoteEventListeners)
			l.noteLabelOn(item, noteLabelOn);
		
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.innernotecontroller.InnerNoteEventListener#noteRotated(synergynetframework.appsystem.contentsystem.items.ContentItem, float, float)
	 */
	@Override
	public void noteRotated(ContentItem edittedItem, float newAngle,
			float oldAngle) {
		for (InnerNoteEventListener l: innerNoteEventListeners)
			l.noteRotated(edittedItem, newAngle, oldAngle);	
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.innernotecontroller.InnerNoteEventListener#noteScaled(synergynetframework.appsystem.contentsystem.items.ContentItem, float, float)
	 */
	@Override
	public void noteScaled(ContentItem edittedItem, float newScaleFactor,
			float oldScaleFactor) {
		for (InnerNoteEventListener l: innerNoteEventListeners)
			l.noteScaled(edittedItem, newScaleFactor, oldScaleFactor);	
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.innernotecontroller.InnerNoteEventListener#noteTranslated(synergynetframework.appsystem.contentsystem.items.ContentItem, float, float, float, float)
	 */
	@Override
	public void noteTranslated(ContentItem edittedItem, float newLocationX,
			float newLocationY, float oldLocationX, float oldLocationY) {
		for (InnerNoteEventListener l: innerNoteEventListeners)
			l.noteTranslated(edittedItem, newLocationX, newLocationY, oldLocationX, oldLocationY);
		
	}
	
	/**
	 * Adds the inner note event listener.
	 *
	 * @param l the l
	 */
	public void addInnerNoteEventListener(InnerNoteEventListener l){
		if (this.innerNoteEventListeners==null)
			this.innerNoteEventListeners = new ArrayList<InnerNoteEventListener>();
		
		if(!this.innerNoteEventListeners.contains(l))
			this.innerNoteEventListeners.add(l);
	}
	
	/**
	 * Removes the inner note event listeners.
	 */
	public void removeInnerNoteEventListeners(){
		innerNoteEventListeners.clear();
	}
	
	/**
	 * Removes the inner note event listener.
	 *
	 * @param l the l
	 */
	public void removeInnerNoteEventListener(InnerNoteEditListener l){
		innerNoteEventListeners.remove(l);
	}
	
	
}
