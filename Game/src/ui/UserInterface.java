package ui;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import classes.AFunction;
import classes.Chunk;
import classes.Object;
import data.WorldData;
import utils.KeySystem;
import utils.Renderer;
import utils.View;
import utils.Window;
import utils.WorldGenerator;

public class UserInterface {
	public Rectangle menuBounds;
	public ArrayList<MenuItem> menu = new ArrayList<MenuItem>();
	int mouseDownCount = 0;

	ObjectMenu objectMenu;

	EventManager eventManager;
	public static InventorySystem inventory;
	public static CharacterSystem character;
	public static SkillSystem skills;
	public static OptionSystem options;

	public static MouseIndex hover;
	public static boolean inventoryHovered = false;
	public static boolean menuHovered = false;
	public static boolean characterHovered = false;
	public static boolean craftingHovered = false;
	public static boolean optionsHovered = false;

	public void setup() {

		MenuItem bag = new MenuItem(new AFunction() {
			public void click() {
				inventory.showSystem = !inventory.showSystem;
				character.showSystem = false;
				skills.showSystem = false;
				options.showSystem = false;
			}
		});
		bag.bounds = new Rectangle(0, 0, 32, 32);
		bag.text = "Inventory";
		bag.material = "BAG";
		menu.add(bag);

		MenuItem chara = new MenuItem(new AFunction() {
			public void click() {
				character.showSystem = !character.showSystem;
				inventory.showSystem = false;
				skills.showSystem = false;
				options.showSystem = false;
			}
		});
		chara.bounds = new Rectangle(32, 0, 32, 32);
		chara.text = "Character";
		chara.material = "CHARACTER_ICON";
		menu.add(chara);

		MenuItem skill = new MenuItem(new AFunction() {
			public void click() {
				skills.showSystem = !skills.showSystem;
				inventory.showSystem = false;
				character.showSystem = false;
				options.showSystem = false;
			}
		});
		skill.bounds = new Rectangle(64, 0, 32, 32);
		skill.text = "Skills";
		skill.material = "SKILL_ICON";
		menu.add(skill);

		MenuItem optionsButton = new MenuItem(new AFunction() {
			public void click() {
				options.showSystem = !options.showSystem;
				inventory.showSystem = false;
				character.showSystem = false;
				skills.showSystem = false;
			}
		});
		optionsButton.bounds = new Rectangle(96, 0, 32, 32);
		optionsButton.text = "Options";
		optionsButton.material = "OPTIONS_ICON";
		menu.add(optionsButton);

		menuBounds = new Rectangle(0, Window.height - 32, menu.size() * 33, 32);

		inventory = new InventorySystem();
		inventory.setup();

		character = new CharacterSystem();
		character.setup();

		skills = new SkillSystem();
		skills.setup();

		options = new OptionSystem();
		options.setup();

		eventManager = new EventManager();
		eventManager.setup();
		objectMenu = new ObjectMenu();
		objectMenu.setup();
	}

	private void pollHover() {
		int cartX = Window.getMouseX() - View.x;
		int cartY = Window.getMouseY() - View.y;
		int isoX = cartX / 2 + (cartY);
		int isoY = cartY - cartX / 2;
		int indexX = (int) Math.floor((float) isoX / (float) 32);
		int indexY = (int) Math.floor((float) isoY / (float) 32);

		int chunkX = (int) Math.floor(indexX / 16);
		int chunkY = (int) Math.floor(indexY / 16);
		if (indexX < 0) {
			chunkX--;
		}
		if (indexY < 0) {
			chunkY--;
		}

		hover = new MouseIndex(indexX, indexY, chunkX, chunkY);
	}

	public void update() {
		if (!inventoryHovered && !menuHovered && !inventory.dragging && !characterHovered && !craftingHovered
				&& !optionsHovered) {
			pollHover();
			objectMenu.update();
			if (Mouse.isButtonDown(0) && hover != null) {
				if (!objectMenu.showObjectMenu && mouseDownCount == 0) {
					int hoverX = hover.getX();
					int hoverY = hover.getY();
					int chunkX = hoverX / 16;
					int chunkY = hoverY / 16;

					Chunk chunk = WorldData.chunks.get(chunkX + "," + chunkY);
					if (chunk != null) {
						boolean ifMove = true;
						int objX = hover.getX() % 16;
						int objY = hover.getY() % 16;
						if (objX >= 0 && objY >= 0) {

							Object ground = chunk.groundObjects[objX][objY];
							Object mask = chunk.maskObjects[objX][objY];
							if (ground != null) {
								if (mask != null) {
									if (mask.getMaterial() != "AIR") {
										ifMove = false;
										String action = objectToAction(mask);
										if (action == "CHOP") {
											Event move = new Event();
											move.eventName = "MOVE";
											move.end = new Point(hover.getX(), hover.getY());

											Event chop = new Event();
											chop.eventName = "CHOP";
											chop.end = new Point(hover.getX(), hover.getY());
											move.followUpEvent = chop;
											EventManager.addEvent(move);
										}
										if (action == "MINE") {
											Event move = new Event();
											move.eventName = "MOVE";
											move.end = new Point(hover.getX(), hover.getY());

											Event mine = new Event();
											mine.eventName = "MINE";
											mine.end = new Point(hover.getX(), hover.getY());
											move.followUpEvent = mine;
											EventManager.addEvent(move);
										}

										if (action == "HARVEST") {
											Event move = new Event();
											move.eventName = "MOVE";
											move.end = new Point(hover.getX(), hover.getY());

											Event harvest = new Event();
											harvest.eventName = "HARVEST";
											harvest.end = new Point(hover.getX(), hover.getY());
											move.followUpEvent = harvest;
											EventManager.addEvent(move);
										}
									}
								}
							}
						}

						if (ifMove) {
							Event move = new Event();
							move.eventName = "MOVE";
							move.end = new Point(hover.getX(), hover.getY());
							EventManager.addEvent(move);
						}
					}
					mouseDownCount++;
				}
			}
			if (!Mouse.isButtonDown(0) && mouseDownCount > 0) {
				mouseDownCount = 0;
			}
		}
		if (Window.wasResized()) {
			menuBounds = new Rectangle(0, Window.height - 32, menu.size() * 33, 32);
		}
		eventManager.update();
		inventory.update();
		character.update();
		skills.update();
		options.update();

		if (KeySystem.keyPressed(Keyboard.KEY_I)) {
			inventory.showSystem = !inventory.showSystem;

			character.showSystem = false;
			keyDownCount++;
		}

		if (KeySystem.keyPressed(Keyboard.KEY_C)) {
			character.showSystem = !character.showSystem;

			inventory.showSystem = false;
			keyDownCount++;
		}

		KeySystem.poll();
		if (KeySystem.keyPressed(Keyboard.KEY_A)) {

			// System.out.println("test:");
		}

		if (menuBounds.contains(new Point(Window.getMouseX(), Window.getMouseY()))) {
			menuHovered = true;
		} else {
			menuHovered = false;
		}
		if (menuHovered) {
			for (MenuItem item : menu) {
				if (item.bounds.contains(new Point(Window.getMouseX(), Window.getMouseY()))) {
					if (Mouse.isButtonDown(0)) {
						item.click();
					} else {
						item.unclick();
					}
				}
			}
		}
	}

	private int keyDownCount = 0;

	public void renderOnMap() {
		if (hover != null) {
			GL11.glColor4f(1f, 0, 0, 0.5f);
			GL11.glBegin(GL11.GL_QUADS);
			Renderer.renderGrid(hover.getX(), hover.getY());
			GL11.glEnd();
		}

		objectMenu.render();

	}

	public void render() {
		inventory.render();
		character.render();
		skills.render();
		options.render();

		Renderer.renderRectangle(menuBounds.x, menuBounds.y, menuBounds.width, menuBounds.height,
				new Color(0, 0, 0, 0.5f));
		int i = 0;
		GL11.glBegin(GL11.GL_TRIANGLES);
		for (MenuItem item : menu) {
			item.bounds.x = menuBounds.x + (i * 33);
			item.bounds.y = menuBounds.y;
			Renderer.renderModel(item.bounds.x, item.bounds.y, "SQUARE", item.material, new Color(1, 1, 1, 1f));
			i++;
		}
		GL11.glEnd();

		Renderer.renderRectangle(0, 0, 100, 30, new Color(0, 0, 0, 0.5f));
		if (hover != null) {
			Renderer.renderText(new Vector2f(0, 0), "Hover:" + hover.getX() + "," + hover.getY(), 12, Color.white);
			int hoverX = hover.getX();
			int hoverY = hover.getY();
			int chunkX = hoverX / 16;
			int chunkY = hoverY / 16;

			if (hoverX < 0) {
				chunkX -= 1;

			}
			if (hoverY < 0) {
				chunkY -= 1;
			}

			Renderer.renderText(new Vector2f(0, 12), "chunk:" + chunkX + "," + chunkY, 12, Color.white);
			Chunk chunk = WorldData.chunks.get(chunkX + "," + chunkY);
			if (chunk != null) {
				int objX = hover.getX() % 16;
				int objY = hover.getY() % 16;

				if (hoverX < 0) {
					objX *= -1;

				}
				if (hoverY < 0) {
					objY *= -1;
				}

				Object ground = chunk.groundObjects[objX][objY];
				Object mask = chunk.maskObjects[objX][objY];
				Object item = chunk.groundItems[objX][objY];
				if (ground != null) {
					String maskString = "/";
					if (mask != null) {
						if (item != null) {
							maskString += item.getMaterial();
						} else {
							maskString += mask.getMaterial();
						}
					}

					Renderer.renderText(new Vector2f(0, 24), "Object:" + ground.getMaterial() + maskString, 12,
							Color.white);
				}
			}

		}
		if (WorldGenerator.centerIndex != null) {
			Renderer.renderText(new Vector2f(0, 36),
					"Center Index:" + WorldGenerator.chunkIndex.getX() + "," + WorldGenerator.chunkIndex.getY(), 12,
					Color.white);
		}
		int size = 0;
		if (eventManager.events != null) {
			size = eventManager.events.size();
		}
		for (Event ev : eventManager.events) {
			// System.out.println("event: " + ev.eventName);
		}
		Renderer.renderText(new Vector2f(0, 48), "Event Count:" + size, 12, Color.white);
		Renderer.renderText(new Vector2f(0, 60), "Chunk Render Count:" + WorldData.chunks.size(), 12, Color.white);

	}

	public static MouseIndex getHover() {
		return hover;
	}

	public void clean() {

	}

	public static String objectToAction(Object obj) {
		String action = "MOVE";
		if (obj != null) {
			MenuItem menuItem;
			if (obj.getMaterial() == "TREE") {
				action = "CHOP";
			} else if (obj.getMaterial() == "WHEAT") {
				action = "HARVEST";
			} else if (obj.getMaterial() == "ORE") {
				action = "MINE";
			}
		}
		return action;
	}
}
