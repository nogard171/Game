package game;

import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import core.ANode;
import core.APathFinder;
import core.Chunk;
import core.ChunkManager;
import core.Entity;
import core.GameDatabase;
import core.GroundItem;
import core.Input;
import core.Renderer;
import core.Resource;
import core.ResourceDatabase;
import core.TaskType;
import core.TextureType;
import core.Tile;
import core.View;
import core.Window;
import ui.UIInventory;
import ui.UIManager;
import core.Task;
import utils.FPS;
import utils.Ticker;

public class Base {

	boolean isRunning = true;

	public static Point playerIndex;
	public static Point test;
	public static View view;

	ChunkManager chunkMgr;

	TaskManager taskMgr;
	UIManager uiMgr;

	public void start() {
		this.setup();

		while (this.isRunning) {
			this.update();
			this.render();
		}
		this.destroy();

	}

	private void pollHover() {
		int cartX = (Input.getMousePoint().x) + view.x;
		int cartY = (Input.getMousePoint().y) + view.y;
		int isoX = (cartX / 2 + (cartY));
		int isoY = (cartY - cartX / 2);

		int indexX = (int) Math.floor((float) isoX / (float) 32);
		int indexY = (int) Math.floor((float) isoY / (float) 32);
		if (!UIInventory.isPanelHovered()) {
			test = new Point(indexX, indexY);
		} else {
			test = null;
		}
	}

	public void setup() {
		Window.start();
		Window.setup();

		Input.setup();

		view = new View(0, 0, Window.width, Window.height);

		FPS.setup();

		ResourceDatabase.load();
		GameDatabase.load();

		taskMgr = new TaskManager();
		taskMgr.setup();

		TextureType type = TextureType.GRASS;
		chunkMgr = new ChunkManager();
		chunkMgr.setup();

		uiMgr = new UIManager();
		uiMgr.setup();

	}

	public void update() {
		Window.update();
		pollHover();
		taskMgr.update();
		chunkMgr.update();
		uiMgr.update();

		if (Window.close()) {
			this.isRunning = false;
		}

		if (Window.wasResized) {
			view.w = Window.width;
			view.h = Window.height;
			Window.wasResized = false;
		}

		playerIndex = TaskManager.getCurrentTaskEnd();
		if (playerIndex == null) {
			playerIndex = ChunkManager.getIndexByType(TextureType.CHARACTER);
		}
		FPS.updateFPS();
		int forceX = 0;
		int forceY = 0;

		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			forceY = -1;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			forceY = 1;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			forceX = -1;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			forceX = 1;
		}
		// fix path finding, if player is next to resource do not run path finding.
		if (Input.isMousePressed(0) && !UIInventory.isPanelHovered()) {
			if (test.x > playerIndex.x - (ChunkManager.viewRange.x * 32)
					&& test.x < playerIndex.x + (ChunkManager.viewRange.x * 32)
					&& test.y > playerIndex.y - (ChunkManager.viewRange.y * 32)
					&& test.y < playerIndex.y + (ChunkManager.viewRange.y * 32)) {
				ANode resourceIndex = new ANode(test.x, test.y);
				boolean isRes = ChunkManager.isResource(test);
				boolean inRange = ChunkManager.resourceInRange(playerIndex, test);
				Resource resource = null;
				if (isRes) {
					resource = ChunkManager.getResource(test);
					test = ChunkManager.findIndexAroundIndex(test);
				}
				boolean isItem = ChunkManager.isItem(test);

				LinkedList<ANode> path = null;

				
				System.out.println("In Range: " + inRange);
				if (inRange) {
					path = new LinkedList<ANode>();
					path.add(new ANode(test));
				} else {
					path = APathFinder.find(new ANode(playerIndex), new ANode(test));
				}

				if (path != null) {
					if (path.size() > 0) {
						for (int i = 0; i < path.size() - 1; i++) {
							ANode node = path.get(i);
							if (node != null) {
								ChunkManager.setObjectAtIndex(node.toPoint(), TextureType.PATH_DURING);
							}
						}
						ANode node = path.get(path.size() - 1);
						ChunkManager.setObjectAtIndex(node.toPoint(), TextureType.PATH_FINISH);

						Task move = new Task(TaskType.WALK, path.getFirst(), path, 1000);

						if (isRes) {

							System.out.println("test");

							if (resource != null) {

								System.out.println("added follow up" + isRes);

								Task chop = new Task(TaskType.RESOURCE, resourceIndex,
										resource.getANode(resourceIndex));

								test = ChunkManager.findIndexAroundIndex(test);

								move.addFollowUp(chop);

							}
						} else if (isItem) {
							Tile item = null;
							if (isItem) {
								item = ChunkManager.getTile(test);
							}
							if (item != null) {

								Task chop = new Task(TaskType.ITEM, resourceIndex);
								test = ChunkManager.findIndexAroundIndex(test);
								move.addFollowUp(chop);

							}
						}
						TaskManager.addTask(move);

					}
				} else {
					// handle failed path
				}
			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_F1)) {
			ResourceDatabase.load();
		}
		// view.x = x;
		// view.y = y;
		view.move(forceX, forceY);
		// System.out.println("NEW Index: " + playerIndex);
	}

	public void render() {
		Window.render();
		Input.poll();

		Renderer.bindTexture(ResourceDatabase.texture);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);

		GL11.glPushMatrix();
		GL11.glTranslatef(-view.x, -view.y, 0);
		chunkMgr.render();
		/*
		 * if (test != null) { Tile tile = ChunkManager.getTile(test); if (tile != null)
		 * { if (tile instanceof Resource || tile instanceof GroundItem) { float posX =
		 * (((test.x - test.y) * 32) - 32); float posY = ((test.y + test.x) * 16);
		 * GL11.glBegin(GL11.GL_QUADS); GL11.glColor4f(0.5f, 0.5f, 0.5f, 0.5f);
		 * Renderer.renderTexture(tile.getType(), (int) posX, (int) posY); GL11.glEnd();
		 * } } }
		 */
		GL11.glPopMatrix();

		uiMgr.render();

		Renderer.renderQuad(new Rectangle(0, 0, 200, 64), new Color(0, 0, 0, 0.5f));
		Renderer.renderText(new Vector2f(0, 0), "FPS: " + FPS.getFPS(), 12, Color.white);

		TextureType type = ChunkManager.getTypeByIndexWithTiles(test);

		if (type != null) {
			Renderer.renderText(new Vector2f(0, 16), "Hover Type: " + type.toString(), 12, Color.white);
		}

		Renderer.renderText(new Vector2f(0, 32), "Chunks In View: " + chunkMgr.chunksInView.size(), 12, Color.white);

		Renderer.renderText(new Vector2f(0, 48), "Input Moved: " + ""+Input.moved, 12, Color.white);

		if (test != null) {

			Tile tile = ChunkManager.getTile(test);
			if (tile != null) {
				String text = tile.toHoverString();
				int fontType = Font.PLAIN;

				if (text.contains("!")) {
					fontType = Font.BOLD;
					text = text.replace("!", "");
				}
				int r = 255;
				int g = 255;
				int b = 255;
				if (text.contains("%")) {
					int c = text.replaceAll("(%[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}%)", "").length();
					Renderer.renderQuad(new Rectangle(Input.getMousePoint().x, Input.getMousePoint().y + 32, c * 8, 20),
							new Color(0, 0, 0, 0.5f));

					String[] data = text.split("%");
					int tempC = 0;
					for (int i = 0; i < data.length; i++) {
						if (data[i].contains(",")) {
							String[] rgb = data[i].split(",");
							r = Integer.parseInt(rgb[0]);
							g = Integer.parseInt(rgb[1]);
							b = Integer.parseInt(rgb[2]);

						} else {
							int textWidth = Renderer.renderTextWithWidth(
									new Vector2f(Input.getMousePoint().x + 5 + tempC, Input.getMousePoint().y + 1 + 32),
									data[i], 12, new Color(r, g, b), Font.BOLD);
							tempC += textWidth;
						}
					}
				} else {
					Renderer.renderQuad(
							new Rectangle(Input.getMousePoint().x, Input.getMousePoint().y + 32, text.length() * 8, 20),
							new Color(0, 0, 0, 0.5f));
					Renderer.renderText(new Vector2f(Input.getMousePoint().x + 5, Input.getMousePoint().y + 1 + 32),
							text, 12, new Color(r, g, b), fontType);
				}
			}
		}
	}

	public void destroy() {
		Window.destroy();
	}
}
