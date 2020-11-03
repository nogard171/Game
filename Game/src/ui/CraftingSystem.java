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

public class CraftingSystem extends BaseSystem {

	public static LinkedList<QueuedRecipe> queuedRecipes = new LinkedList<QueuedRecipe>();

	private Rectangle closeBounds;
	private boolean closeHovered = false;

	public static Point selectedTable = null;

	private Rectangle titleBarBounds;
	private boolean titleHovered = false;
	private Point dragOffset;
	private boolean dragging = false;

	private CraftingMenu menu;
	Button craft;
	Button left;
	Button right;

	int craftCount = 1;

	public static int craftTime = 0;
	public static int currentCraftTime = 0;

	RecipesListView listView;

	public static CraftingTable table;

	@Override
	public void setup() {
		super.setup();
		menu = new CraftingMenu();
		menu.setup();

		baseBounds = new Rectangle(0, 0, 404, 343);
		baseBounds.y = (Window.height - 32) - baseBounds.height;
		setupBounds();

		listView = new RecipesListView(0, 16, 150, baseBounds.height - 49);
		listView.setup();
	}

	public void setupBounds() {
		titleBarBounds = new Rectangle(baseBounds.x, baseBounds.y, baseBounds.width - 21, 20);
		closeBounds = new Rectangle((baseBounds.x + baseBounds.width) - 20, baseBounds.y, 20, 20);

		craft = new Button(new Rectangle(baseBounds.x + 250, baseBounds.y + 280, 50, 20), "Craft", new AFunction() {
			public void click() {
				proceedCrafting();
			}
		});

		left = new Button(new Rectangle(baseBounds.x + 200, baseBounds.y + 252, 40, 20), "Less", new AFunction() {
			public void click() {
				if (craftCount - 1 >= 0) {
					craftCount--;
				}
			}
		});

		right = new Button(new Rectangle(baseBounds.x + 310, baseBounds.y + 252, 40, 20), "More", new AFunction() {
			public void click() {
				craftCount++;
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
			if (table != null) {
				for (CraftingSlot slot : table.slots) {
					slot.update();
					if (slot.isHovered() && Window.isMainAction()) {
						InventoryItem newItem = slot.slotItem;
						if (newItem != null) {
							ItemData data = UIData.itemData.get(newItem.name);
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

			craft.update();
			left.update();
			right.update();

		} else

		{
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
					ItemData itemData = UIData.itemData.get(recipe.name.toUpperCase());
					if (itemData != null) {

						GL11.glBegin(GL11.GL_TRIANGLES);
						Renderer.renderModel(baseBounds.x + 160, baseBounds.y + 48, "SQUARE",
								itemData.inventoryMaterial, new Color(1, 1, 1, 1f));
						GL11.glEnd();

						Renderer.renderText(new Vector2f(baseBounds.x + 200, baseBounds.y + 48), itemData.commonName,
								18, Color.white);
						if (recipe.requiredLevel > 0) {
							Renderer.renderText(new Vector2f(baseBounds.x + 200, baseBounds.y + 67),
									"Required Level: " + recipe.requiredLevel, 12, Color.white);
							craft.isDisabled(true);
						} else {
							craft.isDisabled(false);
						}
					}
					int x = 0;
					int y = 0;
					for (RecipeItem recipeItem : recipe.items) {
						ItemData recipeItemData = UIData.itemData.get(recipeItem.itemName.toUpperCase());
						if (itemData != null) {
							InventoryItem item = new InventoryItem();
							item.name = recipeItem.itemName;
							item.count = craftCount;
							Color c = new Color(1, 1, 1, 1f);
							if (!InventorySystem.hasItems(item)) {
								GL11.glBegin(GL11.GL_TRIANGLES);
								Renderer.renderModel(baseBounds.x + 161 + (x * 33), baseBounds.y + (y * 33) + 176,
										"SQUARE", "X_ICON", new Color(1, 1, 1, 0.5f));
								GL11.glEnd();
								c = new Color(1, 1, 1, 0.5f);
							}
							GL11.glBegin(GL11.GL_TRIANGLES);
							Renderer.renderModel(baseBounds.x + 161 + (x * 33), baseBounds.y + (y * 33) + 176, "SQUARE",
									recipeItemData.inventoryMaterial, c);
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
			Renderer.renderRectangle(baseBounds.x + 250, baseBounds.y + 250, 50, 24, new Color(1, 1, 1, 0.5f));

			Renderer.renderText(new Vector2f(baseBounds.x + 253, baseBounds.y + 248), "" + craftCount, 18, Color.white);

			left.render();
			right.render();

			craft.render();

			/*
			 * for (CraftingSlot slot : recipeSlots) { slot.render(baseBounds.x,
			 * baseBounds.y); }
			 */
			if (table != null) {
				for (CraftingSlot slot : table.slots) {
					slot.render(baseBounds.x, baseBounds.y);
					if (slot.slotItem != null) {
						ItemData itemData = UIData.itemData.get(slot.slotItem.name.toUpperCase());
						if (itemData != null) {

						}
					}
				}
			}

			if (queuedRecipes.size() > 0) {
				Renderer.renderRectangle(baseBounds.x + baseBounds.width + 2, baseBounds.y + 20, 34,
						baseBounds.height - 20, new Color(0, 0, 0, 0.5f));
				for (int i = 0; i < 10; i++) {
					if (queuedRecipes.size() > i) {
						QueuedRecipe queuedRecipe = queuedRecipes.get(i);
						if (queuedRecipe != null) {
							RecipeData recipe = UIData.recipeData.get(queuedRecipe.recipe);
							if (recipe != null) {
								ItemData itemData = UIData.itemData.get(recipe.name);
								if (itemData != null) {
									Renderer.renderRectangle(baseBounds.x + baseBounds.width + 3,
											baseBounds.y + 21 + (i * 33), 32, 32, new Color(1, 1, 1, 0.5f));
									GL11.glBegin(GL11.GL_TRIANGLES);
									Renderer.renderModel(baseBounds.x + baseBounds.width + 3,
											baseBounds.y + 21 + (i * 33), "SQUARE", itemData.inventoryMaterial,
											new Color(1, 1, 1, 1f));
									GL11.glEnd();

									Renderer.renderText(
											new Vector2f(baseBounds.x + baseBounds.width + 23,
													baseBounds.y + 40 + (i * 33)),
											"" + queuedRecipe.time, 10, Color.white);

								}
							}
						}
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
			//
			menu.render();
		}
	}

	@Override
	public void clean() {
		super.clean();

	}

	public void proceedCrafting() {
		if (listView.selectedRecipe != "") {
			boolean canCraft = false;
			RecipeData data = UIData.recipeData.get(listView.selectedRecipe);
			if (data != null) {
				for (int i = 0; i < craftCount; i++) {
					int itemCount = 0;
					for (RecipeItem recipeItem : data.items) {
						InventoryItem item = new InventoryItem();
						item.name = recipeItem.itemName;
						item.count = recipeItem.itemCount;

						boolean has = InventorySystem.hasItems(item);

						if (has) {
							itemCount++;
						}
					}
					if (itemCount == data.items.size()) {
						canCraft = true;
					}

					if (canCraft) {
						for (RecipeItem recipeItem : data.items) {
							InventoryItem item = new InventoryItem();
							item.name = recipeItem.itemName;
							item.count = recipeItem.itemCount;
							InventorySystem.removeItem(item);
						}
						String hash = Tools.getRandomHexString(16);

						queuedRecipes.add(new QueuedRecipe(hash, listView.selectedRecipe));
						Event move = new Event();
						move.step = data.craftTime;
						move.eventName = "CRAFT_RECIPE";

						Event recipe = new Event();
						recipe.eventName = listView.selectedRecipe;
						recipe.end = selectedTable;
						recipe.stepTime = data.craftTime;
						recipe.hash = hash;
						move.followUpEvent = recipe;
						EventManager.addEvent(move);
					}
				}
			}
		}
	}

	public static boolean addToTable(Point end, InventoryItem newItem) {
		boolean canAdd = false;
		if (table != null) {
			int chunkX = end.x / 16;
			int chunkY = end.y / 16;

			Chunk chunk = WorldData.chunks.get(chunkX + "," + chunkY);
			if (chunk != null) {
				int objX = end.x % 16;
				int objY = end.y % 16;

				Object obj = chunk.maskObjects[objX][objY];
				if (obj != null) {
					try {
						CraftingTable table = (CraftingTable) obj;
						for (CraftingSlot slot : table.slots) {
							if (slot.slotItem == null) {
								slot.slotItem = newItem;
								canAdd = true;
								break;
							}
						}
					} catch (ClassCastException e) {
						System.out.println("end: " + end);
						e.printStackTrace();
					}
				}
			}
		}
		return canAdd;
	}

	public static void removeQueuedRecipe(String hash) {
		for (int i = 0; i < queuedRecipes.size(); i++) {
			if (queuedRecipes.get(i).hash.equals(hash)) {
				queuedRecipes.remove(i);
				break;
			}
		}
	}

	public static void updateQueuedTime(String hash, int step) {
		for (int i = 0; i < queuedRecipes.size(); i++) {
			if (queuedRecipes.get(i).hash.equals(hash)) {
				queuedRecipes.get(i).time = step;
				break;
			}
		}
	}
}
