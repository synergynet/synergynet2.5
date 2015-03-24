package apps.threedbuttonsexperiment.calculator.button;

import java.awt.event.KeyEvent;

/**
 * The listener interface for receiving key events. The class that is interested
 * in processing a key event implements this interface, and the object created
 * with that class is registered with a component using the component's
 * <code>addKeyListener<code> method. When
 * the key event occurs, that object's appropriate
 * method is invoked.
 *
 * @see KeyEvent
 */
public interface KeyListener {

	/**
	 * Key pressed.
	 *
	 * @param key
	 *            the key
	 */
	public void keyPressed(String key);
}
