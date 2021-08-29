package ui;

import java.awt.Point;
import java.awt.Rectangle;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import core.GameDatabase;
import core.Input;
import core.Item;
import core.ItemData;
import core.ItemType;
import core.Renderer;
import core.ResourceDatabase;
import core.TextureType;
import game.TaskManager;
import utils.FPS;
import utils.Ticker;

public class UIInventory {
	private int id = -1;
	public static Vector2f position = new Vector2f(100, 100);
	public static boolean updateID = false;
	public ItemSlot dragSlot;
	private static Point slotMargin = new Point(37, 37);

	public Rectangle eatBound = null;
	public ItemSlot eatSlot;
	private Ticker eatTicker;
	public Rectangle dropBound = null;
	public Rectangle inspectBound = null;

	public void setup() {
		eatTicker = new Ticker();
	}

	public static boolean isPanelHovered() {
		boolean isHovered = false;
		int slotCount = Inventory.getSlotCount();
		int slotWidth = Inventory.getSlotWidth();
		Rectangle rec = new Rectangle((int) position.x, (int) position.y, (slotWidth + 3) * 32,
				((slotCount / slotWidth) + 2) * 32);
		if (rec.contains(Input.getMousePoint())) {
			isHovered = true;
		}
		return isHovered;
	}

	public void build() {
		slotMargin = new Point(45, 42);
		id = GL11.glGenLists(1);
		int slotCount = Inventory.getSlotCount();
		int slotWidth = Inventory.getSlotWidth();

		GL11.glNewList(id, GL11.GL_COMPILE_AND_EXECUTE);

		GL11.glBegin(GL11.GL_QUADS);
		UIPanel.renderPanel((int) position.x, (int) position.y, slotWidth + 3,
				(int) Math.ceil((float) slotCount / (float) slotWidth) + 2);

		int x = 0;
		int y = 0;
		for (int index = 0; index < slotCount; index++) {
			ItemSlot slot = Inventory.itemSlots.get(index);
			if (slot != null) {

				Item item = slot.item;
				Point pos = new Point((int) position.x + 5 + (x * slotMargin.x),
						(int) position.y + 5 + (y * slotMargin.y));
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

		Point pos = new Point((int) position.x + 5 + (5 * slotMargin.x) - 10,
				(int) position.y + 5 + (0 * slotMargin.y));
		Renderer.renderUITexture(UITextureType.ITEM_BACK, pos.x, pos.y, 32, 32);
		Renderer.renderUITexture(UITextureType.MOUTH_ICON, pos.x, pos.y, 32, 32);

		eatBound = new Rectangle(pos.x, pos.y, 32, 32);

		Renderer.renderUITexture(UITextureType.ITEM_BACK, pos.x, pos.y + (42 * 5), 32, 32);
		Renderer.renderUITexture(UITextureType.DROP_ICON, pos.x, pos.y + (42 * 5), 32, 32);

		dropBound = new Rectangle(pos.x, pos.y + (42 * 5), 32, 32);

		Renderer.renderUITexture(UITextureType.ITEM_BACK, pos.x, pos.y + (42 * 4), 32, 32);
		Renderer.renderUITexture(UITextureType.INSPECT_ICON, pos.x, pos.y + (42 * 4), 32, 32);

		inspectBound = new Rectangle(pos.x, pos.y + (42 * 4), 32, 32);

		GL11.glEnd();

		x = 0;
		y = 0;
		for (int index = 0; index < slotCount; index++) {
			ItemSlot slot = Inventory.itemSlots.get(index);
			if (slot != null) {

				Item item = slot.item;

				if (item != null) {
					if (slot.count > 1) {
						DecimalFormat df = new DecimalFormat("#.#");
						df.format(55.544545);
						String s = (slot.count > 1000000000000000l ? "?"
								: (slot.count > 1000000000000l ? df.format(slot.count / 1000000000000l) + "T"
										: (slot.count > 1000000000 ? df.format(slot.count / 1000000000) + "B"
												: (slot.count > 1000000 ? df.format(slot.count / 1000000) + "M"
														: (slot.count > 1000 ? df.format(slot.count / 1000) + "K"
																: slot.count + "")))));

						int l = Renderer.getTextWidth(s, 12);
						pos = new Point((int) position.x + 36 - (l) + (x * slotMargin.x),
								(int) position.y + 32 + (y * slotMargin.y));

						Renderer.renderText(new Vector2f(pos.x, pos.y), s, 12, Color.white);
					}
				}
				if (x >= slotWidth - 1) {
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
		previousSlotIndexHovered = slotIndexHovered;
		slotIndexHovered = -1;
		int offsetMouseX = (int) (Input.getMousePoint().x - position.x);
		int offsetMouseY = (int) (Input.getMousePoint().y - position.y);
		if (offsetMouseX >= 0 && offsetMouseY >= 0) {
			int itemX = offsetMouseX / slotMargin.x;
			int itemY = offsetMouseY / slotMargin.y;

			int slotWidth = Inventory.getSlotWidth();
			int index = itemX + (itemY * slotWidth);

			if (index < Inventory.itemSlots.size() && index >= 0) {
				ItemSlot slot = Inventory.itemSlots.get(index);
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

		if (Input.isMousePressed(0) && slotIndexHovered > -1) {
			if (slotIndexHovered < Inventory.getSlotCount()) {
				ItemSlot tempSlot = Inventory.itemSlots.remove(slotIndexHovered);
				System.out.println("Index: " + slotIndexHovered + "=>" + tempSlot.item);

				if (tempSlot.item != null && dragSlot == null) {
					dragSlot = tempSlot;
					System.out.println("test123: " + dragSlot.item);
					updateID = true;
					tempSlot = new ItemSlot();

				} else if (dragSlot != null && tempSlot.item == null) {
					tempSlot = dragSlot;
					System.out.println("Count: " + dragSlot.count);

					// Inventory.itemSlots.set(slotIndexHovered, tempSlot);
					updateID = true;
					dragSlot = null;
				}

				if (tempSlot.item != null && dragSlot != null) {

					System.out.println("Use");
					// TaskManager.addTask(null);
				}
				Inventory.itemSlots.add(slotIndexHovered, tempSlot);

			}
			if (dragSlot != null) {
				// System.out.println("test: " + dragSlot.item);
			}
		}
		if (slotIndexHovered != previousSlotIndexHovered) {
			showContext = false;
		}
		if (Input.isMousePressed(1) && slotPointHovered != null) {
			showContext = true;
		}

		if (Input.isMousePressed(0)) {
			if (dragSlot != null) {
				if (dragSlot.item != null) {
					if (eatBound.contains(Input.getMousePoint())) {
						ItemData dat = GameDatabase.getItemData(dragSlot.item.getType());
						if (dat != null) {
							if (dat.attr.contains("EDIBLE")) {
								if (eatSlot != null) {
									if (eatSlot.item == null) {
										System.out.println("Eat:" + dragSlot.item.getType());
										eatSlot = new ItemSlot();
										eatSlot.item = dragSlot.item;
										eatSlot.count = dragSlot.count;

										dragSlot = null;
									}
								}
							}
						}
					}
					if (inspectBound.contains(Input.getMousePoint())) {
						System.out.println("Inspect:" + dragSlot.item.getType());
					}
					if (dropBound.contains(Input.getMousePoint())) {
						System.out.println("Drop: " + dragSlot.item.getType());
					}
				}
			}
		}

	}

	public void render() {

		if (id == -1 || updateID) {
			build();
		} else {
			GL11.glCallList(id);
		}

		if (slotIndexHovered != -1) {
			Point tempPos = new Point((int) position.x + 6 + (slotPointHovered.x * slotMargin.x),
					(int) position.y + 6 + (slotPointHovered.y * slotMargin.y));
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
			Renderer.renderQuad(new Rectangle(tempPos.x, tempPos.y, 32, 32), new Color(255, 255, 255));
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		}

		if (dragSlot != null) {
			if (dragSlot.item != null) {
				Renderer.bindTexture(ResourceDatabase.uiTexture);
				GL11.glBegin(GL11.GL_QUADS);
				Renderer.renderUITexture(dragSlot.item.getTexture(), Input.getMousePoint().x, Input.getMousePoint().y,
						32, 32);
				GL11.glEnd();
			}
		}

		if (eatSlot != null) {
			if (eatSlot.item != null) {
				eatTicker.poll(500);
				Renderer.bindTexture(ResourceDatabase.uiTexture);
				GL11.glBegin(GL11.GL_QUADS);
				Renderer.renderUITexture(eatSlot.item.getTexture(), eatBound.x, eatBound.y, 32, 32, eatCount, 0);
				GL11.glEnd();
				if (eatTicker.ticked()) {
					eatCount--;
				}
				if (eatCount <= 0) {
					eatCount = 32;
					eatSlot.item = null;
				}
			}
		}

	}

	int eatCount = 32;
	boolean showContext = false;

	public void destroy() {

	}
}
