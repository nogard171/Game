package threads;

import java.awt.Point;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import classes.Chunk;
import classes.Event;
import classes.EventManager;
import classes.Index;
import classes.MouseIndex;
import classes.TextureType;
import classes.UserInterface;
import classes.World;
import classes.Object;
import classes.ObjectMenu;
import data.MaterialData;
import data.ModelData;
import data.WorldData;
import utils.APathFinder;
import utils.FPS;
import utils.Loader;
import utils.Renderer;
import utils.Ticker;
import utils.View;
import utils.Window;
import utils.WorldGenerator;

public class GameThread extends BaseThread {
	World world;
	UserInterface ui;

	@Override
	public void setup() {
		super.setup();

		Window.create();
		Loader.loadMaterials();
		Loader.loadTextures();

		world = new World();
		world.setup();

		ui = new UserInterface();
		ui.setup();

		FPS.setup();
	}

	@Override
	public void update() {
		super.update();
		Window.update();
		FPS.updateFPS();
		world.update();
		View.update();
		ui.update();

		float speed = 1 * FPS.getDelta();

		if (Window.isKeyDown(Keyboard.KEY_A)) {
			View.x += speed;
		}

		if (Window.isKeyDown(Keyboard.KEY_D)) {
			View.x -= speed;
		}
		if (Window.isKeyDown(Keyboard.KEY_W)) {
			View.y += speed;
		}

		if (Window.isKeyDown(Keyboard.KEY_S)) {
			View.y -= speed;
		}

	}

	@Override
	public void render() {
		super.render();
		Window.render();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, MaterialData.texture.getTextureID());

		GL11.glPushMatrix();
		GL11.glTranslatef(View.x, View.y, 0);

		world.render();
		ui.renderOnMap();
		GL11.glPopMatrix();

		ui.render();

		Window.finalizeRender();
	}

	@Override
	public void clean() {

		ui.clean();
		super.clean();
	}

}
