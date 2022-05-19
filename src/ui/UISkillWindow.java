package ui;

import java.awt.Point;
import java.awt.Rectangle;
import java.text.DecimalFormat;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import core.ChunkManager;
import core.GameDatabase;
import core.GroundItem;
import core.Input;
import core.Item;
import core.ItemData;
import core.Renderer;
import core.ResourceDatabase;
import core.TextureType;
import core.Window;
import game.PlayerDatabase;
import utils.Ticker;

public class UISkillWindow {
	private int id = -1;
	public static Vector2f position = new Vector2f(100, 100);
	public static boolean updateID = false;
	public static ItemSlot dragSlot;
	private static Point slotMargin = new Point(37, 37);

	public Ticker hoverTicker;

	public static boolean show = false;

	public void setup() {
		hoverTicker = new Ticker();
		int slotCount = Inventory.getSlotCount();
		int slotWidth = Inventory.getSlotWidth();
		position = new Vector2f(0, Window.height - (((size.y) * 32) + 32));
	}

	public static boolean isPanelHovered() {
		boolean isHovered = false;
		if (show) {
			int slotCount = Inventory.getSlotCount();
			int slotWidth = Inventory.getSlotWidth();
			Rectangle rec = new Rectangle((int) position.x, (int) position.y, (slotWidth + 3) * 32,
					((slotCount / slotWidth) + 2) * 32);
			if (rec.contains(Input.getMousePoint())) {
				isHovered = true;
			}
		}
		return isHovered;
	}

	Point size = new Point(7, 8);

	public void build() {
		slotMargin = new Point(50, 42);
		id = GL11.glGenLists(1);

		GL11.glNewList(id, GL11.GL_COMPILE_AND_EXECUTE);

		GL11.glBegin(GL11.GL_QUADS);
		UIPanel.renderPanel((int) position.x, (int) position.y, size.x, size.y);

		int x = 0;
		int y = 0;
		for (int index = 0; index < PlayerDatabase.skills.size(); index++) {
			Renderer.bindTexture(ResourceDatabase.uiTexture);
			Skill skill = PlayerDatabase.skills.get(index);

			SkillData skillData = GameDatabase.skillData.get(skill.skill);
			Point pos = new Point((int) position.x + 5 + (x * slotMargin.x), (int) position.y + 5 + (y * slotMargin.y));

			GL11.glBegin(GL11.GL_QUADS);
			Renderer.renderUITexture(UITextureType.ITEM_BACK, pos.x, pos.y, 32, 32);
			Renderer.renderUITexture(UITextureType.ITEM_BACK_SIDE, pos.x + 30, pos.y, 32, 32);
			if (skillData != null) {

				UITextureType type = skillData.type;
				if (type == null) {
					type = UITextureType.INSPECT_ICON;
				}
				Renderer.renderUITexture(type, pos.x, pos.y, 32, 32);

			}
			GL11.glEnd();

			int width = Math.round((float) 45 * ((float) skill.xp / (float) skill.nextXP));
			width = (width > 45 ? 45 : width);
			Renderer.renderQuad(new Rectangle(pos.x + 2, pos.y + 28, width, 2), new Color(0.3f, 1f, 0.2f, 0.75f));
			if (x >= size.x - 1) {
				y++;
				x = 0;
			} else {
				x++;
			}
		}
		GL11.glEnd();
		x = 0;
		y = 0;
		for (int index = 0; index < PlayerDatabase.skills.size(); index++) {
			Skill data = PlayerDatabase.skills.get(index);
			if (data != null) {

				int l = Renderer.getTextWidth(data.level + "", 12);

				Point pos = new Point((int) position.x + 50 - (l) + (x * slotMargin.x),
						(int) position.y + 18 + (y * slotMargin.y));

				Renderer.renderText(new Vector2f(pos.x, pos.y), data.level + "", 12, new Color(0.75f, 0.75f, 0.75f));

				if (x >= size.x - 1) {
					y++;
					x = 0;
				} else {
					x++;
				}
			}

		}

		GL11.glEndList();

		updateID = false;
	}

	int slotIndexHovered = -1;
	int previousSlotIndexHovered = -2;
	Point slotPointHovered = new Point(-1, -1);

	public void update() {
		if (Window.wasResized) {
			int slotCount = Inventory.getSlotCount();
			int slotWidth = Inventory.getSlotWidth();
			position = new Vector2f(0, Window.height - (((size.y) * 32) + 32));
			build();
		}
		if (show) {
			hoverTicker.poll(1000);

			previousSlotIndexHovered = slotIndexHovered;
			slotIndexHovered = -1;
			int offsetMouseX = (int) (Input.getMousePoint().x - position.x);
			int offsetMouseY = (int) (Input.getMousePoint().y - position.y);
			if (offsetMouseX >= 0 && offsetMouseY >= 0) {
				int skillX = offsetMouseX / slotMargin.x;
				int skillY = offsetMouseY / slotMargin.y;

				int slotWidth = Inventory.getSlotWidth();
				int index = skillX + (skillY * slotWidth);
				if (index < PlayerDatabase.skills.size() && index >= 0) {
					Skill skill = PlayerDatabase.skills.get(index);

					if (skill != null) {
						// System.out.println("Skill Hovered:" + skill.skill.toString());
						slotIndexHovered = index;
						slotPointHovered = new Point(skillX, skillY);
					}
				}

			}
		}
	}

	long startTicks = 0;

	public void render() {
		// show=true;
		if (show) {
			if (id == -1 || updateID) {
				build();
			} else {
				GL11.glCallList(id);
			}

			if (slotIndexHovered != -1) {
				Point tempPos = new Point((int) position.x + 6 + (slotPointHovered.x * slotMargin.x),
						(int) position.y + 6 + (slotPointHovered.y * slotMargin.y));

				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
				Renderer.renderQuad(new Rectangle(tempPos.x, tempPos.y, 48, 32), new Color(255, 255, 255));
				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
				if (startTicks == 0 && !Input.moved) {
					startTicks = hoverTicker.getTicks();
				} else if (Input.moved) {
					startTicks = 0;
				}

				if (startTicks + 1 < hoverTicker.getTicks() && !Input.moved) {
					Skill skillHovered = PlayerDatabase.skills.get(slotIndexHovered);
					if (skillHovered != null) {
						Renderer.bindTexture(ResourceDatabase.uiTexture);
						GL11.glBegin(GL11.GL_QUADS);
						UIPanel.renderPanel(tempPos.x + 32, tempPos.y, 7, 3);
						GL11.glEnd();

						String tempSkillName = skillHovered.skill.toString().toLowerCase();
						String tempFirstLetter = tempSkillName.substring(0, 1).toUpperCase();
						String tempLower = tempSkillName.substring(1, tempSkillName.length());

						Renderer.renderText(tempPos.x + 32 + 4, tempPos.y + 4, tempFirstLetter + tempLower, 20,
								Color.white);

						SkillData dat = GameDatabase.skillData.get(skillHovered.skill);
						if (dat != null) {
							String tempString = dat.description;
							if (tempString.length() > 35) {
								String ts = tempString.substring(35, tempString.length());

								tempString = tempString.substring(0, 35) + ts.substring(0, ts.indexOf(" "));

								String tempSecondString = dat.description.substring(35, dat.description.length())
										.replace(ts.substring(0, ts.indexOf(" ")), "");
								if (tempSecondString.length() > 35) {
									tempSecondString = tempSecondString.substring(0, 35) + "...";
								}

								Renderer.renderText(tempPos.x + 32 + 4, tempPos.y + 42, tempSecondString, 12,
										Color.white);
							}
							Renderer.renderText(tempPos.x + 32 + 4, tempPos.y + 30, "       " + tempString, 12,
									Color.white);

						}

						String expString = "XP: " + skillHovered.xp + " / " + skillHovered.nextXP;
						int l = Renderer.getTextWidth(expString, 12);

						Renderer.renderText(tempPos.x + 250 - l, tempPos.y + 78, expString, 12, Color.white);
					}
				}
			}
		}
	}

	public void destroy() {

	}
}
