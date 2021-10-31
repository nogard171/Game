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
import core.ItemType;
import core.Renderer;
import core.Resource;
import core.ResourceDatabase;
import core.TaskType;
import core.TextureType;
import core.Tile;
import core.View;
import core.Window;
import ui.Skill;
import ui.SkillData;
import ui.SkillManager;
import ui.SkillName;
import ui.UIInventory;
import ui.UIManager;
import core.Task;
import utils.FPS;
import utils.Ticker;

public class Base {

	boolean isRunning = true;

	public static Point playerIndex;
	public static Point hoverIndex;
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
		if (!UIManager.isHovered()) {
			hoverIndex = new Point(indexX, indexY);
		} else {
			hoverIndex = null;
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
		if (Input.isMousePressed(0) && !UIManager.isHovered()) {
			if (hoverIndex.x > playerIndex.x - (ChunkManager.viewRange.x * 32)
					&& hoverIndex.x < playerIndex.x + (ChunkManager.viewRange.x * 32)
					&& hoverIndex.y > playerIndex.y - (ChunkManager.viewRange.y * 32)
					&& hoverIndex.y < playerIndex.y + (ChunkManager.viewRange.y * 32)) {
				boolean useHoe = false;

				if (UIInventory.dragSlot != null) {
					if (UIInventory.dragSlot.item != null) {
						if (UIInventory.dragSlot.item.getType().equals(ItemType.HOE)) {
							useHoe = true;
						}
					}
				}
				ANode hoeIndex = null;
				if (useHoe) {
					hoeIndex = new ANode(hoverIndex.x, hoverIndex.y);
					hoverIndex = ChunkManager.findIndexAroundIndex(playerIndex, hoverIndex);
				}
				ANode resourceIndex = new ANode(hoverIndex.x, hoverIndex.y);
				boolean isRes = ChunkManager.isResource(hoverIndex);
				boolean inRange = ChunkManager.resourceInRange(playerIndex, hoverIndex);
				Resource resource = null;
				if (isRes) {
					resource = ChunkManager.getResource(hoverIndex);
					hoverIndex = ChunkManager.findIndexAroundIndex(playerIndex, hoverIndex);
				}
				boolean isItem = ChunkManager.isItem(hoverIndex);

				LinkedList<ANode> path = null;

				if (inRange) {
					path = new LinkedList<ANode>();
					path.add(new ANode(hoverIndex));
				} else {
					path = APathFinder.find(new ANode(playerIndex), new ANode(hoverIndex));
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

						if (useHoe) {
							Resource temp = new Resource(TextureType.AIR);
							temp.setHealth(5);

							Task till = new Task(TaskType.TILL, hoeIndex, temp.getANode(hoeIndex));

							hoverIndex = ChunkManager.findIndexAroundIndex(playerIndex, hoverIndex);

							move.addFollowUp(till);
						} else if (isRes) {

							if (resource != null) {

								Task chop = new Task(TaskType.RESOURCE, resourceIndex,
										resource.getANode(resourceIndex));

								hoverIndex = ChunkManager.findIndexAroundIndex(playerIndex, hoverIndex);

								move.addFollowUp(chop);

							}
						} else if (isItem) {
							Tile item = null;
							if (isItem) {
								item = ChunkManager.getTile(hoverIndex);
							}
							if (item != null) {

								Task chop = new Task(TaskType.ITEM, resourceIndex);
								hoverIndex = ChunkManager.findIndexAroundIndex(playerIndex, hoverIndex);
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
		GL11.glPopMatrix();

		uiMgr.render();

		Renderer.renderQuad(new Rectangle(0, 0, 200, 64), new Color(0, 0, 0, 0.5f));
		Renderer.renderText(new Vector2f(0, 0), "FPS: " + FPS.getDelta() + "/" + FPS.getFPS(), 12, Color.white);
		Renderer.renderText(new Vector2f(0, 16), "Hover Index: " + hoverIndex, 12, Color.white);
		TextureType type = ChunkManager.getTypeByIndexWithTiles(hoverIndex);

		if (type != null) {
			Renderer.renderText(new Vector2f(0, 32), "Hover Type: " + type.toString(), 12, Color.white);
		}

		Renderer.renderText(new Vector2f(0, 48), "Chunks In View: " + chunkMgr.chunksInView.size(), 12, Color.white);

		Renderer.renderText(new Vector2f(0, 64), "Input Moved: " + "" + Input.moved, 12, Color.white);

	}

	public void destroy() {
		Window.destroy();
	}
}
