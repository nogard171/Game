package ui;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import classes.EquipmentItem;
import data.Settings;
import utils.Renderer;
import utils.Window;

public class BaseMenu {
	public boolean showMenu = false;
	public boolean hovered = false;
	Rectangle menuBounds;

	LinkedHashMap<String, MenuItem> menuItems = new LinkedHashMap<String, MenuItem>();

	public void setup() {
		menuBounds = new Rectangle(0, 0, 100, menuItems.size() * 13);
	}

	public void update() {
		if (Mouse.isButtonDown(Settings.secondaryActionIndex ) && !showMenu) {
			showMenu = true;
			menuBounds = new Rectangle(Window.getMouseX(), Window.getMouseY(), 100, menuItems.size() * 13);

		}
		if (showMenu) {
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

	public void render() {
		if (showMenu) {
			Renderer.renderRectangle(menuBounds.x, menuBounds.y, menuBounds.width, menuBounds.height,
					new Color(0, 0, 0, 0.5f));
			int y = 0;
			for (MenuItem item : menuItems.values()) {
				if (item.visible || item.anlwaysVisible) {
					item.bounds = new Rectangle(menuBounds.x, (menuBounds.y) + (y * 12) + 2, 100, 12);
					if (item.hovered) {
						Renderer.renderRectangle(item.bounds.x, item.bounds.y, item.bounds.width, item.bounds.height,
								new Color(1, 0, 0, 0.5f));
					}
					Renderer.renderText(new Vector2f(menuBounds.x + 3, menuBounds.y + (y * 12)), item.text, 12,
							Color.white);

					y++;
				}
			}
		}
	}

	public void clean() {

	}
}
