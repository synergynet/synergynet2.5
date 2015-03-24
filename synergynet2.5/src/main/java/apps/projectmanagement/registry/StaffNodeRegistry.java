package apps.projectmanagement.registry;

import java.util.ArrayList;
import java.util.List;

import apps.projectmanagement.component.staffnode.StaffNode;

/**
 * The Class StaffNodeRegistry.
 */
public class StaffNodeRegistry {
	
	/** The Constant INSTANCE. */
	private static final StaffNodeRegistry INSTANCE = new StaffNodeRegistry();
	
	/**
	 * Gets the single instance of StaffNodeRegistry.
	 *
	 * @return single instance of StaffNodeRegistry
	 */
	public static StaffNodeRegistry getInstance() {
		return INSTANCE;
	}
	
	/** The nodes. */
	protected List<StaffNode> nodes = new ArrayList<StaffNode>();
	
	/**
	 * Instantiates a new staff node registry.
	 */
	private StaffNodeRegistry() {
	}
	
	/**
	 * Adds the node.
	 *
	 * @param node
	 *            the node
	 */
	public void addNode(StaffNode node) {
		nodes.add(node);
	}

	/**
	 * Gets the nodes.
	 *
	 * @return the nodes
	 */
	public List<StaffNode> getNodes() {
		return nodes;
	}

	/**
	 * Hide nodes.
	 */
	public void hideNodes() {
		for (StaffNode node : nodes) {
			node.setVisibility(false);
		}
	}
	
	/**
	 * Removes the node.
	 *
	 * @param node
	 *            the node
	 */
	public void removeNode(StaffNode node) {
		nodes.remove(node);
	}
	
	/**
	 * Sets the nodes.
	 *
	 * @param nodes
	 *            the new nodes
	 */
	public void setNodes(List<StaffNode> nodes) {
		this.nodes = nodes;
	}
	
	/**
	 * Show nodes.
	 */
	public void showNodes() {
		for (StaffNode node : nodes) {
			node.setVisibility(true);
		}
		
	}
	
}
