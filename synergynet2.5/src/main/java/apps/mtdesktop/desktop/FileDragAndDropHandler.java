package apps.mtdesktop.desktop;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import apps.mtdesktop.desktop.tree.TabletopTree;
import apps.mtdesktop.desktop.tree.nodes.OutboxNode;




public class FileDragAndDropHandler extends TransferHandler {
    
	  /**
	 * 
	 */
	private static final long serialVersionUID = -371630949779707923L;

	
	@SuppressWarnings("rawtypes")
	public boolean importData(JComponent comp, Transferable t) {
    // Make sure we have the right starting points
    if (!(comp instanceof TabletopTree)) {
      return false;
    }
    if (!t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
      return false;
    }
    
    // Grab the tree, its model and the root node
    TabletopTree tree = (TabletopTree)comp;
    DropLocation dropLocation = (DropLocation) tree.getDropLocation();
    Point p = dropLocation.getDropPoint();
    TreePath path = tree.getClosestPathForLocation(p.x, p.y);
    DefaultMutableTreeNode dropNode = (DefaultMutableTreeNode) path.getLastPathComponent();
    if(dropNode == null)	  return false;
    if(!(dropNode instanceof OutboxNode)){
    	System.out.println("Not a peer node!");
    	return false;
    }
    try {
      List data = (List)t.getTransferData(DataFlavor.javaFileListFlavor);
      Iterator i = data.iterator();
      while (i.hasNext()) {
        File f = (File)i.next();
        tree.getController().addFileNode(dropNode, f);
      }
      return true;
    }
    catch (UnsupportedFlavorException ufe) {
      System.err.println("Ack! we should not be here.\nBad Flavor.");
    }
    catch (IOException ioe) {
		System.out.println("upload complete");
    }
    return false;
  }
  
// We only support file lists on FSTrees...
  public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
    if (comp instanceof TabletopTree) {
      for (int i = 0; i < transferFlavors.length; i++) {
        if (!transferFlavors[i].equals(DataFlavor.javaFileListFlavor)) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

}
