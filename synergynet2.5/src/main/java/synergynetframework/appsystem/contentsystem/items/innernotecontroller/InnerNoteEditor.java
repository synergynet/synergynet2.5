package synergynetframework.appsystem.contentsystem.items.innernotecontroller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import common.CommonResources;

import apps.conceptmap.GraphConfig;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.Updateable;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.Keyboard;
import synergynetframework.appsystem.contentsystem.items.LineItem;
import synergynetframework.appsystem.contentsystem.items.MultiLineTextLabel;
import synergynetframework.appsystem.contentsystem.items.OrthoContainer;
import synergynetframework.appsystem.contentsystem.items.OrthoContentItem;
import synergynetframework.appsystem.contentsystem.items.QuadContentItem;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.listener.BringToTopListener;
import synergynetframework.appsystem.contentsystem.items.listener.InnerNoteEditListener;
import synergynetframework.appsystem.contentsystem.items.listener.ItemEventAdapter;
import synergynetframework.appsystem.contentsystem.items.listener.OrthoControlPointRotateTranslateScaleAdapter;
import synergynetframework.appsystem.contentsystem.items.utils.Direction;
import synergynetframework.appsystem.contentsystem.items.utils.Location;
import synergynetframework.appsystem.services.net.networkedcontentmanager.controllers.NetworkedFlickController;
import synergynetframework.jme.gfx.twod.keyboard.Key;
import synergynetframework.jme.gfx.twod.keyboard.MTKeyListener;


/**
 * The Class InnerNoteEditor.
 */
public class InnerNoteEditor implements Updateable {
	
	/** The Constant log. */
	private static final Logger log = Logger.getLogger(InnerNoteEditor.class.getName());	
	
	/** The content system. */
	protected ContentSystem contentSystem;
	
	/** The note controller. */
	protected InnerNoteController noteController;
	
	/** The note node. */
	protected OrthoContainer noteNode;
	
	/** The note label. */
	protected MultiLineTextLabel noteLabel;
	
	/** The editted item. */
	protected QuadContentItem edittedItem;
	
	/** The edit button. */
	protected SimpleButton editButton;
	
	/** The keyboard. */
	protected Keyboard keyboard;
	
	/** The line. */
	protected LineItem line;
	
	/** The inner note event listeners. */
	protected transient List<InnerNoteEventListener> innerNoteEventListeners = new ArrayList<InnerNoteEventListener>();
	
	/** The is keyborad on. */
	protected boolean isKeyboradOn = false;	
	
	/** The background color. */
	protected Color backgroundColor = Color.yellow;
	
	/** The is show note animation enabled. */
	private boolean isShowNoteAnimationEnabled = false;
	
	/** The is hide note animation enabled. */
	private boolean isHideNoteAnimationEnabled = false;
	
	/** The original editted item scale. */
	private float originalEdittedItemScale = 1;
	
	/** The original note scale. */
	private float originalNoteScale = 1;
	
	/** The current editted item scale. */
	private float currentEdittedItemScale = 0;
	
	/** The current note scale. */
	private float currentNoteScale = 0;	
	
	/** The editted parent item. */
	private ContentItem edittedParentItem = null;
	
	/**
	 * Instantiates a new inner note editor.
	 *
	 * @param noteController the note controller
	 * @param edittedItem the editted item
	 * @param edittedParentItem the editted parent item
	 */
	protected InnerNoteEditor(InnerNoteController noteController, QuadContentItem edittedItem, ContentItem edittedParentItem){
		this(noteController, edittedItem);
		this.edittedParentItem = edittedParentItem;
		initNoteNode();
	}
	
	/**
	 * Instantiates a new inner note editor.
	 *
	 * @param noteController the note controller
	 * @param edittedItem the editted item
	 */
	protected InnerNoteEditor(InnerNoteController noteController, QuadContentItem edittedItem){
		
		contentSystem = edittedItem.getContentSystem();
		contentSystem.addUpdateableListener(this);
		this.noteController = noteController;
		this.edittedItem = edittedItem;	
		
		noteNode = (OrthoContainer)contentSystem.createContentItem(OrthoContainer.class);
		
		//set note label
		noteLabel = (MultiLineTextLabel)contentSystem.createContentItem(MultiLineTextLabel.class);
		noteLabel.setBorderColour(Color.white);
		noteLabel.setBorderSize(2);
		noteLabel.setBackgroundColour(this.backgroundColor);
		noteLabel.setTextColour(Color.black);
		Font font = new Font("Arial", Font.PLAIN, 12);
		noteLabel.setFont(font);		
		noteLabel.setAutoFitSize(false);
		noteLabel.setHeight(edittedItem.getHeight());
		noteLabel.setWidth(edittedItem.getWidth());			
		noteNode.addSubItem(noteLabel);
		
		//set edit button
		this.addEditButton();
		noteLabel.setLocalLocation(0, 0);
		noteLabel.setCRLFSeparatedString(edittedItem.getNote());
					
		this.listenToChanges();
		
		this.addInnerNoteEventListener(this.noteController);
		
		isShowNoteAnimationEnabled = true;
		
		initNoteNode();
		
	}
	
	/**
	 * Inits the note node.
	 */
	public void initNoteNode(){
		
		ContentItem item=null;
		if (edittedParentItem!=null){
			item = edittedParentItem;
			noteNode.setLocalLocation(item.getLocalLocation().getX()+edittedItem.getLocalLocation().getX(), item.getLocalLocation().getY()+edittedItem.getLocalLocation().getY());
			noteLabel.setHeight(noteLabel.getHeight()+edittedParentItem.getBorderSize()*2);
			noteLabel.setWidth(noteLabel.getWidth()+edittedParentItem.getBorderSize()*2);
		}
		else{
			item = edittedItem;
			noteNode.setLocalLocation(item.getLocalLocation().getX(), item.getLocalLocation().getY());
		}
			
		noteNode.setAngle(item.getAngle());
		noteNode.setScale(item.getScale());
		noteNode.setOrder(((OrthoContentItem)item).getOrder()+1);
		
		originalEdittedItemScale = item.getScale();
		originalNoteScale = noteNode.getScale();
		currentEdittedItemScale = originalEdittedItemScale;
		
		this.setAsTopObject();
	}
	
	/**
	 * Sets the location.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public void setLocation(float x, float y){
		noteNode.setLocalLocation(x, y);
	}
	
	/**
	 * Gets the location.
	 *
	 * @return the location
	 */
	public Location getLocation(){
		return noteNode.getLocalLocation();
	}

	/**
	 * Gets the background color.
	 *
	 * @return the background color
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * Sets the background color.
	 *
	 * @param backgroundColor the new background color
	 */
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	
	/**
	 * Adds the edit button.
	 */
	private void addEditButton(){
		
		editButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);		
		editButton.setAutoFitSize(false);
		editButton.setWidth(20);
		editButton.setHeight(20);
		editButton.setBorderSize(2);
		editButton.setBorderColour(Color.black);
		editButton.drawImage(GraphConfig.nodeEditImageResource, 0, 0, editButton.getWidth(), editButton.getHeight());
		editButton.addItemListener(new ItemEventAdapter(){
			
			@Override
			public void cursorReleased(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorReleased(item, id, x, y, pressure);
				if (!isKeyboradOn){
					showKeyborad();
					isKeyboradOn = true;
				}
				else{
					hideKeyborad();
					isKeyboradOn= false;
				}
			}

		});	
		noteNode.addSubItem(editButton);
		editButton.setLocalLocation(noteLabel.getWidth()/2-editButton.getWidth()/2, noteLabel.getHeight()/2-editButton.getHeight()/2);
		editButton.setOrder(noteLabel.getOrder()+1);
	}
	
	/**
	 * Show keyborad.
	 */
	private void showKeyborad(){
		if (keyboard!= null) return;
		keyboard = (Keyboard)contentSystem.createContentItem(Keyboard.class);
		keyboard.setLocalLocation(noteNode.getLocalLocation().getX(), noteNode.getLocalLocation().getY());
		keyboard.setOrder(noteNode.getOrder()+1);
		keyboard.setScale(0.5f);
		keyboard.setKeyboardImageResource(GraphConfig.nodeKeyboardImageResource);
		keyboard.setKeyDefinitions(this.getKeyDefs());
		
		line = (LineItem)contentSystem.createContentItem(LineItem.class);
		line.setSourceItem(noteNode);
		line.setSourceLocation(noteNode.getLocalLocation());
		line.setTargetItem(keyboard);
		line.setTargetLocation(keyboard.getLocalLocation());
		line.setArrowMode(LineItem.NO_ARROWS);
		line.setLineMode(LineItem.SEGMENT_LINE);

		keyboard.addKeyListener(new MTKeyListener(){

			@Override
			public void keyReleasedEvent(KeyEvent evt) {
				String text = noteLabel.getText();
				if(text == null) text ="";
				if(evt.getKeyChar() == KeyEvent.VK_ENTER){
					noteLabel.setCRLFSeparatedString(text + evt.getKeyChar());
				}
				else if(evt.getKeyChar() == KeyEvent.VK_BACK_SPACE){
					if(text.length() >0){
						text = text.substring(0,text.length()-1);
						noteLabel.setCRLFSeparatedString(text);
					}
				}
				else if(evt.getKeyChar() != KeyEvent.VK_CAPS_LOCK){
					text = text+evt.getKeyChar();
					noteLabel.setCRLFSeparatedString(text);
				}
				
				edittedItem.setNote(text);
				for (InnerNoteEventListener l: innerNoteEventListeners)
					l.noteChanged(edittedItem, text);
				
			}

			@Override
			public void keyPressedEvent(KeyEvent evt) {
				
			}});
		
		keyboard.addOrthoControlPointRotateTranslateScaleListener(new OrthoControlPointRotateTranslateScaleAdapter(){

			@Override
			public void itemTranslated(ContentItem item, float newLocationX,
					float newLocationY, float oldLocationX, float oldLocationY) {
				updateLine();
			}
			
		});
	}
	
	/**
	 * Hide keyborad.
	 */
	private void hideKeyborad(){
		if (this.keyboard!=null){
			this.contentSystem.removeContentItem(keyboard);
			keyboard = null;
		}
		
		if (this.line!=null){
			this.contentSystem.removeContentItem(line);
			line = null;
		}
	}
	
	/**
	 * Gets the key defs.
	 *
	 * @return the key defs
	 */
	@SuppressWarnings("unchecked")
	private List<Key> getKeyDefs() {
		try {
			ObjectInputStream ois = new ObjectInputStream(CommonResources.class.getResourceAsStream("keyboard.def"));
			return (List<Key>) ois.readObject();
		} catch (FileNotFoundException e) {
			log.warning("Key Defs file has not been found.");
		} catch (IOException e) {
			log.warning("IOException when accessing Key Defs file.");
		} catch (ClassNotFoundException e) {
			log.warning("ClassNotFoundExcetpion when accessing Key Defs file.");
		}
		return null;
	}
	
	/**
	 * Update line.
	 */
	private void updateLine(){
		if (line!=null && keyboard!=null){
			line.setSourceLocation(noteNode.getLocalLocation());
			line.setTargetLocation(keyboard.getLocalLocation());
		}
	}
	
	/**
	 * Listen to changes.
	 */
	private void listenToChanges(){
		
		noteNode.addBringToTopListener(new BringToTopListener(){

			@Override
			public void itemBringToToped(ContentItem item) {
				for (InnerNoteEventListener l: innerNoteEventListeners){
					if (edittedParentItem!=null)
						l.noteBringToTop(edittedParentItem);
					else
						l.noteBringToTop(edittedItem);		
				}
				if (edittedParentItem!=null)
					((OrthoContentItem)edittedParentItem).setAsTopObject();
				else
					((OrthoContentItem)edittedItem).setAsTopObject();
				setAsTopObject();
			}

		});
		
		noteNode.addOrthoControlPointRotateTranslateScaleListener(new OrthoControlPointRotateTranslateScaleAdapter(){

			@Override
			public void itemRotated(ContentItem item, float newAngle,
					float oldAngle) {
				if (edittedParentItem!=null)
					edittedParentItem.setAngle(newAngle);
				else
					edittedItem.setAngle(newAngle);
				
				for (InnerNoteEventListener l: innerNoteEventListeners){
					if (edittedParentItem!=null)
						l.noteRotated(edittedParentItem, newAngle, oldAngle);
					else
						l.noteRotated(edittedItem, newAngle, oldAngle);	
				}
			}

			@Override
			public void itemScaled(ContentItem item, float newScaleFactor,
					float oldScaleFactor) {
				if (edittedParentItem!=null)
					edittedParentItem.setScale(newScaleFactor);
				else
					edittedItem.setScale(newScaleFactor);
				for (InnerNoteEventListener l: innerNoteEventListeners){
					if (edittedParentItem!=null)
						l.noteScaled(edittedParentItem, newScaleFactor, oldScaleFactor);
					else
						l.noteScaled(edittedItem, newScaleFactor, oldScaleFactor);
				}
			}

			@Override
			public void itemTranslated(ContentItem item, float newLocationX,
					float newLocationY, float oldLocationX, float oldLocationY) {
				if (edittedParentItem!=null)
					edittedParentItem.setLocalLocation(newLocationX, newLocationY);
				else
					edittedItem.setLocalLocation(newLocationX, newLocationY);
				updateLine();
				for (InnerNoteEventListener l: innerNoteEventListeners){
					if (edittedParentItem!=null)
						l.noteTranslated(edittedParentItem, newLocationX, newLocationY, oldLocationX, oldLocationY);
					else
						l.noteTranslated(edittedItem, newLocationX, newLocationY, oldLocationX, oldLocationY);
				}
			}
			
		});
		noteLabel.addItemListener(new ItemEventAdapter(){
			@Override
			public void cursorClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorClicked(item, id, x, y, pressure);
				QuadContentItem contentItem = (QuadContentItem)item;
				Rectangle center = new Rectangle(contentItem.getWidth()-20, contentItem.getHeight()-20, contentItem.getWidth(), contentItem.getHeight());
				if (center.contains(x, y)){	
					if (edittedParentItem!=null)
						((OrthoContentItem)edittedParentItem).setAsTopObject();
					else
						((OrthoContentItem)edittedItem).setAsTopObject();
					
					originalNoteScale = noteNode.getScale();
					currentNoteScale = originalNoteScale;
					originalEdittedItemScale = originalNoteScale;
					currentEdittedItemScale = 0;
					isHideNoteAnimationEnabled = true;	
								
				}
			}		
		});
	}
	
	/**
	 * Clear note editor.
	 */
	public void clearNoteEditor(){
		if (noteLabel!=null){
			contentSystem.removeContentItem(noteLabel);
			noteLabel = null;
		}
		if (editButton!=null){
			contentSystem.removeContentItem(editButton);
			editButton = null;
		}
		hideKeyborad();		
	}
	
	/**
	 * Sets the note.
	 *
	 * @param s the new note
	 */
	public void setNote(String s){
		this.noteLabel.setCRLFSeparatedString(s);
		this.edittedItem.setNote(s);
	}

	/**
	 * Gets the note controller.
	 *
	 * @return the note controller
	 */
	public InnerNoteController getNoteController() {
		return noteController;
	}

	/**
	 * Gets the note node.
	 *
	 * @return the note node
	 */
	public OrthoContainer getNoteNode() {
		return noteNode;
	}

	/**
	 * Sets the note node.
	 *
	 * @param noteNode the new note node
	 */
	public void setNoteNode(OrthoContainer noteNode) {
		this.noteNode = noteNode;
	}
	
	/**
	 * Sets the as top object.
	 */
	public void setAsTopObject(){
		getNoteNode().setAsTopObject();
		this.editButton.setOrder(getNoteNode().getOrder()-1);
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
	
	/**
	 * Show node animation.
	 *
	 * @param tpf the tpf
	 */
	private void showNodeAnimation(float tpf){
	
		ContentItem item=null;
		if (edittedParentItem!=null)
			item = edittedParentItem;
		else
			item = edittedItem;
				
		noteNode.setVisible(false);

		if (currentEdittedItemScale>0){
			item.setScale(currentEdittedItemScale, Direction.X);
			currentEdittedItemScale-=tpf*3.5*originalNoteScale;
		}
		else if (currentNoteScale<originalNoteScale){
			noteNode.setVisible(true);
			this.noteNode.setScale(currentNoteScale, Direction.X);
			currentNoteScale +=tpf*3.5*originalNoteScale;
		}
		else{
			noteNode.setVisible(true);
			this.noteNode.setScale(originalNoteScale, Direction.X);
			item.setScale(originalEdittedItemScale, Direction.X);
			this.isShowNoteAnimationEnabled = false;
		}
	}
	
	/**
	 * Hide node animation.
	 *
	 * @param tpf the tpf
	 */
	private void hideNodeAnimation(float tpf){

		ContentItem item=null;
		if (edittedParentItem!=null)
			item = edittedParentItem;
		else
			item = edittedItem;
	
		if (currentNoteScale>0){
			item.setVisible(false);
			this.noteNode.setScale(currentNoteScale, Direction.X);
			
			currentNoteScale-=tpf*3.5*originalNoteScale;
		}
		else if (this.currentEdittedItemScale<this.originalEdittedItemScale){
			noteNode.setVisible(false);
			item.setVisible(true);
			item.setScale(currentEdittedItemScale, Direction.X);
			currentEdittedItemScale +=tpf*3.5*originalNoteScale;
		}
		else{
			item.setScale(originalEdittedItemScale, Direction.X);
			this.isHideNoteAnimationEnabled = false;
			
			//remove noteNode
			if (edittedParentItem==null)
				noteController.removeNoteEditor(edittedItem);
			else
				noteController.removeNoteEditor(edittedItem, edittedParentItem);
			for (InnerNoteEventListener l: innerNoteEventListeners)
				l.noteLabelOn(item, false);			
		}
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.Updateable#update(float)
	 */
	@Override
	public void update(float tpf) {
		if (this.isShowNoteAnimationEnabled){
			showNodeAnimation(tpf);
		}else if (this.isHideNoteAnimationEnabled){
			this.hideNodeAnimation(tpf);
		}
	}

	/**
	 * Make flickable.
	 */
	public void makeFlickable() {
		noteLabel.makeFlickable(NetworkedFlickController.DefaultDeceleration);		
	}
	
	/**
	 * Make unflickable.
	 */
	public void makeUnflickable() {
		noteLabel.makeUnflickable();		
	}
	
	/**
	 * Checks if is flickable.
	 *
	 * @return true, if is flickable
	 */
	public boolean isFlickable() {
		return noteLabel.isFlickable();		
	}
}
