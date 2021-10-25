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
		slotMargin = new Point(36, 42);
		id = GL11.glGenLists(1);

		GL11.glNewList(id, GL11.GL_COMPILE_AND_EXECUTE);

		GL11.glBegin(GL11.GL_QUADS);
		UIPanel.renderPanel((int) position.x, (int) position.y, size.x, size.y);

		int x = 0;
		int y = 0;
		for (int index = 0; index < PlayerDatabase.skills.size(); index++) {
			Skill skill = PlayerDatabase.skills.get(index);

			SkillData skillData = GameDatabase.skillData.get(skill.skill.toLowerCase().replace(" ", ""));
			Point pos = new Point((int) position.x + 5 + (x * slotMargin.x), (int) position.y + 5 + (y * slotMargin.y));

			Renderer.renderUITexture(UITextureType.ITEM_BACK, pos.x, pos.y, 32, 32);
			System.out.println("test: " + GameDatabase.skillData.size());
			if (skillData != null) {

				UITextureType type = skillData.type;
				if (type == null) {
					type = UITextureType.INSPECT_ICON;
				}

				Renderer.renderUITexture(type, pos.x, pos.y, 32, 32);
			}
			if (x >= size.x - 1) {
				y++;
				x = 0;
			} else {
				x++;
			}
			/*
			 * 
			 * ItemSlot slot = PlayerDatabase.itemSlots.get(index); if (slot != null) {
			 * 
			 * Item item = slot.item;
			 * 
			 * Renderer.renderUITexture(UITextureType.ITEM_BACK, pos.x, pos.y, 32, 32); if
			 * (item != null) {
			 * 
			 * Renderer.renderUITexture(item.getTexture(), pos.x, pos.y, 32, 32); } if (x >=
			 * size.x - 1) { y++; x = 0; } else { x++; }
			 * 
			 * }
			 */
		}
		GL11.glEnd();
		x = 0;
		y = 0;
		for (int index = 0; index < PlayerDatabase.skills.size(); index++) {
			Skill data = PlayerDatabase.skills.get(index);
			if (data != null) {
				Point pos = new Point((int) position.x + 30 + (x * slotMargin.x),
						(int) position.y + 20 + (y * slotMargin.y));
				Renderer.renderText(new Vector2f(pos.x, pos.y), data.level + "", 12, Color.white);

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
				int itemX = offsetMouseX / slotMargin.x;
				int itemY = offsetMouseY / slotMargin.y;

				int slotWidth = Inventory.getSlotWidth();
				int index = itemX + (itemY * slotWidth);

				if (index < PlayerDatabase.itemSlots.size() && index >= 0) {
					ItemSlot slot = PlayerDatabase.itemSlots.get(index);
					if (slot != null) {
						if (itemX < slotWidth) {
							slotIndexHovered = index;
							slotPointHovered = new Point(itemX, itemY);
							if (slot.item != null) {
								// System.out.println("Slot Count: " + slot.count);
								// System.out.println("Offset: " + index + "=" + slot + "->" +
								// slot.item.getType());
							}

						}
					} else {
						// slotIndexHovered = -1;
					}
				}
			}

		}
	}

	long startTicks = 0;

	public void render() {
		// show=true;
		if (show) {
			if (id == -1 || updateID || true == true) {
				build();
			} else {
				GL11.glCallList(id);
			}
			/*
			 * if (slotIndexHovered != -1) { Point tempPos = new Point((int) position.x + 6
			 * + (slotPointHovered.x * slotMargin.x), (int) position.y + 6 +
			 * (slotPointHovered.y * slotMargin.y));
			 * 
			 * GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
			 * Renderer.renderQuad(new Rectangle(tempPos.x, tempPos.y, 32, 32), new
			 * Color(255, 255, 255)); GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK,
			 * GL11.GL_FILL); if (startTicks == 0 && !Input.moved) { startTicks =
			 * hoverTicker.getTicks(); } else if (Input.moved) { startTicks = 0; }
			 * 
			 * if (startTicks + 1 < hoverTicker.getTicks() && !Input.moved) { ItemSlot
			 * tempSlot = PlayerDatabase.itemSlots.get(slotIndexHovered); if (tempSlot !=
			 * null) { if (tempSlot.item != null) { ItemData data =
			 * GameDatabase.getItemData(tempSlot.item.getType()); if (data != null) { //
			 * Renderer.renderQuad(new Rectangle(tempPos.x + 32, tempPos.y, 200, 50), new //
			 * Color(0, 0, 0, 224)); String tempString = data.description; if
			 * (tempString.length() > 35) { tempString = tempString.substring(0, 35) +
			 * "..."; } Renderer.bindTexture(ResourceDatabase.uiTexture);
			 * GL11.glBegin(GL11.GL_QUADS); UIPanel.renderPanel(tempPos.x + 32, tempPos.y,
			 * 7, 2); GL11.glEnd(); String tempName = data.type.name(); tempName =
			 * tempName.substring(0, 1).toUpperCase() + tempName.substring(1,
			 * tempName.length()).toLowerCase();
			 * 
			 * Renderer.renderText(tempPos.x + 32 + 4, tempPos.y + 4, tempName, 12,
			 * Color.white); Renderer.renderText(tempPos.x + 32 + 4, tempPos.y + 18,
			 * tempString, 12, Color.white); Renderer.renderText(tempPos.x + 32 + 4,
			 * tempPos.y + 44, "Value: " + (data.value * tempSlot.count), 12, Color.white);
			 * } } } } } else { startTicks = 0; }
			 * 
			 * if (dragSlot != null) { if (dragSlot.item != null) {
			 * Renderer.bindTexture(ResourceDatabase.uiTexture);
			 * GL11.glBegin(GL11.GL_QUADS);
			 * Renderer.renderUITexture(dragSlot.item.getTexture(), Input.getMousePoint().x,
			 * Input.getMousePoint().y, 32, 32); GL11.glEnd(); } }
			 * 
			 */ }
	}

	public void destroy() {

	}
}
