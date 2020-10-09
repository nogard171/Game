package ui;

import classes.ItemData;
import classes.Object;
import data.WorldData;

public class InventoryItem extends Object {
	public String name = "";
	public String commonName = "";
	public int count = 1;
	public boolean hovered = false;
	public int durability = -1;
}
