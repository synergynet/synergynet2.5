package synergynetframework.appsystem.table.appregistry.groupcontrol;

/**
 * The Class GroupBoundaries.
 */
public class GroupBoundaries {

	/** The max x. */
	public int maxX = Integer.MIN_VALUE;

	/** The max y. */
	public int maxY = Integer.MIN_VALUE;

	/** The min x. */
	public int minX = Integer.MAX_VALUE;

	/** The min y. */
	public int minY = Integer.MAX_VALUE;
	
	/**
	 * Instantiates a new group boundaries.
	 */
	public GroupBoundaries() {
	}

	/**
	 * Instantiates a new group boundaries.
	 *
	 * @param cursorId
	 *            the cursor id
	 * @param minX
	 *            the min x
	 * @param minY
	 *            the min y
	 * @param maxX
	 *            the max x
	 * @param maxY
	 *            the max y
	 */
	public GroupBoundaries(long cursorId, int minX, int minY, int maxX, int maxY) {
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
	}
	
	/**
	 * Gets the max x.
	 *
	 * @return the max x
	 */
	public int getMaxX() {
		return maxX;
	}
	
	/**
	 * Gets the max y.
	 *
	 * @return the max y
	 */
	public int getMaxY() {
		return maxY;
	}
	
	/**
	 * Gets the min x.
	 *
	 * @return the min x
	 */
	public int getMinX() {
		return minX;
	}
	
	/**
	 * Gets the min y.
	 *
	 * @return the min y
	 */
	public int getMinY() {
		return minY;
	}
	
	/**
	 * Sets the max x.
	 *
	 * @param maxX
	 *            the new max x
	 */
	public void setMaxX(int maxX) {
		this.maxX = maxX;
	}
	
	/**
	 * Sets the max y.
	 *
	 * @param maxY
	 *            the new max y
	 */
	public void setMaxY(int maxY) {
		this.maxY = maxY;
	}
	
	/**
	 * Sets the min x.
	 *
	 * @param minX
	 *            the new min x
	 */
	public void setMinX(int minX) {
		this.minX = minX;
	}
	
	/**
	 * Sets the min y.
	 *
	 * @param minY
	 *            the new min y
	 */
	public void setMinY(int minY) {
		this.minY = minY;
	}
}
