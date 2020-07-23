package ui;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import classes.Item;
import classes.Size;
import utils.Renderer;
import utils.Window;

public class InventorySystem {
	static ArrayList<Item> items = new ArrayList<Item>();

	public boolean showInventory = false;
	public Rectangle inventoryBounds;
	public Size size;

	public static void addItem(Item newItem) {
		int count = newItem.count;
		for (int c = 0; c < count; c++) {
			newItem.count = 1;
			items.add(newItem);
		}
	}

	public void setup() {
		size = new Size(7, 10);
		inventoryBounds = new Rectangle(0, 0, (size.getWidth() * 33) + 1, (size.getHeight() * 33) + 1);
		inventoryBounds.y = (Window.height - 32) - inventoryBounds.height;
	}

	public void update() {

		if (Window.wasResized()) {
			inventoryBounds.y = (Window.height - 32) - inventoryBounds.height;
		}
		if (showInventory) {
			if (inventoryBounds.contains(new Point(Window.getMouseX(), Window.getMouseY()))) {
				UserInterface.inventoryHovered = true;

				int x = (Window.getMouseX() - inventoryBounds.x) / 33;
				int y = (Window.getMouseY() - inventoryBounds.y) / 33;

				int index = x + (y * size.getWidth());
				if (items.size() > index) {
					Item item = items.get(index);
					if (item != null) {
						hint = item.name;
						hintPosition = new Point((x * 33) + inventoryBounds.x, (y * 33) + inventoryBounds.y);
					}
				} else {
					hint = "";
				}

			} else {
				hint = "";
				UserInterface.inventoryHovered = false;
			}
		}
	}

	String hint = "";
	Point hintPosition = new Point(0, 0);
	public int inventoryBackID = -1;

	public void render() {
		if (showInventory) {

			Renderer.renderRectangle(inventoryBounds.x, inventoryBounds.y, inventoryBounds.width,
					inventoryBounds.height, new Color(0, 0, 0, 0.5f));
			if (inventoryBackID <= -1) {

				inventoryBackID = GL11.glGenLists(1);

				GL11.glNewList(inventoryBackID, GL11.GL_COMPILE);

				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glBegin(GL11.GL_QUADS);
				for (int x = 0; x < size.getWidth(); x++) {
					for (int y = 0; y < size.getHeight(); y++) {
						if (x == size.getWidth() - 1 && y == size.getHeight() - 1) {
						} else {
							Renderer.renderRectangleWithoutBegin(inventoryBounds.x + 1 + (x * 33),
									inventoryBounds.y + 1 + (y * 33), 32, 32, new Color(1, 1, 1, 0.5f));

						}
					}
				}
				GL11.glEnd();

				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glEndList();
			} else {
				GL11.glCallList(inventoryBackID);
			}
			if (items.size() > 0) {
				for (int x = 0; x < size.getWidth(); x++) {
					for (int y = 0; y < size.getHeight(); y++) {
						if (x == size.getWidth() - 1 && y == size.getHeight() - 1) {
						} else {

							if (items.size() > 0) {
								int index = x + (y * size.getWidth());
								if (items.size() > index) {
									Item item = items.get(index);
									if (item != null) {
										GL11.glBegin(GL11.GL_TRIANGLES);
										Renderer.renderModel(inventoryBounds.x + 1 + (x * 33),
												inventoryBounds.y + 1 + (y * 33), "SQUARE", item.name,
												new Color(1, 1, 1, 1f));
										GL11.glEnd();
										/*
										 * if (item.count > 1) {
										 * 
										 * Renderer.renderText( new Vector2f(inventoryBounds.x + 24 + (x * 33),
										 * inventoryBounds.y + 17 + (y * 33)), item.count + "", 12, Color.white); }
										 */
									}

								} else {
									break;
								}
							}
						}
					}
				}
			}

			if (hint != "") {
				Renderer.renderRectangle(hintPosition.x, hintPosition.y + 2, hint.length() * 7, 14,
						new Color(0, 0, 0, 0.5f));
				Renderer.renderText(new Vector2f(hintPosition.x, hintPosition.y), hint, 12, Color.white);
			}

			Renderer.renderText(new Vector2f(inventoryBounds.x + 1 + ((size.getWidth() - 1) * 33),
					inventoryBounds.y + 0 + ((size.getHeight() - 1) * 33)), items.size() + "", 12, Color.white);
			Renderer.renderText(new Vector2f(inventoryBounds.x + 13 + ((size.getWidth() - 1) * 33),
					inventoryBounds.y + 8 + ((size.getHeight() - 1) * 33)), "/", 12, Color.white);

			Renderer.renderText(
					new Vector2f(inventoryBounds.x + 19 + ((size.getWidth() - 1) * 33),
							inventoryBounds.y + 17 + ((size.getHeight() - 1) * 33)),
					"" + ((size.getWidth() * size.getHeight()) - 1), 12, Color.white);
		}
	}

	public void clean() {

	}
}
