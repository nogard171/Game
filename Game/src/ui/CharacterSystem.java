package ui;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import classes.EquipmentItem;
import classes.Index;
import classes.InventoryItem;
import classes.Size;
import data.WorldData;
import utils.Renderer;
import utils.Window;

public class CharacterSystem extends BaseSystem {
	public static HashMap<String, EquipmentItem> items = new HashMap<String, EquipmentItem>();

	String hint = "";
	Point hintPosition = new Point(0, 0);

	public static InventoryItem equipItem(InventoryItem item) {
		InventoryItem returnItem = item;
		if (item != null) {
			MenuItem menuItem;
			if (item.name.toUpperCase().contains("SWORD")) {
				EquipmentItem equip = items.get("WEAPON");
				if (equip != null) {
					if (equip.item == null) {
						equip.item = item;
						returnItem = null;
					}
				}
			}
		}
		return returnItem;
	}

	@Override
	public void setup() {
		super.setup();
		baseBounds = new Rectangle(0, 0, 165, 330);
		baseBounds.y = (Window.height - 32) - baseBounds.height;
		EquipmentItem helmItem = new EquipmentItem();
		helmItem.name = "Helmet";
		helmItem.material = "HELM_ICON";
		helmItem.bounds = new Rectangle(baseBounds.x + 64, baseBounds.y + 5, 35, 34);
		items.put(helmItem.name.toUpperCase(), helmItem);

		EquipmentItem capeItem = new EquipmentItem();
		capeItem.name = "Cape";
		capeItem.material = "CAPE_ICON";
		capeItem.bounds = new Rectangle(baseBounds.x + 110, baseBounds.y + 5, 35, 34);
		items.put(capeItem.name.toUpperCase(), capeItem);

		EquipmentItem shoulderItem = new EquipmentItem();
		shoulderItem.name = "Shoulder";
		shoulderItem.material = "SHOULDER_ICON";
		shoulderItem.bounds = new Rectangle(baseBounds.x + 18, baseBounds.y + 50, 35, 34);
		items.put(shoulderItem.name.toUpperCase(), shoulderItem);

		EquipmentItem chestItem = new EquipmentItem();
		chestItem.name = "Chest";
		chestItem.material = "CHEST_ICON";
		chestItem.bounds = new Rectangle(baseBounds.x + 64, baseBounds.y + 50, 35, 34);
		items.put(chestItem.name.toUpperCase(), chestItem);

		EquipmentItem armsItem = new EquipmentItem();
		armsItem.name = "Arms";
		armsItem.material = "ARMS_ICON";
		armsItem.bounds = new Rectangle(baseBounds.x + 110, baseBounds.y + 50, 35, 34);
		items.put(armsItem.name.toUpperCase(), armsItem);

		EquipmentItem weaponItem = new EquipmentItem();
		weaponItem.name = "Weapon";
		weaponItem.material = "WEAPON_ICON";
		weaponItem.bounds = new Rectangle(baseBounds.x + 18, baseBounds.y + 96, 35, 34);
		items.put(weaponItem.name.toUpperCase(), weaponItem);

		EquipmentItem legsItem = new EquipmentItem();
		legsItem.name = "Chaps";
		legsItem.material = "CHAPS_ICON";
		legsItem.bounds = new Rectangle(baseBounds.x + 64, baseBounds.y + 96, 35, 34);
		items.put(legsItem.name.toUpperCase(), legsItem);

		EquipmentItem shieldItem = new EquipmentItem();
		shieldItem.name = "Shield";
		shieldItem.material = "SHIELD_ICON";
		shieldItem.bounds = new Rectangle(baseBounds.x + 110, baseBounds.y + 96, 35, 34);
		items.put(shieldItem.name.toUpperCase(), shieldItem);

		EquipmentItem bootItem = new EquipmentItem();
		bootItem.name = "Boots";
		bootItem.material = "BOOT_ICON";
		bootItem.bounds = new Rectangle(baseBounds.x + 64, baseBounds.y + 141, 35, 34);
		items.put(bootItem.name.toUpperCase(), bootItem);

		EquipmentItem glovesItem = new EquipmentItem();
		glovesItem.name = "Gloves";
		glovesItem.material = "GLOVES_ICON";
		glovesItem.bounds = new Rectangle(baseBounds.x + 110, baseBounds.y + 141, 35, 34);
		items.put(glovesItem.name.toUpperCase(), glovesItem);
	}

	@Override
	public void update() {
		super.update();
		if (showSystem) {
			if (baseBounds.contains(new Point(Window.getMouseX(), Window.getMouseY()))) {
				baseHovered = true;
			} else {
				baseHovered = false;
			}
			if (baseHovered) {
				UserInterface.inventoryHovered = true;

				hint = "";
				for (EquipmentItem item : items.values()) {
					if (item.bounds.contains(new Point(Window.getMouseX(), Window.getMouseY()))) {

						hint = item.name + " Slot";
						hintPosition = new Point(item.bounds.x, item.bounds.y);

						if (item.item != null) {
							hint += "(" + item.item.name + ")";
						}

					}
				}

			} else {
				hint = "";
				UserInterface.inventoryHovered = false;
			}
		}
	}

	@Override
	public void render() {
		super.render();
		if (showSystem) {
			Renderer.renderRectangle(baseBounds.x, baseBounds.y, baseBounds.width, baseBounds.height,
					new Color(0, 0, 0, 0.5f));

			Renderer.renderRectangle(baseBounds.x + 1, baseBounds.y + 1, baseBounds.width - 2, baseBounds.height - 2,
					new Color(1, 1, 1, 0.5f));

			Color backGround = new Color(0, 0, 0, 0.65f);

			for (EquipmentItem item : items.values()) {
				Renderer.renderRectangle(item.bounds.x, item.bounds.y, item.bounds.width, item.bounds.height,
						backGround);
			}

			GL11.glBegin(GL11.GL_TRIANGLES);
			for (EquipmentItem item : items.values()) {
				Renderer.renderModel(item.bounds.x + 1, item.bounds.y + 1, "SQUARE", item.material,
						new Color(1, 1, 1, 0.5f));
				if (item.item != null) {
					Renderer.renderModel(item.bounds.x + 1, item.bounds.y + 1, "SQUARE", item.item.getMaterial(),
							new Color(1, 1, 1, 1f));
				}
			}

			GL11.glEnd();
			
			Renderer.renderText(new Vector2f(baseBounds.x+16, baseBounds.y+190), "DMG:10000", 12, Color.white);

			Renderer.renderText(new Vector2f(baseBounds.x+85, baseBounds.y+190), "DEF:10000", 12, Color.white);

			Renderer.renderText(new Vector2f(baseBounds.x+16, baseBounds.y+212), "AGI:10000", 12, Color.white);

			Renderer.renderText(new Vector2f(baseBounds.x+85, baseBounds.y+212), "INT:10000", 12, Color.white);

			Renderer.renderText(new Vector2f(baseBounds.x+16, baseBounds.y+234), "WEG:10000", 12, Color.white);

			Renderer.renderText(new Vector2f(baseBounds.x+85, baseBounds.y+234), "SPP:10000", 12, Color.white);

			if (hint != "") {

				Renderer.renderRectangle(hintPosition.x, hintPosition.y + 2, hint.length() * 7, 14,
						new Color(0, 0, 0, 0.5f));
				Renderer.renderText(new Vector2f(hintPosition.x, hintPosition.y), hint, 12, Color.white);
			}
		}
	}

	@Override
	public void clean() {
		super.clean();

	}
}
