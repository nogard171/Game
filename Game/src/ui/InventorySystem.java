package ui;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import classes.Index;
import classes.ItemData;
import classes.Size;
import data.Settings;
import data.WorldData;
import utils.Renderer;
import utils.Window;

public class InventorySystem extends BaseSystem {
	static LinkedHashMap<Integer, InventoryItem> items = new LinkedHashMap<Integer, InventoryItem>();

	// static int lastIndex = 0;
	public static Size size;
	public static Index hover;
	public Index startIndex;
	public InventoryItem draggedItem = null;
	public Point draggedPosition = new Point(0, 0);
	public static boolean dragging = false;
	private static int draggedIndex = -1;

	ItemMenu itemMenu;

	public static void addItem(InventoryItem newItem) {
		int count = newItem.count;
		boolean add = true;
		/*
		 * // for (int c = 0; c < count; c++) { boolean add = false; // newItem.count =
		 * 1; while (!add) { if (items.containsKey(lastIndex)) { if
		 * (items.get(lastIndex) == null) { add = true; } else { lastIndex++; } } else {
		 * add = true; } }
		 * 
		 * 
		 * if (add) { items.put(lastIndex, newItem); }
		 * 
		 * }
		 */
		int lastIndex = -1;
		int nextIndex = -1;
		for (int x = 0; x < size.getWidth(); x++) {
			for (int y = 0; y < size.getHeight(); y++) {
				if (x == size.getWidth() - 1 && y == size.getHeight() - 1) {
				} else {
					int index = x + (y * size.getWidth());
					if (items.containsKey(index)) {
						InventoryItem item = items.get(index);
						// System.out.println("test: " + index + "=" + item);
						if (item != null) {
							ItemData data = WorldData.itemData.get(item.name);
							if (data != null) {
								if (item.name == newItem.name && item.count < data.stackSize) {
									item.count += newItem.count;
									add = false;
								} else {
									nextIndex = index + 1;
								}
							} else {
								lastIndex = index + 1;
							}
						}
					}
				}
			}
		}
		if (nextIndex < 0 && lastIndex < 0) {
			nextIndex = 0;
		}
		// System.out.println("index: " + lastIndex);
		if (add && nextIndex >= 0) {

			System.out.println("test2:" + nextIndex + "=" + newItem.durability);
			while (items.containsKey(nextIndex)) {
				nextIndex++;
			}
			items.put(nextIndex, newItem);
		} else if (add && lastIndex >= 0) {
			while (items.containsKey(lastIndex)) {
				lastIndex++;
			}
			items.put(lastIndex, newItem);
		}
	}

	public static void addItemAt(int index, InventoryItem newItem) {
		// int count = newItem.count;
		// for (int c = 0; c < count; c++) {
		// newItem.count = 1;
		if (!items.containsKey(index)) {
			items.put(index, newItem);
		} else {
			items.put(draggedIndex, newItem);
		}
		// lastIndex += index;
		// }
	}

	@Override
	public void setup() {
		super.setup();
		itemMenu = new ItemMenu();
		itemMenu.setup();
		size = new Size(7, 10);
		baseBounds = new Rectangle(0, 0, (size.getWidth() * 33) + 1, (size.getHeight() * 33) + 1);
		baseBounds.y = (Window.height - 32) - baseBounds.height;

		/*
		ItemData data = WorldData.itemData.get("IRON_SWORD_ITEM");
		InventoryItem test = new InventoryItem();
		test.commonName = data.commonName;
		test.name = data.name;
		test.setMaterial(data.material);
		addItem(test);

		data = WorldData.itemData.get("STONE_TOOL_ITEM");
		InventoryItem knife = new InventoryItem();
		knife.commonName = data.commonName;
		knife.name = data.name;
		knife.setMaterial(data.material);
		knife.durability = 1;
		addItem(knife);*/

	}

	private boolean moveItemOut = false;
	private InventoryItem movedItem;

	public void moveItemOut(boolean moveOut) {
		moveItemOut = moveOut;
	}

	public InventoryItem getDraggedItem() {
		movedItem = draggedItem;
		draggedItem = null;
		dragging = false;
		return movedItem;
	}

	@Override
	public void update() {
		super.update();

		if (this.showSystem) {
			itemMenu.update();

			if (baseHovered) {
				UserInterface.inventoryHovered = true;

				int x = (Window.getMouseX() - baseBounds.x) / 33;
				int y = (Window.getMouseY() - baseBounds.y) / 33;

				if (x >= size.getWidth() - 1 && y >= size.getHeight() - 1) {
				} else {
					hover = new Index(x, y);
					int index = x + (y * size.getWidth());
					if (items.containsKey(index)) {
						InventoryItem item = items.get(index);
						if (item != null) {
							hint = item.name;
							hintPosition = new Point((x * 33) + baseBounds.x, (y * 33) + baseBounds.y);
						}
					} else {
						hint = "";
					}
					if (Window.isMainAction() && hover != null && draggedItem == null && !itemMenu.showMenu) {
						if (items.containsKey(index)) {
							draggedItem = items.remove(index);
							draggedIndex = index;
							startIndex = hover;
							dragging = true;
						}
					}

					if (!Window.isMainAction() && draggedItem != null && !moveItemOut) {
						addItemAt(index, draggedItem);
						dragging = false;
						draggedItem = null;
						draggedIndex = -1;
					}
				}

			} else {
				hint = "";
				hintPosition = null;
				hover = null;
				UserInterface.inventoryHovered = false;
				if (!Window.isMainAction() && draggedItem != null && !moveItemOut) {
					int start = startIndex.getX() + (startIndex.getY() * size.getWidth());
					addItemAt(start, draggedItem);
					dragging = false;
					draggedItem = null;
				}
				if (!Window.isMainAction() && draggedItem == null && moveItemOut) {
					moveItemOut = false;
				}
			}
			if (Window.isMainAction() && draggedItem != null) {
				draggedPosition = new Point(Window.getMouseX(), Window.getMouseY());
			}

			if (items.size() > 0) {
				for (int x = 0; x < size.getWidth(); x++) {
					for (int y = 0; y < size.getHeight(); y++) {
						if (x >= size.getWidth() - 1 && y >= size.getHeight() - 1) {
						} else {
							int index = x + (y * size.getWidth());
							if (items.containsKey(index)) {
								InventoryItem item = items.get(index);
								if (item != null) {
									ItemData data = WorldData.itemData.get(item.name);
									if (data != null) {

										// System.out.println("dura: " + item.durability + "/" + data.durability);
										if (item.durability <= 0 && data.durability > 0) {
											items.remove(index);
										}
									}
								}
							}
						}
					}
				}
			}

		} else {
			UserInterface.inventoryHovered = false;
		}
	}

	String hint = "";
	Point hintPosition = new Point(0, 0);

	public int inventoryBackID = -1;

	@Override
	public void render() {
		if (showSystem) {

			Renderer.renderRectangle(baseBounds.x, baseBounds.y, baseBounds.width, baseBounds.height,
					new Color(0, 0, 0, 0.5f));

			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glBegin(GL11.GL_QUADS);
			for (int x = 0; x < size.getWidth(); x++) {
				for (int y = 0; y < size.getHeight(); y++) {
					if (x == size.getWidth() - 1 && y == size.getHeight() - 1) {
					} else {
						Renderer.renderRectangleWithoutBegin(baseBounds.x + 1 + (x * 33), baseBounds.y + 1 + (y * 33),
								32, 32, new Color(1, 1, 1, 0.5f));
					}
				}
			}
			GL11.glEnd();

			if (hover != null) {
				if (hover.getX() == size.getWidth() - 1 && hover.getY() == size.getHeight() - 1) {
				} else {
					GL11.glBegin(GL11.GL_QUADS);
					Renderer.renderRectangleWithoutBegin(baseBounds.x + 1 + (hover.getX() * 33),
							baseBounds.y + 1 + (hover.getY() * 33), 32, 32, new Color(0, 0, 0, 0.5f));
					GL11.glEnd();
				}
			}

			GL11.glEnable(GL11.GL_TEXTURE_2D);

			if (items.size() > 0) {
				for (int x = 0; x < size.getWidth(); x++) {
					for (int y = 0; y < size.getHeight(); y++) {
						if (x >= size.getWidth() - 1 && y >= size.getHeight() - 1) {
						} else {
							int index = x + (y * size.getWidth());
							if (items.containsKey(index)) {
								InventoryItem item = items.get(index);

								if (item != null) {

									ItemData data = WorldData.itemData.get(item.name);

									if (data != null) {
										// System.out.println("data: " + data.inventoryMaterial);
										GL11.glBegin(GL11.GL_TRIANGLES);
										Renderer.renderModel(baseBounds.x + 1 + (x * 33), baseBounds.y + 1 + (y * 33),
												"SQUARE", data.inventoryMaterial, new Color(1, 1, 1, 1f));
										GL11.glEnd();
										if (data.durability > 0) {
											int width = (int) (((float) item.durability / (float) data.durability)
													* (float) 32);

											Renderer.renderRectangle(baseBounds.x + 1 + (x * 33),
													baseBounds.y + 1 + (y * 33) + 29, width, 3,
													new Color(0, 1, 0, 0.5f));
										}
										if (item.count > 1) {

											Renderer.renderText(
													new Vector2f(baseBounds.x + 24 + (x * 33),
															baseBounds.y + 17 + (y * 33)),
													item.count + "", 12, Color.white);
										}
										item.hovered = false;
									}

								}

							}

						}
					}
				}
			}

			if (draggedItem != null) {
				GL11.glBegin(GL11.GL_TRIANGLES);
				Renderer.renderModel(draggedPosition.x, draggedPosition.y, "SQUARE", draggedItem.getMaterial(),
						new Color(1, 1, 1, 1f));
				GL11.glEnd();
			}

			if (hint != "") {
				Renderer.renderRectangle(hintPosition.x, hintPosition.y + 2, hint.length() * 7, 14,
						new Color(0, 0, 0, 0.5f));
				Renderer.renderText(new Vector2f(hintPosition.x, hintPosition.y), hint, 12, Color.white);
			}

			Renderer.renderText(new Vector2f(baseBounds.x + 1 + ((size.getWidth() - 1) * 33),
					baseBounds.y + 0 + ((size.getHeight() - 1) * 33)), items.size() + "", 12, Color.white);
			Renderer.renderText(new Vector2f(baseBounds.x + 13 + ((size.getWidth() - 1) * 33),
					baseBounds.y + 8 + ((size.getHeight() - 1) * 33)), "/", 12, Color.white);

			Renderer.renderText(
					new Vector2f(baseBounds.x + 19 + ((size.getWidth() - 1) * 33),
							baseBounds.y + 17 + ((size.getHeight() - 1) * 33)),
					"" + ((size.getWidth() * size.getHeight()) - 1), 12, Color.white);
			itemMenu.render();

		}
	}

	@Override
	public void clean() {

	}

	public static Index getHover() {
		return hover;
	}
}
