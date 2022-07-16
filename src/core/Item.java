package core;

import ui.UITextureType;

public class Item {
	private ItemType type = ItemType.NONE;
	public UITextureType texture = UITextureType.BLANK;
	private int count;
	private int maxCount;

	public Item(ItemType newType) {
		this(newType, 1, 1);
	}

	public Item(ItemType newType, int newCount) {
		this(newType, newCount, 1);
	}

	public Item(ItemType newType, int newCount, int newMaxCount) {
		type = newType;
		count = newCount;
		maxCount = newMaxCount;
	}

	public ItemType getType() {
		return type;
	}

	public int getCount() {
		return count;
	}

	public int getMaxCount() {
		return maxCount;
	}

	public UITextureType getTexture() {
		return texture;
	}

	public void setTexture(UITextureType texture) {
		this.texture = texture;
	}

}
