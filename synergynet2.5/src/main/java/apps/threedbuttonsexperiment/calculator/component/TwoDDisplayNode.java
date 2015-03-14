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

public class TwoDDisplayNode {

	protected float width = 1, length = 1;
	protected URL textureURL;
	protected URL textTextureURL;
	protected RoundedBox rb;
	protected GraphicsImageQuad quad;
	protected ImageGraphics gfx;
	protected String name;
	
	public TwoDDisplayNode(String name, float width, float length){
		this.name = name;
		this.width = width;
		this.length = length;		
		init();		
	}

	protected void init(){
					
		render("");	
		
	}
	
	
	protected void render(String text){
		
		if (quad!=null && quad.getParent()!=null){
			quad.getParent().detachChild(quad);
		}
		
		quad = new GraphicsImageQuad("Display "+this.name, (int)(this.width), (int)(this.length), 1);
		quad.setTextureApplyMode(ApplyMode.Replace);
		
		gfx = quad.getImageGraphics();	
		
		quad.setModelBound(new BoundingBox());
		quad.updateModelBound();
	
		
		gfx = quad.getImageGraphics();	
		
		gfx.setColor(new Color(51, 0, 102));
		gfx.fillRect(0, 0, 300, 200);
		gfx.setColor(Color.white);
		Font font = new Font("Arial Narrow", Font.PLAIN,45);
		gfx.setFont(font);
		
	
		gfx.setColor(Color.white);
		int textWidth =  ContentSystemUtils.getStringWidth(font, text);		
		gfx.drawString(text, 165-textWidth, 45);
		
		quad.setCullHint(CullHint.Never);
		quad.updateGeometricState(0f, false);
		quad.updateGraphics();
	}
	
	public void setText(String text){
		Vector3f location = quad.getLocalTranslation();
		render(text);
		quad.setLocalTranslation(location);
	}
	
	public Quad getDisplayQuad(){
		return quad;
	}
	
	public void setLocalLocation(float x, float y){
		quad.setLocalTranslation(x, y, quad.getLocalTranslation().z);
	}
	
}
