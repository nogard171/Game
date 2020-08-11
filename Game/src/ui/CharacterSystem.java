package ui;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

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
	public ArrayList<EquipmentItem> items = new ArrayList<EquipmentItem>();

	String hint = "";
	Point hintPosition = new Point(0, 0);

	@Override
	public void setup() {
		super.setup();
		baseBounds = new Rectangle(0, 0, 165, 330);
		baseBounds.y = (Window.height - 32) - baseBounds.height;
		EquipmentItem helmItem = new EquipmentItem();
		helmItem.name = "Helmet";
		helmItem.material = "HELM_ICON";
		helmItem.bounds = new Rectangle(baseBounds.x + 64, baseBounds.y + 5, 35, 34);
		items.add(helmItem);

		EquipmentItem capeItem = new EquipmentItem();
		capeItem.name = "Cape";
		capeItem.material = "CAPE_ICON";
		capeItem.bounds = new Rectangle(baseBounds.x + 110, baseBounds.y + 5, 35, 34);
		items.add(capeItem);

		EquipmentItem shoulderItem = new EquipmentItem();
		shoulderItem.name = "Shoulder";
		shoulderItem.material = "SHOULDER_ICON";
		shoulderItem.bounds = new Rectangle(baseBounds.x + 18, baseBounds.y + 50, 35, 34);
		items.add(shoulderItem);

		EquipmentItem chestItem = new EquipmentItem();
		chestItem.name = "Chest";
		chestItem.material = "CHEST_ICON";
		chestItem.bounds = new Rectangle(baseBounds.x + 64, baseBounds.y + 50, 35, 34);
		items.add(chestItem);

		EquipmentItem armsItem = new EquipmentItem();
		armsItem.name = "Arms";
		armsItem.material = "ARMS_ICON";
		armsItem.bounds = new Rectangle(baseBounds.x + 110, baseBounds.y + 50, 35, 34);
		items.add(armsItem);

		EquipmentItem weaponItem = new EquipmentItem();
		weaponItem.name = "Weapon";
		weaponItem.material = "WEAPON_ICON";
		weaponItem.bounds = new Rectangle(baseBounds.x + 18, baseBounds.y + 96, 35, 34);
		items.add(weaponItem);

		EquipmentItem legsItem = new EquipmentItem();
		legsItem.name = "Chaps";
		legsItem.material = "CHAPS_ICON";
		legsItem.bounds = new Rectangle(baseBounds.x + 64, baseBounds.y + 96, 35, 34);
		items.add(legsItem);

		EquipmentItem shieldItem = new EquipmentItem();
		shieldItem.name = "Shield";
		shieldItem.material = "SHIELD_ICON";
		shieldItem.bounds = new Rectangle(baseBounds.x + 110, baseBounds.y + 96, 35, 34);
		items.add(shieldItem);

		EquipmentItem bootItem = new EquipmentItem();
		bootItem.name = "Boots";
		bootItem.material = "BOOT_ICON";
		bootItem.bounds = new Rectangle(baseBounds.x + 64, baseBounds.y + 141, 35, 34);
		items.add(bootItem);

		EquipmentItem glovesItem = new EquipmentItem();
		glovesItem.name = "Gloves";
		glovesItem.material = "GLOVES_ICON";
		glovesItem.bounds = new Rectangle(baseBounds.x + 110, baseBounds.y + 141, 35, 34);
		items.add(glovesItem);
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
				for (EquipmentItem item : items) {
					if (item.bounds.contains(new Point(Window.getMouseX(), Window.getMouseY()))) {
						hint = item.name;
						hintPosition = new Point(item.bounds.x, item.bounds.y);
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

			for (EquipmentItem item : items) {
				Renderer.renderRectangle(item.bounds.x, item.bounds.y, item.bounds.width,
						item.bounds.height, backGround);
			}
			
			
			GL11.glBegin(GL11.GL_TRIANGLES);
			for (EquipmentItem item : items) {
				Renderer.renderModel(item.bounds.x + 1, item.bounds.y + 1, "SQUARE", item.material,
						new Color(1, 1, 1, 1f));

			}

			GL11.glEnd();

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
