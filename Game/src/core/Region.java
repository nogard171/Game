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
	// y,x,z
	Size size;
	public Cell[][][][] cellData;
	public LinkedList<Cell> knownCells = new LinkedList<Cell>();
	public LinkedList<Cell> visibleCells = new LinkedList<Cell>();

	private boolean update = false;
	public int textureCount = 0;
	public Polygon bounds;

	public Region(Index newIndex) {
		this.index = newIndex;
		size = Database.regionSize;
		cellData = new Cell[size.getHeight()][size.getWidth()][size.getDepth()][10];
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
						cellIndex.i = 0;
						setCell(cellIndex, charc);
						knownCells.add(charc);
						Database.agentMgr.addAgent(charc.getHash());
					}
					if (index.x == 0 && index.y == 0 && index.z == 0 && y == 0 && x == 5 && z == 5) {
						cell = new Cell(cellIndex, "tree");
						cellIndex.i = 1;
						setCell(cellIndex, cell);
						knownCells.add(cell);
					}
					setCell(cellIndex, cell);
					if (y == 1) {
						knownCells.add(cell);
					}
					if (y <= 1) {
						knownCells.add(cell);
					}
				}
			}
		}
	}

	public void setCell(Index newIndex, Cell newCell) {
		for (int c = 0; c < 10; c++) {
			Cell cell = cellData[newIndex.y][newIndex.x][newIndex.z][c];
			if (cell == null) {
				newCell.setIndex(new Index(newIndex.y, newIndex.x, newIndex.z, newIndex.i));
				cellData[newIndex.y][newIndex.x][newIndex.z][c] = newCell;
				break;
			}
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
		float regionX = ((index.x - index.z) * 32) * 16;
		float regionY = index.y * 32;
		float regionZ = (((index.z + index.x) * 16) + regionY) * 16;

		Point regionPosition = new Point((int) regionX, (int) regionZ);
		textureCount = 0;
		size = Database.regionSize;
		list = GL11.glGenLists(1);
		GL11.glNewList(list, GL11.GL_COMPILE);

		Renderer.begin();
		/*
		 * for (int y = size.getHeight() - 1; y >= 0; y--) { for (int x = 0; x <
		 * size.getWidth(); x++) { for (int z = 0; z < size.getDepth(); z++) { Cell cell
		 * = cellData[y][x][z]; if (cell != null) { TextureData data =
		 * Database.textureData.get(cell.getTexture()); if (data != null) { float localX
		 * = (x - z) * 33; float localY = y * 33; float localZ = ((z + x) * 17) +
		 * localY;
		 * 
		 * Renderer.renderTexture( new Point((int) localX + regionPosition.x, (int)
		 * localZ + regionPosition.y), data); textureCount++; } } } } }
		 */
		Polygon rb = new Polygon();
		rb.addPoint(regionPosition.x + 32, regionPosition.y + 32);
		rb.addPoint(regionPosition.x + 32 + Database.regionSize.getDepth() * 32,
				regionPosition.y + (Database.regionSize.getWidth() * 16) + 32);
		rb.addPoint(regionPosition.x + 32, regionPosition.y + (Database.regionSize.getWidth() * 32) + 32);
		rb.addPoint(regionPosition.x + 32 - Database.regionSize.getDepth() * 32,
				regionPosition.y + (Database.regionSize.getWidth() * 16) + 32);
		this.bounds = rb;
		for (Cell c : knownCells) {
			Index index = c.getIndex();

			if (lowestKnownLevel < index.y) {
				lowestKnownLevel = index.y;
			}
			String texture = c.getTexture();
			if (texture != "air") {

				if (index.y >= visibleLevel) {
					TextureData data = Database.textureData.get(texture);
					if (data != null) {
						float localX = (index.x - index.z) * 32;
						float localY = index.y * 32;
						float localZ = ((index.z + index.x) * 16) + localY;
						Polygon b = new Polygon();
						for (int p = 0; p < data.bounds.npoints; p++) {
							Point point = new Point(
									(int) localX + regionPosition.x + data.bounds.xpoints[p] + data.centerX,
									(int) localZ + regionPosition.y + data.bounds.ypoints[p] + data.centerY);
							b.addPoint(point.x, point.y);
						}
						c.setBounds(b);
						Renderer.renderTexture(
								new Point((int) localX + regionPosition.x, (int) localZ + regionPosition.y), data);

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
		if (newIndex.y >= 0 && newIndex.x >= 0 && newIndex.z >= 0 && newIndex.i >= 0) {
			Cell cell = cellData[newIndex.y][newIndex.x][newIndex.z][newIndex.i];
			if (cell != null) {
				cells.add(cell);
			}
		}
		return cells;
	}

	public Cell getCell(Index newIndex) {
		Cell cell = cellData[newIndex.y][newIndex.x][newIndex.z][0];
		return cell;
	}
}
