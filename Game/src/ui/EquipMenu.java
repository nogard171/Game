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

public class EquipMenu {
	public boolean showMenu = false;
	public boolean hovered = false;
	Rectangle menuBounds;

	public String equipmentName = "";

	LinkedHashMap<String, MenuItem> menuItems = new LinkedHashMap<String, MenuItem>();

	public void setup() {
		MenuItem unequip = new MenuItem(new AFunction() {
			public void click() {
				System.out.println("Equip");

				EquipmentItem equipItem = UserInterface.character.items.get(equipmentName.toUpperCase());
				if (equipItem != null) {
					if (equipItem.item != null) {

						UserInterface.inventory.addItem(equipItem.item);

						equipItem.item = null;

					}
				}

			}
		});
		unequip.text = "Unequip";
		menuItems.put(unequip.text.toUpperCase(), unequip);

		MenuItem inspect = new MenuItem(new AFunction() {
			public void click() {
				System.out.println("inspect");

			}
		});
		inspect.text = "Inspect";
		inspect.alwaysVisible = true;
		menuItems.put(inspect.text.toUpperCase(), inspect);

		MenuItem cancel = new MenuItem(new AFunction() {
			public void click() {
				System.out.println("cancel");
				showMenu = false;
			}
		});
		cancel.text = "Cancel";
		cancel.alwaysVisible = true;
		menuItems.put(cancel.text.toUpperCase(), cancel);

		menuBounds = new Rectangle(0, 0, 100, menuItems.size() * 13);
	}

	public void update() {
		if (Mouse.isButtonDown(Settings.secondaryActionIndex ) && UserInterface.character.equipName != "" && !showMenu) {
			showMenu = true;
			equipmentName = UserInterface.character.equipName;
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
			if (!item.alwaysVisible) {
				item.visible = false;
			} else if (item.alwaysVisible) {
				menuCount++;
			}
			EquipmentItem equipItem = UserInterface.character.items.get(equipmentName.toUpperCase());
			if (equipItem != null) {
				if (equipItem.item != null) {
					item.visible = true;
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
			if (equipmentName != "") {

				EquipmentItem equipItem = UserInterface.character.items.get(equipmentName.toUpperCase());
				// System.out.println("test:" + equipmentName);
				if (equipItem != null) {

					int cartX = (int) ((equipItem.bounds.getX()) + UserInterface.inventory.baseBounds.getX());
					int cartZ = (int) ((equipItem.bounds.getY()) + UserInterface.inventory.baseBounds.getY());

					menuBounds.x = cartX;
					menuBounds.y = cartZ;
					Renderer.renderRectangle(menuBounds.x, menuBounds.y, menuBounds.width, menuBounds.height,
							new Color(0, 0, 0, 0.5f));
					int y = 0;
					for (MenuItem item : menuItems.values()) {
						if (item.visible || item.alwaysVisible) {
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
	}

	public void clean() {

	}
}
