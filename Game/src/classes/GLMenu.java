package classes;

import java.awt.Point;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.Rectangle;
import org.newdawn.slick.Color;

import game.Main;
import utils.GLDebug;

public class GLMenu {
	public boolean visible = false;
	private Rectangle bounds;
	public ArrayList<GLMenuItem> menuItems = new ArrayList<GLMenuItem>();

	public void setup() {
		GLMenuItem menuItem = new GLMenuItem("test");
		menuItem.click(new GLActionHandler() {
			public void onClick(GLMenuItem obj) {
				System.out.println("test");
			}
		});
		menuItems.add(menuItem);

		GLMenuItem menuItem2 = new GLMenuItem("test2");
		menuItem2.click(new GLActionHandler() {
			public void onClick(GLMenuItem obj) {
				System.out.println("test2");
			}
		});
		menuItems.add(menuItem2);
		updateBounds(100, 100);
	}

	public void updateBounds(int x, int y) {
		bounds = new Rectangle(x, y, 200, menuItems.size() * 14);
	}

	public void update() {
		if (visible) {
			Point mousePoint = new Point(Mouse.getX() + (int) Main.view.getPosition().x,
					Display.getHeight() - Mouse.getY() + (int) Main.view.getPosition().y);
			if (bounds.contains(mousePoint.x, mousePoint.y)) {
				for (GLMenuItem menuItem : menuItems) {
					menuItem.update();
				}
			}
		}
	}

	public void render() {

		if (visible) {
			GLDebug.RenderBackground(bounds.getX() - Main.view.getPosition().x,
					bounds.getY() - Main.view.getPosition().y, 200, menuItems.size() * 14);
			int i = 0;
			for (GLMenuItem menuItem : menuItems) {
				GLDebug.RenderString(bounds.getX() - Main.view.getPosition().x,
						bounds.getY() - Main.view.getPosition().y + (14 * i), "Menu Item: " + menuItem.getValue(), 12,
						Color.white);
				i++;
			}
		}
	}

	public void destroy() {

	}

	public void setPosition(int posX, int posZ) {
		this.bounds.setX(posX);
		this.bounds.setY(posZ);

	}
}
