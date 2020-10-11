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

	private Rectangle closeBounds;
	private boolean closeHovered = false;

	private Rectangle titleBarBounds;
	private boolean titleHovered = false;
	private Point dragOffset;
	private boolean dragging = false;

	private CraftingMenu menu;
	Button craft;

	@Override
	public void setup() {
		super.setup();
		menu = new CraftingMenu();
		menu.setup();

		baseBounds = new Rectangle(0, 0, 304, 333);
		baseBounds.y = (Window.height - 32) - baseBounds.height;
		setupBounds();

		for (RecipeData data : UIData.recipeData.values()) {
			// System.out.println("data: " + data.name);
		}

		CraftingSlot newSlot = new CraftingSlot(60, 60);
		slots.add(newSlot);
		newSlot = new CraftingSlot(120, 60);
		slots.add(newSlot);
	}

	public void setupBounds() {
		titleBarBounds = new Rectangle(baseBounds.x, baseBounds.y, baseBounds.width - 21, 20);
		closeBounds = new Rectangle((baseBounds.x + baseBounds.width) - 20, baseBounds.y, 20, 20);

		craft = new Button(new Rectangle(baseBounds.x + 110, baseBounds.y + 100, 50, 20), "Craft", new AFunction() {
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
				UserInterface.craftingHovered = false;
			}

			for (CraftingSlot slot : slots) {
				slot.update();
			}

			craft.update();

		} /*
			 * else { UserInterface.inventory.moveItemOut(false);
			 * UserInterface.craftingHovered = false; UserInterface.craftingDragging =
			 * false;
			 * 
			 * dragging = false; }
			 * 
			 * if (!Window.isMainAction()) { dragging = false; dragOffset = null;
			 * UserInterface.craftingDragging = false; }
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
			for (CraftingSlot slot : slots) {
				slot.render(baseBounds.x, baseBounds.y);
				itemCount++;
			}

			Renderer.renderText(new Vector2f(baseBounds.x + 102, baseBounds.y + 66), "+", 12, Color.white);

			Renderer.renderText(new Vector2f(baseBounds.x + 162, baseBounds.y + 66), "=", 12, Color.white);

			Renderer.renderRectangle(baseBounds.x + 180, baseBounds.y + 60, 32, 32, new Color(1, 1, 1, 0.5f));
			if (itemCount > 0) {
				String recipeName = getRecipeName();
				RecipeData recipe = UIData.recipeData.get(recipeName);
				if (recipe != null) {
					ItemData item = WorldData.itemData.get(recipe.name);
					if (item != null) {
						GL11.glBegin(GL11.GL_TRIANGLES);
						Renderer.renderModel(baseBounds.x + 180, baseBounds.y + 60, "SQUARE", item.inventoryMaterial,
								new Color(1, 1, 1, 0.5f));
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
			Event move = new Event();
			move.step = 100;
			move.eventName = "CRAFT_RECIPE";
			Event recipe = new Event();
			recipe.eventName = comboRecipeName;
			move.followUpEvent = recipe;
			EventManager.addEvent(move);
		}
	}
}
