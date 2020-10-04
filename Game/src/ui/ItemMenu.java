package ui;

import java.awt.ItemSelectable;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedHashMap;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import classes.Chunk;
import classes.Index;
import classes.InventoryItem;
import classes.ItemData;
import classes.Object;
import data.WorldData;
import utils.Renderer;
import utils.Window;
import data.CharacterData;
import data.Settings;

public class ItemMenu {
	InventoryItem item;
	public boolean showMenu = false;
	public boolean hovered = false;
	MouseIndex objectIndex;

	Index itemIndex;
	Rectangle menuBounds;

	LinkedHashMap<String, MenuItem> menuItems = new LinkedHashMap<String, MenuItem>();

	public void setup() {
		MenuItem equip = new MenuItem(new AFunction() {
			public void click() {
				System.out.println("Equip");

				int x = itemIndex.getX();
				int y = itemIndex.getY();

				int itemIndex = x + (y * UserInterface.inventory.size.getWidth());
				if (UserInterface.inventory.items.containsKey(itemIndex)) {
					InventoryItem equipItem = InventorySystem.items.remove(itemIndex);

					InventoryItem returnItem = CharacterSystem.equipItem(equipItem);

					if (returnItem != null) {
						InventorySystem.addItem(returnItem);
					}

				}
			}
		});
		equip.text = "Equip";
		menuItems.put(equip.text.toUpperCase(), equip);

		MenuItem drop = new MenuItem(new AFunction() {
			public void click() {

				Event drop = new Event();
				drop.eventName = "DROP_ITEM";
				drop.end = new Point(itemIndex.getX(), itemIndex.getY());
				EventManager.addEvent(drop);
			}
		});
		drop.text = "Drop";
		drop.anlwaysVisible = true;
		menuItems.put(drop.text.toUpperCase(), drop);

		MenuItem inspect = new MenuItem(new AFunction() {
			public void click() {
				System.out.println("inspect");

			}
		});
		inspect.text = "Inspect";
		inspect.anlwaysVisible = true;
		menuItems.put(inspect.text.toUpperCase(), inspect);

		MenuItem cancel = new MenuItem(new AFunction() {
			public void click() {
				System.out.println("cancel");
				showMenu = false;
			}
		});
		cancel.text = "Cancel";
		cancel.anlwaysVisible = true;
		menuItems.put(cancel.text.toUpperCase(), cancel);

		menuBounds = new Rectangle(0, 0, 100, menuItems.size() * 13);
	}

	public void update() {
		if (Mouse.isButtonDown(Settings.secondaryActionIndex ) && UserInterface.inventory.getHover() != null && !showMenu) {

			showMenu = true;
			itemIndex = UserInterface.inventory.getHover();

			if (itemIndex != null) {
				int x = itemIndex.getX();
				int y = itemIndex.getY();

				int index = x + (y * UserInterface.inventory.size.getWidth());
				if (UserInterface.inventory.items.containsKey(index)) {

					InventoryItem inventiryItem = UserInterface.inventory.items.get(index);
					if (inventiryItem != null) {
						item = inventiryItem;
					}
				}
			}

		}
		if (showMenu) {
			fixMenu();
			if (menuBounds.contains(new Point(Window.getMouseX(), Window.getMouseY()))) {
				for (MenuItem item : menuItems.values()) {
					item.hovered = false;
					if (item.bounds.contains(new Point(Window.getMouseX(), Window.getMouseY()))) {
						item.hovered = true;
						if (Window.isMainAction()) {
							item.click();
						} else {
							item.unclick();
						}
					}
				}

				menuIn++;
			}
			if (!menuBounds.contains(new Point(Window.getMouseX(), Window.getMouseY())) && menuIn > 0) {
				showMenu = false;
				menuIn = 0;
			}
		}
	}

	int menuIn = 0;
	int menuCount = 0;
	int previousCount = 0;

	public void fixMenu() {
		menuCount = 0;
		for (MenuItem item : menuItems.values()) {
			if (!item.anlwaysVisible) {
				item.visible = false;
			} else if (item.anlwaysVisible) {
				menuCount++;
			}
		}
		if (item != null) {
			MenuItem menuItem;
			if (item.name.toUpperCase().contains("SWORD")) {
				menuItem = menuItems.get("EQUIP");
				if (menuItem != null) {
					menuItem.visible = true;
					menuCount++;
				}
			}
		}
		if (item != null) {
			MenuItem menuItem;
			if (item.name.toUpperCase().contains("KNIFE")) {
				menuItem = menuItems.get("CARVE");
				if (menuItem != null) {
					menuItem.visible = true;
					menuCount++;
				}
			}
		}
		if (menuCount != previousCount) {
			menuBounds = new Rectangle(0, 0, 100, (menuCount) * 13);
			previousCount = menuCount;
		}
	}

	public void render() {
		if (showMenu) {
			if (itemIndex != null) {
				int cartX = (int) ((itemIndex.getX() * 33) + UserInterface.inventory.baseBounds.getX());
				int cartZ = (int) ((itemIndex.getY() * 33) + UserInterface.inventory.baseBounds.getY());

				menuBounds.x = cartX;
				menuBounds.y = cartZ;
				Renderer.renderRectangle(menuBounds.x, menuBounds.y, menuBounds.width, menuBounds.height,
						new Color(0, 0, 0, 0.5f));
				int y = 0;
				for (MenuItem item : menuItems.values()) {
					if (item.visible || item.anlwaysVisible) {
						item.bounds = new Rectangle(cartX, (cartZ) + (y * 12) + 2, 100, 12);
						if (item.hovered) {
							Renderer.renderRectangle(item.bounds.x, item.bounds.y, item.bounds.width,
									item.bounds.height, new Color(1, 0, 0, 0.5f));
						}
						Renderer.renderText(new Vector2f(cartX + 3, cartZ + (y * 12)), item.text, 12, Color.white);

						y++;
					}
				}
			}
		}
	}

	public void clean() {

	}
}
