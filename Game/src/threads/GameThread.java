package threads;

import java.awt.Point;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import classes.Chunk;
import classes.Index;
import classes.MouseIndex;
import classes.TextureType;
import classes.World;
import classes.Object;
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

	@Override
	public void setup() {
		super.setup();

		Window.create();
		Loader.loadMaterials();
		Loader.loadTextures();

		world = new World();
		world.setup();

		FPS.setup();

		pathFinder = new APathFinder();

	}

	@Override
	public void update() {
		super.update();
		Window.update();
		FPS.updateFPS();
		pollHover();
		world.update();
		View.update();

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

		if (Mouse.isButtonDown(0) && hover != null) {
			// start = new Point(hover.getX(), hover.getY());
			end = new Point(hover.getX(), hover.getY());
		}
		if (Mouse.isButtonDown(1) && hover != null) {
			//end = new Point(hover.getX(), hover.getY());
		}
		if (start != null && end != null) {
			List path = pathFinder.find(start, end);
			if (path != null) {
				for (int i = 0; i < path.size(); i++) {
					Point point = (Point) path.get(i);
					if (point != null) {
						int chunkX = point.x / 16;
						int chunkY = point.y / 16;
						Chunk chunk = WorldData.chunks.get(chunkX + "," + chunkY);
						if (chunk != null) {
							int objX = point.x % 16;
							int objY = point.y % 16;
							chunk.setObjectColor(objX, objY, new Color(1, 0.5f, 0.5f, 1));
							chunk.needsUpdating();
						}
					}
				}

			}
			start = end;
			end = null;
		}
	}

	Point start = new Point(0, 0);
	Point end;

	APathFinder pathFinder;

	MouseIndex hover;

	private void pollHover() {
		int cartX = Window.getMouseX() - View.x;
		int cartY = Window.getMouseY() - View.y;
		int isoX = cartX / 2 + (cartY);
		int isoY = cartY - cartX / 2;
		int indexX = (int) Math.floor((float) isoX / (float) 32);
		int indexY = (int) Math.floor((float) isoY / (float) 32);

		int chunkX = (int) Math.floor(indexX / 16);
		int chunkY = (int) Math.floor(indexY / 16);
		if (indexX < 0) {
			chunkX--;
		}
		if (indexY < 0) {
			chunkY--;
		}

		hover = new MouseIndex(indexX, indexY, chunkX, chunkY);
	}

	@Override
	public void render() {
		super.render();
		Window.render();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, MaterialData.texture.getTextureID());

		GL11.glPushMatrix();
		GL11.glTranslatef(View.x, View.y, 0);

		world.render();
		if (hover != null) {
			GL11.glColor4f(1f, 0, 0, 0.5f);
			GL11.glBegin(GL11.GL_QUADS);
			Renderer.renderGrid(hover.getX(), hover.getY());
			GL11.glEnd();
		}
		GL11.glPopMatrix();
		Renderer.renderRectangle(0, 0, 100, 30, new Color(0, 0, 0, 0.5f));
		if (hover != null) {
			Renderer.renderText(new Vector2f(0, 0), "Hover:" + hover.getX() + "," + hover.getY(), 12, Color.white);
		}
		if (WorldGenerator.centerIndex != null) {
			Renderer.renderText(new Vector2f(0, 12),
					"Center Index:" + WorldGenerator.chunkIndex.getX() + "," + WorldGenerator.chunkIndex.getY(), 12,
					Color.white);
		}
		Renderer.renderText(new Vector2f(0, 24), "Start/End:" + start + "/" + end, 12, Color.white);
		Renderer.renderText(new Vector2f(0, 36), "Chunk Render Count:" + WorldData.chunks.size(), 12, Color.white);

		Window.finalizeRender();
	}

	@Override
	public void clean() {

		super.clean();
	}
}
