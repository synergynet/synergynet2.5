/*
 * Copyright (c) 2008 University of Durham, England All rights reserved.
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

package synergynetframework.appsystem.contentsystem.jme.items;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IThreeDContainerImplementation;
import synergynetframework.appsystem.contentsystem.items.utils.Background;
import synergynetframework.appsystem.contentsystem.items.utils.Border;

import com.jme.scene.Node;
import com.jme.scene.Spatial;

/**
 * The Class JMEThreeDContainer.
 */
public class JMEThreeDContainer extends JMEThreeDContentItem implements
		IThreeDContainerImplementation {
	
	/** The node. */
	protected Node node;

	/**
	 * Instantiates a new JME three d container.
	 *
	 * @param contentItem
	 *            the content item
	 */
	public JMEThreeDContainer(ContentItem contentItem) {
		super(contentItem, new Node(contentItem.getName()));
		this.node = (Node) this.spatial;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IThreeDContainerImplementation#addSubItem(synergynetframework.appsystem.
	 * contentsystem.items.ContentItem)
	 */
	@Override
	public void addSubItem(ContentItem contentItem) {

		((Spatial) (contentItem.getImplementationObject())).removeFromParent();
		this.node
				.attachChild((Spatial) (contentItem.getImplementationObject()));
		
		this.node.updateGeometricState(0f, true);
		this.node.updateModelBound();
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .
	 * IThreeDContainerImplementation#removeSubItem(synergynetframework.appsystem
	 * .contentsystem.items.ContentItem)
	 */
	@Override
	public void removeSubItem(ContentItem contentItem) {
		Spatial parentSpatial = ((Spatial) (contentItem
				.getImplementationObject())).getParent();
		((Spatial) (contentItem.getImplementationObject())).removeFromParent();
		
		parentSpatial.updateGeometricState(0f, true);
		parentSpatial.updateModelBound();

	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMEContentItem#
	 * setBackGround
	 * (synergynetframework.appsystem.contentsystem.items.utils.Background)
	 */
	@Override
	public void setBackGround(Background backGround) {
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMEContentItem#
	 * setBorder(synergynetframework.appsystem.contentsystem.items.utils.Border)
	 */
	@Override
	public void setBorder(Border border) {
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMEContentItem#
	 * setBoundaryEnabled(boolean)
	 */
	@Override
	public void setBoundaryEnabled(boolean isBoundaryEnabled) {

	}
	
}
