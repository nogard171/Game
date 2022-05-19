package ui;

import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;

import core.ChunkManager;
import core.GameDatabase;
import core.GroundItem;
import core.Item;
import core.ItemType;
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
}
