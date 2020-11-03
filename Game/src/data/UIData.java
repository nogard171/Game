package data;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.newdawn.slick.TrueTypeFont;

import classes.BuildingData;
import classes.ItemData;
import classes.MaterialData;
import classes.ModelData;
import classes.ResourceData;
import classes.SkillData;
import ui.RecipeData;

public class UIData {
	public static LinkedHashMap<String, RecipeData> recipeData = new LinkedHashMap<String, RecipeData>();

	public static HashMap<String, MaterialData> materialData = new HashMap<String, MaterialData>();
	public static HashMap<String, ModelData> modelData = new HashMap<String, ModelData>();
	public static HashMap<String, ResourceData> resourceData = new HashMap<String, ResourceData>();
	public static HashMap<String, ItemData> itemData = new HashMap<String, ItemData>();
	public static HashMap<String, SkillData> skillData = new HashMap<String, SkillData>();
	public static HashMap<String, BuildingData> buildingData = new HashMap<String, BuildingData>();
	public static HashMap<Integer, TrueTypeFont> fonts = new HashMap<Integer, TrueTypeFont>();
}
