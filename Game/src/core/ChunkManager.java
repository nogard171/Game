package core;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import game.Base;
import ui.UIManager;
import utils.Ticker;

public class ChunkManager {
	public static HashMap<Point, Chunk> chunks = new HashMap<Point, Chunk>();
	public static ArrayList<Chunk> chunksInView = new ArrayList<Chunk>();

	static Point chunkDim = new Point(10, 10);
	public static Rectangle viewRange = new Rectangle(0, 2, 5, 3);
	Ticker tickerUtil;

	public void setup() {
		tickerUtil = new Ticker();
		int chunkCount = chunkDim.x * chunkDim.y;
		int chunkI = 0;
		for (int x = 0; x < chunkDim.x; x++) {
			for (int y = 0; y < chunkDim.y; y++) {
				Chunk chunk = new Chunk(x, y);
				chunk.setup();
				chunks.put(new Point(x, y), chunk);
				System.out.println("Complete Chunk:" + (chunkI) + " of " + chunkCount);
				chunkI++;
			}
		}
		// future world output
		if (true == false) {
			try {

				BufferedImage tileset = ImageIO.read(new File("assets/textures/tileset.png"));
				int pCount = (chunkDim.x * Chunk.size.width) * (chunkDim.y * Chunk.size.height);
				int p = 0;
				BufferedImage bi = new BufferedImage((chunkDim.x * Chunk.size.width * 64),
						chunkDim.y * Chunk.size.height * 32, BufferedImage.TYPE_INT_RGB);
				Graphics2D g = (Graphics2D) bi.getGraphics();

				for (int x = 0; x <= chunkDim.x; x++) {
					for (int y = 0; y <= chunkDim.y; y++) {
						Chunk chunk = chunks.get(new Point(x, y));

						if (chunk != null) {
							for (int tx = 0; tx < Chunk.size.width; tx++) {
								for (int ty = 0; ty < Chunk.size.height; ty++) {
									Tile tile = chunk.tiles.get(new Point(tx, ty));

									if (tile != null) {
										/*
										 * bi.setRGB((x * Chunk.size.width) + tx, (y * Chunk.size.height) + ty,
										 * tile.getType().toColorInt());
										 */
										TextureType type = tile.getType();
										System.out.println("Type:" + type + "=>" + ((int) type.x * 64) + ","
												+ ((int) type.y * 64));
										g.drawImage(
												tileset.getSubimage((int) type.x * 64, (int) type.y * 64, (int) 64,
														(int) 64),
												(int) (tile.getPosition().x + ((chunkDim.x * Chunk.size.width * 64) / 2)
														+ (64 * type.xOffset)),
												(int) (tile.getPosition().y + (64 * type.yOffset)), null);
										System.out.println("Complete Pixel:" + p + " of " + pCount);
										p++;
									}
								}
							}
						}
					}
				}

				for (int x = 0; x <= chunkDim.x; x++) {
					for (int y = 0; y <= chunkDim.y; y++) {
						Chunk chunk = chunks.get(new Point(x, y));

						if (chunk != null) {
							for (int tx = 0; tx < Chunk.size.width; tx++) {
								for (int ty = 0; ty < Chunk.size.height; ty++) {
									Object obj = chunk.objects.get(new Point(tx, ty));
									if (obj != null) {
										TextureType type = obj.getType();
										if (!type.equals(TextureType.AIR)) {
											g.drawImage(
													tileset.getSubimage((int) (type.x * 64), (int) (type.y * 64),
															(int) ((type.w * 64)), (int) ((type.h * 64))),
													(int) (obj.getPosition().x
															+ ((chunkDim.x * Chunk.size.width * 64) / 2)
															+ (64 * type.xOffset)),
													(int) (obj.getPosition().y + (64 * type.yOffset)), null);
											System.out.println("Complete Pixel:" + type + "=>" + p + " of " + pCount);
											p++;
										}
									}
								}
							}
						}
					}
				}
				File outputfile = new File("saved.png");
				ImageIO.write(bi, "png", outputfile);
				System.out.println("Saved!");
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	public int colorToInt(int r, int g, int b, int a) {
		return (a << 24) | (r << 16) | (g << 8) | b;
	}

	public static Point getIndexByType(TextureType type) {
		Point index = null;

		for (Chunk chunk : chunksInView) {
			if (chunk != null) {
				index = chunk.getIndexByType(type);
			}
			if (index != null) {
				break;
			}
		}
		if (index == null) {

			for (int x = 0; x < chunkDim.x; x++) {
				for (int y = 0; y < chunkDim.y; y++) {
					Chunk chunk = chunks.get(new Point(x, y));
					if (chunk != null) {
						index = chunk.getIndexByType(type);
					}
					if (index != null) {
						break;
					}
				}
				if (index != null) {
					break;
				}
			}
		}
		return index;
	}

	public static void dropItem(GroundItem item) {
		Point test = ChunkManager.getIndexByType(TextureType.CHARACTER);
		dropItemAtIndex(test, item);
	}

	public static void dropItemAtIndex(Point index, GroundItem item) {
		Point test = index;
		if (test != null) {
			int chunkX = (int) (test.x / Chunk.size.width);
			int chunkY = (int) (test.y / Chunk.size.height);
			Chunk chunk = ChunkManager.chunks.get(new Point(chunkX, chunkY));
			if (chunk != null) {
				int objX = (int) (test.x % 16);
				int objY = (int) (test.y % 16);
				chunk.droppedItems.put(new Point(objX, objY), item);
				chunk.build();
			}
		}
	}

	public static double calculateDistanceBetweenPointsWithHypot(double x1, double y1, double x2, double y2) {

		double ac = Math.abs(y2 - y1);
		double cb = Math.abs(x2 - x1);

		return Math.hypot(ac, cb);
	}

	public static Point findIndexAroundIndex(Point playerIndex, Point index) {
		Point newIndex = index;
		double closestDist = 1000;
		if (index != null) {
			for (int x = -1; x < 2; x++) {
				for (int y = -1; y < 2; y++) {
					Point tempIndex = new Point(index.x + x, index.y + y);
					double dist = calculateDistanceBetweenPointsWithHypot(playerIndex.x, playerIndex.y, tempIndex.x,
							tempIndex.y);
					if (closestDist >= dist) {
						if ((index.x + x == index.x || index.y + y == index.y) && tempIndex != index) {

							TextureType type = getTypeByIndexWithTiles(tempIndex);
							if (type != null) {
								if (type == TextureType.AIR || type == TextureType.GRASS || type == TextureType.GRASS0
										|| type == TextureType.SAND) {
									newIndex = tempIndex;
								}
							}
							closestDist = dist;
						}
					}
				}
			}
		}
		return newIndex;
	}

	public static TextureType getTypeByIndexWithTiles(Point index) {
		TextureType type = null;
		if (index != null) {
			int chunkX = (int) (index.x / Chunk.size.width);
			int chunkY = (int) (index.y / Chunk.size.height);
			Chunk chunk = ChunkManager.chunks.get(new Point(chunkX, chunkY));
			if (chunk != null) {
				int objX = (int) (index.x % 16);
				int objY = (int) (index.y % 16);
				Tile tile = chunk.getObjectAtIndex(new Point(objX, objY), true, false);
				if (tile != null) {
					type = tile.getType();
				}
			}
		}
		return type;
	}

	public static TextureType getTypeByIndex(Point index) {
		TextureType type = null;
		for (int x = 0; x < 2; x++) {
			for (int y = 0; y < 2; y++) {
				Chunk chunk = chunks.get(new Point(x, y));
				if (chunk != null) {
					Tile tile = chunk.getObjectAtIndex(index, true, true);
					if (tile != null) {
						type = tile.getType();
					}
				}
			}
		}
		return type;
	}

	public void update() {
		tickerUtil.poll(200);
		chunksInView.clear();
		Point center = new Point(Base.view.x / Chunk.size.width, Base.view.y / Chunk.size.height);// UIManager.playerIndex;
		int cartX = center.x;
		int cartY = center.y;
		int isoX = (cartX / 2 + (cartY));
		int isoY = (cartY - cartX / 2);

		int indexX = (int) Math.floor((float) isoX / (float) 32);
		int indexY = (int) Math.floor((float) isoY / (float) 32);

		if (center != null) {
			int chunkX = indexX;// (int) (center.x / Chunk.size.width);
			int chunkY = indexY;// (int) (center.y / Chunk.size.height);
			for (int x = (chunkX - viewRange.x); x < (chunkX + viewRange.width); x++) {
				for (int y = (chunkY - viewRange.y); y < (chunkY + viewRange.height); y++) {
					Point key = new Point(x, y);
					Chunk chunk = chunks.get(key);
					if (chunk != null) {
						chunk.update(tickerUtil.ticked());
						chunksInView.add(chunk);
					}
				}
			}
		}
	}

	public void render() {
		for (Chunk chunk : chunksInView) {
			chunk.renderTiles();
		}
		for (Chunk chunk : chunksInView) {
			chunk.render();
		}
	}

	public static LinkedList<Point> chunksToBuild = new LinkedList<Point>();

	public static void move(Point index, Point newIndex) {
		if (index != null && newIndex != null) {
			int chunkX = (int) (index.x / Chunk.size.width);
			int chunkY = (int) (index.y / Chunk.size.height);
			Chunk chunk = ChunkManager.chunks.get(new Point(chunkX, chunkY));
			if (chunk != null) {
				int objX = (int) (index.x % 16);
				int objY = (int) (index.y % 16);
				Entity ent = chunk.entities.remove(new Point(objX, objY));
				if (ent != null) {
					int newChunkX = (int) (newIndex.x / Chunk.size.width);
					int newChunkY = (int) (newIndex.y / Chunk.size.height);
					Chunk newChunk = ChunkManager.chunks.get(new Point(newChunkX, newChunkY));
					if (newChunk != null) {
						int newObjX = (int) (newIndex.x % 16);
						int newObjY = (int) (newIndex.y % 16);
						newChunk.entities.put(new Point(newObjX, newObjY), ent);

						// chunksToBuild.add(new Point(newChunkX, newChunkY));
						newChunk.build();
					}
					// chunksToBuild.add(new Point(chunkX, chunkY));
					chunk.build();
				}
			}
		}
	}

	public static void setItemAtIndex(Point index, TextureType type) {
		if (index != null) {
			int chunkX = (int) (index.x / Chunk.size.width);
			int chunkY = (int) (index.y / Chunk.size.height);
			Chunk chunk = ChunkManager.chunks.get(new Point(chunkX, chunkY));
			if (chunk != null) {
				int objX = (int) (index.x % 16);
				int objY = (int) (index.y % 16);
				Object obj = chunk.objects.get(new Point(objX, objY));
				if (obj != null) {
					chunk.droppedItems.put(new Point(objX, objY), new GroundItem(type));
					chunk.build();

				}
			}
		}
	}

	public static void setTileAtIndex(Point index, TextureType type) {
		if (index != null) {
			int chunkX = (int) (index.x / Chunk.size.width);
			int chunkY = (int) (index.y / Chunk.size.height);
			Chunk chunk = ChunkManager.chunks.get(new Point(chunkX, chunkY));
			if (chunk != null) {
				int objX = (int) (index.x % 16);
				int objY = (int) (index.y % 16);
				Tile obj = chunk.tiles.get(new Point(objX, objY));
				if (obj != null) {
					chunk.tiles.put(new Point(objX, objY), new Tile(type));
					chunk.build();

				}
			}
		}
	}

	public static void setObjectAtIndex(Point index, TextureType type) {
		if (index != null) {
			int chunkX = (int) (index.x / Chunk.size.width);
			int chunkY = (int) (index.y / Chunk.size.height);
			Chunk chunk = ChunkManager.chunks.get(new Point(chunkX, chunkY));
			if (chunk != null) {
				int objX = (int) (index.x % 16);
				int objY = (int) (index.y % 16);
				Object obj = chunk.objects.get(new Point(objX, objY));
				if (obj != null) {
					chunk.objects.put(new Point(objX, objY), new Object(type));
					chunk.build();

				}
			}
		}
	}

	public static void removeType(TextureType type) {
		for (int x = 0; x < chunkDim.x; x++) {
			for (int y = 0; y < chunkDim.y; y++) {
				Chunk chunk = chunks.get(new Point(x, y));
				if (chunk != null) {
					chunk.removeType(type);
				}
			}
		}
	}

	public static boolean isResource(Point index) {
		boolean isRes = false;
		if (index != null) {
			int chunkX = (int) (index.x / Chunk.size.width);
			int chunkY = (int) (index.y / Chunk.size.height);
			Chunk chunk = ChunkManager.chunks.get(new Point(chunkX, chunkY));
			if (chunk != null) {
				int objX = (int) (index.x % 16);
				int objY = (int) (index.y % 16);
				Tile tile = chunk.getObjectAtIndex(new Point(objX, objY), true);
				if (tile != null) {
					isRes = (tile instanceof Resource);
				}
			}
		}

		return isRes;
	}

	public static boolean isItem(Point index) {
		boolean isItem = false;
		if (index != null) {
			int chunkX = (int) (index.x / Chunk.size.width);
			int chunkY = (int) (index.y / Chunk.size.height);
			Chunk chunk = ChunkManager.chunks.get(new Point(chunkX, chunkY));
			if (chunk != null) {
				int objX = (int) (index.x % 16);
				int objY = (int) (index.y % 16);
				Tile tile = chunk.getObjectAtIndex(new Point(objX, objY), true);
				if (tile != null) {
					isItem = (tile instanceof GroundItem);
				}
			}
		}

		return isItem;
	}

	public static boolean isSearchable(Point index) {
		boolean isSearchable = false;
		if (index != null) {
			int chunkX = (int) (index.x / Chunk.size.width);
			int chunkY = (int) (index.y / Chunk.size.height);
			Chunk chunk = ChunkManager.chunks.get(new Point(chunkX, chunkY));
			if (chunk != null) {
				int objX = (int) (index.x % 16);
				int objY = (int) (index.y % 16);
				Tile tile = chunk.getObjectAtIndex(new Point(objX, objY), true);
				if (tile != null) {
					ResourceData res = GameDatabase.resources.get(tile.getBaseType());
					if (res != null) {
						isSearchable = (res.isSearchable ? true : false);
					}
				}
			}
		}

		return isSearchable;
	}

	public static GroundItem getItem(Point index) {
		GroundItem item = null;
		if (index != null) {
			int chunkX = (int) (index.x / Chunk.size.width);
			int chunkY = (int) (index.y / Chunk.size.height);
			Chunk chunk = ChunkManager.chunks.get(new Point(chunkX, chunkY));
			if (chunk != null) {
				int objX = (int) (index.x % 16);
				int objY = (int) (index.y % 16);
				item = chunk.getItemAtIndex(new Point(objX, objY));
			}
		}

		return item;
	}

	public static Tile getTile(Point index) {
		Tile tile = null;
		if (index != null) {
			int chunkX = (int) (index.x / Chunk.size.width);
			int chunkY = (int) (index.y / Chunk.size.height);
			Chunk chunk = ChunkManager.chunks.get(new Point(chunkX, chunkY));
			if (chunk != null) {
				int objX = (int) (index.x % 16);
				int objY = (int) (index.y % 16);
				tile = chunk.getObjectAtIndex(new Point(objX, objY), true);
			}
		}

		return tile;
	}

	public static boolean resourceInRange(Point index, Point resourceIndex) {
		boolean inRange = false;
		Rectangle rec = new Rectangle(resourceIndex.x - 1, resourceIndex.y - 1, 3, 3);
		// System.out.println("rec:" + rec);
		if (rec.contains(index)) {
			inRange = true;
		}
		return inRange;
	}

	public static Resource getResource(Point index) {
		Resource res = null;
		if (index != null) {
			int chunkX = (int) (index.x / Chunk.size.width);
			int chunkY = (int) (index.y / Chunk.size.height);
			Chunk chunk = ChunkManager.chunks.get(new Point(chunkX, chunkY));
			if (chunk != null) {
				int objX = (int) (index.x % 16);
				int objY = (int) (index.y % 16);
				Tile tile = chunk.getObjectAtIndex(new Point(objX, objY), true);
				if (tile != null) {
					if (tile instanceof Resource) {
						res = (Resource) tile;
					} else if (tile instanceof GroundItem) {

					}
				}
			}
		}

		return res;
	}

	public static boolean checkSurroundingTilesFor(Point index, TextureType type) {
		boolean found = false;
		for (int x = index.x - 1; x <= index.x + 1; x++) {
			for (int y = index.y - 1; y <= index.y + 1; y++) {
				if ((index.x - x) + (index.y - y) == 1 || (index.x - x) + (index.y - y) == -1) {
					int chunkX = x / Chunk.size.width;
					int chunkY = y / Chunk.size.height;
					Chunk chunk = chunks.get(new Point(chunkX, chunkY));
					if (chunk != null) {
						int objX = (int) (x % Chunk.size.width);
						int objY = (int) (y % Chunk.size.height);
						Tile tile = chunk.getObjectAtIndex(new Point(objX, objY), true, false);

						if (tile != null) {
							if (tile.getType() == type) {
								found = true;
							}
						}
					}
					if (found) {
						break;
					}
				}
			}

			if (found) {
				break;
			}
		}

		return found;
	}
}