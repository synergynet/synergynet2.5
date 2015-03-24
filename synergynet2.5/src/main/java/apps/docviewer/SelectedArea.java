package apps.docviewer;

/**
 * The Class SelectedArea.
 */
public class SelectedArea {

	/** The id. */
	protected long id;

	/** The y2. */
	protected int x1, y1, x2, y2;
	
	/**
	 * Instantiates a new selected area.
	 */
	public SelectedArea() {
	}

	/**
	 * Instantiates a new selected area.
	 *
	 * @param id
	 *            the id
	 * @param x1
	 *            the x1
	 * @param y1
	 *            the y1
	 * @param x2
	 *            the x2
	 * @param y2
	 *            the y2
	 */
	public SelectedArea(long id, int x1, int y1, int x2, int y2) {
		this.id = id;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public float getHeight() {
		return y1 - y2;
	}
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public float getWidth() {
		return x2 - x1;
	}
	
	/**
	 * Gets the x1.
	 *
	 * @return the x1
	 */
	public float getX1() {
		return x1;
	}
	
	/**
	 * Gets the x2.
	 *
	 * @return the x2
	 */
	public float getX2() {
		return x2;
	}
	
	/**
	 * Gets the y1.
	 *
	 * @return the y1
	 */
	public float getY1() {
		return y1;
	}
	
	/**
	 * Gets the y2.
	 *
	 * @return the y2
	 */
	public float getY2() {
		return y2;
	}
	
	/**
	 * Sets the id.
	 *
	 * @param id
	 *            the new id
	 */
	public void setId(long id) {
		this.id = id;
	}
	
	/**
	 * Sets the x1.
	 *
	 * @param x1
	 *            the new x1
	 */
	public void setX1(int x1) {
		this.x1 = x1;
	}
	
	/**
	 * Sets the x2.
	 *
	 * @param x2
	 *            the new x2
	 */
	public void setX2(int x2) {
		this.x2 = x2;
	}

	/**
	 * Sets the y1.
	 *
	 * @param y1
	 *            the new y1
	 */
	public void setY1(int y1) {
		this.y1 = y1;
	}

	/**
	 * Sets the y2.
	 *
	 * @param y2
	 *            the new y2
	 */
	public void setY2(int y2) {
		this.y2 = y2;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "id = " + id + " , x1 = " + x1 + " , y1 = " + y1 + " , x2 = "
				+ x2 + " , y2 = " + y2;
	}
}
