package classes;

import java.util.LinkedList;

import ui.CraftingSlot;

public class Furnace extends Object {
	public int level = 0;

	public LinkedList<CraftingSlot> slots = new LinkedList<CraftingSlot>();

	public Furnace() {
		for (int i = 0; i < 5; i++) {
			CraftingSlot newSlot = new CraftingSlot(3 + (i * 33), 170);
			slots.add(newSlot);
		}
	}
}
