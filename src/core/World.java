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
	private static ArrayList<Region> regionsInView = new ArrayList<Region>();
	private static int regionCount = 0;
	public static int textureCount = 0;

	public void setup() {
		for (int x = 0; x < 10; x++) {
			for (int z = 0; z < 10; z++) {
				Generator.generateRegion(0, x, z);
			}
		}
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

	int visibleLevel = 0;

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
			for (Cell c : r.visibleCells) {
				if (c.getBounds() != null) {
					if (c.getBounds().contains(mousePoint)) {
						Index index = new Index((r.getIndex().y * Database.regionSize.getHeight()) + c.getIndex().y,
								(r.getIndex().x * Database.regionSize.getWidth()) + c.getIndex().x,
								(r.getIndex().z * Database.regionSize.getDepth()) + c.getIndex().z);
						if (!indexes.contains(index)) {
							indexes.add(index);
						}
					}
				}
			}
		}
		return indexes;
	}

	public static int getTextureCount() {
		return textureCount;
	}

	int range = 7;
	Index[] indexes;

	private ArrayList<Region> getRegionsInView(int y, int x, int z) {
		if (indexes == null) {
			int i = 0;
			indexes = new Index[(range * 2) * (range * 2)];
			System.out.println("size:"+indexes.length);
			for (int tx = 0 - (range/2); tx <= (range/2); tx++) {
				for (int tz = 0 - (range/2); tz <= (range/2); tz++) {
					indexes[i] = new Index(0, tx, tz);
					i++;
				}
			}
		}
		/*
		 * Index[] indexes = new Index[] { new Index(y, x, z), new Index(y, x - range,
		 * z), new Index(y, x + range, z), new Index(y, x, z - range), new Index(y, x, z
		 * + range), new Index(y, x - range, z - range), new Index(y, x + range, z +
		 * range) };
		 */
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

	public LinkedList<Cell> getCells(Index i) {
		LinkedList<Cell> c = new LinkedList<Cell>();
		int regionX = i.x / Database.regionSize.getWidth();
		int regionY = i.y / Database.regionSize.getHeight();
		int regionZ = i.z / Database.regionSize.getDepth();
		Index index = new Index(regionY, regionX, regionZ);
		Region reg = Database.regions.get(index);
		if (reg != null) {
			int cellX = i.x % Database.regionSize.getWidth();
			int cellY = i.y % Database.regionSize.getHeight();
			int cellZ = i.z % Database.regionSize.getDepth();
			LinkedList<Cell> cells = reg.getCells(new Index(cellY, cellX, cellZ));
			if (cells != null) {
				for (Cell cell : cells) {
					c.add(cell);// reg.cellData[cellY][cellX][cellZ];
				}
			}
		}
		return c;
	}

	public LinkedList<Cell> getCells(LinkedList<Index> indexes) {
		LinkedList<Cell> c = new LinkedList<Cell>();
		for (Index i : indexes) {
			int regionX = i.x / Database.regionSize.getWidth();
			int regionY = i.y / Database.regionSize.getHeight();
			int regionZ = i.z / Database.regionSize.getDepth();
			Index index = new Index(regionY, regionX, regionZ);
			Region reg = Database.regions.get(index);
			if (reg != null) {
				int cellX = i.x % Database.regionSize.getWidth();
				int cellY = i.y % Database.regionSize.getHeight();
				int cellZ = i.z % Database.regionSize.getDepth();
				LinkedList<Cell> cells = reg.getCells(new Index(cellY, cellX, cellZ));
				if (cells != null) {
					for (Cell cell : cells) {
						c.add(cell);// reg.cellData[cellY][cellX][cellZ];
					}
				}
			}
		}
		return c;
	}

	public static LinkedList<Cell> setCell(Index i, String type) {

		LinkedList<Cell> c = new LinkedList<Cell>();
		// for (Index i : indexes) {
		int regionX = i.x / Database.regionSize.getWidth();
		int regionY = i.y / Database.regionSize.getHeight();
		int regionZ = i.z / Database.regionSize.getDepth();
		Index index = new Index(regionY, regionX, regionZ);
		Region reg = Database.regions.get(index);
		if (reg != null) {
			int cellX = i.x % Database.regionSize.getWidth();
			int cellY = i.y % Database.regionSize.getHeight();
			int cellZ = i.z % Database.regionSize.getDepth();
			LinkedList<Cell> cells = reg.getCells(new Index(cellY, cellX, cellZ));
			if (cells != null) {
				if (cells.size() > 0) {
					// for (Cell cell : cells) {
					cells.get(0).setTexture("dirt");
					reg.rebuild();
					/*
					 * try { Thread.sleep(100); } catch (InterruptedException e) { // TODO
					 * Auto-generated catch block e.printStackTrace(); }
					 */
					// c.add(cell);// reg.cellData[cellY][cellX][cellZ];
					// }
				}
			}
		}
		// }
		return c;
	}

	public static Cell getCharacterByHash(String hash) {
		Cell character = null;
		for (Region r : regionsInView) {
			for (Cell c : r.visibleCells) {
				if (c.getBounds() != null) {
					if (c.getClass() == Character.class) {
						character = c;
						break;
					}
				}
			}
		}
		return character;
	}
}
