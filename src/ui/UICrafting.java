package ui;

import java.awt.Point;
import java.awt.Rectangle;
import java.text.DecimalFormat;
import java.util.LinkedList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import core.ChunkManager;
import core.GameDatabase;
import core.GroundItem;
import core.Input;
import core.Item;
import core.ItemData;
import core.ItemType;
import core.RecipeData;
import core.RecipeItemData;
import core.Renderer;
import core.ResourceDatabase;
import core.TextureType;
import core.Window;
import game.PlayerDatabase;
import utils.Ticker;

public class UICrafting {
	private int id = -1;
	public static Vector2f position = new Vector2f(0, 0);
	public static Point size = new Point(10, 10);
	private int showCount = 10;
	public static boolean updateID = false;

	public static boolean show = true;

	UIButton craftBtn;

	Ticker tickerUtil;

	public void setup() {
		tickerUtil = new Ticker();
		showCount = (size.y * 32) / 15;
		craftBtn = new UIButton(UITextureType.BLANK,
				new Rectangle((int) (position.x + 120), (int) (position.y + 176), 100, 20), new UIAction() {
					@Override
					public void click(UIButton btn) {
						if (recipe != null) {
							boolean has = Inventory.hasRecipeItems(recipe.recipeItems);
							if (has) {
								ItemType type = ItemType.valueOf(recipe.outputItem.toUpperCase());
								if (type != null) {
									Inventory.removeRecipeItems(recipe.recipeItems);
									for (int c = 0; c < recipe.outputItemCount; c++) {
										crafting = true;
										craftingTicks = recipe.tickCount;
										// Inventory.addItem(type);
									}
								}
							}
						}
					}
				});
		craftBtn.value = "Craft";
	}

	public static boolean isPanelHovered() {
		boolean isHovered = false;
		if (show) {
			Rectangle rec = new Rectangle((int) position.x, (int) position.y, size.x * 32, size.y * 32);
			if (rec.contains(Input.getMousePoint())) {
				isHovered = true;
			}
		}
		return isHovered;
	}

	public void build() {
		id = GL11.glGenLists(1);

		GL11.glNewList(id, GL11.GL_COMPILE_AND_EXECUTE);

		GL11.glBegin(GL11.GL_QUADS);
		UIPanel.renderPanel((int) position.x, (int) position.y, size.x, size.y);
		GL11.glEnd();

		GL11.glEndList();

		updateID = false;

		craftBtn.bounds = new Rectangle((int) (position.x + 120), (int) (position.y + 280), 60, 20);
	}

	int slotIndexHovered = -1;
	int previousSlotIndexHovered = -2;
	Point slotPointHovered = new Point(-1, -1);

	ItemSlot contextSlot;
	boolean crafting = false;
	int craftingTicks = 0;

	public void update() {
		if (Window.wasResized) {
			position = new Vector2f((Window.width / 2) - ((size.x * 32) / 2),
					(Window.height / 2) - ((size.y * 32) / 2));
			build();
		}
		if (show) {
			craftBtn.poll();

			if (crafting == true) {
				tickerUtil.poll(1000);
				if (tickerUtil.ticked()) {
					craftingTicks--;
				}
				if (craftingTicks <= 0) {
					ItemType type = ItemType.valueOf(recipe.outputItem.toUpperCase());
					if (type != null) {
						Inventory.addItem(type);
						crafting = false;
					}
				}
			}

			if (scrollBounds != null) {
				if (scrollBounds.contains(Input.mousePoint)) {
					int wheel = Input.getMouseWheel();
					if (wheel < 0 && shownIndex + showCount < PlayerDatabase.knownRecipes.size() - 1) {
						shownIndex++;
						System.out.println("Index:" + shownIndex);
					} else if (wheel > 0 && shownIndex > 0) {
						shownIndex--;
						System.out.println("Index:" + shownIndex);
					}

					indexHovered = (int) (Input.mousePoint.y - this.position.y) / 15;
					if (indexHovered > PlayerDatabase.knownRecipes.size() - 1) {
						indexHovered = -1;
					}
				} else {
					indexHovered = -1;
				}
			}
		} else {
			indexHovered = -1;
		}

		if (indexHovered > -1 && Input.isMousePressed(0)) {
			int i = (shownIndex + indexHovered);
			String name = PlayerDatabase.knownRecipes.get(i);
			if (name != "") {
				recipe = GameDatabase.recipeData.get(name.toLowerCase());
			}

		}
	}

	RecipeData recipe;

	int indexHovered = -1;
	long startTicks = 0;
	int shownIndex = 0;

	Rectangle scrollBounds;

	public void render() {
		// show=true;
		if (show) {

			Renderer.bindTexture(ResourceDatabase.uiTexture);
			if (id == -1 || updateID) {
				build();
			} else {
				GL11.glCallList(id);
			}

			if (indexHovered < showCount && indexHovered > -1) {
				Renderer.renderQuad(
						new Rectangle((int) position.x + 5, Math.round(position.y + (indexHovered * 15)) + 2, 95, 15),
						new Color(255, 255, 255, 128));

			}
			int y = 0;
			for (int i = shownIndex; i < shownIndex + showCount; i++) {
				if (PlayerDatabase.knownRecipes.size() > i && i >= 0) {
					String name = PlayerDatabase.knownRecipes.get(i);
					Renderer.renderText(position.x + 5, position.y + (y * 15), name, 12, Color.white);
				}
				y++;
			}

			float height = (size.y * 32) - 6;
			scrollBounds = new Rectangle((int) position.x, (int) position.y + 3, 110, (int) height);
			Renderer.renderQuad(new Rectangle((int) position.x + 100, (int) position.y + 3, 10, (int) height),
					new Color(128, 128, 128, 128));

			float steps = height / PlayerDatabase.knownRecipes.size();
			int tempHeight = Math.round((steps * showCount));
			if (tempHeight > height) {
				tempHeight = (int) height;
			}
			Renderer.renderQuad(new Rectangle((int) position.x + 100, Math.round(position.y + 3 + (shownIndex * steps)),
					10, tempHeight), new Color(128, 0, 0, 128));

			if (recipe != null) {
				Renderer.renderText(new Vector2f(position.x + 120, position.y + 20), recipe.name, 20, Color.white);

				Renderer.renderText(new Vector2f(position.x + 120, position.y + 120), recipe.description, 12,
						Color.white);

				Renderer.renderQuad(new Rectangle((int) (position.x + 120), (int) (position.y + 50), 64, 64),
						new Color(128, 128, 128, 255));

				for (int i = 0; i < 5; i++) {

					Renderer.renderQuad(
							new Rectangle((int) (position.x + 120 + (i * 37)), (int) (position.y + 180), 36, 36),
							new Color(128, 128, 128, 255));
					if (recipe.recipeItems.size() > i) {
						RecipeItemData item = recipe.recipeItems.get(i);
						if (item != null) {
							Renderer.renderText(
									new Vector2f((int) (position.x + 120 + (i * 37)), (int) (position.y + 176)),
									item.amount + "", 12, Color.white);
						}
					}
				}

				Renderer.bindTexture(ResourceDatabase.uiTexture);
				GL11.glBegin(GL11.GL_QUADS);
				Renderer.renderUITexture(recipe.outputType, (int) (position.x + 120 + 16), (int) (position.y + 50 + 16),
						32, 32);

				for (int i = 0; i < 5; i++) {
					if (recipe.recipeItems.size() > i) {
						RecipeItemData item = recipe.recipeItems.get(i);
						if (item != null) {
							Renderer.renderUITexture(item.type, (int) (position.x + 120 + (i * 37)),
									(int) (position.y + 180), 32, 32);
						}
					}

				}
				GL11.glEnd();
			}

			if (craftBtn != null) {
				Renderer.renderQuad(new Rectangle(craftBtn.bounds.x, craftBtn.bounds.y, craftBtn.bounds.width,
						craftBtn.bounds.height), new Color(128, 128, 128, 255));
				Renderer.renderText(new Vector2f(craftBtn.bounds.x + 8, craftBtn.bounds.y - 4), craftBtn.value, 20,
						Color.white);

				if (recipe != null) {
					Renderer.renderText(new Vector2f(craftBtn.bounds.x + 100, craftBtn.bounds.y - 4),
							craftingTicks + "", 20, Color.white);
				}
			}
			/*
			 * for (String s : PlayerDatabase.knownRecipes) {
			 * Renderer.renderText(position.x, position.y, s, 12, Color.white); }
			 */
		}

	}

	public void destroy() {

	}
}
