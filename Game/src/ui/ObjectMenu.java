package ui;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import classes.Chunk;
import classes.Object;
import classes.ResourceData;
import data.Settings;
import data.WorldData;
import threads.GameThread;
import utils.Renderer;
import utils.View;
import utils.Window;

public class ObjectMenu {
	Object obj;
	public boolean showObjectMenu = false;
	MouseIndex objectIndex;
	Rectangle menuBounds;

	LinkedHashMap<String, MenuItem> menuItems = new LinkedHashMap<String, MenuItem>();

	public void setup() {
		MenuItem mine = new MenuItem(new AFunction() {
			public void click() {
				Event move = new Event();
				move.eventName = "MOVE";
				move.end = new Point(objectIndex.getX(), objectIndex.getY());

				Event mine = new Event();
				mine.eventName = "MINE";
				mine.end = new Point(objectIndex.getX(), objectIndex.getY());
				move.followUpEvent = mine;
				EventManager.addEvent(move);
				showObjectMenu = false;
			}
		});

		mine.text = "Mine";
		menuItems.put(mine.text.toUpperCase(), mine);

		MenuItem till = new MenuItem(new AFunction() {
			public void click() {
				System.out.println("till");
			}
		});
		till.text = "Till";
		menuItems.put(till.text.toUpperCase(), till);

		MenuItem harvest = new MenuItem(new AFunction() {
			public void click() {
				Event move = new Event();
				move.eventName = "MOVE";
				move.end = new Point(objectIndex.getX(), objectIndex.getY());

				Event harvest = new Event();
				harvest.eventName = "HARVEST";
				harvest.end = new Point(objectIndex.getX(), objectIndex.getY());
				move.followUpEvent = harvest;
				EventManager.addEvent(move);
				showObjectMenu = false;
			}
		});
		harvest.text = "Harvest";
		menuItems.put(harvest.text.toUpperCase(), harvest);

		MenuItem chop = new MenuItem(new AFunction() {
			public void click() {
				Event move = new Event();
				move.eventName = "MOVE";
				move.end = new Point(objectIndex.getX(), objectIndex.getY());

				Event chop = new Event();
				chop.eventName = "CHOP";
				chop.end = new Point(objectIndex.getX(), objectIndex.getY());
				move.followUpEvent = chop;
				EventManager.addEvent(move);
				showObjectMenu = false;
			}
		});
		chop.text = "Chop";
		menuItems.put(chop.text.toUpperCase(), chop);

		MenuItem pickup = new MenuItem(new AFunction() {
			public void click() {
				System.out.println("pickup");
				Event move = new Event();
				move.eventName = "MOVE";
				move.end = new Point(objectIndex.getX(), objectIndex.getY());

				Event pickup = new Event();
				pickup.eventName = "PICKUP";
				pickup.end = new Point(objectIndex.getX(), objectIndex.getY());
				move.followUpEvent = pickup;
				EventManager.addEvent(move);
			}
		});
		pickup.text = "Pickup";
		menuItems.put(pickup.text.toUpperCase(), pickup);

		MenuItem craft = new MenuItem(new AFunction() {
			public void click() {
				System.out.println("craft");
				Event move = new Event();
				move.eventName = "MOVE";
				move.end = new Point(objectIndex.getX(), objectIndex.getY());

				Event craft = new Event();
				craft.eventName = "CRAFT";
				craft.end = new Point(objectIndex.getX(), objectIndex.getY());
				move.followUpEvent = craft;
				EventManager.addEvent(move);
			}
		});
		craft.text = "Craft";
		menuItems.put(craft.text.toUpperCase(), craft);

		MenuItem smelt = new MenuItem(new AFunction() {
			public void click() {
				System.out.println("craft");
				Event move = new Event();
				move.eventName = "MOVE";
				move.end = new Point(objectIndex.getX(), objectIndex.getY());

				Event smelt = new Event();
				smelt.eventName = "SMELT";
				smelt.end = new Point(objectIndex.getX(), objectIndex.getY());
				move.followUpEvent = smelt;
				EventManager.addEvent(move);
			}
		});
		smelt.text = "Smelt";
		menuItems.put(smelt.text.toUpperCase(), smelt);

		MenuItem info = new MenuItem(new AFunction() {
			public void click() {
				System.out.println("info");
			}
		});
		info.text = "Info";
		info.alwaysVisible = true;
		menuItems.put(info.text.toUpperCase(), info);
		MenuItem cancel = new MenuItem(new AFunction() {
			public void click() {
				showObjectMenu = false;
			}
		});
		cancel.text = "Cancel";
		cancel.alwaysVisible = true;
		menuItems.put(cancel.text.toUpperCase(), cancel);
		menuBounds = new Rectangle(0, 0, 100, menuItems.size() * 13);
	}

	int menuIn = 0;

	public void update() {
		if (Window.isSecondaryAction() && UserInterface.getHover() != null) {
			showObjectMenu = true;
			objectIndex = UserInterface.getHover();

			int hoverX = objectIndex.getX();
			int hoverY = objectIndex.getY();
			int chunkX = hoverX / 16;
			int chunkY = hoverY / 16;

			Chunk chunk = WorldData.chunks.get(chunkX + "," + chunkY);
			if (chunk != null) {
				int objX = objectIndex.getX() % 16;
				int objY = objectIndex.getY() % 16;
				if (objX < 0) {
					objX = (chunk.size.getWidth() + objX);
				}
				if (objY < 0) {
					objY = (chunk.size.getDepth() + objY);
				}

				Object ground = chunk.groundObjects[objX][objY];
				Object mask = chunk.maskObjects[objX][objY];
				Object item = chunk.groundItems[objX][objY];
				if (ground != null) {
					obj = ground;

				}
				if (mask != null && mask.getMaterial() != "AIR") {
					obj = mask;
				}
				if (item != null) {
					obj = item;
				}
			}

		}
		if (showObjectMenu) {
			int menuCartX = objectIndex.getX() * 32;
			int menuCartZ = objectIndex.getY() * 32;

			int isoX = menuCartX - menuCartZ;
			int isoZ = (menuCartX + menuCartZ) / 2;
			menuBounds.x = isoX;
			menuBounds.y = isoZ;

			for (MenuItem item : menuItems.values()) {
				int cartX = Window.getMouseX() - View.x;
				int cartY = Window.getMouseY() - View.y;
				if (item.bounds.contains(new Point(cartX, cartY))) {
					item.hovered = true;
				} else {
					item.hovered = false;
				}

				if (item.hovered && Window.isMainAction()) {
					item.click();
				} else {
					item.unclick();
				}
			}

			int cartX = Window.getMouseX() - View.x;
			int cartY = Window.getMouseY() - View.y;
			if (menuBounds.contains(new Point(cartX, cartY))) {
				menuIn++;
			}
			if (!menuBounds.contains(new Point(cartX, cartY)) && menuIn > 0) {
				showObjectMenu = false;
				menuIn = 0;
			}
		}
		fixMenu();
	}

	public void render() {
		if (showObjectMenu) {
			int cartX = (objectIndex.getX() * 32);
			int cartZ = (objectIndex.getY() * 32);

			int isoX = cartX - cartZ;
			int isoZ = (cartX + cartZ) / 2;
			menuBounds.x = isoX;
			menuBounds.y = isoZ;
			Renderer.renderRectangle(menuBounds.x, menuBounds.y, menuBounds.width, menuBounds.height,
					new Color(0, 0, 0, 0.5f));
			int y = 0;
			for (MenuItem item : menuItems.values()) {
				if (item.visible || item.alwaysVisible) {
					item.bounds = new Rectangle(isoX, (isoZ) + (y * 12) + 2, 100, 12);
					if (item.hovered) {
						Renderer.renderRectangle(item.bounds.x, item.bounds.y, item.bounds.width, item.bounds.height,
								new Color(1, 0, 0, 0.5f));
					}
					Renderer.renderText(new Vector2f(isoX + 3, isoZ + (y * 12)), item.text, 12, Color.white);

					y++;
				}
			}
		}
	}

	int menuCount = 0;

	public void fixMenu() {
		menuCount = 0;
		for (MenuItem item : menuItems.values()) {
			if (!item.alwaysVisible) {
				item.visible = false;
			} else if (item.alwaysVisible) {
				menuCount++;
			}
		}
		if (obj != null) {
			MenuItem menuItem;
			ResourceData data = WorldData.resourceData.get(obj.getMaterial());
			if (data != null) {
				menuItem = menuItems.get(data.action.toUpperCase());
				if (menuItem != null) {
					menuItem.visible = true;
					menuCount++;
				}
			}
			if (obj.isItem) {
				menuItem = menuItems.get("PICKUP");
				if (menuItem != null) {
					menuItem.visible = true;
					menuCount++;
				}
			}

			/*
			 * if (obj.getMaterial() == "TREE") { menuItem = menuItems.get("CHOP"); if
			 * (menuItem != null) { menuItem.visible = true; menuCount++; } } else if
			 * (obj.getMaterial() == "WHEAT") { menuItem = menuItems.get("HARVEST"); if
			 * (menuItem != null) { menuItem.visible = true; menuCount++; } } else if
			 * (obj.getMaterial() == "ORE") { menuItem = menuItems.get("MINE"); if (menuItem
			 * != null) { menuItem.visible = true; menuCount++; } } if
			 * (obj.getMaterial().contains("ITEM")) { menuItem = menuItems.get("PICKUP"); if
			 * (menuItem != null) { menuItem.visible = true; menuCount++; } } if
			 * (obj.getMaterial().contains("CRAFTING")) { menuItem = menuItems.get("CRAFT");
			 * if (menuItem != null) { menuItem.visible = true; menuCount++; } }
			 */
		}
		menuBounds = new Rectangle(0, 0, 100, menuCount * 13);
	}
}
