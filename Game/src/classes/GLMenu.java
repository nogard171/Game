package classes;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.Rectangle;
import org.newdawn.slick.Color;

import core.GLChunk;
import core.GLChunkManager;
import game.Main;
import utils.GLDebug;

public class GLMenu {
	public boolean visible = false;
	private Rectangle bounds;
	public HashMap<String, GLMenuItem> menuItems = new HashMap<String, GLMenuItem>();
	protected GLIndex index;

	public void setup() {

	}

	public void updateBounds(int x, int y) {
		bounds = new Rectangle(x, y, 100, visibleCount() * 14);
	}
	
	private int visibleCount()
	{
		int i = 0;
		for (GLMenuItem menuItem : menuItems.values()) {
			if (menuItem.isVisible()) {
				i++;
			}
		}
		return i;
	}

	public void update() {
		if (visible) {
			Point mousePoint = new Point(Mouse.getX() + (int) Main.view.getPosition().x,
					Display.getHeight() - Mouse.getY() + (int) Main.view.getPosition().y);
			if (bounds.contains(mousePoint.x, mousePoint.y)) {
				int i = 0;
				for (GLMenuItem menuItem : menuItems.values()) {
					if (menuItem.isVisible()) {
						menuItem.setIndex(i);
						menuItem.update(bounds);
						i++;
					}
				}
			} else {
				if (Mouse.isButtonDown(0) && visible) {
					visible = false;
				}
			}
		}
	}

	public void render() {

		if (visible) {
			GLDebug.RenderBackground(bounds.getX() - Main.view.getPosition().x,
					bounds.getY() - Main.view.getPosition().y, bounds.getWidth(), bounds.getHeight());
			
			int i = 0;
			for (GLMenuItem menuItem : menuItems.values()) {
				if (menuItem.isVisible()) {
					menuItem.setIndex(i);
					if (menuItem.hovered) {
						GLDebug.RenderBackground(bounds.getX() - Main.view.getPosition().x,
								bounds.getY() - Main.view.getPosition().y + (14 * menuItem.getIndex()),
								bounds.getWidth(), 12, new Color(1, 1, 1, 0.2f));
					}

					GLDebug.RenderString(bounds.getX() - Main.view.getPosition().x,
							bounds.getY() - Main.view.getPosition().y + (14 * menuItem.getIndex()), menuItem.getValue(),
							12, Color.white);
					i++;
				}
			}
		}
	}

	public void destroy() {

	}

	public void setPosition(int posX, int posZ) {
		this.bounds.setX(posX);
		this.bounds.setY(posZ);
		updateBounds(posX, posZ);

	}

	public void setIndex(GLIndex hover) {
		index = hover;
	}
}
