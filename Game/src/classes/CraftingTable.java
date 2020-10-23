package classes;

import java.util.LinkedList;

import ui.CraftingSlot;

public class CraftingTable extends Object {
	public int level = 0;

	public LinkedList<CraftingSlot> slots = new LinkedList<CraftingSlot>();

	public CraftingTable() {

		for (int i = 0; i < 7; i++) {
			CraftingSlot newSlot = new CraftingSlot(160 + (i * 33), 302);
			slots.add(newSlot);
		}
	}
}
