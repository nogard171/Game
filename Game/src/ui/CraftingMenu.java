package ui;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedHashMap;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import classes.EquipmentItem;
import classes.ItemData;
import data.Settings;
import data.WorldData;
import utils.Renderer;
import utils.Window;

public class CraftingMenu {
	public boolean showMenu = false;
	public boolean hovered = false;
	Rectangle menuBounds;

	CraftingSlot slot;
	InventoryItem item;
	Point itemPosition = new Point(0, 0);

	LinkedHashMap<String, MenuItem> menuItems = new LinkedHashMap<String, MenuItem>();

	public void setup() {
		MenuItem remove = new MenuItem(new AFunction() {
			public void click() {
				System.out.println("remove");
				System.out.println("item: " + slot);
				InventoryItem slotItem = slot.slotItem;
				slot.slotItem = null;
				
				UserInterface.inventory.addItem(slotItem);
				
			}
		});
		remove.text = "Remove";
		menuItems.put(remove.text.toUpperCase(), remove);

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
		if (Mouse.isButtonDown(Settings.secondaryActionIndex) && !showMenu) {
			showMenu = true;
			itemPosition = UserInterface.crafting.getHoveredPosition();
			item = UserInterface.crafting.getHoveredItem();
			slot = UserInterface.crafting.getHoveredSlot();
			if (item != null) {
				System.out.println("tesT: " + item.getMaterial());
			}
			if (itemPosition == null) {
				showMenu = false;
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

		}
		if (!menuBounds.contains(new Point(Window.getMouseX(), Window.getMouseY())) && menuIn > 0) {
			showMenu = false;
			menuIn = 0;
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
			if (item != null) {
				menuItem = menuItems.get("REMOVE");
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

			int cartX = (int) (UserInterface.crafting.baseBounds.getX()
					+ (itemPosition.x - UserInterface.crafting.baseBounds.getX()));
			int cartZ = (int) (UserInterface.crafting.baseBounds.getY()
					+ (itemPosition.y - UserInterface.crafting.baseBounds.getY()));

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
