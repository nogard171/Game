package game;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

import core.Chunk;
import utils.FPS;
import utils.Renderer;

public class Game {
	Chunk chunk;

	public void setup() {
		chunk = new Chunk();
		chunk.setup();
		chunk.build();
	}

	public void update() {

	}

	public void render() {

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, Renderer.texture.getTextureID());
		GL11.glColor3f(1, 1, 1);

		chunk.render();

		chunk.renderFringe();

		
	}

	public void destroy() {

	}
}
