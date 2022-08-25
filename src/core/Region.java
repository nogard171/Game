package core;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

import game.Database;
import utils.Renderer;

public class Region {
	private Index index;
	private int list = -1;
	Point regionPosition;
	// y,x,z
	Size size;
	// fix array to use 3 dimensions
	public Cell[][][] cellData;
	public LinkedList<Cell> knownCells = new LinkedList<Cell>();
	public LinkedList<Cell> visibleCells = new LinkedList<Cell>();

	private boolean update = false;
	public int textureCount = 0;
	public Polygon bounds;

	public Region(Index newIndex) {
		this.index = newIndex;

		float regionX = ((index.x - index.z) * 32) * 16;
		float regionY = index.y * 32;
		float regionZ = (((index.z + index.x) * 16) + regionY) * 16;

		regionPosition = new Point((int) regionX, (int) regionZ);

		Polygon rb = new Polygon();
		rb.addPoint(regionPosition.x + 32, regionPosition.y + 32);
		rb.addPoint(regionPosition.x + 32 + Database.regionSize.getDepth() * 32,
				regionPosition.y + (Database.regionSize.getWidth() * 16) + 32);
		rb.addPoint(regionPosition.x + 32, regionPosition.y + (Database.regionSize.getWidth() * 32) + 32);
		rb.addPoint(regionPosition.x + 32 - Database.regionSize.getDepth() * 32,
				regionPosition.y + (Database.regionSize.getWidth() * 16) + 32);
		this.bounds = rb;

		size = Database.regionSize;
		cellData = new Cell[size.getHeight()][size.getWidth()][size.getDepth()];
		for (int y = size.getHeight() - 1; y >= 0; y--) {
			for (int x = 0; x < size.getWidth(); x++) {
				for (int z = 0; z < size.getDepth(); z++) {
					Index cellIndex = new Index(y, x, z);

					String texture = "air";
					if (y >= 1) {
						texture = "grass";
						Random r = new Random();
						int t = r.nextInt(10 - 1 + 1) + 1;
						if (t == 3) {
							texture = "dirt";
						}
						if (t == 2) {
							texture = "sand";
						}
						if (t == 1) {
							texture = "stone";
						}
					} else if (y == 0) {
						Random r = new Random();
						int t = r.nextInt(10 - 1 + 1) + 1;
						if (t == 3) {
							texture = "tree";
						}
					}
					Cell cell = new Cell(cellIndex, texture);
					if (index.x == 0 && index.y == 0 && index.z == 0 && y == 0 && x == 5 && z == 5) {
						Character charc = new Character(cellIndex, "character");
						setCell(cellIndex, charc);
						knownCells.add(charc);
						Database.agentMgr.addAgent(charc.getHash());
					}
					setCell(cellIndex, cell);
					if (y == 1) {
						knownCells.add(cell);
					}
					if (y <= 1) {
						knownCells.add(cell);
					}
					if (lowestKnownLevel < index.y) {
						lowestKnownLevel = index.y;
					}
					//knownCells.add(cell);
				}
			}
		}
	}

	public void setCell(Index newIndex, Cell newCell) {
		Cell cell = cellData[newIndex.y][newIndex.x][newIndex.z];
		if (cell == null) {
			cellData[newIndex.y][newIndex.x][newIndex.z] = newCell;
		}
	}

	private int visibleLevel = 0;
	private int lowestKnownLevel = 0;

	public void setLevel(int newLevel) {
		if (newLevel != visibleLevel && newLevel <= lowestKnownLevel) {
			visibleLevel = newLevel;
			update = true;
		}
	}

	private void build() {
		visibleCells.clear();
		textureCount = 0;
		size = Database.regionSize;
		list = GL11.glGenLists(1);
		GL11.glNewList(list, GL11.GL_COMPILE_AND_EXECUTE);
		Renderer.begin();
		for (Cell c : knownCells) {
			Index index = c.getIndex();

			if (lowestKnownLevel < index.y) {
				lowestKnownLevel = index.y;
			}
			String texture = c.getTexture();
			if (texture != "air") {
				if (index.y >= visibleLevel) {
					TextureData data = c.data;
					if (data == null) {
						data = Database.textureData.get(texture);
						c.data = data;
					}
					if (data != null) {
						float localX = (index.x - index.z) * 32;
						float localY = index.y * 32;
						float localZ = ((index.z + index.x) * 16) + localY;
						if (c.getBounds() == null) {
							Polygon b = new Polygon();
							for (int p = 0; p < data.bounds.npoints; p++) {
								Point point = new Point(
										(int) localX + regionPosition.x + data.bounds.xpoints[p] + data.centerX,
										(int) localZ + regionPosition.y + data.bounds.ypoints[p] + data.centerY);
								b.addPoint(point.x, point.y);
							}
							c.setBounds(b);
						}
						Renderer.renderTexture(
								new Point((int) localX + regionPosition.x, (int) localZ + regionPosition.y), c.data);
						visibleCells.add(c);
						textureCount++;
					}
				}
			}
		}
		Renderer.end();
		GL11.glEndList();
	}

	public void render() {
		if (list == -1 || this.update) {
			build();
			this.update = false;
		} else {
			GL11.glCallList(list);
		}
		Renderer.renderBounds(bounds, Color.red);
	}

	public void rebuild() {
		this.update = true;
	}

	public Index getIndex() {
		return index;
	}

	public void setIndex(Index index) {
		this.index = index;
	}

	public Polygon getBounds() {
		return this.bounds;
	}

	public LinkedList<Cell> getCells(Index newIndex) {
		LinkedList<Cell> cells = new LinkedList<Cell>();
		// for (int c = 0; c < 10; c++) {
		if (newIndex.y >= 0 && newIndex.x >= 0 && newIndex.z >= 0 ) {
			Cell cell = cellData[newIndex.y][newIndex.x][newIndex.z];
			if (cell != null) {
				cells.add(cell);
			}
		}
		return cells;
	}

	public Cell getCell(Index newIndex) {
		Cell cell = cellData[newIndex.y][newIndex.x][newIndex.z];
		return cell;
	}
}
