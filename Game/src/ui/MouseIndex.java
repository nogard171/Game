package ui;

import classes.Index;

public class MouseIndex extends Index {
	private int chunkX;
	private int chunkY;

	public int getChunkX() {
		return chunkX;
	}

	public void setChunkX(int chunkX) {
		this.chunkX = chunkX;
	}

	public int getChunkY() {
		return chunkY;
	}

	public void setChunkY(int chunkY) {
		this.chunkY = chunkY;
	}

	public MouseIndex(int newX, int newY) {
		super(newX, newY);

	}

	public MouseIndex(int newX, int newY, int newChunkX, int newChunkY) {
		super(newX, newY);
		this.chunkX = newChunkX;
		this.chunkY = newChunkY;
	}

}
