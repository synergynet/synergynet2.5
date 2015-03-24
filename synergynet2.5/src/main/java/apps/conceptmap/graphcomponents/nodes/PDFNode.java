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

package apps.conceptmap.graphcomponents.nodes;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.PDFViewer;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;
import apps.conceptmap.GraphConfig;
import apps.conceptmap.utility.GraphManager;

/**
 * The Class PDFNode.
 */
public class PDFNode extends DocNode {
	
	/** The previous page. */
	protected SimpleButton nextPage, previousPage;

	/** The pdf. */
	protected PDFViewer pdf;

	/**
	 * Instantiates a new PDF node.
	 *
	 * @param contentSystem
	 *            the content system
	 * @param gManager
	 *            the g manager
	 */
	public PDFNode(ContentSystem contentSystem, GraphManager gManager) {
		super(contentSystem, gManager);
		pdf = (PDFViewer) this.contentSystem.createContentItem(PDFViewer.class);
		super.setNodeContent(pdf);
		nextPage = this
				.createButtonWithImage(GraphConfig.nodeNextPageImageResource);
		previousPage = this
				.createButtonWithImage(GraphConfig.nodePreviousPageImageResource);
		container.addSubItem(nextPage);
		container.addSubItem(previousPage);
		nextPage.setOrder(2);
		previousPage.setOrder(2);
		nextPage.addButtonListener(new SimpleButtonAdapter() {
			
			@Override
			public void buttonClicked(SimpleButton b, long id, float x,
					float y, float pressure) {
				if (pdf.getCurrentPageIndex() < (pdf.getPageCount() - 1)) {
					pdf.nextPage();
				}
			}
		});
		previousPage.addButtonListener(new SimpleButtonAdapter() {
			
			@Override
			public void buttonClicked(SimpleButton b, long id, float x,
					float y, float pressure) {
				if (pdf.getCurrentPageIndex() > 0) {
					pdf.previousPage();
				}
			}
		});
	}

	/**
	 * Gets the next page button.
	 *
	 * @return the next page button
	 */
	public SimpleButton getNextPageButton() {
		return nextPage;
	}

	/**
	 * Gets the pdf viewer.
	 *
	 * @return the pdf viewer
	 */
	public PDFViewer getPdfViewer() {
		return pdf;
	}

	/**
	 * Gets the previous page button.
	 *
	 * @return the previous page button
	 */
	public SimpleButton getPreviousPageButton() {
		return previousPage;
	}

	/**
	 * Sets the next page button location.
	 *
	 * @param location
	 *            the new next page button location
	 */
	public void setNextPageButtonLocation(String location) {
		if (location != null) {
			this.positionButton(nextPage, pdf, location);
		}
	}

	/**
	 * Sets the previous page button location.
	 *
	 * @param location
	 *            the new previous page button location
	 */
	public void setPreviousPageButtonLocation(String location) {
		if (location != null) {
			this.positionButton(previousPage, pdf, location);
		}
	}
}
