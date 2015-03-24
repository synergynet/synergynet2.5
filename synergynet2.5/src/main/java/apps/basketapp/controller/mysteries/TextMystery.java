package apps.basketapp.controller.mysteries;

import java.util.ArrayList;
import java.util.List;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.LightImageLabel;
import apps.basketapp.BasketAppResources;

/**
 * The Class TextMystery.
 */
public class TextMystery {
	
	/** The max scale. */
	public static float MIN_SCALE = 0.1f, MAX_SCALE = 1.5f;
	
	/**
	 * Gets the mystery.
	 *
	 * @param contentSystem
	 *            the content system
	 * @return the mystery
	 */
	public List<ContentItem> getMystery(ContentSystem contentSystem) {
		List<ContentItem> items = new ArrayList<ContentItem>();
		
		LightImageLabel blog = (LightImageLabel) contentSystem
				.createContentItem(LightImageLabel.class);
		blog.drawImage(BasketAppResources.class.getResource("blog.png"));
		blog.setScale(0.5f);
		blog.setScaleLimit(MIN_SCALE, MAX_SCALE);
		blog.centerItem();
		blog.rotateRandom();
		blog.setNote("text");
		items.add(blog);
		
		LightImageLabel hw = (LightImageLabel) contentSystem
				.createContentItem(LightImageLabel.class);
		hw.drawImage(BasketAppResources.class.getResource("hw.jpg"));
		hw.setScale(0.5f);
		hw.setScaleLimit(MIN_SCALE, MAX_SCALE);
		hw.centerItem();
		hw.rotateRandom();
		hw.setNote("text");
		items.add(hw);
		
		LightImageLabel let01 = (LightImageLabel) contentSystem
				.createContentItem(LightImageLabel.class);
		let01.drawImage(BasketAppResources.class.getResource("let01.png"));
		let01.setScale(0.5f);
		let01.setScaleLimit(MIN_SCALE, MAX_SCALE);
		let01.centerItem();
		let01.rotateRandom();
		let01.setNote("text");
		items.add(let01);
		
		LightImageLabel let02 = (LightImageLabel) contentSystem
				.createContentItem(LightImageLabel.class);
		let02.drawImage(BasketAppResources.class.getResource("let02.png"));
		let02.setScale(0.5f);
		let02.setScaleLimit(MIN_SCALE, MAX_SCALE);
		let02.centerItem();
		let02.rotateRandom();
		let02.setNote("text");
		items.add(let02);
		
		LightImageLabel let03 = (LightImageLabel) contentSystem
				.createContentItem(LightImageLabel.class);
		let03.drawImage(BasketAppResources.class.getResource("let03.png"));
		let03.setScale(0.5f);
		let03.setScaleLimit(MIN_SCALE, MAX_SCALE);
		let03.centerItem();
		let03.rotateRandom();
		let03.setNote("text");
		items.add(let03);
		
		return items;
	}
	
}
