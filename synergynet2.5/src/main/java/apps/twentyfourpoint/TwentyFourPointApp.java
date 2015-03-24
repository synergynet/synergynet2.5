/*
 * Copyright (c) 2009 University of Durham, England
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

package apps.twentyfourpoint;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import core.SynergyNetDesktop;

import apps.twentyfourpoint.algorithm.Expression;
import apps.twentyfourpoint.algorithm.Solution;
import apps.twentyfourpoint.message.BroadcastData;
import apps.twentyfourpoint.message.ResultMessage;
import apps.twentyfourpoint.utils.IllegalExpressionException;
import apps.twentyfourpoint.utils.NOAnswerConfirmDialogue;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.BackgroundController;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.Frame;
import synergynetframework.appsystem.contentsystem.items.MultiLineTextLabel;
import synergynetframework.appsystem.contentsystem.items.RoundTextLabel;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.listener.ItemEventAdapter;
import synergynetframework.appsystem.contentsystem.items.utils.Location;
import synergynetframework.appsystem.services.ServiceManager;
import synergynetframework.appsystem.services.exceptions.CouldNotStartServiceException;
import synergynetframework.appsystem.services.net.tablecomms.client.TableCommsClientService;
import synergynetframework.appsystem.services.net.tablecomms.messages.control.fromclient.ApplicationCommsRequest;
import synergynetframework.appsystem.table.SynergyNetAppUtils;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.appsystem.table.appregistry.menucontrol.HoldTopRightConfirmVisualExit;


/**
 * The Class TwentyFourPointApp.
 */
public class TwentyFourPointApp extends DefaultSynergyNetApp {
	
	/** The comms. */
	private TableCommsClientService comms;
	
	/** The content system. */
	protected ContentSystem contentSystem;
	
	/** The message handler. */
	protected MessageHandler messageHandler;
	
	/** The items. */
	protected List<ContentItem> items = new ArrayList<ContentItem>();
	
	/** The items in answer area. */
	protected List<ContentItem> itemsInAnswerArea = new ArrayList<ContentItem>();
	
	/** The answer box location. */
	protected Location answerBoxLocation = new Location(512, 200, 0);
	
	/** The answer box width. */
	protected int answerBoxWidth = 700;
	
	/** The answer box height. */
	protected int answerBoxHeight = 200;
	
	/** The cards. */
	protected int[] cards = new int[4];
	
	/** The current result scale. */
	protected float currentResultScale = 0.3f;
	
	/** The answer. */
	protected String answer="";
	
	/** The has solution. */
	protected boolean hasSolution = true;
	
	/** The finish movie on. */
	protected boolean finishMovieOn = false;
	
	/** The win number. */
	protected int winNumber=0;
	
	/** The lose number. */
	protected int loseNumber =0;
	
	/** The result label. */
	protected MultiLineTextLabel resultLabel;
	
	/** The win score label. */
	protected MultiLineTextLabel winScoreLabel;
	
	/** The lose score label. */
	protected MultiLineTextLabel loseScoreLabel;
	
	/** The confirm dialogue. */
	protected NOAnswerConfirmDialogue confirmDialogue=null;

	/**
	 * Instantiates a new twenty four point app.
	 *
	 * @param info the info
	 */
	public TwentyFourPointApp(ApplicationInfo info) {
		super(info);		
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#addContent()
	 */
	@Override
	public void addContent() {		
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(this);		
		setMenuController(new HoldTopRightConfirmVisualExit(this));
		SynergyNetAppUtils.addTableOverlay(this);
		BackgroundController backgroundController;backgroundController = (BackgroundController)contentSystem.createContentItem(BackgroundController.class);
		backgroundController.setOrder(Integer.MIN_VALUE);
		backgroundController.setBackgroundRotateTranslateScalable(false);

		//load startup window
		loadStartUpContent();		
		loadScorePanel();
				
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#onActivate()
	 */
	@Override
	public void onActivate() {
		if(comms == null) {
			
			messageHandler = new MessageHandler(this);
			try {
				comms = (TableCommsClientService) ServiceManager.getInstance().get(TableCommsClientService.class);
			} catch (CouldNotStartServiceException e1) {
				e1.printStackTrace();
			}
			try {
				comms.register(this, messageHandler);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			comms.sendMessage(new ApplicationCommsRequest(TwentyFourPointApp.class.getName()));
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp#stateUpdate(float)
	 */
	@Override
	public void stateUpdate(float tpf) {
		if(comms != null) comms.update();
		if(contentSystem != null) contentSystem.update(tpf);
		
		calculateResult();
		
		//start end movie
		if (this.finishMovieOn && this.resultLabel!=null){
			resultLabel.setScale(this.currentResultScale);
			this.currentResultScale+=tpf;
			
			if (this.currentResultScale>=1.3f){
				this.finishMovieOn=false;
				this.currentResultScale = 0.3f;
				showFinishMenu();
			}
		}
	
	}
	
	/**
	 * Check has solution.
	 */
	private void checkHasSolution(){
		Solution solution = new Solution(cards[0],cards[1], cards[2],cards[3]);
		if (solution.hasSolution()){
			this.hasSolution = true;
			this.answer = Solution.getSolutionString(solution.getSolution());
		}
		else
			this.hasSolution = false;
			
	}
	
	/**
	 * Send message.
	 *
	 * @param obj the obj
	 */
	private void sendMessage(Object obj) {
		if(comms != null) {
			try {
				comms.sendMessage(obj);					
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#onDeactivate()
	 */
	@Override
	public void onDeactivate() {
		
	}
	
	/**
	 * Load start up content.
	 */
	private void loadStartUpContent(){
		SimpleButton startUpButton = (SimpleButton)this.contentSystem.createContentItem(SimpleButton.class);
		startUpButton.setText("START GAME");
		startUpButton.setFont(new Font("Arial", Font.PLAIN, 100));
		startUpButton.setBackgroundColour(Color.BLACK);
		startUpButton.setBorderSize(4);
		startUpButton.setBorderColour(Color.green);
		startUpButton.setTextColour(Color.green);
		startUpButton.setLocalLocation(512, 360);
		startUpButton.setRotateTranslateScalable(false);
		startUpButton.setBringToTopable(false);
		startUpButton.addItemListener(new ItemEventAdapter(){
		@Override
		public void cursorClicked(ContentItem item, long id, float x, float y,
				float pressure) {
			removeAllItems();
			loadContent();
			sendMessage(new BroadcastData(items));
		}		
	});
		
		items.add(startUpButton);
	}
	
	/**
	 * Load score panel.
	 */
	private void loadScorePanel(){
		RoundTextLabel winLabel = (RoundTextLabel)this.contentSystem.createContentItem(RoundTextLabel.class);		
		winLabel.setAutoFitSize(false);
		winLabel.setRadius(30f);
		winLabel.setBackgroundColour(Color.BLUE);
		winLabel.setBorderColour(Color.white);
		winLabel.setTextColour(Color.white);
		winLabel.setLines("WIN", 40);
		winLabel.setFont(new Font("Arial", Font.PLAIN,16));
		winLabel.setBringToTopable(false);
		winLabel.setRotateTranslateScalable(false);
		winLabel.setLocalLocation(40, 700);
		//items.add(winLabel);
		
		winScoreLabel = (MultiLineTextLabel)this.contentSystem.createContentItem(MultiLineTextLabel.class);
		winScoreLabel.setBackgroundColour(Color.black);
		winScoreLabel.setBorderSize(0);
		winScoreLabel.setLines(String.valueOf(this.winNumber), 30);
		winScoreLabel.setFont(new Font("Arial", Font.PLAIN,30));
		winScoreLabel.setLocalLocation(100, 700);
		winScoreLabel.setBringToTopable(false);
		winScoreLabel.setRotateTranslateScalable(false);
		//items.add(winScoreLabel);
		
		RoundTextLabel loseLabel = (RoundTextLabel)this.contentSystem.createContentItem(RoundTextLabel.class);		
		loseLabel.setAutoFitSize(false);
		loseLabel.setRadius(30f);
		loseLabel.setBackgroundColour(Color.RED);
		loseLabel.setBorderColour(Color.white);
		loseLabel.setTextColour(Color.white);
		loseLabel.setLines("LOSE", 40);
		loseLabel.setFont(new Font("Arial", Font.PLAIN,16));
		loseLabel.setBringToTopable(false);
		loseLabel.setRotateTranslateScalable(false);
		loseLabel.setLocalLocation(40, 620);
		//items.add(loseLabel);
		
		loseScoreLabel = (MultiLineTextLabel)this.contentSystem.createContentItem(MultiLineTextLabel.class);
		loseScoreLabel.setBackgroundColour(Color.black);
		loseScoreLabel.setBorderSize(0);
		loseScoreLabel.setLines(String.valueOf(this.loseNumber).trim(), 30);
		loseScoreLabel.setFont(new Font("Arial", Font.PLAIN,30));
		loseScoreLabel.setLocalLocation(100, 620);
		loseScoreLabel.setBringToTopable(false);
		loseScoreLabel.setRotateTranslateScalable(false);
		//items.add(loseScoreLabel);
	}
	
	/**
	 * Load content.
	 */
	private void loadContent(){
		
		items.clear();
		
		MultiLineTextLabel title = (MultiLineTextLabel)this.contentSystem.createContentItem(MultiLineTextLabel.class);
		title.setLines("Calculate 24", 30);
		title.setFont(new Font("Arial", Font.PLAIN, 62));
		title.setBackgroundColour(Color.BLACK);
		title.setBorderSize(4);
		title.setBorderColour(Color.black);
		title.setTextColour(Color.green);
		title.setLocalLocation(512, 700);
		title.setRotateTranslateScalable(false);
		title.setBringToTopable(false);
		items.add(title);
		
		Frame answerBox = (Frame)this.contentSystem.createContentItem(Frame.class);
		answerBox.setLocalLocation(this.answerBoxLocation);
		answerBox.setWidth(this.answerBoxWidth);
		answerBox.setHeight(this.answerBoxHeight);
		answerBox.setBackgroundColour(Color.black);
		answerBox.setBorderSize(4);
		answerBox.setBorderColour(Color.green);
		answerBox.setRotateTranslateScalable(false);
		answerBox.setBringToTopable(false);
		items.add(answerBox);
		
		MultiLineTextLabel noAnswerLabel = (MultiLineTextLabel)this.contentSystem.createContentItem(MultiLineTextLabel.class);
		noAnswerLabel.setLines("No Answer", 30);
		noAnswerLabel.setLocalLocation(900, 670);
		noAnswerLabel.setFont(new Font("Arial", Font.PLAIN, 30));
		noAnswerLabel.setBackgroundColour(Color.black);
		noAnswerLabel.setBorderSize(4);
		noAnswerLabel.setBorderColour(Color.black);
		noAnswerLabel.setTextColour(Color.green);	
		items.add(noAnswerLabel);
		
		this.loadOperatorCard();	
		loadCard();
		
		this.answer = "";
		checkHasSolution();	
		sendMessage(new BroadcastData(items));
	
	}
	
	/**
	 * Load content.
	 *
	 * @param items the items
	 */
	public void loadContent(List<ContentItem> items){
		for (ContentItem item:items){
			contentSystem.addContentItem(item);
			this.items.add(item);
		}	
		
		this.answer = "";
		checkHasSolution();	
		
		loadCardImage();
	}
	
	/**
	 * Load operator card.
	 */
	private void loadOperatorCard(){

			//plus
			MultiLineTextLabel plusCard1 = (MultiLineTextLabel)this.contentSystem.createContentItem(MultiLineTextLabel.class);
			this.renderOperatorCard(plusCard1, "+", 850, 600);
			MultiLineTextLabel plusCard2 = (MultiLineTextLabel)this.contentSystem.createContentItem(MultiLineTextLabel.class);
			this.renderOperatorCard(plusCard2, "+", 850, 600);
			MultiLineTextLabel plusCard3 = (MultiLineTextLabel)this.contentSystem.createContentItem(MultiLineTextLabel.class);
			this.renderOperatorCard(plusCard3, "+", 850, 600);
			
			//sub
			MultiLineTextLabel subtractCard1 = (MultiLineTextLabel)this.contentSystem.createContentItem(MultiLineTextLabel.class);
			this.renderOperatorCard(subtractCard1, "-", 950, 605);
			MultiLineTextLabel subtractCard2 = (MultiLineTextLabel)this.contentSystem.createContentItem(MultiLineTextLabel.class);
			this.renderOperatorCard(subtractCard2, "-", 950, 605);
			MultiLineTextLabel subtractCard3 = (MultiLineTextLabel)this.contentSystem.createContentItem(MultiLineTextLabel.class);
			this.renderOperatorCard(subtractCard3, "-", 950, 605);
			
			//sub
			MultiLineTextLabel multiplyCard1 = (MultiLineTextLabel)this.contentSystem.createContentItem(MultiLineTextLabel.class);
			this.renderOperatorCard(multiplyCard1, "*", 850, 500);
			MultiLineTextLabel multiplyCard2 = (MultiLineTextLabel)this.contentSystem.createContentItem(MultiLineTextLabel.class);
			this.renderOperatorCard(multiplyCard2, "*", 850, 500);
			MultiLineTextLabel multiplyCard3 = (MultiLineTextLabel)this.contentSystem.createContentItem(MultiLineTextLabel.class);
			this.renderOperatorCard(multiplyCard3, "*", 850, 500);
			
			//divide
			MultiLineTextLabel divideCard1 = (MultiLineTextLabel)this.contentSystem.createContentItem(MultiLineTextLabel.class);
			this.renderOperatorCard(divideCard1, "/", 950, 505);
			MultiLineTextLabel divideCard2 = (MultiLineTextLabel)this.contentSystem.createContentItem(MultiLineTextLabel.class);
			this.renderOperatorCard(divideCard2, "/", 950, 505);
			MultiLineTextLabel divideCard3 = (MultiLineTextLabel)this.contentSystem.createContentItem(MultiLineTextLabel.class);
			this.renderOperatorCard(divideCard3, "/", 950, 505);
			
			//(
			MultiLineTextLabel leftCard1 = (MultiLineTextLabel)this.contentSystem.createContentItem(MultiLineTextLabel.class);
			this.renderOperatorCard(leftCard1, "(", 850, 400);
			MultiLineTextLabel leftCard2 = (MultiLineTextLabel)this.contentSystem.createContentItem(MultiLineTextLabel.class);
			this.renderOperatorCard(leftCard2, "(", 850, 400);
			MultiLineTextLabel leftCard3 = (MultiLineTextLabel)this.contentSystem.createContentItem(MultiLineTextLabel.class);
			this.renderOperatorCard(leftCard3, "(", 850, 400);
			
			//)
			MultiLineTextLabel rightCard1 = (MultiLineTextLabel)this.contentSystem.createContentItem(MultiLineTextLabel.class);
			this.renderOperatorCard(rightCard1, ")", 950, 400);
			MultiLineTextLabel rightCard2 = (MultiLineTextLabel)this.contentSystem.createContentItem(MultiLineTextLabel.class);
			this.renderOperatorCard(rightCard2, ")", 950, 400);
			MultiLineTextLabel rightCard3 = (MultiLineTextLabel)this.contentSystem.createContentItem(MultiLineTextLabel.class);
			this.renderOperatorCard(rightCard3, ")", 950, 400);
				
	}
	
	/**
	 * Render operator card.
	 *
	 * @param operatorCard the operator card
	 * @param operator the operator
	 * @param x the x
	 * @param y the y
	 */
	private void renderOperatorCard(MultiLineTextLabel operatorCard, String operator, int x, int y){
		operatorCard.setLocalLocation(x, y);
		operatorCard.setFont(new Font("Arial", Font.PLAIN, 60));
		operatorCard.setLines(operator, 30);
		operatorCard.setBackgroundColour(Color.black);
		operatorCard.setBorderSize(4);
		operatorCard.setBorderColour(Color.black);
		operatorCard.setTextColour(Color.green);
		items.add(operatorCard);
	}
	
	/**
	 * Load card.
	 */
	private void loadCard(){
		MultiLineTextLabel card1 = (MultiLineTextLabel)this.contentSystem.createContentItem(MultiLineTextLabel.class);
		renderCard(card1, 0);
		MultiLineTextLabel card2 = (MultiLineTextLabel)this.contentSystem.createContentItem(MultiLineTextLabel.class);
		renderCard(card2, 1);
		MultiLineTextLabel card3 = (MultiLineTextLabel)this.contentSystem.createContentItem(MultiLineTextLabel.class);
		renderCard(card3, 2);
		MultiLineTextLabel card4 = (MultiLineTextLabel)this.contentSystem.createContentItem(MultiLineTextLabel.class);
		renderCard(card4, 3);
		
	}
	
	/**
	 * Render card.
	 *
	 * @param card the card
	 * @param i the i
	 */
	private void renderCard(MultiLineTextLabel card, int i){
		Random r = new Random();
		int number =r.nextInt(13)+1;
		this.cards[i] = number;
		card.setAutoFitSize(false);
		card.setLocalLocation(350+i*100, 500);
		card.setLines(String.valueOf(number), 30);
		card.setWidth(72);
		card.setHeight(96);
		int cardType = r.nextInt(4)+1;
		String imagePath = String.valueOf(number)+String.valueOf(cardType)+".png";
		card.setNote(imagePath);
		card.drawImage(TwentyFourPointApp.class.getResource(imagePath), 0, 0, 72, 96);
		items.add(card);
	}
	
	/**
	 * Calculate result.
	 */
	private void calculateResult(){
		this.getItemsInAnswerArea();
			
		String answerString = this.getAnswerString();
		Expression exp = new Expression(answerString);
		try {
			Double value = exp.getValue();
			
			if (value==24 && this.allPointCardInAnswerArea()){
				this.answer = answerString;
				this.sendMessage(new ResultMessage(true, this.answer));
				finishGame(true);
			}
				
		} catch (IllegalExpressionException e) {
		}
		
	}
	
	/**
	 * Gets the items in answer area.
	 *
	 * @return the items in answer area
	 */
	private void getItemsInAnswerArea(){
			
		this.itemsInAnswerArea.clear();
		Rectangle innerAnswerArea = new Rectangle((int)(this.answerBoxLocation.x-this.answerBoxWidth/2+20), (int)(this.answerBoxLocation.y-this.answerBoxHeight/2+20), (int)(this.answerBoxWidth-40), (int)(this.answerBoxHeight-40));
		for(ContentItem item:items){
			if (innerAnswerArea.contains((int)(item.getLocalLocation().getX()), (int)(item.getLocalLocation().getY()))){
				this.itemsInAnswerArea.add(item);			
			}				
		}
		
		//sort by locationX
		int minIndex=0;
		for (int i=0; i<this.itemsInAnswerArea.size()-1; i++){		
			minIndex = i;
			for (int j=i; j<this.itemsInAnswerArea.size(); j++){
				if (itemsInAnswerArea.get(j).getLocalLocation().getX()<itemsInAnswerArea.get(minIndex).getLocalLocation().getX()){
					minIndex = j;
				}			
			}		
			ContentItem temp = itemsInAnswerArea.get(minIndex);
			this.itemsInAnswerArea.remove(minIndex);
			this.itemsInAnswerArea.add(i, temp);
		}
		
	}
	
	/**
	 * Gets the answer string.
	 *
	 * @return the answer string
	 */
	private String getAnswerString(){
		String answerString = "";
		for (int i=0; i<this.itemsInAnswerArea.size(); i++){
			ContentItem item = this.itemsInAnswerArea.get(i);
			if (item instanceof MultiLineTextLabel){
				if (((MultiLineTextLabel) item).getFirstLine().trim().equals("No Answer")){
					if (confirmDialogue==null)
						confirmDialogue = new NOAnswerConfirmDialogue(contentSystem, this, (MultiLineTextLabel) item );
				}
				
				answerString+=(((MultiLineTextLabel) item).getFirstLine());
			}
		}
		return answerString;
	}
	
	/**
	 * Finish game.
	 *
	 * @param win the win
	 */
	private void finishGame(boolean win){
		removeAllItems();
		
		//plus
		resultLabel = (MultiLineTextLabel)this.contentSystem.createContentItem(MultiLineTextLabel.class);
		resultLabel.setLocalLocation(512, 384);
		resultLabel.setFont(new Font("Arial", Font.PLAIN, 300));
		String result="WIN!";
		if (!win){
			result = "LOSE!";
			this.loseNumber=this.loseNumber+1;
			loseScoreLabel.setLines(String.valueOf(this.loseNumber), 30);
		}else
		{
			this.winNumber = this.winNumber+1;
			winScoreLabel.setLines(String.valueOf(this.winNumber), 30);
		}
		resultLabel.setLines(result, 30);
		resultLabel.setBackgroundColour(Color.black);
		resultLabel.setBorderSize(4);
		resultLabel.setBorderColour(Color.black);
		resultLabel.setTextColour(Color.green);
		resultLabel.setScaleLimit(0.1f, 10f);
		items.add(resultLabel);
		
		this.finishMovieOn = true;
		this.currentResultScale = 0.3f;
		
	}
	
	/**
	 * Show finish menu.
	 */
	private void showFinishMenu(){
		if (this.resultLabel!=null){
			if (!this.answer.equals("No Answer"))
				this.answer+="=24";
			resultLabel.setLines(this.answer, 50);
			resultLabel.setScale(0.3f);
			resultLabel.setLocalLocation(512, 550);
		}
				
		SimpleButton restartButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);
		restartButton.setLocalLocation(400, 250);
		restartButton.setFont(new Font("Arial", Font.PLAIN, 30));
		restartButton.setText("NEXT");
		restartButton.setBackgroundColour(Color.black);
		restartButton.setBorderSize(4);
		restartButton.setBorderColour(Color.green);
		restartButton.setTextColour(Color.green);
		restartButton.setRotateTranslateScalable(false);
		restartButton.setBringToTopable(false);
		restartButton.addItemListener(new ItemEventAdapter(){
			@Override
			public void cursorClicked(ContentItem item, long id, float x, float y,
					float pressure) {
				restart();
			}		
		});
		items.add(restartButton);
		
		SimpleButton quitButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);
		quitButton.setLocalLocation(600, 250);
		quitButton.setFont(new Font("Arial", Font.PLAIN, 30));
		quitButton.setText("QUIT");
		quitButton.setBackgroundColour(Color.black);
		quitButton.setBorderSize(4);
		quitButton.setBorderColour(Color.green);
		quitButton.setTextColour(Color.green);
		quitButton.setRotateTranslateScalable(false);
		quitButton.setBringToTopable(false);
		quitButton.addItemListener(new ItemEventAdapter(){
			@Override
			public void cursorClicked(ContentItem item, long id, float x, float y,
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
		items.add(quitButton);
		
		
	}
	
	/**
	 * Removes the all items.
	 */
	public void removeAllItems(){
			
		for (ContentItem item:items){
			contentSystem.removeContentItem(item);
		}	
		
		if (this.resultLabel!=null)
			this.resultLabel=null;
		items.clear();
	}
	
	/**
	 * Restart.
	 */
	private void restart(){	
		this.removeAllItems();
		this.loadContent();
	}	
	
	/**
	 * Check no answer.
	 */
	public void checkNoAnswer(){
		if (!this.hasSolution){
			this.answer = "No Answer";
			this.sendMessage(new ResultMessage(true, this.answer));
			finishGame(true);
		}
		else {
			this.sendMessage(new ResultMessage(false, this.answer));
			finishGame(false);
		}
		this.removeConfirmDialogue();	
	}
	
	/**
	 * Removes the confirm dialogue.
	 */
	public void removeConfirmDialogue(){
		if (this.confirmDialogue!=null){
			this.confirmDialogue.clearContent();
			this.confirmDialogue=null;
		}
	}
	
	/**
	 * Receive result.
	 *
	 * @param win the win
	 * @param resultString the result string
	 */
	public void receiveResult(boolean win, String resultString){
		this.answer = resultString;
		this.finishGame(!win);	
	}
	
	/**
	 * Load card image.
	 */
	private void loadCardImage(){
		for (ContentItem item:items){
			String imagePath = item.getNote();
			if (!imagePath.equals("") && (item instanceof MultiLineTextLabel)){
				((MultiLineTextLabel)item).drawImage(TwentyFourPointApp.class.getResource(imagePath), 0, 0, 72, 96);
			}
		}
	}
	
	/**
	 * All point card in answer area.
	 *
	 * @return true, if successful
	 */
	private boolean allPointCardInAnswerArea(){
		int numberOfPointCard =0;
		for (ContentItem item:this.itemsInAnswerArea){
			if (!item.getNote().equals("")){
				numberOfPointCard++;
			}
		}
		if (numberOfPointCard==4)
			return true;
		else
			return false;
	}
	
}
