package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class Resource extends Object {
	private ArrayList<Item> drops = new ArrayList<Item>(Arrays.asList(new Item(ItemType.LOG, 1, 5)));
	private int health;

	public Resource(TextureType newType) {
		super(newType);
		health = 1;
	}

	public Resource(TextureType newType, int newHealth) {
		super(newType);
		health = newHealth;
	}

	public void setType(TextureType newType, int newHealth) {
		setType(newType);
		health = newHealth;
	}

	public int getHealth() {
		return health;
	}

	public LinkedList<ANode> getANode(ANode node) {

		LinkedList<ANode> nodePath = new LinkedList<ANode>();

		for (int h = 0; h < health; h++) {
			nodePath.add(node);
		}

		return nodePath;
	}
}
