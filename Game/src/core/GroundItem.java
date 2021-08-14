package core;

import ui.UITextureType;

public class GroundItem extends Tile {
	public UITextureType type = UITextureType.BLANK;
	public ItemType item = ItemType.NONE;
	public int count = 0;

	public GroundItem(TextureType newType) {
		super(newType);
	}

}