/* Copyright (c) 2008 University of Durham, England
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

package synergynetframework.appsystem.contentsystem.jme.items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.OrthoContainer;
import synergynetframework.appsystem.contentsystem.items.OrthoContentItem;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IOrthoContainerImplementation;
import synergynetframework.appsystem.contentsystem.items.utils.Background;
import synergynetframework.appsystem.contentsystem.items.utils.Border;
import synergynetframework.appsystem.contentsystem.jme.JMEContentSystem;



/**
 * The Class JMEOrthoContainer.
 */
public class JMEOrthoContainer extends JMEOrthoContentItem implements IOrthoContainerImplementation {

	/** The node. */
	protected Node node;
	
	/** The container item. */
	protected ContentItem containerItem;
	
	/**
	 * Instantiates a new JME ortho container.
	 *
	 * @param contentItem the content item
	 */
	public JMEOrthoContainer(ContentItem contentItem) {
		super(contentItem, new Node(contentItem.getName()));
		this.node = (Node)this.spatial;
		this.containerItem = contentItem;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.jme.items.JMEOrthoContentItem#setBackGround(synergynetframework.appsystem.contentsystem.items.utils.Background)
	 */
	@Override
	public void setBackGround(Background backGround) {	
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.jme.items.JMEOrthoContentItem#setBorder(synergynetframework.appsystem.contentsystem.items.utils.Border)
	 */
	@Override
	public void setBorder(Border border) {	
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IOrthoContainerImplementation#addSubItem(synergynetframework.appsystem.contentsystem.items.ContentItem)
	 */
	@Override
	public void addSubItem(ContentItem contentItem) {
		
		((Spatial)(contentItem.getImplementationObject())).removeFromParent();
		this.node.attachChild((Spatial)(contentItem.getImplementationObject()));
		
		((Spatial)(contentItem.getImplementationObject())).setZOrder(this.node.getZOrder());
		
		//ordering all the sub items
		if (this.contentItem instanceof OrthoContainer){
			((OrthoContainer)this.contentItem).updateOrder(0);
		}
				
		this.updateContainerGestureStatus();
		this.updateContainerListeners((OrthoContentItem)contentItem);
			
		this.node.updateGeometricState(0f, true);
		this.node.updateModelBound();
				
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IOrthoContainerImplementation#removeSubItem(synergynetframework.appsystem.contentsystem.items.ContentItem)
	 */
	@Override
	public void removeSubItem(ContentItem contentItem) {		
		Spatial parentSpatial = ((Spatial)(contentItem.getImplementationObject())).getParent();
		((Spatial)(contentItem.getImplementationObject())).removeFromParent();
			
		parentSpatial.updateGeometricState(0f, true);
		parentSpatial.updateModelBound();
		
		this.updateContainerGestureStatus();
		this.updateContainerListeners((OrthoContentItem)contentItem);
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IOrthoContainerImplementation#detachSubItem(synergynetframework.appsystem.contentsystem.items.ContentItem)
	 */
	@Override
	public void detachSubItem(ContentItem contentItem) {
		Spatial parentSpatial = ((Spatial)(contentItem.getImplementationObject())).getParent();
		Spatial childSpatial = (Spatial)(contentItem.getImplementationObject());
		Node orthoNode = ((Node)(((JMEContentSystem)contentItem.getContentSystem()).getOrthoRootNode()));
		float angle = childSpatial.getWorldRotation().toAngleAxis(new Vector3f(0,0,1));
		float scale = childSpatial.getWorldScale().x;
		if(orthoNode != null){
			//Vector3f loc = childSpatial.getWorldTranslation();
			orthoNode.attachChild((Spatial)(contentItem.getImplementationObject()));
			//childSpatial.setLocalTranslation(loc);
			contentItem.setAngle(angle);
			contentItem.setScale(scale);
		}
			
		parentSpatial.updateGeometricState(0f, true);
		parentSpatial.updateModelBound();
		orthoNode.updateGeometricState(0f, true);
		orthoNode.updateModelBound();
		
		this.updateContainerGestureStatus();
		this.updateContainerListeners((OrthoContentItem)contentItem);

		((OrthoContentItem)contentItem).setRotateTranslateScalable(((OrthoContentItem)contentItem).isRotateTranslateScaleEnabled());
		((OrthoContentItem)contentItem).setBringToTopable(((OrthoContentItem)contentItem).isBringToTopEnabled());
		((OrthoContentItem)contentItem).setSnapable(((OrthoContentItem)contentItem).isSnapEnabled());

	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IOrthoContainerImplementation#updateOrder(int)
	 */
	@Override
	public void updateOrder(int order) {
	
		List<ContentItem> contentItems = ((OrthoContainer)contentItem).getAllItemsIncludeSystemItems();
		List<OrthoContentItem> items = new ArrayList<OrthoContentItem>();
		for (ContentItem item:contentItems){
			items.add((OrthoContentItem)item);
		}
		
		Collections.sort(items, Collections.reverseOrder());
	
		for(int i=0; i<items.size(); i++){
			Spatial spatial = (Spatial)(items.get(i).getImplementationObject());
			if (this.node.getChildren().contains(spatial)){
				this.node.getChildren().remove(spatial);
			    this.node.getChildren().add(i, spatial);
			}
		}
		
		this.node.updateGeometricState(0f, true);
		this.node.updateModelBound();	

	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IOrthoContainerImplementation#getNode()
	 */
	@Override
	public Node getNode(){
		return node;
	}
}
