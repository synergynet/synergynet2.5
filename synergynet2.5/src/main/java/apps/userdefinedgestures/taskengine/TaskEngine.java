package apps.userdefinedgestures.taskengine;

import java.util.List;

import apps.userdefinedgestures.object.TargetObjectCollection;
import apps.userdefinedgestures.transform.RotateTransformer;
import apps.userdefinedgestures.transform.ScaleTransformer;
import apps.userdefinedgestures.transform.Transformer;
import apps.userdefinedgestures.transform.TranslationTransformer;
import apps.userdefinedgestures.util.Label;
import apps.userdefinedgestures.util.Utility;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.pass.DirectionalShadowMapPass;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.Spatial.CullHint;


public class TaskEngine {

	private RotateTransformer rotateTransformer = new RotateTransformer();
	private TranslationTransformer translationTransformer = new TranslationTransformer();
	private ScaleTransformer scaleTransformer = new ScaleTransformer();
	
	private TargetObjectCollection targetObjectCollection;
	private Camera cam;
	private Node orthoNode;
	private List<String> taskList;
	
	private int currentIndex = -1;	
	private Spatial currentObject;
	private Transformer currentTransformer;
	private Label taskLabel;
	private Label completeLabel;
	private Label startLabel;
	private String currentAction;
	
	
	public TaskEngine(Node worldNode, Node orthoNode, DirectionalShadowMapPass sPass, Camera cam, List<String> taskList){
		
		targetObjectCollection = new TargetObjectCollection(worldNode, sPass);
		this.cam = cam;
		this.orthoNode = orthoNode;
		this.taskList = taskList;
		
		taskLabel = new Label(worldNode, "");
		
		completeLabel= new Label(worldNode, "");
		completeLabel.setSize(4);
		completeLabel.setPosition(new Vector3f(150, 350, 0));
		
		completeLabel.setText("Finished, Thanks!!!");
		completeLabel.setVisibility(false);
		
		startLabel= new Label(worldNode, "");
		startLabel.setSize(3);
		startLabel.setPosition(new Vector3f(130, 450, 0));
		startLabel.setVisibility(true);
		startLabel.setText("Press key 'K' to start...");
	
	}
	
	public Transformer getCurrentTransformer(){
		return this.currentTransformer;
	}	
	
	public boolean moveToNextTask(){
		
		completeLabel.setVisibility(false);
		startLabel.setVisibility(false);
		
		if (currentIndex+1>=taskList.size()){
			completeLabel.setVisibility(true);
			return false;
		}
		else{
			currentIndex++;		
			setTask();
			taskLabel.setText("Task "+(currentIndex+1)+" - "+currentAction);
			this.currentTransformer.setActive(true);
			return true;
		}
	}
	
	public boolean moveToPreviousTask(){
		if (currentIndex-1<0)
			return false;
		else{
			currentIndex--;
			setTask();
			this.currentTransformer.setActive(true);
			return true;
		}
	}
	
	public void resetPosition(){
		if (this.currentObject!=null)
			this.currentObject.setLocalTranslation(0, 7, 0);
		
	}
	
	private void setTask(){
		String task = taskList.get(currentIndex);
		String[] taskAttributes = task.trim().split(",");
		
		currentObject = targetObjectCollection.getObject(taskAttributes[0]);
		resetPosition();
		
		//set object
		targetObjectCollection.hideAllObjects();
		currentObject.setCullHint(CullHint.Never);
		
		//set viewport
		if (taskAttributes[1].equals("front"))
			Utility.setViewPort(cam, Utility.ViewPort.FRONT);
		else
			Utility.setViewPort(cam, Utility.ViewPort.TOPDOWN);
		
		//set transformer
		
		if (taskAttributes[2].equals("moveright")){
			translationTransformer = new TranslationTransformer();
			translationTransformer.setObject(currentObject);
			translationTransformer.SetTargetPosition(TranslationTransformer.RIGHT);
			currentObject.addController(translationTransformer);
			currentTransformer = translationTransformer;
			
			currentAction = "Move Right";
		}
		else if (taskAttributes[2].equals("moveup")){
			translationTransformer = new TranslationTransformer();
			translationTransformer.setObject(currentObject);
			translationTransformer.SetTargetPosition(TranslationTransformer.UP);
			currentObject.addController(translationTransformer);
			currentTransformer = translationTransformer;
			
			currentAction = "Move Up";
		}
		else if (taskAttributes[2].equals("moveinside")){
			translationTransformer = new TranslationTransformer();
			translationTransformer.setObject(currentObject);
			translationTransformer.SetTargetPosition(TranslationTransformer.INSIDE);
			currentObject.addController(translationTransformer);
			currentTransformer = translationTransformer;
			
			currentAction = "Move Inside";
		}
		else if (taskAttributes[2].equals("moverightinside")){
			translationTransformer = new TranslationTransformer();
			translationTransformer.setObject(currentObject);
			translationTransformer.SetTargetPosition(TranslationTransformer.TOPRIGHTINSIDE);
			currentObject.addController(translationTransformer);
			currentTransformer = translationTransformer;
			
			currentAction = "Move Up Right (Inside)";
		}
		else if (taskAttributes[2].equals("moverightoutside")){
			translationTransformer = new TranslationTransformer();
			translationTransformer.setObject(currentObject);
			translationTransformer.SetTargetPosition(TranslationTransformer.TOPRIGHTOUTSIDE);
			currentObject.addController(translationTransformer);
			currentTransformer = translationTransformer;
			
			currentAction = "Move Up Right (Outside)";
		}
		else if (taskAttributes[2].equals("rotatex")){
			rotateTransformer = new RotateTransformer();
			rotateTransformer.setObject(currentObject);
			rotateTransformer.SetAxis(RotateTransformer.XAxis);
			currentObject.addController(rotateTransformer);
			currentTransformer = rotateTransformer;
			
			currentAction = "Rotate";
		}
		else if (taskAttributes[2].equals("rotatey")){
			rotateTransformer = new RotateTransformer();
			rotateTransformer.setObject(currentObject);
			rotateTransformer.SetAxis(RotateTransformer.YAxis);
			currentObject.addController(rotateTransformer);
			currentTransformer = rotateTransformer;
			
			currentAction = "Rotate";
		}
		else if (taskAttributes[2].equals("rotatez")){
			rotateTransformer = new RotateTransformer();
			rotateTransformer.setObject(currentObject);
			rotateTransformer.SetAxis(RotateTransformer.ZAxis);
			currentObject.addController(rotateTransformer);
			currentTransformer = rotateTransformer;
			
			currentAction = "Rotate";
		}
		else if (taskAttributes[2].equals("scaleup")){
			scaleTransformer = new ScaleTransformer();
			scaleTransformer.setObject(currentObject);
			scaleTransformer.SetDirection(ScaleTransformer.UP);
			currentObject.addController(scaleTransformer);
			currentTransformer = scaleTransformer;
			
			currentAction = "Scale Up";
		}
		else if (taskAttributes[2].equals("scaledown")){
			scaleTransformer = new ScaleTransformer();
			scaleTransformer.setObject(currentObject);
			scaleTransformer.SetDirection(ScaleTransformer.DOWN);
			currentObject.addController(scaleTransformer);
			currentTransformer = scaleTransformer;
			
			currentAction = "Scale Down";
		}
		
		this.orthoNode.updateRenderState();
		this.orthoNode.updateGeometricState(0, false);
				
	}
}
