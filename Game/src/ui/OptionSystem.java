package ui;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import classes.EquipmentItem;
import classes.InventoryItem;
import classes.ItemData;
import data.CharacterData;
import data.Settings;
import data.WorldData;
import utils.Renderer;
import utils.Window;

public class OptionSystem extends BaseSystem {

	String hint = "";
	Point hintPosition = new Point(0, 0);

	private DirectionMenu directionsMenu;
	private ActionMenu actionMenu;

	private Rectangle directionBounds;
	private Rectangle mainActionBounds;

	public Rectangle controlsBounds;
	public Rectangle graphicsBounds;
	public Rectangle etcBounds;
	public boolean controlsHovered = false;
	public boolean graphicsHovered = false;
	public boolean etcHovered = false;
	public boolean controlSelected = true;
	public boolean graphicsSelected = false;
	public boolean etcSelected = false;

	@Override
	public void setup() {
		super.setup();
		directionsMenu = new DirectionMenu();
		directionsMenu.setup();

		actionMenu = new ActionMenu();
		actionMenu.setup();

		baseBounds = new Rectangle(0, 0, 304, 333);
		baseBounds.y = (Window.height - 32) - baseBounds.height;

		controlsBounds = new Rectangle(baseBounds.x + 1, baseBounds.y + 1, 100, 20);
		graphicsBounds = new Rectangle(baseBounds.x + 102, baseBounds.y + 1, 100, 20);
		etcBounds = new Rectangle(baseBounds.x + 203, baseBounds.y + 1, 100, 20);

		mainActionBounds = new Rectangle(controlsBounds.x + 120, controlsBounds.y + 70, 100, 20);
		directionBounds = new Rectangle(controlsBounds.x + 120, controlsBounds.y + 40, 100, 20);

	}

	@Override
	public void update() {
		super.update();
		if (Window.wasResized()) {
			actionMenu.setup();
			directionsMenu.setup();
			controlsBounds = new Rectangle(baseBounds.x + 1, baseBounds.y + 1, 100, 20);
			graphicsBounds = new Rectangle(baseBounds.x + 102, baseBounds.y + 1, 100, 20);
			etcBounds = new Rectangle(baseBounds.x + 203, baseBounds.y + 1, 100, 20);

			mainActionBounds = new Rectangle(controlsBounds.x + 120, controlsBounds.y + 70, 100, 20);
			directionBounds = new Rectangle(controlsBounds.x + 120, controlsBounds.y + 40, 100, 20);
		}
		if (showSystem) {
			if (mainActionBounds.contains(new Point(Window.getMouseX(), Window.getMouseY()))) {
				actionMenu.poll();
			}
			actionMenu.update();

			if (directionBounds.contains(new Point(Window.getMouseX(), Window.getMouseY()))) {
				directionsMenu.poll();
			}
			directionsMenu.update();
			if (Window.wasResized()) {
				baseBounds.y = (Window.height - 32) - baseBounds.height;
			}
			if (baseBounds.contains(new Point(Window.getMouseX(), Window.getMouseY()))) {
				baseHovered = true;
			} else {
				baseHovered = false;
			}
			if (baseHovered) {
				UserInterface.optionsHovered = true;

				if (controlsBounds.contains(new Point(Window.getMouseX(), Window.getMouseY()))) {
					controlsHovered = true;
				} else {
					controlsHovered = false;
				}

				if (graphicsBounds.contains(new Point(Window.getMouseX(), Window.getMouseY()))) {
					graphicsHovered = true;
				} else {
					graphicsHovered = false;
				}

				if (etcBounds.contains(new Point(Window.getMouseX(), Window.getMouseY()))) {
					etcHovered = true;
				} else {
					etcHovered = false;
				}

				if (controlsHovered && Window.isMainAction()) {
					System.out.println("test");
					controlSelected = true;
					etcSelected = false;
					graphicsSelected = false;
				}
				if (graphicsHovered && Window.isMainAction()) {
					graphicsSelected = true;
					controlSelected = false;
					etcSelected = false;
				}
				if (etcHovered && Window.isMainAction()) {
					etcSelected = true;
					controlSelected = false;
					graphicsSelected = false;
				}

			} else {
				UserInterface.optionsHovered = false;
			}
		}
	}

	@Override
	public void render() {
		super.render();
		if (showSystem) {
			Renderer.renderRectangle(baseBounds.x, baseBounds.y, baseBounds.width, baseBounds.height,
					new Color(0, 0, 0, 0.5f));
			Color controlColor = new Color(0.5f, 0.5f, 0.5f, 0.5f);
			if (controlSelected) {
				controlColor = new Color(1, 1, 1, 0.5f);
			}
			if (controlsHovered) {
				controlColor = new Color(1, 0.5f, 0.5f, 0.5f);
			}
			Renderer.renderRectangle(controlsBounds.x, controlsBounds.y, controlsBounds.width, controlsBounds.height,
					controlColor);
			Renderer.renderText(new Vector2f(controlsBounds.x + 20, controlsBounds.y + 1), "Controls", 12, Color.white);
			if (controlSelected) {
				Renderer.renderText(new Vector2f(controlsBounds.x + 15, controlsBounds.y + 16), "Movement", 18,
						Color.white);
				Renderer.renderText(new Vector2f(controlsBounds.x + 15, controlsBounds.y + 40), "Direction", 12,
						Color.white);

				Renderer.renderRectangle(directionBounds.x, directionBounds.y, directionBounds.width,
						directionBounds.height, new Color(1, 1, 1, 0.5f));
				Renderer.renderText(new Vector2f(controlsBounds.x + 120, controlsBounds.y + 40),
						Settings.movementDirections, 12, Color.white);

				Renderer.renderText(new Vector2f(controlsBounds.x + 15, controlsBounds.y + 70), "Main Action", 12,
						Color.white);

				Renderer.renderRectangle(mainActionBounds.x, mainActionBounds.y, mainActionBounds.width,
						mainActionBounds.height, new Color(1, 1, 1, 0.5f));
				Renderer.renderText(new Vector2f(controlsBounds.x + 120, controlsBounds.y + 70), Settings.mainAction,
						12, Color.white);

			}

			Color graphicsColor = new Color(0.5f, 0.5f, 0.5f, 0.5f);
			if (graphicsSelected) {
				graphicsColor = new Color(1, 1, 1, 0.5f);
			}
			if (graphicsHovered) {
				graphicsColor = new Color(1, 0.5f, 0.5f, 0.5f);
			}
			Renderer.renderRectangle(graphicsBounds.x, graphicsBounds.y, graphicsBounds.width, graphicsBounds.height,
					graphicsColor);
			Renderer.renderText(new Vector2f(graphicsBounds.x + 20, graphicsBounds.y + 1), "Graphics", 12, Color.white);
			Color etcColor = new Color(0.5f, 0.5f, 0.5f, 0.5f);
			if (etcSelected) {
				etcColor = new Color(1, 1, 1, 0.5f);
			}
			if (etcHovered) {
				etcColor = new Color(1, 0.5f, 0.5f, 0.5f);
			}
			Renderer.renderRectangle(etcBounds.x, etcBounds.y, etcBounds.width, etcBounds.height, etcColor);
			Renderer.renderText(new Vector2f(etcBounds.x + 40, etcBounds.y + 1), "Etc", 12, Color.white);

			directionsMenu.render();
			actionMenu.render();
		}
	}

	@Override
	public void clean() {
		super.clean();

	}
}
