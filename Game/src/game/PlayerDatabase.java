package game;

import java.util.HashMap;
import java.util.LinkedList;

import ui.Inventory;
import ui.ItemSlot;
import ui.Skill;
import ui.SkillName;

public class PlayerDatabase {
	public static String name ="Silly_Box";
	public static int maxHealth = 100;
	public static int health = 100;
	public static int level = 1;
	public static long xp=0;
	public static long nextXP=100;
	
	
	
	public static LinkedList<Skill> skills = new LinkedList<Skill>();
	public static LinkedList<ItemSlot> itemSlots = new LinkedList<ItemSlot>();

}
