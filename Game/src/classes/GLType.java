package classes;

import java.util.HashMap;
import java.util.Map;

public enum GLType {
	AIR(0, true), UNKNOWN(0, false), GRASS(0, false), DIRT(0, false), TREE(0, true), SAND(0, false), WATER(0, true),
	BUSH(0, true), SUNFLOWER(0, true), WOOD_PLANKS(0, false), TIN_ORE(0, false), COPPER_ORE(0, false),
	IRON_ORE(0, false),
	CHARACTER(0,true);

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
}
