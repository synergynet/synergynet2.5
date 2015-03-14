package apps.mtdesktop.desktop.tree.nodes;

import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import apps.mtdesktop.MTDesktopConfigurations;


public class TextNode extends AssetNode{

	private static final long serialVersionUID = -186252460659304007L;

	protected String text;

	public TextNode(File assetFile) {
		super(assetFile);
	}
	
	public TextNode(String text){
		super(null);
		this.setText(text);
	}

	public void setText(String text){
		this.text = text;
		if(text != null){
			String userObject = text;
			if(text.length() > 20)
				userObject = text.substring(0, 20) + "..";
			this.setUserObject(userObject);
		}
	}
	
	public String getText(){
		return text;
	}
	
	@Override
	public Icon getIcon() {
		 return new ImageIcon(MTDesktopConfigurations.class.getResource("desktop/treeicons/text.gif"));
	}
	 

	}