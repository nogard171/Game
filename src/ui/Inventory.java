package ui;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import core.ChunkManager;
import core.GameDatabase;
import core.GroundItem;
import core.Item;
import core.ItemData;
import core.ItemType;
import core.RecipeItemData;
import core.TextureType;
import game.PlayerDatabase;

public class Inventory {
	private static int slotCount = 30;

	public static int getSlotCount() {
		return slotCount;
	}

	private static int slotWidth = 5;

	public static void setup() {
		for (int i = 0; i < slotCount; i++) {
			ItemSlot slot = new ItemSlot();
			PlayerDatabase.itemSlots.add(slot);
		}
	}

	public static boolean hasItemType(String itemType) {
		boolean hasItem = false;
		for (ItemSlot slot : PlayerDatabase.itemSlots) {
			if (slot.item != null) {
				ItemData dat = GameDatabase.items.get(slot.item.getType());
				hasItem = dat.hasAttr(itemType);
				if (hasItem) {
					break;
				}
			}
		}
		return hasItem;
	}

	public static boolean addItem(ItemType type) {
		return addItem(type, 1);
	}

	public static boolean addItem(ItemType type, long count) {
		boolean pickedUp = false;
		Item item = GameDatabase.getItem(type);
		if (item != null) {
			int index = (GameDatabase.isItemStackable(type) ? findItemIndex(type) : -1);

			if (index < 0) {
				index = findEmpty();
			}
			if (index >= 0) {
				ItemSlot slot = PlayerDatabase.itemSlots.get(index);
				if (slot.item != null) {
					slot.count += count;
				} else {
					slot.item = item;
					slot.count = count;
				}
				pickedUp = true;
				UIInventory.updateID = true;
			} else {
				GroundItem groundItem = new GroundItem(TextureType.ITEM);
				groundItem.type = item.texture;
				groundItem.item = type;
				groundItem.count = count;
				ChunkManager.dropItem(groundItem);
			}
		}
		return pickedUp;
	}

	private static int findItemIndex(ItemType type) {
		int index = -1;
		for (int i = 0; i < slotCount; i++) {
			ItemSlot slot = PlayerDatabase.itemSlots.get(i);
			if (slot != null) {
				if (slot.item != null) {
					if (slot.item.getType() == type) {
						if (slot.count < 1000000000000000000l) {
							index = i;
							break;
						}
					}
				}
			}
		}
		return index;
	}

	private static int findEmpty() {
		int index = -1;
		for (int i = 0; i < slotCount; i++) {
			ItemSlot slot = PlayerDatabase.itemSlots.get(i);
			if (slot != null) {
				if (slot.item == null) {
					index = i;
					break;
				}
			}
		}
		return index;
	}

	public static void printItems() {
		for (int i = 0; i < slotCount; i++) {
			ItemSlot slot = PlayerDatabase.itemSlots.get(i);
			if (slot != null) {
				System.out.println("Index: " + i + "->" + slot.item);
			}
		}
	}

	public static boolean putItem(int index, ItemType type) {
		boolean added = false;
		Item item = GameDatabase.getItem(type);
		if (item != null) {
			ItemSlot slot = PlayerDatabase.itemSlots.get(index);
			if (slot.item == null) {
				slot.item = item;
				added = true;
			}
		}
		if (!added) {
			System.out.println("Item not added at: " + index);
		}
		return added;
	}

	public static int getSlotWidth() {
		return slotWidth;
	}

	public static boolean hasRecipeItems(ArrayList<RecipeItemData> recipeItems) {
		boolean hasItems = false;
		int hasItemCount = 0;
		for (RecipeItemData dat : recipeItems) {
			int itemCount = 0;
			for (int i = 0; i < slotCount; i++) {
				ItemSlot slot = PlayerDatabase.itemSlots.get(i);
				if (slot != null) {
					if (slot.item != null) {
						if (slot.item.texture.equals(dat.type)) {
							if (slot.count > 0) {
								itemCount += slot.count;
							}
						}
					}
				}
			}
			if (itemCount >= dat.amount) {
				hasItemCount++;
			}
		}
		if (hasItemCount >= recipeItems.size() - 1) {
			hasItems = true;
		}
		return hasItems;
	}

	public static void removeRecipeItems(ArrayList<RecipeItemData> recipeItems) {
		for (RecipeItemData dat : recipeItems) {
			int removeCount = 0;
			for (int i = 0; i < slotCount; i++) {
				if (removeCount < dat.amount) {
					ItemSlot slot = PlayerDatabase.itemSlots.get(i);
					if (slot != null) {
						if (slot.item != null) {
							if (slot.item.texture.equals(dat.type)) {
								System.out.println("Removed:" + slot.item.texture);
								if (slot.count > 0) {
									slot.count--;
									removeCount++;
								}
								if (slot.count <= 0) {
									slot.item = null;
								}
							}
						}
					}
				} else {
					break;
				}
			}
		}
	}
}
