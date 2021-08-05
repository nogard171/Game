package core;

import ui.UITextureType;

public class ItemData {

	public ItemType type = ItemType.NONE;
	public UITextureType texture = UITextureType.BLANK;

	public UITextureType getTexture() {
		return texture;
	}

	public void setTexture(UITextureType texture) {
		this.texture = texture;
	}

	public ItemData(ItemType newType) {
		type = newType;
	}

	public Item toItem() {
		Item newItem =  new Item(type);
		newItem.setTexture(texture);
		return newItem;
	}
}
