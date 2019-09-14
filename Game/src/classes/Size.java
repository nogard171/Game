package classes;

public class Size {
	private int width = 0;
	private int height = 0;
	private int depth = 0;

	public Size(int i, int j, int k) {
		this.setWidth(i);
		this.setHeight(j);
		this.setDepth(k);
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}
}
