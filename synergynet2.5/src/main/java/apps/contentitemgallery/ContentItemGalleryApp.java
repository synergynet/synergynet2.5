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

package apps.contentitemgallery;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.net.URISyntaxException;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ControlBar;
import synergynetframework.appsystem.contentsystem.items.DropDownList;
import synergynetframework.appsystem.contentsystem.items.Frame;
import synergynetframework.appsystem.contentsystem.items.Keyboard;
import synergynetframework.appsystem.contentsystem.items.ListContainer;
import synergynetframework.appsystem.contentsystem.items.MediaPlayer;
import synergynetframework.appsystem.contentsystem.items.MultiLineTextLabel;
import synergynetframework.appsystem.contentsystem.items.PDFViewer;
import synergynetframework.appsystem.contentsystem.items.PPTViewer;
import synergynetframework.appsystem.contentsystem.items.QuadContentItem;
import synergynetframework.appsystem.contentsystem.items.RoundFrame;
import synergynetframework.appsystem.contentsystem.items.RoundImageLabel;
import synergynetframework.appsystem.contentsystem.items.RoundListContainer;
import synergynetframework.appsystem.contentsystem.items.RoundTextLabel;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.innernotecontroller.InnerNoteController;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.appsystem.table.appregistry.menucontrol.HoldTopRightConfirmVisualExit;
import synergynetframework.jme.sysutils.CameraUtility;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import common.CommonResources;

/**
 * The Class ContentItemGalleryApp.
 */
public class ContentItemGalleryApp extends DefaultSynergyNetApp {

	/** The content system. */
	private ContentSystem contentSystem;

	/** The inner note controller. */
	InnerNoteController innerNoteController;
	
	/**
	 * Instantiates a new content item gallery app.
	 *
	 * @param info
	 *            the info
	 */
	public ContentItemGalleryApp(ApplicationInfo info) {
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
		setMenuController(new HoldTopRightConfirmVisualExit(this));

		// InnerNoteController is used to flip the content item and allow user
		// to add note on back side of the item
		innerNoteController = new InnerNoteController();

		addSimpleButton();
		addListContainer();
		addMediaPlayer();
		addPPTViewer();
		try {
			addPDFViewer();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		addRoundListContainer();
		addRoundImageLabel();
		addWindow();
		addFrame();
		addRoundFrame();
		addMultiLineTextLabel();
		addRoundTextLabel();
		addDropDownList();
		addControlBar();
		Keyboard keyBoard = (Keyboard) contentSystem
				.createContentItem(Keyboard.class);
		keyBoard.setLocalLocation(200, 200);
	}

	/**
	 * Adds the control bar.
	 */
	private void addControlBar() {
		ControlBar controlBar = (ControlBar) contentSystem
				.createContentItem(ControlBar.class);
		controlBar.setLocalLocation(507, 700);
		controlBar.setControlBarLength(400);
	}

	/**
	 * Adds the drop down list.
	 */
	private void addDropDownList() {
		DropDownList dropDownList = (DropDownList) this.contentSystem
				.createContentItem(DropDownList.class);
		dropDownList.placeRandom();
		dropDownList.addListItem("Option 1", "Option 1");
		dropDownList.addListItem("Option 2", "Option 2");
	}

	/**
	 * Adds the frame.
	 */
	private void addFrame() {
		Frame frame = (Frame) contentSystem.createContentItem(Frame.class);
		frame.placeRandom();
		frame.setBackgroundColour(Color.red);
		frame.setBorderColour(Color.white);
		frame.setBorderSize(5);
		frame.setHeight(40);
		frame.setWidth(60);
	}

	/**
	 * Adds the list container.
	 */
	private void addListContainer() {
		ListContainer menu = (ListContainer) contentSystem
				.createContentItem(ListContainer.class);
		menu.setBackgroundColour(Color.BLUE);
		menu.setWidth(200);
		menu.setItemHeight(30);
		menu.placeRandom();

		final SimpleButton button1 = (SimpleButton) contentSystem
				.createContentItem(SimpleButton.class);
		button1.setAutoFitSize(false);
		button1.setText("Start Game");
		button1.setBackgroundColour(Color.lightGray);
		button1.addButtonListener(new SimpleButtonAdapter() {
			public void buttonClicked(SimpleButton b, long id, float x,
					float y, float pressure) {
				if (button1.getText().equals("Start Game")) {
					button1.setText("Stop Game");
				} else {
					button1.setText("Start Game");
				}
			}
		});
		
		final SimpleButton button2 = (SimpleButton) contentSystem
				.createContentItem(SimpleButton.class);
		button2.setAutoFitSize(false);
		button2.setText("Start App");
		button2.setBackgroundColour(Color.lightGray);
		button2.addButtonListener(new SimpleButtonAdapter() {
			public void buttonClicked(SimpleButton b, long id, float x,
					float y, float pressure) {
				if (button2.getText().equals("Start App")) {
					button2.setText("Stop App");
				} else {
					button2.setText("Start App");
				}
			}
		});
		
		menu.addSubItem(button1);
		menu.addSubItem(button2);

	}

	/**
	 * Adds the media player.
	 */
	private void addMediaPlayer() {
		MediaPlayer video = (MediaPlayer) contentSystem
				.createContentItem(MediaPlayer.class);
		video.setMediaURL(CommonResources.class.getResource("smallvid.mp4"));
		video.setPixelsPerUnit(1);
		video.placeRandom();
		video.setScale(0.5f);

		innerNoteController.addNoteController(video,
				(QuadContentItem) video.getPlayerFrame());
	}

	/**
	 * Adds the multi line text label.
	 */
	private void addMultiLineTextLabel() {
		MultiLineTextLabel mlt = (MultiLineTextLabel) this.contentSystem
				.createContentItem(MultiLineTextLabel.class);
		mlt.setAutoFitSize(true);
		mlt.placeRandom();
		mlt.setLines(
				"Platform independent multi-touch software. This software relies on an externally provided vision system (touchlib works well). Written predominantly for a jMonkeyEngine environment, but also supporting other Java-based frameworks. ",
				30);
		mlt.setFont(new Font("Arial", Font.PLAIN, 15));
	}

	/**
	 * Adds the pdf viewer.
	 *
	 * @throws URISyntaxException
	 *             the URI syntax exception
	 */
	private void addPDFViewer() throws URISyntaxException {
		PDFViewer pdf = (PDFViewer) this.contentSystem
				.createContentItem(PDFViewer.class);
		pdf.setPdfFile(new File(CommonResources.class.getResource("test.pdf")
				.toURI()));
		pdf.setWidth(350);
		pdf.placeRandom();
		pdf.setScale(0.5f);
		pdf.makeFlickable(2f);
	}

	/**
	 * Adds the ppt viewer.
	 */
	private void addPPTViewer() {
		PPTViewer ppt = (PPTViewer) contentSystem
				.createContentItem(PPTViewer.class);
		ppt.setPPTFile(CommonResources.class
				.getResource("synergynet-july09.ppt"));
		ppt.placeRandom();
		ppt.setScale(0.5f);
	}

	/**
	 * Adds the round frame.
	 */
	private void addRoundFrame() {
		RoundFrame roundFrame = (RoundFrame) this.contentSystem
				.createContentItem(RoundFrame.class);
		roundFrame.placeRandom();
		roundFrame.setBackgroundColour(Color.yellow);
		roundFrame.setBorderColour(Color.white);
		roundFrame.setRadius(40);
	}

	/**
	 * Adds the round image label.
	 */
	private void addRoundImageLabel() {

		RoundImageLabel roundImageLabel1 = (RoundImageLabel) this.contentSystem
				.createContentItem(RoundImageLabel.class);
		roundImageLabel1.setImageInfo(ContentItemGalleryApp.class
				.getResource("telmug.jpg"));
		roundImageLabel1.setRadius(40);

		RoundImageLabel roundImageLabel2 = (RoundImageLabel) this.contentSystem
				.createContentItem(RoundImageLabel.class);
		roundImageLabel2.setImageInfo(ContentItemGalleryApp.class
				.getResource("puzzlepieces.jpg"));
		roundImageLabel2.setRadius(40);

		RoundImageLabel roundImageLabel3 = (RoundImageLabel) this.contentSystem
				.createContentItem(RoundImageLabel.class);
		roundImageLabel3.setImageInfo(ContentItemGalleryApp.class
				.getResource("butterfly.jpg"));
		roundImageLabel3.setRadius(40);

		RoundListContainer roundListContainer = (RoundListContainer) this.contentSystem
				.createContentItem(RoundListContainer.class);
		roundListContainer.placeRandom();
		roundListContainer.addSubItem(roundImageLabel1);
		roundListContainer.addSubItem(roundImageLabel2);
		roundListContainer.addSubItem(roundImageLabel3);
		roundListContainer.run();

		roundListContainer.makeFlickable(3f);
	}

	/**
	 * Adds the round list container.
	 */
	private void addRoundListContainer() {

		final RoundListContainer menu = (RoundListContainer) this.contentSystem
				.createContentItem(RoundListContainer.class);
		menu.placeRandom();
		
		final RoundTextLabel loadICTContent = (RoundTextLabel) this.contentSystem
				.createContentItem(RoundTextLabel.class);
		loadICTContent.setAutoFitSize(false);
		loadICTContent.setRadius(40f);
		loadICTContent.setBackgroundColour(Color.black);
		loadICTContent.setBorderColour(Color.gray);
		loadICTContent.setTextColour(new Color(0.3f, 0.3f, 1f));
		loadICTContent.setLines("ICT", 40);
		loadICTContent.setFont(new Font("Arial", Font.PLAIN, 20));

		final RoundTextLabel loadImageVersionContent = (RoundTextLabel) this.contentSystem
				.createContentItem(RoundTextLabel.class);
		loadImageVersionContent.setAutoFitSize(false);
		loadImageVersionContent.setRadius(40f);
		loadImageVersionContent.setBackgroundColour(Color.black);
		loadImageVersionContent.setBorderColour(Color.gray);
		loadImageVersionContent.setTextColour(Color.gray);
		loadImageVersionContent.setLines("Image", 40);
		loadImageVersionContent.setFont(new Font("Arial", Font.PLAIN, 20));

		final RoundTextLabel resetButton = (RoundTextLabel) this.contentSystem
				.createContentItem(RoundTextLabel.class);
		resetButton.setAutoFitSize(false);
		resetButton.setRadius(40f);
		resetButton.setBackgroundColour(Color.black);
		resetButton.setBorderColour(Color.gray);
		resetButton.setTextColour(Color.gray);
		resetButton.setLines("Reset", 40);
		resetButton.setFont(new Font("Arial", Font.PLAIN, 20));

		menu.addSubItem(loadICTContent);
		menu.addSubItem(loadImageVersionContent);
		menu.addSubItem(resetButton);
		menu.run();
		
	}

	/**
	 * Adds the round text label.
	 */
	private void addRoundTextLabel() {
		RoundTextLabel roundTextLabel = (RoundTextLabel) this.contentSystem
				.createContentItem(RoundTextLabel.class);
		roundTextLabel.placeRandom();
		roundTextLabel.setBackgroundColour(Color.black);
		roundTextLabel
				.setLines(
						"Platform independent multi-touch software. This software relies on an externally provided vision system (touchlib works well). Written predominantly for a jMonkeyEngine environment, but also supporting other Java-based frameworks.",
						30);
		roundTextLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		
	}
	
	/**
	 * Adds the simple button.
	 */
	private void addSimpleButton() {
		final SimpleButton button1 = (SimpleButton) contentSystem
				.createContentItem(SimpleButton.class);
		button1.setText("I'm a Button");
		button1.setBackgroundColour(Color.lightGray);
		button1.placeRandom();
		button1.addButtonListener(new SimpleButtonAdapter() {
			public void buttonClicked(SimpleButton b, long id, float x,
					float y, float pressure) {
				if (button1.getText().equals("I'm a Button")) {
					button1.setText("Click me!");
				} else {
					button1.setText("I'm a Button");
				}
			}
		});
	}

	/**
	 * Adds the window.
	 */
	private void addWindow() {
		ListContainer window = (ListContainer) contentSystem
				.createContentItem(ListContainer.class);
		window.setBackgroundColour(Color.BLUE);
		window.setBorderColour(Color.white);
		window.setHeight(40);
		window.setWidth(60);
		window.placeRandom();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp
	 * #getCamera()
	 */
	protected Camera getCamera() {
		if (cam == null) {
			cam = CameraUtility.getCamera();
			cam.setLocation(new Vector3f(0f, 10f, 50f));
			cam.lookAt(new Vector3f(), new Vector3f(0, 0, -1));
			cam.update();
		}
		return cam;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp
	 * #stateUpdate(float)
	 */
	@Override
	protected void stateUpdate(float tpf) {
		super.stateUpdate(tpf);
		if (contentSystem != null) {
			contentSystem.update(tpf);
		}

		// if(rtl2.hasCollision(list))
		// System.out.println("Collision");
	}
	
}
