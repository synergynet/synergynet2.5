package apps.mtdesktop.desktop.tree.nodes;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class ImageNode extends AssetNode{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7063668231614709939L;
	private static int ICON_WIDTH = 40;
	private static int ICON_HEIGHT = 30;
	
	protected Image img;
	
	public ImageNode(File assetFile) {
		super(assetFile);
		// TODO Auto-generated constructor stub
	}

	public ImageNode(Image img){
		super(null);
		this.img = img;
	}
	
	public void setImage(Image img){
		this.img = img;
	}
	
	public Image getImage(){
		return img;
	}
	
	public Icon getIcon(){
		try{
			 ImageIcon imgIcon = null;
			if(assetFile != null){
				imgIcon = new ImageIcon(assetFile.toURI().toURL());
			}else if(img != null){
				imgIcon = new ImageIcon(img);
			}
			BufferedImage bi = new BufferedImage(ICON_WIDTH, ICON_HEIGHT, BufferedImage.TYPE_INT_ARGB);
			Graphics g = bi.createGraphics();
			g.drawImage(imgIcon.getImage(), 0, 0, ICON_WIDTH, ICON_HEIGHT, null);
			return new ImageIcon(bi);
		} catch (MalformedURLException e) {
				e.printStackTrace();
				return null;
			}
		}
	}