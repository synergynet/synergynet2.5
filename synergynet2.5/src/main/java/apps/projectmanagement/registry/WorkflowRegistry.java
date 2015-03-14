package apps.projectmanagement.registry;

import java.util.ArrayList;
import java.util.List;

import apps.projectmanagement.component.workflowchart.core.graphcomponents.links.GraphLink;
import apps.projectmanagement.component.workflowchart.core.graphcomponents.nodes.GraphNode;


public class WorkflowRegistry {

    private static final WorkflowRegistry INSTANCE = new WorkflowRegistry();
    
    protected List<GraphNode> nodes = new ArrayList<GraphNode>();
    protected List<GraphLink> links = new ArrayList<GraphLink>();
    
    private WorkflowRegistry() {
    }
    
    public List<GraphNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<GraphNode> nodes) {
		this.nodes = nodes;
	}

	public List<GraphLink> getLinks() {
		return links;
	}

	public void setLinks(List<GraphLink> links) {
		this.links = links;
	}
	
	public void addNode(GraphNode node){
		nodes.add(node);
	}
	
	public void removeNode(GraphNode node){
		nodes.remove(node);
	}
	
	public void addLink(GraphLink link){
		links.add(link);
	}
	
	public void removeLink(GraphLink link){
		links.remove(link);
	}

    public static WorkflowRegistry getInstance() {
        return INSTANCE;
    }
    
    public void hideWorkflow(){
    	for (GraphNode node: nodes){
    		node.setVisible(false);
    	}
    	for (GraphLink link: links){
    		link.setVisiblity(false);
    	}
    }
    
    public void showWorkflow(){
    	for (GraphNode node: nodes){
    		node.setVisible(true);
    	}
    	for (GraphLink link: links){
    		link.setVisiblity(true);
    	}
    }
    
    public void setEditable(boolean b){
    	
    	for (GraphNode node: nodes){
    		node.setEditable(b);
    	}
    	for (GraphLink link: links){
    		link.setEditable(b);
    	}
    	
    }
  
}
