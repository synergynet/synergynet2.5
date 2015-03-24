package apps.mtdesktop.desktop.tree.nodes;

import java.io.File;
import java.util.UUID;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * The Class AssetNode.
 */
public abstract class AssetNode extends DefaultMutableTreeNode {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6987549845971451228L;

	/** The asset file. */
	protected File assetFile;

	/** The id. */
	protected String id;

	/** The transfer complete. */
	protected boolean transferComplete = false;

	/**
	 * Instantiates a new asset node.
	 *
	 * @param assetFile
	 *            the asset file
	 */
	public AssetNode(File assetFile) {
		id = UUID.randomUUID().toString();
		this.assetFile = assetFile;
		this.setAssetFile(assetFile);
	}
	
	/**
	 * Gets the asset file.
	 *
	 * @return the asset file
	 */
	public File getAssetFile() {
		return assetFile;
	}

	/**
	 * Gets the asset id.
	 *
	 * @return the asset id
	 */
	public String getAssetId() {
		return id;
	}
	
	/**
	 * Gets the file name.
	 *
	 * @return the file name
	 */
	private String getFileName() {
		if (assetFile != null) {
			return assetFile.getName().substring(0,
					assetFile.getName().lastIndexOf("."));
		}
		return null;
	}
	
	/**
	 * Gets the icon.
	 *
	 * @return the icon
	 */
	public abstract javax.swing.Icon getIcon();
	
	/**
	 * Sets the asset file.
	 *
	 * @param file
	 *            the new asset file
	 */
	public void setAssetFile(File file) {
		this.assetFile = file;
		this.setUserObject(this.getFileName());

	}
	
	/**
	 * Sets the asset id.
	 *
	 * @param id
	 *            the new asset id
	 */
	public void setAssetId(String id) {
		this.id = id;
	}
	
	/**
	 * Sets the node title.
	 *
	 * @param title
	 *            the new node title
	 */
	public void setNodeTitle(String title) {
		this.setUserObject(title);
	}
}
