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

public class CraftingSystem extends BaseSystem {

	public static LinkedList<CraftingSlot> slots = new LinkedList<CraftingSlot>();

	public static LinkedList<CraftingSlot> recipeSlots = new LinkedList<CraftingSlot>();

	private Rectangle closeBounds;
	private boolean closeHovered = false;

	private Rectangle titleBarBounds;
	private boolean titleHovered = false;
	private Point dragOffset;
	private boolean dragging = false;

	private CraftingMenu menu;
	Button craft;

	public static int craftTime = 0;
	public static int currentCraftTime = 0;

	RecipesListView listView;

	@Override
	public void setup() {
		super.setup();
		menu = new CraftingMenu();
		menu.setup();

		baseBounds = new Rectangle(0, 0, 404, 343);
		baseBounds.y = (Window.height - 32) - baseBounds.height;
		setupBounds();

		for (RecipeData data : UIData.recipeData.values()) {
			// System.out.println("data: " + data.name);
		}
		for (int i = 0; i < 7; i++) {
			CraftingSlot newSlot = new CraftingSlot(160 + (i * 33), 302);
			slots.add(newSlot);
		}

		for (int x = 0; x < 7; x++) {
			for (int y = 0; y < 2; y++) {
				recipeSlots.add(new CraftingSlot(161 + (x * 33), (y * 33) + 176));
			}
		}

		listView = new RecipesListView(0, 16, 150, baseBounds.height - 49);
		listView.setup();
	}

	public void setupBounds() {
		titleBarBounds = new Rectangle(baseBounds.x, baseBounds.y, baseBounds.width - 21, 20);
		closeBounds = new Rectangle((baseBounds.x + baseBounds.width) - 20, baseBounds.y, 20, 20);

		craft = new Button(new Rectangle(baseBounds.x + 110, baseBounds.y + 105, 50, 20), "Craft", new AFunction() {
			public void click() {
				proceedCrafting();
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
			UserInterface.craftingHovered = baseHovered;
			titleHovered = titleBarBounds.contains(new Point(Window.getMouseX(), Window.getMouseY()));
			listView.update();

			closeHovered = closeBounds.contains(new Point(Window.getMouseX(), Window.getMouseY()));
			if (closeHovered && Window.isMainAction()) {
				this.showSystem = false;
				UserInterface.craftingHovered = false;
			}
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

			for (CraftingSlot slot : slots) {
				slot.update();
			}

			craft.update();

		} else {
			// UserInterface.inventory.moveItemOut(false);
			UserInterface.craftingHovered = false;
			UserInterface.craftingDragging = false;

			dragging = false;
		}

		if (!Window.isMainAction()) {
			dragging = false;
			dragOffset = null;
			UserInterface.craftingDragging = false;
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

			Renderer.renderRectangle(baseBounds.x + 160, baseBounds.y + 48, 32, 32, new Color(1, 1, 1, 0.5f));

			Renderer.renderRectangle(baseBounds.x + 160, baseBounds.y + 175, 235, 68, new Color(1, 1, 1, 0.5f));

			if (listView.selectedRecipe != "") {
				RecipeData recipe = UIData.recipeData.get(listView.selectedRecipe);
				if (recipe != null) {
					ItemData itemData = WorldData.itemData.get(recipe.name.toUpperCase());
					if (itemData != null) {

						GL11.glBegin(GL11.GL_TRIANGLES);
						Renderer.renderModel(baseBounds.x + 160, baseBounds.y + 48, "SQUARE",
								itemData.inventoryMaterial, new Color(1, 1, 1, 1f));
						GL11.glEnd();

						Renderer.renderText(new Vector2f(baseBounds.x + 200, baseBounds.y + 48), itemData.commonName,
								18, Color.white);

					}
					int x = 0;
					int y = 0;
					for (RecipeItem recipeItem : recipe.items) {
						ItemData recipeItemData = WorldData.itemData.get(recipeItem.itemName.toUpperCase());
						if (itemData != null) {

							GL11.glBegin(GL11.GL_TRIANGLES);
							Renderer.renderModel(baseBounds.x + 161 + (x * 33), baseBounds.y + (y * 33) + 176, "SQUARE",
									recipeItemData.inventoryMaterial, new Color(1, 1, 1, 1f));
							GL11.glEnd();

							if (recipeItem.itemCount > 1) {
								Renderer.renderText(
										new Vector2f(baseBounds.x + 161 + (x * 33) + 28,
												baseBounds.y + 176 + 16 + (y * 33)),
										recipeItem.itemCount + "", 12, Color.white);
							}
						}
						if (x > 6) {
							x = 0;
							y++;
						} else {
							x++;

						}
					}
				}
			}
			/*
			for (CraftingSlot slot : recipeSlots) {
				slot.render(baseBounds.x, baseBounds.y);
			}*/
			for (CraftingSlot slot : slots) {
				slot.render(baseBounds.x, baseBounds.y);
				if (slot.slotItem != null) {
					ItemData itemData = WorldData.itemData.get(slot.slotItem.name.toUpperCase());
					if (itemData != null) {

					}
				}
			}

			/*
			 * 
			 * 
			 * int itemCount = 0; for (CraftingSlot slot : slots) {
			 * slot.render(baseBounds.x, baseBounds.y); itemCount++; }
			 * 
			 * Renderer.renderText(new Vector2f(baseBounds.x + 102, baseBounds.y + 66), "+",
			 * 12, Color.white);
			 * 
			 * Renderer.renderText(new Vector2f(baseBounds.x + 162, baseBounds.y + 66), "=",
			 * 12, Color.white);
			 * 
			 * Renderer.renderRectangle(baseBounds.x + 60, baseBounds.y + 96, 152, 4, new
			 * Color(1, 1, 1, 0.5f));
			 * 
			 * if (craftTime > 0) { int width = (int) (((float) currentCraftTime / (float)
			 * craftTime) * (float) 152); Renderer.renderRectangle(baseBounds.x + 60,
			 * baseBounds.y + 96, width, 4, new Color(0, 1, 0, 0.5f)); }
			 * 
			 * // Renderer.renderRectangle(baseBounds.x + 180, baseBounds.y + 60, 32, 32,
			 * new // Color(1, 1, 1, 0.5f)); finalSlot.render(baseBounds.x, baseBounds.y);
			 * if (itemCount > 0) { String recipeName = getRecipeName(); RecipeData recipe =
			 * UIData.recipeData.get(recipeName); if (recipe != null) { ItemData item =
			 * WorldData.itemData.get(recipe.name); if (item != null) {
			 * GL11.glBegin(GL11.GL_TRIANGLES); Renderer.renderModel(baseBounds.x + 180,
			 * baseBounds.y + 60, "SQUARE", item.inventoryMaterial, new Color(1, 1, 1,
			 * 0.5f)); GL11.glEnd(); } } }
			 * 
			 * 
			 * 
			 * 
			 */
			// craft.render();
			menu.render();
		}
	}

	@Override
	public void clean() {
		super.clean();

	}

	public String getRecipeName() {
		String comboRecipeName = "";
		for (CraftingSlot slot : slots) {
			if (slot.slotItem != null) {

				if (comboRecipeName != "") {
					comboRecipeName += "+";
				}
				comboRecipeName += slot.slotItem.name;

			}
		}

		RecipeData data = UIData.recipeData.get(comboRecipeName);
		if (data == null) {
			comboRecipeName = "";
			for (int i = slots.size() - 1; i >= 0; i--) {
				CraftingSlot slot = slots.get(i);
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

	public CraftingSlot getHoveredSlot() {
		CraftingSlot hoveredSlot = null;
		for (CraftingSlot slot : slots) {
			if (slot.isHovered()) {
				hoveredSlot = slot;
				break;
			}
		}

		return hoveredSlot;
	}

	public Point getHoveredPosition() {
		Point position = null;
		for (CraftingSlot slot : slots) {
			if (slot.isHovered()) {
				position = slot.getPosition();
			}
		}

		return position;
	}

	public InventoryItem getHoveredItem() {
		InventoryItem item = null;
		for (CraftingSlot slot : slots) {
			if (slot.isHovered()) {
				item = slot.getItem();
			}
		}

		return item;
	}

	public void proceedCrafting() {
		String comboRecipeName = getRecipeName();
		if (comboRecipeName != "") {

			RecipeData data = UIData.recipeData.get(comboRecipeName);

			if (data != null) {
				craftTime = data.craftTime;
				currentCraftTime = data.craftTime;
				Event move = new Event();
				move.step = data.craftTime;
				move.eventName = "CRAFT_RECIPE";
				Event recipe = new Event();
				recipe.eventName = comboRecipeName;
				move.followUpEvent = recipe;
				EventManager.addEvent(move);
			}
		}
	}
}
