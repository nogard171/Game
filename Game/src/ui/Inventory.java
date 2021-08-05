package ui;

import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;

import core.GameDatabase;
import core.Item;
import core.ItemType;

public class Inventory {
	private static int slotCount = 35;

	public static int getSlotCount() {
		return slotCount;
	}

	private static int slotWidth = 5;
	public static LinkedList<ItemSlot> itemSlots = new LinkedList<ItemSlot>();

	public static void setup() {
		for (int i = 0; i < slotCount; i++) {
			ItemSlot slot = new ItemSlot();
			itemSlots.add(slot);
		}
	}

	public static void addItem(ItemType type) {
		Item item = GameDatabase.getItem(type);
		System.out.println("test" + type);
		if (item != null) {
			int index = findEmpty();
			if (index >= 0) {
				ItemSlot slot = itemSlots.get(index);
				slot.item = item;

				UIInventory.updateID = true;
			}
			else
			{
				//drop
			}
		}
	}

	private static int findEmpty() {
		int index = -1;
		for (int i = 0; i < slotCount; i++) {
			ItemSlot slot = itemSlots.get(i);
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
			ItemSlot slot = itemSlots.get(i);
			if (slot != null) {
				System.out.println("Index: " + i + "->" + slot.item);
			}
		}
	}

	public static boolean putItem(int index, ItemType type) {
		boolean added = false;
		Item item = GameDatabase.getItem(type);
		if (item != null) {
			ItemSlot slot = itemSlots.get(index);
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
