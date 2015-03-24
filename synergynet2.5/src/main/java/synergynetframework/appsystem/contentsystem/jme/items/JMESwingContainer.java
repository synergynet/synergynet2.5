package synergynetframework.appsystem.contentsystem.jme.items;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.logging.Logger;

import javax.swing.JDesktopPane;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.Frame;
import synergynetframework.appsystem.contentsystem.items.SwingContainer;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.ISwingContainerImplementation;
import synergynetframework.appsystem.contentsystem.items.listener.ItemListener;

import com.jme.input.InputHandler;
import com.jme.scene.Spatial;
import com.jme.system.DisplaySystem;
import com.jmex.awt.swingui.JMEDesktop;

/**
 * The Class JMESwingContainer.
 */
public class JMESwingContainer extends JMEWindow implements
		ISwingContainerImplementation {
	
	/** The Constant log. */
	private static final Logger log = Logger.getLogger(JMESwingContainer.class
			.getName());

	/** The desktop. */
	protected JMEDesktop desktop;

	/** The handler. */
	protected InputHandler handler;

	/** The robot. */
	protected Robot robot;

	/** The swing container. */
	protected SwingContainer swingContainer;

	/** The height. */
	protected int width = 300, height = 300;

	/**
	 * Instantiates a new JME swing container.
	 *
	 * @param contentItem
	 *            the content item
	 */
	public JMESwingContainer(ContentItem contentItem) {
		super(contentItem);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .ISwingContainerImplementation#getJDesktopPane()
	 */
	@Override
	public JDesktopPane getJDesktopPane() {
		return desktop.getJDesktop();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMEWindow#init()
	 */
	@Override
	public void init() {
		this.swingContainer = (SwingContainer) contentItem;
		try {
			robot = new Robot();
		} catch (AWTException e1) {
			log.warning("Failed to create robot caused by AWTException\n"
					+ e1.toString());
		}
		backgroundFrame = (Frame) contentItem.getContentSystem()
				.createContentItem(Frame.class);
		backgroundFrame.setVisible(false, false);
		backgroundFrame.setWidth(width);
		backgroundFrame.setHeight(height);
		swingContainer.addSubItem(backgroundFrame);
		handler = new InputHandler();
		desktop = new JMEDesktop(node.getName() + "_jmedesktop", width, height,
				handler);
		node.attachChild(desktop);
		node.setCullHint(Spatial.CullHint.Never);
		node.setLightCombineMode(Spatial.LightCombineMode.Off);
		DisplaySystem.getDisplaySystem().getRenderer().draw(node);
		node.updateGeometricState(0, true);
		node.updateRenderState();
		backgroundFrame.addItemListener(new ItemListener() {
			
			@Override
			public void cursorChanged(ContentItem item, long id, float x,
					float y, float pressure) {
				
			}
			
			@Override
			public void cursorClicked(ContentItem item, long id, float x,
					float y, float pressure) {
			}
			
			@Override
			public void cursorDoubleClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				
			}
			
			@Override
			public void cursorLongHeld(ContentItem item, long id, float x,
					float y, float pressure) {
				
			}
			
			@Override
			public void cursorPressed(ContentItem item, long id, float x,
					float y, float pressure) {
				robot.mousePress(InputEvent.BUTTON1_MASK);
			}
			
			@Override
			public void cursorReleased(ContentItem item, long id, float x,
					float y, float pressure) {
				robot.mouseRelease(InputEvent.BUTTON1_MASK);
			}
			
			@Override
			public void cursorRightClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				
			}
			
		});
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMEOrthoContentItem
	 * #update(float)
	 */
	@Override
	public void update(float tpf) {
		DisplaySystem.getDisplaySystem().getRenderer().draw(node);
		handler.update(tpf);
		node.updateGeometricState(0, true);
		super.update(tpf);
	}
}
