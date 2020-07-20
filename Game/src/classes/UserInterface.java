package classes;

import java.awt.Point;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import data.WorldData;
import utils.Renderer;
import utils.View;
import utils.Window;
import utils.WorldGenerator;

public class UserInterface {
	public ArrayList<MenuItem> menu = new ArrayList<MenuItem>();
	int mouseDownCount = 0;

	ObjectMenu objectMenu;

	EventManager eventManager;

	public static MouseIndex hover;

	public void setup() {

		eventManager = new EventManager();
		eventManager.setup();
		objectMenu = new ObjectMenu();
		objectMenu.setup();
	}

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

	public void update() {

		pollHover();
		objectMenu.update();
		eventManager.update();

		if (Mouse.isButtonDown(0) && hover != null && !objectMenu.showObjectMenu && mouseDownCount == 0
				&& eventManager.start.x != hover.getX() && eventManager.start.y != hover.getY()) {
			Event move = new Event();
			move.eventName = "MOVE";
			move.end = new Point(hover.getX(), hover.getY());
			EventManager.addEvent(move);
			mouseDownCount++;
		}

		if (!Mouse.isButtonDown(0) && mouseDownCount > 0) {
			mouseDownCount = 0;
		}
	}

	public void renderOnMap() {
		if (hover != null) {
			GL11.glColor4f(1f, 0, 0, 0.5f);
			GL11.glBegin(GL11.GL_QUADS);
			Renderer.renderGrid(hover.getX(), hover.getY());
			GL11.glEnd();
		}
		
		objectMenu.render();
	}

	public void render() {

		
		Renderer.renderRectangle(0, 0, 100, 30, new Color(0, 0, 0, 0.5f));
		if (hover != null) {
			Renderer.renderText(new Vector2f(0, 0), "Hover:" + hover.getX() + "," + hover.getY(), 12, Color.white);
			int hoverX = hover.getX();
			int hoverY = hover.getY();
			int chunkX = hoverX / 16;
			int chunkY = hoverY / 16;

			if (hoverX < 0) {
				chunkX -= 1;

			}
			if (hoverY < 0) {
				chunkY -= 1;
			}

			Renderer.renderText(new Vector2f(0, 12), "chunk:" + chunkX + "," + chunkY, 12, Color.white);
			Chunk chunk = WorldData.chunks.get(chunkX + "," + chunkY);
			if (chunk != null) {
				int objX = hover.getX() % 16;
				int objY = hover.getY() % 16;

				if (hoverX < 0) {
					objX *= -1;

				}
				if (hoverY < 0) {
					objY *= -1;
				}

				Object ground = chunk.groundObjects[objX][objY];
				Object mask = chunk.maskObjects[objX][objY];
				if (ground != null) {
					String maskString = "/";
					if (mask != null) {
						maskString += mask.getMaterial();
					}

					Renderer.renderText(new Vector2f(0, 24), "Object:" + ground.getMaterial() + maskString, 12,
							Color.white);
				}
			}

		}
		if (WorldGenerator.centerIndex != null) {
			Renderer.renderText(new Vector2f(0, 36),
					"Center Index:" + WorldGenerator.chunkIndex.getX() + "," + WorldGenerator.chunkIndex.getY(), 12,
					Color.white);
		}
		int size = 0;
		if (eventManager.events != null) {
			size = eventManager.events.size();
		}
		Renderer.renderText(new Vector2f(0, 48), "Event Count:" + size, 12, Color.white);
		Renderer.renderText(new Vector2f(0, 60), "Chunk Render Count:" + WorldData.chunks.size(), 12, Color.white);

	}

	public static MouseIndex getHover() {
		return hover;
	}

	public void clean() {

	}
}
