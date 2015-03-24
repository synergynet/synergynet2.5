package apps.projectmanagement.registry;

import java.util.ArrayList;
import java.util.List;

import apps.projectmanagement.component.workflowchart.core.graphcomponents.links.GraphLink;
import apps.projectmanagement.component.workflowchart.core.graphcomponents.nodes.GraphNode;



/**
 * The Class WorkflowRegistry.
 */
public class WorkflowRegistry {

    /** The Constant INSTANCE. */
    private static final WorkflowRegistry INSTANCE = new WorkflowRegistry();
    
    /** The nodes. */
    protected List<GraphNode> nodes = new ArrayList<GraphNode>();
    
    /** The links. */
    protected List<GraphLink> links = new ArrayList<GraphLink>();
    
    /**
     * Instantiates a new workflow registry.
     */
    private WorkflowRegistry() {
    }
    
    /**
     * Gets the nodes.
     *
     * @return the nodes
     */
    public List<GraphNode> getNodes() {
		return nodes;
	}

	/**
	 * Sets the nodes.
	 *
	 * @param nodes the new nodes
	 */
	public void setNodes(List<GraphNode> nodes) {
		this.nodes = nodes;
	}

	/**
	 * Gets the links.
	 *
	 * @return the links
	 */
	public List<GraphLink> getLinks() {
		return links;
	}

	/**
	 * Sets the links.
	 *
	 * @param links the new links
	 */
	public void setLinks(List<GraphLink> links) {
		this.links = links;
	}
	
	/**
	 * Adds the node.
	 *
	 * @param node the node
	 */
	public void addNode(GraphNode node){
		nodes.add(node);
	}
	
	/**
	 * Removes the node.
	 *
	 * @param node the node
	 */
	public void removeNode(GraphNode node){
		nodes.remove(node);
	}
	
	/**
	 * Adds the link.
	 *
	 * @param link the link
	 */
	public void addLink(GraphLink link){
		links.add(link);
	}
	
	/**
	 * Removes the link.
	 *
	 * @param link the link
	 */
	public void removeLink(GraphLink link){
		links.remove(link);
	}

    /**
     * Gets the single instance of WorkflowRegistry.
     *
     * @return single instance of WorkflowRegistry
     */
    public static WorkflowRegistry getInstance() {
        return INSTANCE;
    }
    
    /**
     * Hide workflow.
     */
    public void hideWorkflow(){
    	for (GraphNode node: nodes){
    		node.setVisible(false);
    	}
    	for (GraphLink link: links){
    		link.setVisiblity(false);
    	}
    }
    
    /**
     * Show workflow.
     */
    public void showWorkflow(){
    	for (GraphNode node: nodes){
    		node.setVisible(true);
    	}
    	for (GraphLink link: links){
    		link.setVisiblity(true);
    	}
    }
    
    /**
     * Sets the editable.
     *
     * @param b the new editable
     */
    public void setEditable(boolean b){
    	
    	for (GraphNode node: nodes){
    		node.setEditable(b);
    	}
    	for (GraphLink link: links){
    		link.setEditable(b);
    	}
    	
    }
  
}
