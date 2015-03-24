package apps.threedinteraction.clock;

import java.net.URL;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.image.Texture.ApplyMode;
import com.jme.image.Texture.WrapMode;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.shape.Disk;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

/**
 * The Class Clock.
 */
public class Clock extends Node {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1111175967783111868L;

	/** The clock face. */
	protected Disk clockFace;

	/** The second hand. */
	protected Quad hourHand, minHand, secondHand;

	/** The name. */
	protected String name;

	/** The height. */
	protected float radius = 10, height = 3;

	/** The second hand texture. */
	protected URL textureURL, hourHandTexture, minHandTexture,
			secondHandTexture;
	
	/**
	 * Instantiates a new clock.
	 *
	 * @param name
	 *            the name
	 * @param radius
	 *            the radius
	 * @param height
	 *            the height
	 * @param bgTexture
	 *            the bg texture
	 * @param hourHandTexture
	 *            the hour hand texture
	 * @param minHandTexture
	 *            the min hand texture
	 * @param secondHandTexture
	 *            the second hand texture
	 */
	public Clock(String name, float radius, float height, URL bgTexture,
			URL hourHandTexture, URL minHandTexture, URL secondHandTexture) {
		super(name);
		this.name = name;
		this.radius = radius;
		this.height = height;
		this.textureURL = bgTexture;
		this.hourHandTexture = hourHandTexture;
		this.minHandTexture = minHandTexture;
		this.secondHandTexture = secondHandTexture;
		init();
	}
	
	/**
	 * Creates the hands.
	 */
	private void createHands() {
		
		hourHand = new Quad(this.name + "hourHand", 2, 2 * (radius - 5));
		hourHand.setLocalTranslation(new Vector3f(0, -2.8f, (height / 2) + 0.3f));
		minHand = new Quad(this.name + "minHand", 1.5f, 2 * (radius - 4f));
		minHand.setLocalTranslation(new Vector3f(0, -2.8f, (height / 2) + 0.6f));
		secondHand = new Quad(this.name + "secondHand", 1, 2 * (radius - 3f));
		secondHand.setLocalTranslation(new Vector3f(0, -2.8f,
				(height / 2) + 0.9f));
		
		BlendState alpha = DisplaySystem.getDisplaySystem().getRenderer()
				.createBlendState();
		alpha.setEnabled(true);
		alpha.setBlendEnabled(true);
		alpha.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		alpha.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
		alpha.setTestEnabled(true);
		alpha.setTestFunction(BlendState.TestFunction.GreaterThan);
		hourHand.setRenderState(alpha);
		minHand.setRenderState(alpha);
		secondHand.setRenderState(alpha);

		TextureState ts1;
		Texture texture1;
		ts1 = DisplaySystem.getDisplaySystem().getRenderer()
				.createTextureState();
		ts1.setCorrectionType(TextureState.CorrectionType.Perspective);
		texture1 = TextureManager.loadTexture(hourHandTexture,
				Texture.MinificationFilter.Trilinear,
				Texture.MagnificationFilter.Bilinear);
		texture1.setWrap(WrapMode.Repeat);
		texture1.setApply(ApplyMode.Replace);
		texture1.setScale(new Vector3f(1, 1, 1));
		ts1.setTexture(texture1);
		ts1.apply();
		hourHand.setRenderState(ts1);
		hourHand.updateRenderState();
		
		TextureState ts2;
		Texture texture2;
		ts2 = DisplaySystem.getDisplaySystem().getRenderer()
				.createTextureState();
		ts2.setCorrectionType(TextureState.CorrectionType.Perspective);
		texture2 = TextureManager.loadTexture(minHandTexture,
				Texture.MinificationFilter.Trilinear,
				Texture.MagnificationFilter.Bilinear);
		texture2.setWrap(WrapMode.Repeat);
		texture2.setApply(ApplyMode.Replace);
		texture2.setScale(new Vector3f(1, 1, 1));
		ts2.setTexture(texture2);
		ts2.apply();
		minHand.setRenderState(ts2);
		minHand.updateRenderState();
		
		TextureState ts3;
		Texture texture3;
		ts3 = DisplaySystem.getDisplaySystem().getRenderer()
				.createTextureState();
		ts3.setCorrectionType(TextureState.CorrectionType.Perspective);
		texture3 = TextureManager.loadTexture(secondHandTexture,
				Texture.MinificationFilter.Trilinear,
				Texture.MagnificationFilter.Bilinear);
		texture3.setWrap(WrapMode.Repeat);
		texture3.setApply(ApplyMode.Replace);
		texture3.setScale(new Vector3f(1, 1, 1));
		ts3.setTexture(texture3);
		ts3.apply();
		secondHand.setRenderState(ts3);
		secondHand.updateRenderState();
		
		this.attachChild(hourHand);
		this.attachChild(minHand);
		this.attachChild(secondHand);
		
		this.updateGeometricState(0f, false);
		
	}

	/**
	 * Inits the.
	 */
	protected void init() {

		clockFace = new Disk(name + "clock", 16, 32, radius);
		clockFace.setModelBound(new BoundingBox());
		clockFace.updateModelBound();
		clockFace.getLocalTranslation().set(0, 0, 0);

		this.attachChild(clockFace);

		BlendState alpha = DisplaySystem.getDisplaySystem().getRenderer()
				.createBlendState();
		alpha.setEnabled(true);
		alpha.setBlendEnabled(true);
		alpha.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		alpha.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
		alpha.setTestEnabled(true);
		alpha.setTestFunction(BlendState.TestFunction.GreaterThan);
		clockFace.setRenderState(alpha);

		TextureState ts;
		Texture texture;
		ts = DisplaySystem.getDisplaySystem().getRenderer()
				.createTextureState();
		ts.setCorrectionType(TextureState.CorrectionType.Perspective);
		texture = TextureManager.loadTexture(textureURL,
				Texture.MinificationFilter.Trilinear,
				Texture.MagnificationFilter.Bilinear);
		texture.setWrap(WrapMode.Repeat);
		texture.setApply(ApplyMode.Replace);
		texture.setScale(new Vector3f(1, 1, 1));
		ts.setTexture(texture);
		ts.apply();
		clockFace.setRenderState(ts);
		clockFace.updateRenderState();

		Quaternion tq = new Quaternion();
		tq.fromAngleAxis((2.75f * FastMath.PI) / 2f, new Vector3f(1, 0, 0));
		this.setLocalRotation(tq);
		
		createHands();
		
	}

	/**
	 * Sets the time.
	 *
	 * @param hour
	 *            the hour
	 * @param min
	 *            the min
	 * @param second
	 *            the second
	 */
	public void setTime(int hour, int min, int second) {
		// set hour hand
		
		hour = (hour % 12) + (min / 60);
		float hourAngle = (hour * 2 * FastMath.PI) / 12;
		Quaternion tq = new Quaternion();
		tq.fromAngleAxis(-hourAngle, new Vector3f(0, 0, 1));
		hourHand.setLocalRotation(tq);
		hourHand.updateGeometricState(0f, false);
		
		float minAngle = (min * 2 * FastMath.PI) / 60;
		Quaternion tq1 = new Quaternion();
		tq1.fromAngleAxis(-minAngle, new Vector3f(0, 0, 1));
		minHand.setLocalRotation(tq1);
		minHand.updateGeometricState(0f, false);
		
		float secondAngle = (second * 2 * FastMath.PI) / 60;
		Quaternion tq2 = new Quaternion();
		tq2.fromAngleAxis(-secondAngle, new Vector3f(0, 0, 1));
		secondHand.setLocalRotation(tq2);
		secondHand.updateGeometricState(0f, false);
		
	}
	
}
