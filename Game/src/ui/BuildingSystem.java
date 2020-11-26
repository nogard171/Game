package ui;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.LinkedList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import classes.BuildingData;
import classes.BuildingMaterial;
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

	public static boolean building = false;

	BuildingListView listView;
	public static String selectedBuilding = "";

	@Override
	public void setup() {
		super.setup();

		baseBounds = new Rectangle(0, 0, 100, 343);
		baseBounds.y = (Window.height - 32) - baseBounds.height;
		setupBounds();

		listView = new BuildingListView(0, 16, 98, baseBounds.height - 100);
		listView.setup();

	}

	public void setupBounds() {
		titleBarBounds = new Rectangle(baseBounds.x, baseBounds.y, baseBounds.width - 21, 20);
		closeBounds = new Rectangle((baseBounds.x + baseBounds.width) - 20, baseBounds.y, 20, 20);
		listView = new BuildingListView(0, 16, 98, baseBounds.height - 120);
		listView.setup();

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
			UserInterface.buildingDragging = false;
			dragging = false;
		}
		if (Window.isSecondaryAction()) {
			building = false;
		}

		if (!Window.isMainAction()) {
			dragging = false;
			dragOffset = null;
			UserInterface.buildingDragging = false;
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

			if (UserInterface.hover != null) {
				GL11.glColor4f(1f, 0, 0, 0.5f);
				GL11.glBegin(GL11.GL_QUADS);
				Renderer.renderGrid(UserInterface.hover.getX(), UserInterface.hover.getY());
				GL11.glEnd();
			}
			
			
			Renderer.renderRectangle(baseBounds.x+1, baseBounds.y+baseBounds.height-33,32,32,
					new Color(1, 1, 1, 0.5f));
			Renderer.renderRectangle(baseBounds.x+34, baseBounds.y+baseBounds.height-33,32,32,
					new Color(1, 1, 1, 0.5f));
			Renderer.renderRectangle(baseBounds.x+67, baseBounds.y+baseBounds.height-33,32,32,
					new Color(1, 1, 1, 0.5f));
			

			Renderer.renderRectangle(baseBounds.x+1, baseBounds.y+baseBounds.height-66,32,32,
					new Color(1, 1, 1, 0.5f));
			Renderer.renderRectangle(baseBounds.x+34, baseBounds.y+baseBounds.height-66,32,32,
					new Color(1, 1, 1, 0.5f));
			Renderer.renderRectangle(baseBounds.x+67, baseBounds.y+baseBounds.height-66,32,32,
					new Color(1, 1, 1, 0.5f));
		}
	}

	public void handleBuild(int x, int y) {

		Event move = new Event();
		move.eventName = "MOVE";
		move.end = new Point(x, y);

		Event secondary = new Event();
		secondary.eventName = "BUILD";
		if (BuildingSystem.selectedBuilding.equals("DECONSTRUCT")) {
			secondary.eventName = "DECONSTRUCT";
		}
		secondary.end = new Point(x, y);

		Event buildItem = new Event();
		buildItem.eventName = selectedBuilding;
		secondary.followUpEvent = buildItem;

		move.followUpEvent = secondary;

		EventManager.addEvent(move);

	}

	public void renderOnMap() {
		if (selectedBuilding != "" && building) {
			if (UserInterface.hover != null) {
				/*
				 * GL11.glColor4f(1f, 0, 0, 0.5f); GL11.glBegin(GL11.GL_QUADS);
				 * Renderer.renderGrid(UserInterface.hover.getX(), UserInterface.hover.getY());
				 * GL11.glEnd();
				 */

				BuildingData buildData = UIData.buildingData.get(selectedBuilding);

				if (buildData != null) {
					/*
					 * Renderer.renderModel(baseBounds.x + 145, baseBounds.y + 309, "COIN",
					 * "COPPER_CURRENCY", new Color(1, 1, 1, 1f));
					 * 
					 */
					GL11.glBegin(GL11.GL_TRIANGLES);
					for (int b = 0; b < buildData.materials.size(); b++) {
						BuildingMaterial mat = buildData.materials.get(b);
						if (mat != null) {
							int carX = 0;// (UserInterface.hover.getChunkX() * 32) * 16;
							int carY = 0;// (UserInterface.hover.getChunkY() * 32) * 16;
							int isoX = carX - carY;
							int isoY = (carY + carX) / 2;

							int selfX = isoX;
							int selfY = isoY;
							int objX = (UserInterface.hover.getX() * 32) - (UserInterface.hover.getY() * 32);
							int objY = ((UserInterface.hover.getY() * 32) + (UserInterface.hover.getX() * 32)) / 2;
							Renderer.renderModel(objX + selfX + mat.offset.x, objY + selfY + mat.offset.y,
									buildData.model, mat.name, new Color(1f, 1f, 1f, 0.5f));
						}

					}
					GL11.glEnd();
				}
			}
		}

	}

	@Override
	public void clean() {
		super.clean();
	}

	static int totalConstruction = 0;
	static int totalDeconstruction = 0;

	public static void construct() {
		totalConstruction++;
	}

	public static void deconstruct() {
		totalDeconstruction++;
	}

	public static void complete() {
		totalConstruction = 0;
		totalDeconstruction = 0;
	}

}
