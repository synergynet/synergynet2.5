package apps.lightrays.raytracer;

import java.awt.Dimension;
import java.awt.image.ColorModel;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageProducer;

import apps.lightrays.gfxlib.Pixels;
import apps.lightrays.raytracer.scene.Camera;
import apps.lightrays.raytracer.scene.Colour;
import apps.lightrays.raytracer.scene.Ray;
import apps.lightrays.raytracer.scene.Scene;
import apps.lightrays.raytracer.scene.Vector;



/**
 * The Class RayTracerEngine.
 */
public class RayTracerEngine implements ImageProducer, Runnable {

	/** The width. */
	private int width;
	
	/** The height. */
	private int height;
	//private int[] pixels;
	/** The pixels. */
	private Pixels pixels;

	/** The lines_produced. */
	private int lines_produced;
	
	/** The lines_consumed. */
	private int lines_consumed;

	/** The t. */
	private Thread t;
	
	/** The consumer. */
	private ImageConsumer consumer;

	/** The scene. */
	private Scene scene;
	
	/** The camera. */
	public Camera camera;

	/** The forward. */
	private Vector forward;
	
	/** The rtl. */
	private RayTracerListener rtl;
	
	/** The supersampling. */
	private boolean supersampling = false;
	
	/** The finished. */
	private boolean finished = true;
	
	/** The percent_complete. */
	private double percent_complete = 0.0;

	/**
	 * Instantiates a new ray tracer engine.
	 *
	 * @param image_size the image_size
	 */
	public RayTracerEngine(Dimension image_size) {
		this(image_size, null, null);
	}

	/**
	 * Instantiates a new ray tracer engine.
	 *
	 * @param image_size the image_size
	 * @param scene the scene
	 * @param camera the camera
	 */
	public RayTracerEngine(Dimension image_size, Scene scene, Camera camera) {
		this.scene = scene;
		this.camera = camera;
		this.width = image_size.width;
		this.height = image_size.height;
		forward = new Vector(0,0,1);
		forward.normalise();		

	}

	/**
	 * Sets the super sampling.
	 *
	 * @param flag the new super sampling
	 */
	public void setSuperSampling(boolean flag) {
		this.supersampling = flag;
	}

	/**
	 * Sets the ray tracer listener.
	 *
	 * @param l the new ray tracer listener
	 */
	public void setRayTracerListener(RayTracerListener l) {
		this.rtl = l;
	}

	/**
	 * Start.
	 */
	public void start() {
		lines_produced = 0;
		lines_consumed = 0;
		finished = false;
		percent_complete = 0.0;
		int pix[] = new int[width * height];
		pixels = new Pixels(pix, width, height);				
		t = new Thread(this);
		t.start();		
	}

	/* (non-Javadoc)
	 * @see java.awt.image.ImageProducer#addConsumer(java.awt.image.ImageConsumer)
	 */
	public void addConsumer(ImageConsumer ic) {		
		consumer = ic;
		consumer.setColorModel(ColorModel.getRGBdefault());
		consumer.setDimensions(width, height);
		consumer.setHints(ImageConsumer.COMPLETESCANLINES | ImageConsumer.TOPDOWNLEFTRIGHT);		
		consumer = ic;
	}

	/* (non-Javadoc)
	 * @see java.awt.image.ImageProducer#isConsumer(java.awt.image.ImageConsumer)
	 */
	public boolean isConsumer(ImageConsumer ic) {
		return consumer == ic;

	}

	/* (non-Javadoc)
	 * @see java.awt.image.ImageProducer#removeConsumer(java.awt.image.ImageConsumer)
	 */
	public void removeConsumer(ImageConsumer ic) {
		consumer = null;
	}

	/* (non-Javadoc)
	 * @see java.awt.image.ImageProducer#startProduction(java.awt.image.ImageConsumer)
	 */
	public void startProduction(ImageConsumer ic) {
		addConsumer(ic);
		if(lines_produced == height) {
			requestTopDownLeftRightResend(consumer);
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.image.ImageProducer#requestTopDownLeftRightResend(java.awt.image.ImageConsumer)
	 */
	public void requestTopDownLeftRightResend(ImageConsumer ic) {
		ic.setHints(ImageConsumer.TOPDOWNLEFTRIGHT);
		ic.setPixels (0, 0, width, lines_produced, ColorModel.getRGBdefault (), pixels.pixels, 0, width);
		lines_consumed = lines_produced;
		if (lines_produced == height)
		{
			ic.imageComplete(ImageConsumer.STATICIMAGEDONE);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		if(rtl != null)
			rtl.traceStarted();
		for(int y = 0; y < height; y++) {
			if(supersampling)
				traceLineSupersampling(y);
			else
				traceLine(y);

			if(rtl != null)
				rtl.tracedLine(y);
			synchronized (this)
			{
				lines_produced++;
				if (consumer != null)
				{
					// give pixels to the consumer			

					consumer.setPixels (
							0, 								// starting x
							lines_consumed,					// starting y 
							width, 							// width
							lines_produced - lines_consumed,// height
							ColorModel.getRGBdefault(),	// color model 
							pixels.pixels, 						// array of imagePixels
							lines_consumed * width, 		// offset into array
							width);							// line width

					lines_consumed = lines_produced;

					// Tell the consumer to update the display
					consumer.imageComplete (ImageConsumer.SINGLEFRAMEDONE);
				}
			}	
		}
		synchronized (this)
		{
			if (consumer != null) {
				consumer.imageComplete (ImageConsumer.STATICIMAGEDONE);
			}
		}
		if(rtl != null)
			rtl.traceFinished();

		finished = true;
	}

	/**
	 * Checks if is finished.
	 *
	 * @return true, if is finished
	 */
	public boolean isFinished() {
		return finished;
	}

	/**
	 * Gets the percent complete.
	 *
	 * @return the percent complete
	 */
	public double getPercentComplete() {
		this.percent_complete = (double)lines_produced / (double)height * 100.0;
		return this.percent_complete;
	}

	/**
	 * Gets the pixels.
	 *
	 * @return the pixels
	 */
	public Pixels getPixels() {
		return this.pixels;
	}

	/**
	 * Trace line.
	 *
	 * @param y the y
	 */
	public void traceLine (int y)
	{
		for (int x = 0; x < width; x++)
		{
			Ray r = camera.generateRay(scene, x, y);
			Colour shade = new Colour();   	
			r.fire(shade);
			pixels.pixels[(y * width) + x] = shade.getRGB();

		}
	}

	/**
	 * Trace line supersampling.
	 *
	 * @param y the y
	 */
	public void traceLineSupersampling(int y) {
		int side = 2;
		for (int x = 0; x < width; x++)
		{
			Colour shade = new Colour();
			Ray[][] r = camera.generateSupersampledRays(scene, x, y, 2, 4.0);
			Colour[] pixel_c = new Colour[(side*2+1)*(side*2+1)];
			int index = 0;
			for(int i = 0; i < side*2+1; i++) {
				for(int j = 0; j < side*2+1; j++) {
					pixel_c[index] = new Colour();
					r[i][j].fire(pixel_c[index]);
					index++;
				}
			}

			shade = Colour.getAverageColour(pixel_c);

			pixels.pixels[(y * width) + x] = shade.getRGB();

		}		
	}

	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Sets the camera.
	 *
	 * @param camera the new camera
	 */
	public void setCamera(Camera camera) {
		this.camera = camera;		
	}	

	/**
	 * Sets the scene.
	 *
	 * @param s the new scene
	 */
	public void setScene(Scene s) {
		this.scene = s;
	}
}
