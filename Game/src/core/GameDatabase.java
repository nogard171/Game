package core;

import java.util.HashMap;

import org.newdawn.slick.TrueTypeFont;

import ui.UITextureType;

public class GameDatabase {
	public static HashMap<TextureType, ResourceData> resources = new HashMap<TextureType, ResourceData>();

	public static HashMap<ItemType, ItemData> items = new HashMap<ItemType, ItemData>();

	public static void load() {
		loadResources();
		loadItems();
	}

	private static void loadResources() {
		ResourceData dat = new ResourceData();
		dat.addDrop(new ResourceItemDrop(ItemType.ROCK));
		resources.put(TextureType.ROCK, dat);

		dat = new ResourceData();
		dat.addDrop(new ResourceItemDrop(ItemType.TIN_ORE));
		resources.put(TextureType.TIN_ORE, dat);

		dat = new ResourceData();
		dat.addDrop(new ResourceItemDrop(ItemType.COPPER_ORE));
		resources.put(TextureType.COPPER_ORE, dat);

		dat = new ResourceData();
		dat.addDrop(new ResourceItemDrop(ItemType.LOG));
		resources.put(TextureType.TREE, dat);
	}

	private static void loadItems() {
		ItemData newItem = new ItemData(ItemType.ROCK,"This is one solid piece of Rock.");
		newItem.setTexture(UITextureType.ROCK_ITEM);
		items.put(newItem.type, newItem);

		newItem = new ItemData(ItemType.COPPER_ORE,"It has a orangy ting.");
		newItem.setTexture(UITextureType.COPPER_ORE_ITEM);
		items.put(newItem.type, newItem);

		newItem = new ItemData(ItemType.TIN_ORE,"This looks simular to Rock.");
		newItem.setTexture(UITextureType.TIN_ORE_ITEM);
		items.put(newItem.type, newItem);

		newItem = new ItemData(ItemType.LOG, "Long sturdy piece of Wood.");
		newItem.setTexture(UITextureType.LOG_ITEM);
		items.put(newItem.type, newItem);
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
