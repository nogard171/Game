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

	public static boolean uiHovered = false;

	LinkedList<UIButton> menu = new LinkedList<UIButton>();

	public void setup() {
		uiInventory = new UIInventory();
		uiInventory.setup();
		uiSkill = new UISkillWindow();
		uiSkill.setup();
		uiChat = new UIChat();
		uiChat.setup();

		Inventory.setup();
		Inventory.addItem(ItemType.COINS, 9);
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

		/*
		 * Inventory.addItem(ItemType.ROCK); Inventory.addItem(ItemType.ROCK);
		 * Inventory.addItem(ItemType.ROCK);
		 * 
		 * 
		 * 
		 * Inventory.printItems();
		 */
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
	}

	public static Point playerIndex;
	public static Point hoverIndex;

	private void pollHover() {
		int cartX = (Input.getMousePoint().x) + Base.view.x;
		int cartY = (Input.getMousePoint().y) + Base.view.y;
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

		pollHover();

		handleUIControls();
		boolean btnHovered = false;
		for (UIButton btn : menu) {
			btn.poll();
			btn.bounds.y = Window.height - 32;
			btnHovered = (!btnHovered ? btn.hovered : btnHovered);
		}

		uiHovered = (uiInventory.isPanelHovered() || uiSkill.isPanelHovered() || uiChat.isPanelHovered() || btnHovered
				? true
				: false);

		// fix path finding, if player is next to resource do not run path finding.
		if (Input.isMousePressed(0) && !UIManager.isHovered()) {
			boolean canDo = false;
			Tile tile = ChunkManager.getTile(hoverIndex);
			if (tile != null) {
				SkillName skillName = SkillManager.getSkillByType(tile.getBaseType());
				if (skillName != null) {					
					SkillData dat = GameDatabase.skillData.get(skillName);
					if (dat != null) {
						boolean isSearchable = ChunkManager.isSearchable(hoverIndex);
						if (dat.resourceLevels.containsKey(tile.getBaseType())) {
							Integer level = dat.resourceLevels.get(tile.getBaseType());

							if (level != null) {
								Skill skill = SkillManager.getSkillByName(skillName);
								if (skill != null) {
									if (skill.level > level) {
										canDo = true;
									} else {
										UIChat.addMessage("System:You need to be Level " + level + " in "
												+ skillName.toUserString() + " interact with that object.");
									}
								}
							}

						} else if (isSearchable) {
							canDo = true;
						} else if (tile.getBaseType().equals(TextureType.ITEM)) {
							canDo = true;
						}
					}
				}
			}
			if (canDo||tile==null) {

				playerIndex = TaskManager.getCurrentTaskEnd();
				if (playerIndex == null) {
					playerIndex = ChunkManager.getIndexByType(TextureType.CHARACTER);
				}
				Point center = new Point(Input.mousePoint.x, Input.mousePoint.y);// UIManager.playerIndex;
				int cartX = center.x;
				int cartY = center.y;
				int isoX = (cartX / 2 + (cartY));
				int isoY = (cartY - cartX / 2);

				int indexX = (int) Math.floor((float) isoX / (float) 32);
				int indexY = (int) Math.floor((float) isoY / (float) 32);

				if (hoverIndex.x > playerIndex.x - 100 && hoverIndex.x < playerIndex.x + 100
						&& hoverIndex.y > playerIndex.y - 100 && hoverIndex.y < playerIndex.y + 100) {
					boolean useHoe = false;
					if (UIInventory.dragSlot != null) {
						if (UIInventory.dragSlot.item != null) {
							if (UIInventory.dragSlot.item.getType().equals(ItemType.HOE)) {
								useHoe = true;
							}
						}
					}
					ANode hoeIndex = null;
					if (useHoe) {
						hoeIndex = new ANode(hoverIndex.x, hoverIndex.y);
						hoverIndex = ChunkManager.findIndexAroundIndex(playerIndex, hoverIndex);
					}
					ANode resourceIndex = new ANode(hoverIndex.x, hoverIndex.y);
					boolean isRes = ChunkManager.isResource(hoverIndex);
					boolean inRange = ChunkManager.resourceInRange(playerIndex, hoverIndex);
					Resource resource = null;
					if (isRes) {
						resource = ChunkManager.getResource(hoverIndex);
						hoverIndex = ChunkManager.findIndexAroundIndex(playerIndex, hoverIndex);
					}
					boolean isItem = ChunkManager.isItem(hoverIndex);
					boolean isSearchable = ChunkManager.isSearchable(hoverIndex);

					LinkedList<ANode> path = null;

					if (inRange) {
						path = new LinkedList<ANode>();
						path.add(new ANode(hoverIndex));
					} else {
						path = APathFinder.find(new ANode(playerIndex), new ANode(hoverIndex));
					}
					if (path != null) {
						if (path.size() > 0) {
							for (int i = 0; i < path.size() - 1; i++) {
								ANode node = path.get(i);
								if (node != null) {
									ChunkManager.setObjectAtIndex(node.toPoint(), TextureType.PATH_DURING);
								}
							}
							ANode node = path.get(path.size() - 1);
							ChunkManager.setObjectAtIndex(node.toPoint(), TextureType.PATH_FINISH);

							Task move = new Task(TaskType.WALK, path.getFirst(), path, 1000);

							if (useHoe) {
								Resource temp = new Resource(TextureType.AIR);
								temp.setHealth(5);

								Task till = new Task(TaskType.TILL, hoeIndex, temp.getANode(hoeIndex));

								hoverIndex = ChunkManager.findIndexAroundIndex(playerIndex, hoverIndex);

								move.addFollowUp(till);
							} else if (isRes) {

								if (resource != null) {

									Task chop = new Task(TaskType.RESOURCE, resourceIndex,
											resource.getANode(resourceIndex));

									hoverIndex = ChunkManager.findIndexAroundIndex(playerIndex, hoverIndex);

									move.addFollowUp(chop);

								}
							} else if (isItem) {
								Tile item = null;
								if (isItem) {
									item = ChunkManager.getTile(hoverIndex);
								}
								if (item != null) {

									Task chop = new Task(TaskType.ITEM, resourceIndex);
									hoverIndex = ChunkManager.findIndexAroundIndex(playerIndex, hoverIndex);
									move.addFollowUp(chop);

								}
							} else if (isSearchable) {
									Task search = new Task(TaskType.SEARCH, resourceIndex);
									hoverIndex = ChunkManager.findIndexAroundIndex(playerIndex, hoverIndex);
									move.addFollowUp(search);

							}
							TaskManager.addTask(move);

						}
					} else {
						// handle failed path
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

	public void render() {

		Renderer.bindTexture(ResourceDatabase.uiTexture);
		uiInventory.render();
		uiSkill.render();
		uiChat.render();

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

		Renderer.renderUITexture(UITextureType.CURSOR, Input.getMousePoint().x, Input.getMousePoint().y, 32, 32);
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
								new Rectangle(Input.getMousePoint().x, Input.getMousePoint().y + 32, (c * 8) + 10, 20),
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
														"(Lvl:" + level + ")", 12, new Color(255, 0, 0), fontType);
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
