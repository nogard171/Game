package classes;

import java.util.LinkedList;

import ui.CraftingSlot;
import ui.QueuedRecipe;
import ui.SmeltingSlot;

public class Furnace extends Object {
	public int level = 0;

	public LinkedList<SmeltingSlot> oreSlots = new LinkedList<SmeltingSlot>();
	public LinkedList<SmeltingSlot> fuelSlots = new LinkedList<SmeltingSlot>();
	public LinkedList<SmeltingSlot> slots = new LinkedList<SmeltingSlot>();
	public LinkedList<QueuedRecipe> queuedRecipes = new LinkedList<QueuedRecipe>();

	public Furnace() {
		name = "FURNACE";
		type = ObjectType.FURNACE;
		for (int i = 0; i < 5; i++) {
			SmeltingSlot newSlot = new SmeltingSlot(3 + (i * 33), 170);
			slots.add(newSlot);
		}
		for (int x = 0; x < 2; x++) {
			for (int y = 0; y < 3; y++) {
				SmeltingSlot slot = new SmeltingSlot((x * 33) + 5, (y * 33) + 32);
				oreSlots.add(slot);
			}
		}

		for (int x = 0; x < 2; x++) {
			for (int y = 0; y < 3; y++) {
				SmeltingSlot slot = new SmeltingSlot((x * 33) + 100, (y * 33) + 32);
				fuelSlots.add(slot);
			}
		}
	}
}
