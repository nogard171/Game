package ui;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.LinkedList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import classes.ItemData;
import data.Settings;
import data.UIData;
import data.WorldData;
import utils.Renderer;
import utils.Window;

public class SmeltingSystem extends BaseSystem {

	public static LinkedList<SmeltingSlot> slots = new LinkedList<SmeltingSlot>();

	private Rectangle closeBounds;
	private boolean closeHovered = false;

	private Rectangle titleBarBounds;
	private boolean titleHovered = false;
	private Point dragOffset;
	private boolean dragging = false;

	public static int fuel = 0;
	public static int currentFuel = 0;

	public static SmeltingSlot fuelSlot;
	public static InventoryItem smeltedItem;

	private SmeltingMenu menu;
	Button craft;

	@Override
	public void setup() {
		super.setup();
		menu = new SmeltingMenu();
		menu.setup();

		baseBounds = new Rectangle(0, 0, 304, 333);
		baseBounds.y = (Window.height - 32) - baseBounds.height;
		setupBounds();
		SmeltingSlot newSlot = new SmeltingSlot(60, 60);
		slots.add(newSlot);

		fuelSlot = new SmeltingSlot(60, 101);

	}

	public void setupBounds() {
		titleBarBounds = new Rectangle(baseBounds.x, baseBounds.y, baseBounds.width - 21, 20);
		closeBounds = new Rectangle((baseBounds.x + baseBounds.width) - 20, baseBounds.y, 20, 20);

		craft = new Button(new Rectangle(baseBounds.x + 75, baseBounds.y + 145, 50, 20), "Smelt", new AFunction() {
			public void click() {
				proceedSmelting();
			}
		});
	}

	@Override
	public void update() {
		super.update();
		if (Window.wasResized()) {
			setupBounds();
		}
		if (showSystem) {
			menu.update();
			if (baseBounds.contains(new Point(Window.getMouseX(), Window.getMouseY()))) {
				baseHovered = true;
			} else {
				baseHovered = false;
			}
			UserInterface.smeltingHovered = baseHovered;
			titleHovered = titleBarBounds.contains(new Point(Window.getMouseX(), Window.getMouseY()));

			if (titleHovered && Window.isMainAction()) {

				if (dragOffset == null) {
					dragOffset = new Point(Window.getMouseX() - baseBounds.x, Window.getMouseY() - baseBounds.y);
					dragging = true;
					UserInterface.craftingDragging = true;
				}
				baseBounds.x = Window.getMouseX() - dragOffset.x;
				baseBounds.y = Window.getMouseY() - dragOffset.y;
			}
			if (dragging) {
				setupBounds();
			} else {
				UserInterface.inventory.moveItemOut(false);
			}

			closeHovered = closeBounds.contains(new Point(Window.getMouseX(), Window.getMouseY()));

			if (closeHovered && Window.isMainAction()) {
				this.showSystem = false;
				UserInterface.smeltingHovered = false;
				UserInterface.smeltingDragging = false;
			}

			for (SmeltingSlot slot : slots) {
				slot.update();
			}

			fuelSlot.update();

			craft.update();

		} /*
			 * else { UserInterface.inventory.moveItemOut(false);
			 * UserInterface.smeltingHovered = false; UserInterface.smeltingDragging =
			 * false;
			 * 
			 * dragging = false; }
			 * 
			 * if (!Window.isMainAction()) { dragging = false; dragOffset = null;
			 * UserInterface.smeltingDragging = false; }
			 */

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
			int itemCount = 0;
			for (SmeltingSlot slot : slots) {
				slot.render(baseBounds.x, baseBounds.y);
				itemCount++;
			}

			Renderer.renderRectangle(baseBounds.x + 60, baseBounds.y + 94, 32, 5, new Color(1, 1, 1, 0.5f));

			if (fuel > 0) {
				
				int fuelWidth = (int) (((float) currentFuel / (float) fuel) * (float) 32);
				Renderer.renderRectangle(baseBounds.x + 60, baseBounds.y + 94, fuelWidth, 5, new Color(1, 0.6f, 0, 0.75f));

			}
			/*
			 * if (fuelSlot.slotItem != null) { ItemData data =
			 * WorldData.itemData.get(fuelSlot.slotItem.name); if (data != null) { if
			 * (data.fuelAmount > 0) { fuel += data.fuelAmount * fuelSlot.slotItem.count;
			 * currentFuel = fuel; fuelSlot.slotItem = null; } } }
			 */
			fuelSlot.render(baseBounds.x, baseBounds.y);

			Renderer.renderText(new Vector2f(baseBounds.x + 100, baseBounds.y + 86), "=", 12, Color.white);

			Renderer.renderRectangle(baseBounds.x + 114, baseBounds.y + 80, 32, 32, new Color(1, 1, 1, 0.5f));
			if (itemCount > 0) {
				String recipeName = getRecipeName();
				RecipeData recipe = UIData.recipeData.get(recipeName);
				if (recipe != null) {
					ItemData item = WorldData.itemData.get(recipe.name);
					if (item != null) {
						GL11.glBegin(GL11.GL_TRIANGLES);
						Renderer.renderModel(baseBounds.x + 180, baseBounds.y + 60, "SQUARE", item.inventoryMaterial,
								new Color(1, 1, 1, 1f));
						GL11.glEnd();
					}
				}
			}

			craft.render();

			menu.render();
		}
	}

	@Override
	public void clean() {
		super.clean();

	}

	public String getRecipeName() {
		String comboRecipeName = "";

		for (SmeltingSlot slot : slots) {
			if (slot.slotItem != null) {

				if (comboRecipeName != "") {
					comboRecipeName += "+";
				}
				comboRecipeName += slot.slotItem.name;

			}
		}
		/*
		 * if (fuel > 0) { comboRecipeName += "+FUEL"; }
		 */

		if (fuelSlot.slotItem != null) {
			ItemData itemData = WorldData.itemData.get(fuelSlot.slotItem.name);
			if (itemData != null) {
				if (itemData.fuelAmount > 0) {
					comboRecipeName += "+FUEL";
				}
			}
		}

		RecipeData data = UIData.recipeData.get(comboRecipeName);
		if (data == null) {
			comboRecipeName = "";


			if (fuelSlot.slotItem != null) {
				ItemData itemData = WorldData.itemData.get(fuelSlot.slotItem.name);
				if (itemData != null) {
					if (itemData.fuelAmount > 0) {
						comboRecipeName += "FUEL";
					}
				}
			}
			for (int i = slots.size() - 1; i >= 0; i--) {
				SmeltingSlot slot = slots.get(i);
				if (slot.slotItem != null) {

					if (comboRecipeName != "") {
						comboRecipeName += "+";
					}
					comboRecipeName += slot.slotItem.name;

				}
			}
		}
		return comboRecipeName;
	}

	public SmeltingSlot getHoveredSlot() {
		SmeltingSlot hoveredSlot = null;
		for (SmeltingSlot slot : slots) {
			if (slot.isHovered()) {
				hoveredSlot = slot;
				break;
			}
		}
		if (fuelSlot.isHovered()) {
			hoveredSlot = fuelSlot;
		}

		return hoveredSlot;
	}

	public Point getHoveredPosition() {
		Point position = null;
		for (SmeltingSlot slot : slots) {
			if (slot.isHovered()) {
				position = slot.getPosition();
			}
		}
		if (fuelSlot.isHovered()) {
			position = fuelSlot.getPosition();
		}

		return position;
	}

	public InventoryItem getHoveredItem() {
		InventoryItem item = null;
		for (SmeltingSlot slot : slots) {
			if (slot.isHovered()) {
				item = slot.getItem();
			}
		}

		if (fuelSlot.isHovered()) {
			item = fuelSlot.getItem();
		}
		return item;
	}

	public void proceedSmelting() {

		String comboRecipeName = getRecipeName();
		if (comboRecipeName != "") {
			RecipeData data = UIData.recipeData.get(comboRecipeName);
			if (data != null) {
				Event move = new Event();
				move.step = 100;
				move.eventName = "SMELT_RECIPE";
				Event recipe = new Event();
				recipe.eventName = comboRecipeName;
				move.followUpEvent = recipe;
				EventManager.addEvent(move);
			}
		}
	}
}
