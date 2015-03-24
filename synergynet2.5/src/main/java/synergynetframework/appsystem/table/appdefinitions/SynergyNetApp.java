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

package synergynetframework.appsystem.table.appdefinitions;

import java.io.File;
import java.util.logging.Logger;

import synergynetframework.appsystem.table.animationsystem.AnimationSystem;
import synergynetframework.appsystem.table.appregistry.ApplicationControlError;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.appsystem.table.appregistry.ApplicationRegistry;
import synergynetframework.appsystem.table.appregistry.ApplicationTaskManager;
import synergynetframework.appsystem.table.appregistry.menucontrol.ApplicationDefined;
import synergynetframework.appsystem.table.appregistry.menucontrol.MenuController;
import synergynetframework.config.display.DisplayConfigPrefsItem;
import synergynetframework.jme.cursorsystem.flicksystem.FlickSystem;
import synergynetframework.jme.pickingsystem.PickSystemException;
import synergynetframework.jme.sysutils.InputUtility;
import synergynetframework.jme.sysutils.StereoRenderPass;
import synergynetframework.jme.sysutils.StereoRenderPass.StereoMode;



import com.acarter.scenemonitor.SceneMonitor;
import com.jme.bounding.BoundingBox;
import com.jme.bounding.OrthogonalBoundingBox;
import com.jme.image.Texture;
import com.jme.input.InputHandler;
import com.jme.renderer.Camera;
import com.jme.renderer.Renderer;
import com.jme.renderer.pass.BasicPassManager;
import com.jme.renderer.pass.RenderPass;
import com.jme.scene.Node;
import com.jme.scene.Spatial.LightCombineMode;
import com.jme.scene.state.WireframeState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.util.geom.Debugger;
import com.jmex.game.state.GameState;

import core.SynergyNetDesktop;


/**
 * The Class SynergyNetApp.
 */
public abstract class SynergyNetApp extends GameState {
	
	/** The Constant log. */
	private static final Logger log = Logger.getLogger(SynergyNetApp.class.getName());	
	
	/** The input. */
	protected InputHandler input;	
	
	/** The root node. */
	private Node rootNode;
	
	/** The ortho node. */
	protected Node orthoNode;
	
	/** The world node. */
	protected Node worldNode;
	
	/** The activation count. */
	private int activationCount = 0;
	
	/** The info. */
	protected ApplicationInfo info;
	
	/** The application data directory. */
	protected File applicationDataDirectory;
	
	/** The wire state. */
	private WireframeState wireState;
	
	/** The p manager. */
	protected BasicPassManager pManager;
	
	/** The world render pass. */
	private RenderPass worldRenderPass;
	
	/** The ortho render pass. */
	private RenderPass orthoRenderPass;
	
	/** The exit state. */
	protected boolean exitState = false;
	
	/** The update scene monitor. */
	private boolean updateSceneMonitor = false;

	/**
	 * Instantiates a new synergy net app.
	 *
	 * @param info the info
	 */
	public SynergyNetApp(ApplicationInfo info) {
		super();
		this.info = info;
		this.applicationDataDirectory = new File(SynergyNetDesktop.getSynergyNetLocalWorkingDirectory(), info.getApplicationName() + "_" + info.getApplicationVersion());
		log.info("Create application Data Directory at: "+this.applicationDataDirectory.getAbsolutePath());
	}

	/**
	 * Inits the.
	 */
	public void init() {		
		initInput();
		pManager = new BasicPassManager();
		setWorldRenderPass(new RenderPass());
		setOrthoRenderPass(new RenderPass());
		
		setRootNode(new Node(name + ".rootnode"));
		wireState = DisplaySystem.getDisplaySystem().getRenderer().createWireframeState();
		wireState.setEnabled(false);
		getRootNode().setRenderState(wireState);
		
		
		getRootNode().setModelBound(new BoundingBox());
		getRootNode().updateModelBound();
		orthoNode = createOrthoNode();
		worldNode = createWorldNode();

		getRootNode().attachChild(orthoNode);
		getRootNode().attachChild(worldNode);
		getCamera();
		addContent();
		orthoNode.updateModelBound();
		getRootNode().updateGeometricState(0, true);
		getRootNode().updateRenderState();
		
		getWorldRenderPass().add(getRootNode());
		pManager.add(getWorldRenderPass());
		getOrthoRenderPass().add(getOrthoNode());
		pManager.add(getOrthoRenderPass());

		String threeDeeMode = new DisplayConfigPrefsItem().getThreeDee();
		
		if (!threeDeeMode.equals("NONE")){			
			StereoRenderPass sp = new StereoRenderPass(rootNode);
			sp.setStereoMode(StereoMode.valueOf(threeDeeMode));
			pManager.clearAll();
	        pManager.add(sp);
		}
		
	}

	/**
	 * Gets the ortho node.
	 *
	 * @return the ortho node
	 */
	public Node getOrthoNode() {
		return orthoNode;
	}

	/**
	 * Gets the world node.
	 *
	 * @return the world node
	 */
	public Node getWorldNode() {
		return worldNode;
	}

	/**
	 * Creates the world node.
	 *
	 * @return the node
	 */
	private Node createWorldNode() {
		Node n = new Node(info.getApplicationName() + "_worldNode");
		n.setModelBound(new BoundingBox());
		n.updateModelBound();
		ZBufferState buf = DisplaySystem.getDisplaySystem().getRenderer().createZBufferState();
		buf.setEnabled(true);
		buf.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
		n.setRenderState(buf);
		log.info("WorldNode is created");
		return n;
	}

	/**
	 * Creates the ortho node.
	 *
	 * @return the node
	 */
	private Node createOrthoNode() {
		DisplaySystem display = DisplaySystem.getDisplaySystem();
		Node n = new Node(info.getApplicationName() + "_orthoNode");
		n.setModelBound(new OrthogonalBoundingBox());
		n.updateModelBound();
		n.setRenderQueueMode(Renderer.QUEUE_ORTHO);
		n.setLightCombineMode(LightCombineMode.Off);
		ZBufferState buf = display.getRenderer().createZBufferState();
		buf.setEnabled(true);
		buf.setFunction(ZBufferState.TestFunction.NotEqualTo);		
		n.setRenderState(buf);		
		n.updateRenderState();
		n.updateGeometricState(0f, true);
		log.info("OrthoNode is created");
		return n;
	}



	/**
	 * On activate.
	 */
	public void onActivate() {
		setActivationCount(getActivationCount() + 1);
		DisplaySystem.getDisplaySystem().setTitle(name);
		DisplaySystem.getDisplaySystem().getRenderer().setCamera(getCamera());
		getCamera().update();
		
		DisplayConfigPrefsItem displayPrefs = new DisplayConfigPrefsItem();		
		if(displayPrefs.getShowSceneMonitor()) {
			SceneMonitor.getMonitor().registerNode(rootNode, "Root Node");
	        SceneMonitor.getMonitor().showViewer(true);
	        updateSceneMonitor  = true;
		}
		
		log.info("SynergyNet Applicaiton Instance '"+name+"' is activated ");
	}
	
	/**
	 * On deactivate.
	 */
	protected void onDeactivate() {		
		DisplayConfigPrefsItem displayPrefs = new DisplayConfigPrefsItem();
		if(displayPrefs.getShowSceneMonitor()) {
			SceneMonitor.getMonitor().unregisterNode(rootNode);
	        SceneMonitor.getMonitor().showViewer(false);
	        updateSceneMonitor = false;
		}
		
		log.info("SynergyNet Applicaiton Instance '"+name+"' is deactivated ");
	}

	/**
	 * Request focus.
	 */
	protected void requestFocus() {
		try {
			SynergyNetDesktop.getInstance().getPickSystem().setPickingRootNode(worldNode);
			SynergyNetDesktop.getInstance().getPickSystem().setOrthogonalPickingRoot(orthoNode);
		} catch (PickSystemException e) {
			
			//how to handle exception
			e.printStackTrace();
		}		
	}


	/**
	 * State render.
	 *
	 * @param tpf the tpf
	 */
	protected void stateRender(float tpf) {
	}
	
	/* (non-Javadoc)
	 * @see com.jmex.game.state.GameState#setActive(boolean)
	 */
	public void setActive(boolean active) {
		FlickSystem.setNetworkFlickMode(false);
		if (active) onActivate();
		else onDeactivate();
		super.setActive(active);
		requestFocus();
	}

	/* (non-Javadoc)
	 * @see com.jmex.game.state.GameState#update(float)
	 */
	public final void update(float tpf) {		
		stateUpdate(tpf);
		pManager.updatePasses(tpf);
		if(InputUtility.wireframeMode && !wireState.isEnabled()) {
			wireState.setEnabled(true);
			getRootNode().updateRenderState();
		}else if(!InputUtility.wireframeMode && wireState.isEnabled()) {
			wireState.setEnabled(false);
			getRootNode().updateRenderState();			
		}        
		
		AnimationSystem.getInstance().update(tpf);
		getRootNode().updateGeometricState(tpf, true);
		if(updateSceneMonitor) {
			SceneMonitor.getMonitor().updateViewer(tpf);
		}
		if (exitState){
			exitState = false;
			try {
				ApplicationTaskManager.getInstance().setActiveApplication(ApplicationRegistry.getInstance().getDefaultApp());
			} catch (ApplicationControlError e) {
				e.printStackTrace();
			}
		}
		menuController.update(tpf);
	}
	
	/**
	 * Exit app.
	 */
	public void exitApp(){
		exitState = true;
	}

	/* (non-Javadoc)
	 * @see com.jmex.game.state.GameState#render(float)
	 */
	public final void render(float tpf) {
		stateRender(tpf);
		renderPasses(tpf);
		renderDebug(tpf);
	}

	/**
	 * Render passes.
	 *
	 * @param tpf the tpf
	 */
	private void renderPasses(float tpf) {
		try{
			pManager.renderPasses(DisplaySystem.getDisplaySystem().getRenderer());
		}catch(IndexOutOfBoundsException e){
			log.severe("Exception occurs when rendering passes: \n"+e.toString());
		}
	}

	/**
	 * Render debug.
	 *
	 * @param tpf the tpf
	 */
	public void renderDebug(float tpf) {
		if(InputUtility.showBounds) Debugger.drawBounds(getRootNode(), DisplaySystem.getDisplaySystem().getRenderer(), true);
        if(InputUtility.showNormals) Debugger.drawNormals(getRootNode(), DisplaySystem.getDisplaySystem().getRenderer());		
        if(InputUtility.showDepth) {
            DisplaySystem.getDisplaySystem().getRenderer().renderQueue();
            Debugger.drawBuffer(Texture.RenderToTextureType.Depth, Debugger.NORTHEAST, DisplaySystem.getDisplaySystem().getRenderer());
        }		
	}

	/* (non-Javadoc)
	 * @see com.jmex.game.state.GameState#cleanup()
	 */
	@Override
	public void cleanup() {

	}

	/**
	 * Adds the content.
	 */
	public abstract void addContent();	

	/**
	 * Inits the input.
	 */
	protected abstract void initInput();
	
	/**
	 * Gets the camera.
	 *
	 * @return the camera
	 */
	protected abstract Camera getCamera();	
	
	/**
	 * State update.
	 *
	 * @param tpf the tpf
	 */
	protected abstract void stateUpdate(float tpf);

	/**
	 * Sets the activation count.
	 *
	 * @param activationCount the new activation count
	 */
	public void setActivationCount(int activationCount) {
		this.activationCount = activationCount;
	}

	/**
	 * Gets the activation count.
	 *
	 * @return the activation count
	 */
	public int getActivationCount() {
		return activationCount;
	}

	/**
	 * Gets the info.
	 *
	 * @return the info
	 */
	public ApplicationInfo getInfo() {
		return info;
	}

	/**
	 * Gets the application data directory.
	 *
	 * @return the application data directory
	 */
	public File getApplicationDataDirectory() {
		if(!applicationDataDirectory.exists()) applicationDataDirectory.mkdirs();	
		return applicationDataDirectory;
	}

	/**
	 * Sets the root node.
	 *
	 * @param rootNode the new root node
	 */
	public void setRootNode(Node rootNode) {
		this.rootNode = rootNode;
	}

	/**
	 * Gets the root node.
	 *
	 * @return the root node
	 */
	public Node getRootNode() {
		return rootNode;
	}

	/**
	 * Sets the ortho render pass.
	 *
	 * @param orthoRenderPass the new ortho render pass
	 */
	public void setOrthoRenderPass(RenderPass orthoRenderPass) {
		this.orthoRenderPass = orthoRenderPass;
	}

	/**
	 * Gets the ortho render pass.
	 *
	 * @return the ortho render pass
	 */
	public RenderPass getOrthoRenderPass() {
		return orthoRenderPass;
	}

	/**
	 * Sets the world render pass.
	 *
	 * @param worldRenderPass the new world render pass
	 */
	public void setWorldRenderPass(RenderPass worldRenderPass) {
		this.worldRenderPass = worldRenderPass;
	}

	/**
	 * Gets the world render pass.
	 *
	 * @return the world render pass
	 */
	public RenderPass getWorldRenderPass() {
		return worldRenderPass;
	}
	
	/** The menu controller. */
	protected MenuController menuController = new ApplicationDefined();
	
	/**
	 * Gets the menu controller.
	 *
	 * @return the menu controller
	 */
	public MenuController getMenuController() {
		return menuController;
	}
	
	/**
	 * Sets the menu controller.
	 *
	 * @param mc the new menu controller
	 */
	public void setMenuController(MenuController mc) {
		this.menuController = mc;
	}

}