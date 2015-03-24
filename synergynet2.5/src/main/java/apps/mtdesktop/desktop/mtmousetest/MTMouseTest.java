package apps.mtdesktop.desktop.mtmousetest;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;

import synergynetframework.appsystem.services.net.rapidnetworkmanager.RapidNetworkManager;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.handlers.MessageProcessor;
import apps.mtdesktop.MTDesktopConfigurations;
import apps.mtdesktop.messages.MTMouseMessage;

/**
 * The Class MTMouseTest.
 */
public class MTMouseTest extends JDialog implements MessageProcessor {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5971578934608634199L;

	/** The Constant WINDOW_HEIGHT. */
	private static final int WINDOW_HEIGHT = 600;
	
	/** The Constant WINDOW_WIDTH. */
	private static final int WINDOW_WIDTH = 800; // pixels
	
	/** The images. */
	private List<ImageLabel> images = new ArrayList<ImageLabel>();
	
	/** The map panel. */
	private JPanel mapPanel = new JPanel(null);
	
	/**
	 * Instantiates a new MT mouse test.
	 */
	public MTMouseTest() {
		RapidNetworkManager.registerMessageProcessor(this);
		this.setModal(false);
		this.setTitle("Network Map");
		this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		
		ImageLabel star1 = new ImageLabel(
				MTDesktopConfigurations.class
						.getResource("mousetest/img01.jpg"));
		ImageLabel star2 = new ImageLabel(
				MTDesktopConfigurations.class
						.getResource("mousetest/img02.jpg"));
		ImageLabel star3 = new ImageLabel(
				MTDesktopConfigurations.class
						.getResource("mousetest/img03.jpg"));
		ImageLabel star4 = new ImageLabel(
				MTDesktopConfigurations.class
						.getResource("mousetest/img04.jpg"));
		
		mapPanel.add(star1);
		mapPanel.add(star2);
		mapPanel.add(star3);
		mapPanel.add(star4);
		
		images.add(star1);
		images.add(star2);
		images.add(star3);
		images.add(star4);
		
		this.add(mapPanel);
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int w = this.getSize().width;
		int h = this.getSize().height;
		int x = (dim.width - w) / 2;
		int y = (dim.height - h) / 2;
		
		this.setLocation(x, y);
		this.setVisible(true);
		
		WindowListener wndCloser = new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				RapidNetworkManager.removeMessageProcessor(MTMouseTest.this);
			}
		};
			addWindowListener(wndCloser);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.services.net.rapidnetworkmanager.handlers
	 * .MessageProcessor#process(java.lang.Object)
	 */
	@Override
	public void process(Object obj) {
		if (obj instanceof MTMouseMessage) {
			MTMouseMessage msg = (MTMouseMessage) obj;
			this.update(msg.getAngle(), msg.getScale());
		}
	}
	
	/**
	 * Update.
	 *
	 * @param angle
	 *            the angle
	 * @param scale
	 *            the scale
	 */
	public void update(float angle, float scale) {
		for (ImageLabel image : images) {
			image.notifyChange(-angle, scale);
		}
	}
	
}