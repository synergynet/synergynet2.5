package synergynetframework.appsystem.contentsystem.items.innernotecontroller;

import synergynetframework.appsystem.contentsystem.items.ContentItem;

/**
 * The listener interface for receiving innerNoteEvent events. The class that is
 * interested in processing a innerNoteEvent event implements this interface,
 * and the object created with that class is registered with a component using
 * the component's <code>addInnerNoteEventListener<code> method. When
 * the innerNoteEvent event occurs, that object's appropriate
 * method is invoked.
 *
 * @see InnerNoteEventEvent
 */
public interface InnerNoteEventListener {
	
	/**
	 * Note bring to top.
	 *
	 * @param edittedItem
	 *            the editted item
	 */
	public void noteBringToTop(ContentItem edittedItem);

	/**
	 * Note changed.
	 *
	 * @param item
	 *            the item
	 * @param text
	 *            the text
	 */
	public void noteChanged(ContentItem item, String text);

	/**
	 * Note label on.
	 *
	 * @param item
	 *            the item
	 * @param noteLabelOn
	 *            the note label on
	 */
	public void noteLabelOn(ContentItem item, boolean noteLabelOn);

	/**
	 * Note rotated.
	 *
	 * @param edittedItem
	 *            the editted item
	 * @param newAngle
	 *            the new angle
	 * @param oldAngle
	 *            the old angle
	 */
	public void noteRotated(ContentItem edittedItem, float newAngle,
			float oldAngle);

	/**
	 * Note scaled.
	 *
	 * @param edittedItem
	 *            the editted item
	 * @param newScaleFactor
	 *            the new scale factor
	 * @param oldScaleFactor
	 *            the old scale factor
	 */
	public void noteScaled(ContentItem edittedItem, float newScaleFactor,
			float oldScaleFactor);

	/**
	 * Note translated.
	 *
	 * @param edittedItem
	 *            the editted item
	 * @param newLocationX
	 *            the new location x
	 * @param newLocationY
	 *            the new location y
	 * @param oldLocationX
	 *            the old location x
	 * @param oldLocationY
	 *            the old location y
	 */
	public void noteTranslated(ContentItem edittedItem, float newLocationX,
			float newLocationY, float oldLocationX, float oldLocationY);

}
