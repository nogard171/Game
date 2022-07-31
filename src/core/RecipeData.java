package core;

import java.util.ArrayList;

import ui.UITextureType;

public class RecipeData {
	public String name = "";
	public UITextureType outputType;
	public String description = "";
	public ArrayList<RecipeItemData> recipeItems = new ArrayList<RecipeItemData>();
	public String outputItem;
}
