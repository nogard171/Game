package core;

import java.awt.Point;
import java.util.LinkedList;

import org.lwjgl.opengl.GL11;

import game.Database;
import utils.Renderer;

public class Region {
	private Index index;
	private int list = -1;
	// y,x,z
	Size size;
	public Cell[][][] cellData;
	private LinkedList<Cell> knownCells = new LinkedList<Cell>();

	private boolean update = false;
	public int textureCount = 0;

	public Region() {
		size = Database.regionSize;
		cellData = new Cell[size.getHeight()][size.getWidth()][size.getDepth()];
		for (int y = size.getHeight() - 1; y >= 0; y--) {
			for (int x = 0; x < size.getWidth(); x++) {
				for (int z = 0; z < size.getDepth(); z++) {
					Index cellIndex = new Index(y, x, z);
					String texture = "grass";
					Cell cell = new Cell(cellIndex, texture);
					cellData[y][x][z] = cell;
					if (x >= 7 && x <= 8 && z >= 7 && z <= 8 && y == 1) {
						knownCells.add(cell);
					} else if (x >= 7 && x <= 8 && z >= 7 && z <= 8 && y == 0) {
						// knownCells.add(cell);
					} else if (y == 0) {
						knownCells.add(cell);
					}
				}
			}
		}
	}

	private int visibleLevel = 1;

	public void setLevel(int newLevel) {
		if (newLevel != visibleLevel) {
			visibleLevel = newLevel;
			update = true;
		}
	}

	private void build() {
		float regionX = ((index.x - index.z) * 33) * 16;
		float regionY = index.y * 33;
		float regionZ = (((index.z + index.x) * 17) + regionY) * 16;

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
		for (Cell c : knownCells) {
			Index index = c.getIndex();
			if (index.y >= visibleLevel) {
				TextureData data = Database.textureData.get(c.getTexture());
				if (data != null) {
					float localX = (index.x - index.z) * 33;
					float localY = index.y * 33;
					float localZ = ((index.z + index.x) * 17) + localY;

					Renderer.renderTexture(new Point((int) localX + regionPosition.x, (int) localZ + regionPosition.y),
							data);
					textureCount++;
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
}
