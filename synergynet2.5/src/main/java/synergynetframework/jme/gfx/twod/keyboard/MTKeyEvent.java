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

package synergynetframework.jme.gfx.twod.keyboard;

/**
 * The Class MTKeyEvent.
 */
public class MTKeyEvent {

	/** The Constant PRESSED. */
	public static final int PRESSED = 0;

	/** The Constant RELEASED. */
	public static final int RELEASED = 1;

	/** The key. */
	protected int key;

	/** The key text. */
	protected String keyText;

	/** The modifiers. */
	protected int modifiers;

	/** The source. */
	protected MTKeyboard source;

	/** The type. */
	protected int type;
	
	/**
	 * Instantiates a new MT key event.
	 *
	 * @param source
	 *            the source
	 * @param keyType
	 *            the key type
	 * @param keyCode
	 *            the key code
	 * @param keyText
	 *            the key text
	 * @param modifiers
	 *            the modifiers
	 */
	public MTKeyEvent(MTKeyboard source, int keyType, int keyCode,
			String keyText, int modifiers) {
		this.source = source;
		this.key = keyCode;
		this.type = keyType;
		this.keyText = keyText;
		this.modifiers = modifiers;
	}
	
	/**
	 * Gets the key.
	 *
	 * @return the key
	 */
	public int getKey() {
		return key;
	}
	
	/**
	 * Gets the key text.
	 *
	 * @return the key text
	 */
	public String getKeyText() {
		return keyText;
	}
	
	/**
	 * Gets the modifiers.
	 *
	 * @return the modifiers
	 */
	public int getModifiers() {
		return modifiers;
	}
	
	/**
	 * Gets the source.
	 *
	 * @return the source
	 */
	public MTKeyboard getSource() {
		return source;
	}
	
	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	
}
