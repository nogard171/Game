package ui;

import java.awt.Point;
import java.awt.Rectangle;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import core.Input;
import core.Item;
import core.ItemType;
import core.Renderer;
import core.ResourceDatabase;
import core.TextureType;
import game.TaskManager;
import utils.FPS;

public class UIInventory {
	private int id = -1;
	public static Vector2f position = new Vector2f(100, 100);
	public static boolean updateID = false;
	public ItemSlot dragSlot;

	public void setup() {

	}

	public static boolean isPanelHovered() {
		boolean isHovered = false;
		int slotCount = Inventory.getSlotCount();
		int slotWidth = Inventory.getSlotWidth();
		Rectangle rec = new Rectangle((int) position.x, (int) position.y, (slotWidth + 1) * 32,
				((slotCount / slotWidth) + 1) * 32);
		if (rec.contains(Input.getMousePoint())) {
			isHovered = true;
		}
		return isHovered;
	}

	public void build() {
		id = GL11.glGenLists(1);
		int slotCount = Inventory.getSlotCount();
		int slotWidth = Inventory.getSlotWidth();

		GL11.glNewList(id, GL11.GL_COMPILE_AND_EXECUTE);

		GL11.glBegin(GL11.GL_QUADS);
		System.out.println(
				"test: " + slotCount + "/" + slotWidth + "=" + Math.ceil((float) slotCount / (float) slotWidth));
		UIPanel.renderPanel((int) position.x, (int) position.y, slotWidth + 1,
				(int) Math.ceil((float) slotCount / (float) slotWidth) + 1);

		int x = 0;
		int y = 0;
		for (int index = 0; index < slotCount; index++) {
			ItemSlot slot = Inventory.itemSlots.get(index);
			if (slot != null) {

				Item item = slot.item;
				Point pos = new Point((int) position.x + 5 + (x * 37), (int) position.y + 5 + (y * 36));
				Renderer.renderUITexture(UITextureType.ITEM_BACK, pos.x, pos.y, 32, 32);
				if (item != null) {

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

	int slotIndexHovered = -1;
	Point slotPointHovered = new Point(-1, -1);

	public void update() {
		slotIndexHovered = -1;
		int offsetMouseX = (int) (Input.getMousePoint().x - position.x);
		int offsetMouseY = (int) (Input.getMousePoint().y - position.y);
		if (offsetMouseX >= 0 && offsetMouseY >= 0) {
			int itemX = offsetMouseX / 37;
			int itemY = offsetMouseY / 36;

			int slotWidth = Inventory.getSlotWidth();
			int index = itemX + (itemY * slotWidth);

			if (index < Inventory.itemSlots.size() && index >= 0) {
				ItemSlot slot = Inventory.itemSlots.get(index);
				if (slot != null) {
					if (itemX < slotWidth) {
						slotIndexHovered = index;
						slotPointHovered = new Point(itemX, itemY);
						if (slot.item != null) {

							// System.out.println("Offset: " + index + "=" + slot + "->" +
							// slot.item.getType());
						}
					}
				}
			}
		}

		if (Input.isMousePressed(0) && slotIndexHovered > -1) {
			if (slotIndexHovered < Inventory.getSlotCount()) {
				ItemSlot tempSlot = Inventory.itemSlots.remove(slotIndexHovered);
				System.out.println("Index: " + slotIndexHovered + "=>" + tempSlot.item);
				if (tempSlot.item != null && dragSlot == null) {
					dragSlot = tempSlot;
					// tempSlot.count = 1;
					// tempSlot.item = null;
					System.out.println("test123: " + dragSlot.item);
					updateID = true;
					tempSlot = new ItemSlot();

				}else if (dragSlot != null && tempSlot.item == null) {
					tempSlot = dragSlot;
					// Inventory.itemSlots.set(slotIndexHovered, tempSlot);
					updateID = true;
					dragSlot = null;
				}

				if (tempSlot.item != null && dragSlot != null) {
					
					System.out.println("Use");
					//TaskManager.addTask(null);
				} 
				Inventory.itemSlots.add(slotIndexHovered, tempSlot);

			}
			if (dragSlot != null) {
				// System.out.println("test: " + dragSlot.item);
			}
		}
		/*
		 * int slotCount = Inventory.getSlotCount(); int slotWidth =
		 * Inventory.getSlotWidth(); int x = 0; int y = 0; for (int index = 0; index <
		 * slotCount; index++) { ItemSlot slot = Inventory.itemSlots.get(index); if
		 * (slot != null) { Point pos = new Point((int) position.x + 5 + (x * 38), (int)
		 * position.y + 5 + (y * 36)); Rectangle rec = new Rectangle(pos.x,pos.y,32,32);
		 * 
		 * if(rec.contains(Input.getMousePoint())) { if(slot.item!=null) {
		 * System.out.println("Item: " + slot.item.getTexture()); //break; } } if (x >=
		 * slotWidth - 1) { y++; x = 0; } else { x++; } } }
		 */

	}

	public void render() {

		if (id == -1 || updateID) {
			build();
		} else {
			GL11.glCallList(id);
		}

		if (slotIndexHovered != -1) {
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
			Renderer.renderQuad(new Rectangle((int) position.x + 6 + (slotPointHovered.x * 37),
					(int) position.y + 6 + (slotPointHovered.y * 36), 32, 32), new Color(255, 255, 255));
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		}

		int slotCount = Inventory.getSlotCount();
		int slotWidth = Inventory.getSlotWidth();
		int x = 0;
		int y = 0;
		for (int index = 0; index < slotCount; index++) {
			ItemSlot slot = Inventory.itemSlots.get(index);
			if (slot != null) {
				if (slot.count > 1) {
					Item item = slot.item;
					Point pos = new Point((int) position.x + 5 + (x * 37), (int) position.y + 5 + (y * 36));
					if (item != null) {
						Renderer.renderText(new Vector2f(pos.x, pos.y), slot.count + "", 12, Color.white);
					}
					if (x >= slotWidth - 1) {
						y++;
						x = 0;
					} else {
						x++;
					}
				}
			}
		}

		if (dragSlot != null) {
			// dragSlot
			if (dragSlot.item != null) {
				Point pos = new Point((int) position.x + 5 + (slotPointHovered.x * 37),
						(int) position.y + 5 + (slotPointHovered.y * 36));
				GL11.glBegin(GL11.GL_QUADS);
				Renderer.renderUITexture(dragSlot.item.getTexture(), Input.getMousePoint().x, Input.getMousePoint().y,
						32, 32);
				GL11.glEnd();
			}
		}
	}

	public void destroy() {

	}
}
