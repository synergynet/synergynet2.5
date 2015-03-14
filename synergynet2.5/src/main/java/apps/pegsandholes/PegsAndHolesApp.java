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

package apps.pegsandholes;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Spatial;
import com.jme.system.DisplaySystem;

import common.CommonResources;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.ImageTextLabel;
import synergynetframework.appsystem.contentsystem.items.ObjShape;
import synergynetframework.appsystem.contentsystem.items.TextLabel;
import synergynetframework.appsystem.contentsystem.items.listener.ItemListener;
import synergynetframework.appsystem.table.SynergyNetAppUtils;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.appsystem.table.appregistry.menucontrol.HoldTopRightConfirmVisualExit;
import synergynetframework.jme.sysutils.SpatialUtility;

public class PegsAndHolesApp extends DefaultSynergyNetApp {
	
	private static String[] shapeFiles = {"circle", "equilateralTriangle", "hexagon", "isometricTriangle", "octagon", "oval", "pentagon", "rectangle", "rightAngleTriangle", "semiCircle", "square"};
	private boolean[] solved;
	
	final private static boolean DUPLICATE_SHAPES = false;
	final private static int HOLE_OFFSET = 200;
	final private static int SQUARE_WIDTH = 600;
	final private static int SQUARE_HEIGHT = 600;
	final private static int NUMBER_OF_SHAPES = 9;
	final private static int NUMBER_OF_COLUMNS = 3;
	final private static int DISTANCE_THRESHOLD = 20;
	final private static int VERTEX_THRESHOLD = 5;
	
	private String puzzleName = "Pegs and Holes";
	private List<String> shapes = new ArrayList<String>();
	private List<ObjShape> holes = new ArrayList<ObjShape>();
	private List<ObjShape> pegs = new ArrayList<ObjShape>();
	private int holesRemaining = 1;

	private TextLabel titleText;
	private ImageTextLabel startButton;
	private TextLabel modeText;
	private TextLabel modeSelectedText;
	
	private TextLabel square;
	private int mode = 1;
	private int fontSize = 30;

	public PegsAndHolesApp(ApplicationInfo info) {
		super(info);
	}

	private ContentSystem contentSystem;

	@Override
	public void addContent() {
		SynergyNetAppUtils.addTableOverlay(this);
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(this);
		setMenuController(new HoldTopRightConfirmVisualExit(this));
	}

	@Override
	public void onActivate() {
		contentSystem.removeAllContentItems();
		
		shapes.clear();
		Collections.shuffle(Arrays.asList(shapeFiles));
		
		solved = new boolean[NUMBER_OF_SHAPES];
		
		for (int i = 0; i< NUMBER_OF_SHAPES; i++){
			shapes.add(shapeFiles[i]);									
			solved[i]= false;
			if (DUPLICATE_SHAPES)Collections.shuffle(Arrays.asList(shapeFiles));
		}

		holes.clear();
		pegs.clear();

		titleText = (TextLabel)this.contentSystem.createContentItem(TextLabel.class);
		titleText.setBackgroundColour(Color.black);
		titleText.setBorderColour(Color.black);
		titleText.setTextColour(Color.red);
		titleText.setText(puzzleName);
		titleText.setFont(new Font("Arial", Font.ITALIC,50));
		titleText.setLocalLocation(DisplaySystem.getDisplaySystem().getRenderer().getWidth()/2,
				DisplaySystem.getDisplaySystem().getRenderer().getHeight() - titleText.getHeight());
		titleText.setRotateTranslateScalable(false);

		startButton = (ImageTextLabel)contentSystem.createContentItem(ImageTextLabel.class);
		startButton.setBackgroundColour(Color.black);
		startButton.setBorderColour(Color.black);
		startButton.setAutoFitSize(false);
		startButton.setImageInfo(CommonResources.class.getResource("startpuzzle.png"));
		startButton.setLocalLocation(DisplaySystem.getDisplaySystem().getRenderer().getWidth()/2,
				DisplaySystem.getDisplaySystem().getRenderer().getHeight()/2);
		startButton.setHeight(56);
		startButton.setWidth(300);
		startButton.addItemListener(new ItemListener(){

			@Override
			public void cursorChanged(ContentItem item, long id, float x, float y, float pressure) {}

			@Override
			public void cursorClicked(ContentItem item, long id, float x, float y, float pressure) {
				
				addPegs();
				
				addHoles();

				titleText.setVisible(false);

				modeSelectedText.setVisible(false, true);

				modeText.setVisible(false, true);

				startButton.setVisible(false, true);

			}

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

		startButton.setRotateTranslateScalable(false);

		modeText = (TextLabel)this.contentSystem.createContentItem(TextLabel.class);
		modeText.setBackgroundColour(Color.black);
		modeText.setBorderColour(Color.black);
		modeText.setTextColour(Color.white);
		modeText.setText("Mode:");
		modeText.setFont(new Font("Arial", Font.PLAIN,fontSize));
		modeText.setLocalLocation(DisplaySystem.getDisplaySystem().getRenderer().getWidth()/2,
				DisplaySystem.getDisplaySystem().getRenderer().getHeight()/4);
		modeText.setRotateTranslateScalable(false);
		modeText.addItemListener(new ItemListener(){

			@Override
			public void cursorChanged(ContentItem item, long id, float x, float y, float pressure) {}

			@Override
			public void cursorClicked(ContentItem item, long id, float x, float y, float pressure) {
				changeMode();
			}

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

		modeSelectedText = (TextLabel)this.contentSystem.createContentItem(TextLabel.class);
		modeSelectedText.setBackgroundColour(Color.black);
		modeSelectedText.setBorderColour(Color.black);
		modeSelectedText.setText("     " + mode);
		if (mode == 2){
			modeSelectedText.setTextColour(Color.orange);
		}else if (mode == 3){
			modeSelectedText.setTextColour(Color.red);
		}else{
			modeSelectedText.setTextColour(Color.green);
		}
		modeSelectedText.setBringToTopable(false);
		modeSelectedText.setFont(new Font("Arial", Font.PLAIN,fontSize));
		modeSelectedText.setLocalLocation(DisplaySystem.getDisplaySystem().getRenderer().getWidth()/2  + modeText.getWidth()/2,
				DisplaySystem.getDisplaySystem().getRenderer().getHeight()/4);
		modeSelectedText.setRotateTranslateScalable(false);
		modeSelectedText.addItemListener(new ItemListener(){

			@Override
			public void cursorChanged(ContentItem item, long id, float x, float y, float pressure) {}

			@Override
			public void cursorClicked(ContentItem item, long id, float x, float y, float pressure) {
				changeMode();
			}

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

	}

	private void changeMode() {
		if (mode == 1){
			modeSelectedText.setTextColour(Color.orange);
			modeSelectedText.setText("     2");
			mode = 2;
		}else if(mode == 2){
			modeSelectedText.setTextColour(Color.red);
			modeSelectedText.setText("     3");
			mode = 3;
		}else if(mode == 3){
			modeSelectedText.setTextColour(Color.green);
			modeSelectedText.setText("     1");
			mode = 1;
		}

	}

	private void addHoles() {
		
		square = (TextLabel)this.contentSystem.createContentItem(TextLabel.class);
		square.setLocation(DisplaySystem.getDisplaySystem().getRenderer().getWidth()/2,
				DisplaySystem.getDisplaySystem().getRenderer().getHeight()/2);		
		square.setBringToTopable(false);		
		square.setOrder(-100);
		square.setRotateTranslateScalable(false);		
		square.setBackgroundColour(Color.white);
		square.setBorderSize(0);		
		square.setAutoFitSize(false);
		square.setWidth(SQUARE_WIDTH);
		square.setHeight(SQUARE_HEIGHT);	
				
		int xLoc = DisplaySystem.getDisplaySystem().getRenderer().getWidth()/2 - HOLE_OFFSET;
		int yLoc = DisplaySystem.getDisplaySystem().getRenderer().getHeight()/2 + HOLE_OFFSET;
		
		int colCount = 1;
		
		for (int i = 0; i < shapes.size(); i++){
			holes.add((ObjShape)this.contentSystem.createContentItem(ObjShape.class));			
			holes.get(i).setShapeGeometry(PegsAndHolesApp.class.getResource(shapes.get(i) + ".obj"));				
			holes.get(i).flushGraphics();			
			holes.get(i).setSolidColour(ColorRGBA.black);
			holes.get(i).setBringToTopable(false);
						
			holes.get(i).setLocation(xLoc, yLoc);
			xLoc+= HOLE_OFFSET;
			colCount++;
			
			if(colCount > NUMBER_OF_COLUMNS){
				xLoc = DisplaySystem.getDisplaySystem().getRenderer().getWidth()/2 - HOLE_OFFSET;
				yLoc-= HOLE_OFFSET;
				colCount = 1;
			}
			
			holes.get(i).setRotateTranslateScalable(false);
		}		

		holesRemaining = holes.size();

	}

	private int saveIt = 0;

	private void addPegs() {
		for (int i = 0; i < shapes.size(); i++){
			pegs.add((ObjShape)this.contentSystem.createContentItem(ObjShape.class));			
			pegs.get(i).setShapeGeometry(PegsAndHolesApp.class.getResource(shapes.get(i) + ".obj"));
			
			pegs.get(i).setBackgroundColour(Color.getHSBColor( (float)(Math.random()/2+0.5f),(float)(Math.random()/2+0.5f),(float)(Math.random()/2+0.5f)));
			pegs.get(i).setBorderSize(0);
			
			pegs.get(i).setAsTopObject();
			
			pegs.get(i).setScaleLimit(1, 1);
			
			if (mode == 1){		
				pegs.get(i).setZRotateLimit(0, 0);

			}else{
				pegs.get(i).setAngle((FastMath.nextRandomFloat()*2) * FastMath.PI);
				if (mode > 2){
					pegs.get(i).setScale(FastMath.nextRandomFloat()+0.5f);
					pegs.get(i).setScaleLimit(0.5f, 1.5f);					
				}
			}
			
			int xLoc = 0;
			int yLoc = 0;

			if (FastMath.nextRandomInt(0, 1)==1){
				xLoc = FastMath.nextRandomInt(0, DisplaySystem.getDisplaySystem().getRenderer().getWidth());
				if (FastMath.nextRandomInt(0, 1)==1){
					yLoc = DisplaySystem.getDisplaySystem().getRenderer().getHeight() - (pegs.get(i).getHeight()/2);
				}else{
					yLoc = pegs.get(i).getHeight()/2;
				}
			}else{
				yLoc = FastMath.nextRandomInt(0, DisplaySystem.getDisplaySystem().getRenderer().getHeight());
				if (FastMath.nextRandomInt(0, 1)==1){
					xLoc = DisplaySystem.getDisplaySystem().getRenderer().getWidth() - (pegs.get(i).getWidth()/2);
				}else{
					xLoc = pegs.get(i).getWidth()/2;
				}
			}
			
			pegs.get(i).setLocation(xLoc, yLoc);

			saveIt = i;
			pegs.get(i).addItemListener(new ItemListener(){

				private int q = saveIt;

				@Override
				public void cursorChanged(ContentItem item, long id, float x, float y, float pressure) {}

				@Override
				public void cursorClicked(ContentItem item, long id, float x, float y, float pressure) {}

				@Override
				public void cursorDoubleClicked(ContentItem item, long id, float x,	float y, float pressure) {}

				@Override
				public void cursorLongHeld(ContentItem item, long id, float x, float y, float pressure) {}

				@Override
				public void cursorPressed(ContentItem item, long id, float x, float y, float pressure) {}

				@Override
				public void cursorReleased(ContentItem item, long id, float x, float y, float pressure) {
					checkAnswers(q);
				}

				@Override
				public void cursorRightClicked(ContentItem item, long id, float x, float y, float pressure) {}
			});

		}
	}

	@Override
	protected void stateUpdate(float tpf) {
		super.stateUpdate(tpf);
		if(contentSystem != null) contentSystem.update(tpf);
	}

	private void checkAnswers(int q) {

		int[] results = matchingPosition(q);

		if (results[0] == 1){

			Color colour = pegs.get(q).getBackgroundColour();
			float[] hsbvals = {0,0,0};
			Color.RGBtoHSB(colour.getRed(), colour.getGreen(), colour.getBlue(), hsbvals);
			hsbvals[1] = hsbvals[1]/2;		
			colour = Color.getHSBColor(hsbvals[0], hsbvals[1], hsbvals[2]);			
			holes.get(results[1]).setSolidColour(new ColorRGBA(colour.getRed()/255f, colour.getGreen()/255f, colour.getBlue()/255f, 1));
			pegs.get(q).setVisible(false, true);
			
			--holesRemaining;

			new Thread(){public void run(){Applet.newAudioClip(CommonResources.class.getResource("correct.wav")).play();}}.start();

			if (holesRemaining == 0){
				
				square.setVisible(false);
				for (int i = 0; i < pegs.size(); i++){
					holes.get(i).setVisible(false);
				}
				
				TextLabel endText = (TextLabel)this.contentSystem.createContentItem(TextLabel.class);
				endText.setTextColour(Color.green);
				endText.setBackgroundColour(Color.black);
				endText.setBorderSize(0);
				endText.setText("Puzzle Complete!");
				endText.setFont(new Font("Arial", Font.BOLD,30));
				endText.setLocalLocation(DisplaySystem.getDisplaySystem().getRenderer().getWidth()/2, DisplaySystem.getDisplaySystem().getRenderer().getHeight()/2);
				endText.setRotateTranslateScalable(false);

				final ImageTextLabel endButton = (ImageTextLabel)contentSystem.createContentItem(ImageTextLabel.class);
				endButton.setBackgroundColour(Color.black);
				endButton.setBorderColour(Color.black);
				endButton.setAutoFitSize(false);
				endButton.setImageInfo(CommonResources.class.getResource("exitpuzzle.png"));
				endButton.setLocalLocation(DisplaySystem.getDisplaySystem().getRenderer().getWidth()/2,
						DisplaySystem.getDisplaySystem().getRenderer().getHeight()/2);
				endButton.setHeight(56);
				endButton.setWidth(300);
				endButton.setLocalLocation(DisplaySystem.getDisplaySystem().getRenderer().getWidth()/2, DisplaySystem.getDisplaySystem().getRenderer().getHeight()/4);
				endButton.addItemListener(new ItemListener(){

					@Override
					public void cursorChanged(ContentItem item, long id, float x, float y, float pressure) {}

					@Override
					public void cursorClicked(ContentItem item, long id, float x, float y, float pressure) {

					}

					@Override
					public void cursorDoubleClicked(ContentItem item, long id, float x,	float y, float pressure) {}

					@Override
					public void cursorLongHeld(ContentItem item, long id, float x, float y, float pressure) {}

					@Override
					public void cursorPressed(ContentItem item, long id, float x, float y, float pressure) {}

					@Override
					public void cursorReleased(ContentItem item, long id, float x, float y, float pressure) {
						exitApp();
					}

					@Override
					public void cursorRightClicked(ContentItem item, long id, float x, float y, float pressure) {}
				});
				endButton.setRotateTranslateScalable(false);
			}
		}

	}

	private int[] matchingPosition(int i) {
		int[] result = {0,0};
		
		for (int j = 0; j < holes.size(); j++){
			if (!solved[j]){

				if (FastMath.abs(pegs.get(i).getLocation().x - holes.get(j).getLocation().x) < DISTANCE_THRESHOLD){
					if (FastMath.abs(pegs.get(i).getLocation().y - holes.get(j).getLocation().y) < DISTANCE_THRESHOLD){
						Spatial pegSpatial = (Spatial)pegs.get(i).getImplementationObject();
						Spatial holeSpatial = (Spatial)holes.get(j).getImplementationObject();
						
						boolean closeEnough = true;
						
						ArrayList<Vector3f> pegVertices = SpatialUtility.generateRelevantCoords(pegSpatial, SpatialUtility.getIndexOfRelevantCoords(pegSpatial));
						ArrayList<Vector3f> holeVertices = SpatialUtility.generateRelevantCoords(holeSpatial, SpatialUtility.getIndexOfRelevantCoords(holeSpatial));									

						for (int k = 0; k < pegVertices.size(); k++){
							boolean found = false;
							for (int l = 0; l < holeVertices.size(); l++){
								if (pegVertices.get(k).distance(holeVertices.get(l)) < VERTEX_THRESHOLD){
									found = true;
									holeVertices.remove(l);
									break;
								}								
							}

							if (!found){
								closeEnough = false;
								break;
							}
						}
					
						if (closeEnough){
							solved[j] = true;
							result[0] = 1;
							result[1] = j;
							break;
						}
					}
				}				
			}
		}		
		
		return result;
	}
		

}
