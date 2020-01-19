package game;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.SwingUtilities;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import classes.GLObject;
import classes.GLSize;
import classes.GLSpriteData;
import classes.GLType;
import classes.GLView;
import classes.GLActionHandler;
import classes.GLIndex;
import classes.GLMenuItem;
import core.GLChunk;
import core.GLChunkManager;
import core.GLDisplay;
import core.GLQueueManager;
import core.GLUIManager;
import utils.GLDebug;
import utils.GLFPS;
import utils.GLGenerator;
import utils.GLLoader;

public class Main extends GLDisplay {
	public static HashMap<String, GLSpriteData> sprites = new HashMap<String, GLSpriteData>();

	int currentLevel = 0;
	public static Texture texture;

	GLChunkManager manager;
	GLUIManager uiManager;
	GLQueueManager queueManager;

	public static GLView view;

	public void run() {
		this.createDisplay();
		GLFPS.setup();
		manager = new GLChunkManager();
		manager.setup();

		uiManager = new GLUIManager();
		uiManager.setup();
		
		queueManager = new GLQueueManager();
		queueManager.setup();

		view = new GLView();

		try {
			texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(Settings.getTextureFile()));
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());

		GLLoader.loadSprites();

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);

		

		while (!Display.isCloseRequested()) {
			GLFPS.updateFPS();
			this.update();

			this.render();

			this.sync();
		}
		this.destroy();
	}

	private void destroy() {
		
		this.destroyDisplay();
	}

	public void update() {
		view.setSize(new GLSize(this.WIDTH, this.HEIGHT));
		float speed = 0.5f * GLFPS.getDelta();
		manager.update();
		uiManager.update();
		
		queueManager.update();
		
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			view.move(new Vector2f(speed, 0));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			view.move(new Vector2f(-speed, 0));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			view.move(new Vector2f(0, speed));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			view.move(new Vector2f(0, -speed));
		}

	}

	@Override
	public void render() {
		super.render();

		texture.bind();
		GL11.glPushMatrix();
		GL11.glTranslatef(-view.getPosition().x, -view.getPosition().y, 0);
		manager.render();
		GL11.glPopMatrix();
		uiManager.render();
	}

	public static void main(String[] args) {
		final Main app = new Main();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				app.run();
			}
		});
	}
}