package core;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Map;

import org.lwjgl.input.Mouse;

import game.Database;
import utils.Generator;
import utils.Renderer;

public class World {
	ArrayList<Region> regionsInView = new ArrayList<Region>();
	private static int regionCount = 0;
	public static int textureCount = 0;

	public void setup() {
		Generator.generateRegion(0, 0, 0);
		Generator.generateRegion(0, 1, 0);
		regionsInView = getRegionsInView(0, 0, 0);
		for (Region r : regionsInView) {
			r.render();
		}
	}

	public void update() {
		if (View.Moved()) {
			regionsInView = getRegionsInView(0, 0, 0);
		}
		float wheel = Mouse.getDWheel();
		if (wheel > 0) {
			System.out.println("up");
			if (visibleLevel > 0) {
				visibleLevel--;
			}
		}
		if (wheel < 0) {
			System.out.println("down");
			if (visibleLevel < Database.regionSize.getHeight() - 1) {
				visibleLevel++;
			}
		}
	}

	int visibleLevel = 1;

	public void render() {
		textureCount = 0;
		for (Region r : regionsInView) {
			r.setLevel(visibleLevel);
			r.render();
			textureCount += r.textureCount;
		}
	}

	public static int getTextureCount() {
		return textureCount;
	}

	private ArrayList<Region> getRegionsInView(int y, int x, int z) {
		Index[] indexes = new Index[] { new Index(y, x, z), new Index(y, x - 1, z), new Index(y, x + 1, z),
				new Index(y, x, z - 1), new Index(y, x, z + 1) };
		ArrayList<Region> regionsInView = new ArrayList<Region>();
		for (Index i : indexes) {
			Region reg = Database.regions.get(i);
			if (reg != null) {
				regionsInView.add(reg);
			}
		}
		regionCount = regionsInView.size();
		return regionsInView;

	}

	public static int getRegionCount() {
		return regionCount;
	}
}
