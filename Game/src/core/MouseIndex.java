package core;

public class MouseIndex extends Index {
	private int chunkX;
	private int chunkY;
	private int objIndex;

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

	public MouseIndex(int newX, int newY, int newChunkX, int newChunkY, int objectIndex) {
		super(newX, newY);
		this.chunkX = newChunkX;
		this.chunkY = newChunkY;
		this.objIndex = objectIndex;
	}

	public int getObjectIndex() {
		return this.objIndex;
	}

	@Override
	public String toString() {
		return super.getX() + "," + objIndex + "," + super.getY();
	}
}
