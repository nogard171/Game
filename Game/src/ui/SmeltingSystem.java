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
import classes.Furnace;
import classes.ItemData;
import classes.Object;
import data.Settings;
import data.UIData;
import data.WorldData;
import utils.Renderer;
import utils.Tools;
import utils.Window;

public class SmeltingSystem extends BaseSystem {

	private Rectangle closeBounds;
	private boolean closeHovered = false;

	private Rectangle titleBarBounds;
	private boolean titleHovered = false;
	private Point dragOffset;
	private boolean dragging = false;

	public static int totalFuel = 0;

	public static int totalSmelting = 0;
	public static int maxSmelting = 0;

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

	public static void addFuel() {
		if (furnace != null) {
			if (totalFuel <= 0) {
				for (SmeltingSlot slot : furnace.fuelSlots) {
					if (slot.slotItem != null) {
						ItemData data = UIData.itemData.get(slot.slotItem.name);
						if (data != null) {
							for (int c = 0; c < slot.slotItem.count; c++) {
								totalFuel += data.fuelAmount;
								if (slot.slotItem.count >= 1) {
									slot.slotItem.count--;
									break;
								}
							}
						}
					}
				}
			}
		}
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

			if (titleHovered && Window.isMainAction()&&!UserInterface.inventory.isDragging()) {
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

			if (closeHovered && Window.isMainAction()&&!UserInterface.inventory.isDragging()) {
				this.showSystem = false;
				UserInterface.smeltingHovered = false;
				UserInterface.smeltingDragging = false;
			}
			if (furnace != null&&!UserInterface.inventory.isDragging()) {
				for (SmeltingSlot slot : furnace.slots) {
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
			if (furnace != null) {
				for (SmeltingSlot slot : furnace.oreSlots) {
					slot.update();
				}
				for (SmeltingSlot slot : furnace.fuelSlots) {
					slot.update();
					if (slot.slotItem != null) {
						if (slot.slotItem.count <= 0) {
							slot.slotItem = null;
						}
					}
				}
			}
			if (!UserInterface.inventory.isDragging()) {
				smelt.update();
			}
		} else {
			UserInterface.smeltingHovered = false;
			UserInterface.smeltingDragging = false;
			dragging = false;
		}

		if (!Window.isMainAction()) {
			dragging = false;
			dragOffset = null;
			UserInterface.smeltingDragging = false;
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
			if (furnace != null) {
				for (SmeltingSlot slot : furnace.oreSlots) {
					slot.render(baseBounds.x, baseBounds.y);
				}

				for (SmeltingSlot slot : furnace.fuelSlots) {
					slot.render(baseBounds.x, baseBounds.y);
				}
			}
			Renderer.renderRectangle(baseBounds.x + 80, baseBounds.y + 32, 10, 98, new Color(1, 1, 1, 0.5f));

			if (maxSmelting > 0) {
				int maxHeight = (int) (((float) totalSmelting / (float) maxSmelting) * (float) 98);
				Renderer.renderRectangle(baseBounds.x + 80, baseBounds.y + 32 - maxHeight + 98, 10, maxHeight,
						new Color(0, 1, 0, 0.5f));

			}

			if (furnace != null) {
				for (SmeltingSlot slot : furnace.slots) {
					slot.render(baseBounds.x, baseBounds.y);
				}
			}
			// System.out.println("Fuel:" + totalFuel);

			smelt.render();
			menu.render();

			if (furnace != null) {
				if (furnace.queuedRecipes.size() > 0) {
					Renderer.renderRectangle(baseBounds.x + baseBounds.width + 2, baseBounds.y + 20, 34,
							baseBounds.height - 20, new Color(0, 0, 0, 0.5f));
					for (int i = 0; i < 10; i++) {
						if (furnace.queuedRecipes.size() > i) {
							QueuedRecipe queuedRecipe = furnace.queuedRecipes.get(i);
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

										if (maxSmelting > 0) {
											if (queuedRecipe.time > 0) {
												int maxHeight = (int) (((float) queuedRecipe.time / (float) 100)
														* (float) 32);
												Renderer.renderRectangle(baseBounds.x + baseBounds.width + 3,
														baseBounds.y + 21 + (i * 33) + 32, 3, maxHeight - 32,
														new Color(0, 1, 0, 0.5f));
											}
										}

										/*
										 * Renderer.renderText( new Vector2f(baseBounds.x + baseBounds.width + 23,
										 * baseBounds.y + 40 + (i * 33)), "" + queuedRecipe.time, 10, Color.white);
										 */

									}
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void clean() {
		super.clean();

	}

	public static boolean smelting = false;

	public void proceedSmelting() {
		maxSmelting = 0;
		resetSmelting();
		for (SmeltingSlot slot : furnace.oreSlots) {
			if (slot.slotItem != null) {
				ItemData data = UIData.itemData.get(slot.slotItem.name);
				if (data != null) {
					for (int c = slot.slotItem.count; c > 0; c--) {
						System.out.println("item: " + slot.slotItem.name + "/" + slot.slotItem.count);
						QueuedRecipe queuedRecipe = setupQueuedRecipe(slot.slotItem.name);
						Event move = new Event();
						move.step = 100;
						move.eventName = "SMELT_RECIPE";
						Event recipe = new Event();
						recipe.eventName = queuedRecipe.recipe;
						recipe.hash = queuedRecipe.hash;
						recipe.end = selectedFurnace;
						move.followUpEvent = recipe;
						EventManager.addEvent(move);
						maxSmelting += 100;
						smelting = true;
						slot.slotItem.count--;
					}
					if (slot.slotItem.count <= 0) {
						System.out.println("clear");
						slot.slotItem = null;
					}
				}
			}
		}
	}

	public QueuedRecipe setupQueuedRecipe(String item) {
		String hash = "";
		String recipeName = "";
		for (RecipeData recipe : UIData.recipeData.values()) {
			for (RecipeItem recipeItem : recipe.items) {
				ItemData data = UIData.itemData.get(recipeItem.itemName);
				if (data != null) {
					if (data.name.equals(item)) {
						recipeName = recipe.name;
						break;
					}
				}
			}
		}
		if (recipeName != "") {
			hash = SmeltingSystem.addQueuedRecipe(new QueuedRecipe(recipeName));
		}
		return new QueuedRecipe(hash, recipeName);
	}

	public static String addQueuedRecipe(QueuedRecipe recipe) {
		String hash = "";
		if (furnace != null) {
			hash = Tools.getRandomHexString(16);
			recipe.hash = hash;
			furnace.queuedRecipes.add(recipe);
		}
		return hash;
	}

	public static void removeQueuedRecipe(String hash) {
		if (furnace != null) {
			for (int i = 0; i < furnace.queuedRecipes.size(); i++) {
				if (furnace.queuedRecipes.get(i).hash.equals(hash)) {
					furnace.queuedRecipes.remove(i);
					break;
				}

			}
		}
	}

	public static void updateQueuedTime(String hash, int step) {
		if (furnace != null) {
			for (int i = 0; i < furnace.queuedRecipes.size(); i++) {
				if (furnace.queuedRecipes.get(i).hash.equals(hash)) {
					furnace.queuedRecipes.get(i).time = step;
					break;
				}
			}
		}
	}

	public static void resetSmelting() {
		totalSmelting = 0;
	}
	public static boolean addToFurnace(Point end, InventoryItem newItem) {
		boolean canAdd = false;
		if (furnace != null) {
			int chunkX = end.x / 16;
			int chunkY = end.y / 16;

			Chunk chunk = WorldData.chunks.get(chunkX + "," + chunkY);
			if (chunk != null) {
				int objX = end.x % 16;
				int objY = end.y % 16;

				Object obj = chunk.maskObjects[objX][objY];
				if (obj != null) {
					try {
						Furnace fur = (Furnace) obj;
						for (SmeltingSlot slot : fur.slots) {
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
}
