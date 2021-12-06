package core;

public class Cell {
	private Index index;
	private String texture;

	public Cell(Index newIndex, String newTexture) {
		index = newIndex;
		texture = newTexture;
	}

	public Index getIndex() {
		return index;
	}

	public void setIndex(Index index) {
		this.index = index;
	}

	public String getTexture() {
		return texture;
	}

	public void setTexture(String texture) {
		this.texture = texture;
	}

}
