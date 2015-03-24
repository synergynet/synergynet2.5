package apps.threedpuzzle.buildingblock;

import com.jme.scene.Node;

/**
 * The Class BuildingBlock.
 */
public class BuildingBlock extends Node {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5812346028867024173L;
	
	/** The name. */
	protected String name;

	/**
	 * Instantiates a new building block.
	 *
	 * @param name
	 *            the name
	 */
	public BuildingBlock(String name) {
		this.name = name;
	}

}
