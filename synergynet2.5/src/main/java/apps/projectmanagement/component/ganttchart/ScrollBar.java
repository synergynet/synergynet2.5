package apps.projectmanagement.component.ganttchart;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.Frame;
import synergynetframework.appsystem.contentsystem.items.Window;

import apps.projectmanagement.gesture.ScrollBarControl;
import apps.projectmanagement.gesture.ScrollBarControl.ScrollBarListener;

import com.jme.scene.Spatial;


/**
 * The Class ScrollBar.
 */
public class ScrollBar {

	/** The length. */
	private int length;
	
	/** The heigth. */
	private int heigth;
	
	/** The content length. */
	private int contentLength;
	
	/** The frame. */
	private Window frame;
	
	/** The handle bar. */
	private Frame handleBar;
	
	/** The content system. */
	private ContentSystem contentSystem;
	
	/** The border size. */
	private int borderSize = 2;
	
	/**
	 * The Enum Direction.
	 */
	public enum Direction{/** The h. */
H, /** The v. */
 V};
	
	/** The direction. */
	protected Direction direction = Direction.H;
	
	/** The scroll bar listeners. */
	protected List<ScrollListener> scrollBarListeners = new ArrayList<ScrollListener>();
	
	/**
	 * Instantiates a new scroll bar.
	 *
	 * @param length the length
	 * @param heigth the heigth
	 * @param contentLength the content length
	 * @param contentSystem the content system
	 * @param direction the direction
	 */
	public ScrollBar(int length, int heigth, int contentLength, ContentSystem contentSystem, Direction direction){
		this.length = length;
		this.heigth = heigth;
		this.contentLength = contentLength;
		this.contentSystem=contentSystem;
		this.direction = direction;
		
		init();
	}
	
	/**
	 * Inits the.
	 */
	protected void init(){
		frame = (Window) contentSystem.createContentItem(Window.class);
		if (this.direction==Direction.H){
			frame.setWidth(length+borderSize);
			frame.setHeight(heigth+borderSize);
		}
		else{
			frame.setWidth(heigth+borderSize);
			frame.setHeight(length+borderSize);
		}
		
		frame.setLocation(0,0);
		frame.setBorderSize(2);
		frame.setBorderColour(Color.darkGray);
		frame.setRotateTranslateScalable(false);

		int barLength = length*length/contentLength;
		int barX = -(length/2-barLength/2)+borderSize;
		if (this.direction==Direction.V){
			barX = (length/2-barLength/2)-borderSize;
		}
		
		handleBar = (Frame) contentSystem.createContentItem(Frame.class);
		
		if (this.direction==Direction.H){
			handleBar.setLocalLocation(barX, 0);
			handleBar.setWidth(barLength);
			handleBar.setHeight((int) (heigth*0.8));
		}
		else{
			handleBar.setLocalLocation(0, barX);
			handleBar.setWidth((int) (heigth*0.8));
			handleBar.setHeight(barLength);
		}

		handleBar.setBorderSize(1);
		handleBar.setBackgroundColour(Color.orange);
		handleBar.setRotateTranslateScalable(false);
		
		frame.addSubItem(handleBar);
		
		Spatial handleBarSpatial = (Spatial)(handleBar.getImplementationObject());
		ScrollBarControl scrollBarControl = new ScrollBarControl(handleBarSpatial, handleBarSpatial, length, barLength, contentLength, barX, direction);
		scrollBarControl.addScrollBarListener(new ScrollBarListener (){

			@Override
			public void moved(float scrollBarMovedDistance,
					float contentMovedDistance) {
				for (ScrollListener l: scrollBarListeners)
					l.moved(scrollBarMovedDistance, contentMovedDistance);

			}
			
		});
	
	}
	
	/**
	 * Adds the scroll bar listener.
	 *
	 * @param l the l
	 */
	public void addScrollBarListener(ScrollListener l){
		scrollBarListeners.add(l);
	}

	/**
	 * Removes the scroll bar listener.
	 *
	 * @param l the l
	 */
	public void removeScrollBarListener(ScrollListener l){
		if (scrollBarListeners.contains(l))
			scrollBarListeners.remove(l);
	}

	/**
	 * The listener interface for receiving scroll events.
	 * The class that is interested in processing a scroll
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addScrollListener<code> method. When
	 * the scroll event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see ScrollEvent
	 */
	public interface ScrollListener {
		
		/**
		 * Moved.
		 *
		 * @param scrollBarMovedDistance the scroll bar moved distance
		 * @param contentMovedDistance the content moved distance
		 */
		public void moved(float scrollBarMovedDistance, float contentMovedDistance);
	}
	
	/**
	 * Gets the scroll bar content item.
	 *
	 * @return the scroll bar content item
	 */
	public Window getScrollBarContentItem(){
		return frame;
	}
	
}
