package ui;

import java.awt.Rectangle;
import java.nio.IntBuffer;

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

public class UIManager {
	UIInventory uiInventory;

	public void setup() {
		uiInventory = new UIInventory();
		uiInventory.setup();
		Inventory.setup();
		Inventory.addItem(ItemType.COINS,9);
		/*
		 *  Inventory.addItem(ItemType.ROCK);
		 * Inventory.addItem(ItemType.ROCK); Inventory.addItem(ItemType.ROCK);
		 * 
		 * 
		 * 
		 * Inventory.printItems();
		 */
	}
	public void update() {
		//uiInventory.update();
		if(Input.isKeyPressed(Keyboard.KEY_1)) {

			Inventory.addItem(ItemType.COINS,900);
		}
		if(Input.isKeyPressed(Keyboard.KEY_2)) {

			Inventory.addItem(ItemType.COINS,900000);
		}
		if(Input.isKeyPressed(Keyboard.KEY_3)) {

			Inventory.addItem(ItemType.COINS,900000000);
		}
		if(Input.isKeyDown(Keyboard.KEY_4)) {

			Inventory.addItem(ItemType.COINS,90000000000000l);
		}
	}

	public void render() {

		Renderer.bindTexture(ResourceDatabase.uiTexture);
		//uiInventory.render();

		Renderer.bindTexture(ResourceDatabase.uiTexture);
		GL11.glBegin(GL11.GL_QUADS);
	
		Renderer.renderUITexture(UITextureType.PANEL_TL, 0,Window.height-32, 32, 32);
		int w= 5;
		for(int c=0;c<w;c++)
		{
			Renderer.renderUITexture(UITextureType.PANEL_TC, 32+(c*32),Window.height-32, 32, 32);
		}
		Renderer.renderUITexture(UITextureType.PANEL_TR,32+((w)*32),Window.height-32, 32, 32);
		

		Renderer.renderUITexture(UITextureType.CURSOR, Input.getMousePoint().x, Input.getMousePoint().y, 32, 32);
		GL11.glEnd();
	}

	public void destroy() {
	}
}
