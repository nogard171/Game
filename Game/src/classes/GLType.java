package classes;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.LongStream;

public enum GLType {
	AIR(0, true), UNKNOWN(-1, false), GRASS(1, false), DIRT(2, false), TREE(3, true), SAND(4, false), WATER(5, true),
	BUSH(6, true), WOOD_PLANKS(8, false), TIN_ORE(9, false), COPPER_ORE(10, false), IRON_ORE(11, false),
	CHARACTER(12, true), STONE(13, false), EMPTY_ORE(14, false), BUSH_TRUNK(7, true), TREE_TRUNK(15, true);

	private int value;
	private boolean mask;

	private static Map<Integer, GLType> map = new HashMap<>();

	private GLType(int newValue, boolean newMask) {
		this.value = newValue;
		this.mask = newMask;
	}

	static {
		for (GLType pageType : GLType.values()) {
			map.put(pageType.value, pageType);
		}
	}

	public static GLType valueOf(int pageType) {
		return (GLType) map.get(pageType);
	}

	public int getValue() {
		return value;
	}

	public boolean isMask() {
		return mask;
	}

	public boolean isHarvestable() {
		boolean harvestable = false;

		GLType[] harvestables = { BUSH };

		for (int i = 0; i < harvestables.length; i++) {
			if (this == harvestables[i]) {
				harvestable = true;
				break;
			}
		}

		return harvestable;
	}

	public boolean isDigable() {
		boolean harvestable = false;

		GLType[] harvestables = { GRASS, DIRT, SAND };

		for (int i = 0; i < harvestables.length; i++) {
			if (this == harvestables[i]) {
				harvestable = true;
				break;
			}
		}

		return harvestable;
	}

	public boolean isChopable() {
		boolean harvestable = false;

		GLType[] harvestables = { BUSH, TREE, WOOD_PLANKS };

		for (int i = 0; i < harvestables.length; i++) {
			if (this == harvestables[i]) {
				harvestable = true;
				break;
			}
		}

		return harvestable;
	}

	public boolean isMineable() {
		boolean harvestable = false;

		GLType[] harvestables = { IRON_ORE, COPPER_ORE, TIN_ORE, STONE };

		for (int i = 0; i < harvestables.length; i++) {
			if (this == harvestables[i]) {
				harvestable = true;
				break;
			}
		}

		return harvestable;
	}

	public boolean isCollectable() {
		boolean collectable = false;

		GLType[] collectables = { WATER };

		for (int i = 0; i < collectables.length; i++) {
			if (this == collectables[i]) {
				collectable = true;
				break;
			}
		}

		return collectable;
	}

	public boolean isMoveable() {
		boolean moveable = false;

		GLType[] moveables = { AIR, BUSH, GRASS };

		for (int i = 0; i < moveables.length; i++) {
			if (this == moveables[i]) {
				moveable = true;
				break;
			}
		}

		return moveable;
	}

	public boolean isObject() {
		boolean object = false;

		GLType[] objects = { TREE, TREE_TRUNK, BUSH, BUSH_TRUNK, WOOD_PLANKS, TIN_ORE, COPPER_ORE, IRON_ORE };

		for (int i = 0; i < objects.length; i++) {
			if (this == objects[i]) {
				object = true;
				break;
			}
		}

		return object;
	}
}
