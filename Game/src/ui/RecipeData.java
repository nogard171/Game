package ui;

import java.util.ArrayList;

public class RecipeData {
	public String name = "";
	public ArrayList<RecipeItem> items = new ArrayList<RecipeItem>();
	public String material = "";
	public String requiredSkill = "";
	public int requiredLevel = 0;
	public int minCount = 0;
	public int maxCount = 0;
}
