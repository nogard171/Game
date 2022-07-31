package ui;

import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.nio.IntBuffer;
import java.util.LinkedList;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import core.ANode;
import core.APathFinder;
import core.Chunk;
import core.ChunkManager;
import core.GameDatabase;
import core.GroundItem;
import core.Input;
import core.Item;
import core.ItemType;
import core.Renderer;
import core.Resource;
import core.ResourceDatabase;
import core.Task;
import core.TaskType;
import core.TextureType;
import core.Tile;
import core.Window;
import game.Base;
import game.PlayerDatabase;
import game.TaskManager;

public class UIManager {
	UIInventory uiInventory;
	UISkillWindow uiSkill;
	UIChat uiChat;
	UIEquipment uiEquipment;
	UICrafting uiCrafting;

	UIHud hud;

	public static boolean uiHovered = false;
	public static ItemSlot dragSlot;

	LinkedList<UIButton> menu = new LinkedList<UIButton>();

	public void setup() {
		uiInventory = new UIInventory();
		uiInventory.setup();
		uiSkill = new UISkillWindow();
		uiSkill.setup();
		uiChat = new UIChat();
		uiChat.setup();
		uiEquipment = new UIEquipment();
		uiEquipment.setup();

		uiCrafting = new UICrafting();
		uiCrafting.setup();

		hud = new UIHud();
		hud.setup();

		PlayerDatabase.knownRecipes.add("Pickaxe");

		Inventory.setup();
		Inventory.addItem(ItemType.ROCK);
		Inventory.addItem(ItemType.ROCK);
		Inventory.addItem(ItemType.ROCK);
		Inventory.addItem(ItemType.STICK);
		Inventory.addItem(ItemType.STICK);
		Inventory.addItem(ItemType.STICK);
		Inventory.addItem(ItemType.HOE, 1);

		Skill test = new Skill();
		test.skill = SkillName.WOODCUTTING;
		test.level = 1;

		PlayerDatabase.skills.add(test);

		test = new Skill();
		test.skill = SkillName.FISHING;
		test.level = 1;

		PlayerDatabase.skills.add(test);

		test = new Skill();
		test.skill = SkillName.MINING;
		test.level = 1;

		PlayerDatabase.skills.add(test);

		test = new Skill();
		test.skill = SkillName.AGILITY;
		test.level = 1;

		PlayerDatabase.skills.add(test);

		test = new Skill();
		test.skill = SkillName.FARMING;
		test.level = 1;

		PlayerDatabase.skills.add(test);

		menu.add(new UIButton(UITextureType.INVENTORY_ICON, new Rectangle(0, 0, 32, 32), new UIAction() {
			@Override
			public void click(UIButton btn) {
				uiSkill.show = false;
				uiChat.show = false;
				uiInventory.show = !uiInventory.show;
			}
		}));
		menu.add(new UIButton(UITextureType.SKILL_ICON, new Rectangle(32, 0, 32, 32), new UIAction() {
			@Override
			public void click(UIButton btn) {
				uiInventory.show = false;
				uiChat.show = false;
				uiSkill.show = !uiSkill.show;
			}
		}));
		menu.add(new UIButton(UITextureType.CHAT_ICON, new Rectangle(64, 0, 32, 32), new UIAction() {
			@Override
			public void click(UIButton btn) {
				uiInventory.show = false;
				uiSkill.show = false;
				uiChat.show = !uiChat.show;
			}
		}));
		menu.add(new UIButton(UITextureType.CHAT_ICON, new Rectangle(64, 0, 32, 32), new UIAction() {
			@Override
			public void click(UIButton btn) {
				uiInventory.show = false;
				uiSkill.show = false;
				uiChat.show = !uiChat.show;
			}
		}));
	}

	public static Point playerIndex = new Point(0, 0);
	public static Point hoverIndex;

	private void pollHover() {
		int cartX = (Input.getMousePoint().x) + (int) Base.view.x;
		int cartY = (Input.getMousePoint().y) + (int) Base.view.y;
		int isoX = (cartX / 2 + (cartY));
		int isoY = (cartY - cartX / 2);

		int indexX = (int) Math.floor((float) isoX / (float) 32);
		int indexY = (int) Math.floor((float) isoY / (float) 32);
		if (!UIManager.isHovered()) {
			hoverIndex = new Point(indexX, indexY);
		} else {
			hoverIndex = null;
		}
	}

	public void update() {
		uiInventory.update();
		uiSkill.update();
		uiChat.update();
		// uiEquipment.update();
		uiCrafting.update();
		hud.update();

		pollHover();

		handleUIControls();
		boolean btnHovered = false;
		for (UIButton btn : menu) {
			btn.poll();
			btn.bounds.y = Window.height - 32;
			btnHovered = (!btnHovered ? btn.hovered : btnHovered);
		}

		uiHovered = (UIInventory.isPanelHovered() || UISkillWindow.isPanelHovered() || UICrafting.isPanelHovered()
				|| UIChat.isPanelHovered() || btnHovered ? true : false);
		boolean cursorInRange = false;
		boolean defaultCursor = false;
		// change hover to hover the object, instead of the tile
		if (hoverIndex != null && playerIndex != null) {
			int range = 100;
			cursorInRange = (hoverIndex.x > playerIndex.x - range && hoverIndex.x < playerIndex.x + range
					&& hoverIndex.y > playerIndex.y - range && hoverIndex.y < playerIndex.y + range);

			if (cursorInRange) {
				playerIndex = TaskManager.getCurrentTaskEnd();
				if (playerIndex == null) {
					playerIndex = ChunkManager.getIndexByType(TextureType.CHARACTER);
				}
				boolean useHoe = false;
				if (dragSlot != null) {
					if (dragSlot.item != null) {
						if (dragSlot.item.getType().equals(ItemType.HOE)) {
							useHoe = true;
						}
					}
				}
				if (useHoe) {
					cursorType = UITextureType.CURSOR_TILLING;
				} else {
					Tile tile = ChunkManager.getTile(hoverIndex);
					if (tile != null) {
						SkillName skillName = SkillManager.getSkillByType(tile.getBaseType());
						if (skillName != null) {
							switch (skillName) {
							case MINING:
								cursorType = UITextureType.CURSOR_MINING;
								break;
							case WOODCUTTING:
								cursorType = UITextureType.CURSOR_WOODCUTTING;
								break;
							default:
								defaultCursor = true;
							}
						} else {
							defaultCursor = true;
						}
					} else {
						defaultCursor = true;
					}
				}
			} else if (cursorType != UITextureType.CURSOR_INVALID) {
				cursorType = UITextureType.CURSOR_INVALID;
			} else {
				defaultCursor = true;
			}
		} else {
			defaultCursor = true;
		}
		if (defaultCursor) {
			cursorType = UITextureType.CURSOR;

		}

		// fix path finding, if player is next to resource do not run path finding.
		// refactor code to work everytime.

		if (Input.isMousePressed(0) && !UIManager.isHovered()) {
			processAction();
		}
		/*
		 * if (Input.isMousePressed(0) && !UIManager.isHovered()&&true==false) { boolean
		 * canDo = false; Tile tile = ChunkManager.getTile(hoverIndex); if (tile !=
		 * null) { SkillName skillName =
		 * SkillManager.getSkillByType(tile.getBaseType()); if (skillName != null) {
		 * SkillData dat = GameDatabase.skillData.get(skillName); if (dat != null) {
		 * boolean isSearchable = ChunkManager.isSearchable(hoverIndex); if
		 * (dat.resourceLevels.containsKey(tile.getBaseType())) { Integer level =
		 * dat.resourceLevels.get(tile.getBaseType());
		 * 
		 * if (level != null) { Skill skill = SkillManager.getSkillByName(skillName); if
		 * (skill != null) { if (skill.level > level) { canDo = true; } else {
		 * UIChat.addMessage("System:You need to be Level " + level + " in " +
		 * skillName.toUserString() + " interact with that object."); } } }
		 * 
		 * } else if (isSearchable) { canDo = true; } else if
		 * (tile.getBaseType().equals(TextureType.ITEM)) { canDo = true; } } } } if
		 * (canDo || tile == null) { if (cursorInRange) { boolean useHoe = false; if
		 * (UIInventory.dragSlot != null) { if (UIInventory.dragSlot.item != null) { if
		 * (UIInventory.dragSlot.item.getType().equals(ItemType.HOE)) { useHoe = true; }
		 * } } ANode hoeIndex = null; if (useHoe) { hoeIndex = new ANode(hoverIndex.x,
		 * hoverIndex.y); hoverIndex = ChunkManager.findIndexAroundIndex(playerIndex,
		 * hoverIndex); } ANode resourceIndex = new ANode(hoverIndex.x, hoverIndex.y);
		 * boolean isRes = ChunkManager.isResource(hoverIndex); boolean inRange =
		 * ChunkManager.resourceInRange(playerIndex, hoverIndex); Resource resource =
		 * null; if (isRes) { resource = ChunkManager.getResource(hoverIndex);
		 * hoverIndex = ChunkManager.findIndexAroundIndex(playerIndex, hoverIndex); }
		 * boolean isItem = ChunkManager.isItem(hoverIndex);
		 * System.out.println("Size:"); boolean isSearchable =
		 * ChunkManager.isSearchable(hoverIndex); System.out.println("test:");
		 * 
		 * LinkedList<ANode> path = null;
		 * 
		 * if (inRange) { path = new LinkedList<ANode>(); path.add(new
		 * ANode(hoverIndex)); } else { path = APathFinder.find(new ANode(playerIndex),
		 * new ANode(hoverIndex)); } if (path != null) { if (path.size() > 0) { for (int
		 * i = 0; i < path.size() - 1; i++) { ANode node = path.get(i); if (node !=
		 * null) { ChunkManager.setObjectAtIndex(node.toPoint(),
		 * TextureType.PATH_DURING); } } ANode node = path.get(path.size() - 1);
		 * ChunkManager.setObjectAtIndex(node.toPoint(), TextureType.PATH_FINISH);
		 * 
		 * Task move = new Task(TaskType.WALK, path.getFirst(), path, 1000);
		 * 
		 * if (useHoe) { Resource temp = new Resource(TextureType.AIR);
		 * temp.setHealth(5);
		 * 
		 * Task till = new Task(TaskType.TILL, hoeIndex, temp.getANode(hoeIndex));
		 * 
		 * hoverIndex = ChunkManager.findIndexAroundIndex(playerIndex, hoverIndex);
		 * 
		 * move.addFollowUp(till); } else if (isRes) {
		 * 
		 * if (resource != null) {
		 * 
		 * Task chop = new Task(TaskType.RESOURCE, resourceIndex,
		 * resource.getANode(resourceIndex));
		 * 
		 * hoverIndex = ChunkManager.findIndexAroundIndex(playerIndex, hoverIndex);
		 * 
		 * move.addFollowUp(chop);
		 * 
		 * } } else if (isItem) { Tile item = null; if (isItem) { item =
		 * ChunkManager.getTile(hoverIndex); } if (item != null) {
		 * 
		 * Task chop = new Task(TaskType.ITEM, resourceIndex); hoverIndex =
		 * ChunkManager.findIndexAroundIndex(playerIndex, hoverIndex);
		 * move.addFollowUp(chop);
		 * 
		 * } } else if (isSearchable) { Task search = new Task(TaskType.SEARCH,
		 * resourceIndex); hoverIndex = ChunkManager.findIndexAroundIndex(playerIndex,
		 * hoverIndex); move.addFollowUp(search);
		 * 
		 * } TaskManager.addTask(move);
		 * 
		 * } } } } }
		 */

	}

	private void processAction() {
		Item useItem = null;
		if (dragSlot != null) {
			useItem = dragSlot.item;
		}

		LinkedList<Tile> tiles = ChunkManager.getTiles(hoverIndex);
		boolean actionable = false;
		if (tiles.size() >= 1) {
			Tile topTile = tiles.getFirst();
			SkillName skillName = SkillManager.getSkillByType(topTile.getBaseType());
			if (skillName != null) {
				SkillData dat = GameDatabase.skillData.get(skillName);
				if (dat != null) {
					if (dat.resourceLevels.containsKey(topTile.getBaseType())) {
						Integer level = dat.resourceLevels.get(topTile.getBaseType());
						if (level != null) {
							Skill skill = SkillManager.getSkillByName(skillName);
							if (skill != null) {
								boolean hasRequiredItem = false;
								if (dat.requiredItem != "" && dat.requiredItem != null) {
									hasRequiredItem = Inventory.hasItemType(dat.requiredItem);
								} else {
									hasRequiredItem = true;
								}

								if (hasRequiredItem) {
									if (skill.level >= level) {
										actionable = true;
									} else {
										UIChat.addMessage("System:You need to be Level " + level + " in "
												+ skillName.toUserString() + " to interact with that object.");
									}
								} else {
									UIChat.addMessage("System:You need to has Item " + dat.requiredItem
											+ " in your inventory to interact with that object.");
								}
							}
						}
					}
				}
			}

			if (topTile.getType().toString().toLowerCase().equals("item")) {
				actionable = true;
			}
		}
		if (actionable) {
			LinkedList<ANode> path = APathFinder.findTest(new ANode(playerIndex), new ANode(hoverIndex));
			if (path != null) {
				if (path.size() > 0) {
					if (path.size() > 2) {
						for (int i = 0; i < path.size() - 2; i++) {
							ANode node = path.get(i);
							if (node != null) {
								ChunkManager.setPathAtIndex(node.toPoint(), TextureType.PATH_DURING);
							}
						}
						ANode node = path.get(path.size() - 2);
						ChunkManager.setPathAtIndex(node.toPoint(), TextureType.PATH_FINISH);
					}
					if (tiles.size() == 1) {
						Task move = new Task(TaskType.WALK, path.getFirst(), path, 1000);
						if (useItem != null) {
							Tile topTile = tiles.removeFirst();
							ANode actionIndex = path.removeLast();
							SkillName skillName = SkillManager.getSkillByTypeAndItem(topTile.getBaseType(),
									useItem.texture);
							SkillData dat = GameDatabase.skillData.get(skillName);
							if (dat != null) {
								if (dat.resourceLevels.containsKey(topTile.getBaseType())) {
									Integer level = dat.resourceLevels.get(topTile.getBaseType());
									if (level != null) {
										Skill skill = SkillManager.getSkillByName(skillName);
										if (skill != null) {
											if (skill.level >= level) {
												if (skillName.equals(SkillName.FARMING)) {
													Task search = new Task(TaskType.TILL, actionIndex);
													move.addFollowUp(search);
												}
											}
										}
									}
								}
							}
						}
						TaskManager.addTask(move);
					} else {
						System.out.println("Walk: " + path.size());
						if (tiles.size() > 1) {
							Tile topTile = tiles.removeFirst();
							ANode actionIndex = path.removeLast();
							if (path.size() >= 1) {
								Task move = new Task(TaskType.WALK, path.getFirst(), path, 1000);
								if (topTile.getType().toString().toLowerCase().contains("tree")
										|| topTile.getType().toString().toLowerCase().contains("ore")) {
									Resource resource = ChunkManager.getResource(hoverIndex);
									if (resource != null) {
										Task action = new Task(TaskType.RESOURCE, actionIndex,
												resource.getANode(actionIndex));
										move.addFollowUp(action);
									} else {
										System.out.println("Invalid Resource");
									}
								}
								if (topTile.getType().toString().toLowerCase().contains("bush")) {
									Task search = new Task(TaskType.SEARCH, actionIndex);
									move.addFollowUp(search);
								}
								if (topTile.getType().toString().toLowerCase().contains("item")) {
									path.add(actionIndex);
									move = new Task(TaskType.WALK, path.getFirst(), path, 1000);
									Task search = new Task(TaskType.ITEM, actionIndex);
									move.addFollowUp(search);
								}
								if (topTile.getType().toString().toLowerCase().contains("plant")) {
									System.out.println("Harvest Plant");
								}
								TaskManager.addTask(move);
							}
						}
					}
				}
			}
		}
	}

	private void handleUIControls() {
		if (Input.isKeyPressed(Keyboard.KEY_I)) {
			uiSkill.show = false;
			uiInventory.show = !uiInventory.show;
			uiChat.show = false;
		}

		if (Input.isKeyPressed(Keyboard.KEY_K)) {
			uiInventory.show = false;
			uiSkill.show = !uiSkill.show;
			uiChat.show = false;
		}

		if (Input.isKeyPressed(Keyboard.KEY_C)) {
			uiInventory.show = false;
			uiSkill.show = false;
			uiChat.show = !uiChat.show;
			System.out.println("Chat:" + uiChat.show);
		}

		if (Input.isKeyPressed(Keyboard.KEY_1)) {
			SkillManager.addExperienceByResource(TextureType.TREE, 3000000);
			long xp = SkillManager.getSkillExperince(SkillName.WOODCUTTING);
			System.out.println("XP: " + String.valueOf(xp));
		}

	}

	UITextureType cursorType = UITextureType.CURSOR;

	public void render() {

		Renderer.bindTexture(ResourceDatabase.uiTexture);
		uiInventory.render();
		uiSkill.render();
		uiChat.render();
		// uiEquipment.render();
		uiCrafting.render();
		hud.render();

		Renderer.bindTexture(ResourceDatabase.uiTexture);
		GL11.glBegin(GL11.GL_QUADS);

		Renderer.renderUITexture(UITextureType.PANEL_TL, 0, Window.height - 32, 32, 32);
		int w = 5;
		for (int c = 0; c < w; c++) {
			Renderer.renderUITexture(UITextureType.PANEL_TC, 32 + (c * 32), Window.height - 32, 32, 32);
		}
		Renderer.renderUITexture(UITextureType.PANEL_TR, 32 + ((w) * 32), Window.height - 32, 32, 32);

		for (UIButton btn : menu) {
			Renderer.renderUITexture(btn.type, btn.bounds.x, btn.bounds.y, btn.bounds.width, btn.bounds.height);
		}

		Renderer.renderUITexture(cursorType, Input.getMousePoint().x, Input.getMousePoint().y, 32, 32);
		GL11.glEnd();
		if (!isHovered()) {
			renderHover();
		}
	}

	public void renderHover() {

		if (hoverIndex != null) {

			Tile tile = ChunkManager.getTile(hoverIndex);
			if (tile != null) {
				if (tile instanceof Resource || tile instanceof GroundItem || tile instanceof core.Object) {

					String text = tile.toHoverString();
					int fontType = Font.PLAIN;

					if (text.contains("!")) {
						fontType = Font.BOLD;
						text = text.replace("!", "");
					}
					int r = 255;
					int g = 255;
					int b = 255;
					int tempC = 0;
					if (text.contains("%")) {
						int c = text.replaceAll("(%[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}%)", "").length();

						Renderer.renderQuad(
								new Rectangle(Input.getMousePoint().x, Input.getMousePoint().y + 32, (c * 10) + 10, 20),
								new Color(0, 0, 0, 0.5f));

						String[] data = text.split("%");
						tempC = 0;
						for (int i = 0; i < data.length; i++) {
							if (data[i].contains(",")) {
								String[] rgb = data[i].split(",");
								r = Integer.parseInt(rgb[0]);
								g = Integer.parseInt(rgb[1]);
								b = Integer.parseInt(rgb[2]);

							} else {
								int textWidth = Renderer.renderTextWithWidth(
										new Vector2f(Input.getMousePoint().x + 5 + tempC,
												Input.getMousePoint().y + 1 + 32),
										data[i], 12, new Color(r, g, b), Font.BOLD);
								tempC += textWidth;
							}
						}
						SkillName skillName = SkillManager.getSkillByType(tile.getBaseType());
						if (skillName != null) {

							SkillData dat = GameDatabase.skillData.get(skillName);
							if (dat != null) {
								if (dat.resourceLevels.containsKey(tile.getBaseType())) {
									Integer level = dat.resourceLevels.get(tile.getBaseType());
									if (level != null) {
										Skill skill = SkillManager.getSkillByName(skillName);
										if (skill != null) {
											if (skill.level < level) {
												Renderer.renderText(
														new Vector2f(Input.getMousePoint().x + tempC + 10,
																Input.getMousePoint().y + 1 + 32),
														"(Lvl:" + level + ")", 12, new Color(204, 71, 78), fontType);
											}
										}
									}
								}
							}
						}
					} else {
						Renderer.renderQuad(new Rectangle(Input.getMousePoint().x, Input.getMousePoint().y + 32,
								text.length() * 8, 20), new Color(0, 0, 0, 0.5f));
						Renderer.renderText(new Vector2f(Input.getMousePoint().x + 5, Input.getMousePoint().y + 1 + 32),
								text, 12, new Color(r, g, b), fontType);

					}
				}
			}
		}
	}

	public void destroy() {
	}

	public static boolean isHovered() {
		return uiHovered;
	}
}
