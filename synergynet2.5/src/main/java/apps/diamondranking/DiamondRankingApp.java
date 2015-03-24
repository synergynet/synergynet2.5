/*
 * Copyright (c) 2009 University of Durham, England All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of 'SynergyNet' nor the names of
 * its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission. THIS SOFTWARE IS PROVIDED
 * BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package apps.diamondranking;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.BackgroundController;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.Frame;
import synergynetframework.appsystem.contentsystem.items.MultiLineTextLabel;
import synergynetframework.appsystem.contentsystem.items.OrthoContentItem;
import synergynetframework.appsystem.contentsystem.items.RoundListContainer;
import synergynetframework.appsystem.contentsystem.items.RoundTextLabel;
import synergynetframework.appsystem.contentsystem.items.TextLabel;
import synergynetframework.appsystem.contentsystem.items.listener.ItemEventAdapter;
import synergynetframework.appsystem.contentsystem.items.listener.ItemListener;
import synergynetframework.appsystem.contentsystem.items.utils.Location;
import synergynetframework.appsystem.table.SynergyNetAppUtils;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.appsystem.table.appregistry.menucontrol.HoldTopRightConfirmVisualExit;
import synergynetframework.jme.cursorsystem.fixutils.FixLocationStatus;
import core.SynergyNetDesktop;

/**
 * The Class DiamondRankingApp.
 */
public class DiamondRankingApp extends DefaultSynergyNetApp {

	/**
	 * The Class CursorPoint.
	 */
	class CursorPoint {

		/** The id. */
		private long id;

		/** The point. */
		private Point2D.Float point;

		/**
		 * Instantiates a new cursor point.
		 *
		 * @param id
		 *            the id
		 * @param point
		 *            the point
		 */
		public CursorPoint(long id, Point2D.Float point) {
			this.id = id;
			this.point = point;
		}

		/**
		 * Gets the cursor id.
		 *
		 * @return the cursor id
		 */
		public long getCursorId() {
			return id;
		}

		/**
		 * Gets the point.
		 *
		 * @return the point
		 */
		public Point2D.Float getPoint() {
			return point;
		}
	}
	
	/** The clicks per item map. */
	protected Map<ContentItem, ArrayList<CursorPoint>> clicksPerItemMap = new HashMap<ContentItem, ArrayList<CursorPoint>>();

	/** The content system. */
	protected ContentSystem contentSystem;

	/** The current sub app. */
	protected String currentSubApp = "";
	
	/** The init location. */
	protected Location initLocation = new Location(0, 0, 0);

	/** The items. */
	protected Map<String, Map<ContentItem, Location>> items = new HashMap<String, Map<ContentItem, Location>>();

	/** The lock url. */
	protected URL lockURL = DiamondRankingApp.class.getResource("padlock.jpg");
	
	/**
	 * Instantiates a new diamond ranking app.
	 *
	 * @param info
	 *            the info
	 */
	public DiamondRankingApp(ApplicationInfo info) {
		super(info);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#addContent
	 * ()
	 */
	@Override
	public void addContent() {
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(this);
		SynergyNetAppUtils.addTableOverlay(this);
		setMenuController(new HoldTopRightConfirmVisualExit(this));
		
		BackgroundController backgroundController = (BackgroundController) contentSystem
				.createContentItem(BackgroundController.class);
		backgroundController.setOrder(Integer.MAX_VALUE);
		backgroundController.setBackgroundRotateTranslateScalable(false);

		final RoundListContainer menu = (RoundListContainer) this.contentSystem
				.createContentItem(RoundListContainer.class);
		menu.setLocalLocation(250, 300);

		backgroundController.addItemListener(new ItemEventAdapter() {

			boolean topRightCornerSelected = false;

			public void cursorLongHeld(ContentItem b, long id, float x,
					float y, float pressure) {
				Rectangle leftBottomCorner = new Rectangle(0, contentSystem
						.getScreenHeight() - 30, 30, contentSystem
						.getScreenHeight());
				if (!leftBottomCorner.contains(x, y)) {
					return;
				}
				if (!topRightCornerSelected) {
					return;
				}
				try {
					SynergyNetDesktop.getInstance().showMainMenu();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

				/*
				 * if (menu.isVisible()){ menu.setVisible(false); } else{
				 * menu.setVisible(true); menu.setLocalLocation(x,
				 * contentSystem.getScreenHeight()-y); }
				 */
			}
			
			@Override
			public void cursorPressed(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorPressed(item, id, x, y, pressure);
				Rectangle rightTopCorner = new Rectangle(contentSystem
						.getScreenWidth() - 30, 0, contentSystem
						.getScreenWidth(), 30);
				if (rightTopCorner.contains(x, y)) {
					topRightCornerSelected = true;
				}
			}
			
			@Override
			public void cursorReleased(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorReleased(item, id, x, y, pressure);
				Rectangle rightTopCorner = new Rectangle(contentSystem
						.getScreenWidth() - 30, 0, contentSystem
						.getScreenWidth(), 30);
				if (rightTopCorner.contains(x, y)) {
					topRightCornerSelected = false;
				}

			}
		});
		
		final RoundTextLabel loadICTContent = (RoundTextLabel) this.contentSystem
				.createContentItem(RoundTextLabel.class);
		loadICTContent.setAutoFitSize(false);
		loadICTContent.setRadius(40f);
		loadICTContent.setBackgroundColour(Color.black);
		loadICTContent.setBorderColour(Color.green);
		loadICTContent.setTextColour(Color.green);
		loadICTContent.setLines("ICI", 40);
		loadICTContent.setFont(new Font("Arial", Font.PLAIN, 20));

		final RoundTextLabel loadImageVersionContent = (RoundTextLabel) this.contentSystem
				.createContentItem(RoundTextLabel.class);
		loadImageVersionContent.setAutoFitSize(false);
		loadImageVersionContent.setRadius(40f);
		loadImageVersionContent.setBackgroundColour(Color.black);
		loadImageVersionContent.setBorderColour(Color.green);
		loadImageVersionContent.setTextColour(Color.green);
		loadImageVersionContent.setLines("IMAGE", 40);
		loadImageVersionContent.setFont(new Font("Arial", Font.PLAIN, 20));

		final RoundTextLabel resetButton = (RoundTextLabel) this.contentSystem
				.createContentItem(RoundTextLabel.class);
		resetButton.setAutoFitSize(false);
		resetButton.setRadius(40f);
		resetButton.setBackgroundColour(Color.black);
		resetButton.setBorderColour(Color.green);
		resetButton.setTextColour(Color.green);
		resetButton.setLines("RESET", 40);
		resetButton.setFont(new Font("Arial", Font.PLAIN, 20));

		final RoundTextLabel backToMainMenu = (RoundTextLabel) this.contentSystem
				.createContentItem(RoundTextLabel.class);
		backToMainMenu.setAutoFitSize(false);
		backToMainMenu.setRadius(40f);
		backToMainMenu.setBackgroundColour(Color.black);
		backToMainMenu.setBorderColour(Color.green);
		backToMainMenu.setTextColour(Color.green);
		backToMainMenu.setLines("BACK", 40);
		backToMainMenu.setFont(new Font("Arial", Font.PLAIN, 20));
		backToMainMenu.addItemListener(new ItemEventAdapter() {
			public void cursorClicked(ContentItem b, long id, float x, float y,
					float pressure) {
				try {
					SynergyNetDesktop.getInstance().showMainMenu();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		});
		
		this.loadContent("data/diamondranking/ict/ict.xml", "ICT");
		this.loadContent("data/diamondranking/imagetest/imagetest.xml", "IMAGE");
		currentSubApp = "ICT";
		initLocation = getInitLocations();
		hideAllItems();

		loadICTContent.addItemListener(new ItemEventAdapter() {
			public void cursorClicked(ContentItem b, long id, float x, float y,
					float pressure) {
				currentSubApp = "ICT";
				loadImageVersionContent.setTextColour(Color.green);
				hideAllItems();
				Map<ContentItem, Location> contentItemToLocation = items
						.get(currentSubApp);
				for (ContentItem item : contentItemToLocation.keySet()) {

					item.setLocalLocation(contentItemToLocation.get(item)
							.getX(), contentItemToLocation.get(item).getY());
				}
				
				menu.setVisible(false);
			}
		});
		
		loadImageVersionContent.addItemListener(new ItemEventAdapter() {
			public void cursorClicked(ContentItem b, long id, float x, float y,
					float pressure) {
				currentSubApp = "IMAGE";
				loadImageVersionContent.setTextColour(Color.green);
				hideAllItems();
				Map<ContentItem, Location> contentItemToLocation = items
						.get(currentSubApp);
				for (ContentItem item : contentItemToLocation.keySet()) {
					item.setLocalLocation(contentItemToLocation.get(item)
							.getX(), contentItemToLocation.get(item).getY());
				}
				
				menu.setVisible(false);
			}
		});
		
		resetButton.addItemListener(new ItemEventAdapter() {
			@Override
			public void cursorClicked(ContentItem b, long id, float x, float y,
					float pressure) {

				for (ContentItem item : items.get(currentSubApp).keySet()) {
					if ((item instanceof MultiLineTextLabel)
							&& ((OrthoContentItem) item)
									.isRotateTranslateScaleEnabled()) {
						item.setLocalLocation(initLocation.getX(),
								initLocation.getY());
						((OrthoContentItem) item).rotateRandom();
					}
				}

				menu.setVisible(false);
			}
		});

		menu.addSubItem(loadICTContent);
		menu.addSubItem(loadImageVersionContent);
		menu.addSubItem(resetButton);
		menu.addSubItem(backToMainMenu);
		menu.run();
		// menu.makeFlickable(5f);

		currentSubApp = "IMAGE";
		hideAllItems();
		Map<ContentItem, Location> contentItemToLocation = items
				.get(currentSubApp);
		for (ContentItem item : contentItemToLocation.keySet()) {
			item.setLocalLocation(contentItemToLocation.get(item).getX(),
					contentItemToLocation.get(item).getY());
		}
		
		menu.setVisible(false);

	}
	
	/**
	 * Adds the fix location mt element.
	 *
	 * @param fixLocations
	 *            the fix locations
	 * @param tolerance
	 *            the tolerance
	 * @param fileName
	 *            the file name
	 */
	private void addFixLocationMTElement(List<FixLocationStatus> fixLocations,
			float tolerance, String fileName) {
		for (ContentItem item : items.get(fileName).keySet()) {
			if (item instanceof OrthoContentItem) {
				((OrthoContentItem) item).setTolerance(tolerance);
				((OrthoContentItem) item).setFixLocations(fixLocations);
			}
		}
	}

	/**
	 * Adds the lock feature for content.
	 */
	@SuppressWarnings("unused")
	private void addLockFeatureForContent() {
		clicksPerItemMap.clear();
		for (ContentItem contentItem : contentSystem.getAllContentItems()
				.values()) {
			((OrthoContentItem) contentItem)
					.addItemListener(new ItemListener() {
						
						@Override
						public void cursorChanged(ContentItem item, long id,
								float x, float y, float pressure) {
						}
						
						@Override
						public void cursorClicked(ContentItem item, long id,
								float x, float y, float pressure) {
						}
						
						@Override
						public void cursorDoubleClicked(ContentItem item,
								long id, float x, float y, float pressure) {
							
						}
						
						@Override
						public void cursorLongHeld(ContentItem item, long id,
								float x, float y, float pressure) {
							
						}
						
						@Override
						public void cursorPressed(ContentItem item, long id,
								float x, float y, float pressure) {
							if (clicksPerItemMap.containsKey(item)) {
								ArrayList<CursorPoint> cursorPoints = clicksPerItemMap
										.get(item);
								cursorPoints.add(new CursorPoint(id,
										new Point2D.Float(x, y)));
							} else {
								ArrayList<CursorPoint> cursorPoints = new ArrayList<CursorPoint>();
								cursorPoints.add(new CursorPoint(id,
										new Point2D.Float(x, y)));
								clicksPerItemMap.put(item, cursorPoints);
							}
							
							if ((clicksPerItemMap.get(item).size() == 4)
									&& clicksOnCorners(item,
											clicksPerItemMap.get(item))) {
								if (item instanceof Frame) {
									Frame mlt = (Frame) item;
									if (mlt.isRotateTranslateScaleEnabled()) {
										mlt.setRotateTranslateScalable(false);
										mlt.drawImage(lockURL,
												mlt.getWidth() - 20,
												mlt.getHeight() - 20, 20, 20);
									} else {
										mlt.setRotateTranslateScalable(true);
										mlt.removeImage(lockURL);
									}
									clicksPerItemMap.get(item).clear();
								}
							}
						}
						
						@Override
						public void cursorReleased(ContentItem item, long id,
								float x, float y, float pressure) {
							if (clicksPerItemMap.containsKey(item)) {
								ArrayList<CursorPoint> cursorPoints = clicksPerItemMap
										.get(item);
								Iterator<CursorPoint> iter = cursorPoints
										.iterator();
								while (iter.hasNext()) {
									CursorPoint cid = iter.next();
									if (cid.getCursorId() == id) {
										iter.remove();
									}
								}
							}
						}
						
						@Override
						public void cursorRightClicked(ContentItem item,
								long id, float x, float y, float pressure) {
							
						}
					});
		}
	}

	/**
	 * Clicks on corners.
	 *
	 * @param item
	 *            the item
	 * @param clickPoints
	 *            the click points
	 * @return true, if successful
	 */
	public boolean clicksOnCorners(ContentItem item,
			ArrayList<CursorPoint> clickPoints) {
		/*
		 * if(item instanceof Frame){ Frame mlt = (Frame) item; float threshold
		 * = 50; boolean topRight = false, topLeft = false, bottomRight = false,
		 * bottomLeft = false; Point2D.Float topRightCorner = new
		 * Point2D.Float(mlt.getWidth(), 0); Point2D.Float topLeftCorner = new
		 * Point2D.Float(0, 0); Point2D.Float bottomRightCorner = new
		 * Point2D.Float(mlt.getWidth(), mlt.getHeight()); Point2D.Float
		 * bottomLeftCorner = new Point2D.Float(0, mlt.getHeight());
		 * for(CursorPoint cursorPoint: clickPoints){
		 * if(topRightCorner.distance(cursorPoint.getPoint()) <= threshold)
		 * topRight = true; if(topLeftCorner.distance(cursorPoint.getPoint()) <=
		 * threshold) topLeft = true;
		 * if(bottomRightCorner.distance(cursorPoint.getPoint()) <= threshold)
		 * bottomRight = true;
		 * if(bottomLeftCorner.distance(cursorPoint.getPoint()) <= threshold)
		 * bottomLeft = true; } return topRight && topLeft && bottomRight &&
		 * bottomLeft; }
		 */
		return false;
	}
	
	/**
	 * Gets the fix locations.
	 *
	 * @param fileName
	 *            the file name
	 * @return the fix locations
	 */
	private List<FixLocationStatus> getFixLocations(String fileName) {
		List<FixLocationStatus> fixLocations = new ArrayList<FixLocationStatus>();
		for (ContentItem item : items.get(fileName).keySet()) {
			if (!(item instanceof TextLabel)) {
				Point location = new Point(
						(int) item.getLocalLocation().getX(), (int) item
								.getLocalLocation().getY());

				fixLocations.add(new FixLocationStatus(location));
			}
		}
		return fixLocations;
	}

	/**
	 * Gets the inits the locations.
	 *
	 * @return the inits the locations
	 */
	private Location getInitLocations() {

		for (ContentItem item : items.get(currentSubApp).keySet()) {
			if ((item instanceof MultiLineTextLabel)
					&& ((OrthoContentItem) item)
							.isRotateTranslateScaleEnabled()) {
				return new Location(item.getLocalLocation().getX(), item
						.getLocalLocation().getY(), item.getLocalLocation()
						.getZ());
			}
		}

		return null;
	}

	/**
	 * Hide all items.
	 */
	private void hideAllItems() {

		for (String key : items.keySet()) {
			Map<ContentItem, Location> contentItemToLocation = items.get(key);
			for (ContentItem item : contentItemToLocation.keySet()) {
				if ((item.getLocalLocation().getX() != -1000)
						&& (item.getLocalLocation().getY() != -1000)) {
					contentItemToLocation.get(item).setX(
							item.getLocalLocation().getX());
					contentItemToLocation.get(item).setY(
							item.getLocalLocation().getY());
					contentItemToLocation.get(item).setZ(
							item.getLocalLocation().getZ());
				}
				
				item.setLocalLocation(-1000, -1000, 0);
			}
		}
	}

	/**
	 * Load content.
	 *
	 * @param filePath
	 *            the file path
	 * @param name
	 *            the name
	 */
	private void loadContent(String filePath, String name) {

		Set<ContentItem> contentItems = contentSystem
				.loadContentItems(filePath);
		Map<ContentItem, Location> contentItemToLocation = new HashMap<ContentItem, Location>();
		for (ContentItem item : contentItems) {
			contentItemToLocation.put(item, new Location(item
					.getLocalLocation().getX(), item.getLocalLocation().getY(),
					item.getLocalLocation().getZ()));
		}

		items.put(name, contentItemToLocation);
		
		List<FixLocationStatus> fixLocations = getFixLocations(name);
		addFixLocationMTElement(fixLocations, 100, name);
		
		// addLockFeatureForContent();

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#onDeactivate
	 * ()
	 */
	@Override
	public void onDeactivate() {
		super.onDeactivate();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp
	 * #stateUpdate(float)
	 */
	@Override
	public void stateUpdate(float tpf) {
		if (contentSystem != null) {
			contentSystem.update(tpf);
		}
	}
}
