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

import core.Input;
import core.Renderer;
import core.ResourceDatabase;
import core.TextureType;
import core.View;
import core.Window;
import utils.FPS;

public class Base {

	boolean isRunning = true;

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

		view = new View(0, 0, Window.width, Window.height);

		ResourceDatabase.load();

		TextureType type = TextureType.GRASS;
		System.out.println("Type: " + type.toString());
	}

	public void update() {
		Window.update();

		if (Window.close()) {
			this.isRunning = false;
		}

		if (Window.wasResized) {
			view.w = Window.width;
			view.h = Window.height;
			Window.wasResized = false;
		}

		FPS.updateFPS();

	}

	public static View view;

	public void render() {
		Window.render();
		Input.poll();

		//GL11.glBindTexture(GL11.GL_TEXTURE_2D, ResourceDatabase.texture.getTextureID());
		GL11.glPushMatrix();
		GL11.glTranslatef(-view.x, -view.y, 0);

		GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		int[] heights = { 0, 0, 0, 0 };
		GL11.glColor3f(1, 0, 0);
		GL11.glBegin(GL11.GL_QUADS);

		Renderer.renderSprite("test", 0,0, heights);

		GL11.glEnd();

		Renderer.renderQuad(new Rectangle(0, 0, 200, 64), new Color(0, 0, 0, 0.5f));
		Renderer.renderText(new Vector2f(0, 0), "FPS: " + FPS.getFPS(), 12, Color.white);

	}

	public void destroy() {
		Window.destroy();
	}
}
