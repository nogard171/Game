package core;

import java.util.HashMap;

import org.newdawn.slick.TrueTypeFont;

import ui.Skill;
import ui.SkillData;
import ui.SkillName;
import ui.UITextureType;

public class GameDatabase {
	public static HashMap<TextureType, ResourceData> resources = new HashMap<TextureType, ResourceData>();
	public static HashMap<ItemType, ItemData> items = new HashMap<ItemType, ItemData>();

	public static HashMap<SkillName, SkillData> skillData = new HashMap<SkillName, SkillData>();

	public static void load() {
		loadResources();
		loadItems();
		loadSkills();
	}

	private static void loadResources() {
		ResourceData dat = new ResourceData();
		dat.addDrop(new ResourceItemDrop(ItemType.ROCK));
		resources.put(TextureType.ROCK_ORE, dat);
		dat.setXPGain(5);

		dat = new ResourceData();
		dat.addDrop(new ResourceItemDrop(ItemType.TIN_ORE));
		resources.put(TextureType.TIN_ORE, dat);
		dat.setXPGain(10);

		dat = new ResourceData();
		dat.addDrop(new ResourceItemDrop(ItemType.COPPER_ORE));
		resources.put(TextureType.COPPER_ORE, dat);
		dat.setXPGain(10);

		dat = new ResourceData();
		dat.addDrop(new ResourceItemDrop(ItemType.COAL_ORE));
		resources.put(TextureType.COAL_ORE, dat);
		dat.setXPGain(20);
		
		dat = new ResourceData();
		dat.addDrop(new ResourceItemDrop(ItemType.LOG));
		dat.setXPGain(5);
		resources.put(TextureType.TREE, dat);

		dat = new ResourceData();
		dat.addDrop(new ResourceItemDrop(ItemType.LOG));
		dat.setXPGain(10);
		resources.put(TextureType.MAPLE_TREE, dat);

		dat = new ResourceData();
		dat.addDrop(new ResourceItemDrop(ItemType.STICK, 0, 2, 5));
		dat.isSearchable = true;
		resources.put(TextureType.BUSH, dat);

		dat = new ResourceData();
		dat.addDrop(new ResourceItemDrop(ItemType.FISH));
		dat.setXPGain(5);

		dat.destroyOnTask = false;
		dat.isRenewable = true;
		TextureType[] types = { TextureType.FISHING_SPOT, TextureType.FISHING_SPOT1, TextureType.FISHING_SPOT2,
				TextureType.FISHING_SPOT3, TextureType.FISHING_SPOT4 };
		dat.animationTypes = types;
		resources.put(TextureType.FISHING_SPOT, dat);
	}

	private static void loadItems() {
		ItemData newItem = new ItemData(ItemType.ROCK, "This is one solid piece of Rock.", 1);
		newItem.setTexture(UITextureType.ROCK_ITEM);
		items.put(newItem.type, newItem);

		newItem = new ItemData(ItemType.COINS, "Chan Ching, looks good in the hand.", 1);
		newItem.setTexture(UITextureType.COINS_ITEM);
		newItem.stackable = true;
		items.put(newItem.type, newItem);

		newItem = new ItemData(ItemType.COPPER_ORE, "It has a orangy ting.", 10);
		newItem.setTexture(UITextureType.COPPER_ORE_ITEM);
		items.put(newItem.type, newItem);

		newItem = new ItemData(ItemType.TIN_ORE, "This looks simular to Rock.", 10);
		newItem.setTexture(UITextureType.TIN_ORE_ITEM);
		items.put(newItem.type, newItem);

		newItem = new ItemData(ItemType.LOG, "Long sturdy piece of Wood.", 5);
		newItem.setTexture(UITextureType.LOG_ITEM);
		items.put(newItem.type, newItem);

		newItem = new ItemData(ItemType.FISH, "Floppy fish.", 20);
		newItem.setTexture(UITextureType.FISH_ITEM);
		newItem.attr.add("EDIBLE");
		items.put(newItem.type, newItem);

		newItem = new ItemData(ItemType.HOE, "A Hoe used for tiling.", 20);
		newItem.setTexture(UITextureType.HOE_ITEM);
		newItem.attr.add("HOE");
		items.put(newItem.type, newItem);
		
		newItem = new ItemData(ItemType.PICKAXE, "A Pickaxe used for mining ore/rocks.", 20);
		newItem.setTexture(UITextureType.PICKAXE_ITEM);
		newItem.attr.add("PICKAXE");
		items.put(newItem.type, newItem);

		newItem = new ItemData(ItemType.STICK, "A few sticks.", 20);
		newItem.setTexture(UITextureType.STICK_ITEM);
		items.put(newItem.type, newItem);
	}

	public static void loadSkills() {
		SkillData fishingSkill = new SkillData(SkillName.FISHING, "Fish for many diferent fish.", UITextureType.FISH_ITEM,"FISHING_TOOL");
		fishingSkill.resourceLevels.put(TextureType.FISHING_SPOT, 5);
		skillData.put(fishingSkill.name, fishingSkill);

		SkillData woodCuttingSkill = new SkillData(SkillName.WOODCUTTING,
				"Wood cutting allow for the gathering of wood.", UITextureType.WOODCUTTING_SKILL_ICON,"AXE");
		woodCuttingSkill.resourceLevels.put(TextureType.TREE, 0);
		woodCuttingSkill.resourceLevels.put(TextureType.MAPLE_TREE, 2);
		skillData.put(woodCuttingSkill.name, woodCuttingSkill);

		SkillData miningSkill = new SkillData(SkillName.MINING, "Wood cutting allow for the gathering of wood.",
				UITextureType.MINING_SKILL_ICON,"PICKAXE");
		miningSkill.resourceLevels.put(TextureType.ROCK_ORE, 0);
		miningSkill.resourceLevels.put(TextureType.COPPER_ORE, 3);
		miningSkill.resourceLevels.put(TextureType.TIN_ORE, 3);
		miningSkill.resourceLevels.put(TextureType.COAL_ORE, 20);
		skillData.put(miningSkill.name, miningSkill);

		SkillData agilitySkill = new SkillData(SkillName.AGILITY, "Wood cutting allow for the gathering of wood.",
				UITextureType.MINING_SKILL_ICON);
		agilitySkill.resourceLevels.put(TextureType.GRASS, 0);
		agilitySkill.resourceLevels.put(TextureType.GRASS0, 0);
		agilitySkill.resourceLevels.put(TextureType.DIRT, 0);
		agilitySkill.resourceLevels.put(TextureType.SAND, 0);
		skillData.put(agilitySkill.name, agilitySkill);

	}

	public static ItemData getItemData(ItemType type) {
		ItemData data = items.get(type);

		return data;
	}

	public static Item getItem(ItemType type) {
		ItemData data = items.get(type);
		Item item = null;
		if (data != null) {
			item = data.toItem();
		}
		return item;
	}

	public static boolean isItemStackable(ItemType type) {
		boolean stackable = false;
		ItemData data = items.get(type);
		if (data != null) {
			stackable = data.stackable;
		}
		return stackable;
	}
}
