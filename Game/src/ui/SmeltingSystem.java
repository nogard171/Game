package ui;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.LinkedList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import classes.CraftingTable;
import classes.Furnace;
import classes.ItemData;
import data.Settings;
import data.UIData;
import data.WorldData;
import utils.Renderer;
import utils.Window;

public class SmeltingSystem extends BaseSystem {

	public static LinkedList<QueuedRecipe> queuedRecipes = new LinkedList<QueuedRecipe>();
	public static LinkedList<SmeltingSlot> oreSlots = new LinkedList<SmeltingSlot>();
	public static LinkedList<SmeltingSlot> fuelSlots = new LinkedList<SmeltingSlot>();

	private Rectangle closeBounds;
	private boolean closeHovered = false;

	private Rectangle titleBarBounds;
	private boolean titleHovered = false;
	private Point dragOffset;
	private boolean dragging = false;

	public static int totalFuel = 0;
	public static int maxFuel = 100;

	private SmeltingMenu menu;
	Button smelt;
	public static Furnace furnace;
	public static Point selectedFurnace = null;

	@Override
	public void setup() {
		super.setup();
		menu = new SmeltingMenu();
		menu.setup();

		baseBounds = new Rectangle(0, 0, 304, 333);
		baseBounds.y = (Window.height - 32) - baseBounds.height;
		setupBounds();

		for (int x = 0; x < 2; x++) {
			for (int y = 0; y < 3; y++) {
				SmeltingSlot slot = new SmeltingSlot((x * 33) + 5, (y * 33) + 32);
				oreSlots.add(slot);
			}
		}

		for (int x = 0; x < 2; x++) {
			for (int y = 0; y < 3; y++) {
				SmeltingSlot slot = new SmeltingSlot((x * 33) + 100, (y * 33) + 32);
				fuelSlots.add(slot);
			}
		}
	}

	public void setupBounds() {
		titleBarBounds = new Rectangle(baseBounds.x, baseBounds.y, baseBounds.width - 21, 20);
		closeBounds = new Rectangle((baseBounds.x + baseBounds.width) - 20, baseBounds.y, 20, 20);

		smelt = new Button(new Rectangle(baseBounds.x + 60, baseBounds.y + 145, 50, 20), "Smelt", new AFunction() {
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
					UserInterface.smeltingDragging = true;
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
			if (furnace != null) {
				for (CraftingSlot slot : furnace.slots) {
					slot.update();
					if (slot.isHovered() && Window.isMainAction()) {
						InventoryItem newItem = slot.slotItem;
						if (newItem != null) {
							ItemData data = WorldData.itemData.get(newItem.name);
							if (data != null) {
								if (data.durability > 0) {
									newItem.durability = data.durability;
								}
								slot.slotItem = null;
								InventorySystem.addItem(newItem);
							}
						}
					}
				}
			}

			for (SmeltingSlot slot : oreSlots) {
				slot.update();
			}
			for (SmeltingSlot slot : fuelSlots) {
				slot.update();
			}
			smelt.update();
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

			for (SmeltingSlot slot : oreSlots) {
				slot.render(baseBounds.x, baseBounds.y);
			}

			for (SmeltingSlot slot : fuelSlots) {
				slot.render(baseBounds.x, baseBounds.y);
			}
			Renderer.renderRectangle(baseBounds.x + 80, baseBounds.y + 32, 10, 98, new Color(1, 1, 1, 0.5f));

			totalFuel = 50;
			if (totalFuel > 0) {
				int fuelHeight = (int) (((float) totalFuel / (float) maxFuel) * (float) 98);
				Renderer.renderRectangle(baseBounds.x + 80, (baseBounds.y + 130) - fuelHeight, 10, fuelHeight,
						new Color(1, 0.65f, 0, 0.75f));
			}

			if (furnace != null) {
				for (CraftingSlot slot : furnace.slots) {
					slot.render(baseBounds.x, baseBounds.y);
				}
			}

			smelt.render();
			menu.render();
		}
	}

	@Override
	public void clean() {
		super.clean();

	}

	public void proceedSmelting() {

		/*
		 * String comboRecipeName = getRecipeName(); if (comboRecipeName != "") {
		 * RecipeData data = UIData.recipeData.get(comboRecipeName); if (data != null) {
		 * Event move = new Event(); move.step = 100; move.eventName = "SMELT_RECIPE";
		 * Event recipe = new Event(); recipe.eventName = comboRecipeName;
		 * move.followUpEvent = recipe; EventManager.addEvent(move); } }
		 */
	}
}
