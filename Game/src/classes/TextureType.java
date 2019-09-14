package classes;

public enum TextureType {
	AIR, GRASS, DIRT;

	public static TextureType getType(String name) {
		TextureType type = AIR;
		switch (name) {
		case "GRASS":
			type = GRASS;
			break;
		case "DIRT":
			type = DIRT;
			break;
		}
		return type;
	}

	public static boolean isMask(TextureType type) {
		boolean mask = false;
		switch (type) {
		case AIR:
			mask = true;
			break;
		}
		return mask;
	}
}
