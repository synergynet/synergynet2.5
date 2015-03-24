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

package synergynetframework.appsystem.contentsystem.items;

import java.util.ArrayList;
import java.util.List;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IRoundListContainerImplementation;

/**
 * The Class RoundListContainer.
 */
public class RoundListContainer extends RoundWindow implements
		IRoundListContainerImplementation {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4456222243914896154L;
	
	/** The list items. */
	protected List<RoundContentItem> listItems = new ArrayList<RoundContentItem>();

	/**
	 * Instantiates a new round list container.
	 *
	 * @param contentSystem
	 *            the content system
	 * @param name
	 *            the name
	 */
	public RoundListContainer(ContentSystem contentSystem, String name) {
		super(contentSystem, name);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IRoundListContainerImplementation#addSubItem(int,
	 * synergynetframework.appsystem.contentsystem.items.RoundContentItem)
	 */
	public void addSubItem(int index, RoundContentItem item) {
		if (!listItems.contains(item)) {
			listItems.add(index, item);
			super.addSubItem(item);
		}

		((IRoundListContainerImplementation) this.contentItemImplementation)
				.addSubItem(index, item);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .
	 * IRoundListContainerImplementation#addSubItem(synergynetframework.appsystem
	 * .contentsystem.items.RoundContentItem)
	 */
	public void addSubItem(RoundContentItem item) {
		if (!listItems.contains(item)) {
			listItems.add(item);
			super.addSubItem(item);
		}

		((IRoundListContainerImplementation) this.contentItemImplementation)
				.addSubItem(item);
	}

	/**
	 * Gets the list items.
	 *
	 * @return the list items
	 */
	public List<RoundContentItem> getListItems() {
		return listItems;
	}

	/**
	 * Removes the sub item.
	 *
	 * @param index
	 *            the index
	 */
	public void removeSubItem(int index) {
		if (listItems.size() > index) {
			super.removeSubItem(listItems.get(index));
			((IRoundListContainerImplementation) this.contentItemImplementation)
					.removeSubItem(listItems.get(index));
			listItems.remove(index);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .
	 * IRoundListContainerImplementation#removeSubItem(synergynetframework.appsystem
	 * .contentsystem.items.RoundContentItem)
	 */
	public void removeSubItem(RoundContentItem item) {
		if (listItems.contains(item)) {
			listItems.remove(item);
			super.removeSubItem(item);
		}

		((IRoundListContainerImplementation) this.contentItemImplementation)
				.removeSubItem(item);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IRoundListContainerImplementation#run()
	 */
	public void run() {
		((IRoundListContainerImplementation) this.contentItemImplementation)
				.run();
	}
	
}
