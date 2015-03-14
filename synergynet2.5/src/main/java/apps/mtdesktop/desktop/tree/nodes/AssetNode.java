package apps.mtdesktop.desktop.tree.nodes;

import java.io.File;
import java.util.UUID;

import javax.swing.tree.DefaultMutableTreeNode;

public abstract class AssetNode extends DefaultMutableTreeNode{
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = -6987549845971451228L;
	protected String id;
	protected File assetFile;
	protected boolean transferComplete = false;
	
	public AssetNode(File assetFile){
		id = UUID.randomUUID().toString();
		this.assetFile = assetFile;
		this.setAssetFile(assetFile);
	}

	public void setAssetFile(File file){
		this.assetFile = file;
		this.setUserObject(this.getFileName());
		
	}
	
	public File getAssetFile(){
		return assetFile;
	}
	
	 private String getFileName(){
		 if(assetFile != null)
			 return assetFile.getName().substring(0, assetFile.getName().lastIndexOf("."));
		 return null;
	 }
	 
	 public void setNodeTitle(String title){
		 this.setUserObject(title);
	 }
	
	 public void setAssetId(String id){
		 this.id = id;
	 }
	 
	 public String getAssetId(){
		 return id;
	 }
	 
	public abstract javax.swing.Icon getIcon();
}
