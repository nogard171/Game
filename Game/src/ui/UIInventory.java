package ui;

import java.awt.Point;
import java.awt.Rectangle;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import core.Input;
import core.Item;
import core.Renderer;
import core.ResourceDatabase;
import core.TextureType;

public class UIInventory {
	private int id = -1;
	public static Vector2f position = new Vector2f(100, 100);
	public static boolean updateID = false;

	public void setup() {

	}

	public static boolean isPanelHovered() {
		boolean isHovered = false;
		int slotCount = Inventory.getSlotCount();
		int slotWidth = Inventory.getSlotWidth();
		Rectangle rec = new Rectangle((int) position.x, (int) position.y, (slotWidth + 1) * 32,
				(slotCount / slotWidth) * 32);
		if (rec.contains(Input.getMousePoint())) {
			isHovered = true;
		}
		return isHovered;
	}

	public void build() {
		id = GL11.glGenLists(1);
		int slotCount = Inventory.getSlotCount();
		int slotWidth = Inventory.getSlotWidth();

		GL11.glNewList(id, GL11.GL_COMPILE);

		GL11.glBegin(GL11.GL_QUADS);
		UIPanel.renderPanel((int) position.x, (int) position.y, slotWidth + 1, slotCount / slotWidth);

		int x = 0;
		int y = 0;
		for (int index = 0; index < slotCount; index++) {
			ItemSlot slot = Inventory.itemSlots.get(index);
			if (slot != null) {
				Item item = slot.item;
				Point pos = new Point((int) position.x + 5 + (x * 38), (int) position.y + 5 + (y * 38));
				if (item != null) {
					System.out.println("item(" + index + "): " + item.getTexture());
					Renderer.renderUITexture(item.getTexture(), pos.x, pos.y, 32, 32);
				}
				if (x >= slotWidth - 1) {
					y++;
					x = 0;
				} else {
					x++;
				}
			}
		}

		GL11.glEnd();

		GL11.glEndList();

		updateID = false;
	}

	public void update() {
	}

	public void render() {

		if (id == -1 || updateID) {
			build();
		} else {
			GL11.glCallList(id);
		}
	}

	public void destroy() {

	}
}
