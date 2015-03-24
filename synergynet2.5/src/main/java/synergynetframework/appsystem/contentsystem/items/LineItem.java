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

package synergynetframework.appsystem.contentsystem.items;

import java.awt.Color;
import java.awt.Font;
import java.io.Serializable;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.ILineImplementation;
import synergynetframework.appsystem.contentsystem.items.utils.Location;

/**
 * The Class LineItem.
 */
public class LineItem extends OrthoContainer implements Serializable,
		ILineImplementation {
	
	/** The Constant ANIMATION. */
	public static final int ANIMATION = 3;
	
	/** The Constant ARROW_TO_SOURCE. */
	public static final int ARROW_TO_SOURCE = 2;
	
	/** The Constant ARROW_TO_TARGET. */
	public static final int ARROW_TO_TARGET = 1;

	/** The Constant BIDIRECTIONAL_ARROWS. */
	public static final int BIDIRECTIONAL_ARROWS = 0;

	/** The Constant CONNECTED_LINE. */
	public static final int CONNECTED_LINE = 1;

	/** The Constant DOTTED_LINE. */
	public static final int DOTTED_LINE = 2;

	/** The Constant NO_ARROWS. */
	public static final int NO_ARROWS = 3;

	/** The Constant SEGMENT_LINE. */
	public static final int SEGMENT_LINE = 0;

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The arrow mode. */
	protected int arrowMode = BIDIRECTIONAL_ARROWS;

	/** The arrows enabled. */
	protected boolean arrowsEnabled = true;

	/** The line colour. */
	protected Color lineColour = Color.white;

	/** The line mode. */
	protected int lineMode = CONNECTED_LINE;

	/** The line width. */
	protected float lineWidth = 1f;

	/** The target item. */
	protected ContentItem sourceItem, targetItem;

	/** The target location. */
	protected Location sourceLocation, targetLocation;

	/** The text. */
	protected StringBuffer text = new StringBuffer();

	/** The text colour. */
	protected Color textColour = Color.white;

	/** The text enabled. */
	protected boolean textEnabled = true;

	/** The text font. */
	protected Font textFont = new Font("Arial", Font.PLAIN, 16);

	/**
	 * Instantiates a new line item.
	 *
	 * @param contentSystem
	 *            the content system
	 * @param name
	 *            the name
	 */
	public LineItem(ContentSystem contentSystem, String name) {
		super(contentSystem, name);
	}

	/**
	 * Instantiates a new line item.
	 *
	 * @param contentSystem
	 *            the content system
	 * @param name
	 *            the name
	 * @param sourceLocation
	 *            the source location
	 * @param targetLocation
	 *            the target location
	 */
	public LineItem(ContentSystem contentSystem, String name,
			Location sourceLocation, Location targetLocation) {
		super(contentSystem, name);
		this.sourceLocation = sourceLocation;
		this.targetLocation = targetLocation;
	}

	/**
	 * Gets the arrow mode.
	 *
	 * @return the arrow mode
	 */
	public int getArrowMode() {
		return arrowMode;
	}

	/**
	 * Gets the line colour.
	 *
	 * @return the line colour
	 */
	public Color getLineColour() {
		return lineColour;
	}
	
	/**
	 * Gets the line mode.
	 *
	 * @return the line mode
	 */
	public int getLineMode() {
		return lineMode;
	}

	/**
	 * Gets the source item.
	 *
	 * @return the source item
	 */
	public ContentItem getSourceItem() {
		return sourceItem;
	}

	/**
	 * Gets the source location.
	 *
	 * @return the source location
	 */
	public Location getSourceLocation() {
		return sourceLocation;
	}

	/**
	 * Gets the target item.
	 *
	 * @return the target item
	 */
	public ContentItem getTargetItem() {
		return targetItem;
	}

	/**
	 * Gets the target location.
	 *
	 * @return the target location
	 */
	public Location getTargetLocation() {
		return targetLocation;
	}
	
	/**
	 * Gets the text.
	 *
	 * @return the text
	 */
	public String getText() {
		return text.toString();
	}

	/**
	 * Gets the text colour.
	 *
	 * @return the text colour
	 */
	public Color getTextColour() {
		return textColour;
	}

	/**
	 * Gets the text font.
	 *
	 * @return the text font
	 */
	public Font getTextFont() {
		return textFont;
	}

	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public float getWidth() {
		return lineWidth;
	}

	/**
	 * Checks if is annotation enabled.
	 *
	 * @return true, if is annotation enabled
	 */
	public boolean isAnnotationEnabled() {
		return textEnabled;
	}

	/**
	 * Checks if is arrow enabled.
	 *
	 * @return true, if is arrow enabled
	 */
	public boolean isArrowEnabled() {
		return arrowsEnabled;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .ILineImplementation#setAnnotationEnabled(boolean)
	 */
	public void setAnnotationEnabled(boolean isEnabled) {
		this.textEnabled = isEnabled;
		((ILineImplementation) this.contentItemImplementation)
				.setAnnotationEnabled(isEnabled);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .ILineImplementation#setArrowMode(int)
	 */
	public void setArrowMode(int arrowMode) {
		this.arrowMode = arrowMode;
		((ILineImplementation) this.contentItemImplementation)
				.setArrowMode(arrowMode);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .ILineImplementation#setArrowsEnabled(boolean)
	 */
	public void setArrowsEnabled(boolean isEnabled) {
		this.arrowsEnabled = isEnabled;
		((ILineImplementation) this.contentItemImplementation)
				.setArrowsEnabled(isEnabled);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .ILineImplementation#setLineColour(java.awt.Color)
	 */
	public void setLineColour(Color lineColour) {
		this.lineColour = lineColour;
		((ILineImplementation) this.contentItemImplementation)
				.setLineColour(lineColour);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .ILineImplementation#setLineMode(int)
	 */
	public void setLineMode(int lineMode) {
		this.lineMode = lineMode;
		((ILineImplementation) this.contentItemImplementation)
				.setLineMode(lineMode);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .
	 * ILineImplementation#setSourceItem(synergynetframework.appsystem.contentsystem
	 * .items.ContentItem)
	 */
	public void setSourceItem(ContentItem sourceItem) {
		this.sourceItem = sourceItem;
		((ILineImplementation) this.contentItemImplementation)
				.setSourceItem(sourceItem);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .ILineImplementation#setSourceLocation(synergynetframework.appsystem.
	 * contentsystem.items.utils.Location)
	 */
	public void setSourceLocation(Location sourceLocation) {
		this.sourceLocation = sourceLocation;
		((ILineImplementation) this.contentItemImplementation)
				.setSourceLocation(sourceLocation);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .
	 * ILineImplementation#setTargetItem(synergynetframework.appsystem.contentsystem
	 * .items.ContentItem)
	 */
	public void setTargetItem(ContentItem targetItem) {
		this.targetItem = targetItem;
		((ILineImplementation) this.contentItemImplementation)
				.setTargetItem(targetItem);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .ILineImplementation#setTargetLocation(synergynetframework.appsystem.
	 * contentsystem.items.utils.Location)
	 */
	public void setTargetLocation(Location targetLocation) {
		this.targetLocation = targetLocation;
		((ILineImplementation) this.contentItemImplementation)
				.setTargetLocation(targetLocation);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .ILineImplementation#setText(java.lang.String)
	 */
	public void setText(String text) {
		this.text.setLength(0);
		this.text.append(text);
		((ILineImplementation) this.contentItemImplementation).setText(text);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .ILineImplementation#setTextColour(java.awt.Color)
	 */
	public void setTextColour(Color textColour) {
		this.textColour = textColour;
		((ILineImplementation) this.contentItemImplementation)
				.setTextColour(textColour);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .ILineImplementation#setTextFont(java.awt.Font)
	 */
	public void setTextFont(Font textFont) {
		this.textFont = textFont;
		((ILineImplementation) this.contentItemImplementation)
				.setTextFont(textFont);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .ILineImplementation#setWidth(float)
	 */
	public void setWidth(float lineWidth) {
		this.lineWidth = lineWidth;
		((ILineImplementation) this.contentItemImplementation)
				.setWidth(lineWidth);
	}
}
