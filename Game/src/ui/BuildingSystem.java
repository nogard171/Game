package ui;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.LinkedList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import classes.Chunk;
import classes.CraftingTable;
import classes.ItemData;
import classes.Object;
import data.Settings;
import data.UIData;
import data.WorldData;
import utils.Renderer;
import utils.Tools;
import utils.Window;

public class BuildingSystem extends BaseSystem {

	private Rectangle closeBounds;
	private boolean closeHovered = false;

	public static Point selectedTable = null;

	private Rectangle titleBarBounds;
	private boolean titleHovered = false;
	private Point dragOffset;
	private boolean dragging = false;

	BuildingListView listView;

	@Override
	public void setup() {
		super.setup();

		baseBounds = new Rectangle(0, 0, 64, 343);
		baseBounds.y = (Window.height - 32) - baseBounds.height;
		setupBounds();

		listView = new BuildingListView(0, 16, 150, baseBounds.height - 49);
		listView.setup();
	}

	public void setupBounds() {
		titleBarBounds = new Rectangle(baseBounds.x, baseBounds.y, baseBounds.width - 21, 20);
		closeBounds = new Rectangle((baseBounds.x + baseBounds.width) - 20, baseBounds.y, 20, 20);

	}

	@Override
	public void update() {
		super.update();
		if (Window.wasResized()) {
			setupBounds();
		}
		if (showSystem) {

			if (baseBounds.contains(new Point(Window.getMouseX(), Window.getMouseY()))) {
				baseHovered = true;
			} else {
				baseHovered = false;
			}
			UserInterface.buildingHovered = baseHovered;
			titleHovered = titleBarBounds.contains(new Point(Window.getMouseX(), Window.getMouseY()));
			listView.update();

			closeHovered = closeBounds.contains(new Point(Window.getMouseX(), Window.getMouseY()));
			if (closeHovered && Window.isMainAction()) {
				this.showSystem = false;
				UserInterface.buildingHovered = false;
			}
			if (titleHovered && Window.isMainAction()) {
				if (dragOffset == null) {
					dragOffset = new Point(Window.getMouseX() - baseBounds.x, Window.getMouseY() - baseBounds.y);
					dragging = true;
					UserInterface.buildingDragging = true;
				}
				baseBounds.x = Window.getMouseX() - dragOffset.x;
				baseBounds.y = Window.getMouseY() - dragOffset.y;
			}

			if (dragging) {
				setupBounds();
			}
		} else {
			UserInterface.buildingHovered = false;
			dragging = false;
		}
	}

	@Override
	public void render() {
		super.render();
		if (showSystem) {
			Renderer.renderRectangle(baseBounds.x, baseBounds.y, baseBounds.width, baseBounds.height,
					new Color(0, 0, 0, 0.5f));
			Renderer.renderRectangle(titleBarBounds.x, titleBarBounds.y, titleBarBounds.width, titleBarBounds.height,
					new Color(1, 1, 1, 0.5f));

			Renderer.renderRectangle(closeBounds.x, closeBounds.y, closeBounds.width, closeBounds.height,
					new Color(1, 1, 1, 0.5f));

			Renderer.renderText(new Vector2f(closeBounds.x + 6, closeBounds.y + 1), "X", 12, Color.white);
			listView.render(baseBounds.x + 1, baseBounds.y + 48);

		}
	}

	@Override
	public void clean() {
		super.clean();

	}

}
