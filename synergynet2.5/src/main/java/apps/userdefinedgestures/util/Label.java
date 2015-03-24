package apps.userdefinedgestures.util;

import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Text;
import com.jme.scene.Spatial.CullHint;
import com.jme.scene.state.MaterialState;
import com.jme.system.DisplaySystem;


/**
 * The Class Label.
 */
public class Label {

	/** The text. */
	protected Text text;
	
	/** The world node. */
	protected Node worldNode;
	
	/** The size. */
	protected int size = 2;
	
	/** The position. */
	protected Vector3f position=new Vector3f(5,700,0);
	
	/**
	 * Instantiates a new label.
	 *
	 * @param worldNode the world node
	 * @param textString the text string
	 */
	public Label(Node worldNode, String textString){
		
		this.worldNode = worldNode;
		
		setText(textString);
	}
	
	/**
	 * Sets the text.
	 *
	 * @param textString the new text
	 */
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
	
	/**
	 * Sets the size.
	 *
	 * @param size the new size
	 */
	public void setSize(int size){
		this.size = size;
		worldNode.updateGeometricState(0.0f, true);
	}
	
	/**
	 * Sets the position.
	 *
	 * @param position the new position
	 */
	public void setPosition(Vector3f position){
		this.position = position;
		worldNode.updateGeometricState(0.0f, true);
	}
	
	/**
	 * Sets the visibility.
	 *
	 * @param visiable the new visibility
	 */
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
