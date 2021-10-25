package ui;

import java.awt.Rectangle;
import java.nio.IntBuffer;
import java.util.LinkedList;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

import core.GameDatabase;
import core.Input;
import core.ItemType;
import core.Renderer;
import core.ResourceDatabase;
import core.TextureType;
import core.Window;
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
		test.skill = "Wood Cutting";
		test.level = 1;

		PlayerDatabase.skills.add(test);

		test = new Skill();
		test.skill = "discovery";
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
	}

	public void destroy() {
	}

	public static boolean isHovered() {
		return uiHovered;
	}
}
