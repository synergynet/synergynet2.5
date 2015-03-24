package apps.threedbuttonsexperiment.calculator.component;

import java.awt.Color;
import java.awt.Font;
import java.net.URL;

import synergynetframework.appsystem.contentsystem.ContentSystemUtils;
import synergynetframework.jme.gfx.twod.utils.GraphicsImageQuad;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture.ApplyMode;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial.CullHint;
import com.jme.scene.shape.Quad;
import com.jme.scene.shape.RoundedBox;
import com.jmex.awt.swingui.ImageGraphics;

/**
 * The Class TwoDDisplayNode.
 */
public class TwoDDisplayNode {
	
	/** The gfx. */
	protected ImageGraphics gfx;

	/** The name. */
	protected String name;

	/** The quad. */
	protected GraphicsImageQuad quad;

	/** The rb. */
	protected RoundedBox rb;

	/** The text texture url. */
	protected URL textTextureURL;

	/** The texture url. */
	protected URL textureURL;

	/** The length. */
	protected float width = 1, length = 1;

	/**
	 * Instantiates a new two d display node.
	 *
	 * @param name
	 *            the name
	 * @param width
	 *            the width
	 * @param length
	 *            the length
	 */
	public TwoDDisplayNode(String name, float width, float length) {
		this.name = name;
		this.width = width;
		this.length = length;
		init();
	}
	
	/**
	 * Gets the display quad.
	 *
	 * @return the display quad
	 */
	public Quad getDisplayQuad() {
		return quad;
	}
	
	/**
	 * Inits the.
	 */
	protected void init() {
		
		render("");
		
	}

	/**
	 * Render.
	 *
	 * @param text
	 *            the text
	 */
	protected void render(String text) {

		if ((quad != null) && (quad.getParent() != null)) {
			quad.getParent().detachChild(quad);
		}

		quad = new GraphicsImageQuad("Display " + this.name,
				(int) (this.width), (int) (this.length), 1);
		quad.setTextureApplyMode(ApplyMode.Replace);

		gfx = quad.getImageGraphics();
		
		quad.setModelBound(new BoundingBox());
		quad.updateModelBound();
		
		gfx = quad.getImageGraphics();
		
		gfx.setColor(new Color(51, 0, 102));
		gfx.fillRect(0, 0, 300, 200);
		gfx.setColor(Color.white);
		Font font = new Font("Arial Narrow", Font.PLAIN, 45);
		gfx.setFont(font);
		
		gfx.setColor(Color.white);
		int textWidth = ContentSystemUtils.getStringWidth(font, text);
		gfx.drawString(text, 165 - textWidth, 45);

		quad.setCullHint(CullHint.Never);
		quad.updateGeometricState(0f, false);
		quad.updateGraphics();
	}

	/**
	 * Sets the local location.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public void setLocalLocation(float x, float y) {
		quad.setLocalTranslation(x, y, quad.getLocalTranslation().z);
	}

	/**
	 * Sets the text.
	 *
	 * @param text
	 *            the new text
	 */
	public void setText(String text) {
		Vector3f location = quad.getLocalTranslation();
		render(text);
		quad.setLocalTranslation(location);
	}

}
