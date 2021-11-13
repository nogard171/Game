package core;

import java.util.ArrayList;
import java.util.Random;

public class ResourceItemDrop {
	public ItemType type = ItemType.NONE;
	private int minRange = 1;
	private int maxRange = 1;
	private int rarity = 1;// 1 in 1 chance

	public ResourceItemDrop(ItemType newType) {
		this(newType, 1, 1, 1);
	}

	public ResourceItemDrop(ItemType newType, int newMin, int newMax) {
		this(newType, newMin, newMax, 1);
	}

	public ResourceItemDrop(ItemType newType, int newMin, int newMax, int newRarity) {
		type = newType;
		minRange = newMin;
		maxRange = newMax;
		rarity = newRarity;
	}

	public ArrayList<ItemType> getDroppedItems() {
		ArrayList<ItemType> items = new ArrayList<ItemType>();

		Random ran = new Random();
		int c = ran.nextInt(maxRange) + minRange;

		for (int i = 0; i < c; i++) {
			items.add(type);
		}
		return items;
	}

	public int getRarity() {
		return rarity;
	}

	public void setRarity(int rarity) {
		this.rarity = rarity;
	}
}
