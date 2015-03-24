package apps.mtdesktop.desktop;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.EditorKit;


/**
 * The Class Utility.
 */
public class Utility {

	
	  /**
  	 * Rtf to html.
  	 *
  	 * @param rtf the rtf
  	 * @return the string
  	 * @throws IOException Signals that an I/O exception has occurred.
  	 */
  	public static String rtfToHtml(Reader rtf) throws IOException 
		{		
			JEditorPane p = new JEditorPane();
			p.setContentType("text/rtf");
			EditorKit kitRtf = p.getEditorKitForContentType("text/rtf");
			try {	
				kitRtf.read(rtf, p.getDocument(), 0);
				kitRtf = null;
				EditorKit kitHtml = p.getEditorKitForContentType("text/html");
				Writer writer = new StringWriter();	
				kitHtml.write(writer, p.getDocument(), 0, p.getDocument().getLength());
				return writer.toString();
			} catch (BadLocationException e) {
				e.printStackTrace();
				}		return null;
		}
	  
		
	    /**
    	 * To buffered image.
    	 *
    	 * @param src the src
    	 * @return the buffered image
    	 */
    	public static BufferedImage toBufferedImage(Image src) {
	        int w = src.getWidth(null);
	        int h = src.getHeight(null);
	        int type = BufferedImage.TYPE_INT_RGB;  
	        BufferedImage dest = new BufferedImage(w, h, type);
	        Graphics2D g2 = dest.createGraphics();
	        g2.drawImage(src, 0, 0, null);
	        g2.dispose();
	        return dest;
	    }
}
