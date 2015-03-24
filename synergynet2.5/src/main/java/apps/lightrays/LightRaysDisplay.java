package apps.lightrays;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

import apps.lightrays.raytracer.RayTracerEngine;
import apps.lightrays.raytracer.scene.Camera;
import apps.lightrays.raytracer.scene.Colour;
import apps.lightrays.raytracer.scene.Light;
import apps.lightrays.raytracer.scene.Lighting;
import apps.lightrays.raytracer.scene.OpticalProperties;
import apps.lightrays.raytracer.scene.Point;
import apps.lightrays.raytracer.scene.Scene;
import apps.lightrays.raytracer.scene.Vector;
import apps.lightrays.raytracer.scene.geometry.Plane;
import apps.lightrays.raytracer.scene.geometry.Sphere;

import com.jme.scene.Spatial;
import com.jmex.awt.swingui.ImageGraphics;

import synergynetframework.appsystem.table.gfx.FullScreenCanvas;
import synergynetframework.jme.gfx.twod.DrawableSpatialImage;


/**
 * The Class LightRaysDisplay.
 */
public class LightRaysDisplay extends FullScreenCanvas implements DrawableSpatialImage {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8495609533882350013L;

	/** The gfx. */
	private ImageGraphics gfx;

	/** The tracer. */
	private RayTracerEngine tracer;
	
	/** The raytrace. */
	private Image raytrace;
	
	/** The buf_img. */
	private BufferedImage buf_img;
	
	/** The off_gfx. */
	private Graphics off_gfx;
	
	/** The info_font. */
	private Font info_font = new Font("Arial", Font.PLAIN, 8);
	
	/** The fm. */
	private FontMetrics fm;
	
	/** The messages. */
	private List<String> messages = new ArrayList<String>();

	/**
	 * Instantiates a new light rays display.
	 *
	 * @param name the name
	 */
	public LightRaysDisplay(String name) {
		super(name);
		gfx = getGraphics();
		
		Camera c = new Camera();
		c.setViewpoint(new Vector(0, 100, -600));
		
		Scene scene = new Scene();		
		scene.setMaxDepth(16);
		scene.setAmbientColour(new Colour(0.1,0.1,0.1));
		
		Light l = new Light(new Point(500, 1500, 0));
		l.setBrightness(0.5);
		l.setOpticProperties(new OpticalProperties(new Colour(1,1,1), 0, 0, 0.5, 0.5, true));
		Lighting lg = new Lighting();
		lg.addLight(l);		
		scene.setLighting(lg);
		lg.addLightsToScene(scene);

		scene.addSceneObject(new Sphere(new Point(80, 0, 800), 400, new OpticalProperties(new Colour(0.1, 0.1, 1), 0, 0, 0.5, 0.4, false)));
		scene.addSceneObject(new Sphere(new Point(0, 0, 100), 80, new OpticalProperties(new Colour(0.4, 0.95, 0.3), 0.98, 0, 0.5, 0.4, false)));
		scene.addSceneObject(new Sphere(new Point(300, 0, 100), 80, new OpticalProperties(new Colour(0.95, 0.0, 0.95), 0.98, 0, 0.6, 0.1, false)));
		
		Plane p = new Plane(
				new Point(0, -80, 0),
				new Vector(0, 1, 0),
				new OpticalProperties(new Colour(0.4, 0.4, 0.4), 0, 0, 0.6, 1.0, false)
				);
		scene.addSceneObject(p);
				
		tracer = new RayTracerEngine(new Dimension(this.getImageWidth(), this.getImageHeight()), scene, c);
		raytrace = new JPanel().createImage(tracer);
		tracer.setSuperSampling(true);	
		tracer.start();
	}

	/* (non-Javadoc)
	 * @see synergynetframework.jme.gfx.twod.DrawableSpatialImage#cursorDragged(long, int, int)
	 */
	@Override
	public void cursorDragged(long id, int x, int y) {
	}

	/* (non-Javadoc)
	 * @see synergynetframework.jme.gfx.twod.DrawableSpatialImage#cursorPressed(long, int, int)
	 */
	@Override
	public void cursorPressed(long cursorID, int x, int y) {
	}

	/* (non-Javadoc)
	 * @see synergynetframework.jme.gfx.twod.DrawableSpatialImage#cursorReleased(long, int, int)
	 */
	@Override
	public void cursorReleased(long cursorID, int x, int y) {
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.jme.gfx.twod.DrawableSpatialImage#cursorClicked(long, int, int)
	 */
	@Override
	public void cursorClicked(long cursorID, int x, int y) {}


	/* (non-Javadoc)
	 * @see synergynetframework.jme.gfx.twod.DrawableSpatialImage#draw()
	 */
	public void draw() {
		
		if(gfx == null) return;
		
		if(off_gfx == null || buf_img == null) {
			buf_img = new BufferedImage(tracer.getWidth(), tracer.getHeight(), BufferedImage.TYPE_INT_RGB);
			off_gfx = buf_img.getGraphics();	
			fm = gfx.getFontMetrics(info_font);
		}
//		if(raytrace != null) off_gfx.drawImage(raytrace, 0, 0, null);
//		if(buf_img != null) gfx.drawImage(buf_img, 0, 0, null);
		
		gfx.drawImage(raytrace, 0, 0, null);
		
		gfx.setColor(Color.white);
		gfx.setFont(info_font);

		if(tracer != null) {
			if(!tracer.isFinished()) {
				int y = 10;
				gfx.drawString("Rendering..." + (int)tracer.getPercentComplete() + "%", 10, y);
				Iterator<String> i = messages.iterator();
				while(i.hasNext()) {
					y += fm.getHeight();
					gfx.drawString((String)i.next(), 10, y);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see synergynetframework.jme.gfx.twod.DrawableSpatialImage#getSpatial()
	 */
	public Spatial getSpatial() {
		return this;
	}

}
