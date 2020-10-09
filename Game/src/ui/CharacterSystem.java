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
import classes.ItemData;
import classes.Size;
import data.WorldData;
import utils.Renderer;
import utils.Window;
import data.CharacterData;;

public class CharacterSystem extends BaseSystem {
	public static HashMap<String, EquipmentItem> items = new HashMap<String, EquipmentItem>();

	String hint = "";
	Point hintPosition = new Point(0, 0);

	EquipMenu equipMenu;
	public static String equipName = "";

	public static InventoryItem equipItem(InventoryItem item) {
		InventoryItem returnItem = item;
		if (item != null) {
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
		equipMenu = new EquipMenu();
		equipMenu.setup();

		baseBounds = new Rectangle(0, 0, 165, 330);
		baseBounds.y = (Window.height - 32) - baseBounds.height;
		EquipmentItem helmItem = new EquipmentItem();
		helmItem.name = "Helmet";
		helmItem.material = "HELM_ICON";
		helmItem.bounds = new Rectangle(64, 5, 35, 34);
		items.put(helmItem.name.toUpperCase(), helmItem);

		EquipmentItem capeItem = new EquipmentItem();
		capeItem.name = "Cape";
		capeItem.material = "CAPE_ICON";
		capeItem.bounds = new Rectangle(110, 5, 35, 34);
		items.put(capeItem.name.toUpperCase(), capeItem);

		EquipmentItem shoulderItem = new EquipmentItem();
		shoulderItem.name = "Shoulder";
		shoulderItem.material = "SHOULDER_ICON";
		shoulderItem.bounds = new Rectangle(18, 50, 35, 34);
		items.put(shoulderItem.name.toUpperCase(), shoulderItem);

		EquipmentItem chestItem = new EquipmentItem();
		chestItem.name = "Chest";
		chestItem.material = "CHEST_ICON";
		chestItem.bounds = new Rectangle(64, 50, 35, 34);
		items.put(chestItem.name.toUpperCase(), chestItem);

		EquipmentItem armsItem = new EquipmentItem();
		armsItem.name = "Arms";
		armsItem.material = "ARMS_ICON";
		armsItem.bounds = new Rectangle(110, 50, 35, 34);
		items.put(armsItem.name.toUpperCase(), armsItem);

		EquipmentItem weaponItem = new EquipmentItem();
		weaponItem.name = "Weapon";
		weaponItem.material = "WEAPON_ICON";
		weaponItem.bounds = new Rectangle(18, 96, 35, 34);
		items.put(weaponItem.name.toUpperCase(), weaponItem);

		EquipmentItem legsItem = new EquipmentItem();
		legsItem.name = "Chaps";
		legsItem.material = "CHAPS_ICON";
		legsItem.bounds = new Rectangle(64, 96, 35, 34);
		items.put(legsItem.name.toUpperCase(), legsItem);

		EquipmentItem shieldItem = new EquipmentItem();
		shieldItem.name = "Shield";
		shieldItem.material = "SHIELD_ICON";
		shieldItem.bounds = new Rectangle(110, 96, 35, 34);
		items.put(shieldItem.name.toUpperCase(), shieldItem);

		EquipmentItem bootItem = new EquipmentItem();
		bootItem.name = "Boots";
		bootItem.material = "BOOT_ICON";
		bootItem.bounds = new Rectangle(64, 141, 35, 34);
		items.put(bootItem.name.toUpperCase(), bootItem);

		EquipmentItem glovesItem = new EquipmentItem();
		glovesItem.name = "Gloves";
		glovesItem.material = "GLOVES_ICON";
		glovesItem.bounds = new Rectangle(110, 141, 35, 34);
		items.put(glovesItem.name.toUpperCase(), glovesItem);
	}

	@Override
	public void update() {
		super.update();

		if (showSystem) {
			equipMenu.update();
			if (baseBounds.contains(new Point(Window.getMouseX(), Window.getMouseY()))) {
				baseHovered = true;
			} else {
				baseHovered = false;
			}
			if (baseHovered) {
				UserInterface.characterHovered = true;
				equipName = "";
				for (EquipmentItem item : items.values()) {
					Rectangle bounds = new Rectangle(item.bounds.x + baseBounds.x, item.bounds.y + baseBounds.y,
							item.bounds.width, item.bounds.height);

					if (bounds.contains(new Point(Window.getMouseX(), Window.getMouseY()))) {
						equipName = item.name;
						// System.out.println("test");
						hint = item.name + " Slot";
						hintPosition = new Point(item.bounds.x + baseBounds.x, item.bounds.y + baseBounds.y);

						if (item.item != null) {
							hint += "(" + item.item.name + ")";
						}

					}
				}

			} else {
				hint = "";
				UserInterface.characterHovered = false;
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
				Renderer.renderRectangle(baseBounds.x + item.bounds.x, baseBounds.y + item.bounds.y, item.bounds.width,
						item.bounds.height, backGround);
			}

			GL11.glBegin(GL11.GL_TRIANGLES);
			for (EquipmentItem item : items.values()) {
				Renderer.renderModel(baseBounds.x + item.bounds.x + 1, baseBounds.y + item.bounds.y + 1, "SQUARE",
						item.material, new Color(1, 1, 1, 0.5f));
				if (item.item != null) {
					Renderer.renderModel(baseBounds.x + item.bounds.x + 1, baseBounds.y + item.bounds.y + 1, "SQUARE",
							item.item.getMaterial(), new Color(1, 1, 1, 1f));
				}
			}

			GL11.glEnd();

			Renderer.renderText(new Vector2f(baseBounds.x + 20, baseBounds.y + 190), "Damage:", 12, Color.white);
			int dmg = CharacterData.damage;

			EquipmentItem weapon = items.get("WEAPON");
			if (weapon != null) {
				if (weapon.item != null) {
					ItemData itemData = WorldData.itemData.get(weapon.item.getMaterial());
					if (itemData != null) {
						dmg += itemData.attributeValue;
					}
				}
			}

			int damageX = String.valueOf(dmg).length() * 7;
			Renderer.renderText(new Vector2f((baseBounds.x + 145) - damageX, baseBounds.y + 190), dmg + "", 12,
					Color.white);

			Renderer.renderText(new Vector2f(baseBounds.x + 20, baseBounds.y + 210), "Defense:", 12, Color.white);
			int def = CharacterData.defense;
			int defenseX = String.valueOf(def).length() * 7;
			Renderer.renderText(new Vector2f((baseBounds.x + 145) - defenseX, baseBounds.y + 210), def + "", 12,
					Color.white);

			Renderer.renderText(new Vector2f(baseBounds.x + 20, baseBounds.y + 230), "Agility:", 12, Color.white);
			int agi = CharacterData.agility;
			int agilityX = String.valueOf(agi).length() * 7;
			Renderer.renderText(new Vector2f((baseBounds.x + 145) - agilityX, baseBounds.y + 230), agi + "", 12,
					Color.white);

			Renderer.renderText(new Vector2f(baseBounds.x + 20, baseBounds.y + 250), "Intellect:", 12, Color.white);
			int inte = CharacterData.agility;
			int intellectX = String.valueOf(inte).length() * 7;
			Renderer.renderText(new Vector2f((baseBounds.x + 145) - intellectX, baseBounds.y + 250), inte + "", 12,
					Color.white);

			Renderer.renderText(new Vector2f(baseBounds.x + 20, baseBounds.y + 270), "Regen:", 12, Color.white);
			int reg = CharacterData.agility;
			int regenX = String.valueOf(reg).length() * 7;
			Renderer.renderText(new Vector2f((baseBounds.x + 145) - regenX, baseBounds.y + 270), reg + "", 12,
					Color.white);

			Renderer.renderText(new Vector2f(baseBounds.x + 20, baseBounds.y + 290), "Vitality:", 12, Color.white);
			int vit = CharacterData.agility;
			int vitalityX = String.valueOf(vit).length() * 7;
			Renderer.renderText(new Vector2f((baseBounds.x + 145) - vitalityX, baseBounds.y + 290), vit + "", 12,
					Color.white);

			int gol = CharacterData.gold;
			int sil = CharacterData.silver;
			int cop = CharacterData.copper;
			GL11.glBegin(GL11.GL_TRIANGLES);
			if (gol > 0) {
				Renderer.renderModel(baseBounds.x + 65, baseBounds.y + 309, "COIN", "GOLD_CURRENCY",
						new Color(1, 1, 1, 1f));
			}
			if (sil > 0) {
				Renderer.renderModel(baseBounds.x + 105, baseBounds.y + 309, "COIN", "SILVER_CURRENCY",
						new Color(1, 1, 1, 1f));
			}
			Renderer.renderModel(baseBounds.x + 145, baseBounds.y + 309, "COIN", "COPPER_CURRENCY",
					new Color(1, 1, 1, 1f));

			GL11.glEnd();

			if (gol > 0) {
				int golX = String.valueOf(gol).length() * 7;
				Renderer.renderText(new Vector2f((baseBounds.x + 64) - golX, baseBounds.y + 308), gol + "", 12,
						Color.white);
			}
			if (sil > 0) {
				int silX = String.valueOf(sil).length() * 7;
				Renderer.renderText(new Vector2f((baseBounds.x + 104) - silX, baseBounds.y + 308), sil + "", 12,
						Color.white);
			}
			int copX = String.valueOf(cop).length() * 7;
			Renderer.renderText(new Vector2f((baseBounds.x + 144) - copX, baseBounds.y + 308), cop + "", 12,
					Color.white);

			if (hint != "") {

				Renderer.renderRectangle(hintPosition.x, hintPosition.y + 2, hint.length() * 7, 14,
						new Color(0, 0, 0, 0.5f));
				Renderer.renderText(new Vector2f(hintPosition.x, hintPosition.y), hint, 12, Color.white);
			}

			equipMenu.render();
		}
	}

	@Override
	public void clean() {
		super.clean();

	}
}
