package apps.remotecontrol.tableportal;

import java.awt.Color;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.Window;

/**
 * The Class Radar.
 */
public class Radar {

	/** The size_ratio. */
	public static float size_ratio = 0.15f;

	/** The radar window. */
	private Window radarWindow;

	/** The viewed area. */
	private Window viewedArea;

	/**
	 * Instantiates a new radar.
	 *
	 * @param contentSystem
	 *            the content system
	 * @param portal
	 *            the portal
	 */
	public Radar(ContentSystem contentSystem, TablePortal portal) {
		radarWindow = (Window) contentSystem.createContentItem(Window.class);
		radarWindow.setWidth(portal.backFrame.getWidth());
		radarWindow.setHeight(portal.backFrame.getHeight());
		radarWindow.setScale(size_ratio);
		radarWindow.setBackgroundColour(Color.white);
		radarWindow.setBorderSize(0);
		radarWindow.setBringToTopable(false);

		viewedArea = (Window) contentSystem.createContentItem(Window.class);
		viewedArea.setWidth(radarWindow.getWidth() - 2);
		viewedArea.setHeight(radarWindow.getHeight() - 2);
		viewedArea.setBackgroundColour(Color.white);
		viewedArea.setBorderSize(3);
		viewedArea.setBackgroundColour(Color.DARK_GRAY);
		radarWindow.addSubItem(viewedArea);

		radarWindow.setRotateTranslateScalable(false);
		radarWindow.setBringToTopable(false);
		radarWindow.getBackgroundFrame().setRotateTranslateScalable(false);
		radarWindow.getBackgroundFrame().setBringToTopable(false);

		viewedArea.setRotateTranslateScalable(false);
		viewedArea.setBringToTopable(false);
		viewedArea.getBackgroundFrame().setRotateTranslateScalable(false);
		viewedArea.getBackgroundFrame().setBringToTopable(false);
	}

	/**
	 * Gets the radar window.
	 *
	 * @return the radar window
	 */
	public Window getRadarWindow() {
		return radarWindow;
	}

	/**
	 * Gets the viewed area.
	 *
	 * @return the viewed area
	 */
	public Window getViewedArea() {
		return viewedArea;
	}
}
