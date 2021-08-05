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
		dat.addDrop(new ResourceItemDrop(ItemType.LOG));
		resources.put(TextureType.TREE, dat);
	}

	private static void loadItems() {
		ItemData newItem = new ItemData(ItemType.ROCK);
		newItem.setTexture(UITextureType.ROCK_ITEM);
		items.put(newItem.type, newItem);
		
		

		newItem = new ItemData(ItemType.LOG);
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
}
