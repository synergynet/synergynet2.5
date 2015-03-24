package apps.tablepositionsetup;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;

import com.jme.system.DisplaySystem;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.LineItem;
import synergynetframework.appsystem.contentsystem.items.TextLabel;
import synergynetframework.appsystem.contentsystem.items.listener.ItemListener;
import synergynetframework.appsystem.contentsystem.items.utils.Location;


/**
 * The Class Slider.
 */
public class Slider {

	/** The suffix text. */
	private TextLabel prefixText, distanceText, suffixText = null;
	
	/** The marker run. */
	private LineItem markerOne, markerTwo, markerDistance, markerRun = null;
	
	/** The result. */
	private float result = 0;
	
	/** The loc y. */
	private int height, min, max, width, locX, locY = 0;

	/**
	 * Instantiates a new slider.
	 *
	 * @param contentSystem the content system
	 * @param x the x
	 * @param y the y
	 * @param widthNew the width new
	 * @param heightNew the height new
	 * @param maxValue the max value
	 * @param minValue the min value
	 * @param prefix the prefix
	 * @param suffix the suffix
	 */
	public Slider(ContentSystem contentSystem, int x, int y, int widthNew, int heightNew, int maxValue, int minValue, String prefix, String suffix){
		this.locX = x;
		this.locY = y;
		this.height = heightNew;
		this.min = minValue;
		this.max = maxValue;
		this.width = widthNew;
		markerOne = (LineItem)contentSystem.createContentItem(LineItem.class);
		markerOne.setSourceLocation(new Location(x - width/2, y + height/2, 0));
		markerOne.setTargetLocation(new Location(x - width/2, y - height/2, 0));
		markerOne.setArrowMode(3);
		markerOne.setWidth(25);

		markerRun = (LineItem)contentSystem.createContentItem(LineItem.class);
		markerRun.setSourceLocation(new Location(x - width/2, y, 0));
		markerRun.setTargetLocation(new Location(x + width/2, y, 0));
		markerRun.setArrowMode(3);

		markerDistance = (LineItem)contentSystem.createContentItem(LineItem.class);
		markerDistance.setSourceLocation(new Location(x - width/2, y+height/2, 0));
		markerDistance.setTargetLocation(new Location(x, y+height/2, 0));
		
		prefixText = (TextLabel)contentSystem.createContentItem(TextLabel.class);
		prefixText.setBackgroundColour(Color.black);
		prefixText.setBorderSize(0);
		prefixText.setTextColour(Color.white);
		prefixText.setFont(new Font("Arial", Font.PLAIN,20));
		prefixText.setRotateTranslateScalable(false);
		prefixText.setText(prefix);

		distanceText = (TextLabel)contentSystem.createContentItem(TextLabel.class);
		distanceText.setBackgroundColour(Color.black);
		distanceText.setBorderSize(0);
		distanceText.setTextColour(Color.white);
		distanceText.setFont(new Font("Arial", Font.PLAIN,20));
		distanceText.setRotateTranslateScalable(false);
		
		suffixText = (TextLabel)contentSystem.createContentItem(TextLabel.class);
		suffixText.setBackgroundColour(Color.black);
		suffixText.setBorderSize(0);
		suffixText.setTextColour(Color.white);
		suffixText.setFont(new Font("Arial", Font.PLAIN,20));
		suffixText.setRotateTranslateScalable(false);
		suffixText.setText(suffix);

		markerTwo = (LineItem)contentSystem.createContentItem(LineItem.class);
		markerTwo.setSourceLocation(new Location(x, y+height/2, 0));
		markerTwo.setTargetLocation(new Location(x, y-height/2, 0));
		markerTwo.setArrowMode(3);
		markerTwo.setWidth(25);
		markerTwo.addItemListener(new ItemListener(){

			@Override
			public void cursorChanged(ContentItem item, long id, float x, float y, float pressure) {
				x = x * DisplaySystem.getDisplaySystem().getRenderer().getWidth();
				if (x > locX - width/2 && x < locX + width/2){
					markerTwo.setSourceLocation(new Location(x, locY+height/2,0));
					markerTwo.setTargetLocation(new Location(x, locY-height/2,0));
					markerDistance.setTargetLocation(new Location(x, locY+height/2, 0));					
					markerTwo.setAsTopObject();
					markerDistance.setAsTopObject();
					result =  ((markerTwo.getSourceLocation().x - markerOne.getSourceLocation().x)+min) * (max-min)/width;
					setText();
				}else if(x > locX - width/2 && result != max){
					result = max;
					setText();
				}else if(x < locX + width/2){
					result = min;
					setText();
				}
			}

			@Override
			public void cursorClicked(ContentItem item, long id, float x, float y, float pressure) {}

			@Override
			public void cursorDoubleClicked(ContentItem item, long id, float x,	float y, float pressure) {}

			@Override
			public void cursorLongHeld(ContentItem item, long id, float x, float y, float pressure) {}

			@Override
			public void cursorPressed(ContentItem item, long id, float x, float y, float pressure) {}

			@Override
			public void cursorReleased(ContentItem item, long id, float x, float y, float pressure) {}

			@Override
			public void cursorRightClicked(ContentItem item, long id, float x, float y, float pressure) {}
		});
		markerTwo.setAsTopObject();
		markerDistance.setAsTopObject();

		result =  ((markerTwo.getSourceLocation().x - markerOne.getSourceLocation().x)+min) * (max-min)/width;
		distanceText.setLocalLocation(locX, y + height/2 + distanceText.getHeight()/2 + 10);
		prefixText.setLocalLocation(distanceText.getLocation().x - (distanceText.getWidth()/2) - (prefixText.getWidth()/2),distanceText.getLocation().y);
		suffixText.setLocalLocation(distanceText.getLocation().x + distanceText.getWidth()/2 + suffixText.getWidth()/2, locY+height/2 + distanceText.getHeight()/2 + 10);
	}
	
	/**
	 * Sets the text.
	 */
	private void setText(){
		distanceText.setText(new DecimalFormat("0.##").format(result));
		distanceText.setLocalLocation(prefixText.getLocation().x + prefixText.getWidth()/2 + distanceText.getWidth()/2, locY+height/2 + distanceText.getHeight()/2 + 10);
		suffixText.setLocalLocation(distanceText.getLocation().x + distanceText.getWidth()/2 + suffixText.getWidth()/2, locY+height/2 + distanceText.getHeight()/2 + 10);
	}

	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(float value){
		result = value;
		float newMarkerXPos = ((result/(max-min))*width) + markerOne.getSourceLocation().x;
		markerTwo.setSourceLocation(new Location(newMarkerXPos, markerTwo.getSourceLocation().y, 0));
		markerTwo.setTargetLocation(new Location(newMarkerXPos, markerTwo.getTargetLocation().y, 0));
		markerDistance.setTargetLocation(new Location(newMarkerXPos, markerDistance.getTargetLocation().y, 0));
		setText();
		markerTwo.setAsTopObject();
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public float getValue(){
		return result;
	}

	/**
	 * Destroy slider.
	 */
	public void destroySlider(){
		markerOne.setVisible(false, true);
		markerTwo.setVisible(false, true);
		markerDistance.setVisible(false, true);
		markerRun.setVisible(false, true);
		distanceText.setVisible(false, true);
		prefixText.setVisible(false, true);
		suffixText.setVisible(false, true);
	}

}
