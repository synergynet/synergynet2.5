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

package apps.xmlpuzzle;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import com.jme.math.FastMath;
import com.jme.system.DisplaySystem;

import common.CommonResources;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.ImageTextLabel;
import synergynetframework.appsystem.contentsystem.items.TextLabel;
import synergynetframework.appsystem.contentsystem.items.listener.ItemListener;
import synergynetframework.appsystem.table.SynergyNetAppUtils;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.appsystem.table.appregistry.menucontrol.HoldTopRightConfirmVisualExit;


/**
 * The Class XmlPuzzleApp.
 */
public class XmlPuzzleApp extends DefaultSynergyNetApp {

	/** The puzzle name. */
	private String puzzleName = "";
	
	/** The question values. */
	private List<String> questionValues = new ArrayList<String>();
	
	/** The answer values. */
	private List<String> answerValues = new ArrayList<String>();
	
	/** The question text. */
	private List<TextLabel> questionText = new ArrayList<TextLabel>();
	
	/** The answer text. */
	private List<TextLabel> answerText = new ArrayList<TextLabel>();
	
	/** The max question length. */
	private int maxQuestionLength = 20;
	
	/** The max answer length. */
	private int maxAnswerLength = 20;
	
	/** The questions remaining. */
	private int questionsRemaining = 1;

	/** The Constant TRANSLATION_THRESHOLD. */
	final private static float TRANSLATION_THRESHOLD = 20;
	
	/** The Constant ROTATION_THRESHOLD. */
	final private static float ROTATION_THRESHOLD = 0.5f;
	
	/** The Constant SCALE_THRESHOLD. */
	final private static float SCALE_THRESHOLD = 0.5f;

	/** The list layout. */
	private static boolean LIST_LAYOUT = true;
	
	/** The highlight questions. */
	private static boolean HIGHLIGHT_QUESTIONS = true;

	/** The title text. */
	private TextLabel titleText;
	
	/** The start button. */
	private ImageTextLabel startButton;
	
	/** The mode text. */
	private TextLabel modeText;
	
	/** The mode selected text. */
	private TextLabel modeSelectedText;

	/** The mode. */
	private int mode = 1;

	/** The font size. */
	private int fontSize = 30;

	/**
	 * Instantiates a new xml puzzle app.
	 *
	 * @param info the info
	 */
	public XmlPuzzleApp(ApplicationInfo info) {
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
		readIn();
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#onActivate()
	 */
	@Override
	public void onActivate() {
		contentSystem.removeAllContentItems();

		questionText.clear();
		answerText.clear();

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
				
				addQuestions();

				addAnswers();

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

	/**
	 * Change mode.
	 */
	private void changeMode() {
		if (mode ==1){
			modeSelectedText.setTextColour(Color.orange);
			modeSelectedText.setText("     2");
			mode = 2;
			LIST_LAYOUT = false;
			HIGHLIGHT_QUESTIONS = true;
		}else if(mode == 2){
			modeSelectedText.setTextColour(Color.red);
			modeSelectedText.setText("     3");
			mode = 3;
			LIST_LAYOUT = false;
			HIGHLIGHT_QUESTIONS = false;
		}else if(mode == 3){
			modeSelectedText.setTextColour(Color.green);
			modeSelectedText.setText("     1");
			mode = 1;
			LIST_LAYOUT = true;
			HIGHLIGHT_QUESTIONS = false;
		}

	}

	/**
	 * Adds the questions.
	 */
	private void addQuestions() {
		
		float yDecrement = DisplaySystem.getDisplaySystem().getHeight()/(questionValues.size()+1);
		float yOffset = DisplaySystem.getDisplaySystem().getHeight() - yDecrement;

		for (int i = 0; i < questionValues.size(); i++){
			questionText.add((TextLabel)this.contentSystem.createContentItem(TextLabel.class));
			questionText.get(i).setText(questionValues.get(i));
			questionText.get(i).setFont(new Font("Arial", Font.PLAIN,fontSize));
			questionText.get(i).setLocation(30 + questionText.get(i).getWidth()/2, yOffset);
			questionText.get(i).flushGraphics();

			if (LIST_LAYOUT){
				questionText.get(i).setBorderSize(0);
				questionText.get(i).setBackgroundColour(Color.black);
				questionText.get(i).setTextColour(Color.white);
				questionText.get(i).setRotateTranslateScalable(false);
				questionText.get(i).setBringToTopable(false);
				if (yDecrement < questionText.get(i).getHeight() + (questionText.get(i).getHeight()/2))
					yDecrement = questionText.get(i).getHeight() + (questionText.get(i).getHeight()/2);
				yOffset-= yDecrement;
				if(yOffset < titleText.getHeight()*1)
					i = questionValues.size();
			}else{
				if (HIGHLIGHT_QUESTIONS){
					questionText.get(i).setBorderColour(Color.white);
					questionText.get(i).setBackgroundColour(Color.black);
					questionText.get(i).setTextColour(Color.red);
				}else{
					questionText.get(i).setBorderSize(0);
					questionText.get(i).setBackgroundColour(Color.white);
					questionText.get(i).setTextColour(Color.black);
				}
				questionText.get(i).placeRandom();
				questionText.get(i).setAngle((FastMath.nextRandomFloat()*2) * FastMath.PI);
				questionText.get(i).setScale((FastMath.nextRandomFloat()+0.5f) * 2);
				questionText.get(i).setScaleLimit(1f, 3f);
				saveIt = i;
				questionText.get(i).addItemListener(new ItemListener(){

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

		questionsRemaining = questionText.size();

	}

	/** The save it. */
	private int saveIt = 0;

	/**
	 * Adds the answers.
	 */
	private void addAnswers() {
		for (int i = 0; i < questionText.size(); i++){
			answerText.add((TextLabel)this.contentSystem.createContentItem(TextLabel.class));
			if (!LIST_LAYOUT && HIGHLIGHT_QUESTIONS){
				answerText.get(i).setBackgroundColour(Color.black);
				answerText.get(i).setBorderColour(Color.white);
				answerText.get(i).setTextColour(Color.green);
			}else{
				answerText.get(i).setBackgroundColour(Color.white);
				answerText.get(i).setBorderSize(0);
				answerText.get(i).setTextColour(Color.black);
			}
			answerText.get(i).setAsTopObject();
			answerText.get(i).setText(answerValues.get(i));
			answerText.get(i).setFont(new Font("Arial", Font.PLAIN,fontSize));
			answerText.get(i).setScaleLimit(1f, 3f);
			answerText.get(i).placeRandom();
			answerText.get(i).setAngle((FastMath.nextRandomFloat()*2) * FastMath.PI);
			answerText.get(i).setScale((FastMath.nextRandomFloat()+0.5f) * 2);
			saveIt = i;
			answerText.get(i).addItemListener(new ItemListener(){

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

	/**
	 * Read in.
	 */
	private void readIn() {
		getXML("questionsAndAnswers.xml");
	}

	/**
	 * Gets the xml.
	 *
	 * @param resourceName the resource name
	 * @return the xml
	 */
	public void getXML(String resourceName){

		Document document = null;

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setNamespaceAware(false);

		try{
			DocumentBuilder builder;
			builder = factory.newDocumentBuilder();
			document = builder.parse(XmlPuzzleApp.class.getResourceAsStream(resourceName));
		}catch(ParserConfigurationException e){
			e.printStackTrace();
		}catch(SAXException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}

		if (document != null){
			getValues(document);

		}
	}

	/**
	 * Gets the values.
	 *
	 * @param document the document
	 * @return the values
	 */
	private void getValues(Document document) {

		NodeList list = document.getElementsByTagName("Name");
		for (int i = 0; i <list.getLength(); i++){
			puzzleName = list.item(i).getTextContent();
		}

		list = document.getElementsByTagName("Question");

		for (int i = 0; i <list.getLength(); i++){
			questionValues.add(list.item(i).getTextContent());
			if (list.item(i).getTextContent().length() > maxQuestionLength){
				System.out.println("The question's length is too long: \n" +
						list.item(i).getTextContent());
				System.exit(0);

			}
		}

		list = document.getElementsByTagName("Answer");

		for (int i = 0; i <list.getLength(); i++){
			answerValues.add(list.item(i).getTextContent());
			if (list.item(i).getTextContent().length() > maxAnswerLength){
				System.out.println("The question's length is too long: \n" +
						list.item(i).getTextContent());
				System.exit(0);
			}
		}
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp#stateUpdate(float)
	 */
	@Override
		protected void stateUpdate(float tpf) {
			super.stateUpdate(tpf);
			if(contentSystem != null) contentSystem.update(tpf);
		}

	/**
	 * Check answers.
	 *
	 * @param q the q
	 */
	private void checkAnswers(int q) {
		boolean correct = false;

		if (matchingPosition(q))
			correct = true;

		if (correct && questionText.get(q).isVisible()){

			if (questionText.get(q).isVisible() && answerText.get(q).isVisible()){
				questionText.get(q).setVisible(false, true);
				answerText.get(q).setVisible(false, true);
				--questionsRemaining;
			}
			new Thread(){public void run(){Applet.newAudioClip(CommonResources.class.getResource("correct.wav")).play();}}.start();

			if (questionsRemaining == 0){
			
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

	/**
	 * Matching position.
	 *
	 * @param i the i
	 * @return true, if successful
	 */
	private boolean matchingPosition(int i) {
		float distance = answerText.get(i).getWidth()/2 + questionText.get(i).getWidth()/2;
		if (LIST_LAYOUT){
			if (FastMath.abs(answerText.get(i).getLocation().x - (questionText.get(i).getLocation().x + distance)) < TRANSLATION_THRESHOLD ){
				if (FastMath.abs(answerText.get(i).getLocation().y - (questionText.get(i).getLocation().y)) < TRANSLATION_THRESHOLD){
					if(FastMath.abs(questionText.get(i).getAngle() - answerText.get(i).getAngle()) < ROTATION_THRESHOLD ||
							FastMath.abs((questionText.get(i).getAngle() + (2*FastMath.PI)) - answerText.get(i).getAngle()) < ROTATION_THRESHOLD){
						if(FastMath.abs(questionText.get(i).getScale() - answerText.get(i).getScale()) < SCALE_THRESHOLD){
							return true;
						}
					}
				}
			}
			return false;
		}else{
			distance = distance * questionText.get(i).getScale();

			float xTarget = questionText.get(i).getLocation().x +  FastMath.cos(questionText.get(i).getAngle()) * distance;
			float yTarget = questionText.get(i).getLocation().y +  FastMath.sin(questionText.get(i).getAngle()) * distance;

			if (FastMath.abs(answerText.get(i).getLocation().x - xTarget) < TRANSLATION_THRESHOLD){
				if (FastMath.abs(answerText.get(i).getLocation().y - yTarget) < TRANSLATION_THRESHOLD){
					if(FastMath.abs(questionText.get(i).getAngle() - answerText.get(i).getAngle()) < ROTATION_THRESHOLD ||
							FastMath.abs((questionText.get(i).getAngle() + (2*FastMath.PI)) - answerText.get(i).getAngle()) < ROTATION_THRESHOLD){
						if(FastMath.abs(questionText.get(i).getScale() - answerText.get(i).getScale()) < SCALE_THRESHOLD){
							return true;
						}
					}
				}
			}
			return false;
		}
	}

}
