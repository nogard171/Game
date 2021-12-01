package core;

public class TextureData {
	int x = 0;
	int y = 0;
	int width = 0;
	int height = 0;

	int centerX = 0;
	int centerY = 0;

	public TextureData(int tempx, int tempy, int tempwidth, int tempheight) {
		this.x = tempx;
		this.y = tempy;
		this.width = tempwidth;
		this.height = tempheight;
	}

	public TextureData(int tempx, int tempy, int tempwidth, int tempheight, int tempcenterx, int tempcentery) {
		this.x = tempx;
		this.y = tempy;
		this.width = tempwidth;
		this.height = tempheight;
		this.centerX = tempcenterx;
		this.centerY = tempcentery;
	}
}
