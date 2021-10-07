package game;

import java.util.HashMap;
import java.util.LinkedList;

import ui.Inventory;
import ui.ItemSlot;
import ui.Skill;

public class PlayerDatabase {

	public static HashMap<String, Skill> skills = new HashMap<String, Skill>();	
	public static LinkedList<ItemSlot> itemSlots = new LinkedList<ItemSlot>();
}
