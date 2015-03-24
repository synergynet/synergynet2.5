package apps.mtdesktop.desktop.tree.nodes;

import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import apps.mtdesktop.MTDesktopConfigurations;

/**
 * The Class PdfNode.
 */
public class PdfNode extends AssetNode {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -186252460659304007L;
	
	/**
	 * Instantiates a new pdf node.
	 *
	 * @param assetFile
	 *            the asset file
	 */
	public PdfNode(File assetFile) {
		super(assetFile);
	}
	
	/*
	 * (non-Javadoc)
	 * @see apps.mtdesktop.desktop.tree.nodes.AssetNode#getIcon()
	 */
	public Icon getIcon() {
		return new ImageIcon(
				MTDesktopConfigurations.class
						.getResource("desktop/treeicons/pdf.png"));
	}
	
}