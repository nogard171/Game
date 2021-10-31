package ui;

import java.awt.Font;
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

import core.ChunkManager;
import core.GameDatabase;
import core.GroundItem;
import core.Input;
import core.ItemType;
import core.Renderer;
import core.Resource;
import core.ResourceDatabase;
import core.TextureType;
import core.Tile;
import core.Window;
import game.Base;
import game.PlayerDatabase;

public class UIManager {
	UIInventory uiInventory;
	UISkillWindow uiSkill;
	public static boolean uiHovered = false;

	LinkedList<UIButton> menu = new LinkedList<UIButton>();

	public void setup() {
		uiInventory = new UIInventory();
		uiInventory.setup();
		uiSkill = new UISkillWindow();
		uiSkill.setup();
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
				uiInventory.show = !uiInventory.show;
			}
		}));
		menu.add(new UIButton(UITextureType.SKILL_ICON, new Rectangle(32, 0, 32, 32), new UIAction() {
			@Override
			public void click(UIButton btn) {
				uiInventory.show = false;
				uiSkill.show = !uiSkill.show;
			}
		}));
	}

	public void update() {
		uiInventory.update();
		uiSkill.update();

		handleUIControls();
		boolean btnHovered = false;
		for (UIButton btn : menu) {
			btn.poll();
			btn.bounds.y = Window.height - 32;
			btnHovered = (!btnHovered ? btn.hovered : btnHovered);
		}

		uiHovered = (uiInventory.isPanelHovered() || uiSkill.isPanelHovered() || btnHovered ? true : false);

	}

	private void handleUIControls() {
		if (Input.isKeyPressed(Keyboard.KEY_I)) {
			uiSkill.show = false;
			uiInventory.show = !uiInventory.show;
		}

		if (Input.isKeyPressed(Keyboard.KEY_K)) {
			uiInventory.show = false;
			uiSkill.show = !uiSkill.show;
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
		
		renderHover();
	}
	
	public void renderHover()
	{

		if (Base.hoverIndex != null) {

			Tile tile = ChunkManager.getTile(Base.hoverIndex);
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
								System.out.println("hovered"+dat.resourceLevels);
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
