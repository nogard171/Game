package game;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;

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

import core.GLChunk;
import core.GLDebug;
import core.GLDisplay;
import core.GLFPS;
import core.GLGenerator;
import core.GLLoader;
import core.GLSize;
import core.GLSpriteData;
import core.GLType;

public class Main extends GLDisplay {
	GLFPS fps = new GLFPS();

	public static HashMap<String, GLSpriteData> sprites = new HashMap<String, GLSpriteData>();

	HashMap<String, GLChunk> chunks = new HashMap<String, GLChunk>();
	int currentLevel = 0;
	public static Texture texture;

	int mapWidth = 3;
	int mapDepth = 3;
	
	int mapMaxHeight = 16;

	public void run() {
		this.createDisplay();

		try {
			texture = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("resources/textures/tileset.png"));
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());

		GLGenerator gen = new GLGenerator();
		gen.setupMultiChunk(mapWidth, mapDepth);
		gen.init();
		
		for (int x = 0; x < mapWidth; x++) {
			for (int z = 0; z < mapDepth; z++) {
				GLChunk chunk = new GLChunk(x, 0, z);
				int[][] test = gen.getMultiChunkMap(x,z);

				
				chunk.setData(test);
				chunk.setupChunk(false);
				chunks.put(x + ",0," + z, chunk);
			}
		}

		GLLoader.loadSprites("resources/data/sprites.xml");

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);

		while (!Display.isCloseRequested()) {
			fps.updateFPS();
			this.update();

			this.render();

			this.postRender();
		}
		this.destroyDisplay();
	}

	Vector2f camera = new Vector2f(0, 0);

	public void update() {
		float speed = 0.5f * fps.getDelta();

		int mouseWheel = Mouse.getDWheel();
		if (mouseWheel < 0 && this.currentLevel < mapMaxHeight-1) {
			this.currentLevel++;
		}

		if (mouseWheel > 0 && this.currentLevel > 0) {
			this.currentLevel--;

		}

		for (Object obj : chunks.values()) {
			GLChunk chunk = (GLChunk) obj;
			if (chunk != null) {
				//if (!chunk.isEmpty()) {
					if (chunk.getLevel() != this.currentLevel) {
						chunk.changeLevel(this.currentLevel);
					}
				//}
				chunk.update(camera, chunks);

			}
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			camera.x += speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			camera.x -= speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			camera.y += speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			camera.y -= speed;
		}

	}

	@Override
	public void render() {
		super.render();
		texture.bind();
		GL11.glPushMatrix();
		GL11.glTranslatef(camera.x, camera.y, 0);
		/*
		 * for (GLChunk chunk : chunks) { chunk.render(); }
		 */

		for (int x = 0; x < mapWidth; x++) {
			for (int z = 0; z < mapDepth; z++) {
				GLChunk chunk = chunks.get(x + ",0," + z);

				if (chunk != null) {
					if (!chunk.isEmpty()) {
						chunk.render();
					}

				}
			}
		}

		GL11.glPopMatrix();

		GLDebug.RenderBackground(0, 0, 100, 50);
		GLDebug.RenderString(0, 0, "FPS: " + this.fps.fps, 12, Color.white);
	}

	public static void main(String[] args) {
		new Main().run();
	}
}