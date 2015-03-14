package apps.projectmanagement.registry;

import java.util.ArrayList;
import java.util.List;

import apps.projectmanagement.component.staffnode.StaffNode;


public class StaffNodeRegistry {

    private static final StaffNodeRegistry INSTANCE = new StaffNodeRegistry();
    
    protected List<StaffNode> nodes = new ArrayList<StaffNode>();
    
    private StaffNodeRegistry() {
    }
    
    public List<StaffNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<StaffNode> nodes) {
		this.nodes = nodes;
	}
	
	public void addNode(StaffNode node){
		nodes.add(node);
	}
	
	public void removeNode(StaffNode node){
		nodes.remove(node);
	}

    public static StaffNodeRegistry getInstance() {
        return INSTANCE;
    }
    
    public void hideNodes(){
    	for (StaffNode node: nodes){
    		node.setVisibility(false);
    	}
    }
    
    public void showNodes(){
    	for (StaffNode node: nodes){
    		node.setVisibility(true);
    	}
   
    }
  
}
