package apps.threedinteraction.desktop;

import apps.threedmanipulation.ThreeDManipulation;

import com.jme.image.Texture;
import com.jme.image.Texture.ApplyMode;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.math.spring.SpringPoint;
import com.jme.math.spring.SpringPointForce;
import com.jme.scene.Node;
import com.jme.scene.Spatial.CullHint;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.effects.cloth.ClothPatch;

/**
 * The Class FloatDesktop.
 */
public class FloatDesktop {
	
	/**
	 * The Class RandomWindForce.
	 */
	private class RandomWindForce extends SpringPointForce {
		
		/** The strength. */
		private final float strength;
		
		/** The wind direction. */
		private final Vector3f windDirection;
		
		/**
		 * Instantiates a new random wind force.
		 *
		 * @param strength
		 *            the strength
		 * @param direction
		 *            the direction
		 */
		public RandomWindForce(float strength, Vector3f direction) {
			this.strength = strength;
			this.windDirection = direction;
		}
		
		/*
		 * (non-Javadoc)
		 * @see com.jme.math.spring.SpringPointForce#apply(float,
		 * com.jme.math.spring.SpringPoint)
		 */
		public void apply(float dt, SpringPoint node) {
			windDirection.x += dt * (FastMath.nextRandomFloat() - 0.5f);
			windDirection.z += dt * (FastMath.nextRandomFloat() - 0.5f);
			windDirection.normalize();
			float tStr = FastMath.nextRandomFloat() * strength;
			node.acceleration.addLocal(windDirection.x * tStr, windDirection.y
					* tStr, windDirection.z * tStr);
		}
		
	}
	
	/** The desktop. */
	private ClothPatch desktop;
	
	/** The desktop shown. */
	private boolean desktopShown = false;
	
	/** The wind. */
	private SpringPointForce wind;
	
	/** The wind direction. */
	private Vector3f windDirection = new Vector3f(-1f, -0.2f, -1f);
	
	/** The wind strength. */
	private float windStrength = 1f;
	
	/**
	 * Instantiates a new float desktop.
	 *
	 * @param name
	 *            the name
	 * @param location
	 *            the location
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 * @param worldNode
	 *            the world node
	 */
	public FloatDesktop(String name, Vector3f location, int width, int height,
			Node worldNode) {
		desktop = new ClothPatch(name + "desktop", width, height, 1f, 10);
		wind = new RandomWindForce(windStrength, windDirection);
		desktop.addForce(wind);
		desktop.setLocalTranslation(location);
		
		TextureState ts;
		Texture texture;
		ts = DisplaySystem.getDisplaySystem().getRenderer()
				.createTextureState();
		ts.setCorrectionType(TextureState.CorrectionType.Perspective);
		texture = TextureManager.loadTexture(
				ThreeDManipulation.class.getResource("wallpaper.png"),
				Texture.MinificationFilter.Trilinear,
				Texture.MagnificationFilter.Bilinear);
		texture.setApply(ApplyMode.Replace);
		Quaternion tq = new Quaternion();
		tq.fromAngleAxis(FastMath.PI * 0, new Vector3f(0, 0, 1));
		texture.setRotation(tq);
		ts.setTexture(texture);
		ts.apply();
		
		desktop.setRenderState(ts);
		desktop.updateRenderState();
		
		Quaternion tq1 = new Quaternion();
		tq1.fromAngleAxis(-FastMath.PI / 2f, new Vector3f(1, 0, 0));
		desktop.setLocalRotation(tq1);
		desktop.updateGeometricState(0f, false);
		
		for (int i = 0; i < 31; i++) {
			desktop.getSystem().getNode(i * 37).position.y *= 1f;
			desktop.getSystem().getNode(i * 37)
					.setMass(Float.POSITIVE_INFINITY);
		}
		
		for (int i = 1; i < 32; i++) {
			desktop.getSystem().getNode((i * 37) - 1).position.y *= 1f;
			desktop.getSystem().getNode((i * 37) - 1)
					.setMass(Float.POSITIVE_INFINITY);
		}
		
		for (int i = 0; i < 37; i++) {
			desktop.getSystem().getNode(i).position.y *= 1f;
			desktop.getSystem().getNode(i).setMass(Float.POSITIVE_INFINITY);
		}
		
		for (int i = 0; i < 37; i++) {
			desktop.getSystem().getNode((37 * 31) + i).position.y *= 1f;
			desktop.getSystem().getNode((37 * 31) + i)
					.setMass(Float.POSITIVE_INFINITY);
		}
		
		worldNode.attachChild(desktop);
		
	}
	
	/**
	 * Checks for desktop shown.
	 *
	 * @return true, if successful
	 */
	public boolean hasDesktopShown() {
		return this.desktopShown;
	}
	
	/**
	 * Show desktop.
	 *
	 * @param b
	 *            the b
	 */
	public void showDesktop(boolean b) {
		this.desktopShown = b;
		if (b) {
			desktop.setCullHint(CullHint.Never);
		} else {
			desktop.setCullHint(CullHint.Always);
		}
	};
	
}
