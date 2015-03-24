package apps.threedinteraction.calculator;

import synergynetframework.jme.cursorsystem.elements.threed.ControlPointRotateTranslateScale;

import apps.threedinteraction.button.EdittableButtonNode;
import apps.threedinteraction.button.KeyListener;

import com.jme.scene.Node;

import data.DataResources;



/**
 * The Class MenuNode.
 */
public class MenuNode extends Node {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2429175967783608868L;
	
	/** The text string. */
	protected String textString="";
	
	/**
	 * Instantiates a new menu node.
	 *
	 * @param name the name
	 */
	public MenuNode(String name){
		super(name);	
		init();
		
	}

	/**
	 * Inits the.
	 */
	protected void init(){
		
		float width = 20f;
		float length = 40f;
		float height = 2f;
		
		float buttonWidth = 18f;
		float buttonLength = 6f;
		float buttonHeight = 6f;
			
		ControllerBodyNode bn = new ControllerBodyNode("Body"+name, width/2, length/2, height/2, 0.1f, 
				DataResources.class.getResource("diamondranking/imagetest/image/body.png"));	
		this.attachChild(bn);
			
		float buttonZ= bn.getLocalTranslation().z+height;
		//float horizontalSpace = (width-buttonWidth*4)/7;
		float verticalSpace = (length - buttonLength*1.5f - buttonLength*4f)/9f+1f;
		
		
		EdittableButtonNode buttonNode0 = new EdittableButtonNode("0"+name, buttonWidth/2,buttonLength/2, buttonHeight/2, 0.05f, 
				DataResources.class.getResource("diamondranking/imagetest/image/buttonBg.png"), "editable button");
		buttonNode0.setLocalTranslation(0, 2*verticalSpace+buttonLength/2-length/2, buttonZ);
		buttonNode0.addKeyListener(new KeyListener(){
			@Override
			public void keyPressed(String key) {
				textString="0";
			}			
		});
		this.attachChild(buttonNode0);
		
		EdittableButtonNode buttonNode1 = new EdittableButtonNode("1"+name, buttonWidth/2,buttonLength/2, buttonHeight/2, 0.05f, 
				DataResources.class.getResource("diamondranking/imagetest/image/buttonBg.png"), "editable button");
		buttonNode1.setLocalTranslation(0, 3*verticalSpace+buttonLength+buttonLength/2-length/2, buttonZ);
		buttonNode1.addKeyListener(new KeyListener(){
			@Override
			public void keyPressed(String key) {
				textString="0";
			}			
		});
		this.attachChild(buttonNode1);
		
		EdittableButtonNode buttonNode2 = new EdittableButtonNode("2"+name, buttonWidth/2,buttonLength/2, buttonHeight/2, 0.05f, 
				DataResources.class.getResource("diamondranking/imagetest/image/buttonBg.png"), "editable button");
		buttonNode2.setLocalTranslation(0, 4*verticalSpace+2*buttonLength+buttonLength/2-length/2, buttonZ);
		buttonNode2.addKeyListener(new KeyListener(){
			@Override
			public void keyPressed(String key) {
				textString="0";
			}			
		});
		this.attachChild(buttonNode2);
		
	
		
		
		ControlPointRotateTranslateScale cprts = new ControlPointRotateTranslateScale(bn, this);
		cprts.setPickMeOnly(true);
		
	}
	
}
