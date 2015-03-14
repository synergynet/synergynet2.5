package synergynetframework.appsystem.contentsystem.items.innernotecontroller;

import synergynetframework.appsystem.contentsystem.items.ContentItem;

public interface InnerNoteEventListener {

	public void noteBringToTop(ContentItem edittedItem);
	public void noteRotated(ContentItem edittedItem, float newAngle,
				float oldAngle);
	public void noteScaled(ContentItem edittedItem, float newScaleFactor,
				float oldScaleFactor);
	public void noteTranslated(ContentItem edittedItem, float newLocationX,
			float newLocationY, float oldLocationX, float oldLocationY);
	public void noteChanged(ContentItem item, String text);
	public void noteLabelOn(ContentItem item, boolean noteLabelOn);
	
}
