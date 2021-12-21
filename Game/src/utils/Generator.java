package utils;

import core.Index;
import core.Region;
import game.Database;

public class Generator {
	public static void generateRegion(int y, int x, int z) {
		Index index = new Index(y, x, z);
		Region reg = Database.regions.get(index);
		if (reg == null) {			
			reg = new Region(index);
			Database.regions.put(index, reg);
		}
	}
}
