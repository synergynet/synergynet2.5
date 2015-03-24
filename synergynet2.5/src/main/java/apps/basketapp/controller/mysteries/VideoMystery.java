package apps.basketapp.controller.mysteries;

import java.util.ArrayList;
import java.util.List;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.MediaPlayer;
import apps.basketapp.BasketAppResources;

/**
 * The Class VideoMystery.
 */
public class VideoMystery {

	/**
	 * Gets the mystery.
	 *
	 * @param contentSystem
	 *            the content system
	 * @return the mystery
	 */
	public List<ContentItem> getMystery(ContentSystem contentSystem) {

		List<ContentItem> items = new ArrayList<ContentItem>();
		
		MediaPlayer video1 = (MediaPlayer) contentSystem
				.createContentItem(MediaPlayer.class);
		video1.setMediaURL(BasketAppResources.class
				.getResource("Moralreasoning1.avi"));
		video1.centerItem();
		items.add(video1);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		MediaPlayer video2 = (MediaPlayer) contentSystem
				.createContentItem(MediaPlayer.class);
		video2.setMediaURL(BasketAppResources.class
				.getResource("Moralreasoning2.avi"));
		video2.centerItem();
		items.add(video2);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		MediaPlayer video3 = (MediaPlayer) contentSystem
				.createContentItem(MediaPlayer.class);
		video3.setMediaURL(BasketAppResources.class
				.getResource("Moralreasoning3.avi"));
		video3.centerItem();
		items.add(video3);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		MediaPlayer video4 = (MediaPlayer) contentSystem
				.createContentItem(MediaPlayer.class);
		video4.setMediaURL(BasketAppResources.class
				.getResource("Moralreasoning4.avi"));
		video4.centerItem();
		items.add(video4);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		MediaPlayer video5 = (MediaPlayer) contentSystem
				.createContentItem(MediaPlayer.class);
		video5.setMediaURL(BasketAppResources.class
				.getResource("Moralreasoning5.avi"));
		video5.centerItem();
		items.add(video5);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return items;
	}
}
