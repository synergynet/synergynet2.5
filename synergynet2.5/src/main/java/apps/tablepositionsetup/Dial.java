package apps.tablepositionsetup;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;

import com.jme.math.FastMath;
import com.jme.math.Vector2f;
import com.jme.system.DisplaySystem;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.ImageTextLabel;
import synergynetframework.appsystem.contentsystem.items.LineItem;
import synergynetframework.appsystem.contentsystem.items.RoundImageLabel;
import synergynetframework.appsystem.contentsystem.items.TextLabel;
import synergynetframework.appsystem.contentsystem.items.listener.ItemEventAdapter;
import synergynetframework.appsystem.contentsystem.items.utils.Location;


/**
 * The Class Dial.
 */
public class Dial {

	/** The gap. */
	private int gap = 20;
	
	/** The distance text. */
	private TextLabel distanceText = null;
	
	/** The marker. */
	private LineItem marker = null;
	
	/** The result. */
	private float result = 0;
	
	/** The rot value. */
	private float radius, rotValue = 0;
	
	/** The measurement. */
	private String measurement;
	
	/** The bg. */
	private RoundImageLabel bg;
	
	/** The drag angle. */
	private float dragAngle = 0;
	
	/** The precision. */
	private boolean precision = false;
	
	/** The precise value. */
	private float preciseValue = 0;
	
	/** The content system. */
	private ContentSystem contentSystem;
	
	/** The upper limit. */
	private boolean lowerLimit, upperLimit = false;
	
	/** The upper bound. */
	private float lowerBound, upperBound = 0;

	/**
	 * Instantiates a new dial.
	 *
	 * @param contentSystem the content system
	 * @param xLoc the x loc
	 * @param yLoc the y loc
	 * @param dialRadius the dial radius
	 * @param startValue the start value
	 * @param valuePerFullRotation the value per full rotation
	 * @param measure the measure
	 */
	public Dial(ContentSystem contentSystem, int xLoc, int yLoc, 
			float dialRadius, float startValue, float valuePerFullRotation, String measure){
		this.measurement = measure;
		this.contentSystem = contentSystem;
		this.radius = dialRadius;
		this.rotValue = valuePerFullRotation;
		this.result = startValue;
		
		bg = (RoundImageLabel)contentSystem.createContentItem(RoundImageLabel.class);
		bg.setAutoFit(false);
		bg.setRadius(radius);
		bg.setBorderSize(3);
		bg.setBorderColour(Color.WHITE);
		bg.setBackgroundColour(Color.BLACK);
		bg.setLocation(xLoc,yLoc);
		bg.setRotateTranslateScalable(false);
		bg.setAsBottomObject();
		
		marker = (LineItem)contentSystem.createContentItem(LineItem.class);
		marker.setSourceLocation(new Location(0, 0, 0));
		marker.setTargetLocation(new Location(0, radius, 0));
		marker.setLocation(bg.getLocation());
		marker.setArrowMode(3);
		marker.setWidth(3);		
		marker.setAsTopObject();
		
		bg.setBringToTopable(false);
		marker.setBringToTopable(false);
		
		distanceText = (TextLabel)contentSystem.createContentItem(TextLabel.class);
		distanceText.setBackgroundColour(Color.black);
		distanceText.setBorderSize(0);
		distanceText.setTextColour(Color.white);
		distanceText.setFont(new Font("Arial", Font.PLAIN,20));
		distanceText.setText(new DecimalFormat("0.##").format(result) + measurement);
		distanceText.setLocation(bg.getLocation().x + radius + distanceText.getWidth()/2 + gap, bg.getLocation().y);
		distanceText.setRotateTranslateScalable(false);
		
		ItemEventAdapter ie = new ItemEventAdapter() {	
			private boolean down = false;
			private Vector2f origin = new Vector2f(bg.getLocation().x, bg.getLocation().y);
			private Vector2f touchDown = new Vector2f();
			private float lastAngle, angle = 0f;
			
			public void cursorPressed(ContentItem item, long id, float x, float y, float pressure) {
				down = true;
				touchDown = new Vector2f(x * DisplaySystem.getDisplaySystem().getRenderer().getWidth(),
						y * DisplaySystem.getDisplaySystem().getRenderer().getHeight());
				lastAngle = 0;
			}
			
			public void cursorChanged(ContentItem item, long id, float x, float y, float pressure) {
				if (down){
					Vector2f touchDrag = new Vector2f(x * DisplaySystem.getDisplaySystem().getRenderer().getWidth(),
							y * DisplaySystem.getDisplaySystem().getRenderer().getHeight());
					
					angle = new Vector2f(touchDrag.x - origin.x, touchDrag.y - origin.y).angleBetween(
							new Vector2f(touchDown.x - origin.x, touchDown.y - origin.y));
					
					float changeInAngle = lastAngle - angle;
					
					dragAngle += changeInAngle;
									
					if (changeInAngle > FastMath.DEG_TO_RAD *180){
						changeInAngle = - ((FastMath.DEG_TO_RAD *180 - FastMath.abs(lastAngle))
						 + (FastMath.DEG_TO_RAD *180 - FastMath.abs(angle)));
					}else if(changeInAngle < FastMath.DEG_TO_RAD *-180){
						changeInAngle = (FastMath.DEG_TO_RAD *180 - FastMath.abs(lastAngle))
						 + (FastMath.DEG_TO_RAD *180 - FastMath.abs(angle));
					}
					
					result -= changeInAngle * (rotValue/(2*FastMath.PI));
									
					float toDisplay = result + preciseValue;
					
					if (upperLimit){
						if (toDisplay > upperBound){
							result = upperBound - preciseValue;
							dragAngle = -result * (rotValue/2*FastMath.PI);
							toDisplay = result;
						}
					}
					if (lowerLimit){
						if (toDisplay < lowerBound){
							result = lowerBound - preciseValue;
							dragAngle = -result * (rotValue/2*FastMath.PI);
							toDisplay = result + preciseValue;
						}
					}
					
					dragAngle = dragAngle % (FastMath.DEG_TO_RAD * 360);
					
					marker.setAngle(dragAngle);				
					
					distanceText.setText(new DecimalFormat("0.##").format(toDisplay) + measurement);		
					distanceText.setLocation(bg.getLocation().x + radius + distanceText.getWidth()/2 + gap, bg.getLocation().y);
					marker.setAsTopObject();			
					
					lastAngle = angle;
					
				}
			}
			
			public void cursorReleased(ContentItem item, long id, float x, float y, float pressure) {
				down = false;
			}
			
		};
		
		bg.addItemListener(ie);
	
	}

	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(float value){
		
		dragAngle = -value * (rotValue/2*FastMath.PI);
		
		dragAngle = dragAngle % (FastMath.DEG_TO_RAD * 360);
		
		marker.setAngle(dragAngle);	
		
		result = value;
		
		distanceText.setText(new DecimalFormat("0.##").format(result) + measurement);		
		distanceText.setLocation(bg.getLocation().x + radius + distanceText.getWidth()/2 + 10, bg.getLocation().y);
		marker.setAsTopObject();	
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
	 * Destroy dial.
	 */
	public void destroyDial(){
		marker.setVisible(false, true);
		bg.setVisible(false, true);
		distanceText.setVisible(false, true);
		if (precision){
			sliderBox.setVisible(false, true);
			markerMove.setVisible(false, true);
		}
	}
	
	/**
	 * Sets the lower bound.
	 *
	 * @param bound the new lower bound
	 */
	public void setLowerBound(float bound){
		lowerLimit = true;
		lowerBound = bound;		
	}
	
	/**
	 * Sets the upper bound.
	 *
	 * @param bound the new upper bound
	 */
	public void setUpperBound(float bound){
		upperLimit = true;
		upperBound = bound;		
	}
	
	/** The marker move. */
	private LineItem markerMove = null;
	
	/** The slider box. */
	private ImageTextLabel sliderBox = null;
	
	/** The range value. */
	private float rangeValue = 0;
	
	
	/**
	 * Adds the precision slider.
	 *
	 * @param range the range
	 */
	public void addPrecisionSlider(float range){
		this.rangeValue = range;
		
		precision = true;
			
		sliderBox = (ImageTextLabel) contentSystem.createContentItem(ImageTextLabel.class);
		sliderBox.setLocation(bg.getLocation().x + radius + gap, bg.getLocation().y);
		sliderBox.setAsBottomObject();
		sliderBox.setBringToTopable(false);
		sliderBox.setAutoFit(false);
		sliderBox.setBorderSize(2);	
		sliderBox.setBorderColour(Color.WHITE);
		sliderBox.setBackgroundColour(Color.black);
		sliderBox.setRotateTranslateScalable(false);
		sliderBox.setWidth(gap);
		sliderBox.setHeight((int) (radius*2 - gap));
		
		gap = (gap*2) + sliderBox.getWidth();
		distanceText.setLocation(bg.getLocation().x + radius + distanceText.getWidth()/2 + gap, bg.getLocation().y);

		markerMove = (LineItem)contentSystem.createContentItem(LineItem.class);
		markerMove.setSourceLocation(new Location(sliderBox.getLocation().x-sliderBox.getWidth()/2, sliderBox.getLocation().y, 0));
		markerMove.setTargetLocation(new Location(sliderBox.getLocation().x+sliderBox.getWidth()/2, sliderBox.getLocation().y, 0));
		markerMove.setArrowMode(3);
		markerMove.setWidth(4);
		markerMove.addItemListener(new ItemEventAdapter(){

			public void cursorChanged(ContentItem item, long id, float x, float y, float pressure) {				
				y = y * DisplaySystem.getDisplaySystem().getRenderer().getHeight();
				
				if (y > sliderBox.getLocation().y - sliderBox.getHeight()/2 && 
						y < sliderBox.getLocation().y + sliderBox.getHeight()/2){
					
					
					if (upperLimit){
						if ((result + (y - sliderBox.getLocation().y) * (rangeValue/sliderBox.getHeight())) > upperBound){
							return;
						}
					}
					
					if (lowerLimit){
						if ((result + (y - sliderBox.getLocation().y) * (rangeValue/sliderBox.getHeight())) < lowerBound){
							return;
						}
					}
					
					markerMove.setSourceLocation(new Location(sliderBox.getLocation().x-sliderBox.getWidth()/2, y,0));
					markerMove.setTargetLocation(new Location(sliderBox.getLocation().x+sliderBox.getWidth()/2, y,0));
					markerMove.setAsTopObject();
					
					preciseValue = (markerMove.getTargetLocation().y - sliderBox.getLocation().y) * 
																		(rangeValue/sliderBox.getHeight());
					
					float toDisplay = result + preciseValue;
					
					distanceText.setText(new DecimalFormat("0.##").format(toDisplay) + measurement);		
					distanceText.setLocation(bg.getLocation().x + radius + distanceText.getWidth()/2 + gap, bg.getLocation().y);
				}
			}

		});
		markerMove.setAsTopObject();
	}

}
