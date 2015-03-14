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

public class ScrollBar {

	private int length;
	private int heigth;
	private int contentLength;
	private Window frame;
	private Frame handleBar;
	private ContentSystem contentSystem;
	private int borderSize = 2;
	
	public enum Direction{H, V};
	protected Direction direction = Direction.H;
	
	protected List<ScrollListener> scrollBarListeners = new ArrayList<ScrollListener>();
	
	public ScrollBar(int length, int heigth, int contentLength, ContentSystem contentSystem, Direction direction){
		this.length = length;
		this.heigth = heigth;
		this.contentLength = contentLength;
		this.contentSystem=contentSystem;
		this.direction = direction;
		
		init();
	}
	
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
	
	public void addScrollBarListener(ScrollListener l){
		scrollBarListeners.add(l);
	}

	public void removeScrollBarListener(ScrollListener l){
		if (scrollBarListeners.contains(l))
			scrollBarListeners.remove(l);
	}

	public interface ScrollListener {
		public void moved(float scrollBarMovedDistance, float contentMovedDistance);
	}
	
	public Window getScrollBarContentItem(){
		return frame;
	}
	
}
