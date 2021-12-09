package core;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;

import game.Database;
import utils.Generator;
import utils.Input;
import utils.Renderer;

public class World {
	ArrayList<Region> regionsInView = new ArrayList<Region>();
	private static int regionCount = 0;
	public static int textureCount = 0;

	public void setup() {
		for (int x = 0; x < 10; x++) {
			for (int z = 0; z < 10; z++) {
				Generator.generateRegion(0, x, z);
			}
		}
		// Generator.generateRegion(0, 1, 0);
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
			if (visibleLevel > 0) {
				visibleLevel--;
			}
		}
		if (wheel < 0) {
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

	public LinkedList<Index> getHoveredIndexes(Point mousePoint) {
		LinkedList<Index> indexes = new LinkedList<Index>();
		for (Region r : regionsInView) {

			// if (r.getBounds().contains(mousePoint)) {
			for (Cell c : r.visibleCells) {
				if (c.getBounds() != null) {
					if (c.getBounds().contains(mousePoint)) {
						indexes.add(new Index((r.getIndex().y * Database.regionSize.getHeight()) + c.getIndex().y,
								(r.getIndex().x * Database.regionSize.getWidth()) + c.getIndex().x,
								(r.getIndex().z * Database.regionSize.getDepth()) + c.getIndex().z));
					}
				}
			}
			// }
		}
		return indexes;
	}

	public static int getTextureCount() {
		return textureCount;
	}

	private ArrayList<Region> getRegionsInView(int y, int x, int z) {
		Index[] indexes = new Index[] { new Index(y, x, z), new Index(y, x - 1, z), new Index(y, x + 1, z),
				new Index(y, x, z - 1), new Index(y, x, z + 1), new Index(y, x - 1, z - 1),
				new Index(y, x + 1, z + 1) };
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

	public Cell getCell(Index i) {
		Cell c = null;
		int regionX = i.x / Database.regionSize.getWidth();
		int regionY = i.y / Database.regionSize.getHeight();
		int regionZ = i.z / Database.regionSize.getDepth();
		Index index = new Index(regionY, regionX, regionZ);
		Region reg = Database.regions.get(index);
		if (reg != null) {

			int cellX = i.x % Database.regionSize.getWidth();
			int cellY = i.y % Database.regionSize.getHeight();
			int cellZ = i.z % Database.regionSize.getDepth();
			c = reg.cellData[cellY][cellX][cellZ];
		}
		return c;
	}
}