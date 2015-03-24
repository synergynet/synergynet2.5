package apps.mtdesktop.desktop.tree.nodes;

import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import apps.mtdesktop.MTDesktopConfigurations;

/**
 * The Class TextNode.
 */
public class TextNode extends AssetNode {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -186252460659304007L;
	
	/** The text. */
	protected String text;
	
	/**
	 * Instantiates a new text node.
	 *
	 * @param assetFile
	 *            the asset file
	 */
	public TextNode(File assetFile) {
		super(assetFile);
	}

	/**
	 * Instantiates a new text node.
	 *
	 * @param text
	 *            the text
	 */
	public TextNode(String text) {
		super(null);
		this.setText(text);
	}
	
	/*
	 * (non-Javadoc)
	 * @see apps.mtdesktop.desktop.tree.nodes.AssetNode#getIcon()
	 */
	@Override
	public Icon getIcon() {
		return new ImageIcon(
				MTDesktopConfigurations.class
						.getResource("desktop/treeicons/text.gif"));
	}

	/**
	 * Gets the text.
	 *
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the text.
	 *
	 * @param text
	 *            the new text
	 */
	public void setText(String text) {
		this.text = text;
		if (text != null) {
			String userObject = text;
			if (text.length() > 20) {
				userObject = text.substring(0, 20) + "..";
			}
			this.setUserObject(userObject);
		}
	}
	
}