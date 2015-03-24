/*
 * Copyright (c) 2008 University of Durham, England
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'SynergyNet' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package apps.tablepositionsetup;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.util.prefs.Preferences;



import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.system.DisplaySystem;

import core.ConfigurationSystem;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.ImageTextLabel;
import synergynetframework.appsystem.contentsystem.items.LineItem;
import synergynetframework.appsystem.contentsystem.items.RoundImageLabel;
import synergynetframework.appsystem.contentsystem.items.TextLabel;
import synergynetframework.appsystem.contentsystem.items.listener.ItemListener;
import synergynetframework.appsystem.contentsystem.items.utils.Location;
import synergynetframework.appsystem.table.SynergyNetAppUtils;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.appsystem.table.appregistry.menucontrol.HoldTopRightConfirmVisualExit;
import synergynetframework.config.position.PositionConfigPrefsItem;


/**
 * The Class TablePosApp.
 */
public class TablePosApp extends DefaultSynergyNetApp {

	/** The table width. */
	private float referenceDistance, tableX, tableY, tableOrientation, tableWidth = 0f;
	
	/** The prefs. */
	private final Preferences prefs = ConfigurationSystem.getPreferences(PositionConfigPrefsItem.class);;
	
	/** The stage. */
	private int stage = 0;
	
	/** The time to quit. */
	private boolean quit, timeToQuit = false;
	
	/** The mode. */
	private int mode = 0;

	/** The Constant MAX_MARKER_DISTANCE. */
	public final static int MAX_MARKER_DISTANCE = 10;
	
	/** The Constant MAX_TABLE_WIDTH. */
	public final static int MAX_TABLE_WIDTH = 2;

	/** The circle. */
	private RoundImageLabel circle = null;
	
	/** The quit button. */
	private TextLabel confirmButton, instructions, manualButton, aimOneButton, aimTwoButton, aimThreeButton, saveButton, saveText, setupButton, quitButton = null;
	
	/** The orientation box. */
	private ImageTextLabel orientationBox =null;
	
	/** The line three. */
	private LineItem lineOne, lineTwo, lineThree = null;
	
	/** The angle slider. */
	private Slider angleSlider = null;
	
	/** The reference distance dial. */
	private Dial widthDial, referenceDistanceDial = null;
	
	/** The stepper y. */
	private Stepper stepperX, stepperY = null;
	
	/** The count. */
	private int count = 0;
	
	/** The north mode. */
	private boolean northMode = false;

	/**
	 * Instantiates a new table pos app.
	 *
	 * @param info the info
	 */
	public TablePosApp(ApplicationInfo info) {
		super(info);
	}

	/** The content system. */
	private ContentSystem contentSystem;

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#addContent()
	 */
	@Override
	public void addContent() {
		SynergyNetAppUtils.addTableOverlay(this);
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(this);
		setMenuController(new HoldTopRightConfirmVisualExit(this));
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#onActivate()
	 */
	@Override
	public void onActivate() {
				
		stage = 0;
		quit = false;
		timeToQuit = false;
		contentSystem.removeAllContentItems();

		instructions = (TextLabel)this.contentSystem.createContentItem(TextLabel.class);
		instructions.setBackgroundColour(Color.black);
		instructions.setBorderSize(0);
		instructions.setTextColour(Color.white);
		instructions.setText("Choose action:");
		instructions.setFont(new Font("Arial", Font.PLAIN,20));
		instructions.setLocation(DisplaySystem.getDisplaySystem().getRenderer().getWidth()/2, (DisplaySystem.getDisplaySystem().getRenderer().getHeight()/8)*7);
		instructions.setRotateTranslateScalable(false);
		
		setupButton = (TextLabel)this.contentSystem.createContentItem(TextLabel.class);
		setupButton.setBackgroundColour(Color.black);
		setupButton.setBorderColour(Color.white);
		setupButton.setTextColour(Color.white);
		setupButton.setText(" Set table information ");
		setupButton.setFont(new Font("Arial", Font.ITALIC,20));
		setupButton.setRotateTranslateScalable(false);
		setupButton.addItemListener(new ItemListener(){

			@Override
			public void cursorClicked(ContentItem item, long id, float x, float y, float pressure) {
					setupMenu();			
			}	
			
			public void cursorChanged(ContentItem item, long id, float x, float y, float pressure) {}
			public void cursorDoubleClicked(ContentItem item, long id, float x,	float y, float pressure) {}
			public void cursorLongHeld(ContentItem item, long id, float x, float y, float pressure) {}
			public void cursorPressed(ContentItem item, long id, float x, float y, float pressure) {}
			public void cursorReleased(ContentItem item, long id, float x, float y, float pressure) {}
			public void cursorRightClicked(ContentItem item, long id, float x, float y, float pressure) {}
		});

		manualButton = (TextLabel)this.contentSystem.createContentItem(TextLabel.class);
		manualButton.setBackgroundColour(Color.black);
		manualButton.setBorderColour(Color.white);
		manualButton.setTextColour(Color.white);
		manualButton.setText("Enter table position values directly");
		manualButton.setFont(new Font("Arial", Font.PLAIN,20));
		manualButton.setRotateTranslateScalable(false);
		manualButton.addItemListener(new ItemListener(){

			@Override
			public void cursorClicked(ContentItem item, long id, float x, float y, float pressure) {
				manualMenu();
			}
			public void cursorChanged(ContentItem item, long id, float x, float y, float pressure) {}
			public void cursorDoubleClicked(ContentItem item, long id, float x,	float y, float pressure) {}
			public void cursorLongHeld(ContentItem item, long id, float x, float y, float pressure) {}
			public void cursorPressed(ContentItem item, long id, float x, float y, float pressure) {}
			public void cursorReleased(ContentItem item, long id, float x, float y, float pressure) {}
			public void cursorRightClicked(ContentItem item, long id, float x, float y, float pressure) {}
		});

		aimOneButton = (TextLabel)this.contentSystem.createContentItem(TextLabel.class);
		aimOneButton.setBackgroundColour(Color.black);
		aimOneButton.setBorderColour(Color.white);
		aimOneButton.setTextColour(Color.white);
		aimOneButton.setText(" Aim at 'north' and two reference points to set table location ");
		aimOneButton.setFont(new Font("Arial", Font.PLAIN,20));
		aimOneButton.setLocation(DisplaySystem.getDisplaySystem().getRenderer().getWidth()/2, (DisplaySystem.getDisplaySystem().getRenderer().getHeight()/8) * 3);
		aimOneButton.setRotateTranslateScalable(false);
		aimOneButton.addItemListener(new ItemListener(){

			@Override
			public void cursorClicked(ContentItem item, long id, float x, float y, float pressure) {
				northMode = false;
				aimMenu();
			}
			
			public void cursorChanged(ContentItem item, long id, float x, float y, float pressure) {}
			public void cursorDoubleClicked(ContentItem item, long id, float x,	float y, float pressure) {}
			public void cursorLongHeld(ContentItem item, long id, float x, float y, float pressure) {}
			public void cursorPressed(ContentItem item, long id, float x, float y, float pressure) {}
			public void cursorReleased(ContentItem item, long id, float x, float y, float pressure) {}
			public void cursorRightClicked(ContentItem item, long id, float x, float y, float pressure) {}
		});
		
		aimTwoButton = (TextLabel)this.contentSystem.createContentItem(TextLabel.class);
		aimTwoButton.setBackgroundColour(Color.black);
		aimTwoButton.setBorderColour(Color.white);
		aimTwoButton.setTextColour(Color.white);
		aimTwoButton.setText(" Aim at reference points and rotate boxes to set table location ");
		aimTwoButton.setFont(new Font("Arial", Font.PLAIN,20));
		aimTwoButton.setLocation(DisplaySystem.getDisplaySystem().getRenderer().getWidth()/2, (DisplaySystem.getDisplaySystem().getRenderer().getHeight()/8) * 2.5f);
		aimTwoButton.setRotateTranslateScalable(false);
		aimTwoButton.addItemListener(new ItemListener(){

			@Override
			public void cursorClicked(ContentItem item, long id, float x, float y, float pressure) {
				northMode = true;
				aimMenu();
			}
			
			public void cursorChanged(ContentItem item, long id, float x, float y, float pressure) {}
			public void cursorDoubleClicked(ContentItem item, long id, float x,	float y, float pressure) {}
			public void cursorLongHeld(ContentItem item, long id, float x, float y, float pressure) {}
			public void cursorPressed(ContentItem item, long id, float x, float y, float pressure) {}
			public void cursorReleased(ContentItem item, long id, float x, float y, float pressure) {}
			public void cursorRightClicked(ContentItem item, long id, float x, float y, float pressure) {}
		});
		
		aimThreeButton = (TextLabel)this.contentSystem.createContentItem(TextLabel.class);
		aimThreeButton.setBackgroundColour(Color.black);
		aimThreeButton.setBorderColour(Color.white);
		aimThreeButton.setTextColour(Color.white);
		aimThreeButton.setText(" Aim at three reference points to set table location ");
		aimThreeButton.setFont(new Font("Arial", Font.PLAIN,20));
		aimThreeButton.setLocation(DisplaySystem.getDisplaySystem().getRenderer().getWidth()/2, (DisplaySystem.getDisplaySystem().getRenderer().getHeight()/8) * 2);
		aimThreeButton.setRotateTranslateScalable(false);
		aimThreeButton.addItemListener(new ItemListener(){

			@Override
			public void cursorClicked(ContentItem item, long id, float x, float y, float pressure) {
				aimThreeMenu();
			}
			
			public void cursorChanged(ContentItem item, long id, float x, float y, float pressure) {}
			public void cursorDoubleClicked(ContentItem item, long id, float x,	float y, float pressure) {}
			public void cursorLongHeld(ContentItem item, long id, float x, float y, float pressure) {}
			public void cursorPressed(ContentItem item, long id, float x, float y, float pressure) {}
			public void cursorReleased(ContentItem item, long id, float x, float y, float pressure) {}
			public void cursorRightClicked(ContentItem item, long id, float x, float y, float pressure) {}
		});
		
		manualButton.setAutoFit(false);
		manualButton.setWidth(aimOneButton.getWidth());
		manualButton.setHeight(aimOneButton.getHeight());
		manualButton.setLocation(DisplaySystem.getDisplaySystem().getRenderer().getWidth()/2,
				aimOneButton.getLocation().y + (aimOneButton.getHeight()*2));
		setupButton.setLocation(DisplaySystem.getDisplaySystem().getRenderer().getWidth()/2,
				manualButton.getLocation().y + (manualButton.getHeight()*3));
		
		quitButton = (TextLabel)this.contentSystem.createContentItem(TextLabel.class);
		quitButton.setBackgroundColour(Color.black);
		quitButton.setBorderColour(Color.white);
		quitButton.setTextColour(Color.white);
		quitButton.setText(" Quit ");
		quitButton.setFont(new Font("Arial", Font.ITALIC,20));
		quitButton.setRotateTranslateScalable(false);
		quitButton.addItemListener(new ItemListener(){

			@Override
			public void cursorReleased(ContentItem item, long id, float x, float y, float pressure) {
				exitApp();
			}

			public void cursorChanged(ContentItem item, long id, float x, float y, float pressure) {}
			public void cursorClicked(ContentItem item, long id, float x, float y, float pressure) {}
			public void cursorDoubleClicked(ContentItem item, long id, float x,	float y, float pressure) {}
			public void cursorLongHeld(ContentItem item, long id, float x, float y, float pressure) {}
			public void cursorPressed(ContentItem item, long id, float x, float y, float pressure) {}
			public void cursorRightClicked(ContentItem item, long id, float x, float y, float pressure) {}
		});
		
		quitButton.setLocation(DisplaySystem.getDisplaySystem().getRenderer().getWidth()/2,
				(DisplaySystem.getDisplaySystem().getRenderer().getHeight()/8) * 1);
		
	}
	
	/**
	 * Setup menu.
	 */
	private void setupMenu(){
		mode = 0;
		
		addConfirmButton();
		
		hideOpenMenu();
		
		confirmButton.setText(" Back to Menu ");
		
		instructions.setText("Adjust the dials to represent current display values");
		
		tableWidth = Float.parseFloat(prefs.get(PositionConfigPrefsItem.TABLE_WIDTH, "0"));
		referenceDistance = Float.parseFloat(prefs.get(PositionConfigPrefsItem.REFERENCE_DISTANCE, "0"));
		
		
		TextLabel widthText = (TextLabel)contentSystem.createContentItem(TextLabel.class);
		widthText.setBackgroundColour(Color.black);
		widthText.setBorderSize(0);
		widthText.setTextColour(Color.white);
		widthText.setText("            Display width = ");
		widthText.setFont(new Font("Arial", Font.PLAIN,20));
		widthText.setLocation(DisplaySystem.getDisplaySystem().getRenderer().getWidth()/4,
				DisplaySystem.getDisplaySystem().getRenderer().getHeight()/3*2);
		widthText.setRotateTranslateScalable(false);
		
		widthDial = new Dial(contentSystem, DisplaySystem.getDisplaySystem().getRenderer().getWidth()/2, DisplaySystem.getDisplaySystem().getRenderer().getHeight()/3*2,
				70, 0.5f, MAX_TABLE_WIDTH, "m");
		widthDial.setValue(tableWidth);
		widthDial.addPrecisionSlider(0.5f);
		widthDial.setLowerBound(0);
		
		TextLabel referenceDistanceTextOne = (TextLabel)contentSystem.createContentItem(TextLabel.class);
		referenceDistanceTextOne.setBackgroundColour(Color.black);
		referenceDistanceTextOne.setBorderSize(0);
		referenceDistanceTextOne.setTextColour(Color.white);
		referenceDistanceTextOne.setText("Distance between      ");
		referenceDistanceTextOne.setFont(new Font("Arial", Font.PLAIN,20));
		referenceDistanceTextOne.setLocation(DisplaySystem.getDisplaySystem().getRenderer().getWidth()/4,
				DisplaySystem.getDisplaySystem().getRenderer().getHeight()/3 + referenceDistanceTextOne.getHeight()/2);
		referenceDistanceTextOne.setRotateTranslateScalable(false);
		
		TextLabel referenceDistanceTextTwo = (TextLabel)contentSystem.createContentItem(TextLabel.class);
		referenceDistanceTextTwo.setBackgroundColour(Color.black);
		referenceDistanceTextTwo.setBorderSize(0);
		referenceDistanceTextTwo.setTextColour(Color.white);
		referenceDistanceTextTwo.setText("reference points = ");
		referenceDistanceTextTwo.setFont(new Font("Arial", Font.PLAIN,20));
		referenceDistanceTextTwo.setLocation(DisplaySystem.getDisplaySystem().getRenderer().getWidth()/4,
				DisplaySystem.getDisplaySystem().getRenderer().getHeight()/3 - referenceDistanceTextTwo.getHeight()/2);
		referenceDistanceTextTwo.setRotateTranslateScalable(false);
		
		referenceDistanceDial = new Dial(contentSystem, DisplaySystem.getDisplaySystem().getRenderer().getWidth()/2, DisplaySystem.getDisplaySystem().getRenderer().getHeight()/3,
				70, 5, MAX_MARKER_DISTANCE, "m");
		referenceDistanceDial.setValue(Float.parseFloat(prefs.get(PositionConfigPrefsItem.REFERENCE_DISTANCE, "0")));
		referenceDistanceDial.addPrecisionSlider(1);
		referenceDistanceDial.setLowerBound(0);
		
		
		saveText = (TextLabel)contentSystem.createContentItem(TextLabel.class);
		saveText.setBackgroundColour(Color.black);
		saveText.setBorderSize(0);
		saveText.setTextColour(Color.green);
		saveText.setText("");
		saveText.setFont(new Font("Arial", Font.ITALIC,20));
		saveText.setRotateTranslateScalable(false);
		
		saveButton = (TextLabel)contentSystem.createContentItem(TextLabel.class);
		saveButton.setBackgroundColour(Color.black);
		saveButton.setBorderColour(Color.white);
		saveButton.setTextColour(Color.white);
		saveButton.setText("Save");
		saveButton.setFont(new Font("Arial", Font.PLAIN,35));
		saveButton.setLocation(DisplaySystem.getDisplaySystem().getRenderer().getWidth()/8*7,
				DisplaySystem.getDisplaySystem().getRenderer().getHeight()/2);
		saveButton.setRotateTranslateScalable(false);
		saveButton.addItemListener(new ItemListener(){

			@Override
			public void cursorClicked(ContentItem item, long id, float x, float y, float pressure) {
				saveDisplayPrefs();
			}

			public void cursorChanged(ContentItem item, long id, float x, float y, float pressure) {}
			public void cursorDoubleClicked(ContentItem item, long id, float x,	float y, float pressure) {}
			public void cursorLongHeld(ContentItem item, long id, float x, float y, float pressure) {}
			public void cursorPressed(ContentItem item, long id, float x, float y, float pressure) {}
			public void cursorReleased(ContentItem item, long id, float x, float y, float pressure) {}
			public void cursorRightClicked(ContentItem item, long id, float x, float y, float pressure) {}
		});

		saveText.setLocation(DisplaySystem.getDisplaySystem().getRenderer().getWidth()/8*7,
				(saveButton.getLocation().y - saveButton.getHeight()/2 - saveText.getHeight()));
					
		timeToQuit = true;

	}
	
	/**
	 * Manual menu.
	 */
	public void manualMenu(){
		mode = 1;
		
		addConfirmButton();
		
		instructions.setText("Adjust the values to represent display position values");
		
		tableWidth = Float.parseFloat(prefs.get(PositionConfigPrefsItem.TABLE_WIDTH, "0"));
		
		tableX = Float.parseFloat(prefs.get(PositionConfigPrefsItem.PREFS_LOCATION_X, "0"));
		tableY = Float.parseFloat(prefs.get(PositionConfigPrefsItem.PREFS_LOCATION_Y, "0"));			
						
		stepperX = new Stepper(contentSystem, DisplaySystem.getDisplaySystem().getRenderer().getWidth()/2,
				DisplaySystem.getDisplaySystem().getRenderer().getHeight()/10*7, toMeterValue(tableX), 1f, "Table position x = ","", "m");
		stepperY = new Stepper(contentSystem, DisplaySystem.getDisplaySystem().getRenderer().getWidth()/2,
				DisplaySystem.getDisplaySystem().getRenderer().getHeight()/10*5, toMeterValue(tableY), 1f, "Table position y = ","", "m");
					
		
		angleSlider = new Slider(contentSystem, DisplaySystem.getDisplaySystem().getRenderer().getWidth()/2, DisplaySystem.getDisplaySystem().getRenderer().getHeight()/10*3,
				DisplaySystem.getDisplaySystem().getRenderer().getWidth()/2, 40, 360, 0, "Table angle = ", " degrees");
		angleSlider.setValue(Float.parseFloat(prefs.get(PositionConfigPrefsItem.PREFS_ANGLE, "0")));

		hideOpenMenu();
	}
	
	/**
	 * Aim menu.
	 */
	public void aimMenu(){
		mode = 2;
		referenceDistance = Float.parseFloat(prefs.get(PositionConfigPrefsItem.REFERENCE_DISTANCE, "0"));
		tableWidth = Float.parseFloat(prefs.get(PositionConfigPrefsItem.TABLE_WIDTH, "0"));
		Location loc = new Location(DisplaySystem.getDisplaySystem().getRenderer().getWidth()/2, DisplaySystem.getDisplaySystem().getRenderer().getHeight()/2, 0);
		
		if (northMode){
			instructions.setText("Rotate the box to align with the room.");
			
			orientationBox = (ImageTextLabel)this.contentSystem.createContentItem(ImageTextLabel.class);
			orientationBox.setImageInfo(TablePosApp.class.getResource("orientationBox.png"));
			orientationBox.centerItem();
			orientationBox.setScale(1.5f);
			orientationBox.setScaleLimit(1.5f, 1.5f);
			orientationBox.setAsTopObject();

		}else{
			instructions.setText("Drag the arrow to point in the 'North' direction.");
			addCircle();
			lineOne = (LineItem)contentSystem.createContentItem(LineItem.class);
			lineOne.setSourceLocation(loc);
			lineOne.setTargetLocation(loc);
			lineOne.setArrowMode(1);
			lineOne.setAsTopObject();
		}	

		addConfirmButton();
		hideOpenMenu();
	}
	
	/**
	 * Aim three menu.
	 */
	public void aimThreeMenu(){
		mode = 3;
		stage = 1;

		referenceDistance = Float.parseFloat(prefs.get(PositionConfigPrefsItem.REFERENCE_DISTANCE, "0"));
		tableWidth = Float.parseFloat(prefs.get(PositionConfigPrefsItem.TABLE_WIDTH, "0"));
		Location loc = new Location(DisplaySystem.getDisplaySystem().getRenderer().getWidth()/2, DisplaySystem.getDisplaySystem().getRenderer().getHeight()/2, 0);
		
		instructions.setText("Drag the arrows to point towards to the three markers.");
		addCircle();
		lineOne = (LineItem)contentSystem.createContentItem(LineItem.class);
		lineOne.setSourceLocation(loc);
		lineOne.setTargetLocation(loc);
		lineOne.setArrowMode(1);
		lineOne.setAsTopObject();
		lineTwo = (LineItem)this.contentSystem.createContentItem(LineItem.class);
		lineTwo.setSourceLocation(loc);
		lineTwo.setTargetLocation(loc);
		lineTwo.setArrowMode(1);
		lineTwo.setAsTopObject();
		lineThree = (LineItem)this.contentSystem.createContentItem(LineItem.class);
		lineThree.setSourceLocation(loc);
		lineThree.setTargetLocation(loc);
		lineThree.setArrowMode(1);
		lineThree.setAsTopObject();

		addConfirmButton();
		hideOpenMenu();
	}
	
	/**
	 * Hide open menu.
	 */
	private void hideOpenMenu() {
		aimOneButton.setVisible(false, true);
		aimTwoButton.setVisible(false, true);
		aimThreeButton.setVisible(false, true);
		manualButton.setVisible(false, true);
		setupButton.setVisible(false,true);
		quitButton.setVisible(false, true);		
	}


	/**
	 * Adds the confirm button.
	 */
	private void addConfirmButton() {

		confirmButton = (TextLabel)this.contentSystem.createContentItem(TextLabel.class);
		confirmButton.setBackgroundColour(Color.black);
		confirmButton.setBorderColour(Color.white);
		confirmButton.setTextColour(Color.white);
		confirmButton.setText("Confirm");
		confirmButton.setFont(new Font("Arial", Font.PLAIN,40));
		confirmButton.setLocation(DisplaySystem.getDisplaySystem().getRenderer().getWidth()/2,
				DisplaySystem.getDisplaySystem().getRenderer().getHeight()/8);
		confirmButton.setRotateTranslateScalable(false);
		confirmButton.addItemListener(new ItemListener(){

			@Override
			public void cursorReleased(ContentItem item, long id, float x, float y, float pressure) {
				if (timeToQuit)	quit = true;
				confirmClicked();
			}

			public void cursorChanged(ContentItem item, long id, float x, float y, float pressure) {}
			public void cursorClicked(ContentItem item, long id, float x, float y, float pressure) {}
			public void cursorDoubleClicked(ContentItem item, long id, float x,	float y, float pressure) {}
			public void cursorLongHeld(ContentItem item, long id, float x, float y, float pressure) {}
			public void cursorPressed(ContentItem item, long id, float x, float y, float pressure) {}
			public void cursorRightClicked(ContentItem item, long id, float x, float y, float pressure) {}
		});

		
	}

	/**
	 * Confirm clicked.
	 */
	private void confirmClicked(){		
		if (mode == 3){
			aimConfirmMenu();
		}else if (mode == 2){
			if (stage == 0){
				aimAtCornersMenu();
			}else if (stage ==  1){
				aimConfirmMenu();
			}
		}else if (mode == 1){
			manualConfirmMenu();
		}
	}

	/**
	 * Aim at corners menu.
	 */
	private void aimAtCornersMenu() {
		Location loc = new Location(DisplaySystem.getDisplaySystem().getRenderer().getWidth()/2, DisplaySystem.getDisplaySystem().getRenderer().getHeight()/2, 0);
		if (!northMode){
			if (lineOne.getSourceLocation().equals(lineOne.getTargetLocation()))return;					
			tableOrientation = angleFromLocalNorth(lineOne.getTargetLocation().x, lineOne.getTargetLocation().y);
		}else{
			orientationBox.setVisible(false, true);					
			tableOrientation = angleFromBox();				
			addCircle();
			lineOne = (LineItem)contentSystem.createContentItem(LineItem.class);
			lineOne.setArrowMode(1);
			lineOne.setAsTopObject();
		}	
		
		instructions.setText("Drag the arrows to point towards to the two markers.");
		
		lineOne.setSourceLocation(loc);
		lineOne.setTargetLocation(loc);
		lineTwo = (LineItem)this.contentSystem.createContentItem(LineItem.class);
		lineTwo.setSourceLocation(loc);
		lineTwo.setTargetLocation(loc);
		lineTwo.setArrowMode(1);
		lineTwo.setAsTopObject();
		stage++;
	}

	/**
	 * Aim confirm menu.
	 */
	private void aimConfirmMenu() {
		if (!lineOne.getSourceLocation().equals(lineOne.getTargetLocation()) && !lineTwo.getSourceLocation().equals(lineTwo.getTargetLocation())){
			if (mode == 3){
				if (lineThree.getSourceLocation().equals(lineThree.getTargetLocation()))return; 
				calculatePosition(lineOne.getTargetLocation().x, lineOne.getTargetLocation().y,	lineTwo.getTargetLocation().x, lineTwo.getTargetLocation().y, 
						lineThree.getTargetLocation().x, lineThree.getTargetLocation().y);
				lineThree.setVisible(false, true);
			}else{
				calculatePosition(lineOne.getTargetLocation().x, lineOne.getTargetLocation().y,	lineTwo.getTargetLocation().x, lineTwo.getTargetLocation().y);
			}			
			tableOrientation *= FastMath.RAD_TO_DEG;
			instructions.setText("Table Co-ordinate = (" + new DecimalFormat("0.##").format((float)tableX) + "m, "
					+ new DecimalFormat("0.##").format((float)tableY) + "m)\t Table Orientation = " + new DecimalFormat("0.##").format((float)tableOrientation) + " degrees");
			circle.setVisible(false, true);
			lineOne.setVisible(false, true);
			lineTwo.setVisible(false, true);
			addSaveButton();
			confirmButton.setText("Back to Menu");
			timeToQuit = true;			
		}		
	}
	
	/**
	 * Manual confirm menu.
	 */
	private void manualConfirmMenu() {
		tableX = stepperX.getValue();
		tableY = stepperY.getValue();
		tableOrientation = angleSlider.getValue();
		instructions.setText("Table Co-ordinate = (" + new DecimalFormat("0.##").format((float)tableX) + "m, "
				+ new DecimalFormat("0.##").format((float)tableY) + "m)\t Table Orientation = " + new DecimalFormat("0.##").format((float)tableOrientation) + " degrees");
		stepperX.destroyDial();
		stepperY.destroyDial();
		angleSlider.destroySlider();
		addSaveButton();
		confirmButton.setText("Back to Menu");
		timeToQuit = true;		
	}
	
	/**
	 * Adds the save button.
	 */
	private void addSaveButton(){
		
		saveText = (TextLabel)this.contentSystem.createContentItem(TextLabel.class);
		saveText.setBackgroundColour(Color.black);
		saveText.setBorderSize(0);
		saveText.setTextColour(Color.green);
		saveText.setText("");
		saveText.setFont(new Font("Arial", Font.ITALIC,20));
		saveText.setLocation(DisplaySystem.getDisplaySystem().getRenderer().getWidth()/2,
				(DisplaySystem.getDisplaySystem().getRenderer().getHeight()/3));
		saveText.setRotateTranslateScalable(false);

		saveButton = (TextLabel)this.contentSystem.createContentItem(TextLabel.class);
		saveButton.setBackgroundColour(Color.black);
		saveButton.setBorderColour(Color.white);
		saveButton.setTextColour(Color.white);
		saveButton.setText("Save");
		saveButton.setFont(new Font("Arial", Font.PLAIN,40));
		saveButton.setLocation(DisplaySystem.getDisplaySystem().getRenderer().getWidth()/2,
				DisplaySystem.getDisplaySystem().getRenderer().getHeight()/2);
		saveButton.setRotateTranslateScalable(false);
		saveButton.addItemListener(new ItemListener(){

			@Override
			public void cursorClicked(ContentItem item, long id, float x, float y, float pressure) {
				savePositionPrefs();
			}

			public void cursorChanged(ContentItem item, long id, float x, float y, float pressure) {}
			public void cursorDoubleClicked(ContentItem item, long id, float x,	float y, float pressure) {}
			public void cursorLongHeld(ContentItem item, long id, float x, float y, float pressure) {}
			public void cursorPressed(ContentItem item, long id, float x, float y, float pressure) {}
			public void cursorReleased(ContentItem item, long id, float x, float y, float pressure) {}
			public void cursorRightClicked(ContentItem item, long id, float x, float y, float pressure) {}
		});

		saveText.setLocation(DisplaySystem.getDisplaySystem().getRenderer().getWidth()/2,
				(saveButton.getLocation().y - saveButton.getHeight()/2 - saveText.getHeight()));
	}

	/**
	 * Adds the circle.
	 */
	private void addCircle() {
		circle = (RoundImageLabel)this.contentSystem.createContentItem(RoundImageLabel.class);
		circle.setAutoFit(false);
		circle.setBackgroundColour(Color.black);
		circle.setRadius(DisplaySystem.getDisplaySystem().getRenderer().getHeight()/4);
		circle.setRotateTranslateScalable(false);
		circle.setBringToTopable(false);
		circle.removeBringToTopListeners();
		circle.setLocation(DisplaySystem.getDisplaySystem().getRenderer().getWidth()/2, DisplaySystem.getDisplaySystem().getRenderer().getHeight()/2);
		circle.addItemListener(new ItemListener(){

			@Override
			public void cursorChanged(ContentItem item, long id, float x, float y, float pressure) {
				if (circle.contains(new Point2D.Float(x * DisplaySystem.getDisplaySystem().getRenderer().getWidth(), y * DisplaySystem.getDisplaySystem().getRenderer().getHeight()))){
					circleModified(x, y);
				}
			}

			@Override
			public void cursorPressed(ContentItem item, long id, float x, float y, float pressure) {
				circleModified(x, y);
			}

			@Override
			public void cursorReleased(ContentItem item, long id, float x, float y, float pressure) {
				if (count == 0){
					count++;
				}else if (count == 1){
					count++;
				}else if (count == 2){
					count++;
				}
			}
			
			public void cursorClicked(ContentItem item, long id, float x, float y, float pressure) {}
			public void cursorDoubleClicked(ContentItem item, long id, float x,	float y, float pressure) {}
			public void cursorLongHeld(ContentItem item, long id, float x, float y, float pressure) {}
			public void cursorRightClicked(ContentItem item, long id, float x, float y, float pressure) {}
		});
		count = 0;

	}

	
	/**
	 * Circle modified.
	 *
	 * @param x the x
	 * @param y the y
	 */
	private void circleModified(float x, float y){
		x = x * DisplaySystem.getDisplaySystem().getRenderer().getWidth();
		y = y * DisplaySystem.getDisplaySystem().getRenderer().getHeight();
		switch (stage){
		case 0:
			if (lineOne != null){
				lineOne.setTargetLocation(new Location(x, y, 0));
			}
			break;
		case 1:
			if (mode != 3){
				if(lineOne != null && lineTwo != null){
			
					if (count == 0){
						lineOne.setTargetLocation(new Location(x, y, 0));
					}else if (count == 1){
						lineTwo.setTargetLocation(new Location(x, y, 0));
					}else{
						Vector3f touchVec = new Vector3f(x, y, 0);
						Vector3f lineOneVec = new Vector3f(lineOne.getTargetLocation().x, lineOne.getTargetLocation().y, 0);
						Vector3f lineTwoVec = new Vector3f(lineTwo.getTargetLocation().x, lineTwo.getTargetLocation().y, 0);
						if (touchVec.distance(lineOneVec) <= touchVec.distance(lineTwoVec)){
							lineOne.setTargetLocation(new Location(x, y, 0));
						}else{
							lineTwo.setTargetLocation(new Location(x, y, 0));
						}
					}
				}
			}else{
				if(lineOne != null && lineTwo != null && lineThree != null){
					
					if (count == 0){
						lineOne.setTargetLocation(new Location(x, y, 0));
					}else if (count == 1){
						lineTwo.setTargetLocation(new Location(x, y, 0));
					}else if (count == 2){
						lineThree.setTargetLocation(new Location(x, y, 0));
					}else{
						Vector3f touchVec = new Vector3f(x, y, 0);
						Vector3f lineOneVec = new Vector3f(lineOne.getTargetLocation().x, lineOne.getTargetLocation().y, 0);
						Vector3f lineTwoVec = new Vector3f(lineTwo.getTargetLocation().x, lineTwo.getTargetLocation().y, 0);
						Vector3f lineThreeVec = new Vector3f(lineThree.getTargetLocation().x, lineThree.getTargetLocation().y, 0);
						if (touchVec.distance(lineOneVec) <= touchVec.distance(lineTwoVec) && touchVec.distance(lineOneVec) <= touchVec.distance(lineThreeVec) ){
							lineOne.setTargetLocation(new Location(x, y, 0));
						}else if (touchVec.distance(lineTwoVec) <= touchVec.distance(lineOneVec) && touchVec.distance(lineTwoVec) <= touchVec.distance(lineThreeVec) ){
							lineTwo.setTargetLocation(new Location(x, y, 0));
						}else{
							lineThree.setTargetLocation(new Location(x, y, 0));
						}
					}
				}
			}
			break;
		}
		if(lineOne != null)lineOne.setAsTopObject();
		if(lineTwo != null)lineTwo.setAsTopObject();
		if(lineTwo != null)lineThree.setAsTopObject();
	}

	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp#stateUpdate(float)
	 */
	@Override
	protected void stateUpdate(float tpf) {
		super.stateUpdate(tpf);
		if(contentSystem != null) contentSystem.update(tpf);
		if (quit){
			onActivate();
		}
		if (stage == 0 && mode == 1){
			if (stepperX != null){
				stepperX.update(tpf);
			}
			if (stepperY != null){
				stepperY.update(tpf);
			}
		}
	}

	
	/**
	 * Calculate position.
	 *
	 * @param x1 the x1
	 * @param y1 the y1
	 * @param x2 the x2
	 * @param y2 the y2
	 */
	private void calculatePosition(float x1, float y1, float x2, float y2) {

		float north = (FastMath.RAD_TO_DEG * tableOrientation) + 360;
		float lineA = angleFromLocalNorth(x1, y1);
		lineA = (FastMath.RAD_TO_DEG * lineA) + 360;
		float lineB = angleFromLocalNorth(x2, y2);
		lineB = (FastMath.RAD_TO_DEG * lineB) + 360;

		float p = 0f;
		float C = 0f;

		if (north - lineA < north - lineB){
			p = north - lineA;
			C = lineA - lineB;
		}else{
			p = north - lineB;
			C = lineB - lineA;
		}

		C *= FastMath.DEG_TO_RAD;
		p *= FastMath.DEG_TO_RAD;

		float q = (FastMath.DEG_TO_RAD * 90) - p;
		float B = (FastMath.DEG_TO_RAD * 90) - q;
		float A = (FastMath.DEG_TO_RAD * 180) - C - B;
		float f = (FastMath.DEG_TO_RAD * 90) - A;
		float e = FastMath.sin(B) * (referenceDistance/FastMath.sin(C));

		tableY = FastMath.sin(f) * (e/FastMath.sin(FastMath.DEG_TO_RAD * 90));
		tableX = FastMath.sin(A) * (e/FastMath.sin(FastMath.DEG_TO_RAD * 90));
				
	}
	
	/**
	 * Calculate position.
	 *
	 * @param x1 the x1
	 * @param y1 the y1
	 * @param x2 the x2
	 * @param y2 the y2
	 * @param x3 the x3
	 * @param y3 the y3
	 */
	private void calculatePosition(float x1, float y1, float x2, float y2, float x3, float y3) {
		
		float angleOne = FastMath.abs((angleBetweenLines(x1, y1, x2, y2)));
		float largest = angleOne;
		float angleTwo = FastMath.abs((angleBetweenLines(x3, y3, x2, y2)));
		if (angleTwo > largest)largest = angleTwo;
		float angleThree = FastMath.abs((angleBetweenLines(x1, y1, x3, y3)));
		if (angleThree > largest)largest = angleThree;
		
		float omega = 0;
		
		float a = 0;
		float b = 0;
		float c = 0;
		
		if (largest == angleOne){
			if (angleBetweenLines(x1, y1, x2, y2) > 0){ 
				a = angleThree;
				b = angleTwo;
				c = angleOne;
				omega = angleFromLocalNorth(x2, y2);
			}else{
				a = angleTwo;
				b = angleThree;
				c = angleOne;
				omega = angleFromLocalNorth(x1, y1);
			}
		}else if (largest == angleTwo){
			if (angleBetweenLines(x2, y2, x3, y3) > 0){ 
				a = angleOne;
				b = angleThree;
				c = angleTwo;
				omega = angleFromLocalNorth(x3, y3);
			}else{
				a = angleThree;
				b = angleOne;
				c = angleTwo;
				omega = angleFromLocalNorth(x2, y2);
			}
		}else{
			if (angleBetweenLines(x1, y1, x3, y3) > 0){ 
				a = angleOne;
				b = angleTwo;
				c = angleThree;
				omega = angleFromLocalNorth(x3, y3);
			}else{
				a = angleTwo;
				b = angleOne;
				c = angleThree;
				omega = angleFromLocalNorth(x1, y1);
			}
		}
		
		System.out.println("a = " + (FastMath.RAD_TO_DEG * a));
		System.out.println("b = " + (FastMath.RAD_TO_DEG * b));
		System.out.println("c = " + (FastMath.RAD_TO_DEG * c));
		
		float e = ((FastMath.DEG_TO_RAD * 90)/c) * (referenceDistance/2);
		
		System.out.println("e = " + e);
		
		float i = FastMath.asin((e - FastMath.sin(b))/(referenceDistance/2));
		float h = FastMath.asin((e - FastMath.sin(a))/(referenceDistance/2));
		
		System.out.println("i = " + (FastMath.RAD_TO_DEG * i));
		System.out.println("h = " + (FastMath.RAD_TO_DEG * h));
		
		float f = FastMath.sin(i) * (referenceDistance/(FastMath.sin(c)));
		float g = FastMath.sin(h) * (referenceDistance/(FastMath.sin(c)));
		
		System.out.println("f = " + f);
		System.out.println("g = " + g);
		
		tableX = f * FastMath.sin(h);
		tableY = g * FastMath.cos(i);
		
		float j = (FastMath.DEG_TO_RAD * 90) - i;
		float k = (FastMath.DEG_TO_RAD * 90) - j;
		
		System.out.println("j = " + (FastMath.RAD_TO_DEG * j));
		System.out.println("k = " + (FastMath.RAD_TO_DEG * k));
		System.out.println("omega = " + (FastMath.RAD_TO_DEG * omega));
		
		tableOrientation = omega - k;
		
	}
	
	/**
	 * Angle between lines.
	 *
	 * @param x1 the x1
	 * @param y1 the y1
	 * @param x2 the x2
	 * @param y2 the y2
	 * @return the float
	 */
	private float angleBetweenLines(float x1, float y1, float x2, float y2){
		x1 -=  DisplaySystem.getDisplaySystem().getRenderer().getWidth()/2;
		y1 -= DisplaySystem.getDisplaySystem().getRenderer().getHeight()/2;
		x2 -=  DisplaySystem.getDisplaySystem().getRenderer().getWidth()/2;
		y2 -= DisplaySystem.getDisplaySystem().getRenderer().getHeight()/2;
        return FastMath.atan2( (x1 * y2) - (y1 * x2), (x1 * x2) +  (y1 * y2));
    }
	
	/**
	 * Angle from box.
	 *
	 * @return the float
	 */
	private float angleFromBox() {
		return orientationBox.getAngle();
	}

	
	/**
	 * Angle from local north.
	 *
	 * @param x the x
	 * @param y the y
	 * @return the float
	 */
	private float angleFromLocalNorth(float x, float y) {
		x -=  DisplaySystem.getDisplaySystem().getRenderer().getWidth()/2;
		y -= DisplaySystem.getDisplaySystem().getRenderer().getHeight()/2;
		float angle = 0f;
		if (x >= 0 && y >= 0){
			angle = FastMath.atan(x/y);
		}else if (x >= 0 && y < 0){
			angle = FastMath.atan((-y)/x) + (90 * FastMath.DEG_TO_RAD);
		}else if (x < 0 && y < 0){
			angle = FastMath.atan((-x)/(-y)) + (180 * FastMath.DEG_TO_RAD);
		}else if (x < 0 && y >= 0){
			angle = FastMath.atan(y/(-x)) + (270 * FastMath.DEG_TO_RAD);
		}
		return angle;
	}

	
	/**
	 * Save display prefs.
	 */
	private void saveDisplayPrefs() {
		saveText.setVisible(true);
		saveText.setText("Values saved");
		new Thread(){public void run(){
			try {sleep(2500);} catch (Exception e) {}
			saveText.setVisible(false);
		}}.start();
		prefs.putFloat(PositionConfigPrefsItem.TABLE_WIDTH, widthDial.getValue());
		prefs.putFloat(PositionConfigPrefsItem.REFERENCE_DISTANCE, referenceDistanceDial.getValue());
		
	}
	
	
	/**
	 * Save position prefs.
	 */
	private void savePositionPrefs(){
		saveText.setVisible(true);
		saveText.setText("Display position values saved");
		new Thread(){public void run(){
			try {sleep(2500);} catch (Exception e) {}
			saveText.setVisible(false);
		}}.start();
		prefs.putInt(PositionConfigPrefsItem.PREFS_LOCATION_X, toPixelValue(tableX));
		prefs.putInt(PositionConfigPrefsItem.PREFS_LOCATION_Y, toPixelValue(tableY));
		prefs.putFloat(PositionConfigPrefsItem.PREFS_ANGLE, tableOrientation);
	}

	
	/**
	 * To pixel value.
	 *
	 * @param a the a
	 * @return the int
	 */
	private int toPixelValue(float a) {
		a /= tableWidth/DisplaySystem.getDisplaySystem().getRenderer().getWidth();
		return (int)a;
	}

	
	/**
	 * To meter value.
	 *
	 * @param p the p
	 * @return the float
	 */
	private float toMeterValue(float p) {
		float m = p * (tableWidth/DisplaySystem.getDisplaySystem().getRenderer().getWidth());
		return m;
	}

}
