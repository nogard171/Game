package game;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;

import core.View;
import core.Window;
import utils.FPS;
import utils.Input;
import utils.Renderer;

public class Base {

	boolean isRunning = true;
	public static Point mousePosition;

	public void start() {
		this.setup();

		while (this.isRunning) {
			this.update();
			this.render();
		}
		this.destroy();

	}

	public void setup() {

		Window.start();
		Window.setup();		
		Input.setup();
		FPS.setup();
		
		Data.setup();
		
		Database.build();

		viewFrame = new View(0, 0, Window.width, Window.height);
	}
	
	

	public void update() {
		Window.update();

	mousePosition = new Point(Mouse.getX() + viewFrame.x, (Window.height - Mouse.getY()) + viewFrame.y);

		if (Window.close()) {
			this.isRunning = false;
		}

		if (Window.wasResized) {
			viewFrame.w = Window.width;
			viewFrame.h = Window.height;
			Window.wasResized = false;
		}

		FPS.updateFPS();

		float speed = FPS.getDelta();
		int forceX = 0;
		int forceY = 0;
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			forceX = (int) -speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			forceX = (int) speed;

		}
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			forceY = (int) -speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			forceY = (int) speed;
		}
		viewFrame.move(forceX, forceY);
	}

	public static ArrayList<Object> hoveredObjects = new ArrayList<Object>();
	public static View viewFrame;

	public void render() {
		Window.render();
		Input.poll();

		Renderer.bindTexture();
		GL11.glPushMatrix();
		GL11.glTranslatef(-viewFrame.x,-viewFrame.y, 0);
		
		Renderer.renderTexture(new Rectangle(0,0,64,64), Color.white);
		
		GL11.glPopMatrix();


		Renderer.renderQuad(new Rectangle(0, 0, 200, 64), new Color(0, 0, 0, 0.5f));
		Renderer.renderText(0,0, "FPS: " + FPS.getFPS(), 12, Color.white);
	}

	public void destroy() {
		Window.destroy();
	}
}
