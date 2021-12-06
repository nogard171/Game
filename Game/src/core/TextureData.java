package core;

import java.awt.Rectangle;

public class TextureData {
	public int width = 0;
	public int height = 0;

	public int textureX = 0;
	public int textureY = 0;
	public int textureWidth = 0;
	public int textureHeight = 0;

	public int centerX = 0;
	public int centerY = 0;

	public TextureData(int tempx, int tempy, int tempwidth, int tempheight, int tempTextureWidth,
			int tempTextureHeight) {
		this.textureX = tempx;
		this.textureY = tempy;
		this.width = tempwidth;
		this.height = tempheight;
		this.textureWidth = tempTextureWidth;
		this.textureHeight = tempTextureHeight;
	}

	public TextureData(int tempx, int tempy, int tempwidth, int tempheight, int tempTextureWidth, int tempTextureHeight,
			int tempcenterx, int tempcentery) {
		this.textureX = tempx;
		this.textureY = tempy;
		this.width = tempwidth;
		this.height = tempheight;
		this.textureWidth = tempTextureWidth;
		this.textureHeight = tempTextureHeight;
		this.centerX = tempcenterx;
		this.centerY = tempcentery;
	}
}
