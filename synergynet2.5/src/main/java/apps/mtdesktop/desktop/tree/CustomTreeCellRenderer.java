package apps.mtdesktop.desktop.tree;

import java.awt.Component;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import apps.mtdesktop.desktop.tree.nodes.AssetNode;
import apps.mtdesktop.desktop.tree.nodes.InboxNode;
import apps.mtdesktop.desktop.tree.nodes.OutboxNode;
import apps.mtdesktop.desktop.tree.nodes.TopNode;



public class CustomTreeCellRenderer extends DefaultTreeCellRenderer{
	
 /**
	 * 
	 */
	private static final long serialVersionUID = 9074590081824475875L;

public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {

	
	 if((value instanceof AssetNode) && (value != null)) {
		 setIcon(((AssetNode)value).getIcon());
	  }else if((value instanceof OutboxNode) && (value != null)){
		 setIcon(((OutboxNode)value).getIcon());
	  }else if((value instanceof InboxNode) && (value != null)){
		 setIcon(((InboxNode)value).getIcon());
	  }else if((value instanceof TopNode) && (value != null)){
			 setIcon(((TopNode)value).getIcon());
	  }

 String stringValue = tree.convertValueToText(value, sel,expanded, leaf, row, hasFocus);

 this.hasFocus = hasFocus;
 setText(stringValue);
 if(sel)
   setForeground(getTextSelectionColor());
 else
   setForeground(getTextNonSelectionColor());

 if (!tree.isEnabled()) {
   setEnabled(false);
 }
 else {
   setEnabled(true);
 }
  setComponentOrientation(tree.getComponentOrientation());
 selected = sel;
 return this;

}
}