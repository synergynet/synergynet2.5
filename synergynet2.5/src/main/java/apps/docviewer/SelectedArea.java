package apps.docviewer;

public class SelectedArea {
	
	protected long id;
	protected int x1, y1, x2, y2;
	
	
	public SelectedArea(){}
	
	public SelectedArea(long id, int x1, int y1, int x2, int y2){
		this.id = id;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public float getX1() {
		return x1;
	}

	public void setX1(int x1) {
		this.x1 = x1;
	}

	public float getY1() {
		return y1;
	}

	public void setY1(int y1) {
		this.y1 = y1;
	}

	public float getX2() {
		return x2;
	}

	public void setX2(int x2) {
		this.x2 = x2;
	}

	public float getY2() {
		return y2;
	}

	public void setY2(int y2) {
		this.y2 = y2;
	}
	
	public String toString(){
		return "id = "+id+" , x1 = "+x1+" , y1 = "+y1+" , x2 = "+x2+" , y2 = "+y2;
	}
	
	public float getWidth(){
		return x2-x1;
	}
	
	public float getHeight(){
		return y1-y2;
	}
}
