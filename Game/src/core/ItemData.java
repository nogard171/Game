package core;

import java.util.LinkedList;

import ui.UITextureType;

public class ItemData {

	public ItemType type = ItemType.NONE;
	public UITextureType texture = UITextureType.BLANK;
	public boolean stackable = false;
	public String description = "";
	public LinkedList<String> attr = new LinkedList<String>();

	public String getDescription() {
		
		return description;
	}

	public UITextureType getTexture() {
		return texture;
	}

	public void setTexture(UITextureType texture) {
		this.texture = texture;
	}

	public ItemData(ItemType newType) {
		type = newType;
	}

	public ItemData(ItemType newType, String newDescription) {
		type = newType;
		description = newDescription;
	}

	public ItemData(ItemType newType, boolean isStackable) {
		type = newType;
		stackable = isStackable;
	}

	public ItemData(ItemType newType, String newDescription, boolean isStackable) {
		type = newType;
		description = newDescription;
		stackable = isStackable;
	}

	public Item toItem() {
		Item newItem = new Item(type);
		newItem.setTexture(texture);
		return newItem;
	}
}
