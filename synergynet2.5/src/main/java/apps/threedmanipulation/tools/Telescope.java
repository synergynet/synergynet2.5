package apps.threedmanipulation.tools;

import java.util.ArrayList;
import java.util.List;

import synergynetframework.appsystem.contentsystem.ContentSystem;

import apps.threedmanipulation.listener.ToolListener;

import com.jme.image.Texture;
import com.jme.image.Texture2D;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.TextureRenderer;
import com.jme.scene.CameraNode;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;

public class Telescope {

	private CameraNode camNode;

	private TextureRenderer tRenderer;
	private Texture2D fakeTex;
	private TelescopeLen telescopeLen;
	protected List<ToolListener> toolListeners = new ArrayList<ToolListener>();  
	private Node worldNode;
	private Node orthoNode;
	
	public void cleanup() {
		tRenderer.cleanup();
		worldNode.detachChild(camNode);
		worldNode.updateGeometricState(0f, false);
		orthoNode.detachChild(telescopeLen);
		orthoNode.updateGeometricState(0f, false);
	}

	public void render(Node renderedNode) {
		tRenderer.render(renderedNode, fakeTex);
	}
	
	public Telescope(String name, ContentSystem contentSystem, Node worldNode, Node orthoNode, List<Spatial> manipulatableOjbects, Vector3f mainCameraPosition, Vector2f initTelescopePosition, float initTelescopeZoom, float telescopeRadius){
		
		this.worldNode = worldNode;
		this.orthoNode = orthoNode;
		
		tRenderer = DisplaySystem.getDisplaySystem().createTextureRenderer(256, 256, TextureRenderer.Target.Texture2D);
		camNode = new CameraNode(name+"Camera Node", tRenderer.getCamera());
		tRenderer.getCamera().lookAt(new Vector3f(), new Vector3f(0, 0,-1));	
		tRenderer.getCamera().update();
		
		camNode.setLocalTranslation(mainCameraPosition);
		camNode.getCamera().setFrustumNear(initTelescopeZoom);
		camNode.updateGeometricState(0, true);
		camNode.updateRenderState();	
		worldNode.attachChild(camNode);
			 
		telescopeLen = new TelescopeLen(name+"telescopeLen", contentSystem, telescopeRadius, camNode, manipulatableOjbects);
		orthoNode.attachChild(telescopeLen);
		telescopeLen.setLocalTranslation(initTelescopePosition.x, initTelescopePosition.y, 0);
		telescopeLen.addToolListener(new ToolListener(){
			@Override
			public void disposeTool(float x, float y) {
				for (ToolListener l: toolListeners){
					l.disposeTool(x, y);
				}
			}		
		});
		
		tRenderer.setBackgroundColor(new ColorRGBA(1f, 0f, 0f, 1f));
		fakeTex = new Texture2D();
		fakeTex.setRenderToTextureType(Texture.RenderToTextureType.RGBA);
		tRenderer.setupTexture(fakeTex);
		TextureState screen = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		screen.setTexture(fakeTex);
		screen.setEnabled(true);	
		
		telescopeLen.getScreenDisk().setRenderState(screen);
		
		Vector3f cursorWorldStart = DisplaySystem.getDisplaySystem().getWorldCoordinates(initTelescopePosition, 0);
		Vector3f cursorWorldEnd = DisplaySystem.getDisplaySystem().getWorldCoordinates(initTelescopePosition, 1);					
		Vector3f direction = cursorWorldEnd.subtract(cursorWorldStart);
		camNode.lookAt(direction.mult(10), new Vector3f(0, 1,0));		
		
		orthoNode.updateGeometricState(0f, false);
		orthoNode.updateRenderState();
		
		
	}
	
	public void addToolListener(ToolListener l){
		toolListeners.add(l);
	}

	public void removeToolListener(ToolListener l){
		if (toolListeners.contains(l))
			toolListeners.remove(l);
	}
	
	public TelescopeLen getTelescopeLens(){
		return this.telescopeLen;
	}

}
