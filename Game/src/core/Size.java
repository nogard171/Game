package core;

public class Size {
	private int width = 0;
	private int height = 0;
	private int depth = 0;

	public Size(int nw, int nh, int nd) {
		width = nw;
		height = nh;
		depth = nd;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getDepth() {
		return depth;
	}
}
