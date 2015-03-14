package apps.mtdesktop.desktop.mtmousetest;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.JLabel;

public class ImageLabel extends JLabel implements MouseMotionListener, MouseListener{
	
	/**
	 * 
	 */
	private boolean isDragged = false;
	int width = 1024;
	int height = 1024;
	
	int imageWidth = width/4;
	int imageHeight = height/4;
	
	private static final long serialVersionUID = 1622760377813315620L;
	private BufferedImage bi;
	private double angle = 0;
//	private double scale = 1.0;
	private AffineTransform at;
	
	public ImageLabel(URL imageResource){
		super();
        this.setBounds(0,0,width, height);
        this.addMouseListener(this);
		this.addMouseMotionListener(this);
	    
	    Image image = getToolkit().getImage(imageResource);

	    MediaTracker mt = new MediaTracker(this);
	    mt.addImage(image, 1);
	    try {
	      mt.waitForAll();
	    } catch (Exception e) {
	      System.out.println("Exception while loading image.");
	    }
	    
	    bi = new BufferedImage(imageWidth, imageHeight,   BufferedImage.TYPE_INT_ARGB);
	    Graphics2D big = bi.createGraphics();
	    big.drawImage(image, 0, 0,imageWidth,imageHeight, this);
	    
		at =   AffineTransform.getScaleInstance((double)imageWidth/bi.getWidth(),   (double)imageHeight/bi.getHeight());
	}

	public void setAngle(double angle){
		this.angle = angle;
		this.repaint();
	}
	
	public double getAngle(){
		return angle;
	}
	
	public void setScale(double scale){
//		this.scale = scale;
		at =   AffineTransform.getScaleInstance((double)(imageWidth*scale)/bi.getWidth(),   (double)(imageHeight*scale)/bi.getHeight());
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
        Component c = e.getComponent();
        c.setLocation( c.getX()+e.getX() - imageWidth/2 - 30, c.getY()+e.getY() - imageHeight/2 - 30);
        c.repaint();
	}
	
	  public void paintComponent(Graphics g) {
		    super.paintComponent(g);
		    Graphics2D g2D = (Graphics2D) g;
		    g2D.rotate(angle, bi.getWidth() / 2, bi.getHeight() / 2);
		    g2D.drawImage(bi, at, this);
		  }

	@Override
	public void mouseMoved(MouseEvent arg0) {
		 
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		 
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		 
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		 
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		isDragged = true;		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		isDragged = false;		
		
	}

	public void notifyChange(double angle, double scale) {
		if(isDragged){	
			this.setAngle(angle);
			this.setScale(scale);
		}
	}
}
