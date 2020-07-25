package ui;

import java.awt.Rectangle;
import java.util.LinkedHashMap;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import classes.AFunction;
import classes.Chunk;
import classes.Index;
import classes.Item;
import classes.Object;
import data.WorldData;
import utils.Renderer;
import utils.Window;

public class ItemMenu {
	Item item;
	public boolean showMenu = false;

	Index itemIndex;
	Rectangle menuBounds;

	LinkedHashMap<String, MenuItem> menuItems = new LinkedHashMap<String, MenuItem>();

	public void setup() {

		MenuItem drop = new MenuItem(new AFunction() {
			public void click() {
			}
		});
		drop.text = "Drop";
		drop.anlwaysVisible = true;
		menuItems.put(drop.text.toUpperCase(), drop);

		MenuItem inspect = new MenuItem(new AFunction() {
			public void click() {
			}
		});
		inspect.text = "Inspect";
		inspect.anlwaysVisible = true;
		menuItems.put(inspect.text.toUpperCase(), inspect);

		MenuItem cancel = new MenuItem(new AFunction() {
			public void click() {
			}
		});
		cancel.text = "Cancel";
		cancel.anlwaysVisible = true;
		menuItems.put(cancel.text.toUpperCase(), cancel);

		menuBounds = new Rectangle(0, 0, 100, menuItems.size() * 13);
	}

	public void update() {
		if (Mouse.isButtonDown(1) && UserInterface.getHover() != null && !showMenu) {
			showMenu = true;
			itemIndex = InventorySystem.getHover();
			if (itemIndex != null) {
				int x = itemIndex.getX();
				int y = itemIndex.getY();

				int index = x + (y * InventorySystem.size.getWidth());
				if (InventorySystem.items.size() > index) {

					Item item = InventorySystem.items.get(index);
					if (item != null) {
						System.out.println("Item: " + item.name);
					}
				}
			}
		}
	}

	public void render() {
		if (showMenu) {
			int cartX = (int) ((itemIndex.getX() * 33) + InventorySystem.inventoryBounds.getX());
			int cartZ = (int) ((itemIndex.getY() * 33) + InventorySystem.inventoryBounds.getY());

			menuBounds.x = cartX;
			menuBounds.y = cartZ;
			Renderer.renderRectangle(menuBounds.x, menuBounds.y, menuBounds.width, menuBounds.height,
					new Color(0, 0, 0, 0.5f));
			int y = 0;
			for (MenuItem item : menuItems.values()) {
				if (item.visible || item.anlwaysVisible) {
					item.bounds = new Rectangle(cartX, (cartZ) + (y * 12) + 2, 100, 12);
					if (item.hovered) {
						Renderer.renderRectangle(item.bounds.x, item.bounds.y, item.bounds.width, item.bounds.height,
								new Color(1, 0, 0, 0.5f));
					}
					Renderer.renderText(new Vector2f(cartX + 3, cartZ + (y * 12)), item.text, 12, Color.white);

					y++;
				}
			}
		}
	}

	public void clean() {

	}
}
