package synergynetframework.appsystem.table.appregistry.groupcontrol;

public class GroupBoundaries {
	
	public int minX = Integer.MAX_VALUE;
	public int minY = Integer.MAX_VALUE;
	public int maxX = Integer.MIN_VALUE;
	public int maxY = Integer.MIN_VALUE;

	public GroupBoundaries(){}
	
	public GroupBoundaries(long cursorId, int minX, int minY, int maxX, int maxY){
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
	}


	public int getMinX() {
		return minX;
	}

	public void setMinX(int minX) {
		this.minX = minX;
	}

	public int getMinY() {
		return minY;
	}

	public void setMinY(int minY) {
		this.minY = minY;
	}

	public int getMaxX() {
		return maxX;
	}

	public void setMaxX(int maxX) {
		this.maxX = maxX;
	}

	public int getMaxY() {
		return maxY;
	}

	public void setMaxY(int maxY) {
		this.maxY = maxY;
	}
}
