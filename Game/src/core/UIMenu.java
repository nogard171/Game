package core;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

public class UIMenu {

	public String name = "";
	public LinkedHashMap<String, UIMenuItem> menuItems = new LinkedHashMap<String, UIMenuItem>();

	public Rectangle bounds = new Rectangle(100, 100, 100, 100);
	public Rectangle padding = new Rectangle(5, 0, 0, 0);

	public boolean isVisible = false;
	public int resetMenuItems = 0;
	public int itemsVisible = 0;

	public UIAction onClick;

	public UIMenu(String newName, Rectangle newBounds, UIAction newOnClick) {
		this.name = newName;
		this.bounds = newBounds;
		this.onClick = newOnClick;
	}

	public void setItemVisiblility(String name, boolean isVisible) {
		UIMenuItem item = menuItems.get(name);
		if (item != null) {
			if (item.isVisible != isVisible) {
				item.isVisible = isVisible;
				// bounds.height += 16 + padding.y + padding.height;
			}
		}
	}

	public void addItem(UIMenuItem newItem) {
		menuItems.put(newItem.value.toLowerCase(), newItem);
		if (newItem.isVisible) {
			// bounds.height += 16 + padding.y + padding.height;
		}
	}

	public void update() {
		if (isVisible) {
			itemsVisible = 0;
			int y = 0;
			if (!bounds.contains(Input.getMousePoint())) {
				isVisible = false;
			}
			for (UIMenuItem item : menuItems.values()) {
				if (item.isVisible) {
					Rectangle rec = new Rectangle(bounds.x, bounds.y + (y * (16 + padding.y + padding.height)),
							bounds.width, 16 + padding.y + padding.height);
					item.bounds = rec;
					item.hovered = item.bounds.contains(Input.getMousePoint());
					if (item.hovered && Input.isMousePressed(0)) {
						item.onClick(this);
						item.onClick();
					}
					y++;
					itemsVisible++;
				}
			}
			resetMenuItems = 0;
		} else {
			if (resetMenuItems == 0) {
				for (UIMenuItem item : menuItems.values()) {
					item.isVisible = item.defaultVisibility;
					resetMenuItems++;
				}
			}
		}
	}

	public void render() {
		if (isVisible) {
			bounds = new Rectangle(bounds.x, bounds.y, bounds.width, (this.itemsVisible * 16));
			Renderer.renderQuad(bounds, new Color(0, 0, 0, 0.5f));
			int y = 0;
			for (UIMenuItem item : menuItems.values()) {
				if (item.isVisible) {
					if (item.hovered) {
						Renderer.renderQuad(item.bounds, new Color(1, 0, 0, 0.5f));
					}
					Renderer.renderText(
							new Vector2f(bounds.x + padding.x, bounds.y + (y * (16 + padding.y + padding.height))),
							item.value, 12, Color.white);
					y++;
				}
			}
		}
	}

	public void destroy() {

	}

	public void show() {
		Point p = Input.getMousePoint();
		bounds.x = p.x - 5;
		bounds.y = p.y - 5;
		this.isVisible = true;
		onClick.onClick(this);
	}
}
