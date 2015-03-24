package apps.basketapp.client;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.LightImageLabel;
import synergynetframework.appsystem.contentsystem.items.MediaPlayer;
import synergynetframework.appsystem.contentsystem.items.MultiLineTextLabel;

import java.io.*;

import org.apache.commons.lang.StringEscapeUtils;
import org.w3c.dom.*;

import javax.xml.parsers.*;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;


/**
 * The Class StateRecorder.
 */
public class StateRecorder {
	
	/** The record enabled. */
	private boolean recordEnabled = false;
	
	/** The delay. */
	private long DELAY = 2000;
	
	/** The log path. */
	public static String logPath = System.getProperty("user.dir") + "apps/basketapp/client/log.xml";
	
	/** The app. */
	private BasketApp app;
	
	/** The dbfac. */
	private DocumentBuilderFactory dbfac;
	
	/** The doc builder. */
	private DocumentBuilder docBuilder;
	
	/**
	 * Instantiates a new state recorder.
	 *
	 * @param app the app
	 */
	public StateRecorder(BasketApp app){
		this.app = app;
        dbfac = DocumentBuilderFactory.newInstance();
        try {
			docBuilder = dbfac.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			 
			e.printStackTrace();
		}
	}
	
	/**
	 * Start state recording.
	 */
	public void startStateRecording(){
		this.recordEnabled = true;
		
		new Thread(new Runnable(){

			@Override
			public void run() {
				try{
					while(recordEnabled){
			            Document doc = docBuilder.newDocument();
			            
			            Element root = doc.createElement("items");
			            doc.appendChild(root);
			            
			            Thread.sleep(DELAY);
						for(ContentItem item: app.getOnlineItems()){
							
				            Element locXNode = doc.createElement("locx");
				            Text t1 = doc.createTextNode(String.valueOf(item.getLocalLocation().x));
				            locXNode.appendChild(t1);	
				            
				            Element locYNode = doc.createElement("locy");
				            Text t2 = doc.createTextNode(String.valueOf(item.getLocalLocation().y));
				            locYNode.appendChild(t2);	
				            
				            Element angleNode = doc.createElement("angle");
				            Text t3 = doc.createTextNode(String.valueOf(item.getAngle()));
				            angleNode.appendChild(t3);	
				            
				            Element scaleNode = doc.createElement("scale");
				            Text t4 = doc.createTextNode(String.valueOf(item.getScale()));
				            scaleNode.appendChild(t4);
							
				            Element child = null;
							if(item instanceof MediaPlayer){
					            child = doc.createElement("video");
					            root.appendChild(child);
					            
					            Element urlNode = doc.createElement("url");
					            child.appendChild(urlNode);
					            Text text = doc.createTextNode(((MediaPlayer)item).getMediaURL().toString());
					            urlNode.appendChild(text);					            
							}else if(item instanceof LightImageLabel){
					            child = doc.createElement("image");
					            root.appendChild(child);
					            
					            Element urlNode = doc.createElement("url");
					            child.appendChild(urlNode);
					            Text text = doc.createTextNode(((LightImageLabel)item).getImageResource().toString());
					            urlNode.appendChild(text);
					            
					            Element widthNode = doc.createElement("width");
					            child.appendChild(widthNode);
					            Text text2 = doc.createTextNode(String.valueOf(((LightImageLabel)item).getWidth()));
					            widthNode.appendChild(text2);	
					            
					            Element heightNode = doc.createElement("height");
					            child.appendChild(heightNode);
					            Text text3 = doc.createTextNode(String.valueOf(((LightImageLabel)item).getHeight()));
					            heightNode.appendChild(text3);
				            
							}else if(item instanceof MultiLineTextLabel){
							
					            child = doc.createElement("text");
					            root.appendChild(child);
					            
					            Element txtNode = doc.createElement("txt");
					            child.appendChild(txtNode);
					            String str = "";
					            for(String line: ((MultiLineTextLabel)item).getLines())
					            	str+= line + "\n";
					            str = str.substring(0, str.lastIndexOf("\n"));
					            Text text = doc.createTextNode(StringEscapeUtils.unescapeXml(str));
					            txtNode.appendChild(text);
					            
					            Element fontFamilyNode = doc.createElement("font_family");
					            child.appendChild(fontFamilyNode);
					            Text text2 = doc.createTextNode(((MultiLineTextLabel)item).getFont().getFamily());
					            fontFamilyNode.appendChild(text2);	
					            
					            Element fontSizeNode = doc.createElement("font_size");
					            child.appendChild(fontSizeNode);
					            Text text3 = doc.createTextNode(String.valueOf(((MultiLineTextLabel)item).getFont().getSize()));
					            fontSizeNode.appendChild(text3);
					            
					            Element fontColorNode = doc.createElement("font_color");
					            child.appendChild(fontColorNode);
					            Text text4 = doc.createTextNode(String.valueOf(((MultiLineTextLabel)item).getTextColour().getRGB()));
					            fontColorNode.appendChild(text4);	
					            
					            Element backColorNode = doc.createElement("back_color");
					            child.appendChild(backColorNode);
					            Text text5 = doc.createTextNode(String.valueOf(((MultiLineTextLabel)item).getBackgroundColour().getRGB()));
					            backColorNode.appendChild(text5);
					            
					            Element borderColorNode = doc.createElement("border_color");
					            child.appendChild(borderColorNode);
					            Text text6 = doc.createTextNode(String.valueOf(((MultiLineTextLabel)item).getBorderColour().getRGB()));
					            borderColorNode.appendChild(text6);
					            
					            Element borderSizeNode = doc.createElement("border_size");
					            child.appendChild(borderSizeNode);
					            Text text7 = doc.createTextNode(String.valueOf(((MultiLineTextLabel)item).getBorderSize()));
					            borderSizeNode.appendChild(text7);
							}
							if(child != null){
					            child.appendChild(locXNode);
					            child.appendChild(locYNode);
					            child.appendChild(angleNode);
					            child.appendChild(scaleNode);
							}
						}
						
			            TransformerFactory transfac = TransformerFactory.newInstance();
			            Transformer trans = transfac.newTransformer();
			            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			            trans.setOutputProperty(OutputKeys.INDENT, "yes");

			            //create string from xml tree
			            FileOutputStream fos = new FileOutputStream(new File(logPath));
			            StreamResult result = new StreamResult(fos);
			            DOMSource source = new DOMSource(doc);
			            trans.transform(source, result);
			            fos.close();
					}
				}catch(Exception exp){
					exp.printStackTrace();
				}
			}
			
		}).start();
	}
	
	/**
	 * Stop state recording.
	 */
	public void stopStateRecording(){
		this.recordEnabled = false;
	}
	
}
