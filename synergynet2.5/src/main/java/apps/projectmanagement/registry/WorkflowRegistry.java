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
	
	/**
	 * Gets the single instance of WorkflowRegistry.
	 *
	 * @return single instance of WorkflowRegistry
	 */
	public static WorkflowRegistry getInstance() {
		return INSTANCE;
	}
	
	/** The links. */
	protected List<GraphLink> links = new ArrayList<GraphLink>();
	
	/** The nodes. */
	protected List<GraphNode> nodes = new ArrayList<GraphNode>();
	
	/**
	 * Instantiates a new workflow registry.
	 */
	private WorkflowRegistry() {
	}
	
	/**
	 * Adds the link.
	 *
	 * @param link
	 *            the link
	 */
	public void addLink(GraphLink link) {
		links.add(link);
	}
	
	/**
	 * Adds the node.
	 *
	 * @param node
	 *            the node
	 */
	public void addNode(GraphNode node) {
		nodes.add(node);
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
	 * Gets the nodes.
	 *
	 * @return the nodes
	 */
	public List<GraphNode> getNodes() {
		return nodes;
	}

	/**
	 * Hide workflow.
	 */
	public void hideWorkflow() {
		for (GraphNode node : nodes) {
			node.setVisible(false);
		}
		for (GraphLink link : links) {
			link.setVisiblity(false);
		}
	}

	/**
	 * Removes the link.
	 *
	 * @param link
	 *            the link
	 */
	public void removeLink(GraphLink link) {
		links.remove(link);
	}

	/**
	 * Removes the node.
	 *
	 * @param node
	 *            the node
	 */
	public void removeNode(GraphNode node) {
		nodes.remove(node);
	}
	
	/**
	 * Sets the editable.
	 *
	 * @param b
	 *            the new editable
	 */
	public void setEditable(boolean b) {
		
		for (GraphNode node : nodes) {
			node.setEditable(b);
		}
		for (GraphLink link : links) {
			link.setEditable(b);
		}
		
	}
	
	/**
	 * Sets the links.
	 *
	 * @param links
	 *            the new links
	 */
	public void setLinks(List<GraphLink> links) {
		this.links = links;
	}
	
	/**
	 * Sets the nodes.
	 *
	 * @param nodes
	 *            the new nodes
	 */
	public void setNodes(List<GraphNode> nodes) {
		this.nodes = nodes;
	}
	
	/**
	 * Show workflow.
	 */
	public void showWorkflow() {
		for (GraphNode node : nodes) {
			node.setVisible(true);
		}
		for (GraphLink link : links) {
			link.setVisiblity(true);
		}
	}
	
}
