package ui;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import classes.Index;
import classes.InventoryItem;
import classes.Size;
import utils.Renderer;
import utils.Window;

public class InventorySystem extends BaseSystem {
	static ArrayList<InventoryItem> items = new ArrayList<InventoryItem>();

	public static Size size;
	public static Index hover;

	ItemMenu itemMenu;

	public static void addItem(InventoryItem newItem) {
		int count = newItem.count;
		for (int c = 0; c < count; c++) {
			newItem.count = 1;
			items.add(newItem);
		}
	}

	
	
	@Override
	public void setup() {
		super.setup();
		itemMenu = new ItemMenu();
		itemMenu.setup();
		size = new Size(7, 10);
		baseBounds = new Rectangle(0, 0, (size.getWidth() * 33) + 1, (size.getHeight() * 33) + 1);
		baseBounds.y = (Window.height - 32) - baseBounds.height;

		InventoryItem test = new InventoryItem();
		test.name = "Iron Sword";
		test.setMaterial("IRON_SWORD_ITEM");
		items.add(test);
		
	}

	@Override
	public void update() {
		super.update();
		if (Window.wasResized()) {
			baseBounds.y = (Window.height - 32) - baseBounds.height;
		}
		if (this.showSystem) {

			itemMenu.update();

			if (baseHovered) {
				UserInterface.inventoryHovered = true;

				int x = (Window.getMouseX() - baseBounds.x) / 33;
				int y = (Window.getMouseY() - baseBounds.y) / 33;

				int index = x + (y * size.getWidth());
				if (items.size() > index) {
					InventoryItem item = items.get(index);
					if (item != null) {

						hover = new Index(x, y);
						hint = item.name;
						hintPosition = new Point((x * 33) + baseBounds.x, (y * 33) + baseBounds.y);
					}
				} else {
					hint = "";
					hover = null;
				}

			} else {
				hint = "";
				hintPosition = null;
				hover = null;
				UserInterface.inventoryHovered = false;
			}
		} else {

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

			GL11.glEnable(GL11.GL_TEXTURE_2D);

			if (items.size() > 0) {
				if (hover != null) {
					GL11.glBegin(GL11.GL_QUADS);
					Renderer.renderRectangleWithoutBegin(baseBounds.x + 1 + (hover.getX() * 33),
							baseBounds.y + 1 + (hover.getY() * 33), 32, 32, new Color(0, 0, 0, 0.5f));
					GL11.glEnd();
				}
				for (int x = 0; x < size.getWidth(); x++) {
					for (int y = 0; y < size.getHeight(); y++) {
						if (x == size.getWidth() - 1 && y == size.getHeight() - 1) {
						} else {
							int index = x + (y * size.getWidth());
							if (items.size() > index) {
								InventoryItem item = items.get(index);

								if (item != null) {

									GL11.glBegin(GL11.GL_TRIANGLES);
									Renderer.renderModel(baseBounds.x + 1 + (x * 33), baseBounds.y + 1 + (y * 33),
											"SQUARE", item.getMaterial(), new Color(1, 1, 1, 1f));
									GL11.glEnd();

									if (item.count > 1) {

										Renderer.renderText(
												new Vector2f(baseBounds.x + 24 + (x * 33),
														baseBounds.y + 17 + (y * 33)),
												item.count + "", 12, Color.white);
									}
									item.hovered = false;

								}

							} else {
								break;
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
