package core;

import ui.UITextureType;

public class RecipeItemData {
	public int amount = 0;
	public UITextureType type;

	public RecipeItemData(int newAmount, UITextureType newType) {
		this.amount = newAmount;
		this.type = newType;
	}
}
