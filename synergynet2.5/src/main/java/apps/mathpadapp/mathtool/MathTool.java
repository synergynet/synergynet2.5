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

package apps.mathpadapp.mathtool;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import mit.ai.nl.core.Expression;
import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.MathPad;
import synergynetframework.appsystem.contentsystem.items.MathPad.MathHandwritingListener;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.TextLabel;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;
import synergynetframework.appsystem.contentsystem.jme.items.utils.DrawData;
import apps.mathpadapp.conceptmapping.GraphManager;
import apps.mathpadapp.controllerapp.assignmentbuilder.Assignment;
import apps.mathpadapp.controllerapp.assignmentcontroller.AssignmentInfo;
import apps.mathpadapp.util.MTFrame;
import apps.mathpadapp.util.MTMessageBox;
import apps.mathpadapp.util.MTMessageBox.MessageListener;

/**
 * The Class MathTool.
 */
public class MathTool extends MTFrame {

	/**
	 * The listener interface for receiving mathTool events. The class that is
	 * interested in processing a mathTool event implements this interface, and
	 * the object created with that class is registered with a component using
	 * the component's <code>addMathToolListener<code> method. When
	 * the mathTool event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see MathToolEvent
	 */
	public interface MathToolListener {

		/**
		 * Assignment answer ready.
		 *
		 * @param info
		 *            the info
		 */
		public void assignmentAnswerReady(AssignmentInfo info);

		/**
		 * Math pad closed.
		 *
		 * @param mathTool
		 *            the math tool
		 */
		public void mathPadClosed(MathTool mathTool);

		/**
		 * Separator changed.
		 *
		 * @param newState
		 *            the new state
		 */
		public void separatorChanged(SeparatorState newState);

		/**
		 * User login.
		 *
		 * @param userName
		 *            the user name
		 * @param password
		 *            the password
		 */
		public void userLogin(String userName, String password);
	}

	/**
	 * The Enum SeparatorState.
	 */
	public enum SeparatorState {
		/** The collapsed. */
		COLLAPSED,
		/** The expanded. */
		EXPANDED
	}

	/**
	 * The Enum WritingState.
	 */
	public enum WritingState {
		/** The eraser. */
		ERASER, /** The free draw. */
		FREE_DRAW
	}

	/** The answer pad. */
	protected AnswerDialog answerPad;

	/** The assignment handler. */
	protected AssignmentHandler assignmentHandler;

	/** The back text color. */
	protected Color backTextColor;

	/** The control panel. */
	protected MathToolControlPanel controlPanel;

	/** The current math pad. */
	protected MathPad currentMathPad;;

	/** The current writing state. */
	protected WritingState currentWritingState = WritingState.FREE_DRAW;;
	
	/** The login dialog. */
	protected LoginDialog loginDialog;

	/** The math pad list. */
	protected List<MathPad> mathPadList = new ArrayList<MathPad>();

	/** The math tool initialiser. */
	protected MathToolInitialiser mathToolInitialiser;

	/** The math tool listeners. */
	protected transient Queue<MathToolListener> mathToolListeners = new ConcurrentLinkedQueue<MathToolListener>();

	/** The pad index. */
	protected int padIndex = 0;

	/** The page no label. */
	protected TextLabel pageNoLabel;

	/** The separator btn. */
	protected SimpleButton separatorBtn;
	
	/** The separator state. */
	protected SeparatorState separatorState = SeparatorState.COLLAPSED;

	/**
	 * Instantiates a new math tool.
	 *
	 * @param contentSystem
	 *            the content system
	 */
	public MathTool(final ContentSystem contentSystem) {
		this(contentSystem, null);
	}

	/**
	 * Instantiates a new math tool.
	 *
	 * @param contentSystem
	 *            the content system
	 * @param graphManager
	 *            the graph manager
	 */
	public MathTool(final ContentSystem contentSystem, GraphManager graphManager) {
		super(contentSystem, graphManager);
		this.setTitle("Math Pad");
		this.getWindow().setBackgroundColour(Color.red);
		this.setWidth((GraphConfig.MAIN_WINDOW_WIDTH));
		this.setHeight((GraphConfig.MAIN_WINDOW_HEIGHT));
		mathToolInitialiser = new MathToolInitialiser(this);

		assignmentHandler = new AssignmentHandler(this);
		assignmentHandler.setAssignment(new Assignment(UUID.randomUUID()
				.toString()));

		closeButton.removeButtonListeners();
		closeButton.addButtonListener(new SimpleButtonAdapter() {
			
			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				final MTMessageBox msg = new MTMessageBox(MathTool.this,
						contentSystem);
				msg.setTitle("Close");
				msg.setMessage("Are you sure you want to \nclose the math tool?");
				msg.addMessageListener(new MessageListener() {
					@Override
					public void buttonClicked(String buttonId) {
					}
					
					@Override
					public void buttonReleased(String buttonId) {
						msg.close();
						if (buttonId.equals("OK")) {
							MathTool.this.terminate();
							for (MathToolListener listener : mathToolListeners) {
								listener.mathPadClosed(MathTool.this);
							}
						}
					}
				});
			}
		});

		// Add control panel
		controlPanel = new MathToolControlPanel(contentSystem, this);
		controlPanel
				.addControlPanelListener(new ControlPanelListenerImpl(this));

		// Add sketch pad
		currentMathPad = (MathPad) contentSystem
				.createContentItem(MathPad.class);
		currentMathPad.setBorderSize(2);
		currentMathPad.setBorderColour(Color.black);
		currentMathPad.setWidth(this.getWindow().getWidth()
				- controlPanel.getWidth()
				- (4 * this.getWindow().getBorderSize()));
		currentMathPad.setHeight(controlPanel.getHeight());

		/*
		 * currentMathPad.setMathEngineEnabled(true);
		 * currentMathPad.addMathHandwritingListener(new
		 * MathHandwritingListener(){
		 * @Override public void expressionsWritten(List<Expression>
		 * expressions) { for(Expression exp: expressions){ if(exp != null)
		 * System.out.println("Expression: "+exp.getTeX());
		 * System.out.println("exp.getArea() :" + exp.getArea());
		 * System.out.println("exp.getAscent() :" + exp.getAscent());
		 * System.out.println("exp.getAspectRatio() :" + exp.getAspectRatio());
		 * System.out.println("exp.getBaseline() :" + exp.getBaseline());
		 * System.out.println("exp.getCx() :" + exp.getCx());
		 * System.out.println("exp.getCy() :" + exp.getCy());
		 * System.out.println("exp.getLx() :" + exp.getLx());
		 * System.out.println("exp.getLy() :" + exp.getLy());
		 * System.out.println("exp.getHeight() :" + exp.getHeight());
		 * System.out.println("exp.getWidth() :" + exp.getWidth());
		 * System.out.println("exp.getUx() :" + exp.getUx());
		 * System.out.println("exp.getUy() :" + exp.getUy());
		 * System.out.println("--------------------------------------"); } } });
		 */

		this.mathPadList.add(currentMathPad);

		// Add page number label
		pageNoLabel = (TextLabel) contentSystem
				.createContentItem(TextLabel.class);
		pageNoLabel.setText("Page " + (padIndex + 1));
		pageNoLabel.setFont(new Font("Times New Roman", Font.PLAIN, 10));
		pageNoLabel.setBorderSize(0);
		pageNoLabel.setTextColour(Color.black);
		pageNoLabel.setBackgroundColour(currentMathPad.getBackgroundColour());
		pageNoLabel.setLocalLocation((this.getWindow().getWidth() / 2)
				- (pageNoLabel.getWidth() / 2)
				- (4 * this.getWindow().getBorderSize()) - 5, (-this
				.getWindow().getHeight() / 2)
				+ (pageNoLabel.getHeight() / 2)
				+ (3 * this.getWindow().getBorderSize()));
		this.getWindow().addSubItem(pageNoLabel);
		// Add separator
		separatorBtn = (SimpleButton) contentSystem
				.createContentItem(SimpleButton.class);
		separatorBtn.setAutoFitSize(false);
		separatorBtn.setWidth(20);
		separatorBtn.setHeight(20);
		separatorBtn
				.setBackgroundColour(this.getWindow().getBackgroundColour());
		separatorBtn.setBorderSize(0);
		separatorBtn.addButtonListener(new SimpleButtonAdapter() {
			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				if (controlPanel.getContentPanel().isVisible()) {
					separatorState = SeparatorState.EXPANDED;
				} else {
					separatorState = SeparatorState.COLLAPSED;
				}
				setSeparatorState(separatorState);
				for (MathToolListener listener : mathToolListeners) {
					listener.separatorChanged(separatorState);
				}
			}
		});
		this.getWindow().addSubItem(controlPanel.getContentPanel());
		this.getWindow().addSubItem(currentMathPad);
		this.getWindow().addSubItem(separatorBtn);
		setSeparatorState(separatorState);
		answerPad = new AnswerDialog(contentSystem, this);
		answerPad.setVisible(false);
		loginDialog = new LoginDialog(contentSystem, this);
		loginDialog.setVisible(false);
		this.getWindow().setScaleLimit(0.6f, 1.5f);
	}
	
	/**
	 * Adds the math tool listener.
	 *
	 * @param listener
	 *            the listener
	 */
	public void addMathToolListener(MathToolListener listener) {
		if (!mathToolListeners.contains(listener)) {
			mathToolListeners.add(listener);
		}
	}

	/**
	 * Adds the new pad.
	 *
	 * @return the math pad
	 */
	public MathPad addNewPad() {
		MathPad newPad = (MathPad) contentSystem
				.createContentItem(MathPad.class);
		newPad.setWidth(this.getWindow().getWidth() - controlPanel.getWidth()
				- (4 * this.getWindow().getBorderSize()));
		newPad.setHeight(controlPanel.getHeight());
		newPad.setBorderSize(2);
		newPad.setBorderColour(Color.black);
		newPad.addMathHandwritingListener(new MathHandwritingListener() {
			
			@Override
			public void expressionsWritten(List<Expression> expressions) {
				for (Expression exp : expressions) {
					if (exp != null) {
						System.out.println("Expression: "
								+ exp.getMathematica());
					}
				}
			}
		});
		newPad.setLineWidth(currentMathPad.getLineWidth());
		newPad.setBackgroundColour(currentMathPad.getBackgroundColour());
		newPad.setTextColor(currentMathPad.getTextColor());
		this.mathPadList.add(newPad);
		this.getWindow().addSubItem(newPad);
		this.currentMathPad = newPad;
		padIndex = mathPadList.indexOf(newPad);
		pageNoLabel.setText("Page " + (padIndex + 1));
		for (MathPad pad : mathPadList) {
			if (!pad.equals(newPad)) {
				pad.setVisible(false);
			}
		}
		newPad.setOrder(1000);
		pageNoLabel.setOrder(1000);
		setSeparatorState(this.separatorState);
		controlPanel.updateNextPreviousStatus();
		return newPad;
	}

	/**
	 * Gets the all pads.
	 *
	 * @return the all pads
	 */
	public List<MathPad> getAllPads() {
		return this.mathPadList;
	}

	/**
	 * Gets the answer dialog.
	 *
	 * @return the answer dialog
	 */
	public AnswerDialog getAnswerDialog() {
		return answerPad;
	}

	/**
	 * Gets the assignment handler.
	 *
	 * @return the assignment handler
	 */
	public AssignmentHandler getAssignmentHandler() {
		return this.assignmentHandler;
	}

	/**
	 * Gets the control panel.
	 *
	 * @return the control panel
	 */
	public MathToolControlPanel getControlPanel() {
		return controlPanel;
	}

	/**
	 * Gets the current assignment info.
	 *
	 * @return the current assignment info
	 */
	public AssignmentInfo getCurrentAssignmentInfo() {
		Assignment assignment = this.assignmentHandler.getAssignment();
		if (assignment == null) {
			return null;
		}
		AssignmentInfo info = new AssignmentInfo(assignment.getAssignmentId());
		info.setHandwritingResult(this.getAnswerDialog().getAnswerPad()
				.getDrawData());
		List<Expression> mathExpressions = this.getAnswerDialog()
				.getAnswerPad().getMathExpressions();
		List<String> strExpressions = new ArrayList<String>();
		for (Expression expression : mathExpressions) {
			strExpressions.add(expression.getTeX());
		}
		info.setExpressionResult(strExpressions);
		return info;
	}

	/**
	 * Gets the current pad.
	 *
	 * @return the current pad
	 */
	public MathPad getCurrentPad() {
		return currentMathPad;
	}

	/**
	 * Gets the current pad index.
	 *
	 * @return the current pad index
	 */
	protected int getCurrentPadIndex() {
		return this.padIndex;
	}
	
	/**
	 * Gets the current writing state.
	 *
	 * @return the current writing state
	 */
	public WritingState getCurrentWritingState() {
		return this.currentWritingState;
	}

	/**
	 * Gets the draw data.
	 *
	 * @return the draw data
	 */
	public HashMap<Integer, List<DrawData>> getDrawData() {
		HashMap<Integer, List<DrawData>> drawData = new HashMap<Integer, List<DrawData>>();
		for (int i = 0; i < this.mathPadList.size(); i++) {
			drawData.put(i, mathPadList.get(i).getDrawData());
		}
		return drawData;
	}

	/**
	 * Gets the inits the settings.
	 *
	 * @return the inits the settings
	 */
	public MathToolInitSettings getInitSettings() {
		return mathToolInitialiser.getInitSettings();
	}
	
	/**
	 * Inits the.
	 *
	 * @param settings
	 *            the settings
	 */
	public void init(MathToolInitSettings settings) {
		mathToolInitialiser.init(settings);
	}

	/**
	 * Next pad.
	 *
	 * @return the int
	 */
	protected int nextPad() {
		if (((padIndex + 1) > 0) && ((padIndex + 1) < mathPadList.size())) {
			padIndex++;
			mathPadList.get(padIndex).setVisible(true);
			this.currentMathPad = mathPadList.get(padIndex);
			for (int i = 0; i < mathPadList.size(); i++) {
				if (i != padIndex) {
					mathPadList.get(i).setVisible(false);
				} else {
					mathPadList.get(i).setOrder(1000);
				}
			}
			pageNoLabel.setOrder(1000);
			pageNoLabel.setText("Page " + (padIndex + 1));
		}
		controlPanel.updateNextPreviousStatus();
		return padIndex;
	}

	/**
	 * Previous pad.
	 *
	 * @return the int
	 */
	protected int previousPad() {
		if (((padIndex - 1) >= 0) && ((padIndex - 1) < mathPadList.size())) {
			padIndex--;
			mathPadList.get(padIndex).setVisible(true);
			this.currentMathPad = mathPadList.get(padIndex);
			for (int i = 0; i < mathPadList.size(); i++) {
				if (i != padIndex) {
					mathPadList.get(i).setVisible(false);
				} else {
					mathPadList.get(i).setOrder(1000);
				}
			}
			pageNoLabel.setOrder(1000);
			pageNoLabel.setText("Page " + (padIndex + 1));
		}
		controlPanel.updateNextPreviousStatus();
		return padIndex;
	}

	/**
	 * Removes the math tool listener.
	 *
	 * @param listener
	 *            the listener
	 */
	public void removeMathToolListener(MathToolListener listener) {
		mathToolListeners.remove(listener);
	}

	/**
	 * Removes the math tool listeners.
	 */
	public void removeMathToolListeners() {
		mathToolListeners.clear();
	}

	/**
	 * Removes the pad.
	 *
	 * @param padIndex
	 *            the pad index
	 */
	public void removePad(int padIndex) {
		if (mathPadList.size() == 1) {
			return;
		}
		if ((padIndex >= 0) && (padIndex < mathPadList.size())) {
			mathPadList.get(padIndex).removeDrawListeners();
			mathPadList.get(padIndex).removeItemListerners();
			mathPadList.get(padIndex).setVisible(false);
			mathPadList.remove(padIndex);
			if (padIndex > 0) {
				this.showPad(padIndex - 1);
			} else {
				this.showPad(0);
			}
		}
	}

	/**
	 * Sets the line width.
	 *
	 * @param lineWidth
	 *            the new line width
	 */
	public void setLineWidth(float lineWidth) {
		if (!(new Float(lineWidth).isNaN())) {
			for (MathPad pad : mathPadList) {
				pad.setLineWidth(lineWidth);
			}
			this.getControlPanel().setLineWidth(lineWidth);
		}
	}

	/**
	 * Sets the separator state.
	 *
	 * @param separatorState
	 *            the new separator state
	 */
	public void setSeparatorState(SeparatorState separatorState) {
		if (separatorState == SeparatorState.COLLAPSED) {
			controlPanel.getContentPanel().setVisible(true);
			controlPanel.getContentPanel().setLocalLocation(
					(-this.getWindow().getWidth() / 2)
							+ (controlPanel.getWidth() / 2)
							+ this.getWindow().getBorderSize(),
					((controlPanel.getHeight() / 2) - (this.getWindow()
							.getHeight() / 2))
							+ this.getWindow().getBorderSize());
			for (MathPad pad : mathPadList) {
				pad.setWidth(this.getWindow().getWidth()
						- controlPanel.getWidth()
						- (6 * this.getWindow().getBorderSize()));
				pad.setHeight(controlPanel.getHeight());
				pad.setSketchArea(new Rectangle(0, 0, pad.getWidth(), pad
						.getHeight()));
				pad.setLocalLocation(
						(controlPanel.getWidth() / 2)
								+ this.getWindow().getBorderSize(),
						((pad.getHeight() / 2) - (this.getWindow().getHeight() / 2))
								+ this.getWindow().getBorderSize());
			}
			separatorBtn.setText("<");
			separatorBtn
					.setLocalLocation(
							(controlPanel.getContentPanel().getLocalLocation().x
									+ (controlPanel.getWidth() / 2) + (separatorBtn
									.getWidth() / 2)) - 3,
							(currentMathPad.getLocalLocation().y + (currentMathPad
									.getHeight() / 2))
									- (separatorBtn.getHeight() / 2));
		} else {
			controlPanel.getContentPanel().setVisible(false);
			for (MathPad pad : mathPadList) {
				pad.setWidth(this.getWindow().getWidth()
						- (4 * this.getWindow().getBorderSize())
						- separatorBtn.getWidth());
				pad.setHeight(controlPanel.getHeight());
				pad.setSketchArea(new Rectangle(0, 0, pad.getWidth(), pad
						.getHeight()));
				pad.setLocalLocation(
						this.getWindow().getBorderSize(),
						((pad.getHeight() / 2) - (this.getWindow().getHeight() / 2))
								+ this.getWindow().getBorderSize());
			}
			separatorBtn.setText(">");
			separatorBtn
					.setLocalLocation(
							(-this.getWindow().getWidth() / 2)
									+ (4 * this.getWindow().getBorderSize()),
							(currentMathPad.getLocalLocation().y + (currentMathPad
									.getHeight() / 2))
									- (separatorBtn.getHeight() / 2));
		}
	}

	/**
	 * Sets the text color.
	 *
	 * @param color
	 *            the new text color
	 */
	public void setTextColor(Color color) {
		for (MathPad pad : mathPadList) {
			pad.setTextColor(color);
		}
		this.getControlPanel().setTextColor(color);
	}
	
	/**
	 * Sets the writing state.
	 *
	 * @param initWritingState
	 *            the new writing state
	 */
	public void setWritingState(WritingState initWritingState) {
		this.currentWritingState = initWritingState;
		if (initWritingState == WritingState.FREE_DRAW) {
			if (backTextColor == null) {
				backTextColor = currentMathPad.getTextColor();
			}
			for (MathPad pad : mathPadList) {
				pad.setTextColor(backTextColor);
			}
		} else if (initWritingState == WritingState.ERASER) {
			backTextColor = currentMathPad.getTextColor();
			for (MathPad pad : mathPadList) {
				pad.setTextColor(pad.getBackgroundColour());
			}
		}
		this.getControlPanel().setWritingState(initWritingState);
	}
	
	/**
	 * Show login dialog.
	 */
	protected void showLoginDialog() {
		loginDialog.setVisible(true);
	}

	/**
	 * Show pad.
	 *
	 * @param padIndex
	 *            the pad index
	 */
	public void showPad(int padIndex) {
		if ((padIndex >= 0) && (padIndex < mathPadList.size())) {
			for (int i = 0; i < mathPadList.size(); i++) {
				mathPadList.get(i).setVisible(false);
			}
			mathPadList.get(padIndex).setVisible(true);
			mathPadList.get(padIndex).setOrder(1000);
			pageNoLabel.setOrder(1000);
			pageNoLabel.setText("Page " + (padIndex + 1));
			this.currentMathPad = mathPadList.get(padIndex);
			this.padIndex = padIndex;
			controlPanel.updateNextPreviousStatus();
		}
	}
	
	/**
	 * Terminate.
	 */
	public void terminate() {
		answerPad.close();
		loginDialog.close();
		MathTool.this.close();
	}
}
