package ui;

import java.awt.Rectangle;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

import core.GameDatabase;
import core.Input;
import core.ItemType;
import core.Renderer;
import core.ResourceDatabase;
import core.TextureType;

public class UIManager {
	UIInventory uiInventory;

	public void setup() {
		uiInventory = new UIInventory();
		uiInventory.setup();
		Inventory.setup();
		/*
		 * Inventory.addItem(ItemType.ROCK); Inventory.addItem(ItemType.ROCK);
		 * Inventory.addItem(ItemType.ROCK); Inventory.addItem(ItemType.ROCK);
		 * 
		 * Inventory.putItem(7, ItemType.ROCK);
		 * 
		 * Inventory.printItems();
		 */
	}
	public void update() {
		uiInventory.update();
	}

	public void render() {

		Renderer.bindTexture(ResourceDatabase.uiTexture);
		uiInventory.render();

		GL11.glBegin(GL11.GL_QUADS);
		Renderer.renderUITexture(UITextureType.CURSOR, Input.getMousePoint().x, Input.getMousePoint().y, 32, 32);
		GL11.glEnd();
	}

	public void destroy() {
	}
}
