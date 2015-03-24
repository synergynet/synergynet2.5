package apps.mtdesktop.desktop.tree.nodes;

import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import apps.mtdesktop.MTDesktopConfigurations;


/**
 * The Class VideoNode.
 */
public class VideoNode extends AssetNode{

	/**
	 * Instantiates a new video node.
	 *
	 * @param assetFile the asset file
	 */
	public VideoNode(File assetFile) {
		super(assetFile);
	}

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -186252460659304007L;
	
	 /* (non-Javadoc)
 	 * @see apps.mtdesktop.desktop.tree.nodes.AssetNode#getIcon()
 	 */
 	public Icon getIcon(){
		 return new ImageIcon(MTDesktopConfigurations.class.getResource("desktop/treeicons/video.jpg"));
	 }
	}