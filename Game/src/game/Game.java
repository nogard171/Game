package game;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

import core.WorldManager;
import utils.Renderer;

public class Game {
	WorldManager worldMgr;

	public void setup() {
		worldMgr = new WorldManager();
		worldMgr.setup();
	}

	public void update() {
		worldMgr.update();
	}

	public void render() {

		Renderer.bindTexture(0);
		GL11.glColor3f(1, 1, 1);

		worldMgr.render();

	}

	public void destroy() {
		worldMgr.destroy();
	}
}
