package apps.userdefinedgestures.scene;

import java.net.URL;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.image.Texture.ApplyMode;
import com.jme.image.Texture.WrapMode;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

/**
 * The Class Background.
 */
public class Background extends Node {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7253106690333705792L;

	/** The wall texture. */
	protected URL floorTexture, wallTexture;

	/** The wall texture scale. */
	protected Vector3f floorTextureScale, wallTextureScale;

	/** The height. */
	protected float length, width, height;

	/** The name. */
	protected String name;

	/**
	 * Instantiates a new background.
	 *
	 * @param name
	 *            the name
	 * @param length
	 *            the length
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 * @param floorTexture
	 *            the floor texture
	 * @param floorTextureScale
	 *            the floor texture scale
	 * @param wallTexture
	 *            the wall texture
	 * @param wallTextureScale
	 *            the wall texture scale
	 */
	public Background(String name, float length, float width, float height,
			URL floorTexture, Vector3f floorTextureScale, URL wallTexture,
			Vector3f wallTextureScale) {
		super();
		this.name = name;
		this.length = length;
		this.width = width;
		this.height = height;
		this.floorTexture = floorTexture;
		this.wallTexture = wallTexture;
		this.floorTextureScale = floorTextureScale;
		this.wallTextureScale = wallTextureScale;

		buildFloor();
		// buildWall(0, width, length, height);

	}

	/**
	 * Builds the floor.
	 */
	private void buildFloor() {
		Vector3f min = new Vector3f(-width / 2, -1, -length / 2);
		Vector3f max = new Vector3f(width / 2, 1, length / 2);
		final Box floor = new Box(name + "floor", min, max);
		floor.setLocalTranslation(new Vector3f(0f, 0f, 0f));
		floor.setModelBound(new BoundingBox());
		floor.updateModelBound();
		floor.updateRenderState();
		this.attachChild(floor);

		if (this.floorTexture == null) {
			return;
		}
		TextureState ts;
		Texture texture;
		ts = DisplaySystem.getDisplaySystem().getRenderer()
				.createTextureState();
		ts.setCorrectionType(TextureState.CorrectionType.Perspective);
		texture = TextureManager.loadTexture(floorTexture,
				Texture.MinificationFilter.Trilinear,
				Texture.MagnificationFilter.Bilinear);
		texture.setWrap(WrapMode.Repeat);
		texture.setApply(ApplyMode.Replace);
		texture.setScale(this.floorTextureScale);
		ts.setTexture(texture);
		ts.apply();

		floor.setRenderState(ts);
		floor.updateRenderState();
		
	}

	/**
	 * Builds the wall.
	 *
	 * @param floorCenter
	 *            the floor center
	 * @param floorWidth
	 *            the floor width
	 * @param floorLength
	 *            the floor length
	 * @param wallHeight
	 *            the wall height
	 */
	@SuppressWarnings("unused")
	private void buildWall(float floorCenter, float floorWidth,
			float floorLength, float wallHeight) {
		Vector3f min = new Vector3f(-floorWidth / 2, -wallHeight / 2, -1);
		Vector3f max = new Vector3f(floorWidth / 2, wallHeight / 2, 1);
		final Box north = new Box(name + "north wall", min, max);
		north.setLocalTranslation(new Vector3f(0f, wallHeight / 2, floorCenter
				- (floorLength / 2)));
		north.setModelBound(new BoundingBox());
		north.updateModelBound();
		north.updateRenderState();
		this.attachChild(north);
		
		final Box south = new Box(name + "south wall", min, max);
		south.setLocalTranslation(new Vector3f(0f, wallHeight / 2, floorCenter
				+ (floorLength / 2)));
		south.setModelBound(new BoundingBox());
		south.updateModelBound();
		south.updateRenderState();
		this.attachChild(south);

		min = new Vector3f(-1, -wallHeight / 2, -floorLength / 2);
		max = new Vector3f(1, wallHeight / 2, floorLength / 2);

		final Box east = new Box(name + "east wall", min, max);
		east.setLocalTranslation(new Vector3f(floorWidth / 2, wallHeight / 2,
				floorCenter));
		east.setModelBound(new BoundingBox());
		east.updateModelBound();
		east.updateRenderState();
		this.attachChild(east);

		final Box west = new Box(name + "west wall", min, max);
		west.setLocalTranslation(new Vector3f(-floorWidth / 2, wallHeight / 2,
				floorCenter));
		west.setModelBound(new BoundingBox());
		west.updateModelBound();
		west.updateRenderState();
		this.attachChild(west);

		if (this.wallTexture == null) {
			return;
		}
		TextureState ts;
		Texture texture;
		ts = DisplaySystem.getDisplaySystem().getRenderer()
				.createTextureState();
		ts.setCorrectionType(TextureState.CorrectionType.Perspective);
		texture = TextureManager.loadTexture(wallTexture,
				Texture.MinificationFilter.Trilinear,
				Texture.MagnificationFilter.Bilinear);
		texture.setWrap(WrapMode.Repeat);
		texture.setApply(ApplyMode.Replace);
		texture.setScale(wallTextureScale);
		ts.setTexture(texture);
		ts.apply();

		north.setRenderState(ts);
		north.updateRenderState();

		south.setRenderState(ts);
		south.updateRenderState();

		east.setRenderState(ts);
		east.updateRenderState();

		west.setRenderState(ts);
		west.updateRenderState();

	}
	
}
