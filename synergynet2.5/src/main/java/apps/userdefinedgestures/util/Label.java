package apps.userdefinedgestures.util;

import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Text;
import com.jme.scene.Spatial.CullHint;
import com.jme.scene.state.MaterialState;
import com.jme.system.DisplaySystem;

public class Label {

	protected Text text;
	protected Node worldNode;
	protected int size = 2;
	protected Vector3f position=new Vector3f(5,700,0);
	
	public Label(Node worldNode, String textString){
		
		this.worldNode = worldNode;
		
		setText(textString);
	}
	
	public void setText(String textString){
		if (text!=null)
			text.getParent().detachChild(text);
		
		text = Text.createDefaultTextLabel("text", textString);	 
	     text.setLocalTranslation(position);
	     text.setLocalScale(size);
	     
	     MaterialState materialState = DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
	     materialState.setAmbient(ColorRGBA.green);
	     materialState.setDiffuse(ColorRGBA.green);
	     materialState.setEnabled(true);
	     text.setRenderState(materialState);

	     worldNode.attachChild(text);
	     worldNode.updateGeometricState(0.0f, true);
	     worldNode.updateRenderState();
		
	}
	
	public void setSize(int size){
		this.size = size;
		worldNode.updateGeometricState(0.0f, true);
	}
	
	public void setPosition(Vector3f position){
		this.position = position;
		worldNode.updateGeometricState(0.0f, true);
	}
	
	public void setVisibility(boolean visiable){
		if (visiable){
			text.setCullHint(CullHint.Never);
		}
		else
		{
			text.setCullHint(CullHint.Always);
		}
		worldNode.updateRenderState();
	}
	
}
