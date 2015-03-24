package synergynetframework.appsystem.contentsystem.items;

import javax.swing.JDesktopPane;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.ISwingContainerImplementation;

/**
 * The Class SwingContainer.
 */
public class SwingContainer extends Window implements
		ISwingContainerImplementation {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8809882603380004248L;
	
	/**
	 * Instantiates a new swing container.
	 *
	 * @param contentSystem
	 *            the content system
	 * @param name
	 *            the name
	 */
	public SwingContainer(ContentSystem contentSystem, String name) {
		super(contentSystem, name);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .ISwingContainerImplementation#getJDesktopPane()
	 */
	@Override
	public JDesktopPane getJDesktopPane() {
		return ((ISwingContainerImplementation) this.contentItemImplementation)
				.getJDesktopPane();
	}
	
}
