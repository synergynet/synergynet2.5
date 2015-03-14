package apps.mtdesktop.desktop.tree.nodes;

import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import apps.mtdesktop.MTDesktopConfigurations;

public class PdfNode extends AssetNode{

	private static final long serialVersionUID = -186252460659304007L;

	public PdfNode(File assetFile) {
		super(assetFile);
	}

	public Icon getIcon(){
		 return new ImageIcon(MTDesktopConfigurations.class.getResource("desktop/treeicons/pdf.png"));
	 }
	 

	}