package apps.mtdesktop.desktop.tree;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.TextExtractor;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.RapidNetworkManager;
import apps.mtdesktop.MTDesktopConfigurations;
import apps.mtdesktop.desktop.DesktopClient;
import apps.mtdesktop.desktop.Utility;
import apps.mtdesktop.desktop.tree.nodes.AssetNode;
import apps.mtdesktop.desktop.tree.nodes.ImageNode;
import apps.mtdesktop.desktop.tree.nodes.InboxNode;
import apps.mtdesktop.desktop.tree.nodes.OutboxNode;
import apps.mtdesktop.desktop.tree.nodes.PdfNode;
import apps.mtdesktop.desktop.tree.nodes.TextNode;
import apps.mtdesktop.desktop.tree.nodes.VideoNode;
import apps.mtdesktop.fileutility.AssetRegistry;
import apps.mtdesktop.fileutility.FileTransferListener;
import apps.mtdesktop.messages.DeleteContentMessage;
import apps.mtdesktop.messages.HtmlContentMessage;
import apps.mtdesktop.tabletop.MTTableClient;

/**
 * The Class TabletopTreeController.
 */
public class TabletopTreeController implements FileTransferListener {

	/**
	 * The Class FileNodeInfo.
	 */
	private class FileNodeInfo {

		/** The asset id. */
		public String assetId;

		/** The drop node. */
		public DefaultMutableTreeNode dropNode;

		/**
		 * Instantiates a new file node info.
		 *
		 * @param dropNode
		 *            the drop node
		 * @param assetId
		 *            the asset id
		 */
		public FileNodeInfo(DefaultMutableTreeNode dropNode, String assetId) {
			this.dropNode = dropNode;
			this.assetId = assetId;
		}
	}

	/** The clipboard. */
	protected Clipboard clipboard;

	/** The file node map. */
	private Map<File, FileNodeInfo> fileNodeMap = new HashMap<File, FileNodeInfo>();

	/** The html flavor. */
	private DataFlavor rtfFlavor, htmlFlavor;

	/** The selected node. */
	protected DefaultMutableTreeNode selectedNode;
	
	/** The tree. */
	protected TabletopTree tree;
	
	/**
	 * Instantiates a new tabletop tree controller.
	 *
	 * @param tree
	 *            the tree
	 */
	public TabletopTreeController(TabletopTree tree) {
		this.tree = tree;
		clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		rtfFlavor = new DataFlavor("text/rtf", "rtf");
		htmlFlavor = new DataFlavor(
				"text/html;representationclass=java.io.InputStream;charset=windows-1252",
				"html");
		AssetRegistry.getInstance().addFileTransferListener(this);
	}
	
	/**
	 * Adds the file node.
	 *
	 * @param dropNode
	 *            the drop node
	 * @param f
	 *            the f
	 */
	public void addFileNode(DefaultMutableTreeNode dropNode, final File f) {
		if ((dropNode == null) || (f == null)) {
			return;
		}
		if (fileNodeMap.containsKey(f)) {
			JOptionPane
					.showMessageDialog(
							tree,
							"The chosen file is currently being upload to the tabletop",
							"File Transfer Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		final String assetId = UUID.randomUUID().toString();
		fileNodeMap.put(f, new FileNodeInfo(dropNode, assetId));
		
		new Thread(new Runnable() {
			public void run() {
				try {
					AssetRegistry.getInstance().registerAsset(
							DesktopClient.File_SERVER_URL
									+ MTDesktopConfigurations.FTP_SERVLET_PATH,
							assetId, f);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(
							tree,
							"Cannot upload file to the tabletop: "
									+ e.getMessage(), "File transfer error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}).start();
		
	}
	
	/**
	 * Adds the image node.
	 *
	 * @param dropNode
	 *            the drop node
	 * @param img
	 *            the img
	 */
	private void addImageNode(final DefaultMutableTreeNode dropNode,
			final Image img) {
		if ((dropNode == null) || (img == null)) {
			return;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					int maxDelay = 100000; // max delay to wait
					File temp = new File(
							MTDesktopConfigurations.tabletopTempFolder + "/"
									+ UUID.randomUUID().toString() + ".jpg");
					ImageIO.write(Utility.toBufferedImage(img), "jpg", temp);
					int delay = 0;
					while ((delay < maxDelay) && !temp.exists()) {
						Thread.sleep(1000);
						delay += 1000;
					}
					if (temp.exists()) {
						String assetId = UUID.randomUUID().toString();
						AssetRegistry
								.getInstance()
								.registerAsset(
										DesktopClient.File_SERVER_URL
												+ MTDesktopConfigurations.FTP_SERVLET_PATH,
										assetId, temp);
						temp.deleteOnExit();
					}
				} catch (IOException exp) {
					JOptionPane.showMessageDialog(
							tree,
							"Cannot add image to the tabletop: "
									+ exp.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
					
				} catch (InterruptedException e) {
					JOptionPane.showMessageDialog(
							tree,
							"Cannot add image to the tabletop: "
									+ e.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}).start();
	}
	
	/**
	 * Adds the text node.
	 *
	 * @param dropNode
	 *            the drop node
	 * @param s
	 *            the s
	 * @param title
	 *            the title
	 */
	private void addTextNode(DefaultMutableTreeNode dropNode, String s,
			String title) {
		if ((dropNode == null) || (s == null)) {
			return;
		}
		try {
			String assetId = UUID.randomUUID().toString();
			RapidNetworkManager.getTableCommsClientService().sendMessage(
					(new HtmlContentMessage(MTTableClient.class,
							DesktopClient.tableId, assetId, s)));
			TextNode node = new TextNode(s);
			node.setNodeTitle(title);
			node.setAssetId(assetId);
			dropNode.add(node);
			((DefaultTreeModel) tree.getModel()).reload();
			tree.expandRow(dropNode.getLevel());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(tree,
					"Cannot add text to the tabletop: " + e.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Delete selected.
	 *
	 * @param paths
	 *            the paths
	 */
	public void deleteSelected(TreePath[] paths) {
		if ((paths != null) && (paths.length > 0)) {
			int choice = 0;
			choice = JOptionPane.showConfirmDialog(tree,
					"Are you sure you want to delete the selected items?");
			if (choice == 0) {
				for (TreePath path : paths) {
					MutableTreeNode mNode = (MutableTreeNode) path
							.getLastPathComponent();
					AssetNode assetNode = (AssetNode) mNode;
					MutableTreeNode parentNode = (MutableTreeNode) mNode
							.getParent();
					if (parentNode instanceof OutboxNode) {
						try {
							RapidNetworkManager.getTableCommsClientService()
									.sendMessage(
											(new DeleteContentMessage(
													MTTableClient.class,
													DesktopClient.tableId,
													assetNode.getAssetId())));
							((DefaultTreeModel) tree.getModel())
									.removeNodeFromParent(mNode);
						} catch (IOException e) {
							JOptionPane.showMessageDialog(tree,
									"Cannot delete item from the tabletop: "
											+ e.getMessage(), "Delete error",
									JOptionPane.ERROR_MESSAGE);
						}
					} else if (parentNode instanceof InboxNode) {
						File f = assetNode.getAssetFile();
						if (f != null) {
							f.delete();
						}
						((DefaultTreeModel) tree.getModel())
								.removeNodeFromParent(mNode);
						updateInboxNode();
					}
				}
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * apps.mtdesktop.fileutility.FileTransferListener#fileDownloadCompleted
	 * (java.io.File)
	 */
	@Override
	public void fileDownloadCompleted(File file) {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * apps.mtdesktop.fileutility.FileTransferListener#fileDownloadFailed(java
	 * .io.File, java.lang.String)
	 */
	@Override
	public void fileDownloadFailed(File file, String responseMessage) {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * apps.mtdesktop.fileutility.FileTransferListener#fileDownloadStarted(java
	 * .io.File)
	 */
	@Override
	public void fileDownloadStarted(File file) {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * apps.mtdesktop.fileutility.FileTransferListener#fileUploadCompleted(java
	 * .io.File)
	 */
	@Override
	public void fileUploadCompleted(File file) {
		if (fileNodeMap.containsKey(file)) {
			String ext = file.getName().substring(
					file.getName().lastIndexOf(".") + 1);
			if (ext.equalsIgnoreCase("bmp") || ext.equalsIgnoreCase("jpg")
					|| ext.equalsIgnoreCase("jpeg")
					|| ext.equalsIgnoreCase("png")
					|| ext.equalsIgnoreCase("gif")
					|| ext.equalsIgnoreCase("tiff")) {
				try {
					Image img = ImageIO.read(file);
					insertImageTreeNode(fileNodeMap.get(file).dropNode,
							fileNodeMap.get(file).assetId, img);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				insertFileTreeNode(fileNodeMap.get(file).dropNode,
						fileNodeMap.get(file).assetId, file);
			}
			fileNodeMap.remove(file);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * apps.mtdesktop.fileutility.FileTransferListener#fileUploadFailed(java
	 * .io.File, java.lang.String)
	 */
	@Override
	public void fileUploadFailed(File file, String responseMessage) {
		if (fileNodeMap.containsKey(file)) {
			fileNodeMap.remove(file);
		}
		JOptionPane.showMessageDialog(tree, responseMessage, "Upload error",
				JOptionPane.ERROR_MESSAGE);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * apps.mtdesktop.fileutility.FileTransferListener#fileUploadStarted(java
	 * .io.File)
	 */
	@Override
	public void fileUploadStarted(File file) {
		
	}
	
	/**
	 * Gets the clipboard.
	 *
	 * @return the clipboard
	 */
	public Clipboard getClipboard() {
		return clipboard;
	}
	
	/**
	 * Insert file tree node.
	 *
	 * @param dropNode
	 *            the drop node
	 * @param assetId
	 *            the asset id
	 * @param f
	 *            the f
	 */
	private void insertFileTreeNode(DefaultMutableTreeNode dropNode,
			String assetId, File f) {
		int mid = f.getName().lastIndexOf(".");
		String ext = f.getName().substring(mid + 1);
		AssetNode node;
		if (ext.equalsIgnoreCase("pdf")) {
			node = new PdfNode(f);
			node.setAssetId(assetId);
			dropNode.add(node);
		} else if (ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("bmp")
				|| ext.equalsIgnoreCase("png") || ext.equalsIgnoreCase("jpeg")
				|| ext.equalsIgnoreCase("gif") || ext.equalsIgnoreCase("tiff")) {
			node = new ImageNode(f);
			node.setAssetId(assetId);
			dropNode.add(node);
		} else if (ext.equalsIgnoreCase("mpg") || ext.equalsIgnoreCase("mpeg")
				|| ext.equalsIgnoreCase("gif") || ext.equalsIgnoreCase("avi")) {
			node = new VideoNode(f);
			node.setAssetId(assetId);
			dropNode.add(node);
		} else if (ext.equalsIgnoreCase("txt")) {
			node = new TextNode(f);
			node.setAssetId(assetId);
			dropNode.add(node);
		} else {
			JOptionPane.showMessageDialog(tree, "Unsupported file type",
					"Error", JOptionPane.ERROR_MESSAGE);
		}
		((DefaultTreeModel) tree.getModel()).reload();
		tree.expandRow(dropNode.getLevel());
	}
	
	/**
	 * Insert image tree node.
	 *
	 * @param dropNode
	 *            the drop node
	 * @param assetId
	 *            the asset id
	 * @param img
	 *            the img
	 */
	private void insertImageTreeNode(DefaultMutableTreeNode dropNode,
			String assetId, Image img) {
		
		// create a name for the image including its number
		int count = 1;
		for (int i = 0; i < dropNode.getChildCount(); i++) {
			if (dropNode.getChildAt(i) instanceof ImageNode) {
				count++;
			}
		}
		ImageNode imgNode = new ImageNode(img);
		imgNode.setNodeTitle("Image " + count);
		imgNode.setAssetId(assetId);
		dropNode.add(imgNode);
		((DefaultTreeModel) tree.getModel()).reload();
		tree.expandRow(dropNode.getLevel());
	}
	
	/**
	 * Process delete.
	 */
	public void processDelete() {
		if (selectedNode != null) {
			TreePath[] paths = new TreePath[selectedNode.getChildCount()];
			for (int i = 0; i < selectedNode.getChildCount(); i++) {
				paths[i] = new TreePath(selectedNode.getChildAt(i));
			}
			this.deleteSelected(paths);
		}
	}
	
	/**
	 * Process paste.
	 */
	@SuppressWarnings("rawtypes")
	public void processPaste() {
		
		Transferable clipData = clipboard.getContents(clipboard);
		for (int i = 0; i < clipData.getTransferDataFlavors().length; i++) {
			System.out.println(clipData.getTransferDataFlavors()[i]);
		}
		if ((clipData != null) && (selectedNode != null)) {
			try {
				if (clipData.isDataFlavorSupported(rtfFlavor)) {
					Object transfertData = clipboard.getContents(this)
							.getTransferData(rtfFlavor);
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					InputStream in = (InputStream) transfertData;
					int c;
					String temp = "";
					while ((c = in.read()) != -1) {
						out.write(c);
						temp += (char) c;
					}
					String s = Utility.rtfToHtml(new StringReader(temp));
					TextExtractor exct = new TextExtractor(new Source(s));
					String title = exct.toString();
					if (title.length() > 20) {
						title = title.substring(0, 20) + "..";
					}
					this.addTextNode(selectedNode, s, title);
					in.close();
				} else if (clipData
						.isDataFlavorSupported(DataFlavor.imageFlavor)) {
					Image img = (Image) clipData
							.getTransferData(DataFlavor.imageFlavor);
					this.addImageNode(selectedNode, img);
					
				} else if (clipData.isDataFlavorSupported(htmlFlavor)) {
					Object transfertData = clipboard.getContents(this)
							.getTransferData(htmlFlavor);
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					InputStream in = (InputStream) transfertData;
					int c;
					String temp = "";
					while ((c = in.read()) != -1) {
						out.write(c);
						temp += (char) c;
					}
					TextExtractor exct = new TextExtractor(new Source(temp));
					if (exct.toString().trim().equals("")) {
						String s = (String) (clipData
								.getTransferData(DataFlavor.stringFlavor));
						String title = s;
						if (title.length() > 20) {
							title = title.substring(0, 20) + "..";
						}
						this.addTextNode(selectedNode, s, title);
					} else {
						String title = exct.toString();
						if (title.length() > 20) {
							title = title.substring(0, 20) + "..";
						}
						this.addTextNode(selectedNode, temp, title);
					}
					in.close();
				} else if (clipData
						.isDataFlavorSupported(DataFlavor.stringFlavor)) {
					String s = (String) (clipData
							.getTransferData(DataFlavor.stringFlavor));
					String title = s;
					if (title.length() > 20) {
						title = title.substring(0, 20) + "..";
					}
					this.addTextNode(selectedNode, s, title);
				} else if (clipData
						.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
					List data = (List) clipData
							.getTransferData(DataFlavor.javaFileListFlavor);
					Iterator i = data.iterator();
					while (i.hasNext()) {
						File f = (File) i.next();
						this.addFileNode(selectedNode, f);
					}
				}
				
			} catch (UnsupportedFlavorException ufe) {
				JOptionPane.showMessageDialog(tree, "Unsupported Data Type : "
						+ ufe.getMessage(), "Error Copying Data",
						JOptionPane.ERROR_MESSAGE);
			} catch (IOException ioe) {
				JOptionPane
						.showMessageDialog(
								tree,
								"Error copying data to the table : "
										+ ioe.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/**
	 * Sets the selected node.
	 *
	 * @param selectedNode
	 *            the new selected node
	 */
	public void setSelectedNode(DefaultMutableTreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}
	
	/**
	 * Update inbox node.
	 */
	public void updateInboxNode() {
		// delete all nodes
		InboxNode node = tree.getInboxNode(DesktopClient.tableId);
		TreePath[] paths = new TreePath[node.getChildCount()];
		for (int i = 0; i < node.getChildCount(); i++) {
			paths[i] = new TreePath(node.getChildAt(i));
			MutableTreeNode mNode = (MutableTreeNode) paths[i]
					.getLastPathComponent();
			((DefaultTreeModel) tree.getModel()).removeNodeFromParent(mNode);
		}
		
		// import files
		File dir = new File(MTDesktopConfigurations.inboxFolder);
		File[] files = dir.listFiles();
		if (files == null) {
			JOptionPane.showMessageDialog(tree,
					"Unable to read from inbox folder", "Error",
					JOptionPane.ERROR_MESSAGE);
		} else {
			int count = 0;
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile()) {
					insertFileTreeNode(node, UUID.randomUUID().toString(),
							files[i]);
					count++;
				}
			}
			node.setUserObject("Inbox (" + count + ")");
		}
	}
	
}
